package searchEngine;

import java.util.ArrayList;
import java.util.Stack;


/**
 * @author Armin EsmaeilZadeh
 * @Date Spring 2015
 * @Class Advance Database
 * @Professor Kazem Taghve Ph.D
 */

public class InfixToPostfix {

	public static ArrayList<String> postfix = new ArrayList<String>();
	
	
	private boolean isOperator(String c) {
		if (c.contains("or") || c.contains("and") || c.contains("not"))
			return true;
		return false;
	}


	private boolean checkPrecedence(String c1, String c2) {
		
		if ((c2.contains("not")) && (c1.contains("not")))
			return true;
		else if ((c2.contains("or") || c2.contains("and"))
				&& (c1.contains("or") || c1.contains("and") || c1.contains("not")))
			return true;
		else
			return false;
	}

	public void convert(ArrayList<String> infix) {
		
		System.out.printf("%-8s%-10s%-15s\n", "Input", "Stack", "Postfix");
		
		Stack<String> s = new Stack<>(); 
		s.push("#");
		
		Stack<String> s3 = new Stack<>(); 
		s3.push("#");
		
		ArrayList<String> postfix2 = new ArrayList<String>();
		
		System.out.printf("%-8s%-10s%-15s\n", "", s, postfix);
		
		if(infix.size() <= 4 || (infix.size() > 4 
				&& !(infix.get(1).contains("or") && infix.get(2).contains("not")))){
			
			for (int i = 0; i < infix.size(); i++) {
				String inputSymbol = infix.get(i); 
				if (isOperator(inputSymbol)){
					if(isOperator(infix.get(i+1))){
						s.push(infix.get(i));
						s.push(infix.get(i+1));
						s.push(infix.get(i+2));
						i = i+3;
					}else{
					while (checkPrecedence(inputSymbol, s.peek()))
						postfix.add(s.pop());
					s.push(inputSymbol);
					}
				} else if (inputSymbol.contains("("))
					s.push(inputSymbol);
				else if (inputSymbol.contains(")")) {
					while (s.peek() != "(")
						postfix.add(s.pop());
					s.pop();
				}else
					postfix.add(inputSymbol);
					System.out.printf("%-8s%-10s%-15s\n", "" + inputSymbol, s, postfix);
			}
		
		}else if(infix.size() > 4){
			
			ArrayList<String> list1 = new ArrayList<String>();
			ArrayList<String> list2 = new ArrayList<String>();
			
			for(int i=0; i<4 ; i++)
				list1.add(infix.get(i));
			
			for(int i=4; i<infix.size() ; i++)
				list2.add(infix.get(i));
			
			for (int i = 0; i < list1.size(); i++) {
				String inputSymbol = list1.get(i); 
				if (isOperator(inputSymbol)){
					if(isOperator(list1.get(i+1))){
						s.push(list1.get(i));
						s.push(list1.get(i+1));
						s.push(list1.get(i+2));
						i = i+3;
					}else{
					while (checkPrecedence(inputSymbol, s.peek()))
						postfix.add(s.pop());
					s.push(inputSymbol);
					}
				} else if (inputSymbol.contains("("))
					s.push(inputSymbol);
				else if (inputSymbol.contains(")")) {
					while (s.peek() != "(")
						postfix.add(s.pop());
					s.pop();
				}else
					postfix.add(inputSymbol);
			}
			
			for (int i = 0; i < list2.size(); i++) {
				String inputSymbol = list2.get(i); 
				if (isOperator(inputSymbol)){
					while (checkPrecedence(inputSymbol, s3.peek()))
						postfix2.add(s3.pop());
					s3.push(inputSymbol);
				}else if (inputSymbol.contains("("))
					s3.push(inputSymbol);
				else if (inputSymbol.contains(")")) {
					while (s3.peek() != "(")
						postfix2.add(s3.pop());
					s3.pop();
				}else
					postfix2.add(inputSymbol);
			}
			postfix2.add(list2.get(1));
			postfix.addAll(postfix2);
		}
			
		
		while (s.peek() != "#") {
			postfix.add(s.pop());
			System.out.printf("%-8s%-10s%-15s\n", "", s, postfix);
		}
	}

	public static void main(String[] args) {
		
		InfixToPostfix obj = new InfixToPostfix();
		
		System.out.println("================================");
		System.out.println("Converting the Query From Infix To Postfix : ");
		obj.convert(Query.query);
		System.out.println("Postfix : " + postfix);

	}
}