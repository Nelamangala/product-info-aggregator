package com.target.product.aggregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@Service
public class ProductGeneralInfoService {
	private final RestTemplate productInfoService = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(ProductGeneralInfoService.class);
	

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
			JsonObject jsonObject = new Gson().fromJson(productInfoStr, JsonObject.class);
			if(jsonObject.has("product") ) {
				jsonObject = jsonObject.getAsJsonObject("product");
				if(jsonObject.has("item")) {
					jsonObject = jsonObject.getAsJsonObject("item");
					if(jsonObject.has("product_description")) {
						jsonObject = jsonObject.getAsJsonObject("product_description");
						if(jsonObject.has("title")) {
							productName = jsonObject.get("title").getAsString();
						}
					}
				}
			}
		} catch (JsonSyntaxException e) {
			logger.error("Error parsing product name from JSON response");
			e.printStackTrace();
		}
		return productName;
	}
}
