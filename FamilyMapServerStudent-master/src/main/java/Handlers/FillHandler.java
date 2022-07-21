package Handlers;

import Requests.FillRequest;
import Requests.LoginRequest;
import Responses.FillResponse;
import Responses.LoginResponse;
import Services.FillService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import java.io.IOException;

public class FillHandler extends Handler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    System.out.println("Handling...");
    FillService service = new FillService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Headers reqHeaders = exchange.getRequestHeaders();

        Gson gson = new Gson();

        FillRequest request = createRequest(exchange);

        System.out.println("request.toString = " + request.toString());

        FillResponse response = service.fill(request, null, null);

        success = response.getSuccess();

        if (!success) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        } else {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }

        OutputStream respBody = exchange.getResponseBody();

        gson = new GsonBuilder().setPrettyPrinting().create();
        writeString(gson.toJson(response), respBody);

        respBody.close();

      }


    } catch (Exception e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
      exchange.getResponseBody().close();

      e.printStackTrace();
    }
  }

  private FillRequest createRequest(HttpExchange exchange){
    String uri = exchange.getRequestURI().toString();

    StringBuilder username = new StringBuilder("");
    int i = 6;
    char c = uri.charAt(i);
    while (c != '/' && i != uri.length()) {
      username.append(c);
      i++;
      if (!(i >= uri.length())){
        c = uri.charAt(i);
      }
    }

    int generations;

    if (i != uri.length()){
      generations = Character.getNumericValue(uri.charAt(i+1));
    } else {
      generations = 4;
    }

    return new FillRequest(username.toString(), generations);
  }

}
