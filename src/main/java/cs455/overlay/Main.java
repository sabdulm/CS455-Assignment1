package cs455.overlay;


import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 6 && args.length != 5) {
            System.out.println("Invalid number of args: \nUsage for collator => java -cp <jarfilepath> <main_class> collator <hostname> <port> <number of nodes> <number of rounds> <messages per round>\nUsage for node => java -cp <jarfilepath> <main_class> node <hostname> <port> <collator_hostname> collator_port\n");
            return;
        }


        String type = args[0];
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);
        if(Objects.equals(type, "collator")){
            // run collator node;
            int numNodes = Integer.parseInt(args[3]);
            int numRounds = Integer.parseInt(args[4]);
            int numMessages = Integer.parseInt(args[5]);
            Collator collator = new Collator(hostname, port, numNodes, numRounds, numMessages);
            collator.runCollator();
        } else if (Objects.equals(type, "node")) {
            String collatorHostName = args[3];
            int collatorPort = Integer.parseInt(args[4]);
            Node node = new Node(hostname, port, collatorHostName, collatorPort);
            node.runNode();
        } else {
            System.out.println("Invalid type supplied in argument");
        }
    }



}
