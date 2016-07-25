package java.beans;

import java.applet.Applet;
import java.beans.beancontext.BeanContext;

public abstract interface AppletInitializer
{
  public abstract void initialize(Applet paramApplet, BeanContext paramBeanContext);

  public abstract void activate(Applet paramApplet);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.AppletInitializer
 * JD-Core Version:    0.6.2
 */