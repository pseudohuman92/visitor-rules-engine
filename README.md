# Ancient Aliens CCG

## To run the server

You need protobuf compiler (protoc) to be installed to build the project. protoc should be in "/usr/bin". If you want it to be in somewhere else, edit pom.xml file with correct path.

To build: mvn install

To start: mvn jetty:run

To stop: <CTRL+C> or mvn jetty:stop (if running in background)

## To run the client

Install yarn and npm.

Go to the webclient directory.

To install dependencies: yarn install

To start sever: yarn start
