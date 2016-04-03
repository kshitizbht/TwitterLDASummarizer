package classes;

import java.util.ArrayList;


public class Instances {
	public String classLabel = "Topic";
	private Tweets tweets;
	private ArrayList<String> filenames;
	public Instances(String classLabel, ArrayList<String> filenames) {
		this.classLabel = classLabel;
		this.filenames = filenames;
		this.tweets = new Tweets();
	}
	
	public Instances(String classLabel, Tweets topTweets) {
		this.classLabel = classLabel;
		this.tweets = topTweets;
	}
	
	public Instances(){
		this.filenames = new ArrayList<String>();
		this.tweets = new Tweets();

	}

	public int getSize(){
		return tweets.tweets.size();
	}
	public String getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public Tweets getTweets() {
		return tweets;
	}

	public ArrayList<Tweet> getTweetsAsList(){
		return tweets.tweets;
	}
	public void setTweets(Tweets tweets) {
		this.tweets = tweets;
	}

	public ArrayList<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
}
