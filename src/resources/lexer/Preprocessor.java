package resources.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.commons.lang3.StringEscapeUtils;

import classes.Config;
import classes.Fileio;
import classes.Instances;
import classes.Tweet;
import classes.Tweets;
import classes.Vocabulary;

public class Preprocessor {
	private Vocabulary vocab;
	private Tweets allTweets;
	private Lexer scanner;
	private String classes;
	private ArrayList<Instances> labelledData;
	private int totalNumOfTweets=0;
	private int finalNumOfTweets=0;
	private int toRemoveCount = 0;
	public Preprocessor(){
		vocab = new Vocabulary();
		allTweets = new Tweets();
		scanner = new Lexer();
		classes = "";
	}
	/*
	 *Just for paper 
	 */
	public void useHashtagsAsLabels(Tweets tweets, int maxNum){
		/**
		 * go through each instance on use preprocess2()
		 */
		HashMap<String,ArrayList<Tweet>> hashTags = new HashMap<String,ArrayList<Tweet>>();
		ArrayList<String> topics = new ArrayList<String>();
		ArrayList<Tweet> toRemove = new ArrayList<Tweet>();
		for(Tweet t : tweets.tweets){
			if(t.getHashtags().size() > 0){
				//for(String hashtag : t.getHashtags()){
					String hashtag = t.getHashtags().get(0);
					if (hashTags.containsKey(hashtag)){
						t.classlabel = hashtag;
						hashTags.get(hashtag).add(t);
					}else{
						ArrayList<Tweet> tws = new ArrayList<Tweet>();
						tws.add(t);
						t.classlabel = hashtag;
						hashTags.put(hashtag, tws);
					}
				//}
			}else{
				toRemove.add(t);
			}
		}
		// remaining to do instances
		int cutoff = 300;
		int minSize = 150;
		labelledData = new ArrayList<Instances>();
		for(String hashtag : hashTags.keySet()){
			Tweets t = new Tweets();
			Tweets temp = new Tweets();
			temp.tweets = hashTags.get(hashtag);
			t.tweets = hashTags.get(hashtag);
			if (t.tweets.size() > minSize){
				t.tweets = t.getRange(0, cutoff);
				Instances ins = new Instances(hashtag,t);
				labelledData.add(ins);
				topics.add(hashtag);
				System.out.println("HashTags = " + hashtag + " NumTweets = " + t.tweets.size());
			}else{
				toRemove.addAll(t.tweets);
			}
			//remove excess
			for(int i = cutoff; i < temp.tweets.size(); i++){
				toRemove.add(temp.get(i));
			}
		}
		
		for (Tweet t: toRemove){
			allTweets.tweets.remove(t);
		}
		allTweets.numTopics = labelledData.size();
		allTweets.topics = topics;
		
		writeBack();
		System.out.println("Num of unique hashTags " + hashTags.size());	
	}
	
	
	public void readInputFromAllFiles(String dirPath, int features){
		ArrayList<Instances> filelist = Fileio.getFiles(dirPath);
		Iterator<Instances> iter = filelist.iterator();
		while(iter.hasNext()){
			Instances s = iter.next();
			ArrayList<Tweet> tempTweets = new ArrayList<Tweet>();
			for(String path: s.getFilenames()){
				Tweets temp = TwitterParser.deserialize(path);
				preprocess(temp,s.classLabel,features);
				tempTweets.addAll(temp.tweets);
			}
			Tweets tweets = new Tweets(tempTweets);
			s.setTweets(tweets);
			totalNumOfTweets += tweets.tweets.size();
			allTweets.tweets.addAll(tempTweets);
			allTweets.addTopic(s.classLabel);
			allTweets.increaseNumTopic();
		}
		vocab = scanner.getVocabulary();
		labelledData = filelist;
		removeLessFrequenctWords(features);
		this.finalNumOfTweets = this.totalNumOfTweets - this.toRemoveCount;
		writeBack();
	}
	public Tweets getAllTweets() {
		return allTweets;
	}
	public void setAllTweets(Tweets allTweets) {
		this.allTweets = allTweets;
	}
	public ArrayList<Instances> getLabelledData() {
		return labelledData;
	}
	public void setLabelledData(ArrayList<Instances> labelledData) {
		this.labelledData = labelledData;
	}
	public void setVocab(Vocabulary vocab) {
		this.vocab = vocab;
	}
	public void setScanner(Lexer scanner) {
		this.scanner = scanner;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public void preprocess(Tweets mtweets,String classlbl, int features){
		//TODO
		//remove toRemove list from here.
		ArrayList<Tweet> toRemove = new ArrayList<Tweet>();
		for (Tweet tweet: mtweets.tweets){
			String unescapedTweet = StringEscapeUtils.unescapeHtml3(tweet.text);
			tweet.preprocessed = scanner.parseString(unescapedTweet);
			tweet.setHashtags(scanner.getHashTags());
			tweet.wordCount = tweet.preprocessed.split(" ").length;
			tweet.classlabel = classlbl;
			if (tweet.wordCount < features){
				toRemove.add(tweet);
			}
			System.out.print(".");
		}
		toRemoveCount += toRemove.size();
		System.out.println("\nRemoving short length tweet " + toRemove.size());
		for (Tweet t: toRemove){
			mtweets.tweets.remove(t);
		}
	}
	
	public String getClasses(){
		return this.classes;
	}
	public void removeLessFrequenctWords(int features){
		ArrayList<Tweet> toRemove = new ArrayList<Tweet>();
		for(Tweet tweet: allTweets.tweets){
			String[] preprocessed = tweet.preprocessed.split(" ");
			String newtweet = "";			
			for(String s: preprocessed){
				if (vocab.getFrequency(s) >= 3){
					newtweet += s + " ";
				}else{
					//vocab.remove(s);
				}
			}
			tweet.preprocessed = newtweet;
			tweet.wordCount = newtweet.split(" ").length;
			if (tweet.wordCount <= features){
				toRemove.add(tweet);
			}
		}
		System.out.println("No of tweets before frequency removal "+ allTweets.tweets.size());
		System.out.println("No of tweets requested to remove "+ toRemove.size());
		toRemoveCount += toRemove.size();
		for (Tweet t: toRemove){
			allTweets.tweets.remove(t);
		}
		System.out.println("No of tweets after frequency removal "+ allTweets.tweets.size());
		
	}
	public void readLastKnownGoodConfiguration(boolean vocabyn){
		if (vocabyn){
			System.out.println("Reading vocab from "+Config.vocabFile);
			vocab = new Vocabulary(Config.vocabFile);
		}
		System.out.println("Reading preprocessed tweeets from file" + Config.preprocessedTweetFile);
		allTweets = TwitterParser.deserialize(Config.preprocessedTweetFile);
	}
	
	public void writeBack(){
		vocab.writeWords2File(Config.vocabFile);		
		TwitterParser.serialize(allTweets, Config.preprocessedTweetFile);
		TwitterParser.write_tweets_to_file(Config.preprocessedTextFile, allTweets);
	}
	
	public Vocabulary getVocab() {
		return vocab;
	}
	public Tweets getTweets() {
		return allTweets;
	}
	public Lexer getScanner() {
		return scanner;
	}

	public int getTotalNumOfTweets() {
		return totalNumOfTweets;
	}

	public int getFinalNumOfTweets() {
		return finalNumOfTweets;
	}

	public int getToRemoveCount() {
		return toRemoveCount;
	}
	
}
