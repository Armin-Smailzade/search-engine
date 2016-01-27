package searchEngine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class CranQuery {

	
	public static String cranQueryFile = SearchEngine.directory + "SearchEnginePhaseTwo/src/documents/cranQuery.txt";
	
	public static ArrayList<String> list = new ArrayList<String>();
	
	public static void main(String args[]) throws FileNotFoundException{
		
		
		CranQuery cran = new CranQuery();
		cran.chopCranQuery();
		System.out.println(list);
		
	}
	
	
	public void chopCranQuery() throws FileNotFoundException {

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		
		try {
			fis = new FileInputStream(cranQueryFile);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			String line = null;

			while ((line = br.readLine()) != null) {

				if (!line.matches(".I 00"))
					continue;
				else if (line.matches(".I 00")) {

					while ((line = br.readLine()) != null) {
						if (line.matches(".I 002"))
							break;
						else
							list.add(line);
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
	}
}
