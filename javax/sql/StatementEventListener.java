package javax.sql;

import java.util.EventListener;

public abstract interface StatementEventListener extends EventListener
{
  public abstract void statementClosed(StatementEvent paramStatementEvent);

  public abstract void statementErrorOccurred(StatementEvent paramStatementEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.StatementEventListener
 * JD-Core Version:    0.6.2
 */