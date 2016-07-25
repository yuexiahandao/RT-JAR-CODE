package java.sql;

public abstract interface Savepoint
{
  public abstract int getSavepointId()
    throws SQLException;

  public abstract String getSavepointName()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Savepoint
 * JD-Core Version:    0.6.2
 */