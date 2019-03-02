import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.BindException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.UUID;

public class ChatServer {

  private static ServerSocket serverSocket;
  private static int portNumber = 60123;
  private static ConcurrentHashMap<UUID,ServiceConnection> connections = new ConcurrentHashMap<UUID,ServiceConnection>();


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
        UUID groupId = UUID.randomUUID();
        ServiceConnection connection = new ServiceConnection(groupId, serviceSocket);
        connection.start();
        connections.put(groupId, connection);
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }


  public static ConcurrentHashMap<UUID,ServiceConnection> getConnections(){
    return connections;
  }
}
