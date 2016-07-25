package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;

public abstract interface PreparedStatement extends Statement
{
  public abstract ResultSet executeQuery()
    throws SQLException;

  public abstract int executeUpdate()
    throws SQLException;

  public abstract void setNull(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setBoolean(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setByte(int paramInt, byte paramByte)
    throws SQLException;

  public abstract void setShort(int paramInt, short paramShort)
    throws SQLException;

  public abstract void setInt(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setLong(int paramInt, long paramLong)
    throws SQLException;

  public abstract void setFloat(int paramInt, float paramFloat)
    throws SQLException;

  public abstract void setDouble(int paramInt, double paramDouble)
    throws SQLException;

  public abstract void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void setString(int paramInt, String paramString)
    throws SQLException;

  public abstract void setBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void setDate(int paramInt, Date paramDate)
    throws SQLException;

  public abstract void setTime(int paramInt, Time paramTime)
    throws SQLException;

  public abstract void setTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException;

  public abstract void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  /** @deprecated */
  public abstract void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;

  public abstract void clearParameters()
    throws SQLException;

  public abstract void setObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException;

  public abstract void setObject(int paramInt, Object paramObject)
    throws SQLException;

  public abstract boolean execute()
    throws SQLException;

  public abstract void addBatch()
    throws SQLException;

  public abstract void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException;

  public abstract void setRef(int paramInt, Ref paramRef)
    throws SQLException;

  public abstract void setBlob(int paramInt, Blob paramBlob)
    throws SQLException;

  public abstract void setClob(int paramInt, Clob paramClob)
    throws SQLException;

  public abstract void setArray(int paramInt, Array paramArray)
    throws SQLException;

  public abstract ResultSetMetaData getMetaData()
    throws SQLException;

  public abstract void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
    throws SQLException;

  public abstract void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException;

  public abstract void setNull(int paramInt1, int paramInt2, String paramString)
    throws SQLException;

  public abstract void setURL(int paramInt, URL paramURL)
    throws SQLException;

  public abstract ParameterMetaData getParameterMetaData()
    throws SQLException;

  public abstract void setRowId(int paramInt, RowId paramRowId)
    throws SQLException;

  public abstract void setNString(int paramInt, String paramString)
    throws SQLException;

  public abstract void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setNClob(int paramInt, NClob paramNClob)
    throws SQLException;

  public abstract void setClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException;

  public abstract void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
    throws SQLException;

  public abstract void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException;

  public abstract void setCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException;

  public abstract void setAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setClob(int paramInt, Reader paramReader)
    throws SQLException;

  public abstract void setBlob(int paramInt, InputStream paramInputStream)
    throws SQLException;

  public abstract void setNClob(int paramInt, Reader paramReader)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.PreparedStatement
 * JD-Core Version:    0.6.2
 */