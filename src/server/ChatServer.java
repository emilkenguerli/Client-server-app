package server;

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


  public static ChatGroup addConnectionToNewGroup(String groupName, String password, ServiceConnection connection){
    for(ChatGroup group : groups.values()) {
      if(group.getGroupName().equals(groupName)) {
        return null;
      }
    }
    UUID groupId = UUID.randomUUID();
    ChatGroup searchGroup = new ChatGroup(groupName, password, groupId);
    searchGroup.addConnection(connection);
    groups.put(groupId, searchGroup);
    return searchGroup;
  }


  public static ChatGroup addConnectionToExistingGroup(String groupName, String password, ServiceConnection connection){
    ChatGroup searchGroup = null;
    for(ChatGroup group : groups.values()) {
      if(group.getGroupName().equals(groupName)) {
        if(!password.equals(group.getPassword())){
          return null;
        }else{
          searchGroup = group;
          connection.setGroupId(group.getGroupId());
          searchGroup.addConnection(connection);
          break;
        }
      }
    }
    System.out.println("Correct function call: " + searchGroup);
    return searchGroup;
  }


  public static Socket establishFileConnection(){
    Socket fileServiceSocket = null;
    try{
      fileServiceSocket = serverSocket.accept();
      FileConnection fileConnection = new FileConnection(fileServiceSocket);
    }catch(IOException e){
      System.out.println(e);
    }finally{
      return fileServiceSocket;
    }
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
