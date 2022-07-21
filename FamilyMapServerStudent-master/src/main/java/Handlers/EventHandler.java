package Handlers;

import Models.AuthToken;
import Responses.EventResponse;
import Responses.PersonResponse;
import Services.EventService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler extends Handler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    EventService service = new EventService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        EventResponse response;
        Gson gson;

        if (reqHeaders.containsKey("Authorization")) {

          AuthToken authToken = new AuthToken(reqHeaders.getFirst("Authorization"), null);

          String uri = exchange.getRequestURI().toString();
          StringBuilder eventID = new StringBuilder("");
          if (uri.length() > 6) {
            int i = 7;
            char c = uri.charAt(i);
            while (c != '/' && i != uri.length()) {
              eventID.append(c);
              i++;
              if (i < uri.length()) {
                c = uri.charAt(i);
              }
            }

            response = service.getEvent(eventID.toString(), authToken);
          } else {
            response = service.getEventTable(authToken);
          }

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
      }


    } catch (Exception e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
      exchange.getResponseBody().close();

      e.printStackTrace();
    }
  }
}
