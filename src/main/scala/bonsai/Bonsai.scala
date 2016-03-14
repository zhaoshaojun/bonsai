/**
 * Copyright 2012 eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bonsai

import play.api.libs.json._
import scala.io.Source

/**
 * Main app to manage json encoded decision tree model.
 */
object Bonsai extends App {

  type prediction = Double
  var m: Model = null

  args.toList match {

    // evaluate a model on a test-input file
    case "-s" :: modelFile :: Nil => {
      m = processModel(modelFile)
      runPredictionSeq()
    }
    case "-p" :: modelFile :: Nil => {
      m = processModel(modelFile)
      runPredictionPar()
    }
    case _ => {
      System.err.println("Usage: \n" +
        " to evaluate a model over a test file: cat input-file | bonsai-streaming -[p|s] model-file > output-file\n")
    }
  }

  /**
   * All model processing is here. Leave out operations where corresponding input/output parameter is None.
   */
  def processModel(modelFile: String) = {

    System.err.println(s"Processing model: ${modelFile}")

    // parse model json file
    val json = Json.parse(fixAllScientificNotations(Source.fromFile(modelFile).mkString))
    Model.fromJSON(json)

  }

  def runPredictionSeq() {

    System.err.println(s"Running prediction in 'seq' mode")

    // evaluate model and validate it against specified output values
    for (line <- io.Source.stdin.getLines) {

        val data = Json.parse(line).as[Seq[JsObject]]

	for (e <- data) {
			 val calc = m.eval(e)
			 // val user_id = (e \ "USER_ID").as[Int]
			 // println("[{" + "\"USER_ID\":" + user_id + "," + "\"score\":" + calc + "}]")
	}

    }
    
  }

  def runPredictionPar() {

    System.err.println(s"Running prediction in 'par' mode")

    // evaluate model and validate it against specified output values
    for (line <- io.Source.stdin.getLines) {

        val data = Json.parse(line).as[Seq[JsObject]]

	data.par.foreach {
			 e => val calc = m.eval(e)
			 // val user_id = (e \ "USER_ID").as[Int]
			 // println("[{" + "\"USER_ID\":" + user_id + "," + "\"score\":" + calc + "}]")
	}

    }
    
  }

  def fixAllScientificNotations(x: String) = x.replaceAll("(\\d+)(e|E)\\+(\\d+)", "$1E$3") // 1e+2 is not accepted by the parser ("+" must be removed)

}

/**
 * Small utility to read test files for regression testing.
 */
object TestFile {
  def readFile(filename: String): Seq[Map[String, Double]] = {
    val lines = Source.fromFile(filename).getLines
    lines map {
      _.split("\\s+") map {
        pair =>
          val arr = pair.split(":")
          (arr(0), arr(1).toDouble)
      } toMap
    } toSeq
  }
}
