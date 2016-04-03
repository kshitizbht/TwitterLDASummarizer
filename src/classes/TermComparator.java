package classes;

import java.util.Comparator;

public class TermComparator implements Comparator<Term>{

	@Override
	public int compare(Term arg0, Term arg1) {
		double pcent1 = arg0.percentage;
		double pcent2 = arg1.percentage;
		if(pcent1 > pcent2)
			return -1;
		else if (pcent1 < pcent2)
			return 1;
		else
			return 0;
	}
	
}