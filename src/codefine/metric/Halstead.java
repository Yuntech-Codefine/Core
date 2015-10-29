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
	String escapedchar[] = {"\\","\'","\"","\b","\f","\n","\r","\t"};  //跳脫字元
	String keys[] = {"%b","%h","%s","%c","%d","%o","x","e","f","g","a","t",};
    String keyschar[] = {"case","int", "abstract", "continue", "for", "new", "switch", "assert", "default", 
    	"goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", 
    	"double","interface", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", 
    	"try", "char", "final", "inrerface", "static", "void", "class", "finally", "long", 
    	"strictfp", "volatile", "const", "float", "native", "super", "while", "String", "echo","++",
    	"*", "+", "-", "<<", "<", "==", "&", "^", "|", "&&","||", "?:", ">=", "+=", "-=", "*=", "/=",":",
    	"%=", "&=","^=", "|=", "<<=", ">>=", ">>>=", "--", "!=","=", "*", "/", "%", "!", "==",
		"!=", ">", ">=", "<", "<=", "=,", "~", ">>", ">>>",";" ,"(",")", "[",  "]", "{","}"}; //保留字、運算子
    String[] SP = {"string",",", ".", ";", "@","\\", "/*", "*/", "\""};  //特殊符號;
    String[] Num = {"0","1","2","3","4","5","6","7","8","9"};
    
    int countclass = 0;
    int countbig = 0, bigindex = 0;
    int countsmall = 0, smallindex = 0;
	
    HashMap<String, Integer> operators;
	HashMap<String, Integer> operands;
	ArrayList<HalsteadKeys> Results = new ArrayList<HalsteadKeys>(); 
	
	public Halstead() {
		operators = new HashMap<String, Integer>();
		operands = new HashMap<String, Integer>();
		//System.out.println("'");
		for(int i = 0; i < keyschar.length; i++)
			operators.put(keyschar[i], 0);
	}
	
	public void getname(String fline){
		if(fline.contains("class")) {
			countclass+=1;
			String gettest1,gettest2,gettest3;
			gettest3 = fline.substring(fline.indexOf("class"),fline.indexOf("{"));
			//System.out.println(gettest3);
			if((fline.contains("<"))||(fline.contains(">"))){ //找class name
				gettest1 = fline.substring(fline.indexOf("<"),fline.indexOf(">")+1); //進來<>裡面
				gettest1 = gettest1.replace(" ", ""); //空白全刪
				gettest1 = gettest1.replace(",", ", ");//逗號後要空白
				gettest3=fline.substring(fline.indexOf("class")+6,fline.indexOf("<"));
				gettest3=gettest3.replace(" ","");
				nameclass = gettest3 + gettest1;
				//System.out.println("1. "+nameclass);
			}else{
				String[] tokens= gettest3.split("\\s+"); //許多空白分割成一個~
			    for(String token:tokens ){
			    	  gettest3 += token+ " ";
			     }
			    int a1 = gettest3.indexOf(" "); gettest3 = gettest3.substring(a1+1);
			    int a2 = gettest3.indexOf(" "); gettest3 = gettest3.substring(0,a2);
			    nameclass = gettest3;
				//System.out.println("2. "+nameclass);
			}
		}
	
	}
	public void readLine(String line) {
		
		String line15 = line;
		String line3 = line;
		String line4 = line;
		String line20 = line;
		int keyIndex = 0;			
		int leftBound = -1;
		int size = 0; // size of "put"
		
		while ((keyIndex = line.substring(leftBound + 1).indexOf("\"")) >= 0) {
			////找出跳脫字元 然後略過
			if(size > 0) {
				put.add(keyIndex + put.get(put.size() - 1));
			} else {
				put.add(keyIndex);
			}
			size++;
			leftBound += keyIndex + 1;
     	}
		if(size > 0) { // 有讀到字串
			for (int t = put.size() - 1; t >= 0; t = t - 2) {
				String key = line.substring(put.get(t - 1) + size - 1, put.get(t) + size - 1);
				if(operands.containsKey(key)) {
					operands.put(key, operands.get(key) + 1);
				} else {
					operands.put(key, 1);
				}
				System.out.println("\n原始雙引號：" + line);
				line = line.substring(0, put.get(t - 1) + size - 1) + line.substring(put.get(t) + size - 1);
				System.out.println("幹掉之後的：" + line);
				size -= 2;
				if(size < 2) break;
			}
		}
		
		line3 =line;
		line4 =line;
		int find2 = line4.indexOf('{');
		int find =line3.indexOf('{');
		if (find > 0){
		find = find -1 ;
		if (line3.charAt(find) == '\''){
			find = find +1;
			line3= line3.replace(line3.charAt(find), ' ');
		}
		}
		if (find2 > 0){
		if (line4.charAt(find) == '\''){
			find = find +1;
			line4= line4.replace(line4.charAt(find), ' ');
		}
		}
		while (line3.contains("{")) {
			int bigindexx = line3.indexOf("{");
			if (countbig == 0){
				bigindexx = bigindexx +1; 
				countbig = countbig + 1;
				if (countbig == 1){
				getname(line);
				}
			}else{
				countbig = countbig + 1;
				bigindexx = bigindexx +1; 				
			}
			
			line3 = line3.substring(bigindexx);
		}
		
		if (countbig != 0) {
			
			line = line.toLowerCase(); // 小寫
			//String[] token = line.split(" "); // 遇空白切割
			
			if(line != null) {
				if (line.contains("//")) { // 遇到註解
	                line = line.substring(0, line.indexOf("//"));  // 移除註解後面的東西
	            }
			
				//這裡開始是區塊註解部分
				if (line.contains("/*")) {   //當遇到/*時 只接刪除整行
					line = line.substring(line.indexOf("/*"));
					line = line.replace(line, "");						
				} else if(line.contains("*/")) { //當程式\碼中有 */也刪除
					line = line.substring(0,line.indexOf("*/")+2);
					line = line.replace(line, "");
				}
				
			
				//line = line.replace(" ", ""); // 拿掉所有空格
				
				
			} //此為line!=null的結尾
		
		
			for(int i = 0; i < keyschar.length; i++) { //從第一個保留字開始
				
				int here= 0 ;
				while (line20.contains(keyschar[i])){
					operators.put(keyschar[i], operators.get(keyschar[i])+1);
					here = line20.indexOf(keyschar[i]);
					here = here +1 ;
					line20 = line20.substring(here);
				}
			}
			
			for(int replacekey = 0 ; replacekey < keyschar.length; replacekey ++){
	            line = line.replace(keyschar[replacekey], " ");
	        }
			
			for (int b = 0; b < SP.length; b++){
				boolean c = line.contains(SP[b]);
				line = line.replace(SP[b], " ");
			}
			
			String[] token = line.split("\\s+");
			
			for(int xx = 0; xx < token.length; xx++) { //[列印出陣列]
				for(int xy = 0; xy < Num.length ; xy++) { //檢查token字串的第一個是否為數字，如果是就刪除
					if(token[xx].equals("")) continue;
					if((token[xx].substring(0, 1)).equals(Num[xy])){
						token[xx] = token[xx].replace(token[xx],"");
					}
				}
				if(token[xx].equals("")) continue;
				if(operands.containsKey(token[xx])) {
						operands.put(token[xx], operands.get(token[xx]) + 1);
				} else {
					operands.put(token[xx], 1);
				}
			}
			
			while (line4.contains("}")) {
				smallindex = line4.indexOf("}");
				smallindex = smallindex +1; 
				countbig = countbig - 1;
				//System.out.println("xxxxx");
				line4 = line4.substring(smallindex);
			}
		}
		
		if ((countbig == 0) && (operands.size() != 0)) {
			int n1 = 0, N1 = 0;
			int n2 = 0, N2 = 0;
			
			for (int a = 0; a < keyschar.length; a++ ) {
				if ((operators.get(keyschar[a])) > 0) {
					n2 = n2 + 1 ;
					N2 = operators.get(keyschar[a]) + N2;
				}
			}
			
		    n1 = operands.size();
			for (Object key: operands.keySet()) {
				N1 += operands.get(key); 
			}
			
	        for(int i = 0; i < keyschar.length; i++) {
				operators.put(keyschar[i], 0);
			}
	        
			HalsteadKeys halsteadKeys = new HalsteadKeys(nameclass, operators, operands, N1, N2, n1, n2);
			Results.add(halsteadKeys);
	        operands.clear();
		}
		//System.out.println(countbig);
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
	    return Math.log(n) / Math.log(2);
	}
}
