/*     */ package java.util;
/*     */ 
/*     */ public class StringTokenizer
/*     */   implements Enumeration<Object>
/*     */ {
/*     */   private int currentPosition;
/*     */   private int newPosition;
/*     */   private int maxPosition;
/*     */   private String str;
/*     */   private String delimiters;
/*     */   private boolean retDelims;
/*     */   private boolean delimsChanged;
/*     */   private int maxDelimCodePoint;
/* 130 */   private boolean hasSurrogates = false;
/*     */   private int[] delimiterCodePoints;
/*     */ 
/*     */   private void setMaxDelimCodePoint()
/*     */   {
/* 143 */     if (this.delimiters == null) {
/* 144 */       this.maxDelimCodePoint = 0;
/* 145 */       return;
/*     */     }
/*     */ 
/* 148 */     int i = 0;
/*     */ 
/* 150 */     int k = 0;
/*     */     int j;
/* 151 */     for (int m = 0; m < this.delimiters.length(); m += Character.charCount(j)) {
/* 152 */       j = this.delimiters.charAt(m);
/* 153 */       if ((j >= 55296) && (j <= 57343)) {
/* 154 */         j = this.delimiters.codePointAt(m);
/* 155 */         this.hasSurrogates = true;
/*     */       }
/* 157 */       if (i < j)
/* 158 */         i = j;
/* 159 */       k++;
/*     */     }
/* 161 */     this.maxDelimCodePoint = i;
/*     */ 
/* 163 */     if (this.hasSurrogates) {
/* 164 */       this.delimiterCodePoints = new int[k];
/* 165 */       m = 0; for (int n = 0; m < k; n += Character.charCount(j)) {
/* 166 */         j = this.delimiters.codePointAt(n);
/* 167 */         this.delimiterCodePoints[m] = j;
/*     */ 
/* 165 */         m++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public StringTokenizer(String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/* 195 */     this.currentPosition = 0;
/* 196 */     this.newPosition = -1;
/* 197 */     this.delimsChanged = false;
/* 198 */     this.str = paramString1;
/* 199 */     this.maxPosition = paramString1.length();
/* 200 */     this.delimiters = paramString2;
/* 201 */     this.retDelims = paramBoolean;
/* 202 */     setMaxDelimCodePoint();
/*     */   }
/*     */ 
/*     */   public StringTokenizer(String paramString1, String paramString2)
/*     */   {
/* 221 */     this(paramString1, paramString2, false);
/*     */   }
/*     */ 
/*     */   public StringTokenizer(String paramString)
/*     */   {
/* 236 */     this(paramString, " \t\n\r\f", false);
/*     */   }
/*     */ 
/*     */   private int skipDelimiters(int paramInt)
/*     */   {
/* 245 */     if (this.delimiters == null) {
/* 246 */       throw new NullPointerException();
/*     */     }
/* 248 */     int i = paramInt;
/* 249 */     while ((!this.retDelims) && (i < this.maxPosition))
/*     */     {
/*     */       int j;
/* 250 */       if (!this.hasSurrogates) {
/* 251 */         j = this.str.charAt(i);
/* 252 */         if ((j > this.maxDelimCodePoint) || (this.delimiters.indexOf(j) < 0))
/*     */           break;
/* 254 */         i++;
/*     */       } else {
/* 256 */         j = this.str.codePointAt(i);
/* 257 */         if ((j > this.maxDelimCodePoint) || (!isDelimiter(j))) {
/*     */           break;
/*     */         }
/* 260 */         i += Character.charCount(j);
/*     */       }
/*     */     }
/* 263 */     return i;
/*     */   }
/*     */ 
/*     */   private int scanToken(int paramInt)
/*     */   {
/* 271 */     int i = paramInt;
/*     */     int j;
/* 272 */     while (i < this.maxPosition) {
/* 273 */       if (!this.hasSurrogates) {
/* 274 */         j = this.str.charAt(i);
/* 275 */         if ((j <= this.maxDelimCodePoint) && (this.delimiters.indexOf(j) >= 0))
/*     */           break;
/* 277 */         i++;
/*     */       } else {
/* 279 */         j = this.str.codePointAt(i);
/* 280 */         if ((j <= this.maxDelimCodePoint) && (isDelimiter(j)))
/*     */           break;
/* 282 */         i += Character.charCount(j);
/*     */       }
/*     */     }
/* 285 */     if ((this.retDelims) && (paramInt == i)) {
/* 286 */       if (!this.hasSurrogates) {
/* 287 */         j = this.str.charAt(i);
/* 288 */         if ((j <= this.maxDelimCodePoint) && (this.delimiters.indexOf(j) >= 0))
/* 289 */           i++;
/*     */       } else {
/* 291 */         j = this.str.codePointAt(i);
/* 292 */         if ((j <= this.maxDelimCodePoint) && (isDelimiter(j)))
/* 293 */           i += Character.charCount(j);
/*     */       }
/*     */     }
/* 296 */     return i;
/*     */   }
/*     */ 
/*     */   private boolean isDelimiter(int paramInt) {
/* 300 */     for (int i = 0; i < this.delimiterCodePoints.length; i++) {
/* 301 */       if (this.delimiterCodePoints[i] == paramInt) {
/* 302 */         return true;
/*     */       }
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreTokens()
/*     */   {
/* 323 */     this.newPosition = skipDelimiters(this.currentPosition);
/* 324 */     return this.newPosition < this.maxPosition;
/*     */   }
/*     */ 
/*     */   public String nextToken()
/*     */   {
/* 341 */     this.currentPosition = ((this.newPosition >= 0) && (!this.delimsChanged) ? this.newPosition : skipDelimiters(this.currentPosition));
/*     */ 
/* 345 */     this.delimsChanged = false;
/* 346 */     this.newPosition = -1;
/*     */ 
/* 348 */     if (this.currentPosition >= this.maxPosition)
/* 349 */       throw new NoSuchElementException();
/* 350 */     int i = this.currentPosition;
/* 351 */     this.currentPosition = scanToken(this.currentPosition);
/* 352 */     return this.str.substring(i, this.currentPosition);
/*     */   }
/*     */ 
/*     */   public String nextToken(String paramString)
/*     */   {
/* 371 */     this.delimiters = paramString;
/*     */ 
/* 374 */     this.delimsChanged = true;
/*     */ 
/* 376 */     setMaxDelimCodePoint();
/* 377 */     return nextToken();
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/* 391 */     return hasMoreTokens();
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/* 407 */     return nextToken();
/*     */   }
/*     */ 
/*     */   public int countTokens()
/*     */   {
/* 420 */     int i = 0;
/* 421 */     int j = this.currentPosition;
/* 422 */     while (j < this.maxPosition) {
/* 423 */       j = skipDelimiters(j);
/* 424 */       if (j >= this.maxPosition)
/*     */         break;
/* 426 */       j = scanToken(j);
/* 427 */       i++;
/*     */     }
/* 429 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.StringTokenizer
 * JD-Core Version:    0.6.2
 */