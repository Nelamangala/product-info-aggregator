//package com.target.product.aggregator;
//
//import java.io.IOException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.integration.support.json.JsonObjectMapper;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//public class Test2 {
//
//	public static void main(String[] args) throws RestClientException, IOException, JSONException {
//		String readTree = new RestTemplate().getForObject("http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics", String.class);
//		String productName = null;
//
////		JSONObject jsonObject = new JSONObject(readTree);
////        
////		if(jsonObject.has("product") ) {
////			jsonObject = jsonObject.getJSONObject("product");
////			if(jsonObject.has("item")) {
////				jsonObject = jsonObject.getJSONObject("item");
////				if(jsonObject.has("product_description")) {
////					jsonObject = jsonObject.getJSONObject("product_description");
////					if(jsonObject.has("title")) {
////						productName = jsonObject.getString("title");
////					}
////				}
////			}
////		}
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode readTree2 = mapper.readTree(readTree);
//		if(readTree2.has("product") ) {
//			readTree2 = readTree2.get("product");
//			if(readTree2.has("item")) {
//				readTree2 = readTree2.get("item");
//				if(readTree2.has("product_description")) {
//					readTree2 = readTree2.get("product_description");
//					if(readTree2.has("title")) {
//						productName = readTree2.get("title").asText();
//					}
//				}
//			}
//		}
//
//		System.out.println(productName);
//	}
//
//}
