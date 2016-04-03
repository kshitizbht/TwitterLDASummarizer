package resources.algorithm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import classes.Config;

public class WekaBagWords2Ascii {
	public static String newline = System.getProperty("line.separator");
	
	public String convert(int row, String data){
		try{
			data = data.substring(data.indexOf("{")+1,data.lastIndexOf("}"));
			String[] datas = data.split(",");
			String result = "";
			for(String r : datas){
				result += row + " " + r.trim() + newline;
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return newline;
		}
	}
	
	public void sparseMatrixToAscii(String from, String to){
		
		try {
			String arffFile = FileUtils.readFileToString(new File(from));
			String dataSection = arffFile.split("@data")[1];
			String[] datum = dataSection.split(newline);
			String asciiFile = "";
			for(int i = 1; i < datum.length; i++){
				asciiFile += convert(i,datum[i-1]);
			}
			FileUtils.writeStringToFile(new File(to), asciiFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		String row1 = "{77 1,104 1,1210 1}";
		WekaBagWords2Ascii wa = new WekaBagWords2Ascii();
		System.out.println(wa.convert(1, row1));
		wa.sparseMatrixToAscii(Config.filesDir+"weka/preprocessedMatlabSNW.arff", Config.filesDir + "weka/bag.txt");
	}
}

