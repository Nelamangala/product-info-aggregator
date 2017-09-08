package com.target.product.aggregator.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class Price {
	@DecimalMax("10000000000.0") @DecimalMin("0.0")
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
