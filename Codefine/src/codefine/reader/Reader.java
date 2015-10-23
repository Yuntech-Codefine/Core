package codefine.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.json.*;

import codefine.metric.Cyclomatic;
import codefine.metric.Halstead;
import codefine.metric.SLOC;


public class Reader {
	
	private SLOC sloc;
	private Cyclomatic cyclomatic;
	//private Halstead halstead;
	
	public Reader() {  
		sloc = new SLOC();
		cyclomatic = new Cyclomatic();
		//halstead = new Halstead();
	}
	
	public void read(Connection connection) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getConnection().getInputStream()));
			
			String line;
			
			while ((line = bufferedReader.readLine()) != null)
			{
				sloc.readLine(line);
				cyclomatic.readLine(line);
				//halstead.readLine(line);
			}
			
			bufferedReader.close();
			
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
