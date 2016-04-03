package resources.lda;

import java.text.DecimalFormat;

public class Stat {

	private double accuracy;
	private double precision;
	private double recall;
	private double fmeasure;
	private double purity;
	private String[] metric;
	private int a,b,c,d;
	/**
	 * 
	 * @param a true positive
	 * @param b false negative
	 * @param c false positive
	 * @param d true negative
	 */
	public Stat(int a,int b,int c, int d){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.metric = new String[4];
		
		accuracy = (a+d)*1.0/(a+b+c+d);
		precision = a*1.0/(a+c);
		recall = a*1.0/(a+b);
		fmeasure = (2.0*a)/(2*a + b + c);
		purity = (1.0*a)/(a+b+c+d);
		
		DecimalFormat format = new DecimalFormat("##.##");
		metric[0] = format.format(accuracy);
		metric[1] = format.format(precision);
		metric[2] = format.format(recall);
		metric[3] = format.format(fmeasure);
	}
	
	public String[] getMetric(){
		return metric;
	}
	
	@Override
	public String toString(){
		return "\t--- ---\n\t"+a+"\t"+b+"\n\t"+c+"\t"+d;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getFmeasure() {
		return fmeasure;
	}

	public double getPurity() {
		return purity;
	}

	
}
