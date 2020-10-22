
/**
 * GDTService
 */
import common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.function.Supplier;

/*
* This class handle all the back-end part
* It brings a methods to communicate with the server to get or update product post.
* */
public class GDTService implements Runnable {
  public Socket socket;
  int SERV_PORT;
  String SERV_ADDR = "localhost";

  BufferedReader in;
  PrintWriter out;


  public GDTService(String serverAddress, int serverPort) throws IOException {
    this.SERV_PORT = serverPort;
    this.SERV_ADDR = serverAddress;
    socket = new Socket(InetAddress.getByName(SERV_ADDR),SERV_PORT);

    in =
            new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
    out =
            new PrintWriter(socket.getOutputStream(),true);
  }

    public GDTService() throws IOException {
//    Default socket address
      this("localhost",1027);
    }

    private String readMessage() throws IOException {
      String message = "";
      do {
        message += in.readLine();
      }
      while(!message.equals(".")  );
      return message;
    }

    public CompletableFuture<Message> askFor(Message request){
      return CompletableFuture.supplyAsync( () ->  {
        out.print(request);
        try {
          return Message.stringToMessage(readMessage());
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }

      );
    }
  @Override
  public void run() {

  }
}
