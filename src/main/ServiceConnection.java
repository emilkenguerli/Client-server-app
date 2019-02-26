import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServiceConnection extends Thread {
  private Socket serviceSocket;
  private PrintWriter out;
  private BufferedReader in;

  public ServiceConnection(Socket serviceSocket){
    this.serviceSocket = serviceSocket;
  }

  public void run(){
    try{
      out = new PrintWriter(serviceSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
      System.out.println("Just connected to " + serviceSocket.getRemoteSocketAddress());
      // THIS IS WHERE IT IS CURRENTLY NOT WORKING
      while(!serviceSocket.isClosed()){
        System.out.println(in.ready());
        if(in.ready()){
          String receivedMessage = in.readLine();
          System.out.println("Message received from " + serviceSocket.getRemoteSocketAddress() + ": " + receivedMessage);
          ArrayList<ServiceConnection> connections = ChatServer.getConnections();
          sendMessages(connections, receivedMessage);
        }
      }
      serviceSocket.close();
      System.out.println("Connection with " + serviceSocket.getRemoteSocketAddress() + " is now closed");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public PrintWriter getWriter(){
    return out;
  }

  private void sendMessages(ArrayList<ServiceConnection> connections, String message){
    for(ServiceConnection connection: connections){
      connection.getWriter().write(message);
    }
  }
}
