package java.sql;

public abstract interface SQLData
{
  public abstract String getSQLTypeName()
    throws SQLException;

  public abstract void readSQL(SQLInput paramSQLInput, String paramString)
    throws SQLException;

  public abstract void writeSQL(SQLOutput paramSQLOutput)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLData
 * JD-Core Version:    0.6.2
 */