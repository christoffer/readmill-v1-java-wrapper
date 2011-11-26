package com.readmill.dal;

import com.readmill.api.ApiWrapper;
import com.readmill.api.Env;
import com.readmill.api.ReadmillAPI.TokenStateListener;
import com.readmill.api.Token;

import java.io.IOException;
import java.net.URI;

public class ReadmillDAL {
  private static ApiWrapper mWrapper;

  private ReadmillDAL() {
  }

  public static void initialize(String clientId, String clientSecret, URI redirectUri, Token token, Env env) {
    mWrapper = new ApiWrapper(clientId, clientSecret, redirectUri, token, env);
  }

  public static ApiWrapper getWrapper() throws ReadmillException {
    if(mWrapper == null) {
      throw new ReadmillException("Readmill DAL Not initialized");
    }
    return mWrapper;
  }

  public static void setAccessToken(Token newToken) {
    mWrapper.setToken(newToken);
  }

  public static Token getAccessToken() {
    return mWrapper.getToken();
  }

  public static boolean exchangeCodeForToken(String code) {
    try {
      mWrapper.authorizationCode(code);
      return mWrapper.getToken().valid();
    } catch(IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static String getAuthorizeURL() throws ReadmillException {
    return getWrapper().authorizationCodeUrl().toString();
  }

  public static Env getEnvironment() throws ReadmillException {
    return getWrapper().env;
  }

  public static void addTokenStateChangeListener(TokenStateListener listener) throws ReadmillException {
    getWrapper().addTokenStateListener(listener);
  }

}
