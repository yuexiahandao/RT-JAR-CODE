package sun.jdbc.odbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

public abstract interface JdbcOdbcResultSetInterface extends ResultSet
{
  public abstract JdbcOdbcPseudoCol getPseudoCol(int paramInt);

  public abstract int mapColumn(int paramInt);

  public abstract void clearWarnings()
    throws SQLException;

  public abstract long getHSTMT();

  public abstract int getColumnCount()
    throws SQLException;

  public abstract int getScale(int paramInt)
    throws SQLException;

  public abstract int getColumnType(int paramInt)
    throws SQLException;

  public abstract int getColAttribute(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setWarning(SQLWarning paramSQLWarning)
    throws SQLException;

  public abstract String mapColumnName(String paramString, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcResultSetInterface
 * JD-Core Version:    0.6.2
 */