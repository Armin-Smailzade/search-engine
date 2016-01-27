package searchEngine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
//import java.util.Scanner;

/**
 * @author Armin EsmaeilZadeh
 * @Date Spring 2015
 * @Class Advance Database
 * @Professor Kazem Taghve Ph.D
 */

public class SearchEngine {

	/**
	 * Directories:
	 * 
	 * All the files are already in the
	 * ../src/documents/... folder. Please set the
	 * directory for the directory variable mentioned below:
	 * 
	 */
	
	//Please set the directory here:
	public static String directory = "/.../SearchEnginePhase4";
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String DocumentFile = directory + "src/documents/Document.txt";
	public static String StopWordFile = directory + "src/documents/stopwords.txt";
	public static String ChoppedDocumentFile = directory + "src/documents/ChoppedDocument.txt";
	public static String[] NoStop = { directory + "src/documents/NoStop.txt" };
	public static String StemmedFile = directory + "src/documents/stemmedFile.txt";
	public static String OutPut = directory + "src/documents/Phase1.txt";
	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/** Creating the HashMap accessible from all parts of the program */
	public static HashMap<String, List<List<Integer>>> wordMap = new HashMap<String, List<List<Integer>>>();

	public static ArrayList<String> StopWordList = new ArrayList<String>();

	
	public static HashMap<Integer, Double> wd = new HashMap<Integer, Double>();
	public static HashMap<Integer, HashMap<String, List<Double>>> h = new HashMap<Integer, HashMap<String, List<Double>>>();
	
	public static HashMap<String, List<Double>> TfIdf2 = new HashMap<String, List<Double>>();
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Main Method
	 * 
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {


		// /////////////////////////////
		// Creating The inverted File
		// ////////////////////////////

		SearchEngine se = new SearchEngine();
		// Creating the List of Stop Words
		StopWordList = se.getStopWordList();
		// Create the inverted file
		se.InvertFile(se);
		//vector space info
		se.VectorSpaceInfo();
		
		cranQuery.main(args);
		
		
	}


	
	
	public void VectorSpaceInfo(){
		
		System.out.println("* This is the TF and IDF for each word in each document : Document = { word = [TF, IDF] } : ");
		
		//Wd for doc 1
		for(int j = 1; j<= 20 ; j++){
			
			
			HashMap<String, List<Double>> TfIdf = new HashMap<String, List<Double>>();
			
			
			double d = 0;
			
			for (String key : wordMap.keySet()) {
				
				  for(int i=0; i<wordMap.get(key).size(); i++){
					  
					  List<Double> list = new ArrayList<Double>();
					  
			    		if(wordMap.get(key).get(i).get(0) == j){
			    			
			    			//TF
			    			list.add((1 + Math.log(wordMap.get(key).get(i).get(1))));
			    			//wd
			    			d += Math.pow((1 + Math.log(wordMap.get(key).get(i).get(1))), 2);
			    			//IDF
			    			list.add(Math.log(1+5/wordMap.get(key).size()));
			    			//wd
			    			TfIdf.put(key, list);
			    			TfIdf2.put(key, list);
			    		}
				  }
				  wd.put(j, Math.sqrt(d));
			}
			h.put(j, TfIdf);
		}
		
		
		System.out.println();
		System.out.println("Result : " + h);
		
		//calculate Wd for each document
		System.out.println("================================");
		System.out.println("* This is the Wd for each document : Document = [Wd]");
		System.out.println();
		System.out.println("Result : " + wd);
		
	}
	
	
	public void InvertFile(SearchEngine se) throws FileNotFoundException{
		
		for (int DocumentId = 1; DocumentId <= 20; DocumentId++) {

			se.chopDoc(DocumentId);
			se.deleteStops(StopWordList);
			Stemmer.main(NoStop, StemmedFile);
			se.addHashMap(DocumentId);
		}
		
		System.out.println("================================");
		System.out.println("* Inverted File : word=[docId, frequency]");
		System.out.println();
		System.out.println("Result : " + wordMap);
		System.out.println("================================");
		
	}
	/**
	 * GetStopWordList will take the StopWordFile and make a List of all the
	 * stop words
	 **/
	public ArrayList<String> getStopWordList() {

		/* input stuff */
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;

		/* ArrayList */
		ArrayList<String> StopWordList = new ArrayList<String>();

		/* Array Counter */
		int i = 0;

		/* Reading the File and Adding StopWords to the ArrayList */
		try {
			fis = new FileInputStream(StopWordFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\n");

				while (st.hasMoreTokens()) {
					StopWordList.add(i, st.nextToken().toLowerCase());
					i++;
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

		return StopWordList;
	}

	/**
	 * Gets the 1400 DocumentFile, cuts one document of it and save it into the
	 * ChoppedDocumentFile for processing
	 **/
	public void chopDoc(int i) throws FileNotFoundException {

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		PrintWriter writer = new PrintWriter(ChoppedDocumentFile);
		try {
			fis = new FileInputStream(DocumentFile);
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
						else
							writer.println(line);
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

	/**
	 * DeleteStops will get a document and throws away all the stop words from
	 * it
	 **/
	public void deleteStops(ArrayList<String> StopWordList) {

		/* input stuff */
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		PrintWriter writer = null;

		/* Reading the File and Checking for StopWords */
		try {
			fis = new FileInputStream(ChoppedDocumentFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			writer = new PrintWriter(NoStop[0]);
			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,
						"[ \n\t\r.,;:!?(){}]'-/[0-9]");
				while (st.hasMoreTokens()) {
					String tmp = st.nextToken().toLowerCase();

					// Checking if input is number
					boolean number = isNumeric(tmp);
					if (number)
						continue;

					if (isNoStopWord(tmp, StopWordList)) {
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

	/**
	 * Checking every word with the StopWord list
	 **/
	public static boolean isNoStopWord(String tmp, List<String> list) {
		for (String stopword : list) {
			if (stopword.equals(tmp)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if the String contains a number
	 **/
	public static boolean isNumeric(String str) {
		// match a number with optional '-' and decimal.
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	/**
	 * Putting words into the HashMap
	 **/
	public void addHashMap(int documentId) {

		/* input stuff */
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;

		try {
			fis = new FileInputStream(StemmedFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, " ");

				while (st.hasMoreTokens()) {
					String tmp = st.nextToken();

					List<Integer> list2 = new ArrayList<Integer>();
					List<List<Integer>> list1 = new ArrayList<List<Integer>>();
					boolean foundDocument = false;

					if (wordMap.containsKey(tmp)) {
						// if the word is in this doc
						for (int j = 0; j < wordMap.get(tmp).size(); j++) {
							List<Integer> listTmp = wordMap.get(tmp).get(j);
							if (listTmp.get(0).equals(documentId)) {
								int fr = listTmp.get(1) + 1;
								listTmp.set(1, fr);
								foundDocument = true;
								break;
							} else {
								continue;
							}
						}
						// if not in this doc
						if (!foundDocument) {
							list2.add(documentId);
							list2.add(1);
							list1 = wordMap.get(tmp);
							list1.add(list2);
						}
					} else {
						// Seeing the word for the first time !!
						list2.add(documentId);
						list2.add(1);
						list1.add(list2);
						wordMap.put(tmp, list1);
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
		// System.out.println(wordMap);
	}

	/**
	 * Gets the Entry Set of the Map, puts it in a list and then sorts the list
	 **/
	public List<Entry<String, List<List<Integer>>>> sortByKey() {

		List<Entry<String, List<List<Integer>>>> list = new ArrayList<Entry<String, List<List<Integer>>>>(
				wordMap.entrySet());

		Collections.sort(list,
				new Comparator<Map.Entry<String, List<List<Integer>>>>() {
					public int compare(
							Map.Entry<String, List<List<Integer>>> o1,
							Map.Entry<String, List<List<Integer>>> o2) {
						return (o1.getKey()).compareTo(o2.getKey());
					}
				});
		return list;
	}
}
