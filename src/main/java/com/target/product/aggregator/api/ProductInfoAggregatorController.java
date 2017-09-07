package com.target.product.aggregator.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.target.product.aggregator.service.ProductAggregatorService;
import com.target.product.aggregator.service.ProductAggregatorService.ProductAggregatorServiceResponse;

@RestController
@RequestMapping(value = "/products")
public class ProductInfoAggregatorController {

	private static final Logger logger = LoggerFactory.getLogger(ProductInfoAggregatorController.class);
	private ProductAggregatorService productAggregatorService;

	@Autowired
	public ProductInfoAggregatorController(ProductAggregatorService productAggregatorService) {
		this.productAggregatorService = productAggregatorService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProduct(@PathVariable String id) {
		logger.info("Getting aggregated product information for id: " + id);
		ProductAggregatorServiceResponse aggregatedProductInfo = productAggregatorService.getProductInfoAsync(id);
		return new ResponseEntity<ProductAggregatorServiceResponse>(aggregatedProductInfo, HttpStatus.OK);
	}

}
