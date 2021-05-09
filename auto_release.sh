#!/bin/bash -l

#
# Copyright (c) 2021 kotx__.
# Twitter: https://twitter.com/kotx__
#

semanticVersionToAbstractValue() {
  MAJOR1=$(echo $1 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[1] }')
  MINOR1=$(echo $1 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[2] }')
  PATCH1=$(echo $1 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[3] }')
  MAJOR2=$(echo $2 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[1] }')
  MINOR2=$(echo $2 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[2] }')
  PATCH2=$(echo $2 | awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[3] }')

  if [ $MAJOR1 -lt $MAJOR2 ]; then
    echo false
  elif [ $MINOR1 -lt $MINOR2 ]; then
    echo false
  elif [ $PATCH1 -lt $PATCH2 ]; then
    echo false
  else
    echo true
  fi
}

createRelease() {
  sh ./gradlew build
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

REPOSITORY_NAME=$(echo "$GITHUB_REPOSITORY" | awk -F / '{print $2}')

PROJECT_VERSION=$(cat $TARGET_FILE | grep -m 1 "version = " | awk 'match($0, /version = "(.+)"/, groups) { print groups[1] }')
REMOTE_LATEST_VERSION=$(curl --silent "https://api.github.com/repos/$GITHUB_REPOSITORY/releases/latest" | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/')

IS_RELEASEABLE=$(semanticVersionToAbstractValue $PROJECT_VERSION $REMOTE_LATEST_VERSION)

echo "Repository: $REPOSITORY_NAME"
echo "Project Version: $PROJECT_VERSION"
echo "Release Version: $REMOTE_LATEST_VERSION"

if [ -z $REMOTE_LATEST_VERSION ]; then
  createRelease
elif [ $IS_RELEASEABLE = "true" ]; then
  createRelease
else
  exit
fi