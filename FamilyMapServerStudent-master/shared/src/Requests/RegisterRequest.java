package Requests;

import Responses.RegisterResponse;

/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 */
public class RegisterRequest {
  String username;
  String password;
  String email;
  String firstName;
  String lastName;
  String gender;

  public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
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


  @Override
  public String toString() {
    return "RegisterRequest{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", gender='" + gender + '\'';
  }
}


