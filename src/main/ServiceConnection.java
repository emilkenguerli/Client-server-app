import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceConnection extends Thread {
  private UUID clientId;
  private UUID groupId;
  private Socket serviceSocket;
  private SocketAddress remoteSocketAddress;
  private DataOutputStream out;
  private DataInputStream in;


  public ServiceConnection(UUID clientId, Socket serviceSocket){
    this.clientId = clientId;
    this.serviceSocket = serviceSocket;
    this.remoteSocketAddress = serviceSocket.getRemoteSocketAddress();
  }


  public void run(){
    try{
      out = new DataOutputStream(serviceSocket.getOutputStream());
      in = new DataInputStream(new BufferedInputStream(serviceSocket.getInputStream()));
      System.out.println("Just connected to " + remoteSocketAddress);
      String groupName = in.readUTF();
      ChatGroup group = ChatServer.addConnection(groupName, this);
      while(!serviceSocket.isClosed()){
        if(!(in.available() == 0)){
          String receivedMessage = in.readUTF();
          System.out.println("Message received from " + remoteSocketAddress + ": " + receivedMessage);
          // ChatGroup group = ChatServer.getGroup(groupId);
          group.sendMessages(receivedMessage);
          // ConcurrentHashMap<UUID,ServiceConnection> connections = ChatServer.getConnections();
          // sendMessages(connections, receivedMessage);
        }
      }
      System.out.println("Connection with " + remoteSocketAddress + " is now closed");
    } catch (IOException e) {
      System.out.println(e);
    }
  }


  private void sendMessages(ConcurrentHashMap<UUID,ServiceConnection> connections, String message){
    for(ServiceConnection connection: connections.values()){
      try{
        connection.getWriter().writeUTF(message);
      }catch(IOException e){
        connections.remove(connection.getClientId());
        System.out.println("Connection with remote socket " + connection.getRemoteSocketAddress() + " has been closed");
      }
    }
  }


  public void setGroupId(UUID groupId){
    this.groupId = groupId;
  }


  public UUID getGroupId(){
    return groupId;
  }


  public SocketAddress getRemoteSocketAddress(){
    return this.remoteSocketAddress;
  }


  public DataOutputStream getWriter(){
      return out;
  }


  public UUID getClientId(){
    return clientId;
  }
}
