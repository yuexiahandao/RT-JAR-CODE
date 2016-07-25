package javax.accessibility;

import javax.swing.text.AttributeSet;

public abstract interface AccessibleEditableText extends AccessibleText
{
  public abstract void setTextContents(String paramString);

  public abstract void insertTextAtIndex(int paramInt, String paramString);

  public abstract String getTextRange(int paramInt1, int paramInt2);

  public abstract void delete(int paramInt1, int paramInt2);

  public abstract void cut(int paramInt1, int paramInt2);

  public abstract void paste(int paramInt);

  public abstract void replaceText(int paramInt1, int paramInt2, String paramString);

  public abstract void selectText(int paramInt1, int paramInt2);

  public abstract void setAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleEditableText
 * JD-Core Version:    0.6.2
 */