package codefine.metric;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

class Keys {
	private String className;
	private int loc;
	
	public void setClassName(String className) {
		this.className = className;
	}
	public void setLOC(int loc) {
		this.loc = loc;
	}
	public String getClassName() {
		return className;
	}
	public int getLOC() {
		return loc;
	}
}

public class SLOC extends Algorithm {
	ArrayList<Keys> Results = new ArrayList<Keys>(); 
	private int value = 0;
	private int brackets = 0;
	
	public void readLine(String line) {
		line = line.replaceAll("\\n|\\t|\\s", "");
		if((!line.equals("")) && (!line.startsWith("//"))) {
			value++;
		}
		
		for(int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == '{') {
				brackets++;
				
				if(brackets == 1) {
					Keys LOC = new Keys();
					LOC.setClassName(searchClassName(line, i));
					Results.add(LOC);
				}
			} else if(line.charAt(i) == '}') {
				brackets--;
				
				if(brackets == 0) {
					Results.get(Results.size() - 1).setLOC(value);
					value = 0;
				}
			}
		}
	}
	
	public String getValue() {
		JSONArray jsonArr = new JSONArray();
		
		for(int i = 0; i < Results.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("Class Name", Results.get(i).getClassName());
			jsonObj.put("LOC", Results.get(i).getLOC());
			
			jsonArr.put(jsonObj);
		}
		
		return jsonArr.toString();
	}
	
	private String searchClassName(String line, int index) {
		String result = "";
		
		int ci = line.substring(0, index).lastIndexOf("class ");
		if(ci >= 0) {
			int ei = line.substring(ci, index).indexOf("<");
			int si = line.substring(ci, index).indexOf(">");
			
			if(si >= 0) {
				int tmp = ei - 1;
				while(line.substring(ci, index).charAt(tmp) == ' ') tmp--;
				result = line.substring(ci + 6, ci + tmp + 1) + "<" + formatParam(line.substring(ci, index).substring(ei + 1, si)) + ">";
			} else {
				int ti = line.substring(ci + 6, index).indexOf(" ");
				if(ti >= 0)
					result = line.substring(ci + 6, ci + 6 + ti);
				else
					result = line.substring(ci + 6, index);
			}
		}
		
		return result;
	}
	
	private String formatParam(String param) { //回傳格式化後的泛型部分
		
		return param.replace(" ", "").replace(",", ", ");
	}
}
