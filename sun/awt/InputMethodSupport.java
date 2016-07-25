package sun.awt;

import java.awt.AWTException;
import java.awt.Window;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;
import sun.awt.im.InputContext;

public abstract interface InputMethodSupport
{
  public abstract InputMethodDescriptor getInputMethodAdapterDescriptor()
    throws AWTException;

  public abstract Window createInputMethodWindow(String paramString, InputContext paramInputContext);

  public abstract boolean enableInputMethodsForTextComponent();

  public abstract Locale getDefaultKeyboardLocale();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.InputMethodSupport
 * JD-Core Version:    0.6.2
 */