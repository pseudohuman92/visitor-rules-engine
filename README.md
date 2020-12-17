# Visitor: the Card Game

## To run the server

### Required Software

Install Maven: https://maven.apache.org/

Install Protobuf Compiler: https://developers.google.com/protocol-buffers/

---

### Before Building

Find absolute path of protobuf compiler (protoc), then edit <protocExecutable> tag in the pom.xml file with the correct path.

---

To build: mvn install

To start: mvn jetty:run

To stop: <CTRL+C> or mvn jetty:stop (if running in background)

## To run the client

### Required Software

Install Yarn: https://yarnpkg.com/en/

Install npm: https://www.npmjs.com/

---

### To start the client

sh ./webclient/run.sh
