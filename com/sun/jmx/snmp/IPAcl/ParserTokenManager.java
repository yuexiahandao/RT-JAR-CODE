/*      */ package com.sun.jmx.snmp.IPAcl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ 
/*      */ class ParserTokenManager
/*      */   implements ParserConstants
/*      */ {
/*  751 */   static final long[] jjbitVec0 = { 0L, 0L, -1L, -1L };
/*      */ 
/* 1363 */   static final int[] jjnextStates = { 18, 19, 21, 28, 29, 39, 23, 24, 26, 27, 41, 42, 7, 8, 10, 18, 20, 21, 44, 46, 13, 1, 2, 4, 37, 28, 38, 26, 27, 37, 28, 38, 15, 16 };
/*      */ 
/* 1368 */   public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, "access", "acl", "=", "communities", "enterprise", "hosts", "{", "managers", "-", "}", "read-only", "read-write", "trap", "inform", "trap-community", "inform-community", "trap-num", null, null, null, null, null, null, null, null, null, null, null, null, ",", ".", "!", "/" };
/*      */ 
/* 1377 */   public static final String[] lexStateNames = { "DEFAULT" };
/*      */ 
/* 1380 */   static final long[] jjtoToken = { 1067601362817L };
/*      */ 
/* 1383 */   static final long[] jjtoSkip = { 126L };
/*      */   private ASCII_CharStream input_stream;
/* 1387 */   private final int[] jjrounds = new int[47];
/* 1388 */   private final int[] jjstateSet = new int[94];
/*      */   protected char curChar;
/* 1441 */   int curLexState = 0;
/* 1442 */   int defaultLexState = 0;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */ 
/*      */   private final int jjStopStringLiteralDfa_0(int paramInt, long paramLong)
/*      */   {
/*   34 */     switch (paramInt)
/*      */     {
/*      */     case 0:
/*   37 */       if ((paramLong & 0x8000) != 0L)
/*   38 */         return 0;
/*   39 */       if ((paramLong & 0xFE5000) != 0L)
/*      */       {
/*   41 */         this.jjmatchedKind = 31;
/*   42 */         return 47;
/*      */       }
/*   44 */       if ((paramLong & 0xD80) != 0L)
/*      */       {
/*   46 */         this.jjmatchedKind = 31;
/*   47 */         return 48;
/*      */       }
/*   49 */       return -1;
/*      */     case 1:
/*   51 */       if ((paramLong & 0xFE5C00) != 0L)
/*      */       {
/*   53 */         this.jjmatchedKind = 31;
/*   54 */         this.jjmatchedPos = 1;
/*   55 */         return 49;
/*      */       }
/*   57 */       if ((paramLong & 0x180) != 0L)
/*      */       {
/*   59 */         this.jjmatchedKind = 31;
/*   60 */         this.jjmatchedPos = 1;
/*   61 */         return 50;
/*      */       }
/*   63 */       return -1;
/*      */     case 2:
/*   65 */       if ((paramLong & 0xFE5C00) != 0L)
/*      */       {
/*   67 */         this.jjmatchedKind = 31;
/*   68 */         this.jjmatchedPos = 2;
/*   69 */         return 49;
/*      */       }
/*   71 */       if ((paramLong & 0x100) != 0L)
/*   72 */         return 49;
/*   73 */       if ((paramLong & 0x80) != 0L)
/*      */       {
/*   75 */         this.jjmatchedKind = 31;
/*   76 */         this.jjmatchedPos = 2;
/*   77 */         return 50;
/*      */       }
/*   79 */       return -1;
/*      */     case 3:
/*   81 */       if ((paramLong & 0x565C00) != 0L)
/*      */       {
/*   83 */         if (this.jjmatchedPos != 3)
/*      */         {
/*   85 */           this.jjmatchedKind = 31;
/*   86 */           this.jjmatchedPos = 3;
/*      */         }
/*   88 */         return 49;
/*      */       }
/*   90 */       if ((paramLong & 0xA80000) != 0L)
/*   91 */         return 49;
/*   92 */       if ((paramLong & 0x80) != 0L)
/*      */       {
/*   94 */         if (this.jjmatchedPos != 3)
/*      */         {
/*   96 */           this.jjmatchedKind = 31;
/*   97 */           this.jjmatchedPos = 3;
/*      */         }
/*   99 */         return 50;
/*      */       }
/*  101 */       return -1;
/*      */     case 4:
/*  103 */       if ((paramLong & 0xA00000) != 0L)
/*  104 */         return 51;
/*  105 */       if ((paramLong & 0x60000) != 0L)
/*      */       {
/*  107 */         if (this.jjmatchedPos < 3)
/*      */         {
/*  109 */           this.jjmatchedKind = 31;
/*  110 */           this.jjmatchedPos = 3;
/*      */         }
/*  112 */         return 51;
/*      */       }
/*  114 */       if ((paramLong & 0x1000) != 0L)
/*  115 */         return 49;
/*  116 */       if ((paramLong & 0x504C80) != 0L)
/*      */       {
/*  118 */         this.jjmatchedKind = 31;
/*  119 */         this.jjmatchedPos = 4;
/*  120 */         return 49;
/*      */       }
/*  122 */       return -1;
/*      */     case 5:
/*  124 */       if ((paramLong & 0x500080) != 0L)
/*  125 */         return 49;
/*  126 */       if ((paramLong & 0x4C00) != 0L)
/*      */       {
/*  128 */         if (this.jjmatchedPos != 5)
/*      */         {
/*  130 */           this.jjmatchedKind = 31;
/*  131 */           this.jjmatchedPos = 5;
/*      */         }
/*  133 */         return 49;
/*      */       }
/*  135 */       if ((paramLong & 0xA60000) != 0L)
/*      */       {
/*  137 */         if (this.jjmatchedPos != 5)
/*      */         {
/*  139 */           this.jjmatchedKind = 31;
/*  140 */           this.jjmatchedPos = 5;
/*      */         }
/*  142 */         return 51;
/*      */       }
/*  144 */       return -1;
/*      */     case 6:
/*  146 */       if ((paramLong & 0x400000) != 0L)
/*  147 */         return 51;
/*  148 */       if ((paramLong & 0x4C00) != 0L)
/*      */       {
/*  150 */         this.jjmatchedKind = 31;
/*  151 */         this.jjmatchedPos = 6;
/*  152 */         return 49;
/*      */       }
/*  154 */       if ((paramLong & 0xA60000) != 0L)
/*      */       {
/*  156 */         this.jjmatchedKind = 31;
/*  157 */         this.jjmatchedPos = 6;
/*  158 */         return 51;
/*      */       }
/*  160 */       return -1;
/*      */     case 7:
/*  162 */       if ((paramLong & 0x660000) != 0L)
/*      */       {
/*  164 */         this.jjmatchedKind = 31;
/*  165 */         this.jjmatchedPos = 7;
/*  166 */         return 51;
/*      */       }
/*  168 */       if ((paramLong & 0x800000) != 0L)
/*  169 */         return 51;
/*  170 */       if ((paramLong & 0x4000) != 0L)
/*  171 */         return 49;
/*  172 */       if ((paramLong & 0xC00) != 0L)
/*      */       {
/*  174 */         this.jjmatchedKind = 31;
/*  175 */         this.jjmatchedPos = 7;
/*  176 */         return 49;
/*      */       }
/*  178 */       return -1;
/*      */     case 8:
/*  180 */       if ((paramLong & 0x20000) != 0L)
/*  181 */         return 51;
/*  182 */       if ((paramLong & 0xC00) != 0L)
/*      */       {
/*  184 */         this.jjmatchedKind = 31;
/*  185 */         this.jjmatchedPos = 8;
/*  186 */         return 49;
/*      */       }
/*  188 */       if ((paramLong & 0x640000) != 0L)
/*      */       {
/*  190 */         this.jjmatchedKind = 31;
/*  191 */         this.jjmatchedPos = 8;
/*  192 */         return 51;
/*      */       }
/*  194 */       return -1;
/*      */     case 9:
/*  196 */       if ((paramLong & 0x40000) != 0L)
/*  197 */         return 51;
/*  198 */       if ((paramLong & 0x800) != 0L)
/*  199 */         return 49;
/*  200 */       if ((paramLong & 0x600000) != 0L)
/*      */       {
/*  202 */         this.jjmatchedKind = 31;
/*  203 */         this.jjmatchedPos = 9;
/*  204 */         return 51;
/*      */       }
/*  206 */       if ((paramLong & 0x400) != 0L)
/*      */       {
/*  208 */         this.jjmatchedKind = 31;
/*  209 */         this.jjmatchedPos = 9;
/*  210 */         return 49;
/*      */       }
/*  212 */       return -1;
/*      */     case 10:
/*  214 */       if ((paramLong & 0x600000) != 0L)
/*      */       {
/*  216 */         this.jjmatchedKind = 31;
/*  217 */         this.jjmatchedPos = 10;
/*  218 */         return 51;
/*      */       }
/*  220 */       if ((paramLong & 0x400) != 0L)
/*  221 */         return 49;
/*  222 */       return -1;
/*      */     case 11:
/*  224 */       if ((paramLong & 0x600000) != 0L)
/*      */       {
/*  226 */         this.jjmatchedKind = 31;
/*  227 */         this.jjmatchedPos = 11;
/*  228 */         return 51;
/*      */       }
/*  230 */       return -1;
/*      */     case 12:
/*  232 */       if ((paramLong & 0x600000) != 0L)
/*      */       {
/*  234 */         this.jjmatchedKind = 31;
/*  235 */         this.jjmatchedPos = 12;
/*  236 */         return 51;
/*      */       }
/*  238 */       return -1;
/*      */     case 13:
/*  240 */       if ((paramLong & 0x400000) != 0L)
/*      */       {
/*  242 */         this.jjmatchedKind = 31;
/*  243 */         this.jjmatchedPos = 13;
/*  244 */         return 51;
/*      */       }
/*  246 */       if ((paramLong & 0x200000) != 0L)
/*  247 */         return 51;
/*  248 */       return -1;
/*      */     case 14:
/*  250 */       if ((paramLong & 0x400000) != 0L)
/*      */       {
/*  252 */         this.jjmatchedKind = 31;
/*  253 */         this.jjmatchedPos = 14;
/*  254 */         return 51;
/*      */       }
/*  256 */       return -1;
/*      */     }
/*  258 */     return -1;
/*      */   }
/*      */ 
/*      */   private final int jjStartNfa_0(int paramInt, long paramLong)
/*      */   {
/*  263 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(paramInt, paramLong), paramInt + 1);
/*      */   }
/*      */ 
/*      */   private final int jjStopAtPos(int paramInt1, int paramInt2) {
/*  267 */     this.jjmatchedKind = paramInt2;
/*  268 */     this.jjmatchedPos = paramInt1;
/*  269 */     return paramInt1 + 1;
/*      */   }
/*      */ 
/*      */   private final int jjStartNfaWithStates_0(int paramInt1, int paramInt2, int paramInt3) {
/*  273 */     this.jjmatchedKind = paramInt2;
/*  274 */     this.jjmatchedPos = paramInt1;
/*      */     try { this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {
/*  276 */       return paramInt1 + 1;
/*  277 */     }return jjMoveNfa_0(paramInt3, paramInt1 + 1);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  281 */     switch (this.curChar)
/*      */     {
/*      */     case '!':
/*  284 */       return jjStopAtPos(0, 38);
/*      */     case ',':
/*  286 */       return jjStopAtPos(0, 36);
/*      */     case '-':
/*  288 */       return jjStartNfaWithStates_0(0, 15, 0);
/*      */     case '.':
/*  290 */       return jjStopAtPos(0, 37);
/*      */     case '/':
/*  292 */       return jjStopAtPos(0, 39);
/*      */     case '=':
/*  294 */       return jjStopAtPos(0, 9);
/*      */     case 'a':
/*  296 */       return jjMoveStringLiteralDfa1_0(384L);
/*      */     case 'c':
/*  298 */       return jjMoveStringLiteralDfa1_0(1024L);
/*      */     case 'e':
/*  300 */       return jjMoveStringLiteralDfa1_0(2048L);
/*      */     case 'h':
/*  302 */       return jjMoveStringLiteralDfa1_0(4096L);
/*      */     case 'i':
/*  304 */       return jjMoveStringLiteralDfa1_0(5242880L);
/*      */     case 'm':
/*  306 */       return jjMoveStringLiteralDfa1_0(16384L);
/*      */     case 'r':
/*  308 */       return jjMoveStringLiteralDfa1_0(393216L);
/*      */     case 't':
/*  310 */       return jjMoveStringLiteralDfa1_0(11010048L);
/*      */     case '{':
/*  312 */       return jjStopAtPos(0, 13);
/*      */     case '}':
/*  314 */       return jjStopAtPos(0, 16);
/*      */     }
/*  316 */     return jjMoveNfa_0(5, 0);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa1_0(long paramLong) {
/*      */     try {
/*  321 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  323 */       jjStopStringLiteralDfa_0(0, paramLong);
/*  324 */       return 1;
/*      */     }
/*  326 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  329 */       return jjMoveStringLiteralDfa2_0(paramLong, 16384L);
/*      */     case 'c':
/*  331 */       return jjMoveStringLiteralDfa2_0(paramLong, 384L);
/*      */     case 'e':
/*  333 */       return jjMoveStringLiteralDfa2_0(paramLong, 393216L);
/*      */     case 'n':
/*  335 */       return jjMoveStringLiteralDfa2_0(paramLong, 5244928L);
/*      */     case 'o':
/*  337 */       return jjMoveStringLiteralDfa2_0(paramLong, 5120L);
/*      */     case 'r':
/*  339 */       return jjMoveStringLiteralDfa2_0(paramLong, 11010048L);
/*      */     case 'b':
/*      */     case 'd':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'm':
/*      */     case 'p':
/*  343 */     case 'q': } return jjStartNfa_0(0, paramLong);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa2_0(long paramLong1, long paramLong2) {
/*  347 */     if ((paramLong2 &= paramLong1) == 0L)
/*  348 */       return jjStartNfa_0(0, paramLong1); try {
/*  349 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  351 */       jjStopStringLiteralDfa_0(1, paramLong2);
/*  352 */       return 2;
/*      */     }
/*  354 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  357 */       return jjMoveStringLiteralDfa3_0(paramLong2, 11403264L);
/*      */     case 'c':
/*  359 */       return jjMoveStringLiteralDfa3_0(paramLong2, 128L);
/*      */     case 'f':
/*  361 */       return jjMoveStringLiteralDfa3_0(paramLong2, 5242880L);
/*      */     case 'l':
/*  363 */       if ((paramLong2 & 0x100) != 0L)
/*  364 */         return jjStartNfaWithStates_0(2, 8, 49);
/*      */       break;
/*      */     case 'm':
/*  367 */       return jjMoveStringLiteralDfa3_0(paramLong2, 1024L);
/*      */     case 'n':
/*  369 */       return jjMoveStringLiteralDfa3_0(paramLong2, 16384L);
/*      */     case 's':
/*  371 */       return jjMoveStringLiteralDfa3_0(paramLong2, 4096L);
/*      */     case 't':
/*  373 */       return jjMoveStringLiteralDfa3_0(paramLong2, 2048L);
/*      */     case 'b':
/*      */     case 'd':
/*      */     case 'e':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'o':
/*      */     case 'p':
/*      */     case 'q':
/*  377 */     case 'r': } return jjStartNfa_0(1, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa3_0(long paramLong1, long paramLong2) {
/*  381 */     if ((paramLong2 &= paramLong1) == 0L)
/*  382 */       return jjStartNfa_0(1, paramLong1); try {
/*  383 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  385 */       jjStopStringLiteralDfa_0(2, paramLong2);
/*  386 */       return 3;
/*      */     }
/*  388 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  391 */       return jjMoveStringLiteralDfa4_0(paramLong2, 16384L);
/*      */     case 'd':
/*  393 */       return jjMoveStringLiteralDfa4_0(paramLong2, 393216L);
/*      */     case 'e':
/*  395 */       return jjMoveStringLiteralDfa4_0(paramLong2, 2176L);
/*      */     case 'm':
/*  397 */       return jjMoveStringLiteralDfa4_0(paramLong2, 1024L);
/*      */     case 'o':
/*  399 */       return jjMoveStringLiteralDfa4_0(paramLong2, 5242880L);
/*      */     case 'p':
/*  401 */       if ((paramLong2 & 0x80000) != 0L)
/*      */       {
/*  403 */         this.jjmatchedKind = 19;
/*  404 */         this.jjmatchedPos = 3;
/*      */       }
/*  406 */       return jjMoveStringLiteralDfa4_0(paramLong2, 10485760L);
/*      */     case 't':
/*  408 */       return jjMoveStringLiteralDfa4_0(paramLong2, 4096L);
/*      */     case 'b':
/*      */     case 'c':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'n':
/*      */     case 'q':
/*      */     case 'r':
/*  412 */     case 's': } return jjStartNfa_0(2, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa4_0(long paramLong1, long paramLong2) {
/*  416 */     if ((paramLong2 &= paramLong1) == 0L)
/*  417 */       return jjStartNfa_0(2, paramLong1); try {
/*  418 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  420 */       jjStopStringLiteralDfa_0(3, paramLong2);
/*  421 */       return 4;
/*      */     }
/*  423 */     switch (this.curChar)
/*      */     {
/*      */     case '-':
/*  426 */       return jjMoveStringLiteralDfa5_0(paramLong2, 10878976L);
/*      */     case 'g':
/*  428 */       return jjMoveStringLiteralDfa5_0(paramLong2, 16384L);
/*      */     case 'r':
/*  430 */       return jjMoveStringLiteralDfa5_0(paramLong2, 5244928L);
/*      */     case 's':
/*  432 */       if ((paramLong2 & 0x1000) != 0L)
/*  433 */         return jjStartNfaWithStates_0(4, 12, 49);
/*  434 */       return jjMoveStringLiteralDfa5_0(paramLong2, 128L);
/*      */     case 'u':
/*  436 */       return jjMoveStringLiteralDfa5_0(paramLong2, 1024L);
/*      */     }
/*      */ 
/*  440 */     return jjStartNfa_0(3, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa5_0(long paramLong1, long paramLong2) {
/*  444 */     if ((paramLong2 &= paramLong1) == 0L)
/*  445 */       return jjStartNfa_0(3, paramLong1); try {
/*  446 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  448 */       jjStopStringLiteralDfa_0(4, paramLong2);
/*  449 */       return 5;
/*      */     }
/*  451 */     switch (this.curChar)
/*      */     {
/*      */     case 'c':
/*  454 */       return jjMoveStringLiteralDfa6_0(paramLong2, 2097152L);
/*      */     case 'e':
/*  456 */       return jjMoveStringLiteralDfa6_0(paramLong2, 16384L);
/*      */     case 'm':
/*  458 */       if ((paramLong2 & 0x100000) != 0L)
/*      */       {
/*  460 */         this.jjmatchedKind = 20;
/*  461 */         this.jjmatchedPos = 5;
/*      */       }
/*  463 */       return jjMoveStringLiteralDfa6_0(paramLong2, 4194304L);
/*      */     case 'n':
/*  465 */       return jjMoveStringLiteralDfa6_0(paramLong2, 8389632L);
/*      */     case 'o':
/*  467 */       return jjMoveStringLiteralDfa6_0(paramLong2, 131072L);
/*      */     case 'p':
/*  469 */       return jjMoveStringLiteralDfa6_0(paramLong2, 2048L);
/*      */     case 's':
/*  471 */       if ((paramLong2 & 0x80) != 0L)
/*  472 */         return jjStartNfaWithStates_0(5, 7, 49);
/*      */       break;
/*      */     case 'w':
/*  475 */       return jjMoveStringLiteralDfa6_0(paramLong2, 262144L);
/*      */     case 'd':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'q':
/*      */     case 'r':
/*      */     case 't':
/*      */     case 'u':
/*  479 */     case 'v': } return jjStartNfa_0(4, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa6_0(long paramLong1, long paramLong2) {
/*  483 */     if ((paramLong2 &= paramLong1) == 0L)
/*  484 */       return jjStartNfa_0(4, paramLong1); try {
/*  485 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  487 */       jjStopStringLiteralDfa_0(5, paramLong2);
/*  488 */       return 6;
/*      */     }
/*  490 */     switch (this.curChar)
/*      */     {
/*      */     case '-':
/*  493 */       return jjMoveStringLiteralDfa7_0(paramLong2, 4194304L);
/*      */     case 'i':
/*  495 */       return jjMoveStringLiteralDfa7_0(paramLong2, 1024L);
/*      */     case 'n':
/*  497 */       return jjMoveStringLiteralDfa7_0(paramLong2, 131072L);
/*      */     case 'o':
/*  499 */       return jjMoveStringLiteralDfa7_0(paramLong2, 2097152L);
/*      */     case 'r':
/*  501 */       return jjMoveStringLiteralDfa7_0(paramLong2, 280576L);
/*      */     case 'u':
/*  503 */       return jjMoveStringLiteralDfa7_0(paramLong2, 8388608L);
/*      */     }
/*      */ 
/*  507 */     return jjStartNfa_0(5, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa7_0(long paramLong1, long paramLong2) {
/*  511 */     if ((paramLong2 &= paramLong1) == 0L)
/*  512 */       return jjStartNfa_0(5, paramLong1); try {
/*  513 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  515 */       jjStopStringLiteralDfa_0(6, paramLong2);
/*  516 */       return 7;
/*      */     }
/*  518 */     switch (this.curChar)
/*      */     {
/*      */     case 'c':
/*  521 */       return jjMoveStringLiteralDfa8_0(paramLong2, 4194304L);
/*      */     case 'i':
/*  523 */       return jjMoveStringLiteralDfa8_0(paramLong2, 264192L);
/*      */     case 'l':
/*  525 */       return jjMoveStringLiteralDfa8_0(paramLong2, 131072L);
/*      */     case 'm':
/*  527 */       if ((paramLong2 & 0x800000) != 0L)
/*  528 */         return jjStartNfaWithStates_0(7, 23, 51);
/*  529 */       return jjMoveStringLiteralDfa8_0(paramLong2, 2097152L);
/*      */     case 's':
/*  531 */       if ((paramLong2 & 0x4000) != 0L)
/*  532 */         return jjStartNfaWithStates_0(7, 14, 49);
/*      */       break;
/*      */     case 't':
/*  535 */       return jjMoveStringLiteralDfa8_0(paramLong2, 1024L);
/*      */     case 'd':
/*      */     case 'e':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'n':
/*      */     case 'o':
/*      */     case 'p':
/*      */     case 'q':
/*  539 */     case 'r': } return jjStartNfa_0(6, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa8_0(long paramLong1, long paramLong2) {
/*  543 */     if ((paramLong2 &= paramLong1) == 0L)
/*  544 */       return jjStartNfa_0(6, paramLong1); try {
/*  545 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  547 */       jjStopStringLiteralDfa_0(7, paramLong2);
/*  548 */       return 8;
/*      */     }
/*  550 */     switch (this.curChar)
/*      */     {
/*      */     case 'i':
/*  553 */       return jjMoveStringLiteralDfa9_0(paramLong2, 1024L);
/*      */     case 'm':
/*  555 */       return jjMoveStringLiteralDfa9_0(paramLong2, 2097152L);
/*      */     case 'o':
/*  557 */       return jjMoveStringLiteralDfa9_0(paramLong2, 4194304L);
/*      */     case 's':
/*  559 */       return jjMoveStringLiteralDfa9_0(paramLong2, 2048L);
/*      */     case 't':
/*  561 */       return jjMoveStringLiteralDfa9_0(paramLong2, 262144L);
/*      */     case 'y':
/*  563 */       if ((paramLong2 & 0x20000) != 0L)
/*  564 */         return jjStartNfaWithStates_0(8, 17, 51); break;
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'n':
/*      */     case 'p':
/*      */     case 'q':
/*      */     case 'r':
/*      */     case 'u':
/*      */     case 'v':
/*      */     case 'w':
/*  569 */     case 'x': } return jjStartNfa_0(7, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa9_0(long paramLong1, long paramLong2) {
/*  573 */     if ((paramLong2 &= paramLong1) == 0L)
/*  574 */       return jjStartNfa_0(7, paramLong1); try {
/*  575 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  577 */       jjStopStringLiteralDfa_0(8, paramLong2);
/*  578 */       return 9;
/*      */     }
/*  580 */     switch (this.curChar)
/*      */     {
/*      */     case 'e':
/*  583 */       if ((paramLong2 & 0x800) != 0L)
/*  584 */         return jjStartNfaWithStates_0(9, 11, 49);
/*  585 */       if ((paramLong2 & 0x40000) != 0L)
/*  586 */         return jjStartNfaWithStates_0(9, 18, 51);
/*  587 */       return jjMoveStringLiteralDfa10_0(paramLong2, 1024L);
/*      */     case 'm':
/*  589 */       return jjMoveStringLiteralDfa10_0(paramLong2, 4194304L);
/*      */     case 'u':
/*  591 */       return jjMoveStringLiteralDfa10_0(paramLong2, 2097152L);
/*      */     }
/*      */ 
/*  595 */     return jjStartNfa_0(8, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa10_0(long paramLong1, long paramLong2) {
/*  599 */     if ((paramLong2 &= paramLong1) == 0L)
/*  600 */       return jjStartNfa_0(8, paramLong1); try {
/*  601 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  603 */       jjStopStringLiteralDfa_0(9, paramLong2);
/*  604 */       return 10;
/*      */     }
/*  606 */     switch (this.curChar)
/*      */     {
/*      */     case 'm':
/*  609 */       return jjMoveStringLiteralDfa11_0(paramLong2, 4194304L);
/*      */     case 'n':
/*  611 */       return jjMoveStringLiteralDfa11_0(paramLong2, 2097152L);
/*      */     case 's':
/*  613 */       if ((paramLong2 & 0x400) != 0L) {
/*  614 */         return jjStartNfaWithStates_0(10, 10, 49);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/*  619 */     return jjStartNfa_0(9, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa11_0(long paramLong1, long paramLong2) {
/*  623 */     if ((paramLong2 &= paramLong1) == 0L)
/*  624 */       return jjStartNfa_0(9, paramLong1); try {
/*  625 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  627 */       jjStopStringLiteralDfa_0(10, paramLong2);
/*  628 */       return 11;
/*      */     }
/*  630 */     switch (this.curChar)
/*      */     {
/*      */     case 'i':
/*  633 */       return jjMoveStringLiteralDfa12_0(paramLong2, 2097152L);
/*      */     case 'u':
/*  635 */       return jjMoveStringLiteralDfa12_0(paramLong2, 4194304L);
/*      */     }
/*      */ 
/*  639 */     return jjStartNfa_0(10, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa12_0(long paramLong1, long paramLong2) {
/*  643 */     if ((paramLong2 &= paramLong1) == 0L)
/*  644 */       return jjStartNfa_0(10, paramLong1); try {
/*  645 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  647 */       jjStopStringLiteralDfa_0(11, paramLong2);
/*  648 */       return 12;
/*      */     }
/*  650 */     switch (this.curChar)
/*      */     {
/*      */     case 'n':
/*  653 */       return jjMoveStringLiteralDfa13_0(paramLong2, 4194304L);
/*      */     case 't':
/*  655 */       return jjMoveStringLiteralDfa13_0(paramLong2, 2097152L);
/*      */     }
/*      */ 
/*  659 */     return jjStartNfa_0(11, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa13_0(long paramLong1, long paramLong2) {
/*  663 */     if ((paramLong2 &= paramLong1) == 0L)
/*  664 */       return jjStartNfa_0(11, paramLong1); try {
/*  665 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  667 */       jjStopStringLiteralDfa_0(12, paramLong2);
/*  668 */       return 13;
/*      */     }
/*  670 */     switch (this.curChar)
/*      */     {
/*      */     case 'i':
/*  673 */       return jjMoveStringLiteralDfa14_0(paramLong2, 4194304L);
/*      */     case 'y':
/*  675 */       if ((paramLong2 & 0x200000) != 0L) {
/*  676 */         return jjStartNfaWithStates_0(13, 21, 51);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/*  681 */     return jjStartNfa_0(12, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa14_0(long paramLong1, long paramLong2) {
/*  685 */     if ((paramLong2 &= paramLong1) == 0L)
/*  686 */       return jjStartNfa_0(12, paramLong1); try {
/*  687 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  689 */       jjStopStringLiteralDfa_0(13, paramLong2);
/*  690 */       return 14;
/*      */     }
/*  692 */     switch (this.curChar)
/*      */     {
/*      */     case 't':
/*  695 */       return jjMoveStringLiteralDfa15_0(paramLong2, 4194304L);
/*      */     }
/*      */ 
/*  699 */     return jjStartNfa_0(13, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa15_0(long paramLong1, long paramLong2) {
/*  703 */     if ((paramLong2 &= paramLong1) == 0L)
/*  704 */       return jjStartNfa_0(13, paramLong1); try {
/*  705 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  707 */       jjStopStringLiteralDfa_0(14, paramLong2);
/*  708 */       return 15;
/*      */     }
/*  710 */     switch (this.curChar)
/*      */     {
/*      */     case 'y':
/*  713 */       if ((paramLong2 & 0x400000) != 0L) {
/*  714 */         return jjStartNfaWithStates_0(15, 22, 51);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/*  719 */     return jjStartNfa_0(14, paramLong2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAdd(int paramInt) {
/*  723 */     if (this.jjrounds[paramInt] != this.jjround)
/*      */     {
/*  725 */       this.jjstateSet[(this.jjnewStateCnt++)] = paramInt;
/*  726 */       this.jjrounds[paramInt] = this.jjround;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void jjAddStates(int paramInt1, int paramInt2) {
/*      */     do
/*  732 */       this.jjstateSet[(this.jjnewStateCnt++)] = jjnextStates[paramInt1];
/*  733 */     while (paramInt1++ != paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddTwoStates(int paramInt1, int paramInt2) {
/*  737 */     jjCheckNAdd(paramInt1);
/*  738 */     jjCheckNAdd(paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddStates(int paramInt1, int paramInt2) {
/*      */     do
/*  743 */       jjCheckNAdd(jjnextStates[paramInt1]);
/*  744 */     while (paramInt1++ != paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddStates(int paramInt) {
/*  748 */     jjCheckNAdd(jjnextStates[paramInt]);
/*  749 */     jjCheckNAdd(jjnextStates[(paramInt + 1)]);
/*      */   }
/*      */ 
/*      */   private final int jjMoveNfa_0(int paramInt1, int paramInt2)
/*      */   {
/*  757 */     int i = 0;
/*  758 */     this.jjnewStateCnt = 47;
/*  759 */     int j = 1;
/*  760 */     this.jjstateSet[0] = paramInt1;
/*  761 */     int k = 2147483647;
/*      */     while (true)
/*      */     {
/*  764 */       if (++this.jjround == 2147483647)
/*  765 */         ReInitRounds();
/*      */       long l1;
/*  766 */       if (this.curChar < '@')
/*      */       {
/*  768 */         l1 = 1L << this.curChar;
/*      */         do
/*      */         {
/*  771 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 49:
/*  774 */             if ((0x0 & l1) != 0L)
/*  775 */               jjCheckNAddTwoStates(18, 19);
/*  776 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  778 */               if (k > 31)
/*  779 */                 k = 31;
/*  780 */               jjCheckNAddStates(0, 2);
/*      */             }
/*  782 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  784 */               if (k > 31)
/*  785 */                 k = 31;
/*  786 */               jjCheckNAdd(20);
/*      */             }
/*  788 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  790 */               if (k > 31)
/*  791 */                 k = 31;
/*  792 */               jjCheckNAdd(19); } break;
/*      */           case 48:
/*  796 */             if ((0x0 & l1) != 0L)
/*  797 */               jjCheckNAddTwoStates(18, 19);
/*  798 */             else if (this.curChar == ':')
/*  799 */               jjCheckNAddStates(3, 5);
/*  800 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  802 */               if (k > 31)
/*  803 */                 k = 31;
/*  804 */               jjCheckNAddStates(0, 2);
/*      */             }
/*  806 */             else if (this.curChar == ':') {
/*  807 */               jjCheckNAddTwoStates(23, 25);
/*  808 */             }if ((0x0 & l1) != 0L)
/*      */             {
/*  810 */               if (k > 31)
/*  811 */                 k = 31;
/*  812 */               jjCheckNAdd(20);
/*      */             }
/*  814 */             if ((0x0 & l1) != 0L)
/*  815 */               jjCheckNAddTwoStates(26, 27);
/*  816 */             if ((0x0 & l1) != 0L)
/*  817 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 47:
/*  820 */             if ((0x0 & l1) != 0L)
/*  821 */               jjCheckNAddTwoStates(18, 19);
/*  822 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  824 */               if (k > 31)
/*  825 */                 k = 31;
/*  826 */               jjCheckNAddStates(0, 2);
/*      */             }
/*  828 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  830 */               if (k > 31)
/*  831 */                 k = 31;
/*  832 */               jjCheckNAdd(20); } break;
/*      */           case 50:
/*  836 */             if ((0x0 & l1) != 0L)
/*  837 */               jjCheckNAddTwoStates(18, 19);
/*  838 */             else if (this.curChar == ':')
/*  839 */               jjCheckNAddStates(3, 5);
/*  840 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  842 */               if (k > 31)
/*  843 */                 k = 31;
/*  844 */               jjCheckNAddStates(0, 2);
/*      */             }
/*  846 */             else if (this.curChar == ':') {
/*  847 */               jjCheckNAddTwoStates(23, 25);
/*  848 */             }if ((0x0 & l1) != 0L)
/*      */             {
/*  850 */               if (k > 31)
/*  851 */                 k = 31;
/*  852 */               jjCheckNAdd(20);
/*      */             }
/*  854 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  856 */               if (k > 31)
/*  857 */                 k = 31;
/*  858 */               jjCheckNAdd(19);
/*      */             }
/*  860 */             if ((0x0 & l1) != 0L)
/*  861 */               jjCheckNAddTwoStates(26, 27);
/*  862 */             if ((0x0 & l1) != 0L)
/*  863 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 5:
/*  866 */             if ((0x0 & l1) != 0L)
/*  867 */               jjCheckNAddStates(6, 9);
/*  868 */             else if (this.curChar == ':')
/*  869 */               jjAddStates(10, 11);
/*  870 */             else if (this.curChar == '"')
/*  871 */               jjCheckNAddTwoStates(15, 16);
/*  872 */             else if (this.curChar == '#')
/*  873 */               jjCheckNAddStates(12, 14);
/*  874 */             else if (this.curChar == '-')
/*  875 */               this.jjstateSet[(this.jjnewStateCnt++)] = 0;
/*  876 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  878 */               if (k > 31)
/*  879 */                 k = 31;
/*  880 */               jjCheckNAddStates(15, 17);
/*      */             }
/*  882 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  884 */               if (k > 24)
/*  885 */                 k = 24;
/*  886 */               jjCheckNAddTwoStates(12, 13);
/*      */             }
/*  888 */             else if (this.curChar == '0')
/*      */             {
/*  890 */               if (k > 24)
/*  891 */                 k = 24;
/*  892 */               jjCheckNAddStates(18, 20); } break;
/*      */           case 51:
/*  896 */             if ((0x0 & l1) != 0L)
/*  897 */               jjCheckNAddTwoStates(18, 19);
/*  898 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  900 */               if (k > 31)
/*  901 */                 k = 31;
/*  902 */               jjCheckNAdd(19); } break;
/*      */           case 0:
/*  906 */             if (this.curChar == '-')
/*  907 */               jjCheckNAddStates(21, 23); break;
/*      */           case 1:
/*  910 */             if ((0xFFFFDBFF & l1) != 0L)
/*  911 */               jjCheckNAddStates(21, 23); break;
/*      */           case 2:
/*  914 */             if (((0x2400 & l1) != 0L) && (k > 5))
/*  915 */               k = 5; break;
/*      */           case 3:
/*  918 */             if ((this.curChar == '\n') && (k > 5))
/*  919 */               k = 5; break;
/*      */           case 4:
/*  922 */             if (this.curChar == '\r')
/*  923 */               this.jjstateSet[(this.jjnewStateCnt++)] = 3; break;
/*      */           case 6:
/*  926 */             if (this.curChar == '#')
/*  927 */               jjCheckNAddStates(12, 14); break;
/*      */           case 7:
/*  930 */             if ((0xFFFFDBFF & l1) != 0L)
/*  931 */               jjCheckNAddStates(12, 14); break;
/*      */           case 8:
/*  934 */             if (((0x2400 & l1) != 0L) && (k > 6))
/*  935 */               k = 6; break;
/*      */           case 9:
/*  938 */             if ((this.curChar == '\n') && (k > 6))
/*  939 */               k = 6; break;
/*      */           case 10:
/*  942 */             if (this.curChar == '\r')
/*  943 */               this.jjstateSet[(this.jjnewStateCnt++)] = 9; break;
/*      */           case 11:
/*  946 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  948 */               if (k > 24)
/*  949 */                 k = 24;
/*  950 */               jjCheckNAddTwoStates(12, 13);
/*  951 */             }break;
/*      */           case 12:
/*  953 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  955 */               if (k > 24)
/*  956 */                 k = 24;
/*  957 */               jjCheckNAddTwoStates(12, 13);
/*  958 */             }break;
/*      */           case 14:
/*  960 */             if (this.curChar == '"')
/*  961 */               jjCheckNAddTwoStates(15, 16); break;
/*      */           case 15:
/*  964 */             if ((0xFFFFFFFF & l1) != 0L)
/*  965 */               jjCheckNAddTwoStates(15, 16); break;
/*      */           case 16:
/*  968 */             if ((this.curChar == '"') && (k > 35))
/*  969 */               k = 35; break;
/*      */           case 17:
/*  972 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  974 */               if (k > 31)
/*  975 */                 k = 31;
/*  976 */               jjCheckNAddStates(15, 17);
/*  977 */             }break;
/*      */           case 18:
/*  979 */             if ((0x0 & l1) != 0L)
/*  980 */               jjCheckNAddTwoStates(18, 19); break;
/*      */           case 19:
/*  983 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  985 */               if (k > 31)
/*  986 */                 k = 31;
/*  987 */               jjCheckNAdd(19);
/*  988 */             }break;
/*      */           case 20:
/*  990 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  992 */               if (k > 31)
/*  993 */                 k = 31;
/*  994 */               jjCheckNAdd(20);
/*  995 */             }break;
/*      */           case 21:
/*  997 */             if ((0x0 & l1) != 0L)
/*      */             {
/*  999 */               if (k > 31)
/* 1000 */                 k = 31;
/* 1001 */               jjCheckNAddStates(0, 2);
/* 1002 */             }break;
/*      */           case 22:
/* 1004 */             if ((0x0 & l1) != 0L)
/* 1005 */               jjCheckNAddStates(6, 9); break;
/*      */           case 23:
/* 1008 */             if ((0x0 & l1) != 0L)
/* 1009 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 24:
/* 1012 */             if (this.curChar == ':')
/* 1013 */               jjCheckNAddTwoStates(23, 25); break;
/*      */           case 25:
/*      */           case 41:
/* 1017 */             if ((this.curChar == ':') && (k > 28))
/* 1018 */               k = 28; break;
/*      */           case 26:
/* 1021 */             if ((0x0 & l1) != 0L)
/* 1022 */               jjCheckNAddTwoStates(26, 27); break;
/*      */           case 27:
/* 1025 */             if (this.curChar == ':')
/* 1026 */               jjCheckNAddStates(3, 5); break;
/*      */           case 28:
/*      */           case 42:
/* 1030 */             if (this.curChar == ':')
/* 1031 */               jjCheckNAddTwoStates(29, 36); break;
/*      */           case 29:
/* 1034 */             if ((0x0 & l1) != 0L)
/* 1035 */               jjCheckNAddTwoStates(29, 30); break;
/*      */           case 30:
/* 1038 */             if (this.curChar == '.')
/* 1039 */               jjCheckNAdd(31); break;
/*      */           case 31:
/* 1042 */             if ((0x0 & l1) != 0L)
/* 1043 */               jjCheckNAddTwoStates(31, 32); break;
/*      */           case 32:
/* 1046 */             if (this.curChar == '.')
/* 1047 */               jjCheckNAdd(33); break;
/*      */           case 33:
/* 1050 */             if ((0x0 & l1) != 0L)
/* 1051 */               jjCheckNAddTwoStates(33, 34); break;
/*      */           case 34:
/* 1054 */             if (this.curChar == '.')
/* 1055 */               jjCheckNAdd(35); break;
/*      */           case 35:
/* 1058 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1060 */               if (k > 28)
/* 1061 */                 k = 28;
/* 1062 */               jjCheckNAdd(35);
/* 1063 */             }break;
/*      */           case 36:
/* 1065 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1067 */               if (k > 28)
/* 1068 */                 k = 28;
/* 1069 */               jjCheckNAddStates(24, 26);
/* 1070 */             }break;
/*      */           case 37:
/* 1072 */             if ((0x0 & l1) != 0L)
/* 1073 */               jjCheckNAddTwoStates(37, 28); break;
/*      */           case 38:
/* 1076 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1078 */               if (k > 28)
/* 1079 */                 k = 28;
/* 1080 */               jjCheckNAdd(38);
/* 1081 */             }break;
/*      */           case 39:
/* 1083 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1085 */               if (k > 28)
/* 1086 */                 k = 28;
/* 1087 */               jjCheckNAddStates(27, 31);
/* 1088 */             }break;
/*      */           case 40:
/* 1090 */             if (this.curChar == ':')
/* 1091 */               jjAddStates(10, 11); break;
/*      */           case 43:
/* 1094 */             if (this.curChar == '0')
/*      */             {
/* 1096 */               if (k > 24)
/* 1097 */                 k = 24;
/* 1098 */               jjCheckNAddStates(18, 20);
/* 1099 */             }break;
/*      */           case 45:
/* 1101 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1103 */               if (k > 24)
/* 1104 */                 k = 24;
/* 1105 */               jjCheckNAddTwoStates(45, 13);
/* 1106 */             }break;
/*      */           case 46:
/* 1108 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1110 */               if (k > 24)
/* 1111 */                 k = 24;
/* 1112 */               jjCheckNAddTwoStates(46, 13); } break;
/*      */           case 13:
/*      */           case 44:
/*      */           }
/* 1116 */         }while (j != i);
/*      */       }
/* 1118 */       else if (this.curChar < '')
/*      */       {
/* 1120 */         l1 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1123 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 49:
/* 1126 */             if ((0x87FFFFFE & l1) != 0L)
/* 1127 */               jjCheckNAddTwoStates(18, 19);
/* 1128 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1130 */               if (k > 31)
/* 1131 */                 k = 31;
/* 1132 */               jjCheckNAddStates(0, 2);
/*      */             }
/* 1134 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1136 */               if (k > 31)
/* 1137 */                 k = 31;
/* 1138 */               jjCheckNAdd(20);
/*      */             }
/* 1140 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1142 */               if (k > 31)
/* 1143 */                 k = 31;
/* 1144 */               jjCheckNAdd(19); } break;
/*      */           case 48:
/* 1148 */             if ((0x87FFFFFE & l1) != 0L)
/* 1149 */               jjCheckNAddTwoStates(18, 19);
/* 1150 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1152 */               if (k > 31)
/* 1153 */                 k = 31;
/* 1154 */               jjCheckNAddStates(0, 2);
/*      */             }
/* 1156 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1158 */               if (k > 31)
/* 1159 */                 k = 31;
/* 1160 */               jjCheckNAdd(20);
/*      */             }
/* 1162 */             if ((0x7E & l1) != 0L)
/* 1163 */               jjCheckNAddTwoStates(26, 27);
/* 1164 */             if ((0x7E & l1) != 0L)
/* 1165 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 47:
/* 1168 */             if ((0x87FFFFFE & l1) != 0L)
/* 1169 */               jjCheckNAddTwoStates(18, 19);
/* 1170 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1172 */               if (k > 31)
/* 1173 */                 k = 31;
/* 1174 */               jjCheckNAddStates(0, 2);
/*      */             }
/* 1176 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1178 */               if (k > 31)
/* 1179 */                 k = 31;
/* 1180 */               jjCheckNAdd(20); } break;
/*      */           case 50:
/* 1184 */             if ((0x87FFFFFE & l1) != 0L)
/* 1185 */               jjCheckNAddTwoStates(18, 19);
/* 1186 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1188 */               if (k > 31)
/* 1189 */                 k = 31;
/* 1190 */               jjCheckNAddStates(0, 2);
/*      */             }
/* 1192 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1194 */               if (k > 31)
/* 1195 */                 k = 31;
/* 1196 */               jjCheckNAdd(20);
/*      */             }
/* 1198 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1200 */               if (k > 31)
/* 1201 */                 k = 31;
/* 1202 */               jjCheckNAdd(19);
/*      */             }
/* 1204 */             if ((0x7E & l1) != 0L)
/* 1205 */               jjCheckNAddTwoStates(26, 27);
/* 1206 */             if ((0x7E & l1) != 0L)
/* 1207 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 5:
/* 1210 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1212 */               if (k > 31)
/* 1213 */                 k = 31;
/* 1214 */               jjCheckNAddStates(15, 17);
/*      */             }
/* 1216 */             if ((0x7E & l1) != 0L)
/* 1217 */               jjCheckNAddStates(6, 9); break;
/*      */           case 51:
/* 1220 */             if ((0x87FFFFFE & l1) != 0L)
/* 1221 */               jjCheckNAddTwoStates(18, 19);
/* 1222 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1224 */               if (k > 31)
/* 1225 */                 k = 31;
/* 1226 */               jjCheckNAdd(19); } break;
/*      */           case 1:
/* 1230 */             jjAddStates(21, 23);
/* 1231 */             break;
/*      */           case 7:
/* 1233 */             jjAddStates(12, 14);
/* 1234 */             break;
/*      */           case 13:
/* 1236 */             if (((0x1000 & l1) != 0L) && (k > 24))
/* 1237 */               k = 24; break;
/*      */           case 15:
/* 1240 */             jjAddStates(32, 33);
/* 1241 */             break;
/*      */           case 17:
/* 1243 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1245 */               if (k > 31)
/* 1246 */                 k = 31;
/* 1247 */               jjCheckNAddStates(15, 17);
/* 1248 */             }break;
/*      */           case 18:
/* 1250 */             if ((0x87FFFFFE & l1) != 0L)
/* 1251 */               jjCheckNAddTwoStates(18, 19); break;
/*      */           case 19:
/* 1254 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1256 */               if (k > 31)
/* 1257 */                 k = 31;
/* 1258 */               jjCheckNAdd(19);
/* 1259 */             }break;
/*      */           case 20:
/* 1261 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1263 */               if (k > 31)
/* 1264 */                 k = 31;
/* 1265 */               jjCheckNAdd(20);
/* 1266 */             }break;
/*      */           case 21:
/* 1268 */             if ((0x7FFFFFE & l1) != 0L)
/*      */             {
/* 1270 */               if (k > 31)
/* 1271 */                 k = 31;
/* 1272 */               jjCheckNAddStates(0, 2);
/* 1273 */             }break;
/*      */           case 22:
/* 1275 */             if ((0x7E & l1) != 0L)
/* 1276 */               jjCheckNAddStates(6, 9); break;
/*      */           case 23:
/* 1279 */             if ((0x7E & l1) != 0L)
/* 1280 */               jjCheckNAddTwoStates(23, 24); break;
/*      */           case 26:
/* 1283 */             if ((0x7E & l1) != 0L)
/* 1284 */               jjCheckNAddTwoStates(26, 27); break;
/*      */           case 36:
/* 1287 */             if ((0x7E & l1) != 0L)
/*      */             {
/* 1289 */               if (k > 28)
/* 1290 */                 k = 28;
/* 1291 */               jjCheckNAddStates(24, 26);
/* 1292 */             }break;
/*      */           case 37:
/* 1294 */             if ((0x7E & l1) != 0L)
/* 1295 */               jjCheckNAddTwoStates(37, 28); break;
/*      */           case 38:
/* 1298 */             if ((0x7E & l1) != 0L)
/*      */             {
/* 1300 */               if (k > 28)
/* 1301 */                 k = 28;
/* 1302 */               jjCheckNAdd(38);
/* 1303 */             }break;
/*      */           case 39:
/* 1305 */             if ((0x7E & l1) != 0L)
/*      */             {
/* 1307 */               if (k > 28)
/* 1308 */                 k = 28;
/* 1309 */               jjCheckNAddStates(27, 31);
/* 1310 */             }break;
/*      */           case 44:
/* 1312 */             if ((0x1000000 & l1) != 0L)
/* 1313 */               jjCheckNAdd(45); break;
/*      */           case 45:
/* 1316 */             if ((0x7E & l1) != 0L)
/*      */             {
/* 1318 */               if (k > 24)
/* 1319 */                 k = 24;
/* 1320 */               jjCheckNAddTwoStates(45, 13); } break;
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 6:
/*      */           case 8:
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*      */           case 12:
/*      */           case 14:
/*      */           case 16:
/*      */           case 24:
/*      */           case 25:
/*      */           case 27:
/*      */           case 28:
/*      */           case 29:
/*      */           case 30:
/*      */           case 31:
/*      */           case 32:
/*      */           case 33:
/*      */           case 34:
/*      */           case 35:
/*      */           case 40:
/*      */           case 41:
/*      */           case 42:
/*      */           case 43:
/* 1324 */           case 46: }  } while (j != i);
/*      */       }
/*      */       else
/*      */       {
/* 1328 */         int m = (this.curChar & 0xFF) >> '\006';
/* 1329 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1332 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 1:
/* 1335 */             if ((jjbitVec0[m] & l2) != 0L)
/* 1336 */               jjAddStates(21, 23); break;
/*      */           case 7:
/* 1339 */             if ((jjbitVec0[m] & l2) != 0L)
/* 1340 */               jjAddStates(12, 14); break;
/*      */           case 15:
/* 1343 */             if ((jjbitVec0[m] & l2) != 0L)
/* 1344 */               jjAddStates(32, 33);
/*      */             break;
/*      */           }
/*      */         }
/* 1348 */         while (j != i);
/*      */       }
/* 1350 */       if (k != 2147483647)
/*      */       {
/* 1352 */         this.jjmatchedKind = k;
/* 1353 */         this.jjmatchedPos = paramInt2;
/* 1354 */         k = 2147483647;
/*      */       }
/* 1356 */       paramInt2++;
/* 1357 */       if ((j = this.jjnewStateCnt) == (i = 47 - (this.jjnewStateCnt = i)))
/* 1358 */         return paramInt2; try {
/* 1359 */         this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {  }
/* 1360 */     }return paramInt2;
/*      */   }
/*      */ 
/*      */   public ParserTokenManager(ASCII_CharStream paramASCII_CharStream)
/*      */   {
/* 1394 */     this.input_stream = paramASCII_CharStream;
/*      */   }
/*      */ 
/*      */   public ParserTokenManager(ASCII_CharStream paramASCII_CharStream, int paramInt) {
/* 1398 */     this(paramASCII_CharStream);
/* 1399 */     SwitchTo(paramInt);
/*      */   }
/*      */ 
/*      */   public void ReInit(ASCII_CharStream paramASCII_CharStream) {
/* 1403 */     this.jjmatchedPos = (this.jjnewStateCnt = 0);
/* 1404 */     this.curLexState = this.defaultLexState;
/* 1405 */     this.input_stream = paramASCII_CharStream;
/* 1406 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   private final void ReInitRounds()
/*      */   {
/* 1411 */     this.jjround = -2147483647;
/* 1412 */     for (int i = 47; i-- > 0; )
/* 1413 */       this.jjrounds[i] = -2147483648;
/*      */   }
/*      */ 
/*      */   public void ReInit(ASCII_CharStream paramASCII_CharStream, int paramInt) {
/* 1417 */     ReInit(paramASCII_CharStream);
/* 1418 */     SwitchTo(paramInt);
/*      */   }
/*      */ 
/*      */   public void SwitchTo(int paramInt) {
/* 1422 */     if ((paramInt >= 1) || (paramInt < 0)) {
/* 1423 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + paramInt + ". State unchanged.", 2);
/*      */     }
/* 1425 */     this.curLexState = paramInt;
/*      */   }
/*      */ 
/*      */   private final Token jjFillToken()
/*      */   {
/* 1430 */     Token localToken = Token.newToken(this.jjmatchedKind);
/* 1431 */     localToken.kind = this.jjmatchedKind;
/* 1432 */     String str = jjstrLiteralImages[this.jjmatchedKind];
/* 1433 */     localToken.image = (str == null ? this.input_stream.GetImage() : str);
/* 1434 */     localToken.beginLine = this.input_stream.getBeginLine();
/* 1435 */     localToken.beginColumn = this.input_stream.getBeginColumn();
/* 1436 */     localToken.endLine = this.input_stream.getEndLine();
/* 1437 */     localToken.endColumn = this.input_stream.getEndColumn();
/* 1438 */     return localToken;
/*      */   }
/*      */ 
/*      */   public final Token getNextToken()
/*      */   {
/* 1451 */     Object localObject = null;
/*      */ 
/* 1453 */     int i = 0;
/*      */     do
/*      */     {
/*      */       while (true)
/*      */       {
/*      */         try
/*      */         {
/* 1460 */           this.curChar = this.input_stream.BeginToken();
/*      */         }
/*      */         catch (IOException localIOException1)
/*      */         {
/* 1464 */           this.jjmatchedKind = 0;
/* 1465 */           return jjFillToken();
/*      */         }
/*      */         try
/*      */         {
/* 1469 */           this.input_stream.backup(0);
/* 1470 */           while ((this.curChar <= ' ') && ((0x2600 & 1L << this.curChar) != 0L))
/* 1471 */             this.curChar = this.input_stream.BeginToken(); 
/*      */         } catch (IOException localIOException2) {  }
/*      */ 
/*      */       }
/* 1474 */       this.jjmatchedKind = 2147483647;
/* 1475 */       this.jjmatchedPos = 0;
/* 1476 */       i = jjMoveStringLiteralDfa0_0();
/* 1477 */       if (this.jjmatchedKind == 2147483647)
/*      */         break;
/* 1479 */       if (this.jjmatchedPos + 1 < i)
/* 1480 */         this.input_stream.backup(i - this.jjmatchedPos - 1); 
/*      */     }
/* 1481 */     while ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) == 0L);
/*      */ 
/* 1483 */     Token localToken = jjFillToken();
/* 1484 */     return localToken;
/*      */ 
/* 1491 */     int j = this.input_stream.getEndLine();
/* 1492 */     int k = this.input_stream.getEndColumn();
/* 1493 */     String str = null;
/* 1494 */     boolean bool = false;
/*      */     try { this.input_stream.readChar(); this.input_stream.backup(1);
/*      */     } catch (IOException localIOException3) {
/* 1497 */       bool = true;
/* 1498 */       str = i <= 1 ? "" : this.input_stream.GetImage();
/* 1499 */       if ((this.curChar == '\n') || (this.curChar == '\r')) {
/* 1500 */         j++;
/* 1501 */         k = 0;
/*      */       }
/*      */       else {
/* 1504 */         k++;
/*      */       }
/*      */     }
/* 1506 */     if (!bool) {
/* 1507 */       this.input_stream.backup(1);
/* 1508 */       str = i <= 1 ? "" : this.input_stream.GetImage();
/*      */     }
/* 1510 */     throw new TokenMgrError(bool, this.curLexState, j, k, str, this.curChar, 0);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.ParserTokenManager
 * JD-Core Version:    0.6.2
 */