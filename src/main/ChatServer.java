import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public class ChatServer {

  private static ServerSocket serverSocket;
  private static int portNumber = 60123;
  private static ArrayList<ServiceConnection> connections = new ArrayList<ServiceConnection>();

  public static void main(String args[]){
    initialiseServerSocket();
    acceptClients();
  }

  public static void initialiseServerSocket(){
    try{
      serverSocket = new ServerSocket(portNumber);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public static void acceptClients(){
    while(true){
      try{
        Socket serviceSocket = serverSocket.accept();
        ServiceConnection connection = new ServiceConnection(serviceSocket);
        connections.add(connection);
        connection.start();
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }

  public static ArrayList<ServiceConnection> getConnections(){
    return connections;
  }
}
