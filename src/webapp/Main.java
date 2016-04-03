package webapp;

import java.util.ArrayList;
import java.util.HashMap;

import resources.lda.Lda;
import resources.lda.Theta;
import resources.lexer.Preprocessor;
import resources.lexer.TwitterParser;
import classes.Config;
import classes.Instances;
import classes.Tweet;
import classes.Tweets;
import classes.Vocabulary;

public class Main {

	private Preprocessor preprocessor;
	private Lda lda;
	private int numTopics = 6;
	private Vocabulary vocab;
	private Tweets allTweets;
	private ArrayList<Instances> instances;
	private double alpha=2;
	private double beta=0.01;
	private int features = 3;
	private ArrayList<Instances> topTweets;
	public Main(){
		//Config.createNewSession();
		//Config.log("Main invoked..");
		preprocessor = new Preprocessor();
		
	}
	
	public void readFolder(String dir){
		preprocessor.readInputFromAllFiles(dir,features);
		numTopics = preprocessor.getTweets().numTopics;
		vocab = preprocessor.getVocab();
		allTweets = preprocessor.getTweets();
		instances = preprocessor.getLabelledData();
	}
	
	/*
	 * Just for paper
	 */
	public void readFolderUseHashTagLabelling(String dir, int maxNum){
		preprocessor.readInputFromAllFiles(dir,features);
		allTweets = preprocessor.getTweets();
		preprocessor.useHashtagsAsLabels(allTweets, maxNum);
		numTopics = preprocessor.getTweets().numTopics;
		vocab = preprocessor.getVocab();
		instances = preprocessor.getLabelledData();
	}
	
	public void setTweetCorpus(int begining, int end){
		Tweets temp = new Tweets();
		for(Instances i: instances){
			ArrayList<Tweet> tempTweets = i.getTweets().getRange(begining, end);
			temp.tweets.addAll(tempTweets);
		}
		allTweets.tweets = temp.tweets;
		System.out.println(allTweets.tweets.size());
		//TwitterParser.write_tweets_to_file(Config.preprocessedTextFile, allTweets);

	}
	public void beginLDA(){
		lda = new Lda(numTopics,alpha,beta,vocab);
		lda.getTweets2Documents(allTweets);
		lda.beginLda();
	}
	
    /*
     * Just for paper
     */
	public void beginAggregatingLDA(){
		lda = new Lda(numTopics,alpha,beta,vocab);
		lda.getTweets2Documents(allTweets);
		lda.beginAggregatingLDA(true);
	}
	public void readOldLDA(String filename){
		lda = new Lda(numTopics,alpha,beta,vocab);
		lda.getTweets2Documents(allTweets);
		lda.useLastConfigLDA(filename);
	}
	
	public HashMap<String,Integer> getHistogram(){
		HashMap<String,Integer> hist = new HashMap<String, Integer>();
		for(Instances i: instances)
			hist.put(i.classLabel, i.getSize());
		return hist;
	}

	public void setNumTopics(int numTopics){
		this.numTopics = numTopics;
	}
	public void setLdaParameters(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public void generateTopStories(int topStories){
		topTweets = new ArrayList<Instances>();
		ArrayList<Theta> thetas = new ArrayList<Theta>();
		for(Instances instance: instances){
			lda = new Lda(topStories,alpha,beta,vocab);
			//lda.deeperMine(instance.getTweets(), topStories);
			lda.getTweets2Documents(instance.getTweets());
			lda.beginAggregatingLDA(false);
			Theta theTheta = lda.getTheTheta();
			thetas.add(theTheta);
			
			theTheta.createLDAclasses(instance.getTweets());
			ArrayList<Tweets> toPrintTopTweets = theTheta.getTweetsPerClasses();
	    	
			ArrayList<Tweet> top = new ArrayList<Tweet>();
	    	for(Tweets ts: toPrintTopTweets){
	    		top.addAll(theTheta.printTopTweets(ts, 1));
	    	}
	    	Tweets topTws = new Tweets();
	    	topTws.setTweets(top);
			topTweets.add(new Instances(instance.classLabel,topTws));
		}
	}

	
	public ArrayList<Instances> getTopTweets(){
		return topTweets;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Main m = new Main();
		//m.readFolder("C:/Users/kshitiz/Documents/workspace/TwitterSummarizer/WebContent/Files/SumarrizingStories/");
		//m.readFolder(Config.tweetDir);
		m.readFolderUseHashTagLabelling(Config.webDir + "TweetsAll/", 50);
		System.out.println("**************************************************");
		System.out.println("Using sample size of 500");
		m.setTweetCorpus(0, 500);
		m.beginLDA();
		//m.getLda().writeIncorrectTweetsToHTML();
		//m.generateTopStories(10);
	}

	public void createTopStroies(String dirPath){
		this.readFolder(dirPath);
		this.generateTopStories(10);
		ArrayList<Instances> i = this.getTopTweets();
		Tweets a = new Tweets();
		for(Instances ins: i){
			a.tweets.addAll(ins.getTweets().tweets);
		}
		TwitterParser.serialize(a, Config.preprocessedTweetFile);
		//return a;
	}
	public void setFeatures(int features){
		this.features = features;
	}
	public Preprocessor getPreprocessor() {
		return preprocessor;
	}

	public Lda getLda() {
		return lda;
	}

	public int getNumTopics() {
		return numTopics;
	}

	public Vocabulary getVocab() {
		return vocab;
	}

	public Tweets getAllTweets() {
		return allTweets;
	}

	public ArrayList<Instances> getInstances() {
		return instances;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getBeta() {
		return beta;
	}
	
	public boolean isInt(String i)
	{
		try
		{
			Integer.parseInt(i);
			return true;
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
	}
}
