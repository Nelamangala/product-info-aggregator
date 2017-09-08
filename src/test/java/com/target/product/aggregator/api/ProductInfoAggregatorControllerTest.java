package com.target.product.aggregator.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.product.aggregator.ProductInfoAggregatorApplication;
import com.target.product.aggregator.async.ProductAggregatorAsync;
import com.target.product.aggregator.async.ProductAggregatorAsync.ProductAggregatorServiceResponse;
import com.target.product.aggregator.model.Price;
import com.target.product.aggregator.model.Product;
import com.target.product.aggregator.services.ProductPriceService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProductInfoAggregatorApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductInfoAggregatorControllerTest {
	
	@MockBean
	private ProductAggregatorAsync productAggregatorServiceMock;
	@MockBean
	private ProductPriceService productPriceServiceMock;
	
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}
	
	@Test
	public void testGetProductInfo() throws Exception {
		String productId = "100";
		Double price = 100d;
		ProductAggregatorServiceResponse aggregatedProductInfo = new ProductAggregatorServiceResponse();
		Product product = new Product();
		product.setId(productId);
		Price prodPrice = new Price(price, "USD");
		product.setCurrentPrice(prodPrice);
		
		aggregatedProductInfo.setProduct(product);
		given(productAggregatorServiceMock.getProductInfoAsync(productId)).willReturn(aggregatedProductInfo);
		mockMvc.perform(get("/products/" + productId).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
		.andExpect(jsonPath("$.product.id", is(productId)))
		.andExpect(jsonPath("$.product.currentPrice.value", is(price)))
		.andExpect(jsonPath("$.product.currentPrice.currency_code", is("USD")));
	}
	
	
	@Test
	public void testPutValidProductInfo() throws Exception {
		String productId = "100";
		Double price = 100d;
		Double newPriceValue = 101d;
		ProductAggregatorServiceResponse aggregatedProductInfo = new ProductAggregatorServiceResponse();
		Product product = new Product();
		product.setId(productId);
		Price prodPrice = new Price(price, "USD");
		product.setCurrentPrice(prodPrice);
		
		aggregatedProductInfo.setProduct(product);
		
		Product newProductPrice = new Product();
		newProductPrice.setId(productId);
		Price newPrice = new Price(newPriceValue, "USD");
		newProductPrice.setCurrentPrice(newPrice);
		
		ProductAggregatorServiceResponse aggregatedProductInfo2 = new ProductAggregatorServiceResponse();
		aggregatedProductInfo2.setProduct(newProductPrice);
		given(productAggregatorServiceMock.getProductInfoAsync(productId)).willReturn(aggregatedProductInfo, aggregatedProductInfo2);
		
		
		mockMvc.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(newProductPrice))).andExpect(status().isOk())
		.andExpect(jsonPath("$.product.id", is(productId)))
		.andExpect(jsonPath("$.product.currentPrice.value", is(newPriceValue)))
		.andExpect(jsonPath("$.product.currentPrice.currency_code", is("USD")));
	}
	
	
	@Test
	public void testPutNegativeProductPrice() throws Exception {
		String productId = "100";
		Double price = -100d;
		Product product = new Product();
		product.setId(productId);
		Price prodPrice = new Price(price, "USD");
		product.setName("test");
		product.setCurrentPrice(prodPrice);
		
		
		mockMvc.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(product))).andExpect(status().isBadRequest()).andExpect(jsonPath("$",is("Invalid price input.  Input has to be a double value between 0 and 100000.0")));
	}
	
	@Test
	public void testPutNullProductPrice() throws Exception {
		String productId = "100";
		Product product = new Product();
		product.setId(productId);
		Price prodPrice = new Price(null, "USD");
		product.setName("test");
		product.setCurrentPrice(prodPrice);
		
		
		mockMvc.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(product))).andExpect(status().isBadRequest()).andExpect(jsonPath("$",is("Invalid input, please check input format from https://target-product-info-aggregator.cfapps.io/swagger-ui.html")));
	}
	
	@Test
	public void testPutInvalidJson() throws Exception {
		String productId = "100";
		mockMvc.perform(put("/products/" + productId).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString("{,"))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message",is("Invalid input, please check input format against https://target-product-info-aggregator.cfapps.io/swagger-ui.html ")));
	}
}
