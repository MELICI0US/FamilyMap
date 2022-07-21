package Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fnames extends Data{
  public ArrayList<String> data;

  public Fnames() {}

  public String getRandomName(){
    return getRandom(data);
  }
//  public String getRandom(ArrayList<String> list){
//    Random rand = new Random();
//    int upperbound = list.size();
//    int random = rand.nextInt(upperbound);
//
//    return list.get(random);
//  }

  public ArrayList<String> getData() {
    return data;
  }
}
