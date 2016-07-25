package javax.sql.rowset;

import java.sql.SQLException;

public abstract interface RowSetFactory
{
  public abstract CachedRowSet createCachedRowSet()
    throws SQLException;

  public abstract FilteredRowSet createFilteredRowSet()
    throws SQLException;

  public abstract JdbcRowSet createJdbcRowSet()
    throws SQLException;

  public abstract JoinRowSet createJoinRowSet()
    throws SQLException;

  public abstract WebRowSet createWebRowSet()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.RowSetFactory
 * JD-Core Version:    0.6.2
 */