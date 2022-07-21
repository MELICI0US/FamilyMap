package com.example.familymap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;
import Requests.LoginRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;

import static org.junit.Assert.*;

public class DataTest {
    static DataCache cache = new DataCache();

    @BeforeClass
    public static void login() {
        try {
            ServerProxy serverProxy = new ServerProxy();

            LoginRequest request = new LoginRequest("username", "password");

            cache.setUserId("1ee2b60");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, LoginRequest.class);

            URL url = new URL("http://127.0.0.1:8080/user/login");

            String urlContent = serverProxy.postUrl(url, json);
            gson = new Gson();
            LoginResponse response = gson.fromJson(urlContent, LoginResponse.class);

            String authToken = response.getAuthtoken();

            URL urlPerson = new URL("http://127.0.0.1:8080/person");

            urlContent = serverProxy.getData(urlPerson, authToken);
            PersonResponse parsonResponse = gson.fromJson(urlContent, PersonResponse.class);
            System.out.println("Getting persons...");
            ArrayList<Person> personList = parsonResponse.getData();
            Map<String, Person> persons = new HashMap<>();

            for (Person person : personList) {
                persons.put(person.getPersonID(), person);
            }

            cache.setPersons(persons);

            url = new URL("http://127.0.0.1:8080/event");

            urlContent = serverProxy.getData(url, authToken);
            EventResponse eventResponse = gson.fromJson(urlContent, EventResponse.class);
            System.out.println("Getting events...");
            ArrayList<Event> eventList = eventResponse.getData();
            Map<String, Event> events = new HashMap<>();

            for (Event event : eventList) {
                events.put(event.getEventID(), event);
            }

            cache.setEvents(events);
        } catch (
                MalformedURLException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void familyRelations() {
        String personID = "1ee2b60";
        Map<String, String> relations = cache.getFamilyRelations(personID);

        String motherID = "2cef751";
        String fatherID = "2c858fe";

        assertEquals(2, relations.size());
        assertTrue(relations.containsKey(motherID));
        assertEquals("Mother", relations.get(motherID));
        assertTrue(relations.containsKey(fatherID));
        assertEquals("Father", relations.get(fatherID));

        personID = "2cef751";
        relations = cache.getFamilyRelations(personID);

        String childID = "1ee2b60";
        String spouseID = "2c858fe";
        motherID = "8dc3cc9";
        fatherID = "e3bc240";

        assertEquals(4, relations.size());
        assertTrue(relations.containsKey(childID));
        assertEquals("Child", relations.get(childID));
        assertTrue(relations.containsKey(spouseID));
        assertEquals("Spouse", relations.get(spouseID));
        assertTrue(relations.containsKey(motherID));
        assertEquals("Mother", relations.get(motherID));
        assertTrue(relations.containsKey(fatherID));
        assertEquals("Father", relations.get(fatherID));
    }

    @Test
    public void filterEvents() {
        Settings settings = new Settings();

        String fathersBirth = "cf7904d";
        String mothersBirth = "1849278";

        settings.setMaleEvents(false);
        Map<String, Event> filteredEvents = cache.getFilteredEvents();

        assertFalse(filteredEvents.containsKey(fathersBirth));
        assertTrue(filteredEvents.containsKey(mothersBirth));

        settings.setMaleEvents(true);
        settings.setFemaleEvents(false);
        filteredEvents = cache.getFilteredEvents();

        assertTrue(filteredEvents.containsKey(fathersBirth));
        assertFalse(filteredEvents.containsKey(mothersBirth));

        settings.setFemaleEvents(true);
        settings.setFathersSide(false);
        filteredEvents = cache.getFilteredEvents();

        assertFalse(filteredEvents.containsKey(fathersBirth));
        assertTrue(filteredEvents.containsKey(mothersBirth));

        settings.setFathersSide(true);
        settings.setMothersSide(false);
        filteredEvents = cache.getFilteredEvents();

        assertTrue(filteredEvents.containsKey(fathersBirth));
        assertFalse(filteredEvents.containsKey(mothersBirth));
    }

    @Test
    public void sortPersonEvents() {
        Map<String, List<Event>> sortedEvents = cache.getEventsByPerson();
        for (List<Event> eventList : sortedEvents.values()) {
            assertEquals("birth", eventList.get(0).getEventType().toLowerCase());
            if (eventList.size() - 1 > 0) {
                assertEquals("death", eventList.get(eventList.size() - 1).getEventType().toLowerCase());
            }

            int lastDate = 0;
            for (Event event : eventList) {
                assertTrue(lastDate <= event.getYear());
                lastDate = event.getYear();
            }
        }
    }

    @Test
    public void search() {
        List<List<String>> searchResults = cache.search("first");
        List<String> personResults = searchResults.get(0);
        List<String> eventResults = searchResults.get(1);

        assertEquals(1, personResults.size());
        assertEquals("1ee2b60", personResults.get(0));
        assertEquals(0, eventResults.size());

        searchResults = cache.search("aiuewfahpweofhowahefoawehf");
        personResults = searchResults.get(0);
        eventResults = searchResults.get(1);

        assertEquals(0, personResults.size());
        assertEquals(0, eventResults.size());

        searchResults = cache.search("Lil");
        personResults = searchResults.get(0);
        eventResults = searchResults.get(1);

        assertEquals(0, personResults.size());
        assertEquals(2, eventResults.size());
        assertEquals("c261a8c", eventResults.get(0));
        assertEquals("c8003b2", eventResults.get(1));
    }
}
