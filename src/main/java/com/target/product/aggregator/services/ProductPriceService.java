package com.target.product.aggregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.target.product.price.domain.ProductPrice;

import com.target.product.aggregator.model.Product;


@Service
@PropertySource("classpath:config.properties")
public class ProductPriceService {
	
	@Value( "${product.price.api.url}" )
	private String productPriceApiBaseUrl;
	private final RestTemplate productPriceService = new RestTemplate();
	
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);

	public ProductPrice getProductPrice(String productId) throws Exception {
		ProductPrice productPrice = null;
		try {
			productPrice = productPriceService.getForObject(productPriceApiBaseUrl + "/product-price/" + productId, ProductPrice.class);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return productPrice;
	}
	
	public void updateProductPrice(Product product)  {
		ProductPrice productPrice = new ProductPrice(product.getId(), product.getCurrentPrice().getValue(), product.getCurrentPrice().getCurrency_code());
		try {
			productPriceService.put(productPriceApiBaseUrl +"/product-price", productPrice);	
		}catch(RestClientException restException) {
			logger.error("Failed to update product price information for productId:" + product.getId());
		}
	}
	
}
