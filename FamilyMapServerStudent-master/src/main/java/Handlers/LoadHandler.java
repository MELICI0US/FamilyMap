package Handlers;

import Requests.LoadRequest;
import Requests.RegisterRequest;
import Responses.LoadResponse;
import Responses.RegisterResponse;
import Services.LoadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends Handler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    LoadService service = new LoadService();
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Gson gson = new Gson();
        InputStream reqBody = (exchange.getRequestBody());
        String reqData = readString(reqBody);
        System.out.println(reqData);
        LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
        System.out.println("request.toString = " + request.toString());

        LoadResponse response = service.load(request);

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


