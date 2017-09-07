package com.target.product.aggregator.model;

public class Price {
	private Double value;
	private String currency_code;
	
	public Price() {
		
	}
	public Price(Double value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	
}
