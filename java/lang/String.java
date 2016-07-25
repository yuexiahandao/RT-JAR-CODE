/*      */ package java.lang;
/*      */ 
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Formatter;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import sun.misc.Hashing;
/*      */ 
/*      */ public final class String
/*      */   implements Serializable, Comparable<String>, CharSequence
/*      */ {
/*      */   private final char[] value;
/*      */   private int hash;
/*      */   private static final long serialVersionUID = -6849794470754667710L;
/*  131 */   private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
/*      */ 
/* 1168 */   public static final Comparator<String> CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator(null);
/*      */ 
/* 3070 */   private static final int HASHING_SEED = i;
/*      */ 
/* 3076 */   private transient int hash32 = 0;
/*      */ 
/*      */   public String()
/*      */   {
/*  140 */     this.value = new char[0];
/*      */   }
/*      */ 
/*      */   public String(String paramString)
/*      */   {
/*  154 */     this.value = paramString.value;
/*  155 */     this.hash = paramString.hash;
/*      */   }
/*      */ 
/*      */   public String(char[] paramArrayOfChar)
/*      */   {
/*  168 */     this.value = Arrays.copyOf(paramArrayOfChar, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public String(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  193 */     if (paramInt1 < 0) {
/*  194 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*      */     }
/*  196 */     if (paramInt2 < 0) {
/*  197 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*      */     }
/*      */ 
/*  200 */     if (paramInt1 > paramArrayOfChar.length - paramInt2) {
/*  201 */       throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
/*      */     }
/*  203 */     this.value = Arrays.copyOfRange(paramArrayOfChar, paramInt1, paramInt1 + paramInt2);
/*      */   }
/*      */ 
/*      */   public String(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  235 */     if (paramInt1 < 0) {
/*  236 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*      */     }
/*  238 */     if (paramInt2 < 0) {
/*  239 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*      */     }
/*      */ 
/*  242 */     if (paramInt1 > paramArrayOfInt.length - paramInt2) {
/*  243 */       throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
/*      */     }
/*      */ 
/*  246 */     int i = paramInt1 + paramInt2;
/*      */ 
/*  249 */     int j = paramInt2;
/*  250 */     for (int k = paramInt1; k < i; k++) {
/*  251 */       m = paramArrayOfInt[k];
/*  252 */       if (!Character.isBmpCodePoint(m))
/*      */       {
/*  254 */         if (Character.isValidCodePoint(m))
/*  255 */           j++;
/*  256 */         else throw new IllegalArgumentException(Integer.toString(m));
/*      */       }
/*      */     }
/*      */ 
/*  260 */     char[] arrayOfChar = new char[j];
/*      */ 
/*  262 */     int m = paramInt1; for (int n = 0; m < i; n++) {
/*  263 */       int i1 = paramArrayOfInt[m];
/*  264 */       if (Character.isBmpCodePoint(i1))
/*  265 */         arrayOfChar[n] = ((char)i1);
/*      */       else
/*  267 */         Character.toSurrogates(i1, arrayOfChar, n++);
/*  262 */       m++;
/*      */     }
/*      */ 
/*  270 */     this.value = arrayOfChar;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  314 */     checkBounds(paramArrayOfByte, paramInt2, paramInt3);
/*  315 */     char[] arrayOfChar = new char[paramInt3];
/*      */     int i;
/*  317 */     if (paramInt1 == 0) {
/*  318 */       for (i = paramInt3; i-- > 0; )
/*  319 */         arrayOfChar[i] = ((char)(paramArrayOfByte[(i + paramInt2)] & 0xFF));
/*      */     }
/*      */     else {
/*  322 */       paramInt1 <<= 8;
/*  323 */       for (i = paramInt3; i-- > 0; ) {
/*  324 */         arrayOfChar[i] = ((char)(paramInt1 | paramArrayOfByte[(i + paramInt2)] & 0xFF));
/*      */       }
/*      */     }
/*  327 */     this.value = arrayOfChar;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  362 */     this(paramArrayOfByte, paramInt, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   private static void checkBounds(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  370 */     if (paramInt2 < 0)
/*  371 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*  372 */     if (paramInt1 < 0)
/*  373 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  374 */     if (paramInt1 > paramArrayOfByte.length - paramInt2)
/*  375 */       throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  413 */     if (paramString == null)
/*  414 */       throw new NullPointerException("charsetName");
/*  415 */     checkBounds(paramArrayOfByte, paramInt1, paramInt2);
/*  416 */     this.value = StringCoding.decode(paramString, paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Charset paramCharset)
/*      */   {
/*  450 */     if (paramCharset == null)
/*  451 */       throw new NullPointerException("charset");
/*  452 */     checkBounds(paramArrayOfByte, paramInt1, paramInt2);
/*  453 */     this.value = StringCoding.decode(paramCharset, paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte, String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  481 */     this(paramArrayOfByte, 0, paramArrayOfByte.length, paramString);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte, Charset paramCharset)
/*      */   {
/*  505 */     this(paramArrayOfByte, 0, paramArrayOfByte.length, paramCharset);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  535 */     checkBounds(paramArrayOfByte, paramInt1, paramInt2);
/*  536 */     this.value = StringCoding.decode(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String(byte[] paramArrayOfByte)
/*      */   {
/*  556 */     this(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public String(StringBuffer paramStringBuffer)
/*      */   {
/*  569 */     synchronized (paramStringBuffer) {
/*  570 */       this.value = Arrays.copyOf(paramStringBuffer.getValue(), paramStringBuffer.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String(StringBuilder paramStringBuilder)
/*      */   {
/*  590 */     this.value = Arrays.copyOf(paramStringBuilder.getValue(), paramStringBuilder.length());
/*      */   }
/*      */ 
/*      */   String(char[] paramArrayOfChar, boolean paramBoolean)
/*      */   {
/*  601 */     this.value = paramArrayOfChar;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   String(int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */   {
/*  611 */     this(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public int length()
/*      */   {
/*  623 */     return this.value.length;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  635 */     return this.value.length == 0;
/*      */   }
/*      */ 
/*      */   public char charAt(int paramInt)
/*      */   {
/*  657 */     if ((paramInt < 0) || (paramInt >= this.value.length)) {
/*  658 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/*  660 */     return this.value[paramInt];
/*      */   }
/*      */ 
/*      */   public int codePointAt(int paramInt)
/*      */   {
/*  686 */     if ((paramInt < 0) || (paramInt >= this.value.length)) {
/*  687 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/*  689 */     return Character.codePointAtImpl(this.value, paramInt, this.value.length);
/*      */   }
/*      */ 
/*      */   public int codePointBefore(int paramInt)
/*      */   {
/*  715 */     int i = paramInt - 1;
/*  716 */     if ((i < 0) || (i >= this.value.length)) {
/*  717 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/*  719 */     return Character.codePointBeforeImpl(this.value, paramInt, 0);
/*      */   }
/*      */ 
/*      */   public int codePointCount(int paramInt1, int paramInt2)
/*      */   {
/*  744 */     if ((paramInt1 < 0) || (paramInt2 > this.value.length) || (paramInt1 > paramInt2)) {
/*  745 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  747 */     return Character.codePointCountImpl(this.value, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   public int offsetByCodePoints(int paramInt1, int paramInt2)
/*      */   {
/*  771 */     if ((paramInt1 < 0) || (paramInt1 > this.value.length)) {
/*  772 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  774 */     return Character.offsetByCodePointsImpl(this.value, 0, this.value.length, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   void getChars(char[] paramArrayOfChar, int paramInt)
/*      */   {
/*  783 */     System.arraycopy(this.value, 0, paramArrayOfChar, paramInt, this.value.length);
/*      */   }
/*      */ 
/*      */   public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*      */   {
/*  817 */     if (paramInt1 < 0) {
/*  818 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*      */     }
/*  820 */     if (paramInt2 > this.value.length) {
/*  821 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*      */     }
/*  823 */     if (paramInt1 > paramInt2) {
/*  824 */       throw new StringIndexOutOfBoundsException(paramInt2 - paramInt1);
/*      */     }
/*  826 */     System.arraycopy(this.value, paramInt1, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void getBytes(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */   {
/*  874 */     if (paramInt1 < 0) {
/*  875 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*      */     }
/*  877 */     if (paramInt2 > this.value.length) {
/*  878 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*      */     }
/*  880 */     if (paramInt1 > paramInt2) {
/*  881 */       throw new StringIndexOutOfBoundsException(paramInt2 - paramInt1);
/*      */     }
/*  883 */     int i = paramInt3;
/*  884 */     int j = paramInt2;
/*  885 */     int k = paramInt1;
/*  886 */     char[] arrayOfChar = this.value;
/*      */ 
/*  888 */     while (k < j)
/*  889 */       paramArrayOfByte[(i++)] = ((byte)arrayOfChar[(k++)]);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  915 */     if (paramString == null) throw new NullPointerException();
/*  916 */     return StringCoding.encode(paramString, this.value, 0, this.value.length);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(Charset paramCharset)
/*      */   {
/*  938 */     if (paramCharset == null) throw new NullPointerException();
/*  939 */     return StringCoding.encode(paramCharset, this.value, 0, this.value.length);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes()
/*      */   {
/*  956 */     return StringCoding.encode(this.value, 0, this.value.length);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  975 */     if (this == paramObject) {
/*  976 */       return true;
/*      */     }
/*  978 */     if ((paramObject instanceof String)) {
/*  979 */       String str = (String)paramObject;
/*  980 */       int i = this.value.length;
/*  981 */       if (i == str.value.length) {
/*  982 */         char[] arrayOfChar1 = this.value;
/*  983 */         char[] arrayOfChar2 = str.value;
/*  984 */         int j = 0;
/*  985 */         while (i-- != 0) {
/*  986 */           if (arrayOfChar1[j] != arrayOfChar2[j])
/*  987 */             return false;
/*  988 */           j++;
/*      */         }
/*  990 */         return true;
/*      */       }
/*      */     }
/*  993 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean contentEquals(StringBuffer paramStringBuffer)
/*      */   {
/* 1011 */     synchronized (paramStringBuffer) {
/* 1012 */       return contentEquals(paramStringBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean contentEquals(CharSequence paramCharSequence)
/*      */   {
/* 1031 */     if (this.value.length != paramCharSequence.length()) {
/* 1032 */       return false;
/*      */     }
/* 1034 */     if ((paramCharSequence instanceof AbstractStringBuilder)) {
/* 1035 */       arrayOfChar1 = this.value;
/* 1036 */       char[] arrayOfChar2 = ((AbstractStringBuilder)paramCharSequence).getValue();
/* 1037 */       j = 0;
/* 1038 */       int k = this.value.length;
/* 1039 */       while (k-- != 0) {
/* 1040 */         if (arrayOfChar1[j] != arrayOfChar2[j])
/* 1041 */           return false;
/* 1042 */         j++;
/*      */       }
/* 1044 */       return true;
/*      */     }
/*      */ 
/* 1047 */     if (paramCharSequence.equals(this)) {
/* 1048 */       return true;
/*      */     }
/* 1050 */     char[] arrayOfChar1 = this.value;
/* 1051 */     int i = 0;
/* 1052 */     int j = this.value.length;
/* 1053 */     while (j-- != 0) {
/* 1054 */       if (arrayOfChar1[i] != paramCharSequence.charAt(i))
/* 1055 */         return false;
/* 1056 */       i++;
/*      */     }
/* 1058 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean equalsIgnoreCase(String paramString)
/*      */   {
/* 1090 */     return this == paramString;
/*      */   }
/*      */ 
/*      */   public int compareTo(String paramString)
/*      */   {
/* 1138 */     int i = this.value.length;
/* 1139 */     int j = paramString.value.length;
/* 1140 */     int k = Math.min(i, j);
/* 1141 */     char[] arrayOfChar1 = this.value;
/* 1142 */     char[] arrayOfChar2 = paramString.value;
/*      */ 
/* 1144 */     int m = 0;
/* 1145 */     while (m < k) {
/* 1146 */       int n = arrayOfChar1[m];
/* 1147 */       int i1 = arrayOfChar2[m];
/* 1148 */       if (n != i1) {
/* 1149 */         return n - i1;
/*      */       }
/* 1151 */       m++;
/*      */     }
/* 1153 */     return i - j;
/*      */   }
/*      */ 
/*      */   public int compareToIgnoreCase(String paramString)
/*      */   {
/* 1220 */     return CASE_INSENSITIVE_ORDER.compare(this, paramString);
/*      */   }
/*      */ 
/*      */   public boolean regionMatches(int paramInt1, String paramString, int paramInt2, int paramInt3)
/*      */   {
/* 1256 */     char[] arrayOfChar1 = this.value;
/* 1257 */     int i = paramInt1;
/* 1258 */     char[] arrayOfChar2 = paramString.value;
/* 1259 */     int j = paramInt2;
/*      */ 
/* 1261 */     if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt1 > this.value.length - paramInt3) || (paramInt2 > paramString.value.length - paramInt3))
/*      */     {
/* 1264 */       return false;
/*      */     }
/* 1266 */     while (paramInt3-- > 0) {
/* 1267 */       if (arrayOfChar1[(i++)] != arrayOfChar2[(j++)]) {
/* 1268 */         return false;
/*      */       }
/*      */     }
/* 1271 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean regionMatches(boolean paramBoolean, int paramInt1, String paramString, int paramInt2, int paramInt3)
/*      */   {
/* 1326 */     char[] arrayOfChar1 = this.value;
/* 1327 */     int i = paramInt1;
/* 1328 */     char[] arrayOfChar2 = paramString.value;
/* 1329 */     int j = paramInt2;
/*      */ 
/* 1331 */     if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt1 > this.value.length - paramInt3) || (paramInt2 > paramString.value.length - paramInt3))
/*      */     {
/* 1334 */       return false;
/*      */     }
/* 1336 */     while (paramInt3-- > 0) {
/* 1337 */       char c1 = arrayOfChar1[(i++)];
/* 1338 */       char c2 = arrayOfChar2[(j++)];
/* 1339 */       if (c1 != c2)
/*      */       {
/* 1342 */         if (paramBoolean)
/*      */         {
/* 1347 */           char c3 = Character.toUpperCase(c1);
/* 1348 */           char c4 = Character.toUpperCase(c2);
/* 1349 */           if ((c3 == c4) || 
/* 1356 */             (Character.toLowerCase(c3) == Character.toLowerCase(c4)))
/*      */             break;
/*      */         }
/*      */         else {
/* 1360 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1362 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean startsWith(String paramString, int paramInt)
/*      */   {
/* 1383 */     char[] arrayOfChar1 = this.value;
/* 1384 */     int i = paramInt;
/* 1385 */     char[] arrayOfChar2 = paramString.value;
/* 1386 */     int j = 0;
/* 1387 */     int k = paramString.value.length;
/*      */ 
/* 1389 */     if ((paramInt < 0) || (paramInt > this.value.length - k))
/* 1390 */       return false;
/*      */     do {
/* 1392 */       k--; if (k < 0) break; 
/* 1393 */     }while (arrayOfChar1[(i++)] == arrayOfChar2[(j++)]);
/* 1394 */     return false;
/*      */ 
/* 1397 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean startsWith(String paramString)
/*      */   {
/* 1414 */     return startsWith(paramString, 0);
/*      */   }
/*      */ 
/*      */   public boolean endsWith(String paramString)
/*      */   {
/* 1429 */     return startsWith(paramString, this.value.length - paramString.value.length);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1446 */     int i = this.hash;
/* 1447 */     if ((i == 0) && (this.value.length > 0)) {
/* 1448 */       char[] arrayOfChar = this.value;
/*      */ 
/* 1450 */       for (int j = 0; j < this.value.length; j++) {
/* 1451 */         i = 31 * i + arrayOfChar[j];
/*      */       }
/* 1453 */       this.hash = i;
/*      */     }
/* 1455 */     return i;
/*      */   }
/*      */ 
/*      */   public int indexOf(int paramInt)
/*      */   {
/* 1483 */     return indexOf(paramInt, 0);
/*      */   }
/*      */ 
/*      */   public int indexOf(int paramInt1, int paramInt2)
/*      */   {
/* 1526 */     int i = this.value.length;
/* 1527 */     if (paramInt2 < 0)
/* 1528 */       paramInt2 = 0;
/* 1529 */     else if (paramInt2 >= i)
/*      */     {
/* 1531 */       return -1;
/*      */     }
/*      */ 
/* 1534 */     if (paramInt1 < 65536)
/*      */     {
/* 1537 */       char[] arrayOfChar = this.value;
/* 1538 */       for (int j = paramInt2; j < i; j++) {
/* 1539 */         if (arrayOfChar[j] == paramInt1) {
/* 1540 */           return j;
/*      */         }
/*      */       }
/* 1543 */       return -1;
/*      */     }
/* 1545 */     return indexOfSupplementary(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private int indexOfSupplementary(int paramInt1, int paramInt2)
/*      */   {
/* 1553 */     if (Character.isValidCodePoint(paramInt1)) {
/* 1554 */       char[] arrayOfChar = this.value;
/* 1555 */       int i = Character.highSurrogate(paramInt1);
/* 1556 */       int j = Character.lowSurrogate(paramInt1);
/* 1557 */       int k = arrayOfChar.length - 1;
/* 1558 */       for (int m = paramInt2; m < k; m++) {
/* 1559 */         if ((arrayOfChar[m] == i) && (arrayOfChar[(m + 1)] == j)) {
/* 1560 */           return m;
/*      */         }
/*      */       }
/*      */     }
/* 1564 */     return -1;
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(int paramInt)
/*      */   {
/* 1591 */     return lastIndexOf(paramInt, this.value.length - 1);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(int paramInt1, int paramInt2)
/*      */   {
/* 1629 */     if (paramInt1 < 65536)
/*      */     {
/* 1632 */       char[] arrayOfChar = this.value;
/* 1633 */       for (int i = Math.min(paramInt2, arrayOfChar.length - 1); 
/* 1634 */         i >= 0; i--) {
/* 1635 */         if (arrayOfChar[i] == paramInt1) {
/* 1636 */           return i;
/*      */         }
/*      */       }
/* 1639 */       return -1;
/*      */     }
/* 1641 */     return lastIndexOfSupplementary(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private int lastIndexOfSupplementary(int paramInt1, int paramInt2)
/*      */   {
/* 1649 */     if (Character.isValidCodePoint(paramInt1)) {
/* 1650 */       char[] arrayOfChar = this.value;
/* 1651 */       int i = Character.highSurrogate(paramInt1);
/* 1652 */       int j = Character.lowSurrogate(paramInt1);
/* 1653 */       for (int k = Math.min(paramInt2, arrayOfChar.length - 2); 
/* 1654 */         k >= 0; k--) {
/* 1655 */         if ((arrayOfChar[k] == i) && (arrayOfChar[(k + 1)] == j)) {
/* 1656 */           return k;
/*      */         }
/*      */       }
/*      */     }
/* 1660 */     return -1;
/*      */   }
/*      */ 
/*      */   public int indexOf(String paramString)
/*      */   {
/* 1678 */     return indexOf(paramString, 0);
/*      */   }
/*      */ 
/*      */   public int indexOf(String paramString, int paramInt)
/*      */   {
/* 1698 */     return indexOf(this.value, 0, this.value.length, paramString.value, 0, paramString.value.length, paramInt);
/*      */   }
/*      */ 
/*      */   static int indexOf(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1718 */     if (paramInt5 >= paramInt2) {
/* 1719 */       return paramInt4 == 0 ? paramInt2 : -1;
/*      */     }
/* 1721 */     if (paramInt5 < 0) {
/* 1722 */       paramInt5 = 0;
/*      */     }
/* 1724 */     if (paramInt4 == 0) {
/* 1725 */       return paramInt5;
/*      */     }
/*      */ 
/* 1728 */     int i = paramArrayOfChar2[paramInt3];
/* 1729 */     int j = paramInt1 + (paramInt2 - paramInt4);
/*      */ 
/* 1731 */     for (int k = paramInt1 + paramInt5; k <= j; k++)
/*      */     {
/* 1733 */       if (paramArrayOfChar1[k] != i) {
/*      */         do k++; while ((k <= j) && (paramArrayOfChar1[k] != i));
/*      */       }
/*      */ 
/* 1738 */       if (k <= j) {
/* 1739 */         int m = k + 1;
/* 1740 */         int n = m + paramInt4 - 1;
/* 1741 */         for (int i1 = paramInt3 + 1; (m < n) && (paramArrayOfChar1[m] == paramArrayOfChar2[i1]); 
/* 1742 */           i1++) m++;
/*      */ 
/* 1744 */         if (m == n)
/*      */         {
/* 1746 */           return k - paramInt1;
/*      */         }
/*      */       }
/*      */     }
/* 1750 */     return -1;
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String paramString)
/*      */   {
/* 1769 */     return lastIndexOf(paramString, this.value.length);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String paramString, int paramInt)
/*      */   {
/* 1789 */     return lastIndexOf(this.value, 0, this.value.length, paramString.value, 0, paramString.value.length, paramInt);
/*      */   }
/*      */ 
/*      */   static int lastIndexOf(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1813 */     int i = paramInt2 - paramInt4;
/* 1814 */     if (paramInt5 < 0) {
/* 1815 */       return -1;
/*      */     }
/* 1817 */     if (paramInt5 > i) {
/* 1818 */       paramInt5 = i;
/*      */     }
/*      */ 
/* 1821 */     if (paramInt4 == 0) {
/* 1822 */       return paramInt5; } 
/*      */ int j = paramInt3 + paramInt4 - 1;
/* 1826 */     int k = paramArrayOfChar2[j];
/* 1827 */     int m = paramInt1 + paramInt4 - 1;
/* 1828 */     int n = m + paramInt5;
/*      */     int i2;
/*      */     while (true) if ((n >= m) && (paramArrayOfChar1[n] != k)) {
/* 1833 */         n--;
/*      */       } else {
/* 1835 */         if (n < m) {
/* 1836 */           return -1;
/*      */         }
/* 1838 */         int i1 = n - 1;
/* 1839 */         i2 = i1 - (paramInt4 - 1);
/* 1840 */         int i3 = j - 1;
/*      */         do
/* 1842 */           if (i1 <= i2) break;
/* 1843 */         while (paramArrayOfChar1[(i1--)] == paramArrayOfChar2[(i3--)]);
/* 1844 */         n--;
/*      */       }
/*      */ 
/*      */ 
/* 1848 */     return i2 - paramInt1 + 1;
/*      */   }
/*      */ 
/*      */   public String substring(int paramInt)
/*      */   {
/* 1870 */     if (paramInt < 0) {
/* 1871 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/* 1873 */     int i = this.value.length - paramInt;
/* 1874 */     if (i < 0) {
/* 1875 */       throw new StringIndexOutOfBoundsException(i);
/*      */     }
/* 1877 */     return paramInt == 0 ? this : new String(this.value, paramInt, i);
/*      */   }
/*      */ 
/*      */   public String substring(int paramInt1, int paramInt2)
/*      */   {
/* 1903 */     if (paramInt1 < 0) {
/* 1904 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*      */     }
/* 1906 */     if (paramInt2 > this.value.length) {
/* 1907 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*      */     }
/* 1909 */     int i = paramInt2 - paramInt1;
/* 1910 */     if (i < 0) {
/* 1911 */       throw new StringIndexOutOfBoundsException(i);
/*      */     }
/* 1913 */     return (paramInt1 == 0) && (paramInt2 == this.value.length) ? this : new String(this.value, paramInt1, i);
/*      */   }
/*      */ 
/*      */   public CharSequence subSequence(int paramInt1, int paramInt2)
/*      */   {
/* 1946 */     return substring(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String concat(String paramString)
/*      */   {
/* 1970 */     int i = paramString.length();
/* 1971 */     if (i == 0) {
/* 1972 */       return this;
/*      */     }
/* 1974 */     int j = this.value.length;
/* 1975 */     char[] arrayOfChar = Arrays.copyOf(this.value, j + i);
/* 1976 */     paramString.getChars(arrayOfChar, j);
/* 1977 */     return new String(arrayOfChar, true);
/*      */   }
/*      */ 
/*      */   public String replace(char paramChar1, char paramChar2)
/*      */   {
/* 2010 */     if (paramChar1 != paramChar2) {
/* 2011 */       int i = this.value.length;
/* 2012 */       int j = -1;
/* 2013 */       char[] arrayOfChar1 = this.value;
/*      */       while (true) {
/* 2015 */         j++; if (j < i) {
/* 2016 */           if (arrayOfChar1[j] == paramChar1)
/* 2017 */             break;
/*      */         }
/*      */       }
/* 2020 */       if (j < i) {
/* 2021 */         char[] arrayOfChar2 = new char[i];
/* 2022 */         for (char c = '\000'; c < j; c++) {
/* 2023 */           arrayOfChar2[c] = arrayOfChar1[c];
/*      */         }
/* 2025 */         while (j < i) {
/* 2026 */           c = arrayOfChar1[j];
/* 2027 */           arrayOfChar2[j] = (c == paramChar1 ? paramChar2 : c);
/* 2028 */           j++;
/*      */         }
/* 2030 */         return new String(arrayOfChar2, true);
/*      */       }
/*      */     }
/* 2033 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean matches(String paramString)
/*      */   {
/* 2063 */     return Pattern.matches(paramString, this);
/*      */   }
/*      */ 
/*      */   public boolean contains(CharSequence paramCharSequence)
/*      */   {
/* 2076 */     return indexOf(paramCharSequence.toString()) > -1;
/*      */   }
/*      */ 
/*      */   public String replaceFirst(String paramString1, String paramString2)
/*      */   {
/* 2119 */     return Pattern.compile(paramString1).matcher(this).replaceFirst(paramString2);
/*      */   }
/*      */ 
/*      */   public String replaceAll(String paramString1, String paramString2)
/*      */   {
/* 2162 */     return Pattern.compile(paramString1).matcher(this).replaceAll(paramString2);
/*      */   }
/*      */ 
/*      */   public String replace(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
/*      */   {
/* 2180 */     return Pattern.compile(paramCharSequence1.toString(), 16).matcher(this).replaceAll(Matcher.quoteReplacement(paramCharSequence2.toString()));
/*      */   }
/*      */ 
/*      */   public String[] split(String paramString, int paramInt)
/*      */   {
/* 2271 */     int i = 0;
/* 2272 */     if (((paramString.value.length == 1) && (".$|()[{^?*+\\".indexOf(i = paramString.charAt(0)) == -1)) || ((paramString.length() == 2) && (paramString.charAt(0) == '\\') && (((i = paramString.charAt(1)) - '0' | 57 - i) < 0) && ((i - 97 | 122 - i) < 0) && ((i - 65 | 90 - i) < 0) && ((i < 55296) || (i > 57343))))
/*      */     {
/* 2282 */       int j = 0;
/* 2283 */       int k = 0;
/* 2284 */       int m = paramInt > 0 ? 1 : 0;
/* 2285 */       ArrayList localArrayList = new ArrayList();
/* 2286 */       while ((k = indexOf(i, j)) != -1) {
/* 2287 */         if ((m == 0) || (localArrayList.size() < paramInt - 1)) {
/* 2288 */           localArrayList.add(substring(j, k));
/* 2289 */           j = k + 1;
/*      */         }
/*      */         else {
/* 2292 */           localArrayList.add(substring(j, this.value.length));
/* 2293 */           j = this.value.length;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2298 */       if (j == 0) {
/* 2299 */         return new String[] { this };
/*      */       }
/*      */ 
/* 2302 */       if ((m == 0) || (localArrayList.size() < paramInt)) {
/* 2303 */         localArrayList.add(substring(j, this.value.length));
/*      */       }
/*      */ 
/* 2306 */       int n = localArrayList.size();
/* 2307 */       if (paramInt == 0)
/* 2308 */         while ((n > 0) && (((String)localArrayList.get(n - 1)).length() == 0))
/* 2309 */           n--;
/* 2310 */       String[] arrayOfString = new String[n];
/* 2311 */       return (String[])localArrayList.subList(0, n).toArray(arrayOfString);
/*      */     }
/* 2313 */     return Pattern.compile(paramString).split(this, paramInt);
/*      */   }
/*      */ 
/*      */   public String[] split(String paramString)
/*      */   {
/* 2355 */     return split(paramString, 0);
/*      */   }
/*      */ 
/*      */   public String toLowerCase(Locale paramLocale)
/*      */   {
/* 2411 */     if (paramLocale == null) {
/* 2412 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 2416 */     int j = this.value.length;
/*      */ 
/* 2420 */     for (int i = 0; i < j; ) {
/* 2421 */       int k = this.value[i];
/* 2422 */       if ((k >= 55296) && (k <= 56319))
/*      */       {
/* 2424 */         m = codePointAt(i);
/* 2425 */         if (m != Character.toLowerCase(m)) {
/*      */           break label99;
/*      */         }
/* 2428 */         i += Character.charCount(m);
/*      */       } else {
/* 2430 */         if (k != Character.toLowerCase(k)) {
/*      */           break label99;
/*      */         }
/* 2433 */         i++;
/*      */       }
/*      */     }
/* 2436 */     return this;
/*      */ 
/* 2439 */     label99: Object localObject = new char[j];
/* 2440 */     int m = 0;
/*      */ 
/* 2444 */     System.arraycopy(this.value, 0, localObject, 0, i);
/*      */ 
/* 2446 */     String str = paramLocale.getLanguage();
/* 2447 */     int n = (str == "tr") || (str == "az") || (str == "lt") ? 1 : 0;
/*      */     int i3;
/* 2453 */     for (int i4 = i; i4 < j; i4 += i3) {
/* 2454 */       int i2 = this.value[i4];
/* 2455 */       if (((char)i2 >= 55296) && ((char)i2 <= 56319))
/*      */       {
/* 2457 */         i2 = codePointAt(i4);
/* 2458 */         i3 = Character.charCount(i2);
/*      */       } else {
/* 2460 */         i3 = 1;
/*      */       }
/*      */       int i1;
/* 2462 */       if ((n != 0) || (i2 == 931) || (i2 == 304))
/*      */       {
/* 2465 */         i1 = ConditionalSpecialCasing.toLowerCaseEx(this, i4, paramLocale);
/*      */       }
/* 2467 */       else i1 = Character.toLowerCase(i2);
/*      */ 
/* 2469 */       if ((i1 == -1) || (i1 >= 65536))
/*      */       {
/*      */         char[] arrayOfChar1;
/* 2471 */         if (i1 == -1) {
/* 2472 */           arrayOfChar1 = ConditionalSpecialCasing.toLowerCaseCharArray(this, i4, paramLocale);
/*      */         } else {
/* 2474 */           if (i3 == 2) {
/* 2475 */             m += Character.toChars(i1, (char[])localObject, i4 + m) - i3;
/* 2476 */             continue;
/*      */           }
/* 2478 */           arrayOfChar1 = Character.toChars(i1);
/*      */         }
/*      */ 
/* 2482 */         int i5 = arrayOfChar1.length;
/* 2483 */         if (i5 > i3) {
/* 2484 */           char[] arrayOfChar2 = new char[localObject.length + i5 - i3];
/* 2485 */           System.arraycopy(localObject, 0, arrayOfChar2, 0, i4 + m);
/* 2486 */           localObject = arrayOfChar2;
/*      */         }
/* 2488 */         for (int i6 = 0; i6 < i5; i6++) {
/* 2489 */           localObject[(i4 + m + i6)] = arrayOfChar1[i6];
/*      */         }
/* 2491 */         m += i5 - i3;
/*      */       } else {
/* 2493 */         localObject[(i4 + m)] = ((char)i1);
/*      */       }
/*      */     }
/* 2496 */     return new String((char[])localObject, 0, j + m);
/*      */   }
/*      */ 
/*      */   public String toLowerCase()
/*      */   {
/* 2519 */     return toLowerCase(Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public String toUpperCase(Locale paramLocale)
/*      */   {
/* 2571 */     if (paramLocale == null) {
/* 2572 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 2576 */     int j = this.value.length;
/*      */ 
/* 2580 */     for (int i = 0; i < j; ) {
/* 2581 */       int k = this.value[i];
/*      */ 
/* 2583 */       if ((k >= 55296) && (k <= 56319))
/*      */       {
/* 2585 */         k = codePointAt(i);
/* 2586 */         m = Character.charCount(k);
/*      */       } else {
/* 2588 */         m = 1;
/*      */       }
/* 2590 */       int n = Character.toUpperCaseEx(k);
/* 2591 */       if ((n == -1) || (k != n))
/*      */       {
/*      */         break label100;
/*      */       }
/* 2595 */       i += m;
/*      */     }
/* 2597 */     return this;
/*      */ 
/* 2600 */     label100: Object localObject = new char[j];
/* 2601 */     int m = 0;
/*      */ 
/* 2605 */     System.arraycopy(this.value, 0, localObject, 0, i);
/*      */ 
/* 2607 */     String str = paramLocale.getLanguage();
/* 2608 */     int i1 = (str == "tr") || (str == "az") || (str == "lt") ? 1 : 0;
/*      */     int i4;
/* 2614 */     for (int i5 = i; i5 < j; i5 += i4) {
/* 2615 */       int i3 = this.value[i5];
/* 2616 */       if (((char)i3 >= 55296) && ((char)i3 <= 56319))
/*      */       {
/* 2618 */         i3 = codePointAt(i5);
/* 2619 */         i4 = Character.charCount(i3);
/*      */       } else {
/* 2621 */         i4 = 1;
/*      */       }
/*      */       int i2;
/* 2623 */       if (i1 != 0)
/* 2624 */         i2 = ConditionalSpecialCasing.toUpperCaseEx(this, i5, paramLocale);
/*      */       else {
/* 2626 */         i2 = Character.toUpperCaseEx(i3);
/*      */       }
/* 2628 */       if ((i2 == -1) || (i2 >= 65536))
/*      */       {
/*      */         char[] arrayOfChar1;
/* 2630 */         if (i2 == -1) {
/* 2631 */           if (i1 != 0) {
/* 2632 */             arrayOfChar1 = ConditionalSpecialCasing.toUpperCaseCharArray(this, i5, paramLocale);
/*      */           }
/*      */           else
/* 2635 */             arrayOfChar1 = Character.toUpperCaseCharArray(i3);
/*      */         } else {
/* 2637 */           if (i4 == 2) {
/* 2638 */             m += Character.toChars(i2, (char[])localObject, i5 + m) - i4;
/* 2639 */             continue;
/*      */           }
/* 2641 */           arrayOfChar1 = Character.toChars(i2);
/*      */         }
/*      */ 
/* 2645 */         int i6 = arrayOfChar1.length;
/* 2646 */         if (i6 > i4) {
/* 2647 */           char[] arrayOfChar2 = new char[localObject.length + i6 - i4];
/* 2648 */           System.arraycopy(localObject, 0, arrayOfChar2, 0, i5 + m);
/* 2649 */           localObject = arrayOfChar2;
/*      */         }
/* 2651 */         for (int i7 = 0; i7 < i6; i7++) {
/* 2652 */           localObject[(i5 + m + i7)] = arrayOfChar1[i7];
/*      */         }
/* 2654 */         m += i6 - i4;
/*      */       } else {
/* 2656 */         localObject[(i5 + m)] = ((char)i2);
/*      */       }
/*      */     }
/* 2659 */     return new String((char[])localObject, 0, j + m);
/*      */   }
/*      */ 
/*      */   public String toUpperCase()
/*      */   {
/* 2682 */     return toUpperCase(Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public String trim()
/*      */   {
/* 2717 */     int i = this.value.length;
/* 2718 */     int j = 0;
/* 2719 */     char[] arrayOfChar = this.value;
/*      */ 
/* 2721 */     while ((j < i) && (arrayOfChar[j] <= ' ')) {
/* 2722 */       j++;
/*      */     }
/* 2724 */     while ((j < i) && (arrayOfChar[(i - 1)] <= ' ')) {
/* 2725 */       i--;
/*      */     }
/* 2727 */     return (j > 0) || (i < this.value.length) ? substring(j, i) : this;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2736 */     return this;
/*      */   }
/*      */ 
/*      */   public char[] toCharArray()
/*      */   {
/* 2748 */     char[] arrayOfChar = new char[this.value.length];
/* 2749 */     System.arraycopy(this.value, 0, arrayOfChar, 0, this.value.length);
/* 2750 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   public static String format(String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2792 */     return new Formatter().format(paramString, paramArrayOfObject).toString();
/*      */   }
/*      */ 
/*      */   public static String format(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2836 */     return new Formatter(paramLocale).format(paramString, paramArrayOfObject).toString();
/*      */   }
/*      */ 
/*      */   public static String valueOf(Object paramObject)
/*      */   {
/* 2849 */     return paramObject == null ? "null" : paramObject.toString();
/*      */   }
/*      */ 
/*      */   public static String valueOf(char[] paramArrayOfChar)
/*      */   {
/* 2863 */     return new String(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public static String valueOf(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2888 */     return new String(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public static String copyValueOf(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2903 */     return new String(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public static String copyValueOf(char[] paramArrayOfChar)
/*      */   {
/* 2915 */     return new String(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public static String valueOf(boolean paramBoolean)
/*      */   {
/* 2927 */     return paramBoolean ? "true" : "false";
/*      */   }
/*      */ 
/*      */   public static String valueOf(char paramChar)
/*      */   {
/* 2939 */     char[] arrayOfChar = { paramChar };
/* 2940 */     return new String(arrayOfChar, true);
/*      */   }
/*      */ 
/*      */   public static String valueOf(int paramInt)
/*      */   {
/* 2954 */     return Integer.toString(paramInt);
/*      */   }
/*      */ 
/*      */   public static String valueOf(long paramLong)
/*      */   {
/* 2968 */     return Long.toString(paramLong);
/*      */   }
/*      */ 
/*      */   public static String valueOf(float paramFloat)
/*      */   {
/* 2982 */     return Float.toString(paramFloat);
/*      */   }
/*      */ 
/*      */   public static String valueOf(double paramDouble)
/*      */   {
/* 2996 */     return Double.toString(paramDouble);
/*      */   }
/*      */ 
/*      */   public native String intern();
/*      */ 
/*      */   int hash32()
/*      */   {
/* 3084 */     int i = this.hash32;
/* 3085 */     if (0 == i)
/*      */     {
/* 3087 */       i = Hashing.murmur3_32(HASHING_SEED, this.value, 0, this.value.length);
/*      */ 
/* 3090 */       i = 0 != i ? i : 1;
/*      */ 
/* 3092 */       this.hash32 = i;
/*      */     }
/*      */ 
/* 3095 */     return i;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 3030 */     long l1 = System.nanoTime();
/* 3031 */     long l2 = System.currentTimeMillis();
/* 3032 */     int[] arrayOfInt1 = { System.identityHashCode(String.class), System.identityHashCode(System.class), (int)(l1 >>> 32), (int)l1, (int)(l2 >>> 32), (int)l2, (int)(System.nanoTime() >>> 2) };
/*      */ 
/* 3044 */     int i = 0;
/*      */ 
/* 3047 */     for (int m : arrayOfInt1) {
/* 3048 */       m *= -862048943;
/* 3049 */       m = m << 15 | m >>> 17;
/* 3050 */       m *= 461845907;
/*      */ 
/* 3052 */       i ^= m;
/* 3053 */       i = i << 13 | i >>> 19;
/* 3054 */       i = i * 5 + -430675100;
/*      */     }
/*      */ 
/* 3061 */     i ^= arrayOfInt1.length * 4;
/*      */ 
/* 3064 */     i ^= i >>> 16;
/* 3065 */     i *= -2048144789;
/* 3066 */     i ^= i >>> 13;
/* 3067 */     i *= -1028477387;
/* 3068 */     i ^= i >>> 16;
/*      */   }
/*      */ 
/*      */   private static class CaseInsensitiveComparator
/*      */     implements Comparator<String>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 8575799808933029326L;
/*      */ 
/*      */     public int compare(String paramString1, String paramString2)
/*      */     {
/* 1176 */       int i = paramString1.length();
/* 1177 */       int j = paramString2.length();
/* 1178 */       int k = Math.min(i, j);
/* 1179 */       for (int m = 0; m < k; m++) {
/* 1180 */         char c1 = paramString1.charAt(m);
/* 1181 */         char c2 = paramString2.charAt(m);
/* 1182 */         if (c1 != c2) {
/* 1183 */           c1 = Character.toUpperCase(c1);
/* 1184 */           c2 = Character.toUpperCase(c2);
/* 1185 */           if (c1 != c2) {
/* 1186 */             c1 = Character.toLowerCase(c1);
/* 1187 */             c2 = Character.toLowerCase(c2);
/* 1188 */             if (c1 != c2)
/*      */             {
/* 1190 */               return c1 - c2;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1195 */       return i - j;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.String
 * JD-Core Version:    0.6.2
 */