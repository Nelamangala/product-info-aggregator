package com.target.product.aggregator.api;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.product.aggregator.async.ProductAggregatorAsync.ProductAggregatorServiceResponse;
import com.target.product.aggregator.model.Price;
import com.target.product.aggregator.model.Product;

public class ProductInfoAggregatorIntegrationTest {

	@Test
	public void testGetProductAggregationApi() throws ClientProtocolException, IOException {
		String productId = "13860428";
		HttpUriRequest request = new HttpGet( "https://target-product-info-aggregator.cfapps.io/products/" + productId);
		HttpResponse response = new DefaultHttpClient().execute( request );
		ObjectMapper mapper = new ObjectMapper();
		String responseAsString = EntityUtils.toString(response.getEntity());
		ProductAggregatorServiceResponse responseValue = mapper.readValue(responseAsString, ProductAggregatorServiceResponse.class);
		
		assertThat(response.getStatusLine().getStatusCode(), Matchers.is(HttpStatus.SC_OK));
		assertNotNull(responseValue);
		assertTrue(responseValue.getMessage().contains("Success"));
		assertNotNull(responseValue.getProduct());
		assertThat( responseValue.getProduct().getId(), Matchers.is(productId) );
		assertNotNull(responseValue.getProduct().getName());
		assertTrue(!responseValue.getProduct().getName().isEmpty());
		assertNotNull(responseValue.getProduct().getCurrentPrice());
		assertTrue( responseValue.getProduct().getCurrentPrice().getValue() > 0d);

	}
	
	@Test
	public void testPutProductPriceAggregationApi() throws ClientProtocolException, IOException {
		String productId = "13860428";
		Double price = 9999.99d;
		Product product = new Product();
		product.setId(productId);
		Price prodPrice = new Price(price, "USD");
		product.setCurrentPrice(prodPrice);
		
		
		HttpPut request = new HttpPut( "https://target-product-info-aggregator.cfapps.io/products/" + productId);
		request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(product)));
		request.addHeader("content-type", "application/json");
		HttpResponse response = new DefaultHttpClient().execute( request );
		ObjectMapper mapper = new ObjectMapper();
		String responseAsString = EntityUtils.toString(response.getEntity());
		ProductAggregatorServiceResponse responseValue = mapper.readValue(responseAsString, ProductAggregatorServiceResponse.class);
		
		assertThat(response.getStatusLine().getStatusCode(), Matchers.is(HttpStatus.SC_OK));
		assertNotNull(responseValue);
		assertNotNull(responseValue.getProduct());
		assertThat( responseValue.getProduct().getId(), Matchers.is(productId) );
		assertNotNull(responseValue.getProduct().getCurrentPrice());
		assertThat( responseValue.getProduct().getCurrentPrice().getValue(), Matchers.is(price)); // Validates returned price is the updated value

	}
}
