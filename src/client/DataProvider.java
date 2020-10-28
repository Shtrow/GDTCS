package client;

import common.Annonce;
import common.Logs;
import common.Message;

import java.util.List;
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
  String userName = null;
  String token = null;

  private static void basicRequest(Message message,
                                   Message.MessageType expected,
                                   Message.MessageType error,
                                   Consumer<Message> success,
                                   Consumer<Message> failure) {
    if (message == null) {Logs.log("TIMEOUT : Server does not respond \n Disconnection..."); System.exit(1);}
    if(message.getType() == expected) success.accept(message);
    else if(message.getType() == error  ) failure.accept(message);
    //TODO : Add NO_CONNECTED and UNKNOWN_REQUEST cases
    else {Logs.error("Communication error \nServer response :"+ message.toNetFormat());}
    // Fill the Logger
  }

  public void connect(){
    if(token == null){
      System.out.println("The first time you connect, you have to choose a user name. \n connect [user name]");
      return;
    }
    Message message = new Message(Message.MessageType.CONNECT, new String[]{token});
    service.askFor(message)
    .thenAccept(
            m -> {
              basicRequest(m, Message.MessageType.CONNECT_OK, Message.MessageType.CONNECT_KO,
                      v -> System.out.println("Connected! Welcome back "+userName),
                      v -> System.out.println("Connection refused by the server"));
            }
    );
  }

  public void connect(String userName) {
    this.userName = userName;
    Message message = new Message(Message.MessageType.CONNECT, new String[]{userName});
    service.askFor(message)
            .thenAccept(
                    m -> {
                      basicRequest(m, Message.MessageType.CONNECT_NEW_USER_OK, Message.MessageType.CONNECT_NEW_USER_KO,
                              v -> {
                                this.token = "#" + m.getArgs()[0];
                                System.out.println("Connected! Welcome "+userName+"\n Your token is : "+token);
                              },
                              v -> System.out.println("Connection refused by the server"));
                    }
            );
  }

  public void disconnect(){
    service.send(new Message(Message.MessageType.DISCONNECT));
    System.out.println("Disconnected");
    System.exit(1);
  }

  public void getProductByDomain(String domain){
    Message message = new Message(Message.MessageType.REQUEST_ANC, new String[]{domain});
    Consumer<Message> successBehavior = answer -> {
      //TODO: DISPLAY CORRECTLY
      Logs.log("OK");
    };
    Consumer<Message> failBehaviour = answer -> {Logs.error("Domain not found");};
    service.askFor(message).
            thenAccept(
              answer->{
                basicRequest(answer, Message.MessageType.SEND_ANC_OK, Message.MessageType.SEND_ANC_KO,successBehavior,failBehaviour);
              }
            );

  }

  public void getMyAn(){
    Message message = new Message(Message.MessageType.REQUEST_OWN_ANC);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> {
      System.out.println("My posts : \n");
      productViewer.displayProductList(m.getArgs());
    };
    Consumer<Message> failBehavior = m -> Logs.error("Post not found");
    answer.thenAccept(
            m -> basicRequest(m, Message.MessageType.SEND_OWN_ANC_OK, Message.MessageType.SEND_OWN_ANC_KO,successBehavior,failBehavior)
    );
  }

  public void postAnc(String[] annonce){
    Message message = new Message(Message.MessageType.POST_ANC,annonce);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Annonce posted !");
    Consumer<Message> failBehavior = m -> Logs.error("The server refused your annonce");
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
    Consumer<Message> successBehavior = m -> System.out.println("Post succesfuly deleted");
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.DELETE_ANC_OK, Message.MessageType.DELETE_ANC_KO,successBehavior,failBehavior)
    );
  }
  public void getDomains(){
    Message message = new Message(Message.MessageType.REQUEST_DOMAIN);
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("Here your domain :" );
    Consumer<Message> failBehavior = m -> System.out.println("Oupsi");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.SEND_DOMAINE_OK, Message.MessageType.SEND_DOMAINE_OK,successBehavior,failBehavior)
    );
  }
  public void requestIP(String postID){
    Message message = new Message(Message.MessageType.REQUEST_IP, new String[]{postID});
    var answer = service.askFor(message);
    Consumer<Message> successBehavior = m -> System.out.println("IP = : "+m.getArgs()[0]);
    Consumer<Message> failBehavior = m -> System.out.println("The server refused your request");
    answer.thenAcceptAsync(
            m -> basicRequest(m, Message.MessageType.REQUEST_IP_OK, Message.MessageType.REQUEST_IP_KO,successBehavior,failBehavior)
    );
  }

  public void talkWith(String peerUserName){

  }
}
