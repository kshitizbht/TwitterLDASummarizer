package resources.lexer;
import java.io.*; 
import java.util.ArrayList;
import java.util.HashMap;
import classes.Tweet;
import classes.Config;
import classes.Vocabulary;
%% 
%class Lexer
%public
%unicode
%{
private String parsedText="";
private Stemmer stemmer= new Stemmer();
private	ArrayList<String> stopwords = new ArrayList<String>();
private URLUtilities urlutils = new URLUtilities();
private Vocabulary vocab = new Vocabulary();
private ArrayList<String> hashtags;
	public Lexer(){
		this.zzReader = new java.io.StringReader("Initialize Tweet Parser...");
		readStopWordsFromFile();
	}

	public static void main(String [] args) throws IOException 
	{ 
		String text = "usa u.s.a  also,am,among,an,and,any,are,as,at,be,because,been,but,";
		Lexer scanner = new Lexer();
		System.out.println(scanner.parseString(text));
		System.out.println(scanner.parseString("[\\]\\[~'!$%&*()-_+=|{}:;>.<,? i have"));
	}
	 
	public String parseString(String text){
	 hashtags = new ArrayList<String>();
	 try{
         	this.yyreset(new java.io.StringReader(text));
			while ( !this.zzAtEOF ){
			 	this.yylex();
			}
			String result = parsedText;
			parsedText = "";
			return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	return null;
	}
	
	public void readStopWordsFromFile(){
		try
		{
			String strFile = Config.stopWordsFile;
			BufferedReader br = new BufferedReader( new FileReader(strFile));
			String strLine = "";
			while( (strLine = br.readLine()) != null)
			{
				 String[] list = strLine.split(",");
				 for(String s: list)
					 stopwords.add(s);
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while reading csv file: " + e);                  
		}
	}
	
	public void add2vocab(String word){
		//if(!word.equals(""))
			this.vocab.add(word);
	}
	
	
	public Vocabulary getVocabulary(){
		return this.vocab;
	}
	
	public void addDomain(String yytxt){
		/*String domain = urlutils.tiny2domain(yytxt);
		if (domain != null && !domain.equals("") && !doman.equal("link")){
			add2vocab(domain);
			addWord(domain);
		}*/
	
	}
	
	public void addWord(String word){
		if (!stopwords.contains(word)){
			stemmer.add(word.toCharArray(),word.length());
			stemmer.stem();
			word = stemmer.toString();
			add2vocab(word);
			parsedText += word + " ";
		}
		
		//if(!word.equals(""))
			//if (!(Main.vocab.getFrequency(word) > 10)){
			//	parsedText += word + " ";
			//}
	}
	
	public ArrayList<String> getHashTags(){
		return hashtags;
	}

%}
%type Object
whitespace = [ \n\r\t]+
string =     [^ \n\r\t]+
url = [hH][tT][tT][pP][:][/][/]

www = [wW][wW][wW][.]
word = [a-zA-Z0-9]+
number = [0-9]+
ignoreChar = [\]\[~£â€°?´'!$%&*éº()+=|§{}:;>.<,‘’“”–—]+
%%
{url}?{www}[.]{word}[.]{word}		  {addDomain(yytext());}
{url}?{www}[.]{word}[.]{word}[/]{word} {addDomain(yytext());}
{url}{word}[.]{word}[/]{word}		 {addDomain(yytext());}
@{string} 			{/*Ignore Mentions*/}								
\?					{/*Ignore punctuation*/}
\-					{/*Ignore punctuation*/}
\_					{/*Ignore punctuation*/}
\^					{/*Ignore punctuation*/}
\\					{/*Ignore punctuation*/}
\[					{/*Ignore punctuation*/}
\]					{/*Ignore punctuation*/}
\"					{/*Ignore punctuation*/}
\,					{/*Ignore punctuation*/}
\'					{/*Ignore punctuation*/}
\/					{/*Ignore punctuation*/}
\\					{/*Ignore punctuation*/}
\…					{/*Ignore punctuation*/}
\@		            {/*Ignore Mentions*/}
{ignoreChar}    	{/*Ignore punctuation*/}
\#{word}			{/*Ignore hashtags*/
					//add label
					String hashtag = yytext().substring(1).toLowerCase();
					addWord(hashtag);
					hashtags.add(hashtag);
					}
{number}			{/*Ignore numbers*/}
{word}    			{
						String word = yytext().toLowerCase();
						addWord(word);
					}
{whitespace}		{/*Ignore */}
.					{
					//System.out.println("BAD::"+yytext());
					//TODO
					//remove the println at the end.
					}