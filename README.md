# Google Cloud Dataflow Template Pipelines

These Dataflow templates are an effort to solve simple, but large, in-Cloud data
tasks, including data import/export/backup/restore and bulk API operations,
without a development environment. The technology under the hood which makes
these operations possible is the
[Google Cloud Dataflow](https://cloud.google.com/dataflow/) service combined
with a set of [Apache Beam](https://beam.apache.org/) SDK templated pipelines.

Google is providing this collection of pre-implemented Dataflow templates as a
reference and to provide easy customization for developers wanting to extend
their functionality.

[![Open in Cloud Shell](http://gstatic.com/cloudssh/images/open-btn.svg)](https://console.cloud.google.com/cloudshell/editor?cloudshell_git_repo=https%3A%2F%2Fgithub.com%2FGoogleCloudPlatform%2FDataflowTemplates.git)

## Note on Default Branch

As of November 18, 2021, our default branch is now named "main". This does not
affect forks. If you would like your fork and its local clone to reflect these
changes you can follow [GitHub's branch renaming guide](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-branches-in-your-repository/renaming-a-branch).

## Building

Maven commands should be run on the parent POM. An example would be:

```
mvn clean install -pl v2/pubsub-binary-to-bigquery -am
```

## Template Pipelines

* [BigQuery to Bigtable](v2/bigquery-to-bigtable/src/main/java/com/google/cloud/teleport/v2/templates/BigQueryToBigtable.java)
* [BigQuery to Datastore](v1/src/main/java/com/google/cloud/teleport/templates/BigQueryToDatastore.java)
* [BigQuery to TFRecords](v1/src/main/java/com/google/cloud/teleport/templates/BigQueryToTFRecord.java)
* [Bigtable to GCS Avro](v1/src/main/java/com/google/cloud/teleport/bigtable/BigtableToAvro.java)
* [Bulk Compressor](v1/src/main/java/com/google/cloud/teleport/templates/BulkCompressor.java)
* [Bulk Decompressor](v1/src/main/java/com/google/cloud/teleport/templates/BulkDecompressor.java)
* [Datastore Bulk Delete](v1/src/main/java/com/google/cloud/teleport/templates/DatastoreToDatastoreDelete.java) *
* [Datastore to BigQuery](v1/src/main/java/com/google/cloud/teleport/templates/DatastoreToBigQuery.java)
* [Datastore to GCS Text](v1/src/main/java/com/google/cloud/teleport/templates/DatastoreToText.java) *
* [Datastore to Pub/Sub](v1/src/main/java/com/google/cloud/teleport/templates/DatastoreToPubsub.java) *
* [Datastore Unique Schema Count](v1/src/main/java/com/google/cloud/teleport/templates/DatastoreSchemasCountToText.java)
* [DLP Text to BigQuery (Streaming)](v1/src/main/java/com/google/cloud/teleport/templates/DLPTextToBigQueryStreaming.java)
* [File Format Conversion](v2/file-format-conversion/src/main/java/com/google/cloud/teleport/v2/templates/FileFormatConversion.java)
* [GCS Avro to Bigtable](v1/src/main/java/com/google/cloud/teleport/bigtable/AvroToBigtable.java)
* [GCS Avro to Spanner](v1/src/main/java/com/google/cloud/teleport/spanner/ImportPipeline.java)
* [GCS Text to Spanner](v1/src/main/java/com/google/cloud/teleport/spanner/TextImportPipeline.java)
* [GCS Text to BigQuery](v1/src/main/java/com/google/cloud/teleport/templates/TextIOToBigQuery.java) *
* [GCS Text to Datastore](v1/src/main/java/com/google/cloud/teleport/templates/TextToDatastore.java)
* [GCS Text to Pub/Sub (Batch)](v1/src/main/java/com/google/cloud/teleport/templates/TextToPubsub.java)
* [GCS Text to Pub/Sub (Streaming)](v1/src/main/java/com/google/cloud/teleport/templates/TextToPubsubStream.java)
* [Jdbc to BigQuery](v1/src/main/java/com/google/cloud/teleport/templates/JdbcToBigQuery.java)
* [Pub/Sub to BigQuery](v1/src/main/java/com/google/cloud/teleport/templates/PubSubToBigQuery.java) *
* [Pub/Sub to Datastore](v1/src/main/java/com/google/cloud/teleport/templates/PubsubToDatastore.java) *
* [Pub/Sub to GCS Avro](v1/src/main/java/com/google/cloud/teleport/templates/PubsubToAvro.java)
* [Pub/Sub to GCS Text](v1/src/main/java/com/google/cloud/teleport/templates/PubsubToText.java)
* [Pub/Sub to Pub/Sub](v1/src/main/java/com/google/cloud/teleport/templates/PubsubToPubsub.java)
* [Pub/Sub to Splunk](v1/src/main/java/com/google/cloud/teleport/templates/PubSubToSplunk.java) *
* [Spanner to GCS Avro](v1/src/main/java/com/google/cloud/teleport/spanner/ExportPipeline.java)
* [Spanner to GCS Text](v1/src/main/java/com/google/cloud/teleport/templates/SpannerToText.java)
* [Word Count](v1/src/main/java/com/google/cloud/teleport/templates/WordCount.java)


\* Supports user-defined functions (UDFs).

For documentation on each template's usage and parameters, please see
the official [docs](https://cloud.google.com/dataflow/docs/templates/provided-templates).

## Getting Started

### Requirements

* Java 11
* Maven 3

### Building the Project

Build the entire project using the maven compile command.

```sh
mvn clean compile
```

### Building/Testing from IntelliJ

IntelliJ, by default, will often skip necessary Maven goals, leading to
build failures. You can fix these in the Maven view by going to
**Module_Name > Plugins > Plugin_Name** where Module_Name and Plugin_Name are
the names of the respective module and plugin with the rule. From there,
right-click the rule and select "Execute Before Build".

The list of known rules that require this are:

* common > Plugins > protobuf > protobuf:compile
* common > Plugins > protobuf > protobuf:test-compile

### Formatting Code

From either the root directory or v2/ directory, run:

```sh
mvn spotless:apply
```

This will format the code and add a license header. To verify that the code is
formatted correctly, run:

```sh
mvn spotless:check
```

The directory to run the commands from is based on whether the changes are under
v2/ or not.

### Creating a Template File

Dataflow templates can be [created](https://cloud.google.com/dataflow/docs/templates/creating-templates#creating-and-staging-templates)
using a maven command which builds the project and stages the template
file on Google Cloud Storage. Any parameters passed at template build
time will not be able to be overwritten at execution time.

```sh
mvn compile exec:java \
-Dexec.mainClass=com.google.cloud.teleport.templates.<template-class> \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args=" \
--project=<project-id> \
--stagingLocation=gs://<bucket-name>/staging \
--tempLocation=gs://<bucket-name>/temp \
--templateLocation=gs://<bucket-name>/templates/<template-name>.json \
--runner=DataflowRunner"
```


### Executing a Template File

Once the template is staged on Google Cloud Storage, it can then be
executed using the
[gcloud CLI](https://cloud.google.com/sdk/gcloud/reference/dataflow/jobs/run)
tool. The runtime parameters required by the template can be passed in the
parameters field via comma-separated list of `paramName=Value`.

```sh
gcloud dataflow jobs run <job-name> \
--gcs-location=<template-location> \
--zone=<zone> \
--parameters <parameters>
```


## Using UDFs

User-defined functions (UDFs) allow you to customize a template's
functionality by providing a short JavaScript function without having to
maintain the entire codebase. This is useful in situations which you'd
like to rename fields, filter values, or even transform data formats
before output to the destination. All UDFs are executed by providing the
payload of the element as a string to the JavaScript function. You can
then use JavaScript's in-built JSON parser or other system functions to
transform the data prior to the pipeline's output. The return statement
of a UDF specifies the payload to pass forward in the pipeline. This
should always return a string value. If no value is returned or the
function returns undefined, the incoming record will be filtered from
the output.

### UDF Function Specification
| Template              | UDF Input Type | Input Description                               | UDF Output Type | Output Description                                                            |
|-----------------------|----------------|-------------------------------------------------|-----------------|-------------------------------------------------------------------------------|
| Datastore Bulk Delete | String         | A JSON string of the entity                     | String          | A JSON string of the entity to delete; filter entities by returning undefined |
| Datastore to Pub/Sub  | String         | A JSON string of the entity                     | String          | The payload to publish to Pub/Sub                                             |
| Datastore to GCS Text | String         | A JSON string of the entity                     | String          | A single-line within the output file                                          |
| GCS Text to BigQuery  | String         | A single-line within the input file             | String          | A JSON string which matches the destination table's schema                    |
| Pub/Sub to BigQuery   | String         | A string representation of the incoming payload | String          | A JSON string which matches the destination table's schema                    |
| Pub/Sub to Datastore  | String         | A string representation of the incoming payload | String          | A JSON string of the entity to write to Datastore                             |
| Pub/Sub to Splunk  | String         | A string representation of the incoming payload | String          | The event data to be sent to Splunk HEC events endpoint. Must be a string or a stringified JSON object |


### UDF Examples

#### Adding fields
```js
/**
 * A transform which adds a field to the incoming data.
 * @param {string} inJson
 * @return {string} outJson
 */
function transform(inJson) {
  var obj = JSON.parse(inJson);
  obj.dataFeed = "Real-time Transactions";
  obj.dataSource = "POS";
  return JSON.stringify(obj);
}
```

#### Filtering records
```js
/**
 * A transform function which only accepts 42 as the answer to life.
 * @param {string} inJson
 * @return {string} outJson
 */
function transform(inJson) {
  var obj = JSON.parse(inJson);
  // only output objects which have an answer to life of 42.
  if (obj.hasOwnProperty('answerToLife') && obj.answerToLife === 42) {
    return JSON.stringify(obj);
  }
}
```
