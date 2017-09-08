package com.target.product.aggregator.async;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import com.target.product.aggregator.services.ProductGeneralInfoService;

public class ProductGeneralInfoAsyncTask implements Callable<String>{
	
	private ProductGeneralInfoService productInfoService;
	private String productId;
	private static final Logger logger = LoggerFactory.getLogger(ProductGeneralInfoAsyncTask.class);
	
	
	public ProductGeneralInfoAsyncTask(ProductGeneralInfoService productInfoService, String productId) {
		this.productInfoService = productInfoService;
		this.productId = productId;
	}
	

	@Override
	public String call() throws Exception {
		String productName = null;
		try {
			productName = productInfoService.getProductName(productId);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return productName;
	}
}
