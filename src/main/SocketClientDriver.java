import java.net.InetAddress;
import java.io.IOException;


public class SocketClientDriver {
  public static void main(String args[]){
    try{
      int portNumber = 9876;
      InetAddress host = InetAddress.getLocalHost();
      String hostName = host.getHostName();
      SocketClient client = new SocketClient(hostName, portNumber);
      client.talkToServer();
    }catch(IOException e){
      System.out.println(e);
    }
  }
}
