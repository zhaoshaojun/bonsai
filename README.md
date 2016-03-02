[![Build Status](https://travis-ci.org/eBay/bonsai.svg?branch=master)](https://travis-ci.org/eBay/bonsai)

## Bonsai web service: a machine learning prediction system as web service

# Motivation
integrating machine learning prediction system with existing production system can be very challenge, and often takes much longer time than initially estimated. A web service architecture will isolate the machine learning prediction system from the existing production system, and the integration becomes trivial. A update on the machine learning model will lead to a often simple modification on the web service, and the existing production system does not even aware of the change of the model. A bonus benefit of web service is that the production system does not need to care how the web service and the machine learning prediction system is implemented, particularly what language is used to implement the web service and the machine learning prediction system.

This code shows how to implement a web service for GBM (http://cran.r-project.org/web/packages/gbm) on top of bonsai (https://github.com/eBay/bonsai).

# How to run
  * Build: mvn clean package
  * Run as an application: cat <input-file> | java -jar target/bonsai-1.0-jar-with-dependencies.jar > <output-file>
  * Run as web serive: sbt run <port>




