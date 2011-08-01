package com.readmill.api;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRequestDirector;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;

public class ApiWrapper implements ReadmillAPI, Serializable {
	private static final long serialVersionUID = 8095813939211459249L;

	/** The current environment */
	public final Env env;

	private Token mToken;
	public final String mClientId, mClientSecret;
	private final URI mRedirectUri;
	transient private HttpClient httpClient;
	transient private Set<TokenStateListener> listeners;

	/**
	 * Constructs a new ApiWrapper instance.
	 */
	public ApiWrapper(String clientId, String clientSecret, URI redirectUri,
			Token token, Env env) {
		mClientId = clientId;
		mClientSecret = clientSecret;
		mRedirectUri = redirectUri;
		mToken = token == null ? new Token(null, null) : token;
		this.env = env;
	}

	@Override
	public Token authorizationCode(String code) throws IOException {
		if (code == null) {
			throw new IllegalArgumentException("need a code");
		}

		mToken = requestToken(Request.to(Endpoints.TOKEN).with("grant_type",
				AUTHORIZATION_CODE, "client_id", mClientId, "client_secret",
				mClientSecret, "redirect_uri", mRedirectUri, "code", code));
		return mToken;
	}

	@Override
	public Token refreshToken() throws IOException {
		if (mToken == null || mToken.refresh == null)
			throw new IllegalStateException("no refresh token available");
		mToken = requestToken(Request.to(Endpoints.TOKEN).with("grant_type",
				REFRESH_TOKEN, "client_id", mClientId, "client_secret",
				mClientSecret, "refresh_token", mToken.refresh));
		return mToken;
	}

	@Override
	public void invalidateToken() {
		if (mToken != null) {
			mToken.invalidate();
			if (listeners != null) {
				for (TokenStateListener l : listeners)
					l.onTokenInvalid(mToken);
			}
		}
	}

	@Override
	public URI authorizationCodeUrl() {
		return getURI(
				Request.to(Endpoints.AUTHORIZE).with("redirect_uri",
						mRedirectUri, "client_id", mClientId, "response_type",
						"code", "mobile", "1"), false, false);
	}

	public URI getURI(Request request, boolean api, boolean secure) {
		return URI
				.create((api ? env.getApiHost(secure) : env.getWebHost(secure))
						.toURI()).resolve(request.toUrl());
	}

	protected Token requestToken(Request request) throws IOException {
		// TODO We should really, really, really do this over SSL =)
		HttpResponse response = getHttpClient().execute(env.webHost,
				request.buildRequest(HttpPost.class));
		final int status = response.getStatusLine().getStatusCode();

		if (status == HttpStatus.SC_OK) {
			final Token token = new Token(Http.getJSON(response));
			if (listeners != null) {
				for (TokenStateListener l : listeners)
					l.onTokenRefreshed(token);
			}
			return token;
		} else {
			String sError = new String();
			try {
				sError = Http.getString(response);
			} catch (IOException ignored) {
			}
			throw status == HttpStatus.SC_UNAUTHORIZED ? new InvalidTokenException(
					status, sError) : new IOException(status + " "
					+ response.getStatusLine().getReasonPhrase() + " ("
					+ sError + ")");
		}
	}

	protected HttpParams getParams() {
		return Http.defaultParams();
	}

	protected SocketFactory getSocketFactory() {
		return PlainSocketFactory.getSocketFactory();
	}

	protected SSLSocketFactory getSSLSocketFactory() {
		return SSLSocketFactory.getSocketFactory();
	}

	protected String getUserAgent() {
		return USER_AGENT;
	}

	/** @return The HttpClient instance used to make the calls */
	public HttpClient getHttpClient() {
		if (httpClient == null) {
			final HttpParams params = getParams();
			HttpClientParams.setRedirecting(params, false);
			HttpProtocolParams.setUserAgent(params, getUserAgent());

			final SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", getSocketFactory(), 80));
			final SSLSocketFactory sslFactory = getSSLSocketFactory();
			if (env == Env.STAGING) {
				// disable strict checks on sandbox XXX remove when certificate
				// is fixed
				sslFactory
						.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			}
			// registry.register(new Scheme("https", sslFactory, 443));
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
					params, registry), params) {
				{
					setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
						@Override
						public long getKeepAliveDuration(
								HttpResponse httpResponse,
								HttpContext httpContext) {
							return 20 * 1000; // milliseconds
						}
					});

					getCredentialsProvider().setCredentials(
							new AuthScope(AuthScope.ANY_HOST,
									AuthScope.ANY_PORT, ReadmillAPI.REALM,
									OAUTH_SCHEME),
							OAuth2Scheme.EmptyCredentials.INSTANCE);

					getAuthSchemes().register(ReadmillAPI.OAUTH_SCHEME,
							new OAuth2Scheme.Factory(ApiWrapper.this));
				}

				@Override
				protected HttpContext createHttpContext() {
					HttpContext ctxt = super.createHttpContext();
					ctxt.setAttribute(ClientContext.AUTH_SCHEME_PREF,
							Arrays.asList(ReadmillAPI.OAUTH_SCHEME, "digest",
									"basic"));
					return ctxt;
				}

				@Override
				protected BasicHttpProcessor createHttpProcessor() {
					BasicHttpProcessor processor = super.createHttpProcessor();
					processor
							.addInterceptor(new OAuth2HttpRequestInterceptor());
					return processor;
				}

				// for testability only
				@Override
				protected RequestDirector createClientRequestDirector(
						HttpRequestExecutor requestExec,
						ClientConnectionManager conman,
						ConnectionReuseStrategy reustrat,
						ConnectionKeepAliveStrategy kastrat,
						HttpRoutePlanner rouplan, HttpProcessor httpProcessor,
						HttpRequestRetryHandler retryHandler,
						RedirectHandler redirectHandler,
						AuthenticationHandler targetAuthHandler,
						AuthenticationHandler proxyAuthHandler,
						UserTokenHandler stateHandler, HttpParams params) {
					return getRequestDirector(requestExec, conman, reustrat,
							kastrat, rouplan, httpProcessor, retryHandler,
							redirectHandler, targetAuthHandler,
							proxyAuthHandler, stateHandler, params);
				}
			};
		}
		return httpClient;
	}

	@Override
	public HttpResponse get(Request request) throws IOException {
		return prepareAndExecute(request, HttpGet.class);
	}

	@Override
	public HttpResponse put(Request request) throws IOException {
		return prepareAndExecute(request, HttpPut.class);
	}

	@Override
	public HttpResponse post(Request request) throws IOException {
		return prepareAndExecute(request, HttpPost.class);
	}

	@Override
	public HttpResponse delete(Request request) throws IOException {
		return prepareAndExecute(request, HttpDelete.class);
	}

	public HttpResponse prepareAndExecute(Request request,
			Class<? extends HttpRequestBase> method) throws IOException {
		refreshTokenIfOld();
		ensureClientId(request);
		return execute(request.buildRequest(method));
	}

	// TODO Remove this hack when we have support for giving client id in the
	// headers
	private void ensureClientId(Request request) {
		if (!request.hasParam("client_id")) {
			request.add("client_id", mClientId);
		}
	}

	@Override
	public Token getToken() {
		return mToken;
	}

	@Override
	public void setToken(Token newToken) {
		mToken = newToken;
	}

	@Override
	public synchronized void addTokenStateListener(TokenStateListener listener) {
		if (listeners == null)
			listeners = new HashSet<TokenStateListener>();
		listeners.add(listener);
	}

	public HttpResponse execute(HttpRequest req) throws IOException {
		return getHttpClient().execute(env.apiSSLHost, addHeaders(req));
	}

	/** Creates an OAuth2 header for the given token */
	public static Header createOAuthHeader(Token token) {
		return new BasicHeader(AUTH.WWW_AUTH_RESP, "OAuth "
				+ (token == null || !token.valid() ? "invalidated"
						: token.access));
	}

	/** Adds an OAuth2 header to a given request */
	protected HttpRequest addAuthHeader(HttpRequest request) {
		if (!request.containsHeader(AUTH.WWW_AUTH_RESP)) {
			request.addHeader(createOAuthHeader(getToken()));
		}
		return request;
	}

	/** Forces JSON */
	protected HttpRequest addAcceptHeader(HttpRequest request) {
		if (!request.containsHeader("Accept")) {
			request.addHeader("Accept", "application/json");
		}
		return request;
	}

	/** Adds all required headers to the request */
	protected HttpRequest addHeaders(HttpRequest req) {
		System.out.println("ApiWrapper: addHeaders with token: "
				+ mToken.access);
		return addAcceptHeader(addAuthHeader(req));
	}

	/**
	 * Get a fresh token (refreshing the current one if necessary)
	 */
	public void refreshTokenIfOld() {
		if (mToken != null && mToken.isExpired()) {
			try {
				refreshToken();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method mainly exists to make the wrapper more testable. oh, apache's
	 * insanity.
	 */
	protected RequestDirector getRequestDirector(
			HttpRequestExecutor requestExec, ClientConnectionManager conman,
			ConnectionReuseStrategy reustrat,
			ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan,
			HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler,
			RedirectHandler redirectHandler,
			AuthenticationHandler targetAuthHandler,
			AuthenticationHandler proxyAuthHandler,
			UserTokenHandler stateHandler, HttpParams params) {
		return new DefaultRequestDirector(requestExec, conman, reustrat,
				kastrat, rouplan, httpProcessor, retryHandler, redirectHandler,
				targetAuthHandler, proxyAuthHandler, stateHandler, params);
	}

}
