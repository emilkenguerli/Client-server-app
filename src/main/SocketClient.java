import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient{

  private String hostName;
  private int portNumber;

  public SocketClient(String hostName, int portNumber){
    this.hostName = hostName;
    this.portNumber = portNumber;
  }


  public void talkToServer(){
    try{
      boolean talking = true;
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      Socket clientSocket = new Socket(hostName, portNumber);
      System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
      OutputStream outToServer = clientSocket.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);

      // InputStream inFromServer = clientSocket.getInputStream();
      // DataInputStream in = new DataInputStream(inFromServer);

      System.out.println("Write a message to the server: ");
      Scanner keyboard = new Scanner(System.in);
      System.out.print(clientSocket.getInetAddress() + ": ");
      String clientMessage = keyboard.nextLine();
      out.writeUTF(clientMessage);

      // String serverMessage = in.readUTF();
      // System.out.println(clientSocket.getRemoteSocketAddress() + ": " + serverMessage);

      clientSocket.close();
      System.out.println("Connection is now closed");
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

}
