/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ 
/*     */ public class Rdn
/*     */   implements Serializable, Comparable<Object>
/*     */ {
/*     */   private transient ArrayList entries;
/*     */   private static final int DEFAULT_SIZE = 1;
/*     */   private static final long serialVersionUID = -5994465067210009656L;
/*     */   private static final String escapees = ",=+<>#;\"\\";
/*     */ 
/*     */   public Rdn(Attributes paramAttributes)
/*     */     throws InvalidNameException
/*     */   {
/* 130 */     if (paramAttributes.size() == 0) {
/* 131 */       throw new InvalidNameException("Attributes cannot be empty");
/*     */     }
/* 133 */     this.entries = new ArrayList(paramAttributes.size());
/* 134 */     NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/*     */     try {
/* 136 */       for (int i = 0; localNamingEnumeration.hasMore(); i++) {
/* 137 */         localObject = new RdnEntry(null);
/* 138 */         Attribute localAttribute = (Attribute)localNamingEnumeration.next();
/* 139 */         ((RdnEntry)localObject).type = localAttribute.getID();
/* 140 */         ((RdnEntry)localObject).value = localAttribute.get();
/* 141 */         this.entries.add(i, localObject);
/*     */       }
/*     */     } catch (NamingException localNamingException) {
/* 144 */       Object localObject = new InvalidNameException(localNamingException.getMessage());
/*     */ 
/* 146 */       ((InvalidNameException)localObject).initCause(localNamingException);
/* 147 */       throw ((Throwable)localObject);
/*     */     }
/* 149 */     sort();
/*     */   }
/*     */ 
/*     */   public Rdn(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 164 */     this.entries = new ArrayList(1);
/* 165 */     new Rfc2253Parser(paramString).parseRdn(this);
/*     */   }
/*     */ 
/*     */   public Rdn(Rdn paramRdn)
/*     */   {
/* 175 */     this.entries = new ArrayList(paramRdn.entries.size());
/* 176 */     this.entries.addAll(paramRdn.entries);
/*     */   }
/*     */ 
/*     */   public Rdn(String paramString, Object paramObject)
/*     */     throws InvalidNameException
/*     */   {
/* 194 */     if (paramObject == null) {
/* 195 */       throw new NullPointerException("Cannot set value to null");
/*     */     }
/* 197 */     if ((paramString.equals("")) || (isEmptyValue(paramObject))) {
/* 198 */       throw new InvalidNameException("type or value cannot be empty, type:" + paramString + " value:" + paramObject);
/*     */     }
/*     */ 
/* 202 */     this.entries = new ArrayList(1);
/* 203 */     put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   private boolean isEmptyValue(Object paramObject) {
/* 207 */     return (((paramObject instanceof String)) && (paramObject.equals(""))) || (((paramObject instanceof byte[])) && (((byte[])paramObject).length == 0));
/*     */   }
/*     */ 
/*     */   Rdn()
/*     */   {
/* 213 */     this.entries = new ArrayList(1);
/*     */   }
/*     */ 
/*     */   Rdn put(String paramString, Object paramObject)
/*     */   {
/* 231 */     RdnEntry localRdnEntry = new RdnEntry(null);
/* 232 */     localRdnEntry.type = paramString;
/* 233 */     if ((paramObject instanceof byte[]))
/* 234 */       localRdnEntry.value = ((byte[])paramObject).clone();
/*     */     else {
/* 236 */       localRdnEntry.value = paramObject;
/*     */     }
/* 238 */     this.entries.add(localRdnEntry);
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   void sort() {
/* 243 */     if (this.entries.size() > 1)
/* 244 */       Collections.sort(this.entries);
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 260 */     return ((RdnEntry)this.entries.get(0)).getValue();
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 278 */     return ((RdnEntry)this.entries.get(0)).getType();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 289 */     StringBuilder localStringBuilder = new StringBuilder();
/* 290 */     int i = this.entries.size();
/* 291 */     if (i > 0) {
/* 292 */       localStringBuilder.append(this.entries.get(0));
/*     */     }
/* 294 */     for (int j = 1; j < i; j++) {
/* 295 */       localStringBuilder.append('+');
/* 296 */       localStringBuilder.append(this.entries.get(j));
/*     */     }
/* 298 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 321 */     if (!(paramObject instanceof Rdn)) {
/* 322 */       throw new ClassCastException("The obj is not a Rdn");
/*     */     }
/* 324 */     if (paramObject == this) {
/* 325 */       return 0;
/*     */     }
/* 327 */     Rdn localRdn = (Rdn)paramObject;
/* 328 */     int i = Math.min(this.entries.size(), localRdn.entries.size());
/* 329 */     for (int j = 0; j < i; j++)
/*     */     {
/* 332 */       int k = ((RdnEntry)this.entries.get(j)).compareTo(localRdn.entries.get(j));
/*     */ 
/* 334 */       if (k != 0) {
/* 335 */         return k;
/*     */       }
/*     */     }
/* 338 */     return this.entries.size() - localRdn.entries.size();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 363 */     if (paramObject == this) {
/* 364 */       return true;
/*     */     }
/* 366 */     if (!(paramObject instanceof Rdn)) {
/* 367 */       return false;
/*     */     }
/* 369 */     Rdn localRdn = (Rdn)paramObject;
/* 370 */     if (this.entries.size() != localRdn.size()) {
/* 371 */       return false;
/*     */     }
/* 373 */     for (int i = 0; i < this.entries.size(); i++) {
/* 374 */       if (!this.entries.get(i).equals(localRdn.entries.get(i))) {
/* 375 */         return false;
/*     */       }
/*     */     }
/* 378 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 392 */     int i = 0;
/*     */ 
/* 395 */     for (int j = 0; j < this.entries.size(); j++) {
/* 396 */       i += this.entries.get(j).hashCode();
/*     */     }
/* 398 */     return i;
/*     */   }
/*     */ 
/*     */   public Attributes toAttributes()
/*     */   {
/* 409 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/* 410 */     for (int i = 0; i < this.entries.size(); i++) {
/* 411 */       RdnEntry localRdnEntry = (RdnEntry)this.entries.get(i);
/* 412 */       Attribute localAttribute = localBasicAttributes.put(localRdnEntry.getType(), localRdnEntry.getValue());
/* 413 */       if (localAttribute != null) {
/* 414 */         localAttribute.add(localRdnEntry.getValue());
/* 415 */         localBasicAttributes.put(localAttribute);
/*     */       }
/*     */     }
/* 418 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 500 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */   public static String escapeValue(Object paramObject)
/*     */   {
/* 519 */     return (paramObject instanceof byte[]) ? escapeBinaryValue((byte[])paramObject) : escapeStringValue((String)paramObject);
/*     */   }
/*     */ 
/*     */   private static String escapeStringValue(String paramString)
/*     */   {
/* 535 */     char[] arrayOfChar = paramString.toCharArray();
/* 536 */     StringBuilder localStringBuilder = new StringBuilder(2 * paramString.length());
/*     */ 
/* 540 */     for (int i = 0; (i < arrayOfChar.length) && 
/* 541 */       (isWhitespace(arrayOfChar[i])); i++);
/* 546 */     for (int j = arrayOfChar.length - 1; (j >= 0) && 
/* 547 */       (isWhitespace(arrayOfChar[j])); j--);
/* 552 */     for (int k = 0; k < arrayOfChar.length; k++) {
/* 553 */       char c = arrayOfChar[k];
/* 554 */       if ((k < i) || (k > j) || (",=+<>#;\"\\".indexOf(c) >= 0)) {
/* 555 */         localStringBuilder.append('\\');
/*     */       }
/* 557 */       localStringBuilder.append(c);
/*     */     }
/* 559 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static String escapeBinaryValue(byte[] paramArrayOfByte)
/*     */   {
/* 570 */     StringBuilder localStringBuilder = new StringBuilder(1 + 2 * paramArrayOfByte.length);
/* 571 */     localStringBuilder.append("#");
/*     */ 
/* 573 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 574 */       int j = paramArrayOfByte[i];
/* 575 */       localStringBuilder.append(Character.forDigit(0xF & j >>> 4, 16));
/* 576 */       localStringBuilder.append(Character.forDigit(0xF & j, 16));
/*     */     }
/* 578 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static Object unescapeValue(String paramString)
/*     */   {
/* 604 */     char[] arrayOfChar = paramString.toCharArray();
/* 605 */     int i = 0;
/* 606 */     int j = arrayOfChar.length;
/*     */ 
/* 609 */     while ((i < j) && (isWhitespace(arrayOfChar[i]))) {
/* 610 */       i++;
/*     */     }
/*     */ 
/* 613 */     while ((i < j) && (isWhitespace(arrayOfChar[(j - 1)]))) {
/* 614 */       j--;
/*     */     }
/*     */ 
/* 620 */     if ((j != arrayOfChar.length) && (i < j) && (arrayOfChar[(j - 1)] == '\\'))
/*     */     {
/* 623 */       j++;
/*     */     }
/* 625 */     if (i >= j) {
/* 626 */       return "";
/*     */     }
/*     */ 
/* 629 */     if (arrayOfChar[i] == '#')
/*     */     {
/* 631 */       return decodeHexPairs(arrayOfChar, ++i, j);
/*     */     }
/*     */ 
/* 635 */     if ((arrayOfChar[i] == '"') && (arrayOfChar[(j - 1)] == '"')) {
/* 636 */       i++;
/* 637 */       j--;
/*     */     }
/*     */ 
/* 640 */     StringBuilder localStringBuilder = new StringBuilder(j - i);
/* 641 */     int k = -1;
/*     */ 
/* 643 */     for (int m = i; m < j; m++) {
/* 644 */       if ((arrayOfChar[m] == '\\') && (m + 1 < j)) {
/* 645 */         if (!Character.isLetterOrDigit(arrayOfChar[(m + 1)])) {
/* 646 */           m++;
/* 647 */           localStringBuilder.append(arrayOfChar[m]);
/* 648 */           k = m;
/*     */         }
/*     */         else
/*     */         {
/* 652 */           byte[] arrayOfByte = getUtf8Octets(arrayOfChar, m, j);
/* 653 */           if (arrayOfByte.length > 0) {
/*     */             try {
/* 655 */               localStringBuilder.append(new String(arrayOfByte, "UTF8"));
/*     */             }
/*     */             catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */             }
/* 659 */             m += arrayOfByte.length * 3 - 1;
/*     */           }
/*     */           else
/*     */           {
/* 663 */             throw new IllegalArgumentException("Not a valid attribute string value:" + paramString + ",improper usage of backslash");
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 669 */         localStringBuilder.append(arrayOfChar[m]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 675 */     m = localStringBuilder.length();
/* 676 */     if ((isWhitespace(localStringBuilder.charAt(m - 1))) && (k != j - 1)) {
/* 677 */       localStringBuilder.setLength(m - 1);
/*     */     }
/* 679 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static byte[] decodeHexPairs(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 689 */     byte[] arrayOfByte = new byte[(paramInt2 - paramInt1) / 2];
/* 690 */     for (int i = 0; paramInt1 + 1 < paramInt2; i++) {
/* 691 */       int j = Character.digit(paramArrayOfChar[paramInt1], 16);
/* 692 */       int k = Character.digit(paramArrayOfChar[(paramInt1 + 1)], 16);
/* 693 */       if ((j < 0) || (k < 0)) {
/*     */         break;
/*     */       }
/* 696 */       arrayOfByte[i] = ((byte)((j << 4) + k));
/* 697 */       paramInt1 += 2;
/*     */     }
/* 699 */     if (paramInt1 != paramInt2) {
/* 700 */       throw new IllegalArgumentException("Illegal attribute value: " + new String(paramArrayOfChar));
/*     */     }
/*     */ 
/* 703 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static byte[] getUtf8Octets(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 715 */     byte[] arrayOfByte1 = new byte[(paramInt2 - paramInt1) / 3];
/* 716 */     int i = 0;
/*     */ 
/* 718 */     while ((paramInt1 + 2 < paramInt2) && (paramArrayOfChar[(paramInt1++)] == '\\'))
/*     */     {
/* 720 */       int j = Character.digit(paramArrayOfChar[(paramInt1++)], 16);
/* 721 */       int k = Character.digit(paramArrayOfChar[(paramInt1++)], 16);
/* 722 */       if ((j < 0) || (k < 0)) {
/*     */         break;
/*     */       }
/* 725 */       arrayOfByte1[(i++)] = ((byte)((j << 4) + k));
/*     */     }
/* 727 */     if (i == arrayOfByte1.length) {
/* 728 */       return arrayOfByte1;
/*     */     }
/* 730 */     byte[] arrayOfByte2 = new byte[i];
/* 731 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 732 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private static boolean isWhitespace(char paramChar)
/*     */   {
/* 740 */     return (paramChar == ' ') || (paramChar == '\r');
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 751 */     paramObjectOutputStream.defaultWriteObject();
/* 752 */     paramObjectOutputStream.writeObject(toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 757 */     paramObjectInputStream.defaultReadObject();
/* 758 */     this.entries = new ArrayList(1);
/* 759 */     String str = (String)paramObjectInputStream.readObject();
/*     */     try {
/* 761 */       new Rfc2253Parser(str).parseRdn(this);
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/* 764 */       throw new StreamCorruptedException("Invalid name: " + str);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RdnEntry
/*     */     implements Comparable
/*     */   {
/*     */     private String type;
/*     */     private Object value;
/* 428 */     private String comparable = null;
/*     */ 
/*     */     String getType() {
/* 431 */       return this.type;
/*     */     }
/*     */ 
/*     */     Object getValue() {
/* 435 */       return this.value;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 442 */       RdnEntry localRdnEntry = (RdnEntry)paramObject;
/*     */ 
/* 444 */       int i = this.type.toUpperCase().compareTo(localRdnEntry.type.toUpperCase());
/*     */ 
/* 446 */       if (i != 0) {
/* 447 */         return i;
/*     */       }
/* 449 */       if (this.value.equals(localRdnEntry.value)) {
/* 450 */         return 0;
/*     */       }
/* 452 */       return getValueComparable().compareTo(localRdnEntry.getValueComparable());
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 457 */       if (paramObject == this) {
/* 458 */         return true;
/*     */       }
/* 460 */       if (!(paramObject instanceof RdnEntry)) {
/* 461 */         return false;
/*     */       }
/*     */ 
/* 465 */       RdnEntry localRdnEntry = (RdnEntry)paramObject;
/* 466 */       return (this.type.equalsIgnoreCase(localRdnEntry.type)) && (getValueComparable().equals(localRdnEntry.getValueComparable()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 472 */       return this.type.toUpperCase().hashCode() + getValueComparable().hashCode();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 477 */       return this.type + "=" + Rdn.escapeValue(this.value);
/*     */     }
/*     */ 
/*     */     private String getValueComparable() {
/* 481 */       if (this.comparable != null) {
/* 482 */         return this.comparable;
/*     */       }
/*     */ 
/* 486 */       if ((this.value instanceof byte[]))
/* 487 */         this.comparable = Rdn.escapeBinaryValue((byte[])this.value);
/*     */       else {
/* 489 */         this.comparable = ((String)this.value).toUpperCase();
/*     */       }
/* 491 */       return this.comparable;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.Rdn
 * JD-Core Version:    0.6.2
 */