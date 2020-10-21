#!/bin/bash
while getopts ":v:" opt; do
  case ${opt} in
  v)
    VERSION="$OPTARG"
    ;;
  \?)
    echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done

if [[ -z ${VERSION} ]]; then
  echo ":::::::::: ERROR: Version wasn't specified ::::::::::"
  echo ":::::::::: You should specify version with '-v' flag ::::::::::"
  echo ":::::::::: Creating app failed ::::::::::"
  exit 1
fi

./gradlew :app:clean
./gradlew :app:assembleRelease --stacktrace -Psdkversion=${VERSION}

if [[ "$?" != "0" ]]; then
  echo ":::::::::::::::::: BUILD FAILED ::::::::::::::::::"
  exit 1
fi
