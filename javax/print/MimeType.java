/*     */ package javax.print;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class MimeType
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -2785720609362367683L;
/*     */   private String[] myPieces;
/* 105 */   private transient String myStringValue = null;
/*     */ 
/* 110 */   private transient ParameterMapEntrySet myEntrySet = null;
/*     */ 
/* 115 */   private transient ParameterMap myParameterMap = null;
/*     */   private static final int TOKEN_LEXEME = 0;
/*     */   private static final int QUOTED_STRING_LEXEME = 1;
/*     */   private static final int TSPECIAL_LEXEME = 2;
/*     */   private static final int EOF_LEXEME = 3;
/*     */   private static final int ILLEGAL_LEXEME = 4;
/*     */ 
/*     */   public MimeType(String paramString)
/*     */   {
/* 204 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 212 */     return getStringValue();
/*     */   }
/*     */ 
/*     */   public String getMediaType()
/*     */   {
/* 219 */     return this.myPieces[0];
/*     */   }
/*     */ 
/*     */   public String getMediaSubtype()
/*     */   {
/* 226 */     return this.myPieces[1];
/*     */   }
/*     */ 
/*     */   public Map getParameterMap()
/*     */   {
/* 238 */     if (this.myParameterMap == null) {
/* 239 */       this.myParameterMap = new ParameterMap(null);
/*     */     }
/* 241 */     return this.myParameterMap;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 251 */     return getStringValue();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 258 */     return getStringValue().hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 278 */     return (paramObject != null) && ((paramObject instanceof MimeType)) && (getStringValue().equals(((MimeType)paramObject).getStringValue()));
/*     */   }
/*     */ 
/*     */   private String getStringValue()
/*     */   {
/* 287 */     if (this.myStringValue == null) {
/* 288 */       StringBuffer localStringBuffer = new StringBuffer();
/* 289 */       localStringBuffer.append(this.myPieces[0]);
/* 290 */       localStringBuffer.append('/');
/* 291 */       localStringBuffer.append(this.myPieces[1]);
/* 292 */       int i = this.myPieces.length;
/* 293 */       for (int j = 2; j < i; j += 2) {
/* 294 */         localStringBuffer.append(';');
/* 295 */         localStringBuffer.append(' ');
/* 296 */         localStringBuffer.append(this.myPieces[j]);
/* 297 */         localStringBuffer.append('=');
/* 298 */         localStringBuffer.append(addQuotes(this.myPieces[(j + 1)]));
/*     */       }
/* 300 */       this.myStringValue = localStringBuffer.toString();
/*     */     }
/* 302 */     return this.myStringValue;
/*     */   }
/*     */ 
/*     */   private static String toUnicodeLowerCase(String paramString)
/*     */   {
/* 478 */     int i = paramString.length();
/* 479 */     char[] arrayOfChar = new char[i];
/* 480 */     for (int j = 0; j < i; j++) {
/* 481 */       arrayOfChar[j] = Character.toLowerCase(paramString.charAt(j));
/*     */     }
/* 483 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   private static String removeBackslashes(String paramString)
/*     */   {
/* 490 */     int i = paramString.length();
/* 491 */     char[] arrayOfChar = new char[i];
/*     */ 
/* 493 */     int k = 0;
/*     */ 
/* 495 */     for (int j = 0; j < i; j++) {
/* 496 */       int m = paramString.charAt(j);
/* 497 */       if (m == 92) {
/* 498 */         m = paramString.charAt(++j);
/*     */       }
/* 500 */       arrayOfChar[(k++)] = m;
/*     */     }
/* 502 */     return new String(arrayOfChar, 0, k);
/*     */   }
/*     */ 
/*     */   private static String addQuotes(String paramString)
/*     */   {
/* 510 */     int i = paramString.length();
/*     */ 
/* 513 */     StringBuffer localStringBuffer = new StringBuffer(i + 2);
/* 514 */     localStringBuffer.append('"');
/* 515 */     for (int j = 0; j < i; j++) {
/* 516 */       char c = paramString.charAt(j);
/* 517 */       if (c == '"') {
/* 518 */         localStringBuffer.append('\\');
/*     */       }
/* 520 */       localStringBuffer.append(c);
/*     */     }
/* 522 */     localStringBuffer.append('"');
/* 523 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void parse(String paramString)
/*     */   {
/* 546 */     if (paramString == null) {
/* 547 */       throw new NullPointerException();
/*     */     }
/* 549 */     LexicalAnalyzer localLexicalAnalyzer = new LexicalAnalyzer(paramString);
/*     */ 
/* 551 */     Vector localVector = new Vector();
/* 552 */     boolean bool1 = false;
/* 553 */     boolean bool2 = false;
/*     */     String str1;
/* 556 */     if (localLexicalAnalyzer.getLexemeType() == 0) {
/* 557 */       str1 = toUnicodeLowerCase(localLexicalAnalyzer.getLexeme());
/* 558 */       localVector.add(str1);
/* 559 */       localLexicalAnalyzer.nextLexeme();
/* 560 */       bool1 = str1.equals("text");
/*     */     } else {
/* 562 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 565 */     if ((localLexicalAnalyzer.getLexemeType() == 2) && (localLexicalAnalyzer.getLexemeFirstCharacter() == '/'))
/*     */     {
/* 567 */       localLexicalAnalyzer.nextLexeme();
/*     */     }
/* 569 */     else throw new IllegalArgumentException();
/*     */ 
/* 571 */     if (localLexicalAnalyzer.getLexemeType() == 0) {
/* 572 */       localVector.add(toUnicodeLowerCase(localLexicalAnalyzer.getLexeme()));
/* 573 */       localLexicalAnalyzer.nextLexeme();
/*     */     } else {
/* 575 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 578 */     while ((localLexicalAnalyzer.getLexemeType() == 2) && (localLexicalAnalyzer.getLexemeFirstCharacter() == ';'))
/*     */     {
/* 581 */       localLexicalAnalyzer.nextLexeme();
/*     */ 
/* 584 */       if (localLexicalAnalyzer.getLexemeType() == 0) {
/* 585 */         str1 = toUnicodeLowerCase(localLexicalAnalyzer.getLexeme());
/* 586 */         localVector.add(str1);
/* 587 */         localLexicalAnalyzer.nextLexeme();
/* 588 */         bool2 = str1.equals("charset");
/*     */       } else {
/* 590 */         throw new IllegalArgumentException();
/*     */       }
/*     */ 
/* 594 */       if ((localLexicalAnalyzer.getLexemeType() == 2) && (localLexicalAnalyzer.getLexemeFirstCharacter() == '='))
/*     */       {
/* 596 */         localLexicalAnalyzer.nextLexeme();
/*     */       }
/* 598 */       else throw new IllegalArgumentException();
/*     */ 
/* 602 */       if (localLexicalAnalyzer.getLexemeType() == 0) {
/* 603 */         str1 = localLexicalAnalyzer.getLexeme();
/* 604 */         localVector.add((bool1) && (bool2) ? toUnicodeLowerCase(str1) : str1);
/*     */ 
/* 607 */         localLexicalAnalyzer.nextLexeme();
/* 608 */       } else if (localLexicalAnalyzer.getLexemeType() == 1) {
/* 609 */         str1 = removeBackslashes(localLexicalAnalyzer.getLexeme());
/* 610 */         localVector.add((bool1) && (bool2) ? toUnicodeLowerCase(str1) : str1);
/*     */ 
/* 613 */         localLexicalAnalyzer.nextLexeme();
/*     */       } else {
/* 615 */         throw new IllegalArgumentException();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 620 */     if (localLexicalAnalyzer.getLexemeType() != 3) {
/* 621 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 625 */     int i = localVector.size();
/* 626 */     this.myPieces = ((String[])localVector.toArray(new String[i]));
/*     */ 
/* 631 */     for (int j = 4; j < i; j += 2) {
/* 632 */       int k = 2;
/* 633 */       while ((k < j) && (this.myPieces[k].compareTo(this.myPieces[j]) <= 0)) {
/* 634 */         k += 2;
/*     */       }
/* 636 */       while (k < j) {
/* 637 */         String str2 = this.myPieces[k];
/* 638 */         this.myPieces[k] = this.myPieces[j];
/* 639 */         this.myPieces[j] = str2;
/* 640 */         str2 = this.myPieces[(k + 1)];
/* 641 */         this.myPieces[(k + 1)] = this.myPieces[(j + 1)];
/* 642 */         this.myPieces[(j + 1)] = str2;
/* 643 */         k += 2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LexicalAnalyzer
/*     */   {
/*     */     protected String mySource;
/*     */     protected int mySourceLength;
/*     */     protected int myCurrentIndex;
/*     */     protected int myLexemeType;
/*     */     protected int myLexemeBeginIndex;
/*     */     protected int myLexemeEndIndex;
/*     */ 
/*     */     public LexicalAnalyzer(String paramString)
/*     */     {
/* 325 */       this.mySource = paramString;
/* 326 */       this.mySourceLength = paramString.length();
/* 327 */       this.myCurrentIndex = 0;
/* 328 */       nextLexeme();
/*     */     }
/*     */ 
/*     */     public int getLexemeType() {
/* 332 */       return this.myLexemeType;
/*     */     }
/*     */ 
/*     */     public String getLexeme() {
/* 336 */       return this.myLexemeBeginIndex >= this.mySourceLength ? null : this.mySource.substring(this.myLexemeBeginIndex, this.myLexemeEndIndex);
/*     */     }
/*     */ 
/*     */     public char getLexemeFirstCharacter()
/*     */     {
/* 342 */       return this.myLexemeBeginIndex >= this.mySourceLength ? '\000' : this.mySource.charAt(this.myLexemeBeginIndex);
/*     */     }
/*     */ 
/*     */     public void nextLexeme()
/*     */     {
/* 348 */       int i = 0;
/* 349 */       int j = 0;
/*     */ 
/* 351 */       while (i >= 0)
/*     */       {
/*     */         int k;
/* 352 */         switch (i)
/*     */         {
/*     */         case 0:
/* 355 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 356 */             this.myLexemeType = 3;
/* 357 */             this.myLexemeBeginIndex = this.mySourceLength;
/* 358 */             this.myLexemeEndIndex = this.mySourceLength;
/* 359 */             i = -1;
/* 360 */           } else if (Character.isWhitespace(k = this.mySource.charAt(this.myCurrentIndex++)))
/*     */           {
/* 362 */             i = 0;
/* 363 */           } else if (k == 34) {
/* 364 */             this.myLexemeType = 1;
/* 365 */             this.myLexemeBeginIndex = this.myCurrentIndex;
/* 366 */             i = 1;
/* 367 */           } else if (k == 40) {
/* 368 */             j++;
/* 369 */             i = 3;
/* 370 */           } else if ((k == 47) || (k == 59) || (k == 61) || (k == 41) || (k == 60) || (k == 62) || (k == 64) || (k == 44) || (k == 58) || (k == 92) || (k == 91) || (k == 93) || (k == 63))
/*     */           {
/* 375 */             this.myLexemeType = 2;
/* 376 */             this.myLexemeBeginIndex = (this.myCurrentIndex - 1);
/* 377 */             this.myLexemeEndIndex = this.myCurrentIndex;
/* 378 */             i = -1;
/*     */           } else {
/* 380 */             this.myLexemeType = 0;
/* 381 */             this.myLexemeBeginIndex = (this.myCurrentIndex - 1);
/* 382 */             i = 5;
/*     */           }
/* 384 */           break;
/*     */         case 1:
/* 387 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 388 */             this.myLexemeType = 4;
/* 389 */             this.myLexemeBeginIndex = this.mySourceLength;
/* 390 */             this.myLexemeEndIndex = this.mySourceLength;
/* 391 */             i = -1;
/* 392 */           } else if ((k = this.mySource.charAt(this.myCurrentIndex++)) == '"') {
/* 393 */             this.myLexemeEndIndex = (this.myCurrentIndex - 1);
/* 394 */             i = -1;
/* 395 */           } else if (k == 92) {
/* 396 */             i = 2;
/*     */           } else {
/* 398 */             i = 1;
/*     */           }
/* 400 */           break;
/*     */         case 2:
/* 403 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 404 */             this.myLexemeType = 4;
/* 405 */             this.myLexemeBeginIndex = this.mySourceLength;
/* 406 */             this.myLexemeEndIndex = this.mySourceLength;
/* 407 */             i = -1;
/*     */           } else {
/* 409 */             this.myCurrentIndex += 1;
/* 410 */             i = 1;
/* 411 */           }break;
/*     */         case 3:
/* 413 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 414 */             this.myLexemeType = 4;
/* 415 */             this.myLexemeBeginIndex = this.mySourceLength;
/* 416 */             this.myLexemeEndIndex = this.mySourceLength;
/* 417 */             i = -1;
/* 418 */           } else if ((k = this.mySource.charAt(this.myCurrentIndex++)) == '(') {
/* 419 */             j++;
/* 420 */             i = 3;
/* 421 */           } else if (k == 41) {
/* 422 */             j--;
/* 423 */             i = j == 0 ? 0 : 3;
/* 424 */           } else if (k == 92) {
/* 425 */             i = 4; } else {
/* 426 */             i = 3;
/*     */           }
/* 428 */           break;
/*     */         case 4:
/* 431 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 432 */             this.myLexemeType = 4;
/* 433 */             this.myLexemeBeginIndex = this.mySourceLength;
/* 434 */             this.myLexemeEndIndex = this.mySourceLength;
/* 435 */             i = -1;
/*     */           } else {
/* 437 */             this.myCurrentIndex += 1;
/* 438 */             i = 3;
/*     */           }
/* 440 */           break;
/*     */         case 5:
/* 443 */           if (this.myCurrentIndex >= this.mySourceLength) {
/* 444 */             this.myLexemeEndIndex = this.myCurrentIndex;
/* 445 */             i = -1;
/* 446 */           } else if (Character.isWhitespace(k = this.mySource.charAt(this.myCurrentIndex++)))
/*     */           {
/* 448 */             this.myLexemeEndIndex = (this.myCurrentIndex - 1);
/* 449 */             i = -1;
/* 450 */           } else if ((k == 34) || (k == 40) || (k == 47) || (k == 59) || (k == 61) || (k == 41) || (k == 60) || (k == 62) || (k == 64) || (k == 44) || (k == 58) || (k == 92) || (k == 91) || (k == 93) || (k == 63))
/*     */           {
/* 455 */             this.myCurrentIndex -= 1;
/* 456 */             this.myLexemeEndIndex = this.myCurrentIndex;
/* 457 */             i = -1;
/*     */           } else {
/* 459 */             i = 5;
/*     */           }
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParameterMap extends AbstractMap
/*     */   {
/*     */     private ParameterMap()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Set entrySet()
/*     */     {
/* 184 */       if (MimeType.this.myEntrySet == null) {
/* 185 */         MimeType.this.myEntrySet = new MimeType.ParameterMapEntrySet(MimeType.this, null);
/*     */       }
/* 187 */       return MimeType.this.myEntrySet;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParameterMapEntry
/*     */     implements Map.Entry
/*     */   {
/*     */     private int myIndex;
/*     */ 
/*     */     public ParameterMapEntry(int arg2)
/*     */     {
/*     */       int i;
/* 123 */       this.myIndex = i;
/*     */     }
/*     */     public Object getKey() {
/* 126 */       return MimeType.this.myPieces[this.myIndex];
/*     */     }
/*     */     public Object getValue() {
/* 129 */       return MimeType.this.myPieces[(this.myIndex + 1)];
/*     */     }
/*     */     public Object setValue(Object paramObject) {
/* 132 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 135 */       return (paramObject != null) && ((paramObject instanceof Map.Entry)) && (getKey().equals(((Map.Entry)paramObject).getKey())) && (getValue().equals(((Map.Entry)paramObject).getValue()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 141 */       return getKey().hashCode() ^ getValue().hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParameterMapEntrySet extends AbstractSet
/*     */   {
/*     */     private ParameterMapEntrySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 172 */       return new MimeType.ParameterMapEntrySetIterator(MimeType.this, null);
/*     */     }
/*     */     public int size() {
/* 175 */       return (MimeType.this.myPieces.length - 2) / 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParameterMapEntrySetIterator
/*     */     implements Iterator
/*     */   {
/* 149 */     private int myIndex = 2;
/*     */ 
/*     */     private ParameterMapEntrySetIterator() {  } 
/* 151 */     public boolean hasNext() { return this.myIndex < MimeType.this.myPieces.length; }
/*     */ 
/*     */     public Object next() {
/* 154 */       if (hasNext()) {
/* 155 */         MimeType.ParameterMapEntry localParameterMapEntry = new MimeType.ParameterMapEntry(MimeType.this, this.myIndex);
/* 156 */         this.myIndex += 2;
/* 157 */         return localParameterMapEntry;
/*     */       }
/* 159 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 163 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.MimeType
 * JD-Core Version:    0.6.2
 */