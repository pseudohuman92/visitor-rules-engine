#!/bin/bash

proto_dir="../src/main/proto"
./bin/protoc --js_out=import_style=commonjs,binary:src/proto --proto_path=${proto_dir} ${proto_dir}/*.proto
for f in src/proto/*.js ; do 
  sed -i '1i/* eslint-disable */\' ${f}
done
