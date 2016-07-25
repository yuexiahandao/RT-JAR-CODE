/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.Format;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.NumberFormat.Field;
/*     */ import java.text.ParseException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class NumberFormatter extends InternationalFormatter
/*     */ {
/*     */   private String specialChars;
/*     */ 
/*     */   public NumberFormatter()
/*     */   {
/* 104 */     this(NumberFormat.getNumberInstance());
/*     */   }
/*     */ 
/*     */   public NumberFormatter(NumberFormat paramNumberFormat)
/*     */   {
/* 113 */     super(paramNumberFormat);
/* 114 */     setFormat(paramNumberFormat);
/* 115 */     setAllowsInvalid(true);
/* 116 */     setCommitsOnValidEdit(false);
/* 117 */     setOverwriteMode(false);
/*     */   }
/*     */ 
/*     */   public void setFormat(Format paramFormat)
/*     */   {
/* 131 */     super.setFormat(paramFormat);
/*     */ 
/* 133 */     DecimalFormatSymbols localDecimalFormatSymbols = getDecimalFormatSymbols();
/*     */ 
/* 135 */     if (localDecimalFormatSymbols != null) {
/* 136 */       StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 138 */       localStringBuilder.append(localDecimalFormatSymbols.getCurrencySymbol());
/* 139 */       localStringBuilder.append(localDecimalFormatSymbols.getDecimalSeparator());
/* 140 */       localStringBuilder.append(localDecimalFormatSymbols.getGroupingSeparator());
/* 141 */       localStringBuilder.append(localDecimalFormatSymbols.getInfinity());
/* 142 */       localStringBuilder.append(localDecimalFormatSymbols.getInternationalCurrencySymbol());
/* 143 */       localStringBuilder.append(localDecimalFormatSymbols.getMinusSign());
/* 144 */       localStringBuilder.append(localDecimalFormatSymbols.getMonetaryDecimalSeparator());
/* 145 */       localStringBuilder.append(localDecimalFormatSymbols.getNaN());
/* 146 */       localStringBuilder.append(localDecimalFormatSymbols.getPercent());
/* 147 */       localStringBuilder.append('+');
/* 148 */       this.specialChars = localStringBuilder.toString();
/*     */     }
/*     */     else {
/* 151 */       this.specialChars = "";
/*     */     }
/*     */   }
/*     */ 
/*     */   Object stringToValue(String paramString, Format paramFormat)
/*     */     throws ParseException
/*     */   {
/* 160 */     if (paramFormat == null) {
/* 161 */       return paramString;
/*     */     }
/* 163 */     Object localObject = paramFormat.parseObject(paramString);
/*     */ 
/* 165 */     return convertValueToValueClass(localObject, getValueClass());
/*     */   }
/*     */ 
/*     */   private Object convertValueToValueClass(Object paramObject, Class paramClass)
/*     */   {
/* 176 */     if ((paramClass != null) && ((paramObject instanceof Number))) {
/* 177 */       Number localNumber = (Number)paramObject;
/* 178 */       if (paramClass == Integer.class) {
/* 179 */         return Integer.valueOf(localNumber.intValue());
/*     */       }
/* 181 */       if (paramClass == Long.class) {
/* 182 */         return Long.valueOf(localNumber.longValue());
/*     */       }
/* 184 */       if (paramClass == Float.class) {
/* 185 */         return Float.valueOf(localNumber.floatValue());
/*     */       }
/* 187 */       if (paramClass == Double.class) {
/* 188 */         return Double.valueOf(localNumber.doubleValue());
/*     */       }
/* 190 */       if (paramClass == Byte.class) {
/* 191 */         return Byte.valueOf(localNumber.byteValue());
/*     */       }
/* 193 */       if (paramClass == Short.class) {
/* 194 */         return Short.valueOf(localNumber.shortValue());
/*     */       }
/*     */     }
/* 197 */     return paramObject;
/*     */   }
/*     */ 
/*     */   private char getPositiveSign()
/*     */   {
/* 204 */     return '+';
/*     */   }
/*     */ 
/*     */   private char getMinusSign()
/*     */   {
/* 211 */     DecimalFormatSymbols localDecimalFormatSymbols = getDecimalFormatSymbols();
/*     */ 
/* 213 */     if (localDecimalFormatSymbols != null) {
/* 214 */       return localDecimalFormatSymbols.getMinusSign();
/*     */     }
/* 216 */     return '-';
/*     */   }
/*     */ 
/*     */   private char getDecimalSeparator()
/*     */   {
/* 223 */     DecimalFormatSymbols localDecimalFormatSymbols = getDecimalFormatSymbols();
/*     */ 
/* 225 */     if (localDecimalFormatSymbols != null) {
/* 226 */       return localDecimalFormatSymbols.getDecimalSeparator();
/*     */     }
/* 228 */     return '.';
/*     */   }
/*     */ 
/*     */   private DecimalFormatSymbols getDecimalFormatSymbols()
/*     */   {
/* 235 */     Format localFormat = getFormat();
/*     */ 
/* 237 */     if ((localFormat instanceof DecimalFormat)) {
/* 238 */       return ((DecimalFormat)localFormat).getDecimalFormatSymbols();
/*     */     }
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   boolean isLegalInsertText(String paramString)
/*     */   {
/* 250 */     if (getAllowsInvalid()) {
/* 251 */       return true;
/*     */     }
/* 253 */     for (int i = paramString.length() - 1; i >= 0; i--) {
/* 254 */       char c = paramString.charAt(i);
/*     */ 
/* 256 */       if ((!Character.isDigit(c)) && (this.specialChars.indexOf(c) == -1))
/*     */       {
/* 258 */         return false;
/*     */       }
/*     */     }
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   boolean isLiteral(Map paramMap)
/*     */   {
/* 269 */     if (!super.isLiteral(paramMap)) {
/* 270 */       if (paramMap == null) {
/* 271 */         return false;
/*     */       }
/* 273 */       int i = paramMap.size();
/*     */ 
/* 275 */       if (paramMap.get(NumberFormat.Field.GROUPING_SEPARATOR) != null) {
/* 276 */         i--;
/* 277 */         if (paramMap.get(NumberFormat.Field.INTEGER) != null) {
/* 278 */           i--;
/*     */         }
/*     */       }
/* 281 */       if (paramMap.get(NumberFormat.Field.EXPONENT_SYMBOL) != null) {
/* 282 */         i--;
/*     */       }
/* 284 */       if (paramMap.get(NumberFormat.Field.PERCENT) != null) {
/* 285 */         i--;
/*     */       }
/* 287 */       if (paramMap.get(NumberFormat.Field.PERMILLE) != null) {
/* 288 */         i--;
/*     */       }
/* 290 */       if (paramMap.get(NumberFormat.Field.CURRENCY) != null) {
/* 291 */         i--;
/*     */       }
/* 293 */       if (paramMap.get(NumberFormat.Field.SIGN) != null) {
/* 294 */         i--;
/*     */       }
/* 296 */       return i == 0;
/*     */     }
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */   boolean isNavigatable(int paramInt)
/*     */   {
/* 307 */     if (!super.isNavigatable(paramInt))
/*     */     {
/* 309 */       return getBufferedChar(paramInt) == getDecimalSeparator();
/*     */     }
/* 311 */     return true;
/*     */   }
/*     */ 
/*     */   private NumberFormat.Field getFieldFrom(int paramInt1, int paramInt2)
/*     */   {
/* 319 */     if (isValidMask()) {
/* 320 */       int i = getFormattedTextField().getDocument().getLength();
/* 321 */       AttributedCharacterIterator localAttributedCharacterIterator = getIterator();
/*     */ 
/* 323 */       if (paramInt1 >= i) {
/* 324 */         paramInt1 += paramInt2;
/*     */       }
/* 326 */       while ((paramInt1 >= 0) && (paramInt1 < i)) {
/* 327 */         localAttributedCharacterIterator.setIndex(paramInt1);
/*     */ 
/* 329 */         Map localMap = localAttributedCharacterIterator.getAttributes();
/*     */         Iterator localIterator;
/* 331 */         if ((localMap != null) && (localMap.size() > 0)) {
/* 332 */           for (localIterator = localMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 333 */             if ((localObject instanceof NumberFormat.Field)) {
/* 334 */               return (NumberFormat.Field)localObject;
/*     */             }
/*     */           }
/*     */         }
/* 338 */         paramInt1 += paramInt2;
/*     */       }
/*     */     }
/* 341 */     return null;
/*     */   }
/*     */ 
/*     */   void replace(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */     throws BadLocationException
/*     */   {
/* 350 */     if ((!getAllowsInvalid()) && (paramInt2 == 0) && (paramString != null) && (paramString.length() == 1) && (toggleSignIfNecessary(paramFilterBypass, paramInt1, paramString.charAt(0))))
/*     */     {
/* 353 */       return;
/*     */     }
/* 355 */     super.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   private boolean toggleSignIfNecessary(DocumentFilter.FilterBypass paramFilterBypass, int paramInt, char paramChar)
/*     */     throws BadLocationException
/*     */   {
/* 366 */     if ((paramChar == getMinusSign()) || (paramChar == getPositiveSign())) {
/* 367 */       NumberFormat.Field localField = getFieldFrom(paramInt, -1);
/*     */       try
/*     */       {
/*     */         Object localObject;
/* 371 */         if ((localField == null) || ((localField != NumberFormat.Field.EXPONENT) && (localField != NumberFormat.Field.EXPONENT_SYMBOL) && (localField != NumberFormat.Field.EXPONENT_SIGN)))
/*     */         {
/* 375 */           localObject = toggleSign(paramChar == getPositiveSign());
/*     */         }
/*     */         else
/*     */         {
/* 379 */           localObject = toggleExponentSign(paramInt, paramChar);
/*     */         }
/* 381 */         if ((localObject != null) && (isValidValue(localObject, false))) {
/* 382 */           int i = getLiteralCountTo(paramInt);
/* 383 */           String str = valueToString(localObject);
/*     */ 
/* 385 */           paramFilterBypass.remove(0, paramFilterBypass.getDocument().getLength());
/* 386 */           paramFilterBypass.insertString(0, str, null);
/* 387 */           updateValue(localObject);
/* 388 */           repositionCursor(getLiteralCountTo(paramInt) - i + paramInt, 1);
/*     */ 
/* 390 */           return true;
/*     */         }
/*     */       } catch (ParseException localParseException) {
/* 393 */         invalidEdit();
/*     */       }
/*     */     }
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */   private Object toggleSign(boolean paramBoolean)
/*     */     throws ParseException
/*     */   {
/* 404 */     Object localObject = stringToValue(getFormattedTextField().getText());
/*     */ 
/* 406 */     if (localObject != null)
/*     */     {
/* 409 */       String str = localObject.toString();
/*     */ 
/* 411 */       if ((str != null) && (str.length() > 0)) {
/* 412 */         if (paramBoolean) {
/* 413 */           if (str.charAt(0) == '-')
/* 414 */             str = str.substring(1);
/*     */         }
/*     */         else
/*     */         {
/* 418 */           if (str.charAt(0) == '+') {
/* 419 */             str = str.substring(1);
/*     */           }
/* 421 */           if ((str.length() > 0) && (str.charAt(0) != '-')) {
/* 422 */             str = "-" + str;
/*     */           }
/*     */         }
/* 425 */         if (str != null) {
/* 426 */           Class localClass = getValueClass();
/*     */ 
/* 428 */           if (localClass == null)
/* 429 */             localClass = localObject.getClass();
/*     */           try
/*     */           {
/* 432 */             ReflectUtil.checkPackageAccess(localClass);
/* 433 */             SwingUtilities2.checkAccess(localClass.getModifiers());
/* 434 */             Constructor localConstructor = localClass.getConstructor(new Class[] { String.class });
/*     */ 
/* 436 */             if (localConstructor != null) {
/* 437 */               SwingUtilities2.checkAccess(localConstructor.getModifiers());
/* 438 */               return localConstructor.newInstance(new Object[] { str });
/*     */             }
/*     */           } catch (Throwable localThrowable) {  }
/*     */ 
/*     */         }
/*     */       }
/*     */     }
/* 444 */     return null;
/*     */   }
/*     */ 
/*     */   private Object toggleExponentSign(int paramInt, char paramChar)
/*     */     throws BadLocationException, ParseException
/*     */   {
/* 453 */     String str = getFormattedTextField().getText();
/* 454 */     int i = 0;
/* 455 */     int j = getAttributeStart(NumberFormat.Field.EXPONENT_SIGN);
/*     */ 
/* 457 */     if (j >= 0) {
/* 458 */       i = 1;
/* 459 */       paramInt = j;
/*     */     }
/* 461 */     if (paramChar == getPositiveSign()) {
/* 462 */       str = getReplaceString(paramInt, i, null);
/*     */     }
/*     */     else {
/* 465 */       str = getReplaceString(paramInt, i, new String(new char[] { paramChar }));
/*     */     }
/*     */ 
/* 468 */     return stringToValue(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.NumberFormatter
 * JD-Core Version:    0.6.2
 */