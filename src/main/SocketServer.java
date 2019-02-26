import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

  //static ServerSocket variable
  //We will only ever have one serverSocket and so it makes sense to have it declared here as static
  private static ServerSocket serverSocket;
  private static Socket serviceSocket; //Only one service socket for now.(We need to incorporate multiple threads somehow)
  //socket server port on which it will listen.
  private static int portNumber = 9876;
  //may be beneficial to have the port number in a config file.

  public static void main(String args[]){
    initialiseServerSocket();
    listenRespond();
  }

  public static void initialiseServerSocket(){
    try{
      serverSocket = new ServerSocket(portNumber);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public static void listenRespond(){
    while(true){
      try{
        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        serviceSocket = serverSocket.accept();
        boolean connected = true;
        System.out.println("Just connected to " + serviceSocket.getRemoteSocketAddress());
        while(connected){
          DataInputStream in = new DataInputStream(serviceSocket.getInputStream());
          System.out.println("Message received: " + in.readUTF());
          DataOutputStream out = new DataOutputStream(serviceSocket.getOutputStream());
          out.writeUTF("Thank you for connecting to " + serviceSocket.getLocalSocketAddress() + "\nGoodbye!");
          System.out.println();
        }
        serviceSocket.close();
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }
}
