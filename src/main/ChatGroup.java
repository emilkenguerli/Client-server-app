import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.net.Socket;

public class ChatGroup {

  ConcurrentHashMap<UUID,ServiceConnection> connections = new ConcurrentHashMap<UUID, ServiceConnection>();
  String groupName;
  UUID groupId;


  public ChatGroup(String groupName, UUID groupId){
    this.groupName = groupName;
    this.groupId = groupId;
  }


  public void addConnection(ServiceConnection connection){
    UUID clientId = connection.getClientId();
    connections.put(clientId, connection);
  }

  public void sendMessages(String message){
    for(ServiceConnection connection: connections.values()){
      try{
        connection.getWriter().writeUTF(message);
      }catch(IOException e){
        connections.remove(connection.getClientId());
        System.out.println("Connection with remote socket " + connection.getRemoteSocketAddress() + " has been closed");
      }
    }
  }

  public String getGroupName(){
    return groupName;
  }


  public UUID getGroupId(){
    return groupId;
  }


  public ConcurrentHashMap<UUID,ServiceConnection> getConnections(){
    return connections;
  }

}
