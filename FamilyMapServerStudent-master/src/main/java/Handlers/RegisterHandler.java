package Handlers;

import java.io.*;
import java.net.*;

import Requests.RegisterRequest;
import Responses.RegisterResponse;
import Services.RegisterService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.*;

public class RegisterHandler extends Handler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    RegisterService service = new RegisterService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Gson gson = new Gson();
        InputStream reqBody = (exchange.getRequestBody());
        String reqData = readString(reqBody);
        System.out.println(reqData);
        RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);
        System.out.println("request.toString = " + request.toString());

        RegisterResponse response = service.register(request);

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

