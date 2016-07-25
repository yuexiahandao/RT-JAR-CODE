package javax.sql;

import java.util.EventListener;

public abstract interface RowSetListener extends EventListener
{
  public abstract void rowSetChanged(RowSetEvent paramRowSetEvent);

  public abstract void rowChanged(RowSetEvent paramRowSetEvent);

  public abstract void cursorMoved(RowSetEvent paramRowSetEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.RowSetListener
 * JD-Core Version:    0.6.2
 */