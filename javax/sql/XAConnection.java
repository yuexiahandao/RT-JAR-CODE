package javax.sql;

import java.sql.SQLException;
import javax.transaction.xa.XAResource;

public abstract interface XAConnection extends PooledConnection
{
  public abstract XAResource getXAResource()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.XAConnection
 * JD-Core Version:    0.6.2
 */