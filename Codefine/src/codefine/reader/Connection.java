package codefine.reader;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


public class Connection {
	private URLConnection urlConnection = null;
	
	public Connection(URL url) throws IOException {
		urlConnection = url.openConnection();
		urlConnection.setConnectTimeout(10000);
		urlConnection.setReadTimeout(10000);
	}
	
	public URLConnection getConnection() {
		return urlConnection;
	}
}