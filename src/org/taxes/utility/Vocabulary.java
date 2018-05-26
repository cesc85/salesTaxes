package org.taxes.utility;

import java.util.ArrayList;
import java.util.List;

public class Vocabulary {

	//I create a list because all exempted categories will be treated in the same way
	//If we want a more scalable solution we can to create a Map<String, List<String>> where keys are categories
	private static List<String> exempted = new ArrayList<String>();
	
	static {
        exempted.add("book");
		exempted.add("chocolate");
		exempted.add("pill");
    }
	
	public static boolean isExempted(String name) {
		if(name == null)
			return false;
		if(name.trim().equals(""))
			return false;
		
		String[] words = name.split(" ");
		//imported class to singularize words
		Inflector inf = new Inflector(); 		
		for(int i = 0; i < words.length; i++) {
			if(exempted.contains(inf.singularize(words[i]))) {
				return true;
			}
		}
		
		return false;
	}
	
}