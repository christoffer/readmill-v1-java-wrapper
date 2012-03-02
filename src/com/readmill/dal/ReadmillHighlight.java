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

public class ReadmillHighlight extends ReadmillEntity {

  // Highlight info
  private long readingId;
  private double position;
  private String content;
  private Date highlightedAt;
  private int commentsCount;
  private int likesCount;
  private long userId;

  // External resources
  private String uri;
  private String permalink;
  private String permalinkURL;

  public ReadmillHighlight(JSONObject json) {
    super(json);
  }

  @Override
  public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    readingId = json.optLong("reading_id", -1);
    position = json.optDouble("position", 0);
    content = getString(json, "content");
    highlightedAt = parseUTC(getString(json, "highlighted_at"));
    commentsCount = json.optInt("comments_count", 0);
    likesCount = json.optInt("likes_count", 0);
    userId = json.optLong("user_id", -1);

    uri = getString(json, "uri");
    permalink = getString(json, "permalink");
    permalinkURL = getString(json, "permalink_url");
  }

  @Override
  public JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("reading_id", readingId);
    json.put("position", position);
    json.put("content", content);
    json.put("highlighted_at", toISO8601(highlightedAt));
    json.put("comments_count", commentsCount);
    json.put("likes_count", likesCount);
    json.put("user_id", userId);
    json.put("uri", uri);
    json.put("permalink", permalink);
    json.put("permalink_url", permalinkURL);

    return json;
  }

  // DAL methods

  /**
   * Fetch all highlights for a given reading
   *
   * @param resourceURI URI of reading to fetch periods for
   * @return A list of all highlights for that reading
   * @throws ReadmillException
   */
  public static ArrayList<ReadmillHighlight> getAllForReading(String resourceURI) throws ReadmillException {
    ArrayList<ReadmillHighlight> highlights = new ArrayList<ReadmillHighlight>();
    JSONArray jsonResult = getAssertedJSONArray(Request.to(toResourceURI(resourceURI)));

    for(int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillHighlight highlight = new ReadmillHighlight((JSONObject) jsonResult.get(i));
        highlights.add(highlight);
      } catch(JSONException ignored) {
        throw new ReadmillException();
      }
    }
    return highlights;
  }

  /**
   * Fetch all highlights by Reading id
   *
   * @param readingId Reading to fetch periods for
   * @return list of highlights
   * @throws ReadmillException When fetching failed
   */
  public static ArrayList<ReadmillHighlight> getAllForReadingId(long readingId) throws ReadmillException {
    return getAllForReading(String.format(Endpoints.HIGHLIGHTS, readingId));
  }

  /**
   * Create a Highlight at Readmill
   *
   * @param readingId  Id of the reading
   * @param position Position of highlight
   * @param content Content of highlight
   * @param highlightedAt Date of highlight
   * @return the created ReadmillHighlight
   * @throws ReadmillException When the operation failed
   */
  public static ReadmillHighlight create(long readingId, double position, String content,
                                         Date highlightedAt) throws ReadmillException {
    Request request = Request.to(Endpoints.HIGHLIGHTS, readingId);
    request.add(Params.Highlight.POSITION, position);
    request.add(Params.Highlight.CONTENT, content);
    request.add(Params.Highlight.HIGHLIGHTED_AT, toISO8601(highlightedAt));

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

  /**
   * Fetch a highlight by URI
   *
   * @param location URI to the highlight to fetch
   * @return A ReadmillHighlight
   * @throws ReadmillException When fetching the highlight failed
   */
  public static ReadmillHighlight get(String location) throws ReadmillException {
    JSONObject data = getAssertedJSON(new Request(toResourceURI(location)));
    return new ReadmillHighlight(data);
  }

  public long getReadingId() {
    return readingId;
  }

  public void setReadingId(long readingId) {
    this.readingId = readingId;
  }

  public double getPosition() {
    return position;
  }

  public void setPosition(double position) {
    this.position = position;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getHighlightedAt() {
    return highlightedAt;
  }

  public void setHighlightedAt(Date highlightedAt) {
    this.highlightedAt = highlightedAt;
  }

  public int getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(int commentsCount) {
    this.commentsCount = commentsCount;
  }

  public int getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(int likesCount) {
    this.likesCount = likesCount;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
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
}
