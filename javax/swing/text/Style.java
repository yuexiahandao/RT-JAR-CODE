package javax.swing.text;

import javax.swing.event.ChangeListener;

public abstract interface Style extends MutableAttributeSet
{
  public abstract String getName();

  public abstract void addChangeListener(ChangeListener paramChangeListener);

  public abstract void removeChangeListener(ChangeListener paramChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Style
 * JD-Core Version:    0.6.2
 */