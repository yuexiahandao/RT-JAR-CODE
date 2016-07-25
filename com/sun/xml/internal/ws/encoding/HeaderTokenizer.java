/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ class HeaderTokenizer
/*     */ {
/*     */   private String string;
/*     */   private boolean skipComments;
/*     */   private String delimiters;
/*     */   private int currentPos;
/*     */   private int maxPos;
/*     */   private int nextPos;
/*     */   private int peekPos;
/*     */   private static final String RFC822 = "()<>@,;:\\\"\t .[]";
/*     */   static final String MIME = "()<>@,;:\\\"\t []/?=";
/* 138 */   private static final Token EOFToken = new Token(-4, null);
/*     */ 
/*     */   HeaderTokenizer(String header, String delimiters, boolean skipComments)
/*     */   {
/* 153 */     this.string = (header == null ? "" : header);
/* 154 */     this.skipComments = skipComments;
/* 155 */     this.delimiters = delimiters;
/* 156 */     this.currentPos = (this.nextPos = this.peekPos = 0);
/* 157 */     this.maxPos = this.string.length();
/*     */   }
/*     */ 
/*     */   HeaderTokenizer(String header, String delimiters)
/*     */   {
/* 167 */     this(header, delimiters, true);
/*     */   }
/*     */ 
/*     */   HeaderTokenizer(String header)
/*     */   {
/* 176 */     this(header, "()<>@,;:\\\"\t .[]");
/*     */   }
/*     */ 
/*     */   Token next()
/*     */     throws WebServiceException
/*     */   {
/* 191 */     this.currentPos = this.nextPos;
/* 192 */     Token tk = getNext();
/* 193 */     this.nextPos = (this.peekPos = this.currentPos);
/* 194 */     return tk;
/*     */   }
/*     */ 
/*     */   Token peek()
/*     */     throws WebServiceException
/*     */   {
/* 209 */     this.currentPos = this.peekPos;
/* 210 */     Token tk = getNext();
/* 211 */     this.peekPos = this.currentPos;
/* 212 */     return tk;
/*     */   }
/*     */ 
/*     */   String getRemainder()
/*     */   {
/* 222 */     return this.string.substring(this.nextPos);
/*     */   }
/*     */ 
/*     */   private Token getNext()
/*     */     throws WebServiceException
/*     */   {
/* 232 */     if (this.currentPos >= this.maxPos) {
/* 233 */       return EOFToken;
/*     */     }
/*     */ 
/* 236 */     if (skipWhiteSpace() == -4) {
/* 237 */       return EOFToken;
/*     */     }
/*     */ 
/* 241 */     boolean filter = false;
/*     */ 
/* 243 */     char c = this.string.charAt(this.currentPos);
/*     */ 
/* 247 */     while (c == '(')
/*     */     {
/* 250 */       int start = ++this.currentPos; int nesting = 1;
/*     */ 
/* 252 */       for (; (nesting > 0) && (this.currentPos < this.maxPos); 
/* 252 */         this.currentPos += 1) {
/* 253 */         c = this.string.charAt(this.currentPos);
/* 254 */         if (c == '\\') {
/* 255 */           this.currentPos += 1;
/* 256 */           filter = true;
/* 257 */         } else if (c == '\r') {
/* 258 */           filter = true;
/* 259 */         } else if (c == '(') {
/* 260 */           nesting++;
/* 261 */         } else if (c == ')') {
/* 262 */           nesting--;
/*     */         }
/*     */       }
/* 264 */       if (nesting != 0) {
/* 265 */         throw new WebServiceException("Unbalanced comments");
/*     */       }
/* 267 */       if (!this.skipComments)
/*     */       {
/*     */         String s;
/*     */         String s;
/* 271 */         if (filter)
/* 272 */           s = filterToken(this.string, start, this.currentPos - 1);
/*     */         else {
/* 274 */           s = this.string.substring(start, this.currentPos - 1);
/*     */         }
/* 276 */         return new Token(-3, s);
/*     */       }
/*     */ 
/* 280 */       if (skipWhiteSpace() == -4)
/* 281 */         return EOFToken;
/* 282 */       c = this.string.charAt(this.currentPos);
/*     */     }
/*     */ 
/* 287 */     if (c == '"') {
/* 288 */       for (int start = ++this.currentPos; this.currentPos < this.maxPos; this.currentPos += 1) {
/* 289 */         c = this.string.charAt(this.currentPos);
/* 290 */         if (c == '\\') {
/* 291 */           this.currentPos += 1;
/* 292 */           filter = true;
/* 293 */         } else if (c == '\r') {
/* 294 */           filter = true;
/* 295 */         } else if (c == '"') {
/* 296 */           this.currentPos += 1;
/*     */           String s;
/*     */           String s;
/* 299 */           if (filter)
/* 300 */             s = filterToken(this.string, start, this.currentPos - 1);
/*     */           else {
/* 302 */             s = this.string.substring(start, this.currentPos - 1);
/*     */           }
/* 304 */           return new Token(-2, s);
/*     */         }
/*     */       }
/* 307 */       throw new WebServiceException("Unbalanced quoted string");
/*     */     }
/*     */ 
/* 311 */     if ((c < ' ') || (c >= '') || (this.delimiters.indexOf(c) >= 0)) {
/* 312 */       this.currentPos += 1;
/* 313 */       char[] ch = new char[1];
/* 314 */       ch[0] = c;
/* 315 */       return new Token(c, new String(ch));
/*     */     }
/*     */ 
/* 319 */     for (int start = this.currentPos; this.currentPos < this.maxPos; this.currentPos += 1) {
/* 320 */       c = this.string.charAt(this.currentPos);
/*     */ 
/* 323 */       if ((c < ' ') || (c >= '') || (c == '(') || (c == ' ') || (c == '"') || (this.delimiters.indexOf(c) >= 0)) {
/*     */         break;
/*     */       }
/*     */     }
/* 327 */     return new Token(-1, this.string.substring(start, this.currentPos));
/*     */   }
/*     */ 
/*     */   private int skipWhiteSpace()
/*     */   {
/* 333 */     for (; this.currentPos < this.maxPos; this.currentPos += 1)
/*     */     {
/*     */       char c;
/* 334 */       if (((c = this.string.charAt(this.currentPos)) != ' ') && (c != '\t') && (c != '\r') && (c != '\n'))
/*     */       {
/* 336 */         return this.currentPos; } 
/* 337 */     }return -4;
/*     */   }
/*     */ 
/*     */   private static String filterToken(String s, int start, int end)
/*     */   {
/* 344 */     StringBuffer sb = new StringBuffer();
/*     */ 
/* 346 */     boolean gotEscape = false;
/* 347 */     boolean gotCR = false;
/*     */ 
/* 349 */     for (int i = start; i < end; i++) {
/* 350 */       char c = s.charAt(i);
/* 351 */       if ((c == '\n') && (gotCR))
/*     */       {
/* 354 */         gotCR = false;
/*     */       }
/*     */       else
/*     */       {
/* 358 */         gotCR = false;
/* 359 */         if (!gotEscape)
/*     */         {
/* 361 */           if (c == '\\')
/* 362 */             gotEscape = true;
/* 363 */           else if (c == '\r')
/* 364 */             gotCR = true;
/*     */           else {
/* 366 */             sb.append(c);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 371 */           sb.append(c);
/* 372 */           gotEscape = false;
/*     */         }
/*     */       }
/*     */     }
/* 375 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static class Token
/*     */   {
/*     */     private int type;
/*     */     private String value;
/*     */     public static final int ATOM = -1;
/*     */     public static final int QUOTEDSTRING = -2;
/*     */     public static final int COMMENT = -3;
/*     */     public static final int EOF = -4;
/*     */ 
/*     */     public Token(int type, String value)
/*     */     {
/*  82 */       this.type = type;
/*  83 */       this.value = value;
/*     */     }
/*     */ 
/*     */     public int getType()
/*     */     {
/* 103 */       return this.type;
/*     */     }
/*     */ 
/*     */     public String getValue()
/*     */     {
/* 115 */       return this.value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.HeaderTokenizer
 * JD-Core Version:    0.6.2
 */