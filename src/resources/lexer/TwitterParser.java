package resources.lexer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import classes.Tweet;
import classes.Tweets;

public class TwitterParser {
	
	public static Tweets deserialize(String filepath){
		File f = new File(filepath);
		Serializer serializer = new Persister();
		try {
			Tweets query = serializer.read(Tweets.class, f);
			return query;
		} catch (Exception e) {
			
			System.out.println("Handeled");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void write_tweets_to_file(String file, Tweets tweets){
		File f = new File(file);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			for (Tweet tweet: tweets.tweets){
				String formattedtweets = "'" + tweet.preprocessed.replace(",", " ") + "'" ;
				formattedtweets += ","+tweet.classlabel;
				//String formattedtweets = tweet.preprocessed.replace(",", "");
				out.write(formattedtweets + "\n");
			}
			out.close();
		} 
		catch (IOException e) 
		{ 
			System.out.println("Exception ");
			e.printStackTrace();
		}
	}
	
	public static void serialize() {
		Serializer serializer = new Persister();
		Tweets query = new Tweets();
		ArrayList<Tweet> files = new ArrayList<Tweet>();
		Tweet file1 = new Tweet();
		file1.name="kb";
		file1.text = "THE first tweet ever";
		files.add(file1);
		query.tweets = files;
		try {
			serializer.write(query, new File("C:/Users/kshitiz/Documents/workspace/TwitterProject/Files/test2.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void serialize(Tweets tweets, String path){
		Serializer serializer = new Persister();
		try {
			serializer.write(tweets, new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

