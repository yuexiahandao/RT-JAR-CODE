package java.sql;

public abstract interface ResultSetMetaData extends Wrapper
{
  public static final int columnNoNulls = 0;
  public static final int columnNullable = 1;
  public static final int columnNullableUnknown = 2;

  public abstract int getColumnCount()
    throws SQLException;

  public abstract boolean isAutoIncrement(int paramInt)
    throws SQLException;

  public abstract boolean isCaseSensitive(int paramInt)
    throws SQLException;

  public abstract boolean isSearchable(int paramInt)
    throws SQLException;

  public abstract boolean isCurrency(int paramInt)
    throws SQLException;

  public abstract int isNullable(int paramInt)
    throws SQLException;

  public abstract boolean isSigned(int paramInt)
    throws SQLException;

  public abstract int getColumnDisplaySize(int paramInt)
    throws SQLException;

  public abstract String getColumnLabel(int paramInt)
    throws SQLException;

  public abstract String getColumnName(int paramInt)
    throws SQLException;

  public abstract String getSchemaName(int paramInt)
    throws SQLException;

  public abstract int getPrecision(int paramInt)
    throws SQLException;

  public abstract int getScale(int paramInt)
    throws SQLException;

  public abstract String getTableName(int paramInt)
    throws SQLException;

  public abstract String getCatalogName(int paramInt)
    throws SQLException;

  public abstract int getColumnType(int paramInt)
    throws SQLException;

  public abstract String getColumnTypeName(int paramInt)
    throws SQLException;

  public abstract boolean isReadOnly(int paramInt)
    throws SQLException;

  public abstract boolean isWritable(int paramInt)
    throws SQLException;

  public abstract boolean isDefinitelyWritable(int paramInt)
    throws SQLException;

  public abstract String getColumnClassName(int paramInt)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.ResultSetMetaData
 * JD-Core Version:    0.6.2
 */