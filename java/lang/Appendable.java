package java.lang;

import java.io.IOException;

public abstract interface Appendable {
    public abstract Appendable append(CharSequence paramCharSequence)
            throws IOException;

    public abstract Appendable append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
            throws IOException;

    public abstract Appendable append(char paramChar)
            throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Appendable
 * JD-Core Version:    0.6.2
 */