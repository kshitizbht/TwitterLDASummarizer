package resources.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import classes.Config;
import classes.Tweet;
import classes.Tweets;
import classes.Vocabulary;

public class BagofWords {

	public Vocabulary simpleVocab;
	public Tweets tweets;
	private String[] labels;
	public static String newline = System.getProperty("line.separator");
	public BagofWords(Tweets tweets, String[] classlabels){
		this.tweets = tweets;
		simpleVocab = new Vocabulary();
		labels = classlabels;
		buildVocab();
	}
	
	public void buildVocab(){
		for(Tweet t : tweets.tweets){
			for(String word : t.preprocessed.split(" ")){
				simpleVocab.add(word);
			}
		}
	}
	
	public int getIndex(String label){
		for(int i = 0; i<labels.length; i++){
			if(label.equals(labels[i]))
				return i+1;
		}
		return 0;
	}
	public void buildArffFile(boolean ldaOrLabel, String file){
		String weka = "@relation tweetsBagOfWords" + newline; 
		for(String attr : simpleVocab.getWord2id().keySet()){
			weka += "@attribute " + attr + " numeric" + newline;
		}
		weka += "@attribute @class@ { 1";
		for(int i = 1; i < labels.length; i++)
			weka += ","+(i+1);
		weka += "}" + newline;
		
		weka += "@data" + newline;
		int lastIndex = simpleVocab.getWord2id().keySet().size();
		for(Tweet t : tweets.tweets){
			if(ldaOrLabel)
				weka += "{" + lastIndex + " " + t.ldalabel;
			else
				weka += "{" + lastIndex + " " + getIndex(t.classlabel);	
			for(String word : t.preprocessed.split(" ")){
				int id = simpleVocab.get(word);
				if (id != 0)
					weka += "," + simpleVocab.get(word) + " 1";
			}
			weka += "}" + newline;
		}
		
		try {
			FileUtils.write(new File(file), weka);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Tweet t = new Tweet();
		t.classlabel = "tech";
		t.preprocessed = "apple king of tech world";
		t.ldalabel =4;
		
		Tweet t1 = new Tweet();
		t1.classlabel = "tech";
		t1.preprocessed = "apple beat msft with a iphone";
		t1.ldalabel = 4;
		
		Tweet t2 = new Tweet();
		t2.classlabel = "tech";
		t2.preprocessed = "apple iphone greatest";
		t2.ldalabel = 4;
		
		Tweet t3 = new Tweet();
		t3.classlabel = "bus";
		t3.preprocessed = "stock fall down today";
		t3.ldalabel = 3;
		
		Tweet t4 = new Tweet();
		t4.classlabel = "bus";
		t4.preprocessed = "stock fall down today2";
		t4.ldalabel = 3;
		
		Tweet t5 = new Tweet();
		t5.classlabel = "bus";
		t5.preprocessed = "stock fall down today3";
		t5.ldalabel = 3;
		
		ArrayList<Tweet> ts = new ArrayList<Tweet>();
		ts.add(t);
		ts.add(t1);
		ts.add(t2);
		ts.add(t3);
		ts.add(t4);
		ts.add(t5);
		
		String[] classlabels = new String[2];
		classlabels[0] = "tech";
		classlabels[1] = "bus";
		
		Tweets tws = new Tweets();
		tws.tweets = ts;
		BagofWords bow = new BagofWords(tws, classlabels);
		bow.buildArffFile(false, Config.filesDir + "bowLabel.arff");
		bow.buildArffFile(true, Config.filesDir + "bowLDA.arff");

	}
}
