package com.target.product.aggregator.model;

public class ProductPrice {
    
    private String id;
        
    private Double price;
    
    private String currency;

    public ProductPrice() {
    }

	public ProductPrice(String product_id, Double price, String currency) {
		super();
		this.id = product_id;
		this.price = price;
		this.currency = currency;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
