package Server;

import Data.Fnames;
import Data.Locations;
import Data.Mnames;
import Data.Snames;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LoadedData {
  public static Fnames fnames;
  public static Mnames mnames;
  public static Snames snames;
  public static Locations locations;

  public void load() throws IOException {
    Gson gson = new Gson();

    BufferedReader br = new BufferedReader(new FileReader("json/fnames.json"));

    fnames = gson.fromJson(br, Fnames.class);


    br = new BufferedReader(new FileReader("json/mnames.json"));

    mnames = gson.fromJson(br, Mnames.class);


    br = new BufferedReader(new FileReader("json/snames.json"));

    snames = gson.fromJson(br, Snames.class);



    br = new BufferedReader(new FileReader("json/locations.json"));

    locations = gson.fromJson(br, Locations.class);

  }
}
