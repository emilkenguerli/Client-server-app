import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.BufferedInputStream;

public class ServiceConnection extends Thread {
  private Socket serviceSocket;
  private DataOutputStream out;
  // private BufferedReader in;
  private DataInputStream in;

  public ServiceConnection(Socket serviceSocket){
    this.serviceSocket = serviceSocket;
  }

  public void run(){
    try{
      // out = new PrintWriter(serviceSocket.getOutputStream(), true);
      // in = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
      out = new DataOutputStream(serviceSocket.getOutputStream());
      in = new DataInputStream(new BufferedInputStream(serviceSocket.getInputStream()));

      System.out.println("Just connected to " + serviceSocket.getRemoteSocketAddress());

      // THIS IS WHERE IT IS CURRENTLY NOT WORKING
      // possibly use: in = new Scanner(serviceSocket.getInputStream());

      while(!serviceSocket.isClosed()){
        String receivedMessage = in.readUTF();
        System.out.println("Message received from " + serviceSocket.getRemoteSocketAddress() + ": " + receivedMessage);
        ArrayList<ServiceConnection> connections = ChatServer.getConnections();
        sendMessages(connections, receivedMessage);
      }
      serviceSocket.close();
      System.out.println("Connection with " + serviceSocket.getRemoteSocketAddress() + " is now closed");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public DataOutputStream getWriter(){
    return out;
  }

  private void sendMessages(ArrayList<ServiceConnection> connections, String message){
    for(ServiceConnection connection: connections){
      try{
        connection.getWriter().writeUTF(message);
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }
}
