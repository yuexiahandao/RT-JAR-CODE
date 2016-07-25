package com.sun.beans.decoder;

final class BooleanElementHandler extends StringElementHandler {
    public Object getValue(String paramString) {
        if (Boolean.TRUE.toString().equalsIgnoreCase(paramString)) {
            return Boolean.TRUE;
        }
        if (Boolean.FALSE.toString().equalsIgnoreCase(paramString)) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Unsupported boolean argument: " + paramString);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.BooleanElementHandler
 * JD-Core Version:    0.6.2
 */