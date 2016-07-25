package java.awt.im.spi;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.util.Locale;

public abstract interface InputMethod
{
  public abstract void setInputMethodContext(InputMethodContext paramInputMethodContext);

  public abstract boolean setLocale(Locale paramLocale);

  public abstract Locale getLocale();

  public abstract void setCharacterSubsets(Character.Subset[] paramArrayOfSubset);

  public abstract void setCompositionEnabled(boolean paramBoolean);

  public abstract boolean isCompositionEnabled();

  public abstract void reconvert();

  public abstract void dispatchEvent(AWTEvent paramAWTEvent);

  public abstract void notifyClientWindowChange(Rectangle paramRectangle);

  public abstract void activate();

  public abstract void deactivate(boolean paramBoolean);

  public abstract void hideWindows();

  public abstract void removeNotify();

  public abstract void endComposition();

  public abstract void dispose();

  public abstract Object getControlObject();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.spi.InputMethod
 * JD-Core Version:    0.6.2
 */