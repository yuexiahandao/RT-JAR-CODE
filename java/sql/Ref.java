package java.sql;

import java.util.Map;

public abstract interface Ref
{
  public abstract String getBaseTypeName()
    throws SQLException;

  public abstract Object getObject(Map<String, Class<?>> paramMap)
    throws SQLException;

  public abstract Object getObject()
    throws SQLException;

  public abstract void setObject(Object paramObject)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Ref
 * JD-Core Version:    0.6.2
 */