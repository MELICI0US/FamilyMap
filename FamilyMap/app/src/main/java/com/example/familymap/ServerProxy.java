package com.example.familymap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class ServerProxy {

    private static final String LOG_TAG = "ServerProxy";

    public String getData(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setRequestProperty("Authorization", authToken);

            System.out.println("Trying to connect...");
            connection.connect();
            System.out.println("Connected!");

            InputStream responseBody = connection.getInputStream();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = responseBody.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            connection.disconnect();

            return outputStream.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return
                    "{\n" +
                            "\t\n" +
                            "  \"message\": \"" + e.getMessage() + "\",\n" +
                            "\t\n" +
                            "  \"success\": false\n" +
                            "\n" +
                            "}";
        }
    }

    public String postUrl(URL url, String request) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            System.out.println("Trying to connect...");
            connection.connect();
            System.out.println("Connected!");

            OutputStream reqBody = connection.getOutputStream();
            writeString(request, reqBody);
            reqBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = responseBody.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                connection.disconnect();

                return outputStream.toString();
            } else {
                InputStream responseBody = connection.getErrorStream();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = responseBody.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                connection.disconnect();

                return outputStream.toString();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return
                    "{\n" +
                            "\n" +
                            "  \"message\": \"" + e.getMessage() + "\",\n" +
                            "\n" +
                            "  \"success\": false\n" +
                            "\n" +
                            "}";
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
