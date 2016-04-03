package webapp;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import classes.Config;

import com.sun.jersey.api.view.Viewable;

@Path("webapp")
public class TwitterWebApp {

	/*
	 * ip = 10.166.30.183
	 */
	public static Main main;
	
	@GET
	public String getTest(){
		return "Rest App is working fine, press back";
	}
	
	@POST
	@Path("readfiles")
	public Viewable getClassFrequency(@FormParam("path") String dir,@FormParam("alpha") double alpha, @FormParam("beta") double beta){
		main = new Main();
		main.setLdaParameters(alpha, beta);
		main.readFolder(dir);
		return new Viewable("/jsp/classdescription",main);
	}
	
	@POST
	@Path("processlda")
	public Viewable processlda(@FormParam("trainingSize") int sampling){
		if (sampling != 0)
			main.setTweetCorpus(0, sampling);
		main.beginLDA();
		//main.readOldLDA("C:/Users/kshitiz/Documents/workspace/TwitterSummarizer/WebContent/Files/LdaData/lda_a_1.0_b_0.1");
		return new Viewable("/jsp/lda",main);
	}
	
	@GET
	@Path("allMisclassified")
	public Viewable allMisclassified(){
		main.getLda().writeIncorrectTweetsToHTML();
		return new Viewable("/tweets.html","HTML REDIRECTION");
	}
	
	@POST
	@Path("mainsummary")
	public void summaryForCurrentClasses(@FormParam("numStories") int topStories, @Context HttpServletResponse response){
		if (topStories == 0)
			topStories = 5;
		main.generateTopStories(topStories);
		//return new Viewable("/jsp/summary",main);
		try {
			response.sendRedirect("/TwitterSummarizer/jsp/summary.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@POST
	@Path("summarize")
	public Viewable summarize(@FormParam("numTopics") int topStories,@FormParam("path") String dirpath){
		main = new Main();
		if (topStories == 0)
			topStories = 5;
		main.readFolder(dirpath);
		main.generateTopStories(topStories);
		return new Viewable("/jsp/summary",main);
	}
	
	
	@POST
	@Path("classify")
	public Viewable classify(@FormParam("tweet") String tweet){
		String result = tweet + "::" + main.getLda().getWordtopic().analyse(tweet);
		return new Viewable("/jsp/classify",result);
	}
	
}
