package com.example.familymap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Models.Event;
import Models.Person;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

import static org.junit.Assert.*;

public class LoginTest {

    String newUsername = "newUser18";

    @Test
    public void login() {
        try {
            ServerProxy serverProxy = new ServerProxy();

            LoginRequest request = new LoginRequest("username", "password");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, LoginRequest.class);

            URL url = new URL("http://127.0.0.1:8080/user/login");

            String urlContent = serverProxy.postUrl(url, json);
            gson = new Gson();
            LoginResponse response = gson.fromJson(urlContent, LoginResponse.class);

            assertTrue(response.getSuccess());
            assertEquals("username", response.getUsername());
            assertNotNull(response.getAuthtoken());
            assertEquals("1ee2b60", response.getPersonID());


            gson = new GsonBuilder().setPrettyPrinting().create();

            request = new LoginRequest("fakeUser", "fakePassword");
            json = gson.toJson(request, LoginRequest.class);
            gson = new Gson();
            urlContent = serverProxy.postUrl(url, json);
            response = gson.fromJson(urlContent, LoginResponse.class);

            assertFalse(response.getSuccess());
            assertEquals("Error: User not found", response.getMessage());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void register() {
        try {
            ServerProxy serverProxy = new ServerProxy();

            String username = newUsername + "x";

            RegisterRequest request = new RegisterRequest(username, "goodPassword123", "User@mail.com", "Joe", "Shmo", "m");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, RegisterRequest.class);

            URL url = new URL("http://127.0.0.1:8080/user/register");

            String urlContent = serverProxy.postUrl(url, json);
            gson = new Gson();
            RegisterResponse response = gson.fromJson(urlContent, RegisterResponse.class);

            System.out.println(response.getSuccess());
            System.out.println(response.getMessage());
            assertTrue(response.getSuccess());
            assertEquals(username, response.getUsername());
            assertNotNull(response.getAuthtoken());
            assertNotNull(response.getPersonID());

            gson = new GsonBuilder().setPrettyPrinting().create();

            request = new RegisterRequest(username, "goodPassword123", "User@mail.com", "Joe", "Shmo", "m");
            json = gson.toJson(request, RegisterRequest.class);
            gson = new Gson();
            urlContent = serverProxy.postUrl(url, json);
            response = gson.fromJson(urlContent, RegisterResponse.class);

            assertFalse(response.getSuccess());
            assertEquals("Error: Username taken", response.getMessage());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void getDataRegister() {
        try {
            ServerProxy serverProxy = new ServerProxy();

            String username = newUsername + "y";

            RegisterRequest request = new RegisterRequest(username, "goodPassword123", "User@mail.com", "Joe", "Shmo", "m");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, RegisterRequest.class);

            URL url = new URL("http://127.0.0.1:8080/user/register");

            String urlContent = serverProxy.postUrl(url, json);
            gson = new Gson();
            RegisterResponse response = gson.fromJson(urlContent, RegisterResponse.class);

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

            assertTrue(response.getSuccess());
            assertEquals(31, personList.size());

            url = new URL("http://127.0.0.1:8080/event");

            urlContent = serverProxy.getData(url, authToken);
            EventResponse eventResponse = gson.fromJson(urlContent, EventResponse.class);
                System.out.println("Getting events...");
                ArrayList<Event> eventList = eventResponse.getData();
                Map<String, Event> events = new HashMap<>();

                for (Event event : eventList) {
                    events.put(event.getEventID(), event);
                }

            assertTrue(response.getSuccess());
            assertEquals(91, eventList.size());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void getDataLogin(){
        try {
            ServerProxy serverProxy = new ServerProxy();

            LoginRequest request = new LoginRequest("username", "password");

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

            assertTrue(response.getSuccess());
            assertEquals(31, personList.size());
            assertTrue(persons.containsKey("1ee2b60"));
            assertTrue(persons.containsKey("2b8f234"));
            assertTrue(persons.containsKey("8907c32"));

            url = new URL("http://127.0.0.1:8080/event");

            urlContent = serverProxy.getData(url, authToken);
            EventResponse eventResponse = gson.fromJson(urlContent, EventResponse.class);
            System.out.println("Getting events...");
            ArrayList<Event> eventList = eventResponse.getData();
            Map<String, Event> events = new HashMap<>();

            for (Event event : eventList) {
                events.put(event.getEventID(), event);
            }

            assertTrue(response.getSuccess());
            assertEquals(91, eventList.size());
            assertTrue(events.containsKey("c261a8c"));
            assertTrue(events.containsKey("00295c8"));
            assertTrue(events.containsKey("d239369"));

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
}
