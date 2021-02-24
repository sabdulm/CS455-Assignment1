package cs455.overlay;


import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 6 && args.length != 5) {
            System.out.println("Invalid number of args");
            System.out.println("Usage for collator => java -cp <jarfilepath> <main_class> collator <hostname> <port> <number of nodes> <number of rounds> <messages per round>");
            System.out.println("Usage for node => java -cp <jarfilepath> <main_class> node <hostname> <port> <collator_hostname> collator_port");
            return;
        }

        try{
            // extracts type of node(collator or regular "node") and the hostname and port.
            String type = args[0];
            String hostname = args[1];
            int port = Integer.parseInt(args[2]);
            if(Objects.equals(type, "collator")){
                // if type is collator then extracts the number of nodes, messages and rounds from the arguments;
                // creates the collator node and calls its runMethod
                int numNodes = Integer.parseInt(args[3]);
                int numRounds = Integer.parseInt(args[4]);
                int numMessages = Integer.parseInt(args[5]);
                Collator collator = new Collator(hostname, port, numNodes, numRounds, numMessages);
                collator.runCollator();
            } else if (Objects.equals(type, "node")) {
                // if type == node then gets the collator hostname and port from the arguments
                // creates the node and calls it runMethod
                String collatorHostName = args[3];
                int collatorPort = Integer.parseInt(args[4]);
                Node node = new Node(hostname, port, collatorHostName, collatorPort);
                node.runNode();
            } else {
                //invalid type supplied
                System.out.println("Invalid type supplied in argument");
            }
        } catch (Exception e) {
            System.out.println("Invalid arguments supplied");
            e.printStackTrace();
        }


    }



}
