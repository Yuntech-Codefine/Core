package codefine.metric;

public abstract class Algorithm {

}


abstract class AnalysisByLine extends Algorithm {
	
	abstract void readLine(String line);
	
}

abstract class AnalysisWhole extends Algorithm {
	
	abstract void read(String content);
	
}