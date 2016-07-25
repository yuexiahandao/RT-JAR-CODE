package java.sql;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract interface Connection extends Wrapper, AutoCloseable
{
  public static final int TRANSACTION_NONE = 0;
  public static final int TRANSACTION_READ_UNCOMMITTED = 1;
  public static final int TRANSACTION_READ_COMMITTED = 2;
  public static final int TRANSACTION_REPEATABLE_READ = 4;
  public static final int TRANSACTION_SERIALIZABLE = 8;

  public abstract Statement createStatement()
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString)
    throws SQLException;

  public abstract CallableStatement prepareCall(String paramString)
    throws SQLException;

  public abstract String nativeSQL(String paramString)
    throws SQLException;

  public abstract void setAutoCommit(boolean paramBoolean)
    throws SQLException;

  public abstract boolean getAutoCommit()
    throws SQLException;

  public abstract void commit()
    throws SQLException;

  public abstract void rollback()
    throws SQLException;

  public abstract void close()
    throws SQLException;

  public abstract boolean isClosed()
    throws SQLException;

  public abstract DatabaseMetaData getMetaData()
    throws SQLException;

  public abstract void setReadOnly(boolean paramBoolean)
    throws SQLException;

  public abstract boolean isReadOnly()
    throws SQLException;

  public abstract void setCatalog(String paramString)
    throws SQLException;

  public abstract String getCatalog()
    throws SQLException;

  public abstract void setTransactionIsolation(int paramInt)
    throws SQLException;

  public abstract int getTransactionIsolation()
    throws SQLException;

  public abstract SQLWarning getWarnings()
    throws SQLException;

  public abstract void clearWarnings()
    throws SQLException;

  public abstract Statement createStatement(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract Map<String, Class<?>> getTypeMap()
    throws SQLException;

  public abstract void setTypeMap(Map<String, Class<?>> paramMap)
    throws SQLException;

  public abstract void setHoldability(int paramInt)
    throws SQLException;

  public abstract int getHoldability()
    throws SQLException;

  public abstract Savepoint setSavepoint()
    throws SQLException;

  public abstract Savepoint setSavepoint(String paramString)
    throws SQLException;

  public abstract void rollback(Savepoint paramSavepoint)
    throws SQLException;

  public abstract void releaseSavepoint(Savepoint paramSavepoint)
    throws SQLException;

  public abstract Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException;

  public abstract CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString, int paramInt)
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString, int[] paramArrayOfInt)
    throws SQLException;

  public abstract PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString)
    throws SQLException;

  public abstract Clob createClob()
    throws SQLException;

  public abstract Blob createBlob()
    throws SQLException;

  public abstract NClob createNClob()
    throws SQLException;

  public abstract SQLXML createSQLXML()
    throws SQLException;

  public abstract boolean isValid(int paramInt)
    throws SQLException;

  public abstract void setClientInfo(String paramString1, String paramString2)
    throws SQLClientInfoException;

  public abstract void setClientInfo(Properties paramProperties)
    throws SQLClientInfoException;

  public abstract String getClientInfo(String paramString)
    throws SQLException;

  public abstract Properties getClientInfo()
    throws SQLException;

  public abstract Array createArrayOf(String paramString, Object[] paramArrayOfObject)
    throws SQLException;

  public abstract Struct createStruct(String paramString, Object[] paramArrayOfObject)
    throws SQLException;

  public abstract void setSchema(String paramString)
    throws SQLException;

  public abstract String getSchema()
    throws SQLException;

  public abstract void abort(Executor paramExecutor)
    throws SQLException;

  public abstract void setNetworkTimeout(Executor paramExecutor, int paramInt)
    throws SQLException;

  public abstract int getNetworkTimeout()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Connection
 * JD-Core Version:    0.6.2
 */