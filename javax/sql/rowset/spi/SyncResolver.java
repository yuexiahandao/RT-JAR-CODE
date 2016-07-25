package javax.sql.rowset.spi;

import java.sql.SQLException;
import javax.sql.RowSet;

public abstract interface SyncResolver extends RowSet
{
  public static final int UPDATE_ROW_CONFLICT = 0;
  public static final int DELETE_ROW_CONFLICT = 1;
  public static final int INSERT_ROW_CONFLICT = 2;
  public static final int NO_ROW_CONFLICT = 3;

  public abstract int getStatus();

  public abstract Object getConflictValue(int paramInt)
    throws SQLException;

  public abstract Object getConflictValue(String paramString)
    throws SQLException;

  public abstract void setResolvedValue(int paramInt, Object paramObject)
    throws SQLException;

  public abstract void setResolvedValue(String paramString, Object paramObject)
    throws SQLException;

  public abstract boolean nextConflict()
    throws SQLException;

  public abstract boolean previousConflict()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.spi.SyncResolver
 * JD-Core Version:    0.6.2
 */