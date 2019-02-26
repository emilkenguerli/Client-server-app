import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServer {

  //static ServerSocket variable
  //We will only ever have one serverSocket and so it makes sense to have it declared here as static
  private static ServerSocket serverSocket;
   //Only one service socket for now.(We need to incorporate multiple threads somehow)
  private static int portNumber = 60123;
  //may be beneficial to have the port number in a config file.

  public static void main(String args[]){
    initialiseServerSocket();
    listen();
  }

  public static void initialiseServerSocket(){
    try{
      serverSocket = new ServerSocket(portNumber);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public static void listen(){
    while(true){
      try{
        Socket serviceSocket = serverSocket.accept();
        Connection connection = new Connection(serviceSocket);
        connection.start();
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }
}
