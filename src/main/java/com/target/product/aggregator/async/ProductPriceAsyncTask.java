package com.target.product.aggregator.async;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import com.target.product.aggregator.model.ProductPrice;
import com.target.product.aggregator.services.ProductPriceService;


public class ProductPriceAsyncTask implements Callable<ProductPrice>{

	private ProductPriceService productPriceService;
	private String productId;
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceAsyncTask.class);
	
	
	public ProductPriceAsyncTask(ProductPriceService productPriceService, String productId) {
		this.productPriceService = productPriceService;
		this.productId = productId;
	}

	@Override
	public ProductPrice call() throws Exception {
		ProductPrice productPrice = null;
		try {
			productPrice = productPriceService.getProductPrice(productId);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return productPrice;
	}
	
	
}
