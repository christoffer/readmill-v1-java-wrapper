package com.readmill.api;

public interface Endpoints {
    String TOKEN               = "/oauth/token";
    String AUTHORIZE           = "/oauth/authorize";
    String REVOKE              = "/oauth/revoke";

    // User
    String ME                  = "/me";
    String USERS               = "/users";
    String USER_DETAILS        = "/users/%d";

    // Book
    String BOOKS               = "/books";
    String BOOK_SEARCH         = "/books";
    String BOOK_READINGS       = "/books/%d/readings";

    // Reading
    String READINGS            = "/readings/%d";
    String PINGS               = "/readings/%d/pings";
    String PERIODS             = "/readings/%d/periods";
}
