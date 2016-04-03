package classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Vocabulary {
	private HashMap<String,Integer> word2id;
	private HashMap<Integer,String> id2word;
	private int id;
	private HashMap<String,Integer> wordFrequency;
	
	public Vocabulary(){
		id = 0;
		word2id = new HashMap<String, Integer>();
		id2word = new HashMap<Integer, String>();
		wordFrequency = new HashMap<String, Integer>();
	}

	public Vocabulary(String file){
		id = 0;
		word2id = new HashMap<String, Integer>();
		id2word = new HashMap<Integer, String>();
		wordFrequency = new HashMap<String, Integer>();
		java.util.Scanner scanner = null;
	    try {
	      scanner = new java.util.Scanner(new FileInputStream(file));
	      while (scanner.hasNextLine()){
	    	String[] word = scanner.nextLine().split(" ");
	    	String term = word[0];
	    	int freq = Integer.parseInt(word[1]);
	        for(int i =0; i <freq; i++)
	        	add(term);
	      }
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    finally{
	      scanner.close();
	    }
	}
	public String get(int id){
		return id2word.get(id);
	}
	public int get(String word){
		if(word2id.containsKey(word))
			return word2id.get(word);
		else{
			System.out.println(word + "Word");
			return 0;
		}
	}
	
	public int getFrequency(String word){
		if(wordFrequency.containsKey(word))
			return wordFrequency.get(word);
		else
			return 0;
	}
	
	public void add(String word){
		if (word2id.containsKey(word)){
			wordFrequency.put(word, wordFrequency.get(word) + 1);
		}else{
			wordFrequency.put(word, 1);
			word2id.put(word, id);
			id2word.put(id, word);
			id++;
		}
	}
	
	public void remove(String word){
		if(word2id.containsKey(word)){
			int id = word2id.get(word);
			word2id.remove(word);
			id2word.remove(id);
			//wordFrequency.remove(word);
		}
	
	}
	
	public int length(){
		return id;
	}
	public HashMap<String, Integer> getWord2id() {
		return word2id;
	}

	public HashMap<Integer, String> getId2word() {
		return id2word;
	}

	public HashMap<String, Integer> getWordFrequency() {
		return wordFrequency;
	}
	
	public void writeWords2File(String path){
		Fileio.writeWords2File(path, wordFrequency);
	}
	
	
}
