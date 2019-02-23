import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient{


  public static void main(String args[]) throws UnknownHostException{
    int portNumber = 9876;
    InetAddress host = InetAddress.getLocalHost();
    String hostName = host.getHostName();
    try {
      System.out.println("Connecting to " + hostName + " on port " + portNumber);
      Socket client = new Socket(hostName, portNumber);

      System.out.println("Just connected to " + client.getRemoteSocketAddress());
      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);

      out.writeUTF("Hello from " + client.getLocalSocketAddress());
      InputStream inFromServer = client.getInputStream();
      DataInputStream in = new DataInputStream(inFromServer);

      System.out.println("Server says " + in.readUTF());
      client.close();
   } catch (IOException e) {
      e.printStackTrace();
   }
 }

}
