package java.sql;

import java.util.Properties;
import java.util.logging.Logger;

public abstract interface Driver
{
  public abstract Connection connect(String paramString, Properties paramProperties)
    throws SQLException;

  public abstract boolean acceptsURL(String paramString)
    throws SQLException;

  public abstract DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties)
    throws SQLException;

  public abstract int getMajorVersion();

  public abstract int getMinorVersion();

  public abstract boolean jdbcCompliant();

  public abstract Logger getParentLogger()
    throws SQLFeatureNotSupportedException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Driver
 * JD-Core Version:    0.6.2
 */