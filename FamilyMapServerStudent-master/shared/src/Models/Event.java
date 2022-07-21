package Models;

import static java.util.UUID.randomUUID;

public class Event {
  String associatedUsername;
  String eventID;
  String personID;
  float latitude;
  float longitude;
  String country;
  String city;
  String eventType;
  int year;

  /**
   * Initializes data for a person
   * @param personId a unique non-empty string to identify the person the event belongs to
   * @param username a unique non-empty string associated with the person the event belongs to
   * @param city a string of the name of the city the event happened in
   * @param country a string of the name of the country the event happened in
   * @param eventId a unique string to identify the event
   * @param eventType a string to classify the event
   * @param latitude a float of the latitude of the place the event happened
   * @param longitude a float of the longitude of the place the event happened
   * @param year and int of the year the event happened
   * */
  public Event(String eventId, String username, String personId, float latitude, float longitude, String country, String city, String eventType, int year) {
    this.eventID=eventId;
    this.associatedUsername=username;
    this.personID=personId;
    this.latitude=latitude;
    this.longitude=longitude;
    this.country=country;
    this.city=city;
    this.eventType=eventType;
    this.year=year;
  }

  public Event(String username, String personId, float latitude, float longitude, String country, String city, String eventType, int year) {
    generateId();
    this.associatedUsername=username;
    this.personID=personId;
    this.latitude=latitude;
    this.longitude=longitude;
    this.country=country;
    this.city=city;
    this.eventType=eventType;
    this.year=year;
  }

  public String getEventID() {
    return eventID;
  }

  public String getAssociatedUsername() {
    return associatedUsername;
  }

  public String getPersonID() {
    return personID;
  }

  public float getLatitude() {
    return latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public String getCountry() {
    return country;
  }

  public String getCity() {
    return city;
  }

  public String getEventType() {
    return eventType;
  }

  public int getYear() {
    return year;
  }

  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (o instanceof Event) {
      Event oEvent = (Event) o;
      return oEvent.getEventID().equals(getEventID()) &&
              oEvent.getAssociatedUsername().equals(getAssociatedUsername()) &&
              oEvent.getPersonID().equals(getPersonID()) &&
              oEvent.getLatitude() == (getLatitude()) &&
              oEvent.getLongitude() == (getLongitude()) &&
              oEvent.getCountry().equals(getCountry()) &&
              oEvent.getCity().equals(getCity()) &&
              oEvent.getEventType().equals(getEventType()) &&
              oEvent.getYear() == (getYear());
    } else {
      return false;
    }
  }

  public void generateId() {
    String id = randomUUID().toString().substring(0, 7);

    this.eventID = id;
  }
}
