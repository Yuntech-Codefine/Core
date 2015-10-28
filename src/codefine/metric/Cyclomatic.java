package codefine.metric;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

class MethodModel {
	String name;
	int cc;
	
	MethodModel(String name) {
		this.name = name;
	}
}

class ClassModel {
	private ArrayList<MethodModel> methodList;
	String className;
	
	ClassModel () {
		methodList = new ArrayList<MethodModel>();
	}
	
	void addMethod(MethodModel mm) {
		methodList.add(mm);
	}
	
	int size() {
		return methodList.size();
	}
	
	MethodModel getMethod(int index) {
		return methodList.get(index);
	}
	
	MethodModel getLast() {
		if(methodList.size() > 0)
			return methodList.get(methodList.size() - 1);
		else
			return null;
	}
}

public class Cyclomatic extends Algorithm {
	
	private int value;
	private int brackets = 0;
	private String combinedLine = "";
	private int lastIndex;
	private boolean isQuotes;
	private ArrayList<ClassModel> classList;
	
	String[] operators = { // 順序很重要，已排好順序
		"++", "+", "--", "-", "*", "/", "%", // 算數運算子
		"&&", "||", "&", "|", "!", "?", "^", ":", // 邏輯運算子
		"=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=", "<<=", ">>=", ">>>=", // 指派運算子
		"~", "<<", ">>", ">>>", // 位元運算子
		"<", ">", "<=", ">=", "==", "!=", "instanceof", // 關係運算子
	};
	String[] SP = { // 其他特殊符號
		",", ".", ";", "@",
		"\\", "/*", "*/", "\"",
		"(", ")", "[", "]", "{", "}"
	};
	String[] keywords = {"if", "for", "while", "case", "default", "continue", "&&", "||", "&", "|" };
	
	public Cyclomatic() {
		classList = new ArrayList<ClassModel>();
	}
	
	public void readLine(String line) {
		isQuotes = false;
		
		if(!line.equals("")) {
			if(line.contains("//")) // 遇到註解
               line = line.substring(0, line.indexOf("//"));
			
			line = line.replace("\t", ""); // 取代定位點
			if(line.trim().equals("")) return; // 若沒東西了就跳下一行
			
			if(brackets <= 2) {
				combinedLine += line;
			} else {
				combinedLine = line;
				lastIndex = 0;
			}
			
			int i;
			boolean isNotMethod = false;
			
			for(i = lastIndex; i < combinedLine.length(); i++) {
				if(combinedLine.charAt(i) == '{') {
					
					int lastEnding = combinedLine.substring(0, i).lastIndexOf(';');
					
					if(lastEnding < 0) {
						if(!combinedLine.substring(combinedLine.lastIndexOf('{'), i).contains("="))
							brackets++;
						else
							isNotMethod = true;
					} else {
						if(!combinedLine.substring(lastEnding, i).contains("="))
							brackets++;
						else
							isNotMethod = true;
					}
					
					if(brackets == 1) {
						ClassModel classModel = new ClassModel();
						classModel.className = searchClassName(combinedLine, i);
						classList.add(classModel);
					} else if(brackets == 2) {
						classList.get(classList.size() - 1).addMethod(new MethodModel(searchMethodName(combinedLine, i)));
					}
				} else if(combinedLine.charAt(i) == '}') {
					if(!isNotMethod)
						brackets--;
					isNotMethod = false;
				}
				
				if(combinedLine.charAt(i) == '"') isQuotes = !isQuotes;
				
				if(brackets >= 2) {
					for(String key : keywords) {
						i = keyCount(combinedLine, key, i);
						classList.get(classList.size() - 1).getLast().cc = value;
						value = 1; // cc從1開始加計
					}
				}
			}
			lastIndex = i;
		}
	}
	
	private int keyCount(String line, String key, int currentIndex) {
		int	index;
		
		for(index = 0; index < key.length(); index++) {
			if(currentIndex + index >= line.length()) break;
			if(line.charAt(currentIndex + index) != key.charAt(index)) break;
		}
		
		if(index >= key.length()) {
			switch(key) {
				case "if":
				case "for":
				case "while":
				case "case":
				case "default":
				default:
					if(!isQuotes) value++;
					break;
			}
			return currentIndex + index;
		} else {
			return currentIndex;
		}
	}
	
	private String searchClassName(String line, int index) {
		String result = "";
		int ci = line.substring(0, index).lastIndexOf("class");
		
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
	
	private String searchMethodName(String line, int index) {
		int ci = line.substring(0, index).lastIndexOf("(");
		int ei = line.substring(0, index).lastIndexOf(")");
		int countP, i1, i2;
		String param = line.substring(ci + 1, ei).replace(",", ", "); // 擷取括號內的東西，且避免全黏在一起
		String paramKeys = "";
		String result = "";
		
		// 處理()括號內的部分
		do {
			countP = 0;
			i1 = 0; i2 = 0;
			for(int k = 0; k < param.length(); k++) { // 抓出目前 param 左邊數來的第一對<>
				switch(param.charAt(k)) {
					case '<':
						if(countP == 0) {
							i1 = k;
						}
						countP += 1;
						break;
					case '>':
						countP -= 1;
						if(countP == 0) {
							i2 = k;
							k = param.length(); // 提前結束for迴圈
						}
						break;
					case ',':
						if(i1 <= i2) { // 確定是不是<>中的逗號
							i1 = param.trim().indexOf(" ");
							i2 = i1;
							k = param.length();
						}
						break;
				}
			}
			
			if(param.substring(i2).contains(",")) { // 後面還有參數
				paramKeys += param.substring(0, i1).trim() + formatParam(param.substring(i1, i2 + 1)) + ", ";
				param = param.substring(param.substring(i2).indexOf(",") + i2 + 1);
			} else if (i1 != 0 && i2 >= i1){ // 找到最後一個參數了
				paramKeys += param.substring(0, i1).trim() + formatParam(param.substring(i1, i2 + 1));
				param = param.substring(i2 + 1).trim();
			}
			
		} while(i1 != 0 && i2 != 0);
		
		// 處理括號前面的部分
		int j = ci - 1;
		if(line.charAt(j) == ' ')
			while(line.charAt(j - 1) == ' ') j--;
		else
			j++;
		
		int tmp = line.substring(0, index).lastIndexOf("{");
		while(line.charAt(tmp + 1) == ' ') tmp++;
		String mAttrs[] = line.substring(0, index).substring(tmp + 1, j).split(" +");
		for(String attr : mAttrs)
			result += attr + " ";
		
		result += "(" + paramKeys + ")";
		
		return result;
	}
	
	private String formatParam(String param) { //回傳格式化後的泛型部分
		
		return param.replace(" ", "").replace(",", ", ");
	}
	
	private boolean isDigitChar(char key) {
		if(key > '0' && key < '9')
			return true;
		else
			return false;
	}
	
    public String getLevel(int value) {
        if (value > 50) {
        	return "Most complex and highly unstable method";
        } else if (value >= 21 && value <= 50) {
        	return "High risk";
        } else if (value >= 11 && value <= 20) {
            return "Moderate risk";
        } else {
            return "Low risk program";
        }
    }
    
	public String getValue() {
		JSONObject jsonClass = new JSONObject();
		
		for(int i = 0; i < classList.size(); i++) {
			
			JSONArray jsonMethods = new JSONArray();
			for(int j = 0; j < classList.get(i).size(); j++) {
				JSONObject jsonMethod = new JSONObject();
				int cc = classList.get(i).getMethod(j).cc;
				
				jsonMethod.put("Method Name", classList.get(i).getMethod(j).name);
				jsonMethod.put("Cyclomatic Complexity", cc);
				jsonMethod.put("Risk Level", getLevel(cc));
				jsonMethods.put(jsonMethod);
			}
			jsonClass.put(classList.get(i).className, jsonMethods);
		}	
		
		return jsonClass.toString();
	}
}
