package com.sun.beans.decoder;

final class CharElementHandler extends StringElementHandler {
    public void addAttribute(String paramString1, String paramString2) {
        if (paramString1.equals("code")) {
            int i = Integer.decode(paramString2).intValue();
            for (char c : Character.toChars(i))
                addCharacter(c);
        } else {
            super.addAttribute(paramString1, paramString2);
        }
    }

    public Object getValue(String paramString) {
        if (paramString.length() != 1) {
            throw new IllegalArgumentException("Wrong characters count");
        }
        return Character.valueOf(paramString.charAt(0));
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.CharElementHandler
 * JD-Core Version:    0.6.2
 */