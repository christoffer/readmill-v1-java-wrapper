package com.readmill.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses
({
    BookAssetTests.class,
    ReadmillBookTests.class,
    PingTests.class,
    ReadingTests.class,
    ReadingPeriodTests.class,
    UserTests.class,
    EntityTests.class
})
public class AllTests {}
