package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

public abstract interface CallableStatement extends PreparedStatement
{
  public abstract void registerOutParameter(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void registerOutParameter(int paramInt1, int paramInt2, int paramInt3)
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

  public abstract Object getObject(int paramInt)
    throws SQLException;

  public abstract BigDecimal getBigDecimal(int paramInt)
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

  public abstract Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract Time getTime(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws SQLException;

  public abstract void registerOutParameter(int paramInt1, int paramInt2, String paramString)
    throws SQLException;

  public abstract void registerOutParameter(String paramString, int paramInt)
    throws SQLException;

  public abstract void registerOutParameter(String paramString, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void registerOutParameter(String paramString1, int paramInt, String paramString2)
    throws SQLException;

  public abstract URL getURL(int paramInt)
    throws SQLException;

  public abstract void setURL(String paramString, URL paramURL)
    throws SQLException;

  public abstract void setNull(String paramString, int paramInt)
    throws SQLException;

  public abstract void setBoolean(String paramString, boolean paramBoolean)
    throws SQLException;

  public abstract void setByte(String paramString, byte paramByte)
    throws SQLException;

  public abstract void setShort(String paramString, short paramShort)
    throws SQLException;

  public abstract void setInt(String paramString, int paramInt)
    throws SQLException;

  public abstract void setLong(String paramString, long paramLong)
    throws SQLException;

  public abstract void setFloat(String paramString, float paramFloat)
    throws SQLException;

  public abstract void setDouble(String paramString, double paramDouble)
    throws SQLException;

  public abstract void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void setString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void setBytes(String paramString, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void setDate(String paramString, Date paramDate)
    throws SQLException;

  public abstract void setTime(String paramString, Time paramTime)
    throws SQLException;

  public abstract void setTimestamp(String paramString, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject, int paramInt)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject)
    throws SQLException;

  public abstract void setCharacterStream(String paramString, Reader paramReader, int paramInt)
    throws SQLException;

  public abstract void setDate(String paramString, Date paramDate, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTime(String paramString, Time paramTime, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException;

  public abstract void setNull(String paramString1, int paramInt, String paramString2)
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

  public abstract byte[] getBytes(String paramString)
    throws SQLException;

  public abstract Date getDate(String paramString)
    throws SQLException;

  public abstract Time getTime(String paramString)
    throws SQLException;

  public abstract Timestamp getTimestamp(String paramString)
    throws SQLException;

  public abstract Object getObject(String paramString)
    throws SQLException;

  public abstract BigDecimal getBigDecimal(String paramString)
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

  public abstract Date getDate(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract Time getTime(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract Timestamp getTimestamp(String paramString, Calendar paramCalendar)
    throws SQLException;

  public abstract URL getURL(String paramString)
    throws SQLException;

  public abstract RowId getRowId(int paramInt)
    throws SQLException;

  public abstract RowId getRowId(String paramString)
    throws SQLException;

  public abstract void setRowId(String paramString, RowId paramRowId)
    throws SQLException;

  public abstract void setNString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNClob(String paramString, NClob paramNClob)
    throws SQLException;

  public abstract void setClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract NClob getNClob(int paramInt)
    throws SQLException;

  public abstract NClob getNClob(String paramString)
    throws SQLException;

  public abstract void setSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException;

  public abstract SQLXML getSQLXML(int paramInt)
    throws SQLException;

  public abstract SQLXML getSQLXML(String paramString)
    throws SQLException;

  public abstract String getNString(int paramInt)
    throws SQLException;

  public abstract String getNString(String paramString)
    throws SQLException;

  public abstract Reader getNCharacterStream(int paramInt)
    throws SQLException;

  public abstract Reader getNCharacterStream(String paramString)
    throws SQLException;

  public abstract Reader getCharacterStream(int paramInt)
    throws SQLException;

  public abstract Reader getCharacterStream(String paramString)
    throws SQLException;

  public abstract void setBlob(String paramString, Blob paramBlob)
    throws SQLException;

  public abstract void setClob(String paramString, Clob paramClob)
    throws SQLException;

  public abstract void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setNCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setBlob(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setNClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract <T> T getObject(int paramInt, Class<T> paramClass)
    throws SQLException;

  public abstract <T> T getObject(String paramString, Class<T> paramClass)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.CallableStatement
 * JD-Core Version:    0.6.2
 */