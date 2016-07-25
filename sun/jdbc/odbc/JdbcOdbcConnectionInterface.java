package sun.jdbc.odbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public abstract interface JdbcOdbcConnectionInterface extends Connection
{
  public abstract long getHDBC();

  public abstract String getURL();

  public abstract int getODBCVer();

  public abstract void validateConnection()
    throws SQLException;

  public abstract void deregisterStatement(Statement paramStatement);

  public abstract void setBatchVector(Vector paramVector, Statement paramStatement);

  public abstract Vector getBatchVector(Statement paramStatement);

  public abstract void removeBatchVector(Statement paramStatement);

  public abstract int getBatchRowCountFlag(int paramInt);

  public abstract short getOdbcCursorType(int paramInt);

  public abstract int getOdbcCursorAttr2(short paramShort)
    throws SQLException;

  public abstract short getOdbcConcurrency(int paramInt);

  public abstract short getBestOdbcCursorType();

  public abstract boolean isFreeStmtsFromConnectionOnly();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcConnectionInterface
 * JD-Core Version:    0.6.2
 */