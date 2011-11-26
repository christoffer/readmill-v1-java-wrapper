package com.readmill.dal;


public class ReadmillException extends Exception {
  private static final long serialVersionUID = 7135772463808664934L;

  public static final int STATUS_UNDEFINED = -1;

  private int mServerResponseCode = STATUS_UNDEFINED;
  private String mMessage = "";

  public ReadmillException() {}

  public ReadmillException(String message) {
    mMessage = message;
  }

  public ReadmillException(int responseCode) {
    mMessage = "ReadmillException: Failed with server response code: " + responseCode;
    mServerResponseCode = responseCode;
  }

  public int getServerResponseCode() {
    return mServerResponseCode;
  }

  @Override
  public String toString() {
    return mMessage;
  }
}


