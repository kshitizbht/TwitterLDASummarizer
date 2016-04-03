package resources.lda;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {

	private int[][] contigencyTable;
	private int[][] confusionMatrix;
	public ArrayList<Stat> stats;
	private String[] toStr;
	private int numTopics;
	
	public Statistics(int[][] matrix){
		confusionMatrix = matrix;
		int numTopics = matrix.length;
		contigencyTable = new int[matrix.length][matrix[0].length];
		stats = new ArrayList<Stat>();
		toStr = new String[matrix.length];
		process();
	}
	
	public void process(){
		int total = 0;
		for(int i = 0; i < confusionMatrix.length; i++){
			for(int j=0; j < confusionMatrix[i].length; j++){
				total +=confusionMatrix[i][j];
			}
		}
		for(int i = 0; i < confusionMatrix.length; i++){
			int a,b,c,d;
			int row = 0,col = 0;
			int[] mstat = confusionMatrix[i];
			int thisIndex = maxVal(mstat);
			contigencyTable[thisIndex] = mstat;
			a = confusionMatrix[i][thisIndex];
			String mrow = "Class " + thisIndex + "\t";
			for(int j=0; j < mstat.length; j++){
				row += mstat[j];
				mrow += "\t" + confusionMatrix[i][j];			
			}
			toStr[thisIndex] = mrow;
			b = row - a;
			
			for(int j=0; j < confusionMatrix.length; j++){
				col +=confusionMatrix[j][thisIndex];
			}
			c = col - a;			
			d = total - a - b - c;
			Stat s = new Stat(a,b,c,d);
			//System.out.println(s + "\tFmeasure= " +s.getFmeasure() );
			stats.add(s);
		}
	}
	public int maxVal(int[] array){
	    	int max = array[0];
	    	int maxIndex = 0;
	    	for(int i = 0; i < array.length; i++){
	    		if (max < array[i]){
	    			max = array[i];
	    			maxIndex = i;
	    		}
	    	}
	    	return maxIndex;
	    }
	
	public ArrayList<Stat> getStats() {
		return stats;
	}

	public int[] getRow(int row){
		return this.contigencyTable[row];
	}
	public int[][] getContigencyTable(){
		return this.contigencyTable;
	}
	 @Override
	 public String toString(){
		 String out = "\t\t\tPredicted Class\n\t\t";
		 for(int i=0;i<numTopics;i++)
			 out+=i+"\t";
		 out += "\n";
		 for(String s: toStr)
			 out += s +"\n";
		 return out;
	 }
	 
	 public void printAllInformation(){
		 System.out.println("Contigency Table");
		 System.out.println(this.toString());
		 String acc = "Accuracy  ";
		 String pre = "Precision ";
		 String rec = "Recall    ";
		 String f   = "F-measure ";
		 String pur = "Purity    ";
		 double purity = 0.0;
		 DecimalFormat format = new DecimalFormat("##.##");
		 for(Stat s : stats){
			 acc += "\t" + format.format(s.getAccuracy());
			 pre += "\t" + format.format(s.getPrecision());
			 rec += "\t" + format.format(s.getRecall());
			 f   += "\t" + format.format(s.getFmeasure());
			 pur += "\t" + format.format(s.getPurity());	
			 purity += s.getPurity();
		 }
		 System.out.println(acc + "\n" +pre + "\n" +rec + "\n" +f );//+"\n" + pur);
		 System.out.println("Overall Purity " + format.format(purity));
	 }
	 public static void main(String[] args){
		 int[][] mat = { {11,68,348,7,16},
				  {41,78,48,306,23},
				  {54,383,73,161,52},
				  {7,13,162,20,400},
				  {504,3,51,11,6}};
		 Statistics s = new Statistics(mat);
		 s.printAllInformation();
	 }
}
