
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.After;
import org.junit.Before;


public class ReadmillTestCase {

	// The relative path of the package root where sample data is stored
	private static final String testResourcePath = "/src/com/readmill/tests/resources";
	 
	/**
	 * Open a file from the support directory and return it's content.
	 *
	 * @param filename Name of the file in the support directory
	 * @return the file content or null if the file was missing
	 */
	 protected String getResourceContent(String resourceName) {
		
		 BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(new File(resourceName).getPath())));
		 String content = "", line = null;
		 try {
			 while((line = in.readLine()) != null) {
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
