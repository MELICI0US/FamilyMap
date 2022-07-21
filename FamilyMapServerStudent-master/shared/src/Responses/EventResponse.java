package Responses;

import Models.Event;
import Models.Person;

import java.util.ArrayList;

/**
 * Returns the single Event object with the specified ID.
 */
public class EventResponse extends BaseResponse{
  ArrayList<Event> data;
  String associatedUsername;
  String eventID;
  String personID;
  float latitude;
  float longitude;
  String country;
  String city;
  String eventType;
  int year;


  public EventResponse(String eventID, String username, String personID, float latitude, float longitude, String country, String city, String eventType, int year, boolean success) {
    super(success, null);
    this.eventID=eventID;
    this.associatedUsername=username;
    this.personID=personID;
    this.latitude=latitude;
    this.longitude=longitude;
    this.country=country;
    this.city=city;
    this.eventType=eventType;
    this.year=year;
  }


  public EventResponse(boolean success, ArrayList<Event> events){
    super(success, null);
    this.data = events;
  }

  public EventResponse(boolean success, String message) {
    super(success, message);
  }

  @Override
  public String toString() {
    return "EventResponse{" +
            "data=" + data +
            ", associatedUsername='" + associatedUsername + '\'' +
            ", eventID='" + eventID + '\'' +
            ", personID='" + personID + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", eventType='" + eventType + '\'' +
            ", year=" + year +
            ", message='" + message + '\'' +
            ", success=" + success +
            '}';
  }

  public ArrayList<Event> getData() {
    return data;
  }
}
