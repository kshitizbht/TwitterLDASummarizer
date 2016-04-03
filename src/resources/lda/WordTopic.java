package resources.lda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import classes.Config;
import classes.Tweet;
import classes.Tweets;
import classes.Vocabulary;

public class WordTopic {

	private int[][] nw;
	private int numTopics;
	private Vocabulary vocab;
	private String[] topics;
	private Tweets tweets;
	
	public WordTopic(int[][] nw, Vocabulary vocab, String[] topics, Tweets tweets){
		this.nw = nw;
		numTopics = nw[0].length;
		this.vocab = vocab;
		this.topics = topics;
		this.tweets = tweets;
	}
	
	public String analyse(String text){
		String[] words = text.split(" ");
		int[] freq = new int[numTopics];
		String result ="";
		for(String s : words){
			int id = vocab.get(s);
			int topic = maxVal(nw[id]);
			freq[topic]++;
			result += topics[topic] + " ";
		}
		int topicIndex = maxVal(freq);
		return result + " :: Classified as " + topics[topicIndex];
	}
	
	public int maxVal(int[] array){
    	int max = array[0];
    	int maxIndex = 0;
    	for(int i = 0; i < array.length; i++){
    		if (max < array[i]){
    			max = array[i];
    			maxIndex = i;
    		}
    	}
    	return maxIndex;
    }
	
	public String htmlMaker(Tweet t){
		String[] words = t.preprocessed.split(" ");
		String nl = System.getProperty("line.separator");
		String result ="<div id=\"tweet\">";
		for(String s : words){
			int id = vocab.get(s);
			int topic = maxVal(nw[id]);
			String stopic = topics[topic];
			result += "<span id=\""+stopic+"\">"+s+" </span> " + nl;
		}
		result += "<span id=\""+t.classlabel+"\" class=\"original\">"+t.classlabel+" </span>" + nl;
		result += "<span id=\""+topics[t.ldalabel]+"\" class=\"classified\">"+topics[t.ldalabel]+" </span>" + nl;
		return result + "</div>" + nl;
	}
	
	public ArrayList<String> sampleMisclassified(){
		ArrayList<String> misclassified = new ArrayList<String>();
		int maxSize = tweets.tweets.size();
		Random generator = new Random();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		boolean done = false;
		while (!done){
			 int randomIndex = generator.nextInt(maxSize);
			 if (selected.size() == 10)
				 done = true;
			 else{
				 if (!selected.contains(randomIndex)){
					 Tweet t = tweets.get(randomIndex);
					 if(!t.classlabel.equals(topics[t.ldalabel])){
							 misclassified.add(htmlMaker(t));
							 selected.add(randomIndex);
					 }
				 }
			 }
		}
		
		return misclassified;
	}
	public void createHtmlFile(Tweets tweets){
		String html = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"/TwitterSummarizer/css/tableless.css\"></head><body>";
    	for(Tweet t : tweets.tweets){
    		if (!t.classlabel.equals(topics[t.ldalabel]))
    			html += htmlMaker(t);
    		System.out.print(".");
    	}
    	html += "</body></html>";
    	try {
    		System.out.println();
    		System.out.println("Writing html file output to " + Config.htmlout);
    		
			FileUtils.write(new File(Config.htmlout), html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
