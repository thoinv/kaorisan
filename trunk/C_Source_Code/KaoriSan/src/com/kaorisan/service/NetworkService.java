package com.kaorisan.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;

public class NetworkService {

//	private static String DOMAIN = "https://www.kaorisan.com/";
	
	private static String DOMAIN = "http://dev.kaorisan.com/";

	private static String API_GET_TOKEN_LOGIN_FACEBOOK = DOMAIN + "api/v1/auth/facebook.json?";
 
	private static String API_GET_TOKEN_LOGIN_GOOGLE = DOMAIN + "api/v1/auth/google.json?";

	private static String API_GET_LIST_TASK = DOMAIN + "api/v1/chores.json?";

	private static String API_GET_ACCOUNT_DASHBOARD = DOMAIN + "api/v1/users/account.json?";

	private static String API_GET_DASHBOARD = DOMAIN + "api/v1/users/dashboard.json?";

	private static String API_CREATE_TASK = DOMAIN + "api/v1/chores/create.json";

	private static String API_CREATE_PENDING_TASK = DOMAIN + "api/v1/chores/pending.json";

	private static String API_ACTIVE_PENDING_TASK = DOMAIN + "api/v1/chores/14/activate.json";

	private static String API_PUSH_TOKEN = DOMAIN + "api/v1/users/push_token.json";

	private static final String API_GET_RECOMMEND_TASK = DOMAIN + "api/v1/chores/recommended.json?";
	
	private static final String API_GOOGLE_TOKEN = "https://accounts.google.com/o/oauth2/token";
	
	private static final String API_LOGOUT = DOMAIN + "/api/v1/users/logout.json";

	// private final String API_PUSH_TOKEN_PARAM_FORMAT =
	// "?token={0}&platform={1}&push_token={2}";

	public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, 
			KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// ConnManagerParams.setTimeout(params, 10000);
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	// private HttpClient createHttpClient()
	// {
	// HttpParams params = new BasicHttpParams();
	// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	// HttpProtocolParams.setContentCharset(params,
	// HTTP.DEFAULT_CONTENT_CHARSET);
	// HttpProtocolParams.setUseExpectContinue(params, true);
	//
	// HttpConnectionParams .setConnectionTimeout(params,20000);
	// HttpConnectionParams.setSoTimeout(params, 20000);
	// SchemeRegistry schReg = new SchemeRegistry();
	// schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),
	// 80));
	// schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),
	// 443));
	// ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,
	// schReg);
	//
	// return new DefaultHttpClient(conMgr, params);
	// }
	private String getJson(HttpClient httpClient, String url, List<NameValuePair> listNameValuePair) {
	
		StringBuilder sb = new StringBuilder();
		try {
			HttpGet getRequest = new HttpGet(url + URLEncodedUtils.format(listNameValuePair, "utf-8"));
			getRequest.addHeader("Accept", "application/json");
			// postRequest.setHeader("Content-type", "application/json");
			DebugLog.logd("Url get: " + getRequest.getURI().toURL());
			HttpResponse response = httpClient.execute(getRequest);
			StatusLine status = response.getStatusLine();
			
			String output;
			if (status.getStatusCode() == HttpStatus.SC_OK) {
				
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), "utf-8"));
				while ((output = br.readLine()) != null) {
					sb.append(output + "\n");
				}
				br.close();
				
			} else {
				response.getEntity().getContent().close();
				throw new IOException(status.getReasonPhrase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String postJson(HttpClient httpClient, String url, List<NameValuePair> listNameValuePair) {
		StringBuilder sb = new StringBuilder();
		try {
			HttpPost postRequest = new HttpPost(url);
			postRequest.setEntity(new UrlEncodedFormEntity(listNameValuePair, "utf-8"));
			postRequest.addHeader("Accept", "application/json");
			// postRequest.setHeader("Content-type", "application/json");
			DebugLog.logd("Url get token : " + postRequest.getURI().toURL());
			HttpResponse response = httpClient.execute(postRequest);

			String output;
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpClient.getConnectionManager().shutdown();
		return sb.toString();
	}

	private JSONObject readJsonFromUrlGet(String url, List<NameValuePair> listNameValuePair) throws IOException, JSONException {
		try {
			HttpClient httpClient = getNewHttpClient();
			String jsonText = getJson(httpClient, url, listNameValuePair);
			DebugLog.logd("Read Json: " + jsonText);
			JSONObject json = new JSONObject(jsonText);
			DebugLog.logd("Read Json: " + json.toString());
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject readJsonFromUrlPost(String url, List<NameValuePair> listNameValuePair) throws IOException, JSONException {
		try {
			HttpClient httpClient = getNewHttpClient();
			String jsonText = postJson(httpClient, url, listNameValuePair);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getTokenKaorisanFromLoginFaceBook(String fbtoken, String fbexpiration) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("fb_access_token", fbtoken));
			listNameValuePair.add(new BasicNameValuePair("fb_expiration_date", fbexpiration));
			jsonResult = readJsonFromUrlGet(API_GET_TOKEN_LOGIN_FACEBOOK, listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Get Token RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject refreshAccessTokenGoogle(String clientId, String clientSecret, String refreshToken) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("client_id", clientId));
			listNameValuePair.add(new BasicNameValuePair("client_secret", clientSecret));
			listNameValuePair.add(new BasicNameValuePair("refresh_token", refreshToken));
			listNameValuePair.add(new BasicNameValuePair("grant_type", "refresh_token"));
			jsonResult = readJsonFromUrlPost(API_GOOGLE_TOKEN, listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Refresh Token RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject getTokenKaorisanFromLoginGoogle(String gpToken, String gpRefreshToken, String gpExpiresDate) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("gp_access_token", gpToken));
			listNameValuePair.add(new BasicNameValuePair("gp_refresh_token", gpRefreshToken));
			listNameValuePair.add(new BasicNameValuePair("gp_expires_in", gpExpiresDate));
			jsonResult = readJsonFromUrlGet(API_GET_TOKEN_LOGIN_GOOGLE, listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Get Token RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	public JSONArray getListTask(String token, String page, String count) {
		JSONArray jsonArray = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("sort", "updated_at"));
			// listNameValuePair.add(new BasicNameValuePair("sort", "id"));
			listNameValuePair.add(new BasicNameValuePair("order", "desc"));
			listNameValuePair.add(new BasicNameValuePair("page", page));
			listNameValuePair.add(new BasicNameValuePair("count", count));

			HttpClient httpClient = getNewHttpClient();
			String jsonText = getJson(httpClient, API_GET_LIST_TASK, listNameValuePair);
			//DebugLog.logd("String listtask: " + jsonText);
			jsonArray = new JSONArray(jsonText);
			// jsonText = jsonText.substring(1, jsonText.length()-2);
			// jsonResult = new JSONObject(jsonText);
			// jsonResult = readJsonFromUrlGet(API_GET_LIST_TASK,
			// listNameValuePair);
			if (jsonArray != null) {
				DebugLog.logd("Get List Task RESULT : " + jsonArray.toString());
			} else {
				DebugLog.logd("data list task return null");
			}
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray getRecommendTasks(String token, String count) {
		JSONArray jsonArray = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("count", count));
			HttpClient httpClient = getNewHttpClient();
			String jsonText = getJson(httpClient, API_GET_RECOMMEND_TASK, listNameValuePair);
			DebugLog.logd("String list recommend task: " + jsonText);
			jsonArray = new JSONArray(jsonText);
			DebugLog.logd("Get List Task Recommend RESULT : " + jsonArray.toString());
			
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getAccountDashboard(String token) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			jsonResult = readJsonFromUrlGet(API_GET_ACCOUNT_DASHBOARD, listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Get Account DashBoard RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getDashBoard(String token) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			jsonResult = readJsonFromUrlGet(API_GET_DASHBOARD, listNameValuePair);
			// if (jsonResult != null) {
			DebugLog.logd("Get DashBoard RESULT : " + jsonResult.toString());
			// } else {
			// DebugLog.loge("data return null");
			// }
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getTaskDetail(String id, String token) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			jsonResult = readJsonFromUrlGet(DOMAIN + "api/v1/chores/" + id + ".json?", listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Get Task Detail RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject createTask(String token, String title, String request,String isVip) {
		JSONObject jsonResult = null;

		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("title", title));
			listNameValuePair.add(new BasicNameValuePair("request", request));
			listNameValuePair.add(new BasicNameValuePair("is_vip", isVip));
			jsonResult = readJsonFromUrlPost(API_CREATE_TASK, listNameValuePair);

			if (jsonResult != null) {
				DebugLog.logd("CreatTask RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}
	
	public JSONObject logout(String token, String platform, String pushToken){
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("platform", platform));
			listNameValuePair.add(new BasicNameValuePair("push_token", pushToken));
			jsonResult = readJsonFromUrlPost(API_LOGOUT, listNameValuePair);
			
			if (jsonResult != null) {
				DebugLog.logd("Logout RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}
	public JSONObject createPendingTask(String token) {
		JSONObject jsonResult = null;

		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			jsonResult = readJsonFromUrlPost(API_CREATE_PENDING_TASK, listNameValuePair);

			if (jsonResult != null) {
				DebugLog.logd("CreatPendingTask RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	public JSONObject activePendingTask(String token) {

		JSONObject jsonResult = null;

		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			jsonResult = readJsonFromUrlPost(API_ACTIVE_PENDING_TASK, listNameValuePair);

			if (jsonResult != null) {
				DebugLog.logd("ActivePendingTask RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	public JSONObject rateTask(String token, String id, String rate) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("rate", rate));
			Log.i("Token and rate", token + " " + rate);
			jsonResult = readJsonFromUrlPost(DOMAIN + "api/v1/chores/" + id + "/rate.json", listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Get Task Detail RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	// Function true
	public JSONObject uploadPhotoToTask(String token, File imagePath, String id) {
		String execute = "";
		HttpClient httpClient;
		try {
			httpClient = getNewHttpClient();
			HttpPost httppost = new HttpPost(DOMAIN + "api/v1/chores/" + id + "/picture.json");

			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart("token", new StringBody(token));
			multipartEntity.addPart("file", new FileBody(imagePath));
			httppost.setEntity(multipartEntity);

			execute = httpClient.execute(httppost, new ResponseHandler<String>() {

						@Override
						public String handleResponse(HttpResponse response)
								throws ClientProtocolException, IOException {
							HttpEntity r_entity = response.getEntity();
							String responseString = EntityUtils.toString(r_entity);
							return responseString;
						}
					});
			DebugLog.logd("upload image to Task : " + execute);
			return new JSONObject(execute);
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	public JSONObject pushToken(String token, String platform, String pushToken) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("platform", platform));
			listNameValuePair.add(new BasicNameValuePair("push_token", pushToken));
			jsonResult = readJsonFromUrlPost(API_PUSH_TOKEN, listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("Regiter GCM RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	public JSONObject createNewRepLy(String token, String body, String id) {
		JSONObject jsonResult = null;
		try {
			List<NameValuePair> listNameValuePair = new ArrayList<NameValuePair>();
			listNameValuePair.add(new BasicNameValuePair("token", token));
			listNameValuePair.add(new BasicNameValuePair("body", body));
			jsonResult = readJsonFromUrlPost(DOMAIN + "api/v1/chores/" + id + "/reply.json", listNameValuePair);
			if (jsonResult != null) {
				DebugLog.logd("CreatTask RESULT : " + jsonResult.toString());
			} else {
				DebugLog.logd("data return null");
			}
			return jsonResult;
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
		}
		return null;
	}

	public JSONObject uploadAudioToTask(String token, String id, File audioPath) {
		String execute = "";
		HttpClient httpClient;
		try {
			httpClient = getNewHttpClient();
			HttpPost httppost = new HttpPost(DOMAIN + "api/v1/chores/" + id + "/audio.json");

			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			multipartEntity.addPart("token", new StringBody(token));
			multipartEntity.addPart("file", new FileBody(audioPath));
			httppost.setEntity(multipartEntity);

			execute = httpClient.execute(httppost, new ResponseHandler<String>() {

						@Override
						public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
							HttpEntity r_entity = response.getEntity();
							String responseString = EntityUtils.toString(r_entity);
							return responseString;
						}
					});
			DebugLog.logd("Audio : " + execute);
			return new JSONObject(execute);
		} catch (Exception e) {
			DebugLog.loge("EXCEPTION " + e.getMessage());
			Utils.dismissCurrentProgressDialog();
		}
		return null;
	}
}
