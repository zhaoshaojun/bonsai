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

  args.toList match {

    // evaluate a model on a test-input file
    case modelJsonFilename :: Nil => {
      processModel(modelJsonFilename)
    }
    case _ => {
      System.err.println("Usage: \n" +
        " to evaluate a model over a test file: cat input-file | bonsai-streaming model-file > output-file\n")
    }
  }

  /**
   * All model processing is here. Leave out operations where corresponding input/output parameter is None.
   */
  def processModel(modelJsonFilename: String) = {

    // val modelJsonFilename = "duvs.new.v1.01.model"
    println(s"Processing model: ${modelJsonFilename}")

    // parse model json file
    val json = Json.parse(fixAllScientificNotations(Source.fromFile(modelJsonFilename).mkString))
    val m = Model.fromJSON(json)

    // evaluate model and validate it against specified output values


    for (line <- io.Source.stdin.getLines) {

        val data = Json.parse(line).as[Seq[JsObject]]
        val stat = data.map({ value =>
          val calc = m.eval(value)
          val orig = (value \ "model").as[Double]
          val diff = orig - calc
          if (Math.abs(diff) > 1e-13) {
            println("!Large Diff:" + diff, "Eval:" + calc, "Orig:" + orig)
            1
          } else
            0
        }).toList.map(x => (x, 1)).foldLeft((0, 0))((a, b) => (a._1 + b._1, a._2 + b._2))
        if (stat._1 == 0)
          println(s"Validation on ${stat._2} test cases SUCCEEDED with 0 errors.")
        else
          println(f"Validation FAILED with ${100 * stat._1.toDouble / stat._2}%2.2f%% error rate, i.e. ${stat._1} of ${stat._2} test cases failed.")

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
