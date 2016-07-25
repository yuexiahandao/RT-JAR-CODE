/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.InvalidSearchFilterException;
/*     */ 
/*     */ public class SearchFilter
/*     */   implements AttrFilter
/*     */ {
/*     */   String filter;
/*     */   int pos;
/*     */   private StringFilter rootFilter;
/*     */   protected static final boolean debug = false;
/*     */   protected static final char BEGIN_FILTER_TOKEN = '(';
/*     */   protected static final char END_FILTER_TOKEN = ')';
/*     */   protected static final char AND_TOKEN = '&';
/*     */   protected static final char OR_TOKEN = '|';
/*     */   protected static final char NOT_TOKEN = '!';
/*     */   protected static final char EQUAL_TOKEN = '=';
/*     */   protected static final char APPROX_TOKEN = '~';
/*     */   protected static final char LESS_TOKEN = '<';
/*     */   protected static final char GREATER_TOKEN = '>';
/*     */   protected static final char EXTEND_TOKEN = ':';
/*     */   protected static final char WILDCARD_TOKEN = '*';
/*     */   static final int EQUAL_MATCH = 1;
/*     */   static final int APPROX_MATCH = 2;
/*     */   static final int GREATER_MATCH = 3;
/*     */   static final int LESS_MATCH = 4;
/*     */ 
/*     */   public SearchFilter(String paramString)
/*     */     throws InvalidSearchFilterException
/*     */   {
/*  65 */     this.filter = paramString;
/*  66 */     this.pos = 0;
/*  67 */     normalizeFilter();
/*  68 */     this.rootFilter = createNextFilter();
/*     */   }
/*     */ 
/*     */   public boolean check(Attributes paramAttributes) throws NamingException
/*     */   {
/*  73 */     if (paramAttributes == null) {
/*  74 */       return false;
/*     */     }
/*  76 */     return this.rootFilter.check(paramAttributes);
/*     */   }
/*     */ 
/*     */   protected void normalizeFilter()
/*     */   {
/*  86 */     skipWhiteSpace();
/*     */ 
/*  89 */     if (getCurrentChar() != '(')
/*  90 */       this.filter = ('(' + this.filter + ')');
/*     */   }
/*     */ 
/*     */   private void skipWhiteSpace()
/*     */   {
/*  99 */     while (Character.isWhitespace(getCurrentChar()))
/* 100 */       consumeChar();
/*     */   }
/*     */ 
/*     */   protected StringFilter createNextFilter()
/*     */     throws InvalidSearchFilterException
/*     */   {
/* 108 */     skipWhiteSpace();
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 112 */       if (getCurrentChar() != '(') {
/* 113 */         throw new InvalidSearchFilterException("expected \"(\" at position " + this.pos);
/*     */       }
/*     */ 
/* 120 */       consumeChar();
/*     */ 
/* 122 */       skipWhiteSpace();
/*     */ 
/* 125 */       switch (getCurrentChar())
/*     */       {
/*     */       case '&':
/* 128 */         localObject = new CompoundFilter(true);
/* 129 */         ((StringFilter)localObject).parse();
/* 130 */         break;
/*     */       case '|':
/* 133 */         localObject = new CompoundFilter(false);
/* 134 */         ((StringFilter)localObject).parse();
/* 135 */         break;
/*     */       case '!':
/* 138 */         localObject = new NotFilter();
/* 139 */         ((StringFilter)localObject).parse();
/* 140 */         break;
/*     */       default:
/* 143 */         localObject = new AtomicFilter();
/* 144 */         ((StringFilter)localObject).parse();
/*     */       }
/*     */ 
/* 148 */       skipWhiteSpace();
/*     */ 
/* 151 */       if (getCurrentChar() != ')') {
/* 152 */         throw new InvalidSearchFilterException("expected \")\" at position " + this.pos);
/*     */       }
/*     */ 
/* 159 */       consumeChar();
/*     */     }
/*     */     catch (InvalidSearchFilterException localInvalidSearchFilterException) {
/* 162 */       throw localInvalidSearchFilterException;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 167 */       throw new InvalidSearchFilterException("Unable to parse character " + this.pos + " in \"" + this.filter + "\"");
/*     */     }
/*     */ 
/* 172 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected char getCurrentChar() {
/* 176 */     return this.filter.charAt(this.pos);
/*     */   }
/*     */ 
/*     */   protected char relCharAt(int paramInt) {
/* 180 */     return this.filter.charAt(this.pos + paramInt);
/*     */   }
/*     */ 
/*     */   protected void consumeChar() {
/* 184 */     this.pos += 1;
/*     */   }
/*     */ 
/*     */   protected void consumeChars(int paramInt) {
/* 188 */     this.pos += paramInt;
/*     */   }
/*     */ 
/*     */   protected int relIndexOf(int paramInt) {
/* 192 */     return this.filter.indexOf(paramInt, this.pos) - this.pos;
/*     */   }
/*     */ 
/*     */   protected String relSubstring(int paramInt1, int paramInt2)
/*     */   {
/* 198 */     return this.filter.substring(paramInt1 + this.pos, paramInt2 + this.pos);
/*     */   }
/*     */ 
/*     */   public static String format(Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 437 */     if ((paramAttributes == null) || (paramAttributes.size() == 0)) {
/* 438 */       return "objectClass=*";
/*     */     }
/*     */ 
/* 442 */     String str1 = "(& ";
/*     */ 
/* 444 */     for (NamingEnumeration localNamingEnumeration1 = paramAttributes.getAll(); localNamingEnumeration1.hasMore(); ) {
/* 445 */       Attribute localAttribute = (Attribute)localNamingEnumeration1.next();
/* 446 */       if ((localAttribute.size() == 0) || ((localAttribute.size() == 1) && (localAttribute.get() == null)))
/*     */       {
/* 448 */         str1 = str1 + "(" + localAttribute.getID() + "=" + "*)";
/*     */       } else {
/* 450 */         NamingEnumeration localNamingEnumeration2 = localAttribute.getAll();
/* 451 */         while (localNamingEnumeration2.hasMore())
/*     */         {
/* 453 */           String str2 = getEncodedStringRep(localNamingEnumeration2.next());
/* 454 */           if (str2 != null) {
/* 455 */             str1 = str1 + "(" + localAttribute.getID() + "=" + str2 + ")";
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 461 */     str1 = str1 + ")";
/*     */ 
/* 463 */     return str1;
/*     */   }
/*     */ 
/*     */   private static void hexDigit(StringBuffer paramStringBuffer, byte paramByte)
/*     */   {
/* 470 */     char c = (char)(paramByte >> 4 & 0xF);
/* 471 */     if (c > '\t')
/* 472 */       c = (char)(c - '\n' + 65);
/*     */     else {
/* 474 */       c = (char)(c + '0');
/*     */     }
/* 476 */     paramStringBuffer.append(c);
/* 477 */     c = (char)(paramByte & 0xF);
/* 478 */     if (c > '\t')
/* 479 */       c = (char)(c - '\n' + 65);
/*     */     else
/* 481 */       c = (char)(c + '0');
/* 482 */     paramStringBuffer.append(c);
/*     */   }
/*     */ 
/*     */   private static String getEncodedStringRep(Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 501 */     if (paramObject == null)
/* 502 */       return null;
/*     */     int j;
/* 504 */     if ((paramObject instanceof byte[]))
/*     */     {
/* 506 */       byte[] arrayOfByte = (byte[])paramObject;
/* 507 */       localStringBuffer = new StringBuffer(arrayOfByte.length * 3);
/* 508 */       for (j = 0; j < arrayOfByte.length; j++) {
/* 509 */         localStringBuffer.append('\\');
/* 510 */         hexDigit(localStringBuffer, arrayOfByte[j]);
/*     */       }
/* 512 */       return localStringBuffer.toString();
/*     */     }
/*     */     String str;
/* 514 */     if (!(paramObject instanceof String))
/* 515 */       str = paramObject.toString();
/*     */     else {
/* 517 */       str = (String)paramObject;
/*     */     }
/* 519 */     int i = str.length();
/* 520 */     StringBuffer localStringBuffer = new StringBuffer(i);
/*     */ 
/* 522 */     for (int k = 0; k < i; k++) {
/* 523 */       switch (j = str.charAt(k)) {
/*     */       case '*':
/* 525 */         localStringBuffer.append("\\2a");
/* 526 */         break;
/*     */       case '(':
/* 528 */         localStringBuffer.append("\\28");
/* 529 */         break;
/*     */       case ')':
/* 531 */         localStringBuffer.append("\\29");
/* 532 */         break;
/*     */       case '\\':
/* 534 */         localStringBuffer.append("\\5c");
/* 535 */         break;
/*     */       case '\000':
/* 537 */         localStringBuffer.append("\\00");
/* 538 */         break;
/*     */       default:
/* 540 */         localStringBuffer.append(j);
/*     */       }
/*     */     }
/* 543 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static int findUnescaped(char paramChar, String paramString, int paramInt)
/*     */   {
/* 553 */     int i = paramString.length();
/*     */ 
/* 555 */     while (paramInt < i) {
/* 556 */       int j = paramString.indexOf(paramChar, paramInt);
/*     */ 
/* 558 */       if ((j == paramInt) || (j == -1) || (paramString.charAt(j - 1) != '\\')) {
/* 559 */         return j;
/*     */       }
/*     */ 
/* 562 */       paramInt = j + 1;
/*     */     }
/* 564 */     return -1;
/*     */   }
/*     */ 
/*     */   public static String format(String paramString, Object[] paramArrayOfObject)
/*     */     throws NamingException
/*     */   {
/* 584 */     int j = 0; int k = 0;
/* 585 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length());
/*     */ 
/* 587 */     while ((j = findUnescaped('{', paramString, k)) >= 0) {
/* 588 */       int m = j + 1;
/* 589 */       int n = paramString.indexOf('}', m);
/*     */ 
/* 591 */       if (n < 0) {
/* 592 */         throw new InvalidSearchFilterException("unbalanced {: " + paramString);
/*     */       }
/*     */       int i;
/*     */       try
/*     */       {
/* 597 */         i = Integer.parseInt(paramString.substring(m, n));
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 599 */         throw new InvalidSearchFilterException("integer expected inside {}: " + paramString);
/*     */       }
/*     */ 
/* 603 */       if (i >= paramArrayOfObject.length) {
/* 604 */         throw new InvalidSearchFilterException("number exceeds argument list: " + i);
/*     */       }
/*     */ 
/* 608 */       localStringBuffer.append(paramString.substring(k, j)).append(getEncodedStringRep(paramArrayOfObject[i]));
/* 609 */       k = n + 1;
/*     */     }
/*     */ 
/* 612 */     if (k < paramString.length()) {
/* 613 */       localStringBuffer.append(paramString.substring(k));
/*     */     }
/* 615 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static Attributes selectAttributes(Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 625 */     if (paramArrayOfString == null) {
/* 626 */       return paramAttributes;
/*     */     }
/* 628 */     BasicAttributes localBasicAttributes = new BasicAttributes();
/*     */ 
/* 630 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 631 */       Attribute localAttribute = paramAttributes.get(paramArrayOfString[i]);
/* 632 */       if (localAttribute != null) {
/* 633 */         localBasicAttributes.put(localAttribute);
/*     */       }
/*     */     }
/*     */ 
/* 637 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   final class AtomicFilter
/*     */     implements SearchFilter.StringFilter
/*     */   {
/*     */     private String attrID;
/*     */     private String value;
/*     */     private int matchType;
/*     */ 
/*     */     AtomicFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void parse()
/*     */       throws InvalidSearchFilterException
/*     */     {
/* 267 */       SearchFilter.this.skipWhiteSpace();
/*     */       try
/*     */       {
/* 271 */         int i = SearchFilter.this.relIndexOf(41);
/*     */ 
/* 274 */         int j = SearchFilter.this.relIndexOf(61);
/*     */ 
/* 276 */         int k = SearchFilter.this.relCharAt(j - 1);
/* 277 */         switch (k)
/*     */         {
/*     */         case 126:
/* 280 */           this.matchType = 2;
/* 281 */           this.attrID = SearchFilter.this.relSubstring(0, j - 1);
/* 282 */           this.value = SearchFilter.this.relSubstring(j + 1, i);
/* 283 */           break;
/*     */         case 62:
/* 287 */           this.matchType = 3;
/* 288 */           this.attrID = SearchFilter.this.relSubstring(0, j - 1);
/* 289 */           this.value = SearchFilter.this.relSubstring(j + 1, i);
/* 290 */           break;
/*     */         case 60:
/* 294 */           this.matchType = 4;
/* 295 */           this.attrID = SearchFilter.this.relSubstring(0, j - 1);
/* 296 */           this.value = SearchFilter.this.relSubstring(j + 1, i);
/* 297 */           break;
/*     */         case 58:
/* 301 */           throw new OperationNotSupportedException("Extensible match not supported");
/*     */         default:
/* 305 */           this.matchType = 1;
/* 306 */           this.attrID = SearchFilter.this.relSubstring(0, j);
/* 307 */           this.value = SearchFilter.this.relSubstring(j + 1, i);
/*     */         }
/*     */ 
/* 311 */         this.attrID = this.attrID.trim();
/* 312 */         this.value = this.value.trim();
/*     */ 
/* 315 */         SearchFilter.this.consumeChars(i);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 320 */         InvalidSearchFilterException localInvalidSearchFilterException = new InvalidSearchFilterException("Unable to parse character " + SearchFilter.this.pos + " in \"" + SearchFilter.this.filter + "\"");
/*     */ 
/* 324 */         localInvalidSearchFilterException.setRootCause(localException);
/* 325 */         throw localInvalidSearchFilterException;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean check(Attributes paramAttributes)
/*     */     {
/*     */       NamingEnumeration localNamingEnumeration;
/*     */       try
/*     */       {
/* 336 */         Attribute localAttribute = paramAttributes.get(this.attrID);
/* 337 */         if (localAttribute == null) {
/* 338 */           return false;
/*     */         }
/* 340 */         localNamingEnumeration = localAttribute.getAll();
/*     */       }
/*     */       catch (NamingException localNamingException)
/*     */       {
/* 344 */         return false;
/*     */       }
/*     */ 
/* 347 */       while (localNamingEnumeration.hasMoreElements()) {
/* 348 */         String str = localNamingEnumeration.nextElement().toString();
/*     */ 
/* 350 */         switch (this.matchType) {
/*     */         case 1:
/*     */         case 2:
/* 353 */           if (substringMatch(this.value, str))
/*     */           {
/* 355 */             return true;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 3:
/* 360 */           if (str.compareTo(this.value) >= 0) {
/* 361 */             return true;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 4:
/* 366 */           if (str.compareTo(this.value) <= 0) {
/* 367 */             return true;
/*     */           }
/*     */ 
/*     */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 375 */       return false;
/*     */     }
/*     */ 
/*     */     private boolean substringMatch(String paramString1, String paramString2)
/*     */     {
/* 381 */       if (paramString1.equals(new Character('*').toString()))
/*     */       {
/* 383 */         return true;
/*     */       }
/*     */ 
/* 387 */       if (paramString1.indexOf('*') == -1) {
/* 388 */         return paramString1.equalsIgnoreCase(paramString2);
/*     */       }
/*     */ 
/* 393 */       int i = 0;
/* 394 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, "*", false);
/*     */ 
/* 397 */       if ((paramString1.charAt(0) != '*') && (!paramString2.toString().toLowerCase().startsWith(localStringTokenizer.nextToken().toLowerCase())))
/*     */       {
/* 401 */         return false;
/*     */       }
/*     */ 
/* 405 */       while (localStringTokenizer.hasMoreTokens()) {
/* 406 */         String str = localStringTokenizer.nextToken();
/*     */ 
/* 409 */         i = paramString2.toLowerCase().indexOf(str.toLowerCase(), i);
/*     */ 
/* 411 */         if (i == -1) {
/* 412 */           return false;
/*     */         }
/* 414 */         i += str.length();
/*     */       }
/*     */ 
/* 418 */       if ((paramString1.charAt(paramString1.length() - 1) != '*') && (i != paramString2.length()))
/*     */       {
/* 421 */         return false;
/*     */       }
/*     */ 
/* 424 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final class CompoundFilter
/*     */     implements SearchFilter.StringFilter
/*     */   {
/* 210 */     private Vector subFilters = new Vector();
/*     */     private boolean polarity;
/*     */ 
/*     */     CompoundFilter(boolean arg2)
/*     */     {
/*     */       boolean bool;
/* 211 */       this.polarity = bool;
/*     */     }
/*     */ 
/*     */     public void parse() throws InvalidSearchFilterException {
/* 215 */       SearchFilter.this.consumeChar();
/* 216 */       while (SearchFilter.this.getCurrentChar() != ')')
/*     */       {
/* 218 */         SearchFilter.StringFilter localStringFilter = SearchFilter.this.createNextFilter();
/* 219 */         this.subFilters.addElement(localStringFilter);
/* 220 */         SearchFilter.this.skipWhiteSpace();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean check(Attributes paramAttributes) throws NamingException {
/* 225 */       for (int i = 0; i < this.subFilters.size(); i++) {
/* 226 */         SearchFilter.StringFilter localStringFilter = (SearchFilter.StringFilter)this.subFilters.elementAt(i);
/* 227 */         if (localStringFilter.check(paramAttributes) != this.polarity) {
/* 228 */           return !this.polarity;
/*     */         }
/*     */       }
/* 231 */       return this.polarity;
/*     */     }
/*     */   }
/*     */ 
/*     */   final class NotFilter implements SearchFilter.StringFilter {
/*     */     private SearchFilter.StringFilter filter;
/*     */ 
/*     */     NotFilter() {
/*     */     }
/*     */ 
/*     */     public void parse() throws InvalidSearchFilterException {
/* 242 */       SearchFilter.this.consumeChar();
/* 243 */       this.filter = SearchFilter.this.createNextFilter();
/*     */     }
/*     */ 
/*     */     public boolean check(Attributes paramAttributes) throws NamingException {
/* 247 */       return !this.filter.check(paramAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface StringFilter extends AttrFilter
/*     */   {
/*     */     public abstract void parse()
/*     */       throws InvalidSearchFilterException;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.SearchFilter
 * JD-Core Version:    0.6.2
 */