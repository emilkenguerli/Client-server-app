import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread{
  // private Client client;
  private static Socket serviceSocket;

  public Connection(Socket serviceSocket){
    // this.client = client;
    this.serviceSocket = serviceSocket;
  }

  public void run(){
    try{
      boolean connected = true;
      System.out.println("Just connected to " + serviceSocket.getRemoteSocketAddress());
      while(connected){
        DataInputStream in = new DataInputStream(serviceSocket.getInputStream());
        System.out.println("Message received: " + in.readUTF());
        DataOutputStream out = new DataOutputStream(serviceSocket.getOutputStream());
        out.writeUTF("Thank you for connecting to " + serviceSocket.getLocalSocketAddress() + "\nGoodbye!");
        System.out.println();
      }
      serviceSocket.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
