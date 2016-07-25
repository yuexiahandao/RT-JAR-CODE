package javax.sql.rowset;

import java.sql.SQLException;
import javax.sql.RowSet;

public abstract interface Predicate
{
  public abstract boolean evaluate(RowSet paramRowSet);

  public abstract boolean evaluate(Object paramObject, int paramInt)
    throws SQLException;

  public abstract boolean evaluate(Object paramObject, String paramString)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.Predicate
 * JD-Core Version:    0.6.2
 */