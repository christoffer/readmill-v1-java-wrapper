package com.readmill.dal;


public class ReadmillException extends Exception {
  public static final int STATUS_UNDEFINED = -1;

  private int mServerResponseCode = STATUS_UNDEFINED;

  public ReadmillException(int responseCode) {
    mServerResponseCode = responseCode;
  }

  public ReadmillException() {
    mServerResponseCode = STATUS_UNDEFINED;
  }

  public int getServerResponseCode() {
    return mServerResponseCode;
  }

  @Override
  public String toString() {
    return "ReadmillException: Failed with server response code=" + mServerResponseCode;
  }
}


