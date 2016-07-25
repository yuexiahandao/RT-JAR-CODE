package java.sql;

public abstract interface Statement extends Wrapper, AutoCloseable
{
  public static final int CLOSE_CURRENT_RESULT = 1;
  public static final int KEEP_CURRENT_RESULT = 2;
  public static final int CLOSE_ALL_RESULTS = 3;
  public static final int SUCCESS_NO_INFO = -2;
  public static final int EXECUTE_FAILED = -3;
  public static final int RETURN_GENERATED_KEYS = 1;
  public static final int NO_GENERATED_KEYS = 2;

  public abstract ResultSet executeQuery(String paramString)
    throws SQLException;

  public abstract int executeUpdate(String paramString)
    throws SQLException;

  public abstract void close()
    throws SQLException;

  public abstract int getMaxFieldSize()
    throws SQLException;

  public abstract void setMaxFieldSize(int paramInt)
    throws SQLException;

  public abstract int getMaxRows()
    throws SQLException;

  public abstract void setMaxRows(int paramInt)
    throws SQLException;

  public abstract void setEscapeProcessing(boolean paramBoolean)
    throws SQLException;

  public abstract int getQueryTimeout()
    throws SQLException;

  public abstract void setQueryTimeout(int paramInt)
    throws SQLException;

  public abstract void cancel()
    throws SQLException;

  public abstract SQLWarning getWarnings()
    throws SQLException;

  public abstract void clearWarnings()
    throws SQLException;

  public abstract void setCursorName(String paramString)
    throws SQLException;

  public abstract boolean execute(String paramString)
    throws SQLException;

  public abstract ResultSet getResultSet()
    throws SQLException;

  public abstract int getUpdateCount()
    throws SQLException;

  public abstract boolean getMoreResults()
    throws SQLException;

  public abstract void setFetchDirection(int paramInt)
    throws SQLException;

  public abstract int getFetchDirection()
    throws SQLException;

  public abstract void setFetchSize(int paramInt)
    throws SQLException;

  public abstract int getFetchSize()
    throws SQLException;

  public abstract int getResultSetConcurrency()
    throws SQLException;

  public abstract int getResultSetType()
    throws SQLException;

  public abstract void addBatch(String paramString)
    throws SQLException;

  public abstract void clearBatch()
    throws SQLException;

  public abstract int[] executeBatch()
    throws SQLException;

  public abstract Connection getConnection()
    throws SQLException;

  public abstract boolean getMoreResults(int paramInt)
    throws SQLException;

  public abstract ResultSet getGeneratedKeys()
    throws SQLException;

  public abstract int executeUpdate(String paramString, int paramInt)
    throws SQLException;

  public abstract int executeUpdate(String paramString, int[] paramArrayOfInt)
    throws SQLException;

  public abstract int executeUpdate(String paramString, String[] paramArrayOfString)
    throws SQLException;

  public abstract boolean execute(String paramString, int paramInt)
    throws SQLException;

  public abstract boolean execute(String paramString, int[] paramArrayOfInt)
    throws SQLException;

  public abstract boolean execute(String paramString, String[] paramArrayOfString)
    throws SQLException;

  public abstract int getResultSetHoldability()
    throws SQLException;

  public abstract boolean isClosed()
    throws SQLException;

  public abstract void setPoolable(boolean paramBoolean)
    throws SQLException;

  public abstract boolean isPoolable()
    throws SQLException;

  public abstract void closeOnCompletion()
    throws SQLException;

  public abstract boolean isCloseOnCompletion()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Statement
 * JD-Core Version:    0.6.2
 */