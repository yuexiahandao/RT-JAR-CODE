/*      */ package java.util;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.StringReader;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.channels.Channels;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.DecimalFormatSymbols;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.regex.MatchResult;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import sun.misc.LRUCache;
/*      */ 
/*      */ public final class Scanner
/*      */   implements Iterator<String>, Closeable
/*      */ {
/*      */   private CharBuffer buf;
/*      */   private static final int BUFFER_SIZE = 1024;
/*      */   private int position;
/*      */   private Matcher matcher;
/*      */   private Pattern delimPattern;
/*      */   private Pattern hasNextPattern;
/*      */   private int hasNextPosition;
/*      */   private String hasNextResult;
/*      */   private Readable source;
/*  378 */   private boolean sourceClosed = false;
/*      */ 
/*  381 */   private boolean needInput = false;
/*      */ 
/*  384 */   private boolean skipped = false;
/*      */ 
/*  387 */   private int savedScannerPosition = -1;
/*      */ 
/*  390 */   private Object typeCache = null;
/*      */ 
/*  393 */   private boolean matchValid = false;
/*      */ 
/*  396 */   private boolean closed = false;
/*      */ 
/*  399 */   private int radix = 10;
/*      */ 
/*  402 */   private int defaultRadix = 10;
/*      */ 
/*  405 */   private Locale locale = null;
/*      */ 
/*  408 */   private LRUCache<String, Pattern> patternCache = new LRUCache(7)
/*      */   {
/*      */     protected Pattern create(String paramAnonymousString) {
/*  411 */       return Pattern.compile(paramAnonymousString);
/*      */     }
/*      */     protected boolean hasName(Pattern paramAnonymousPattern, String paramAnonymousString) {
/*  414 */       return paramAnonymousPattern.pattern().equals(paramAnonymousString); }  } ;
/*      */   private IOException lastException;
/*  422 */   private static Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
/*      */ 
/*  426 */   private static Pattern FIND_ANY_PATTERN = Pattern.compile("(?s).*");
/*      */ 
/*  429 */   private static Pattern NON_ASCII_DIGIT = Pattern.compile("[\\p{javaDigit}&&[^0-9]]");
/*      */ 
/*  437 */   private String groupSeparator = "\\,";
/*  438 */   private String decimalSeparator = "\\.";
/*  439 */   private String nanString = "NaN";
/*  440 */   private String infinityString = "Infinity";
/*  441 */   private String positivePrefix = "";
/*  442 */   private String negativePrefix = "\\-";
/*  443 */   private String positiveSuffix = "";
/*  444 */   private String negativeSuffix = "";
/*      */   private static volatile Pattern boolPattern;
/*      */   private static final String BOOLEAN_PATTERN = "true|false";
/*      */   private Pattern integerPattern;
/*  463 */   private String digits = "0123456789abcdefghijklmnopqrstuvwxyz";
/*  464 */   private String non0Digit = "[\\p{javaDigit}&&[^0]]";
/*  465 */   private int SIMPLE_GROUP_INDEX = 5;
/*      */   private static volatile Pattern separatorPattern;
/*      */   private static volatile Pattern linePattern;
/*      */   private static final String LINE_SEPARATOR_PATTERN = "\r\n|[\n\r  ]";
/*      */   private static final String LINE_PATTERN = ".*(\r\n|[\n\r  ])|.+$";
/*      */   private Pattern floatPattern;
/*      */   private Pattern decimalPattern;
/*      */ 
/*  452 */   private static Pattern boolPattern() { Pattern localPattern = boolPattern;
/*  453 */     if (localPattern == null) {
/*  454 */       boolPattern = localPattern = Pattern.compile("true|false", 2);
/*      */     }
/*  456 */     return localPattern;
/*      */   }
/*      */ 
/*      */   private String buildIntegerPatternString()
/*      */   {
/*  467 */     String str1 = this.digits.substring(0, this.radix);
/*      */ 
/*  472 */     String str2 = "((?i)[" + str1 + "]|\\p{javaDigit})";
/*  473 */     String str3 = "(" + this.non0Digit + str2 + "?" + str2 + "?(" + this.groupSeparator + str2 + str2 + str2 + ")+)";
/*      */ 
/*  477 */     String str4 = "((" + str2 + "++)|" + str3 + ")";
/*  478 */     String str5 = "([-+]?(" + str4 + "))";
/*  479 */     String str6 = this.negativePrefix + str4 + this.negativeSuffix;
/*  480 */     String str7 = this.positivePrefix + str4 + this.positiveSuffix;
/*  481 */     return "(" + str5 + ")|(" + str7 + ")|(" + str6 + ")";
/*      */   }
/*      */ 
/*      */   private Pattern integerPattern()
/*      */   {
/*  486 */     if (this.integerPattern == null) {
/*  487 */       this.integerPattern = ((Pattern)this.patternCache.forName(buildIntegerPatternString()));
/*      */     }
/*  489 */     return this.integerPattern;
/*      */   }
/*      */ 
/*      */   private static Pattern separatorPattern()
/*      */   {
/*  502 */     Pattern localPattern = separatorPattern;
/*  503 */     if (localPattern == null)
/*  504 */       separatorPattern = localPattern = Pattern.compile("\r\n|[\n\r  ]");
/*  505 */     return localPattern;
/*      */   }
/*      */ 
/*      */   private static Pattern linePattern() {
/*  509 */     Pattern localPattern = linePattern;
/*  510 */     if (localPattern == null)
/*  511 */       linePattern = localPattern = Pattern.compile(".*(\r\n|[\n\r  ])|.+$");
/*  512 */     return localPattern;
/*      */   }
/*      */ 
/*      */   private void buildFloatAndDecimalPattern()
/*      */   {
/*  522 */     String str1 = "([0-9]|(\\p{javaDigit}))";
/*  523 */     String str2 = "([eE][+-]?" + str1 + "+)?";
/*  524 */     String str3 = "(" + this.non0Digit + str1 + "?" + str1 + "?(" + this.groupSeparator + str1 + str1 + str1 + ")+)";
/*      */ 
/*  527 */     String str4 = "((" + str1 + "++)|" + str3 + ")";
/*  528 */     String str5 = "(" + str4 + "|" + str4 + this.decimalSeparator + str1 + "*+|" + this.decimalSeparator + str1 + "++)";
/*      */ 
/*  531 */     String str6 = "(NaN|" + this.nanString + "|Infinity|" + this.infinityString + ")";
/*      */ 
/*  533 */     String str7 = "(" + this.positivePrefix + str5 + this.positiveSuffix + str2 + ")";
/*      */ 
/*  535 */     String str8 = "(" + this.negativePrefix + str5 + this.negativeSuffix + str2 + ")";
/*      */ 
/*  537 */     String str9 = "(([-+]?" + str5 + str2 + ")|" + str7 + "|" + str8 + ")";
/*      */ 
/*  539 */     String str10 = "[-+]?0[xX][0-9a-fA-F]*\\.[0-9a-fA-F]+([pP][-+]?[0-9]+)?";
/*      */ 
/*  541 */     String str11 = "(" + this.positivePrefix + str6 + this.positiveSuffix + ")";
/*      */ 
/*  543 */     String str12 = "(" + this.negativePrefix + str6 + this.negativeSuffix + ")";
/*      */ 
/*  545 */     String str13 = "(([-+]?" + str6 + ")|" + str11 + "|" + str12 + ")";
/*      */ 
/*  548 */     this.floatPattern = Pattern.compile(str9 + "|" + str10 + "|" + str13);
/*      */ 
/*  550 */     this.decimalPattern = Pattern.compile(str9);
/*      */   }
/*      */   private Pattern floatPattern() {
/*  553 */     if (this.floatPattern == null) {
/*  554 */       buildFloatAndDecimalPattern();
/*      */     }
/*  556 */     return this.floatPattern;
/*      */   }
/*      */   private Pattern decimalPattern() {
/*  559 */     if (this.decimalPattern == null) {
/*  560 */       buildFloatAndDecimalPattern();
/*      */     }
/*  562 */     return this.decimalPattern;
/*      */   }
/*      */ 
/*      */   private Scanner(Readable paramReadable, Pattern paramPattern)
/*      */   {
/*  576 */     assert (paramReadable != null) : "source should not be null";
/*  577 */     assert (paramPattern != null) : "pattern should not be null";
/*  578 */     this.source = paramReadable;
/*  579 */     this.delimPattern = paramPattern;
/*  580 */     this.buf = CharBuffer.allocate(1024);
/*  581 */     this.buf.limit(0);
/*  582 */     this.matcher = this.delimPattern.matcher(this.buf);
/*  583 */     this.matcher.useTransparentBounds(true);
/*  584 */     this.matcher.useAnchoringBounds(false);
/*  585 */     useLocale(Locale.getDefault(Locale.Category.FORMAT));
/*      */   }
/*      */ 
/*      */   public Scanner(Readable paramReadable)
/*      */   {
/*  596 */     this((Readable)Objects.requireNonNull(paramReadable, "source"), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   public Scanner(InputStream paramInputStream)
/*      */   {
/*  608 */     this(new InputStreamReader(paramInputStream), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   public Scanner(InputStream paramInputStream, String paramString)
/*      */   {
/*  623 */     this(makeReadable((InputStream)Objects.requireNonNull(paramInputStream, "source"), toCharset(paramString)), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   private static Charset toCharset(String paramString)
/*      */   {
/*  633 */     Objects.requireNonNull(paramString, "charsetName");
/*      */     try {
/*  635 */       return Charset.forName(paramString);
/*      */     }
/*      */     catch (IllegalCharsetNameException|UnsupportedCharsetException localIllegalCharsetNameException) {
/*  638 */       throw new IllegalArgumentException(localIllegalCharsetNameException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Readable makeReadable(InputStream paramInputStream, Charset paramCharset) {
/*  643 */     return new InputStreamReader(paramInputStream, paramCharset);
/*      */   }
/*      */ 
/*      */   public Scanner(File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/*  656 */     this(new FileInputStream(paramFile).getChannel());
/*      */   }
/*      */ 
/*      */   public Scanner(File paramFile, String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/*  674 */     this((File)Objects.requireNonNull(paramFile), toDecoder(paramString));
/*      */   }
/*      */ 
/*      */   private Scanner(File paramFile, CharsetDecoder paramCharsetDecoder)
/*      */     throws FileNotFoundException
/*      */   {
/*  680 */     this(makeReadable(new FileInputStream(paramFile).getChannel(), paramCharsetDecoder));
/*      */   }
/*      */ 
/*      */   private static CharsetDecoder toDecoder(String paramString) {
/*  684 */     Objects.requireNonNull(paramString, "charsetName");
/*      */     try {
/*  686 */       return Charset.forName(paramString).newDecoder(); } catch (IllegalCharsetNameException|UnsupportedCharsetException localIllegalCharsetNameException) {
/*      */     }
/*  688 */     throw new IllegalArgumentException(paramString);
/*      */   }
/*      */ 
/*      */   private static Readable makeReadable(ReadableByteChannel paramReadableByteChannel, CharsetDecoder paramCharsetDecoder)
/*      */   {
/*  694 */     return Channels.newReader(paramReadableByteChannel, paramCharsetDecoder, -1);
/*      */   }
/*      */ 
/*      */   public Scanner(Path paramPath)
/*      */     throws IOException
/*      */   {
/*  713 */     this(Files.newInputStream(paramPath, new OpenOption[0]));
/*      */   }
/*      */ 
/*      */   public Scanner(Path paramPath, String paramString)
/*      */     throws IOException
/*      */   {
/*  733 */     this((Path)Objects.requireNonNull(paramPath), toCharset(paramString));
/*      */   }
/*      */ 
/*      */   private Scanner(Path paramPath, Charset paramCharset) throws IOException {
/*  737 */     this(makeReadable(Files.newInputStream(paramPath, new OpenOption[0]), paramCharset));
/*      */   }
/*      */ 
/*      */   public Scanner(String paramString)
/*      */   {
/*  747 */     this(new StringReader(paramString), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   public Scanner(ReadableByteChannel paramReadableByteChannel)
/*      */   {
/*  759 */     this(makeReadable((ReadableByteChannel)Objects.requireNonNull(paramReadableByteChannel, "source")), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   private static Readable makeReadable(ReadableByteChannel paramReadableByteChannel)
/*      */   {
/*  764 */     return makeReadable(paramReadableByteChannel, Charset.defaultCharset().newDecoder());
/*      */   }
/*      */ 
/*      */   public Scanner(ReadableByteChannel paramReadableByteChannel, String paramString)
/*      */   {
/*  779 */     this(makeReadable((ReadableByteChannel)Objects.requireNonNull(paramReadableByteChannel, "source"), toDecoder(paramString)), WHITESPACE_PATTERN);
/*      */   }
/*      */ 
/*      */   private void saveState()
/*      */   {
/*  786 */     this.savedScannerPosition = this.position;
/*      */   }
/*      */ 
/*      */   private void revertState() {
/*  790 */     this.position = this.savedScannerPosition;
/*  791 */     this.savedScannerPosition = -1;
/*  792 */     this.skipped = false;
/*      */   }
/*      */ 
/*      */   private boolean revertState(boolean paramBoolean) {
/*  796 */     this.position = this.savedScannerPosition;
/*  797 */     this.savedScannerPosition = -1;
/*  798 */     this.skipped = false;
/*  799 */     return paramBoolean;
/*      */   }
/*      */ 
/*      */   private void cacheResult() {
/*  803 */     this.hasNextResult = this.matcher.group();
/*  804 */     this.hasNextPosition = this.matcher.end();
/*  805 */     this.hasNextPattern = this.matcher.pattern();
/*      */   }
/*      */ 
/*      */   private void cacheResult(String paramString) {
/*  809 */     this.hasNextResult = paramString;
/*  810 */     this.hasNextPosition = this.matcher.end();
/*  811 */     this.hasNextPattern = this.matcher.pattern();
/*      */   }
/*      */ 
/*      */   private void clearCaches()
/*      */   {
/*  816 */     this.hasNextPattern = null;
/*  817 */     this.typeCache = null;
/*      */   }
/*      */ 
/*      */   private String getCachedResult()
/*      */   {
/*  822 */     this.position = this.hasNextPosition;
/*  823 */     this.hasNextPattern = null;
/*  824 */     this.typeCache = null;
/*  825 */     return this.hasNextResult;
/*      */   }
/*      */ 
/*      */   private void useTypeCache()
/*      */   {
/*  830 */     if (this.closed)
/*  831 */       throw new IllegalStateException("Scanner closed");
/*  832 */     this.position = this.hasNextPosition;
/*  833 */     this.hasNextPattern = null;
/*  834 */     this.typeCache = null;
/*      */   }
/*      */ 
/*      */   private void readInput()
/*      */   {
/*  839 */     if (this.buf.limit() == this.buf.capacity()) {
/*  840 */       makeSpace();
/*      */     }
/*      */ 
/*  843 */     int i = this.buf.position();
/*  844 */     this.buf.position(this.buf.limit());
/*  845 */     this.buf.limit(this.buf.capacity());
/*      */ 
/*  847 */     int j = 0;
/*      */     try {
/*  849 */       j = this.source.read(this.buf);
/*      */     } catch (IOException localIOException) {
/*  851 */       this.lastException = localIOException;
/*  852 */       j = -1;
/*      */     }
/*      */ 
/*  855 */     if (j == -1) {
/*  856 */       this.sourceClosed = true;
/*  857 */       this.needInput = false;
/*      */     }
/*      */ 
/*  860 */     if (j > 0) {
/*  861 */       this.needInput = false;
/*      */     }
/*      */ 
/*  864 */     this.buf.limit(this.buf.position());
/*  865 */     this.buf.position(i);
/*      */   }
/*      */ 
/*      */   private boolean makeSpace()
/*      */   {
/*  871 */     clearCaches();
/*  872 */     int i = this.savedScannerPosition == -1 ? this.position : this.savedScannerPosition;
/*      */ 
/*  874 */     this.buf.position(i);
/*      */ 
/*  876 */     if (i > 0) {
/*  877 */       this.buf.compact();
/*  878 */       translateSavedIndexes(i);
/*  879 */       this.position -= i;
/*  880 */       this.buf.flip();
/*  881 */       return true;
/*      */     }
/*      */ 
/*  884 */     int j = this.buf.capacity() * 2;
/*  885 */     CharBuffer localCharBuffer = CharBuffer.allocate(j);
/*  886 */     localCharBuffer.put(this.buf);
/*  887 */     localCharBuffer.flip();
/*  888 */     translateSavedIndexes(i);
/*  889 */     this.position -= i;
/*  890 */     this.buf = localCharBuffer;
/*  891 */     this.matcher.reset(this.buf);
/*  892 */     return true;
/*      */   }
/*      */ 
/*      */   private void translateSavedIndexes(int paramInt)
/*      */   {
/*  898 */     if (this.savedScannerPosition != -1)
/*  899 */       this.savedScannerPosition -= paramInt;
/*      */   }
/*      */ 
/*      */   private void throwFor()
/*      */   {
/*  905 */     this.skipped = false;
/*  906 */     if ((this.sourceClosed) && (this.position == this.buf.limit())) {
/*  907 */       throw new NoSuchElementException();
/*      */     }
/*  909 */     throw new InputMismatchException();
/*      */   }
/*      */ 
/*      */   private boolean hasTokenInBuffer()
/*      */   {
/*  916 */     this.matchValid = false;
/*  917 */     this.matcher.usePattern(this.delimPattern);
/*  918 */     this.matcher.region(this.position, this.buf.limit());
/*      */ 
/*  921 */     if (this.matcher.lookingAt()) {
/*  922 */       this.position = this.matcher.end();
/*      */     }
/*      */ 
/*  925 */     if (this.position == this.buf.limit()) {
/*  926 */       return false;
/*      */     }
/*  928 */     return true;
/*      */   }
/*      */ 
/*      */   private String getCompleteTokenInBuffer(Pattern paramPattern)
/*      */   {
/*  947 */     this.matchValid = false;
/*      */ 
/*  950 */     this.matcher.usePattern(this.delimPattern);
/*  951 */     if (!this.skipped) {
/*  952 */       this.matcher.region(this.position, this.buf.limit());
/*  953 */       if (this.matcher.lookingAt())
/*      */       {
/*  956 */         if ((this.matcher.hitEnd()) && (!this.sourceClosed)) {
/*  957 */           this.needInput = true;
/*  958 */           return null;
/*      */         }
/*      */ 
/*  961 */         this.skipped = true;
/*  962 */         this.position = this.matcher.end();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  967 */     if (this.position == this.buf.limit()) {
/*  968 */       if (this.sourceClosed)
/*  969 */         return null;
/*  970 */       this.needInput = true;
/*  971 */       return null;
/*      */     }
/*      */ 
/*  980 */     this.matcher.region(this.position, this.buf.limit());
/*  981 */     boolean bool = this.matcher.find();
/*  982 */     if ((bool) && (this.matcher.end() == this.position))
/*      */     {
/*  986 */       bool = this.matcher.find();
/*      */     }
/*  988 */     if (bool)
/*      */     {
/*  995 */       if ((this.matcher.requireEnd()) && (!this.sourceClosed)) {
/*  996 */         this.needInput = true;
/*  997 */         return null;
/*      */       }
/*  999 */       int i = this.matcher.start();
/*      */ 
/* 1001 */       if (paramPattern == null)
/*      */       {
/* 1003 */         paramPattern = FIND_ANY_PATTERN;
/*      */       }
/*      */ 
/* 1006 */       this.matcher.usePattern(paramPattern);
/* 1007 */       this.matcher.region(this.position, i);
/* 1008 */       if (this.matcher.matches()) {
/* 1009 */         String str2 = this.matcher.group();
/* 1010 */         this.position = this.matcher.end();
/* 1011 */         return str2;
/*      */       }
/* 1013 */       return null;
/*      */     }
/*      */ 
/* 1019 */     if (this.sourceClosed) {
/* 1020 */       if (paramPattern == null)
/*      */       {
/* 1022 */         paramPattern = FIND_ANY_PATTERN;
/*      */       }
/*      */ 
/* 1025 */       this.matcher.usePattern(paramPattern);
/* 1026 */       this.matcher.region(this.position, this.buf.limit());
/* 1027 */       if (this.matcher.matches()) {
/* 1028 */         String str1 = this.matcher.group();
/* 1029 */         this.position = this.matcher.end();
/* 1030 */         return str1;
/*      */       }
/*      */ 
/* 1033 */       return null;
/*      */     }
/*      */ 
/* 1038 */     this.needInput = true;
/* 1039 */     return null;
/*      */   }
/*      */ 
/*      */   private String findPatternInBuffer(Pattern paramPattern, int paramInt)
/*      */   {
/* 1045 */     this.matchValid = false;
/* 1046 */     this.matcher.usePattern(paramPattern);
/* 1047 */     int i = this.buf.limit();
/* 1048 */     int j = -1;
/* 1049 */     int k = i;
/* 1050 */     if (paramInt > 0) {
/* 1051 */       j = this.position + paramInt;
/* 1052 */       if (j < i)
/* 1053 */         k = j;
/*      */     }
/* 1055 */     this.matcher.region(this.position, k);
/* 1056 */     if (this.matcher.find()) {
/* 1057 */       if ((this.matcher.hitEnd()) && (!this.sourceClosed))
/*      */       {
/* 1059 */         if (k != j)
/*      */         {
/* 1061 */           this.needInput = true;
/* 1062 */           return null;
/*      */         }
/*      */ 
/* 1065 */         if ((k == j) && (this.matcher.requireEnd()))
/*      */         {
/* 1069 */           this.needInput = true;
/* 1070 */           return null;
/*      */         }
/*      */       }
/*      */ 
/* 1074 */       this.position = this.matcher.end();
/* 1075 */       return this.matcher.group();
/*      */     }
/*      */ 
/* 1078 */     if (this.sourceClosed) {
/* 1079 */       return null;
/*      */     }
/*      */ 
/* 1083 */     if ((paramInt == 0) || (k != j))
/* 1084 */       this.needInput = true;
/* 1085 */     return null;
/*      */   }
/*      */ 
/*      */   private String matchPatternInBuffer(Pattern paramPattern)
/*      */   {
/* 1091 */     this.matchValid = false;
/* 1092 */     this.matcher.usePattern(paramPattern);
/* 1093 */     this.matcher.region(this.position, this.buf.limit());
/* 1094 */     if (this.matcher.lookingAt()) {
/* 1095 */       if ((this.matcher.hitEnd()) && (!this.sourceClosed))
/*      */       {
/* 1097 */         this.needInput = true;
/* 1098 */         return null;
/*      */       }
/* 1100 */       this.position = this.matcher.end();
/* 1101 */       return this.matcher.group();
/*      */     }
/*      */ 
/* 1104 */     if (this.sourceClosed) {
/* 1105 */       return null;
/*      */     }
/*      */ 
/* 1108 */     this.needInput = true;
/* 1109 */     return null;
/*      */   }
/*      */ 
/*      */   private void ensureOpen()
/*      */   {
/* 1114 */     if (this.closed)
/* 1115 */       throw new IllegalStateException("Scanner closed");
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1134 */     if (this.closed)
/* 1135 */       return;
/* 1136 */     if ((this.source instanceof Closeable)) {
/*      */       try {
/* 1138 */         ((Closeable)this.source).close();
/*      */       } catch (IOException localIOException) {
/* 1140 */         this.lastException = localIOException;
/*      */       }
/*      */     }
/* 1143 */     this.sourceClosed = true;
/* 1144 */     this.source = null;
/* 1145 */     this.closed = true;
/*      */   }
/*      */ 
/*      */   public IOException ioException()
/*      */   {
/* 1156 */     return this.lastException;
/*      */   }
/*      */ 
/*      */   public Pattern delimiter()
/*      */   {
/* 1166 */     return this.delimPattern;
/*      */   }
/*      */ 
/*      */   public Scanner useDelimiter(Pattern paramPattern)
/*      */   {
/* 1176 */     this.delimPattern = paramPattern;
/* 1177 */     return this;
/*      */   }
/*      */ 
/*      */   public Scanner useDelimiter(String paramString)
/*      */   {
/* 1195 */     this.delimPattern = ((Pattern)this.patternCache.forName(paramString));
/* 1196 */     return this;
/*      */   }
/*      */ 
/*      */   public Locale locale()
/*      */   {
/* 1209 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public Scanner useLocale(Locale paramLocale)
/*      */   {
/* 1226 */     if (paramLocale.equals(this.locale)) {
/* 1227 */       return this;
/*      */     }
/* 1229 */     this.locale = paramLocale;
/* 1230 */     DecimalFormat localDecimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(paramLocale);
/*      */ 
/* 1232 */     DecimalFormatSymbols localDecimalFormatSymbols = DecimalFormatSymbols.getInstance(paramLocale);
/*      */ 
/* 1236 */     this.groupSeparator = ("\\" + localDecimalFormatSymbols.getGroupingSeparator());
/* 1237 */     this.decimalSeparator = ("\\" + localDecimalFormatSymbols.getDecimalSeparator());
/*      */ 
/* 1241 */     this.nanString = ("\\Q" + localDecimalFormatSymbols.getNaN() + "\\E");
/* 1242 */     this.infinityString = ("\\Q" + localDecimalFormatSymbols.getInfinity() + "\\E");
/* 1243 */     this.positivePrefix = localDecimalFormat.getPositivePrefix();
/* 1244 */     if (this.positivePrefix.length() > 0)
/* 1245 */       this.positivePrefix = ("\\Q" + this.positivePrefix + "\\E");
/* 1246 */     this.negativePrefix = localDecimalFormat.getNegativePrefix();
/* 1247 */     if (this.negativePrefix.length() > 0)
/* 1248 */       this.negativePrefix = ("\\Q" + this.negativePrefix + "\\E");
/* 1249 */     this.positiveSuffix = localDecimalFormat.getPositiveSuffix();
/* 1250 */     if (this.positiveSuffix.length() > 0)
/* 1251 */       this.positiveSuffix = ("\\Q" + this.positiveSuffix + "\\E");
/* 1252 */     this.negativeSuffix = localDecimalFormat.getNegativeSuffix();
/* 1253 */     if (this.negativeSuffix.length() > 0) {
/* 1254 */       this.negativeSuffix = ("\\Q" + this.negativeSuffix + "\\E");
/*      */     }
/*      */ 
/* 1258 */     this.integerPattern = null;
/* 1259 */     this.floatPattern = null;
/*      */ 
/* 1261 */     return this;
/*      */   }
/*      */ 
/*      */   public int radix()
/*      */   {
/* 1274 */     return this.defaultRadix;
/*      */   }
/*      */ 
/*      */   public Scanner useRadix(int paramInt)
/*      */   {
/* 1296 */     if ((paramInt < 2) || (paramInt > 36)) {
/* 1297 */       throw new IllegalArgumentException("radix:" + paramInt);
/*      */     }
/* 1299 */     if (this.defaultRadix == paramInt)
/* 1300 */       return this;
/* 1301 */     this.defaultRadix = paramInt;
/*      */ 
/* 1303 */     this.integerPattern = null;
/* 1304 */     return this;
/*      */   }
/*      */ 
/*      */   private void setRadix(int paramInt)
/*      */   {
/* 1310 */     if (this.radix != paramInt)
/*      */     {
/* 1312 */       this.integerPattern = null;
/* 1313 */       this.radix = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MatchResult match()
/*      */   {
/* 1337 */     if (!this.matchValid)
/* 1338 */       throw new IllegalStateException("No match result available");
/* 1339 */     return this.matcher.toMatchResult();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1350 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1351 */     localStringBuilder.append("java.util.Scanner");
/* 1352 */     localStringBuilder.append("[delimiters=" + this.delimPattern + "]");
/* 1353 */     localStringBuilder.append("[position=" + this.position + "]");
/* 1354 */     localStringBuilder.append("[match valid=" + this.matchValid + "]");
/* 1355 */     localStringBuilder.append("[need input=" + this.needInput + "]");
/* 1356 */     localStringBuilder.append("[source closed=" + this.sourceClosed + "]");
/* 1357 */     localStringBuilder.append("[skipped=" + this.skipped + "]");
/* 1358 */     localStringBuilder.append("[group separator=" + this.groupSeparator + "]");
/* 1359 */     localStringBuilder.append("[decimal separator=" + this.decimalSeparator + "]");
/* 1360 */     localStringBuilder.append("[positive prefix=" + this.positivePrefix + "]");
/* 1361 */     localStringBuilder.append("[negative prefix=" + this.negativePrefix + "]");
/* 1362 */     localStringBuilder.append("[positive suffix=" + this.positiveSuffix + "]");
/* 1363 */     localStringBuilder.append("[negative suffix=" + this.negativeSuffix + "]");
/* 1364 */     localStringBuilder.append("[NaN string=" + this.nanString + "]");
/* 1365 */     localStringBuilder.append("[infinity string=" + this.infinityString + "]");
/* 1366 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public boolean hasNext()
/*      */   {
/* 1379 */     ensureOpen();
/* 1380 */     saveState();
/* 1381 */     while (!this.sourceClosed) {
/* 1382 */       if (hasTokenInBuffer())
/* 1383 */         return revertState(true);
/* 1384 */       readInput();
/*      */     }
/* 1386 */     boolean bool = hasTokenInBuffer();
/* 1387 */     return revertState(bool);
/*      */   }
/*      */ 
/*      */   public String next()
/*      */   {
/* 1403 */     ensureOpen();
/* 1404 */     clearCaches();
/*      */     while (true)
/*      */     {
/* 1407 */       String str = getCompleteTokenInBuffer(null);
/* 1408 */       if (str != null) {
/* 1409 */         this.matchValid = true;
/* 1410 */         this.skipped = false;
/* 1411 */         return str;
/*      */       }
/* 1413 */       if (this.needInput)
/* 1414 */         readInput();
/*      */       else
/* 1416 */         throwFor();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void remove()
/*      */   {
/* 1428 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean hasNext(String paramString)
/*      */   {
/* 1445 */     return hasNext((Pattern)this.patternCache.forName(paramString));
/*      */   }
/*      */ 
/*      */   public String next(String paramString)
/*      */   {
/* 1463 */     return next((Pattern)this.patternCache.forName(paramString));
/*      */   }
/*      */ 
/*      */   public boolean hasNext(Pattern paramPattern)
/*      */   {
/* 1478 */     ensureOpen();
/* 1479 */     if (paramPattern == null)
/* 1480 */       throw new NullPointerException();
/* 1481 */     this.hasNextPattern = null;
/* 1482 */     saveState();
/*      */     while (true)
/*      */     {
/* 1485 */       if (getCompleteTokenInBuffer(paramPattern) != null) {
/* 1486 */         this.matchValid = true;
/* 1487 */         cacheResult();
/* 1488 */         return revertState(true);
/*      */       }
/* 1490 */       if (!this.needInput) break;
/* 1491 */       readInput();
/*      */     }
/* 1493 */     return revertState(false);
/*      */   }
/*      */ 
/*      */   public String next(Pattern paramPattern)
/*      */   {
/* 1510 */     ensureOpen();
/* 1511 */     if (paramPattern == null) {
/* 1512 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 1515 */     if (this.hasNextPattern == paramPattern)
/* 1516 */       return getCachedResult();
/* 1517 */     clearCaches();
/*      */     while (true)
/*      */     {
/* 1521 */       String str = getCompleteTokenInBuffer(paramPattern);
/* 1522 */       if (str != null) {
/* 1523 */         this.matchValid = true;
/* 1524 */         this.skipped = false;
/* 1525 */         return str;
/*      */       }
/* 1527 */       if (this.needInput)
/* 1528 */         readInput();
/*      */       else
/* 1530 */         throwFor();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextLine()
/*      */   {
/* 1543 */     saveState();
/*      */ 
/* 1545 */     String str1 = findWithinHorizon(linePattern(), 0);
/* 1546 */     if (str1 != null) {
/* 1547 */       MatchResult localMatchResult = match();
/* 1548 */       String str2 = localMatchResult.group(1);
/* 1549 */       if (str2 != null) {
/* 1550 */         str1 = str1.substring(0, str1.length() - str2.length());
/*      */ 
/* 1552 */         cacheResult(str1);
/*      */       }
/*      */       else {
/* 1555 */         cacheResult();
/*      */       }
/*      */     }
/* 1558 */     revertState();
/* 1559 */     return str1 != null;
/*      */   }
/*      */ 
/*      */   public String nextLine()
/*      */   {
/* 1579 */     if (this.hasNextPattern == linePattern())
/* 1580 */       return getCachedResult();
/* 1581 */     clearCaches();
/*      */ 
/* 1583 */     String str1 = findWithinHorizon(linePattern, 0);
/* 1584 */     if (str1 == null)
/* 1585 */       throw new NoSuchElementException("No line found");
/* 1586 */     MatchResult localMatchResult = match();
/* 1587 */     String str2 = localMatchResult.group(1);
/* 1588 */     if (str2 != null)
/* 1589 */       str1 = str1.substring(0, str1.length() - str2.length());
/* 1590 */     if (str1 == null) {
/* 1591 */       throw new NoSuchElementException();
/*      */     }
/* 1593 */     return str1;
/*      */   }
/*      */ 
/*      */   public String findInLine(String paramString)
/*      */   {
/* 1611 */     return findInLine((Pattern)this.patternCache.forName(paramString));
/*      */   }
/*      */ 
/*      */   public String findInLine(Pattern paramPattern)
/*      */   {
/* 1633 */     ensureOpen();
/* 1634 */     if (paramPattern == null)
/* 1635 */       throw new NullPointerException();
/* 1636 */     clearCaches();
/*      */ 
/* 1638 */     int i = 0;
/* 1639 */     saveState();
/*      */     while (true) {
/* 1641 */       String str = findPatternInBuffer(separatorPattern(), 0);
/* 1642 */       if (str != null) {
/* 1643 */         i = this.matcher.start();
/* 1644 */         break;
/*      */       }
/* 1646 */       if (this.needInput) {
/* 1647 */         readInput();
/*      */       } else {
/* 1649 */         i = this.buf.limit();
/* 1650 */         break;
/*      */       }
/*      */     }
/* 1653 */     revertState();
/* 1654 */     int j = i - this.position;
/*      */ 
/* 1658 */     if (j == 0) {
/* 1659 */       return null;
/*      */     }
/* 1661 */     return findWithinHorizon(paramPattern, j);
/*      */   }
/*      */ 
/*      */   public String findWithinHorizon(String paramString, int paramInt)
/*      */   {
/* 1679 */     return findWithinHorizon((Pattern)this.patternCache.forName(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public String findWithinHorizon(Pattern paramPattern, int paramInt)
/*      */   {
/* 1713 */     ensureOpen();
/* 1714 */     if (paramPattern == null)
/* 1715 */       throw new NullPointerException();
/* 1716 */     if (paramInt < 0)
/* 1717 */       throw new IllegalArgumentException("horizon < 0");
/* 1718 */     clearCaches();
/*      */     while (true)
/*      */     {
/* 1722 */       String str = findPatternInBuffer(paramPattern, paramInt);
/* 1723 */       if (str != null) {
/* 1724 */         this.matchValid = true;
/* 1725 */         return str;
/*      */       }
/* 1727 */       if (!this.needInput) break;
/* 1728 */       readInput();
/*      */     }
/*      */ 
/* 1732 */     return null;
/*      */   }
/*      */ 
/*      */   public Scanner skip(Pattern paramPattern)
/*      */   {
/* 1759 */     ensureOpen();
/* 1760 */     if (paramPattern == null)
/* 1761 */       throw new NullPointerException();
/* 1762 */     clearCaches();
/*      */     while (true)
/*      */     {
/* 1766 */       String str = matchPatternInBuffer(paramPattern);
/* 1767 */       if (str != null) {
/* 1768 */         this.matchValid = true;
/* 1769 */         this.position = this.matcher.end();
/* 1770 */         return this;
/*      */       }
/* 1772 */       if (this.needInput)
/* 1773 */         readInput();
/*      */       else
/* 1775 */         throw new NoSuchElementException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Scanner skip(String paramString)
/*      */   {
/* 1792 */     return skip((Pattern)this.patternCache.forName(paramString));
/*      */   }
/*      */ 
/*      */   public boolean hasNextBoolean()
/*      */   {
/* 1808 */     return hasNext(boolPattern());
/*      */   }
/*      */ 
/*      */   public boolean nextBoolean()
/*      */   {
/* 1824 */     clearCaches();
/* 1825 */     return Boolean.parseBoolean(next(boolPattern()));
/*      */   }
/*      */ 
/*      */   public boolean hasNextByte()
/*      */   {
/* 1838 */     return hasNextByte(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public boolean hasNextByte(int paramInt)
/*      */   {
/* 1852 */     setRadix(paramInt);
/* 1853 */     boolean bool = hasNext(integerPattern());
/* 1854 */     if (bool) {
/*      */       try {
/* 1856 */         String str = this.matcher.group(this.SIMPLE_GROUP_INDEX) == null ? processIntegerToken(this.hasNextResult) : this.hasNextResult;
/*      */ 
/* 1859 */         this.typeCache = Byte.valueOf(Byte.parseByte(str, paramInt));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 1861 */         bool = false;
/*      */       }
/*      */     }
/* 1864 */     return bool;
/*      */   }
/*      */ 
/*      */   public byte nextByte()
/*      */   {
/* 1883 */     return nextByte(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public byte nextByte(int paramInt)
/*      */   {
/* 1914 */     if ((this.typeCache != null) && ((this.typeCache instanceof Byte)) && (this.radix == paramInt))
/*      */     {
/* 1916 */       byte b = ((Byte)this.typeCache).byteValue();
/* 1917 */       useTypeCache();
/* 1918 */       return b;
/*      */     }
/* 1920 */     setRadix(paramInt);
/* 1921 */     clearCaches();
/*      */     try
/*      */     {
/* 1924 */       String str = next(integerPattern());
/* 1925 */       if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null)
/* 1926 */         str = processIntegerToken(str);
/* 1927 */       return Byte.parseByte(str, paramInt);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 1929 */       this.position = this.matcher.start();
/* 1930 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextShort()
/*      */   {
/* 1944 */     return hasNextShort(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public boolean hasNextShort(int paramInt)
/*      */   {
/* 1958 */     setRadix(paramInt);
/* 1959 */     boolean bool = hasNext(integerPattern());
/* 1960 */     if (bool) {
/*      */       try {
/* 1962 */         String str = this.matcher.group(this.SIMPLE_GROUP_INDEX) == null ? processIntegerToken(this.hasNextResult) : this.hasNextResult;
/*      */ 
/* 1965 */         this.typeCache = Short.valueOf(Short.parseShort(str, paramInt));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 1967 */         bool = false;
/*      */       }
/*      */     }
/* 1970 */     return bool;
/*      */   }
/*      */ 
/*      */   public short nextShort()
/*      */   {
/* 1989 */     return nextShort(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public short nextShort(int paramInt)
/*      */   {
/* 2020 */     if ((this.typeCache != null) && ((this.typeCache instanceof Short)) && (this.radix == paramInt))
/*      */     {
/* 2022 */       short s = ((Short)this.typeCache).shortValue();
/* 2023 */       useTypeCache();
/* 2024 */       return s;
/*      */     }
/* 2026 */     setRadix(paramInt);
/* 2027 */     clearCaches();
/*      */     try
/*      */     {
/* 2030 */       String str = next(integerPattern());
/* 2031 */       if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null)
/* 2032 */         str = processIntegerToken(str);
/* 2033 */       return Short.parseShort(str, paramInt);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2035 */       this.position = this.matcher.start();
/* 2036 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextInt()
/*      */   {
/* 2050 */     return hasNextInt(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public boolean hasNextInt(int paramInt)
/*      */   {
/* 2064 */     setRadix(paramInt);
/* 2065 */     boolean bool = hasNext(integerPattern());
/* 2066 */     if (bool) {
/*      */       try {
/* 2068 */         String str = this.matcher.group(this.SIMPLE_GROUP_INDEX) == null ? processIntegerToken(this.hasNextResult) : this.hasNextResult;
/*      */ 
/* 2071 */         this.typeCache = Integer.valueOf(Integer.parseInt(str, paramInt));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2073 */         bool = false;
/*      */       }
/*      */     }
/* 2076 */     return bool;
/*      */   }
/*      */ 
/*      */   private String processIntegerToken(String paramString)
/*      */   {
/* 2085 */     String str = paramString.replaceAll("" + this.groupSeparator, "");
/* 2086 */     int i = 0;
/* 2087 */     int j = this.negativePrefix.length();
/* 2088 */     if ((j > 0) && (str.startsWith(this.negativePrefix))) {
/* 2089 */       i = 1;
/* 2090 */       str = str.substring(j);
/*      */     }
/* 2092 */     int k = this.negativeSuffix.length();
/* 2093 */     if ((k > 0) && (str.endsWith(this.negativeSuffix))) {
/* 2094 */       i = 1;
/* 2095 */       str = str.substring(str.length() - k, str.length());
/*      */     }
/*      */ 
/* 2098 */     if (i != 0)
/* 2099 */       str = "-" + str;
/* 2100 */     return str;
/*      */   }
/*      */ 
/*      */   public int nextInt()
/*      */   {
/* 2119 */     return nextInt(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public int nextInt(int paramInt)
/*      */   {
/* 2150 */     if ((this.typeCache != null) && ((this.typeCache instanceof Integer)) && (this.radix == paramInt))
/*      */     {
/* 2152 */       int i = ((Integer)this.typeCache).intValue();
/* 2153 */       useTypeCache();
/* 2154 */       return i;
/*      */     }
/* 2156 */     setRadix(paramInt);
/* 2157 */     clearCaches();
/*      */     try
/*      */     {
/* 2160 */       String str = next(integerPattern());
/* 2161 */       if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null)
/* 2162 */         str = processIntegerToken(str);
/* 2163 */       return Integer.parseInt(str, paramInt);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2165 */       this.position = this.matcher.start();
/* 2166 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextLong()
/*      */   {
/* 2180 */     return hasNextLong(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public boolean hasNextLong(int paramInt)
/*      */   {
/* 2194 */     setRadix(paramInt);
/* 2195 */     boolean bool = hasNext(integerPattern());
/* 2196 */     if (bool) {
/*      */       try {
/* 2198 */         String str = this.matcher.group(this.SIMPLE_GROUP_INDEX) == null ? processIntegerToken(this.hasNextResult) : this.hasNextResult;
/*      */ 
/* 2201 */         this.typeCache = Long.valueOf(Long.parseLong(str, paramInt));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2203 */         bool = false;
/*      */       }
/*      */     }
/* 2206 */     return bool;
/*      */   }
/*      */ 
/*      */   public long nextLong()
/*      */   {
/* 2225 */     return nextLong(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public long nextLong(int paramInt)
/*      */   {
/* 2256 */     if ((this.typeCache != null) && ((this.typeCache instanceof Long)) && (this.radix == paramInt))
/*      */     {
/* 2258 */       long l = ((Long)this.typeCache).longValue();
/* 2259 */       useTypeCache();
/* 2260 */       return l;
/*      */     }
/* 2262 */     setRadix(paramInt);
/* 2263 */     clearCaches();
/*      */     try {
/* 2265 */       String str = next(integerPattern());
/* 2266 */       if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null)
/* 2267 */         str = processIntegerToken(str);
/* 2268 */       return Long.parseLong(str, paramInt);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2270 */       this.position = this.matcher.start();
/* 2271 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private String processFloatToken(String paramString)
/*      */   {
/* 2284 */     String str = paramString.replaceAll(this.groupSeparator, "");
/* 2285 */     if (!this.decimalSeparator.equals("\\."))
/* 2286 */       str = str.replaceAll(this.decimalSeparator, ".");
/* 2287 */     int i = 0;
/* 2288 */     int j = this.negativePrefix.length();
/* 2289 */     if ((j > 0) && (str.startsWith(this.negativePrefix))) {
/* 2290 */       i = 1;
/* 2291 */       str = str.substring(j);
/*      */     }
/* 2293 */     int k = this.negativeSuffix.length();
/* 2294 */     if ((k > 0) && (str.endsWith(this.negativeSuffix))) {
/* 2295 */       i = 1;
/* 2296 */       str = str.substring(str.length() - k, str.length());
/*      */     }
/*      */ 
/* 2299 */     if (str.equals(this.nanString))
/* 2300 */       str = "NaN";
/* 2301 */     if (str.equals(this.infinityString))
/* 2302 */       str = "Infinity";
/* 2303 */     if (i != 0) {
/* 2304 */       str = "-" + str;
/*      */     }
/*      */ 
/* 2307 */     Matcher localMatcher = NON_ASCII_DIGIT.matcher(str);
/* 2308 */     if (localMatcher.find()) {
/* 2309 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2310 */       for (int m = 0; m < str.length(); m++) {
/* 2311 */         char c = str.charAt(m);
/* 2312 */         if (Character.isDigit(c)) {
/* 2313 */           int n = Character.digit(c, 10);
/* 2314 */           if (n != -1)
/* 2315 */             localStringBuilder.append(n);
/*      */           else
/* 2317 */             localStringBuilder.append(c);
/*      */         } else {
/* 2319 */           localStringBuilder.append(c);
/*      */         }
/*      */       }
/* 2322 */       str = localStringBuilder.toString();
/*      */     }
/*      */ 
/* 2325 */     return str;
/*      */   }
/*      */ 
/*      */   public boolean hasNextFloat()
/*      */   {
/* 2338 */     setRadix(10);
/* 2339 */     boolean bool = hasNext(floatPattern());
/* 2340 */     if (bool) {
/*      */       try {
/* 2342 */         String str = processFloatToken(this.hasNextResult);
/* 2343 */         this.typeCache = Float.valueOf(Float.parseFloat(str));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2345 */         bool = false;
/*      */       }
/*      */     }
/* 2348 */     return bool;
/*      */   }
/*      */ 
/*      */   public float nextFloat()
/*      */   {
/* 2380 */     if ((this.typeCache != null) && ((this.typeCache instanceof Float))) {
/* 2381 */       float f = ((Float)this.typeCache).floatValue();
/* 2382 */       useTypeCache();
/* 2383 */       return f;
/*      */     }
/* 2385 */     setRadix(10);
/* 2386 */     clearCaches();
/*      */     try {
/* 2388 */       return Float.parseFloat(processFloatToken(next(floatPattern())));
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2390 */       this.position = this.matcher.start();
/* 2391 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextDouble()
/*      */   {
/* 2405 */     setRadix(10);
/* 2406 */     boolean bool = hasNext(floatPattern());
/* 2407 */     if (bool) {
/*      */       try {
/* 2409 */         String str = processFloatToken(this.hasNextResult);
/* 2410 */         this.typeCache = Double.valueOf(Double.parseDouble(str));
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2412 */         bool = false;
/*      */       }
/*      */     }
/* 2415 */     return bool;
/*      */   }
/*      */ 
/*      */   public double nextDouble()
/*      */   {
/* 2447 */     if ((this.typeCache != null) && ((this.typeCache instanceof Double))) {
/* 2448 */       double d = ((Double)this.typeCache).doubleValue();
/* 2449 */       useTypeCache();
/* 2450 */       return d;
/*      */     }
/* 2452 */     setRadix(10);
/* 2453 */     clearCaches();
/*      */     try
/*      */     {
/* 2456 */       return Double.parseDouble(processFloatToken(next(floatPattern())));
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2458 */       this.position = this.matcher.start();
/* 2459 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextBigInteger()
/*      */   {
/* 2476 */     return hasNextBigInteger(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public boolean hasNextBigInteger(int paramInt)
/*      */   {
/* 2491 */     setRadix(paramInt);
/* 2492 */     boolean bool = hasNext(integerPattern());
/* 2493 */     if (bool) {
/*      */       try {
/* 2495 */         String str = this.matcher.group(this.SIMPLE_GROUP_INDEX) == null ? processIntegerToken(this.hasNextResult) : this.hasNextResult;
/*      */ 
/* 2498 */         this.typeCache = new BigInteger(str, paramInt);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2500 */         bool = false;
/*      */       }
/*      */     }
/* 2503 */     return bool;
/*      */   }
/*      */ 
/*      */   public BigInteger nextBigInteger()
/*      */   {
/* 2523 */     return nextBigInteger(this.defaultRadix);
/*      */   }
/*      */ 
/*      */   public BigInteger nextBigInteger(int paramInt)
/*      */   {
/*      */     Object localObject;
/* 2549 */     if ((this.typeCache != null) && ((this.typeCache instanceof BigInteger)) && (this.radix == paramInt))
/*      */     {
/* 2551 */       localObject = (BigInteger)this.typeCache;
/* 2552 */       useTypeCache();
/* 2553 */       return localObject;
/*      */     }
/* 2555 */     setRadix(paramInt);
/* 2556 */     clearCaches();
/*      */     try
/*      */     {
/* 2559 */       localObject = next(integerPattern());
/* 2560 */       if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null)
/* 2561 */         localObject = processIntegerToken((String)localObject);
/* 2562 */       return new BigInteger((String)localObject, paramInt);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2564 */       this.position = this.matcher.start();
/* 2565 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean hasNextBigDecimal()
/*      */   {
/* 2580 */     setRadix(10);
/* 2581 */     boolean bool = hasNext(decimalPattern());
/* 2582 */     if (bool) {
/*      */       try {
/* 2584 */         String str = processFloatToken(this.hasNextResult);
/* 2585 */         this.typeCache = new BigDecimal(str);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 2587 */         bool = false;
/*      */       }
/*      */     }
/* 2590 */     return bool;
/*      */   }
/*      */ 
/*      */   public BigDecimal nextBigDecimal()
/*      */   {
/*      */     Object localObject;
/* 2615 */     if ((this.typeCache != null) && ((this.typeCache instanceof BigDecimal))) {
/* 2616 */       localObject = (BigDecimal)this.typeCache;
/* 2617 */       useTypeCache();
/* 2618 */       return localObject;
/*      */     }
/* 2620 */     setRadix(10);
/* 2621 */     clearCaches();
/*      */     try
/*      */     {
/* 2624 */       localObject = processFloatToken(next(decimalPattern()));
/* 2625 */       return new BigDecimal((String)localObject);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 2627 */       this.position = this.matcher.start();
/* 2628 */       throw new InputMismatchException(localNumberFormatException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public Scanner reset()
/*      */   {
/* 2654 */     this.delimPattern = WHITESPACE_PATTERN;
/* 2655 */     useLocale(Locale.getDefault(Locale.Category.FORMAT));
/* 2656 */     useRadix(10);
/* 2657 */     clearCaches();
/* 2658 */     return this;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Scanner
 * JD-Core Version:    0.6.2
 */