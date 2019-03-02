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
  private static ConcurrentHashMap<UUID,ChatGroup> groups = new ConcurrentHashMap<UUID,ChatGroup>();


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
        UUID clientId = UUID.randomUUID();
        ServiceConnection connection = new ServiceConnection(clientId, serviceSocket);
        connection.start();
        connections.put(clientId, connection);
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }


  public static ChatGroup addConnection(String groupName, ServiceConnection connection){
    ChatGroup searchGroup = null;
    for(ChatGroup group : groups.values()) {
      if(group.getGroupName().equals(groupName)) {
        searchGroup = group;
        connection.setGroupId(searchGroup.getGroupId());
        break;
      }
    }
    if(searchGroup == null){
      UUID groupId = UUID.randomUUID();
      searchGroup = new ChatGroup(groupName, groupId);
      searchGroup.addConnection(connection);
      groups.put(groupId, searchGroup);
      System.out.println("Made new group");
    }else{
      searchGroup.addConnection(connection);
      System.out.println("Didn't make new group");
    }
    for(ServiceConnection connection2: searchGroup.getConnections().values()){
      System.out.println(connection2.getRemoteSocketAddress());
    }
    return searchGroup;
  }


  public static ChatGroup getGroup(UUID groupId){
    return groups.get(groupId);
  }


  public static ConcurrentHashMap<UUID,ChatGroup> getGroups(){
    return groups;
  }

  public static ConcurrentHashMap<UUID,ServiceConnection> getConnections(){
    return connections;
  }
}
