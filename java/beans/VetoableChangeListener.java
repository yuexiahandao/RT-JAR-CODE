package java.beans;

import java.util.EventListener;

public abstract interface VetoableChangeListener extends EventListener
{
  public abstract void vetoableChange(PropertyChangeEvent paramPropertyChangeEvent)
    throws PropertyVetoException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.VetoableChangeListener
 * JD-Core Version:    0.6.2
 */