package javax.sql.rowset;

import java.sql.SQLException;

public abstract interface Joinable
{
  public abstract void setMatchColumn(int paramInt)
    throws SQLException;

  public abstract void setMatchColumn(int[] paramArrayOfInt)
    throws SQLException;

  public abstract void setMatchColumn(String paramString)
    throws SQLException;

  public abstract void setMatchColumn(String[] paramArrayOfString)
    throws SQLException;

  public abstract int[] getMatchColumnIndexes()
    throws SQLException;

  public abstract String[] getMatchColumnNames()
    throws SQLException;

  public abstract void unsetMatchColumn(int paramInt)
    throws SQLException;

  public abstract void unsetMatchColumn(int[] paramArrayOfInt)
    throws SQLException;

  public abstract void unsetMatchColumn(String paramString)
    throws SQLException;

  public abstract void unsetMatchColumn(String[] paramArrayOfString)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.Joinable
 * JD-Core Version:    0.6.2
 */