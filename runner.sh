#!/bin/bash

# USERNAME=sam97
# NODE_HOSTS="montpelier.cs.colostate.edu trenton.cs.colostate.edu saint-paul.cs.colostate.edu"
# COLLATOR_HOST="columbus-oh.cs.colostate.edu"
# PORT=21097

# COLLATOR_SCRIPT="ls"
# NODE_SCRIPT="git"

# # running collator node
# ssh -l ${USERNAME} ${COLLATOR_HOST} "${COLLATOR_SCRIPT}"

# #running messaging nodes

# for HOSTNAME in ${NODE_HOSTS} ; do
#     ssh -l ${USERNAME} ${HOSTNAME} "${NODE_SCRIPT}"
# done

# java -cp hw1-1.0.jar cs455.overlay.Main collator 127.0.0.1 10000 3 50 50 &
# echo "here"
PORTS="8500 9000 9500 7000"

for PORT in ${PORTS} ; do
    java -cp build/libs/hw1-1.0.jar cs455.overlay.Main node 127.0.0.1 ${PORT} 127.0.0.1 10000 &
done
