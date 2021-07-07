#!/bin/bash -l

#
# Copyright (c) 2021 kotx__.
# Twitter: https://twitter.com/kotx__
#

parseVersion() {
  awk 'match($0, /^([0-9]+)\.([0-9]+)\.([0-9]+)(?:-([0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-]+)?$/, groups) { print groups[$1] }'
}

abstractVersion() {
  PROJECT_MAJOR=$(parseVersion $1 1)
  PROJECT_MINOR=$(parseVersion $1 2)
  PROJECT_PATCH=$(parseVersion $1 3)

  REMOTE_MAJOR=$(parseVersion $2 1)
  REMOTE_MINOR=$(parseVersion $2 2)
  REMOTE_PATCH=$(parseVersion $2 3)

  if [ $PROJECT_MAJOR -gt $REMOTE_MAJOR ]; then
    echo true
  elif [ $PROJECT_MINOR -gt $REMOTE_MINOR ]; then
    echo true
  elif [ $PROJECT_PATCH -gt $REMOTE_PATCH ]; then
    echo true
  else
    echo false
  fi
}

PROJECT_VERSION=$(cat < build.gradle.kts | awk 'match($0, /val projectVersion = "(.+)"/, groups) { print groups[1] }')
REMOTE_VERSION=$(curl --silent "https://api.github.com/repos/$GITHUB_REPOSITORY/releases/latest" | grep "\"tag_name\":" | sed -E "s/.*\"([^\"]+)\".*/\1/")
CAN_RELEASE=$(abstractVersion $PROJECT_VERSION $REMOTE_VERSION)

if [ -z $REMOTE_VERSION ]; then
  echo "RELEASE_VERSION=$PROJECT_VERSION" >> $GITHUB_ENV
elif [ $CAN_RELEASE = "true" ]; then
  echo "RELEASE_VERSION=$PROJECT_VERSION" >> $GITHUB_ENV
else
  exit
fi