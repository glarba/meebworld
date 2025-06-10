#!/bin/bash

source ./build.sh || {
    echo -e "Failed build, aborting."
    exit 1
}

echo -e "Running..."
java -jar "$JAR_FILE"
