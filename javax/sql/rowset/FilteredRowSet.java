package javax.sql.rowset;

import java.sql.SQLException;

public abstract interface FilteredRowSet extends WebRowSet
{
  public abstract void setFilter(Predicate paramPredicate)
    throws SQLException;

  public abstract Predicate getFilter();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.FilteredRowSet
 * JD-Core Version:    0.6.2
 */