package org.taxes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.taxes.beans.Input;
import org.taxes.beans.Item;

public class Core{
	
	private static final String INPUT_FILE = "src/inputs/Input.txt";
	private static final String OUTPUT_FILE = "src/outputs/Output.txt";
	private static final DecimalFormat df = new DecimalFormat("#,##0.00");
	
	public static void main(String[] args) throws IOException {
		Scanner sc = null;
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintWriter pw = new PrintWriter(fos);
        sc = new Scanner(inputFile);
        sc.useDelimiter(System.getProperty("line.separator"));
    	try{
    		//every Input object contains a list of items
    		List<Input> inputs = new ArrayList<Input>();
    		Input input = new Input();
    		//every line is a new Item
    		List<Item> items = new ArrayList<Item>();
    		while (sc.hasNext()) {
    			String line = sc.next();
    			//ignore empty lines
    			if("".equals(line.trim())) {
    				continue;
    			}
    			//close previous input and create a new one
    			else if(line.length() > 3 && "input".equals(line.substring(0, 5).toLowerCase())) {
    				if(items.size() > 0) {
	    				input.setItems(items);
	    				inputs.add(input);
	    				input = new Input();
	    	    		items = new ArrayList<Item>();
    				}
    			}
    			//add line to input
    			else {
    				Item i = new Item(line);
    				items.add(i);
    			}
    		}
    		//add last input
    		input.setItems(items);
			inputs.add(input);
			
			int cont = 1;
			for(Input in : inputs) {
				String receipt = calculateReceipt(in);
				pw.write("Output " + cont + ":\n" + receipt+"\n");
				cont ++;
			}
	        pw.close();
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    		pw.close();
    		sc.close();
    		fos.close();
    	}
    }

	//calculate taxes and output string
	private static String calculateReceipt(Input in) {
		String out = "";
		BigDecimal salesTaxes = new BigDecimal("0");
		BigDecimal total = new BigDecimal("0");
		
		for(Item i : in.getItems()) {
			BigDecimal taxAdded = new BigDecimal("0");
			if(i.isImported()) {
				double percentage = Math.ceil(i.getPrice() * 0.05 * 20.0) / 20.0;
				BigDecimal percentageB = new BigDecimal(percentage, MathContext.DECIMAL64);
				taxAdded = taxAdded.add(percentageB.setScale(2, BigDecimal.ROUND_UP));
//				taxAdded = Math.round(percentage * 20.0) / 20.0;
			}
			if(!i.isExempted()) {
				double percentage = Math.ceil(i.getPrice() * 0.1 * 20.0) / 20.0;
				BigDecimal percentageB = new BigDecimal(percentage, MathContext.DECIMAL64);
				taxAdded = taxAdded.add(percentageB.setScale(2, BigDecimal.ROUND_UP));
			}
			BigDecimal priceB = new BigDecimal(i.getPrice(), MathContext.DECIMAL64);
			BigDecimal newPrice = priceB.add(taxAdded);
			
			out += i.getQuantity() + " " + i.getName() + ": " + df.format(newPrice) + "\n";
			total = total.add(newPrice);
			salesTaxes = salesTaxes.add(taxAdded);
		}
		
		out += "Sales Taxes: " + df.format(salesTaxes) + "\n";
		out += "Total: " + df.format(total) + "\n";
		
		return out;
	}
	
}