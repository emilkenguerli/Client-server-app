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

public class Client{

  private static String clientId;
  private static String userName;
  private static String hostName;
  private static int portNumber = 60123;
  private static PrintWriter out;
  private static BufferedReader in;
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
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      while (!clientSocket.isClosed()){
        String receivedMessage;
        if(in.ready()){
          receivedMessage = in.readLine();
        }else{
          receivedMessage = null;
        }
        if(receivedMessage != null){
          System.out.println("Message received: " + receivedMessage);
        }
        System.out.println("receivedMessage: " + receivedMessage);
        System.out.println("Send a message to the group or type 'QUIT' to disconnect: ");
        Scanner keyboard = new Scanner(System.in);
        System.out.print(clientSocket.getInetAddress() + ": ");
        String clientMessage = keyboard.nextLine();
        if(clientMessage == "QUIT"){
          clientSocket.close();
          System.out.println("Connection is now closed");
        }else{
          out.write(clientMessage);
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
