package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

public abstract interface ResultSet extends Wrapper, AutoCloseable
{
  public static final int FETCH_FORWARD = 1000;
  public static final int FETCH_REVERSE = 1001;
  public static final int FETCH_UNKNOWN = 1002;
  public static final int TYPE_FORWARD_ONLY = 1003;
  public static final int TYPE_SCROLL_INSENSITIVE = 1004;
  public static final int TYPE_SCROLL_SENSITIVE = 1005;
  public static final int CONCUR_READ_ONLY = 1007;
  public static final int CONCUR_UPDATABLE = 1008;
  public static final int HOLD_CURSORS_OVER_COMMIT = 1;
  public static final int CLOSE_CURSORS_AT_COMMIT = 2;

  public abstract boolean next()
    throws SQLException;

  public abstract void close()
    throws SQLException;

  public abstract boolean wasNull()
    throws SQLException;

  public abstract String getString(int paramInt)
    throws SQLException;

  public abstract boolean getBoolean(int paramInt)
    throws SQLException;

  public abstract byte getByte(int paramInt)
    throws SQLException;

  public abstract short getShort(int paramInt)
    throws SQLException;

  public abstract int getInt(int paramInt)
    throws SQLException;

  public abstract long getLong(int paramInt)
    throws SQLException;

  public abstract float getFloat(int paramInt)
    throws SQLException;

  public abstract double getDouble(int paramInt)
    throws SQLException;

  /** @deprecated */
  public abstract BigDecimal getBigDecimal(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract byte[] getBytes(int paramInt)
    throws SQLException;

  public abstract Date getDate(int paramInt)
    throws SQLException;

  public abstract Time getTime(int paramInt)
    throws SQLException;

  public abstract Timestamp getTimestamp(int paramInt)
    throws SQLException;

  public abstract InputStream getAsciiStream(int paramInt)
    throws SQLException;

  /** @deprecated */
  public abstract InputStream getUnicodeStream(int paramInt)
    throws SQLException;

  public abstract InputStream getBinaryStream(int paramInt)
    throws SQLException;

  public abstract String getString(String paramString)
    throws SQLException;

  public abstract boolean getBoolean(String paramString)
    throws SQLException;

  public abstract byte getByte(String paramString)
    throws SQLException;

  public abstract short getShort(String paramString)
    throws SQLException;

  public abstract int getInt(String paramString)
    throws SQLException;

  public abstract long getLong(String paramString)
    throws SQLException;

  public abstract float getFloat(String paramString)
    throws SQLException;

  public abstract double getDouble(String paramString)
    throws SQLException;

  /** @deprecated */
  public abstract BigDecimal getBigDecimal(String paramString, int paramInt)
    throws SQLException;

  public abstract byte[] getBytes(String paramString)
    throws SQLException;

  public abstract Date getDate(String paramString)
    throws SQLException;

  public abstract Time getTime(String paramString)
    throws SQLException;

  public abstract Timestamp getTimestamp(String paramString)
    throws SQLException;

  public abstract InputStream getAsciiStream(String paramString)
    throws SQLException;

  /** @deprecated */
  public abstract InputStream getUnicodeStream(String paramString)
    throws SQLException;

  public abstract InputStream getBinaryStream(String paramString)
    throws SQLException;

  public abstract SQLWarning getWarnings()
    throws SQLException;

  public abstract void clearWarnings()
    throws SQLException;

  public abstract String getCursorName()
    throws SQLException;

  public abstract ResultSetMetaData getMetaData()
    throws SQLException;

  public abstract Object getObject(int paramInt)
    throws SQLException;

  public abstract Object getObject(String paramString)
    throws SQLException;

  public abstract int findColumn(String paramString)
    throws SQLException;

  public abstract Reader getCharacterStream(int paramInt)
    throws SQLException;

  public abstract Reader getCharacterStream(String paramString)
    throws SQLException;

  public abstract BigDecimal getBigDecimal(int paramInt)
    throws SQLException;

  public abstract BigDecimal getBigDecimal(String paramString)
    throws SQLException;

  public abstract boolean isBeforeFirst()
    throws SQLException;

  public abstract boolean isAfterLast()
    throws SQLException;

  public abstract boolean isFirst()
    throws SQLException;

  public abstract boolean isLast()
    throws SQLException;

  public abstract void beforeFirst()
    throws SQLException;

  public abstract void afterLast()
    throws SQLException;

  public abstract boolean first()
    throws SQLException;

  public abstract boolean last()
    throws SQLException;

  public abstract int getRow()
    throws SQLException;

  public abstract boolean absolute(int paramInt)
    throws SQLException;

  public abstract boolean relative(int paramInt)
    throws SQLException;

  public abstract boolean previous()
    throws SQLException;

  public abstract void setFetchDirection(int paramInt)
    throws SQLException;

  public abstract int getFetchDirection()
    throws SQLException;

  public abstract void setFetchSize(int paramInt)
    throws SQLException;

  public abstract int getFetchSize()
    throws SQLException;

  public abstract int getType()
    throws SQLException;

  public abstract int getConcurrency()
    throws SQLException;

  public abstract boolean rowUpdated()
    throws SQLException;

  public abstract boolean rowInserted()
    throws SQLException;

  public abstract boolean rowDeleted()
    throws SQLException;

  public abstract void updateNull(int paramInt)
    throws SQLException;

  public abstract void updateBoolean(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void updateByte(int paramInt, byte paramByte)
    throws SQLException;

  public abstract void updateShort(int paramInt, short paramShort)
    throws SQLException;

  public abstract void updateInt(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void updateLong(int paramInt, long paramLong)
    throws SQLException;

  public abstract void updateFloat(int paramInt, float paramFloat)
    throws SQLException;

  public abstract void updateDouble(int paramInt, double paramDouble)
    throws SQLException;

  public abstract void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void updateString(int paramInt, String paramString)
    throws SQLException;

  public abstract void updateBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void updateDate(int paramInt, Date paramDate)
    throws SQLException;

  public abstract void updateTime(int paramInt, Time paramTime)
    throws SQLException;

  public abstract void updateTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException;

  public abstract void updateObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException;

  public abstract void updateObject(int paramInt, Object paramObject)
    throws SQLException;

  public abstract void updateNull(String paramString)
    throws SQLException;

  public abstract void updateBoolean(String paramString, boolean paramBoolean)
    throws SQLException;

  public abstract void updateByte(String paramString, byte paramByte)
    throws SQLException;

  public abstract void updateShort(String paramString, short paramShort)
    throws SQLException;

  public abstract void updateInt(String paramString, int paramInt)
    throws SQLException;

  public abstract void updateLong(String paramString, long paramLong)
    throws SQLException;

  public abstract void updateFloat(String paramString, float paramFloat)
    throws SQLException;

  public abstract void updateDouble(String paramString, double paramDouble)
    throws SQLException;

  public abstract void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void updateString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void updateBytes(String paramString, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void updateDate(String paramString, Date paramDate)
    throws SQLException;

  public abstract void updateTime(String paramString, Time paramTime)
    throws SQLException;

  public abstract void updateTimestamp(String paramString, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
    throws SQLException;

  public abstract void updateObject(String paramString, Object paramObject, int paramInt)
    throws SQLException;

  public abstract void updateObject(String paramString, Object paramObject)
    throws SQLException;

  public abstract void insertRow()
    throws SQLException;

  public abstract void updateRow()
    throws SQLException;

  public abstract void deleteRow()
    throws SQLException;

  public abstract void refreshRow()
    throws SQLException;

  public abstract void cancelRowUpdates()
    throws SQLException;

  public abstract void moveToInsertRow()
    throws SQLException;

  public abstract void moveToCurrentRow()
    throws SQLException;

  public abstract Statement getStatement()
    throws SQLException;

  public abstract Object getObject(int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException;

  public abstract Ref getRef(int paramInt)
    throws SQLException;

  public abstract Blob getBlob(int paramInt)
    throws SQLException;

  public abstract Clob getClob(int paramInt)
    throws SQLException;

  public abstract Array getArray(int paramInt)
    throws SQLException;

  public abstract Object getObject(String paramString, Map<String, Class<?>> paramMap)
    throws SQLException;

  public abstract Ref getRef(String paramString)
    throws SQLException;

  public abstract Blob getBlob(String paramString)
    throws SQLException;

  public abstract Clob getClob(String paramString)
    throws SQLException;

  public abstract Array getArray(String paramString)
    throws SQLException;

  public abstract Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract Date getDate(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract Time getTime(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract Time getTime(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract Timestamp getTimestamp(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract URL getURL(int paramInt)
    throws SQLException;

  public abstract URL getURL(String paramString)
    throws SQLException;

  public abstract void updateRef(int paramInt, Ref paramRef)
    throws SQLException;

  public abstract void updateRef(String paramString, Ref paramRef)
    throws SQLException;

  public abstract void updateBlob(int paramInt, Blob paramBlob)
    throws SQLException;

  public abstract void updateBlob(String paramString, Blob paramBlob)
    throws SQLException;

  public abstract void updateClob(int paramInt, Clob paramClob)
    throws SQLException;

  public abstract void updateClob(String paramString, Clob paramClob)
    throws SQLException;

  public abstract void updateArray(int paramInt, Array paramArray)
    throws SQLException;

  public abstract void updateArray(String paramString, Array paramArray)
    throws SQLException;

  public abstract RowId getRowId(int paramInt)
    throws SQLException;

  public abstract RowId getRowId(String paramString)
    throws SQLException;

  public abstract void updateRowId(int paramInt, RowId paramRowId)
    throws SQLException;

  public abstract void updateRowId(String paramString, RowId paramRowId)
    throws SQLException;

  public abstract int getHoldability()
    throws SQLException;

  public abstract boolean isClosed()
    throws SQLException;

  public abstract void updateNString(int paramInt, String paramString)
    throws SQLException;

  public abstract void updateNString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void updateNClob(int paramInt, NClob paramNClob)
    throws SQLException;

  public abstract void updateNClob(String paramString, NClob paramNClob)
    throws SQLException;

  public abstract NClob getNClob(int paramInt)
    throws SQLException;

  public abstract NClob getNClob(String paramString)
    throws SQLException;

  public abstract SQLXML getSQLXML(int paramInt)
    throws SQLException;

  public abstract SQLXML getSQLXML(String paramString)
    throws SQLException;

  public abstract void updateSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException;

  public abstract void updateSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException;

  public abstract String getNString(int paramInt)
    throws SQLException;

  public abstract String getNString(String paramString)
    throws SQLException;

  public abstract Reader getNCharacterStream(int paramInt)
    throws SQLException;

  public abstract Reader getNCharacterStream(String paramString)
    throws SQLException;

  public abstract void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void updateClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void updateNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void updateNCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void updateAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void updateAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void updateBlob(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateBlob(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void updateClob(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void updateClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void updateNClob(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void updateNClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract <T> T getObject(int paramInt, Class<T> paramClass)
    throws SQLException;

  public abstract <T> T getObject(String paramString, Class<T> paramClass)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.ResultSet
 * JD-Core Version:    0.6.2
 */