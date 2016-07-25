package java.sql;

import java.util.Map;

public abstract interface Struct
{
  public abstract String getSQLTypeName()
    throws SQLException;

  public abstract Object[] getAttributes()
    throws SQLException;

  public abstract Object[] getAttributes(Map<String, Class<?>> paramMap)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Struct
 * JD-Core Version:    0.6.2
 */