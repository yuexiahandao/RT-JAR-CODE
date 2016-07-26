/*    */
package java.lang;

/*    */
/*    */ abstract class CharacterData
/*    */ {
    /*    */
    abstract int getProperties(int paramInt);

    /*    */
/*    */
    abstract int getType(int paramInt);

    /*    */
/*    */
    abstract boolean isWhitespace(int paramInt);

    /*    */
/*    */
    abstract boolean isMirrored(int paramInt);

    /*    */
/*    */
    abstract boolean isJavaIdentifierStart(int paramInt);

    /*    */
/*    */
    abstract boolean isJavaIdentifierPart(int paramInt);

    /*    */
/*    */
    abstract boolean isUnicodeIdentifierStart(int paramInt);

    /*    */
/*    */
    abstract boolean isUnicodeIdentifierPart(int paramInt);

    /*    */
/*    */
    abstract boolean isIdentifierIgnorable(int paramInt);

    /*    */
/*    */
    abstract int toLowerCase(int paramInt);

    /*    */
/*    */
    abstract int toUpperCase(int paramInt);

    /*    */
/*    */
    abstract int toTitleCase(int paramInt);

    /*    */
/*    */
    abstract int digit(int paramInt1, int paramInt2);

    /*    */
/*    */
    abstract int getNumericValue(int paramInt);

    /*    */
/*    */
    abstract byte getDirectionality(int paramInt);

    /*    */
/*    */   int toUpperCaseEx(int paramInt)
/*    */ {
/* 47 */
        return toUpperCase(paramInt);
/*    */
    }

    /*    */
/*    */   char[] toUpperCaseCharArray(int paramInt) {
/* 51 */
        return null;
/*    */
    }

    /*    */
/*    */   boolean isOtherLowercase(int paramInt) {
/* 55 */
        return false;
/*    */
    }

    /*    */
/*    */   boolean isOtherUppercase(int paramInt) {
/* 59 */
        return false;
/*    */
    }

    /*    */
/*    */   boolean isOtherAlphabetic(int paramInt) {
/* 63 */
        return false;
/*    */
    }

    /*    */
/*    */   boolean isIdeographic(int paramInt) {
/* 67 */
        return false;
/*    */
    }

    /*    */
/*    */
    static final CharacterData of(int paramInt)
/*    */ {
/* 77 */
        if (paramInt >>> 8 == 0) {
/* 78 */
            return CharacterDataLatin1.instance;
/*    */
        }
/* 80 */
        switch (paramInt >>> 16) {
/*    */
            case 0:
/* 82 */
                return CharacterData00.instance;
/*    */
            case 1:
/* 84 */
                return CharacterData01.instance;
/*    */
            case 2:
/* 86 */
                return CharacterData02.instance;
/*    */
            case 14:
/* 88 */
                return CharacterData0E.instance;
/*    */
            case 15:
/*    */
            case 16:
/* 91 */
                return CharacterDataPrivateUse.instance;
/*    */
            case 3:
/*    */
            case 4:
/*    */
            case 5:
/*    */
            case 6:
/*    */
            case 7:
/*    */
            case 8:
/*    */
            case 9:
/*    */
            case 10:
/*    */
            case 11:
/*    */
            case 12:
/* 93 */
            case 13:
        }
        return CharacterDataUndefined.instance;
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterData
 * JD-Core Version:    0.6.2
 */