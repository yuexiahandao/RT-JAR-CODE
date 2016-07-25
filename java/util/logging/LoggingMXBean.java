package java.util.logging;

import java.util.List;

public abstract interface LoggingMXBean
{
  public abstract List<String> getLoggerNames();

  public abstract String getLoggerLevel(String paramString);

  public abstract void setLoggerLevel(String paramString1, String paramString2);

  public abstract String getParentLoggerName(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.LoggingMXBean
 * JD-Core Version:    0.6.2
 */