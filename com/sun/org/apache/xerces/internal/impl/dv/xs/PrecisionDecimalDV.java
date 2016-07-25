/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ 
/*     */ class PrecisionDecimalDV extends TypeValidator
/*     */ {
/*     */   public short getAllowedFacets()
/*     */   {
/* 409 */     return 4088; } 
/*     */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException { // Byte code:
/*     */     //   0: new 43	com/sun/org/apache/xerces/internal/impl/dv/xs/PrecisionDecimalDV$XPrecisionDecimal
/*     */     //   3: dup
/*     */     //   4: aload_1
/*     */     //   5: invokespecial 69	com/sun/org/apache/xerces/internal/impl/dv/xs/PrecisionDecimalDV$XPrecisionDecimal:<init>	(Ljava/lang/String;)V
/*     */     //   8: areturn
/*     */     //   9: astore_3
/*     */     //   10: new 41	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   13: dup
/*     */     //   14: ldc 1
/*     */     //   16: iconst_2
/*     */     //   17: anewarray 46	java/lang/Object
/*     */     //   20: dup
/*     */     //   21: iconst_0
/*     */     //   22: aload_1
/*     */     //   23: aastore
/*     */     //   24: dup
/*     */     //   25: iconst_1
/*     */     //   26: ldc 2
/*     */     //   28: aastore
/*     */     //   29: invokespecial 66	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   32: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	9	java/lang/NumberFormatException } 
/* 427 */   public int compare(Object value1, Object value2) { return ((XPrecisionDecimal)value1).compareTo((XPrecisionDecimal)value2); }
/*     */ 
/*     */ 
/*     */   public int getFractionDigits(Object value)
/*     */   {
/* 432 */     return ((XPrecisionDecimal)value).fracDigits;
/*     */   }
/*     */ 
/*     */   public int getTotalDigits(Object value)
/*     */   {
/* 437 */     return ((XPrecisionDecimal)value).totalDigits;
/*     */   }
/*     */ 
/*     */   public boolean isIdentical(Object value1, Object value2)
/*     */   {
/* 442 */     if ((!(value2 instanceof XPrecisionDecimal)) || (!(value1 instanceof XPrecisionDecimal)))
/* 443 */       return false;
/* 444 */     return ((XPrecisionDecimal)value1).isIdentical((XPrecisionDecimal)value2);
/*     */   }
/*     */ 
/*     */   static final class XPrecisionDecimal
/*     */   {
/*  38 */     int sign = 1;
/*     */ 
/*  40 */     int totalDigits = 0;
/*     */ 
/*  42 */     int intDigits = 0;
/*     */ 
/*  44 */     int fracDigits = 0;
/*     */ 
/*  48 */     String ivalue = "";
/*     */ 
/*  50 */     String fvalue = "";
/*     */ 
/*  52 */     int pvalue = 0;
/*     */     private String canonical;
/*     */ 
/*     */     XPrecisionDecimal(String content)
/*     */       throws NumberFormatException
/*     */     {
/*  56 */       if (content.equals("NaN")) {
/*  57 */         this.ivalue = content;
/*  58 */         this.sign = 0;
/*     */       }
/*  60 */       if ((content.equals("+INF")) || (content.equals("INF")) || (content.equals("-INF"))) {
/*  61 */         this.ivalue = (content.charAt(0) == '+' ? content.substring(1) : content);
/*  62 */         return;
/*     */       }
/*  64 */       initD(content);
/*     */     }
/*     */ 
/*     */     void initD(String content) throws NumberFormatException {
/*  68 */       int len = content.length();
/*  69 */       if (len == 0) {
/*  70 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/*  74 */       int intStart = 0; int intEnd = 0; int fracStart = 0; int fracEnd = 0;
/*     */ 
/*  77 */       if (content.charAt(0) == '+')
/*     */       {
/*  79 */         intStart = 1;
/*     */       }
/*  81 */       else if (content.charAt(0) == '-') {
/*  82 */         intStart = 1;
/*  83 */         this.sign = -1;
/*     */       }
/*     */ 
/*  87 */       int actualIntStart = intStart;
/*  88 */       while ((actualIntStart < len) && (content.charAt(actualIntStart) == '0')) {
/*  89 */         actualIntStart++;
/*     */       }
/*     */ 
/*  93 */       for (intEnd = actualIntStart; (intEnd < len) && (TypeValidator.isDigit(content.charAt(intEnd))); intEnd++);
/*  96 */       if (intEnd < len)
/*     */       {
/*  98 */         if ((content.charAt(intEnd) != '.') && (content.charAt(intEnd) != 'E') && (content.charAt(intEnd) != 'e')) {
/*  99 */           throw new NumberFormatException();
/*     */         }
/* 101 */         if (content.charAt(intEnd) == '.')
/*     */         {
/* 103 */           fracStart = intEnd + 1;
/*     */ 
/* 107 */           fracEnd = fracStart;
/* 108 */           while ((fracEnd < len) && (TypeValidator.isDigit(content.charAt(fracEnd)))) {
/* 109 */             fracEnd++;
/*     */           }
/*     */         }
/* 112 */         this.pvalue = Integer.parseInt(content.substring(intEnd + 1, len));
/*     */       }
/*     */ 
/* 117 */       if ((intStart == intEnd) && (fracStart == fracEnd)) {
/* 118 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 126 */       for (int fracPos = fracStart; fracPos < fracEnd; fracPos++) {
/* 127 */         if (!TypeValidator.isDigit(content.charAt(fracPos))) {
/* 128 */           throw new NumberFormatException();
/*     */         }
/*     */       }
/* 131 */       this.intDigits = (intEnd - actualIntStart);
/* 132 */       this.fracDigits = (fracEnd - fracStart);
/*     */ 
/* 134 */       if (this.intDigits > 0) {
/* 135 */         this.ivalue = content.substring(actualIntStart, intEnd);
/*     */       }
/*     */ 
/* 138 */       if (this.fracDigits > 0) {
/* 139 */         this.fvalue = content.substring(fracStart, fracEnd);
/* 140 */         if (fracEnd < len) {
/* 141 */           this.pvalue = Integer.parseInt(content.substring(fracEnd + 1, len));
/*     */         }
/*     */       }
/* 144 */       this.totalDigits = (this.intDigits + this.fracDigits);
/*     */     }
/*     */ 
/*     */     private static String canonicalToStringForHashCode(String ivalue, String fvalue, int sign, int pvalue)
/*     */     {
/* 154 */       if ("NaN".equals(ivalue)) {
/* 155 */         return "NaN";
/*     */       }
/* 157 */       if ("INF".equals(ivalue)) {
/* 158 */         return sign < 0 ? "-INF" : "INF";
/*     */       }
/* 160 */       StringBuilder builder = new StringBuilder();
/* 161 */       int ilen = ivalue.length();
/* 162 */       int flen0 = fvalue.length();
/*     */ 
/* 164 */       for (int lastNonZero = flen0; (lastNonZero > 0) && 
/* 165 */         (fvalue.charAt(lastNonZero - 1) == '0'); lastNonZero--);
/* 167 */       int flen = lastNonZero;
/*     */ 
/* 169 */       int exponent = pvalue;
/* 170 */       for (int iStart = 0; (iStart < ilen) && 
/* 171 */         (ivalue.charAt(iStart) == '0'); iStart++);
/* 173 */       int fStart = 0;
/* 174 */       if (iStart < ivalue.length()) {
/* 175 */         builder.append(sign == -1 ? "-" : "");
/* 176 */         builder.append(ivalue.charAt(iStart));
/* 177 */         iStart++;
/*     */       }
/* 179 */       else if (flen > 0) {
/* 180 */         for (fStart = 0; (fStart < flen) && 
/* 181 */           (fvalue.charAt(fStart) == '0'); fStart++);
/* 183 */         if (fStart < flen) {
/* 184 */           builder.append(sign == -1 ? "-" : "");
/* 185 */           builder.append(fvalue.charAt(fStart));
/* 186 */           exponent -= ++fStart;
/*     */         } else {
/* 188 */           return "0";
/*     */         }
/*     */       } else {
/* 191 */         return "0";
/*     */       }
/*     */ 
/* 195 */       if ((iStart < ilen) || (fStart < flen)) {
/* 196 */         builder.append('.');
/*     */       }
/* 198 */       while (iStart < ilen) {
/* 199 */         builder.append(ivalue.charAt(iStart++));
/* 200 */         exponent++;
/*     */       }
/* 202 */       while (fStart < flen) {
/* 203 */         builder.append(fvalue.charAt(fStart++));
/*     */       }
/* 205 */       if (exponent != 0) {
/* 206 */         builder.append("E").append(exponent);
/*     */       }
/* 208 */       return builder.toString();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object val)
/*     */     {
/* 213 */       if (val == this) {
/* 214 */         return true;
/*     */       }
/* 216 */       if (!(val instanceof XPrecisionDecimal))
/* 217 */         return false;
/* 218 */       XPrecisionDecimal oval = (XPrecisionDecimal)val;
/*     */ 
/* 220 */       return compareTo(oval) == 0;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 234 */       return canonicalToStringForHashCode(this.ivalue, this.fvalue, this.sign, this.pvalue).hashCode();
/*     */     }
/*     */ 
/*     */     private int compareFractionalPart(XPrecisionDecimal oval)
/*     */     {
/* 241 */       if (this.fvalue.equals(oval.fvalue)) {
/* 242 */         return 0;
/*     */       }
/* 244 */       StringBuffer temp1 = new StringBuffer(this.fvalue);
/* 245 */       StringBuffer temp2 = new StringBuffer(oval.fvalue);
/*     */ 
/* 247 */       truncateTrailingZeros(temp1, temp2);
/* 248 */       return temp1.toString().compareTo(temp2.toString());
/*     */     }
/*     */ 
/*     */     private void truncateTrailingZeros(StringBuffer fValue, StringBuffer otherFValue) {
/* 252 */       for (int i = fValue.length() - 1; (i >= 0) && 
/* 253 */         (fValue.charAt(i) == '0'); i--)
/*     */       {
/* 254 */         fValue.deleteCharAt(i);
/*     */       }
/*     */ 
/* 258 */       for (int i = otherFValue.length() - 1; (i >= 0) && 
/* 259 */         (otherFValue.charAt(i) == '0'); i--)
/*     */       {
/* 260 */         otherFValue.deleteCharAt(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int compareTo(XPrecisionDecimal val)
/*     */     {
/* 268 */       if (this.sign == 0) {
/* 269 */         return 2;
/*     */       }
/*     */ 
/* 272 */       if ((this.ivalue.equals("INF")) || (val.ivalue.equals("INF"))) {
/* 273 */         if (this.ivalue.equals(val.ivalue))
/* 274 */           return 0;
/* 275 */         if (this.ivalue.equals("INF"))
/* 276 */           return 1;
/* 277 */         return -1;
/*     */       }
/*     */ 
/* 281 */       if ((this.ivalue.equals("-INF")) || (val.ivalue.equals("-INF"))) {
/* 282 */         if (this.ivalue.equals(val.ivalue))
/* 283 */           return 0;
/* 284 */         if (this.ivalue.equals("-INF"))
/* 285 */           return -1;
/* 286 */         return 1;
/*     */       }
/*     */ 
/* 289 */       if (this.sign != val.sign) {
/* 290 */         return this.sign > val.sign ? 1 : -1;
/*     */       }
/* 292 */       return this.sign * compare(val);
/*     */     }
/*     */ 
/*     */     private int compare(XPrecisionDecimal val)
/*     */     {
/* 299 */       if ((this.pvalue != 0) || (val.pvalue != 0)) {
/* 300 */         if (this.pvalue == val.pvalue) {
/* 301 */           return intComp(val);
/*     */         }
/*     */ 
/* 304 */         if (this.intDigits + this.pvalue != val.intDigits + val.pvalue) {
/* 305 */           return this.intDigits + this.pvalue > val.intDigits + val.pvalue ? 1 : -1;
/*     */         }
/*     */ 
/* 308 */         if (this.pvalue > val.pvalue) {
/* 309 */           int expDiff = this.pvalue - val.pvalue;
/* 310 */           StringBuffer buffer = new StringBuffer(this.ivalue);
/* 311 */           StringBuffer fbuffer = new StringBuffer(this.fvalue);
/* 312 */           for (int i = 0; i < expDiff; i++)
/* 313 */             if (i < this.fracDigits) {
/* 314 */               buffer.append(this.fvalue.charAt(i));
/* 315 */               fbuffer.deleteCharAt(i);
/*     */             }
/*     */             else {
/* 318 */               buffer.append('0');
/*     */             }
/* 320 */           return compareDecimal(buffer.toString(), val.ivalue, fbuffer.toString(), val.fvalue);
/*     */         }
/*     */ 
/* 323 */         int expDiff = val.pvalue - this.pvalue;
/* 324 */         StringBuffer buffer = new StringBuffer(val.ivalue);
/* 325 */         StringBuffer fbuffer = new StringBuffer(val.fvalue);
/* 326 */         for (int i = 0; i < expDiff; i++)
/* 327 */           if (i < val.fracDigits) {
/* 328 */             buffer.append(val.fvalue.charAt(i));
/* 329 */             fbuffer.deleteCharAt(i);
/*     */           }
/*     */           else {
/* 332 */             buffer.append('0');
/*     */           }
/* 334 */         return compareDecimal(this.ivalue, buffer.toString(), this.fvalue, fbuffer.toString());
/*     */       }
/*     */ 
/* 339 */       return intComp(val);
/*     */     }
/*     */ 
/*     */     private int intComp(XPrecisionDecimal val)
/*     */     {
/* 348 */       if (this.intDigits != val.intDigits) {
/* 349 */         return this.intDigits > val.intDigits ? 1 : -1;
/*     */       }
/* 351 */       return compareDecimal(this.ivalue, val.ivalue, this.fvalue, val.fvalue);
/*     */     }
/*     */ 
/*     */     private int compareDecimal(String iValue, String fValue, String otherIValue, String otherFValue)
/*     */     {
/* 359 */       int ret = iValue.compareTo(otherIValue);
/* 360 */       if (ret != 0) {
/* 361 */         return ret > 0 ? 1 : -1;
/*     */       }
/* 363 */       if (fValue.equals(otherFValue)) {
/* 364 */         return 0;
/*     */       }
/* 366 */       StringBuffer temp1 = new StringBuffer(fValue);
/* 367 */       StringBuffer temp2 = new StringBuffer(otherFValue);
/*     */ 
/* 369 */       truncateTrailingZeros(temp1, temp2);
/* 370 */       ret = temp1.toString().compareTo(temp2.toString());
/* 371 */       return ret > 0 ? 1 : ret == 0 ? 0 : -1;
/*     */     }
/*     */ 
/*     */     public synchronized String toString()
/*     */     {
/* 378 */       if (this.canonical == null) {
/* 379 */         makeCanonical();
/*     */       }
/* 381 */       return this.canonical;
/*     */     }
/*     */ 
/*     */     private void makeCanonical()
/*     */     {
/* 386 */       this.canonical = "TBD by Working Group";
/*     */     }
/*     */ 
/*     */     public boolean isIdentical(XPrecisionDecimal decimal)
/*     */     {
/* 394 */       if ((this.ivalue.equals(decimal.ivalue)) && ((this.ivalue.equals("INF")) || (this.ivalue.equals("-INF")) || (this.ivalue.equals("NaN")))) {
/* 395 */         return true;
/*     */       }
/* 397 */       if ((this.sign == decimal.sign) && (this.intDigits == decimal.intDigits) && (this.fracDigits == decimal.fracDigits) && (this.pvalue == decimal.pvalue) && (this.ivalue.equals(decimal.ivalue)) && (this.fvalue.equals(decimal.fvalue)))
/*     */       {
/* 399 */         return true;
/* 400 */       }return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.PrecisionDecimalDV
 * JD-Core Version:    0.6.2
 */