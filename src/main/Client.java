import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;

public class Client{

  private static String clientId;
  private static String userName;
  private static String hostName;
  private static int portNumber = 60123;
  // private static PrintWriter out;
  private static DataOutputStream out;
  private static DataInputStream in;
  private static Socket clientSocket;


  public static void main(String argsp[]){
    try{
      InetAddress host = InetAddress.getLocalHost();
      hostName = host.getHostName();
      clientSocket = new Socket(hostName, portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }
    talkToServer();
  }


  private static void talkToServer(){
    try{
      // out = new PrintWriter(clientSocket.getOutputStream(), true);
      out = new DataOutputStream(clientSocket.getOutputStream());
      in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      while (!clientSocket.isClosed()){
        if(!(in.available() == 0)){
          System.out.println("Received messages:");
          while(in.available() > 0){
            String receivedMessage = in.readUTF();
            System.out.println(receivedMessage);
          }
        }else{
          System.out.println("Received messages: None");
        }
        System.out.println("Send a message to the group or type 'QUIT' to disconnect: ");
        Scanner keyboard = new Scanner(System.in);
        System.out.print(clientSocket.getInetAddress() + ": ");
        String clientMessage = keyboard.nextLine();
        if(clientMessage.equals("QUIT")){
          out.close();
          clientSocket.close();
          System.out.println("Connection is now closed");
        }else{
          out.writeUTF(clientMessage);
        }
        System.out.println();
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  public String getClientId(){
    return clientId;
  }

}
