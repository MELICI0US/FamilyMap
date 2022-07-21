package Data;

import java.util.ArrayList;
import java.util.Random;

public class Data {

  public <T> T getRandom(ArrayList<T> list){
    Random rand = new Random();
    int upperbound = list.size();
    int random = rand.nextInt(upperbound);

    return list.get(random);
  }
}
