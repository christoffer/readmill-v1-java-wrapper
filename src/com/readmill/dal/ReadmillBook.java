package com.readmill.dal;

import com.readmill.api.Endpoints;
import com.readmill.api.Params;
import com.readmill.api.Request;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Christoffer
 */
public class ReadmillBook extends ReadmillEntity {

  // Book info
  private String title;
  private String author;
  private String coverURL;
  private String isbn;
  private String story;

  private Date publishedAt;

  private String language;

  // External resources
  private String permalink;
  private String permalinkURL;
  private String uri;

  // Relational data
  private ArrayList<ReadmillBookAsset> assets;

  public ReadmillBook(JSONObject json) {
    super(json);
    if(assets == null) {
      assets = new ArrayList<ReadmillBookAsset>();
    }
  }

  /**
   * convertFromJSON
   */
  @Override
  public void convertFromJSON(JSONObject json) {
    // must be here, because super(json) use this method
    assets = new ArrayList<ReadmillBookAsset>();

    id = json.optLong("id", -1);
    title = getString(json, "title");
    author = getString(json, "author");
    coverURL = getString(json, "cover_url");
    isbn = getString(json, "isbn");
    story = getString(json, "story");
    publishedAt = parseUTC(getString(json, "published_at"));
    language = getString(json, "language");
    permalink = getString(json, "permalink");
    permalinkURL = getString(json, "permalink_url");
    uri = getString(json, "uri");

    JSONArray jsonAssets = json.optJSONArray("assets");
    if(jsonAssets != null) {
      for(int i = 0; i < jsonAssets.length(); i++) {
        try {
          assets.add(new ReadmillBookAsset(jsonAssets.getJSONObject(i)));
        } catch(JSONException ignore) {}
      }
    }
  }

  /**
   * convertToJSON
   */
  @Override
  public JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();

    json.put("id", id);
    json.put("title", title);
    json.put("author", author);
    json.put("cover_url", coverURL);
    json.put("isbn", isbn);
    json.put("story", story);
    json.put("published_at", this.toISO8601(publishedAt));
    json.put("language", language);
    json.put("permalink", permalink);
    json.put("permalink_url", permalinkURL);
    json.put("uri", uri);

    for(int i = 0; i < assets.size(); i++) {
      array.put(i, assets.get(i).convertToJSON());
    }

    json.put("assets", array);

    return json;
  }

  // DAL methods

  public static ArrayList<ReadmillBook> search(String searchText) throws ReadmillException {
    ArrayList<ReadmillBook> foundBooks = new ArrayList<ReadmillBook>();
    JSONArray jsonResult = getAssertedJSONArray(Request.to(Endpoints.BOOK_SEARCH).with("q", searchText));
    for(int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillBook book = new ReadmillBook((JSONObject) jsonResult.get(i));
        foundBooks.add(book);
      } catch(JSONException ignored) {
        throw new ReadmillException();
      }
    }
    return foundBooks;
  }

  /**
   * Fetch a book by URI
   *
   * @param location URI to the book to fetch
   * @return A ReadmillBook
   * @throws ReadmillException When fetching the book failed
   */
  public static ReadmillBook get(String location) throws ReadmillException {
    JSONObject data = getAssertedJSON(new Request(toResourceURI(location)));
    return new ReadmillBook(data);
  }

  /**
   * Create a Book at Readmill
   *
   * @param title  Title of book
   * @param author Author of book
   * @return the created ReadmillBook
   * @throws ReadmillException When the operation failed
   */
  public static ReadmillBook create(String title, String author) throws ReadmillException {
    Request request = Request.to(Endpoints.BOOKS);
    request.add(Params.Book.TITLE, title);
    request.add(Params.Book.AUTHOR, author);

    try {
      HttpResponse response = ReadmillDAL.getWrapper().post(request);
      assertResponseCode(HttpStatus.SC_CREATED, response);
      String location = response.getFirstHeader("Location").getValue();
      return get(location);
    } catch(IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  // Setters and getters

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCoverURL() {
    return coverURL;
  }

  public void setCoverURL(String coverURL) {
    this.coverURL = coverURL;
  }

  public String getISBN() {
    return isbn;
  }

  public void setISBN(String isbn) {
    this.isbn = isbn;
  }

  public String getStory() {
    return story;
  }

  public void setStory(String story) {
    this.story = story;
  }

  public Date getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Date publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getPermalink() {
    return permalink;
  }

  public void setPermalink(String permalink) {
    this.permalink = permalink;
  }

  public String getPermalinkURL() {
    return permalinkURL;
  }

  public void setPermalinkURL(String permalinkURL) {
    this.permalinkURL = permalinkURL;
  }

  public String getURI() {
    return uri;
  }

  public void setURI(String uri) {
    this.uri = uri;
  }

  public ArrayList<ReadmillBookAsset> getAssets() {
    return assets;
  }

  public void setAssets(ArrayList<ReadmillBookAsset> assets) {
    this.assets = assets;
  }

}
