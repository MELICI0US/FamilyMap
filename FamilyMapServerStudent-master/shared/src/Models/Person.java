package Models;

import static java.util.UUID.randomUUID;

public class Person {
  String associatedUsername;
  String personID;
  String firstName;
  String lastName;
  String gender;
  String fatherID;
  String motherID;
  String spouseID;

  /**
   * Initializes data for a person
   * @param personId a unique non-empty string to identify the person
   * @param username a unique non-empty string associated with the person
   * @param firstName a non-empty string
   * @param lastName a non-empty string
   * @param gender either "f" or "m"
   * @param fatherId a unique non-empty string to link the person's father
   * @param motherId a unique non-empty string to link the person's mother
   * @param spouseId a unique non-empty string to link the person's spouse
   */
  public Person(String personId, String username, String firstName, String lastName, String gender, String fatherId, String motherId, String spouseId) {
    this.personID=personId;
    this.associatedUsername=username;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
    this.fatherID=fatherId;
    this.motherID=motherId;
    this.spouseID=spouseId;
  }

  public Person(String username, String firstName, String lastName, String gender, String fatherId, String motherId, String spouseId) {
    generateId();
    this.associatedUsername=username;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
    this.fatherID=fatherId;
    this.motherID=motherId;
    this.spouseID=spouseId;
  }

  public void setFatherID(String fatherID) {
    this.fatherID = fatherID;
  }

  public void setMotherID(String motherID) {
    this.motherID = motherID;
  }

  public void setSpouseID(String spouseID) {
    this.spouseID = spouseID;
  }

  public String getPersonID() {
    return personID;
  }

  public String getUsername() {
    return associatedUsername;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getGender() {
    return gender;
  }

  public String getFatherID() {
    return fatherID;
  }

  public String getMotherID() {
    return motherID;
  }

  public String getSpouseID() {
    return spouseID;
  }

  @Override
  public boolean equals(Object obj) {
    System.out.println("Checking if equal...");
    if (obj == this) {
      return true;
    }

    if (obj.getClass() != this.getClass()) {
      return false;
    }
    System.out.println("Same class...");


    Person other=(Person) obj;

    if (other.getFatherID().equals(fatherID) && other.getFirstName().equals(firstName) && other.getGender().equals(gender) && other.getLastName().equals(lastName) && other.getMotherID().equals(motherID) && other.getPersonID().equals(personID) && other.getSpouseID().equals(spouseID) && other.getUsername().equals(associatedUsername)) {
      System.out.println("All parameters equal...");
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    String person = "";

    person += "personID: " + personID;
    person += " username: " + associatedUsername;
    person += " firstName: " + firstName;
    person += " lastName: " + lastName;
    person += " gender: " + gender;
    person += " fatherID: " + fatherID;
    person += " motherID: " + motherID;
    person += " spouseID: " + spouseID;

    return person;
  }

  public void generateId() {
    String id = randomUUID().toString().substring(0, 7);

    this.personID = id;
  }
}

