package codefine.metric;
import java.util.*;
import java.util.HashMap;
//Step1 抓出保留字 
//Step2 找出特殊符號
//Step3 剩下的就是使用者的命名
//line.contains("elseif") = t f  line裡有elseif的話就回傳T或F,不用完全相等
//line.indexOf("elseif") = 0 1 1 如上

public class Halstead extends Algorithm {
	private double value;

	String escapedchar[] = {"\\","\'","\"","\b","\f","\n","\r","\t"};  //跳脫字元1234444
	String keys[] = {"%b","%h","%s","%c","%d","%o","x","e","f","g","a","t",};
    String keyschar[] = {"case","int", "abstract", "continue", "for", "new", "switch", "assert", "default",
    	"goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", 
    	"double","interface", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", 
    	"try", "char", "final", "inrerface", "static", "void", "class", "finally", "long", 
    	"strictfp", "volatile", "const", "float", "native", "super", "while", "String", "echo","++",
    	"*", "+", "-", "<<", "<", "==", "&", "^", "|", "&&","||", "?:", ">=", "+=", "-=", "*=", "/=",":",
    	"%=", "&=","^=", "|=", "<<=", ">>=", ">>>=", "--", "!=","=", "*", "/", "%", "!", "==",
		"!=", ">", ">=", "<", "<=", "=,", "~", ">>", ">>>" ,"(",")", "[",  "]", "{","}"}; //保留字、運算子
    String[] SP = {"string",",", ".", ";", "@","\\", "/*", "*/", "\"","0","1","2",
    	"3","4","5","6","7","8","9","0"	};  //特殊符號
	
	HashMap<String, Integer> operators;
	HashMap<String, Integer> operands;
	
	public Halstead() {
		value = 0;
		operators = new HashMap<String, Integer>();
		operands = new HashMap<String, Integer>();
		for(int i = 0; i < keyschar.length; i++)
			operators.put(keyschar[i], 0);
	}
	public void readLine(String line) {
		int firs = 0, las = 0, d=0, n1= 0, t = 0;
		ArrayList<Integer> put = new ArrayList<Integer>(); 
		line = line.toLowerCase(); // 小寫
		//String[] token = line.split(" "); // 遇空白切割
		if(line != null) {
			if (line.contains("//")) { // 遇到註解
                line = line.substring(0, line.indexOf("//"));  // 移除註解後面的東西
            }
			line = line.replace(" ", ""); // 拿掉所有空格
			int keyIndex = 0;			
			int leftBound = -1;
			int h = 0;
			while((keyIndex = line.substring(leftBound + 1).indexOf("\"")) >= 0){
				if(h > 0){
					put.add(keyIndex + put.get(h-1) + 1);
					//System.out.println(keyIndex + put.get(h-1) + 1);
				} else {
					put.add(keyIndex);
				}
				
				h++;
				leftBound += keyIndex + 1;
				
			}
			
			
			for (t = put.size() - 1; t >= 0; t = t - 2) {
				//System.out.println(line.substring(put.get(t - 1) + 1, put.get(t)));
				String key = line.substring(put.get(t - 1) + 1, put.get(t));
				
				if(operands.containsKey(key)) {
					operands.put(key, operands.get(key) + 1);
				} else {
					operands.put(key, 1);
				}
				
				line = line.substring(0, put.get(t - 1)) + line.substring(put.get(t));
			}
		}
		
		
		for(int i = 0; i < keyschar.length; i++) { //從第一個保留字開始
			boolean a = line.contains(keyschar[i]); //判斷line裡是否有這些保留字
			
			if (a == true){ //如果有
					if ((operators.get(keyschar[i])) == 0){
					operators.put(keyschar[i], 1);
					
					for (int g = 0; g< escapedchar.length; g++){
						boolean escaped = line.contains(escapedchar[g]);
							if (escaped == true){
								line = line.replace(escapedchar[g], " ");
							}
				}
					}else {
					operators.put(keyschar[i], operators.get(keyschar[i])+1);
					}
				}
			line = line.replace(keyschar[i], " ");
		}
		
		
		for (int b = 0; b < SP.length; b++){
			boolean c = line.contains(SP[b]);
			line = line.replace(SP[b], " ");
		}
		
		//System.out.println(line);
	    /*String[] token = line.split(" ");
		for (String tokens: token){
			System.out.println(token);
		}*/
		
		String[] token = line.split("\\s+");
		
		for(int xx = 0; xx < token.length; xx++) { //[列印出陣列]
			if(token[xx].equals("")) continue;
			if(operands.containsKey(token[xx])) {
				operands.put(token[xx], operands.get(token[xx]) + 1);
			} else {
				operands.put(token[xx], 1);
			}
			
		}
	}
	
	public double getValue() {
		System.out.println(operators.keySet());
		System.out.println(operators.values());
		System.out.println(operands.keySet());
		System.out.println(operands.values());
		int n1 = 0 , N1 = 0;
		int n2 = 0 , N2 = 0;
		
		for (int a = 0; a < keyschar.length; a++ ){
			if ((operators.get(keyschar[a])) != 0) {
				n2 = n2 + 1 ;
				N2 = operators.get(keyschar[a]) + N2;
			}
		}
		
	    n1 = operands.size();
		for (Object key: operands.keySet()){
			N1 += operands.get(key); 
		}
	
		System.out.println("不同的運算元個數: n2 :"+ n2 );
		System.out.println("所有運算元合計出現的次數: N2 :"+ N2 );
		System.out.println("不同的運算子個數: n1 :"+ n1 );
		System.out.println("所有運算子合計出現的次數: N1 :"+ N1);
		System.out.println("程式詞彙數（Program vocabulary）： n = "+ (n1+n2));
		System.out.println("程式長度（Program length）： N = "+ (N1+N2));
		System.out.println("容量（Volume） V =  "+ (N1+N2) * Math.log(n1+n2));
		System.out.println("難度(Difficulty） D =  "+ (n1 / 2) * (N2 / n2));
		System.out.println("精力（Effort） E =  "+ ((N1+N2) * Math.log(n1+n2))*((n1 / 2) * (N2 / n2)));
		double NN =0;
		NN = n1* (Math.log(n2)) + n2* Math.log(n2);
		System.out.println("計算程式長度（Calculated program length）：N^ " + NN);
		return value;
	}
}			
