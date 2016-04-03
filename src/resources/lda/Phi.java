package resources.lda;

import java.util.ArrayList;

import classes.Fileio;
import classes.Term;
import classes.Topic;
import classes.Tweet;
import classes.Vocabulary;

public class Phi {
	
	private double[][] phi;
	private ArrayList<Topic> topics;
	private Vocabulary vocab;
	private int numTopics;
	public Phi(double[][] phi, Vocabulary vocab, int numTopics){
		this.phi = phi;
		this.topics = new ArrayList<Topic>();
		this.vocab = vocab;
		this.numTopics = numTopics;
	}
	public void createPhi(){
    	for (int i = 0; i < phi.length; i++) {
    		ArrayList<Term> terms = new ArrayList<Term>();
        	for(int j=0; j<phi[i].length; j++){
            	terms.add(new Term(j,phi[i][j],vocab));
            }
        	topics.add(new Topic(i,terms));
        }
	}
	
	/*public int classify(Tweet tweet){
		int[] association = new int[numTopics];
		String[] words = tweet.preprocessed.split(" ");
		for(int i = 0; i < tweet.wordCount ; i ++){
			String word = words[i];
			for (Topic t: topics)
				t.terms.get(i)
		}
	}*/
	
}
