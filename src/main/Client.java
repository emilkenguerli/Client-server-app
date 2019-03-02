import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client{

  private static String clientId;
  private static String userName;
  private static String hostName;
  private static int portNumber = 60123;
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
      System.out.print("Enter a valid group name: ");
      Scanner keyboard = new Scanner(System.in);
      String groupName = keyboard.nextLine();
      out.writeUTF(groupName);
      while (!clientSocket.isClosed()){
        System.out.println();
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
        System.out.print(clientSocket.getInetAddress() + ": ");
        String clientMessage = keyboard.nextLine();
        if(clientMessage.equals("QUIT")){
          out.close();
          clientSocket.close();
          System.out.println("Connection is now closed");
        }else{
          out.writeUTF(clientMessage);
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  public String getClientId(){
    return clientId;
  }
}
