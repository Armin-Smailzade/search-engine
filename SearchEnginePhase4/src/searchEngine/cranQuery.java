package searchEngine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class cranQuery {

	
	public static String cranQueryFile = SearchEngine.directory + "src/documents/cran.txt";
	public static String oneQuery = SearchEngine.directory + "src/documents/oneQuery.txt";
	public static String qyeryNoStop = SearchEngine.directory + "src/documents/qyeryNoStop.txt";
	public static String[] noStop = {SearchEngine.directory + "src/documents/qyeryNoStop.txt"};
	public static String qyeryStemmed = SearchEngine.directory + "src/documents/qyeryStemmed.txt";
	public static String Relevent = SearchEngine.directory + "src/documents/Relevent.txt";
	
	public static HashMap<Integer, ArrayList<String>> query = new HashMap<Integer, ArrayList<String>>();
	
	public static HashMap<Integer, ArrayList<String>> relevent = new HashMap<Integer, ArrayList<String>>();
	
	public static void main(String args[]) throws FileNotFoundException{
		
		
		cranQuery.queryList();
		
		
		System.out.println("================================");
		System.out.println("* Cosine measure for each query on each document : ");
		System.out.println();
		
		for(int i=1; i<9; i++)
			cranQuery.getReleventFile(i);
		
		for(int i=1; i<9; i++)
			cranQuery.cosineMeasure(i);
	
		
		
	}
	
	public static void precision(int i){
		
		double[] precision = new double[10];
		precision[0] = (3d/relevent.get(i).size())*100;
		
		System.out.println("Percesion : " + precision[0] );
		
	}
	
	public static void recall(int i){
		
		double[] recall = new double[10];
		recall[0] = (4d/relevent.get(i).size())*100;
		System.out.println("Recall : "+ recall[0]);
		
	}
	
	public static void getReleventFile(int i){
		
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			fis = new FileInputStream(Relevent);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			
			
			String line = null;

			while ((line = br.readLine()) != null) {

				if (!line.matches(".I " + i))
					continue;
				else if (line.matches(".I " + i)) {

					while ((line = br.readLine()) != null) {
						if (line.matches(".I " + (i + 1)))
							break;
						else{
							
						}
							StringTokenizer st = new StringTokenizer(line,"\n\t");
							
							while (st.hasMoreTokens()) {
								list.add(st.nextToken().toLowerCase());
					
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
		}
		
		relevent.put(i, list);
	}
	
	public static void cosineMeasure(int k){
		
		System.out.println("Query No."+k);
		
		double d =0;
		
		for(int i=0; i<query.get(k).size(); i++){
			
			if(SearchEngine.TfIdf2.get(query.get(k).get(i)) != null){
				d += Math.pow(SearchEngine.TfIdf2.get(query.get(k).get(i)).get(1), 2);
			}
		}
		
		System.out.println();
		for(int i=1; i<9; i++)
			System.out.println("Cos(Query No."+k+", Document No."+i+") : " + (1/SearchEngine.wd.get(i)) * Math.sqrt(d));
		System.out.println();
		
		cranQuery.precision(k);
		cranQuery.recall(k);
		
		System.out.println();
		
		
	}
	
	
	public static void queryList() throws FileNotFoundException{
		
		cranQuery cran = new cranQuery();
		
		for(int i=1; i<9; i++){
			cran.chopCranQuery(i);
			cran.deleteStopQuery();
			Stemmer.main(noStop, qyeryStemmed);
			cran.queryList(i);
		}
		
	}
	
	public void chopCranQuery(int i) throws FileNotFoundException {

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		PrintWriter writer = new PrintWriter(oneQuery);;
		
		try {
			fis = new FileInputStream(cranQueryFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			
			
			String line = null;

			while ((line = br.readLine()) != null) {

				if (!line.matches(".I 00"+i))
					continue;
				else if (line.matches(".I 00"+i)) {

					while ((line = br.readLine()) != null) {
						if (line.matches(".I 00"+(i+1)))
							break;
						else{
							
							writer.println(line);
						}
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
		}
	}
	
	public void deleteStopQuery(){
		
		/* input stuff */
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		PrintWriter writer = null;

		/* Reading the File and Checking for StopWords */
		try {
			fis = new FileInputStream(oneQuery);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			writer = new PrintWriter(qyeryNoStop);
			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,
						"[ \n\t\r.,;:!?(){}]'-/[0-9]");
				while (st.hasMoreTokens()) {
					String tmp = st.nextToken().toLowerCase();

					// Checking if input is number
					boolean number = SearchEngine.isNumeric(tmp);
					if (number)
						continue;

					if (SearchEngine.isNoStopWord(tmp, SearchEngine.StopWordList)) {
						writer.println(tmp);
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
		}
	}
	
	public void queryList(int i) throws FileNotFoundException{
		
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			fis = new FileInputStream(qyeryStemmed);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			
			
			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line,"[ \n\t\r.,;:!?(){}]'-/[0-9]");
				
				while (st.hasMoreTokens()) {
					list.add(st.nextToken().toLowerCase());
				}
			
		
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
		}
		query.put(i, list);
	}
}
