package com.readmill.dal;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Christoffer
 *
 */
public class ReadmillBook extends ReadmillEntity {

	private String title;
	private String author;
	private String coverURL;
	private String isbn;
	private String story;
	private Date publishedAt;
	private String language;
	private String permalink;
	private String permalinkURL;
	private String uri;
	private ArrayList<ReadmillBookAsset> assets;

	public ReadmillBook() {
		super();
		assets = new ArrayList<ReadmillBookAsset>();
	}

	public ReadmillBook(JSONObject json) {
		super(json);
	}

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

	/**
	 * convertFromJSON
	 */
	@Override
	public void convertFromJSON(JSONObject json) {
		// must be here, because super(json) use this method
		assets = new ArrayList<ReadmillBookAsset>();

		id = json.optLong("id", -1);

		title = json.optString("title", "");
		author = json.optString("author", "");
		coverURL = json.optString("cover_url", "");
		isbn = json.optString("isbn", "");
		story = json.optString("story", "");
		publishedAt = this.fromUTC(json.optString("published_at", ""));
		language = json.optString("language", "");
		permalink = json.optString("permalink", "");
		permalinkURL = json.optString("permalink_url", "");
		uri = json.optString("uri", "");

		if (json.optJSONArray("assets") != null) {
			for (int i = 0; i < json.optJSONArray("assets").length(); i++) {
				assets.add(new ReadmillBookAsset(json.optJSONArray("assets")
						.optJSONObject(i)));
			}
		}
	}

	/**
	 * convertToJSON
	 * 
	 * @throws DatatypeConfigurationException
	 */
	@Override
	public JSONObject convertToJSON() throws JSONException,
			DatatypeConfigurationException {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();

		json.put("id", id);
		json.put("title", title);
		json.put("author", author);
		json.put("cover_url", coverURL);
		json.put("isbn", isbn);
		json.put("story", story);
		json.put("published_at", this.toUTC(publishedAt));
		json.put("language", language);
		json.put("permalink", permalink);
		json.put("permalink_url", permalinkURL);
		json.put("uri", uri);

		for (int i = 0; i < assets.size(); i++) {
			array.put(i, assets.get(i).convertToJSON());
		}

		json.put("assets", array);

		return json;
	}
}