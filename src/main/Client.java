import java.net.InetAddress;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{

  private static String clientId;
  private static String userName;
  private static String hostName;
  private static int portNumber = 60123;


  public static void main(String argsp[]){
    try{
      InetAddress host = InetAddress.getLocalHost();
      hostName = host.getHostName();
    } catch (IOException e) {
      System.out.println(e);
    }
    talkToServer();
  }


  private static void talkToServer(){
    try{
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      Socket clientSocket = new Socket(hostName, portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      OutputStream outToServer = clientSocket.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);

      System.out.println("Write a message to the server: ");
      Scanner keyboard = new Scanner(System.in);
      System.out.print(clientSocket.getInetAddress() + ": ");
      String clientMessage = keyboard.nextLine();
      out.writeUTF(clientMessage);

      // String serverMessage = in.readUTF();
      // System.out.println(clientSocket.getRemoteSocketAddress() + ": " + serverMessage);

      clientSocket.close();
      System.out.println("Connection is now closed");
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  public String getClientId(){
    return clientId;
  }

}
