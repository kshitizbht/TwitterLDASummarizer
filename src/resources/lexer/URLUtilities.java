package resources.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class URLUtilities {
	
	private ArrayList<String> urlEndings;
	public URLUtilities(){
		urlEndings = new ArrayList<String>();
		urlEndings.add("com");
		urlEndings.add("edu");
		urlEndings.add("org");
		urlEndings.add("gov");
		urlEndings.add("net");
	}
	public String tiny2real(String urlstr){
		if (!urlstr.contains("http")){
			urlstr = "http://"+urlstr;
		}
		BufferedReader in = null;
		boolean realurl = checkURL(urlstr);
		if (realurl){
			URL url;
			try {
				url = new URL(urlstr);
				URLConnection conn = url.openConnection();
		        InputStream in2 = conn.getInputStream();
		        String result = conn.getURL().toString();
		        in2.close();
		        return result;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "link";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "link";
			}
	        
		}
		else{
			try
			{
				URL url = new URL(urlstr);
				HttpURLConnection uc = (HttpURLConnection)url.openConnection();
				uc.setInstanceFollowRedirects(false);
				uc.connect();
				in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String result =  (uc.getHeaderField("Location")); 
				if (result == null){
					return "link";
				}else{
					if (checkURL(result)){
						return result;
					}else{
						return tiny2real(result);
					}
				}
			}
			catch (MalformedURLException e)
			{
				//e.printStackTrace();
				return "link";
			}
			catch (IOException e)
			{
				//e.printStackTrace();
				return "link";
			}
			finally
			{
				try
				{
					if (null != in)
					{
						in.close();
					}
				}
				catch (IOException e)
				{
				// ignore
				}
			}
		}
		//return "link";//urlstr.substring(urlstr.indexOf("."),urlstr.indexOf(".")+3);
	}
	public boolean checkURL(String urlstr){
		for (String s: urlEndings){
			if (urlstr.contains(s)){
				return true;
			}
		}
		return false;
	}
	
	public String real2domain(String urlstr){
		int firstdot = urlstr.indexOf(".");
		int seconddot = urlstr.indexOf(".", firstdot+1);
		String domain="";
		if (firstdot < seconddot){
			domain = urlstr.substring(firstdot+1, seconddot);
			if (domain == null || domain.equals("")){
				return "";
			}
		}
		return domain;
	}
	
	public String tiny2domain(String urlstr){
		String real = tiny2real(urlstr);
		return real2domain(real);
	}
	
	public String getTitle(String urlstr){
		try{
			
			URL url = new URL(urlstr);
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			uc.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			while(in.ready()){
				String line = in.readLine();
				int title = line.indexOf("<title>")+7;
				int titleend = line.indexOf("</title>");
				if (title > 0 && titleend > 0){
					System.out.println(line.substring(title, titleend));
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	

	public static void main(String[] args){
		URLUtilities util = new URLUtilities();
		System.out.println(util.tiny2domain("www.google.com"));
		System.out.println(util.tiny2domain("http://t.co/YA5SRkBa"));
		System.out.println(util.tiny2domain("http://bit.ly/zh4lH7"));
		System.out.println(util.real2domain("mason.gmu.edu"));
	}
}
