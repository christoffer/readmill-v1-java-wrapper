package com.readmill.dal;

import com.readmill.api.Endpoints;
import com.readmill.api.Request;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillUser extends ReadmillEntity {

  // User info
  private String userName;
  private String fullName;
  private String firstName;
  private String lastName;
  private String description;
  private String country;
  private String city;
  private String website;

  // External resources
  private String uri;
  private String avatarUrl;
  private String permalinkUrl;

  // Book info
  private long booksInteresting;
  private long booksOpen;
  private long booksFinished;
  private long booksAbandoned;

  // Follower counts
  private long followers;
  private long followings;

  private String email;

  // Fetchable relations
  // private List<ReadmillReading> readings;
  private String readingsResourceUrl;

  /**
   * convertFromJSON
   */
  @Override
  public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    userName = getString(json, "username");
    fullName = getString(json, "fullname");
    firstName = getString(json, "firstname");
    lastName = getString(json, "lastname");
    country = getString(json, "country");
    city = getString(json, "city");
    website = getString(json, "website");
    description = getString(json, "description");
    uri = getString(json, "uri");
    permalinkUrl = getString(json, "permalink_url");
    avatarUrl = getString(json, "avatar_url");
    booksInteresting = json.optLong("books_interesting", 0);
    booksOpen = json.optLong("books_open", 0);
    booksFinished = json.optLong("books_finished", 0);
    booksAbandoned = json.optLong("books_abandoned", 0);
    followers = json.optLong("followers");
    followings = json.optLong("followings");
    email = getString(json, "email");
    readingsResourceUrl = getString(json, "readings");
  }

  /**
   * convertToJSON
   */
  @Override
  public JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("username", userName);
    json.put("fullname", fullName);
    json.put("firstname", firstName);
    json.put("lastname", lastName);
    json.put("country", country);
    json.put("city", city);
    json.put("website", website);
    json.put("description", description);
    json.put("uri", uri);
    json.put("permalink_url", permalinkUrl);
    json.put("avatar_url", avatarUrl);
    json.put("books_interesting", booksInteresting);
    json.put("books_open", booksOpen);
    json.put("books_finished", booksFinished);
    json.put("books_abandoned", booksAbandoned);
    json.put("followers", followers);
    json.put("followings", followings);
    json.put("email", email);
    json.put("readings", readingsResourceUrl);

    return json;
  }

  // DAL Methods

  /**
   * Return the user for the currently set token
   *
   * @return Current user
   * @throws ReadmillException if there was an error fetching the user
   */
  public static ReadmillUser getCurrent() throws ReadmillException {
    JSONObject data = getAssertedJSON(Request.to(Endpoints.ME));
    return new ReadmillUser(data);
  }

  // Get readings
  //  public ArrayList<ReadmillReading> getReadings() {
  //
  //  }

  // Convenience

  public String getShortName() {
    if(firstName.length() > 0) {
      return firstName;
    }

    if(userName.length() > 0) {
      return userName;
    }

    return "";
  }


  // Getters and setters

  public ReadmillUser(JSONObject json) {
    super(json);
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String username) {
    this.userName = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullname) {
    this.fullName = fullname;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstname) {
    this.firstName = firstname;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastname) {
    this.lastName = lastname;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getPermalinkUrl() {
    return permalinkUrl;
  }

  public void setPermalinkUrl(String permalinkUrl) {
    this.permalinkUrl = permalinkUrl;
  }

  public long getBooksInteresting() {
    return booksInteresting;
  }

  public void setBooksInteresting(long booksInteresting) {
    this.booksInteresting = booksInteresting;
  }

  public long getBooksOpen() {
    return booksOpen;
  }

  public void setBooksOpen(long booksOpen) {
    this.booksOpen = booksOpen;
  }

  public long getBooksFinished() {
    return booksFinished;
  }

  public void setBooksFinished(long booksFinished) {
    this.booksFinished = booksFinished;
  }

  public long getBooksAbandoned() {
    return booksAbandoned;
  }

  public void setBooksAbandoned(long booksAbandoned) {
    this.booksAbandoned = booksAbandoned;
  }

  public String getReadingsResourceUrl() {
    return readingsResourceUrl;
  }

  public void setReadingsResourceUrl(String readingsResourceUrl) {
    this.readingsResourceUrl = readingsResourceUrl;
  }

  public long getFollowers() {
    return followers;
  }

  public void setFollowers(long followers) {
    this.followers = followers;
  }

  public long getFollowings() {
    return followings;
  }

  public void setFollowings(long followings) {
    this.followings = followings;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
