package Handlers;

import Models.AuthToken;
import Requests.FillRequest;
import Responses.FillResponse;
import Responses.PersonResponse;
import Services.PersonService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler extends Handler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    PersonService service = new PersonService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        PersonResponse response;
        Gson gson;

        if (reqHeaders.containsKey("Authorization")) {

          AuthToken authToken = new AuthToken(reqHeaders.getFirst("Authorization"), null);

            String uri = exchange.getRequestURI().toString();
            StringBuilder personID = new StringBuilder("");
            if (uri.length() > 7) {
              int i = 8;
              char c = uri.charAt(i);
              while (c != '/' && i != uri.length()) {
                personID.append(c);
                i++;
                if (i < uri.length()) {
                  c = uri.charAt(i);
                }
              }

              response = service.getPerson(personID.toString(), authToken);
            } else {
              response = service.getPersonTable(authToken);
            }


            System.out.println("response: " + response.toString());

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

