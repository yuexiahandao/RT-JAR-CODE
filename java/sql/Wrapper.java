package java.sql;

public abstract interface Wrapper
{
  public abstract <T> T unwrap(Class<T> paramClass)
    throws SQLException;

  public abstract boolean isWrapperFor(Class<?> paramClass)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Wrapper
 * JD-Core Version:    0.6.2
 */