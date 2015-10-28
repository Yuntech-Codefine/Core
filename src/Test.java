import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import codefine.metric.Cyclomatic;
import codefine.metric.Halstead;

public class Test {
	public static void main(String[] args) {
		
		//Reader reader = new Reader();
		Halstead halstead = new Halstead();
		Cyclomatic cyclomatic = new Cyclomatic();
		
		try {
			//URL url = new URL("http://codefine-yuntech.rhcloud.com/uploads/2015081211412096kp.java");
			//http://codefine-yuntech.rhcloud.com/uploads/20150911122659g698.java
			//Connection connection = new Connection(url);
			//reader.read(connection);
			
			FileInputStream fis = new FileInputStream("t.java");
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
			
			String line;
			
			while ((line = bufferedReader.readLine()) != null)
			{
				cyclomatic.readLine(line);
				halstead.readLine(line);
			}
			
			bufferedReader.close();
			System.out.println(halstead.getValue());
			System.out.println(cyclomatic.getValue());
			//Reader reader = new Reader();
			//reader.read(connection);
			//System.out.println(reader.getCyclomatic());
			
		//} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
