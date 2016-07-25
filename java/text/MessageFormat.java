/*      */ package java.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Locale.Category;
/*      */ 
/*      */ public class MessageFormat extends Format
/*      */ {
/*      */   private static final long serialVersionUID = 6479157306784022952L;
/*      */   private Locale locale;
/* 1179 */   private String pattern = "";
/*      */   private static final int INITIAL_FORMATS = 10;
/* 1188 */   private Format[] formats = new Format[10];
/*      */ 
/* 1195 */   private int[] offsets = new int[10];
/*      */ 
/* 1203 */   private int[] argumentNumbers = new int[10];
/*      */ 
/* 1212 */   private int maxOffset = -1;
/*      */   private static final int SEG_RAW = 0;
/*      */   private static final int SEG_INDEX = 1;
/*      */   private static final int SEG_TYPE = 2;
/*      */   private static final int SEG_MODIFIER = 3;
/*      */   private static final int TYPE_NULL = 0;
/*      */   private static final int TYPE_NUMBER = 1;
/*      */   private static final int TYPE_DATE = 2;
/*      */   private static final int TYPE_TIME = 3;
/*      */   private static final int TYPE_CHOICE = 4;
/* 1362 */   private static final String[] TYPE_KEYWORDS = { "", "number", "date", "time", "choice" };
/*      */   private static final int MODIFIER_DEFAULT = 0;
/*      */   private static final int MODIFIER_CURRENCY = 1;
/*      */   private static final int MODIFIER_PERCENT = 2;
/*      */   private static final int MODIFIER_INTEGER = 3;
/* 1376 */   private static final String[] NUMBER_MODIFIER_KEYWORDS = { "", "currency", "percent", "integer" };
/*      */   private static final int MODIFIER_SHORT = 1;
/*      */   private static final int MODIFIER_MEDIUM = 2;
/*      */   private static final int MODIFIER_LONG = 3;
/*      */   private static final int MODIFIER_FULL = 4;
/* 1389 */   private static final String[] DATE_TIME_MODIFIER_KEYWORDS = { "", "short", "medium", "long", "full" };
/*      */ 
/* 1398 */   private static final int[] DATE_TIME_MODIFIERS = { 2, 3, 2, 1, 0 };
/*      */ 
/*      */   public MessageFormat(String paramString)
/*      */   {
/*  362 */     this.locale = Locale.getDefault(Locale.Category.FORMAT);
/*  363 */     applyPattern(paramString);
/*      */   }
/*      */ 
/*      */   public MessageFormat(String paramString, Locale paramLocale)
/*      */   {
/*  380 */     this.locale = paramLocale;
/*  381 */     applyPattern(paramString);
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale paramLocale)
/*      */   {
/*  402 */     this.locale = paramLocale;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  411 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public void applyPattern(String paramString)
/*      */   {
/*  426 */     StringBuilder[] arrayOfStringBuilder = new StringBuilder[4];
/*      */ 
/*  429 */     arrayOfStringBuilder[0] = new StringBuilder();
/*      */ 
/*  431 */     int i = 0;
/*  432 */     int j = 0;
/*  433 */     int k = 0;
/*  434 */     int m = 0;
/*  435 */     this.maxOffset = -1;
/*  436 */     for (int n = 0; n < paramString.length(); n++) {
/*  437 */       char c = paramString.charAt(n);
/*  438 */       if (i == 0) {
/*  439 */         if (c == '\'') {
/*  440 */           if ((n + 1 < paramString.length()) && (paramString.charAt(n + 1) == '\''))
/*      */           {
/*  442 */             arrayOfStringBuilder[i].append(c);
/*  443 */             n++;
/*      */           } else {
/*  445 */             k = k == 0 ? 1 : 0;
/*      */           }
/*  447 */         } else if ((c == '{') && (k == 0)) {
/*  448 */           i = 1;
/*  449 */           if (arrayOfStringBuilder[1] == null)
/*  450 */             arrayOfStringBuilder[1] = new StringBuilder();
/*      */         }
/*      */         else {
/*  453 */           arrayOfStringBuilder[i].append(c);
/*      */         }
/*      */       }
/*  456 */       else if (k != 0) {
/*  457 */         arrayOfStringBuilder[i].append(c);
/*  458 */         if (c == '\'')
/*  459 */           k = 0;
/*      */       }
/*      */       else {
/*  462 */         switch (c) {
/*      */         case ',':
/*  464 */           if (i < 3) {
/*  465 */             if (arrayOfStringBuilder[(++i)] == null)
/*  466 */               arrayOfStringBuilder[i] = new StringBuilder();
/*      */           }
/*      */           else {
/*  469 */             arrayOfStringBuilder[i].append(c);
/*      */           }
/*  471 */           break;
/*      */         case '{':
/*  473 */           m++;
/*  474 */           arrayOfStringBuilder[i].append(c);
/*  475 */           break;
/*      */         case '}':
/*  477 */           if (m == 0) {
/*  478 */             i = 0;
/*  479 */             makeFormat(n, j, arrayOfStringBuilder);
/*  480 */             j++;
/*      */ 
/*  482 */             arrayOfStringBuilder[1] = null;
/*  483 */             arrayOfStringBuilder[2] = null;
/*  484 */             arrayOfStringBuilder[3] = null;
/*      */           } else {
/*  486 */             m--;
/*  487 */             arrayOfStringBuilder[i].append(c);
/*      */           }
/*  489 */           break;
/*      */         case ' ':
/*  492 */           if ((i != 2) || (arrayOfStringBuilder[2].length() > 0))
/*  493 */             arrayOfStringBuilder[i].append(c); break;
/*      */         case '\'':
/*  497 */           k = 1;
/*      */         default:
/*  500 */           arrayOfStringBuilder[i].append(c);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  506 */     if ((m == 0) && (i != 0)) {
/*  507 */       this.maxOffset = -1;
/*  508 */       throw new IllegalArgumentException("Unmatched braces in the pattern.");
/*      */     }
/*  510 */     this.pattern = arrayOfStringBuilder[0].toString();
/*      */   }
/*      */ 
/*      */   public String toPattern()
/*      */   {
/*  523 */     int i = 0;
/*  524 */     StringBuilder localStringBuilder = new StringBuilder();
/*  525 */     for (int j = 0; j <= this.maxOffset; j++) {
/*  526 */       copyAndFixQuotes(this.pattern, i, this.offsets[j], localStringBuilder);
/*  527 */       i = this.offsets[j];
/*  528 */       localStringBuilder.append('{').append(this.argumentNumbers[j]);
/*  529 */       Format localFormat = this.formats[j];
/*  530 */       if (localFormat != null)
/*      */       {
/*  532 */         if ((localFormat instanceof NumberFormat)) {
/*  533 */           if (localFormat.equals(NumberFormat.getInstance(this.locale)))
/*  534 */             localStringBuilder.append(",number");
/*  535 */           else if (localFormat.equals(NumberFormat.getCurrencyInstance(this.locale)))
/*  536 */             localStringBuilder.append(",number,currency");
/*  537 */           else if (localFormat.equals(NumberFormat.getPercentInstance(this.locale)))
/*  538 */             localStringBuilder.append(",number,percent");
/*  539 */           else if (localFormat.equals(NumberFormat.getIntegerInstance(this.locale))) {
/*  540 */             localStringBuilder.append(",number,integer");
/*      */           }
/*  542 */           else if ((localFormat instanceof DecimalFormat))
/*  543 */             localStringBuilder.append(",number,").append(((DecimalFormat)localFormat).toPattern());
/*  544 */           else if ((localFormat instanceof ChoiceFormat)) {
/*  545 */             localStringBuilder.append(",choice,").append(((ChoiceFormat)localFormat).toPattern());
/*      */           }
/*      */ 
/*      */         }
/*  550 */         else if ((localFormat instanceof DateFormat))
/*      */         {
/*  552 */           for (int k = 0; k < DATE_TIME_MODIFIERS.length; k++) {
/*  553 */             DateFormat localDateFormat = DateFormat.getDateInstance(DATE_TIME_MODIFIERS[k], this.locale);
/*      */ 
/*  555 */             if (localFormat.equals(localDateFormat)) {
/*  556 */               localStringBuilder.append(",date");
/*  557 */               break;
/*      */             }
/*  559 */             localDateFormat = DateFormat.getTimeInstance(DATE_TIME_MODIFIERS[k], this.locale);
/*      */ 
/*  561 */             if (localFormat.equals(localDateFormat)) {
/*  562 */               localStringBuilder.append(",time");
/*  563 */               break;
/*      */             }
/*      */           }
/*  566 */           if (k >= DATE_TIME_MODIFIERS.length) {
/*  567 */             if ((localFormat instanceof SimpleDateFormat)) {
/*  568 */               localStringBuilder.append(",date,").append(((SimpleDateFormat)localFormat).toPattern());
/*      */             }
/*      */ 
/*      */           }
/*  572 */           else if (k != 0) {
/*  573 */             localStringBuilder.append(',').append(DATE_TIME_MODIFIER_KEYWORDS[k]);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  578 */       localStringBuilder.append('}');
/*      */     }
/*  580 */     copyAndFixQuotes(this.pattern, i, this.pattern.length(), localStringBuilder);
/*  581 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public void setFormatsByArgumentIndex(Format[] paramArrayOfFormat)
/*      */   {
/*  608 */     for (int i = 0; i <= this.maxOffset; i++) {
/*  609 */       int j = this.argumentNumbers[i];
/*  610 */       if (j < paramArrayOfFormat.length)
/*  611 */         this.formats[i] = paramArrayOfFormat[j];
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFormats(Format[] paramArrayOfFormat)
/*      */   {
/*  639 */     int i = paramArrayOfFormat.length;
/*  640 */     if (i > this.maxOffset + 1) {
/*  641 */       i = this.maxOffset + 1;
/*      */     }
/*  643 */     for (int j = 0; j < i; j++)
/*  644 */       this.formats[j] = paramArrayOfFormat[j];
/*      */   }
/*      */ 
/*      */   public void setFormatByArgumentIndex(int paramInt, Format paramFormat)
/*      */   {
/*  667 */     for (int i = 0; i <= this.maxOffset; i++)
/*  668 */       if (this.argumentNumbers[i] == paramInt)
/*  669 */         this.formats[i] = paramFormat;
/*      */   }
/*      */ 
/*      */   public void setFormat(int paramInt, Format paramFormat)
/*      */   {
/*  692 */     this.formats[paramInt] = paramFormat;
/*      */   }
/*      */ 
/*      */   public Format[] getFormatsByArgumentIndex()
/*      */   {
/*  716 */     int i = -1;
/*  717 */     for (int j = 0; j <= this.maxOffset; j++) {
/*  718 */       if (this.argumentNumbers[j] > i) {
/*  719 */         i = this.argumentNumbers[j];
/*      */       }
/*      */     }
/*  722 */     Format[] arrayOfFormat = new Format[i + 1];
/*  723 */     for (int k = 0; k <= this.maxOffset; k++) {
/*  724 */       arrayOfFormat[this.argumentNumbers[k]] = this.formats[k];
/*      */     }
/*  726 */     return arrayOfFormat;
/*      */   }
/*      */ 
/*      */   public Format[] getFormats()
/*      */   {
/*  746 */     Format[] arrayOfFormat = new Format[this.maxOffset + 1];
/*  747 */     System.arraycopy(this.formats, 0, arrayOfFormat, 0, this.maxOffset + 1);
/*  748 */     return arrayOfFormat;
/*      */   }
/*      */ 
/*      */   public final StringBuffer format(Object[] paramArrayOfObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  819 */     return subformat(paramArrayOfObject, paramStringBuffer, paramFieldPosition, null);
/*      */   }
/*      */ 
/*      */   public static String format(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  835 */     MessageFormat localMessageFormat = new MessageFormat(paramString);
/*  836 */     return localMessageFormat.format(paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public final StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*      */   {
/*  860 */     return subformat((Object[])paramObject, paramStringBuffer, paramFieldPosition, null);
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object paramObject)
/*      */   {
/*  899 */     StringBuffer localStringBuffer = new StringBuffer();
/*  900 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  902 */     if (paramObject == null) {
/*  903 */       throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
/*      */     }
/*      */ 
/*  906 */     subformat((Object[])paramObject, localStringBuffer, null, localArrayList);
/*  907 */     if (localArrayList.size() == 0) {
/*  908 */       return createAttributedCharacterIterator("");
/*      */     }
/*  910 */     return createAttributedCharacterIterator((AttributedCharacterIterator[])localArrayList.toArray(new AttributedCharacterIterator[localArrayList.size()]));
/*      */   }
/*      */ 
/*      */   public Object[] parse(String paramString, ParsePosition paramParsePosition)
/*      */   {
/*  943 */     if (paramString == null) {
/*  944 */       Object[] arrayOfObject1 = new Object[0];
/*  945 */       return arrayOfObject1;
/*      */     }
/*      */ 
/*  948 */     int i = -1;
/*  949 */     for (int j = 0; j <= this.maxOffset; j++) {
/*  950 */       if (this.argumentNumbers[j] > i) {
/*  951 */         i = this.argumentNumbers[j];
/*      */       }
/*      */     }
/*  954 */     Object[] arrayOfObject2 = new Object[i + 1];
/*      */ 
/*  956 */     int k = 0;
/*  957 */     int m = paramParsePosition.index;
/*  958 */     ParsePosition localParsePosition = new ParsePosition(0);
/*  959 */     for (int n = 0; n <= this.maxOffset; n++)
/*      */     {
/*  961 */       int i1 = this.offsets[n] - k;
/*  962 */       if ((i1 == 0) || (this.pattern.regionMatches(k, paramString, m, i1)))
/*      */       {
/*  964 */         m += i1;
/*  965 */         k += i1;
/*      */       } else {
/*  967 */         paramParsePosition.errorIndex = m;
/*  968 */         return null;
/*      */       }
/*      */ 
/*  972 */       if (this.formats[n] == null)
/*      */       {
/*  976 */         int i2 = n != this.maxOffset ? this.offsets[(n + 1)] : this.pattern.length();
/*      */         int i3;
/*  979 */         if (k >= i2)
/*  980 */           i3 = paramString.length();
/*      */         else {
/*  982 */           i3 = paramString.indexOf(this.pattern.substring(k, i2), m);
/*      */         }
/*      */ 
/*  986 */         if (i3 < 0) {
/*  987 */           paramParsePosition.errorIndex = m;
/*  988 */           return null;
/*      */         }
/*  990 */         String str = paramString.substring(m, i3);
/*  991 */         if (!str.equals("{" + this.argumentNumbers[n] + "}")) {
/*  992 */           arrayOfObject2[this.argumentNumbers[n]] = paramString.substring(m, i3);
/*      */         }
/*  994 */         m = i3;
/*      */       }
/*      */       else {
/*  997 */         localParsePosition.index = m;
/*  998 */         arrayOfObject2[this.argumentNumbers[n]] = this.formats[n].parseObject(paramString, localParsePosition);
/*      */ 
/* 1000 */         if (localParsePosition.index == m) {
/* 1001 */           paramParsePosition.errorIndex = m;
/* 1002 */           return null;
/*      */         }
/* 1004 */         m = localParsePosition.index;
/*      */       }
/*      */     }
/* 1007 */     n = this.pattern.length() - k;
/* 1008 */     if ((n == 0) || (this.pattern.regionMatches(k, paramString, m, n)))
/*      */     {
/* 1010 */       paramParsePosition.index = (m + n);
/*      */     } else {
/* 1012 */       paramParsePosition.errorIndex = m;
/* 1013 */       return null;
/*      */     }
/* 1015 */     return arrayOfObject2;
/*      */   }
/*      */ 
/*      */   public Object[] parse(String paramString)
/*      */     throws ParseException
/*      */   {
/* 1032 */     ParsePosition localParsePosition = new ParsePosition(0);
/* 1033 */     Object[] arrayOfObject = parse(paramString, localParsePosition);
/* 1034 */     if (localParsePosition.index == 0) {
/* 1035 */       throw new ParseException("MessageFormat parse error!", localParsePosition.errorIndex);
/*      */     }
/* 1037 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public Object parseObject(String paramString, ParsePosition paramParsePosition)
/*      */   {
/* 1065 */     return parse(paramString, paramParsePosition);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1074 */     MessageFormat localMessageFormat = (MessageFormat)super.clone();
/*      */ 
/* 1077 */     localMessageFormat.formats = ((Format[])this.formats.clone());
/* 1078 */     for (int i = 0; i < this.formats.length; i++) {
/* 1079 */       if (this.formats[i] != null) {
/* 1080 */         localMessageFormat.formats[i] = ((Format)this.formats[i].clone());
/*      */       }
/*      */     }
/* 1083 */     localMessageFormat.offsets = ((int[])this.offsets.clone());
/* 1084 */     localMessageFormat.argumentNumbers = ((int[])this.argumentNumbers.clone());
/*      */ 
/* 1086 */     return localMessageFormat;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1093 */     if (this == paramObject)
/* 1094 */       return true;
/* 1095 */     if ((paramObject == null) || (getClass() != paramObject.getClass()))
/* 1096 */       return false;
/* 1097 */     MessageFormat localMessageFormat = (MessageFormat)paramObject;
/* 1098 */     return (this.maxOffset == localMessageFormat.maxOffset) && (this.pattern.equals(localMessageFormat.pattern)) && (((this.locale != null) && (this.locale.equals(localMessageFormat.locale))) || ((this.locale == null) && (localMessageFormat.locale == null) && (Arrays.equals(this.offsets, localMessageFormat.offsets)) && (Arrays.equals(this.argumentNumbers, localMessageFormat.argumentNumbers)) && (Arrays.equals(this.formats, localMessageFormat.formats))));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1111 */     return this.pattern.hashCode();
/*      */   }
/*      */ 
/*      */   private StringBuffer subformat(Object[] paramArrayOfObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition, List paramList)
/*      */   {
/* 1230 */     int i = 0;
/* 1231 */     int j = paramStringBuffer.length();
/* 1232 */     for (int k = 0; k <= this.maxOffset; k++) {
/* 1233 */       paramStringBuffer.append(this.pattern.substring(i, this.offsets[k]));
/* 1234 */       i = this.offsets[k];
/* 1235 */       int m = this.argumentNumbers[k];
/* 1236 */       if ((paramArrayOfObject == null) || (m >= paramArrayOfObject.length)) {
/* 1237 */         paramStringBuffer.append('{').append(m).append('}');
/*      */       }
/*      */       else
/*      */       {
/* 1245 */         Object localObject1 = paramArrayOfObject[m];
/* 1246 */         String str = null;
/* 1247 */         Object localObject2 = null;
/* 1248 */         if (localObject1 == null) {
/* 1249 */           str = "null";
/* 1250 */         } else if (this.formats[k] != null) {
/* 1251 */           localObject2 = this.formats[k];
/* 1252 */           if ((localObject2 instanceof ChoiceFormat)) {
/* 1253 */             str = this.formats[k].format(localObject1);
/* 1254 */             if (str.indexOf('{') >= 0) {
/* 1255 */               localObject2 = new MessageFormat(str, this.locale);
/* 1256 */               localObject1 = paramArrayOfObject;
/* 1257 */               str = null;
/*      */             }
/*      */           }
/* 1260 */         } else if ((localObject1 instanceof Number))
/*      */         {
/* 1262 */           localObject2 = NumberFormat.getInstance(this.locale);
/* 1263 */         } else if ((localObject1 instanceof Date))
/*      */         {
/* 1265 */           localObject2 = DateFormat.getDateTimeInstance(3, 3, this.locale);
/*      */         }
/* 1267 */         else if ((localObject1 instanceof String)) {
/* 1268 */           str = (String)localObject1;
/*      */         }
/*      */         else {
/* 1271 */           str = localObject1.toString();
/* 1272 */           if (str == null) str = "null";
/*      */ 
/*      */         }
/*      */ 
/* 1279 */         if (paramList != null)
/*      */         {
/* 1282 */           if (j != paramStringBuffer.length()) {
/* 1283 */             paramList.add(createAttributedCharacterIterator(paramStringBuffer.substring(j)));
/*      */ 
/* 1286 */             j = paramStringBuffer.length();
/*      */           }
/* 1288 */           if (localObject2 != null) {
/* 1289 */             AttributedCharacterIterator localAttributedCharacterIterator = ((Format)localObject2).formatToCharacterIterator(localObject1);
/*      */ 
/* 1292 */             append(paramStringBuffer, localAttributedCharacterIterator);
/* 1293 */             if (j != paramStringBuffer.length()) {
/* 1294 */               paramList.add(createAttributedCharacterIterator(localAttributedCharacterIterator, Field.ARGUMENT, Integer.valueOf(m)));
/*      */ 
/* 1298 */               j = paramStringBuffer.length();
/*      */             }
/* 1300 */             str = null;
/*      */           }
/* 1302 */           if ((str != null) && (str.length() > 0)) {
/* 1303 */             paramStringBuffer.append(str);
/* 1304 */             paramList.add(createAttributedCharacterIterator(str, Field.ARGUMENT, Integer.valueOf(m)));
/*      */ 
/* 1308 */             j = paramStringBuffer.length();
/*      */           }
/*      */         }
/*      */         else {
/* 1312 */           if (localObject2 != null) {
/* 1313 */             str = ((Format)localObject2).format(localObject1);
/*      */           }
/* 1315 */           j = paramStringBuffer.length();
/* 1316 */           paramStringBuffer.append(str);
/* 1317 */           if ((k == 0) && (paramFieldPosition != null) && (Field.ARGUMENT.equals(paramFieldPosition.getFieldAttribute())))
/*      */           {
/* 1319 */             paramFieldPosition.setBeginIndex(j);
/* 1320 */             paramFieldPosition.setEndIndex(paramStringBuffer.length());
/*      */           }
/* 1322 */           j = paramStringBuffer.length();
/*      */         }
/*      */       }
/*      */     }
/* 1326 */     paramStringBuffer.append(this.pattern.substring(i, this.pattern.length()));
/* 1327 */     if ((paramList != null) && (j != paramStringBuffer.length())) {
/* 1328 */       paramList.add(createAttributedCharacterIterator(paramStringBuffer.substring(j)));
/*      */     }
/*      */ 
/* 1331 */     return paramStringBuffer;
/*      */   }
/*      */ 
/*      */   private void append(StringBuffer paramStringBuffer, CharacterIterator paramCharacterIterator)
/*      */   {
/* 1339 */     if (paramCharacterIterator.first() != 65535)
/*      */     {
/* 1342 */       paramStringBuffer.append(paramCharacterIterator.first());
/*      */       char c;
/* 1343 */       while ((c = paramCharacterIterator.next()) != 65535)
/* 1344 */         paramStringBuffer.append(c);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void makeFormat(int paramInt1, int paramInt2, StringBuilder[] paramArrayOfStringBuilder)
/*      */   {
/* 1409 */     String[] arrayOfString = new String[paramArrayOfStringBuilder.length];
/* 1410 */     for (int i = 0; i < paramArrayOfStringBuilder.length; i++) {
/* 1411 */       StringBuilder localStringBuilder = paramArrayOfStringBuilder[i];
/* 1412 */       arrayOfString[i] = (localStringBuilder != null ? localStringBuilder.toString() : "");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1418 */       i = Integer.parseInt(arrayOfString[1]);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 1420 */       throw new IllegalArgumentException("can't parse argument number: " + arrayOfString[1], localNumberFormatException);
/*      */     }
/*      */ 
/* 1423 */     if (i < 0) {
/* 1424 */       throw new IllegalArgumentException("negative argument number: " + i);
/*      */     }
/*      */ 
/* 1429 */     if (paramInt2 >= this.formats.length) {
/* 1430 */       j = this.formats.length * 2;
/* 1431 */       localObject = new Format[j];
/* 1432 */       int[] arrayOfInt1 = new int[j];
/* 1433 */       int[] arrayOfInt2 = new int[j];
/* 1434 */       System.arraycopy(this.formats, 0, localObject, 0, this.maxOffset + 1);
/* 1435 */       System.arraycopy(this.offsets, 0, arrayOfInt1, 0, this.maxOffset + 1);
/* 1436 */       System.arraycopy(this.argumentNumbers, 0, arrayOfInt2, 0, this.maxOffset + 1);
/* 1437 */       this.formats = ((Format[])localObject);
/* 1438 */       this.offsets = arrayOfInt1;
/* 1439 */       this.argumentNumbers = arrayOfInt2;
/*      */     }
/* 1441 */     int j = this.maxOffset;
/* 1442 */     this.maxOffset = paramInt2;
/* 1443 */     this.offsets[paramInt2] = arrayOfString[0].length();
/* 1444 */     this.argumentNumbers[paramInt2] = i;
/*      */ 
/* 1447 */     Object localObject = null;
/* 1448 */     if (arrayOfString[2].length() != 0) {
/* 1449 */       int k = findKeyword(arrayOfString[2], TYPE_KEYWORDS);
/* 1450 */       switch (k)
/*      */       {
/*      */       case 0:
/* 1454 */         break;
/*      */       case 1:
/* 1457 */         switch (findKeyword(arrayOfString[3], NUMBER_MODIFIER_KEYWORDS)) {
/*      */         case 0:
/* 1459 */           localObject = NumberFormat.getInstance(this.locale);
/* 1460 */           break;
/*      */         case 1:
/* 1462 */           localObject = NumberFormat.getCurrencyInstance(this.locale);
/* 1463 */           break;
/*      */         case 2:
/* 1465 */           localObject = NumberFormat.getPercentInstance(this.locale);
/* 1466 */           break;
/*      */         case 3:
/* 1468 */           localObject = NumberFormat.getIntegerInstance(this.locale);
/* 1469 */           break;
/*      */         default:
/*      */           try {
/* 1472 */             localObject = new DecimalFormat(arrayOfString[3], DecimalFormatSymbols.getInstance(this.locale));
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException1) {
/* 1475 */             this.maxOffset = j;
/* 1476 */             throw localIllegalArgumentException1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 2:
/*      */       case 3:
/* 1484 */         int m = findKeyword(arrayOfString[3], DATE_TIME_MODIFIER_KEYWORDS);
/* 1485 */         if ((m >= 0) && (m < DATE_TIME_MODIFIER_KEYWORDS.length)) {
/* 1486 */           if (k == 2) {
/* 1487 */             localObject = DateFormat.getDateInstance(DATE_TIME_MODIFIERS[m], this.locale);
/*      */           }
/*      */           else {
/* 1490 */             localObject = DateFormat.getTimeInstance(DATE_TIME_MODIFIERS[m], this.locale);
/*      */           }
/*      */         }
/*      */         else {
/*      */           try
/*      */           {
/* 1496 */             localObject = new SimpleDateFormat(arrayOfString[3], this.locale);
/*      */           } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 1498 */             this.maxOffset = j;
/* 1499 */             throw localIllegalArgumentException2;
/*      */           }
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 4:
/*      */         try
/*      */         {
/* 1507 */           localObject = new ChoiceFormat(arrayOfString[3]);
/*      */         } catch (Exception localException) {
/* 1509 */           this.maxOffset = j;
/* 1510 */           throw new IllegalArgumentException("Choice Pattern incorrect: " + arrayOfString[3], localException);
/*      */         }
/*      */ 
/*      */       default:
/* 1516 */         this.maxOffset = j;
/* 1517 */         throw new IllegalArgumentException("unknown format type: " + arrayOfString[2]);
/*      */       }
/*      */     }
/*      */ 
/* 1521 */     this.formats[paramInt2] = localObject;
/*      */   }
/*      */ 
/*      */   private static final int findKeyword(String paramString, String[] paramArrayOfString) {
/* 1525 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1526 */       if (paramString.equals(paramArrayOfString[i])) {
/* 1527 */         return i;
/*      */       }
/*      */     }
/*      */ 
/* 1531 */     String str = paramString.trim().toLowerCase(Locale.ROOT);
/* 1532 */     if (str != paramString) {
/* 1533 */       for (int j = 0; j < paramArrayOfString.length; j++) {
/* 1534 */         if (str.equals(paramArrayOfString[j]))
/* 1535 */           return j;
/*      */       }
/*      */     }
/* 1538 */     return -1;
/*      */   }
/*      */ 
/*      */   private static final void copyAndFixQuotes(String paramString, int paramInt1, int paramInt2, StringBuilder paramStringBuilder)
/*      */   {
/* 1543 */     int i = 0;
/*      */ 
/* 1545 */     for (int j = paramInt1; j < paramInt2; j++) {
/* 1546 */       char c = paramString.charAt(j);
/* 1547 */       if (c == '{') {
/* 1548 */         if (i == 0) {
/* 1549 */           paramStringBuilder.append('\'');
/* 1550 */           i = 1;
/*      */         }
/* 1552 */         paramStringBuilder.append(c);
/* 1553 */       } else if (c == '\'') {
/* 1554 */         paramStringBuilder.append("''");
/*      */       } else {
/* 1556 */         if (i != 0) {
/* 1557 */           paramStringBuilder.append('\'');
/* 1558 */           i = 0;
/*      */         }
/* 1560 */         paramStringBuilder.append(c);
/*      */       }
/*      */     }
/* 1563 */     if (i != 0)
/* 1564 */       paramStringBuilder.append('\'');
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1574 */     paramObjectInputStream.defaultReadObject();
/* 1575 */     int i = (this.maxOffset >= -1) && (this.formats.length > this.maxOffset) && (this.offsets.length > this.maxOffset) && (this.argumentNumbers.length > this.maxOffset) ? 1 : 0;
/*      */ 
/* 1579 */     if (i != 0) {
/* 1580 */       int j = this.pattern.length() + 1;
/* 1581 */       for (int k = this.maxOffset; k >= 0; k--) {
/* 1582 */         if ((this.offsets[k] < 0) || (this.offsets[k] > j)) {
/* 1583 */           i = 0;
/* 1584 */           break;
/*      */         }
/* 1586 */         j = this.offsets[k];
/*      */       }
/*      */     }
/*      */ 
/* 1590 */     if (i == 0)
/* 1591 */       throw new InvalidObjectException("Could not reconstruct MessageFormat from corrupt stream.");
/*      */   }
/*      */ 
/*      */   public static class Field extends Format.Field
/*      */   {
/*      */     private static final long serialVersionUID = 7899943957617360810L;
/* 1162 */     public static final Field ARGUMENT = new Field("message argument field");
/*      */ 
/*      */     protected Field(String paramString)
/*      */     {
/* 1133 */       super();
/*      */     }
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/* 1144 */       if (getClass() != Field.class) {
/* 1145 */         throw new InvalidObjectException("subclass didn't correctly implement readResolve");
/*      */       }
/*      */ 
/* 1148 */       return ARGUMENT;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.MessageFormat
 * JD-Core Version:    0.6.2
 */