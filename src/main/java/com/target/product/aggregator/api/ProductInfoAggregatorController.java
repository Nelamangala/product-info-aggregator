package com.target.product.aggregator.api;

import javax.validation.Valid;

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

import com.target.product.aggregator.async.ProductAggregatorService;
import com.target.product.aggregator.async.ProductAggregatorService.ProductAggregatorServiceResponse;
import com.target.product.aggregator.model.Product;
import com.target.product.aggregator.services.ProductPriceService;

@RestController
@RequestMapping(value = "/products")
public class ProductInfoAggregatorController {

	private static final Logger logger = LoggerFactory.getLogger(ProductInfoAggregatorController.class);
	private ProductAggregatorService productAggregatorService;
	private ProductPriceService productPriceService;
	
	@Autowired
	public ProductInfoAggregatorController(ProductAggregatorService productAggregatorService, ProductPriceService productPriceService) {
		this.productAggregatorService = productAggregatorService;
		this.productPriceService = productPriceService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProduct(@PathVariable String id) {
		logger.info("Getting aggregated product information for id: " + id);
		ProductAggregatorServiceResponse aggregatedProductInfo = productAggregatorService.getProductInfoAsync(id);
		return new ResponseEntity<ProductAggregatorServiceResponse>(aggregatedProductInfo, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateProductPrice(@RequestBody @Valid Product product) {
        logger.info("Request to update price for product with id: " + product.getId());
        ProductAggregatorServiceResponse aggregatedProductInfo = productAggregatorService.getProductInfoAsync(product.getId());
        
        // If price input price is different from currently existing price, update the price and get info again
        if(product.getCurrentPrice() != null && !product.getCurrentPrice().equals(aggregatedProductInfo.getProduct().getCurrentPrice())) {
        		productPriceService.updateProductPrice(product);
        		aggregatedProductInfo = productAggregatorService.getProductInfoAsync(product.getId());
        }
        
        return new ResponseEntity<ProductAggregatorServiceResponse>(aggregatedProductInfo, HttpStatus.OK);
    }

}
