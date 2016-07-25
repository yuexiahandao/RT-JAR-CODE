/*     */ package java.lang;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class StringBuffer extends AbstractStringBuilder
/*     */   implements Serializable, CharSequence
/*     */ {
/*     */   static final long serialVersionUID = 3388685877147921107L;
/* 575 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("value", [C.class), new ObjectStreamField("count", Integer.TYPE), new ObjectStreamField("shared", Boolean.TYPE) };
/*     */ 
/*     */   public StringBuffer()
/*     */   {
/*  96 */     super(16);
/*     */   }
/*     */ 
/*     */   public StringBuffer(int paramInt)
/*     */   {
/* 108 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public StringBuffer(String paramString)
/*     */   {
/* 120 */     super(paramString.length() + 16);
/* 121 */     append(paramString);
/*     */   }
/*     */ 
/*     */   public StringBuffer(CharSequence paramCharSequence)
/*     */   {
/* 139 */     this(paramCharSequence.length() + 16);
/* 140 */     append(paramCharSequence);
/*     */   }
/*     */ 
/*     */   public synchronized int length() {
/* 144 */     return this.count;
/*     */   }
/*     */ 
/*     */   public synchronized int capacity() {
/* 148 */     return this.value.length;
/*     */   }
/*     */ 
/*     */   public synchronized void ensureCapacity(int paramInt)
/*     */   {
/* 153 */     if (paramInt > this.value.length)
/* 154 */       expandCapacity(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void trimToSize()
/*     */   {
/* 162 */     super.trimToSize();
/*     */   }
/*     */ 
/*     */   public synchronized void setLength(int paramInt)
/*     */   {
/* 170 */     super.setLength(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized char charAt(int paramInt)
/*     */   {
/* 178 */     if ((paramInt < 0) || (paramInt >= this.count))
/* 179 */       throw new StringIndexOutOfBoundsException(paramInt);
/* 180 */     return this.value[paramInt];
/*     */   }
/*     */ 
/*     */   public synchronized int codePointAt(int paramInt)
/*     */   {
/* 187 */     return super.codePointAt(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized int codePointBefore(int paramInt)
/*     */   {
/* 194 */     return super.codePointBefore(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized int codePointCount(int paramInt1, int paramInt2)
/*     */   {
/* 201 */     return super.codePointCount(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized int offsetByCodePoints(int paramInt1, int paramInt2)
/*     */   {
/* 208 */     return super.offsetByCodePoints(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*     */   {
/* 218 */     super.getChars(paramInt1, paramInt2, paramArrayOfChar, paramInt3);
/*     */   }
/*     */ 
/*     */   public synchronized void setCharAt(int paramInt, char paramChar)
/*     */   {
/* 226 */     if ((paramInt < 0) || (paramInt >= this.count))
/* 227 */       throw new StringIndexOutOfBoundsException(paramInt);
/* 228 */     this.value[paramInt] = paramChar;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(Object paramObject) {
/* 232 */     super.append(String.valueOf(paramObject));
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(String paramString) {
/* 237 */     super.append(paramString);
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(StringBuffer paramStringBuffer)
/*     */   {
/* 266 */     super.append(paramStringBuffer);
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuffer append(CharSequence paramCharSequence)
/*     */   {
/* 294 */     if (paramCharSequence == null)
/* 295 */       paramCharSequence = "null";
/* 296 */     if ((paramCharSequence instanceof String))
/* 297 */       return append((String)paramCharSequence);
/* 298 */     if ((paramCharSequence instanceof StringBuffer))
/* 299 */       return append((StringBuffer)paramCharSequence);
/* 300 */     return append(paramCharSequence, 0, paramCharSequence.length());
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*     */   {
/* 309 */     super.append(paramCharSequence, paramInt1, paramInt2);
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(char[] paramArrayOfChar) {
/* 314 */     super.append(paramArrayOfChar);
/* 315 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 322 */     super.append(paramArrayOfChar, paramInt1, paramInt2);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(boolean paramBoolean) {
/* 327 */     super.append(paramBoolean);
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(char paramChar) {
/* 332 */     super.append(paramChar);
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(int paramInt) {
/* 337 */     super.append(paramInt);
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer appendCodePoint(int paramInt)
/*     */   {
/* 345 */     super.appendCodePoint(paramInt);
/* 346 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(long paramLong) {
/* 350 */     super.append(paramLong);
/* 351 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(float paramFloat) {
/* 355 */     super.append(paramFloat);
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer append(double paramDouble) {
/* 360 */     super.append(paramDouble);
/* 361 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer delete(int paramInt1, int paramInt2)
/*     */   {
/* 369 */     super.delete(paramInt1, paramInt2);
/* 370 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer deleteCharAt(int paramInt)
/*     */   {
/* 378 */     super.deleteCharAt(paramInt);
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer replace(int paramInt1, int paramInt2, String paramString)
/*     */   {
/* 387 */     super.replace(paramInt1, paramInt2, paramString);
/* 388 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized String substring(int paramInt)
/*     */   {
/* 396 */     return substring(paramInt, this.count);
/*     */   }
/*     */ 
/*     */   public synchronized CharSequence subSequence(int paramInt1, int paramInt2)
/*     */   {
/* 404 */     return super.substring(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized String substring(int paramInt1, int paramInt2)
/*     */   {
/* 412 */     return super.substring(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
/*     */   {
/* 422 */     super.insert(paramInt1, paramArrayOfChar, paramInt2, paramInt3);
/* 423 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt, Object paramObject)
/*     */   {
/* 430 */     super.insert(paramInt, String.valueOf(paramObject));
/* 431 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt, String paramString)
/*     */   {
/* 438 */     super.insert(paramInt, paramString);
/* 439 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt, char[] paramArrayOfChar)
/*     */   {
/* 446 */     super.insert(paramInt, paramArrayOfChar);
/* 447 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt, CharSequence paramCharSequence)
/*     */   {
/* 456 */     if (paramCharSequence == null)
/* 457 */       paramCharSequence = "null";
/* 458 */     if ((paramCharSequence instanceof String))
/* 459 */       return insert(paramInt, (String)paramCharSequence);
/* 460 */     return insert(paramInt, paramCharSequence, 0, paramCharSequence.length());
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3)
/*     */   {
/* 470 */     super.insert(paramInt1, paramCharSequence, paramInt2, paramInt3);
/* 471 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt, boolean paramBoolean)
/*     */   {
/* 478 */     return insert(paramInt, String.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer insert(int paramInt, char paramChar)
/*     */   {
/* 485 */     super.insert(paramInt, paramChar);
/* 486 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt1, int paramInt2)
/*     */   {
/* 493 */     return insert(paramInt1, String.valueOf(paramInt2));
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt, long paramLong)
/*     */   {
/* 500 */     return insert(paramInt, String.valueOf(paramLong));
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt, float paramFloat)
/*     */   {
/* 507 */     return insert(paramInt, String.valueOf(paramFloat));
/*     */   }
/*     */ 
/*     */   public StringBuffer insert(int paramInt, double paramDouble)
/*     */   {
/* 514 */     return insert(paramInt, String.valueOf(paramDouble));
/*     */   }
/*     */ 
/*     */   public int indexOf(String paramString)
/*     */   {
/* 522 */     return indexOf(paramString, 0);
/*     */   }
/*     */ 
/*     */   public synchronized int indexOf(String paramString, int paramInt)
/*     */   {
/* 530 */     return String.indexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(String paramString)
/*     */   {
/* 540 */     return lastIndexOf(paramString, this.count);
/*     */   }
/*     */ 
/*     */   public synchronized int lastIndexOf(String paramString, int paramInt)
/*     */   {
/* 548 */     return String.lastIndexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized StringBuffer reverse()
/*     */   {
/* 556 */     super.reverse();
/* 557 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized String toString() {
/* 561 */     return new String(this.value, 0, this.count);
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 588 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 589 */     localPutField.put("value", this.value);
/* 590 */     localPutField.put("count", this.count);
/* 591 */     localPutField.put("shared", false);
/* 592 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 601 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 602 */     this.value = ((char[])localGetField.get("value", null));
/* 603 */     this.count = localGetField.get("count", 0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.StringBuffer
 * JD-Core Version:    0.6.2
 */