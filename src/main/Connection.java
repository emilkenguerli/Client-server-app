import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread{
  private static Socket serviceSocket;

  public Connection(Socket serviceSocket){
    this.serviceSocket = serviceSocket;
  }

  public void run(){
    try{
      System.out.println("Just connected to " + serviceSocket.getRemoteSocketAddress());
      System.out.println();
      DataInputStream in = new DataInputStream(serviceSocket.getInputStream());
      String receivedMessage = in.readUTF();
      System.out.println("Message received from" + serviceSocket.getRemoteSocketAddress() + ": " + receivedMessage);

      // DataOutputStream out = new DataOutputStream(serviceSocket.getOutputStream());
      // out.writeUTF("Message recieved at " + serviceSocket.getLocalSocketAddress());
      // The server doesn't need to write out but we'll need this functionality eventually

      serviceSocket.close();
      System.out.println("Connection with " + serviceSocket.getRemoteSocketAddress() + " is now closed");
      System.out.println();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
