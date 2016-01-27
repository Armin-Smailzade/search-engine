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

/**
* @author	Armin EsmaeilZadeh
* @Date		Spring 2015
* @Class	Advance Database
* @Professor	Kazem Taghve Ph.D	
*/

public class SearchEngine {

	/**
	 * Directories:
	 * 
	 * All the files are already in the ../SearchEngine_PhaseOne/src/documents/... folder.
	 * Please set the directory for the below mentioned files:
	 * 
	 * DocumentFile => 1400 document file
	 * StopWordFile => long version of the stop word list
	 * ChoppedDocumentFile => each time the program cuts a document from the 1400 docs and save it into this file
	 * NoStop => Gets the ChoppedDocument and throws away the stopWords
	 * StemmedFile => the Stemmed version of the one document without stop words
	 * OutPut => for output
	 * 
	 * 
	 * I have already ran the program on the 1400 documents and the results are in the OutPut File.
	 * 
	 */
	private static String DocumentFile = "/.../SearchEngine_PhaseOne/src/documents/Document.txt";
	private static String StopWordFile = "/.../SearchEngine_PhaseOne/src/documents/stopwords.txt";
	private static String ChoppedDocumentFile = "/.../SearchEngine_PhaseOne/src/documents/ChoppedDocument.txt";
	private static String[] NoStop = { "/.../SearchEngine_PhaseOne/src/documents/NoStop.txt" };
	private static String StemmedFile = "/.../SearchEngine_PhaseOne/src/documents/stemmedFile.txt";
	private static String OutPut = "/.../SearchEngine_PhaseOne/src/documents/Phase1.txt";
	
	
	private static PrintWriter writer = null;
	
	/** Creating the HashMap accessible from all parts of the program */
	private static HashMap<String, List<List<Integer>>> wordMap = new HashMap<String, List<List<Integer>>>();
	
	/** Main Method
	 * @throws FileNotFoundException */
	public static void main(String[] args) throws FileNotFoundException{

		// Class Object
		SearchEngine se = new SearchEngine();
		// Info info = new Info();

		// Getting a List of Stop Words
		ArrayList<String> StopWordList = new ArrayList<String>();
		StopWordList = se.getStopWordList();
		
		writer = new PrintWriter(OutPut);
		
		for (int DocumentId = 1; DocumentId <= 1400; DocumentId++) {
			
			se.chopDoc(DocumentId);
			se.deleteStops(StopWordList);
			Stemmer.main(NoStop, StemmedFile);
			HashMap<String, Integer> phase1 = se.phaseOne(DocumentId);
			
			writer.println("Document Id : "+DocumentId);
			writer.println("Number of Unique Terms in Document : " + phase1.size());
			writer.println("Word Frequency in this Document : [Term = Frequency]");
			writer.println(se.sortByKey1(phase1));
			writer.println();
		}
		writer.close();
	}
	
	/** GetStopWordList will take the StopWordFile and make a List of all the stop words*/
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
	
	/** Gets the 1400 DocumentFile, cuts one document of it and save it into the ChoppedDocumentFile for processing */
	public void chopDoc(int i)
			throws FileNotFoundException {

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
	
	/** DeleteStops will get a document and throws away all the stop words from it */
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
				// Tokenizing
				StringTokenizer st = new StringTokenizer(line, "[ \n\t\r.,;:!?(){}]'-/[0-9]");
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
	
	/** Checking every word with the StopWord list */
	private boolean isNoStopWord(String tmp, List<String> list) {
		for (String stopword : list) {
			if (stopword.equals(tmp)) {
				return false;
			}
		}
		return true;
	}

	/** Check if the String contains a number */
	public static boolean isNumeric(String str) {
		// match a number with optional '-' and decimal.
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	/** Putting words into the HashMap */
	public HashMap<String, Integer> phaseOne(int DocumentId){
		
		/* input stuff */
		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		HashMap<String, Integer> phase1 = new HashMap<String, Integer>();
		
		try {
			fis = new FileInputStream(StemmedFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, " ");
				
				while (st.hasMoreTokens()) {
					String tmp = st.nextToken();

					if(phase1.containsKey(tmp)){
						phase1.put(tmp, phase1.get(tmp)+1);
                    } else {
                    	phase1.put(tmp, 1);
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
		return phase1;
	}
	/** Gets the Entry Set of the Map, puts it in a list and then sorts the list */
	public List<Entry<String, Integer>> sortByKey1(Map<String, Integer> phase1) {

		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(phase1.entrySet());

		Collections.sort(list,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(
							Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						return (o1.getKey()).compareTo(o2.getKey());
					}
				});
		//System.out.println(list);
		return list;
	}
}
