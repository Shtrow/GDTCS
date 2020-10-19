/*
 * Main class
 */

package client;

import common.Message;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Future;

public class Client{
  public Client(){}


  public static void main(String [] args) throws IOException {
    int SERV_PORT = 1027;
    String SERV_ADDR = "localhost";

    if ( args.length > 1){

      SERV_PORT = Integer.parseInt(args[1]);
      SERV_ADDR = args[0];
    }
    welcome();

    //Socket creation
    Socket serverSocket = null;
    try {
      serverSocket = new Socket(InetAddress.getByName(SERV_ADDR),SERV_PORT);
    } catch (IOException e) {
      System.out.println("Connection to server failed\n");
      e.printStackTrace();
    }

    BufferedReader in =
            new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
    PrintWriter out =
            new PrintWriter(serverSocket.getOutputStream(),true);



    connect(in,out);

  }

  private static void welcome() {
    System.out.println("Hello User !");
  }

  private static void connect(BufferedReader in, PrintWriter out) throws IOException {
    //Creation of the "CONNECT" message
    Message connectMessage = new Message(Message.MessageType.CONNECT, new String[]{"User1"});

    //Sending CONNECT message
    out.print(connectMessage.toNetFormat());
    System.out.println("Sending :\n"+connectMessage.toNetFormat());


    String answer;
    while((answer = in.readLine()) != null){
      String connect_ok = Message.MessageType.CONNECT_OK.toString();
      String connect_new_user_ok = Message.MessageType.CONNECT_NEW_USER_OK.toString();
      if (connect_ok.equals(answer) || connect_new_user_ok.equals(answer)) {
        System.out.println("Connection accomplished !");
      }
      System.out.println("Packet received : " + answer);
    }
    //Future<String> answer = new Future<String>(in.readLine());
  }
}
