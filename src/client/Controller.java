/**
 * Controller
 */

package client;
import java.util.Arrays;
import java.util.Scanner;

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
      case "own":
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
      case "delete":
        if(tokens.length <2){
          return missingArg;
        }
        else {
          return () -> {
            dataProvider.deleteAnc(tokens[1]);
          };
        }
      case "post":
        if (tokens.length<5) return missingArg;
        else {
          return (() -> dataProvider.postAnc(Arrays.copyOfRange(tokens,1,tokens.length)));
        }
      case "update" :
        if (tokens.length<5) return missingArg;
        else {
          return (() -> dataProvider.updateAnc(Arrays.copyOfRange(tokens,1,tokens.length)));
        }
      case "exit":
        return dataProvider::disconnect;
      case "echo":
        return () -> System.out.println(command);
      case "domains" :
        return dataProvider::getDomains;
      case "ancs":
        return () ->dataProvider.getProductByDomain(tokens[1]);
      case "ip" :
        return () -> dataProvider.requestIP(tokens[1]);
      case "" :
        return () -> {};
      case "help" :
        return this::displayHelp;
      default:
        return () -> System.out.println("Command not found");
    }


  }

  private void displayHelp(){
    int indentSize = 20;
    String s = "" +
            "- %" + -indentSize + "s Reconnect to the server\n" +
            "- %" + -indentSize + "s Quite GTDT Client\n" +
            "- %" + -indentSize + "s Display available domains\n" +
            "- %" + -indentSize + "s Display all \"annonces\" from DOMAIN\n" +
            "- %" + -indentSize + "s Request your \"annonces\"\n" +
            "- %" + -indentSize + "s Post an \"annonces\" (enter interactive mode)\n" +
            "- %" + -indentSize + "s Update an \"annonces\"(enter interactive mode)\n" +
            "- %" + -indentSize + "s Request the ip of the \"annonces\" owner\n"+
            "- %" + -indentSize + "s Delete an \"annonces\"\n";
            System.out.printf(s,
            "connect","exit","domains","ancs [DOMAIN]","own","post","update [ANC ID]","ip [ANC ID]","delete [ANC ID]");
  }
}
