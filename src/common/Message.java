package common;

public class Message{
  private MessageType type;
  private String[] args;
  
  //Constructors
  public Message(MessageType type, String[] args){
    this.type = type;
    this.args = args;
  }
  public Message(MessageType type){
    this.type = type;
    this.args = null;
  }
  
  public String toNetFormat() {
    String res = type.toString();
    res += "\n";
    if(args != null){
      for (String arg : args){
        res += arg + "\n";
      }
    }
    res += ".";
    return res;
  }

  public enum MessageType {

    CONNECT("CONNECT"),

    CONNECT_OK("CONNECT_OK"),

    CONNECT_NEW_USER_OK("CONNECT_NEW_USER_OK"),

    CONNECT_NEW_USER_KO("CONNECT_NEW_USER_KO"),

    CONNECT_KO("CONNECT_KO"),

    DISCONNECT("DISCONNECT"),

    POST_ANC("POST_ANC"),

    POST_ANC_OK("POST_ANC_OK"),

    POST_ANC_KO("POST_ANC_KO"),

    MAJ_ANC("MAJ_ANC"),

    MAJ_ANC_OK("MAJ_ANC_OK"),

    MAJ_ANC_KO("MAJ_ANC_KO"),

    DELETE_ANC("DELETE_ANC"),

    DELETE_ANC_OK("DELETE_ANC_OK"),

    DELETE_ANC_KO("DELETE_ANC_KO"),

    REQUEST_DOMAIN("REQUEST_DOMAIN"),

    SEND_DOMAINE_OK("SEND_DOMAINE_OK"),

    SEND_DOMAIN_KO("SEND_DOMAIN_KO"),

    REQUEST_ANC("REQUEST_ANC"),

    SEND_ANC_OK("SEND_ANC_OK"),

    SEND_ANC_KO("SEND_ANC_KO"),

    REQUEST_OWN_ANC("REQUEST_OWN_ANC"),

    SEND_OWN_ANC_OK("SEND_OWN_ANC_OK"),

    SEND_OWN_ANC_KO("SEND_OWN_ANC_KO"),

    REQUEST_IP("REQUEST_IP"),

    REQUEST_IP_OK("REQUEST_IP_OK"),

    REQUEST_IP_KO("REQUEST_IP_KO"),

    CONNECT_PAIR("CONNECT_PAIR"),

    CONNECT_PAIR_REJECTED("CONNECT_PAIR_REJECTED"),

    CONNECT_PAIR_OK("CONNECT_PAIR_OK"),

    CONNECT_PAIR_KO("CONNECT_PAIR_KO"),

    DISCONNECT_PAIR("DISCONNECT_PAIR"),

    SEND_MSG("SEND_MSG"),

    SEND_MSG_OK("SEND_MSG_OK"),

    SEND_MSG_KO("SEND_MSG_KO");

    private String name;

    private MessageType(String name){
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

  }

}
