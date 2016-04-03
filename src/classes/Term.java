package classes;

public class Term {
	public String term;
	public double percentage;
	
	public Term(int id, double percentage, Vocabulary vocab){
		this.percentage = percentage;
		this.term = vocab.get(id);
	}

	@Override
	public String toString() {
		return term + " %=" + percentage;
	}
	
	
}

