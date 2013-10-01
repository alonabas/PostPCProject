package il.ac.huji.hujime;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewConfiguration;

public class StaticMethods {

	public static boolean checkInternetAccess(String url) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpResponse response;
		try {
			HttpGet method = new HttpGet(new URI(url));
			response = client.execute(method);
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

	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void getOverflowMenu(Context context) {
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
