
/**
 * GDTService
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.*;

public class GDTService implements Runnable {
  public Socket socket;
  private final int poolSize = 200;
  private final ExecutorService executor;
  int SERV_PORT = 1027;
  String SERV_ADDR = "localhost";

  BufferedReader in;
  PrintWriter out;


  public GDTService(String serverAddress, int serverPort) throws IOException {
    this.SERV_PORT = serverPort;
    this.SERV_ADDR = serverAddress;
    in =
            new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
    out =
            new PrintWriter(socket.getOutputStream(),true);


    this.socket = socket;
    this.executor = Executors.newFixedThreadPool(poolSize);
  }

  public Future<String> getProducts(String domain){
    return executor.submit(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "somme product";
      }
    });
  }
  @Override
  public void run() {

  }
}
