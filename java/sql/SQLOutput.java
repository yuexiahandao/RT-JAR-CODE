package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;

public abstract interface SQLOutput
{
  public abstract void writeString(String paramString)
    throws SQLException;

  public abstract void writeBoolean(boolean paramBoolean)
    throws SQLException;

  public abstract void writeByte(byte paramByte)
    throws SQLException;

  public abstract void writeShort(short paramShort)
    throws SQLException;

  public abstract void writeInt(int paramInt)
    throws SQLException;

  public abstract void writeLong(long paramLong)
    throws SQLException;

  public abstract void writeFloat(float paramFloat)
    throws SQLException;

  public abstract void writeDouble(double paramDouble)
    throws SQLException;

  public abstract void writeBigDecimal(BigDecimal paramBigDecimal)
    throws SQLException;

  public abstract void writeBytes(byte[] paramArrayOfByte)
    throws SQLException;

  public abstract void writeDate(Date paramDate)
    throws SQLException;

  public abstract void writeTime(Time paramTime)
    throws SQLException;

  public abstract void writeTimestamp(Timestamp paramTimestamp)
    throws SQLException;

  public abstract void writeCharacterStream(Reader paramReader)
    throws SQLException;

  public abstract void writeAsciiStream(InputStream paramInputStream)
    throws SQLException;

  public abstract void writeBinaryStream(InputStream paramInputStream)
    throws SQLException;

  public abstract void writeObject(SQLData paramSQLData)
    throws SQLException;

  public abstract void writeRef(Ref paramRef)
    throws SQLException;

  public abstract void writeBlob(Blob paramBlob)
    throws SQLException;

  public abstract void writeClob(Clob paramClob)
    throws SQLException;

  public abstract void writeStruct(Struct paramStruct)
    throws SQLException;

  public abstract void writeArray(Array paramArray)
    throws SQLException;

  public abstract void writeURL(URL paramURL)
    throws SQLException;

  public abstract void writeNString(String paramString)
    throws SQLException;

  public abstract void writeNClob(NClob paramNClob)
    throws SQLException;

  public abstract void writeRowId(RowId paramRowId)
    throws SQLException;

  public abstract void writeSQLXML(SQLXML paramSQLXML)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLOutput
 * JD-Core Version:    0.6.2
 */