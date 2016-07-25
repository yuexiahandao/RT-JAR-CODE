package javax.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public abstract interface RowSet extends ResultSet
{
  public abstract String getUrl()
    throws SQLException;

  public abstract void setUrl(String paramString)
    throws SQLException;

  public abstract String getDataSourceName();

  public abstract void setDataSourceName(String paramString)
    throws SQLException;

  public abstract String getUsername();

  public abstract void setUsername(String paramString)
    throws SQLException;

  public abstract String getPassword();

  public abstract void setPassword(String paramString)
    throws SQLException;

  public abstract int getTransactionIsolation();

  public abstract void setTransactionIsolation(int paramInt)
    throws SQLException;

  public abstract Map<String, Class<?>> getTypeMap()
    throws SQLException;

  public abstract void setTypeMap(Map<String, Class<?>> paramMap)
    throws SQLException;

  public abstract String getCommand();

  public abstract void setCommand(String paramString)
    throws SQLException;

  public abstract boolean isReadOnly();

  public abstract void setReadOnly(boolean paramBoolean)
    throws SQLException;

  public abstract int getMaxFieldSize()
    throws SQLException;

  public abstract void setMaxFieldSize(int paramInt)
    throws SQLException;

  public abstract int getMaxRows()
    throws SQLException;

  public abstract void setMaxRows(int paramInt)
    throws SQLException;

  public abstract boolean getEscapeProcessing()
    throws SQLException;

  public abstract void setEscapeProcessing(boolean paramBoolean)
    throws SQLException;

  public abstract int getQueryTimeout()
    throws SQLException;

  public abstract void setQueryTimeout(int paramInt)
    throws SQLException;

  public abstract void setType(int paramInt)
    throws SQLException;

  public abstract void setConcurrency(int paramInt)
    throws SQLException;

  public abstract void setNull(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setNull(String paramString, int paramInt)
    throws SQLException;

  public abstract void setNull(int paramInt1, int paramInt2, String paramString)
    throws SQLException;

  public abstract void setNull(String paramString1, int paramInt, String paramString2)
    throws SQLException;

  public abstract void setBoolean(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setBoolean(String paramString, boolean paramBoolean)
    throws SQLException;

  public abstract void setByte(int paramInt, byte paramByte)
    throws SQLException;

  public abstract void setByte(String paramString, byte paramByte)
    throws SQLException;

  public abstract void setShort(int paramInt, short paramShort)
    throws SQLException;

  public abstract void setShort(String paramString, short paramShort)
    throws SQLException;

  public abstract void setInt(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setInt(String paramString, int paramInt)
    throws SQLException;

  public abstract void setLong(int paramInt, long paramLong)
    throws SQLException;

  public abstract void setLong(String paramString, long paramLong)
    throws SQLException;

  public abstract void setFloat(int paramInt, float paramFloat)
    throws SQLException;

  public abstract void setFloat(String paramString, float paramFloat)
    throws SQLException;

  public abstract void setDouble(int paramInt, double paramDouble)
    throws SQLException;

  public abstract void setDouble(String paramString, double paramDouble)
    throws SQLException;

  public abstract void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void setString(int paramInt, String paramString)
    throws SQLException;

  public abstract void setString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void setBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void setBytes(String paramString, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void setDate(int paramInt, Date paramDate)
    throws SQLException;

  public abstract void setTime(int paramInt, Time paramTime)
    throws SQLException;

  public abstract void setTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void setTimestamp(String paramString, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException;

  public abstract void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException;

  public abstract void setCharacterStream(String paramString, Reader paramReader, int paramInt)
    throws SQLException;

  public abstract void setAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject, int paramInt)
    throws SQLException;

  public abstract void setObject(String paramString, Object paramObject)
    throws SQLException;

  public abstract void setObject(int paramInt, Object paramObject)
    throws SQLException;

  public abstract void setRef(int paramInt, Ref paramRef)
    throws SQLException;

  public abstract void setBlob(int paramInt, Blob paramBlob)
    throws SQLException;

  public abstract void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setBlob(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setBlob(String paramString, Blob paramBlob)
    throws SQLException;

  public abstract void setBlob(String paramString, InputStream paramInputStream)
    throws SQLException;

  public abstract void setClob(int paramInt, Clob paramClob)
    throws SQLException;

  public abstract void setClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setClob(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setClob(String paramString, Clob paramClob)
    throws SQLException;

  public abstract void setClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setArray(int paramInt, Array paramArray)
    throws SQLException;

  public abstract void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
    throws SQLException;

  public abstract void setDate(String paramString, Date paramDate)
    throws SQLException;

  public abstract void setDate(String paramString, Date paramDate, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTime(String paramString, Time paramTime)
    throws SQLException;

  public abstract void setTime(String paramString, Time paramTime, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException;

  public abstract void clearParameters()
    throws SQLException;

  public abstract void execute()
    throws SQLException;

  public abstract void addRowSetListener(RowSetListener paramRowSetListener);

  public abstract void removeRowSetListener(RowSetListener paramRowSetListener);

  public abstract void setSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException;

  public abstract void setSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException;

  public abstract void setRowId(int paramInt, RowId paramRowId)
    throws SQLException;

  public abstract void setRowId(String paramString, RowId paramRowId)
    throws SQLException;

  public abstract void setNString(int paramInt, String paramString)
    throws SQLException;

  public abstract void setNString(String paramString1, String paramString2)
    throws SQLException;

  public abstract void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNCharacterStream(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setNClob(String paramString, NClob paramNClob)
    throws SQLException;

  public abstract void setNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNClob(String paramString, Reader paramReader)
    throws SQLException;

  public abstract void setNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNClob(int paramInt, NClob paramNClob)
    throws SQLException;

  public abstract void setNClob(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setURL(int paramInt, URL paramURL)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.RowSet
 * JD-Core Version:    0.6.2
 */