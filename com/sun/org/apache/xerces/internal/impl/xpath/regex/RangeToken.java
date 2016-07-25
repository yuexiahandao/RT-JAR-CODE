/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ final class RangeToken extends Token
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3257568399592010545L;
/*     */   int[] ranges;
/*     */   boolean sorted;
/*     */   boolean compacted;
/*  36 */   RangeToken icaseCache = null;
/*  37 */   int[] map = null;
/*     */   int nonMapIndex;
/*     */   private static final int MAPSIZE = 256;
/*     */ 
/*     */   RangeToken(int type)
/*     */   {
/*  41 */     super(type);
/*  42 */     setSorted(false);
/*     */   }
/*     */ 
/*     */   protected void addRange(int start, int end)
/*     */   {
/*  47 */     this.icaseCache = null;
/*     */     int r2;
/*     */     int r1;
/*     */     int r2;
/*  50 */     if (start <= end) {
/*  51 */       int r1 = start;
/*  52 */       r2 = end;
/*     */     } else {
/*  54 */       r1 = end;
/*  55 */       r2 = start;
/*     */     }
/*     */ 
/*  58 */     int pos = 0;
/*  59 */     if (this.ranges == null) {
/*  60 */       this.ranges = new int[2];
/*  61 */       this.ranges[0] = r1;
/*  62 */       this.ranges[1] = r2;
/*  63 */       setSorted(true);
/*     */     } else {
/*  65 */       pos = this.ranges.length;
/*  66 */       if (this.ranges[(pos - 1)] + 1 == r1) {
/*  67 */         this.ranges[(pos - 1)] = r2;
/*  68 */         return;
/*     */       }
/*  70 */       int[] temp = new int[pos + 2];
/*  71 */       System.arraycopy(this.ranges, 0, temp, 0, pos);
/*  72 */       this.ranges = temp;
/*  73 */       if (this.ranges[(pos - 1)] >= r1)
/*  74 */         setSorted(false);
/*  75 */       this.ranges[(pos++)] = r1;
/*  76 */       this.ranges[pos] = r2;
/*  77 */       if (!this.sorted)
/*  78 */         sortRanges();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean isSorted() {
/*  83 */     return this.sorted;
/*     */   }
/*     */   private final void setSorted(boolean sort) {
/*  86 */     this.sorted = sort;
/*  87 */     if (!sort) this.compacted = false; 
/*     */   }
/*     */ 
/*  90 */   private final boolean isCompacted() { return this.compacted; }
/*     */ 
/*     */   private final void setCompacted() {
/*  93 */     this.compacted = true;
/*     */   }
/*     */ 
/*     */   protected void sortRanges() {
/*  97 */     if (isSorted())
/*  98 */       return;
/*  99 */     if (this.ranges == null) {
/* 100 */       return;
/*     */     }
/*     */ 
/* 106 */     for (int i = this.ranges.length - 4; i >= 0; i -= 2) {
/* 107 */       for (int j = 0; j <= i; j += 2) {
/* 108 */         if ((this.ranges[j] > this.ranges[(j + 2)]) || ((this.ranges[j] == this.ranges[(j + 2)]) && (this.ranges[(j + 1)] > this.ranges[(j + 3)])))
/*     */         {
/* 111 */           int tmp = this.ranges[(j + 2)];
/* 112 */           this.ranges[(j + 2)] = this.ranges[j];
/* 113 */           this.ranges[j] = tmp;
/* 114 */           tmp = this.ranges[(j + 3)];
/* 115 */           this.ranges[(j + 3)] = this.ranges[(j + 1)];
/* 116 */           this.ranges[(j + 1)] = tmp;
/*     */         }
/*     */       }
/*     */     }
/* 120 */     setSorted(true);
/*     */   }
/*     */ 
/*     */   protected void compactRanges()
/*     */   {
/* 127 */     boolean DEBUG = false;
/* 128 */     if ((this.ranges == null) || (this.ranges.length <= 2))
/* 129 */       return;
/* 130 */     if (isCompacted())
/* 131 */       return;
/* 132 */     int base = 0;
/* 133 */     int target = 0;
/*     */ 
/* 135 */     while (target < this.ranges.length) {
/* 136 */       if (base != target) {
/* 137 */         this.ranges[base] = this.ranges[(target++)];
/* 138 */         this.ranges[(base + 1)] = this.ranges[(target++)];
/*     */       } else {
/* 140 */         target += 2;
/* 141 */       }int baseend = this.ranges[(base + 1)];
/* 142 */       while ((target < this.ranges.length) && 
/* 143 */         (baseend + 1 >= this.ranges[target]))
/*     */       {
/* 145 */         if (baseend + 1 == this.ranges[target]) {
/* 146 */           if (DEBUG) {
/* 147 */             System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[base] + ", " + this.ranges[(base + 1)] + "], [" + this.ranges[target] + ", " + this.ranges[(target + 1)] + "] -> [" + this.ranges[base] + ", " + this.ranges[(target + 1)] + "]");
/*     */           }
/*     */ 
/* 154 */           this.ranges[(base + 1)] = this.ranges[(target + 1)];
/* 155 */           baseend = this.ranges[(base + 1)];
/* 156 */           target += 2;
/* 157 */         } else if (baseend >= this.ranges[(target + 1)]) {
/* 158 */           if (DEBUG) {
/* 159 */             System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[base] + ", " + this.ranges[(base + 1)] + "], [" + this.ranges[target] + ", " + this.ranges[(target + 1)] + "] -> [" + this.ranges[base] + ", " + this.ranges[(base + 1)] + "]");
/*     */           }
/*     */ 
/* 166 */           target += 2;
/* 167 */         } else if (baseend < this.ranges[(target + 1)]) {
/* 168 */           if (DEBUG) {
/* 169 */             System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[base] + ", " + this.ranges[(base + 1)] + "], [" + this.ranges[target] + ", " + this.ranges[(target + 1)] + "] -> [" + this.ranges[base] + ", " + this.ranges[(target + 1)] + "]");
/*     */           }
/*     */ 
/* 176 */           this.ranges[(base + 1)] = this.ranges[(target + 1)];
/* 177 */           baseend = this.ranges[(base + 1)];
/* 178 */           target += 2;
/*     */         } else {
/* 180 */           throw new RuntimeException("Token#compactRanges(): Internel Error: [" + this.ranges[base] + "," + this.ranges[(base + 1)] + "] [" + this.ranges[target] + "," + this.ranges[(target + 1)] + "]");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 187 */       base += 2;
/*     */     }
/*     */ 
/* 190 */     if (base != this.ranges.length) {
/* 191 */       int[] result = new int[base];
/* 192 */       System.arraycopy(this.ranges, 0, result, 0, base);
/* 193 */       this.ranges = result;
/*     */     }
/* 195 */     setCompacted();
/*     */   }
/*     */ 
/*     */   protected void mergeRanges(Token token) {
/* 199 */     RangeToken tok = (RangeToken)token;
/* 200 */     sortRanges();
/* 201 */     tok.sortRanges();
/* 202 */     if (tok.ranges == null)
/* 203 */       return;
/* 204 */     this.icaseCache = null;
/* 205 */     setSorted(true);
/* 206 */     if (this.ranges == null) {
/* 207 */       this.ranges = new int[tok.ranges.length];
/* 208 */       System.arraycopy(tok.ranges, 0, this.ranges, 0, tok.ranges.length);
/* 209 */       return;
/*     */     }
/* 211 */     int[] result = new int[this.ranges.length + tok.ranges.length];
/* 212 */     int i = 0; int j = 0; for (int k = 0; (i < this.ranges.length) || (j < tok.ranges.length); ) {
/* 213 */       if (i >= this.ranges.length) {
/* 214 */         result[(k++)] = tok.ranges[(j++)];
/* 215 */         result[(k++)] = tok.ranges[(j++)];
/* 216 */       } else if (j >= tok.ranges.length) {
/* 217 */         result[(k++)] = this.ranges[(i++)];
/* 218 */         result[(k++)] = this.ranges[(i++)];
/* 219 */       } else if ((tok.ranges[j] < this.ranges[i]) || ((tok.ranges[j] == this.ranges[i]) && (tok.ranges[(j + 1)] < this.ranges[(i + 1)])))
/*     */       {
/* 221 */         result[(k++)] = tok.ranges[(j++)];
/* 222 */         result[(k++)] = tok.ranges[(j++)];
/*     */       } else {
/* 224 */         result[(k++)] = this.ranges[(i++)];
/* 225 */         result[(k++)] = this.ranges[(i++)];
/*     */       }
/*     */     }
/* 228 */     this.ranges = result;
/*     */   }
/*     */ 
/*     */   protected void subtractRanges(Token token) {
/* 232 */     if (token.type == 5) {
/* 233 */       intersectRanges(token);
/* 234 */       return;
/*     */     }
/* 236 */     RangeToken tok = (RangeToken)token;
/* 237 */     if ((tok.ranges == null) || (this.ranges == null))
/* 238 */       return;
/* 239 */     this.icaseCache = null;
/* 240 */     sortRanges();
/* 241 */     compactRanges();
/* 242 */     tok.sortRanges();
/* 243 */     tok.compactRanges();
/*     */ 
/* 247 */     int[] result = new int[this.ranges.length + tok.ranges.length];
/* 248 */     int wp = 0; int src = 0; int sub = 0;
/* 249 */     while ((src < this.ranges.length) && (sub < tok.ranges.length)) {
/* 250 */       int srcbegin = this.ranges[src];
/* 251 */       int srcend = this.ranges[(src + 1)];
/* 252 */       int subbegin = tok.ranges[sub];
/* 253 */       int subend = tok.ranges[(sub + 1)];
/* 254 */       if (srcend < subbegin)
/*     */       {
/* 259 */         result[(wp++)] = this.ranges[(src++)];
/* 260 */         result[(wp++)] = this.ranges[(src++)];
/* 261 */       } else if ((srcend >= subbegin) && (srcbegin <= subend))
/*     */       {
/* 268 */         if ((subbegin <= srcbegin) && (srcend <= subend))
/*     */         {
/* 273 */           src += 2;
/* 274 */         } else if (subbegin <= srcbegin)
/*     */         {
/* 279 */           this.ranges[src] = (subend + 1);
/* 280 */           sub += 2;
/* 281 */         } else if (srcend <= subend)
/*     */         {
/* 286 */           result[(wp++)] = srcbegin;
/* 287 */           result[(wp++)] = (subbegin - 1);
/* 288 */           src += 2;
/*     */         }
/*     */         else
/*     */         {
/* 294 */           result[(wp++)] = srcbegin;
/* 295 */           result[(wp++)] = (subbegin - 1);
/* 296 */           this.ranges[src] = (subend + 1);
/* 297 */           sub += 2;
/*     */         }
/* 299 */       } else if (subend < srcbegin)
/*     */       {
/* 303 */         sub += 2;
/*     */       } else {
/* 305 */         throw new RuntimeException("Token#subtractRanges(): Internal Error: [" + this.ranges[src] + "," + this.ranges[(src + 1)] + "] - [" + tok.ranges[sub] + "," + tok.ranges[(sub + 1)] + "]");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 312 */     while (src < this.ranges.length) {
/* 313 */       result[(wp++)] = this.ranges[(src++)];
/* 314 */       result[(wp++)] = this.ranges[(src++)];
/*     */     }
/* 316 */     this.ranges = new int[wp];
/* 317 */     System.arraycopy(result, 0, this.ranges, 0, wp);
/*     */   }
/*     */ 
/*     */   protected void intersectRanges(Token token)
/*     */   {
/* 325 */     RangeToken tok = (RangeToken)token;
/* 326 */     if ((tok.ranges == null) || (this.ranges == null))
/* 327 */       return;
/* 328 */     this.icaseCache = null;
/* 329 */     sortRanges();
/* 330 */     compactRanges();
/* 331 */     tok.sortRanges();
/* 332 */     tok.compactRanges();
/*     */ 
/* 334 */     int[] result = new int[this.ranges.length + tok.ranges.length];
/* 335 */     int wp = 0; int src1 = 0; int src2 = 0;
/* 336 */     while ((src1 < this.ranges.length) && (src2 < tok.ranges.length)) {
/* 337 */       int src1begin = this.ranges[src1];
/* 338 */       int src1end = this.ranges[(src1 + 1)];
/* 339 */       int src2begin = tok.ranges[src2];
/* 340 */       int src2end = tok.ranges[(src2 + 1)];
/* 341 */       if (src1end < src2begin)
/*     */       {
/* 346 */         src1 += 2;
/* 347 */       } else if ((src1end >= src2begin) && (src1begin <= src2end))
/*     */       {
/* 354 */         if ((src2begin <= src2begin) && (src1end <= src2end))
/*     */         {
/* 359 */           result[(wp++)] = src1begin;
/* 360 */           result[(wp++)] = src1end;
/* 361 */           src1 += 2;
/* 362 */         } else if (src2begin <= src1begin)
/*     */         {
/* 367 */           result[(wp++)] = src1begin;
/* 368 */           result[(wp++)] = src2end;
/* 369 */           this.ranges[src1] = (src2end + 1);
/* 370 */           src2 += 2;
/* 371 */         } else if (src1end <= src2end)
/*     */         {
/* 376 */           result[(wp++)] = src2begin;
/* 377 */           result[(wp++)] = src1end;
/* 378 */           src1 += 2;
/*     */         }
/*     */         else
/*     */         {
/* 384 */           result[(wp++)] = src2begin;
/* 385 */           result[(wp++)] = src2end;
/* 386 */           this.ranges[src1] = (src2end + 1);
/*     */         }
/* 388 */       } else if (src2end < src1begin)
/*     */       {
/* 392 */         src2 += 2;
/*     */       }
/* 394 */       else throw new RuntimeException("Token#intersectRanges(): Internal Error: [" + this.ranges[src1] + "," + this.ranges[(src1 + 1)] + "] & [" + tok.ranges[src2] + "," + tok.ranges[(src2 + 1)] + "]");
/*     */ 
/*     */     }
/*     */ 
/* 402 */     while (src1 < this.ranges.length) {
/* 403 */       result[(wp++)] = this.ranges[(src1++)];
/* 404 */       result[(wp++)] = this.ranges[(src1++)];
/*     */     }
/* 406 */     this.ranges = new int[wp];
/* 407 */     System.arraycopy(result, 0, this.ranges, 0, wp);
/*     */   }
/*     */ 
/*     */   static Token complementRanges(Token token)
/*     */   {
/* 416 */     if ((token.type != 4) && (token.type != 5))
/* 417 */       throw new IllegalArgumentException("Token#complementRanges(): must be RANGE: " + token.type);
/* 418 */     RangeToken tok = (RangeToken)token;
/* 419 */     tok.sortRanges();
/* 420 */     tok.compactRanges();
/* 421 */     int len = tok.ranges.length + 2;
/* 422 */     if (tok.ranges[0] == 0)
/* 423 */       len -= 2;
/* 424 */     int last = tok.ranges[(tok.ranges.length - 1)];
/* 425 */     if (last == 1114111)
/* 426 */       len -= 2;
/* 427 */     RangeToken ret = Token.createRange();
/* 428 */     ret.ranges = new int[len];
/* 429 */     int wp = 0;
/* 430 */     if (tok.ranges[0] > 0) {
/* 431 */       ret.ranges[(wp++)] = 0;
/* 432 */       ret.ranges[(wp++)] = (tok.ranges[0] - 1);
/*     */     }
/* 434 */     for (int i = 1; i < tok.ranges.length - 2; i += 2) {
/* 435 */       ret.ranges[(wp++)] = (tok.ranges[i] + 1);
/* 436 */       ret.ranges[(wp++)] = (tok.ranges[(i + 1)] - 1);
/*     */     }
/* 438 */     if (last != 1114111) {
/* 439 */       ret.ranges[(wp++)] = (last + 1);
/* 440 */       ret.ranges[wp] = 1114111;
/*     */     }
/* 442 */     ret.setCompacted();
/* 443 */     return ret;
/*     */   }
/*     */ 
/*     */   synchronized RangeToken getCaseInsensitiveToken() {
/* 447 */     if (this.icaseCache != null) {
/* 448 */       return this.icaseCache;
/*     */     }
/* 450 */     RangeToken uppers = this.type == 4 ? Token.createRange() : Token.createNRange();
/* 451 */     for (int i = 0; i < this.ranges.length; i += 2) {
/* 452 */       for (int ch = this.ranges[i]; ch <= this.ranges[(i + 1)]; ch++) {
/* 453 */         if (ch > 65535) {
/* 454 */           uppers.addRange(ch, ch);
/*     */         } else {
/* 456 */           char uch = Character.toUpperCase((char)ch);
/* 457 */           uppers.addRange(uch, uch);
/*     */         }
/*     */       }
/*     */     }
/* 461 */     RangeToken lowers = this.type == 4 ? Token.createRange() : Token.createNRange();
/* 462 */     for (int i = 0; i < uppers.ranges.length; i += 2) {
/* 463 */       for (int ch = uppers.ranges[i]; ch <= uppers.ranges[(i + 1)]; ch++) {
/* 464 */         if (ch > 65535) {
/* 465 */           lowers.addRange(ch, ch);
/*     */         } else {
/* 467 */           char uch = Character.toUpperCase((char)ch);
/* 468 */           lowers.addRange(uch, uch);
/*     */         }
/*     */       }
/*     */     }
/* 472 */     lowers.mergeRanges(uppers);
/* 473 */     lowers.mergeRanges(this);
/* 474 */     lowers.compactRanges();
/*     */ 
/* 476 */     this.icaseCache = lowers;
/* 477 */     return lowers;
/*     */   }
/*     */ 
/*     */   void dumpRanges() {
/* 481 */     System.err.print("RANGE: ");
/* 482 */     if (this.ranges == null)
/* 483 */       System.err.println(" NULL");
/* 484 */     for (int i = 0; i < this.ranges.length; i += 2) {
/* 485 */       System.err.print("[" + this.ranges[i] + "," + this.ranges[(i + 1)] + "] ");
/*     */     }
/* 487 */     System.err.println("");
/*     */   }
/*     */ 
/*     */   boolean match(int ch) {
/* 491 */     if (this.map == null) createMap();
/*     */     boolean ret;
/* 493 */     if (this.type == 4) {
/* 494 */       if (ch < 256)
/* 495 */         return (this.map[(ch / 32)] & 1 << (ch & 0x1F)) != 0;
/* 496 */       boolean ret = false;
/* 497 */       for (int i = this.nonMapIndex; i < this.ranges.length; i += 2)
/* 498 */         if ((this.ranges[i] <= ch) && (ch <= this.ranges[(i + 1)]))
/* 499 */           return true;
/*     */     }
/*     */     else {
/* 502 */       if (ch < 256)
/* 503 */         return (this.map[(ch / 32)] & 1 << (ch & 0x1F)) == 0;
/* 504 */       ret = true;
/* 505 */       for (int i = this.nonMapIndex; i < this.ranges.length; i += 2) {
/* 506 */         if ((this.ranges[i] <= ch) && (ch <= this.ranges[(i + 1)]))
/* 507 */           return false;
/*     */       }
/*     */     }
/* 510 */     return ret;
/*     */   }
/*     */ 
/*     */   private void createMap()
/*     */   {
/* 515 */     int asize = 8;
/* 516 */     int[] map = new int[asize];
/* 517 */     int nonMapIndex = this.ranges.length;
/* 518 */     for (int i = 0; i < asize; i++) {
/* 519 */       map[i] = 0;
/*     */     }
/* 521 */     for (int i = 0; i < this.ranges.length; i += 2) {
/* 522 */       int s = this.ranges[i];
/* 523 */       int e = this.ranges[(i + 1)];
/* 524 */       if (s < 256) {
/* 525 */         for (int j = s; (j <= e) && (j < 256); j++)
/* 526 */           map[(j / 32)] |= 1 << (j & 0x1F);
/*     */       }
/*     */       else
/*     */       {
/* 530 */         nonMapIndex = i;
/* 531 */         break;
/*     */       }
/* 533 */       if (e >= 256) {
/* 534 */         nonMapIndex = i;
/* 535 */         break;
/*     */       }
/*     */     }
/* 538 */     this.map = map;
/* 539 */     this.nonMapIndex = nonMapIndex;
/*     */   }
/*     */ 
/*     */   public String toString(int options)
/*     */   {
/*     */     String ret;
/*     */     String ret;
/* 545 */     if (this.type == 4)
/*     */     {
/*     */       String ret;
/* 546 */       if (this == Token.token_dot) {
/* 547 */         ret = ".";
/*     */       }
/*     */       else
/*     */       {
/*     */         String ret;
/* 548 */         if (this == Token.token_0to9) {
/* 549 */           ret = "\\d";
/*     */         }
/*     */         else
/*     */         {
/*     */           String ret;
/* 550 */           if (this == Token.token_wordchars) {
/* 551 */             ret = "\\w";
/*     */           }
/*     */           else
/*     */           {
/*     */             String ret;
/* 552 */             if (this == Token.token_spaces) {
/* 553 */               ret = "\\s";
/*     */             } else {
/* 555 */               StringBuffer sb = new StringBuffer();
/* 556 */               sb.append("[");
/* 557 */               for (int i = 0; i < this.ranges.length; i += 2) {
/* 558 */                 if (((options & 0x400) != 0) && (i > 0)) sb.append(",");
/* 559 */                 if (this.ranges[i] == this.ranges[(i + 1)]) {
/* 560 */                   sb.append(escapeCharInCharClass(this.ranges[i]));
/*     */                 } else {
/* 562 */                   sb.append(escapeCharInCharClass(this.ranges[i]));
/* 563 */                   sb.append('-');
/* 564 */                   sb.append(escapeCharInCharClass(this.ranges[(i + 1)]));
/*     */                 }
/*     */               }
/* 567 */               sb.append("]");
/* 568 */               ret = sb.toString();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       String ret;
/* 571 */       if (this == Token.token_not_0to9) {
/* 572 */         ret = "\\D";
/*     */       }
/*     */       else
/*     */       {
/*     */         String ret;
/* 573 */         if (this == Token.token_not_wordchars) {
/* 574 */           ret = "\\W";
/*     */         }
/*     */         else
/*     */         {
/*     */           String ret;
/* 575 */           if (this == Token.token_not_spaces) {
/* 576 */             ret = "\\S";
/*     */           } else {
/* 578 */             StringBuffer sb = new StringBuffer();
/* 579 */             sb.append("[^");
/* 580 */             for (int i = 0; i < this.ranges.length; i += 2) {
/* 581 */               if (((options & 0x400) != 0) && (i > 0)) sb.append(",");
/* 582 */               if (this.ranges[i] == this.ranges[(i + 1)]) {
/* 583 */                 sb.append(escapeCharInCharClass(this.ranges[i]));
/*     */               } else {
/* 585 */                 sb.append(escapeCharInCharClass(this.ranges[i]));
/* 586 */                 sb.append('-');
/* 587 */                 sb.append(escapeCharInCharClass(this.ranges[(i + 1)]));
/*     */               }
/*     */             }
/* 590 */             sb.append("]");
/* 591 */             ret = sb.toString();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 594 */     return ret;
/*     */   }
/*     */ 
/*     */   private static String escapeCharInCharClass(int ch)
/*     */   {
/*     */     String ret;
/*     */     String ret;
/* 599 */     switch (ch) { case 44:
/*     */     case 45:
/*     */     case 91:
/*     */     case 92:
/*     */     case 93:
/*     */     case 94:
/* 602 */       ret = "\\" + (char)ch;
/* 603 */       break;
/*     */     case 12:
/* 604 */       ret = "\\f"; break;
/*     */     case 10:
/* 605 */       ret = "\\n"; break;
/*     */     case 13:
/* 606 */       ret = "\\r"; break;
/*     */     case 9:
/* 607 */       ret = "\\t"; break;
/*     */     case 27:
/* 608 */       ret = "\\e"; break;
/*     */     default:
/* 611 */       if (ch < 32) {
/* 612 */         String pre = "0" + Integer.toHexString(ch);
/* 613 */         ret = "\\x" + pre.substring(pre.length() - 2, pre.length());
/*     */       }
/*     */       else
/*     */       {
/*     */         String ret;
/* 614 */         if (ch >= 65536) {
/* 615 */           String pre = "0" + Integer.toHexString(ch);
/* 616 */           ret = "\\v" + pre.substring(pre.length() - 6, pre.length());
/*     */         } else {
/* 618 */           ret = "" + (char)ch;
/*     */         }
/*     */       }break; } return ret;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.RangeToken
 * JD-Core Version:    0.6.2
 */