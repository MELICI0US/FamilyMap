package Server;

import java.io.*;
import java.net.*;

import Handlers.*;
import com.sun.net.httpserver.*;

public class Server {

  private static final int MAX_WAITING_CONNECTIONS = 12;

  private HttpServer server;

  private void run(String portNumber) {

    System.out.println("Initializing HTTP Server.Server");

    try {
      server = HttpServer.create(
              new InetSocketAddress(Integer.parseInt(portNumber)),
              MAX_WAITING_CONNECTIONS);
    }
    catch (IOException e) {
      e.printStackTrace();
      return;
    }

    server.setExecutor(null);

    System.out.println("Creating contexts");

    server.createContext("/user/register", new RegisterHandler());

    server.createContext("/user/login", new LoginHandler());
    server.createContext("/clear", new ClearHandler());
    server.createContext("/fill", new FillHandler());
    server.createContext("/load", new LoadHandler());
    server.createContext("/person", new PersonHandler());
    server.createContext("/event", new EventHandler());
    server.createContext("/", new DefaultHandler());

    System.out.println("Starting server");

    server.start();

    System.out.println("Server.Server started");

    LoadedData loader = new LoadedData();
    try {
      loader.load();
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String portNumber = args[0];
    new Server().run(portNumber);
  }
}

