package javax.swing;

import javax.swing.event.ChangeListener;

public abstract interface BoundedRangeModel
{
  public abstract int getMinimum();

  public abstract void setMinimum(int paramInt);

  public abstract int getMaximum();

  public abstract void setMaximum(int paramInt);

  public abstract int getValue();

  public abstract void setValue(int paramInt);

  public abstract void setValueIsAdjusting(boolean paramBoolean);

  public abstract boolean getValueIsAdjusting();

  public abstract int getExtent();

  public abstract void setExtent(int paramInt);

  public abstract void setRangeProperties(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);

  public abstract void addChangeListener(ChangeListener paramChangeListener);

  public abstract void removeChangeListener(ChangeListener paramChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.BoundedRangeModel
 * JD-Core Version:    0.6.2
 */