package resources.lda;

import java.util.ArrayList;
import java.util.Arrays;
import classes.Tweet;
import classes.TweetComparator;
import classes.Tweets;

public class Theta {

	private int numTopics;
	private Tweets tweets;
	private double[] accuracy;
	private int[] assignedlabel;
	private String[] topics;
	private Statistics stats;
	public String[] getTopics() {
		return topics;
	}

	private int[] total;
	private int[][] hist;
	private int[][] aggregatedMatrix;
	private int actualTopic;
	private ArrayList<Tweets> tweetsPerClasses;
	private ArrayList<String> classlabels;
	public Theta(double[][] theta, Tweets tweets, int numTopics){
		this.numTopics = numTopics;
		this.tweets = tweets;
		tweetsPerClasses = new ArrayList<Tweets>();
		accuracy = new double[numTopics];
		assignedlabel = new int[numTopics];
		topics = new String[numTopics];
		total = new int[numTopics];
		hist = new int[numTopics][numTopics];
		assignLDALAbel(theta);
	}
	
	
    /*
     * Just for paper
     */
	public Theta(double[][] theta, Tweets tweets, int numTopics,boolean aggregating){
		this.numTopics = numTopics;
		this.tweets = tweets;
		tweetsPerClasses = new ArrayList<Tweets>();
		accuracy = new double[numTopics];
		assignedlabel = new int[numTopics];
		topics = new String[numTopics];
		total = new int[numTopics];
		hist = new int[numTopics][numTopics];
		assignLDALAbel(theta,aggregating);
	}
	
	
    /*
     * Just for paper
     */
	public void assignLDALAbel(double[][] theta, boolean aggregating){
		classlabels = new ArrayList<String>();
		for(int i = 0; i < tweets.tweets.size(); i++){
    		int maxIndex = maxVal(theta[i]);
    		Tweet current = tweets.get(i);
    		current.ldalabel = maxIndex;
    		current.association = theta[i][maxIndex];
    		if (!classlabels.contains(current.classlabel)){
    			classlabels.add(current.classlabel);
    		}
    	}
		if (aggregating)
			aggregator();
	}
	
	public void aggregator(){
		int[][] matrix = new int[numTopics][numTopics];
		for (int i =0; i < numTopics; i++){
			System.out.print("\t\t" + i);
		}
		System.out.println();
		for (int j = 0; j < classlabels.size(); j++){
			String c = classlabels.get(j);
			String ws = "";
			for (int k = 0; k < 15 - c.length(); k ++)
				ws += " ";
			System.out.print(c + ws);
			int[] mhist = new int[numTopics];
			int sum = 0;
			for(Tweet t : tweets.tweets){
				if (t.classlabel.equals(c)){
					mhist[t.ldalabel]++;
					sum++;
				}
			}
			matrix[j] = mhist;
			for(int i = 0; i < numTopics; i++){
				System.out.print(mhist[i] + "\t\t");
			}
			System.out.println();
			hist[j] = mhist;
			total[j] = sum;
			//Don't label yet
			//int label2give = maxVal(mhist);
			//assignedlabel[j] = label2give;
			//topics[label2give] = c;
		}
		//Map each topic to one of the labels
		actualTopic = classlabels.size();
		aggregatedMatrix = new int[actualTopic][actualTopic];
		
		fillWith0s();
		for(int i=0; i<numTopics; i++){
			int[] col = new int[actualTopic];
			for (int k=0; k<actualTopic; k++){
				col[k] = hist[k][i];
			}
			int label = this.maxVal(col);
			addCol2Matrix(col,label);
		}
		//debugAggregatedMatrix();
		for (int i =0; i < actualTopic; i++){
			System.out.print("\t\t" + i);
		}
		System.out.println();
		for (int j = 0; j < classlabels.size(); j++){
			String c = classlabels.get(j);
			String ws = "";
			for (int k = 0; k < 15 - c.length(); k ++)
				ws += " ";
			System.out.print(c + ws);
			for(int k=0; k < actualTopic; k++){
				System.out.print("\t\t"+aggregatedMatrix[j][k]);
			}
			System.out.println();
		}
		
		stats = new Statistics(aggregatedMatrix);
		stats.printAllInformation();
	}
	public void fillWith0s(){
		for(int i=0; i < actualTopic ; i++){
			for(int j=0; j < actualTopic; j++){
				aggregatedMatrix[i][j] = 0;
			}
		}
	}
	public void debugAggregatedMatrix(){
		for(int i=0; i<actualTopic; i++){
			for(int j=0; j<actualTopic; j++){
				System.out.print("\t"+aggregatedMatrix[i][j]);
			}
			System.out.println();
		}
	}
	public void addCol2Matrix(int[] col, int index){
		for(int i=0; i<actualTopic; i++){
			for(int j=0; j<actualTopic; j++){
				if(i==index)
					aggregatedMatrix[i][j] += col[j];
			}
		}
	}
	public void assignLDALAbel(double[][] theta){
		//TODO 
		//This is only work if numTopics is actually equal to numClasses as assigned by preprocessor.
		int[][] matrix = new int[numTopics][numTopics];
		ArrayList<String> classlabels = new ArrayList<String>();
		for(int i = 0; i < tweets.tweets.size(); i++){
    		int maxIndex = maxVal(theta[i]);
    		Tweet current = tweets.get(i);
    		current.ldalabel = maxIndex;
    		current.association = theta[i][maxIndex];
    		if (!classlabels.contains(current.classlabel)){
    			classlabels.add(current.classlabel);
    		}
    	}
		
		
		for (int i =0; i < numTopics; i++){
			System.out.print("\t\t" + i);
		}
		System.out.println();
		for (int j = 0; j < classlabels.size(); j++){
			String c = classlabels.get(j);
			String ws = "";
			for (int k = 0; k < 15 - c.length(); k ++)
				ws += " ";
			System.out.print(c + ws);
			int[] mhist = new int[numTopics];
			int sum = 0;
			for(Tweet t : tweets.tweets){
				if (t.classlabel.equals(c)){
					mhist[t.ldalabel]++;
					sum++;
				}
			}
			matrix[j] = mhist;
			for(int i = 0; i < numTopics; i++){
				System.out.print(mhist[i] + "\t\t");
			}
			System.out.println();
			hist[j] = mhist;
			total[j] = sum;
			int label2give = maxVal(mhist);
			assignedlabel[j] = label2give;
			topics[label2give] = c;
		}
		
		//Add correctly classified into arraylist for that topic.
		/*for (String classlbl : classlabels){
			Tweets subtopic = new Tweets();
			for(Tweet t: tweets.tweets){
				if (t.classlabel.equals(classlbl) && t.ldalabel == getTopicIndex(t.classlabel))
					subtopic.add(t);
			}
			tweetsPerClasses.add(subtopic);
		}*/
		calculateInitialAccuracy();
		//printTopTweets();
		for (int i = 0; i < numTopics; i++){
			System.out.print(i +" = " +topics[i] + " ");
		}
		stats = new Statistics(matrix);
		stats.printAllInformation();
	}
	
	
	public void createLDAclasses(Tweets tweets){
		for(int i =0; i<numTopics; i++){
			tweetsPerClasses.add(new Tweets());
		}
		for(Tweet t: tweets.getTweets() ){
			tweetsPerClasses.get(t.ldalabel).add(t);
		}
	}
	public Statistics getStats() {
		return stats;
	}
	

	public int getTopicIndex(String classlabel){
		for (int i = 0; i < numTopics; i++){
			if (topics[i] != null && topics[i].equals(classlabel))
				return i;
		}
		return 0;
	}
	
	public ArrayList<Tweet> printTopTweets(Tweets subClass, int numTweet){
		ArrayList<Tweet> result = new ArrayList<Tweet>();
		Tweet[] t = new Tweet[subClass.tweets.size()];
		subClass.tweets.toArray(t);
		Arrays.sort(t,new TweetComparator());
		System.out.println("Top tweets in " + t[0].classlabel + " category.");
		for(int i =0 ;i < numTweet;i++){
			if (i < t.length){
				System.out.println(t[i].text);
				result.add(t[i]);
			}
		}
		return result;
	
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
	  
	  public void calculateInitialAccuracy(){
		  for (int i = 0; i < numTopics; i ++){
			  int correctlabel = assignedlabel[i];
			  int correct = hist[i][correctlabel];
			  accuracy[i] = (correct * 1.0) / total[i];
			  System.out.print("Sum = " + total[i] + " Correct " + correctlabel + " Total Correctly Classified " + correct);
			  System.out.println(" Topic " + topics[correctlabel] + " Accuracy = " + accuracy[i]);
		  }
		  double sum = 0;
		  for (int j = 0; j < accuracy.length; j++){
			  sum += accuracy[j];
		  }
		  System.out.println("LDA's accuracy on training set " + (sum * 100.00)/this.numTopics + " % ");
	  }


	public int getNumTopics() {
		return numTopics;
	}


	public Tweets getTweets() {
		return tweets;
	}


	public double[] getAccuracy() {
		return accuracy;
	}


	public int[] getAssignedlabel() {
		return assignedlabel;
	}


	public int[] getTotal() {
		return total;
	}


	public int[][] getHist() {
		return hist;
	}


	public int[][] getAggregatedMatrix() {
		return aggregatedMatrix;
	}


	public int getActualTopic() {
		return actualTopic;
	}


	public ArrayList<Tweets> getTweetsPerClasses() {
		return tweetsPerClasses;
	}
	  
	  
	  
}
