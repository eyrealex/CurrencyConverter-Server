
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class UDPEchoServer {

    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;

    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            dgramSocket = new DatagramSocket(PORT);//Step 1.
        } catch (SocketException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        run();
    }// end main method 

    private static void run() {
        try {
            String messageIn, messageOut = null;
            int numMessages = 0;
            double currency = 0.0;
            String oldc = "";
            String newc = "";
            do {
                buffer = new byte[256]; //Step 2.
                inPacket = new DatagramPacket(buffer, buffer.length); //Step 3.
                dgramSocket.receive(inPacket); //Step 4.
                InetAddress clientAddress = inPacket.getAddress(); //Step 5.
                int clientPort = inPacket.getPort(); //Step 5. 
                messageIn = new String(inPacket.getData(), 0, inPacket.getLength()); //Step 6 takes in the users message that was sent from an outpacket and stores it in messageIn

                String[] result = messageIn.split("\\s+"); //splits the users input up into different pieces

                if(result.length == 3) { //ensure that the users input only consists of 3 parts
                    try { //if it is perform a try
                        currency = Double.parseDouble(result[0]); //parse the first part of the split into a double 
                        oldc = result[1].toLowerCase(); //converts the string in the second split to lower case
                        newc = result[2].toLowerCase(); //converts the string in the third split to lower case
                    } catch (Exception e) { //if the try fails make sure the variables are set to default
                        currency = 0.0;
                        oldc = "";
                        newc = "";
                    }

                } else {
                    currency = 0.0;
                    oldc = "";
                    newc = "";
                }

                //if statement for converting Euro to USD
                if (oldc.equals("euro") && (newc.equals("usd"))) {
                    currency *= 1.10;
                    messageOut = ("The converted amount is: " + currency);
                }//end if statement for converting Euro to USD
                //else if statement for converting Euro to GBP
                else if (oldc.equals("euro") && (newc.equals("gbp"))) {
                    currency *= 0.89;
                    messageOut = ("The converted amount is: " + currency);
                }//end else if statement for converting Euro to GBP
                //else if statement for converting USD to Euro
                else if (oldc.equals("usd") && (newc.equals("euro"))) {
                    currency *= 0.84;
                    messageOut = ("The converted amount is: " + currency);
                }//end else if statement for converting USD to Euro
                //else if statement for converting USD to CNY
                else if (oldc.equals("usd") && (newc.equals("cny"))) {
                    currency *= 6.94;
                    messageOut = ("The converted amount is: " + currency);
                }//end else if statement for converting USD to CNY
                //else 
                else {
                    messageOut = ("Please follow the format");
                }//end else

                System.out.println("Message " + numMessages + " received.");
                numMessages++;
                // messageOut = ("Message " + numMessages + ": " + messageIn);
                outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                dgramSocket.send(outPacket); //Step 8.

            } //end do 
            while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally { //If exception thrown, close connection.
            System.out.println("\n* Closing connection... *");
            dgramSocket.close(); //Step 9.
        }//end finally
    }// run method
}//end UDPEchoServer
