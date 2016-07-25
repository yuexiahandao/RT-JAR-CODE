package javax.swing.undo;

import java.util.Hashtable;

public abstract interface StateEditable
{
  public static final String RCSID = "$Id: StateEditable.java,v 1.2 1997/09/08 19:39:08 marklin Exp $";

  public abstract void storeState(Hashtable<Object, Object> paramHashtable);

  public abstract void restoreState(Hashtable<?, ?> paramHashtable);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.undo.StateEditable
 * JD-Core Version:    0.6.2
 */