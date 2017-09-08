package com.target.product.aggregator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Price {
	
	private Double value;
	private String currency_code;
	
	public Price() {
		
	}
	public Price(Double value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}
	
	@ApiModelProperty(position = 1, required = true, value = "Product price, has to be positive double value")
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
