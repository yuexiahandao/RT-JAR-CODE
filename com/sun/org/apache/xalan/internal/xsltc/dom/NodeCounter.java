/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class NodeCounter
/*     */ {
/*     */   public static final int END = -1;
/*  41 */   protected int _node = -1;
/*  42 */   protected int _nodeType = -1;
/*  43 */   protected double _value = -2147483648.0D;
/*     */   public final DOM _document;
/*     */   public final DTMAxisIterator _iterator;
/*     */   public final Translet _translet;
/*     */   protected String _format;
/*     */   protected String _lang;
/*     */   protected String _letterValue;
/*     */   protected String _groupSep;
/*     */   protected int _groupSize;
/*  55 */   private boolean _separFirst = true;
/*  56 */   private boolean _separLast = false;
/*  57 */   private Vector _separToks = new Vector();
/*  58 */   private Vector _formatToks = new Vector();
/*  59 */   private int _nSepars = 0;
/*  60 */   private int _nFormats = 0;
/*     */ 
/*  62 */   private static final String[] Thousands = { "", "m", "mm", "mmm" };
/*     */ 
/*  64 */   private static final String[] Hundreds = { "", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm" };
/*     */ 
/*  66 */   private static final String[] Tens = { "", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc" };
/*     */ 
/*  68 */   private static final String[] Ones = { "", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix" };
/*     */ 
/*  71 */   private StringBuilder _tempBuffer = new StringBuilder();
/*     */   protected boolean _hasFrom;
/*     */ 
/*     */   protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/*  80 */     this._translet = translet;
/*  81 */     this._document = document;
/*  82 */     this._iterator = iterator;
/*     */   }
/*     */ 
/*     */   protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom)
/*     */   {
/*  87 */     this._translet = translet;
/*  88 */     this._document = document;
/*  89 */     this._iterator = iterator;
/*  90 */     this._hasFrom = hasFrom;
/*     */   }
/*     */ 
/*     */   public abstract NodeCounter setStartNode(int paramInt);
/*     */ 
/*     */   public NodeCounter setValue(double value)
/*     */   {
/* 104 */     this._value = value;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   protected void setFormatting(String format, String lang, String letterValue, String groupSep, String groupSize)
/*     */   {
/* 113 */     this._lang = lang;
/* 114 */     this._groupSep = groupSep;
/* 115 */     this._letterValue = letterValue;
/* 116 */     this._groupSize = parseStringToAnInt(groupSize);
/* 117 */     setTokens(format);
/*     */   }
/*     */ 
/*     */   private int parseStringToAnInt(String s)
/*     */   {
/* 132 */     if (s == null) {
/* 133 */       return 0;
/*     */     }
/* 135 */     int result = 0;
/* 136 */     boolean negative = false;
/* 137 */     int radix = 10; int i = 0; int max = s.length();
/*     */ 
/* 140 */     if (max > 0)
/*     */     {
/*     */       int limit;
/* 141 */       if (s.charAt(0) == '-') {
/* 142 */         negative = true;
/* 143 */         int limit = -2147483648;
/* 144 */         i++;
/*     */       } else {
/* 146 */         limit = -2147483647;
/*     */       }
/* 148 */       int multmin = limit / radix;
/* 149 */       if (i < max) {
/* 150 */         int digit = Character.digit(s.charAt(i++), radix);
/* 151 */         if (digit < 0) {
/* 152 */           return 0;
/*     */         }
/* 154 */         result = -digit;
/*     */       }
/* 156 */       while (i < max)
/*     */       {
/* 158 */         int digit = Character.digit(s.charAt(i++), radix);
/* 159 */         if (digit < 0)
/* 160 */           return 0;
/* 161 */         if (result < multmin)
/* 162 */           return 0;
/* 163 */         result *= radix;
/* 164 */         if (result < limit + digit)
/* 165 */           return 0;
/* 166 */         result -= digit;
/*     */       }
/*     */     }
/* 169 */     return 0;
/*     */     int multmin;
/*     */     int limit;
/* 171 */     if (negative) {
/* 172 */       if (i > 1) {
/* 173 */         return result;
/*     */       }
/* 175 */       return 0;
/*     */     }
/* 177 */     return -result;
/*     */   }
/*     */ 
/*     */   private final void setTokens(String format)
/*     */   {
/* 183 */     if ((this._format != null) && (format.equals(this._format))) {
/* 184 */       return;
/*     */     }
/* 186 */     this._format = format;
/*     */ 
/* 188 */     int length = this._format.length();
/* 189 */     boolean isFirst = true;
/* 190 */     this._separFirst = true;
/* 191 */     this._separLast = false;
/* 192 */     this._nSepars = 0;
/* 193 */     this._nFormats = 0;
/* 194 */     this._separToks.clear();
/* 195 */     this._formatToks.clear();
/*     */ 
/* 201 */     int j = 0; for (int i = 0; i < length; ) {
/* 202 */       char c = format.charAt(i);
/* 203 */       for (j = i; Character.isLetterOrDigit(c); ) {
/* 204 */         i++; if (i == length) break;
/* 205 */         c = format.charAt(i);
/*     */       }
/* 207 */       if (i > j) {
/* 208 */         if (isFirst) {
/* 209 */           this._separToks.addElement(".");
/* 210 */           isFirst = this._separFirst = 0;
/*     */         }
/* 212 */         this._formatToks.addElement(format.substring(j, i));
/*     */       }
/*     */ 
/* 215 */       if (i == length)
/*     */         break;
/* 217 */       c = format.charAt(i);
/* 218 */       for (j = i; !Character.isLetterOrDigit(c); ) {
/* 219 */         i++; if (i == length) break;
/* 220 */         c = format.charAt(i);
/* 221 */         isFirst = false;
/*     */       }
/* 223 */       if (i > j) {
/* 224 */         this._separToks.addElement(format.substring(j, i));
/*     */       }
/*     */     }
/*     */ 
/* 228 */     this._nSepars = this._separToks.size();
/* 229 */     this._nFormats = this._formatToks.size();
/* 230 */     if (this._nSepars > this._nFormats) this._separLast = true;
/*     */ 
/* 232 */     if (this._separFirst) this._nSepars -= 1;
/* 233 */     if (this._separLast) this._nSepars -= 1;
/* 234 */     if (this._nSepars == 0) {
/* 235 */       this._separToks.insertElementAt(".", 1);
/* 236 */       this._nSepars += 1;
/*     */     }
/* 238 */     if (this._separFirst) this._nSepars += 1;
/*     */   }
/*     */ 
/*     */   public NodeCounter setDefaultFormatting()
/*     */   {
/* 245 */     setFormatting("1", "en", "alphabetic", null, null);
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */   public abstract String getCounter();
/*     */ 
/*     */   public String getCounter(String format, String lang, String letterValue, String groupSep, String groupSize)
/*     */   {
/* 262 */     setFormatting(format, lang, letterValue, groupSep, groupSize);
/* 263 */     return getCounter();
/*     */   }
/*     */ 
/*     */   public boolean matchesCount(int node)
/*     */   {
/* 272 */     return this._nodeType == this._document.getExpandedTypeID(node);
/*     */   }
/*     */ 
/*     */   public boolean matchesFrom(int node)
/*     */   {
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   protected String formatNumbers(int value)
/*     */   {
/* 287 */     return formatNumbers(new int[] { value });
/*     */   }
/*     */ 
/*     */   protected String formatNumbers(int[] values)
/*     */   {
/* 295 */     int nValues = values.length;
/*     */ 
/* 297 */     boolean isEmpty = true;
/* 298 */     for (int i = 0; i < nValues; i++)
/* 299 */       if (values[i] != -2147483648)
/* 300 */         isEmpty = false;
/* 301 */     if (isEmpty) return "";
/*     */ 
/* 304 */     boolean isFirst = true;
/* 305 */     int t = 0; int n = 0; int s = 1;
/* 306 */     this._tempBuffer.setLength(0);
/* 307 */     StringBuilder buffer = this._tempBuffer;
/*     */ 
/* 310 */     if (this._separFirst) buffer.append((String)this._separToks.elementAt(0));
/*     */ 
/* 313 */     while (n < nValues) {
/* 314 */       int value = values[n];
/* 315 */       if (value != -2147483648) {
/* 316 */         if (!isFirst) buffer.append((String)this._separToks.elementAt(s++));
/* 317 */         formatValue(value, (String)this._formatToks.elementAt(t++), buffer);
/* 318 */         if (t == this._nFormats) t--;
/* 319 */         if (s >= this._nSepars) s--;
/* 320 */         isFirst = false;
/*     */       }
/* 322 */       n++;
/*     */     }
/*     */ 
/* 326 */     if (this._separLast) buffer.append((String)this._separToks.lastElement());
/* 327 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   private void formatValue(int value, String format, StringBuilder buffer)
/*     */   {
/* 336 */     char c = format.charAt(0);
/*     */ 
/* 338 */     if (Character.isDigit(c)) {
/* 339 */       char zero = (char)(c - Character.getNumericValue(c));
/*     */ 
/* 341 */       StringBuilder temp = buffer;
/* 342 */       if (this._groupSize > 0) {
/* 343 */         temp = new StringBuilder();
/*     */       }
/* 345 */       String s = "";
/* 346 */       int n = value;
/* 347 */       while (n > 0) {
/* 348 */         s = (char)(zero + n % 10) + s;
/* 349 */         n /= 10;
/*     */       }
/*     */ 
/* 352 */       for (int i = 0; i < format.length() - s.length(); i++) {
/* 353 */         temp.append(zero);
/*     */       }
/* 355 */       temp.append(s);
/*     */ 
/* 357 */       if (this._groupSize > 0) {
/* 358 */         for (int i = 0; i < temp.length(); i++) {
/* 359 */           if ((i != 0) && ((temp.length() - i) % this._groupSize == 0)) {
/* 360 */             buffer.append(this._groupSep);
/*     */           }
/* 362 */           buffer.append(temp.charAt(i));
/*     */         }
/*     */       }
/*     */     }
/* 366 */     else if ((c == 'i') && (!this._letterValue.equals("alphabetic"))) {
/* 367 */       buffer.append(romanValue(value));
/*     */     }
/* 369 */     else if ((c == 'I') && (!this._letterValue.equals("alphabetic"))) {
/* 370 */       buffer.append(romanValue(value).toUpperCase());
/*     */     }
/*     */     else {
/* 373 */       int min = c;
/* 374 */       int max = c;
/*     */ 
/* 377 */       if ((c >= 'α') && (c <= 'ω')) {
/* 378 */         max = 969;
/*     */       }
/*     */       else
/*     */       {
/* 382 */         while (Character.isLetterOrDigit((char)(max + 1))) {
/* 383 */           max++;
/*     */         }
/*     */       }
/* 386 */       buffer.append(alphaValue(value, min, max));
/*     */     }
/*     */   }
/*     */ 
/*     */   private String alphaValue(int value, int min, int max) {
/* 391 */     if (value <= 0) {
/* 392 */       return "" + value;
/*     */     }
/*     */ 
/* 395 */     int range = max - min + 1;
/* 396 */     char last = (char)((value - 1) % range + min);
/* 397 */     if (value > range) {
/* 398 */       return alphaValue((value - 1) / range, min, max) + last;
/*     */     }
/*     */ 
/* 401 */     return "" + last;
/*     */   }
/*     */ 
/*     */   private String romanValue(int n)
/*     */   {
/* 406 */     if ((n <= 0) || (n > 4000)) {
/* 407 */       return "" + n;
/*     */     }
/* 409 */     return Thousands[(n / 1000)] + Hundreds[(n / 100 % 10)] + Tens[(n / 10 % 10)] + Ones[(n % 10)];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
 * JD-Core Version:    0.6.2
 */