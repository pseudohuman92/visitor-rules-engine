# Ancient Aliens CCG

## To run the server

### Required Software

Install Maven: https://maven.apache.org/

Install Protobuf Compiler: https://developers.google.com/protocol-buffers/

Before Building: Find absolute path of protobuf compiler (protoc). Go and edit <protocExecutable> tag in the pom.xml file with the correct path.

To build: mvn install

To start: mvn jetty:run

To stop: <CTRL+C> or mvn jetty:stop (if running in background)

## To run the client

Install yarn and npm.

Go to the webclient directory.

To install dependencies: yarn install

To start sever: yarn start
