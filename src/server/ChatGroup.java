package server;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.net.Socket;


import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

public class ChatGroup {

  ConcurrentHashMap<UUID,ServiceConnection> connections = new ConcurrentHashMap<UUID, ServiceConnection>();
  ConcurrentHashMap<String,byte[]> files = new ConcurrentHashMap<String,byte[]>();
  String groupName;
  String password;
  UUID groupId;


  public ChatGroup(String groupName, String password, UUID groupId){
    this.groupName = groupName;
    this.password = password;
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


  public void sendMessages(String message, boolean fileUploaded){
    for(ServiceConnection connection: connections.values()){
      try{
        DataOutputStream writer = connection.getWriter();
        if(fileUploaded){
          writer.writeUTF("new_file_uploaded --true");
          writer.writeUTF(message);
        }else{
          writer.writeUTF("client_in_message --true");
          writer.writeUTF(message);
        }
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
      String path = "../"+fileName;
      downloadFile(fileContent, path);
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


  public String getPassword(){
    return password;
  }

  public void downloadFile(byte[] fileContent, String filePath) {
    try {
      Path path = Paths.get(filePath);
      Files.write(path, fileContent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
