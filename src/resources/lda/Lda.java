package resources.lda;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import classes.Config;
import classes.Fileio;
import classes.Term;
import classes.Topic;
import classes.Tweet;
import classes.Tweets;
import classes.Vocabulary;

public class Lda {

	private int[][] documents;
    private int VocabLength;
    private int numTopics;
    private double alpha;
    private double beta;
    private LdaGibbsSampler ldaGibbs;
    private double[][] theta;
    private double[][] phi;
    private Theta theTheta;
    private boolean debug;
    private WordTopic wordtopic;
    private Vocabulary vocab;
    private static final int maxWordsInaTweet = 36;
    private Tweets alltweets;
    private ArrayList<Term[]> top30terms;
    
    public Lda(Vocabulary vocab){
    	numTopics = 5;
    	alpha = 2;
    	beta = 0.5;
    	this.debug = true;
    	this.vocab = vocab;
    }
    
    private Tweets tweets;
    
    public Lda(int numTopics, double alpha, double beta, Vocabulary vocab){
    	System.out.println("***[Alpha = "+alpha+" Beta = "+beta+"]***");
    	this.numTopics = numTopics;
    	this.alpha = alpha;
    	this.beta = beta;
    	this.debug = true;
    	this.vocab = vocab;
    }
    
    public void deeperMine(Tweets tweets, int topics){
    	List<Tweet> tweetList = tweets.tweets;
    	int numTweets = tweetList.size();
    	int[][] subdocuments = new int[numTweets][maxWordsInaTweet];
    	int j;
    	for (int i=0; i<numTweets;i++){
    		String tweet = tweetList.get(i).preprocessed;
    		String[] words = tweet.split(" ");
    		for (j=0;j<words.length;j++){
    			String word = words[j];
    			int id = vocab.get(word);
    			subdocuments[i][j] = id;
    		}
    		subdocuments[i][j] = -1;
    	}
    	VocabLength = vocab.getWord2id().keySet().size();
    	LdaGibbsSampler subldaGibbs = new LdaGibbsSampler(subdocuments, VocabLength,debug);
    	//int iterations, int burnIn, int thinInterval,int sampleLag
    	subldaGibbs.configure(10000, 2000, 100, 10);
        subldaGibbs.gibbs(topics, alpha, beta);
        
    }
    public void getTweets2Documents(Tweets tweets){
    	this.alltweets = tweets;
    	List<Tweet> tweetList = tweets.tweets;
    	this.tweets = tweets;
    	int numTweets = tweetList.size();
    	documents = new int[numTweets][maxWordsInaTweet];
    	int j;
    	for (int i=0; i<numTweets;i++){
    		String tweet = tweetList.get(i).preprocessed;
    		String[] words = tweet.split(" ");
    		for (j=0;j<words.length;j++){
    			String word = words[j];
    			int id = vocab.get(word);
    			documents[i][j] = id;
    		}
    		documents[i][j] = -1;
    	}
    }
    
  
    public void printDocuments(){
    	for (int i=0; i< documents.length; i++){
    		System.out.print("Tweet ID " + i + " ");
    		for(int j=0; j< documents[i].length; j++){
    			System.out.print(documents[i][j] + " ");
    		}
    		System.out.println();
    	}
    }
    
    public void useLastConfigLDA(String filename){
    	ldaGibbs = null;
    	try{
    		System.out.println("Read lda file");
    		ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
    		ldaGibbs = (LdaGibbsSampler) is.readObject();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	theta = ldaGibbs.getTheta();
    	phi = ldaGibbs.getPhi();
    	theTheta = new Theta(theta, tweets, this.numTopics);
    }
    public void beginLda(){
    	VocabLength = vocab.getWord2id().keySet().size();
    	ldaGibbs = new LdaGibbsSampler(documents, VocabLength,debug);
    	//int iterations, int burnIn, int thinInterval,int sampleLag
    	ldaGibbs.configure(10000, 2000, 100, 10);
        ldaGibbs.gibbs(numTopics, alpha, beta);
    	theta = ldaGibbs.getTheta();
    	phi = ldaGibbs.getPhi();
    	writeback();
    	System.out.println();
    	createPhi();
    	theTheta = new Theta(theta, tweets, this.numTopics);
    	wordtopic = new WordTopic(ldaGibbs.getWordTopicAssociation(),vocab, theTheta.getTopics(),tweets);
    }

    public void writeIncorrectTweetsToHTML(){
    	wordtopic.createHtmlFile(tweets);
    }
    /*
     * Just for paper
     */
    public void beginAggregatingLDA(boolean aggregatingOrSummarizing){
    	VocabLength = vocab.getWord2id().keySet().size();
    	ldaGibbs = new LdaGibbsSampler(documents, VocabLength,debug);
    	//int iterations, int burnIn, int thinInterval,int sampleLag
    	ldaGibbs.configure(10000, 2000, 100, 10);
        ldaGibbs.gibbs(numTopics, alpha, beta);
    	theta = ldaGibbs.getTheta();
    	phi = ldaGibbs.getPhi();
    	System.out.println();
    	createPhi();
    	theTheta = new Theta(theta, tweets, this.numTopics,aggregatingOrSummarizing);
    	//wordtopic = new WordTopic(ldaGibbs.getWordTopicAssociation(),vocab, theTheta.getTopics(),tweets);
    	//wordtopic.createHtmlFile(tweets);
    }
    public LdaGibbsSampler getLdaGibbs() {
		return ldaGibbs;
	}

	public WordTopic getWordtopic() {
		return wordtopic;
	}

	public void setTheTheta(Theta theTheta) {
		this.theTheta = theTheta;
	}

	public void writeback(){
    	ObjectOutputStream os = null;
    	try{
    		String filename = Config.lda + "_a_" + alpha + "_b_" + beta;
    		System.out.println("\nWriting lda to file " + filename);
    		os = new ObjectOutputStream(new FileOutputStream(filename));
    		os.writeObject(ldaGibbs);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		try {
				if (os != null) os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    public Theta getTheTheta(){
    	return theTheta;
    }
    public void createTheta(){
    	DecimalFormat formatter = new DecimalFormat("#.##");
    	for(int i = 0; i < 10; i++){
    		System.out.println(this.tweets.get(i).text);
    		int maxIndex = maxVal(theta[i]);
    		System.out.println("Topic " + maxIndex + " with " + formatter.format(theta[i][maxIndex]) + " probability");
    	}
    }
    
    public int maxVal(double[] array){
    	double max = array[0];
    	int maxIndex = 0;
    	for(int i = 0; i < array.length; i++){
    		if (max < array[i]){
    			max = array[i];
    			maxIndex = i;
    		}
    	}
    	return maxIndex;
    }
    public void createPhi(){
    	ArrayList<Topic> topics = new ArrayList<Topic>();
    	for (int i = 0; i < phi.length; i++) {
    		ArrayList<Term> terms = new ArrayList<Term>();
        	for(int j=0; j<phi[i].length; j++){
            	terms.add(new Term(j,phi[i][j],vocab));
            }
        	topics.add(new Topic(i,terms));
        }
    	top30terms = Fileio.writePhitoFile(topics, Config.phi);
	}
    
    public void printTopic(){
        //nwsum[j] total number of words assigned to topic j.
        int[] topic = ldaGibbs.getWordsAssignedtoTopic();
        for (int i=0;i<topic.length; i++){
        	System.out.println("Topic "+ i + "  has " + topic[i] + " words.");
        }
    }
    public void printWordperTweetAssignment(){
    	int[][] topicAssignment = ldaGibbs.getWordperTweetAssignment();
    	for (int i=0; i< topicAssignment.length; i++){
    		System.out.println("Tweet ID " + i + " " + tweets.tweets.get(i).preprocessed);
    		for(int j=0; j< topicAssignment[i].length; j++){
    			System.out.print(topicAssignment[i][j] + " ");
    		}
    		System.out.println();
    	}
    }
    
    public void printWordTopicAssociation(){
    	String[] topic = new String[this.numTopics];
    	for (int k=0;k<topic.length;k++){
    		topic[k] = "";
    	}
       	int[][] topicAssignment = ldaGibbs.getWordTopicAssociation();
    	for (int i=0; i< topicAssignment.length; i++){
    		for(int j=0; j< topicAssignment[i].length; j++){
    			if (topicAssignment[i][j] != 0)
    				topic[j] = topic[j] + vocab.get(i) + " ";
    		}
    	}
    	
    	for (int k=0;k<topic.length;k++){
    		System.out.println("Topic #" + k + " :: " + topic[k]);
    	}
    	
    }

	public int[][] getDocuments() {
		return documents;
	}
	public void setDocuments(int[][] documents) {
		this.documents = documents;
	}
	public double[][] getTheta() {
		return theta;
	}
	public double[][] getPhi() {
		return phi;
	}    
}
