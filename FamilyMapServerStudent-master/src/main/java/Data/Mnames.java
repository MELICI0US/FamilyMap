package Data;

import java.util.ArrayList;

public class Mnames extends Data{
  public ArrayList<String> data;

  public Mnames(ArrayList<String> list) {
    data = list;
  }

  public String getRandomName(){
    return getRandom(data);
  }

  public ArrayList<String> getData() {
    return data;
  }
}
