package codefine.metric;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

class HalsteadKeys {
	private String className;
	private HashMap<String, Integer> operators, operands;
	private int N1, N2, n1, n2;
	
	public HalsteadKeys(String cn, HashMap<String, Integer> operators, HashMap<String, Integer> operands, int N1, int N2, int n1, int n2) {
		className = cn;
		this.operators = operators;
		this.operands = operands;
		this.N1 = N1;
		this.N2 = N2;
		this.n1 = n1;
		this.n2 = n2;
	}
	public String getClassName() {
		return className;
	}
	public HashMap<String, Integer> getOperators() {
		return operators;
	}
	public HashMap<String, Integer> getOperands() {
		return operands;
	}
	public int getN1() {
		return N1;
	}
	public int getN2() {
		return N2;
	}
	public int getn1() {
		return n1;
	}
	public int getn2() {
		return n2;
	}
}

public class Halstead extends Algorithm {
	ArrayList<Integer> put = new ArrayList<Integer>(); 
	String nameclass;
	String escapedchar[] = {"\\","\'","\"","\b","\f","\n","\r","\t"};  // 跳脫字元
	String keys[] = {"%b","%h","%s","%c","%d","%o","x","e","f","g","a","t"};
    String keyschar[] = {"return","case", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "boolean",
        	"do", "if",  "this", "break","double", "implements","throw", "byte", "else", "try", "char",
        	"final ","finally", "long", "const", "float",  "super", "while", "String", "interface ", "throws ", "public ", "protected ", "abstract ", "import ",
    	"static ", "void ", "class ", "strictfp ", "volatile ", "synchronized ", "private ", "native ",
    	"++","*", "+", "-", "<<", "<", "==", "&", "^", "|", "&&","||", "?:", ">=", "+=", "-=", "*=", "/=", ":",
    	"%=", "&=","^=", "|=", "<<=", ">>=", ">>>=", "--", "!=","=", "*", "/", "%", "!", "==",
		"!=", ">", ">=", "<", "<=", "=,", "~", ">>", ">>>",";" ,"(",")", "[",  "]", "{","}"}; // 保留字、運算子
    String needadd[] = {"return","case","int", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "boolean",
        	"double", "if",  "this", "break","do", "implements","throw", "byte", "else", "try", "char",
        	"final ","finally", "long", "const", "float",  "super", "while", "String"};
    String[] SP = {",", ".", ";", "@","\\", "/*", "*/", "\""};  // 特殊符號
    String[] Num = {"0","1","2","3","4","5","6","7","8","9"};
    String[] ann = {"@deprecated","@Override","@Deprecated","@SuppressWarnings","@suppresswarnings","@SafeVarargs","@safevarargs","@FunctionalInterface",
    		"@functionalinterface","@retentizzon","@Retention","@Docmented","@documented","@Target","@target","@Inherited","@inherited","@Repeatable","@repeatable","@Entity","@entity",
    		"@author","@Author","@Ebook","@ebook","@redonly","@Redonly","@Critical","@critical","@Schedule","@schedule","@Alert","@alert","@repeatable","@Repeatable","@interface",
    		"@NonNull","@ReadOnly","@Regex","@Tainted","@Untainted","@m"};
    		
    int countclass = 0;
    int countbig = 0, bigindex = 0;
    int countsmall = 0, smallindex = 0;
	
    HashMap<String, Integer> operators;
	HashMap<String, Integer> operands;
	ArrayList<HalsteadKeys> Results = new ArrayList<HalsteadKeys>(); 
	
	public Halstead() {
		operators = new HashMap<String, Integer>();
		operands = new HashMap<String, Integer>();
		
		for(int i = 0; i < keyschar.length; i++)
			operators.put(keyschar[i], 0);
	}
	
	
	public void getname(String fline) {
		if(fline.contains("class")) {
			countclass += 1;
			String gettest1, gettest3;
			gettest3 = fline.substring(fline.indexOf("class"), fline.indexOf("{"));
			
			if(fline.contains("<") || fline.contains(">")) { // 找class name
				gettest1 = fline.substring(fline.indexOf("<"), fline.indexOf(">") + 1); // 進來<>裡面
				gettest1 = gettest1.replace(" ", ""); // 空白全刪
				gettest1 = gettest1.replace(",", ", "); // 逗號後要空白
				gettest3 = fline.substring(fline.indexOf("class") + 6, fline.indexOf("<"));
				gettest3 = gettest3.replace(" ", "");
				nameclass = gettest3 + gettest1;
				//System.out.println("1. " + nameclass);
			} else {
				String[] tokens = gettest3.split("\\s+"); // 許多空白分割成一個~
				for(String token : tokens)
			    	gettest3 += token + " ";
			    	
				int a1 = gettest3.indexOf(" ");
				gettest3 = gettest3.substring(a1 + 1);
				int a2 = gettest3.indexOf(" ");
				gettest3 = gettest3.substring(0, a2);
				nameclass = gettest3;
				//System.out.println("2. " + nameclass);
			}
		}
	}
	
	public void readLine(String line) {
		int keyIndex = 0;			
		int leftBound = -1;
		int size = 0; // size of put
		
		line = line.replaceAll("\t", ""); // 將定位點取代掉
		while((keyIndex = line.substring(leftBound + 1).indexOf("\"")) >= 0) { // 避免跳脫符號的"
			if(size > 0) {
				if(leftBound + keyIndex > 0) { // ?????
					if(line.charAt(leftBound + keyIndex) != '\\') { // 前一個不是\ (正常字串)
						if(line.substring(put.get(put.size() - 1), leftBound + keyIndex).contains("//")) { //	避免第一對前面有註解符號 (代表是都是註解)
							break;
						} else {
							put.add(keyIndex + put.get(put.size() - 1));
							size++;
						}

					} else {
						if(leftBound + keyIndex > 1) {
							if(line.charAt(leftBound + keyIndex - 1) == '\\') { // 前面是兩個\\
								if(line.substring(put.get(put.size() - 1), leftBound + keyIndex).contains("//")) { //	避免第一對前面有註解符號 (代表是都是註解)
									break;
								} else {
									put.add(keyIndex + put.get(put.size() - 1));
									size++;
								}
							}
						}
						//keyIndex += 2; // 跳過兩個字元\\、\"、\t等
						//↑好像不用這行(?)
					}
				} else { // ??不知道啥時會進來 
					//System.out.println("注意注意!!!"+keyIndex);
					put.add(keyIndex + put.get(put.size() - 1));
					size++;
				}
			} else {
				if(line.substring(0, keyIndex).contains("//")) { //	避免第一對前面有註解符號 (代表是都是註解)
					break;
				} else {
					put.add(keyIndex);
					size++;
				}
			}
			
			leftBound += keyIndex + 1;
     	}
		
		if(size > 0) { // 有讀到""
			int rightBound = line.length();
			boolean inComment = false;
			while((keyIndex = line.substring(0, rightBound).lastIndexOf("//")) >= 0) {
				int tmp = size;
				for(int t = put.size() - 1; t > 0; t -= 2) {
					tmp -= 2;
					if(keyIndex > put.get(t - 1) + tmp && keyIndex < put.get(t) + tmp + 1) // 註解在字串內
						inComment = true;
					
					if(tmp < 2) break;
				}
				if(!inComment) {
					line = line.substring(0, keyIndex);
					inComment = false;
					rightBound = keyIndex;
				} else {
					rightBound -= keyIndex;
				}
			}
			
			for(int t = put.size() - 1; t >= 0; t = t - 2) {
				if(size < 2) break;
				String key = line.substring(put.get(t - 1) + size - 1, put.get(t) + size - 1);
				
				if(operands.containsKey(key)) {
					operands.put(key, operands.get(key) + 1);
				} else {
					operands.put(key, 1);
				}
			//	System.out.println("\n原始雙引號：" + line);
				line = line.substring(0, put.get(t - 1) + size - 1) + line.substring(put.get(t) + size - 1);
			//	System.out.println("幹掉之後的：" + line);
				size -= 2;
			}
		} else {
			if(line.contains("//"))
				line = line.substring(0, line.indexOf("//"));
		}
		
		String line16 = line;
		
		if(line.contains("\'")) {
			int find4 = line.indexOf("\'");
			line16 = line.substring(find4 + 1);
			if(!line16.contains("\'")) {
				line= line.substring(find4 + 1);
			}
		}
		
		String line3 = line;
		String line4 = line;
		int find = line3.indexOf('{');
		
		if(find >= 1) {
			find = find - 1;
			if(line3.charAt(find) == '\'') {
				String line99 = line3.substring(find + 3);
				find = find + 1;
				line3 = line3.substring(0, find + 2);
				line3 = line3.replace(line3.charAt(find), 'x');
				line3 = line3 + line99;
			}
		}
		
		while(line3.contains("{")) {
			int bigindexx = line3.indexOf("{");
			if(countbig == 0) {
				bigindexx = bigindexx + 1; 
				countbig = countbig + 1;
				if(countbig == 1) getname(line);
			} else {
				countbig = countbig + 1;
				bigindexx = bigindexx + 1; 				
			}
			
			line3 = line3.substring(bigindexx);
		}
		
		for(int b = 0; b < ann.length; b++) { 
			  if (line.contains(ann[b])){
				  line=line.replace(ann[b] ," ");
				  System.out.println("幹你老師");
			  }
			}
			
		
		String line20 = line;
		String line21 = line;
	
		if(countbig != 0) {                    //假設我們要找string123
			for (int h = 0; h < needadd.length; h++){   
				while(line21.contains(needadd[h])){    //string123
					
					int aaa,left,bbb,count=0 ;char b;
					String temp;
					String needaddp;
					temp = line.replaceAll(" ", "");
					aaa = needadd[h].length();  //算出string的長度(算出來是6)
					
					System.out.println(aaa+needadd[h]);
					
					left = line21.indexOf(needadd[h]);   //獲得string第一個字string的位置
					int first = left + aaa;  //第一個應該要判斷的
					
					if (aaa == temp.length()){
						break;
					}
					
					System.out.println(aaa+needadd[h]);
					System.out.println(line.length());
					b = line21.charAt(first); 
					int ccc = first;
					int ccc1 = 0;
					while(ccc1 != ccc){  //如果算出來的b值不是空白的話
						ccc1 = ccc;      //轉char成String
						bbb = Integer.valueOf(b);  //String 轉成int
						for (int aa=48; aa<58; aa++){ //0~9的阿斯ㄎㄧ碼
								
									if (bbb == aa ){        //是否等於0-9
										
									    ccc = ccc + 1 ;
										b=line21.charAt(ccc);
										count= count +1;
										
										//break;
									}
							}
										for(int at=97; at<123; at++){
											
											if (bbb == at ){        //是否等於a-z
											
												 ccc = ccc+1 ;
												 System.out.println(line+" "+ at);
												b=line21.charAt(ccc);
												
												count= count +1;
										}
										}
										
										for(int At=65; At<91; At++){
											
												if (bbb == At ){        //是否等於A-Z
													
													 ccc = ccc+1 ;
													b=line21.charAt(ccc+1);
													
											
										}
											 
										
									}//A~Z的最後
										
								}
						int total =0;
						total = count + left+aaa; 
						
						System.out.println(line);
						needaddp = line21.substring(left,total);
						System.out.println(needaddp);
						if(operands.containsKey(needaddp)) {
							operands.put(needaddp, operands.get(needaddp) + 1);
						} else {
							operands.put(needaddp, 1);
						}
						line21 = line21.substring(0,(left))+ line21.substring(total);
						System.out.println(line21);
						}
						
					}
			
			for(int i = 0; i < keyschar.length; i++) { //從第一個保留字開始
				int here = 0;
				 //String t1 = keyschar[i].substring(keyschar[i].length()-1); //抓字串最後一字 , 好比class ,最後一個是空白
			/*if (keyschar[i].length() >= line.length()){
					break;
				}*/
					 while(line20.contains(keyschar[i])) {
						    operators.put(keyschar[i], operators.get(keyschar[i]) + 1);
							here = line20.indexOf(keyschar[i]);
							here = here + 1;
							line20 = line20.substring(here);
						 }
				line20 = line;
				
			}
			
			
			for(int replacekey = 0 ; replacekey < keyschar.length; replacekey++)
	            line = line.replace(keyschar[replacekey], " ");
			
			for(int b = 0; b < SP.length; b++)
				line = line.replace(SP[b], " ");
			
			String[] token = line.split("\\s+");
			
			for(int xx = 0; xx < token.length; xx++) { //[列印出陣列]
				for(int xy = 0; xy < Num.length; xy++) { //檢查token字串的第一個是否為數字，如果是就刪除
					if(token[xx].equals("")) continue;
					if((token[xx].substring(0, 1)).equals(Num[xy]))
						token[xx] = token[xx].replace(token[xx], "");
				}
				
				if(token[xx].equals("")) continue;
				
				if(operands.containsKey(token[xx])) {
						operands.put(token[xx], operands.get(token[xx]) + 1);
				} else {
					operands.put(token[xx], 1);
				}
			}
			
			if(line4.contains("'}'")) {
				int minus = line4.indexOf("'}'"); 
				line4 = line4.substring(0, minus-2) + line4.substring(minus+2);
			}
			while(line4.contains("}")) {
				smallindex = line4.indexOf("}");
				smallindex = smallindex + 1;
				countbig = countbig - 1;
				line4 = line4.substring(smallindex);
			}
		}
		
		if((countbig == 0) && (operands.size() != 0)) {
			 
				
			int n1 = 0, N1 = 0;
			int n2 = 0, N2 = 0;
			System.out.println("\nclass name: "+ nameclass);
			System.out.println(operators.keySet());
			System.out.println(operators.values());
			System.out.println(operands.keySet());
			System.out.println(operands.values());
			for(int a = 0; a < keyschar.length; a++) {
				if ((operators.get(keyschar[a])) > 0) {
					n2 = n2 + 1;
					N2 = operators.get(keyschar[a]) + N2;
				}
			}
			
		    n1 = operands.size();
			for(Object key: operands.keySet()) {
				N1 += operands.get(key); 
			}
			
	        for(int i = 0; i < keyschar.length; i++) {
				operators.put(keyschar[i], 0);
			}
	        
			HalsteadKeys halsteadKeys = new HalsteadKeys(nameclass, operators, operands, N1, N2, n1, n2);
			Results.add(halsteadKeys);
	        operands.clear();
		}
	}
	
	public String getValue() {
		JSONArray jsonArr = new JSONArray();
		
		for(int i = 0; i < Results.size(); i++) {
			int N1 = Results.get(i).getN1();
			int N2 = Results.get(i).getN2();
			int n1 = Results.get(i).getn1();
			int n2 = Results.get(i).getn2();
			
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("Class Name", Results.get(i).getClassName());
			jsonObj.put("N1", N1);
			jsonObj.put("n1", n1);
			jsonObj.put("N2", N2);
			jsonObj.put("n2", n2);
			jsonObj.put("Vocabulary", n1 + n2); // Program vocabulary
			jsonObj.put("Length", N1 + N2); // Program length
			jsonObj.put("Calculated", n1 * log2(n1) + n2 * log2(n2)); // Calculated program length
			jsonObj.put("Volume", (N1 + N2) * log2(n1 + n2)); // V
	        
			if (n2 == 0) {
				jsonObj.put("Difficulty", n1 / 2);
				jsonObj.put("Effort", n1 / 2 * (N1 + N2) * log2(n1 + n2)); // E = D * V
	        } else {
	        	jsonObj.put("Difficulty", n1 / 2 * N2 / n2);
				jsonObj.put("Effort", n1 / 2 * N2 / n2 * (N1 + N2) * log2(n1 + n2)); // E = D * V
	        }
			
			jsonArr.put(jsonObj);
		}
		
		return jsonArr.toString();
	}
	
	public static double log2(int n) {
		if(n <= 0) {
			return 0;
		} else {
			return Math.log(n) / Math.log(2);
		}
	}
}