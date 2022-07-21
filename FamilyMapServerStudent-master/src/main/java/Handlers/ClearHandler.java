package Handlers;

import Responses.ClearResponse;
import Services.ClearService;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler extends Handler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    ClearService service = new ClearService();

    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Headers reqHeaders = exchange.getRequestHeaders();

        ClearResponse response = service.clear();

        System.out.println("response:");
        System.out.println(response.toString());

        success = response.getSuccess();

        if (!success) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        } else {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }

        OutputStream respBody = exchange.getResponseBody();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writeString(gson.toJson(response), respBody);

        respBody.close();
      }

    } catch (IOException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
      exchange.getResponseBody().close();

      e.printStackTrace();
    }
  }
}

