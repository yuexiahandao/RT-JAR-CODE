/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ 
/*     */ public final class LdapName
/*     */   implements Name
/*     */ {
/*     */   private transient String unparsed;
/*     */   private transient Vector rdns;
/*  82 */   private transient boolean valuesCaseSensitive = false;
/*     */   static final long serialVersionUID = -1595520034788997356L;
/*     */ 
/*     */   public LdapName(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/*  92 */     this.unparsed = paramString;
/*  93 */     parse();
/*     */   }
/*     */ 
/*     */   private LdapName(String paramString, Vector paramVector)
/*     */   {
/* 101 */     this.unparsed = paramString;
/* 102 */     this.rdns = ((Vector)paramVector.clone());
/*     */   }
/*     */ 
/*     */   private LdapName(String paramString, Vector paramVector, int paramInt1, int paramInt2)
/*     */   {
/* 111 */     this.unparsed = paramString;
/* 112 */     this.rdns = new Vector();
/* 113 */     for (int i = paramInt1; i < paramInt2; i++)
/* 114 */       this.rdns.addElement(paramVector.elementAt(i));
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 120 */     return new LdapName(this.unparsed, this.rdns);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 124 */     if (this.unparsed != null) {
/* 125 */       return this.unparsed;
/*     */     }
/*     */ 
/* 128 */     StringBuffer localStringBuffer = new StringBuffer();
/* 129 */     for (int i = this.rdns.size() - 1; i >= 0; i--) {
/* 130 */       if (i < this.rdns.size() - 1) {
/* 131 */         localStringBuffer.append(',');
/*     */       }
/* 133 */       Rdn localRdn = (Rdn)this.rdns.elementAt(i);
/* 134 */       localStringBuffer.append(localRdn);
/*     */     }
/*     */ 
/* 137 */     this.unparsed = new String(localStringBuffer);
/* 138 */     return this.unparsed;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 142 */     return ((paramObject instanceof LdapName)) && (compareTo(paramObject) == 0);
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 147 */     LdapName localLdapName = (LdapName)paramObject;
/*     */ 
/* 149 */     if ((paramObject == this) || ((this.unparsed != null) && (this.unparsed.equals(localLdapName.unparsed))))
/*     */     {
/* 151 */       return 0;
/*     */     }
/*     */ 
/* 155 */     int i = Math.min(this.rdns.size(), localLdapName.rdns.size());
/* 156 */     for (int j = 0; j < i; j++)
/*     */     {
/* 158 */       Rdn localRdn1 = (Rdn)this.rdns.elementAt(j);
/* 159 */       Rdn localRdn2 = (Rdn)localLdapName.rdns.elementAt(j);
/*     */ 
/* 161 */       int k = localRdn1.compareTo(localRdn2);
/* 162 */       if (k != 0) {
/* 163 */         return k;
/*     */       }
/*     */     }
/* 166 */     return this.rdns.size() - localLdapName.rdns.size();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 171 */     int i = 0;
/*     */ 
/* 174 */     for (int j = 0; j < this.rdns.size(); j++) {
/* 175 */       Rdn localRdn = (Rdn)this.rdns.elementAt(j);
/* 176 */       i += localRdn.hashCode();
/*     */     }
/* 178 */     return i;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 182 */     return this.rdns.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 186 */     return this.rdns.isEmpty();
/*     */   }
/*     */ 
/*     */   public Enumeration getAll() {
/* 190 */     final Enumeration localEnumeration = this.rdns.elements();
/*     */ 
/* 192 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 194 */         return localEnumeration.hasMoreElements();
/*     */       }
/*     */       public Object nextElement() {
/* 197 */         return localEnumeration.nextElement().toString();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String get(int paramInt) {
/* 203 */     return this.rdns.elementAt(paramInt).toString();
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt) {
/* 207 */     return new LdapName(null, this.rdns, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt) {
/* 211 */     return new LdapName(null, this.rdns, paramInt, this.rdns.size());
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName) {
/* 215 */     int i = this.rdns.size();
/* 216 */     int j = paramName.size();
/* 217 */     return (i >= j) && (matches(0, j, paramName));
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Name paramName)
/*     */   {
/* 222 */     int i = this.rdns.size();
/* 223 */     int j = paramName.size();
/* 224 */     return (i >= j) && (matches(i - j, i, paramName));
/*     */   }
/*     */ 
/*     */   public void setValuesCaseSensitive(boolean paramBoolean)
/*     */   {
/* 234 */     toString();
/* 235 */     this.rdns = null;
/*     */     try {
/* 237 */       parse();
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 240 */       throw new IllegalStateException("Cannot parse name: " + this.unparsed);
/*     */     }
/* 242 */     this.valuesCaseSensitive = paramBoolean;
/*     */   }
/*     */ 
/*     */   private boolean matches(int paramInt1, int paramInt2, Name paramName)
/*     */   {
/* 253 */     for (int i = paramInt1; i < paramInt2; i++)
/*     */     {
/*     */       Object localObject;
/*     */       Rdn localRdn;
/* 255 */       if ((paramName instanceof LdapName)) {
/* 256 */         localObject = (LdapName)paramName;
/* 257 */         localRdn = (Rdn)((LdapName)localObject).rdns.elementAt(i - paramInt1);
/*     */       } else {
/* 259 */         localObject = paramName.get(i - paramInt1);
/*     */         try {
/* 261 */           localRdn = new DnParser((String)localObject, this.valuesCaseSensitive).getRdn();
/*     */         } catch (InvalidNameException localInvalidNameException) {
/* 263 */           return false;
/*     */         }
/*     */       }
/*     */ 
/* 267 */       if (!localRdn.equals(this.rdns.elementAt(i))) {
/* 268 */         return false;
/*     */       }
/*     */     }
/* 271 */     return true;
/*     */   }
/*     */ 
/*     */   public Name addAll(Name paramName) throws InvalidNameException {
/* 275 */     return addAll(size(), paramName);
/*     */   }
/*     */ 
/*     */   public Name addAll(int paramInt, Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/*     */     Object localObject;
/* 283 */     if ((paramName instanceof LdapName)) {
/* 284 */       localObject = (LdapName)paramName;
/* 285 */       for (int i = 0; i < ((LdapName)localObject).rdns.size(); i++)
/* 286 */         this.rdns.insertElementAt(((LdapName)localObject).rdns.elementAt(i), paramInt++);
/*     */     }
/*     */     else {
/* 289 */       localObject = paramName.getAll();
/* 290 */       while (((Enumeration)localObject).hasMoreElements()) {
/* 291 */         DnParser localDnParser = new DnParser((String)((Enumeration)localObject).nextElement(), this.valuesCaseSensitive);
/*     */ 
/* 293 */         this.rdns.insertElementAt(localDnParser.getRdn(), paramInt++);
/*     */       }
/*     */     }
/* 296 */     this.unparsed = null;
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */   public Name add(String paramString) throws InvalidNameException {
/* 301 */     return add(size(), paramString);
/*     */   }
/*     */ 
/*     */   public Name add(int paramInt, String paramString) throws InvalidNameException {
/* 305 */     Rdn localRdn = new DnParser(paramString, this.valuesCaseSensitive).getRdn();
/* 306 */     this.rdns.insertElementAt(localRdn, paramInt);
/* 307 */     this.unparsed = null;
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt) throws InvalidNameException {
/* 312 */     String str = get(paramInt);
/* 313 */     this.rdns.removeElementAt(paramInt);
/* 314 */     this.unparsed = null;
/* 315 */     return str;
/*     */   }
/*     */ 
/*     */   private void parse() throws InvalidNameException
/*     */   {
/* 320 */     this.rdns = new DnParser(this.unparsed, this.valuesCaseSensitive).getDn();
/*     */   }
/*     */ 
/*     */   private static boolean isWhitespace(char paramChar)
/*     */   {
/* 327 */     return (paramChar == ' ') || (paramChar == '\r');
/*     */   }
/*     */ 
/*     */   public static String escapeAttributeValue(Object paramObject)
/*     */   {
/* 343 */     return TypeAndValue.escapeValue(paramObject);
/*     */   }
/*     */ 
/*     */   public static Object unescapeAttributeValue(String paramString)
/*     */   {
/* 352 */     return TypeAndValue.unescapeValue(paramString);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 364 */     paramObjectOutputStream.writeObject(toString());
/* 365 */     paramObjectOutputStream.writeBoolean(this.valuesCaseSensitive);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 370 */     this.unparsed = ((String)paramObjectInputStream.readObject());
/* 371 */     this.valuesCaseSensitive = paramObjectInputStream.readBoolean();
/*     */     try {
/* 373 */       parse();
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 376 */       throw new StreamCorruptedException("Invalid name: " + this.unparsed);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DnParser
/*     */   {
/*     */     private final String name;
/*     */     private final char[] chars;
/*     */     private final int len;
/* 392 */     private int cur = 0;
/*     */     private boolean valuesCaseSensitive;
/*     */ 
/*     */     DnParser(String paramString, boolean paramBoolean)
/*     */       throws InvalidNameException
/*     */     {
/* 400 */       this.name = paramString;
/* 401 */       this.len = paramString.length();
/* 402 */       this.chars = paramString.toCharArray();
/* 403 */       this.valuesCaseSensitive = paramBoolean;
/*     */     }
/*     */ 
/*     */     Vector getDn()
/*     */       throws InvalidNameException
/*     */     {
/* 410 */       this.cur = 0;
/* 411 */       Vector localVector = new Vector(this.len / 3 + 10);
/*     */ 
/* 413 */       if (this.len == 0) {
/* 414 */         return localVector;
/*     */       }
/*     */ 
/* 417 */       localVector.addElement(parseRdn());
/* 418 */       while (this.cur < this.len) {
/* 419 */         if ((this.chars[this.cur] == ',') || (this.chars[this.cur] == ';')) {
/* 420 */           this.cur += 1;
/* 421 */           localVector.insertElementAt(parseRdn(), 0);
/*     */         } else {
/* 423 */           throw new InvalidNameException("Invalid name: " + this.name);
/*     */         }
/*     */       }
/* 426 */       return localVector;
/*     */     }
/*     */ 
/*     */     LdapName.Rdn getRdn()
/*     */       throws InvalidNameException
/*     */     {
/* 433 */       LdapName.Rdn localRdn = parseRdn();
/* 434 */       if (this.cur < this.len) {
/* 435 */         throw new InvalidNameException("Invalid RDN: " + this.name);
/*     */       }
/* 437 */       return localRdn;
/*     */     }
/*     */ 
/*     */     private LdapName.Rdn parseRdn()
/*     */       throws InvalidNameException
/*     */     {
/* 446 */       LdapName.Rdn localRdn = new LdapName.Rdn();
/* 447 */       while (this.cur < this.len) {
/* 448 */         consumeWhitespace();
/* 449 */         String str1 = parseAttrType();
/* 450 */         consumeWhitespace();
/* 451 */         if ((this.cur >= this.len) || (this.chars[this.cur] != '=')) {
/* 452 */           throw new InvalidNameException("Invalid name: " + this.name);
/*     */         }
/* 454 */         this.cur += 1;
/* 455 */         consumeWhitespace();
/* 456 */         String str2 = parseAttrValue();
/* 457 */         consumeWhitespace();
/*     */ 
/* 459 */         localRdn.add(new LdapName.TypeAndValue(str1, str2, this.valuesCaseSensitive));
/* 460 */         if ((this.cur >= this.len) || (this.chars[this.cur] != '+')) {
/*     */           break;
/*     */         }
/* 463 */         this.cur += 1;
/*     */       }
/* 465 */       return localRdn;
/*     */     }
/*     */ 
/*     */     private String parseAttrType()
/*     */       throws InvalidNameException
/*     */     {
/* 477 */       int i = this.cur;
/* 478 */       while (this.cur < this.len) {
/* 479 */         char c = this.chars[this.cur];
/* 480 */         if ((!Character.isLetterOrDigit(c)) && (c != '.') && (c != '-') && (c != ' '))
/*     */         {
/*     */           break;
/*     */         }
/* 484 */         this.cur += 1;
/*     */       }
/*     */ 
/* 490 */       while ((this.cur > i) && (this.chars[(this.cur - 1)] == ' ')) {
/* 491 */         this.cur -= 1;
/*     */       }
/*     */ 
/* 494 */       if (i == this.cur) {
/* 495 */         throw new InvalidNameException("Invalid name: " + this.name);
/*     */       }
/* 497 */       return new String(this.chars, i, this.cur - i);
/*     */     }
/*     */ 
/*     */     private String parseAttrValue()
/*     */       throws InvalidNameException
/*     */     {
/* 506 */       if ((this.cur < this.len) && (this.chars[this.cur] == '#'))
/* 507 */         return parseBinaryAttrValue();
/* 508 */       if ((this.cur < this.len) && (this.chars[this.cur] == '"')) {
/* 509 */         return parseQuotedAttrValue();
/*     */       }
/* 511 */       return parseStringAttrValue();
/*     */     }
/*     */ 
/*     */     private String parseBinaryAttrValue() throws InvalidNameException
/*     */     {
/* 516 */       int i = this.cur;
/* 517 */       this.cur += 1;
/* 518 */       while ((this.cur < this.len) && (Character.isLetterOrDigit(this.chars[this.cur])))
/*     */       {
/* 520 */         this.cur += 1;
/*     */       }
/* 522 */       return new String(this.chars, i, this.cur - i);
/*     */     }
/*     */ 
/*     */     private String parseQuotedAttrValue() throws InvalidNameException
/*     */     {
/* 527 */       int i = this.cur;
/* 528 */       this.cur += 1;
/*     */ 
/* 530 */       while ((this.cur < this.len) && (this.chars[this.cur] != '"')) {
/* 531 */         if (this.chars[this.cur] == '\\') {
/* 532 */           this.cur += 1;
/*     */         }
/* 534 */         this.cur += 1;
/*     */       }
/* 536 */       if (this.cur >= this.len) {
/* 537 */         throw new InvalidNameException("Invalid name: " + this.name);
/*     */       }
/* 539 */       this.cur += 1;
/*     */ 
/* 541 */       return new String(this.chars, i, this.cur - i);
/*     */     }
/*     */ 
/*     */     private String parseStringAttrValue() throws InvalidNameException
/*     */     {
/* 546 */       int i = this.cur;
/* 547 */       int j = -1;
/*     */ 
/* 549 */       while ((this.cur < this.len) && (!atTerminator())) {
/* 550 */         if (this.chars[this.cur] == '\\') {
/* 551 */           this.cur += 1;
/* 552 */           j = this.cur;
/*     */         }
/* 554 */         this.cur += 1;
/*     */       }
/* 556 */       if (this.cur > this.len) {
/* 557 */         throw new InvalidNameException("Invalid name: " + this.name);
/*     */       }
/*     */ 
/* 562 */       for (int k = this.cur; (k > i) && 
/* 563 */         (LdapName.isWhitespace(this.chars[(k - 1)])) && (j != k - 1); k--);
/* 567 */       return new String(this.chars, i, k - i);
/*     */     }
/*     */ 
/*     */     private void consumeWhitespace() {
/* 571 */       while ((this.cur < this.len) && (LdapName.isWhitespace(this.chars[this.cur])))
/* 572 */         this.cur += 1;
/*     */     }
/*     */ 
/*     */     private boolean atTerminator()
/*     */     {
/* 581 */       return (this.cur < this.len) && ((this.chars[this.cur] == ',') || (this.chars[this.cur] == ';') || (this.chars[this.cur] == '+'));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Rdn
/*     */   {
/* 598 */     private final Vector tvs = new Vector();
/*     */ 
/*     */     void add(LdapName.TypeAndValue paramTypeAndValue)
/*     */     {
/* 605 */       for (int i = 0; i < this.tvs.size(); i++) {
/* 606 */         int j = paramTypeAndValue.compareTo(this.tvs.elementAt(i));
/* 607 */         if (j == 0)
/* 608 */           return;
/* 609 */         if (j < 0)
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/* 614 */       this.tvs.insertElementAt(paramTypeAndValue, i);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 618 */       StringBuffer localStringBuffer = new StringBuffer();
/* 619 */       for (int i = 0; i < this.tvs.size(); i++) {
/* 620 */         if (i > 0) {
/* 621 */           localStringBuffer.append('+');
/*     */         }
/* 623 */         localStringBuffer.append(this.tvs.elementAt(i));
/*     */       }
/* 625 */       return new String(localStringBuffer);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 629 */       return ((paramObject instanceof Rdn)) && (compareTo(paramObject) == 0);
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 635 */       Rdn localRdn = (Rdn)paramObject;
/* 636 */       int i = Math.min(this.tvs.size(), localRdn.tvs.size());
/* 637 */       for (int j = 0; j < i; j++)
/*     */       {
/* 639 */         LdapName.TypeAndValue localTypeAndValue = (LdapName.TypeAndValue)this.tvs.elementAt(j);
/* 640 */         int k = localTypeAndValue.compareTo(localRdn.tvs.elementAt(j));
/* 641 */         if (k != 0) {
/* 642 */           return k;
/*     */         }
/*     */       }
/* 645 */       return this.tvs.size() - localRdn.tvs.size();
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 650 */       int i = 0;
/*     */ 
/* 653 */       for (int j = 0; j < this.tvs.size(); j++) {
/* 654 */         i += this.tvs.elementAt(j).hashCode();
/*     */       }
/* 656 */       return i;
/*     */     }
/*     */ 
/*     */     Attributes toAttributes() {
/* 660 */       BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*     */ 
/* 664 */       for (int i = 0; i < this.tvs.size(); i++) {
/* 665 */         LdapName.TypeAndValue localTypeAndValue = (LdapName.TypeAndValue)this.tvs.elementAt(i);
/*     */         Attribute localAttribute;
/* 666 */         if ((localAttribute = localBasicAttributes.get(localTypeAndValue.getType())) == null)
/* 667 */           localBasicAttributes.put(localTypeAndValue.getType(), localTypeAndValue.getUnescapedValue());
/*     */         else {
/* 669 */           localAttribute.add(localTypeAndValue.getUnescapedValue());
/*     */         }
/*     */       }
/* 672 */       return localBasicAttributes;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TypeAndValue
/*     */   {
/*     */     private final String type;
/*     */     private final String value;
/*     */     private final boolean binary;
/*     */     private final boolean valueCaseSensitive;
/* 690 */     private String comparable = null;
/*     */ 
/*     */     TypeAndValue(String paramString1, String paramString2, boolean paramBoolean) {
/* 693 */       this.type = paramString1;
/* 694 */       this.value = paramString2;
/* 695 */       this.binary = paramString2.startsWith("#");
/* 696 */       this.valueCaseSensitive = paramBoolean;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 700 */       return this.type + "=" + this.value;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 707 */       TypeAndValue localTypeAndValue = (TypeAndValue)paramObject;
/*     */ 
/* 709 */       int i = this.type.toUpperCase().compareTo(localTypeAndValue.type.toUpperCase());
/* 710 */       if (i != 0) {
/* 711 */         return i;
/*     */       }
/* 713 */       if (this.value.equals(localTypeAndValue.value)) {
/* 714 */         return 0;
/*     */       }
/* 716 */       return getValueComparable().compareTo(localTypeAndValue.getValueComparable());
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 721 */       if (!(paramObject instanceof TypeAndValue)) {
/* 722 */         return false;
/*     */       }
/* 724 */       TypeAndValue localTypeAndValue = (TypeAndValue)paramObject;
/* 725 */       return (this.type.equalsIgnoreCase(localTypeAndValue.type)) && ((this.value.equals(localTypeAndValue.value)) || (getValueComparable().equals(localTypeAndValue.getValueComparable())));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 732 */       return this.type.toUpperCase().hashCode() + getValueComparable().hashCode();
/*     */     }
/*     */ 
/*     */     String getType()
/*     */     {
/* 740 */       return this.type;
/*     */     }
/*     */ 
/*     */     Object getUnescapedValue()
/*     */     {
/* 747 */       return unescapeValue(this.value);
/*     */     }
/*     */ 
/*     */     private String getValueComparable()
/*     */     {
/* 760 */       if (this.comparable != null) {
/* 761 */         return this.comparable;
/*     */       }
/*     */ 
/* 765 */       if (this.binary) {
/* 766 */         this.comparable = this.value.toUpperCase();
/*     */       } else {
/* 768 */         this.comparable = ((String)unescapeValue(this.value));
/* 769 */         if (!this.valueCaseSensitive) {
/* 770 */           this.comparable = this.comparable.toUpperCase();
/*     */         }
/*     */       }
/* 773 */       return this.comparable;
/*     */     }
/*     */ 
/*     */     static String escapeValue(Object paramObject)
/*     */     {
/* 781 */       return (paramObject instanceof byte[]) ? escapeBinaryValue((byte[])paramObject) : escapeStringValue((String)paramObject);
/*     */     }
/*     */ 
/*     */     private static String escapeStringValue(String paramString)
/*     */     {
/* 796 */       char[] arrayOfChar = paramString.toCharArray();
/* 797 */       StringBuffer localStringBuffer = new StringBuffer(2 * paramString.length());
/*     */ 
/* 801 */       for (int i = 0; (i < arrayOfChar.length) && 
/* 802 */         (LdapName.isWhitespace(arrayOfChar[i])); i++);
/* 807 */       for (int j = arrayOfChar.length - 1; (j >= 0) && 
/* 808 */         (LdapName.isWhitespace(arrayOfChar[j])); j--);
/* 813 */       for (int k = 0; k < arrayOfChar.length; k++) {
/* 814 */         char c = arrayOfChar[k];
/* 815 */         if ((k < i) || (k > j) || (",=+<>#;\"\\".indexOf(c) >= 0)) {
/* 816 */           localStringBuffer.append('\\');
/*     */         }
/* 818 */         localStringBuffer.append(c);
/*     */       }
/* 820 */       return new String(localStringBuffer);
/*     */     }
/*     */ 
/*     */     private static String escapeBinaryValue(byte[] paramArrayOfByte)
/*     */     {
/* 829 */       StringBuffer localStringBuffer = new StringBuffer(1 + 2 * paramArrayOfByte.length);
/* 830 */       localStringBuffer.append("#");
/*     */ 
/* 832 */       for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 833 */         int j = paramArrayOfByte[i];
/* 834 */         localStringBuffer.append(Character.forDigit(0xF & j >>> 4, 16));
/* 835 */         localStringBuffer.append(Character.forDigit(0xF & j, 16));
/*     */       }
/*     */ 
/* 838 */       return new String(localStringBuffer).toUpperCase();
/*     */     }
/*     */ 
/*     */     static Object unescapeValue(String paramString)
/*     */     {
/* 850 */       char[] arrayOfChar = paramString.toCharArray();
/* 851 */       int i = 0;
/* 852 */       int j = arrayOfChar.length;
/*     */ 
/* 855 */       while ((i < j) && (LdapName.isWhitespace(arrayOfChar[i]))) {
/* 856 */         i++;
/*     */       }
/* 858 */       while ((i < j) && (LdapName.isWhitespace(arrayOfChar[(j - 1)]))) {
/* 859 */         j--;
/*     */       }
/*     */ 
/* 866 */       if ((j != arrayOfChar.length) && (i < j) && (arrayOfChar[(j - 1)] == '\\'))
/*     */       {
/* 869 */         j++;
/*     */       }
/* 871 */       if (i >= j) {
/* 872 */         return "";
/*     */       }
/*     */ 
/* 875 */       if (arrayOfChar[i] == '#')
/*     */       {
/* 877 */         return decodeHexPairs(arrayOfChar, ++i, j);
/*     */       }
/*     */ 
/* 881 */       if ((arrayOfChar[i] == '"') && (arrayOfChar[(j - 1)] == '"')) {
/* 882 */         i++;
/* 883 */         j--;
/*     */       }
/*     */ 
/* 886 */       StringBuffer localStringBuffer = new StringBuffer(j - i);
/* 887 */       int k = -1;
/*     */ 
/* 889 */       for (int m = i; m < j; m++) {
/* 890 */         if ((arrayOfChar[m] == '\\') && (m + 1 < j)) {
/* 891 */           if (!Character.isLetterOrDigit(arrayOfChar[(m + 1)])) {
/* 892 */             m++;
/* 893 */             localStringBuffer.append(arrayOfChar[m]);
/* 894 */             k = m;
/*     */           }
/*     */           else
/*     */           {
/* 898 */             byte[] arrayOfByte = getUtf8Octets(arrayOfChar, m, j);
/* 899 */             if (arrayOfByte.length > 0) {
/*     */               try {
/* 901 */                 localStringBuffer.append(new String(arrayOfByte, "UTF8"));
/*     */               }
/*     */               catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */               }
/* 905 */               m += arrayOfByte.length * 3 - 1;
/*     */             } else {
/* 907 */               throw new IllegalArgumentException("Not a valid attribute string value:" + paramString + ", improper usage of backslash");
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 913 */           localStringBuffer.append(arrayOfChar[m]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 919 */       m = localStringBuffer.length();
/* 920 */       if ((LdapName.isWhitespace(localStringBuffer.charAt(m - 1))) && (k != j - 1)) {
/* 921 */         localStringBuffer.setLength(m - 1);
/*     */       }
/*     */ 
/* 924 */       return new String(localStringBuffer);
/*     */     }
/*     */ 
/*     */     private static byte[] decodeHexPairs(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     {
/* 934 */       byte[] arrayOfByte = new byte[(paramInt2 - paramInt1) / 2];
/* 935 */       for (int i = 0; paramInt1 + 1 < paramInt2; i++) {
/* 936 */         int j = Character.digit(paramArrayOfChar[paramInt1], 16);
/* 937 */         int k = Character.digit(paramArrayOfChar[(paramInt1 + 1)], 16);
/* 938 */         if ((j < 0) || (k < 0)) {
/*     */           break;
/*     */         }
/* 941 */         arrayOfByte[i] = ((byte)((j << 4) + k));
/* 942 */         paramInt1 += 2;
/*     */       }
/* 944 */       if (paramInt1 != paramInt2) {
/* 945 */         throw new IllegalArgumentException("Illegal attribute value: #" + new String(paramArrayOfChar));
/*     */       }
/*     */ 
/* 948 */       return arrayOfByte;
/*     */     }
/*     */ 
/*     */     private static byte[] getUtf8Octets(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     {
/* 960 */       byte[] arrayOfByte1 = new byte[(paramInt2 - paramInt1) / 3];
/* 961 */       int i = 0;
/*     */ 
/* 963 */       while ((paramInt1 + 2 < paramInt2) && (paramArrayOfChar[(paramInt1++)] == '\\'))
/*     */       {
/* 965 */         int j = Character.digit(paramArrayOfChar[(paramInt1++)], 16);
/* 966 */         int k = Character.digit(paramArrayOfChar[(paramInt1++)], 16);
/* 967 */         if ((j < 0) || (k < 0)) {
/*     */           break;
/*     */         }
/* 970 */         arrayOfByte1[(i++)] = ((byte)((j << 4) + k));
/*     */       }
/*     */ 
/* 973 */       if (i == arrayOfByte1.length) {
/* 974 */         return arrayOfByte1;
/*     */       }
/* 976 */       byte[] arrayOfByte2 = new byte[i];
/* 977 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 978 */       return arrayOfByte2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapName
 * JD-Core Version:    0.6.2
 */