/*     */ package com.sun.org.apache.xpath.internal.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.ObjectVector;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ class Lexer
/*     */ {
/*     */   private Compiler m_compiler;
/*     */   PrefixResolver m_namespaceContext;
/*     */   XPathParser m_processor;
/*     */   static final int TARGETEXTRA = 10000;
/*  66 */   private int[] m_patternMap = new int[100];
/*     */   private int m_patternMapSize;
/*     */ 
/*     */   Lexer(Compiler compiler, PrefixResolver resolver, XPathParser xpathProcessor)
/*     */   {
/*  86 */     this.m_compiler = compiler;
/*  87 */     this.m_namespaceContext = resolver;
/*  88 */     this.m_processor = xpathProcessor;
/*     */   }
/*     */ 
/*     */   void tokenize(String pat)
/*     */     throws TransformerException
/*     */   {
/* 100 */     tokenize(pat, null);
/*     */   }
/*     */ 
/*     */   void tokenize(String pat, Vector targetStrings)
/*     */     throws TransformerException
/*     */   {
/* 115 */     this.m_compiler.m_currentPattern = pat;
/* 116 */     this.m_patternMapSize = 0;
/*     */ 
/* 119 */     this.m_compiler.m_opMap = new OpMapVector(2500, 2500, 1);
/*     */ 
/* 121 */     int nChars = pat.length();
/* 122 */     int startSubstring = -1;
/* 123 */     int posOfNSSep = -1;
/* 124 */     boolean isStartOfPat = true;
/* 125 */     boolean isAttrName = false;
/* 126 */     boolean isNum = false;
/*     */ 
/* 130 */     int nesting = 0;
/*     */ 
/* 133 */     for (int i = 0; i < nChars; i++)
/*     */     {
/* 135 */       char c = pat.charAt(i);
/*     */ 
/* 137 */       switch (c)
/*     */       {
/*     */       case '"':
/* 141 */         if (startSubstring != -1)
/*     */         {
/* 143 */           isNum = false;
/* 144 */           isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/* 145 */           isAttrName = false;
/*     */ 
/* 147 */           if (-1 != posOfNSSep)
/*     */           {
/* 149 */             posOfNSSep = mapNSTokens(pat, startSubstring, posOfNSSep, i);
/*     */           }
/*     */           else
/*     */           {
/* 153 */             addToTokenQueue(pat.substring(startSubstring, i));
/*     */           }
/*     */         }
/*     */ 
/* 157 */         startSubstring = i;
/*     */ 
/* 159 */         for (i++; (i < nChars) && ((c = pat.charAt(i)) != '"'); i++);
/* 161 */         if ((c == '"') && (i < nChars))
/*     */         {
/* 163 */           addToTokenQueue(pat.substring(startSubstring, i + 1));
/*     */ 
/* 165 */           startSubstring = -1;
/*     */         }
/*     */         else
/*     */         {
/* 169 */           this.m_processor.error("ER_EXPECTED_DOUBLE_QUOTE", null);
/*     */         }
/*     */ 
/* 173 */         break;
/*     */       case '\'':
/* 175 */         if (startSubstring != -1)
/*     */         {
/* 177 */           isNum = false;
/* 178 */           isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/* 179 */           isAttrName = false;
/*     */ 
/* 181 */           if (-1 != posOfNSSep)
/*     */           {
/* 183 */             posOfNSSep = mapNSTokens(pat, startSubstring, posOfNSSep, i);
/*     */           }
/*     */           else
/*     */           {
/* 187 */             addToTokenQueue(pat.substring(startSubstring, i));
/*     */           }
/*     */         }
/*     */ 
/* 191 */         startSubstring = i;
/*     */ 
/* 193 */         for (i++; (i < nChars) && ((c = pat.charAt(i)) != '\''); i++);
/* 195 */         if ((c == '\'') && (i < nChars))
/*     */         {
/* 197 */           addToTokenQueue(pat.substring(startSubstring, i + 1));
/*     */ 
/* 199 */           startSubstring = -1;
/*     */         }
/*     */         else
/*     */         {
/* 203 */           this.m_processor.error("ER_EXPECTED_SINGLE_QUOTE", null);
/*     */         }
/*     */ 
/* 206 */         break;
/*     */       case '\t':
/*     */       case '\n':
/*     */       case '\r':
/*     */       case ' ':
/* 211 */         if (startSubstring == -1)
/*     */           continue;
/* 213 */         isNum = false;
/* 214 */         isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/* 215 */         isAttrName = false;
/*     */ 
/* 217 */         if (-1 != posOfNSSep)
/*     */         {
/* 219 */           posOfNSSep = mapNSTokens(pat, startSubstring, posOfNSSep, i);
/*     */         }
/*     */         else
/*     */         {
/* 223 */           addToTokenQueue(pat.substring(startSubstring, i));
/*     */         }
/*     */ 
/* 226 */         startSubstring = -1; break;
/*     */       case '@':
/* 230 */         isAttrName = true;
/*     */       case '-':
/* 234 */         if ('-' == c)
/*     */         {
/* 236 */           if ((!isNum) && (startSubstring != -1))
/*     */           {
/*     */             continue;
/*     */           }
/*     */ 
/* 241 */           isNum = false;
/*     */         }
/*     */ 
/*     */       case '!':
/*     */       case '$':
/*     */       case '(':
/*     */       case ')':
/*     */       case '*':
/*     */       case '+':
/*     */       case ',':
/*     */       case '/':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/*     */       case '^':
/*     */       case '|':
/* 261 */         if (startSubstring != -1)
/*     */         {
/* 263 */           isNum = false;
/* 264 */           isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/* 265 */           isAttrName = false;
/*     */ 
/* 267 */           if (-1 != posOfNSSep)
/*     */           {
/* 269 */             posOfNSSep = mapNSTokens(pat, startSubstring, posOfNSSep, i);
/*     */           }
/*     */           else
/*     */           {
/* 273 */             addToTokenQueue(pat.substring(startSubstring, i));
/*     */           }
/*     */ 
/* 276 */           startSubstring = -1;
/*     */         }
/* 278 */         else if (('/' == c) && (isStartOfPat))
/*     */         {
/* 280 */           isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/*     */         }
/* 282 */         else if ('*' == c)
/*     */         {
/* 284 */           isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/* 285 */           isAttrName = false;
/*     */         }
/*     */ 
/* 288 */         if (0 == nesting)
/*     */         {
/* 290 */           if ('|' == c)
/*     */           {
/* 292 */             if (null != targetStrings)
/*     */             {
/* 294 */               recordTokenString(targetStrings);
/*     */             }
/*     */ 
/* 297 */             isStartOfPat = true;
/*     */           }
/*     */         }
/*     */ 
/* 301 */         if ((')' == c) || (']' == c))
/*     */         {
/* 303 */           nesting--;
/*     */         }
/* 305 */         else if (('(' == c) || ('[' == c))
/*     */         {
/* 307 */           nesting++;
/*     */         }
/*     */ 
/* 310 */         addToTokenQueue(pat.substring(i, i + 1));
/* 311 */         break;
/*     */       case ':':
/* 313 */         if (i > 0)
/*     */         {
/* 315 */           if (posOfNSSep == i - 1)
/*     */           {
/* 317 */             if (startSubstring != -1)
/*     */             {
/* 319 */               if (startSubstring < i - 1) {
/* 320 */                 addToTokenQueue(pat.substring(startSubstring, i - 1));
/*     */               }
/*     */             }
/* 323 */             isNum = false;
/* 324 */             isAttrName = false;
/* 325 */             startSubstring = -1;
/* 326 */             posOfNSSep = -1;
/*     */ 
/* 328 */             addToTokenQueue(pat.substring(i - 1, i + 1));
/*     */ 
/* 330 */             continue;
/*     */           }
/*     */ 
/* 334 */           posOfNSSep = i;
/*     */         }
/*     */ 
/*     */         break;
/*     */       }
/*     */ 
/* 340 */       if (-1 == startSubstring)
/*     */       {
/* 342 */         startSubstring = i;
/* 343 */         isNum = Character.isDigit(c);
/*     */       }
/* 345 */       else if (isNum)
/*     */       {
/* 347 */         isNum = Character.isDigit(c);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 352 */     if (startSubstring != -1)
/*     */     {
/* 354 */       isNum = false;
/* 355 */       isStartOfPat = mapPatternElemPos(nesting, isStartOfPat, isAttrName);
/*     */ 
/* 357 */       if ((-1 != posOfNSSep) || ((this.m_namespaceContext != null) && (this.m_namespaceContext.handlesNullPrefixes())))
/*     */       {
/* 360 */         posOfNSSep = mapNSTokens(pat, startSubstring, posOfNSSep, nChars);
/*     */       }
/*     */       else
/*     */       {
/* 364 */         addToTokenQueue(pat.substring(startSubstring, nChars));
/*     */       }
/*     */     }
/*     */ 
/* 368 */     if (0 == this.m_compiler.getTokenQueueSize())
/*     */     {
/* 370 */       this.m_processor.error("ER_EMPTY_EXPRESSION", null);
/*     */     }
/* 372 */     else if (null != targetStrings)
/*     */     {
/* 374 */       recordTokenString(targetStrings);
/*     */     }
/*     */ 
/* 377 */     this.m_processor.m_queueMark = 0;
/*     */   }
/*     */ 
/*     */   private boolean mapPatternElemPos(int nesting, boolean isStart, boolean isAttrName)
/*     */   {
/* 395 */     if (0 == nesting)
/*     */     {
/* 397 */       if (this.m_patternMapSize >= this.m_patternMap.length)
/*     */       {
/* 399 */         int[] patternMap = this.m_patternMap;
/* 400 */         int len = this.m_patternMap.length;
/* 401 */         this.m_patternMap = new int[this.m_patternMapSize + 100];
/* 402 */         System.arraycopy(patternMap, 0, this.m_patternMap, 0, len);
/*     */       }
/* 404 */       if (!isStart)
/*     */       {
/* 406 */         this.m_patternMap[(this.m_patternMapSize - 1)] -= 10000;
/*     */       }
/* 408 */       this.m_patternMap[this.m_patternMapSize] = (this.m_compiler.getTokenQueueSize() - (isAttrName ? 1 : 0) + 10000);
/*     */ 
/* 411 */       this.m_patternMapSize += 1;
/*     */ 
/* 413 */       isStart = false;
/*     */     }
/*     */ 
/* 416 */     return isStart;
/*     */   }
/*     */ 
/*     */   private int getTokenQueuePosFromMap(int i)
/*     */   {
/* 429 */     int pos = this.m_patternMap[i];
/*     */ 
/* 431 */     return pos >= 10000 ? pos - 10000 : pos;
/*     */   }
/*     */ 
/*     */   private final void resetTokenMark(int mark)
/*     */   {
/* 442 */     int qsz = this.m_compiler.getTokenQueueSize();
/*     */ 
/* 444 */     this.m_processor.m_queueMark = (mark > 0 ? mark : mark <= qsz ? mark - 1 : 0);
/*     */ 
/* 447 */     if (this.m_processor.m_queueMark < qsz)
/*     */     {
/* 449 */       this.m_processor.m_token = ((String)this.m_compiler.getTokenQueue().elementAt(this.m_processor.m_queueMark++));
/*     */ 
/* 451 */       this.m_processor.m_tokenChar = this.m_processor.m_token.charAt(0);
/*     */     }
/*     */     else
/*     */     {
/* 455 */       this.m_processor.m_token = null;
/* 456 */       this.m_processor.m_tokenChar = '\000';
/*     */     }
/*     */   }
/*     */ 
/*     */   final int getKeywordToken(String key)
/*     */   {
/*     */     int tok;
/*     */     try
/*     */     {
/* 474 */       Integer itok = (Integer)Keywords.getKeyWord(key);
/*     */ 
/* 476 */       tok = null != itok ? itok.intValue() : 0;
/*     */     }
/*     */     catch (NullPointerException npe)
/*     */     {
/* 480 */       tok = 0;
/*     */     }
/*     */     catch (ClassCastException cce)
/*     */     {
/* 484 */       tok = 0;
/*     */     }
/*     */ 
/* 487 */     return tok;
/*     */   }
/*     */ 
/*     */   private void recordTokenString(Vector targetStrings)
/*     */   {
/* 498 */     int tokPos = getTokenQueuePosFromMap(this.m_patternMapSize - 1);
/*     */ 
/* 500 */     resetTokenMark(tokPos + 1);
/*     */ 
/* 502 */     if (this.m_processor.lookahead('(', 1))
/*     */     {
/* 504 */       int tok = getKeywordToken(this.m_processor.m_token);
/*     */ 
/* 506 */       switch (tok)
/*     */       {
/*     */       case 1030:
/* 509 */         targetStrings.addElement("#comment");
/* 510 */         break;
/*     */       case 1031:
/* 512 */         targetStrings.addElement("#text");
/* 513 */         break;
/*     */       case 1033:
/* 515 */         targetStrings.addElement("*");
/* 516 */         break;
/*     */       case 35:
/* 518 */         targetStrings.addElement("/");
/* 519 */         break;
/*     */       case 36:
/* 521 */         targetStrings.addElement("*");
/* 522 */         break;
/*     */       case 1032:
/* 524 */         targetStrings.addElement("*");
/* 525 */         break;
/*     */       default:
/* 527 */         targetStrings.addElement("*");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 532 */       if (this.m_processor.tokenIs('@'))
/*     */       {
/* 534 */         tokPos++;
/*     */ 
/* 536 */         resetTokenMark(tokPos + 1);
/*     */       }
/*     */ 
/* 539 */       if (this.m_processor.lookahead(':', 1))
/*     */       {
/* 541 */         tokPos += 2;
/*     */       }
/*     */ 
/* 544 */       targetStrings.addElement(this.m_compiler.getTokenQueue().elementAt(tokPos));
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void addToTokenQueue(String s)
/*     */   {
/* 556 */     this.m_compiler.getTokenQueue().addElement(s);
/*     */   }
/*     */ 
/*     */   private int mapNSTokens(String pat, int startSubstring, int posOfNSSep, int posOfScan)
/*     */     throws TransformerException
/*     */   {
/* 577 */     String prefix = "";
/*     */ 
/* 579 */     if ((startSubstring >= 0) && (posOfNSSep >= 0))
/*     */     {
/* 581 */       prefix = pat.substring(startSubstring, posOfNSSep);
/*     */     }
/*     */     String uName;
/* 585 */     if ((null != this.m_namespaceContext) && (!prefix.equals("*")) && (!prefix.equals("xmlns"))) {
/*     */       try
/*     */       {
/*     */         String uName;
/* 590 */         if (prefix.length() > 0) {
/* 591 */           uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
/*     */         }
/*     */         else
/*     */         {
/* 612 */           uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (ClassCastException cce)
/*     */       {
/* 620 */         String uName = this.m_namespaceContext.getNamespaceForPrefix(prefix);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 625 */       uName = prefix;
/*     */     }
/*     */ 
/* 628 */     if ((null != uName) && (uName.length() > 0))
/*     */     {
/* 630 */       addToTokenQueue(uName);
/* 631 */       addToTokenQueue(":");
/*     */ 
/* 633 */       String s = pat.substring(posOfNSSep + 1, posOfScan);
/*     */ 
/* 635 */       if (s.length() > 0) {
/* 636 */         addToTokenQueue(s);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 642 */       this.m_processor.errorForDOM3("ER_PREFIX_MUST_RESOLVE", new String[] { prefix });
/*     */     }
/*     */ 
/* 662 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.Lexer
 * JD-Core Version:    0.6.2
 */