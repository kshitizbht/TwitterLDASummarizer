package classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Fileio {

	public static void readTweetsFromFile(String file, List<Tweet> tweets){
		java.util.Scanner scanner = null;
	    try {
	      scanner = new java.util.Scanner(new FileInputStream(file));
	      while (scanner.hasNextLine()){
	    	String preprocessedTweet = scanner.nextLine();
	    	Tweet tweet = new Tweet();
	    	tweet.preprocessed = preprocessedTweet;
	    	tweets.add(tweet);
	      }
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    finally{
	      scanner.close();
	    }
	}
	
	public static ArrayList<Instances> getFiles(String directory){
		ArrayList<Instances> ins = new ArrayList<Instances>();
		File dir = new File(directory);
		File[] subdirs = dir.listFiles();
		for(File classdir : subdirs){
			if (classdir.isDirectory()){
				String classlabel = classdir.getName();
				File[] tweetfiles = classdir.listFiles();
				ArrayList<String> filelist = new ArrayList<String>();
				for(File xmlfile : tweetfiles){
					if (xmlfile.getName().contains("xml")){
						filelist.add(xmlfile.getAbsolutePath().replace('\\', '/'));
					}
				}
				ins.add(new Instances(classlabel,filelist));
			}
		}
		return ins;
	}
	
	public static void writeWords2File(String path, HashMap<String,Integer> word2id){
		File file = new File(path);
		try {
			System.out.println("Writing vocab to file " + path);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (String word: word2id.keySet()){
				if(!word.equals("") && !word.equals(" "))
					out.write(word + " "+ word2id.get(word) + "\n");
			}
			out.close();
		} 
		catch (IOException e) 
		{ 
			System.out.println("Exception ");
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Term[]> writePhitoFile(ArrayList<Topic> topics, String path){
		File file = new File(path);
		ArrayList<Term[]> top30terms = new ArrayList<Term[]>();
		try {
			System.out.println("Writing topic term association to file using topics" + path);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write("Topic--Term Associations, Phi[k][w]"+")\n");
			for(Topic topic: topics){
				out.write("Topic #" + topic.topicID + "\n");
				Term[] terms = topic.getTop30Terms();
				for(Term t: terms){
					out.write("\t"+t.toString()+"\n");
				}
				out.write("\n");
				top30terms.add(terms);
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return top30terms;
	}
	
	public static void writeDocumentsToFile(String file,int[][] documents,int label ){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for(int i=0; i<documents.length;i++){
				for(int j=0; j<documents[i].length; j++){
					if (documents[i][j] == -1)
						break;
					out.write(documents[i][j] + ",");
				}
				out.write("\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//Fileio.getFiles();
		String word = "#apple";
		System.out.println(word.substring(1).toLowerCase());
	}
}
