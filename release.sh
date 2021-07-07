#!/bin/bash -l

#
# Copyright (c) 2021 kotx__.
# Twitter: https://twitter.com/kotx__
#

semanticVersionToAbstractValue() {
  MAJOR1=$(echo $1 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[1] }")
  MINOR1=$(echo $1 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[2] }")
  PATCH1=$(echo $1 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[3] }")
  MAJOR2=$(echo $2 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[1] }")
  MINOR2=$(echo $2 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[2] }")
  PATCH2=$(echo $2 | awk "match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[3] }")

  if [ $MAJOR1 -gt $MAJOR2 ]; then
    echo true
  elif [ $MINOR1 -gt $MINOR2 ]; then
    echo true
  elif [ $PATCH1 -gt $PATCH2 ]; then
    echo true
  else
    echo false
  fi
}

createRelease() {
  echo "RELEASE_VERSION=$PROJECT_VERSION" >>$GITHUB_ENV
}

TARGET_FILE="./build.gradle"
if [ ! -e $TARGET_FILE ]; then
  TARGET_FILE="./build.gradle.kts"
  if [ ! -e $TARGET_FILE ]; then
    echo "Gradle configuration file not found!"
    exit
  fi
fi

REPOSITORY_NAME=$(echo "$GITHUB_REPOSITORY" | awk -F / "{print $2}")

PROJECT_VERSION=$(cat $TARGET_FILE | grep -m 1 "val projectVersion = " | awk "match($0, /val projectVersion = "(.+)"/, groups) { print groups[1] }")
REMOTE_LATEST_VERSION=$(curl --silent "https://api.github.com/repos/$GITHUB_REPOSITORY/releases/latest" | grep "\"tag_name\":" | sed -E "s/.*\"([^\"]+)\".*/\1/")

IS_RELEASEABLE=$(semanticVersionToAbstractValue $PROJECT_VERSION $REMOTE_LATEST_VERSION)

if [ -z $REMOTE_LATEST_VERSION ]; then
  createRelease
elif [ $IS_RELEASEABLE = "true" ]; then
  createRelease
else
  exit
fi