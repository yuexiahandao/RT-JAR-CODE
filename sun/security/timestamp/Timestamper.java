package sun.security.timestamp;

import java.io.IOException;

public abstract interface Timestamper
{
  public abstract TSResponse generateTimestamp(TSRequest paramTSRequest)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.timestamp.Timestamper
 * JD-Core Version:    0.6.2
 */