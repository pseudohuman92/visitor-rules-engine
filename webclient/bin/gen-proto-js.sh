#!/bin/bash
	
compiled_dir=src/protojs
compiled_file=src/protojs/compiled.js
compile_ts=src/protojs/compiled.d.ts
proto_dir="../src/main/proto"

function compile() {
  yarn run pbjs -t static-module -w commonjs -o ${compiled_file} ${proto_dir}/*.proto
  yarn run pbts -o ${compile_ts} ${compiled_file}
  sed -i '1i/* eslint-disable */\' ${compiled_file}
}

[[ -d ${compiled_dir} ]] || mkdir -p ${compiled_dir}

compiled_ts=0
[[ -f ${compiled_file} ]] && compiled_ts=$(stat -c '%Y' ${compiled_file})
if [[ $compiled_ts == 0 ]]; then
  compile
  exit
fi

for f in ${proto_dir}/*.proto; do
  proto_ts=$(stat -c '%Y' ${f})
  if [[ $proto_ts > $compiled_ts ]]; then
    compile
    exit
  fi
done
