/**
 * Controller
 */

package client;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

public class Controller implements Runnable {

  private DataProvider dataProvider;

  private final Runnable missingArg = () -> System.out.println("Missing arguments");

  public Controller(DataProvider dataProvider){
    this.dataProvider = dataProvider;
  }

  @Override
  public void run(){
    Scanner scanner = new Scanner(System.in);
    while(true){
      System.out.print(">>");
      String commandString = scanner.nextLine();
      Runnable command = parse(commandString);
      command.run();
    }
  }
  public Runnable parse(String command){
    String[] tokens = command.split("\\s+");
    if (tokens.length<1) return () -> {};
    switch (tokens[0]){
      case "mpost":
        return dataProvider::getMyAn;
      case "connect":
        if(tokens.length <2){
          return dataProvider::connect;
        }
        else {
          return () -> {
            dataProvider.connect(tokens[1]);
          };
        }

      case "post":
        if (tokens.length<5) return missingArg;
        else {
          return (() -> dataProvider.postAnc(Arrays.copyOfRange(tokens,1,tokens.length)));
        }
      case "disconnect":
        return dataProvider::disconnect;
      case "echo":
        return () -> System.out.println(command);
      default:
        return () -> {};
    }


  }
}
