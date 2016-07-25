package java.awt;

import java.awt.event.AdjustmentListener;

public abstract interface Adjustable
{
  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;
  public static final int NO_ORIENTATION = 2;

  public abstract int getOrientation();

  public abstract void setMinimum(int paramInt);

  public abstract int getMinimum();

  public abstract void setMaximum(int paramInt);

  public abstract int getMaximum();

  public abstract void setUnitIncrement(int paramInt);

  public abstract int getUnitIncrement();

  public abstract void setBlockIncrement(int paramInt);

  public abstract int getBlockIncrement();

  public abstract void setVisibleAmount(int paramInt);

  public abstract int getVisibleAmount();

  public abstract void setValue(int paramInt);

  public abstract int getValue();

  public abstract void addAdjustmentListener(AdjustmentListener paramAdjustmentListener);

  public abstract void removeAdjustmentListener(AdjustmentListener paramAdjustmentListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Adjustable
 * JD-Core Version:    0.6.2
 */