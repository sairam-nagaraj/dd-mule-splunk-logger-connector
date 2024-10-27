# Mule Splunk Logger Connector
A custom MuleSoft connector that enables seamless integration with Splunk's HTTP Event Collector (HEC) for logging purposes. This connector simplifies the process of sending logs from Mule applications to Splunk, providing a reusable component that can be utilized across multiple projects.
## Features

1. Easy integration with Splunk HEC
2. Configurable logging parameters
3. Support for custom event payloads
4. Reusable across multiple Mule applications
5. Development mode with TLS bypass option (not for production use)

## Prerequisites
Before using this connector, ensure you have:

1. Java 8 JDK and JRE
2. Maven 3.6.0 or higher
3. Mule SDK
4. Intellij IDE or Anypoint Studio 7.x or any choice of Java IDE
5. Splunk instance with HEC endpoint and token
6. Mule runtime 4.x

## Installation

Clone the repository:
```
clone https://github.com/sairam-nagaraj/dd-mule-splunk-logger-connector.git
```
## Build the connector:
```
mvn clean install -Dmaven.test.skip=true
```
## Add dependency to Mule app
Once installed, the dependency will be added to local maven repository. Add this as a dependency to project's pom.xml
```
<dependency>
    <groupId>com.digital.dynamics</groupId>
    <artifactId>dd-mule-splunk-logger-connector</artifactId>
    <version>1.0.0</version>
    <classifier>mule-plugin</classifier>
</dependency>
```
## Configuration
1. Splunk HEC Setup
Ensure you have:

Splunk HEC endpoint URL
Authentication token
Proper certificates for production use

2. Connector Configuration
```
<mule-splunk-logger:config name="Mule_Splunk_Logger_Config" doc:name="Mule Splunk Logger Config" doc:id="8caf52e9-babb-47be-a43f-7fdb3eb3b8be" splunkUrl="${dd.splunk.url}" authToken="${dd.splunk.token}">
    <mule-splunk-logger:splunk-ts-config-dto trustStoreLocation="${dd.splunk.truststore.location}" trustStorePassword="${dd.splunk.truststore.password}" />
</mule-splunk-logger:config>
```
## Usage
### Basic Example

```
<flow name="test-mule-appFlow" doc:id="3068d099-bbc8-4752-8439-08c36b5bb6ac" >
    <http:listener doc:name="Listener" doc:id="0cbdc90e-6847-4b92-8f9b-824c664a2428" config-ref="HTTP_Listener_config" path="/test"/>
    <ee:transform doc:name="Transform Message" doc:id="f5fb573b-bc00-4627-821e-4a61fb249a3b" >
        <ee:message >
			</ee:message>
        <ee:variables >
            <ee:set-variable variableName="splunkRequest" >
                <![CDATA[%dw 2.0
output application/json
---
{
	"request": {
		"id": 1,
		"name": "SomeNameFromMulesoft",
		"surname": "SomeSurname"
	},
	"response": {
		"result": "Client Created Successfully from Mulesoft"
	},
	"uri": "http://localhost:8081/test"
}]]>
            </ee:set-variable>
        </ee:variables>
    </ee:transform>
    <logger level="INFO" doc:name="Logger" doc:id="e37e20c8-c034-42ed-81c0-74c2d74642f2" message="#[%dw 2.0&#10;output application/json&#10;---&#10;vars.splunkRequest]"/>
    <mule-splunk-logger:log-to-splunk-secured doc:name="Secured Log to Splunk" doc:id="f2ba897a-76d1-424d-9edd-949088778cc1" config-ref="Mule_Splunk_Logger_Config" payload="#[vars.splunkRequest]"/>
</flow>
</mule>
```
## Security Considerations
### ⚠️ Important Security Notice:

1. The current implementation includes a TLS bypass option for development purposes
2. Never use TLS bypass in production environments
3. Ensure proper certificate management and security measures in production
4. Follow your organization's security policies

## Troubleshooting
For specific error solutions, check the Troubleshooting Guide on this medium article
https://medium.com/@sairam.nagaraj/building-a-custom-splunk-logging-connector-in-mule-4-a-step-by-step-guide-4816ebf08320

## Acknowledgments

1. https://blogs.mulesoft.com/dev-guides/api-connectors-templates/custom-connector-mule-sdk/
2. https://dzone.com/articles/custom-icon-for-mule-4-custom-connector
3. Claude.ai