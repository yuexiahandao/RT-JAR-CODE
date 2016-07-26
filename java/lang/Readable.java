package java.lang;

import java.io.IOException;
import java.nio.CharBuffer;

public abstract interface Readable {
    public abstract int read(CharBuffer paramCharBuffer)
            throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Readable
 * JD-Core Version:    0.6.2
 */