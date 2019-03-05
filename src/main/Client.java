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

  public static void main(String argsp[]){
    try{
      InetAddress host = InetAddress.getLocalHost();
      hostName = host.getHostName();
      clientSocket = new Socket(hostName, portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }
    talkToServer();
  }


  private static void talkToServer(){
    try{
      fileOut = clientSocket.getOutputStream();
      fileIn = clientSocket.getInputStream();
      out = new DataOutputStream(fileOut);
      in = new DataInputStream(new BufferedInputStream(fileIn));
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      System.out.print("Enter a valid group name: ");
      Scanner keyboard = new Scanner(System.in);
      String groupName = keyboard.nextLine();
      out.writeUTF(groupName);
      while (!clientSocket.isClosed()){
        System.out.println();
        if(!(in.available() == 0)){
          System.out.println("Received messages:");
          while(in.available() > 0){
            String receivedMessage = in.readUTF();
            if(receivedMessage.split(" ")[0].equals("new_file_uploaded")){
              System.out.println(receivedMessage.split(" ")[1]);
            }
            System.out.println(receivedMessage);
          }
        }else{
          System.out.println("Received messages: None");
        }
        System.out.print("Do you want to send a file (y/n): ");
        String fileTransfer = keyboard.nextLine();
        if(fileTransfer.equalsIgnoreCase("y")){
          sendFile(keyboard);
        }
        System.out.print("Do you want to receive a file (y/n): ");
        String fileReceive = keyboard.nextLine();
        if(fileReceive.equalsIgnoreCase("y")){
          receiveFile(keyboard);
        }
        System.out.println("Send a message to the group or type 'QUIT' to disconnect: ");
        System.out.print(clientSocket.getInetAddress() + ": ");
        String clientMessage = keyboard.nextLine();
        if(clientMessage.equals("QUIT")){
          out.close();
          clientSocket.close();
          System.out.println("Connection is now closed");
        }else{
          out.writeUTF(clientMessage);
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public static byte[] uploadFile(String filePath) {
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


  public static void sendFile(Scanner keyboard){
    try{
      System.out.print("Enter file path: ");
      String filePath = keyboard.nextLine();
      out.writeUTF("sending_file --true");
      out.writeUTF(filePath);
      byte[] fileContent = uploadFile(filePath);
      fileOut.write(fileContent);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public static void receiveFile(Scanner keyboard){
    try{
      out.writeUTF("requesting_file --true");
      System.out.print("Specify file name: ");
      String fileName = keyboard.nextLine();
      out.writeUTF(fileName);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while(fileIn.available() != 0) {
          baos.write(fileIn.read());
      }
      byte[] fileContent = baos.toByteArray();
      String filePath = "../" + fileName;
      downloadFile(fileContent, filePath);
    }catch(IOException e){
      System.out.println(e);
    }
  }


  public String getClientId(){
    return clientId;
  }
}
