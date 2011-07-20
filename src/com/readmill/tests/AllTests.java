package com.readmill.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(ReadmillBookTests.class);
    suite.addTestSuite(ReadmillUserTests.class);
    //$JUnit-END$
    return suite;
  }

}
