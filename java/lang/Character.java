/*      */ package java.lang;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class Character
/*      */   implements Serializable, Comparable<Character>
/*      */ {
/*      */   public static final int MIN_RADIX = 2;
/*      */   public static final int MAX_RADIX = 36;
/*      */   public static final char MIN_VALUE = '\000';
/*      */   public static final char MAX_VALUE = 'èøø';
/*  175 */   public static final Class<Character> TYPE = Class.getPrimitiveClass("char");
/*      */   public static final byte UNASSIGNED = 0;
/*      */   public static final byte UPPERCASE_LETTER = 1;
/*      */   public static final byte LOWERCASE_LETTER = 2;
/*      */   public static final byte TITLECASE_LETTER = 3;
/*      */   public static final byte MODIFIER_LETTER = 4;
/*      */   public static final byte OTHER_LETTER = 5;
/*      */   public static final byte NON_SPACING_MARK = 6;
/*      */   public static final byte ENCLOSING_MARK = 7;
/*      */   public static final byte COMBINING_SPACING_MARK = 8;
/*      */   public static final byte DECIMAL_DIGIT_NUMBER = 9;
/*      */   public static final byte LETTER_NUMBER = 10;
/*      */   public static final byte OTHER_NUMBER = 11;
/*      */   public static final byte SPACE_SEPARATOR = 12;
/*      */   public static final byte LINE_SEPARATOR = 13;
/*      */   public static final byte PARAGRAPH_SEPARATOR = 14;
/*      */   public static final byte CONTROL = 15;
/*      */   public static final byte FORMAT = 16;
/*      */   public static final byte PRIVATE_USE = 18;
/*      */   public static final byte SURROGATE = 19;
/*      */   public static final byte DASH_PUNCTUATION = 20;
/*      */   public static final byte START_PUNCTUATION = 21;
/*      */   public static final byte END_PUNCTUATION = 22;
/*      */   public static final byte CONNECTOR_PUNCTUATION = 23;
/*      */   public static final byte OTHER_PUNCTUATION = 24;
/*      */   public static final byte MATH_SYMBOL = 25;
/*      */   public static final byte CURRENCY_SYMBOL = 26;
/*      */   public static final byte MODIFIER_SYMBOL = 27;
/*      */   public static final byte OTHER_SYMBOL = 28;
/*      */   public static final byte INITIAL_QUOTE_PUNCTUATION = 29;
/*      */   public static final byte FINAL_QUOTE_PUNCTUATION = 30;
/*      */   static final int ERROR = -1;
/*      */   public static final byte DIRECTIONALITY_UNDEFINED = -1;
/*      */   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
/*      */   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
/*      */   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;
/*      */   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;
/*      */   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;
/*      */   public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;
/*      */   public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;
/*      */   public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;
/*      */   public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;
/*      */   public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;
/*      */   public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;
/*      */   public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;
/*      */   public static final byte DIRECTIONALITY_WHITESPACE = 12;
/*      */   public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;
/*      */   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;
/*      */   public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;
/*      */   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;
/*      */   public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;
/*      */   public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;
/*      */   public static final char MIN_HIGH_SURROGATE = 'Ì†Ä';
/*      */   public static final char MAX_HIGH_SURROGATE = 'ÌØø';
/*      */   public static final char MIN_LOW_SURROGATE = 'Ì∞Ä';
/*      */   public static final char MAX_LOW_SURROGATE = 'Ìøø';
/*      */   public static final char MIN_SURROGATE = 'Ì†Ä';
/*      */   public static final char MAX_SURROGATE = 'Ìøø';
/*      */   public static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
/*      */   public static final int MIN_CODE_POINT = 0;
/*      */   public static final int MAX_CODE_POINT = 1114111;
/*      */   private final char value;
/*      */   private static final long serialVersionUID = 3786198910865385080L;
/*      */   public static final int SIZE = 16;
/*      */ 
/*      */   public Character(char paramChar)
/*      */   {
/* 4354 */     this.value = paramChar;
/*      */   }
/*      */ 
/*      */   public static Character valueOf(char paramChar)
/*      */   {
/* 4386 */     if (paramChar <= '') {
/* 4387 */       return CharacterCache.cache[paramChar];
/*      */     }
/* 4389 */     return new Character(paramChar);
/*      */   }
/*      */ 
/*      */   public char charValue()
/*      */   {
/* 4398 */     return this.value;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 4408 */     return this.value;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 4422 */     if ((paramObject instanceof Character)) {
/* 4423 */       return this.value == ((Character)paramObject).charValue();
/*      */     }
/* 4425 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 4438 */     char[] arrayOfChar = { this.value };
/* 4439 */     return String.valueOf(arrayOfChar);
/*      */   }
/*      */ 
/*      */   public static String toString(char paramChar)
/*      */   {
/* 4452 */     return String.valueOf(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isValidCodePoint(int paramInt)
/*      */   {
/* 4470 */     int i = paramInt >>> 16;
/* 4471 */     return i < 17;
/*      */   }
/*      */ 
/*      */   public static boolean isBmpCodePoint(int paramInt)
/*      */   {
/* 4486 */     return paramInt >>> 16 == 0;
/*      */   }
/*      */ 
/*      */   public static boolean isSupplementaryCodePoint(int paramInt)
/*      */   {
/* 4505 */     return (paramInt >= 65536) && (paramInt < 1114112);
/*      */   }
/*      */ 
/*      */   public static boolean isHighSurrogate(char paramChar)
/*      */   {
/* 4531 */     return (paramChar >= 55296) && (paramChar < 56320);
/*      */   }
/*      */ 
/*      */   public static boolean isLowSurrogate(char paramChar)
/*      */   {
/* 4554 */     return (paramChar >= 56320) && (paramChar < 57344);
/*      */   }
/*      */ 
/*      */   public static boolean isSurrogate(char paramChar)
/*      */   {
/* 4578 */     return (paramChar >= 55296) && (paramChar < 57344);
/*      */   }
/*      */ 
/*      */   public static boolean isSurrogatePair(char paramChar1, char paramChar2)
/*      */   {
/* 4600 */     return (isHighSurrogate(paramChar1)) && (isLowSurrogate(paramChar2));
/*      */   }
/*      */ 
/*      */   public static int charCount(int paramInt)
/*      */   {
/* 4620 */     return paramInt >= 65536 ? 2 : 1;
/*      */   }
/*      */ 
/*      */   public static int toCodePoint(char paramChar1, char paramChar2)
/*      */   {
/* 4640 */     return (paramChar1 << '\n') + paramChar2 + -56613888;
/*      */   }
/*      */ 
/*      */   public static int codePointAt(CharSequence paramCharSequence, int paramInt)
/*      */   {
/* 4668 */     char c1 = paramCharSequence.charAt(paramInt++);
/* 4669 */     if ((isHighSurrogate(c1)) && 
/* 4670 */       (paramInt < paramCharSequence.length())) {
/* 4671 */       char c2 = paramCharSequence.charAt(paramInt);
/* 4672 */       if (isLowSurrogate(c2)) {
/* 4673 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */ 
/* 4677 */     return c1;
/*      */   }
/*      */ 
/*      */   public static int codePointAt(char[] paramArrayOfChar, int paramInt)
/*      */   {
/* 4702 */     return codePointAtImpl(paramArrayOfChar, paramInt, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public static int codePointAt(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 4731 */     if ((paramInt1 >= paramInt2) || (paramInt2 < 0) || (paramInt2 > paramArrayOfChar.length)) {
/* 4732 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 4734 */     return codePointAtImpl(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static int codePointAtImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 4739 */     char c1 = paramArrayOfChar[(paramInt1++)];
/* 4740 */     if ((isHighSurrogate(c1)) && 
/* 4741 */       (paramInt1 < paramInt2)) {
/* 4742 */       char c2 = paramArrayOfChar[paramInt1];
/* 4743 */       if (isLowSurrogate(c2)) {
/* 4744 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */ 
/* 4748 */     return c1;
/*      */   }
/*      */ 
/*      */   public static int codePointBefore(CharSequence paramCharSequence, int paramInt)
/*      */   {
/* 4773 */     char c1 = paramCharSequence.charAt(--paramInt);
/* 4774 */     if ((isLowSurrogate(c1)) && 
/* 4775 */       (paramInt > 0)) {
/* 4776 */       char c2 = paramCharSequence.charAt(--paramInt);
/* 4777 */       if (isHighSurrogate(c2)) {
/* 4778 */         return toCodePoint(c2, c1);
/*      */       }
/*      */     }
/*      */ 
/* 4782 */     return c1;
/*      */   }
/*      */ 
/*      */   public static int codePointBefore(char[] paramArrayOfChar, int paramInt)
/*      */   {
/* 4807 */     return codePointBeforeImpl(paramArrayOfChar, paramInt, 0);
/*      */   }
/*      */ 
/*      */   public static int codePointBefore(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 4838 */     if ((paramInt1 <= paramInt2) || (paramInt2 < 0) || (paramInt2 >= paramArrayOfChar.length)) {
/* 4839 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 4841 */     return codePointBeforeImpl(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static int codePointBeforeImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 4846 */     char c1 = paramArrayOfChar[(--paramInt1)];
/* 4847 */     if ((isLowSurrogate(c1)) && 
/* 4848 */       (paramInt1 > paramInt2)) {
/* 4849 */       char c2 = paramArrayOfChar[(--paramInt1)];
/* 4850 */       if (isHighSurrogate(c2)) {
/* 4851 */         return toCodePoint(c2, c1);
/*      */       }
/*      */     }
/*      */ 
/* 4855 */     return c1;
/*      */   }
/*      */ 
/*      */   public static char highSurrogate(int paramInt)
/*      */   {
/* 4883 */     return (char)((paramInt >>> 10) + 55232);
/*      */   }
/*      */ 
/*      */   public static char lowSurrogate(int paramInt)
/*      */   {
/* 4912 */     return (char)((paramInt & 0x3FF) + 56320);
/*      */   }
/*      */ 
/*      */   public static int toChars(int paramInt1, char[] paramArrayOfChar, int paramInt2)
/*      */   {
/* 4947 */     if (isBmpCodePoint(paramInt1)) {
/* 4948 */       paramArrayOfChar[paramInt2] = ((char)paramInt1);
/* 4949 */       return 1;
/* 4950 */     }if (isValidCodePoint(paramInt1)) {
/* 4951 */       toSurrogates(paramInt1, paramArrayOfChar, paramInt2);
/* 4952 */       return 2;
/*      */     }
/* 4954 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */   public static char[] toChars(int paramInt)
/*      */   {
/* 4975 */     if (isBmpCodePoint(paramInt))
/* 4976 */       return new char[] { (char)paramInt };
/* 4977 */     if (isValidCodePoint(paramInt)) {
/* 4978 */       char[] arrayOfChar = new char[2];
/* 4979 */       toSurrogates(paramInt, arrayOfChar, 0);
/* 4980 */       return arrayOfChar;
/*      */     }
/* 4982 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */   static void toSurrogates(int paramInt1, char[] paramArrayOfChar, int paramInt2)
/*      */   {
/* 4988 */     paramArrayOfChar[(paramInt2 + 1)] = lowSurrogate(paramInt1);
/* 4989 */     paramArrayOfChar[paramInt2] = highSurrogate(paramInt1);
/*      */   }
/*      */ 
/*      */   public static int codePointCount(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 5016 */     int i = paramCharSequence.length();
/* 5017 */     if ((paramInt1 < 0) || (paramInt2 > i) || (paramInt1 > paramInt2)) {
/* 5018 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 5020 */     int j = paramInt2 - paramInt1;
/* 5021 */     for (int k = paramInt1; k < paramInt2; ) {
/* 5022 */       if ((isHighSurrogate(paramCharSequence.charAt(k++))) && (k < paramInt2) && (isLowSurrogate(paramCharSequence.charAt(k))))
/*      */       {
/* 5024 */         j--;
/* 5025 */         k++;
/*      */       }
/*      */     }
/* 5028 */     return j;
/*      */   }
/*      */ 
/*      */   public static int codePointCount(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 5051 */     if ((paramInt2 > paramArrayOfChar.length - paramInt1) || (paramInt1 < 0) || (paramInt2 < 0)) {
/* 5052 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 5054 */     return codePointCountImpl(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static int codePointCountImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 5058 */     int i = paramInt1 + paramInt2;
/* 5059 */     int j = paramInt2;
/* 5060 */     for (int k = paramInt1; k < i; ) {
/* 5061 */       if ((isHighSurrogate(paramArrayOfChar[(k++)])) && (k < i) && (isLowSurrogate(paramArrayOfChar[k])))
/*      */       {
/* 5063 */         j--;
/* 5064 */         k++;
/*      */       }
/*      */     }
/* 5067 */     return j;
/*      */   }
/*      */ 
/*      */   public static int offsetByCodePoints(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 5094 */     int i = paramCharSequence.length();
/* 5095 */     if ((paramInt1 < 0) || (paramInt1 > i)) {
/* 5096 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */ 
/* 5099 */     int j = paramInt1;
/*      */     int k;
/* 5100 */     if (paramInt2 >= 0)
/*      */     {
/* 5102 */       for (k = 0; (j < i) && (k < paramInt2); k++) {
/* 5103 */         if ((isHighSurrogate(paramCharSequence.charAt(j++))) && (j < i) && (isLowSurrogate(paramCharSequence.charAt(j))))
/*      */         {
/* 5105 */           j++;
/*      */         }
/*      */       }
/* 5108 */       if (k < paramInt2)
/* 5109 */         throw new IndexOutOfBoundsException();
/*      */     }
/*      */     else
/*      */     {
/* 5113 */       for (k = paramInt2; (j > 0) && (k < 0); k++) {
/* 5114 */         if ((isLowSurrogate(paramCharSequence.charAt(--j))) && (j > 0) && (isHighSurrogate(paramCharSequence.charAt(j - 1))))
/*      */         {
/* 5116 */           j--;
/*      */         }
/*      */       }
/* 5119 */       if (k < 0) {
/* 5120 */         throw new IndexOutOfBoundsException();
/*      */       }
/*      */     }
/* 5123 */     return j;
/*      */   }
/*      */ 
/*      */   public static int offsetByCodePoints(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 5161 */     if ((paramInt2 > paramArrayOfChar.length - paramInt1) || (paramInt1 < 0) || (paramInt2 < 0) || (paramInt3 < paramInt1) || (paramInt3 > paramInt1 + paramInt2))
/*      */     {
/* 5163 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 5165 */     return offsetByCodePointsImpl(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   static int offsetByCodePointsImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 5170 */     int i = paramInt3;
/*      */     int j;
/* 5171 */     if (paramInt4 >= 0) {
/* 5172 */       j = paramInt1 + paramInt2;
/*      */ 
/* 5174 */       for (int k = 0; (i < j) && (k < paramInt4); k++) {
/* 5175 */         if ((isHighSurrogate(paramArrayOfChar[(i++)])) && (i < j) && (isLowSurrogate(paramArrayOfChar[i])))
/*      */         {
/* 5177 */           i++;
/*      */         }
/*      */       }
/* 5180 */       if (k < paramInt4)
/* 5181 */         throw new IndexOutOfBoundsException();
/*      */     }
/*      */     else
/*      */     {
/* 5185 */       for (j = paramInt4; (i > paramInt1) && (j < 0); j++) {
/* 5186 */         if ((isLowSurrogate(paramArrayOfChar[(--i)])) && (i > paramInt1) && (isHighSurrogate(paramArrayOfChar[(i - 1)])))
/*      */         {
/* 5188 */           i--;
/*      */         }
/*      */       }
/* 5191 */       if (j < 0) {
/* 5192 */         throw new IndexOutOfBoundsException();
/*      */       }
/*      */     }
/* 5195 */     return i;
/*      */   }
/*      */ 
/*      */   public static boolean isLowerCase(char paramChar)
/*      */   {
/* 5230 */     return isLowerCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isLowerCase(int paramInt)
/*      */   {
/* 5262 */     return (getType(paramInt) == 2) || (CharacterData.of(paramInt).isOtherLowercase(paramInt));
/*      */   }
/*      */ 
/*      */   public static boolean isUpperCase(char paramChar)
/*      */   {
/* 5298 */     return isUpperCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isUpperCase(int paramInt)
/*      */   {
/* 5328 */     return (getType(paramInt) == 1) || (CharacterData.of(paramInt).isOtherUppercase(paramInt));
/*      */   }
/*      */ 
/*      */   public static boolean isTitleCase(char paramChar)
/*      */   {
/* 5370 */     return isTitleCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isTitleCase(int paramInt)
/*      */   {
/* 5406 */     return getType(paramInt) == 3;
/*      */   }
/*      */ 
/*      */   public static boolean isDigit(char paramChar)
/*      */   {
/* 5445 */     return isDigit(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isDigit(int paramInt)
/*      */   {
/* 5479 */     return getType(paramInt) == 9;
/*      */   }
/*      */ 
/*      */   public static boolean isDefined(char paramChar)
/*      */   {
/* 5508 */     return isDefined(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isDefined(int paramInt)
/*      */   {
/* 5532 */     return getType(paramInt) != 0;
/*      */   }
/*      */ 
/*      */   public static boolean isLetter(char paramChar)
/*      */   {
/* 5571 */     return isLetter(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isLetter(int paramInt)
/*      */   {
/* 5604 */     return (62 >> getType(paramInt) & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   public static boolean isLetterOrDigit(char paramChar)
/*      */   {
/* 5637 */     return isLetterOrDigit(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isLetterOrDigit(int paramInt)
/*      */   {
/* 5658 */     return (574 >> getType(paramInt) & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static boolean isJavaLetter(char paramChar)
/*      */   {
/* 5694 */     return isJavaIdentifierStart(paramChar);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static boolean isJavaLetterOrDigit(char paramChar)
/*      */   {
/* 5730 */     return isJavaIdentifierPart(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isAlphabetic(int paramInt)
/*      */   {
/* 5756 */     return ((1086 >> getType(paramInt) & 0x1) != 0) || (CharacterData.of(paramInt).isOtherAlphabetic(paramInt));
/*      */   }
/*      */ 
/*      */   public static boolean isIdeographic(int paramInt)
/*      */   {
/* 5776 */     return CharacterData.of(paramInt).isIdeographic(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isJavaIdentifierStart(char paramChar)
/*      */   {
/* 5807 */     return isJavaIdentifierStart(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isJavaIdentifierStart(int paramInt)
/*      */   {
/* 5836 */     return CharacterData.of(paramInt).isJavaIdentifierStart(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isJavaIdentifierPart(char paramChar)
/*      */   {
/* 5873 */     return isJavaIdentifierPart(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isJavaIdentifierPart(int paramInt)
/*      */   {
/* 5906 */     return CharacterData.of(paramInt).isJavaIdentifierPart(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isUnicodeIdentifierStart(char paramChar)
/*      */   {
/* 5935 */     return isUnicodeIdentifierStart(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isUnicodeIdentifierStart(int paramInt)
/*      */   {
/* 5959 */     return CharacterData.of(paramInt).isUnicodeIdentifierStart(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isUnicodeIdentifierPart(char paramChar)
/*      */   {
/* 5994 */     return isUnicodeIdentifierPart(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isUnicodeIdentifierPart(int paramInt)
/*      */   {
/* 6023 */     return CharacterData.of(paramInt).isUnicodeIdentifierPart(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isIdentifierIgnorable(char paramChar)
/*      */   {
/* 6058 */     return isIdentifierIgnorable(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isIdentifierIgnorable(int paramInt)
/*      */   {
/* 6088 */     return CharacterData.of(paramInt).isIdentifierIgnorable(paramInt);
/*      */   }
/*      */ 
/*      */   public static char toLowerCase(char paramChar)
/*      */   {
/* 6119 */     return (char)toLowerCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static int toLowerCase(int paramInt)
/*      */   {
/* 6148 */     return CharacterData.of(paramInt).toLowerCase(paramInt);
/*      */   }
/*      */ 
/*      */   public static char toUpperCase(char paramChar)
/*      */   {
/* 6179 */     return (char)toUpperCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static int toUpperCase(int paramInt)
/*      */   {
/* 6208 */     return CharacterData.of(paramInt).toUpperCase(paramInt);
/*      */   }
/*      */ 
/*      */   public static char toTitleCase(char paramChar)
/*      */   {
/* 6240 */     return (char)toTitleCase(paramChar);
/*      */   }
/*      */ 
/*      */   public static int toTitleCase(int paramInt)
/*      */   {
/* 6267 */     return CharacterData.of(paramInt).toTitleCase(paramInt);
/*      */   }
/*      */ 
/*      */   public static int digit(char paramChar, int paramInt)
/*      */   {
/* 6321 */     return digit(paramChar, paramInt);
/*      */   }
/*      */ 
/*      */   public static int digit(int paramInt1, int paramInt2)
/*      */   {
/* 6373 */     return CharacterData.of(paramInt1).digit(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public static int getNumericValue(char paramChar)
/*      */   {
/* 6411 */     return getNumericValue(paramChar);
/*      */   }
/*      */ 
/*      */   public static int getNumericValue(int paramInt)
/*      */   {
/* 6444 */     return CharacterData.of(paramInt).getNumericValue(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static boolean isSpace(char paramChar)
/*      */   {
/* 6473 */     return (paramChar <= ' ') && ((4294981120L >> paramChar & 1L) != 0L);
/*      */   }
/*      */ 
/*      */   public static boolean isSpaceChar(char paramChar)
/*      */   {
/* 6506 */     return isSpaceChar(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isSpaceChar(int paramInt)
/*      */   {
/* 6529 */     return (28672 >> getType(paramInt) & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   public static boolean isWhitespace(char paramChar)
/*      */   {
/* 6567 */     return isWhitespace(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isWhitespace(int paramInt)
/*      */   {
/* 6599 */     return CharacterData.of(paramInt).isWhitespace(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isISOControl(char paramChar)
/*      */   {
/* 6623 */     return isISOControl(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isISOControl(int paramInt)
/*      */   {
/* 6644 */     return (paramInt <= 159) && ((paramInt >= 127) || (paramInt >>> 5 == 0));
/*      */   }
/*      */ 
/*      */   public static int getType(char paramChar)
/*      */   {
/* 6692 */     return getType(paramChar);
/*      */   }
/*      */ 
/*      */   public static int getType(int paramInt)
/*      */   {
/* 6734 */     return CharacterData.of(paramInt).getType(paramInt);
/*      */   }
/*      */ 
/*      */   public static char forDigit(int paramInt1, int paramInt2)
/*      */   {
/* 6762 */     if ((paramInt1 >= paramInt2) || (paramInt1 < 0)) {
/* 6763 */       return '\000';
/*      */     }
/* 6765 */     if ((paramInt2 < 2) || (paramInt2 > 36)) {
/* 6766 */       return '\000';
/*      */     }
/* 6768 */     if (paramInt1 < 10) {
/* 6769 */       return (char)(48 + paramInt1);
/*      */     }
/* 6771 */     return (char)(87 + paramInt1);
/*      */   }
/*      */ 
/*      */   public static byte getDirectionality(char paramChar)
/*      */   {
/* 6812 */     return getDirectionality(paramChar);
/*      */   }
/*      */ 
/*      */   public static byte getDirectionality(int paramInt)
/*      */   {
/* 6849 */     return CharacterData.of(paramInt).getDirectionality(paramInt);
/*      */   }
/*      */ 
/*      */   public static boolean isMirrored(char paramChar)
/*      */   {
/* 6872 */     return isMirrored(paramChar);
/*      */   }
/*      */ 
/*      */   public static boolean isMirrored(int paramInt)
/*      */   {
/* 6891 */     return CharacterData.of(paramInt).isMirrored(paramInt);
/*      */   }
/*      */ 
/*      */   public int compareTo(Character paramCharacter)
/*      */   {
/* 6910 */     return compare(this.value, paramCharacter.value);
/*      */   }
/*      */ 
/*      */   public static int compare(char paramChar1, char paramChar2)
/*      */   {
/* 6928 */     return paramChar1 - paramChar2;
/*      */   }
/*      */ 
/*      */   static int toUpperCaseEx(int paramInt)
/*      */   {
/* 6947 */     assert (isValidCodePoint(paramInt));
/* 6948 */     return CharacterData.of(paramInt).toUpperCaseEx(paramInt);
/*      */   }
/*      */ 
/*      */   static char[] toUpperCaseCharArray(int paramInt)
/*      */   {
/* 6964 */     assert (isBmpCodePoint(paramInt));
/* 6965 */     return CharacterData.of(paramInt).toUpperCaseCharArray(paramInt);
/*      */   }
/*      */ 
/*      */   public static char reverseBytes(char paramChar)
/*      */   {
/* 6985 */     return (char)((paramChar & 0xFF00) >> '\b' | paramChar << '\b');
/*      */   }
/*      */ 
/*      */   public static String getName(int paramInt)
/*      */   {
/* 7017 */     if (!isValidCodePoint(paramInt)) {
/* 7018 */       throw new IllegalArgumentException();
/*      */     }
/* 7020 */     String str = CharacterName.get(paramInt);
/* 7021 */     if (str != null)
/* 7022 */       return str;
/* 7023 */     if (getType(paramInt) == 0)
/* 7024 */       return null;
/* 7025 */     UnicodeBlock localUnicodeBlock = UnicodeBlock.of(paramInt);
/* 7026 */     if (localUnicodeBlock != null) {
/* 7027 */       return localUnicodeBlock.toString().replace('_', ' ') + " " + Integer.toHexString(paramInt).toUpperCase(Locale.ENGLISH);
/*      */     }
/*      */ 
/* 7030 */     return Integer.toHexString(paramInt).toUpperCase(Locale.ENGLISH);
/*      */   }
/*      */ 
/*      */   private static class CharacterCache
/*      */   {
/* 4360 */     static final Character[] cache = new Character['¬Ä'];
/*      */ 
/*      */     static {
/* 4363 */       for (int i = 0; i < cache.length; i++)
/* 4364 */         cache[i] = new Character((char)i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Subset
/*      */   {
/*      */     private String name;
/*      */ 
/*      */     protected Subset(String paramString)
/*      */     {
/*  600 */       if (paramString == null) {
/*  601 */         throw new NullPointerException("name");
/*      */       }
/*  603 */       this.name = paramString;
/*      */     }
/*      */ 
/*      */     public final boolean equals(Object paramObject)
/*      */     {
/*  614 */       return this == paramObject;
/*      */     }
/*      */ 
/*      */     public final int hashCode()
/*      */     {
/*  625 */       return super.hashCode();
/*      */     }
/*      */ 
/*      */     public final String toString()
/*      */     {
/*  632 */       return this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class UnicodeBlock extends Character.Subset
/*      */   {
/*  649 */     private static Map<String, UnicodeBlock> map = new HashMap(256);
/*      */ 
/*  683 */     public static final UnicodeBlock BASIC_LATIN = new UnicodeBlock("BASIC_LATIN", new String[] { "BASIC LATIN", "BASICLATIN" });
/*      */ 
/*  692 */     public static final UnicodeBlock LATIN_1_SUPPLEMENT = new UnicodeBlock("LATIN_1_SUPPLEMENT", new String[] { "LATIN-1 SUPPLEMENT", "LATIN-1SUPPLEMENT" });
/*      */ 
/*  701 */     public static final UnicodeBlock LATIN_EXTENDED_A = new UnicodeBlock("LATIN_EXTENDED_A", new String[] { "LATIN EXTENDED-A", "LATINEXTENDED-A" });
/*      */ 
/*  710 */     public static final UnicodeBlock LATIN_EXTENDED_B = new UnicodeBlock("LATIN_EXTENDED_B", new String[] { "LATIN EXTENDED-B", "LATINEXTENDED-B" });
/*      */ 
/*  719 */     public static final UnicodeBlock IPA_EXTENSIONS = new UnicodeBlock("IPA_EXTENSIONS", new String[] { "IPA EXTENSIONS", "IPAEXTENSIONS" });
/*      */ 
/*  728 */     public static final UnicodeBlock SPACING_MODIFIER_LETTERS = new UnicodeBlock("SPACING_MODIFIER_LETTERS", new String[] { "SPACING MODIFIER LETTERS", "SPACINGMODIFIERLETTERS" });
/*      */ 
/*  737 */     public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS", new String[] { "COMBINING DIACRITICAL MARKS", "COMBININGDIACRITICALMARKS" });
/*      */ 
/*  749 */     public static final UnicodeBlock GREEK = new UnicodeBlock("GREEK", new String[] { "GREEK AND COPTIC", "GREEKANDCOPTIC" });
/*      */ 
/*  758 */     public static final UnicodeBlock CYRILLIC = new UnicodeBlock("CYRILLIC");
/*      */ 
/*  765 */     public static final UnicodeBlock ARMENIAN = new UnicodeBlock("ARMENIAN");
/*      */ 
/*  772 */     public static final UnicodeBlock HEBREW = new UnicodeBlock("HEBREW");
/*      */ 
/*  779 */     public static final UnicodeBlock ARABIC = new UnicodeBlock("ARABIC");
/*      */ 
/*  786 */     public static final UnicodeBlock DEVANAGARI = new UnicodeBlock("DEVANAGARI");
/*      */ 
/*  793 */     public static final UnicodeBlock BENGALI = new UnicodeBlock("BENGALI");
/*      */ 
/*  800 */     public static final UnicodeBlock GURMUKHI = new UnicodeBlock("GURMUKHI");
/*      */ 
/*  807 */     public static final UnicodeBlock GUJARATI = new UnicodeBlock("GUJARATI");
/*      */ 
/*  814 */     public static final UnicodeBlock ORIYA = new UnicodeBlock("ORIYA");
/*      */ 
/*  821 */     public static final UnicodeBlock TAMIL = new UnicodeBlock("TAMIL");
/*      */ 
/*  828 */     public static final UnicodeBlock TELUGU = new UnicodeBlock("TELUGU");
/*      */ 
/*  835 */     public static final UnicodeBlock KANNADA = new UnicodeBlock("KANNADA");
/*      */ 
/*  842 */     public static final UnicodeBlock MALAYALAM = new UnicodeBlock("MALAYALAM");
/*      */ 
/*  849 */     public static final UnicodeBlock THAI = new UnicodeBlock("THAI");
/*      */ 
/*  856 */     public static final UnicodeBlock LAO = new UnicodeBlock("LAO");
/*      */ 
/*  863 */     public static final UnicodeBlock TIBETAN = new UnicodeBlock("TIBETAN");
/*      */ 
/*  870 */     public static final UnicodeBlock GEORGIAN = new UnicodeBlock("GEORGIAN");
/*      */ 
/*  877 */     public static final UnicodeBlock HANGUL_JAMO = new UnicodeBlock("HANGUL_JAMO", new String[] { "HANGUL JAMO", "HANGULJAMO" });
/*      */ 
/*  886 */     public static final UnicodeBlock LATIN_EXTENDED_ADDITIONAL = new UnicodeBlock("LATIN_EXTENDED_ADDITIONAL", new String[] { "LATIN EXTENDED ADDITIONAL", "LATINEXTENDEDADDITIONAL" });
/*      */ 
/*  895 */     public static final UnicodeBlock GREEK_EXTENDED = new UnicodeBlock("GREEK_EXTENDED", new String[] { "GREEK EXTENDED", "GREEKEXTENDED" });
/*      */ 
/*  904 */     public static final UnicodeBlock GENERAL_PUNCTUATION = new UnicodeBlock("GENERAL_PUNCTUATION", new String[] { "GENERAL PUNCTUATION", "GENERALPUNCTUATION" });
/*      */ 
/*  914 */     public static final UnicodeBlock SUPERSCRIPTS_AND_SUBSCRIPTS = new UnicodeBlock("SUPERSCRIPTS_AND_SUBSCRIPTS", new String[] { "SUPERSCRIPTS AND SUBSCRIPTS", "SUPERSCRIPTSANDSUBSCRIPTS" });
/*      */ 
/*  923 */     public static final UnicodeBlock CURRENCY_SYMBOLS = new UnicodeBlock("CURRENCY_SYMBOLS", new String[] { "CURRENCY SYMBOLS", "CURRENCYSYMBOLS" });
/*      */ 
/*  935 */     public static final UnicodeBlock COMBINING_MARKS_FOR_SYMBOLS = new UnicodeBlock("COMBINING_MARKS_FOR_SYMBOLS", new String[] { "COMBINING DIACRITICAL MARKS FOR SYMBOLS", "COMBININGDIACRITICALMARKSFORSYMBOLS", "COMBINING MARKS FOR SYMBOLS", "COMBININGMARKSFORSYMBOLS" });
/*      */ 
/*  946 */     public static final UnicodeBlock LETTERLIKE_SYMBOLS = new UnicodeBlock("LETTERLIKE_SYMBOLS", new String[] { "LETTERLIKE SYMBOLS", "LETTERLIKESYMBOLS" });
/*      */ 
/*  955 */     public static final UnicodeBlock NUMBER_FORMS = new UnicodeBlock("NUMBER_FORMS", new String[] { "NUMBER FORMS", "NUMBERFORMS" });
/*      */ 
/*  964 */     public static final UnicodeBlock ARROWS = new UnicodeBlock("ARROWS");
/*      */ 
/*  971 */     public static final UnicodeBlock MATHEMATICAL_OPERATORS = new UnicodeBlock("MATHEMATICAL_OPERATORS", new String[] { "MATHEMATICAL OPERATORS", "MATHEMATICALOPERATORS" });
/*      */ 
/*  980 */     public static final UnicodeBlock MISCELLANEOUS_TECHNICAL = new UnicodeBlock("MISCELLANEOUS_TECHNICAL", new String[] { "MISCELLANEOUS TECHNICAL", "MISCELLANEOUSTECHNICAL" });
/*      */ 
/*  989 */     public static final UnicodeBlock CONTROL_PICTURES = new UnicodeBlock("CONTROL_PICTURES", new String[] { "CONTROL PICTURES", "CONTROLPICTURES" });
/*      */ 
/*  998 */     public static final UnicodeBlock OPTICAL_CHARACTER_RECOGNITION = new UnicodeBlock("OPTICAL_CHARACTER_RECOGNITION", new String[] { "OPTICAL CHARACTER RECOGNITION", "OPTICALCHARACTERRECOGNITION" });
/*      */ 
/* 1007 */     public static final UnicodeBlock ENCLOSED_ALPHANUMERICS = new UnicodeBlock("ENCLOSED_ALPHANUMERICS", new String[] { "ENCLOSED ALPHANUMERICS", "ENCLOSEDALPHANUMERICS" });
/*      */ 
/* 1016 */     public static final UnicodeBlock BOX_DRAWING = new UnicodeBlock("BOX_DRAWING", new String[] { "BOX DRAWING", "BOXDRAWING" });
/*      */ 
/* 1025 */     public static final UnicodeBlock BLOCK_ELEMENTS = new UnicodeBlock("BLOCK_ELEMENTS", new String[] { "BLOCK ELEMENTS", "BLOCKELEMENTS" });
/*      */ 
/* 1034 */     public static final UnicodeBlock GEOMETRIC_SHAPES = new UnicodeBlock("GEOMETRIC_SHAPES", new String[] { "GEOMETRIC SHAPES", "GEOMETRICSHAPES" });
/*      */ 
/* 1043 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS", new String[] { "MISCELLANEOUS SYMBOLS", "MISCELLANEOUSSYMBOLS" });
/*      */ 
/* 1052 */     public static final UnicodeBlock DINGBATS = new UnicodeBlock("DINGBATS");
/*      */ 
/* 1059 */     public static final UnicodeBlock CJK_SYMBOLS_AND_PUNCTUATION = new UnicodeBlock("CJK_SYMBOLS_AND_PUNCTUATION", new String[] { "CJK SYMBOLS AND PUNCTUATION", "CJKSYMBOLSANDPUNCTUATION" });
/*      */ 
/* 1068 */     public static final UnicodeBlock HIRAGANA = new UnicodeBlock("HIRAGANA");
/*      */ 
/* 1075 */     public static final UnicodeBlock KATAKANA = new UnicodeBlock("KATAKANA");
/*      */ 
/* 1082 */     public static final UnicodeBlock BOPOMOFO = new UnicodeBlock("BOPOMOFO");
/*      */ 
/* 1089 */     public static final UnicodeBlock HANGUL_COMPATIBILITY_JAMO = new UnicodeBlock("HANGUL_COMPATIBILITY_JAMO", new String[] { "HANGUL COMPATIBILITY JAMO", "HANGULCOMPATIBILITYJAMO" });
/*      */ 
/* 1098 */     public static final UnicodeBlock KANBUN = new UnicodeBlock("KANBUN");
/*      */ 
/* 1105 */     public static final UnicodeBlock ENCLOSED_CJK_LETTERS_AND_MONTHS = new UnicodeBlock("ENCLOSED_CJK_LETTERS_AND_MONTHS", new String[] { "ENCLOSED CJK LETTERS AND MONTHS", "ENCLOSEDCJKLETTERSANDMONTHS" });
/*      */ 
/* 1114 */     public static final UnicodeBlock CJK_COMPATIBILITY = new UnicodeBlock("CJK_COMPATIBILITY", new String[] { "CJK COMPATIBILITY", "CJKCOMPATIBILITY" });
/*      */ 
/* 1123 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS", new String[] { "CJK UNIFIED IDEOGRAPHS", "CJKUNIFIEDIDEOGRAPHS" });
/*      */ 
/* 1132 */     public static final UnicodeBlock HANGUL_SYLLABLES = new UnicodeBlock("HANGUL_SYLLABLES", new String[] { "HANGUL SYLLABLES", "HANGULSYLLABLES" });
/*      */ 
/* 1141 */     public static final UnicodeBlock PRIVATE_USE_AREA = new UnicodeBlock("PRIVATE_USE_AREA", new String[] { "PRIVATE USE AREA", "PRIVATEUSEAREA" });
/*      */ 
/* 1151 */     public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS", new String[] { "CJK COMPATIBILITY IDEOGRAPHS", "CJKCOMPATIBILITYIDEOGRAPHS" });
/*      */ 
/* 1160 */     public static final UnicodeBlock ALPHABETIC_PRESENTATION_FORMS = new UnicodeBlock("ALPHABETIC_PRESENTATION_FORMS", new String[] { "ALPHABETIC PRESENTATION FORMS", "ALPHABETICPRESENTATIONFORMS" });
/*      */ 
/* 1170 */     public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_A = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_A", new String[] { "ARABIC PRESENTATION FORMS-A", "ARABICPRESENTATIONFORMS-A" });
/*      */ 
/* 1179 */     public static final UnicodeBlock COMBINING_HALF_MARKS = new UnicodeBlock("COMBINING_HALF_MARKS", new String[] { "COMBINING HALF MARKS", "COMBININGHALFMARKS" });
/*      */ 
/* 1188 */     public static final UnicodeBlock CJK_COMPATIBILITY_FORMS = new UnicodeBlock("CJK_COMPATIBILITY_FORMS", new String[] { "CJK COMPATIBILITY FORMS", "CJKCOMPATIBILITYFORMS" });
/*      */ 
/* 1197 */     public static final UnicodeBlock SMALL_FORM_VARIANTS = new UnicodeBlock("SMALL_FORM_VARIANTS", new String[] { "SMALL FORM VARIANTS", "SMALLFORMVARIANTS" });
/*      */ 
/* 1206 */     public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_B = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_B", new String[] { "ARABIC PRESENTATION FORMS-B", "ARABICPRESENTATIONFORMS-B" });
/*      */ 
/* 1216 */     public static final UnicodeBlock HALFWIDTH_AND_FULLWIDTH_FORMS = new UnicodeBlock("HALFWIDTH_AND_FULLWIDTH_FORMS", new String[] { "HALFWIDTH AND FULLWIDTH FORMS", "HALFWIDTHANDFULLWIDTHFORMS" });
/*      */ 
/* 1225 */     public static final UnicodeBlock SPECIALS = new UnicodeBlock("SPECIALS");
/*      */ 
/*      */     @Deprecated
/* 1237 */     public static final UnicodeBlock SURROGATES_AREA = new UnicodeBlock("SURROGATES_AREA");
/*      */ 
/* 1244 */     public static final UnicodeBlock SYRIAC = new UnicodeBlock("SYRIAC");
/*      */ 
/* 1251 */     public static final UnicodeBlock THAANA = new UnicodeBlock("THAANA");
/*      */ 
/* 1258 */     public static final UnicodeBlock SINHALA = new UnicodeBlock("SINHALA");
/*      */ 
/* 1265 */     public static final UnicodeBlock MYANMAR = new UnicodeBlock("MYANMAR");
/*      */ 
/* 1272 */     public static final UnicodeBlock ETHIOPIC = new UnicodeBlock("ETHIOPIC");
/*      */ 
/* 1279 */     public static final UnicodeBlock CHEROKEE = new UnicodeBlock("CHEROKEE");
/*      */ 
/* 1286 */     public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS", new String[] { "UNIFIED CANADIAN ABORIGINAL SYLLABICS", "UNIFIEDCANADIANABORIGINALSYLLABICS" });
/*      */ 
/* 1295 */     public static final UnicodeBlock OGHAM = new UnicodeBlock("OGHAM");
/*      */ 
/* 1302 */     public static final UnicodeBlock RUNIC = new UnicodeBlock("RUNIC");
/*      */ 
/* 1309 */     public static final UnicodeBlock KHMER = new UnicodeBlock("KHMER");
/*      */ 
/* 1316 */     public static final UnicodeBlock MONGOLIAN = new UnicodeBlock("MONGOLIAN");
/*      */ 
/* 1323 */     public static final UnicodeBlock BRAILLE_PATTERNS = new UnicodeBlock("BRAILLE_PATTERNS", new String[] { "BRAILLE PATTERNS", "BRAILLEPATTERNS" });
/*      */ 
/* 1332 */     public static final UnicodeBlock CJK_RADICALS_SUPPLEMENT = new UnicodeBlock("CJK_RADICALS_SUPPLEMENT", new String[] { "CJK RADICALS SUPPLEMENT", "CJKRADICALSSUPPLEMENT" });
/*      */ 
/* 1341 */     public static final UnicodeBlock KANGXI_RADICALS = new UnicodeBlock("KANGXI_RADICALS", new String[] { "KANGXI RADICALS", "KANGXIRADICALS" });
/*      */ 
/* 1350 */     public static final UnicodeBlock IDEOGRAPHIC_DESCRIPTION_CHARACTERS = new UnicodeBlock("IDEOGRAPHIC_DESCRIPTION_CHARACTERS", new String[] { "IDEOGRAPHIC DESCRIPTION CHARACTERS", "IDEOGRAPHICDESCRIPTIONCHARACTERS" });
/*      */ 
/* 1359 */     public static final UnicodeBlock BOPOMOFO_EXTENDED = new UnicodeBlock("BOPOMOFO_EXTENDED", new String[] { "BOPOMOFO EXTENDED", "BOPOMOFOEXTENDED" });
/*      */ 
/* 1368 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A", new String[] { "CJK UNIFIED IDEOGRAPHS EXTENSION A", "CJKUNIFIEDIDEOGRAPHSEXTENSIONA" });
/*      */ 
/* 1377 */     public static final UnicodeBlock YI_SYLLABLES = new UnicodeBlock("YI_SYLLABLES", new String[] { "YI SYLLABLES", "YISYLLABLES" });
/*      */ 
/* 1386 */     public static final UnicodeBlock YI_RADICALS = new UnicodeBlock("YI_RADICALS", new String[] { "YI RADICALS", "YIRADICALS" });
/*      */ 
/* 1395 */     public static final UnicodeBlock CYRILLIC_SUPPLEMENTARY = new UnicodeBlock("CYRILLIC_SUPPLEMENTARY", new String[] { "CYRILLIC SUPPLEMENTARY", "CYRILLICSUPPLEMENTARY", "CYRILLIC SUPPLEMENT", "CYRILLICSUPPLEMENT" });
/*      */ 
/* 1406 */     public static final UnicodeBlock TAGALOG = new UnicodeBlock("TAGALOG");
/*      */ 
/* 1413 */     public static final UnicodeBlock HANUNOO = new UnicodeBlock("HANUNOO");
/*      */ 
/* 1420 */     public static final UnicodeBlock BUHID = new UnicodeBlock("BUHID");
/*      */ 
/* 1427 */     public static final UnicodeBlock TAGBANWA = new UnicodeBlock("TAGBANWA");
/*      */ 
/* 1434 */     public static final UnicodeBlock LIMBU = new UnicodeBlock("LIMBU");
/*      */ 
/* 1441 */     public static final UnicodeBlock TAI_LE = new UnicodeBlock("TAI_LE", new String[] { "TAI LE", "TAILE" });
/*      */ 
/* 1450 */     public static final UnicodeBlock KHMER_SYMBOLS = new UnicodeBlock("KHMER_SYMBOLS", new String[] { "KHMER SYMBOLS", "KHMERSYMBOLS" });
/*      */ 
/* 1459 */     public static final UnicodeBlock PHONETIC_EXTENSIONS = new UnicodeBlock("PHONETIC_EXTENSIONS", new String[] { "PHONETIC EXTENSIONS", "PHONETICEXTENSIONS" });
/*      */ 
/* 1468 */     public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A", new String[] { "MISCELLANEOUS MATHEMATICAL SYMBOLS-A", "MISCELLANEOUSMATHEMATICALSYMBOLS-A" });
/*      */ 
/* 1477 */     public static final UnicodeBlock SUPPLEMENTAL_ARROWS_A = new UnicodeBlock("SUPPLEMENTAL_ARROWS_A", new String[] { "SUPPLEMENTAL ARROWS-A", "SUPPLEMENTALARROWS-A" });
/*      */ 
/* 1486 */     public static final UnicodeBlock SUPPLEMENTAL_ARROWS_B = new UnicodeBlock("SUPPLEMENTAL_ARROWS_B", new String[] { "SUPPLEMENTAL ARROWS-B", "SUPPLEMENTALARROWS-B" });
/*      */ 
/* 1496 */     public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B", new String[] { "MISCELLANEOUS MATHEMATICAL SYMBOLS-B", "MISCELLANEOUSMATHEMATICALSYMBOLS-B" });
/*      */ 
/* 1506 */     public static final UnicodeBlock SUPPLEMENTAL_MATHEMATICAL_OPERATORS = new UnicodeBlock("SUPPLEMENTAL_MATHEMATICAL_OPERATORS", new String[] { "SUPPLEMENTAL MATHEMATICAL OPERATORS", "SUPPLEMENTALMATHEMATICALOPERATORS" });
/*      */ 
/* 1516 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_ARROWS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_ARROWS", new String[] { "MISCELLANEOUS SYMBOLS AND ARROWS", "MISCELLANEOUSSYMBOLSANDARROWS" });
/*      */ 
/* 1526 */     public static final UnicodeBlock KATAKANA_PHONETIC_EXTENSIONS = new UnicodeBlock("KATAKANA_PHONETIC_EXTENSIONS", new String[] { "KATAKANA PHONETIC EXTENSIONS", "KATAKANAPHONETICEXTENSIONS" });
/*      */ 
/* 1535 */     public static final UnicodeBlock YIJING_HEXAGRAM_SYMBOLS = new UnicodeBlock("YIJING_HEXAGRAM_SYMBOLS", new String[] { "YIJING HEXAGRAM SYMBOLS", "YIJINGHEXAGRAMSYMBOLS" });
/*      */ 
/* 1544 */     public static final UnicodeBlock VARIATION_SELECTORS = new UnicodeBlock("VARIATION_SELECTORS", new String[] { "VARIATION SELECTORS", "VARIATIONSELECTORS" });
/*      */ 
/* 1553 */     public static final UnicodeBlock LINEAR_B_SYLLABARY = new UnicodeBlock("LINEAR_B_SYLLABARY", new String[] { "LINEAR B SYLLABARY", "LINEARBSYLLABARY" });
/*      */ 
/* 1562 */     public static final UnicodeBlock LINEAR_B_IDEOGRAMS = new UnicodeBlock("LINEAR_B_IDEOGRAMS", new String[] { "LINEAR B IDEOGRAMS", "LINEARBIDEOGRAMS" });
/*      */ 
/* 1571 */     public static final UnicodeBlock AEGEAN_NUMBERS = new UnicodeBlock("AEGEAN_NUMBERS", new String[] { "AEGEAN NUMBERS", "AEGEANNUMBERS" });
/*      */ 
/* 1580 */     public static final UnicodeBlock OLD_ITALIC = new UnicodeBlock("OLD_ITALIC", new String[] { "OLD ITALIC", "OLDITALIC" });
/*      */ 
/* 1589 */     public static final UnicodeBlock GOTHIC = new UnicodeBlock("GOTHIC");
/*      */ 
/* 1596 */     public static final UnicodeBlock UGARITIC = new UnicodeBlock("UGARITIC");
/*      */ 
/* 1603 */     public static final UnicodeBlock DESERET = new UnicodeBlock("DESERET");
/*      */ 
/* 1610 */     public static final UnicodeBlock SHAVIAN = new UnicodeBlock("SHAVIAN");
/*      */ 
/* 1617 */     public static final UnicodeBlock OSMANYA = new UnicodeBlock("OSMANYA");
/*      */ 
/* 1624 */     public static final UnicodeBlock CYPRIOT_SYLLABARY = new UnicodeBlock("CYPRIOT_SYLLABARY", new String[] { "CYPRIOT SYLLABARY", "CYPRIOTSYLLABARY" });
/*      */ 
/* 1633 */     public static final UnicodeBlock BYZANTINE_MUSICAL_SYMBOLS = new UnicodeBlock("BYZANTINE_MUSICAL_SYMBOLS", new String[] { "BYZANTINE MUSICAL SYMBOLS", "BYZANTINEMUSICALSYMBOLS" });
/*      */ 
/* 1642 */     public static final UnicodeBlock MUSICAL_SYMBOLS = new UnicodeBlock("MUSICAL_SYMBOLS", new String[] { "MUSICAL SYMBOLS", "MUSICALSYMBOLS" });
/*      */ 
/* 1651 */     public static final UnicodeBlock TAI_XUAN_JING_SYMBOLS = new UnicodeBlock("TAI_XUAN_JING_SYMBOLS", new String[] { "TAI XUAN JING SYMBOLS", "TAIXUANJINGSYMBOLS" });
/*      */ 
/* 1661 */     public static final UnicodeBlock MATHEMATICAL_ALPHANUMERIC_SYMBOLS = new UnicodeBlock("MATHEMATICAL_ALPHANUMERIC_SYMBOLS", new String[] { "MATHEMATICAL ALPHANUMERIC SYMBOLS", "MATHEMATICALALPHANUMERICSYMBOLS" });
/*      */ 
/* 1671 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B", new String[] { "CJK UNIFIED IDEOGRAPHS EXTENSION B", "CJKUNIFIEDIDEOGRAPHSEXTENSIONB" });
/*      */ 
/* 1680 */     public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT", new String[] { "CJK COMPATIBILITY IDEOGRAPHS SUPPLEMENT", "CJKCOMPATIBILITYIDEOGRAPHSSUPPLEMENT" });
/*      */ 
/* 1689 */     public static final UnicodeBlock TAGS = new UnicodeBlock("TAGS");
/*      */ 
/* 1697 */     public static final UnicodeBlock VARIATION_SELECTORS_SUPPLEMENT = new UnicodeBlock("VARIATION_SELECTORS_SUPPLEMENT", new String[] { "VARIATION SELECTORS SUPPLEMENT", "VARIATIONSELECTORSSUPPLEMENT" });
/*      */ 
/* 1707 */     public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_A = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_A", new String[] { "SUPPLEMENTARY PRIVATE USE AREA-A", "SUPPLEMENTARYPRIVATEUSEAREA-A" });
/*      */ 
/* 1717 */     public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_B = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_B", new String[] { "SUPPLEMENTARY PRIVATE USE AREA-B", "SUPPLEMENTARYPRIVATEUSEAREA-B" });
/*      */ 
/* 1729 */     public static final UnicodeBlock HIGH_SURROGATES = new UnicodeBlock("HIGH_SURROGATES", new String[] { "HIGH SURROGATES", "HIGHSURROGATES" });
/*      */ 
/* 1742 */     public static final UnicodeBlock HIGH_PRIVATE_USE_SURROGATES = new UnicodeBlock("HIGH_PRIVATE_USE_SURROGATES", new String[] { "HIGH PRIVATE USE SURROGATES", "HIGHPRIVATEUSESURROGATES" });
/*      */ 
/* 1754 */     public static final UnicodeBlock LOW_SURROGATES = new UnicodeBlock("LOW_SURROGATES", new String[] { "LOW SURROGATES", "LOWSURROGATES" });
/*      */ 
/* 1763 */     public static final UnicodeBlock ARABIC_SUPPLEMENT = new UnicodeBlock("ARABIC_SUPPLEMENT", new String[] { "ARABIC SUPPLEMENT", "ARABICSUPPLEMENT" });
/*      */ 
/* 1772 */     public static final UnicodeBlock NKO = new UnicodeBlock("NKO");
/*      */ 
/* 1779 */     public static final UnicodeBlock SAMARITAN = new UnicodeBlock("SAMARITAN");
/*      */ 
/* 1786 */     public static final UnicodeBlock MANDAIC = new UnicodeBlock("MANDAIC");
/*      */ 
/* 1793 */     public static final UnicodeBlock ETHIOPIC_SUPPLEMENT = new UnicodeBlock("ETHIOPIC_SUPPLEMENT", new String[] { "ETHIOPIC SUPPLEMENT", "ETHIOPICSUPPLEMENT" });
/*      */ 
/* 1803 */     public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED", new String[] { "UNIFIED CANADIAN ABORIGINAL SYLLABICS EXTENDED", "UNIFIEDCANADIANABORIGINALSYLLABICSEXTENDED" });
/*      */ 
/* 1812 */     public static final UnicodeBlock NEW_TAI_LUE = new UnicodeBlock("NEW_TAI_LUE", new String[] { "NEW TAI LUE", "NEWTAILUE" });
/*      */ 
/* 1821 */     public static final UnicodeBlock BUGINESE = new UnicodeBlock("BUGINESE");
/*      */ 
/* 1828 */     public static final UnicodeBlock TAI_THAM = new UnicodeBlock("TAI_THAM", new String[] { "TAI THAM", "TAITHAM" });
/*      */ 
/* 1837 */     public static final UnicodeBlock BALINESE = new UnicodeBlock("BALINESE");
/*      */ 
/* 1844 */     public static final UnicodeBlock SUNDANESE = new UnicodeBlock("SUNDANESE");
/*      */ 
/* 1851 */     public static final UnicodeBlock BATAK = new UnicodeBlock("BATAK");
/*      */ 
/* 1858 */     public static final UnicodeBlock LEPCHA = new UnicodeBlock("LEPCHA");
/*      */ 
/* 1865 */     public static final UnicodeBlock OL_CHIKI = new UnicodeBlock("OL_CHIKI", new String[] { "OL CHIKI", "OLCHIKI" });
/*      */ 
/* 1874 */     public static final UnicodeBlock VEDIC_EXTENSIONS = new UnicodeBlock("VEDIC_EXTENSIONS", new String[] { "VEDIC EXTENSIONS", "VEDICEXTENSIONS" });
/*      */ 
/* 1884 */     public static final UnicodeBlock PHONETIC_EXTENSIONS_SUPPLEMENT = new UnicodeBlock("PHONETIC_EXTENSIONS_SUPPLEMENT", new String[] { "PHONETIC EXTENSIONS SUPPLEMENT", "PHONETICEXTENSIONSSUPPLEMENT" });
/*      */ 
/* 1894 */     public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS_SUPPLEMENT = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS_SUPPLEMENT", new String[] { "COMBINING DIACRITICAL MARKS SUPPLEMENT", "COMBININGDIACRITICALMARKSSUPPLEMENT" });
/*      */ 
/* 1903 */     public static final UnicodeBlock GLAGOLITIC = new UnicodeBlock("GLAGOLITIC");
/*      */ 
/* 1910 */     public static final UnicodeBlock LATIN_EXTENDED_C = new UnicodeBlock("LATIN_EXTENDED_C", new String[] { "LATIN EXTENDED-C", "LATINEXTENDED-C" });
/*      */ 
/* 1919 */     public static final UnicodeBlock COPTIC = new UnicodeBlock("COPTIC");
/*      */ 
/* 1926 */     public static final UnicodeBlock GEORGIAN_SUPPLEMENT = new UnicodeBlock("GEORGIAN_SUPPLEMENT", new String[] { "GEORGIAN SUPPLEMENT", "GEORGIANSUPPLEMENT" });
/*      */ 
/* 1935 */     public static final UnicodeBlock TIFINAGH = new UnicodeBlock("TIFINAGH");
/*      */ 
/* 1942 */     public static final UnicodeBlock ETHIOPIC_EXTENDED = new UnicodeBlock("ETHIOPIC_EXTENDED", new String[] { "ETHIOPIC EXTENDED", "ETHIOPICEXTENDED" });
/*      */ 
/* 1951 */     public static final UnicodeBlock CYRILLIC_EXTENDED_A = new UnicodeBlock("CYRILLIC_EXTENDED_A", new String[] { "CYRILLIC EXTENDED-A", "CYRILLICEXTENDED-A" });
/*      */ 
/* 1960 */     public static final UnicodeBlock SUPPLEMENTAL_PUNCTUATION = new UnicodeBlock("SUPPLEMENTAL_PUNCTUATION", new String[] { "SUPPLEMENTAL PUNCTUATION", "SUPPLEMENTALPUNCTUATION" });
/*      */ 
/* 1969 */     public static final UnicodeBlock CJK_STROKES = new UnicodeBlock("CJK_STROKES", new String[] { "CJK STROKES", "CJKSTROKES" });
/*      */ 
/* 1978 */     public static final UnicodeBlock LISU = new UnicodeBlock("LISU");
/*      */ 
/* 1985 */     public static final UnicodeBlock VAI = new UnicodeBlock("VAI");
/*      */ 
/* 1992 */     public static final UnicodeBlock CYRILLIC_EXTENDED_B = new UnicodeBlock("CYRILLIC_EXTENDED_B", new String[] { "CYRILLIC EXTENDED-B", "CYRILLICEXTENDED-B" });
/*      */ 
/* 2001 */     public static final UnicodeBlock BAMUM = new UnicodeBlock("BAMUM");
/*      */ 
/* 2008 */     public static final UnicodeBlock MODIFIER_TONE_LETTERS = new UnicodeBlock("MODIFIER_TONE_LETTERS", new String[] { "MODIFIER TONE LETTERS", "MODIFIERTONELETTERS" });
/*      */ 
/* 2017 */     public static final UnicodeBlock LATIN_EXTENDED_D = new UnicodeBlock("LATIN_EXTENDED_D", new String[] { "LATIN EXTENDED-D", "LATINEXTENDED-D" });
/*      */ 
/* 2026 */     public static final UnicodeBlock SYLOTI_NAGRI = new UnicodeBlock("SYLOTI_NAGRI", new String[] { "SYLOTI NAGRI", "SYLOTINAGRI" });
/*      */ 
/* 2035 */     public static final UnicodeBlock COMMON_INDIC_NUMBER_FORMS = new UnicodeBlock("COMMON_INDIC_NUMBER_FORMS", new String[] { "COMMON INDIC NUMBER FORMS", "COMMONINDICNUMBERFORMS" });
/*      */ 
/* 2044 */     public static final UnicodeBlock PHAGS_PA = new UnicodeBlock("PHAGS_PA", "PHAGS-PA");
/*      */ 
/* 2052 */     public static final UnicodeBlock SAURASHTRA = new UnicodeBlock("SAURASHTRA");
/*      */ 
/* 2059 */     public static final UnicodeBlock DEVANAGARI_EXTENDED = new UnicodeBlock("DEVANAGARI_EXTENDED", new String[] { "DEVANAGARI EXTENDED", "DEVANAGARIEXTENDED" });
/*      */ 
/* 2068 */     public static final UnicodeBlock KAYAH_LI = new UnicodeBlock("KAYAH_LI", new String[] { "KAYAH LI", "KAYAHLI" });
/*      */ 
/* 2077 */     public static final UnicodeBlock REJANG = new UnicodeBlock("REJANG");
/*      */ 
/* 2084 */     public static final UnicodeBlock HANGUL_JAMO_EXTENDED_A = new UnicodeBlock("HANGUL_JAMO_EXTENDED_A", new String[] { "HANGUL JAMO EXTENDED-A", "HANGULJAMOEXTENDED-A" });
/*      */ 
/* 2093 */     public static final UnicodeBlock JAVANESE = new UnicodeBlock("JAVANESE");
/*      */ 
/* 2100 */     public static final UnicodeBlock CHAM = new UnicodeBlock("CHAM");
/*      */ 
/* 2107 */     public static final UnicodeBlock MYANMAR_EXTENDED_A = new UnicodeBlock("MYANMAR_EXTENDED_A", new String[] { "MYANMAR EXTENDED-A", "MYANMAREXTENDED-A" });
/*      */ 
/* 2116 */     public static final UnicodeBlock TAI_VIET = new UnicodeBlock("TAI_VIET", new String[] { "TAI VIET", "TAIVIET" });
/*      */ 
/* 2125 */     public static final UnicodeBlock ETHIOPIC_EXTENDED_A = new UnicodeBlock("ETHIOPIC_EXTENDED_A", new String[] { "ETHIOPIC EXTENDED-A", "ETHIOPICEXTENDED-A" });
/*      */ 
/* 2134 */     public static final UnicodeBlock MEETEI_MAYEK = new UnicodeBlock("MEETEI_MAYEK", new String[] { "MEETEI MAYEK", "MEETEIMAYEK" });
/*      */ 
/* 2143 */     public static final UnicodeBlock HANGUL_JAMO_EXTENDED_B = new UnicodeBlock("HANGUL_JAMO_EXTENDED_B", new String[] { "HANGUL JAMO EXTENDED-B", "HANGULJAMOEXTENDED-B" });
/*      */ 
/* 2152 */     public static final UnicodeBlock VERTICAL_FORMS = new UnicodeBlock("VERTICAL_FORMS", new String[] { "VERTICAL FORMS", "VERTICALFORMS" });
/*      */ 
/* 2161 */     public static final UnicodeBlock ANCIENT_GREEK_NUMBERS = new UnicodeBlock("ANCIENT_GREEK_NUMBERS", new String[] { "ANCIENT GREEK NUMBERS", "ANCIENTGREEKNUMBERS" });
/*      */ 
/* 2170 */     public static final UnicodeBlock ANCIENT_SYMBOLS = new UnicodeBlock("ANCIENT_SYMBOLS", new String[] { "ANCIENT SYMBOLS", "ANCIENTSYMBOLS" });
/*      */ 
/* 2179 */     public static final UnicodeBlock PHAISTOS_DISC = new UnicodeBlock("PHAISTOS_DISC", new String[] { "PHAISTOS DISC", "PHAISTOSDISC" });
/*      */ 
/* 2188 */     public static final UnicodeBlock LYCIAN = new UnicodeBlock("LYCIAN");
/*      */ 
/* 2195 */     public static final UnicodeBlock CARIAN = new UnicodeBlock("CARIAN");
/*      */ 
/* 2202 */     public static final UnicodeBlock OLD_PERSIAN = new UnicodeBlock("OLD_PERSIAN", new String[] { "OLD PERSIAN", "OLDPERSIAN" });
/*      */ 
/* 2211 */     public static final UnicodeBlock IMPERIAL_ARAMAIC = new UnicodeBlock("IMPERIAL_ARAMAIC", new String[] { "IMPERIAL ARAMAIC", "IMPERIALARAMAIC" });
/*      */ 
/* 2220 */     public static final UnicodeBlock PHOENICIAN = new UnicodeBlock("PHOENICIAN");
/*      */ 
/* 2227 */     public static final UnicodeBlock LYDIAN = new UnicodeBlock("LYDIAN");
/*      */ 
/* 2234 */     public static final UnicodeBlock KHAROSHTHI = new UnicodeBlock("KHAROSHTHI");
/*      */ 
/* 2241 */     public static final UnicodeBlock OLD_SOUTH_ARABIAN = new UnicodeBlock("OLD_SOUTH_ARABIAN", new String[] { "OLD SOUTH ARABIAN", "OLDSOUTHARABIAN" });
/*      */ 
/* 2250 */     public static final UnicodeBlock AVESTAN = new UnicodeBlock("AVESTAN");
/*      */ 
/* 2257 */     public static final UnicodeBlock INSCRIPTIONAL_PARTHIAN = new UnicodeBlock("INSCRIPTIONAL_PARTHIAN", new String[] { "INSCRIPTIONAL PARTHIAN", "INSCRIPTIONALPARTHIAN" });
/*      */ 
/* 2266 */     public static final UnicodeBlock INSCRIPTIONAL_PAHLAVI = new UnicodeBlock("INSCRIPTIONAL_PAHLAVI", new String[] { "INSCRIPTIONAL PAHLAVI", "INSCRIPTIONALPAHLAVI" });
/*      */ 
/* 2275 */     public static final UnicodeBlock OLD_TURKIC = new UnicodeBlock("OLD_TURKIC", new String[] { "OLD TURKIC", "OLDTURKIC" });
/*      */ 
/* 2284 */     public static final UnicodeBlock RUMI_NUMERAL_SYMBOLS = new UnicodeBlock("RUMI_NUMERAL_SYMBOLS", new String[] { "RUMI NUMERAL SYMBOLS", "RUMINUMERALSYMBOLS" });
/*      */ 
/* 2293 */     public static final UnicodeBlock BRAHMI = new UnicodeBlock("BRAHMI");
/*      */ 
/* 2300 */     public static final UnicodeBlock KAITHI = new UnicodeBlock("KAITHI");
/*      */ 
/* 2307 */     public static final UnicodeBlock CUNEIFORM = new UnicodeBlock("CUNEIFORM");
/*      */ 
/* 2315 */     public static final UnicodeBlock CUNEIFORM_NUMBERS_AND_PUNCTUATION = new UnicodeBlock("CUNEIFORM_NUMBERS_AND_PUNCTUATION", new String[] { "CUNEIFORM NUMBERS AND PUNCTUATION", "CUNEIFORMNUMBERSANDPUNCTUATION" });
/*      */ 
/* 2324 */     public static final UnicodeBlock EGYPTIAN_HIEROGLYPHS = new UnicodeBlock("EGYPTIAN_HIEROGLYPHS", new String[] { "EGYPTIAN HIEROGLYPHS", "EGYPTIANHIEROGLYPHS" });
/*      */ 
/* 2333 */     public static final UnicodeBlock BAMUM_SUPPLEMENT = new UnicodeBlock("BAMUM_SUPPLEMENT", new String[] { "BAMUM SUPPLEMENT", "BAMUMSUPPLEMENT" });
/*      */ 
/* 2342 */     public static final UnicodeBlock KANA_SUPPLEMENT = new UnicodeBlock("KANA_SUPPLEMENT", new String[] { "KANA SUPPLEMENT", "KANASUPPLEMENT" });
/*      */ 
/* 2352 */     public static final UnicodeBlock ANCIENT_GREEK_MUSICAL_NOTATION = new UnicodeBlock("ANCIENT_GREEK_MUSICAL_NOTATION", new String[] { "ANCIENT GREEK MUSICAL NOTATION", "ANCIENTGREEKMUSICALNOTATION" });
/*      */ 
/* 2361 */     public static final UnicodeBlock COUNTING_ROD_NUMERALS = new UnicodeBlock("COUNTING_ROD_NUMERALS", new String[] { "COUNTING ROD NUMERALS", "COUNTINGRODNUMERALS" });
/*      */ 
/* 2370 */     public static final UnicodeBlock MAHJONG_TILES = new UnicodeBlock("MAHJONG_TILES", new String[] { "MAHJONG TILES", "MAHJONGTILES" });
/*      */ 
/* 2379 */     public static final UnicodeBlock DOMINO_TILES = new UnicodeBlock("DOMINO_TILES", new String[] { "DOMINO TILES", "DOMINOTILES" });
/*      */ 
/* 2388 */     public static final UnicodeBlock PLAYING_CARDS = new UnicodeBlock("PLAYING_CARDS", new String[] { "PLAYING CARDS", "PLAYINGCARDS" });
/*      */ 
/* 2398 */     public static final UnicodeBlock ENCLOSED_ALPHANUMERIC_SUPPLEMENT = new UnicodeBlock("ENCLOSED_ALPHANUMERIC_SUPPLEMENT", new String[] { "ENCLOSED ALPHANUMERIC SUPPLEMENT", "ENCLOSEDALPHANUMERICSUPPLEMENT" });
/*      */ 
/* 2408 */     public static final UnicodeBlock ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = new UnicodeBlock("ENCLOSED_IDEOGRAPHIC_SUPPLEMENT", new String[] { "ENCLOSED IDEOGRAPHIC SUPPLEMENT", "ENCLOSEDIDEOGRAPHICSUPPLEMENT" });
/*      */ 
/* 2418 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", new String[] { "MISCELLANEOUS SYMBOLS AND PICTOGRAPHS", "MISCELLANEOUSSYMBOLSANDPICTOGRAPHS" });
/*      */ 
/* 2427 */     public static final UnicodeBlock EMOTICONS = new UnicodeBlock("EMOTICONS");
/*      */ 
/* 2434 */     public static final UnicodeBlock TRANSPORT_AND_MAP_SYMBOLS = new UnicodeBlock("TRANSPORT_AND_MAP_SYMBOLS", new String[] { "TRANSPORT AND MAP SYMBOLS", "TRANSPORTANDMAPSYMBOLS" });
/*      */ 
/* 2443 */     public static final UnicodeBlock ALCHEMICAL_SYMBOLS = new UnicodeBlock("ALCHEMICAL_SYMBOLS", new String[] { "ALCHEMICAL SYMBOLS", "ALCHEMICALSYMBOLS" });
/*      */ 
/* 2453 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C", new String[] { "CJK UNIFIED IDEOGRAPHS EXTENSION C", "CJKUNIFIEDIDEOGRAPHSEXTENSIONC" });
/*      */ 
/* 2463 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D", new String[] { "CJK UNIFIED IDEOGRAPHS EXTENSION D", "CJKUNIFIEDIDEOGRAPHSEXTENSIOND" });
/*      */ 
/* 2468 */     private static final int[] blockStarts = { 0, 128, 256, 384, 592, 688, 768, 880, 1024, 1280, 1328, 1424, 1536, 1792, 1872, 1920, 1984, 2048, 2112, 2144, 2304, 2432, 2560, 2688, 2816, 2944, 3072, 3200, 3328, 3456, 3584, 3712, 3840, 4096, 4256, 4352, 4608, 4992, 5024, 5120, 5760, 5792, 5888, 5920, 5952, 5984, 6016, 6144, 6320, 6400, 6480, 6528, 6624, 6656, 6688, 6832, 6912, 7040, 7104, 7168, 7248, 7296, 7376, 7424, 7552, 7616, 7680, 7936, 8192, 8304, 8352, 8400, 8448, 8528, 8592, 8704, 8960, 9216, 9280, 9312, 9472, 9600, 9632, 9728, 9984, 10176, 10224, 10240, 10496, 10624, 10752, 11008, 11264, 11360, 11392, 11520, 11568, 11648, 11744, 11776, 11904, 12032, 12256, 12272, 12288, 12352, 12448, 12544, 12592, 12688, 12704, 12736, 12784, 12800, 13056, 13312, 19904, 19968, 40960, 42128, 42192, 42240, 42560, 42656, 42752, 42784, 43008, 43056, 43072, 43136, 43232, 43264, 43312, 43360, 43392, 43488, 43520, 43616, 43648, 43744, 43776, 43824, 43968, 44032, 55216, 55296, 56192, 56320, 57344, 63744, 64256, 64336, 65024, 65040, 65056, 65072, 65104, 65136, 65280, 65520, 65536, 65664, 65792, 65856, 65936, 66000, 66048, 66176, 66208, 66272, 66304, 66352, 66384, 66432, 66464, 66528, 66560, 66640, 66688, 66736, 67584, 67648, 67680, 67840, 67872, 67904, 68096, 68192, 68224, 68352, 68416, 68448, 68480, 68608, 68688, 69216, 69248, 69632, 69760, 69840, 73728, 74752, 74880, 77824, 78896, 92160, 92736, 110592, 110848, 118784, 119040, 119296, 119376, 119552, 119648, 119680, 119808, 120832, 126976, 127024, 127136, 127232, 127488, 127744, 128512, 128592, 128640, 128768, 128896, 131072, 173792, 173824, 177984, 178208, 194560, 195104, 917504, 917632, 917760, 918000, 983040, 1048576 };
/*      */ 
/* 2713 */     private static final UnicodeBlock[] blocks = { BASIC_LATIN, LATIN_1_SUPPLEMENT, LATIN_EXTENDED_A, LATIN_EXTENDED_B, IPA_EXTENSIONS, SPACING_MODIFIER_LETTERS, COMBINING_DIACRITICAL_MARKS, GREEK, CYRILLIC, CYRILLIC_SUPPLEMENTARY, ARMENIAN, HEBREW, ARABIC, SYRIAC, ARABIC_SUPPLEMENT, THAANA, NKO, SAMARITAN, MANDAIC, null, DEVANAGARI, BENGALI, GURMUKHI, GUJARATI, ORIYA, TAMIL, TELUGU, KANNADA, MALAYALAM, SINHALA, THAI, LAO, TIBETAN, MYANMAR, GEORGIAN, HANGUL_JAMO, ETHIOPIC, ETHIOPIC_SUPPLEMENT, CHEROKEE, UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS, OGHAM, RUNIC, TAGALOG, HANUNOO, BUHID, TAGBANWA, KHMER, MONGOLIAN, UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED, LIMBU, TAI_LE, NEW_TAI_LUE, KHMER_SYMBOLS, BUGINESE, TAI_THAM, null, BALINESE, SUNDANESE, BATAK, LEPCHA, OL_CHIKI, null, VEDIC_EXTENSIONS, PHONETIC_EXTENSIONS, PHONETIC_EXTENSIONS_SUPPLEMENT, COMBINING_DIACRITICAL_MARKS_SUPPLEMENT, LATIN_EXTENDED_ADDITIONAL, GREEK_EXTENDED, GENERAL_PUNCTUATION, SUPERSCRIPTS_AND_SUBSCRIPTS, CURRENCY_SYMBOLS, COMBINING_MARKS_FOR_SYMBOLS, LETTERLIKE_SYMBOLS, NUMBER_FORMS, ARROWS, MATHEMATICAL_OPERATORS, MISCELLANEOUS_TECHNICAL, CONTROL_PICTURES, OPTICAL_CHARACTER_RECOGNITION, ENCLOSED_ALPHANUMERICS, BOX_DRAWING, BLOCK_ELEMENTS, GEOMETRIC_SHAPES, MISCELLANEOUS_SYMBOLS, DINGBATS, MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A, SUPPLEMENTAL_ARROWS_A, BRAILLE_PATTERNS, SUPPLEMENTAL_ARROWS_B, MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B, SUPPLEMENTAL_MATHEMATICAL_OPERATORS, MISCELLANEOUS_SYMBOLS_AND_ARROWS, GLAGOLITIC, LATIN_EXTENDED_C, COPTIC, GEORGIAN_SUPPLEMENT, TIFINAGH, ETHIOPIC_EXTENDED, CYRILLIC_EXTENDED_A, SUPPLEMENTAL_PUNCTUATION, CJK_RADICALS_SUPPLEMENT, KANGXI_RADICALS, null, IDEOGRAPHIC_DESCRIPTION_CHARACTERS, CJK_SYMBOLS_AND_PUNCTUATION, HIRAGANA, KATAKANA, BOPOMOFO, HANGUL_COMPATIBILITY_JAMO, KANBUN, BOPOMOFO_EXTENDED, CJK_STROKES, KATAKANA_PHONETIC_EXTENSIONS, ENCLOSED_CJK_LETTERS_AND_MONTHS, CJK_COMPATIBILITY, CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A, YIJING_HEXAGRAM_SYMBOLS, CJK_UNIFIED_IDEOGRAPHS, YI_SYLLABLES, YI_RADICALS, LISU, VAI, CYRILLIC_EXTENDED_B, BAMUM, MODIFIER_TONE_LETTERS, LATIN_EXTENDED_D, SYLOTI_NAGRI, COMMON_INDIC_NUMBER_FORMS, PHAGS_PA, SAURASHTRA, DEVANAGARI_EXTENDED, KAYAH_LI, REJANG, HANGUL_JAMO_EXTENDED_A, JAVANESE, null, CHAM, MYANMAR_EXTENDED_A, TAI_VIET, null, ETHIOPIC_EXTENDED_A, null, MEETEI_MAYEK, HANGUL_SYLLABLES, HANGUL_JAMO_EXTENDED_B, HIGH_SURROGATES, HIGH_PRIVATE_USE_SURROGATES, LOW_SURROGATES, PRIVATE_USE_AREA, CJK_COMPATIBILITY_IDEOGRAPHS, ALPHABETIC_PRESENTATION_FORMS, ARABIC_PRESENTATION_FORMS_A, VARIATION_SELECTORS, VERTICAL_FORMS, COMBINING_HALF_MARKS, CJK_COMPATIBILITY_FORMS, SMALL_FORM_VARIANTS, ARABIC_PRESENTATION_FORMS_B, HALFWIDTH_AND_FULLWIDTH_FORMS, SPECIALS, LINEAR_B_SYLLABARY, LINEAR_B_IDEOGRAMS, AEGEAN_NUMBERS, ANCIENT_GREEK_NUMBERS, ANCIENT_SYMBOLS, PHAISTOS_DISC, null, LYCIAN, CARIAN, null, OLD_ITALIC, GOTHIC, null, UGARITIC, OLD_PERSIAN, null, DESERET, SHAVIAN, OSMANYA, null, CYPRIOT_SYLLABARY, IMPERIAL_ARAMAIC, null, PHOENICIAN, LYDIAN, null, KHAROSHTHI, OLD_SOUTH_ARABIAN, null, AVESTAN, INSCRIPTIONAL_PARTHIAN, INSCRIPTIONAL_PAHLAVI, null, OLD_TURKIC, null, RUMI_NUMERAL_SYMBOLS, null, BRAHMI, KAITHI, null, CUNEIFORM, CUNEIFORM_NUMBERS_AND_PUNCTUATION, null, EGYPTIAN_HIEROGLYPHS, null, BAMUM_SUPPLEMENT, null, KANA_SUPPLEMENT, null, BYZANTINE_MUSICAL_SYMBOLS, MUSICAL_SYMBOLS, ANCIENT_GREEK_MUSICAL_NOTATION, null, TAI_XUAN_JING_SYMBOLS, COUNTING_ROD_NUMERALS, null, MATHEMATICAL_ALPHANUMERIC_SYMBOLS, null, MAHJONG_TILES, DOMINO_TILES, PLAYING_CARDS, ENCLOSED_ALPHANUMERIC_SUPPLEMENT, ENCLOSED_IDEOGRAPHIC_SUPPLEMENT, MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS, EMOTICONS, null, TRANSPORT_AND_MAP_SYMBOLS, ALCHEMICAL_SYMBOLS, null, CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B, null, CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C, CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D, null, CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT, null, TAGS, null, VARIATION_SELECTORS_SUPPLEMENT, null, SUPPLEMENTARY_PRIVATE_USE_AREA_A, SUPPLEMENTARY_PRIVATE_USE_AREA_B };
/*      */ 
/*      */     private UnicodeBlock(String paramString)
/*      */     {
/*  656 */       super();
/*  657 */       map.put(paramString, this);
/*      */     }
/*      */ 
/*      */     private UnicodeBlock(String paramString1, String paramString2)
/*      */     {
/*  665 */       this(paramString1);
/*  666 */       map.put(paramString2, this);
/*      */     }
/*      */ 
/*      */     private UnicodeBlock(String paramString, String[] paramArrayOfString)
/*      */     {
/*  674 */       this(paramString);
/*  675 */       for (String str : paramArrayOfString)
/*  676 */         map.put(str, this);
/*      */     }
/*      */ 
/*      */     public static UnicodeBlock of(char paramChar)
/*      */     {
/* 2976 */       return of(paramChar);
/*      */     }
/*      */ 
/*      */     public static UnicodeBlock of(int paramInt)
/*      */     {
/* 2996 */       if (!Character.isValidCodePoint(paramInt)) {
/* 2997 */         throw new IllegalArgumentException();
/*      */       }
/*      */ 
/* 3001 */       int j = 0;
/* 3002 */       int i = blockStarts.length;
/* 3003 */       int k = i / 2;
/*      */ 
/* 3006 */       while (i - j > 1) {
/* 3007 */         if (paramInt >= blockStarts[k])
/* 3008 */           j = k;
/*      */         else {
/* 3010 */           i = k;
/*      */         }
/* 3012 */         k = (i + j) / 2;
/*      */       }
/* 3014 */       return blocks[k];
/*      */     }
/*      */ 
/*      */     public static final UnicodeBlock forName(String paramString)
/*      */     {
/* 3054 */       UnicodeBlock localUnicodeBlock = (UnicodeBlock)map.get(paramString.toUpperCase(Locale.US));
/* 3055 */       if (localUnicodeBlock == null) {
/* 3056 */         throw new IllegalArgumentException();
/*      */       }
/* 3058 */       return localUnicodeBlock;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum UnicodeScript
/*      */   {
/* 3080 */     COMMON, 
/*      */ 
/* 3085 */     LATIN, 
/*      */ 
/* 3090 */     GREEK, 
/*      */ 
/* 3095 */     CYRILLIC, 
/*      */ 
/* 3100 */     ARMENIAN, 
/*      */ 
/* 3105 */     HEBREW, 
/*      */ 
/* 3110 */     ARABIC, 
/*      */ 
/* 3115 */     SYRIAC, 
/*      */ 
/* 3120 */     THAANA, 
/*      */ 
/* 3125 */     DEVANAGARI, 
/*      */ 
/* 3130 */     BENGALI, 
/*      */ 
/* 3135 */     GURMUKHI, 
/*      */ 
/* 3140 */     GUJARATI, 
/*      */ 
/* 3145 */     ORIYA, 
/*      */ 
/* 3150 */     TAMIL, 
/*      */ 
/* 3155 */     TELUGU, 
/*      */ 
/* 3160 */     KANNADA, 
/*      */ 
/* 3165 */     MALAYALAM, 
/*      */ 
/* 3170 */     SINHALA, 
/*      */ 
/* 3175 */     THAI, 
/*      */ 
/* 3180 */     LAO, 
/*      */ 
/* 3185 */     TIBETAN, 
/*      */ 
/* 3190 */     MYANMAR, 
/*      */ 
/* 3195 */     GEORGIAN, 
/*      */ 
/* 3200 */     HANGUL, 
/*      */ 
/* 3205 */     ETHIOPIC, 
/*      */ 
/* 3210 */     CHEROKEE, 
/*      */ 
/* 3215 */     CANADIAN_ABORIGINAL, 
/*      */ 
/* 3220 */     OGHAM, 
/*      */ 
/* 3225 */     RUNIC, 
/*      */ 
/* 3230 */     KHMER, 
/*      */ 
/* 3235 */     MONGOLIAN, 
/*      */ 
/* 3240 */     HIRAGANA, 
/*      */ 
/* 3245 */     KATAKANA, 
/*      */ 
/* 3250 */     BOPOMOFO, 
/*      */ 
/* 3255 */     HAN, 
/*      */ 
/* 3260 */     YI, 
/*      */ 
/* 3265 */     OLD_ITALIC, 
/*      */ 
/* 3270 */     GOTHIC, 
/*      */ 
/* 3275 */     DESERET, 
/*      */ 
/* 3280 */     INHERITED, 
/*      */ 
/* 3285 */     TAGALOG, 
/*      */ 
/* 3290 */     HANUNOO, 
/*      */ 
/* 3295 */     BUHID, 
/*      */ 
/* 3300 */     TAGBANWA, 
/*      */ 
/* 3305 */     LIMBU, 
/*      */ 
/* 3310 */     TAI_LE, 
/*      */ 
/* 3315 */     LINEAR_B, 
/*      */ 
/* 3320 */     UGARITIC, 
/*      */ 
/* 3325 */     SHAVIAN, 
/*      */ 
/* 3330 */     OSMANYA, 
/*      */ 
/* 3335 */     CYPRIOT, 
/*      */ 
/* 3340 */     BRAILLE, 
/*      */ 
/* 3345 */     BUGINESE, 
/*      */ 
/* 3350 */     COPTIC, 
/*      */ 
/* 3355 */     NEW_TAI_LUE, 
/*      */ 
/* 3360 */     GLAGOLITIC, 
/*      */ 
/* 3365 */     TIFINAGH, 
/*      */ 
/* 3370 */     SYLOTI_NAGRI, 
/*      */ 
/* 3375 */     OLD_PERSIAN, 
/*      */ 
/* 3380 */     KHAROSHTHI, 
/*      */ 
/* 3385 */     BALINESE, 
/*      */ 
/* 3390 */     CUNEIFORM, 
/*      */ 
/* 3395 */     PHOENICIAN, 
/*      */ 
/* 3400 */     PHAGS_PA, 
/*      */ 
/* 3405 */     NKO, 
/*      */ 
/* 3410 */     SUNDANESE, 
/*      */ 
/* 3415 */     BATAK, 
/*      */ 
/* 3420 */     LEPCHA, 
/*      */ 
/* 3425 */     OL_CHIKI, 
/*      */ 
/* 3430 */     VAI, 
/*      */ 
/* 3435 */     SAURASHTRA, 
/*      */ 
/* 3440 */     KAYAH_LI, 
/*      */ 
/* 3445 */     REJANG, 
/*      */ 
/* 3450 */     LYCIAN, 
/*      */ 
/* 3455 */     CARIAN, 
/*      */ 
/* 3460 */     LYDIAN, 
/*      */ 
/* 3465 */     CHAM, 
/*      */ 
/* 3470 */     TAI_THAM, 
/*      */ 
/* 3475 */     TAI_VIET, 
/*      */ 
/* 3480 */     AVESTAN, 
/*      */ 
/* 3485 */     EGYPTIAN_HIEROGLYPHS, 
/*      */ 
/* 3490 */     SAMARITAN, 
/*      */ 
/* 3495 */     MANDAIC, 
/*      */ 
/* 3500 */     LISU, 
/*      */ 
/* 3505 */     BAMUM, 
/*      */ 
/* 3510 */     JAVANESE, 
/*      */ 
/* 3515 */     MEETEI_MAYEK, 
/*      */ 
/* 3520 */     IMPERIAL_ARAMAIC, 
/*      */ 
/* 3525 */     OLD_SOUTH_ARABIAN, 
/*      */ 
/* 3530 */     INSCRIPTIONAL_PARTHIAN, 
/*      */ 
/* 3535 */     INSCRIPTIONAL_PAHLAVI, 
/*      */ 
/* 3540 */     OLD_TURKIC, 
/*      */ 
/* 3545 */     BRAHMI, 
/*      */ 
/* 3550 */     KAITHI, 
/*      */ 
/* 3555 */     UNKNOWN;
/*      */ 
/*      */     private static final int[] scriptStarts;
/*      */     private static final UnicodeScript[] scripts;
/*      */     private static HashMap<String, UnicodeScript> aliases;
/*      */ 
/*      */     public static UnicodeScript of(int paramInt)
/*      */     {
/* 4293 */       if (!Character.isValidCodePoint(paramInt))
/* 4294 */         throw new IllegalArgumentException();
/* 4295 */       int i = Character.getType(paramInt);
/*      */ 
/* 4297 */       if (i == 0)
/* 4298 */         return UNKNOWN;
/* 4299 */       int j = Arrays.binarySearch(scriptStarts, paramInt);
/* 4300 */       if (j < 0)
/* 4301 */         j = -j - 2;
/* 4302 */       return scripts[j];
/*      */     }
/*      */ 
/*      */     public static final UnicodeScript forName(String paramString)
/*      */     {
/* 4327 */       paramString = paramString.toUpperCase(Locale.ENGLISH);
/*      */ 
/* 4329 */       UnicodeScript localUnicodeScript = (UnicodeScript)aliases.get(paramString);
/* 4330 */       if (localUnicodeScript != null)
/* 4331 */         return localUnicodeScript;
/* 4332 */       return valueOf(paramString);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 3557 */       scriptStarts = new int[] { 0, 65, 91, 97, 123, 170, 171, 186, 187, 192, 215, 216, 247, 248, 697, 736, 741, 746, 748, 768, 880, 884, 885, 894, 900, 901, 902, 903, 904, 994, 1008, 1024, 1157, 1159, 1329, 1417, 1418, 1425, 1536, 1548, 1549, 1563, 1566, 1567, 1568, 1600, 1601, 1611, 1622, 1631, 1632, 1642, 1648, 1649, 1757, 1758, 1792, 1872, 1920, 1984, 2048, 2112, 2304, 2385, 2387, 2404, 2406, 2416, 2417, 2433, 2561, 2689, 2817, 2946, 3073, 3202, 3330, 3458, 3585, 3647, 3648, 3713, 3840, 4053, 4057, 4096, 4256, 4347, 4348, 4352, 4608, 5024, 5120, 5760, 5792, 5867, 5870, 5888, 5920, 5941, 5952, 5984, 6016, 6144, 6146, 6148, 6149, 6150, 6320, 6400, 6480, 6528, 6624, 6656, 6688, 6912, 7040, 7104, 7168, 7248, 7376, 7379, 7380, 7393, 7394, 7401, 7405, 7406, 7424, 7462, 7467, 7468, 7517, 7522, 7526, 7531, 7544, 7545, 7615, 7616, 7680, 7936, 8192, 8204, 8206, 8305, 8308, 8319, 8320, 8336, 8352, 8400, 8448, 8486, 8487, 8490, 8492, 8498, 8499, 8526, 8527, 8544, 8585, 10240, 10496, 11264, 11360, 11392, 11520, 11568, 11648, 11744, 11776, 11904, 12272, 12293, 12294, 12295, 12296, 12321, 12330, 12334, 12336, 12344, 12348, 12353, 12441, 12443, 12445, 12448, 12449, 12539, 12541, 12549, 12593, 12688, 12704, 12736, 12784, 12800, 12832, 12896, 12927, 13008, 13144, 13312, 19904, 19968, 40960, 42192, 42240, 42560, 42656, 42752, 42786, 42888, 42891, 43008, 43056, 43072, 43136, 43232, 43264, 43312, 43360, 43392, 43520, 43616, 43648, 43777, 43968, 44032, 55292, 63744, 64256, 64275, 64285, 64336, 64830, 64848, 65021, 65024, 65040, 65056, 65072, 65136, 65279, 65313, 65339, 65345, 65371, 65382, 65392, 65393, 65438, 65440, 65504, 65536, 65792, 65856, 65936, 66045, 66176, 66208, 66304, 66352, 66432, 66464, 66560, 66640, 66688, 67584, 67648, 67840, 67872, 68096, 68192, 68352, 68416, 68448, 68608, 69216, 69632, 69760, 73728, 77824, 92160, 110592, 110593, 118784, 119143, 119146, 119163, 119171, 119173, 119180, 119210, 119214, 119296, 119552, 127488, 127489, 131072, 917505, 917760, 918000 };
/*      */ 
/* 3867 */       scripts = new UnicodeScript[] { COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, BOPOMOFO, COMMON, INHERITED, GREEK, COMMON, GREEK, COMMON, GREEK, COMMON, GREEK, COMMON, GREEK, COPTIC, GREEK, CYRILLIC, INHERITED, CYRILLIC, ARMENIAN, COMMON, ARMENIAN, HEBREW, ARABIC, COMMON, ARABIC, COMMON, ARABIC, COMMON, ARABIC, COMMON, ARABIC, INHERITED, ARABIC, INHERITED, COMMON, ARABIC, INHERITED, ARABIC, COMMON, ARABIC, SYRIAC, ARABIC, THAANA, NKO, SAMARITAN, MANDAIC, DEVANAGARI, INHERITED, DEVANAGARI, COMMON, DEVANAGARI, COMMON, DEVANAGARI, BENGALI, GURMUKHI, GUJARATI, ORIYA, TAMIL, TELUGU, KANNADA, MALAYALAM, SINHALA, THAI, COMMON, THAI, LAO, TIBETAN, COMMON, TIBETAN, MYANMAR, GEORGIAN, COMMON, GEORGIAN, HANGUL, ETHIOPIC, CHEROKEE, CANADIAN_ABORIGINAL, OGHAM, RUNIC, COMMON, RUNIC, TAGALOG, HANUNOO, COMMON, BUHID, TAGBANWA, KHMER, MONGOLIAN, COMMON, MONGOLIAN, COMMON, MONGOLIAN, CANADIAN_ABORIGINAL, LIMBU, TAI_LE, NEW_TAI_LUE, KHMER, BUGINESE, TAI_THAM, BALINESE, SUNDANESE, BATAK, LEPCHA, OL_CHIKI, INHERITED, COMMON, INHERITED, COMMON, INHERITED, COMMON, INHERITED, COMMON, LATIN, GREEK, CYRILLIC, LATIN, GREEK, LATIN, GREEK, LATIN, CYRILLIC, LATIN, GREEK, INHERITED, LATIN, GREEK, COMMON, INHERITED, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, INHERITED, COMMON, GREEK, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, LATIN, COMMON, BRAILLE, COMMON, GLAGOLITIC, LATIN, COPTIC, GEORGIAN, TIFINAGH, ETHIOPIC, CYRILLIC, COMMON, HAN, COMMON, HAN, COMMON, HAN, COMMON, HAN, INHERITED, HANGUL, COMMON, HAN, COMMON, HIRAGANA, INHERITED, COMMON, HIRAGANA, COMMON, KATAKANA, COMMON, KATAKANA, BOPOMOFO, HANGUL, COMMON, BOPOMOFO, COMMON, KATAKANA, HANGUL, COMMON, HANGUL, COMMON, KATAKANA, COMMON, HAN, COMMON, HAN, YI, LISU, VAI, CYRILLIC, BAMUM, COMMON, LATIN, COMMON, LATIN, SYLOTI_NAGRI, COMMON, PHAGS_PA, SAURASHTRA, DEVANAGARI, KAYAH_LI, REJANG, HANGUL, JAVANESE, CHAM, MYANMAR, TAI_VIET, ETHIOPIC, MEETEI_MAYEK, HANGUL, UNKNOWN, HAN, LATIN, ARMENIAN, HEBREW, ARABIC, COMMON, ARABIC, COMMON, INHERITED, COMMON, INHERITED, COMMON, ARABIC, COMMON, LATIN, COMMON, LATIN, COMMON, KATAKANA, COMMON, KATAKANA, COMMON, HANGUL, COMMON, LINEAR_B, COMMON, GREEK, COMMON, INHERITED, LYCIAN, CARIAN, OLD_ITALIC, GOTHIC, UGARITIC, OLD_PERSIAN, DESERET, SHAVIAN, OSMANYA, CYPRIOT, IMPERIAL_ARAMAIC, PHOENICIAN, LYDIAN, KHAROSHTHI, OLD_SOUTH_ARABIAN, AVESTAN, INSCRIPTIONAL_PARTHIAN, INSCRIPTIONAL_PAHLAVI, OLD_TURKIC, ARABIC, BRAHMI, KAITHI, CUNEIFORM, EGYPTIAN_HIEROGLYPHS, BAMUM, KATAKANA, HIRAGANA, COMMON, INHERITED, COMMON, INHERITED, COMMON, INHERITED, COMMON, INHERITED, COMMON, GREEK, COMMON, HIRAGANA, COMMON, HAN, COMMON, INHERITED, UNKNOWN };
/*      */ 
/* 4178 */       aliases = new HashMap(128);
/* 4179 */       aliases.put("ARAB", ARABIC);
/* 4180 */       aliases.put("ARMI", IMPERIAL_ARAMAIC);
/* 4181 */       aliases.put("ARMN", ARMENIAN);
/* 4182 */       aliases.put("AVST", AVESTAN);
/* 4183 */       aliases.put("BALI", BALINESE);
/* 4184 */       aliases.put("BAMU", BAMUM);
/* 4185 */       aliases.put("BATK", BATAK);
/* 4186 */       aliases.put("BENG", BENGALI);
/* 4187 */       aliases.put("BOPO", BOPOMOFO);
/* 4188 */       aliases.put("BRAI", BRAILLE);
/* 4189 */       aliases.put("BRAH", BRAHMI);
/* 4190 */       aliases.put("BUGI", BUGINESE);
/* 4191 */       aliases.put("BUHD", BUHID);
/* 4192 */       aliases.put("CANS", CANADIAN_ABORIGINAL);
/* 4193 */       aliases.put("CARI", CARIAN);
/* 4194 */       aliases.put("CHAM", CHAM);
/* 4195 */       aliases.put("CHER", CHEROKEE);
/* 4196 */       aliases.put("COPT", COPTIC);
/* 4197 */       aliases.put("CPRT", CYPRIOT);
/* 4198 */       aliases.put("CYRL", CYRILLIC);
/* 4199 */       aliases.put("DEVA", DEVANAGARI);
/* 4200 */       aliases.put("DSRT", DESERET);
/* 4201 */       aliases.put("EGYP", EGYPTIAN_HIEROGLYPHS);
/* 4202 */       aliases.put("ETHI", ETHIOPIC);
/* 4203 */       aliases.put("GEOR", GEORGIAN);
/* 4204 */       aliases.put("GLAG", GLAGOLITIC);
/* 4205 */       aliases.put("GOTH", GOTHIC);
/* 4206 */       aliases.put("GREK", GREEK);
/* 4207 */       aliases.put("GUJR", GUJARATI);
/* 4208 */       aliases.put("GURU", GURMUKHI);
/* 4209 */       aliases.put("HANG", HANGUL);
/* 4210 */       aliases.put("HANI", HAN);
/* 4211 */       aliases.put("HANO", HANUNOO);
/* 4212 */       aliases.put("HEBR", HEBREW);
/* 4213 */       aliases.put("HIRA", HIRAGANA);
/*      */ 
/* 4216 */       aliases.put("ITAL", OLD_ITALIC);
/* 4217 */       aliases.put("JAVA", JAVANESE);
/* 4218 */       aliases.put("KALI", KAYAH_LI);
/* 4219 */       aliases.put("KANA", KATAKANA);
/* 4220 */       aliases.put("KHAR", KHAROSHTHI);
/* 4221 */       aliases.put("KHMR", KHMER);
/* 4222 */       aliases.put("KNDA", KANNADA);
/* 4223 */       aliases.put("KTHI", KAITHI);
/* 4224 */       aliases.put("LANA", TAI_THAM);
/* 4225 */       aliases.put("LAOO", LAO);
/* 4226 */       aliases.put("LATN", LATIN);
/* 4227 */       aliases.put("LEPC", LEPCHA);
/* 4228 */       aliases.put("LIMB", LIMBU);
/* 4229 */       aliases.put("LINB", LINEAR_B);
/* 4230 */       aliases.put("LISU", LISU);
/* 4231 */       aliases.put("LYCI", LYCIAN);
/* 4232 */       aliases.put("LYDI", LYDIAN);
/* 4233 */       aliases.put("MAND", MANDAIC);
/* 4234 */       aliases.put("MLYM", MALAYALAM);
/* 4235 */       aliases.put("MONG", MONGOLIAN);
/* 4236 */       aliases.put("MTEI", MEETEI_MAYEK);
/* 4237 */       aliases.put("MYMR", MYANMAR);
/* 4238 */       aliases.put("NKOO", NKO);
/* 4239 */       aliases.put("OGAM", OGHAM);
/* 4240 */       aliases.put("OLCK", OL_CHIKI);
/* 4241 */       aliases.put("ORKH", OLD_TURKIC);
/* 4242 */       aliases.put("ORYA", ORIYA);
/* 4243 */       aliases.put("OSMA", OSMANYA);
/* 4244 */       aliases.put("PHAG", PHAGS_PA);
/* 4245 */       aliases.put("PHLI", INSCRIPTIONAL_PAHLAVI);
/* 4246 */       aliases.put("PHNX", PHOENICIAN);
/* 4247 */       aliases.put("PRTI", INSCRIPTIONAL_PARTHIAN);
/* 4248 */       aliases.put("RJNG", REJANG);
/* 4249 */       aliases.put("RUNR", RUNIC);
/* 4250 */       aliases.put("SAMR", SAMARITAN);
/* 4251 */       aliases.put("SARB", OLD_SOUTH_ARABIAN);
/* 4252 */       aliases.put("SAUR", SAURASHTRA);
/* 4253 */       aliases.put("SHAW", SHAVIAN);
/* 4254 */       aliases.put("SINH", SINHALA);
/* 4255 */       aliases.put("SUND", SUNDANESE);
/* 4256 */       aliases.put("SYLO", SYLOTI_NAGRI);
/* 4257 */       aliases.put("SYRC", SYRIAC);
/* 4258 */       aliases.put("TAGB", TAGBANWA);
/* 4259 */       aliases.put("TALE", TAI_LE);
/* 4260 */       aliases.put("TALU", NEW_TAI_LUE);
/* 4261 */       aliases.put("TAML", TAMIL);
/* 4262 */       aliases.put("TAVT", TAI_VIET);
/* 4263 */       aliases.put("TELU", TELUGU);
/* 4264 */       aliases.put("TFNG", TIFINAGH);
/* 4265 */       aliases.put("TGLG", TAGALOG);
/* 4266 */       aliases.put("THAA", THAANA);
/* 4267 */       aliases.put("THAI", THAI);
/* 4268 */       aliases.put("TIBT", TIBETAN);
/* 4269 */       aliases.put("UGAR", UGARITIC);
/* 4270 */       aliases.put("VAII", VAI);
/* 4271 */       aliases.put("XPEO", OLD_PERSIAN);
/* 4272 */       aliases.put("XSUX", CUNEIFORM);
/* 4273 */       aliases.put("YIII", YI);
/* 4274 */       aliases.put("ZINH", INHERITED);
/* 4275 */       aliases.put("ZYYY", COMMON);
/* 4276 */       aliases.put("ZZZZ", UNKNOWN);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Character
 * JD-Core Version:    0.6.2
 */