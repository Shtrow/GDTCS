public abstract class Message{
  private MessageType type;
  private String[] args;
  
  //Constructors
  public Message(MessageType type, String[] args){
    this.type = type;
    this.args = args;
  }
  
  public String toNetFormat() {
    String res = type.toString() + "\n";
    for (String arg : args){
      res += arg + "\n";
    }
    res += ".";
    return res;
  }
}
