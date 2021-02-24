#!/bin/bash

USERNAME=sam97
NODE_HOSTS="montpelier.cs.colostate.edu trenton.cs.colostate.edu saint-paul.cs.colostate.edu boston.cs.colostate.edu jackson.cs.colostate.edu helena.cs.colostate.edu"
COLLATOR_HOST="columbus-oh.cs.colostate.edu"
PORT=21097

JARFILE_PATH="build/libs/hw1-final.jar"
REMOTE_JAR_PATH="cs455-assignment1"

# for copying jar file to remote machine

build_jar() {
    gradle clean
    gradle build
}

copy_jar() {
    ssh -l ${USERNAME} ${COLLATOR_HOST} "mkdir ${REMOTE_JAR_PATH}"
    scp ${JARFILE_PATH} ${USERNAME}@${COLLATOR_HOST}:~/${REMOTE_JAR_PATH}/hw1.jar
}

run_jar() {
    for HOSTNAME in ${NODE_HOSTS} ; do
        ssh -l ${USERNAME} ${HOSTNAME} java -cp ${REMOTE_JAR_PATH}/hw1.jar cs455.overlay.Main node ${HOSTNAME} ${PORT} ${COLLATOR_HOST} ${PORT} &
    done
}

print_usage() {
    echo "Usage: sh runner.sh -flag\n-b to build jar\n-c to copy jar to remote\n-r to run jar on remote machines"
}

clean() {
    gradle clean
}

getopts 'bcrf' flag;
    case "${flag}" in 
    b) build_jar ;;
    c) copy_jar ;;
    r) run_jar ;;
    f) clean ;;
    *) print_usage
        exit 1 ;;
    esac



