package il.ac.huji.hujime;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Web Session to Huji
 * 
 * @author alonaba
 * 
 */
public class HujiSession {

	private final String _main_huji = "www.huji.ac.il";
	private final String _main_huji_ssl = "https://www.huji.ac.il";
	private final String _auth_enter_code = "enter=+%EB%F0%E9%F1%E4+";
	private final String _huji_personal_data = "https://www.huji.ac.il/dataj/controller/student/?";
	private final String _huji_captcha = "https://www.huji.ac.il/dataj/resources/captcha/student";
	private final String _huji_authentication = "https://www.huji.ac.il/dataj/controller/auth/student/?";

	public enum Method {
		Get, Post
	}

	private static HujiSession _instance = null;
	private DefaultHttpClient _client;
	private int _error;
	private String _errorMessage;
	private String _session;

	/**
	 * Static method to get instance of HujiSession
	 * 
	 * @return
	 */
	public static HujiSession getInstance() {
		if (_instance == null) {
			_instance = new HujiSession();
		}
		return _instance;
	}

	/**
	 * Constructor
	 */
	private HujiSession() {
		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));

		// parameters for session
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		// set timeout
		int timeoutConnection = 3000;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		_client = new DefaultHttpClient();
		_session = null;
	}

	/**
	 * Get the error number
	 * 
	 * @return error number
	 */
	// 0 - no error, 1 - no response, 2 - illegal code in response, 3 - error in
	// protocol, 4 - error in URL
	// 5 - input/output error, 6 - general error
	public int getError() {
		int error = _error;
		_error = 0;
		return error;
	}

	/**
	 * Get session ID of huji secure connection
	 * 
	 * @return
	 */
	public String getSession() {
		return _session;
	}

	/**
	 * Method that checks Internet access to huji site
	 * 
	 * @return true, if there is connection
	 */
	public boolean checkInternetAccess() {
		HttpResponse response;
		try {
			HttpGet method = new HttpGet(new URI(_main_huji_ssl));
			response = _client.execute(method);
			if (response == null) {
				return false;
			} else {
				// close connection
				response.getEntity().consumeContent();
				return true;
			}
		} catch (URISyntaxException e) {
			return false;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get captcha
	 * 
	 * @return Bitmap of captcha
	 */
	public Bitmap getCaptcha() {
		try {
			HttpResponse response = sendGet(_huji_personal_data);
			if (response == null) {
				_error = 1;
				return null;
			}
			// status must be 200
			else if (response.getStatusLine().getStatusCode() != 200) {
				_error = 2;
				return null;
			}
			response.getEntity().consumeContent();
			return getImage(_huji_captcha);
		} catch (ClientProtocolException e) {
			return null;
		} catch (URISyntaxException e) {
			_error = 4;
			return null;
		} catch (IOException e) {
			_error = 5;
			return null;
		} catch (Exception e) {
			_error = 6;
			return null;
		}

	}

	/**
	 * Method that authenticate user with id, password and captcha
	 * 
	 * @param id
	 *            - user id
	 * @param code
	 *            - password
	 * @param captcha
	 *            - the captcha
	 * @return true' if user authenticated, false otherwise
	 */
	public boolean authenticate(String id, String code, String captcha) {
		HttpResponse response;
		HttpPost method;
		try {
			method = new HttpPost(new URI(_huji_authentication));
			method.addHeader("Referer", _huji_personal_data);
			setRequestOptions(method);
			// disable redirect
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false);
			method.setParams(params);
			// set Post parameters
			Query postQuery = new Query(Query.Argument.AUTH_ID, id);
			postQuery.addArguments(Query.Argument.AUTH_PASS, code);
			postQuery.addArguments(Query.Argument.AUTH_CAPTCHA, captcha);
			postQuery.addArguments(_auth_enter_code);
			String parameters = postQuery.getQuery();
			method.setEntity(new StringEntity(parameters));
			response = _client.execute(method);
			// there must be redirect with session code
			if (response.getStatusLine().getStatusCode() == 302) {
				response.getEntity().consumeContent();
				_session = response.getLastHeader("Location").getValue();
				if (_session != null) {
					_session = _session.substring(0, _session.length() - 1);
				}
				return true;
			} else {
				setLastMesage(EntityUtils.toString(response.getEntity()),
						"font", "color", "red");
				response.getEntity().consumeContent();
				return false;
			}
		} catch (URISyntaxException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method that gets image from internet
	 * 
	 * @param URL
	 *            - the url of image
	 * @return Bitmap of image
	 */
	public Bitmap getImage(String URL) {
		try {
			HttpResponse response = sendGet(URL);
			if (response.getStatusLine().getStatusCode() != 200) {
				_error = 2;
				return null;
			}
			InputStream data = response.getEntity().getContent();
			Bitmap captcha = BitmapFactory.decodeStream(data);
			data.close();
			return captcha;
		} catch (ClientProtocolException e) {
			return null;
		} catch (URISyntaxException e) {
			_error = 4;
			return null;
		} catch (IOException e) {
			_error = 5;
			return null;
		} catch (Exception e) {
			_error = 6;
			return null;
		}
	}

	/**
	 * Set option to https request are required to https connection to personal
	 * information
	 * 
	 * @param method
	 *            - method to define the options to
	 */
	private void setRequestOptions(HttpMessage method) {
		method.addHeader("Origin", _main_huji_ssl);
		method.addHeader("Host", _main_huji);
		method.addHeader("Content-Type", "application/x-www-form-urlencoded");
		method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		method.addHeader("Accept-Language",
				"he-IL,he;q=0.8,en-US;q=0.6,en;q=0.4");
	}

	/**
	 * Send Get request to URL
	 * 
	 * @param url
	 *            - url to send request to
	 * @return - Http Response from server
	 * @throws URISyntaxException
	 *             - if URL illegal
	 * @throws ClientProtocolException
	 *             - if there is problem in client
	 * @throws IOException
	 *             - general exception
	 */
	private HttpResponse sendGet(String url) throws URISyntaxException,
			ClientProtocolException, IOException {
		HttpGet method = new HttpGet(new URI(url));
		if (url.contains("studean.huji.ac.il")) {
			method.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			method.addHeader("Accept-Language",
					"he-IL,he;q=0.8,en-US;q=0.6,en;q=0.4");
		} else if (!url.equals(_huji_personal_data)) {
			setRequestOptions(method);
		}
		if (url.equals(_huji_captcha)) {
			method.addHeader("Referer", _huji_personal_data);
		}
		HttpResponse response = _client.execute(method);
		return response;
	}

	/**
	 * Send Post request to URL
	 * 
	 * @param URL
	 *            - the URL
	 * @param parameters
	 *            - parameters to add to body of request
	 * @return Http response from server
	 * @throws URISyntaxException
	 *             - if URL illegal
	 * @throws ClientProtocolException
	 *             - if there is problem in client
	 * @throws IOException
	 *             - general exception
	 */
	private HttpResponse sendPost(String URL, String parameters)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpPost method = new HttpPost(new URI(URL));
		setRequestOptions(method);
		method.setEntity(new StringEntity(parameters));
		HttpResponse response = _client.execute(method);
		return response;
	}

	/**
	 * Get secure html page
	 * 
	 * @param method
	 *            - the method to use
	 * @param URL
	 *            - the url
	 * @param postParams
	 *            - parameters for Post
	 * @return String of response body
	 */
	public String getSecureHtml(Method method, String URL, String postParams) {
		HttpResponse response = null;
		HttpEntity responseEntity;
		String htmlPage;
		switch (method) {
		case Get:
			try {
				response = sendGet(URL);
			} catch (ClientProtocolException e) {
				return null;
			} catch (URISyntaxException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
			break;
		case Post:
			try {
				response = sendPost(URL, postParams);
			} catch (ClientProtocolException e1) {
				return null;
			} catch (URISyntaxException e1) {
				return null;
			} catch (IOException e1) {
				return null;
			}
		}
		if (response != null) {
			responseEntity = response.getEntity();
			try {
				htmlPage = EntityUtils.toString(responseEntity);
				responseEntity.consumeContent();
				return htmlPage;
			} catch (ParseException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * Get NonSecurePage with get method
	 * 
	 * @param URL
	 *            - the URL
	 * @return String of response body
	 */
	public String getNonSecurePage(String URL) {
		try {
			HttpGet method = new HttpGet(new URI(URL));
			method.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			method.addHeader("Accept-Language",
					"he-IL,he;q=0.8,en-US;q=0.6,en;q=0.4");
			HttpResponse response = _client.execute(method);
			HttpEntity responseEntity = response.getEntity();
			String htmlPage = EntityUtils.toString(responseEntity);
			responseEntity.consumeContent();
			return htmlPage;

		} catch (URISyntaxException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Get NonSecurePage with post method
	 * 
	 * @param URL
	 *            - the URL
	 * @param postParams
	 *            - parameters for post request
	 * @return String of response body
	 */
	public String getNonSecurePage(String URL, String postParams) {
		try {
			HttpPost method = new HttpPost(new URI(URL));
			method.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			method.addHeader("Accept-Language",
					"he-IL,he;q=0.8,en-US;q=0.6,en;q=0.4");
			method.setEntity(new StringEntity(postParams));
			HttpResponse response = _client.execute(method);
			HttpEntity responseEntity = response.getEntity();
			String htmlPage = EntityUtils.toString(responseEntity);
			responseEntity.consumeContent();
			return htmlPage;

		} catch (URISyntaxException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Get html error message received
	 * 
	 * @param htmlPage
	 *            - the page to retrieve error from
	 * @param tag
	 *            - html tags that above message
	 * @param attribute
	 *            - the attribute
	 * @param value
	 *            - the value in attribute
	 */
	private void setLastMesage(String htmlPage, String tag, String attribute,
			String value) {
		_errorMessage = null;
		Whitelist whitelist = Whitelist.none();
		whitelist.addTags(tag);
		whitelist.addAttributes(tag, attribute);
		htmlPage = Jsoup.clean(htmlPage, whitelist);
		Document document = Jsoup.parse(htmlPage);
		Elements messages = document.select(tag + "[" + attribute + "=" + value
				+ "]");
		if (messages.size() > 0) {
			_errorMessage = messages.first().text();
		} else {
		}
	}

	/**
	 * Get Last error message recieved
	 * 
	 * @return message
	 */
	public String getLastMessage() {
		return _errorMessage;
	}
}
