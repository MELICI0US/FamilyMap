package Data;

import java.util.ArrayList;

public class Locations extends Data {
  public ArrayList<Location> data;

  public Location getRandomLocation() {
    return getRandom(data);
  }

  public ArrayList<Location> getData() {
    return data;
  }
}
