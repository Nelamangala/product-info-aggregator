package com.target.product.aggregator.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.target.product.price.domain.ProductPrice;

import com.target.product.aggregator.model.Price;
import com.target.product.aggregator.model.Product;
import com.target.product.aggregator.services.ProductGeneralInfoService;
import com.target.product.aggregator.services.ProductPriceService;

@Service
@PropertySource("classpath:config.properties")
public class ProductAggregatorAsync {
	@Value( "${product.price.api.timeout}" )
	private int productPriceApiTimeout;
	
	@Value( "${product.name.api.timeout}" )
	private int productNameApiTimeout;
	
	@Autowired
	private ProductPriceService productPriceService;
	@Autowired
	private ProductGeneralInfoService productGeneralInfoService;
	
	private ExecutorService executor = Executors.newFixedThreadPool(2);
	private static final Logger logger = LoggerFactory.getLogger(ProductAggregatorAsync.class);
	
	public ProductAggregatorServiceResponse getProductInfoAsync(String productId) {
		Future<ProductPrice> productPriceFuture = executor.submit(new ProductPriceAsyncTask(productPriceService, productId));
		Future<String> productNameFuture = executor.submit(new ProductGeneralInfoAsyncTask(productGeneralInfoService, productId));
		Product product = new Product();
		product.setId(productId);
		StringBuilder error = new StringBuilder();
		try {
			ProductPrice productPrice = productPriceFuture.get(productPriceApiTimeout, TimeUnit.MILLISECONDS);
			product.setCurrentPrice(productPrice == null ? null : new Price(productPrice.getPrice(), productPrice.getCurrency()));
		} catch (InterruptedException e) {
			error.append("Product price lookup async was interrupted : " + e.getMessage());
		} catch (ExecutionException e) {
			error.append("Exception while executing product lookup async : " + e.getMessage());
		} catch (TimeoutException e) {
			error.append("Timeout when product price lookup for id:" + productId);
		}
		
		try {
			String productName = productNameFuture.get(productNameApiTimeout, TimeUnit.MILLISECONDS);
			product.setName(productName);
		} catch (InterruptedException e) {
			error.append("Product name lookup async was interrupted : " + e.getMessage());
		} catch (ExecutionException e) {
			error.append("Exception while executing product name lookup async : " + e.getMessage());
		} catch (TimeoutException e) {
			error.append("Timeout while product name lookup for id:" + productId);
		}
		
		ProductAggregatorServiceResponse response = new ProductAggregatorServiceResponse();
		response.setProduct(product);
		if(error.length() > 0) {
			logger.error(error.toString());
			response.setMessage("Could not retrieve complete product information.");
		}else {
			response.setMessage("Successfully retrieved complete product information.");
		}
		
		return response;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		executor.shutdown();
		try {
		    if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		    	executor.shutdownNow();
		    } 
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}
	
	public static class ProductAggregatorServiceResponse{
		private String message;
		private Product product;
		
		public ProductAggregatorServiceResponse(){
			
		}
		public ProductAggregatorServiceResponse(String responseMsg, Product productInfo) {
			super();
			this.message = responseMsg;
			this.product = productInfo;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String responseMsg) {
			this.message = responseMsg;
		}
		public Product getProduct() {
			return product;
		}
		public void setProduct(Product productInfo) {
			this.product = productInfo;
		}
		
		
	}
}
