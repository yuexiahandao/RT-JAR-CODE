package javax.swing.text;

import javax.swing.Action;
import javax.swing.KeyStroke;

public abstract interface Keymap
{
  public abstract String getName();

  public abstract Action getDefaultAction();

  public abstract void setDefaultAction(Action paramAction);

  public abstract Action getAction(KeyStroke paramKeyStroke);

  public abstract KeyStroke[] getBoundKeyStrokes();

  public abstract Action[] getBoundActions();

  public abstract KeyStroke[] getKeyStrokesForAction(Action paramAction);

  public abstract boolean isLocallyDefined(KeyStroke paramKeyStroke);

  public abstract void addActionForKeyStroke(KeyStroke paramKeyStroke, Action paramAction);

  public abstract void removeKeyStrokeBinding(KeyStroke paramKeyStroke);

  public abstract void removeBindings();

  public abstract Keymap getResolveParent();

  public abstract void setResolveParent(Keymap paramKeymap);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Keymap
 * JD-Core Version:    0.6.2
 */