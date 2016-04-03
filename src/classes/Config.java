package classes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

public class Config {
	
	public static String tweetDir = "C:/Users/kshitiz/Documents/workspace/TwitterSummarizer/WebContent/tweets/";
	public static String filesDir = "C:/Users/kshitiz/Documents/workspace/TwitterSummarizer/WebContent/Files/";
	public static String webDir = "C:/Users/kshitiz/Documents/workspace/TwitterSummarizer/WebContent/";

	
	public static String inputFile			   = filesDir + "microsoft.xml";
	public static String vocabFile 			   = filesDir + "vocab.txt";
	public static String preprocessedTweetFile = tweetDir + "preprocessed.xml";
	public static String stopWordsFile		   = filesDir + "stopWords.txt";
	public static String preprocessedTextFile  = filesDir + "preprocessed.txt";
	public static String phi 				   = filesDir + "phi.txt";
	public static String wekaFile 			   = filesDir + "wekaoutput.arff";
	public static String htmlout			   = webDir + "tweets.html";
	public static String lda	  			   = filesDir + "LdaData/lda";
	public static String logfile			   = "";

	public static void createNewSession(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd"); 
		String dateStr = sdf.format(cal.getTime());
		logfile = filesDir +"logs/" + dateStr + ".log";
	}
	/*
	 * Appends data to log file
	 */
	public static void log(String msg){
		try {
			FileUtils.write(new File(logfile), msg, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Either create new file or append to existing one
	 */
	public static void log(String msg,boolean append){
		try {
			FileUtils.write(new File(logfile), msg, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
