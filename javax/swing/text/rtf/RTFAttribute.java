package javax.swing.text.rtf;

import java.io.IOException;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;

abstract interface RTFAttribute
{
  public static final int D_CHARACTER = 0;
  public static final int D_PARAGRAPH = 1;
  public static final int D_SECTION = 2;
  public static final int D_DOCUMENT = 3;
  public static final int D_META = 4;

  public abstract int domain();

  public abstract Object swingName();

  public abstract String rtfName();

  public abstract boolean set(MutableAttributeSet paramMutableAttributeSet);

  public abstract boolean set(MutableAttributeSet paramMutableAttributeSet, int paramInt);

  public abstract boolean setDefault(MutableAttributeSet paramMutableAttributeSet);

  public abstract boolean write(AttributeSet paramAttributeSet, RTFGenerator paramRTFGenerator, boolean paramBoolean)
    throws IOException;

  public abstract boolean writeValue(Object paramObject, RTFGenerator paramRTFGenerator, boolean paramBoolean)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFAttribute
 * JD-Core Version:    0.6.2
 */