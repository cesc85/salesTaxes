package org.taxes.beans;

import org.taxes.utility.Vocabulary;

public class Item{
	private int quantity;
	private String name;
	private double price;
	private boolean isImported;
	private boolean isExempted;
	
	public Item(String fullLine) {
		try {
			int nameIndex = fullLine.indexOf(" ");
			int priceIndex = fullLine.lastIndexOf(" at");
			quantity = Integer.parseInt(fullLine.substring(0, nameIndex));
			name = fullLine.substring(nameIndex + 1, priceIndex).toLowerCase();
			price = Double.parseDouble(fullLine.substring(priceIndex + 3));
			isImported = fullLine.contains("imported");
			isExempted = Vocabulary.isExempted(name);
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid input");
		}
		
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isImported() {
		return isImported;
	}

	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	public boolean isExempted() {
		return isExempted;
	}

	public void setExempted(boolean isExempted) {
		this.isExempted = isExempted;
	}
	
}