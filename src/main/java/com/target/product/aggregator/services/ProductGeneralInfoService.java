package com.target.product.aggregator.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductGeneralInfoService {
	private final RestTemplate productInfoService = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(ProductGeneralInfoService.class);
	private final ObjectMapper mapper = new ObjectMapper();
	

	public String getProductName(String productId) throws Exception {
		String productInfoStr = null;
		try {
			productInfoStr = productInfoService.getForObject("http://redsky.target.com/v2/pdp/tcin/" + productId + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics", String.class);	
		}catch(RestClientException restException) {
			logger.error("Failed to retrieve product price information for productId:" + productId);
		}
		
		return extractProductNameFromJson(productInfoStr);
	}


	private String extractProductNameFromJson(String productInfoStr) {
		String productName = null;
		if(productInfoStr == null) {
			return productName;
		}
		try {
			JsonNode readTree2 = mapper.readTree(productInfoStr);
			if(readTree2.has("product") ) {
				readTree2 = readTree2.get("product");
				if(readTree2.has("item")) {
					readTree2 = readTree2.get("item");
					if(readTree2.has("product_description")) {
						readTree2 = readTree2.get("product_description");
						if(readTree2.has("title")) {
							productName = readTree2.get("title").asText();
						}
					}
				}
			}
		} catch ( IOException e) {
			logger.error("Error parsing product name from JSON response");
			e.printStackTrace();
		}
		return productName;
	}
}
