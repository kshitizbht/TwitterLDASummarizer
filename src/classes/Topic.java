package classes;

import java.util.ArrayList;
import java.util.Arrays;

public class Topic {

	public ArrayList<Term> terms;
	public int topicID;
	
	public Topic(int topicID, ArrayList<Term> terms){
		this.topicID = topicID;
		this.terms = terms;
	}
	
	public Term[] getTop30Terms(){
		Term[] topArray = new Term[terms.size()]; 
		terms.toArray(topArray);
		Arrays.sort(topArray, new TermComparator());
		Term[] termArray = new Term[30];
		for (int i=0; i<30; i++){
			termArray[i] = topArray[i];
		}
		return termArray;
	}
}
