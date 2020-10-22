import common.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * ProductProvider
 */

public class DataProvider {
  private GDTService service;
  public DataProvider(GDTService service){
    this.service = service;
  }

  private static void basicRequest(Message message,
                                   Message.MessageType expected,
                                   Message.MessageType error,
                                   Consumer<Message> success,
                                   Consumer<Message> failure) {
    if(message.getType() == expected) success.accept(message);
    else if(message.getType() == expected) failure.accept(message);
    // Fill the Logger
  }

  private void parseNetMessage(String string){
    Message message = Message.stringToMessage(string);
    switch (message.getType()){
      case CONNECT:

        break;
      case CONNECT_OK:
        break;
      case CONNECT_NEW_USER_OK:
        break;
      case CONNECT_NEW_USER_KO:
        break;
      case CONNECT_KO:
        break;
      case DISCONNECT:
        break;
      case POST_ANC:
        break;
      case POST_ANC_OK:
        break;
      case POST_ANC_KO:
        break;
      case MAJ_ANC:
        break;
      case MAJ_ANC_OK:
        break;
      case MAJ_ANC_KO:
        break;
      case DELETE_ANC:
        break;
      case DELETE_ANC_OK:
        break;
      case DELETE_ANC_KO:
        break;
      case REQUEST_DOMAIN:
        break;
      case SEND_DOMAINE_OK:
        break;
      case SEND_DOMAIN_KO:
        break;
      case REQUEST_ANC:
        break;
      case SEND_ANC_OK:
        break;
      case SEND_ANC_KO:
        break;
      case REQUEST_OWN_ANC:
        break;
      case SEND_OWN_ANC_OK:
        break;
      case SEND_OWN_ANC_KO:
        break;
      case REQUEST_IP:
        break;
      case REQUEST_IP_OK:
        break;
      case REQUEST_IP_KO:
        break;
      case CONNECT_PAIR:
        break;
      case CONNECT_PAIR_REJECTED:
        break;
      case CONNECT_PAIR_OK:
        break;
      case CONNECT_PAIR_KO:
        break;
      case DISCONNECT_PAIR:
        break;
      case SEND_MSG:
        break;
      case SEND_MSG_OK:
        break;
      case SEND_MSG_KO:
        break;
    }

    
  }

  public void connect() throws InterruptedException, ExecutionException {
    CompletableFuture<Message> answer = service.askFor(new Message(Message.MessageType.CONNECT));
    answer.thenAccept(
            message -> {
              switch (message.getType()){
                case CONNECT_OK:
                  System.out.println("You are connected !");
                case CONNECT_KO:
                  System.out.println("Connexion refused by the server");
                default:
                  // Fill the Logger with the message
              }
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
    Consumer<Message> successBehavior = m -> System.out.println("Annonce posted !" + m.toNetFormat());
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
    answer.thenAccept(
            m -> basicRequest(m, Message.MessageType.POST_ANC_OK, Message.MessageType.POST_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void updateAnc(String[] annonce){
    Message message = new Message(Message.MessageType.MAJ_ANC,annonce);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce updated !");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAccept(
            m -> basicRequest(m, Message.MessageType.MAJ_ANC_OK, Message.MessageType.MAJ_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void deleteAnc(String ancId){
    Message message = new Message(Message.MessageType.DELETE_ANC, new String[]{ancId});
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce succesfuly deleted");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAccept(
            m -> basicRequest(m, Message.MessageType.DELETE_ANC_OK, Message.MessageType.DELETE_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void talkWith(String peerUserName){

  }
}
