import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.net.Socket;

public class ChatGroup {

  ConcurrentHashMap<UUID,ServiceConnection> connections = new ConcurrentHashMap<UUID, ServiceConnection>();
  ConcurrentHashMap<String,byte[]> files = new ConcurrentHashMap<String,byte[]>();
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

  public void addFile(String fileName, byte[] fileContent){
    System.out.println(fileName + " is now in the database");
    files.put(fileName, fileContent);
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

  public void sendFile(UUID clientId, String fileName){
    try{
      ServiceConnection connection = connections.get(clientId);
      OutputStream fileOut = connection.getFileOut();
      byte[] fileContent = files.get(fileName);
      fileOut.write(fileContent);
    }catch(IOException e){
      System.out.println(e);
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
