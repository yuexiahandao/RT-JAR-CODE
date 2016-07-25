/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ 
/*     */ public final class DnsName
/*     */   implements Name
/*     */ {
/* 109 */   private String domain = "";
/*     */ 
/* 114 */   private ArrayList labels = new ArrayList();
/*     */ 
/* 119 */   private short octets = 1;
/*     */   private static final long serialVersionUID = 7040187611324710271L;
/*     */ 
/*     */   public DnsName()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DnsName(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 136 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   private DnsName(DnsName paramDnsName, int paramInt1, int paramInt2)
/*     */   {
/* 147 */     int i = paramDnsName.size() - paramInt2;
/* 148 */     int j = paramDnsName.size() - paramInt1;
/* 149 */     this.labels.addAll(paramDnsName.labels.subList(i, j));
/*     */ 
/* 151 */     if (size() == paramDnsName.size()) {
/* 152 */       this.domain = paramDnsName.domain;
/* 153 */       this.octets = paramDnsName.octets;
/*     */     } else {
/* 155 */       Iterator localIterator = this.labels.iterator();
/* 156 */       while (localIterator.hasNext()) {
/* 157 */         String str = (String)localIterator.next();
/* 158 */         if (str.length() > 0)
/* 159 */           this.octets = ((short)(this.octets + (short)(str.length() + 1)));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 167 */     if (this.domain == null) {
/* 168 */       StringBuffer localStringBuffer = new StringBuffer();
/* 169 */       Iterator localIterator = this.labels.iterator();
/* 170 */       while (localIterator.hasNext()) {
/* 171 */         String str = (String)localIterator.next();
/* 172 */         if ((localStringBuffer.length() > 0) || (str.length() == 0)) {
/* 173 */           localStringBuffer.append('.');
/*     */         }
/* 175 */         escape(localStringBuffer, str);
/*     */       }
/* 177 */       this.domain = localStringBuffer.toString();
/*     */     }
/* 179 */     return this.domain;
/*     */   }
/*     */ 
/*     */   public boolean isHostName()
/*     */   {
/* 186 */     Iterator localIterator = this.labels.iterator();
/* 187 */     while (localIterator.hasNext()) {
/* 188 */       if (!isHostNameLabel((String)localIterator.next())) {
/* 189 */         return false;
/*     */       }
/*     */     }
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   public short getOctets() {
/* 196 */     return this.octets;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 200 */     return this.labels.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 204 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 208 */     int i = 0;
/* 209 */     for (int j = 0; j < size(); j++) {
/* 210 */       i = 31 * i + getKey(j).hashCode();
/*     */     }
/* 212 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 216 */     if ((!(paramObject instanceof Name)) || ((paramObject instanceof CompositeName))) {
/* 217 */       return false;
/*     */     }
/* 219 */     Name localName = (Name)paramObject;
/* 220 */     return (size() == localName.size()) && (compareTo(paramObject) == 0);
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 225 */     Name localName = (Name)paramObject;
/* 226 */     return compareRange(0, size(), localName);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName) {
/* 230 */     return (size() >= paramName.size()) && (compareRange(0, paramName.size(), paramName) == 0);
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Name paramName)
/*     */   {
/* 235 */     return (size() >= paramName.size()) && (compareRange(size() - paramName.size(), size(), paramName) == 0);
/*     */   }
/*     */ 
/*     */   public String get(int paramInt)
/*     */   {
/* 240 */     if ((paramInt < 0) || (paramInt >= size())) {
/* 241 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 243 */     int i = size() - paramInt - 1;
/* 244 */     return (String)this.labels.get(i);
/*     */   }
/*     */ 
/*     */   public Enumeration getAll() {
/* 248 */     return new Enumeration() {
/* 249 */       int pos = 0;
/*     */ 
/* 251 */       public boolean hasMoreElements() { return this.pos < DnsName.this.size(); }
/*     */ 
/*     */       public Object nextElement() {
/* 254 */         if (this.pos < DnsName.this.size()) {
/* 255 */           return DnsName.this.get(this.pos++);
/*     */         }
/* 257 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt) {
/* 263 */     return new DnsName(this, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt) {
/* 267 */     return new DnsName(this, paramInt, size());
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 271 */     return new DnsName(this, 0, size());
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt) {
/* 275 */     if ((paramInt < 0) || (paramInt >= size())) {
/* 276 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 278 */     int i = size() - paramInt - 1;
/* 279 */     String str = (String)this.labels.remove(i);
/* 280 */     int j = str.length();
/* 281 */     if (j > 0) {
/* 282 */       this.octets = ((short)(this.octets - (short)(j + 1)));
/*     */     }
/* 284 */     this.domain = null;
/* 285 */     return str;
/*     */   }
/*     */ 
/*     */   public Name add(String paramString) throws InvalidNameException {
/* 289 */     return add(size(), paramString);
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, String paramString) throws InvalidNameException {
/* 293 */     if ((paramInt < 0) || (paramInt > size())) {
/* 294 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 297 */     int i = paramString.length();
/* 298 */     if (((paramInt > 0) && (i == 0)) || ((paramInt == 0) && (hasRootLabel())))
/*     */     {
/* 300 */       throw new InvalidNameException("Empty label must be the last label in a domain name");
/*     */     }
/*     */ 
/* 304 */     if (i > 0) {
/* 305 */       if (this.octets + i + 1 >= 256) {
/* 306 */         throw new InvalidNameException("Name too long");
/*     */       }
/* 308 */       this.octets = ((short)(this.octets + (short)(i + 1)));
/*     */     }
/*     */ 
/* 311 */     int j = size() - paramInt;
/* 312 */     verifyLabel(paramString);
/* 313 */     this.labels.add(j, paramString);
/*     */ 
/* 315 */     this.domain = null;
/* 316 */     return this;
/*     */   }
/*     */ 
/*     */   public Name addAll(Name paramName) throws InvalidNameException {
/* 320 */     return addAll(size(), paramName);
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, Name paramName) throws InvalidNameException {
/* 324 */     if ((paramName instanceof DnsName))
/*     */     {
/* 328 */       DnsName localDnsName = (DnsName)paramName;
/*     */ 
/* 330 */       if (localDnsName.isEmpty()) {
/* 331 */         return this;
/*     */       }
/*     */ 
/* 334 */       if (((paramInt > 0) && (localDnsName.hasRootLabel())) || ((paramInt == 0) && (hasRootLabel())))
/*     */       {
/* 336 */         throw new InvalidNameException("Empty label must be the last label in a domain name");
/*     */       }
/*     */ 
/* 340 */       short s = (short)(this.octets + localDnsName.octets - 1);
/* 341 */       if (s > 255) {
/* 342 */         throw new InvalidNameException("Name too long");
/*     */       }
/* 344 */       this.octets = s;
/* 345 */       int j = size() - paramInt;
/* 346 */       this.labels.addAll(j, localDnsName.labels);
/*     */ 
/* 350 */       if (isEmpty())
/* 351 */         this.domain = localDnsName.domain;
/* 352 */       else if ((this.domain == null) || (localDnsName.domain == null))
/* 353 */         this.domain = null;
/* 354 */       else if (paramInt == 0)
/* 355 */         this.domain = (this.domain + (localDnsName.domain.equals(".") ? "" : ".") + localDnsName.domain);
/* 356 */       else if (paramInt == size())
/* 357 */         this.domain = (localDnsName.domain + (this.domain.equals(".") ? "" : ".") + this.domain);
/*     */       else {
/* 359 */         this.domain = null;
/*     */       }
/*     */     }
/* 362 */     else if ((paramName instanceof CompositeName)) {
/* 363 */       paramName = (DnsName)paramName;
/*     */     }
/*     */     else
/*     */     {
/* 367 */       for (int i = paramName.size() - 1; i >= 0; i--) {
/* 368 */         add(paramInt, paramName.get(i));
/*     */       }
/*     */     }
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   boolean hasRootLabel()
/*     */   {
/* 376 */     return (!isEmpty()) && (get(0).equals(""));
/*     */   }
/*     */ 
/*     */   private int compareRange(int paramInt1, int paramInt2, Name paramName)
/*     */   {
/* 389 */     if ((paramName instanceof CompositeName)) {
/* 390 */       paramName = (DnsName)paramName;
/*     */     }
/*     */ 
/* 393 */     int i = Math.min(paramInt2 - paramInt1, paramName.size());
/* 394 */     for (int j = 0; j < i; j++) {
/* 395 */       String str1 = get(j + paramInt1);
/* 396 */       String str2 = paramName.get(j);
/*     */ 
/* 398 */       int k = size() - (j + paramInt1) - 1;
/*     */ 
/* 401 */       int m = compareLabels(str1, str2);
/* 402 */       if (m != 0) {
/* 403 */         return m;
/*     */       }
/*     */     }
/* 406 */     return paramInt2 - paramInt1 - paramName.size();
/*     */   }
/*     */ 
/*     */   String getKey(int paramInt)
/*     */   {
/* 415 */     return keyForLabel(get(paramInt));
/*     */   }
/*     */ 
/*     */   private void parse(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 424 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 426 */     for (int i = 0; i < paramString.length(); i++) {
/* 427 */       char c = paramString.charAt(i);
/*     */ 
/* 429 */       if (c == '\\') {
/* 430 */         c = getEscapedOctet(paramString, i++);
/* 431 */         if (isDigit(paramString.charAt(i))) {
/* 432 */           i += 2;
/*     */         }
/* 434 */         localStringBuffer.append(c);
/*     */       }
/* 436 */       else if (c != '.') {
/* 437 */         localStringBuffer.append(c);
/*     */       }
/*     */       else {
/* 440 */         add(0, localStringBuffer.toString());
/*     */ 
/* 442 */         localStringBuffer.delete(0, i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 451 */     if ((!paramString.equals("")) && (!paramString.equals("."))) {
/* 452 */       add(0, localStringBuffer.toString());
/*     */     }
/*     */ 
/* 455 */     this.domain = paramString;
/*     */   }
/*     */ 
/*     */   private static char getEscapedOctet(String paramString, int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/*     */     try
/*     */     {
/* 467 */       char c1 = paramString.charAt(++paramInt);
/* 468 */       if (isDigit(c1)) {
/* 469 */         char c2 = paramString.charAt(++paramInt);
/* 470 */         char c3 = paramString.charAt(++paramInt);
/* 471 */         if ((isDigit(c2)) && (isDigit(c3))) {
/* 472 */           return (char)((c1 - '0') * 100 + (c2 - '0') * 10 + (c3 - '0'));
/*     */         }
/*     */ 
/* 475 */         throw new InvalidNameException("Invalid escape sequence in " + paramString);
/*     */       }
/*     */ 
/* 479 */       return c1;
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 482 */     throw new InvalidNameException("Invalid escape sequence in " + paramString);
/*     */   }
/*     */ 
/*     */   private static void verifyLabel(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 492 */     if (paramString.length() > 63) {
/* 493 */       throw new InvalidNameException("Label exceeds 63 octets: " + paramString);
/*     */     }
/*     */ 
/* 497 */     for (int i = 0; i < paramString.length(); i++) {
/* 498 */       int j = paramString.charAt(i);
/* 499 */       if ((j & 0xFF00) != 0)
/* 500 */         throw new InvalidNameException("Label has two-byte char: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isHostNameLabel(String paramString)
/*     */   {
/* 510 */     for (int i = 0; i < paramString.length(); i++) {
/* 511 */       char c = paramString.charAt(i);
/* 512 */       if (!isHostNameChar(c)) {
/* 513 */         return false;
/*     */       }
/*     */     }
/* 516 */     return (!paramString.startsWith("-")) && (!paramString.endsWith("-"));
/*     */   }
/*     */ 
/*     */   private static boolean isHostNameChar(char paramChar) {
/* 520 */     return (paramChar == '-') || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= '0') && (paramChar <= '9'));
/*     */   }
/*     */ 
/*     */   private static boolean isDigit(char paramChar)
/*     */   {
/* 527 */     return (paramChar >= '0') && (paramChar <= '9');
/*     */   }
/*     */ 
/*     */   private static void escape(StringBuffer paramStringBuffer, String paramString)
/*     */   {
/* 534 */     for (int i = 0; i < paramString.length(); i++) {
/* 535 */       char c = paramString.charAt(i);
/* 536 */       if ((c == '.') || (c == '\\')) {
/* 537 */         paramStringBuffer.append('\\');
/*     */       }
/* 539 */       paramStringBuffer.append(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int compareLabels(String paramString1, String paramString2)
/*     */   {
/* 550 */     int i = Math.min(paramString1.length(), paramString2.length());
/* 551 */     for (int j = 0; j < i; j++) {
/* 552 */       int k = paramString1.charAt(j);
/* 553 */       int m = paramString2.charAt(j);
/* 554 */       if ((k >= 65) && (k <= 90)) {
/* 555 */         k = (char)(k + 32);
/*     */       }
/* 557 */       if ((m >= 65) && (m <= 90)) {
/* 558 */         m = (char)(m + 32);
/*     */       }
/* 560 */       if (k != m) {
/* 561 */         return k - m;
/*     */       }
/*     */     }
/* 564 */     return paramString1.length() - paramString2.length();
/*     */   }
/*     */ 
/*     */   private static String keyForLabel(String paramString)
/*     */   {
/* 573 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length());
/* 574 */     for (int i = 0; i < paramString.length(); i++) {
/* 575 */       char c = paramString.charAt(i);
/* 576 */       if ((c >= 'A') && (c <= 'Z')) {
/* 577 */         c = (char)(c + ' ');
/*     */       }
/* 579 */       localStringBuffer.append(c);
/*     */     }
/* 581 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 593 */     paramObjectOutputStream.writeObject(toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/*     */     try {
/* 599 */       parse((String)paramObjectInputStream.readObject());
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 602 */       throw new StreamCorruptedException("Invalid name: " + this.domain);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsName
 * JD-Core Version:    0.6.2
 */