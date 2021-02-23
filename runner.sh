#!/bin/bash

USERNAME=sam97
NODE_HOSTS="montpelier.cs.colostate.edu trenton.cs.colostate.edu saint-paul.cs.colostate.edu denver.cs.colostate.edu boston.cs.colostate.edu jackson.cs.colostate.edu helena.cs.colostate.edu"
COLLATOR_HOST="columbus-oh.cs.colostate.edu"
PORT=21097

# for copying jar file to remote machine

# JARFILE_PATH="build/libs/hw1-1.0.jar"
# ssh -l ${USERNAME} ${COLLATOR_HOST} "mkdir cs445-assignment1"
# scp ${JARFILE_PATH} ${USERNAME}@${COLLATOR_HOST}:~/cs455-assignment1/hw1.jar



# #running messaging nodes

for HOSTNAME in ${NODE_HOSTS} ; do
    ssh -l ${USERNAME} ${HOSTNAME} java -cp cs455-assignment1/hw1.jar cs455.overlay.Main node ${HOSTNAME} ${PORT} ${COLLATOR_HOST} ${PORT} &
done

