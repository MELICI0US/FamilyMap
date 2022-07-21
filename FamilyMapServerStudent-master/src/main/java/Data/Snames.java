package Data;

import java.util.ArrayList;

public class Snames extends Data{
  public ArrayList<String> data = new ArrayList<String>();

  public Snames(ArrayList<String> list) {
    data = list;
  }

  public ArrayList<String> getData() {
    return data;
  }

  public String getRandomName(){
    return getRandom(data);
  }
}
