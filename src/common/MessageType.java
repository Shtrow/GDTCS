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

  REQUEST_ANC("REQUEST_ANC");

  private String name;

  private MessageType(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

}
