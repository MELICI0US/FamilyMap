package com.example.familymap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Models.Person;
import Models.Event;
import Models.User;

public class DataCache {
    private static Map<String, Person> persons = null;
    private static Map<String, Event> events = null;
    private static Map<String, List<Event>> eventsByPerson = null;
    private static String userId = null;

    private static Map<String, Person> personsFathers = null;
    private static Map<String, Event> eventsFathers = null;

    private static Map<String, Person> personsMothers = null;
    private static Map<String, Event> eventsMothers = null;

    private static Map<String, String> parentToChild = null;

    public static void clear() {
        persons = null;
        events = null;
        eventsByPerson = null;
        userId = null;
        personsFathers = null;
        eventsFathers = null;
        personsMothers = null;
        eventsMothers = null;
        parentToChild = null;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        DataCache.userId = userId;
    }

    public static Map<String, Person> getPersons() {
        return persons;
    }

    public static void setPersons(Map<String, Person> persons) {
        DataCache.persons = persons;
        if (events != null) {
            mapEventsToPerson();
            setFathersSide();
            setMothersSide();
        }
    }

    public static Map<String, Event> getEvents() {
        return events;
    }

    public static void setEvents(Map<String, Event> events) {
        DataCache.events = events;
        if (events != null) {
            mapEventsToPerson();
            setFathersSide();
            setMothersSide();
        }
    }

    public static Person getPerson(String personID) {
        if (persons.containsKey(personID)) {
            return persons.get(personID);
        } else {
            return null;
        }
    }

    public Event getEvent(String eventID) {
        if (events.containsKey(eventID)) {
            return events.get(eventID);

        } else {
            return null;
        }
    }

    private static void mapEventsToPerson() {
        Map<String, List<Event>> personEvents = new HashMap<>();
        for (Event event : events.values()) {
            if (personEvents.containsKey(event.getPersonID())) {
                List<Event> oldList = personEvents.get(event.getPersonID());
                List<Event> list = new ArrayList<>();
                boolean added = false;
                for (Event el : oldList) {
                    list.add(el);
                    if (event.getEventType().equalsIgnoreCase("birth") && !added) {
                        list.add(0, event);
                        added = true;
                    } else if (el.getEventType().equalsIgnoreCase("death") && !added) {
                        list.add(list.indexOf(el), event);
                        added = true;
                    } else if (event.getYear() < el.getYear() && !added &&
                            !el.getEventType().equalsIgnoreCase("birth")) {
                        list.add(list.indexOf(el), event);
                        added = true;
                    } else if (event.getYear() == el.getYear() && !added && event.getEventType().equalsIgnoreCase("death")) {
                        if (event.getEventType().toLowerCase().compareTo(el.getEventType().toLowerCase()) > 0
                                && !el.getEventType().equalsIgnoreCase("birth")
                                || el.getEventType().equalsIgnoreCase("death")) {
                            list.add(list.indexOf(el), event);
                            added = true;
                        } else if (list.indexOf(el) + 1 != list.size()) {
                            list.add(list.indexOf(el) + 1, event);
                            added = true;
                        }
                    }
                }

                if (!added) {
                    list.add(event);
                }
                System.out.println("list: ");
                for (Event item : list) {
                    System.out.println(item.getYear());
                    System.out.println(item.getEventType());
                }
                System.out.println("adding: ");
                System.out.println(event.getYear());
                System.out.println(event.getEventType());
                personEvents.remove(event.getPersonID());
                personEvents.put(event.getPersonID(), list);
            } else {
                List<Event> list = new ArrayList<>();
                list.add(event);
                personEvents.put(event.getPersonID(), list);
            }
        }

        eventsByPerson = personEvents;
    }

    public static Map<String, List<Event>> getEventsByPerson() {
        return eventsByPerson;
    }

    private static void setFathersSide() {
        System.out.println("Generaing fathers side...");
        Person root = getPerson(userId);
        String father = root.getFatherID();
        personsFathers = new HashMap<>();
        eventsFathers = new HashMap<>();
        parentToChild = new HashMap<>();
        personsFathers.put(father, getPerson(father));
        personsFathers.put(userId, root);


        parentToChild.put(father, root.getPersonID());

        fatherRecur(father);

        System.out.println("Generaing fathers events...");

        for (String person : eventsByPerson.keySet()) {
            if (personsFathers.containsKey(person)) {
                for (Event event : eventsByPerson.get(person)) {
                    System.out.println(event.toString());
                    eventsFathers.put(event.getEventID(), event);
                }
            }
        }
    }

    private static void fatherRecur(String personID) {
        String mom = getPerson(personID).getMotherID();
        String dad = getPerson(personID).getFatherID();

        if (mom != null) {
            personsFathers.put(mom, getPerson(mom));
            parentToChild.put(mom, personID);
            fatherRecur(mom);
        }

        if (dad != null) {
            personsFathers.put(dad, getPerson(dad));
            parentToChild.put(dad, personID);
            fatherRecur(dad);
        }
    }

    private static void setMothersSide() {
        System.out.println("Generaing mothers side...");
        Person root = getPerson(userId);
        String mother = root.getMotherID();
        personsMothers = new HashMap<>();
        eventsMothers = new HashMap<>();
        personsMothers.put(mother, getPerson(mother));
        personsMothers.put(userId, root);

        parentToChild.put(mother, root.getPersonID());

        motherRecur(mother);

        System.out.println("Generaing mothers events...");

        for (String person : eventsByPerson.keySet()) {
            if (personsMothers.containsKey(person)) {
                for (Event event : eventsByPerson.get(person)) {
                    eventsMothers.put(event.getEventID(), event);
                }
            }
        }
    }

    private static void motherRecur(String personID) {
        String mom = getPerson(personID).getMotherID();
        String dad = getPerson(personID).getFatherID();

        if (mom != null) {
            personsMothers.put(mom, getPerson(mom));
            parentToChild.put(mom, personID);
            motherRecur(mom);
        }

        if (dad != null) {
            personsMothers.put(dad, getPerson(dad));
            parentToChild.put(dad, personID);
            motherRecur(dad);
        }
    }

    public static Map<String, Person> getPersonsFathers() {
        return personsFathers;
    }

    public static Map<String, Event> getEventsFathers() {
        return eventsFathers;
    }

    public static Map<String, Person> getPersonsMothers() {
        return personsMothers;
    }

    public static Map<String, Event> getEventsMothers() {
        return eventsMothers;
    }

    public Map<String, String> getFamilyRelations(String personId) {
        Map<String, String> relations = new HashMap<>();

        Person person = getPerson(personId);
        Map<String, Person> filteredPersons = getFilteredPersons();

        if (person.getFatherID() != null) {
            if (filteredPersons.containsKey(person.getFatherID()))
                relations.put(person.getFatherID(), "Father");
        }
        if (person.getMotherID() != null) {
            if (filteredPersons.containsKey(person.getMotherID()))
                relations.put(person.getMotherID(), "Mother");
        }
        if (person.getSpouseID() != null) {
            if (filteredPersons.containsKey(person.getSpouseID()))
                relations.put(person.getSpouseID(), "Spouse");
        }
        if (parentToChild.containsKey(personId)) {
            if (filteredPersons.containsKey(parentToChild.get(personId)))
                relations.put(parentToChild.get(personId), "Child");
        }

        return relations;
    }

    public Map<String, Event> getFilteredEvents() {
        Settings settings = new Settings();
        Map<String, Event> filteredEvents;

        if (settings.isMothersSide() && settings.isFathersSide()) {
            filteredEvents = events;
        } else if (settings.isMothersSide()) {
            filteredEvents = eventsMothers;
        } else if (settings.isFathersSide()) {
            filteredEvents = eventsFathers;
        } else {
            return new HashMap<>();
        }

        if (!settings.isFemaleEvents() && !settings.isMaleEvents()) {
            return new HashMap<>();
        } else if (!settings.isFemaleEvents()) {
            Map<String, Event> unFilteredEvents = new HashMap<>(filteredEvents);
            for (Event event : unFilteredEvents.values()) {
                Person person = getPerson(event.getPersonID());
                if (person.getGender().equals("f")) {
                    filteredEvents.remove(event.getEventID());
                }
            }
        } else if (!settings.isMaleEvents()) {
            Map<String, Event> unFilteredEvents = new HashMap<>(filteredEvents);
            for (Event event : unFilteredEvents.values()) {
                Person person = getPerson(event.getPersonID());
                if (person.getGender().equals("m")) {
                    filteredEvents.remove(event.getEventID());
                }
            }
        }

        return filteredEvents;
    }

    public Map<String, Person> getFilteredPersons() {
        return persons;
    }

    public List<List<String>> search(String search) {
        List<String> searchedPersons = new ArrayList<>();
        List<String> searchedEvents = new ArrayList<>();
        List<List<String>> searchResults = new ArrayList<>();

        Map<String, Event> filteredEvents = getFilteredEvents();
        Map<String, Person> filteredPersons = getFilteredPersons();

        search = search.toLowerCase();

        for (Event event : filteredEvents.values()) {
            if (event.getEventType().toLowerCase().contains(search)) {
                searchedEvents.add(event.getEventID());
            } else if (event.getCountry().toLowerCase().contains(search)) {
                searchedEvents.add(event.getEventID());
            } else if (event.getCity().toLowerCase().contains(search)) {
                searchedEvents.add(event.getEventID());
            } else if (String.valueOf(event.getYear()).contains(search)) {
                searchedEvents.add(event.getEventID());
            }
        }

        for (Person person : filteredPersons.values()) {
            if (person.getFirstName().toLowerCase().contains(search)) {
                searchedPersons.add(person.getPersonID());
            } else if (person.getLastName().toLowerCase().contains(search)) {
                searchedPersons.add(person.getPersonID());
            }
        }

        searchResults.add(searchedPersons);
        searchResults.add(searchedEvents);
        return searchResults;
    }
}
