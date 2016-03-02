[![Build Status](https://travis-ci.org/eBay/bonsai.svg?branch=master)](https://travis-ci.org/eBay/bonsai)

## Bonsai web service: a machine learning prediction system as web service

integrating machine learning prediction system with existing production system can be very challenge, and often takes months more than initially estimated. A web service architeture will isolate the machine learning prediction system from the existing production system, and the integration becomes trivial. A update on the machine learning model will lead to a often simple modification on the web service, and the existing production system does not even aware of the change of the model.

This code shows how to implement a web service for GBM (http://cran.r-project.org/web/packages/gbm) using bonsai (https://github.com/eBay/bonsai).

# How to run
  * Build the package: mvn clean package
  * Run the Bonsai tool: java -jar target/bonsai-1.0-jar-with-dependencies.jar
  
    Usage: 
      to evaluate a model over a test file: cat dataFile | java -jar target/bonsai-1.0-jar-with-dependencies.jar


