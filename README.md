[![Build Status](https://travis-ci.org/eBay/bonsai.svg?branch=master)](https://travis-ci.org/eBay/bonsai)

## Bonsai, a library to parse and evaluate decision trees encoded as JSON
This code allows to export Gradient Boosted Tree Model generated by 
[gbm](http://cran.r-project.org/web/packages/gbm) into a file in JSON format
and to evaluate them on new data outside R.

We have been using it inside our company to deploy gbm-trained model into production, 
either evaluating the JSON directly using the scala code or by generating C++ code from the
JSON and deploying the generated code. 

# Instructions
You can follow the script under `src/test/R/test.R' to see how to create a gbm
model and convert it to JSON using the script under `src/main/R/gmb.R`.
The example uses the the model from gbm's help page (except I reduced the number of trees 
from 1000 to 100 in order to keep the files small) to prepare a test case for the scala 
unit testing code. 


# Notes
gbm handles ordered categorical factors as numeric variables and it requires careful
handling when converting to JSON. I actually represent the conditional as set operators in
the JSON files in order to preserve the categorical nature of the variables.


Dependencies:

* play-json


Steps:
  * Build the package: mvn clean package
  * Run the Bonsai tool: java -jar target/bonsai-1.0-jar-with-dependencies.jar
  
    Usage: 
      to pretty-print a json moodel: Bonsai -p modelFile
      to evaluate a model over a test file: Bonsai -e modelFile dataFile
      to generate C++ code: Bonsai -c (modelFile)*

  * To convert the model json file to cpp file:
 
    java -jar target/bonsai-1.0-jar-with-dependencies.jar 'model1 json file'  'model2 json file' ...

    
# Contributors
* Alex Cozzi
* Mirek Melichar
* Amit Jaiswal
* mijiang
 