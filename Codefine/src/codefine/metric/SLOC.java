package codefine.metric;



public class SLOC extends Algorithm {
	private double value;
	
	public SLOC() {
		value = 0;
	}
	
	public void readLine(String line) {
		if(line != null) {
			line = line.replaceAll("\\n|\\t|\\s", "");
			if((!line.equals("")) && (!line.startsWith("//"))) {
				value++;
			}
		}
	}
	
	public double getValue() {
		return value;
	}
}
