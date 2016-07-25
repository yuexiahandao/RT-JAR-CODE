package javax.sql.rowset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;

public abstract interface CachedRowSet extends RowSet, Joinable
{

  @Deprecated
  public static final boolean COMMIT_ON_ACCEPT_CHANGES = true;

  public abstract void populate(ResultSet paramResultSet)
    throws SQLException;

  public abstract void execute(Connection paramConnection)
    throws SQLException;

  public abstract void acceptChanges()
    throws SyncProviderException;

  public abstract void acceptChanges(Connection paramConnection)
    throws SyncProviderException;

  public abstract void restoreOriginal()
    throws SQLException;

  public abstract void release()
    throws SQLException;

  public abstract void undoDelete()
    throws SQLException;

  public abstract void undoInsert()
    throws SQLException;

  public abstract void undoUpdate()
    throws SQLException;

  public abstract boolean columnUpdated(int paramInt)
    throws SQLException;

  public abstract boolean columnUpdated(String paramString)
    throws SQLException;

  public abstract Collection<?> toCollection()
    throws SQLException;

  public abstract Collection<?> toCollection(int paramInt)
    throws SQLException;

  public abstract Collection<?> toCollection(String paramString)
    throws SQLException;

  public abstract SyncProvider getSyncProvider()
    throws SQLException;

  public abstract void setSyncProvider(String paramString)
    throws SQLException;

  public abstract int size();

  public abstract void setMetaData(RowSetMetaData paramRowSetMetaData)
    throws SQLException;

  public abstract ResultSet getOriginal()
    throws SQLException;

  public abstract ResultSet getOriginalRow()
    throws SQLException;

  public abstract void setOriginalRow()
    throws SQLException;

  public abstract String getTableName()
    throws SQLException;

  public abstract void setTableName(String paramString)
    throws SQLException;

  public abstract int[] getKeyColumns()
    throws SQLException;

  public abstract void setKeyColumns(int[] paramArrayOfInt)
    throws SQLException;

  public abstract RowSet createShared()
    throws SQLException;

  public abstract CachedRowSet createCopy()
    throws SQLException;

  public abstract CachedRowSet createCopySchema()
    throws SQLException;

  public abstract CachedRowSet createCopyNoConstraints()
    throws SQLException;

  public abstract RowSetWarning getRowSetWarnings()
    throws SQLException;

  public abstract boolean getShowDeleted()
    throws SQLException;

  public abstract void setShowDeleted(boolean paramBoolean)
    throws SQLException;

  public abstract void commit()
    throws SQLException;

  public abstract void rollback()
    throws SQLException;

  public abstract void rollback(Savepoint paramSavepoint)
    throws SQLException;

  public abstract void rowSetPopulated(RowSetEvent paramRowSetEvent, int paramInt)
    throws SQLException;

  public abstract void populate(ResultSet paramResultSet, int paramInt)
    throws SQLException;

  public abstract void setPageSize(int paramInt)
    throws SQLException;

  public abstract int getPageSize();

  public abstract boolean nextPage()
    throws SQLException;

  public abstract boolean previousPage()
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.CachedRowSet
 * JD-Core Version:    0.6.2
 */