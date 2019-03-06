package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.lang.Integer;
import java.lang.Boolean;


public class Client{

  private static String clientId;
  private static String userName;
  private static String hostName;
  private static int portNumber = 60123;
  private static DataOutputStream out;
  private static DataInputStream in;
  private static OutputStream fileOut;
  private static InputStream fileIn;
  private static Socket clientSocket;
  private static ArrayList<String> pendingMessages = new ArrayList<String>();
  private static ArrayList<String> availableFiles = new ArrayList<String>();

  public static void main(String[] args){
    try{
      downloadFile(content, path);
      InetAddress host = InetAddress.getLocalHost();
      hostName = host.getHostName();
      clientSocket = new Socket(hostName, portNumber);
      fileOut = clientSocket.getOutputStream();
      fileIn = clientSocket.getInputStream();
      out = new DataOutputStream(clientSocket.getOutputStream());
      in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      System.out.println();

      Scanner keyboard = new Scanner(System.in);
      System.out.print("Enter a username: ");
      String username = keyboard.nextLine();
      groupInterface(keyboard);
      while (!clientSocket.isClosed()){
        System.out.println();
        chatInterface(keyboard);
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  public static void groupInterface(Scanner keyboard){
    boolean valid = false;
    while(!valid){
      System.out.print("Do you wish to (c)reate or (j)oin a group: ");
      String groupInputStatus = keyboard.nextLine();
      if(groupInputStatus.equalsIgnoreCase("c")){
        System.out.print("Group name: ");
        String groupName = keyboard.nextLine();
        System.out.print("Password: ");
        String password = keyboard.nextLine();
        valid = sendGroupName(groupName, password, true);
        if(valid){
          break;
        }else{
          System.out.println("Group already exists");
        }
      }else if(groupInputStatus.equalsIgnoreCase("j")){
        System.out.print("Group name: ");
        String groupName = keyboard.nextLine();
        System.out.print("Password: ");
        String password = keyboard.nextLine();
        valid = sendGroupName(groupName, password, false);
        if(valid){
          break;
        }else{
          System.out.println("Incorrect group name or password");
        }
      }else{
        System.out.println("Enter a valid response");
      }
    }
  }


  public static void chatInterface(Scanner keyboard){
    receiveMessages();
    System.out.println("Number of pending messages: " + pendingMessages.size());
    System.out.println("Available files: ");
    for(String fileName: availableFiles){
      System.out.println(fileName);
    }
    System.out.println();
    System.out.println("Available actions: (u)pload (d)ownload (s)end (r)eceive (q)uit");
    String selectedAction = keyboard.nextLine();
    switch(selectedAction){
      case "u":
        System.out.print("Enter file path: ");
        String fileUploadDestination = keyboard.nextLine();
        sendFile(fileUploadDestination);
        break;
      case "d":
        System.out.print("Enter file to download: ");
        String fileName = keyboard.nextLine();
        System.out.print("Enter download path: ");
        String fileDownloadDestination = keyboard.nextLine();
        receiveFile(fileName, fileDownloadDestination);
        break;
      case "s":
        System.out.print("Enter a message: ");
        String message = keyboard.nextLine();
        sendMessages(message);
        break;
      case "r":
        for(String receiveMessage: pendingMessages){
          System.out.println(receiveMessage);
        }
        pendingMessages.clear();
        break;
      case "q":
        quit();
        break;
      default:
        System.out.println("Invalid response");
    }
  }


  public static  byte[] uploadFile(String filePath) {
    FileInputStream fileInputStream = null;
    byte[] fileContent = null;
    try{
      File file = new File(filePath);
      fileContent = new byte[(int) file.length()];
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(fileContent);
    } catch(Exception e) {
      System.out.println(e);
    }finally{
      if (fileInputStream != null) {
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
      return fileContent;
    }
  }


  public static void downloadFile(byte[] fileContent, String filePath) {
    try {
      Path path = Paths.get(filePath);
      Files.write(path, fileContent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void sendFile(String filePath){
    try{
      OutputStream fileOut = clientSocket.getOutputStream();
      out.writeUTF("sending_file --true");
      out.writeUTF(filePath);
      byte[] fileContent = uploadFile(filePath);
      if(fileContent != null){
        fileOut.write(fileContent);
      }
    }catch(IOException e){
      System.out.println(e);
    }finally{
      if(fileOut != null){
        try {
          fileOut.close();
        }catch(IOException e){
          System.out.println(e);
        }
      }
    }
  }


  public static void receiveFile(String fileName, String filePath){
    try{
      InputStream fileInputStream = clientSocket.getInputStream();
      out.writeUTF("requesting_file --true");
      out.writeUTF(fileName);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while(fileInputStream.available() != 0) {
          baos.write(fileInputStream.read());
      }
      byte[] fileContent = baos.toByteArray();
      downloadFile(fileContent, filePath);
    }catch(IOException e){
      System.out.println(e);
    }
  }


  public static void sendMessages(String message){
    try{
      out.writeUTF("client_out_message --true");
      out.writeUTF(message);
    }catch(IOException e){
      System.out.println(e);
    }
  }


  public static void receiveMessages(){
    try{
      while(in.available() > 0){
        String receivedMessage = in.readUTF();
        switch(receivedMessage){
          case "new_file_uploaded --true":
            String uploadedFile = in.readUTF();
            availableFiles.add(uploadedFile);
            break;
          case "client_in_message --true":
            String clientMessage = in.readUTF();
            pendingMessages.add(clientMessage);
            break;
        }
      }
    }catch(IOException e){
      System.out.println(e);
    }
  }


  public static boolean sendGroupName(String groupName, String password, boolean newGroup){
    try{
      if(newGroup){
        out.writeUTF("set_group_name --new");
        out.writeUTF(groupName);
        out.writeUTF(password);
        String response = in.readUTF();
        return Boolean.parseBoolean(response);
      }else{
        out.writeUTF("set_group_name --existing");
        out.writeUTF(groupName);
        out.writeUTF(password);
        String response = in.readUTF();
        return Boolean.parseBoolean(response);
      }
    }catch(IOException e){
      System.out.println(e);
      return false;
    }
  }



  public static String getClientId(){
    return clientId;
  }


  public static void quit(){
    try{
      clientSocket.close();
      System.out.println("Connection is now closed");
    }catch(IOException e){
      System.out.println(e);
    }
  }
}
