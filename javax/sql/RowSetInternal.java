package javax.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface RowSetInternal
{
  public abstract Object[] getParams()
    throws SQLException;

  public abstract Connection getConnection()
    throws SQLException;

  public abstract void setMetaData(RowSetMetaData paramRowSetMetaData)
    throws SQLException;

  public abstract ResultSet getOriginal()
    throws SQLException;

  public abstract ResultSet getOriginalRow()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.RowSetInternal
 * JD-Core Version:    0.6.2
 */