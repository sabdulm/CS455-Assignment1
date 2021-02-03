package cs455.overlay;


import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Invalid number of args: Usage => java -cp <jarfilepath> <type> <hostname> <port>");
        }
        String type = args[0];
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);
        if(Objects.equals(type, "collator")){
            // run collator node;
            Collator collator = new Collator(hostname, port);
            collator.runCollator();
        } else if (Objects.equals(type, "node")) {
            // node
        } else {
            System.out.println("Invalid type supplied in argument");
        }
    }



}
