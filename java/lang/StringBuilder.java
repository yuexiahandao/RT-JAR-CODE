/*     */ package java.lang;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class StringBuilder extends AbstractStringBuilder
/*     */   implements Serializable, CharSequence
/*     */ {
/*     */   static final long serialVersionUID = 4383685877147921099L;
/*     */ 
/*     */   public StringBuilder()
/*     */   {
/*  85 */     super(16);
/*     */   }
/*     */ 
/*     */   public StringBuilder(int paramInt)
/*     */   {
/*  97 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public StringBuilder(String paramString)
/*     */   {
/* 109 */     super(paramString.length() + 16);
/* 110 */     append(paramString);
/*     */   }
/*     */ 
/*     */   public StringBuilder(CharSequence paramCharSequence)
/*     */   {
/* 123 */     this(paramCharSequence.length() + 16);
/* 124 */     append(paramCharSequence);
/*     */   }
/*     */ 
/*     */   public StringBuilder append(Object paramObject) {
/* 128 */     return append(String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   public StringBuilder append(String paramString) {
/* 132 */     super.append(paramString);
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   private StringBuilder append(StringBuilder paramStringBuilder)
/*     */   {
/* 138 */     if (paramStringBuilder == null)
/* 139 */       return append("null");
/* 140 */     int i = paramStringBuilder.length();
/* 141 */     int j = this.count + i;
/* 142 */     if (j > this.value.length)
/* 143 */       expandCapacity(j);
/* 144 */     paramStringBuilder.getChars(0, i, this.value, this.count);
/* 145 */     this.count = j;
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(StringBuffer paramStringBuffer)
/*     */   {
/* 169 */     super.append(paramStringBuffer);
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(CharSequence paramCharSequence)
/*     */   {
/* 176 */     if (paramCharSequence == null)
/* 177 */       paramCharSequence = "null";
/* 178 */     if ((paramCharSequence instanceof String))
/* 179 */       return append((String)paramCharSequence);
/* 180 */     if ((paramCharSequence instanceof StringBuffer))
/* 181 */       return append((StringBuffer)paramCharSequence);
/* 182 */     if ((paramCharSequence instanceof StringBuilder))
/* 183 */       return append((StringBuilder)paramCharSequence);
/* 184 */     return append(paramCharSequence, 0, paramCharSequence.length());
/*     */   }
/*     */ 
/*     */   public StringBuilder append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*     */   {
/* 191 */     super.append(paramCharSequence, paramInt1, paramInt2);
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(char[] paramArrayOfChar) {
/* 196 */     super.append(paramArrayOfChar);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 204 */     super.append(paramArrayOfChar, paramInt1, paramInt2);
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(boolean paramBoolean) {
/* 209 */     super.append(paramBoolean);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(char paramChar) {
/* 214 */     super.append(paramChar);
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(int paramInt) {
/* 219 */     super.append(paramInt);
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(long paramLong) {
/* 224 */     super.append(paramLong);
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(float paramFloat) {
/* 229 */     super.append(paramFloat);
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder append(double paramDouble) {
/* 234 */     super.append(paramDouble);
/* 235 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder appendCodePoint(int paramInt)
/*     */   {
/* 242 */     super.appendCodePoint(paramInt);
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder delete(int paramInt1, int paramInt2)
/*     */   {
/* 250 */     super.delete(paramInt1, paramInt2);
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder deleteCharAt(int paramInt)
/*     */   {
/* 258 */     super.deleteCharAt(paramInt);
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder replace(int paramInt1, int paramInt2, String paramString)
/*     */   {
/* 266 */     super.replace(paramInt1, paramInt2, paramString);
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
/*     */   {
/* 276 */     super.insert(paramInt1, paramArrayOfChar, paramInt2, paramInt3);
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, Object paramObject)
/*     */   {
/* 284 */     return insert(paramInt, String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, String paramString)
/*     */   {
/* 291 */     super.insert(paramInt, paramString);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, char[] paramArrayOfChar)
/*     */   {
/* 299 */     super.insert(paramInt, paramArrayOfChar);
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, CharSequence paramCharSequence)
/*     */   {
/* 307 */     if (paramCharSequence == null)
/* 308 */       paramCharSequence = "null";
/* 309 */     if ((paramCharSequence instanceof String))
/* 310 */       return insert(paramInt, (String)paramCharSequence);
/* 311 */     return insert(paramInt, paramCharSequence, 0, paramCharSequence.length());
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3)
/*     */   {
/* 320 */     super.insert(paramInt1, paramCharSequence, paramInt2, paramInt3);
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, boolean paramBoolean)
/*     */   {
/* 328 */     super.insert(paramInt, paramBoolean);
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, char paramChar)
/*     */   {
/* 336 */     super.insert(paramInt, paramChar);
/* 337 */     return this;
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt1, int paramInt2)
/*     */   {
/* 344 */     return insert(paramInt1, String.valueOf(paramInt2));
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, long paramLong)
/*     */   {
/* 351 */     return insert(paramInt, String.valueOf(paramLong));
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, float paramFloat)
/*     */   {
/* 358 */     return insert(paramInt, String.valueOf(paramFloat));
/*     */   }
/*     */ 
/*     */   public StringBuilder insert(int paramInt, double paramDouble)
/*     */   {
/* 365 */     return insert(paramInt, String.valueOf(paramDouble));
/*     */   }
/*     */ 
/*     */   public int indexOf(String paramString)
/*     */   {
/* 372 */     return indexOf(paramString, 0);
/*     */   }
/*     */ 
/*     */   public int indexOf(String paramString, int paramInt)
/*     */   {
/* 379 */     return String.indexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(String paramString)
/*     */   {
/* 387 */     return lastIndexOf(paramString, this.count);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(String paramString, int paramInt)
/*     */   {
/* 394 */     return String.lastIndexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public StringBuilder reverse()
/*     */   {
/* 399 */     super.reverse();
/* 400 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 405 */     return new String(this.value, 0, this.count);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 421 */     paramObjectOutputStream.defaultWriteObject();
/* 422 */     paramObjectOutputStream.writeInt(this.count);
/* 423 */     paramObjectOutputStream.writeObject(this.value);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 432 */     paramObjectInputStream.defaultReadObject();
/* 433 */     this.count = paramObjectInputStream.readInt();
/* 434 */     this.value = ((char[])paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.StringBuilder
 * JD-Core Version:    0.6.2
 */