package codefine.metric;
import java.util.*;
import java.util.HashMap;
//Step1 ��X�O�d�r 
//Step2 ��X�S��Ÿ�
//Step3 �ѤU���N�O�ϥΪ̪��R�W
//line.contains("elseif") = t f  line�̦�elseif���ܴN�^��T��F,���Χ����۵�
//line.indexOf("elseif") = 0 1 1 �p�W

public class Halstead extends Algorithm {
	private double value;
	int countclass=0;
	String nameclass;
	String escapedchar[] = {"\\","\'","\"","\b","\f","\n","\r","\t"};  //����
	String keys[] = {"%b","%h","%s","%c","%d","%o","x","e","f","g","a","t",};
    String keyschar[] = {"case","int", "abstract", "continue", "for", "new", "switch", "assert", "default", 
    	"goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", 
    	"double","interface", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", 
    	"try", "char", "final", "inrerface", "static", "void", "class", "finally", "long", 
    	"strictfp", "volatile", "const", "float", "native", "super", "while", "String", "echo","++",
    	"*", "+", "-", "<<", "<", "==", "&", "^", "|", "&&","||", "?:", ">=", "+=", "-=", "*=", "/=",":",
    	"%=", "&=","^=", "|=", "<<=", ">>=", ">>>=", "--", "!=","=", "*", "/", "%", "!", "==",
		"!=", ">", ">=", "<", "<=", "=,", "~", ">>", ">>>",";" ,"(",")", "[",  "]", "{","}"}; //�O�d�r�B�B��l
    String[] SP = {"string",",", ".", ";", "@","\\", "/*", "*/", "\""};  //�S��Ÿ�;
    String[] Num = {"0","1","2","3","4","5","6","7","8","9"};
    int countbig = 0, bigindex = 0;
    int countsmall = 0, smallindex = 0;
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
		
		if(line.contains("class")){
			countclass+=1;
			String getname = null;
			String gettest1,gettest2,gettest3; int aa;
			String gettest4 = "";
			if((line.contains("<"))||(line.contains(">"))){ //��class name
				gettest1 = line.substring(line.indexOf("<"),line.indexOf(">")+1); //�i��<>�̭�
				gettest1 = gettest1.replace(" ", ""); //�ťե��R
				gettest1 = gettest1.replace(",", ", ");//�r����n�ť�
				gettest2 = line.substring(line.indexOf("class")+6,line.indexOf("<"));
				gettest2 = gettest2.replace(" ", "");
				getname = gettest2 + gettest1;
				nameclass = getname;
				System.out.println("class name:" + nameclass);
				
				
			}
			else {
				
				gettest3 = line;
				gettest3 = gettest3.substring(gettest3.indexOf("class"),gettest3.indexOf("{"));
							
			    String[] tokens= gettest3.split("\\s+"); //�\�h�ťդ��Φ��@��~
			    for(String token:tokens ){
			    //	System.out.println(token);
			    	  gettest4 += token+ " ";
			    }
			  
			    int a1 = gettest4.indexOf(" "); gettest4 = gettest4.substring(a1+1);
			    int a2 = gettest4.indexOf(" "); gettest4 = gettest4.substring(0,a2);
			 	getname = gettest4;
				nameclass = getname;
				System.out.println("class name:" + nameclass);
				//getValue();
				
			}
		}
		 String line3 =line;
		 String line4 =line;
		 String line20 = line;
			 while (line3.contains("{")){
				 int bigindexx = line3.indexOf('{');
				 bigindexx = bigindexx +1; 
				 countbig = countbig + 1;
				line3 =line3.substring(bigindexx);
			 }
		if (countbig != 0){
		int firs = 0, las = 0, d=0, n1= 0, t = 0;
		ArrayList<Integer> put = new ArrayList<Integer>(); 
		line = line.toLowerCase(); // �p�g
		//String[] token = line.split(" "); // �J�ťդ���
		if(line != null) {
				if (line.contains("//")) { // �J�����
	                line = line.substring(0, line.indexOf("//"));  // �������ѫ᭱���F��
	            }
			
				//�o�̶}�l�O�϶����ѳ���
				if (line.contains("/*")){   //��J��/*�� �u���R�����
					line = line.substring(line.indexOf("/*"));
					line = line.replace(line, "");
															
				} 	
				else if(line.contains("*/")){ //��{��\�X���� */�]�R��
					line = line.substring(0,line.indexOf("*/")+2);
					line = line.replace(line, "");
				}
				
				if((line.contains("<"))&&(line.contains(">"))){  //��X�d���϶���<~> ex.HashMap<String, Integer>
					int f3,f4;
					f3 = line.indexOf("<"); //��X<����m
					f4 = line.indexOf(">"); //��X>����m
					line=line.substring(f3,f4+1); //HashMap<String, Integer> �ܦ� <String, Integer>
					//System.out.println(line);
					//System.out.println(line.substring(0,1));  ���զӤw
					line = line.replace(("<")," "); //�N<�ܦ��ť�
					line = line.replace((">")," "); //�N>�ܦ��ť�
					}
				//line = line.replace(" ", ""); // �����Ҧ��Ů�
				
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
			}//����line!=null������
		
		
		for(int i = 0; i < keyschar.length; i++) { //�q�Ĥ@�ӫO�d�r�}�l
			
			int here= 0 ;
			while (line20.contains(keyschar[i])){
				operators.put(keyschar[i], operators.get(keyschar[i])+1);
				here = line20.indexOf(keyschar[i]);
				here = here +1 ;
				line20 = line20.substring(here);
			}
			
			
			
			/*boolean a = line.contains(keyschar[i]); //�P�_line�̬O�_���o�ǫO�d�r
			String line5 = line;
			int abc =0;
            if (a == true){ //�p�G��
                    if ((operators.get(keyschar[i])) == 0){
                    operators.put(keyschar[i], 1);
                    int checkresumall = 0;
                  for (int ii = 0; ii < keyschar.length; ii++){
                	   String line2;
                       line2 = line;
                    	int checkresum = 0;
                    	
                    	while (line2.contains(keyschar[ii])){
                    		int checkre =  0;
                    		checkre = line2.indexOf(keyschar[ii]);
                    		checkre = checkre + 1;
                    		checkresum =checkresum + 1;
                    		 checkresumall = checkresumall + checkresum;
                    		line2= line2.substring(checkre);
                    	}
                    	operators.put(keyschar[ii], checkresum);
                    }
                    
                    }else {
                    operators.put(keyschar[i], operators.get(keyschar[i])+1);
                    }
                }*/
		}
		
		
		for(int replacekey = 0 ; replacekey < keyschar.length; replacekey ++){
            line = line.replace(keyschar[replacekey], " ");
        }
		
		for (int b = 0; b < SP.length; b++){
			boolean c = line.contains(SP[b]);
			line = line.replace(SP[b], " ");
		}
		String[] token = line.split("\\s+");
		for(int xx = 0; xx < token.length; xx++) { //[�C�L�X�}�C]
			for(int xy = 0; xy < Num.length ; xy++){ //�ˬdtoken�r�ꪺ�Ĥ@�ӬO�_���Ʀr,�p�G�O�N�R��
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
		 while (line4.contains("}")){
			  smallindex = line4.indexOf('}');
			 smallindex = smallindex +1; 
			 countbig = countbig - 1;
			line4 =line4.substring(smallindex);
		 }	
		}
		if ((countbig == 0) && (operands.size() != 0)){
		
			System.out.println(operators.keySet());
			System.out.println(operators.values());
			System.out.println(operands.keySet());
			System.out.println(operands.values());
			int n1 = 0 , N1 = 0;
			int n2 = 0 , N2 = 0;
			
			for (int a = 0; a < keyschar.length; a++ ){
				if ((operators.get(keyschar[a])) > 0) {
					n2 = n2 + 1 ;
					N2 = operators.get(keyschar[a]) + N2;
				}
			}
			
		    n1 = operands.size();
			for (Object key: operands.keySet()){
				N1 += operands.get(key); 
			}
		
			 System.out.println("���P���B��l�Ӽ�: n2 :"+ n2 );
		        System.out.println("�Ҧ��B��l�X�p�X�{������: N2 :"+ N2 );
		        System.out.println("���P���B�⤸�Ӽ�: n1 :"+ n1 );
		        System.out.println("�Ҧ��B�⤸�X�p�X�{������: N1 :"+ N1);
		        System.out.println("�{�����J�ơ]Program vocabulary�^�G n = "+ (n1+n2));
		        System.out.println("�{�����ס]Program length�^�G N = "+ (N1+N2));
		        System.out.println("�e�q�]Volume�^ V =  "+ (N1+N2) * Math.log(n1+n2));
		        if (n2 == 0){
		            System.out.println("����(Difficulty�^ D =  "+ (n1 / 2));
		            System.out.println("��O�]Effort�^ E =  "+ ((N1+N2) * Math.log(n1+n2))*((n1 / 2)));
		        }else{
		        System.out.println("����(Difficulty�^ D =  "+ (n1 / 2) * (N2 / n2));
		        System.out.println("��O�]Effort�^ E =  "+ ((N1+N2) * Math.log(n1+n2))*((n1 / 2) * (N2 / n2)));
		        }
		        double NN =0;
		        NN = n1* (Math.log(n2)) + n2* Math.log(n2);
		        System.out.println("�p��{�����ס]Calculated program length�^�GN^ " + NN);
		        for(int i = 0; i < keyschar.length; i++){
					operators.put(keyschar[i], 0);
			}
		        operands.clear();
		        System.out.println("");
		}
		
	}
		
	
	

	public double getValue() {
		/*System.out.println(operators.keySet());
		System.out.println(operators.values());
		System.out.println(operands.keySet());
		System.out.println(operands.values());*/
		/*int n1 = 0 , N1 = 0;
		int n2 = 0 , N2 = 0;
		
		for (int a = 0; a < keyschar.length; a++ ){
			if ((operators.get(keyschar[a])) > 0) {
				n2 = n2 + 1 ;
				N2 = operators.get(keyschar[a]) + N2;
			}
		}
		
	    n1 = operands.size();
		for (Object key: operands.keySet()){
			N1 += operands.get(key); 
		}
	
		 System.out.println("���P���B��l�Ӽ�: n2 :"+ n2 );
	        System.out.println("�Ҧ��B��l�X�p�X�{������: N2 :"+ N2 );
	        System.out.println("���P���B�⤸�Ӽ�: n1 :"+ n1 );
	        System.out.println("�Ҧ��B�⤸�X�p�X�{������: N1 :"+ N1);
	        System.out.println("�{�����J�ơ]Program vocabulary�^�G n = "+ (n1+n2));
	        System.out.println("�{�����ס]Program length�^�G N = "+ (N1+N2));
	        System.out.println("�e�q�]Volume�^ V =  "+ (N1+N2) * Math.log(n1+n2));
	        if (n2 == 0){
	            System.out.println("����(Difficulty�^ D =  "+ (n1 / 2));
	            System.out.println("��O�]Effort�^ E =  "+ ((N1+N2) * Math.log(n1+n2))*((n1 / 2)));
	        }else{
	        System.out.println("����(Difficulty�^ D =  "+ (n1 / 2) * (N2 / n2));
	        System.out.println("��O�]Effort�^ E =  "+ ((N1+N2) * Math.log(n1+n2))*((n1 / 2) * (N2 / n2)));
	        }
	        double NN =0;
	        NN = n1* (Math.log(n2)) + n2* Math.log(n2);
	        System.out.println("�p��{�����ס]Calculated program length�^�GN^ " + NN);*/
	        return value;
	}
}			
