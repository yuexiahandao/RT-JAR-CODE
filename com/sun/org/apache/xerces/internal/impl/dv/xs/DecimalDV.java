/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.utils.Objects;
/*     */ import com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class DecimalDV extends TypeValidator
/*     */ {
/*     */   public final short getAllowedFacets()
/*     */   {
/*  44 */     return 4088; } 
/*     */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException { // Byte code:
/*     */     //   0: new 42	com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV$XDecimal
/*     */     //   3: dup
/*     */     //   4: aload_1
/*     */     //   5: invokespecial 64	com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV$XDecimal:<init>	(Ljava/lang/String;)V
/*     */     //   8: areturn
/*     */     //   9: astore_3
/*     */     //   10: new 40	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException
/*     */     //   13: dup
/*     */     //   14: ldc 1
/*     */     //   16: iconst_2
/*     */     //   17: anewarray 45	java/lang/Object
/*     */     //   20: dup
/*     */     //   21: iconst_0
/*     */     //   22: aload_1
/*     */     //   23: aastore
/*     */     //   24: dup
/*     */     //   25: iconst_1
/*     */     //   26: ldc 2
/*     */     //   28: aastore
/*     */     //   29: invokespecial 62	com/sun/org/apache/xerces/internal/impl/dv/InvalidDatatypeValueException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   32: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	9	java/lang/NumberFormatException } 
/*  58 */   public final int compare(Object value1, Object value2) { return ((XDecimal)value1).compareTo((XDecimal)value2); }
/*     */ 
/*     */ 
/*     */   public final int getTotalDigits(Object value)
/*     */   {
/*  63 */     return ((XDecimal)value).totalDigits;
/*     */   }
/*     */ 
/*     */   public final int getFractionDigits(Object value)
/*     */   {
/*  68 */     return ((XDecimal)value).fracDigits; } 
/*  74 */   static final class XDecimal implements XSDecimal { int sign = 1;
/*     */ 
/*  76 */     int totalDigits = 0;
/*     */ 
/*  78 */     int intDigits = 0;
/*     */ 
/*  80 */     int fracDigits = 0;
/*     */ 
/*  82 */     String ivalue = "";
/*     */ 
/*  84 */     String fvalue = "";
/*     */ 
/*  86 */     boolean integer = false;
/*     */     private String canonical;
/*     */ 
/*  89 */     XDecimal(String content) throws NumberFormatException { initD(content); }
/*     */ 
/*     */     XDecimal(String content, boolean integer) throws NumberFormatException {
/*  92 */       if (integer)
/*  93 */         initI(content);
/*     */       else
/*  95 */         initD(content); 
/*     */     }
/*     */ 
/*  98 */     void initD(String content) throws NumberFormatException { int len = content.length();
/*  99 */       if (len == 0) {
/* 100 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 104 */       int intStart = 0; int intEnd = 0; int fracStart = 0; int fracEnd = 0;
/*     */ 
/* 107 */       if (content.charAt(0) == '+')
/*     */       {
/* 109 */         intStart = 1;
/*     */       }
/* 111 */       else if (content.charAt(0) == '-')
/*     */       {
/* 113 */         intStart = 1;
/* 114 */         this.sign = -1;
/*     */       }
/*     */ 
/* 118 */       int actualIntStart = intStart;
/* 119 */       while ((actualIntStart < len) && (content.charAt(actualIntStart) == '0')) {
/* 120 */         actualIntStart++;
/*     */       }
/*     */ 
/* 124 */       intEnd = actualIntStart;
/* 125 */       while ((intEnd < len) && (TypeValidator.isDigit(content.charAt(intEnd)))) {
/* 126 */         intEnd++;
/*     */       }
/*     */ 
/* 129 */       if (intEnd < len)
/*     */       {
/* 131 */         if (content.charAt(intEnd) != '.') {
/* 132 */           throw new NumberFormatException();
/*     */         }
/*     */ 
/* 135 */         fracStart = intEnd + 1;
/* 136 */         fracEnd = len;
/*     */       }
/*     */ 
/* 140 */       if ((intStart == intEnd) && (fracStart == fracEnd)) {
/* 141 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 144 */       while ((fracEnd > fracStart) && (content.charAt(fracEnd - 1) == '0')) {
/* 145 */         fracEnd--;
/*     */       }
/*     */ 
/* 149 */       for (int fracPos = fracStart; fracPos < fracEnd; fracPos++) {
/* 150 */         if (!TypeValidator.isDigit(content.charAt(fracPos))) {
/* 151 */           throw new NumberFormatException();
/*     */         }
/*     */       }
/* 154 */       this.intDigits = (intEnd - actualIntStart);
/* 155 */       this.fracDigits = (fracEnd - fracStart);
/* 156 */       this.totalDigits = (this.intDigits + this.fracDigits);
/*     */ 
/* 158 */       if (this.intDigits > 0) {
/* 159 */         this.ivalue = content.substring(actualIntStart, intEnd);
/* 160 */         if (this.fracDigits > 0) {
/* 161 */           this.fvalue = content.substring(fracStart, fracEnd);
/*     */         }
/*     */       }
/* 164 */       else if (this.fracDigits > 0) {
/* 165 */         this.fvalue = content.substring(fracStart, fracEnd);
/*     */       }
/*     */       else
/*     */       {
/* 169 */         this.sign = 0;
/*     */       } }
/*     */ 
/*     */     void initI(String content) throws NumberFormatException
/*     */     {
/* 174 */       int len = content.length();
/* 175 */       if (len == 0) {
/* 176 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 179 */       int intStart = 0; int intEnd = 0;
/*     */ 
/* 182 */       if (content.charAt(0) == '+')
/*     */       {
/* 184 */         intStart = 1;
/*     */       }
/* 186 */       else if (content.charAt(0) == '-')
/*     */       {
/* 188 */         intStart = 1;
/* 189 */         this.sign = -1;
/*     */       }
/*     */ 
/* 193 */       int actualIntStart = intStart;
/* 194 */       while ((actualIntStart < len) && (content.charAt(actualIntStart) == '0')) {
/* 195 */         actualIntStart++;
/*     */       }
/*     */ 
/* 199 */       intEnd = actualIntStart;
/* 200 */       while ((intEnd < len) && (TypeValidator.isDigit(content.charAt(intEnd)))) {
/* 201 */         intEnd++;
/*     */       }
/*     */ 
/* 204 */       if (intEnd < len) {
/* 205 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 208 */       if (intStart == intEnd) {
/* 209 */         throw new NumberFormatException();
/*     */       }
/* 211 */       this.intDigits = (intEnd - actualIntStart);
/* 212 */       this.fracDigits = 0;
/* 213 */       this.totalDigits = this.intDigits;
/*     */ 
/* 215 */       if (this.intDigits > 0) {
/* 216 */         this.ivalue = content.substring(actualIntStart, intEnd);
/*     */       }
/*     */       else
/*     */       {
/* 220 */         this.sign = 0;
/*     */       }
/*     */ 
/* 223 */       this.integer = true;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object val)
/*     */     {
/* 228 */       if (val == this) {
/* 229 */         return true;
/*     */       }
/* 231 */       if (!(val instanceof XDecimal))
/* 232 */         return false;
/* 233 */       XDecimal oval = (XDecimal)val;
/*     */ 
/* 235 */       if (this.sign != oval.sign)
/* 236 */         return false;
/* 237 */       if (this.sign == 0) {
/* 238 */         return true;
/*     */       }
/* 240 */       return (this.intDigits == oval.intDigits) && (this.fracDigits == oval.fracDigits) && (this.ivalue.equals(oval.ivalue)) && (this.fvalue.equals(oval.fvalue));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 246 */       int hash = 7;
/* 247 */       hash = 17 * hash + this.sign;
/* 248 */       if (this.sign == 0) return hash;
/* 249 */       hash = 17 * hash + this.intDigits;
/* 250 */       hash = 17 * hash + this.fracDigits;
/* 251 */       hash = 17 * hash + Objects.hashCode(this.ivalue);
/* 252 */       hash = 17 * hash + Objects.hashCode(this.fvalue);
/* 253 */       return hash;
/*     */     }
/*     */ 
/*     */     public int compareTo(XDecimal val) {
/* 257 */       if (this.sign != val.sign)
/* 258 */         return this.sign > val.sign ? 1 : -1;
/* 259 */       if (this.sign == 0)
/* 260 */         return 0;
/* 261 */       return this.sign * intComp(val);
/*     */     }
/*     */     private int intComp(XDecimal val) {
/* 264 */       if (this.intDigits != val.intDigits)
/* 265 */         return this.intDigits > val.intDigits ? 1 : -1;
/* 266 */       int ret = this.ivalue.compareTo(val.ivalue);
/* 267 */       if (ret != 0)
/* 268 */         return ret > 0 ? 1 : -1;
/* 269 */       ret = this.fvalue.compareTo(val.fvalue);
/* 270 */       return ret > 0 ? 1 : ret == 0 ? 0 : -1;
/*     */     }
/*     */ 
/*     */     public synchronized String toString()
/*     */     {
/* 276 */       if (this.canonical == null) {
/* 277 */         makeCanonical();
/*     */       }
/* 279 */       return this.canonical;
/*     */     }
/*     */ 
/*     */     private void makeCanonical() {
/* 283 */       if (this.sign == 0) {
/* 284 */         if (this.integer)
/* 285 */           this.canonical = "0";
/*     */         else
/* 287 */           this.canonical = "0.0";
/* 288 */         return;
/*     */       }
/* 290 */       if ((this.integer) && (this.sign > 0)) {
/* 291 */         this.canonical = this.ivalue;
/* 292 */         return;
/*     */       }
/*     */ 
/* 295 */       StringBuilder buffer = new StringBuilder(this.totalDigits + 3);
/* 296 */       if (this.sign == -1)
/* 297 */         buffer.append('-');
/* 298 */       if (this.intDigits != 0)
/* 299 */         buffer.append(this.ivalue);
/*     */       else
/* 301 */         buffer.append('0');
/* 302 */       if (!this.integer) {
/* 303 */         buffer.append('.');
/* 304 */         if (this.fracDigits != 0) {
/* 305 */           buffer.append(this.fvalue);
/*     */         }
/*     */         else {
/* 308 */           buffer.append('0');
/*     */         }
/*     */       }
/* 311 */       this.canonical = buffer.toString();
/*     */     }
/*     */ 
/*     */     public BigDecimal getBigDecimal()
/*     */     {
/* 316 */       if (this.sign == 0) {
/* 317 */         return new BigDecimal(BigInteger.ZERO);
/*     */       }
/* 319 */       return new BigDecimal(toString());
/*     */     }
/*     */ 
/*     */     public BigInteger getBigInteger() throws NumberFormatException
/*     */     {
/* 324 */       if (this.fracDigits != 0) {
/* 325 */         throw new NumberFormatException();
/*     */       }
/* 327 */       if (this.sign == 0) {
/* 328 */         return BigInteger.ZERO;
/*     */       }
/* 330 */       if (this.sign == 1) {
/* 331 */         return new BigInteger(this.ivalue);
/*     */       }
/* 333 */       return new BigInteger("-" + this.ivalue);
/*     */     }
/*     */ 
/*     */     public long getLong() throws NumberFormatException
/*     */     {
/* 338 */       if (this.fracDigits != 0) {
/* 339 */         throw new NumberFormatException();
/*     */       }
/* 341 */       if (this.sign == 0) {
/* 342 */         return 0L;
/*     */       }
/* 344 */       if (this.sign == 1) {
/* 345 */         return Long.parseLong(this.ivalue);
/*     */       }
/* 347 */       return Long.parseLong("-" + this.ivalue);
/*     */     }
/*     */ 
/*     */     public int getInt() throws NumberFormatException
/*     */     {
/* 352 */       if (this.fracDigits != 0) {
/* 353 */         throw new NumberFormatException();
/*     */       }
/* 355 */       if (this.sign == 0) {
/* 356 */         return 0;
/*     */       }
/* 358 */       if (this.sign == 1) {
/* 359 */         return Integer.parseInt(this.ivalue);
/*     */       }
/* 361 */       return Integer.parseInt("-" + this.ivalue);
/*     */     }
/*     */ 
/*     */     public short getShort() throws NumberFormatException
/*     */     {
/* 366 */       if (this.fracDigits != 0) {
/* 367 */         throw new NumberFormatException();
/*     */       }
/* 369 */       if (this.sign == 0) {
/* 370 */         return 0;
/*     */       }
/* 372 */       if (this.sign == 1) {
/* 373 */         return Short.parseShort(this.ivalue);
/*     */       }
/* 375 */       return Short.parseShort("-" + this.ivalue);
/*     */     }
/*     */ 
/*     */     public byte getByte() throws NumberFormatException
/*     */     {
/* 380 */       if (this.fracDigits != 0) {
/* 381 */         throw new NumberFormatException();
/*     */       }
/* 383 */       if (this.sign == 0) {
/* 384 */         return 0;
/*     */       }
/* 386 */       if (this.sign == 1) {
/* 387 */         return Byte.parseByte(this.ivalue);
/*     */       }
/* 389 */       return Byte.parseByte("-" + this.ivalue);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV
 * JD-Core Version:    0.6.2
 */