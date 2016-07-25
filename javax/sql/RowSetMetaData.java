package javax.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract interface RowSetMetaData extends ResultSetMetaData
{
  public abstract void setColumnCount(int paramInt)
    throws SQLException;

  public abstract void setAutoIncrement(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setCaseSensitive(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setSearchable(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setCurrency(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setNullable(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setSigned(int paramInt, boolean paramBoolean)
    throws SQLException;

  public abstract void setColumnDisplaySize(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setColumnLabel(int paramInt, String paramString)
    throws SQLException;

  public abstract void setColumnName(int paramInt, String paramString)
    throws SQLException;

  public abstract void setSchemaName(int paramInt, String paramString)
    throws SQLException;

  public abstract void setPrecision(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setScale(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setTableName(int paramInt, String paramString)
    throws SQLException;

  public abstract void setCatalogName(int paramInt, String paramString)
    throws SQLException;

  public abstract void setColumnType(int paramInt1, int paramInt2)
    throws SQLException;

  public abstract void setColumnTypeName(int paramInt, String paramString)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.RowSetMetaData
 * JD-Core Version:    0.6.2
 */