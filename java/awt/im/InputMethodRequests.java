package java.awt.im;

import java.awt.Rectangle;
import java.awt.font.TextHitInfo;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;

public abstract interface InputMethodRequests
{
  public abstract Rectangle getTextLocation(TextHitInfo paramTextHitInfo);

  public abstract TextHitInfo getLocationOffset(int paramInt1, int paramInt2);

  public abstract int getInsertPositionOffset();

  public abstract AttributedCharacterIterator getCommittedText(int paramInt1, int paramInt2, AttributedCharacterIterator.Attribute[] paramArrayOfAttribute);

  public abstract int getCommittedTextLength();

  public abstract AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute);

  public abstract AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.InputMethodRequests
 * JD-Core Version:    0.6.2
 */