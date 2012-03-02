package com.readmill.api;

/**
 * Request parameters for various objects.
 */
public interface Params {

    interface Reading {
      String IS_PRIVATE              = "reading[is_private]";
      String STATE                   = "reading[state]";
      String CLOSING_REMARK          = "reading[closing_remark]";
    }

    interface Ping {
      String IDENTIFIER              = "ping[identifier]";
      String PROGRESS                = "ping[progress]";
      String DURATION                = "ping[duration]";
      String OCCURRED_AT             = "ping[occurred_at]";
      String LATITUDE                = "ping[lat]";
      String LONGITUDE               = "ping[lng]";
    }

    interface User {
        String NAME                  = "user[username]";
        String FULLNAME              = "user[full_name]";
    }

    interface Book {
      String TITLE                   = "book[title]";
      String AUTHOR                  = "book[author]";
    }

    interface Highlight {
      String POSITION                = "highlight[position]";
      String CONTENT                 = "highlight[content]";
      String HIGHLIGHTED_AT          = "highlight[highlighted_at]";
      String LOCATORS_MID            = "highlight[locators][mid]";
    }

}
