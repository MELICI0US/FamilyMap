package Models;

import static java.util.UUID.randomUUID;

public class User {
  String username;
  String password;
  String email;
  String firstName;
  String lastName;
  String gender;
  String personID;

  /**
   * Initializes data for a person
   *
   * @param personID  a unique non-empty string to identify the person object
   * @param username  a unique non-empty string associated with the person
   * @param password  a non-empty string that corresponds to the username
   * @param email     a non-empty string containing the user's email
   * @param firstName a non-empty string
   * @param lastName  a non-empty string
   * @param gender    either "f" or "m"
   */
  public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.personID = personID;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
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

  public String getPersonID() {
    return personID;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj.getClass() != this.getClass()) {
      return false;
    }

    User other = (User) obj;

    if (other.getEmail().equals(email) && other.getFirstName().equals(firstName) && other.getGender().equals(gender) && other.getLastName().equals(lastName) && other.getPassword().equals(password) && other.getPersonID().equals(personID) && other.getUsername().equals(username)) {
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    String user = "";

    user += "username: " + username;
    user += " password: " + password;
    user += " email: " + email;
    user += " firstName: " + firstName;
    user += " lastName: " + lastName;
    user += " gender: " + gender;
    user += " personID: " + personID;

    return user;
  }

}
