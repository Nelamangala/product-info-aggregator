package com.target.product.aggregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.target.product.aggregator.model.Product;
import com.target.product.aggregator.model.ProductPrice;

@Service
public class ProductPriceService {
	private final RestTemplate productPriceService = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);

	public ProductPrice getProductPrice(String productId) throws Exception {
		ProductPrice productPrice = null;
		try {
			productPrice = productPriceService.getForObject("https://target-product-pricing.cfapps.io/product-price/" + productId, ProductPrice.class);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return productPrice;
	}
	
	public void updateProductPrice(Product product)  {
		try {
			productPriceService.put("https://target-product-pricing.cfapps.io/product-price/" + product.getId(), product);	
		}catch(RestClientException restException) {
			logger.error("Failed to update product price information for productId:" + product.getId());
		}
	}
	
}
