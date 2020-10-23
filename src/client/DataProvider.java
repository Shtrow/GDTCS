package client;

import common.Message;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * ProductProvider
 */
public class DataProvider {
  private GDTService service;
  public DataProvider(GDTService service){
    this.service = service;
  }
  ProductViewer productViewer = new ProductViewer();

  private static void basicRequest(Message message,
                                   Message.MessageType expected,
                                   Message.MessageType error,
                                   Consumer<Message> success,
                                   Consumer<Message> failure) {
    if(message.getType() == expected) success.accept(message);
    else if(message.getType() == expected) failure.accept(message);
    else {System.out.println("Communication error \n"+ message.toNetFormat());}
    // Fill the Logger
  }

  public void connect(){
    Message message = new Message(Message.MessageType.CONNECT, new String[]{"Gontran Girodon"});
    service.askFor(message)
    .thenAcceptAsync(
            m -> {
              basicRequest(m, Message.MessageType.CONNECT_OK, Message.MessageType.CONNECT_KO,
                      v -> System.out.println("Connected !"),
                      v -> System.out.println("Connection refused by the server"));
            }
    );
  }

  public String[][] getProductByDomain(String domain){
    return null;
  }

  public String[][] getDomain(){
    return null;
  }

  public void getMyAn(){
    Message message = new Message(Message.MessageType.REQUEST_OWN_ANC);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> {
      System.out.println("Annonce posted ! \n");
      productViewer.displayProductList(m.getArgs());
    };
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your annonce");
    answer.thenAccept(
            m -> basicRequest(m, Message.MessageType.SEND_ANC_OK, Message.MessageType.SEND_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void postAnc(String[] annonce){
    Message message = new Message(Message.MessageType.POST_ANC,annonce);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce posted !");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your annonce");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.POST_ANC_OK, Message.MessageType.POST_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void updateAnc(String[] annonce){
    Message message = new Message(Message.MessageType.MAJ_ANC,annonce);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce updated !");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.MAJ_ANC_OK, Message.MessageType.MAJ_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void deleteAnc(String ancId){
    Message message = new Message(Message.MessageType.DELETE_ANC, new String[]{ancId});
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce succesfuly deleted");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.DELETE_ANC_OK, Message.MessageType.DELETE_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void talkWith(String peerUserName){

  }
}
