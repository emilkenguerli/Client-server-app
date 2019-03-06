package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Integer;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;


public class ServiceConnection extends Thread {
  private UUID clientId;
  private UUID groupId;
  private Socket serviceSocket;
  private SocketAddress remoteSocketAddress;
  private DataOutputStream out;
  private DataInputStream in;
  private InputStream fileIn;
  private OutputStream fileOut;
  private ChatGroup group;


  public ServiceConnection(UUID clientId, Socket serviceSocket){
    this.clientId = clientId;
    this.serviceSocket = serviceSocket;
    this.remoteSocketAddress = serviceSocket.getRemoteSocketAddress();
  }

  public void run(){
    try{
      fileIn = serviceSocket.getInputStream();
      fileOut = serviceSocket.getOutputStream();
      out = new DataOutputStream(fileOut);
      in = new DataInputStream(new BufferedInputStream(fileIn));
      System.out.println("Just connected to " + remoteSocketAddress);
      while(!serviceSocket.isClosed()){
        String instruction = in.readUTF();
        System.out.println(remoteSocketAddress + ": " + instruction);
        String groupName;
        String password;
        switch(instruction){
          case "set_group_name --new":
            groupName = in.readUTF();
            password = in.readUTF();
            group = ChatServer.addConnectionToNewGroup(groupName,password, this);
            if(group == null){
              out.writeUTF("false");
            }else{
              out.writeUTF("true");
            }
            break;
          case "set_group_name --existing":
            groupName = in.readUTF();
            password = in.readUTF();
            group = ChatServer.addConnectionToExistingGroup(groupName,password, this);
            if(group == null){
              out.writeUTF("false");
            }else{
              out.writeUTF("true");
            }
            break;
          case "sending_file --true":
            String receivedPath = in.readUTF();
            sendFile(receivedPath);
            break;
          case "requesting_file --true":
            String fileName = in.readUTF();
            group.sendFile(this.clientId, fileName);
          case "client_out_message --true":
            String receivedMessage = in.readUTF();
            System.out.println(remoteSocketAddress + " message: " + receivedMessage);
            group.sendMessages(receivedMessage, false);
        }
      }
      System.out.println("Connection with " + remoteSocketAddress + " is now closed");
    }catch(IOException e){
      System.out.println(e);
    }
  }


  // public void run(){
  //   try{
  //     fileIn = serviceSocket.getInputStream();
  //     fileOut = serviceSocket.getOutputStream();
  //     out = new DataOutputStream(fileOut);
  //     in = new DataInputStream(new BufferedInputStream(fileIn));
  //     System.out.println("Just connected to " + remoteSocketAddress);
  //     String groupName = in.readUTF();
  //     ChatGroup group = ChatServer.addConnection(groupName, this);
  //     while(!serviceSocket.isClosed()){
  //       if(!(in.available() == 0)){
  //         String receivedMessage = in.readUTF();
  //         if(receivedMessage.equals("sending_file --true")){
  //           System.out.println("File transfer called");
  //           String receivedPath = in.readUTF();
  //           ByteArrayOutputStream baos = new ByteArrayOutputStream();
  //           while(fileIn.available() != 0) {
  //               baos.write(fileIn.read());
  //           }
  //           byte[] fileContent = baos.toByteArray();
  //           String[] fileNameArray = receivedPath.split("/");
  //           String fileName = fileNameArray[fileNameArray.length -1];
  //           group.addFile(fileName, fileContent);
  //           group.sendMessages("new_file_uploaded " + fileName);
  //         } else if(receivedMessage.equals("requesting_file --true")) {
  //           String fileName = in.readUTF();
  //           group.sendFile(this.clientId, fileName);
  //         }else{
  //           System.out.println("Message received from " + remoteSocketAddress + ": " + receivedMessage);
  //           group.sendMessages(receivedMessage);
  //         }
  //       }
  //     }
  //     System.out.println("Connection with " + remoteSocketAddress + " is now closed");
  //   } catch (IOException e) {
  //     System.out.println(e);
  //   }
  // }
  public void sendFile(String receivedPath){
    try{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while(fileIn.available() != 0) {
          baos.write(fileIn.read());
      }
      byte[] fileContent = baos.toByteArray();
      String[] fileNameArray = receivedPath.split("/");
      String fileName = fileNameArray[fileNameArray.length -1];
      group.addFile(fileName, fileContent);
      group.sendMessages(fileName, true);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public void downloadFile(byte[] fileContent, String filePath) {
    try {
      Path path = Paths.get(filePath);
      Files.write(path, fileContent);
    } catch (IOException e) {
      e.printStackTrace();
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


  public OutputStream getFileOut(){
    return fileOut;
  }


  public UUID getClientId(){
    return clientId;
  }
}
