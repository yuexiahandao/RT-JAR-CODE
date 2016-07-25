/*     */ package java.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ChoiceFormat extends NumberFormat
/*     */ {
/*     */   private static final long serialVersionUID = 1795184449645032964L;
/*     */   private double[] choiceLimits;
/*     */   private String[] choiceFormats;
/*     */   static final long SIGN = -9223372036854775808L;
/*     */   static final long EXPONENT = 9218868437227405312L;
/*     */   static final long POSITIVEINFINITY = 9218868437227405312L;
/*     */ 
/*     */   public void applyPattern(String paramString)
/*     */   {
/* 177 */     StringBuffer[] arrayOfStringBuffer = new StringBuffer[2];
/* 178 */     for (int i = 0; i < arrayOfStringBuffer.length; i++) {
/* 179 */       arrayOfStringBuffer[i] = new StringBuffer();
/*     */     }
/* 181 */     double[] arrayOfDouble = new double[30];
/* 182 */     String[] arrayOfString = new String[30];
/* 183 */     int j = 0;
/* 184 */     int k = 0;
/* 185 */     double d1 = 0.0D;
/* 186 */     double d2 = (0.0D / 0.0D);
/* 187 */     int m = 0;
/* 188 */     for (int n = 0; n < paramString.length(); n++) {
/* 189 */       int i1 = paramString.charAt(n);
/* 190 */       if (i1 == 39)
/*     */       {
/* 192 */         if ((n + 1 < paramString.length()) && (paramString.charAt(n + 1) == i1)) {
/* 193 */           arrayOfStringBuffer[k].append(i1);
/* 194 */           n++;
/*     */         } else {
/* 196 */           m = m == 0 ? 1 : 0;
/*     */         }
/* 198 */       } else if (m != 0) {
/* 199 */         arrayOfStringBuffer[k].append(i1);
/* 200 */       } else if ((i1 == 60) || (i1 == 35) || (i1 == 8804)) {
/* 201 */         if (arrayOfStringBuffer[0].length() == 0)
/* 202 */           throw new IllegalArgumentException();
/*     */         try
/*     */         {
/* 205 */           String str = arrayOfStringBuffer[0].toString();
/* 206 */           if (str.equals("∞"))
/* 207 */             d1 = (1.0D / 0.0D);
/* 208 */           else if (str.equals("-∞"))
/* 209 */             d1 = (-1.0D / 0.0D);
/*     */           else
/* 211 */             d1 = Double.valueOf(arrayOfStringBuffer[0].toString()).doubleValue();
/*     */         }
/*     */         catch (Exception localException) {
/* 214 */           throw new IllegalArgumentException();
/*     */         }
/* 216 */         if ((i1 == 60) && (d1 != (1.0D / 0.0D)) && (d1 != (-1.0D / 0.0D)))
/*     */         {
/* 218 */           d1 = nextDouble(d1);
/*     */         }
/* 220 */         if (d1 <= d2) {
/* 221 */           throw new IllegalArgumentException();
/*     */         }
/* 223 */         arrayOfStringBuffer[0].setLength(0);
/* 224 */         k = 1;
/* 225 */       } else if (i1 == 124) {
/* 226 */         if (j == arrayOfDouble.length) {
/* 227 */           arrayOfDouble = doubleArraySize(arrayOfDouble);
/* 228 */           arrayOfString = doubleArraySize(arrayOfString);
/*     */         }
/* 230 */         arrayOfDouble[j] = d1;
/* 231 */         arrayOfString[j] = arrayOfStringBuffer[1].toString();
/* 232 */         j++;
/* 233 */         d2 = d1;
/* 234 */         arrayOfStringBuffer[1].setLength(0);
/* 235 */         k = 0;
/*     */       } else {
/* 237 */         arrayOfStringBuffer[k].append(i1);
/*     */       }
/*     */     }
/*     */ 
/* 241 */     if (k == 1) {
/* 242 */       if (j == arrayOfDouble.length) {
/* 243 */         arrayOfDouble = doubleArraySize(arrayOfDouble);
/* 244 */         arrayOfString = doubleArraySize(arrayOfString);
/*     */       }
/* 246 */       arrayOfDouble[j] = d1;
/* 247 */       arrayOfString[j] = arrayOfStringBuffer[1].toString();
/* 248 */       j++;
/*     */     }
/* 250 */     this.choiceLimits = new double[j];
/* 251 */     System.arraycopy(arrayOfDouble, 0, this.choiceLimits, 0, j);
/* 252 */     this.choiceFormats = new String[j];
/* 253 */     System.arraycopy(arrayOfString, 0, this.choiceFormats, 0, j);
/*     */   }
/*     */ 
/*     */   public String toPattern()
/*     */   {
/* 260 */     StringBuffer localStringBuffer = new StringBuffer();
/* 261 */     for (int i = 0; i < this.choiceLimits.length; i++) {
/* 262 */       if (i != 0) {
/* 263 */         localStringBuffer.append('|');
/*     */       }
/*     */ 
/* 268 */       double d1 = previousDouble(this.choiceLimits[i]);
/* 269 */       double d2 = Math.abs(Math.IEEEremainder(this.choiceLimits[i], 1.0D));
/* 270 */       double d3 = Math.abs(Math.IEEEremainder(d1, 1.0D));
/*     */ 
/* 272 */       if (d2 < d3) {
/* 273 */         localStringBuffer.append("" + this.choiceLimits[i]);
/* 274 */         localStringBuffer.append('#');
/*     */       } else {
/* 276 */         if (this.choiceLimits[i] == (1.0D / 0.0D))
/* 277 */           localStringBuffer.append("∞");
/* 278 */         else if (this.choiceLimits[i] == (-1.0D / 0.0D))
/* 279 */           localStringBuffer.append("-∞");
/*     */         else {
/* 281 */           localStringBuffer.append("" + d1);
/*     */         }
/* 283 */         localStringBuffer.append('<');
/*     */       }
/*     */ 
/* 287 */       String str = this.choiceFormats[i];
/* 288 */       int j = (str.indexOf('<') >= 0) || (str.indexOf('#') >= 0) || (str.indexOf('≤') >= 0) || (str.indexOf('|') >= 0) ? 1 : 0;
/*     */ 
/* 292 */       if (j != 0) localStringBuffer.append('\'');
/* 293 */       if (str.indexOf('\'') < 0) localStringBuffer.append(str);
/*     */       else {
/* 295 */         for (int k = 0; k < str.length(); k++) {
/* 296 */           char c = str.charAt(k);
/* 297 */           localStringBuffer.append(c);
/* 298 */           if (c == '\'') localStringBuffer.append(c);
/*     */         }
/*     */       }
/* 301 */       if (j != 0) localStringBuffer.append('\'');
/*     */     }
/* 303 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public ChoiceFormat(String paramString)
/*     */   {
/* 311 */     applyPattern(paramString);
/*     */   }
/*     */ 
/*     */   public ChoiceFormat(double[] paramArrayOfDouble, String[] paramArrayOfString)
/*     */   {
/* 319 */     setChoices(paramArrayOfDouble, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void setChoices(double[] paramArrayOfDouble, String[] paramArrayOfString)
/*     */   {
/* 337 */     if (paramArrayOfDouble.length != paramArrayOfString.length) {
/* 338 */       throw new IllegalArgumentException("Array and limit arrays must be of the same length.");
/*     */     }
/*     */ 
/* 341 */     this.choiceLimits = paramArrayOfDouble;
/* 342 */     this.choiceFormats = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   public double[] getLimits()
/*     */   {
/* 350 */     return this.choiceLimits;
/*     */   }
/*     */ 
/*     */   public Object[] getFormats()
/*     */   {
/* 358 */     return this.choiceFormats;
/*     */   }
/*     */ 
/*     */   public StringBuffer format(long paramLong, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*     */   {
/* 372 */     return format(paramLong, paramStringBuffer, paramFieldPosition);
/*     */   }
/*     */ 
/*     */   public StringBuffer format(double paramDouble, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*     */   {
/* 385 */     for (int i = 0; (i < this.choiceLimits.length) && 
/* 386 */       (paramDouble >= this.choiceLimits[i]); i++);
/* 391 */     i--;
/* 392 */     if (i < 0) i = 0;
/*     */ 
/* 394 */     return paramStringBuffer.append(this.choiceFormats[i]);
/*     */   }
/*     */ 
/*     */   public Number parse(String paramString, ParsePosition paramParsePosition)
/*     */   {
/* 411 */     int i = paramParsePosition.index;
/* 412 */     int j = i;
/* 413 */     double d1 = (0.0D / 0.0D);
/* 414 */     double d2 = 0.0D;
/* 415 */     for (int k = 0; k < this.choiceFormats.length; k++) {
/* 416 */       String str = this.choiceFormats[k];
/* 417 */       if (paramString.regionMatches(i, str, 0, str.length())) {
/* 418 */         paramParsePosition.index = (i + str.length());
/* 419 */         d2 = this.choiceLimits[k];
/* 420 */         if (paramParsePosition.index > j) {
/* 421 */           j = paramParsePosition.index;
/* 422 */           d1 = d2;
/* 423 */           if (j == paramString.length()) break;
/*     */         }
/*     */       }
/*     */     }
/* 427 */     paramParsePosition.index = j;
/* 428 */     if (paramParsePosition.index == i) {
/* 429 */       paramParsePosition.errorIndex = j;
/*     */     }
/* 431 */     return new Double(d1);
/*     */   }
/*     */ 
/*     */   public static final double nextDouble(double paramDouble)
/*     */   {
/* 441 */     return nextDouble(paramDouble, true);
/*     */   }
/*     */ 
/*     */   public static final double previousDouble(double paramDouble)
/*     */   {
/* 450 */     return nextDouble(paramDouble, false);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 458 */     ChoiceFormat localChoiceFormat = (ChoiceFormat)super.clone();
/*     */ 
/* 460 */     localChoiceFormat.choiceLimits = ((double[])this.choiceLimits.clone());
/* 461 */     localChoiceFormat.choiceFormats = ((String[])this.choiceFormats.clone());
/* 462 */     return localChoiceFormat;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 469 */     int i = this.choiceLimits.length;
/* 470 */     if (this.choiceFormats.length > 0)
/*     */     {
/* 472 */       i ^= this.choiceFormats[(this.choiceFormats.length - 1)].hashCode();
/*     */     }
/* 474 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 481 */     if (paramObject == null) return false;
/* 482 */     if (this == paramObject)
/* 483 */       return true;
/* 484 */     if (getClass() != paramObject.getClass())
/* 485 */       return false;
/* 486 */     ChoiceFormat localChoiceFormat = (ChoiceFormat)paramObject;
/* 487 */     return (Arrays.equals(this.choiceLimits, localChoiceFormat.choiceLimits)) && (Arrays.equals(this.choiceFormats, localChoiceFormat.choiceFormats));
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 497 */     paramObjectInputStream.defaultReadObject();
/* 498 */     if (this.choiceLimits.length != this.choiceFormats.length)
/* 499 */       throw new InvalidObjectException("limits and format arrays of different length.");
/*     */   }
/*     */ 
/*     */   public static double nextDouble(double paramDouble, boolean paramBoolean)
/*     */   {
/* 567 */     if (Double.isNaN(paramDouble)) {
/* 568 */       return paramDouble;
/*     */     }
/*     */ 
/* 572 */     if (paramDouble == 0.0D) {
/* 573 */       double d = Double.longBitsToDouble(1L);
/* 574 */       if (paramBoolean) {
/* 575 */         return d;
/*     */       }
/* 577 */       return -d;
/*     */     }
/*     */ 
/* 584 */     long l1 = Double.doubleToLongBits(paramDouble);
/*     */ 
/* 587 */     long l2 = l1 & 0xFFFFFFFF;
/*     */ 
/* 590 */     if (l1 > 0L == paramBoolean) {
/* 591 */       if (l2 != 9218868437227405312L) {
/* 592 */         l2 += 1L;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 597 */       l2 -= 1L;
/*     */     }
/*     */ 
/* 601 */     long l3 = l1 & 0x0;
/* 602 */     return Double.longBitsToDouble(l2 | l3);
/*     */   }
/*     */ 
/*     */   private static double[] doubleArraySize(double[] paramArrayOfDouble) {
/* 606 */     int i = paramArrayOfDouble.length;
/* 607 */     double[] arrayOfDouble = new double[i * 2];
/* 608 */     System.arraycopy(paramArrayOfDouble, 0, arrayOfDouble, 0, i);
/* 609 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */   private String[] doubleArraySize(String[] paramArrayOfString) {
/* 613 */     int i = paramArrayOfString.length;
/* 614 */     String[] arrayOfString = new String[i * 2];
/* 615 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, i);
/* 616 */     return arrayOfString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.ChoiceFormat
 * JD-Core Version:    0.6.2
 */