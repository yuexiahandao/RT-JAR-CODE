package javax.sql.rowset.spi;

import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.RowSetWriter;

public abstract interface TransactionalWriter extends RowSetWriter
{
  public abstract void commit()
    throws SQLException;

  public abstract void rollback()
    throws SQLException;

  public abstract void rollback(Savepoint paramSavepoint)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.spi.TransactionalWriter
 * JD-Core Version:    0.6.2
 */