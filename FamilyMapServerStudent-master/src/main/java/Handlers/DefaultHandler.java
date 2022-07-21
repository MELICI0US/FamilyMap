package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler extends Handler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        String urlPath = exchange.getRequestURI().toString();

        System.out.println("Getting url " + urlPath);

        if (urlPath.equals("/")) {
          urlPath = "/index.html";
        }

        String filePath = "web" + urlPath;

        File f = new File(filePath);
        if (f.exists()) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
          OutputStream respBody = exchange.getResponseBody();
          Files.copy(f.toPath(), respBody);
          respBody.close();
        } else {
          f = new File("web/HTML/404.html");
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
          OutputStream respBody = exchange.getResponseBody();
          Files.copy(f.toPath(), respBody);
          respBody.close();
        }
      }
    } catch (IOException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
      exchange.getResponseBody().close();

      e.printStackTrace();
    }
  }

}

