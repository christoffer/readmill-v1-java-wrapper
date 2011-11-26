package com.readmill.tests;

import java.io.*;

/**
 * Base class for adding convenience methods to test cases
 *
 * @author christoffer
 */

public class TestUtils {

  /**
   * Open a file from the support directory and return it's content.
   *
   * @param resourceName Name of the file in the support directory
   * @return the file content or null if the file was missing
   */
  protected static String getResourceContent(String resourceName) {
    String resourceDirectory = System.getenv("READMILL_RESOURCE_DIR");
    if(resourceDirectory == null) {
      throw new RuntimeException("Please set the environment variable READMILL_RESOURCE_DIR to the directory containing file \"" + resourceName + "\"");
    }

    FileInputStream fis;
    try {
      fis = new FileInputStream(new File(resourceDirectory, resourceName).getAbsolutePath());
    } catch(FileNotFoundException e) {
      throw new RuntimeException("Could not find the file \"" + resourceName + "\" in READMILL_RESOURCE_DIR: " + resourceDirectory);
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(fis));
    String content = "", line;
    try {
      while((line = in.readLine()) != null) {
        content += line;
      }
      in.close();
      return content;
    } catch(IOException e) {
      return null;
    }
  }

}
