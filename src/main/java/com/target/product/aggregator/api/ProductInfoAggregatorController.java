package com.target.product.aggregator.api;

import javax.validation.Valid;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.target.product.aggregator.async.ProductAggregatorAsync;
import com.target.product.aggregator.async.ProductAggregatorAsync.ProductAggregatorServiceResponse;
import com.target.product.aggregator.model.Product;
import com.target.product.aggregator.services.ProductPriceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "products", description = "Endpoint for product information.")
@RestController
@RequestMapping(value = "/products")
public class ProductInfoAggregatorController {

	private static final double MAX_ALLOWED_PRICE = 100000;
	private static final Logger logger = LoggerFactory.getLogger(ProductInfoAggregatorController.class);
	private ProductAggregatorAsync productAggregatorService;
	private ProductPriceService productPriceService;
	
	@Autowired
	public ProductInfoAggregatorController(ProductAggregatorAsync productAggregatorService, ProductPriceService productPriceService) {
		this.productAggregatorService = productAggregatorService;
		this.productPriceService = productPriceService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Returns aggregated product information from multiple sources", response = ProductAggregatorServiceResponse.class)
	@Produces({"application/json"})
	public ResponseEntity<ProductAggregatorServiceResponse> getProduct(@PathVariable String id) {
		logger.info("Getting aggregated product information for id: " + id);
		ProductAggregatorServiceResponse aggregatedProductInfo = productAggregatorService.getProductInfoAsync(id);
		return new ResponseEntity<ProductAggregatorServiceResponse>(aggregatedProductInfo, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Updates price value of product", notes = "Returns updated price details.", response = ProductAggregatorServiceResponse.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Produces({"application/json"})
    public ResponseEntity<?> updateProductPrice(@PathVariable String id, @Valid @RequestBody Product product) throws IllegalArgumentException{
        logger.info("Request to update price for product with id: " + id);
        if(product == null || product.getCurrentPrice() == null || product.getCurrentPrice().getValue() == null) {
        	return new ResponseEntity<String>("Invalid input, please check input format from https://target-product-info-aggregator.cfapps.io/swagger-ui.html ", HttpStatus.BAD_REQUEST);
        }else if(product.getCurrentPrice().getValue() < 0 || product.getCurrentPrice().getValue() > MAX_ALLOWED_PRICE) {
        	return new ResponseEntity<String>("Invalid price input. " + " Input has to be a double value between 0 and " + MAX_ALLOWED_PRICE, HttpStatus.BAD_REQUEST);
        }
        ProductAggregatorServiceResponse aggregatedProductInfo = productAggregatorService.getProductInfoAsync(id);
        
        // If price input price is different from currently existing price, update the price and get info again
        if(product.getCurrentPrice() != null && !product.getCurrentPrice().equals(aggregatedProductInfo.getProduct().getCurrentPrice())) {
        		productPriceService.updateProductPrice(product);
        		aggregatedProductInfo = productAggregatorService.getProductInfoAsync(product.getId());
        }
        
        return new ResponseEntity<ProductAggregatorServiceResponse>(aggregatedProductInfo, HttpStatus.OK);
    }
	
}
