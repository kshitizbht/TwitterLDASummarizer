package classes;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root
public class Tweets {
	
	@ElementList
	public ArrayList<Tweet> tweets;
	public int numTopics;
	public ArrayList<String> topics;

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	public Tweets(){
		tweets = new ArrayList<Tweet>();
		topics = new ArrayList<String>();
		numTopics = 0;
	}
	public Tweets(ArrayList<Tweet> tweets){
		this.tweets = tweets;
		topics = new ArrayList<String>();
		numTopics = 0;
	}
	public void increaseNumTopic(){
		numTopics += 1;
	}
	public void addTopic(String topic){
		topics.add(topic);
	}

	public void removeTweets(int num){
		for(int i=0; i<num; i++){
			this.tweets.remove(i);
		}
	}
	
	public ArrayList<Tweet> getRange(int begining, int end){
		ArrayList<Tweet> temp = new ArrayList<Tweet>();
		if (tweets.size() > end){
			for(int i = begining; i<end; i++)
				temp.add(tweets.get(i));
		}else{
			temp = tweets;
		}
		return temp;
	}
	public Tweet get(int i){
		return tweets.get(i);
	}
	
	public void add(Tweet t){
		this.tweets.add(t);
	}
}
