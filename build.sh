#!/usr/bin/env bash

#                        Build tool for meeb world! :3
# =============================================================================
#
# Synopsis:
# ./build.sh [javac options]


# --- Setup variables ---------------------------------------------------------

# Jar name:
JAR_NAME="App"

# Manifest file:
MANIFEST="manifest.mf"

# List of additional directories to include in jar file (use spaces/new-lines
# to separate entries, NOT commas, kthxbye):
DIRS_DEPENDENCIES=(
    resources
)

# Source directory:
DIR_SRC="src"

# Binary/Class directory:
DIR_BIN=".bin"


# --- Runtime variables -------------------------------------------------------

# Files:
JAR_FILE="${JAR_NAME}.jar"

# Directories:
DIR_ROOT=$(pwd)

# Colours:
COL_RED="\033[0;31m"
COL_NONE="\033[0m"


# --- Execution ---------------------------------------------------------------

function panic() {
    echo -e "${COL_RED}[Panic]${COL_NONE} $*"
    cd "$DIR_ROOT"
    [ -f "$JAR_FILE" ] && mv "$JAR_FILE" "${JAR_NAME}.failed.jar"
    exit 1
}

echo -e "[Build] Begin..."

# Move previous jar file:
[ -f "$JAR_FILE" ] && mv "$JAR_FILE" "${JAR_NAME}.old.jar"

# Create new jar file:
jar cmfv "$MANIFEST" "$JAR_FILE" ||
    panic "Failed to create jar archive."

# Compile all java source files in $DIR_SRC:
[ -d "$DIR_SRC" ] ||
    panic "Source directory '${DIR_SRC}' does not exist."
javac $* -d "$DIR_BIN" "$DIR_SRC"/*.java ||
    panic "Failed to compile source files."

# Add all class files to jar archive:
cd "$DIR_BIN"
jar fuv "${DIR_ROOT}/${JAR_FILE}" *.class ||
    panic "Failed to add class files to jar archive."
cd "$DIR_ROOT"

# Add additional dependencies:
jar fuv "$JAR_FILE" ${DIRS_DEPENDENCIES[*]}

echo -e "[Build] Completed!"
