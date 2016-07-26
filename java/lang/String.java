package java.lang;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.Hashing;

public final class String
        implements Serializable, Comparable<String>, CharSequence {
    private final char[] value;
    private int hash;
    private static final long serialVersionUID = -6849794470754667710L;
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];

    public static final Comparator<String> CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator(null);

    private static final int HASHING_SEED = i;

    private transient int hash32 = 0;

    public String() {
        this.value = new char[0];
    }

    public String(String paramString) {
        this.value = paramString.value;
        this.hash = paramString.hash;
    }

    public String(char[] paramArrayOfChar) {
        this.value = Arrays.copyOf(paramArrayOfChar, paramArrayOfChar.length);
    }

    public String(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt1);
        }
        if (paramInt2 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt2);
        }

        if (paramInt1 > paramArrayOfChar.length - paramInt2) {
            throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
        }
        this.value = Arrays.copyOfRange(paramArrayOfChar, paramInt1, paramInt1 + paramInt2);
    }

    public String(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt1);
        }
        if (paramInt2 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt2);
        }

        if (paramInt1 > paramArrayOfInt.length - paramInt2) {
            throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
        }

        int i = paramInt1 + paramInt2;

        int j = paramInt2;
        for (int k = paramInt1; k < i; k++) {
            m = paramArrayOfInt[k];
            if (!Character.isBmpCodePoint(m)) {
                if (Character.isValidCodePoint(m))
                    j++;
                else throw new IllegalArgumentException(Integer.toString(m));
            }
        }

        char[] arrayOfChar = new char[j];

        int m = paramInt1;
        for (int n = 0; m < i; n++) {
            int i1 = paramArrayOfInt[m];
            if (Character.isBmpCodePoint(i1))
                arrayOfChar[n] = ((char) i1);
            else
                Character.toSurrogates(i1, arrayOfChar, n++);
            m++;
        }

        this.value = arrayOfChar;
    }

    @Deprecated
    public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) {
        checkBounds(paramArrayOfByte, paramInt2, paramInt3);
        char[] arrayOfChar = new char[paramInt3];
        int i;
        if (paramInt1 == 0) {
            for (i = paramInt3; i-- > 0; )
                arrayOfChar[i] = ((char) (paramArrayOfByte[(i + paramInt2)] & 0xFF));
        } else {
            paramInt1 <<= 8;
            for (i = paramInt3; i-- > 0; ) {
                arrayOfChar[i] = ((char) (paramInt1 | paramArrayOfByte[(i + paramInt2)] & 0xFF));
            }
        }
        this.value = arrayOfChar;
    }

    @Deprecated
    public String(byte[] paramArrayOfByte, int paramInt) {
        this(paramArrayOfByte, paramInt, 0, paramArrayOfByte.length);
    }

    private static void checkBounds(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramInt2 < 0)
            throw new StringIndexOutOfBoundsException(paramInt2);
        if (paramInt1 < 0)
            throw new StringIndexOutOfBoundsException(paramInt1);
        if (paramInt1 > paramArrayOfByte.length - paramInt2)
            throw new StringIndexOutOfBoundsException(paramInt1 + paramInt2);
    }

    public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
            throws UnsupportedEncodingException {
        if (paramString == null)
            throw new NullPointerException("charsetName");
        checkBounds(paramArrayOfByte, paramInt1, paramInt2);
        this.value = StringCoding.decode(paramString, paramArrayOfByte, paramInt1, paramInt2);
    }

    public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Charset paramCharset) {
        if (paramCharset == null)
            throw new NullPointerException("charset");
        checkBounds(paramArrayOfByte, paramInt1, paramInt2);
        this.value = StringCoding.decode(paramCharset, paramArrayOfByte, paramInt1, paramInt2);
    }

    public String(byte[] paramArrayOfByte, String paramString)
            throws UnsupportedEncodingException {
        this(paramArrayOfByte, 0, paramArrayOfByte.length, paramString);
    }

    public String(byte[] paramArrayOfByte, Charset paramCharset) {
        this(paramArrayOfByte, 0, paramArrayOfByte.length, paramCharset);
    }

    public String(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        checkBounds(paramArrayOfByte, paramInt1, paramInt2);
        this.value = StringCoding.decode(paramArrayOfByte, paramInt1, paramInt2);
    }

    public String(byte[] paramArrayOfByte) {
        this(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public String(StringBuffer paramStringBuffer) {
        synchronized (paramStringBuffer) {
            this.value = Arrays.copyOf(paramStringBuffer.getValue(), paramStringBuffer.length());
        }
    }

    public String(StringBuilder paramStringBuilder) {
        this.value = Arrays.copyOf(paramStringBuilder.getValue(), paramStringBuilder.length());
    }

    String(char[] paramArrayOfChar, boolean paramBoolean) {
        this.value = paramArrayOfChar;
    }

    @Deprecated
    String(int paramInt1, int paramInt2, char[] paramArrayOfChar) {
        this(paramArrayOfChar, paramInt1, paramInt2);
    }

    public int length() {
        return this.value.length;
    }

    public boolean isEmpty() {
        return this.value.length == 0;
    }

    public char charAt(int paramInt) {
        if ((paramInt < 0) || (paramInt >= this.value.length)) {
            throw new StringIndexOutOfBoundsException(paramInt);
        }
        return this.value[paramInt];
    }

    /**
     *
     * @param paramInt
     * @return
     */
    public int codePointAt(int paramInt) {
        if ((paramInt < 0) || (paramInt >= this.value.length)) {
            throw new StringIndexOutOfBoundsException(paramInt);
        }
        return Character.codePointAtImpl(this.value, paramInt, this.value.length);
    }

    public int codePointBefore(int paramInt) {
        int i = paramInt - 1;
        if ((i < 0) || (i >= this.value.length)) {
            throw new StringIndexOutOfBoundsException(paramInt);
        }
        return Character.codePointBeforeImpl(this.value, paramInt, 0);
    }

    public int codePointCount(int paramInt1, int paramInt2) {
        if ((paramInt1 < 0) || (paramInt2 > this.value.length) || (paramInt1 > paramInt2)) {
            throw new IndexOutOfBoundsException();
        }
        return Character.codePointCountImpl(this.value, paramInt1, paramInt2 - paramInt1);
    }

    public int offsetByCodePoints(int paramInt1, int paramInt2) {
        if ((paramInt1 < 0) || (paramInt1 > this.value.length)) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePointsImpl(this.value, 0, this.value.length, paramInt1, paramInt2);
    }

    void getChars(char[] paramArrayOfChar, int paramInt) {
        System.arraycopy(this.value, 0, paramArrayOfChar, paramInt, this.value.length);
    }

    public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3) {
        if (paramInt1 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt1);
        }
        if (paramInt2 > this.value.length) {
            throw new StringIndexOutOfBoundsException(paramInt2);
        }
        if (paramInt1 > paramInt2) {
            throw new StringIndexOutOfBoundsException(paramInt2 - paramInt1);
        }
        System.arraycopy(this.value, paramInt1, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
    }

    @Deprecated
    public void getBytes(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        if (paramInt1 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt1);
        }
        if (paramInt2 > this.value.length) {
            throw new StringIndexOutOfBoundsException(paramInt2);
        }
        if (paramInt1 > paramInt2) {
            throw new StringIndexOutOfBoundsException(paramInt2 - paramInt1);
        }
        int i = paramInt3;
        int j = paramInt2;
        int k = paramInt1;
        char[] arrayOfChar = this.value;

        while (k < j)
            paramArrayOfByte[(i++)] = ((byte) arrayOfChar[(k++)]);
    }

    public byte[] getBytes(String paramString)
            throws UnsupportedEncodingException {
        if (paramString == null) throw new NullPointerException();
        return StringCoding.encode(paramString, this.value, 0, this.value.length);
    }

    public byte[] getBytes(Charset paramCharset) {
        if (paramCharset == null) throw new NullPointerException();
        return StringCoding.encode(paramCharset, this.value, 0, this.value.length);
    }

    public byte[] getBytes() {
        return StringCoding.encode(this.value, 0, this.value.length);
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject instanceof String)) {
            String str = (String) paramObject;
            int i = this.value.length;
            if (i == str.value.length) {
                char[] arrayOfChar1 = this.value;
                char[] arrayOfChar2 = str.value;
                int j = 0;
                while (i-- != 0) {
                    if (arrayOfChar1[j] != arrayOfChar2[j])
                        return false;
                    j++;
                }
                return true;
            }
        }
        return false;
    }

    public boolean contentEquals(StringBuffer paramStringBuffer) {
        synchronized (paramStringBuffer) {
            return contentEquals(paramStringBuffer);
        }
    }

    public boolean contentEquals(CharSequence paramCharSequence) {
        if (this.value.length != paramCharSequence.length()) {
            return false;
        }
        if ((paramCharSequence instanceof AbstractStringBuilder)) {
            arrayOfChar1 = this.value;
            char[] arrayOfChar2 = ((AbstractStringBuilder) paramCharSequence).getValue();
            j = 0;
            int k = this.value.length;
            while (k-- != 0) {
                if (arrayOfChar1[j] != arrayOfChar2[j])
                    return false;
                j++;
            }
            return true;
        }

        if (paramCharSequence.equals(this)) {
            return true;
        }
        char[] arrayOfChar1 = this.value;
        int i = 0;
        int j = this.value.length;
        while (j-- != 0) {
            if (arrayOfChar1[i] != paramCharSequence.charAt(i))
                return false;
            i++;
        }
        return true;
    }

    public boolean equalsIgnoreCase(String paramString) {
        return this == paramString;
    }

    public int compareTo(String paramString) {
        int i = this.value.length;
        int j = paramString.value.length;
        int k = Math.min(i, j);
        char[] arrayOfChar1 = this.value;
        char[] arrayOfChar2 = paramString.value;

        int m = 0;
        while (m < k) {
            int n = arrayOfChar1[m];
            int i1 = arrayOfChar2[m];
            if (n != i1) {
                return n - i1;
            }
            m++;
        }
        return i - j;
    }

    public int compareToIgnoreCase(String paramString) {
        return CASE_INSENSITIVE_ORDER.compare(this, paramString);
    }

    public boolean regionMatches(int paramInt1, String paramString, int paramInt2, int paramInt3) {
        char[] arrayOfChar1 = this.value;
        int i = paramInt1;
        char[] arrayOfChar2 = paramString.value;
        int j = paramInt2;

        if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt1 > this.value.length - paramInt3) || (paramInt2 > paramString.value.length - paramInt3)) {
            return false;
        }
        while (paramInt3-- > 0) {
            if (arrayOfChar1[(i++)] != arrayOfChar2[(j++)]) {
                return false;
            }
        }
        return true;
    }

    public boolean regionMatches(boolean paramBoolean, int paramInt1, String paramString, int paramInt2, int paramInt3) {
        char[] arrayOfChar1 = this.value;
        int i = paramInt1;
        char[] arrayOfChar2 = paramString.value;
        int j = paramInt2;

        if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt1 > this.value.length - paramInt3) || (paramInt2 > paramString.value.length - paramInt3)) {
            return false;
        }
        while (paramInt3-- > 0) {
            char c1 = arrayOfChar1[(i++)];
            char c2 = arrayOfChar2[(j++)];
            if (c1 != c2) {
                if (paramBoolean) {
                    char c3 = Character.toUpperCase(c1);
                    char c4 = Character.toUpperCase(c2);
                    if ((c3 == c4) ||
                            (Character.toLowerCase(c3) == Character.toLowerCase(c4)))
                        break;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean startsWith(String paramString, int paramInt) {
        char[] arrayOfChar1 = this.value;
        int i = paramInt;
        char[] arrayOfChar2 = paramString.value;
        int j = 0;
        int k = paramString.value.length;

        if ((paramInt < 0) || (paramInt > this.value.length - k))
            return false;
        do {
            k--;
            if (k < 0) break;
        } while (arrayOfChar1[(i++)] == arrayOfChar2[(j++)]);
        return false;

        return true;
    }

    public boolean startsWith(String paramString) {
        return startsWith(paramString, 0);
    }

    public boolean endsWith(String paramString) {
        return startsWith(paramString, this.value.length - paramString.value.length);
    }

    public int hashCode() {
        int i = this.hash;
        if ((i == 0) && (this.value.length > 0)) {
            char[] arrayOfChar = this.value;

            for (int j = 0; j < this.value.length; j++) {
                i = 31 * i + arrayOfChar[j];
            }
            this.hash = i;
        }
        return i;
    }

    public int indexOf(int paramInt) {
        return indexOf(paramInt, 0);
    }

    public int indexOf(int paramInt1, int paramInt2) {
        int i = this.value.length;
        if (paramInt2 < 0)
            paramInt2 = 0;
        else if (paramInt2 >= i) {
            return -1;
        }

        if (paramInt1 < 65536) {
            char[] arrayOfChar = this.value;
            for (int j = paramInt2; j < i; j++) {
                if (arrayOfChar[j] == paramInt1) {
                    return j;
                }
            }
            return -1;
        }
        return indexOfSupplementary(paramInt1, paramInt2);
    }

    private int indexOfSupplementary(int paramInt1, int paramInt2) {
        if (Character.isValidCodePoint(paramInt1)) {
            char[] arrayOfChar = this.value;
            int i = Character.highSurrogate(paramInt1);
            int j = Character.lowSurrogate(paramInt1);
            int k = arrayOfChar.length - 1;
            for (int m = paramInt2; m < k; m++) {
                if ((arrayOfChar[m] == i) && (arrayOfChar[(m + 1)] == j)) {
                    return m;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(int paramInt) {
        return lastIndexOf(paramInt, this.value.length - 1);
    }

    public int lastIndexOf(int paramInt1, int paramInt2) {
        if (paramInt1 < 65536) {
            char[] arrayOfChar = this.value;
            for (int i = Math.min(paramInt2, arrayOfChar.length - 1);
                 i >= 0; i--) {
                if (arrayOfChar[i] == paramInt1) {
                    return i;
                }
            }
            return -1;
        }
        return lastIndexOfSupplementary(paramInt1, paramInt2);
    }

    private int lastIndexOfSupplementary(int paramInt1, int paramInt2) {
        if (Character.isValidCodePoint(paramInt1)) {
            char[] arrayOfChar = this.value;
            int i = Character.highSurrogate(paramInt1);
            int j = Character.lowSurrogate(paramInt1);
            for (int k = Math.min(paramInt2, arrayOfChar.length - 2);
                 k >= 0; k--) {
                if ((arrayOfChar[k] == i) && (arrayOfChar[(k + 1)] == j)) {
                    return k;
                }
            }
        }
        return -1;
    }

    public int indexOf(String paramString) {
        return indexOf(paramString, 0);
    }

    public int indexOf(String paramString, int paramInt) {
        return indexOf(this.value, 0, this.value.length, paramString.value, 0, paramString.value.length, paramInt);
    }

    static int indexOf(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5) {
        if (paramInt5 >= paramInt2) {
            return paramInt4 == 0 ? paramInt2 : -1;
        }
        if (paramInt5 < 0) {
            paramInt5 = 0;
        }
        if (paramInt4 == 0) {
            return paramInt5;
        }

        int i = paramArrayOfChar2[paramInt3];
        int j = paramInt1 + (paramInt2 - paramInt4);

        for (int k = paramInt1 + paramInt5; k <= j; k++) {
            if (paramArrayOfChar1[k] != i) {
                do k++; while ((k <= j) && (paramArrayOfChar1[k] != i));
            }

            if (k <= j) {
                int m = k + 1;
                int n = m + paramInt4 - 1;
                for (int i1 = paramInt3 + 1; (m < n) && (paramArrayOfChar1[m] == paramArrayOfChar2[i1]);
                     i1++)
                    m++;

                if (m == n) {
                    return k - paramInt1;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(String paramString) {
        return lastIndexOf(paramString, this.value.length);
    }

    public int lastIndexOf(String paramString, int paramInt) {
        return lastIndexOf(this.value, 0, this.value.length, paramString.value, 0, paramString.value.length, paramInt);
    }

    static int lastIndexOf(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5) {
        int i = paramInt2 - paramInt4;
        if (paramInt5 < 0) {
            return -1;
        }
        if (paramInt5 > i) {
            paramInt5 = i;
        }

        if (paramInt4 == 0) {
            return paramInt5;
        }
        int j = paramInt3 + paramInt4 - 1;
        int k = paramArrayOfChar2[j];
        int m = paramInt1 + paramInt4 - 1;
        int n = m + paramInt5;
        int i2;
        while (true) if ((n >= m) && (paramArrayOfChar1[n] != k)) {
            n--;
        } else {
            if (n < m) {
                return -1;
            }
            int i1 = n - 1;
            i2 = i1 - (paramInt4 - 1);
            int i3 = j - 1;
            do
                if (i1 <= i2) break;
            while (paramArrayOfChar1[(i1--)] == paramArrayOfChar2[(i3--)]);
            n--;
        }


        return i2 - paramInt1 + 1;
    }

    public String substring(int paramInt) {
        if (paramInt < 0) {
            throw new StringIndexOutOfBoundsException(paramInt);
        }
        int i = this.value.length - paramInt;
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        return paramInt == 0 ? this : new String(this.value, paramInt, i);
    }

    public String substring(int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            throw new StringIndexOutOfBoundsException(paramInt1);
        }
        if (paramInt2 > this.value.length) {
            throw new StringIndexOutOfBoundsException(paramInt2);
        }
        int i = paramInt2 - paramInt1;
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        return (paramInt1 == 0) && (paramInt2 == this.value.length) ? this : new String(this.value, paramInt1, i);
    }

    public CharSequence subSequence(int paramInt1, int paramInt2) {
        return substring(paramInt1, paramInt2);
    }

    public String concat(String paramString) {
        int i = paramString.length();
        if (i == 0) {
            return this;
        }
        int j = this.value.length;
        char[] arrayOfChar = Arrays.copyOf(this.value, j + i);
        paramString.getChars(arrayOfChar, j);
        return new String(arrayOfChar, true);
    }

    public String replace(char paramChar1, char paramChar2) {
        if (paramChar1 != paramChar2) {
            int i = this.value.length;
            int j = -1;
            char[] arrayOfChar1 = this.value;
            while (true) {
                j++;
                if (j < i) {
                    if (arrayOfChar1[j] == paramChar1)
                        break;
                }
            }
            if (j < i) {
                char[] arrayOfChar2 = new char[i];
                for (char c = '\000'; c < j; c++) {
                    arrayOfChar2[c] = arrayOfChar1[c];
                }
                while (j < i) {
                    c = arrayOfChar1[j];
                    arrayOfChar2[j] = (c == paramChar1 ? paramChar2 : c);
                    j++;
                }
                return new String(arrayOfChar2, true);
            }
        }
        return this;
    }

    public boolean matches(String paramString) {
        return Pattern.matches(paramString, this);
    }

    public boolean contains(CharSequence paramCharSequence) {
        return indexOf(paramCharSequence.toString()) > -1;
    }

    public String replaceFirst(String paramString1, String paramString2) {
        return Pattern.compile(paramString1).matcher(this).replaceFirst(paramString2);
    }

    public String replaceAll(String paramString1, String paramString2) {
        return Pattern.compile(paramString1).matcher(this).replaceAll(paramString2);
    }

    public String replace(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
        return Pattern.compile(paramCharSequence1.toString(), 16).matcher(this).replaceAll(Matcher.quoteReplacement(paramCharSequence2.toString()));
    }

    public String[] split(String paramString, int paramInt) {
        int i = 0;
        if (((paramString.value.length == 1) && (".$|()[{^?*+\\".indexOf(i = paramString.charAt(0)) == -1)) || ((paramString.length() == 2) && (paramString.charAt(0) == '\\') && (((i = paramString.charAt(1)) - '0' | 57 - i) < 0) && ((i - 97 | 122 - i) < 0) && ((i - 65 | 90 - i) < 0) && ((i < 55296) || (i > 57343)))) {
            int j = 0;
            int k = 0;
            int m = paramInt > 0 ? 1 : 0;
            ArrayList localArrayList = new ArrayList();
            while ((k = indexOf(i, j)) != -1) {
                if ((m == 0) || (localArrayList.size() < paramInt - 1)) {
                    localArrayList.add(substring(j, k));
                    j = k + 1;
                } else {
                    localArrayList.add(substring(j, this.value.length));
                    j = this.value.length;
                }

            }

            if (j == 0) {
                return new String[]{this};
            }

            if ((m == 0) || (localArrayList.size() < paramInt)) {
                localArrayList.add(substring(j, this.value.length));
            }

            int n = localArrayList.size();
            if (paramInt == 0)
                while ((n > 0) && (((String) localArrayList.get(n - 1)).length() == 0))
                    n--;
            String[] arrayOfString = new String[n];
            return (String[]) localArrayList.subList(0, n).toArray(arrayOfString);
        }
        return Pattern.compile(paramString).split(this, paramInt);
    }

    public String[] split(String paramString) {
        return split(paramString, 0);
    }

    public String toLowerCase(Locale paramLocale) {
        if (paramLocale == null) {
            throw new NullPointerException();
        }

        int j = this.value.length;

        for (int i = 0; i < j; ) {
            int k = this.value[i];
            if ((k >= 55296) && (k <= 56319)) {
                m = codePointAt(i);
                if (m != Character.toLowerCase(m)) {
                    break label99;
                }
                i += Character.charCount(m);
            } else {
                if (k != Character.toLowerCase(k)) {
                    break label99;
                }
                i++;
            }
        }
        return this;

        label99:
        Object localObject = new char[j];
        int m = 0;

        System.arraycopy(this.value, 0, localObject, 0, i);

        String str = paramLocale.getLanguage();
        int n = (str == "tr") || (str == "az") || (str == "lt") ? 1 : 0;
        int i3;
        for (int i4 = i; i4 < j; i4 += i3) {
            int i2 = this.value[i4];
            if (((char) i2 >= 55296) && ((char) i2 <= 56319)) {
                i2 = codePointAt(i4);
                i3 = Character.charCount(i2);
            } else {
                i3 = 1;
            }
            int i1;
            if ((n != 0) || (i2 == 931) || (i2 == 304)) {
                i1 = ConditionalSpecialCasing.toLowerCaseEx(this, i4, paramLocale);
            } else i1 = Character.toLowerCase(i2);

            if ((i1 == -1) || (i1 >= 65536)) {
                char[] arrayOfChar1;
                if (i1 == -1) {
                    arrayOfChar1 = ConditionalSpecialCasing.toLowerCaseCharArray(this, i4, paramLocale);
                } else {
                    if (i3 == 2) {
                        m += Character.toChars(i1, (char[]) localObject, i4 + m) - i3;
                        continue;
                    }
                    arrayOfChar1 = Character.toChars(i1);
                }

                int i5 = arrayOfChar1.length;
                if (i5 > i3) {
                    char[] arrayOfChar2 = new char[localObject.length + i5 - i3];
                    System.arraycopy(localObject, 0, arrayOfChar2, 0, i4 + m);
                    localObject = arrayOfChar2;
                }
                for (int i6 = 0; i6 < i5; i6++) {
                    localObject[(i4 + m + i6)] = arrayOfChar1[i6];
                }
                m += i5 - i3;
            } else {
                localObject[(i4 + m)] = ((char) i1);
            }
        }
        return new String((char[]) localObject, 0, j + m);
    }

    public String toLowerCase() {
        return toLowerCase(Locale.getDefault());
    }

    public String toUpperCase(Locale paramLocale) {
        if (paramLocale == null) {
            throw new NullPointerException();
        }

        int j = this.value.length;

        for (int i = 0; i < j; ) {
            int k = this.value[i];

            if ((k >= 55296) && (k <= 56319)) {
                k = codePointAt(i);
                m = Character.charCount(k);
            } else {
                m = 1;
            }
            int n = Character.toUpperCaseEx(k);
            if ((n == -1) || (k != n)) {
                break label100;
            }
            i += m;
        }
        return this;

        label100:
        Object localObject = new char[j];
        int m = 0;

        System.arraycopy(this.value, 0, localObject, 0, i);

        String str = paramLocale.getLanguage();
        int i1 = (str == "tr") || (str == "az") || (str == "lt") ? 1 : 0;
        int i4;
        for (int i5 = i; i5 < j; i5 += i4) {
            int i3 = this.value[i5];
            if (((char) i3 >= 55296) && ((char) i3 <= 56319)) {
                i3 = codePointAt(i5);
                i4 = Character.charCount(i3);
            } else {
                i4 = 1;
            }
            int i2;
            if (i1 != 0)
                i2 = ConditionalSpecialCasing.toUpperCaseEx(this, i5, paramLocale);
            else {
                i2 = Character.toUpperCaseEx(i3);
            }
            if ((i2 == -1) || (i2 >= 65536)) {
                char[] arrayOfChar1;
                if (i2 == -1) {
                    if (i1 != 0) {
                        arrayOfChar1 = ConditionalSpecialCasing.toUpperCaseCharArray(this, i5, paramLocale);
                    } else
                        arrayOfChar1 = Character.toUpperCaseCharArray(i3);
                } else {
                    if (i4 == 2) {
                        m += Character.toChars(i2, (char[]) localObject, i5 + m) - i4;
                        continue;
                    }
                    arrayOfChar1 = Character.toChars(i2);
                }

                int i6 = arrayOfChar1.length;
                if (i6 > i4) {
                    char[] arrayOfChar2 = new char[localObject.length + i6 - i4];
                    System.arraycopy(localObject, 0, arrayOfChar2, 0, i5 + m);
                    localObject = arrayOfChar2;
                }
                for (int i7 = 0; i7 < i6; i7++) {
                    localObject[(i5 + m + i7)] = arrayOfChar1[i7];
                }
                m += i6 - i4;
            } else {
                localObject[(i5 + m)] = ((char) i2);
            }
        }
        return new String((char[]) localObject, 0, j + m);
    }

    public String toUpperCase() {
        return toUpperCase(Locale.getDefault());
    }

    public String trim() {
        int i = this.value.length;
        int j = 0;
        char[] arrayOfChar = this.value;

        while ((j < i) && (arrayOfChar[j] <= ' ')) {
            j++;
        }
        while ((j < i) && (arrayOfChar[(i - 1)] <= ' ')) {
            i--;
        }
        return (j > 0) || (i < this.value.length) ? substring(j, i) : this;
    }

    public String toString() {
        return this;
    }

    public char[] toCharArray() {
        char[] arrayOfChar = new char[this.value.length];
        System.arraycopy(this.value, 0, arrayOfChar, 0, this.value.length);
        return arrayOfChar;
    }

    public static String format(String paramString, Object[] paramArrayOfObject) {
        return new Formatter().format(paramString, paramArrayOfObject).toString();
    }

    public static String format(Locale paramLocale, String paramString, Object[] paramArrayOfObject) {
        return new Formatter(paramLocale).format(paramString, paramArrayOfObject).toString();
    }

    public static String valueOf(Object paramObject) {
        return paramObject == null ? "null" : paramObject.toString();
    }

    public static String valueOf(char[] paramArrayOfChar) {
        return new String(paramArrayOfChar);
    }

    public static String valueOf(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
        return new String(paramArrayOfChar, paramInt1, paramInt2);
    }

    public static String copyValueOf(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
        return new String(paramArrayOfChar, paramInt1, paramInt2);
    }

    public static String copyValueOf(char[] paramArrayOfChar) {
        return new String(paramArrayOfChar);
    }

    public static String valueOf(boolean paramBoolean) {
        return paramBoolean ? "true" : "false";
    }

    public static String valueOf(char paramChar) {
        char[] arrayOfChar = {paramChar};
        return new String(arrayOfChar, true);
    }

    public static String valueOf(int paramInt) {
        return Integer.toString(paramInt);
    }

    public static String valueOf(long paramLong) {
        return Long.toString(paramLong);
    }

    public static String valueOf(float paramFloat) {
        return Float.toString(paramFloat);
    }

    public static String valueOf(double paramDouble) {
        return Double.toString(paramDouble);
    }

    public native String intern();

    int hash32() {
        int i = this.hash32;
        if (0 == i) {
            i = Hashing.murmur3_32(HASHING_SEED, this.value, 0, this.value.length);

            i = 0 != i ? i : 1;

            this.hash32 = i;
        }

        return i;
    }

    static {
        long l1 = System.nanoTime();
        long l2 = System.currentTimeMillis();
        int[] arrayOfInt1 = {System.identityHashCode(String.class), System.identityHashCode(System.class), (int) (l1 >>> 32), (int) l1, (int) (l2 >>> 32), (int) l2, (int) (System.nanoTime() >>> 2)};

        int i = 0;

        for (int m : arrayOfInt1) {
            m *= -862048943;
            m = m << 15 | m >>> 17;
            m *= 461845907;

            i ^= m;
            i = i << 13 | i >>> 19;
            i = i * 5 + -430675100;
        }

        i ^= arrayOfInt1.length * 4;

        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;
    }

    private static class CaseInsensitiveComparator
            implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 8575799808933029326L;

        public int compare(String paramString1, String paramString2) {
            int i = paramString1.length();
            int j = paramString2.length();
            int k = Math.min(i, j);
            for (int m = 0; m < k; m++) {
                char c1 = paramString1.charAt(m);
                char c2 = paramString2.charAt(m);
                if (c1 != c2) {
                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);
                    if (c1 != c2) {
                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);
                        if (c1 != c2) {
                            return c1 - c2;
                        }
                    }
                }
            }
            return i - j;
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.String
 * JD-Core Version:    0.6.2
 */