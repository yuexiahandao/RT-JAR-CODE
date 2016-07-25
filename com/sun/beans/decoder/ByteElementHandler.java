package com.sun.beans.decoder;

final class ByteElementHandler extends StringElementHandler {
    public Object getValue(String paramString) {
        return Byte.decode(paramString);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ByteElementHandler
 * JD-Core Version:    0.6.2
 */