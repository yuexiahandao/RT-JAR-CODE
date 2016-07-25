/*      */ package java.util;
/*      */ 
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.Flushable;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.math.MathContext;
/*      */ import java.math.RoundingMode;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.DecimalFormatSymbols;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import sun.misc.FormattedFloatingDecimal;
/*      */ import sun.misc.FormattedFloatingDecimal.Form;
/*      */ import sun.misc.FpUtils;
/*      */ 
/*      */ public final class Formatter
/*      */   implements Closeable, Flushable
/*      */ {
/*      */   private Appendable a;
/*      */   private final Locale l;
/*      */   private IOException lastException;
/*      */   private final char zero;
/*      */   private static double scaleUp;
/*      */   private static final int MAX_FD_CHARS = 30;
/*      */   private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
/* 2508 */   private static Pattern fsPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
/*      */ 
/*      */   private static Charset toCharset(String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/* 1857 */     Objects.requireNonNull(paramString, "charsetName");
/*      */     try {
/* 1859 */       return Charset.forName(paramString);
/*      */     } catch (IllegalCharsetNameException|UnsupportedCharsetException localIllegalCharsetNameException) {
/*      */     }
/* 1862 */     throw new UnsupportedEncodingException(paramString);
/*      */   }
/*      */ 
/*      */   private static final Appendable nonNullAppendable(Appendable paramAppendable)
/*      */   {
/* 1867 */     if (paramAppendable == null) {
/* 1868 */       return new StringBuilder();
/*      */     }
/* 1870 */     return paramAppendable;
/*      */   }
/*      */ 
/*      */   private Formatter(Locale paramLocale, Appendable paramAppendable)
/*      */   {
/* 1875 */     this.a = paramAppendable;
/* 1876 */     this.l = paramLocale;
/* 1877 */     this.zero = getZero(paramLocale);
/*      */   }
/*      */ 
/*      */   private Formatter(Charset paramCharset, Locale paramLocale, File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/* 1883 */     this(paramLocale, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile), paramCharset)));
/*      */   }
/*      */ 
/*      */   public Formatter()
/*      */   {
/* 1898 */     this(Locale.getDefault(Locale.Category.FORMAT), new StringBuilder());
/*      */   }
/*      */ 
/*      */   public Formatter(Appendable paramAppendable)
/*      */   {
/* 1912 */     this(Locale.getDefault(Locale.Category.FORMAT), nonNullAppendable(paramAppendable));
/*      */   }
/*      */ 
/*      */   public Formatter(Locale paramLocale)
/*      */   {
/* 1929 */     this(paramLocale, new StringBuilder());
/*      */   }
/*      */ 
/*      */   public Formatter(Appendable paramAppendable, Locale paramLocale)
/*      */   {
/* 1945 */     this(paramLocale, nonNullAppendable(paramAppendable));
/*      */   }
/*      */ 
/*      */   public Formatter(String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/* 1976 */     this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramString))));
/*      */   }
/*      */ 
/*      */   public Formatter(String paramString1, String paramString2)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/* 2013 */     this(paramString1, paramString2, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public Formatter(String paramString1, String paramString2, Locale paramLocale)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/* 2052 */     this(toCharset(paramString2), paramLocale, new File(paramString1));
/*      */   }
/*      */ 
/*      */   public Formatter(File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/* 2083 */     this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile))));
/*      */   }
/*      */ 
/*      */   public Formatter(File paramFile, String paramString)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/* 2120 */     this(paramFile, paramString, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public Formatter(File paramFile, String paramString, Locale paramLocale)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/* 2159 */     this(toCharset(paramString), paramLocale, paramFile);
/*      */   }
/*      */ 
/*      */   public Formatter(PrintStream paramPrintStream)
/*      */   {
/* 2176 */     this(Locale.getDefault(Locale.Category.FORMAT), (Appendable)Objects.requireNonNull(paramPrintStream));
/*      */   }
/*      */ 
/*      */   public Formatter(OutputStream paramOutputStream)
/*      */   {
/* 2195 */     this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(paramOutputStream)));
/*      */   }
/*      */ 
/*      */   public Formatter(OutputStream paramOutputStream, String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/* 2220 */     this(paramOutputStream, paramString, Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public Formatter(OutputStream paramOutputStream, String paramString, Locale paramLocale)
/*      */     throws UnsupportedEncodingException
/*      */   {
/* 2246 */     this(paramLocale, new BufferedWriter(new OutputStreamWriter(paramOutputStream, paramString)));
/*      */   }
/*      */ 
/*      */   private static char getZero(Locale paramLocale) {
/* 2250 */     if ((paramLocale != null) && (!paramLocale.equals(Locale.US))) {
/* 2251 */       DecimalFormatSymbols localDecimalFormatSymbols = DecimalFormatSymbols.getInstance(paramLocale);
/* 2252 */       return localDecimalFormatSymbols.getZeroDigit();
/*      */     }
/* 2254 */     return '0';
/*      */   }
/*      */ 
/*      */   public Locale locale()
/*      */   {
/* 2272 */     ensureOpen();
/* 2273 */     return this.l;
/*      */   }
/*      */ 
/*      */   public Appendable out()
/*      */   {
/* 2286 */     ensureOpen();
/* 2287 */     return this.a;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2322 */     ensureOpen();
/* 2323 */     return this.a.toString();
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */   {
/* 2338 */     ensureOpen();
/* 2339 */     if ((this.a instanceof Flushable))
/*      */       try {
/* 2341 */         ((Flushable)this.a).flush();
/*      */       } catch (IOException localIOException) {
/* 2343 */         this.lastException = localIOException;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 2361 */     if (this.a == null)
/* 2362 */       return;
/*      */     try {
/* 2364 */       if ((this.a instanceof Flushable))
/* 2365 */         ((Flushable)this.a).close();
/*      */     } catch (IOException localIOException) {
/* 2367 */       this.lastException = localIOException;
/*      */     } finally {
/* 2369 */       this.a = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureOpen() {
/* 2374 */     if (this.a == null)
/* 2375 */       throw new FormatterClosedException();
/*      */   }
/*      */ 
/*      */   public IOException ioException()
/*      */   {
/* 2389 */     return this.lastException;
/*      */   }
/*      */ 
/*      */   public Formatter format(String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2423 */     return format(this.l, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public Formatter format(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 2462 */     ensureOpen();
/*      */ 
/* 2465 */     int i = -1;
/*      */ 
/* 2467 */     int j = -1;
/*      */ 
/* 2469 */     FormatString[] arrayOfFormatString = parse(paramString);
/* 2470 */     for (int k = 0; k < arrayOfFormatString.length; k++) {
/* 2471 */       FormatString localFormatString = arrayOfFormatString[k];
/* 2472 */       int m = localFormatString.index();
/*      */       try {
/* 2474 */         switch (m) {
/*      */         case -2:
/* 2476 */           localFormatString.print(null, paramLocale);
/* 2477 */           break;
/*      */         case -1:
/* 2479 */           if ((i < 0) || ((paramArrayOfObject != null) && (i > paramArrayOfObject.length - 1)))
/* 2480 */             throw new MissingFormatArgumentException(localFormatString.toString());
/* 2481 */           localFormatString.print(paramArrayOfObject == null ? null : paramArrayOfObject[i], paramLocale);
/* 2482 */           break;
/*      */         case 0:
/* 2484 */           j++;
/* 2485 */           i = j;
/* 2486 */           if ((paramArrayOfObject != null) && (j > paramArrayOfObject.length - 1))
/* 2487 */             throw new MissingFormatArgumentException(localFormatString.toString());
/* 2488 */           localFormatString.print(paramArrayOfObject == null ? null : paramArrayOfObject[j], paramLocale);
/* 2489 */           break;
/*      */         default:
/* 2491 */           i = m - 1;
/* 2492 */           if ((paramArrayOfObject != null) && (i > paramArrayOfObject.length - 1))
/* 2493 */             throw new MissingFormatArgumentException(localFormatString.toString());
/* 2494 */           localFormatString.print(paramArrayOfObject == null ? null : paramArrayOfObject[i], paramLocale);
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException) {
/* 2498 */         this.lastException = localIOException;
/*      */       }
/*      */     }
/* 2501 */     return this;
/*      */   }
/*      */ 
/*      */   private FormatString[] parse(String paramString)
/*      */   {
/* 2514 */     ArrayList localArrayList = new ArrayList();
/* 2515 */     Matcher localMatcher = fsPattern.matcher(paramString);
/* 2516 */     int i = 0; for (int j = paramString.length(); i < j; ) {
/* 2517 */       if (localMatcher.find(i))
/*      */       {
/* 2521 */         if (localMatcher.start() != i)
/*      */         {
/* 2523 */           checkText(paramString, i, localMatcher.start());
/*      */ 
/* 2525 */           localArrayList.add(new FixedString(paramString.substring(i, localMatcher.start())));
/*      */         }
/*      */ 
/* 2528 */         localArrayList.add(new FormatSpecifier(localMatcher));
/* 2529 */         i = localMatcher.end();
/*      */       }
/*      */       else
/*      */       {
/* 2533 */         checkText(paramString, i, j);
/*      */ 
/* 2535 */         localArrayList.add(new FixedString(paramString.substring(i)));
/*      */       }
/*      */     }
/*      */ 
/* 2539 */     return (FormatString[])localArrayList.toArray(new FormatString[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   private static void checkText(String paramString, int paramInt1, int paramInt2) {
/* 2543 */     for (int i = paramInt1; i < paramInt2; i++)
/*      */     {
/* 2545 */       if (paramString.charAt(i) == '%') {
/* 2546 */         char c = i == paramInt2 - 1 ? '%' : paramString.charAt(i + 1);
/* 2547 */         throw new UnknownFormatConversionException(String.valueOf(c));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum BigDecimalLayoutForm
/*      */   {
/* 2567 */     SCIENTIFIC, DECIMAL_FLOAT;
/*      */   }
/*      */ 
/*      */   private static class Conversion
/*      */   {
/*      */     static final char DECIMAL_INTEGER = 'd';
/*      */     static final char OCTAL_INTEGER = 'o';
/*      */     static final char HEXADECIMAL_INTEGER = 'x';
/*      */     static final char HEXADECIMAL_INTEGER_UPPER = 'X';
/*      */     static final char SCIENTIFIC = 'e';
/*      */     static final char SCIENTIFIC_UPPER = 'E';
/*      */     static final char GENERAL = 'g';
/*      */     static final char GENERAL_UPPER = 'G';
/*      */     static final char DECIMAL_FLOAT = 'f';
/*      */     static final char HEXADECIMAL_FLOAT = 'a';
/*      */     static final char HEXADECIMAL_FLOAT_UPPER = 'A';
/*      */     static final char CHARACTER = 'c';
/*      */     static final char CHARACTER_UPPER = 'C';
/*      */     static final char DATE_TIME = 't';
/*      */     static final char DATE_TIME_UPPER = 'T';
/*      */     static final char BOOLEAN = 'b';
/*      */     static final char BOOLEAN_UPPER = 'B';
/*      */     static final char STRING = 's';
/*      */     static final char STRING_UPPER = 'S';
/*      */     static final char HASHCODE = 'h';
/*      */     static final char HASHCODE_UPPER = 'H';
/*      */     static final char LINE_SEPARATOR = 'n';
/*      */     static final char PERCENT_SIGN = '%';
/*      */ 
/*      */     static boolean isValid(char paramChar)
/*      */     {
/* 4271 */       return (isGeneral(paramChar)) || (isInteger(paramChar)) || (isFloat(paramChar)) || (isText(paramChar)) || (paramChar == 't') || (isCharacter(paramChar));
/*      */     }
/*      */ 
/*      */     static boolean isGeneral(char paramChar)
/*      */     {
/* 4277 */       switch (paramChar) {
/*      */       case 'B':
/*      */       case 'H':
/*      */       case 'S':
/*      */       case 'b':
/*      */       case 'h':
/*      */       case 's':
/* 4284 */         return true;
/*      */       }
/* 4286 */       return false;
/*      */     }
/*      */ 
/*      */     static boolean isCharacter(char paramChar)
/*      */     {
/* 4292 */       switch (paramChar) {
/*      */       case 'C':
/*      */       case 'c':
/* 4295 */         return true;
/*      */       }
/* 4297 */       return false;
/*      */     }
/*      */ 
/*      */     static boolean isInteger(char paramChar)
/*      */     {
/* 4303 */       switch (paramChar) {
/*      */       case 'X':
/*      */       case 'd':
/*      */       case 'o':
/*      */       case 'x':
/* 4308 */         return true;
/*      */       }
/* 4310 */       return false;
/*      */     }
/*      */ 
/*      */     static boolean isFloat(char paramChar)
/*      */     {
/* 4316 */       switch (paramChar) {
/*      */       case 'A':
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'a':
/*      */       case 'e':
/*      */       case 'f':
/*      */       case 'g':
/* 4324 */         return true;
/*      */       }
/* 4326 */       return false;
/*      */     }
/*      */ 
/*      */     static boolean isText(char paramChar)
/*      */     {
/* 4332 */       switch (paramChar) {
/*      */       case '%':
/*      */       case 'n':
/* 4335 */         return true;
/*      */       }
/* 4337 */       return false; }  } 
/*      */   private static class DateTime { static final char HOUR_OF_DAY_0 = 'H';
/*      */     static final char HOUR_0 = 'I';
/*      */     static final char HOUR_OF_DAY = 'k';
/*      */     static final char HOUR = 'l';
/*      */     static final char MINUTE = 'M';
/*      */     static final char NANOSECOND = 'N';
/*      */     static final char MILLISECOND = 'L';
/*      */     static final char MILLISECOND_SINCE_EPOCH = 'Q';
/*      */     static final char AM_PM = 'p';
/*      */     static final char SECONDS_SINCE_EPOCH = 's';
/*      */     static final char SECOND = 'S';
/*      */     static final char TIME = 'T';
/*      */     static final char ZONE_NUMERIC = 'z';
/*      */     static final char ZONE = 'Z';
/*      */     static final char NAME_OF_DAY_ABBREV = 'a';
/*      */     static final char NAME_OF_DAY = 'A';
/*      */     static final char NAME_OF_MONTH_ABBREV = 'b';
/*      */     static final char NAME_OF_MONTH = 'B';
/*      */     static final char CENTURY = 'C';
/*      */     static final char DAY_OF_MONTH_0 = 'd';
/*      */     static final char DAY_OF_MONTH = 'e';
/*      */     static final char NAME_OF_MONTH_ABBREV_X = 'h';
/*      */     static final char DAY_OF_YEAR = 'j';
/*      */     static final char MONTH = 'm';
/*      */     static final char YEAR_2 = 'y';
/*      */     static final char YEAR_4 = 'Y';
/*      */     static final char TIME_12_HOUR = 'r';
/*      */     static final char TIME_24_HOUR = 'R';
/*      */     static final char DATE_TIME = 'c';
/*      */     static final char DATE = 'D';
/*      */     static final char ISO_STANDARD_DATE = 'F';
/*      */ 
/* 4390 */     static boolean isValid(char paramChar) { switch (paramChar)
/*      */       {
/*      */       case 'A':
/*      */       case 'B':
/*      */       case 'C':
/*      */       case 'D':
/*      */       case 'F':
/*      */       case 'H':
/*      */       case 'I':
/*      */       case 'L':
/*      */       case 'M':
/*      */       case 'N':
/*      */       case 'Q':
/*      */       case 'R':
/*      */       case 'S':
/*      */       case 'T':
/*      */       case 'Y':
/*      */       case 'Z':
/*      */       case 'a':
/*      */       case 'b':
/*      */       case 'c':
/*      */       case 'd':
/*      */       case 'e':
/*      */       case 'h':
/*      */       case 'j':
/*      */       case 'k':
/*      */       case 'l':
/*      */       case 'm':
/*      */       case 'p':
/*      */       case 'r':
/*      */       case 's':
/*      */       case 'y':
/*      */       case 'z':
/* 4435 */         return true;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'J':
/*      */       case 'K':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*      */       case '[':
/*      */       case '\\':
/*      */       case ']':
/*      */       case '^':
/*      */       case '_':
/*      */       case '`':
/*      */       case 'f':
/*      */       case 'g':
/*      */       case 'i':
/*      */       case 'n':
/*      */       case 'o':
/*      */       case 'q':
/*      */       case 't':
/*      */       case 'u':
/*      */       case 'v':
/*      */       case 'w':
/* 4437 */       case 'x': } return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FixedString
/*      */     implements Formatter.FormatString
/*      */   {
/*      */     private String s;
/*      */ 
/*      */     FixedString(String arg2)
/*      */     {
/*      */       Object localObject;
/* 2560 */       this.s = localObject; } 
/* 2561 */     public int index() { return -2; } 
/*      */     public void print(Object paramObject, Locale paramLocale) throws IOException {
/* 2563 */       Formatter.this.a.append(this.s); } 
/* 2564 */     public String toString() { return this.s; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class Flags
/*      */   {
/*      */     private int flags;
/* 4137 */     static final Flags NONE = new Flags(0);
/*      */ 
/* 4140 */     static final Flags LEFT_JUSTIFY = new Flags(1);
/* 4141 */     static final Flags UPPERCASE = new Flags(2);
/* 4142 */     static final Flags ALTERNATE = new Flags(4);
/*      */ 
/* 4145 */     static final Flags PLUS = new Flags(8);
/* 4146 */     static final Flags LEADING_SPACE = new Flags(16);
/* 4147 */     static final Flags ZERO_PAD = new Flags(32);
/* 4148 */     static final Flags GROUP = new Flags(64);
/* 4149 */     static final Flags PARENTHESES = new Flags(128);
/*      */ 
/* 4152 */     static final Flags PREVIOUS = new Flags(256);
/*      */ 
/*      */     private Flags(int paramInt) {
/* 4155 */       this.flags = paramInt;
/*      */     }
/*      */ 
/*      */     public int valueOf() {
/* 4159 */       return this.flags;
/*      */     }
/*      */ 
/*      */     public boolean contains(Flags paramFlags) {
/* 4163 */       return (this.flags & paramFlags.valueOf()) == paramFlags.valueOf();
/*      */     }
/*      */ 
/*      */     public Flags dup() {
/* 4167 */       return new Flags(this.flags);
/*      */     }
/*      */ 
/*      */     private Flags add(Flags paramFlags) {
/* 4171 */       this.flags |= paramFlags.valueOf();
/* 4172 */       return this;
/*      */     }
/*      */ 
/*      */     public Flags remove(Flags paramFlags) {
/* 4176 */       this.flags &= (paramFlags.valueOf() ^ 0xFFFFFFFF);
/* 4177 */       return this;
/*      */     }
/*      */ 
/*      */     public static Flags parse(String paramString) {
/* 4181 */       char[] arrayOfChar = paramString.toCharArray();
/* 4182 */       Flags localFlags1 = new Flags(0);
/* 4183 */       for (int i = 0; i < arrayOfChar.length; i++) {
/* 4184 */         Flags localFlags2 = parse(arrayOfChar[i]);
/* 4185 */         if (localFlags1.contains(localFlags2))
/* 4186 */           throw new DuplicateFormatFlagsException(localFlags2.toString());
/* 4187 */         localFlags1.add(localFlags2);
/*      */       }
/* 4189 */       return localFlags1;
/*      */     }
/*      */ 
/*      */     private static Flags parse(char paramChar)
/*      */     {
/* 4194 */       switch (paramChar) { case '-':
/* 4195 */         return LEFT_JUSTIFY;
/*      */       case '#':
/* 4196 */         return ALTERNATE;
/*      */       case '+':
/* 4197 */         return PLUS;
/*      */       case ' ':
/* 4198 */         return LEADING_SPACE;
/*      */       case '0':
/* 4199 */         return ZERO_PAD;
/*      */       case ',':
/* 4200 */         return GROUP;
/*      */       case '(':
/* 4201 */         return PARENTHESES;
/*      */       case '<':
/* 4202 */         return PREVIOUS;
/*      */       case '!':
/*      */       case '"':
/*      */       case '$':
/*      */       case '%':
/*      */       case '&':
/*      */       case '\'':
/*      */       case ')':
/*      */       case '*':
/*      */       case '.':
/*      */       case '/':
/*      */       case '1':
/*      */       case '2':
/*      */       case '3':
/*      */       case '4':
/*      */       case '5':
/*      */       case '6':
/*      */       case '7':
/*      */       case '8':
/*      */       case '9':
/*      */       case ':':
/* 4204 */       case ';': } throw new UnknownFormatFlagsException(String.valueOf(paramChar));
/*      */     }
/*      */ 
/*      */     public static String toString(Flags paramFlags)
/*      */     {
/* 4210 */       return paramFlags.toString();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 4214 */       StringBuilder localStringBuilder = new StringBuilder();
/* 4215 */       if (contains(LEFT_JUSTIFY)) localStringBuilder.append('-');
/* 4216 */       if (contains(UPPERCASE)) localStringBuilder.append('^');
/* 4217 */       if (contains(ALTERNATE)) localStringBuilder.append('#');
/* 4218 */       if (contains(PLUS)) localStringBuilder.append('+');
/* 4219 */       if (contains(LEADING_SPACE)) localStringBuilder.append(' ');
/* 4220 */       if (contains(ZERO_PAD)) localStringBuilder.append('0');
/* 4221 */       if (contains(GROUP)) localStringBuilder.append(',');
/* 4222 */       if (contains(PARENTHESES)) localStringBuilder.append('(');
/* 4223 */       if (contains(PREVIOUS)) localStringBuilder.append('<');
/* 4224 */       return localStringBuilder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FormatSpecifier
/*      */     implements Formatter.FormatString
/*      */   {
/* 2570 */     private int index = -1;
/* 2571 */     private Formatter.Flags f = Formatter.Flags.NONE;
/*      */     private int width;
/*      */     private int precision;
/* 2574 */     private boolean dt = false;
/*      */     private char c;
/*      */ 
/*      */     private int index(String paramString)
/*      */     {
/* 2578 */       if (paramString != null)
/*      */         try {
/* 2580 */           this.index = Integer.parseInt(paramString.substring(0, paramString.length() - 1));
/*      */         } catch (NumberFormatException localNumberFormatException) {
/* 2582 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */         }
/*      */       else {
/* 2585 */         this.index = 0;
/*      */       }
/* 2587 */       return this.index;
/*      */     }
/*      */ 
/*      */     public int index() {
/* 2591 */       return this.index;
/*      */     }
/*      */ 
/*      */     private Formatter.Flags flags(String paramString) {
/* 2595 */       this.f = Formatter.Flags.parse(paramString);
/* 2596 */       if (this.f.contains(Formatter.Flags.PREVIOUS))
/* 2597 */         this.index = -1;
/* 2598 */       return this.f;
/*      */     }
/*      */ 
/*      */     Formatter.Flags flags() {
/* 2602 */       return this.f;
/*      */     }
/*      */ 
/*      */     private int width(String paramString) {
/* 2606 */       this.width = -1;
/* 2607 */       if (paramString != null) {
/*      */         try {
/* 2609 */           this.width = Integer.parseInt(paramString);
/* 2610 */           if (this.width < 0)
/* 2611 */             throw new IllegalFormatWidthException(this.width);
/*      */         } catch (NumberFormatException localNumberFormatException) {
/* 2613 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */         }
/*      */       }
/* 2616 */       return this.width;
/*      */     }
/*      */ 
/*      */     int width() {
/* 2620 */       return this.width;
/*      */     }
/*      */ 
/*      */     private int precision(String paramString) {
/* 2624 */       this.precision = -1;
/* 2625 */       if (paramString != null) {
/*      */         try
/*      */         {
/* 2628 */           this.precision = Integer.parseInt(paramString.substring(1));
/* 2629 */           if (this.precision < 0)
/* 2630 */             throw new IllegalFormatPrecisionException(this.precision);
/*      */         } catch (NumberFormatException localNumberFormatException) {
/* 2632 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */         }
/*      */       }
/* 2635 */       return this.precision;
/*      */     }
/*      */ 
/*      */     int precision() {
/* 2639 */       return this.precision;
/*      */     }
/*      */ 
/*      */     private char conversion(String paramString) {
/* 2643 */       this.c = paramString.charAt(0);
/* 2644 */       if (!this.dt) {
/* 2645 */         if (!Formatter.Conversion.isValid(this.c))
/* 2646 */           throw new UnknownFormatConversionException(String.valueOf(this.c));
/* 2647 */         if (Character.isUpperCase(this.c))
/* 2648 */           this.f.add(Formatter.Flags.UPPERCASE);
/* 2649 */         this.c = Character.toLowerCase(this.c);
/* 2650 */         if (Formatter.Conversion.isText(this.c))
/* 2651 */           this.index = -2;
/*      */       }
/* 2653 */       return this.c;
/*      */     }
/*      */ 
/*      */     private char conversion() {
/* 2657 */       return this.c;
/*      */     }
/*      */ 
/*      */     FormatSpecifier(Matcher arg2) {
/* 2661 */       int i = 1;
/*      */       Object localObject;
/* 2663 */       index(localObject.group(i++));
/* 2664 */       flags(localObject.group(i++));
/* 2665 */       width(localObject.group(i++));
/* 2666 */       precision(localObject.group(i++));
/*      */ 
/* 2668 */       String str = localObject.group(i++);
/* 2669 */       if (str != null) {
/* 2670 */         this.dt = true;
/* 2671 */         if (str.equals("T")) {
/* 2672 */           this.f.add(Formatter.Flags.UPPERCASE);
/*      */         }
/*      */       }
/* 2675 */       conversion(localObject.group(i));
/*      */ 
/* 2677 */       if (this.dt)
/* 2678 */         checkDateTime();
/* 2679 */       else if (Formatter.Conversion.isGeneral(this.c))
/* 2680 */         checkGeneral();
/* 2681 */       else if (Formatter.Conversion.isCharacter(this.c))
/* 2682 */         checkCharacter();
/* 2683 */       else if (Formatter.Conversion.isInteger(this.c))
/* 2684 */         checkInteger();
/* 2685 */       else if (Formatter.Conversion.isFloat(this.c))
/* 2686 */         checkFloat();
/* 2687 */       else if (Formatter.Conversion.isText(this.c))
/* 2688 */         checkText();
/*      */       else
/* 2690 */         throw new UnknownFormatConversionException(String.valueOf(this.c));
/*      */     }
/*      */ 
/*      */     public void print(Object paramObject, Locale paramLocale) throws IOException {
/* 2694 */       if (this.dt) {
/* 2695 */         printDateTime(paramObject, paramLocale);
/* 2696 */         return;
/*      */       }
/* 2698 */       switch (this.c) {
/*      */       case 'd':
/*      */       case 'o':
/*      */       case 'x':
/* 2702 */         printInteger(paramObject, paramLocale);
/* 2703 */         break;
/*      */       case 'a':
/*      */       case 'e':
/*      */       case 'f':
/*      */       case 'g':
/* 2708 */         printFloat(paramObject, paramLocale);
/* 2709 */         break;
/*      */       case 'C':
/*      */       case 'c':
/* 2712 */         printCharacter(paramObject);
/* 2713 */         break;
/*      */       case 'b':
/* 2715 */         printBoolean(paramObject);
/* 2716 */         break;
/*      */       case 's':
/* 2718 */         printString(paramObject, paramLocale);
/* 2719 */         break;
/*      */       case 'h':
/* 2721 */         printHashCode(paramObject);
/* 2722 */         break;
/*      */       case 'n':
/* 2724 */         Formatter.this.a.append(System.lineSeparator());
/* 2725 */         break;
/*      */       case '%':
/* 2727 */         Formatter.this.a.append('%');
/* 2728 */         break;
/*      */       default:
/* 2730 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/*      */     }
/*      */ 
/*      */     private void printInteger(Object paramObject, Locale paramLocale) throws IOException {
/* 2735 */       if (paramObject == null)
/* 2736 */         print("null");
/* 2737 */       else if ((paramObject instanceof Byte))
/* 2738 */         print(((Byte)paramObject).byteValue(), paramLocale);
/* 2739 */       else if ((paramObject instanceof Short))
/* 2740 */         print(((Short)paramObject).shortValue(), paramLocale);
/* 2741 */       else if ((paramObject instanceof Integer))
/* 2742 */         print(((Integer)paramObject).intValue(), paramLocale);
/* 2743 */       else if ((paramObject instanceof Long))
/* 2744 */         print(((Long)paramObject).longValue(), paramLocale);
/* 2745 */       else if ((paramObject instanceof BigInteger))
/* 2746 */         print((BigInteger)paramObject, paramLocale);
/*      */       else
/* 2748 */         failConversion(this.c, paramObject);
/*      */     }
/*      */ 
/*      */     private void printFloat(Object paramObject, Locale paramLocale) throws IOException {
/* 2752 */       if (paramObject == null)
/* 2753 */         print("null");
/* 2754 */       else if ((paramObject instanceof Float))
/* 2755 */         print(((Float)paramObject).floatValue(), paramLocale);
/* 2756 */       else if ((paramObject instanceof Double))
/* 2757 */         print(((Double)paramObject).doubleValue(), paramLocale);
/* 2758 */       else if ((paramObject instanceof BigDecimal))
/* 2759 */         print((BigDecimal)paramObject, paramLocale);
/*      */       else
/* 2761 */         failConversion(this.c, paramObject);
/*      */     }
/*      */ 
/*      */     private void printDateTime(Object paramObject, Locale paramLocale) throws IOException {
/* 2765 */       if (paramObject == null) {
/* 2766 */         print("null");
/* 2767 */         return;
/*      */       }
/* 2769 */       Calendar localCalendar = null;
/*      */ 
/* 2773 */       if ((paramObject instanceof Long))
/*      */       {
/* 2776 */         localCalendar = Calendar.getInstance(paramLocale == null ? Locale.US : paramLocale);
/* 2777 */         localCalendar.setTimeInMillis(((Long)paramObject).longValue());
/* 2778 */       } else if ((paramObject instanceof Date))
/*      */       {
/* 2781 */         localCalendar = Calendar.getInstance(paramLocale == null ? Locale.US : paramLocale);
/* 2782 */         localCalendar.setTime((Date)paramObject);
/* 2783 */       } else if ((paramObject instanceof Calendar)) {
/* 2784 */         localCalendar = (Calendar)((Calendar)paramObject).clone();
/* 2785 */         localCalendar.setLenient(true);
/*      */       } else {
/* 2787 */         failConversion(this.c, paramObject);
/*      */       }
/*      */ 
/* 2791 */       print(localCalendar, this.c, paramLocale);
/*      */     }
/*      */ 
/*      */     private void printCharacter(Object paramObject) throws IOException {
/* 2795 */       if (paramObject == null) {
/* 2796 */         print("null");
/* 2797 */         return;
/*      */       }
/* 2799 */       String str = null;
/* 2800 */       if ((paramObject instanceof Character)) {
/* 2801 */         str = ((Character)paramObject).toString();
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/* 2802 */         if ((paramObject instanceof Byte)) {
/* 2803 */           i = ((Byte)paramObject).byteValue();
/* 2804 */           if (Character.isValidCodePoint(i))
/* 2805 */             str = new String(Character.toChars(i));
/*      */           else
/* 2807 */             throw new IllegalFormatCodePointException(i);
/* 2808 */         } else if ((paramObject instanceof Short)) {
/* 2809 */           i = ((Short)paramObject).shortValue();
/* 2810 */           if (Character.isValidCodePoint(i))
/* 2811 */             str = new String(Character.toChars(i));
/*      */           else
/* 2813 */             throw new IllegalFormatCodePointException(i);
/* 2814 */         } else if ((paramObject instanceof Integer)) {
/* 2815 */           i = ((Integer)paramObject).intValue();
/* 2816 */           if (Character.isValidCodePoint(i))
/* 2817 */             str = new String(Character.toChars(i));
/*      */           else
/* 2819 */             throw new IllegalFormatCodePointException(i);
/*      */         } else {
/* 2821 */           failConversion(this.c, paramObject);
/*      */         }
/*      */       }
/* 2823 */       print(str);
/*      */     }
/*      */ 
/*      */     private void printString(Object paramObject, Locale paramLocale) throws IOException {
/* 2827 */       if ((paramObject instanceof Formattable)) {
/* 2828 */         Formatter localFormatter = Formatter.this;
/* 2829 */         if (localFormatter.locale() != paramLocale)
/* 2830 */           localFormatter = new Formatter(localFormatter.out(), paramLocale);
/* 2831 */         ((Formattable)paramObject).formatTo(localFormatter, this.f.valueOf(), this.width, this.precision);
/*      */       } else {
/* 2833 */         if (this.f.contains(Formatter.Flags.ALTERNATE))
/* 2834 */           failMismatch(Formatter.Flags.ALTERNATE, 's');
/* 2835 */         if (paramObject == null)
/* 2836 */           print("null");
/*      */         else
/* 2838 */           print(paramObject.toString());
/*      */       }
/*      */     }
/*      */ 
/*      */     private void printBoolean(Object paramObject)
/*      */       throws IOException
/*      */     {
/*      */       String str;
/* 2844 */       if (paramObject != null) {
/* 2845 */         str = (paramObject instanceof Boolean) ? ((Boolean)paramObject).toString() : Boolean.toString(true);
/*      */       }
/*      */       else
/*      */       {
/* 2849 */         str = Boolean.toString(false);
/* 2850 */       }print(str);
/*      */     }
/*      */ 
/*      */     private void printHashCode(Object paramObject) throws IOException {
/* 2854 */       String str = paramObject == null ? "null" : Integer.toHexString(paramObject.hashCode());
/*      */ 
/* 2857 */       print(str);
/*      */     }
/*      */ 
/*      */     private void print(String paramString) throws IOException {
/* 2861 */       if ((this.precision != -1) && (this.precision < paramString.length()))
/* 2862 */         paramString = paramString.substring(0, this.precision);
/* 2863 */       if (this.f.contains(Formatter.Flags.UPPERCASE))
/* 2864 */         paramString = paramString.toUpperCase();
/* 2865 */       Formatter.this.a.append(justify(paramString));
/*      */     }
/*      */ 
/*      */     private String justify(String paramString) {
/* 2869 */       if (this.width == -1)
/* 2870 */         return paramString;
/* 2871 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2872 */       boolean bool = this.f.contains(Formatter.Flags.LEFT_JUSTIFY);
/* 2873 */       int i = this.width - paramString.length();
/*      */       int j;
/* 2874 */       if (!bool)
/* 2875 */         for (j = 0; j < i; j++) localStringBuilder.append(' ');
/* 2876 */       localStringBuilder.append(paramString);
/* 2877 */       if (bool)
/* 2878 */         for (j = 0; j < i; j++) localStringBuilder.append(' ');
/* 2879 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 2883 */       StringBuilder localStringBuilder = new StringBuilder(37);
/*      */ 
/* 2885 */       Formatter.Flags localFlags = this.f.dup().remove(Formatter.Flags.UPPERCASE);
/* 2886 */       localStringBuilder.append(localFlags.toString());
/* 2887 */       if (this.index > 0)
/* 2888 */         localStringBuilder.append(this.index).append('$');
/* 2889 */       if (this.width != -1)
/* 2890 */         localStringBuilder.append(this.width);
/* 2891 */       if (this.precision != -1)
/* 2892 */         localStringBuilder.append('.').append(this.precision);
/* 2893 */       if (this.dt)
/* 2894 */         localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? 'T' : 't');
/* 2895 */       localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? Character.toUpperCase(this.c) : this.c);
/*      */ 
/* 2897 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     private void checkGeneral() {
/* 2901 */       if (((this.c == 'b') || (this.c == 'h')) && (this.f.contains(Formatter.Flags.ALTERNATE)))
/*      */       {
/* 2903 */         failMismatch(Formatter.Flags.ALTERNATE, this.c);
/*      */       }
/* 2905 */       if ((this.width == -1) && (this.f.contains(Formatter.Flags.LEFT_JUSTIFY)))
/* 2906 */         throw new MissingFormatWidthException(toString());
/* 2907 */       checkBadFlags(new Formatter.Flags[] { Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES });
/*      */     }
/*      */ 
/*      */     private void checkDateTime()
/*      */     {
/* 2912 */       if (this.precision != -1)
/* 2913 */         throw new IllegalFormatPrecisionException(this.precision);
/* 2914 */       if (!Formatter.DateTime.isValid(this.c))
/* 2915 */         throw new UnknownFormatConversionException("t" + this.c);
/* 2916 */       checkBadFlags(new Formatter.Flags[] { Formatter.Flags.ALTERNATE, Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES });
/*      */ 
/* 2919 */       if ((this.width == -1) && (this.f.contains(Formatter.Flags.LEFT_JUSTIFY)))
/* 2920 */         throw new MissingFormatWidthException(toString());
/*      */     }
/*      */ 
/*      */     private void checkCharacter() {
/* 2924 */       if (this.precision != -1)
/* 2925 */         throw new IllegalFormatPrecisionException(this.precision);
/* 2926 */       checkBadFlags(new Formatter.Flags[] { Formatter.Flags.ALTERNATE, Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES });
/*      */ 
/* 2929 */       if ((this.width == -1) && (this.f.contains(Formatter.Flags.LEFT_JUSTIFY)))
/* 2930 */         throw new MissingFormatWidthException(toString());
/*      */     }
/*      */ 
/*      */     private void checkInteger() {
/* 2934 */       checkNumeric();
/* 2935 */       if (this.precision != -1) {
/* 2936 */         throw new IllegalFormatPrecisionException(this.precision);
/*      */       }
/* 2938 */       if (this.c == 'd')
/* 2939 */         checkBadFlags(new Formatter.Flags[] { Formatter.Flags.ALTERNATE });
/* 2940 */       else if (this.c == 'o')
/* 2941 */         checkBadFlags(new Formatter.Flags[] { Formatter.Flags.GROUP });
/*      */       else
/* 2943 */         checkBadFlags(new Formatter.Flags[] { Formatter.Flags.GROUP });
/*      */     }
/*      */ 
/*      */     private void checkBadFlags(Formatter.Flags[] paramArrayOfFlags) {
/* 2947 */       for (int i = 0; i < paramArrayOfFlags.length; i++)
/* 2948 */         if (this.f.contains(paramArrayOfFlags[i]))
/* 2949 */           failMismatch(paramArrayOfFlags[i], this.c);
/*      */     }
/*      */ 
/*      */     private void checkFloat() {
/* 2953 */       checkNumeric();
/* 2954 */       if (this.c != 'f')
/* 2955 */         if (this.c == 'a')
/* 2956 */           checkBadFlags(new Formatter.Flags[] { Formatter.Flags.PARENTHESES, Formatter.Flags.GROUP });
/* 2957 */         else if (this.c == 'e')
/* 2958 */           checkBadFlags(new Formatter.Flags[] { Formatter.Flags.GROUP });
/* 2959 */         else if (this.c == 'g')
/* 2960 */           checkBadFlags(new Formatter.Flags[] { Formatter.Flags.ALTERNATE });
/*      */     }
/*      */ 
/*      */     private void checkNumeric()
/*      */     {
/* 2965 */       if ((this.width != -1) && (this.width < 0)) {
/* 2966 */         throw new IllegalFormatWidthException(this.width);
/*      */       }
/* 2968 */       if ((this.precision != -1) && (this.precision < 0)) {
/* 2969 */         throw new IllegalFormatPrecisionException(this.precision);
/*      */       }
/*      */ 
/* 2972 */       if ((this.width == -1) && ((this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) || (this.f.contains(Formatter.Flags.ZERO_PAD))))
/*      */       {
/* 2974 */         throw new MissingFormatWidthException(toString());
/*      */       }
/*      */ 
/* 2977 */       if (((this.f.contains(Formatter.Flags.PLUS)) && (this.f.contains(Formatter.Flags.LEADING_SPACE))) || ((this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) && (this.f.contains(Formatter.Flags.ZERO_PAD))))
/*      */       {
/* 2979 */         throw new IllegalFormatFlagsException(this.f.toString());
/*      */       }
/*      */     }
/*      */ 
/* 2983 */     private void checkText() { if (this.precision != -1)
/* 2984 */         throw new IllegalFormatPrecisionException(this.precision);
/* 2985 */       switch (this.c) {
/*      */       case '%':
/* 2987 */         if ((this.f.valueOf() != Formatter.Flags.LEFT_JUSTIFY.valueOf()) && (this.f.valueOf() != Formatter.Flags.NONE.valueOf()))
/*      */         {
/* 2989 */           throw new IllegalFormatFlagsException(this.f.toString());
/*      */         }
/* 2991 */         if ((this.width == -1) && (this.f.contains(Formatter.Flags.LEFT_JUSTIFY)))
/* 2992 */           throw new MissingFormatWidthException(toString());
/*      */         break;
/*      */       case 'n':
/* 2995 */         if (this.width != -1)
/* 2996 */           throw new IllegalFormatWidthException(this.width);
/* 2997 */         if (this.f.valueOf() != Formatter.Flags.NONE.valueOf())
/* 2998 */           throw new IllegalFormatFlagsException(this.f.toString());
/*      */         break;
/*      */       default:
/* 3001 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       } }
/*      */ 
/*      */     private void print(byte paramByte, Locale paramLocale) throws IOException
/*      */     {
/* 3006 */       long l = paramByte;
/* 3007 */       if ((paramByte < 0) && ((this.c == 'o') || (this.c == 'x')))
/*      */       {
/* 3010 */         l += 256L;
/* 3011 */         assert (l >= 0L) : l;
/*      */       }
/* 3013 */       print(l, paramLocale);
/*      */     }
/*      */ 
/*      */     private void print(short paramShort, Locale paramLocale) throws IOException {
/* 3017 */       long l = paramShort;
/* 3018 */       if ((paramShort < 0) && ((this.c == 'o') || (this.c == 'x')))
/*      */       {
/* 3021 */         l += 65536L;
/* 3022 */         assert (l >= 0L) : l;
/*      */       }
/* 3024 */       print(l, paramLocale);
/*      */     }
/*      */ 
/*      */     private void print(int paramInt, Locale paramLocale) throws IOException {
/* 3028 */       long l = paramInt;
/* 3029 */       if ((paramInt < 0) && ((this.c == 'o') || (this.c == 'x')))
/*      */       {
/* 3032 */         l += 4294967296L;
/* 3033 */         assert (l >= 0L) : l;
/*      */       }
/* 3035 */       print(l, paramLocale);
/*      */     }
/*      */ 
/*      */     private void print(long paramLong, Locale paramLocale) throws IOException
/*      */     {
/* 3040 */       StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/* 3042 */       if (this.c == 'd') {
/* 3043 */         boolean bool = paramLong < 0L;
/*      */         char[] arrayOfChar;
/* 3045 */         if (paramLong < 0L)
/* 3046 */           arrayOfChar = Long.toString(paramLong, 10).substring(1).toCharArray();
/*      */         else {
/* 3048 */           arrayOfChar = Long.toString(paramLong, 10).toCharArray();
/*      */         }
/*      */ 
/* 3051 */         leadingSign(localStringBuilder, bool);
/*      */ 
/* 3054 */         localizedMagnitude(localStringBuilder, arrayOfChar, this.f, adjustWidth(this.width, this.f, bool), paramLocale);
/*      */ 
/* 3057 */         trailingSign(localStringBuilder, bool);
/*      */       }
/*      */       else
/*      */       {
/*      */         String str;
/*      */         int i;
/*      */         int j;
/* 3058 */         if (this.c == 'o') {
/* 3059 */           checkBadFlags(new Formatter.Flags[] { Formatter.Flags.PARENTHESES, Formatter.Flags.LEADING_SPACE, Formatter.Flags.PLUS });
/*      */ 
/* 3061 */           str = Long.toOctalString(paramLong);
/* 3062 */           i = this.f.contains(Formatter.Flags.ALTERNATE) ? str.length() + 1 : str.length();
/*      */ 
/* 3067 */           if (this.f.contains(Formatter.Flags.ALTERNATE))
/* 3068 */             localStringBuilder.append('0');
/* 3069 */           if (this.f.contains(Formatter.Flags.ZERO_PAD))
/* 3070 */             for (j = 0; j < this.width - i; j++) localStringBuilder.append('0');
/* 3071 */           localStringBuilder.append(str);
/* 3072 */         } else if (this.c == 'x') {
/* 3073 */           checkBadFlags(new Formatter.Flags[] { Formatter.Flags.PARENTHESES, Formatter.Flags.LEADING_SPACE, Formatter.Flags.PLUS });
/*      */ 
/* 3075 */           str = Long.toHexString(paramLong);
/* 3076 */           i = this.f.contains(Formatter.Flags.ALTERNATE) ? str.length() + 2 : str.length();
/*      */ 
/* 3081 */           if (this.f.contains(Formatter.Flags.ALTERNATE))
/* 3082 */             localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? "0X" : "0x");
/* 3083 */           if (this.f.contains(Formatter.Flags.ZERO_PAD))
/* 3084 */             for (j = 0; j < this.width - i; j++) localStringBuilder.append('0');
/* 3085 */           if (this.f.contains(Formatter.Flags.UPPERCASE))
/* 3086 */             str = str.toUpperCase();
/* 3087 */           localStringBuilder.append(str);
/*      */         }
/*      */       }
/*      */ 
/* 3091 */       Formatter.this.a.append(justify(localStringBuilder.toString()));
/*      */     }
/*      */ 
/*      */     private StringBuilder leadingSign(StringBuilder paramStringBuilder, boolean paramBoolean)
/*      */     {
/* 3096 */       if (!paramBoolean) {
/* 3097 */         if (this.f.contains(Formatter.Flags.PLUS))
/* 3098 */           paramStringBuilder.append('+');
/* 3099 */         else if (this.f.contains(Formatter.Flags.LEADING_SPACE)) {
/* 3100 */           paramStringBuilder.append(' ');
/*      */         }
/*      */       }
/* 3103 */       else if (this.f.contains(Formatter.Flags.PARENTHESES))
/* 3104 */         paramStringBuilder.append('(');
/*      */       else {
/* 3106 */         paramStringBuilder.append('-');
/*      */       }
/* 3108 */       return paramStringBuilder;
/*      */     }
/*      */ 
/*      */     private StringBuilder trailingSign(StringBuilder paramStringBuilder, boolean paramBoolean)
/*      */     {
/* 3113 */       if ((paramBoolean) && (this.f.contains(Formatter.Flags.PARENTHESES)))
/* 3114 */         paramStringBuilder.append(')');
/* 3115 */       return paramStringBuilder;
/*      */     }
/*      */ 
/*      */     private void print(BigInteger paramBigInteger, Locale paramLocale) throws IOException {
/* 3119 */       StringBuilder localStringBuilder = new StringBuilder();
/* 3120 */       boolean bool = paramBigInteger.signum() == -1;
/* 3121 */       BigInteger localBigInteger = paramBigInteger.abs();
/*      */ 
/* 3124 */       leadingSign(localStringBuilder, bool);
/*      */       Object localObject;
/* 3127 */       if (this.c == 'd') {
/* 3128 */         localObject = localBigInteger.toString().toCharArray();
/* 3129 */         localizedMagnitude(localStringBuilder, (char[])localObject, this.f, adjustWidth(this.width, this.f, bool), paramLocale);
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/*      */         int j;
/* 3130 */         if (this.c == 'o') {
/* 3131 */           localObject = localBigInteger.toString(8);
/*      */ 
/* 3133 */           i = ((String)localObject).length() + localStringBuilder.length();
/* 3134 */           if ((bool) && (this.f.contains(Formatter.Flags.PARENTHESES))) {
/* 3135 */             i++;
/*      */           }
/*      */ 
/* 3138 */           if (this.f.contains(Formatter.Flags.ALTERNATE)) {
/* 3139 */             i++;
/* 3140 */             localStringBuilder.append('0');
/*      */           }
/* 3142 */           if (this.f.contains(Formatter.Flags.ZERO_PAD)) {
/* 3143 */             for (j = 0; j < this.width - i; j++)
/* 3144 */               localStringBuilder.append('0');
/*      */           }
/* 3146 */           localStringBuilder.append((String)localObject);
/* 3147 */         } else if (this.c == 'x') {
/* 3148 */           localObject = localBigInteger.toString(16);
/*      */ 
/* 3150 */           i = ((String)localObject).length() + localStringBuilder.length();
/* 3151 */           if ((bool) && (this.f.contains(Formatter.Flags.PARENTHESES))) {
/* 3152 */             i++;
/*      */           }
/*      */ 
/* 3155 */           if (this.f.contains(Formatter.Flags.ALTERNATE)) {
/* 3156 */             i += 2;
/* 3157 */             localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? "0X" : "0x");
/*      */           }
/* 3159 */           if (this.f.contains(Formatter.Flags.ZERO_PAD))
/* 3160 */             for (j = 0; j < this.width - i; j++)
/* 3161 */               localStringBuilder.append('0');
/* 3162 */           if (this.f.contains(Formatter.Flags.UPPERCASE))
/* 3163 */             localObject = ((String)localObject).toUpperCase();
/* 3164 */           localStringBuilder.append((String)localObject);
/*      */         }
/*      */       }
/*      */ 
/* 3168 */       trailingSign(localStringBuilder, paramBigInteger.signum() == -1);
/*      */ 
/* 3171 */       Formatter.this.a.append(justify(localStringBuilder.toString()));
/*      */     }
/*      */ 
/*      */     private void print(float paramFloat, Locale paramLocale) throws IOException {
/* 3175 */       print(paramFloat, paramLocale);
/*      */     }
/*      */ 
/*      */     private void print(double paramDouble, Locale paramLocale) throws IOException {
/* 3179 */       StringBuilder localStringBuilder = new StringBuilder();
/* 3180 */       boolean bool = Double.compare(paramDouble, 0.0D) == -1;
/*      */ 
/* 3182 */       if (!Double.isNaN(paramDouble)) {
/* 3183 */         double d = Math.abs(paramDouble);
/*      */ 
/* 3186 */         leadingSign(localStringBuilder, bool);
/*      */ 
/* 3189 */         if (!Double.isInfinite(d))
/* 3190 */           print(localStringBuilder, d, paramLocale, this.f, this.c, this.precision, bool);
/*      */         else {
/* 3192 */           localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? "INFINITY" : "Infinity");
/*      */         }
/*      */ 
/* 3196 */         trailingSign(localStringBuilder, bool);
/*      */       } else {
/* 3198 */         localStringBuilder.append(this.f.contains(Formatter.Flags.UPPERCASE) ? "NAN" : "NaN");
/*      */       }
/*      */ 
/* 3202 */       Formatter.this.a.append(justify(localStringBuilder.toString()));
/*      */     }
/*      */ 
/*      */     private void print(StringBuilder paramStringBuilder, double paramDouble, Locale paramLocale, Formatter.Flags paramFlags, char paramChar, int paramInt, boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/*      */       int i;
/*      */       Object localObject1;
/*      */       char[] arrayOfChar1;
/*      */       int j;
/*      */       char[] arrayOfChar2;
/*      */       int n;
/*      */       Formatter.Flags localFlags;
/*      */       char c1;
/*      */       char[] arrayOfChar4;
/* 3210 */       if (paramChar == 'e')
/*      */       {
/* 3213 */         i = paramInt == -1 ? 6 : paramInt;
/*      */ 
/* 3215 */         localObject1 = new FormattedFloatingDecimal(paramDouble, i, FormattedFloatingDecimal.Form.SCIENTIFIC);
/*      */ 
/* 3219 */         arrayOfChar1 = new char[30];
/* 3220 */         j = ((FormattedFloatingDecimal)localObject1).getChars(arrayOfChar1);
/*      */ 
/* 3222 */         arrayOfChar2 = addZeros(mantissa(arrayOfChar1, j), i);
/*      */ 
/* 3226 */         if ((paramFlags.contains(Formatter.Flags.ALTERNATE)) && (i == 0)) {
/* 3227 */           arrayOfChar2 = addDot(arrayOfChar2);
/*      */         }
/* 3229 */         char[] arrayOfChar3 = paramDouble == 0.0D ? new char[] { '+', '0', '0' } : exponent(arrayOfChar1, j);
/*      */ 
/* 3232 */         n = this.width;
/* 3233 */         if (this.width != -1)
/* 3234 */           n = adjustWidth(this.width - arrayOfChar3.length - 1, paramFlags, paramBoolean);
/* 3235 */         localizedMagnitude(paramStringBuilder, arrayOfChar2, paramFlags, n, paramLocale);
/*      */ 
/* 3237 */         paramStringBuilder.append(paramFlags.contains(Formatter.Flags.UPPERCASE) ? 'E' : 'e');
/*      */ 
/* 3239 */         localFlags = paramFlags.dup().remove(Formatter.Flags.GROUP);
/* 3240 */         c1 = arrayOfChar3[0];
/* 3241 */         assert ((c1 == '+') || (c1 == '-'));
/* 3242 */         paramStringBuilder.append(c1);
/*      */ 
/* 3244 */         arrayOfChar4 = new char[arrayOfChar3.length - 1];
/* 3245 */         System.arraycopy(arrayOfChar3, 1, arrayOfChar4, 0, arrayOfChar3.length - 1);
/* 3246 */         paramStringBuilder.append(localizedMagnitude(null, arrayOfChar4, localFlags, -1, paramLocale));
/* 3247 */       } else if (paramChar == 'f')
/*      */       {
/* 3250 */         i = paramInt == -1 ? 6 : paramInt;
/*      */ 
/* 3252 */         localObject1 = new FormattedFloatingDecimal(paramDouble, i, FormattedFloatingDecimal.Form.DECIMAL_FLOAT);
/*      */ 
/* 3257 */         arrayOfChar1 = new char[31 + Math.abs(((FormattedFloatingDecimal)localObject1).getExponent())];
/*      */ 
/* 3259 */         j = ((FormattedFloatingDecimal)localObject1).getChars(arrayOfChar1);
/*      */ 
/* 3261 */         arrayOfChar2 = addZeros(mantissa(arrayOfChar1, j), i);
/*      */ 
/* 3265 */         if ((paramFlags.contains(Formatter.Flags.ALTERNATE)) && (i == 0)) {
/* 3266 */           arrayOfChar2 = addDot(arrayOfChar2);
/*      */         }
/* 3268 */         int m = this.width;
/* 3269 */         if (this.width != -1)
/* 3270 */           m = adjustWidth(this.width, paramFlags, paramBoolean);
/* 3271 */         localizedMagnitude(paramStringBuilder, arrayOfChar2, paramFlags, m, paramLocale);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject2;
/* 3272 */         if (paramChar == 'g') {
/* 3273 */           i = paramInt;
/* 3274 */           if (paramInt == -1)
/* 3275 */             i = 6;
/* 3276 */           else if (paramInt == 0) {
/* 3277 */             i = 1;
/*      */           }
/* 3279 */           localObject1 = new FormattedFloatingDecimal(paramDouble, i, FormattedFloatingDecimal.Form.GENERAL);
/*      */ 
/* 3284 */           arrayOfChar1 = new char[31 + Math.abs(((FormattedFloatingDecimal)localObject1).getExponent())];
/*      */ 
/* 3286 */           j = ((FormattedFloatingDecimal)localObject1).getChars(arrayOfChar1);
/*      */ 
/* 3288 */           arrayOfChar2 = exponent(arrayOfChar1, j);
/* 3289 */           if (arrayOfChar2 != null)
/* 3290 */             i--;
/*      */           else {
/* 3292 */             i = i - (paramDouble == 0.0D ? 0 : ((FormattedFloatingDecimal)localObject1).getExponentRounded()) - 1;
/*      */           }
/*      */ 
/* 3295 */           localObject2 = addZeros(mantissa(arrayOfChar1, j), i);
/*      */ 
/* 3298 */           if ((paramFlags.contains(Formatter.Flags.ALTERNATE)) && (i == 0)) {
/* 3299 */             localObject2 = addDot((char[])localObject2);
/*      */           }
/* 3301 */           n = this.width;
/* 3302 */           if (this.width != -1) {
/* 3303 */             if (arrayOfChar2 != null)
/* 3304 */               n = adjustWidth(this.width - arrayOfChar2.length - 1, paramFlags, paramBoolean);
/*      */             else
/* 3306 */               n = adjustWidth(this.width, paramFlags, paramBoolean);
/*      */           }
/* 3308 */           localizedMagnitude(paramStringBuilder, (char[])localObject2, paramFlags, n, paramLocale);
/*      */ 
/* 3310 */           if (arrayOfChar2 != null) {
/* 3311 */             paramStringBuilder.append(paramFlags.contains(Formatter.Flags.UPPERCASE) ? 'E' : 'e');
/*      */ 
/* 3313 */             localFlags = paramFlags.dup().remove(Formatter.Flags.GROUP);
/* 3314 */             c1 = arrayOfChar2[0];
/* 3315 */             assert ((c1 == '+') || (c1 == '-'));
/* 3316 */             paramStringBuilder.append(c1);
/*      */ 
/* 3318 */             arrayOfChar4 = new char[arrayOfChar2.length - 1];
/* 3319 */             System.arraycopy(arrayOfChar2, 1, arrayOfChar4, 0, arrayOfChar2.length - 1);
/* 3320 */             paramStringBuilder.append(localizedMagnitude(null, arrayOfChar4, localFlags, -1, paramLocale));
/*      */           }
/* 3322 */         } else if (paramChar == 'a') {
/* 3323 */           i = paramInt;
/* 3324 */           if (paramInt == -1)
/*      */           {
/* 3326 */             i = 0;
/* 3327 */           } else if (paramInt == 0) {
/* 3328 */             i = 1;
/*      */           }
/* 3330 */           localObject1 = hexDouble(paramDouble, i);
/*      */ 
/* 3333 */           boolean bool = paramFlags.contains(Formatter.Flags.UPPERCASE);
/* 3334 */           paramStringBuilder.append(bool ? "0X" : "0x");
/*      */ 
/* 3336 */           if (paramFlags.contains(Formatter.Flags.ZERO_PAD)) {
/* 3337 */             for (k = 0; k < this.width - ((String)localObject1).length() - 2; k++)
/* 3338 */               paramStringBuilder.append('0');
/*      */           }
/* 3340 */           int k = ((String)localObject1).indexOf('p');
/* 3341 */           arrayOfChar1 = ((String)localObject1).substring(0, k).toCharArray();
/* 3342 */           if (bool) {
/* 3343 */             localObject2 = new String(arrayOfChar1);
/*      */ 
/* 3345 */             localObject2 = ((String)localObject2).toUpperCase(Locale.US);
/* 3346 */             arrayOfChar1 = ((String)localObject2).toCharArray();
/*      */           }
/* 3348 */           paramStringBuilder.append(i != 0 ? addZeros(arrayOfChar1, i) : arrayOfChar1);
/* 3349 */           paramStringBuilder.append(bool ? 'P' : 'p');
/* 3350 */           paramStringBuilder.append(((String)localObject1).substring(k + 1));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private char[] mantissa(char[] paramArrayOfChar, int paramInt) {
/* 3356 */       for (int i = 0; (i < paramInt) && 
/* 3357 */         (paramArrayOfChar[i] != 'e'); i++);
/* 3360 */       char[] arrayOfChar = new char[i];
/* 3361 */       System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, i);
/* 3362 */       return arrayOfChar;
/*      */     }
/*      */ 
/*      */     private char[] exponent(char[] paramArrayOfChar, int paramInt)
/*      */     {
/* 3367 */       for (int i = paramInt - 1; (i >= 0) && 
/* 3368 */         (paramArrayOfChar[i] != 'e'); i--);
/* 3371 */       if (i == -1)
/* 3372 */         return null;
/* 3373 */       char[] arrayOfChar = new char[paramInt - i - 1];
/* 3374 */       System.arraycopy(paramArrayOfChar, i + 1, arrayOfChar, 0, paramInt - i - 1);
/* 3375 */       return arrayOfChar;
/*      */     }
/*      */ 
/*      */     private char[] addZeros(char[] paramArrayOfChar, int paramInt)
/*      */     {
/* 3383 */       for (int i = 0; (i < paramArrayOfChar.length) && 
/* 3384 */         (paramArrayOfChar[i] != '.'); i++);
/* 3387 */       int j = 0;
/* 3388 */       if (i == paramArrayOfChar.length) {
/* 3389 */         j = 1;
/*      */       }
/*      */ 
/* 3393 */       int k = paramArrayOfChar.length - i - (j != 0 ? 0 : 1);
/* 3394 */       assert (k <= paramInt);
/* 3395 */       if (k == paramInt) {
/* 3396 */         return paramArrayOfChar;
/*      */       }
/*      */ 
/* 3399 */       char[] arrayOfChar = new char[paramArrayOfChar.length + paramInt - k + (j != 0 ? 1 : 0)];
/*      */ 
/* 3401 */       System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
/*      */ 
/* 3404 */       int m = paramArrayOfChar.length;
/* 3405 */       if (j != 0) {
/* 3406 */         arrayOfChar[paramArrayOfChar.length] = '.';
/* 3407 */         m++;
/*      */       }
/*      */ 
/* 3411 */       for (int n = m; n < arrayOfChar.length; n++) {
/* 3412 */         arrayOfChar[n] = '0';
/*      */       }
/* 3414 */       return arrayOfChar;
/*      */     }
/*      */ 
/*      */     private String hexDouble(double paramDouble, int paramInt)
/*      */     {
/* 3420 */       if ((!FpUtils.isFinite(paramDouble)) || (paramDouble == 0.0D) || (paramInt == 0) || (paramInt >= 13))
/*      */       {
/* 3422 */         return Double.toHexString(paramDouble).substring(2);
/*      */       }
/* 3424 */       assert ((paramInt >= 1) && (paramInt <= 12));
/*      */ 
/* 3426 */       int i = FpUtils.getExponent(paramDouble);
/* 3427 */       int j = i == -1023 ? 1 : 0;
/*      */ 
/* 3432 */       if (j != 0) {
/* 3433 */         Formatter.access$202(FpUtils.scalb(1.0D, 54));
/* 3434 */         paramDouble *= Formatter.scaleUp;
/*      */ 
/* 3437 */         i = FpUtils.getExponent(paramDouble);
/*      */ 
/* 3439 */         assert ((i >= -1022) && (i <= 1023)) : i;
/*      */       }
/*      */ 
/* 3442 */       int k = 1 + paramInt * 4;
/* 3443 */       int m = 53 - k;
/*      */ 
/* 3445 */       assert ((m >= 1) && (m < 53));
/*      */ 
/* 3447 */       long l1 = Double.doubleToLongBits(paramDouble);
/*      */ 
/* 3449 */       long l2 = (l1 & 0xFFFFFFFF) >> m;
/*      */ 
/* 3454 */       long l3 = l1 & (-1L << m ^ 0xFFFFFFFF);
/*      */ 
/* 3461 */       int n = (l2 & 1L) == 0L ? 1 : 0;
/* 3462 */       int i1 = (1L << m - 1 & l3) != 0L ? 1 : 0;
/*      */ 
/* 3464 */       int i2 = (m > 1) && (((1L << m - 1 ^ 0xFFFFFFFF) & l3) != 0L) ? 1 : 0;
/*      */ 
/* 3466 */       if (((n != 0) && (i1 != 0) && (i2 != 0)) || ((n == 0) && (i1 != 0))) {
/* 3467 */         l2 += 1L;
/*      */       }
/*      */ 
/* 3470 */       long l4 = l1 & 0x0;
/* 3471 */       l2 = l4 | l2 << m;
/* 3472 */       double d = Double.longBitsToDouble(l2);
/*      */ 
/* 3474 */       if (Double.isInfinite(d))
/*      */       {
/* 3476 */         return "1.0p1024";
/*      */       }
/* 3478 */       String str1 = Double.toHexString(d).substring(2);
/* 3479 */       if (j == 0) {
/* 3480 */         return str1;
/*      */       }
/*      */ 
/* 3483 */       int i3 = str1.indexOf('p');
/* 3484 */       if (i3 == -1)
/*      */       {
/* 3486 */         if (!$assertionsDisabled) throw new AssertionError();
/* 3487 */         return null;
/*      */       }
/*      */ 
/* 3490 */       String str2 = str1.substring(i3 + 1);
/* 3491 */       int i4 = Integer.parseInt(str2) - 54;
/* 3492 */       return str1.substring(0, i3) + "p" + Integer.toString(i4);
/*      */     }
/*      */ 
/*      */     private void print(BigDecimal paramBigDecimal, Locale paramLocale)
/*      */       throws IOException
/*      */     {
/* 3501 */       if (this.c == 'a')
/* 3502 */         failConversion(this.c, paramBigDecimal);
/* 3503 */       StringBuilder localStringBuilder = new StringBuilder();
/* 3504 */       boolean bool = paramBigDecimal.signum() == -1;
/* 3505 */       BigDecimal localBigDecimal = paramBigDecimal.abs();
/*      */ 
/* 3507 */       leadingSign(localStringBuilder, bool);
/*      */ 
/* 3510 */       print(localStringBuilder, localBigDecimal, paramLocale, this.f, this.c, this.precision, bool);
/*      */ 
/* 3513 */       trailingSign(localStringBuilder, bool);
/*      */ 
/* 3516 */       Formatter.this.a.append(justify(localStringBuilder.toString()));
/*      */     }
/*      */ 
/*      */     private void print(StringBuilder paramStringBuilder, BigDecimal paramBigDecimal, Locale paramLocale, Formatter.Flags paramFlags, char paramChar, int paramInt, boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       int i1;
/* 3524 */       if (paramChar == 'e')
/*      */       {
/* 3526 */         i = paramInt == -1 ? 6 : paramInt;
/* 3527 */         j = paramBigDecimal.scale();
/* 3528 */         k = paramBigDecimal.precision();
/* 3529 */         int m = 0;
/*      */ 
/* 3532 */         if (i > k - 1) {
/* 3533 */           i1 = k;
/* 3534 */           m = i - (k - 1);
/*      */         } else {
/* 3536 */           i1 = i + 1;
/*      */         }
/*      */ 
/* 3539 */         MathContext localMathContext = new MathContext(i1);
/* 3540 */         BigDecimal localBigDecimal2 = new BigDecimal(paramBigDecimal.unscaledValue(), j, localMathContext);
/*      */ 
/* 3543 */         BigDecimalLayout localBigDecimalLayout = new BigDecimalLayout(localBigDecimal2.unscaledValue(), localBigDecimal2.scale(), Formatter.BigDecimalLayoutForm.SCIENTIFIC);
/*      */ 
/* 3547 */         char[] arrayOfChar2 = localBigDecimalLayout.mantissa();
/*      */ 
/* 3554 */         if (((k == 1) || (!localBigDecimalLayout.hasDot())) && ((m > 0) || (paramFlags.contains(Formatter.Flags.ALTERNATE))))
/*      */         {
/* 3556 */           arrayOfChar2 = addDot(arrayOfChar2);
/*      */         }
/*      */ 
/* 3560 */         arrayOfChar2 = trailingZeros(arrayOfChar2, m);
/*      */ 
/* 3562 */         char[] arrayOfChar3 = localBigDecimalLayout.exponent();
/* 3563 */         int i2 = this.width;
/* 3564 */         if (this.width != -1)
/* 3565 */           i2 = adjustWidth(this.width - arrayOfChar3.length - 1, paramFlags, paramBoolean);
/* 3566 */         localizedMagnitude(paramStringBuilder, arrayOfChar2, paramFlags, i2, paramLocale);
/*      */ 
/* 3568 */         paramStringBuilder.append(paramFlags.contains(Formatter.Flags.UPPERCASE) ? 'E' : 'e');
/*      */ 
/* 3570 */         Formatter.Flags localFlags = paramFlags.dup().remove(Formatter.Flags.GROUP);
/* 3571 */         int i3 = arrayOfChar3[0];
/* 3572 */         assert ((i3 == 43) || (i3 == 45));
/* 3573 */         paramStringBuilder.append(arrayOfChar3[0]);
/*      */ 
/* 3575 */         char[] arrayOfChar4 = new char[arrayOfChar3.length - 1];
/* 3576 */         System.arraycopy(arrayOfChar3, 1, arrayOfChar4, 0, arrayOfChar3.length - 1);
/* 3577 */         paramStringBuilder.append(localizedMagnitude(null, arrayOfChar4, localFlags, -1, paramLocale));
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject;
/* 3578 */         if (paramChar == 'f')
/*      */         {
/* 3580 */           i = paramInt == -1 ? 6 : paramInt;
/* 3581 */           j = paramBigDecimal.scale();
/*      */ 
/* 3583 */           if (j > i)
/*      */           {
/* 3585 */             k = paramBigDecimal.precision();
/* 3586 */             if (k <= j)
/*      */             {
/* 3588 */               paramBigDecimal = paramBigDecimal.setScale(i, RoundingMode.HALF_UP);
/*      */             } else {
/* 3590 */               k -= j - i;
/* 3591 */               paramBigDecimal = new BigDecimal(paramBigDecimal.unscaledValue(), j, new MathContext(k));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 3596 */           localObject = new BigDecimalLayout(paramBigDecimal.unscaledValue(), paramBigDecimal.scale(), Formatter.BigDecimalLayoutForm.DECIMAL_FLOAT);
/*      */ 
/* 3601 */           char[] arrayOfChar1 = ((BigDecimalLayout)localObject).mantissa();
/* 3602 */           i1 = ((BigDecimalLayout)localObject).scale() < i ? i - ((BigDecimalLayout)localObject).scale() : 0;
/*      */ 
/* 3609 */           if ((((BigDecimalLayout)localObject).scale() == 0) && ((paramFlags.contains(Formatter.Flags.ALTERNATE)) || (i1 > 0))) {
/* 3610 */             arrayOfChar1 = addDot(((BigDecimalLayout)localObject).mantissa());
/*      */           }
/*      */ 
/* 3614 */           arrayOfChar1 = trailingZeros(arrayOfChar1, i1);
/*      */ 
/* 3616 */           localizedMagnitude(paramStringBuilder, arrayOfChar1, paramFlags, adjustWidth(this.width, paramFlags, paramBoolean), paramLocale);
/* 3617 */         } else if (paramChar == 'g') {
/* 3618 */           i = paramInt;
/* 3619 */           if (paramInt == -1)
/* 3620 */             i = 6;
/* 3621 */           else if (paramInt == 0) {
/* 3622 */             i = 1;
/*      */           }
/* 3624 */           BigDecimal localBigDecimal1 = BigDecimal.valueOf(1L, 4);
/* 3625 */           localObject = BigDecimal.valueOf(1L, -i);
/* 3626 */           if ((paramBigDecimal.equals(BigDecimal.ZERO)) || ((paramBigDecimal.compareTo(localBigDecimal1) != -1) && (paramBigDecimal.compareTo((BigDecimal)localObject) == -1)))
/*      */           {
/* 3630 */             int n = -paramBigDecimal.scale() + (paramBigDecimal.unscaledValue().toString().length() - 1);
/*      */ 
/* 3643 */             i = i - n - 1;
/*      */ 
/* 3645 */             print(paramStringBuilder, paramBigDecimal, paramLocale, paramFlags, 'f', i, paramBoolean);
/*      */           }
/*      */           else {
/* 3648 */             print(paramStringBuilder, paramBigDecimal, paramLocale, paramFlags, 'e', i - 1, paramBoolean);
/*      */           }
/* 3650 */         } else if (paramChar == 'a')
/*      */         {
/* 3653 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int adjustWidth(int paramInt, Formatter.Flags paramFlags, boolean paramBoolean)
/*      */     {
/* 3786 */       int i = paramInt;
/* 3787 */       if ((i != -1) && (paramBoolean) && (paramFlags.contains(Formatter.Flags.PARENTHESES)))
/* 3788 */         i--;
/* 3789 */       return i;
/*      */     }
/*      */ 
/*      */     private char[] addDot(char[] paramArrayOfChar)
/*      */     {
/* 3794 */       char[] arrayOfChar = paramArrayOfChar;
/* 3795 */       arrayOfChar = new char[paramArrayOfChar.length + 1];
/* 3796 */       System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
/* 3797 */       arrayOfChar[(arrayOfChar.length - 1)] = '.';
/* 3798 */       return arrayOfChar;
/*      */     }
/*      */ 
/*      */     private char[] trailingZeros(char[] paramArrayOfChar, int paramInt)
/*      */     {
/* 3804 */       char[] arrayOfChar = paramArrayOfChar;
/* 3805 */       if (paramInt > 0) {
/* 3806 */         arrayOfChar = new char[paramArrayOfChar.length + paramInt];
/* 3807 */         System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
/* 3808 */         for (int i = paramArrayOfChar.length; i < arrayOfChar.length; i++)
/* 3809 */           arrayOfChar[i] = '0';
/*      */       }
/* 3811 */       return arrayOfChar;
/*      */     }
/*      */ 
/*      */     private void print(Calendar paramCalendar, char paramChar, Locale paramLocale) throws IOException
/*      */     {
/* 3816 */       StringBuilder localStringBuilder = new StringBuilder();
/* 3817 */       print(localStringBuilder, paramCalendar, paramChar, paramLocale);
/*      */ 
/* 3820 */       String str = justify(localStringBuilder.toString());
/* 3821 */       if (this.f.contains(Formatter.Flags.UPPERCASE)) {
/* 3822 */         str = str.toUpperCase();
/*      */       }
/* 3824 */       Formatter.this.a.append(str);
/*      */     }
/*      */ 
/*      */     private Appendable print(StringBuilder paramStringBuilder, Calendar paramCalendar, char paramChar, Locale paramLocale)
/*      */       throws IOException
/*      */     {
/* 3831 */       assert (this.width == -1);
/* 3832 */       if (paramStringBuilder == null)
/* 3833 */         paramStringBuilder = new StringBuilder();
/*      */       int i;
/*      */       Object localObject1;
/*      */       Formatter.Flags localFlags1;
/*      */       int j;
/*      */       int k;
/*      */       Locale localLocale;
/*      */       Object localObject3;
/*      */       Object localObject2;
/*      */       char c1;
/* 3834 */       switch (paramChar) {
/*      */       case 'H':
/*      */       case 'I':
/*      */       case 'k':
/*      */       case 'l':
/* 3839 */         i = paramCalendar.get(11);
/* 3840 */         if ((paramChar == 'I') || (paramChar == 'l'))
/* 3841 */           i = (i == 0) || (i == 12) ? 12 : i % 12;
/* 3842 */         localObject1 = (paramChar == 'H') || (paramChar == 'I') ? Formatter.Flags.ZERO_PAD : Formatter.Flags.NONE;
/*      */ 
/* 3846 */         paramStringBuilder.append(localizedMagnitude(null, i, (Formatter.Flags)localObject1, 2, paramLocale));
/* 3847 */         break;
/*      */       case 'M':
/* 3850 */         i = paramCalendar.get(12);
/* 3851 */         localObject1 = Formatter.Flags.ZERO_PAD;
/* 3852 */         paramStringBuilder.append(localizedMagnitude(null, i, (Formatter.Flags)localObject1, 2, paramLocale));
/* 3853 */         break;
/*      */       case 'N':
/* 3856 */         i = paramCalendar.get(14) * 1000000;
/* 3857 */         localObject1 = Formatter.Flags.ZERO_PAD;
/* 3858 */         paramStringBuilder.append(localizedMagnitude(null, i, (Formatter.Flags)localObject1, 9, paramLocale));
/* 3859 */         break;
/*      */       case 'L':
/* 3862 */         i = paramCalendar.get(14);
/* 3863 */         localObject1 = Formatter.Flags.ZERO_PAD;
/* 3864 */         paramStringBuilder.append(localizedMagnitude(null, i, (Formatter.Flags)localObject1, 3, paramLocale));
/* 3865 */         break;
/*      */       case 'Q':
/* 3868 */         long l1 = paramCalendar.getTimeInMillis();
/* 3869 */         localFlags1 = Formatter.Flags.NONE;
/* 3870 */         paramStringBuilder.append(localizedMagnitude(null, l1, localFlags1, this.width, paramLocale));
/* 3871 */         break;
/*      */       case 'p':
/* 3875 */         String[] arrayOfString = { "AM", "PM" };
/* 3876 */         if ((paramLocale != null) && (paramLocale != Locale.US)) {
/* 3877 */           localObject1 = DateFormatSymbols.getInstance(paramLocale);
/* 3878 */           arrayOfString = ((DateFormatSymbols)localObject1).getAmPmStrings();
/*      */         }
/* 3880 */         localObject1 = arrayOfString[paramCalendar.get(9)];
/* 3881 */         paramStringBuilder.append(((String)localObject1).toLowerCase(paramLocale != null ? paramLocale : Locale.US));
/* 3882 */         break;
/*      */       case 's':
/* 3885 */         long l2 = paramCalendar.getTimeInMillis() / 1000L;
/* 3886 */         localFlags1 = Formatter.Flags.NONE;
/* 3887 */         paramStringBuilder.append(localizedMagnitude(null, l2, localFlags1, this.width, paramLocale));
/* 3888 */         break;
/*      */       case 'S':
/* 3891 */         j = paramCalendar.get(13);
/* 3892 */         localObject1 = Formatter.Flags.ZERO_PAD;
/* 3893 */         paramStringBuilder.append(localizedMagnitude(null, j, (Formatter.Flags)localObject1, 2, paramLocale));
/* 3894 */         break;
/*      */       case 'z':
/* 3897 */         j = paramCalendar.get(15) + paramCalendar.get(16);
/* 3898 */         int m = j < 0 ? 1 : 0;
/* 3899 */         paramStringBuilder.append(m != 0 ? '-' : '+');
/* 3900 */         if (m != 0)
/* 3901 */           j = -j;
/* 3902 */         int i1 = j / 60000;
/*      */ 
/* 3904 */         int i2 = i1 / 60 * 100 + i1 % 60;
/* 3905 */         Formatter.Flags localFlags2 = Formatter.Flags.ZERO_PAD;
/*      */ 
/* 3907 */         paramStringBuilder.append(localizedMagnitude(null, i2, localFlags2, 4, paramLocale));
/* 3908 */         break;
/*      */       case 'Z':
/* 3911 */         TimeZone localTimeZone = paramCalendar.getTimeZone();
/* 3912 */         paramStringBuilder.append(localTimeZone.getDisplayName(paramCalendar.get(16) != 0, 0, paramLocale == null ? Locale.US : paramLocale));
/*      */ 
/* 3915 */         break;
/*      */       case 'A':
/*      */       case 'a':
/* 3921 */         k = paramCalendar.get(7);
/* 3922 */         localLocale = paramLocale == null ? Locale.US : paramLocale;
/* 3923 */         localObject3 = DateFormatSymbols.getInstance(localLocale);
/* 3924 */         if (paramChar == 'A')
/* 3925 */           paramStringBuilder.append(localObject3.getWeekdays()[k]);
/*      */         else
/* 3927 */           paramStringBuilder.append(localObject3.getShortWeekdays()[k]);
/* 3928 */         break;
/*      */       case 'B':
/*      */       case 'b':
/*      */       case 'h':
/* 3933 */         k = paramCalendar.get(2);
/* 3934 */         localLocale = paramLocale == null ? Locale.US : paramLocale;
/* 3935 */         localObject3 = DateFormatSymbols.getInstance(localLocale);
/* 3936 */         if (paramChar == 'B')
/* 3937 */           paramStringBuilder.append(localObject3.getMonths()[k]);
/*      */         else
/* 3939 */           paramStringBuilder.append(localObject3.getShortMonths()[k]);
/* 3940 */         break;
/*      */       case 'C':
/*      */       case 'Y':
/*      */       case 'y':
/* 3945 */         k = paramCalendar.get(1);
/* 3946 */         int n = 2;
/* 3947 */         switch (paramChar) {
/*      */         case 'C':
/* 3949 */           k /= 100;
/* 3950 */           break;
/*      */         case 'y':
/* 3952 */           k %= 100;
/* 3953 */           break;
/*      */         case 'Y':
/* 3955 */           n = 4;
/*      */         }
/*      */ 
/* 3958 */         localObject3 = Formatter.Flags.ZERO_PAD;
/* 3959 */         paramStringBuilder.append(localizedMagnitude(null, k, (Formatter.Flags)localObject3, n, paramLocale));
/* 3960 */         break;
/*      */       case 'd':
/*      */       case 'e':
/* 3964 */         k = paramCalendar.get(5);
/* 3965 */         localObject2 = paramChar == 'd' ? Formatter.Flags.ZERO_PAD : Formatter.Flags.NONE;
/*      */ 
/* 3968 */         paramStringBuilder.append(localizedMagnitude(null, k, (Formatter.Flags)localObject2, 2, paramLocale));
/* 3969 */         break;
/*      */       case 'j':
/* 3972 */         k = paramCalendar.get(6);
/* 3973 */         localObject2 = Formatter.Flags.ZERO_PAD;
/* 3974 */         paramStringBuilder.append(localizedMagnitude(null, k, (Formatter.Flags)localObject2, 3, paramLocale));
/* 3975 */         break;
/*      */       case 'm':
/* 3978 */         k = paramCalendar.get(2) + 1;
/* 3979 */         localObject2 = Formatter.Flags.ZERO_PAD;
/* 3980 */         paramStringBuilder.append(localizedMagnitude(null, k, (Formatter.Flags)localObject2, 2, paramLocale));
/* 3981 */         break;
/*      */       case 'R':
/*      */       case 'T':
/* 3987 */         k = 58;
/* 3988 */         print(paramStringBuilder, paramCalendar, 'H', paramLocale).append(k);
/* 3989 */         print(paramStringBuilder, paramCalendar, 'M', paramLocale);
/* 3990 */         if (paramChar == 'T') {
/* 3991 */           paramStringBuilder.append(k);
/* 3992 */           print(paramStringBuilder, paramCalendar, 'S', paramLocale); } break;
/*      */       case 'r':
/* 3997 */         c1 = ':';
/* 3998 */         print(paramStringBuilder, paramCalendar, 'I', paramLocale).append(c1);
/* 3999 */         print(paramStringBuilder, paramCalendar, 'M', paramLocale).append(c1);
/* 4000 */         print(paramStringBuilder, paramCalendar, 'S', paramLocale).append(' ');
/*      */ 
/* 4002 */         localObject2 = new StringBuilder();
/* 4003 */         print((StringBuilder)localObject2, paramCalendar, 'p', paramLocale);
/* 4004 */         paramStringBuilder.append(((StringBuilder)localObject2).toString().toUpperCase(paramLocale != null ? paramLocale : Locale.US));
/* 4005 */         break;
/*      */       case 'c':
/* 4008 */         c1 = ' ';
/* 4009 */         print(paramStringBuilder, paramCalendar, 'a', paramLocale).append(c1);
/* 4010 */         print(paramStringBuilder, paramCalendar, 'b', paramLocale).append(c1);
/* 4011 */         print(paramStringBuilder, paramCalendar, 'd', paramLocale).append(c1);
/* 4012 */         print(paramStringBuilder, paramCalendar, 'T', paramLocale).append(c1);
/* 4013 */         print(paramStringBuilder, paramCalendar, 'Z', paramLocale).append(c1);
/* 4014 */         print(paramStringBuilder, paramCalendar, 'Y', paramLocale);
/* 4015 */         break;
/*      */       case 'D':
/* 4018 */         c1 = '/';
/* 4019 */         print(paramStringBuilder, paramCalendar, 'm', paramLocale).append(c1);
/* 4020 */         print(paramStringBuilder, paramCalendar, 'd', paramLocale).append(c1);
/* 4021 */         print(paramStringBuilder, paramCalendar, 'y', paramLocale);
/* 4022 */         break;
/*      */       case 'F':
/* 4025 */         c1 = '-';
/* 4026 */         print(paramStringBuilder, paramCalendar, 'Y', paramLocale).append(c1);
/* 4027 */         print(paramStringBuilder, paramCalendar, 'm', paramLocale).append(c1);
/* 4028 */         print(paramStringBuilder, paramCalendar, 'd', paramLocale);
/* 4029 */         break;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'J':
/*      */       case 'K':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*      */       case '[':
/*      */       case '\\':
/*      */       case ']':
/*      */       case '^':
/*      */       case '_':
/*      */       case '`':
/*      */       case 'f':
/*      */       case 'g':
/*      */       case 'i':
/*      */       case 'n':
/*      */       case 'o':
/*      */       case 'q':
/*      */       case 't':
/*      */       case 'u':
/*      */       case 'v':
/*      */       case 'w':
/*      */       case 'x':
/*      */       default:
/* 4032 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/* 4034 */       return paramStringBuilder;
/*      */     }
/*      */ 
/*      */     private void failMismatch(Formatter.Flags paramFlags, char paramChar)
/*      */     {
/* 4040 */       String str = paramFlags.toString();
/* 4041 */       throw new FormatFlagsConversionMismatchException(str, paramChar);
/*      */     }
/*      */ 
/*      */     private void failConversion(char paramChar, Object paramObject) {
/* 4045 */       throw new IllegalFormatConversionException(paramChar, paramObject.getClass());
/*      */     }
/*      */ 
/*      */     private char getZero(Locale paramLocale) {
/* 4049 */       if ((paramLocale != null) && (!paramLocale.equals(Formatter.this.locale()))) {
/* 4050 */         DecimalFormatSymbols localDecimalFormatSymbols = DecimalFormatSymbols.getInstance(paramLocale);
/* 4051 */         return localDecimalFormatSymbols.getZeroDigit();
/*      */       }
/* 4053 */       return Formatter.this.zero;
/*      */     }
/*      */ 
/*      */     private StringBuilder localizedMagnitude(StringBuilder paramStringBuilder, long paramLong, Formatter.Flags paramFlags, int paramInt, Locale paramLocale)
/*      */     {
/* 4060 */       char[] arrayOfChar = Long.toString(paramLong, 10).toCharArray();
/* 4061 */       return localizedMagnitude(paramStringBuilder, arrayOfChar, paramFlags, paramInt, paramLocale);
/*      */     }
/*      */ 
/*      */     private StringBuilder localizedMagnitude(StringBuilder paramStringBuilder, char[] paramArrayOfChar, Formatter.Flags paramFlags, int paramInt, Locale paramLocale)
/*      */     {
/* 4068 */       if (paramStringBuilder == null)
/* 4069 */         paramStringBuilder = new StringBuilder();
/* 4070 */       int i = paramStringBuilder.length();
/*      */ 
/* 4072 */       int j = getZero(paramLocale);
/*      */ 
/* 4075 */       char c1 = '\000';
/* 4076 */       int k = -1;
/* 4077 */       char c2 = '\000';
/*      */ 
/* 4079 */       int m = paramArrayOfChar.length;
/* 4080 */       int n = m;
/* 4081 */       for (int i1 = 0; i1 < m; i1++)
/* 4082 */         if (paramArrayOfChar[i1] == '.') {
/* 4083 */           n = i1;
/* 4084 */           break;
/*      */         }
/*      */       DecimalFormatSymbols localDecimalFormatSymbols;
/* 4088 */       if (n < m) {
/* 4089 */         if ((paramLocale == null) || (paramLocale.equals(Locale.US))) {
/* 4090 */           c2 = '.';
/*      */         } else {
/* 4092 */           localDecimalFormatSymbols = DecimalFormatSymbols.getInstance(paramLocale);
/* 4093 */           c2 = localDecimalFormatSymbols.getDecimalSeparator();
/*      */         }
/*      */       }
/*      */ 
/* 4097 */       if (paramFlags.contains(Formatter.Flags.GROUP)) {
/* 4098 */         if ((paramLocale == null) || (paramLocale.equals(Locale.US))) {
/* 4099 */           c1 = ',';
/* 4100 */           k = 3;
/*      */         } else {
/* 4102 */           localDecimalFormatSymbols = DecimalFormatSymbols.getInstance(paramLocale);
/* 4103 */           c1 = localDecimalFormatSymbols.getGroupingSeparator();
/* 4104 */           DecimalFormat localDecimalFormat = (DecimalFormat)NumberFormat.getIntegerInstance(paramLocale);
/* 4105 */           k = localDecimalFormat.getGroupingSize();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4110 */       for (int i2 = 0; i2 < m; i2++) {
/* 4111 */         if (i2 == n) {
/* 4112 */           paramStringBuilder.append(c2);
/*      */ 
/* 4114 */           c1 = '\000';
/*      */         }
/*      */         else
/*      */         {
/* 4118 */           int i3 = paramArrayOfChar[i2];
/* 4119 */           paramStringBuilder.append((char)(i3 - 48 + j));
/* 4120 */           if ((c1 != 0) && (i2 != n - 1) && ((n - i2) % k == 1)) {
/* 4121 */             paramStringBuilder.append(c1);
/*      */           }
/*      */         }
/*      */       }
/* 4125 */       m = paramStringBuilder.length();
/* 4126 */       if ((paramInt != -1) && (paramFlags.contains(Formatter.Flags.ZERO_PAD))) {
/* 4127 */         for (i2 = 0; i2 < paramInt - m; i2++)
/* 4128 */           paramStringBuilder.insert(i, j);
/*      */       }
/* 4130 */       return paramStringBuilder;
/*      */     }
/*      */ 
/*      */     private class BigDecimalLayout
/*      */     {
/*      */       private StringBuilder mant;
/*      */       private StringBuilder exp;
/* 3660 */       private boolean dot = false;
/*      */       private int scale;
/*      */ 
/*      */       public BigDecimalLayout(BigInteger paramInt, int paramBigDecimalLayoutForm, Formatter.BigDecimalLayoutForm arg4)
/*      */       {
/*      */         Formatter.BigDecimalLayoutForm localBigDecimalLayoutForm;
/* 3664 */         layout(paramInt, paramBigDecimalLayoutForm, localBigDecimalLayoutForm);
/*      */       }
/*      */ 
/*      */       public boolean hasDot() {
/* 3668 */         return this.dot;
/*      */       }
/*      */ 
/*      */       public int scale() {
/* 3672 */         return this.scale;
/*      */       }
/*      */ 
/*      */       public char[] layoutChars()
/*      */       {
/* 3677 */         StringBuilder localStringBuilder = new StringBuilder(this.mant);
/* 3678 */         if (this.exp != null) {
/* 3679 */           localStringBuilder.append('E');
/* 3680 */           localStringBuilder.append(this.exp);
/*      */         }
/* 3682 */         return toCharArray(localStringBuilder);
/*      */       }
/*      */ 
/*      */       public char[] mantissa() {
/* 3686 */         return toCharArray(this.mant);
/*      */       }
/*      */ 
/*      */       public char[] exponent()
/*      */       {
/* 3692 */         return toCharArray(this.exp);
/*      */       }
/*      */ 
/*      */       private char[] toCharArray(StringBuilder paramStringBuilder) {
/* 3696 */         if (paramStringBuilder == null)
/* 3697 */           return null;
/* 3698 */         char[] arrayOfChar = new char[paramStringBuilder.length()];
/* 3699 */         paramStringBuilder.getChars(0, arrayOfChar.length, arrayOfChar, 0);
/* 3700 */         return arrayOfChar;
/*      */       }
/*      */ 
/*      */       private void layout(BigInteger paramBigInteger, int paramInt, Formatter.BigDecimalLayoutForm paramBigDecimalLayoutForm) {
/* 3704 */         char[] arrayOfChar = paramBigInteger.toString().toCharArray();
/* 3705 */         this.scale = paramInt;
/*      */ 
/* 3712 */         this.mant = new StringBuilder(arrayOfChar.length + 14);
/*      */ 
/* 3714 */         if (paramInt == 0) {
/* 3715 */           int i = arrayOfChar.length;
/* 3716 */           if (i > 1) {
/* 3717 */             this.mant.append(arrayOfChar[0]);
/* 3718 */             if (paramBigDecimalLayoutForm == Formatter.BigDecimalLayoutForm.SCIENTIFIC) {
/* 3719 */               this.mant.append('.');
/* 3720 */               this.dot = true;
/* 3721 */               this.mant.append(arrayOfChar, 1, i - 1);
/* 3722 */               this.exp = new StringBuilder("+");
/* 3723 */               if (i < 10)
/* 3724 */                 this.exp.append("0").append(i - 1);
/*      */               else
/* 3726 */                 this.exp.append(i - 1);
/*      */             } else {
/* 3728 */               this.mant.append(arrayOfChar, 1, i - 1);
/*      */             }
/*      */           } else {
/* 3731 */             this.mant.append(arrayOfChar);
/* 3732 */             if (paramBigDecimalLayoutForm == Formatter.BigDecimalLayoutForm.SCIENTIFIC)
/* 3733 */               this.exp = new StringBuilder("+00");
/*      */           }
/* 3735 */           return;
/*      */         }
/* 3737 */         long l1 = -paramInt + (arrayOfChar.length - 1);
/* 3738 */         if (paramBigDecimalLayoutForm == Formatter.BigDecimalLayoutForm.DECIMAL_FLOAT)
/*      */         {
/* 3740 */           int j = paramInt - arrayOfChar.length;
/* 3741 */           if (j >= 0)
/*      */           {
/* 3743 */             this.mant.append("0.");
/* 3744 */             this.dot = true;
/* 3745 */             for (; j > 0; j--) this.mant.append('0');
/* 3746 */             this.mant.append(arrayOfChar);
/*      */           }
/* 3748 */           else if (-j < arrayOfChar.length)
/*      */           {
/* 3750 */             this.mant.append(arrayOfChar, 0, -j);
/* 3751 */             this.mant.append('.');
/* 3752 */             this.dot = true;
/* 3753 */             this.mant.append(arrayOfChar, -j, paramInt);
/*      */           }
/*      */           else {
/* 3756 */             this.mant.append(arrayOfChar, 0, arrayOfChar.length);
/* 3757 */             for (int k = 0; k < -paramInt; k++)
/* 3758 */               this.mant.append('0');
/* 3759 */             this.scale = 0;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 3764 */           this.mant.append(arrayOfChar[0]);
/* 3765 */           if (arrayOfChar.length > 1) {
/* 3766 */             this.mant.append('.');
/* 3767 */             this.dot = true;
/* 3768 */             this.mant.append(arrayOfChar, 1, arrayOfChar.length - 1);
/*      */           }
/* 3770 */           this.exp = new StringBuilder();
/* 3771 */           if (l1 != 0L) {
/* 3772 */             long l2 = Math.abs(l1);
/*      */ 
/* 3774 */             this.exp.append(l1 < 0L ? '-' : '+');
/* 3775 */             if (l2 < 10L)
/* 3776 */               this.exp.append('0');
/* 3777 */             this.exp.append(l2);
/*      */           } else {
/* 3779 */             this.exp.append("+00");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract interface FormatString
/*      */   {
/*      */     public abstract int index();
/*      */ 
/*      */     public abstract void print(Object paramObject, Locale paramLocale)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract String toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Formatter
 * JD-Core Version:    0.6.2
 */