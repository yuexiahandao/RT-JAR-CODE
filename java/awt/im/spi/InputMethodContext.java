package java.awt.im.spi;

import java.awt.Window;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import javax.swing.JFrame;

public abstract interface InputMethodContext extends InputMethodRequests
{
  public abstract void dispatchInputMethodEvent(int paramInt1, AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt2, TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2);

  public abstract Window createInputMethodWindow(String paramString, boolean paramBoolean);

  public abstract JFrame createInputMethodJFrame(String paramString, boolean paramBoolean);

  public abstract void enableClientWindowNotification(InputMethod paramInputMethod, boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.spi.InputMethodContext
 * JD-Core Version:    0.6.2
 */