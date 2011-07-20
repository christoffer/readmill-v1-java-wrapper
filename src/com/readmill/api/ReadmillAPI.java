package com.readmill.api;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;

/**
 * Interface with Readmill.
*/
public interface ReadmillAPI {
    // grant types
    String AUTHORIZATION_CODE = "authorization_code";
    String REFRESH_TOKEN      = "refresh_token";

    // other constants
    String REALM              = "Readmill";
    String OAUTH_SCHEME       = "oauth";
    String VERSION            = "0.1";
    String USER_AGENT         = "Readmill Java Wrapper " + VERSION;

    /**
     * Request a token using <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-10#section-4.1.1">
     * Authorization Code</a>.
     *
     * @param code the authorization code
     * @return a valid token
     * @throws com.ReadmillAPI.InvalidTokenException invalid token
     * @throws IOException In case of network/server errors
     */
    Token authorizationCode(String code) throws IOException;

    /**
     * Tries to refresh the currently used access token with the refresh token
     * @return a valid token
     * @throws IOException in case of network problems
     * @throws com.ReadmillAPI.InvalidTokenException invalid token
     * @throws IllegalStateException if no refresh token present
     */
    Token refreshToken() throws IOException;

    /** Called to invalidate the current access token */
    void invalidateToken();

    /**
     * @param request resource to GET
     * @return the HTTP response
     * @throws IOException IO/Error
     */
    HttpResponse get(Request request) throws IOException;

    /**
     * @param request resource to POST
     * @return the HTTP response
     * @throws IOException IO/Error
     */
    HttpResponse post(Request request) throws IOException;

    /**
     * @param request resource to PUT
     * @return the HTTP response
     * @throws IOException IO/Error
     */
    HttpResponse put(Request request) throws IOException;

    /**
     * @param request resource to DELETE
     * @return the HTTP response
     * @throws IOException IO/Error
     */
    HttpResponse delete(Request request) throws IOException;

    /**
     * Resolve the given SoundCloud URI
     *
     * @param uri SoundCloud model URI, e.g. http://soundcloud.com/bob
     * @return the id or -1 if uri not found
     * @throws IOException network errors
     */
//    long resolve(String uri) throws IOException;

    /** @return the current token */
    Token getToken();

    /** @param token the token to be used */
    void setToken(Token token);

    /**
     * Registers a listener. The listener will be informed when an access token was found
     * to be invalid, and when the token had to be refreshed.
     * @param listener token listener
     */
    void addTokenStateListener(TokenStateListener listener);

    /**
     * Request login via authorization code
     * After login, control will go to the redirect URI (wrapper specific), with
     * one of the following query parameters appended:
     * <ul>
     * <li><code>code</code> in case of success, this will contain the code used for the
     *     <code>authorizationCode</code> call to obtain the access token.
     * <li><code>error</code> in case of failure, this contains an error code (most likely
     * <code>access_denied</code>).
     * </ul>
     * @param  options auth endpoint to use (leave out for default)
     * @return the URI to open in a browser/WebView etc.
     * @see ReadmillAPI#authorizationCode(String)
     */
    URI authorizationCodeUrl();

    /**
     * Interested in changes to the current token.
     */
    interface TokenStateListener {
        /**
         * Called when token was found to be invalid
         * @param token the invalid token
         */
        void onTokenInvalid(Token token);

        /**
         * Called when the token got successfully refreshed
         * @param token      the refreshed token
         */
        void onTokenRefreshed(Token token);
    }

    /**
     * Thrown when token is not valid.
     */
    class InvalidTokenException extends IOException {
        private static final long serialVersionUID = 1954919760451539868L;

        /**
         * @param code the HTTP error code
         * @param status the HTTP status, or other error message
         */
        public InvalidTokenException(int code, String status) {
            super("HTTP error:" + code + " (" + status + ")");
        }
    }
}
