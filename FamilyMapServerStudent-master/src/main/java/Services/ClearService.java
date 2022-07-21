package Services;

import Common.Clear;
import Exceptions.DataAccessException;
import Responses.ClearResponse;

/**
 * Service for clear
 */
public class ClearService extends Service{

  public ClearResponse clear() {
    ClearResponse response;
    try {
      connect();
      Clear clearer = new Clear();
      clearer.clear(conn);
      response = new ClearResponse(true, "Clear succeeded.");
      disconnect();
    } catch (DataAccessException e) {
      response = new ClearResponse(false, "Error: " + e.getMessage());
      disconnect();
    }
    return response;
  }

}

