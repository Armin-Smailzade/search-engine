package searchEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Query {

	
	/**
	 * @author Armin EsmaeilZadeh
	 * @Date Spring 2015
	 * @Class Advance Database
	 * @Professor Kazem Taghve Ph.D
	 */
	
	/* Directories */
	
	private static String queryStem = SearchEngine.directory+ "/src/documents/queryStem.txt";
	private static String queryNoStop[] = {SearchEngine.directory+ "/src/documents/queryNoStop.txt"};
	private static String queryNoStop1 = SearchEngine.directory+ "/src/documents/queryNoStop.txt";
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static PrintWriter writer = null; 
	
	public static ArrayList<String> query = new ArrayList<String>();
	
	public static ArrayList<Integer> docId1 = new ArrayList<Integer>();
	public static ArrayList<Integer> docId2 = new ArrayList<Integer>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String args[]) throws FileNotFoundException{
			
		writer = new PrintWriter(queryNoStop1);
		
		//get the user input
		Query.input();
		
		//Stem it
		Stemmer.main(queryNoStop, queryStem);
		
		//Put the query into a list
		Query.inputList();
		
		InfixToPostfix.main(null);
		
		System.out.println();
		System.out.println("================================");
		System.out.println("Documents having your query : ");
		
		//Categorize 
		Query.cat();
		
	}	

	public static void cat(){
		
		if(!checkWords(InfixToPostfix.postfix)){
			System.out.println("The words you entered are not in the documents. Please Enter your query based on the inverted file.");
		}else{
		
			if(InfixToPostfix.postfix.size() == 1){
				
				for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).size(); j++)
					docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).get(j).get(0));
				
				System.out.println(docId1);
			}
			
			if(InfixToPostfix.postfix.size() == 3){
				
				Query.One();
				
				if(!docId1.isEmpty())
					System.out.println(docId1);
				else
					System.out.println("Doesn't have common documents!");
			}
			
			if(InfixToPostfix.postfix.size() == 4){
				
				for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).size(); j++)
					docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).get(j).get(0));
				
				for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(1)).size(); j++)
					docId2.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(1)).get(j).get(0));
				
				System.out.println("All the documents plus : "+docId1+" without : "+docId2);
			}
			
			
			if(InfixToPostfix.postfix.size() == 5){
				
				Query.One();
				
				if(InfixToPostfix.postfix.get(4).contains("or")){
					for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(3)).size(); j++)
						docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(3)).get(j).get(0));
				}else{
					for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(3)).size(); j++)
						docId2.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(3)).get(j).get(0));
					
					docId1.retainAll(docId2);
				}
					
				System.out.println(docId1);
			}
			
			if(InfixToPostfix.postfix.size() > 5){
				
				for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).size(); j++)
					docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(0)).get(j).get(0));
				
				for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(1)).size(); j++)
					docId2.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(1)).get(j).get(0));
				
				System.out.println("All the documents plus : "+docId1+" without : "+docId2);
			}
		}
	}
	
	
	public static void input() throws FileNotFoundException{
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("================================");
		System.out.println("Please Enter Youe Query Based on the Above Inverted File : ");
		
		writer.println(in.nextLine());
		
		in.close();
		
		writer.close();
	}
	
	public static void inputList() throws FileNotFoundException{
		
		Scanner s = new Scanner(new File(queryStem));
		
		while (s.hasNext()){	
				query.add(s.next());
		}
		
		s.close();
	}
	
	public static boolean checkWords(ArrayList<String> words){
		
		for(int i=0; i< words.size(); i++){
			if(words.get(i).contains("or") || words.get(i).contains("and") || words.get(i).contains("not"))
				continue;
			else{
				if(SearchEngine.wordMap.get(words.get(i)) == null)
					return false;
			}
		}
		return true;
	}
	public static void One(){
	
		for(int i=0; i < 3 ; i++){
			
			if(!InfixToPostfix.postfix.get(i).contains("or") && !InfixToPostfix.postfix.get(i).contains("and")){
				continue;
			}
			else{
				if(InfixToPostfix.postfix.get(i).contains("or")){
					
						for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-1)).size(); j++)
							docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-1)).get(j).get(0));
						for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-2)).size(); j++)
							docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-2)).get(j).get(0));
						
				}if(InfixToPostfix.postfix.get(i).contains("and")){
					
					for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-1)).size(); j++)
						docId1.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-1)).get(j).get(0));
					
					for(int j=0; j < SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-2)).size(); j++)
						docId2.add(SearchEngine.wordMap.get(InfixToPostfix.postfix.get(i-2)).get(j).get(0));
					
					docId1.retainAll(docId2);
					
					
				}
			}
		}
	}

}
