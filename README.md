# CS455-Assignment1

Sheikh Mannan - sheikh.mannan@colostate.edu

## Pre-Reqs
 
* Java 11/15 (works on both)
* ssh
* scp
* bash scripting

## How to run

* to run the collator

    `java -cp <jar file> cs455.overlay.Main collator <server_hostname> <server_port> <number_nodes> <number_rounds> <number_messages> `

* to run a messaging node

    `java -cp <jar file> cs455.overlay.Main collator <server_hostname> <server_port> <collator_hostname> <collator_port> `

* note that the collator should already be running before you run a messaging node.

## Runner script for CSU dept machines

* the script for running the messaging nodes on the dept machines is runner.sh
* Need to change the following variables in the script accordingly:

    1. USERNAME
    2. COLLATOR_HOSTNAME
    3. PORT (I am assuming all the nodes and the collator run their server on the same port since they are all on different machines)
    4. NODE_HOSTS
    5. JARFILE_PATH (path to jar on the local machine)
    6. REMOTE_JAR_PATH (directory to keep the jar on the remote machine)

* the script has two functionalities: 
    
    1. It can copy the jar file to your desired directory on the remote machine. To do this pass the -c flag to the script. This uses the scp command.
    2.  It can run nodes on the list of dept machines that you have specified provided the jar file is present in the path you specify. To do this pass the flag -r

    * Note the script will only work for one flag at a time.


## Code documentation

* Main.java - the entry point.
* Collator.java - contains all the logic for the collator.
* CollatorThread.java - handles all the client connections and calls the appropriate functions in the Collator object.
* Node.java - contains all the logic for the node.
* NodeThread.java - handles all the client connections and calls the appropriate functions in the Node object.
* MessageRegister.java - messaging nodes send this to the collator to register themselves.
* MessageStartRounds.java - collator sends this message to all connected nodes to signal the start of the first round. The message includes the list of other nodes and number of rounds and messages as well.
* MessagePayload.java - a messaging nodes sends this to another node which contains a payload, round number and message number.
* MessageDoneSending.java - nodes signal the collator that they have completed their rounds with this type of message.
* MessageSendSummary.java - collators sends this message to ask the nodes for the summary of their rounds.
* MessageSummary.java - in this type of message, nodes send the summary of their rounds.
* MessageClose.java - the collator sends this message to nodes to signal them to shutdown their servers since the process has been completed.