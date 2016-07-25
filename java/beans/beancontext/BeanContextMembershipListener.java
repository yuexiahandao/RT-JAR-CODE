package java.beans.beancontext;

import java.util.EventListener;

public abstract interface BeanContextMembershipListener extends EventListener
{
  public abstract void childrenAdded(BeanContextMembershipEvent paramBeanContextMembershipEvent);

  public abstract void childrenRemoved(BeanContextMembershipEvent paramBeanContextMembershipEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextMembershipListener
 * JD-Core Version:    0.6.2
 */