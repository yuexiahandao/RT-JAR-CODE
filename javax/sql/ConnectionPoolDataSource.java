package javax.sql;

import java.sql.SQLException;

public abstract interface ConnectionPoolDataSource extends CommonDataSource
{
  public abstract PooledConnection getPooledConnection()
    throws SQLException;

  public abstract PooledConnection getPooledConnection(String paramString1, String paramString2)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.ConnectionPoolDataSource
 * JD-Core Version:    0.6.2
 */