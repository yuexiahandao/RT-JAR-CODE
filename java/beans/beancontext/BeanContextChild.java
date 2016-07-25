package java.beans.beancontext;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public abstract interface BeanContextChild
{
  public abstract void setBeanContext(BeanContext paramBeanContext)
    throws PropertyVetoException;

  public abstract BeanContext getBeanContext();

  public abstract void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener);

  public abstract void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener);

  public abstract void addVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener);

  public abstract void removeVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextChild
 * JD-Core Version:    0.6.2
 */