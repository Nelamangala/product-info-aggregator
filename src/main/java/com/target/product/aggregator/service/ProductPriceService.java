package com.target.product.aggregator.service;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.target.product.aggregator.model.ProductPrice;


public class ProductPriceService implements Callable<ProductPrice>{
	private final RestTemplate productPriceService;
	private String productId;
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);
	
	public ProductPriceService(RestTemplate restTemplate, String productId) {
		this.productPriceService = restTemplate;
		this.productId = productId;
	}

	@Override
	public ProductPrice call() throws Exception {
		ProductPrice productPrice = null;
		try {
			productPrice = productPriceService.getForObject("https://target-product-pricing.cfapps.io/product-price/" + productId, ProductPrice.class);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return productPrice;
	}
}
