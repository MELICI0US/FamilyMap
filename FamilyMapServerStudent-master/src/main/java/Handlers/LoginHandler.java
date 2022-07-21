package Handlers;

import Requests.LoginRequest;
import Responses.LoginResponse;
import Services.LoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler extends Handler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    LoginService service = new LoginService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Headers reqHeaders = exchange.getRequestHeaders();

        Gson gson = new Gson();
        InputStream reqBody = (exchange.getRequestBody());
        String reqData = readString(reqBody);
        System.out.println(reqData);
        LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
        System.out.println("request.toString = " + request.toString());

        LoginResponse response = service.login(request, null);

        System.out.println("response:");
        System.out.println(response.toString());

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
}
