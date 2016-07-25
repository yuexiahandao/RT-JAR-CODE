package com.sun.beans.decoder;

final class LongElementHandler extends StringElementHandler {
    public Object getValue(String paramString) {
        return Long.decode(paramString);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.LongElementHandler
 * JD-Core Version:    0.6.2
 */