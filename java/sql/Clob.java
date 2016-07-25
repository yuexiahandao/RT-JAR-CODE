package java.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public abstract interface Clob
{
  public abstract long length()
    throws SQLException;

  public abstract String getSubString(long paramLong, int paramInt)
    throws SQLException;

  public abstract Reader getCharacterStream()
    throws SQLException;

  public abstract InputStream getAsciiStream()
    throws SQLException;

  public abstract long position(String paramString, long paramLong)
    throws SQLException;

  public abstract long position(Clob paramClob, long paramLong)
    throws SQLException;

  public abstract int setString(long paramLong, String paramString)
    throws SQLException;

  public abstract int setString(long paramLong, String paramString, int paramInt1, int paramInt2)
    throws SQLException;

  public abstract OutputStream setAsciiStream(long paramLong)
    throws SQLException;

  public abstract Writer setCharacterStream(long paramLong)
    throws SQLException;

  public abstract void truncate(long paramLong)
    throws SQLException;

  public abstract void free()
    throws SQLException;

  public abstract Reader getCharacterStream(long paramLong1, long paramLong2)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Clob
 * JD-Core Version:    0.6.2
 */