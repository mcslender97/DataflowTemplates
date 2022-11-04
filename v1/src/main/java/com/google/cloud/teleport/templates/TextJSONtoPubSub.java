/*
 * Copyright (C) 2018 Google LLC
 * 
 * Edited by Minh Cuong Do
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.templates;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.options.ValueProvider;

import org.apache.beam.sdk.transforms.ParDo;

/**
 * The {@code TextToPubsub} pipeline publishes records to Cloud Pub/Sub from a set of files. The
 * pipeline reads each file row-by-row and publishes each record as a string message. At the moment,
 * publishing messages with attributes is unsupported.
 *
 * <p>Example Usage:
 *
 * <pre>
 * {@code mvn compile exec:java \
 * -Dexec.mainClass=com.google.cloud.teleport.templates.TextToPubsub \
 * -Dexec.args=" \
 * --project=${PROJECT_ID} \
 * --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_FOLDER}/staging \
 * --tempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_FOLDER}/temp \
 * --runner=DataflowRunner \
 * --inputFilePattern=gs://path/to/demo_file.csv \
 * --outputTopic=projects/${PROJECT_ID}/topics/${TOPIC_NAME}"
 * }
 * </pre>
 */
public class TextJSONtoPubSub {

  /** The custom options supported by the pipeline. Inherits standard configuration options. */
  public interface Options extends PipelineOptions {
    @Description("The file pattern to read records from (e.g. gs://bucket/file-*.json)")
    @Required
    ValueProvider<String> getInputFilePattern();

    void setInputFilePattern(ValueProvider<String> value);

    @Description(
        "The name of the topic which data should be published to. "
            + "The name should be in the format of projects/<project-id>/topics/<topic-name>.")
    @Required
    ValueProvider<String> getOutputTopic();

    void setOutputTopic(ValueProvider<String> value);
  }

  /**
   * Main entry-point for the pipeline. Reads in the command-line arguments, parses them, and
   * executes the pipeline.
   *
   * @param args Arguments passed in from the command-line.
   */
  public static void main(String[] args) {

    // Parse the user options passed from the command-line
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

    run(options);
  }

  /**
   * Executes the pipeline with the provided execution parameters.
   *
   * @param options The execution parameters.
   */
  public static PipelineResult run(Options options) {
    // Create the pipeline.
    Pipeline pipeline = Pipeline.create(options);

    /*
     * Steps:
     *  1) Read from the text source.
     *  2) Write each text record to Pub/Sub
     */
    pipeline
        
        .apply("Read Text Data", TextIO.read().from(options.getInputFilePattern()))
        .apply(
            "Parse JSON from text",                     // the transform name
            ParDo.of(new DoFn<String, String>() {    // a DoFn as an anonymous inner class instance
                @ProcessElement
                public void processElement(@Element String word, OutputReceiver<String> out) {
                    JSONParser parser = new JSONParser();
                    Object objs = null;
                    try {
                        objs = parser.parse(strLine);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject =  (JSONObject) objs;
                    Iterator<?> keys = objs.keys();
                    while (keys.hasNext()){
                        String key = (String) keys.next();
                        String value = (String) jsonObject.get(key);
                        out.output(key+','+value);
                    }
                }
            }))
        .apply("Write to PubSub", PubsubIO.writeStrings().to(options.getOutputTopic()));

    return pipeline.run();
  }
}
