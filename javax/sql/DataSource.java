package javax.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Wrapper;

public abstract interface DataSource extends CommonDataSource, Wrapper
{
  public abstract Connection getConnection()
    throws SQLException;

  public abstract Connection getConnection(String paramString1, String paramString2)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.DataSource
 * JD-Core Version:    0.6.2
 */