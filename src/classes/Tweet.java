package classes;


import java.util.ArrayList;

import org.simpleframework.xml.Element;



@Element(name="tweet")
public class Tweet {

	@Element
	public String text;
	@Element(required=false)
	public String name;
	@Element(required=false)
	public String preprocessed;
	@Element(required=false)
	public String classlabel="Topic";
	@Element(required=false)
	public int wordCount;
	//USED BY LDA LABELLING
	public double association;
	public int ldalabel;
	public ArrayList<String> hashtags;
	
	public Tweet(){
		hashtags = new ArrayList<String>();
		preprocessed = "";
		classlabel = "";
	}
	public Tweet(String text){
		this.text = text;
		hashtags = new ArrayList<String>();
		preprocessed = "";
		classlabel = "";
	}
	public String getText() {
		return text;
	}
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPreprocessed() {
		return preprocessed;
	}
	public void setPreprocessed(String preprocessed) {
		this.preprocessed = preprocessed;
	}
	public String getClasslabel() {
		return classlabel;
	}
	public void setClasslabel(String classlabel) {
		this.classlabel = classlabel;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public double getAssociation() {
		return association;
	}
	public void setAssociation(double association) {
		this.association = association;
	}
	public int getLdalabel() {
		return ldalabel;
	}
	public void setLdalabel(int ldalabel) {
		this.ldalabel = ldalabel;
	}
	public ArrayList<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(ArrayList<String> hashtags) {
		this.hashtags = hashtags;
	}
	public void setText(String text) {
		this.text = text;
	}
	public static void main(String[] args){
		Tweet tweet1 = new Tweet();
		tweet1.association = 2;
		System.out.println(tweet1);
	}
}
