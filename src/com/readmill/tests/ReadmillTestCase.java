package com.readmill.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.TestCase;

/**
 * Base class for adding convenience methods to test cases
 *
 * @author christoffer
 *
 */
public class ReadmillTestCase extends TestCase {

	private static final String testResourcePath = "/com/readmill/tests/resources/";

	/**
	 * Open a file from the support directory and return it's content.
	 * 
	 * @param filename Name of the file in the support directory
	 * @return the file content or null if the file was missing
	 */
	protected String getResourceContent(String resourceName) {
	  String filePath = new File(testResourcePath, resourceName).getPath(); 
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));
		String content = "", line = null;
		try {
			while ((line = in.readLine()) != null) {
				content += line;
			}
			in.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}