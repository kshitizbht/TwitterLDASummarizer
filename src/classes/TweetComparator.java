package classes;

import java.util.Comparator;

public class TweetComparator implements Comparator<Tweet>{

	@Override
	public int compare(Tweet arg0, Tweet arg1) {
		double pcent1 = arg0.association;
		double pcent2 = arg1.association;
		if(pcent1 > pcent2)
			return -1;
		else if (pcent1 < pcent2)
			return 1;
		else
			return 0;
	}
	
}