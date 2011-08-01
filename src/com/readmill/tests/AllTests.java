package com.readmill.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(ReadmillBookAssetTests.class);
    suite.addTestSuite(ReadmillBookTests.class);
    suite.addTestSuite(ReadmillPingTests.class);
    suite.addTestSuite(ReadmillReadingTests.class);
    suite.addTestSuite(ReadmillUserTests.class);
    //$JUnit-END$
    return suite;
  }

}
