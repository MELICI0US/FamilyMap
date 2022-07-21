package Responses;

import Models.Person;

import java.util.ArrayList;

/**
 * Returns the single Person object with the specified ID.
 */
public class PersonResponse extends BaseResponse {
  ArrayList<Person> data;
  String associatedUsername;
  String personID;
  String firstName;
  String lastName;
  String gender;
  String fatherID;
  String motherID;
  String spouseID;


  public PersonResponse(String personID, String username, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, boolean success) {
    super(success, null);
    this.personID=personID;
    this.associatedUsername=username;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
    this.fatherID=fatherID;
    this.motherID=motherID;
    this.spouseID=spouseID;
  }

  public PersonResponse(boolean success, ArrayList<Person> persons){
    super(success, null);
    this.data = persons;
  }

  public PersonResponse(boolean success, String message) {
    super(success, message);
  }

  public ArrayList<Person> getData() {
    return data;
  }
}
