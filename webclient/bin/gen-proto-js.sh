#!/bin/bash

./bin/protoc --js_out=import_style=commonjs,binary:src/proto --proto_path=../src/protocol/ ../src/protocol/*.proto
for f in src/proto/*.js ; do 
  sed -i '1i/* eslint-disable */\' ${f}
done
