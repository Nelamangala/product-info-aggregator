myRetail RESTful service (Product Info Aggregator API)
========================	

This projects gathers information asynchronously from multiple product end points and returns aggregated product information.
This project can be easily deployed on cloud foundry.

Below diagram shows the architecture of the project.
![API Architecture Diagram](https://github.com/Nelamangala/product-info-aggregator/blob/master/API_DIAGRAM.jpg "API Architecture Diagram")


API endpoint documentation can be accessed and tried at : https://target-product-info-aggregator.cfapps.io/swagger-ui.html#/product45info45aggregator45controller

This API end point depends on the project: https://github.com/Nelamangala/product-price-api

### Key Highlights of this implementation
1. This API is independent and scalable on its own.
2. It performs asynchronous requests to get product information.
3. Exposes PUT end point to update price value of product.
4. Validates price value before updating.
5. API async call timeouts are configurable via .properties file
6. Can easily be connected to other components on clound foundry to provide services such as authentication.
7. API documentation is available via swagger.
8. Instance of this project is deployed and publicly available at https://target-product-info-aggregator.cfapps.io/products/

#### Build and deploy
1. Build and deploy https://github.com/Nelamangala/product-price-api. Release project artifact to repository.
2. Use artifact from step 1 as dependency in this project pom.xml
3. Build using maven `mvn clean install`
4. Login into your cloud foundry environment. (Needs CLI tools installed https://docs.cloudfoundry.org/cf-cli/getting-started.html)
5. Push project into cloud foundry. `cf push`