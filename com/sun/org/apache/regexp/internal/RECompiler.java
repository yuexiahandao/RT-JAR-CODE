/*      */ package com.sun.org.apache.regexp.internal;
/*      */ 
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class RECompiler
/*      */ {
/*      */   char[] instruction;
/*      */   int lenInstruction;
/*      */   String pattern;
/*      */   int len;
/*      */   int idx;
/*      */   int parens;
/*      */   static final int NODE_NORMAL = 0;
/*      */   static final int NODE_NULLABLE = 1;
/*      */   static final int NODE_TOPLEVEL = 2;
/*      */   static final int ESC_MASK = 1048560;
/*      */   static final int ESC_BACKREF = 1048575;
/*      */   static final int ESC_COMPLEX = 1048574;
/*      */   static final int ESC_CLASS = 1048573;
/*   63 */   int maxBrackets = 10;
/*      */   static final int bracketUnbounded = -1;
/*   65 */   int brackets = 0;
/*   66 */   int[] bracketStart = null;
/*   67 */   int[] bracketEnd = null;
/*   68 */   int[] bracketMin = null;
/*   69 */   int[] bracketOpt = null;
/*      */ 
/*   72 */   static Hashtable hashPOSIX = new Hashtable();
/*      */ 
/*      */   public RECompiler()
/*      */   {
/*   97 */     this.instruction = new char[''];
/*   98 */     this.lenInstruction = 0;
/*      */   }
/*      */ 
/*      */   void ensure(int n)
/*      */   {
/*  109 */     int curlen = this.instruction.length;
/*      */ 
/*  112 */     if (this.lenInstruction + n >= curlen)
/*      */     {
/*  115 */       while (this.lenInstruction + n >= curlen)
/*      */       {
/*  117 */         curlen *= 2;
/*      */       }
/*      */ 
/*  121 */       char[] newInstruction = new char[curlen];
/*  122 */       System.arraycopy(this.instruction, 0, newInstruction, 0, this.lenInstruction);
/*  123 */       this.instruction = newInstruction;
/*      */     }
/*      */   }
/*      */ 
/*      */   void emit(char c)
/*      */   {
/*  134 */     ensure(1);
/*      */ 
/*  137 */     this.instruction[(this.lenInstruction++)] = c;
/*      */   }
/*      */ 
/*      */   void nodeInsert(char opcode, int opdata, int insertAt)
/*      */   {
/*  150 */     ensure(3);
/*      */ 
/*  153 */     System.arraycopy(this.instruction, insertAt, this.instruction, insertAt + 3, this.lenInstruction - insertAt);
/*  154 */     this.instruction[(insertAt + 0)] = opcode;
/*  155 */     this.instruction[(insertAt + 1)] = ((char)opdata);
/*  156 */     this.instruction[(insertAt + 2)] = '\000';
/*  157 */     this.lenInstruction += 3;
/*      */   }
/*      */ 
/*      */   void setNextOfEnd(int node, int pointTo)
/*      */   {
/*  168 */     int next = this.instruction[(node + 2)];
/*      */ 
/*  171 */     while ((next != 0) && (node < this.lenInstruction))
/*      */     {
/*  179 */       if (node == pointTo) {
/*  180 */         pointTo = this.lenInstruction;
/*      */       }
/*  182 */       node += next;
/*  183 */       next = this.instruction[(node + 2)];
/*      */     }
/*      */ 
/*  187 */     if (node < this.lenInstruction)
/*      */     {
/*  189 */       this.instruction[(node + 2)] = ((char)(short)(pointTo - node));
/*      */     }
/*      */   }
/*      */ 
/*      */   int node(char opcode, int opdata)
/*      */   {
/*  202 */     ensure(3);
/*      */ 
/*  205 */     this.instruction[(this.lenInstruction + 0)] = opcode;
/*  206 */     this.instruction[(this.lenInstruction + 1)] = ((char)opdata);
/*  207 */     this.instruction[(this.lenInstruction + 2)] = '\000';
/*  208 */     this.lenInstruction += 3;
/*      */ 
/*  211 */     return this.lenInstruction - 3;
/*      */   }
/*      */ 
/*      */   void internalError()
/*      */     throws Error
/*      */   {
/*  221 */     throw new Error("Internal error!");
/*      */   }
/*      */ 
/*      */   void syntaxError(String s)
/*      */     throws RESyntaxException
/*      */   {
/*  230 */     throw new RESyntaxException(s);
/*      */   }
/*      */ 
/*      */   void allocBrackets()
/*      */   {
/*  239 */     if (this.bracketStart == null)
/*      */     {
/*  242 */       this.bracketStart = new int[this.maxBrackets];
/*  243 */       this.bracketEnd = new int[this.maxBrackets];
/*  244 */       this.bracketMin = new int[this.maxBrackets];
/*  245 */       this.bracketOpt = new int[this.maxBrackets];
/*      */ 
/*  248 */       for (int i = 0; i < this.maxBrackets; i++)
/*      */       {
/*      */         byte tmp82_81 = (this.bracketMin[i] = this.bracketOpt[i] = -1); this.bracketEnd[i] = tmp82_81; this.bracketStart[i] = tmp82_81;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void reallocBrackets()
/*      */   {
/*  258 */     if (this.bracketStart == null) {
/*  259 */       allocBrackets();
/*      */     }
/*      */ 
/*  262 */     int new_size = this.maxBrackets * 2;
/*  263 */     int[] new_bS = new int[new_size];
/*  264 */     int[] new_bE = new int[new_size];
/*  265 */     int[] new_bM = new int[new_size];
/*  266 */     int[] new_bO = new int[new_size];
/*      */ 
/*  268 */     for (int i = this.brackets; i < new_size; i++)
/*      */     {
/*      */       byte tmp67_66 = (new_bM[i] = new_bO[i] = -1); new_bE[i] = tmp67_66; new_bS[i] = tmp67_66;
/*      */     }
/*  271 */     System.arraycopy(this.bracketStart, 0, new_bS, 0, this.brackets);
/*  272 */     System.arraycopy(this.bracketEnd, 0, new_bE, 0, this.brackets);
/*  273 */     System.arraycopy(this.bracketMin, 0, new_bM, 0, this.brackets);
/*  274 */     System.arraycopy(this.bracketOpt, 0, new_bO, 0, this.brackets);
/*  275 */     this.bracketStart = new_bS;
/*  276 */     this.bracketEnd = new_bE;
/*  277 */     this.bracketMin = new_bM;
/*  278 */     this.bracketOpt = new_bO;
/*  279 */     this.maxBrackets = new_size;
/*      */   }
/*      */ 
/*      */   void bracket()
/*      */     throws RESyntaxException
/*      */   {
/*  289 */     if ((this.idx >= this.len) || (this.pattern.charAt(this.idx++) != '{'))
/*      */     {
/*  291 */       internalError();
/*      */     }
/*      */ 
/*  295 */     if ((this.idx >= this.len) || (!Character.isDigit(this.pattern.charAt(this.idx))))
/*      */     {
/*  297 */       syntaxError("Expected digit");
/*      */     }
/*      */ 
/*  301 */     StringBuffer number = new StringBuffer();
/*  302 */     while ((this.idx < this.len) && (Character.isDigit(this.pattern.charAt(this.idx))))
/*      */     {
/*  304 */       number.append(this.pattern.charAt(this.idx++));
/*      */     }
/*      */     try
/*      */     {
/*  308 */       this.bracketMin[this.brackets] = Integer.parseInt(number.toString());
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*  312 */       syntaxError("Expected valid number");
/*      */     }
/*      */ 
/*  316 */     if (this.idx >= this.len)
/*      */     {
/*  318 */       syntaxError("Expected comma or right bracket");
/*      */     }
/*      */ 
/*  322 */     if (this.pattern.charAt(this.idx) == '}')
/*      */     {
/*  324 */       this.idx += 1;
/*  325 */       this.bracketOpt[this.brackets] = 0;
/*  326 */       return;
/*      */     }
/*      */ 
/*  330 */     if ((this.idx >= this.len) || (this.pattern.charAt(this.idx++) != ','))
/*      */     {
/*  332 */       syntaxError("Expected comma");
/*      */     }
/*      */ 
/*  336 */     if (this.idx >= this.len)
/*      */     {
/*  338 */       syntaxError("Expected comma or right bracket");
/*      */     }
/*      */ 
/*  342 */     if (this.pattern.charAt(this.idx) == '}')
/*      */     {
/*  344 */       this.idx += 1;
/*  345 */       this.bracketOpt[this.brackets] = -1;
/*  346 */       return;
/*      */     }
/*      */ 
/*  350 */     if ((this.idx >= this.len) || (!Character.isDigit(this.pattern.charAt(this.idx))))
/*      */     {
/*  352 */       syntaxError("Expected digit");
/*      */     }
/*      */ 
/*  356 */     number.setLength(0);
/*  357 */     while ((this.idx < this.len) && (Character.isDigit(this.pattern.charAt(this.idx))))
/*      */     {
/*  359 */       number.append(this.pattern.charAt(this.idx++));
/*      */     }
/*      */     try
/*      */     {
/*  363 */       this.bracketOpt[this.brackets] = (Integer.parseInt(number.toString()) - this.bracketMin[this.brackets]);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*  367 */       syntaxError("Expected valid number");
/*      */     }
/*      */ 
/*  371 */     if (this.bracketOpt[this.brackets] < 0)
/*      */     {
/*  373 */       syntaxError("Bad range");
/*      */     }
/*      */ 
/*  377 */     if ((this.idx >= this.len) || (this.pattern.charAt(this.idx++) != '}'))
/*      */     {
/*  379 */       syntaxError("Missing close brace");
/*      */     }
/*      */   }
/*      */ 
/*      */   int escape()
/*      */     throws RESyntaxException
/*      */   {
/*  395 */     if (this.pattern.charAt(this.idx) != '\\')
/*      */     {
/*  397 */       internalError();
/*      */     }
/*      */ 
/*  401 */     if (this.idx + 1 == this.len)
/*      */     {
/*  403 */       syntaxError("Escape terminates string");
/*      */     }
/*      */ 
/*  407 */     this.idx += 2;
/*  408 */     char escapeChar = this.pattern.charAt(this.idx - 1);
/*  409 */     switch (escapeChar)
/*      */     {
/*      */     case 'B':
/*      */     case 'b':
/*  413 */       return 1048574;
/*      */     case 'D':
/*      */     case 'S':
/*      */     case 'W':
/*      */     case 'd':
/*      */     case 's':
/*      */     case 'w':
/*  421 */       return 1048573;
/*      */     case 'u':
/*      */     case 'x':
/*  427 */       int hexDigits = escapeChar == 'u' ? 4 : 2;
/*      */ 
/*  430 */       int val = 0;
/*  431 */       for (; (this.idx < this.len) && (hexDigits-- > 0); this.idx += 1)
/*      */       {
/*  434 */         char c = this.pattern.charAt(this.idx);
/*      */ 
/*  437 */         if ((c >= '0') && (c <= '9'))
/*      */         {
/*  440 */           val = (val << 4) + c - 48;
/*      */         }
/*      */         else
/*      */         {
/*  445 */           c = Character.toLowerCase(c);
/*  446 */           if ((c >= 'a') && (c <= 'f'))
/*      */           {
/*  449 */             val = (val << 4) + (c - 'a') + 10;
/*      */           }
/*      */           else
/*      */           {
/*  455 */             syntaxError("Expected " + hexDigits + " hexadecimal digits after \\" + escapeChar);
/*      */           }
/*      */         }
/*      */       }
/*  459 */       return val;
/*      */     case 't':
/*  463 */       return 9;
/*      */     case 'n':
/*  466 */       return 10;
/*      */     case 'r':
/*  469 */       return 13;
/*      */     case 'f':
/*  472 */       return 12;
/*      */     case '0':
/*      */     case '1':
/*      */     case '2':
/*      */     case '3':
/*      */     case '4':
/*      */     case '5':
/*      */     case '6':
/*      */     case '7':
/*      */     case '8':
/*      */     case '9':
/*  486 */       if (((this.idx < this.len) && (Character.isDigit(this.pattern.charAt(this.idx)))) || (escapeChar == '0'))
/*      */       {
/*  489 */         int val = escapeChar - '0';
/*  490 */         if ((this.idx < this.len) && (Character.isDigit(this.pattern.charAt(this.idx))))
/*      */         {
/*  492 */           val = (val << 3) + (this.pattern.charAt(this.idx++) - '0');
/*  493 */           if ((this.idx < this.len) && (Character.isDigit(this.pattern.charAt(this.idx))))
/*      */           {
/*  495 */             val = (val << 3) + (this.pattern.charAt(this.idx++) - '0');
/*      */           }
/*      */         }
/*  498 */         return val;
/*      */       }
/*      */ 
/*  502 */       return 1048575;
/*      */     case ':':
/*      */     case ';':
/*      */     case '<':
/*      */     case '=':
/*      */     case '>':
/*      */     case '?':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'C':
/*      */     case 'E':
/*      */     case 'F':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'I':
/*      */     case 'J':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case 'Z':
/*      */     case '[':
/*      */     case '\\':
/*      */     case ']':
/*      */     case '^':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'a':
/*      */     case 'c':
/*      */     case 'e':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'm':
/*      */     case 'o':
/*      */     case 'p':
/*      */     case 'q':
/*  507 */     case 'v': } return escapeChar;
/*      */   }
/*      */ 
/*      */   int characterClass()
/*      */     throws RESyntaxException
/*      */   {
/*  519 */     if (this.pattern.charAt(this.idx) != '[')
/*      */     {
/*  521 */       internalError();
/*      */     }
/*      */ 
/*  525 */     if ((this.idx + 1 >= this.len) || (this.pattern.charAt(++this.idx) == ']'))
/*      */     {
/*  527 */       syntaxError("Empty or unterminated class");
/*      */     }
/*      */ 
/*  531 */     if ((this.idx < this.len) && (this.pattern.charAt(this.idx) == ':'))
/*      */     {
/*  534 */       this.idx += 1;
/*      */ 
/*  537 */       int idxStart = this.idx;
/*  538 */       while ((this.idx < this.len) && (this.pattern.charAt(this.idx) >= 'a') && (this.pattern.charAt(this.idx) <= 'z'))
/*      */       {
/*  540 */         this.idx += 1;
/*      */       }
/*      */ 
/*  544 */       if ((this.idx + 1 < this.len) && (this.pattern.charAt(this.idx) == ':') && (this.pattern.charAt(this.idx + 1) == ']'))
/*      */       {
/*  547 */         String charClass = this.pattern.substring(idxStart, this.idx);
/*      */ 
/*  550 */         Character i = (Character)hashPOSIX.get(charClass);
/*  551 */         if (i != null)
/*      */         {
/*  554 */           this.idx += 2;
/*      */ 
/*  557 */           return node('P', i.charValue());
/*      */         }
/*  559 */         syntaxError("Invalid POSIX character class '" + charClass + "'");
/*      */       }
/*  561 */       syntaxError("Invalid POSIX character class syntax");
/*      */     }
/*      */ 
/*  565 */     int ret = node('[', 0);
/*      */ 
/*  568 */     char CHAR_INVALID = 65535;
/*  569 */     char last = CHAR_INVALID;
/*  570 */     char simpleChar = '\000';
/*  571 */     boolean include = true;
/*  572 */     boolean definingRange = false;
/*  573 */     int idxFirst = this.idx;
/*  574 */     char rangeStart = '\000';
/*      */ 
/*  576 */     RERange range = new RERange();
/*  577 */     while ((this.idx < this.len) && (this.pattern.charAt(this.idx) != ']'))
/*      */     {
/*  583 */       switch (this.pattern.charAt(this.idx))
/*      */       {
/*      */       case '^':
/*  586 */         include = !include;
/*  587 */         if (this.idx == idxFirst)
/*      */         {
/*  589 */           range.include(0, 65535, true);
/*      */         }
/*  591 */         this.idx += 1;
/*  592 */         break;
/*      */       case '\\':
/*      */         int c;
/*  598 */         switch (c = escape())
/*      */         {
/*      */         case 1048574:
/*      */         case 1048575:
/*  604 */           syntaxError("Bad character class");
/*      */         case 1048573:
/*  609 */           if (definingRange)
/*      */           {
/*  611 */             syntaxError("Bad character class");
/*      */           }
/*      */ 
/*  615 */           switch (this.pattern.charAt(this.idx - 1))
/*      */           {
/*      */           case 'D':
/*      */           case 'S':
/*      */           case 'W':
/*  620 */             syntaxError("Bad character class");
/*      */           case 's':
/*  623 */             range.include('\t', include);
/*  624 */             range.include('\r', include);
/*  625 */             range.include('\f', include);
/*  626 */             range.include('\n', include);
/*  627 */             range.include('\b', include);
/*  628 */             range.include(' ', include);
/*  629 */             break;
/*      */           case 'w':
/*  632 */             range.include(97, 122, include);
/*  633 */             range.include(65, 90, include);
/*  634 */             range.include('_', include);
/*      */           case 'd':
/*  639 */             range.include(48, 57, include);
/*      */           }
/*      */ 
/*  644 */           last = CHAR_INVALID;
/*  645 */           break;
/*      */         default:
/*  650 */           simpleChar = (char)c;
/*      */         }
/*      */ 
/*  654 */         break;
/*      */       case '-':
/*  659 */         if (definingRange)
/*      */         {
/*  661 */           syntaxError("Bad class range");
/*      */         }
/*  663 */         definingRange = true;
/*      */ 
/*  666 */         rangeStart = last == CHAR_INVALID ? '\000' : last;
/*      */ 
/*  669 */         if ((this.idx + 1 < this.len) && (this.pattern.charAt(++this.idx) == ']'))
/*      */         {
/*  671 */           simpleChar = 65535;
/*  672 */         }break;
/*      */       default:
/*  677 */         simpleChar = this.pattern.charAt(this.idx++);
/*      */ 
/*  682 */         if (definingRange)
/*      */         {
/*  685 */           char rangeEnd = simpleChar;
/*      */ 
/*  688 */           if (rangeStart >= rangeEnd)
/*      */           {
/*  690 */             syntaxError("Bad character class");
/*      */           }
/*  692 */           range.include(rangeStart, rangeEnd, include);
/*      */ 
/*  695 */           last = CHAR_INVALID;
/*  696 */           definingRange = false;
/*      */         }
/*      */         else
/*      */         {
/*  701 */           if ((this.idx >= this.len) || (this.pattern.charAt(this.idx) != '-'))
/*      */           {
/*  703 */             range.include(simpleChar, include);
/*      */           }
/*  705 */           last = simpleChar;
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*  710 */     if (this.idx == this.len)
/*      */     {
/*  712 */       syntaxError("Unterminated character class");
/*      */     }
/*      */ 
/*  716 */     this.idx += 1;
/*      */ 
/*  719 */     this.instruction[(ret + 1)] = ((char)range.num);
/*  720 */     for (int i = 0; i < range.num; i++)
/*      */     {
/*  722 */       emit((char)range.minRange[i]);
/*  723 */       emit((char)range.maxRange[i]);
/*      */     }
/*  725 */     return ret;
/*      */   }
/*      */ 
/*      */   int atom()
/*      */     throws RESyntaxException
/*      */   {
/*  739 */     int ret = node('A', 0);
/*      */ 
/*  742 */     int lenAtom = 0;
/*      */ 
/*  748 */     while (this.idx < this.len)
/*      */     {
/*  751 */       if (this.idx + 1 < this.len)
/*      */       {
/*  753 */         char c = this.pattern.charAt(this.idx + 1);
/*      */ 
/*  756 */         if (this.pattern.charAt(this.idx) == '\\')
/*      */         {
/*  758 */           int idxEscape = this.idx;
/*  759 */           escape();
/*  760 */           if (this.idx < this.len)
/*      */           {
/*  762 */             c = this.pattern.charAt(this.idx);
/*      */           }
/*  764 */           this.idx = idxEscape;
/*      */         }
/*      */ 
/*  768 */         switch (c)
/*      */         {
/*      */         case '*':
/*      */         case '+':
/*      */         case '?':
/*      */         case '{':
/*  777 */           if (lenAtom != 0)
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  785 */       switch (this.pattern.charAt(this.idx))
/*      */       {
/*      */       case '$':
/*      */       case '(':
/*      */       case ')':
/*      */       case '.':
/*      */       case '[':
/*      */       case ']':
/*      */       case '^':
/*      */       case '|':
/*  795 */         break;
/*      */       case '*':
/*      */       case '+':
/*      */       case '?':
/*      */       case '{':
/*  803 */         if (lenAtom != 0) {
/*      */           break label366;
/*      */         }
/*  806 */         syntaxError("Missing operand to closure"); break;
/*      */       case '\\':
/*  814 */         int idxBeforeEscape = this.idx;
/*  815 */         int c = escape();
/*      */ 
/*  818 */         if ((c & 0xFFFF0) == 1048560)
/*      */         {
/*  821 */           this.idx = idxBeforeEscape;
/*  822 */           break label366;
/*      */         }
/*      */ 
/*  826 */         emit((char)c);
/*  827 */         lenAtom++;
/*      */ 
/*  829 */         break;
/*      */       default:
/*  834 */         emit(this.pattern.charAt(this.idx++));
/*  835 */         lenAtom++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  841 */     label366: if (lenAtom == 0)
/*      */     {
/*  843 */       internalError();
/*      */     }
/*      */ 
/*  847 */     this.instruction[(ret + 1)] = ((char)lenAtom);
/*  848 */     return ret;
/*      */   }
/*      */ 
/*      */   int terminal(int[] flags)
/*      */     throws RESyntaxException
/*      */   {
/*  859 */     switch (this.pattern.charAt(this.idx))
/*      */     {
/*      */     case '$':
/*      */     case '.':
/*      */     case '^':
/*  864 */       return node(this.pattern.charAt(this.idx++), 0);
/*      */     case '[':
/*  867 */       return characterClass();
/*      */     case '(':
/*  870 */       return expr(flags);
/*      */     case ')':
/*  873 */       syntaxError("Unexpected close paren");
/*      */     case '|':
/*  876 */       internalError();
/*      */     case ']':
/*  879 */       syntaxError("Mismatched class");
/*      */     case '\000':
/*  882 */       syntaxError("Unexpected end of input");
/*      */     case '*':
/*      */     case '+':
/*      */     case '?':
/*      */     case '{':
/*  888 */       syntaxError("Missing operand to closure");
/*      */     case '\\':
/*  893 */       int idxBeforeEscape = this.idx;
/*      */ 
/*  896 */       switch (escape())
/*      */       {
/*      */       case 1048573:
/*      */       case 1048574:
/*  900 */         flags[0] &= -2;
/*  901 */         return node('\\', this.pattern.charAt(this.idx - 1));
/*      */       case 1048575:
/*  905 */         char backreference = (char)(this.pattern.charAt(this.idx - 1) - '0');
/*  906 */         if (this.parens <= backreference)
/*      */         {
/*  908 */           syntaxError("Bad backreference");
/*      */         }
/*  910 */         flags[0] |= 1;
/*  911 */         return node('#', backreference);
/*      */       }
/*      */ 
/*  918 */       this.idx = idxBeforeEscape;
/*  919 */       flags[0] &= -2;
/*      */     }
/*      */ 
/*  927 */     flags[0] &= -2;
/*  928 */     return atom();
/*      */   }
/*      */ 
/*      */   int closure(int[] flags)
/*      */     throws RESyntaxException
/*      */   {
/*  940 */     int idxBeforeTerminal = this.idx;
/*      */ 
/*  943 */     int[] terminalFlags = { 0 };
/*      */ 
/*  946 */     int ret = terminal(terminalFlags);
/*      */ 
/*  949 */     flags[0] |= terminalFlags[0];
/*      */ 
/*  952 */     if (this.idx >= this.len)
/*      */     {
/*  954 */       return ret;
/*      */     }
/*  956 */     boolean greedy = true;
/*  957 */     char closureType = this.pattern.charAt(this.idx);
/*  958 */     switch (closureType)
/*      */     {
/*      */     case '*':
/*      */     case '?':
/*  964 */       flags[0] |= 1;
/*      */     case '+':
/*  969 */       this.idx += 1;
/*      */     case '{':
/*  974 */       int opcode = this.instruction[(ret + 0)];
/*  975 */       if ((opcode == 94) || (opcode == 36))
/*      */       {
/*  977 */         syntaxError("Bad closure operand");
/*      */       }
/*  979 */       if ((terminalFlags[0] & 0x1) != 0)
/*      */       {
/*  981 */         syntaxError("Closure operand can't be nullable");
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  987 */     if ((this.idx < this.len) && (this.pattern.charAt(this.idx) == '?'))
/*      */     {
/*  989 */       this.idx += 1;
/*  990 */       greedy = false;
/*      */     }
/*      */ 
/*  993 */     if (greedy)
/*      */     {
/*  996 */       switch (closureType)
/*      */       {
/*      */       case '{':
/* 1001 */         boolean found = false;
/*      */ 
/* 1003 */         allocBrackets();
/* 1004 */         for (int i = 0; i < this.brackets; i++)
/*      */         {
/* 1006 */           if (this.bracketStart[i] == this.idx)
/*      */           {
/* 1008 */             found = true;
/* 1009 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1014 */         if (!found)
/*      */         {
/* 1016 */           if (this.brackets >= this.maxBrackets)
/*      */           {
/* 1018 */             reallocBrackets();
/*      */           }
/* 1020 */           this.bracketStart[this.brackets] = this.idx;
/* 1021 */           bracket();
/* 1022 */           this.bracketEnd[this.brackets] = this.idx;
/* 1023 */           i = this.brackets++;
/*      */         }
/*      */         int tmp370_368 = i;
/*      */         int[] tmp370_365 = this.bracketMin;
/*      */         int tmp372_371 = tmp370_365[tmp370_368]; tmp370_365[tmp370_368] = (tmp372_371 - 1); if (tmp372_371 > 0)
/*      */         {
/* 1029 */           if ((this.bracketMin[i] > 0) || (this.bracketOpt[i] != 0))
/*      */           {
/* 1031 */             for (int j = 0; j < this.brackets; j++) {
/* 1032 */               if ((j != i) && (this.bracketStart[j] < this.idx) && (this.bracketStart[j] >= idxBeforeTerminal))
/*      */               {
/* 1035 */                 this.brackets -= 1;
/* 1036 */                 this.bracketStart[j] = this.bracketStart[this.brackets];
/* 1037 */                 this.bracketEnd[j] = this.bracketEnd[this.brackets];
/* 1038 */                 this.bracketMin[j] = this.bracketMin[this.brackets];
/* 1039 */                 this.bracketOpt[j] = this.bracketOpt[this.brackets];
/*      */               }
/*      */             }
/*      */ 
/* 1043 */             this.idx = idxBeforeTerminal;
/*      */           }
/*      */           else {
/* 1046 */             this.idx = this.bracketEnd[i];
/*      */           }
/*      */ 
/*      */         }
/* 1052 */         else if (this.bracketOpt[i] == -1)
/*      */         {
/* 1056 */           closureType = '*';
/* 1057 */           this.bracketOpt[i] = 0;
/* 1058 */           this.idx = this.bracketEnd[i];
/*      */         }
/*      */         else
/*      */         {
/*      */           int tmp588_586 = i;
/*      */           int[] tmp588_583 = this.bracketOpt;
/*      */           int tmp590_589 = tmp588_583[tmp588_586]; tmp588_583[tmp588_586] = (tmp590_589 - 1); if (tmp590_589 > 0)
/*      */           {
/* 1063 */             if (this.bracketOpt[i] > 0)
/*      */             {
/* 1066 */               this.idx = idxBeforeTerminal;
/*      */             }
/*      */             else {
/* 1069 */               this.idx = this.bracketEnd[i];
/*      */             }
/*      */ 
/* 1072 */             closureType = '?';
/*      */           }
/*      */           else
/*      */           {
/* 1077 */             this.lenInstruction = ret;
/* 1078 */             node('N', 0);
/*      */ 
/* 1081 */             this.idx = this.bracketEnd[i]; } 
/* 1082 */         }break;
/*      */       case '*':
/*      */       case '?':
/* 1091 */         if (greedy)
/*      */         {
/* 1096 */           if (closureType == '?')
/*      */           {
/* 1099 */             nodeInsert('|', 0, ret);
/* 1100 */             setNextOfEnd(ret, node('|', 0));
/* 1101 */             int nothing = node('N', 0);
/* 1102 */             setNextOfEnd(ret, nothing);
/* 1103 */             setNextOfEnd(ret + 3, nothing);
/*      */           }
/*      */ 
/* 1106 */           if (closureType == '*')
/*      */           {
/* 1109 */             nodeInsert('|', 0, ret);
/* 1110 */             setNextOfEnd(ret + 3, node('|', 0));
/* 1111 */             setNextOfEnd(ret + 3, node('G', 0));
/* 1112 */             setNextOfEnd(ret + 3, ret);
/* 1113 */             setNextOfEnd(ret, node('|', 0));
/* 1114 */             setNextOfEnd(ret, node('N', 0)); }  } break;
/*      */       case '+':
/* 1122 */         int branch = node('|', 0);
/* 1123 */         setNextOfEnd(ret, branch);
/* 1124 */         setNextOfEnd(node('G', 0), ret);
/* 1125 */         setNextOfEnd(branch, node('|', 0));
/* 1126 */         setNextOfEnd(ret, node('N', 0));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1134 */       setNextOfEnd(ret, node('E', 0));
/*      */ 
/* 1137 */       switch (closureType)
/*      */       {
/*      */       case '?':
/* 1140 */         nodeInsert('/', 0, ret);
/* 1141 */         break;
/*      */       case '*':
/* 1144 */         nodeInsert('8', 0, ret);
/* 1145 */         break;
/*      */       case '+':
/* 1148 */         nodeInsert('=', 0, ret);
/*      */       }
/*      */ 
/* 1153 */       setNextOfEnd(ret, this.lenInstruction);
/*      */     }
/* 1155 */     return ret;
/*      */   }
/*      */ 
/*      */   int branch(int[] flags)
/*      */     throws RESyntaxException
/*      */   {
/* 1168 */     int ret = node('|', 0);
/* 1169 */     int chain = -1;
/* 1170 */     int[] closureFlags = new int[1];
/* 1171 */     boolean nullable = true;
/* 1172 */     while ((this.idx < this.len) && (this.pattern.charAt(this.idx) != '|') && (this.pattern.charAt(this.idx) != ')'))
/*      */     {
/* 1175 */       closureFlags[0] = 0;
/* 1176 */       int node = closure(closureFlags);
/* 1177 */       if (closureFlags[0] == 0)
/*      */       {
/* 1179 */         nullable = false;
/*      */       }
/*      */ 
/* 1183 */       if (chain != -1)
/*      */       {
/* 1185 */         setNextOfEnd(chain, node);
/*      */       }
/*      */ 
/* 1189 */       chain = node;
/*      */     }
/*      */ 
/* 1193 */     if (chain == -1)
/*      */     {
/* 1195 */       node('N', 0);
/*      */     }
/*      */ 
/* 1199 */     if (nullable)
/*      */     {
/* 1201 */       flags[0] |= 1;
/*      */     }
/* 1203 */     return ret;
/*      */   }
/*      */ 
/*      */   int expr(int[] flags)
/*      */     throws RESyntaxException
/*      */   {
/* 1216 */     int paren = -1;
/* 1217 */     int ret = -1;
/* 1218 */     int closeParens = this.parens;
/* 1219 */     if (((flags[0] & 0x2) == 0) && (this.pattern.charAt(this.idx) == '('))
/*      */     {
/* 1222 */       if ((this.idx + 2 < this.len) && (this.pattern.charAt(this.idx + 1) == '?') && (this.pattern.charAt(this.idx + 2) == ':'))
/*      */       {
/* 1224 */         paren = 2;
/* 1225 */         this.idx += 3;
/* 1226 */         ret = node('<', 0);
/*      */       }
/*      */       else
/*      */       {
/* 1230 */         paren = 1;
/* 1231 */         this.idx += 1;
/* 1232 */         ret = node('(', this.parens++);
/*      */       }
/*      */     }
/* 1235 */     flags[0] &= -3;
/*      */ 
/* 1238 */     int branch = branch(flags);
/* 1239 */     if (ret == -1)
/*      */     {
/* 1241 */       ret = branch;
/*      */     }
/*      */     else
/*      */     {
/* 1245 */       setNextOfEnd(ret, branch);
/*      */     }
/*      */ 
/* 1249 */     while ((this.idx < this.len) && (this.pattern.charAt(this.idx) == '|'))
/*      */     {
/* 1251 */       this.idx += 1;
/* 1252 */       branch = branch(flags);
/* 1253 */       setNextOfEnd(ret, branch);
/*      */     }
/*      */     int end;
/*      */     int end;
/* 1258 */     if (paren > 0)
/*      */     {
/* 1260 */       if ((this.idx < this.len) && (this.pattern.charAt(this.idx) == ')'))
/*      */       {
/* 1262 */         this.idx += 1;
/*      */       }
/*      */       else
/*      */       {
/* 1266 */         syntaxError("Missing close paren");
/*      */       }
/*      */       int end;
/* 1268 */       if (paren == 1)
/*      */       {
/* 1270 */         end = node(')', closeParens);
/*      */       }
/*      */       else
/*      */       {
/* 1274 */         end = node('>', 0);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1279 */       end = node('E', 0);
/*      */     }
/*      */ 
/* 1283 */     setNextOfEnd(ret, end);
/*      */ 
/* 1286 */     int currentNode = ret;
/* 1287 */     int nextNodeOffset = this.instruction[(currentNode + 2)];
/*      */ 
/* 1289 */     while ((nextNodeOffset != 0) && (currentNode < this.lenInstruction))
/*      */     {
/* 1292 */       if (this.instruction[(currentNode + 0)] == '|')
/*      */       {
/* 1294 */         setNextOfEnd(currentNode + 3, end);
/*      */       }
/* 1296 */       nextNodeOffset = this.instruction[(currentNode + 2)];
/* 1297 */       currentNode += nextNodeOffset;
/*      */     }
/*      */ 
/* 1301 */     return ret;
/*      */   }
/*      */ 
/*      */   public REProgram compile(String pattern)
/*      */     throws RESyntaxException
/*      */   {
/* 1317 */     this.pattern = pattern;
/* 1318 */     this.len = pattern.length();
/* 1319 */     this.idx = 0;
/* 1320 */     this.lenInstruction = 0;
/* 1321 */     this.parens = 1;
/* 1322 */     this.brackets = 0;
/*      */ 
/* 1325 */     int[] flags = { 2 };
/*      */ 
/* 1328 */     expr(flags);
/*      */ 
/* 1331 */     if (this.idx != this.len)
/*      */     {
/* 1333 */       if (pattern.charAt(this.idx) == ')')
/*      */       {
/* 1335 */         syntaxError("Unmatched close paren");
/*      */       }
/* 1337 */       syntaxError("Unexpected input remains");
/*      */     }
/*      */ 
/* 1341 */     char[] ins = new char[this.lenInstruction];
/* 1342 */     System.arraycopy(this.instruction, 0, ins, 0, this.lenInstruction);
/* 1343 */     return new REProgram(this.parens, ins);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   75 */     hashPOSIX.put("alnum", new Character('w'));
/*   76 */     hashPOSIX.put("alpha", new Character('a'));
/*   77 */     hashPOSIX.put("blank", new Character('b'));
/*   78 */     hashPOSIX.put("cntrl", new Character('c'));
/*   79 */     hashPOSIX.put("digit", new Character('d'));
/*   80 */     hashPOSIX.put("graph", new Character('g'));
/*   81 */     hashPOSIX.put("lower", new Character('l'));
/*   82 */     hashPOSIX.put("print", new Character('p'));
/*   83 */     hashPOSIX.put("punct", new Character('!'));
/*   84 */     hashPOSIX.put("space", new Character('s'));
/*   85 */     hashPOSIX.put("upper", new Character('u'));
/*   86 */     hashPOSIX.put("xdigit", new Character('x'));
/*   87 */     hashPOSIX.put("javastart", new Character('j'));
/*   88 */     hashPOSIX.put("javapart", new Character('k'));
/*      */   }
/*      */ 
/*      */   class RERange
/*      */   {
/* 1351 */     int size = 16;
/* 1352 */     int[] minRange = new int[this.size];
/* 1353 */     int[] maxRange = new int[this.size];
/* 1354 */     int num = 0;
/*      */ 
/*      */     RERange()
/*      */     {
/*      */     }
/*      */ 
/*      */     void delete(int index)
/*      */     {
/* 1363 */       if ((this.num == 0) || (index >= this.num))
/*      */       {
/*      */         return;
/*      */       }
/*      */       while (true)
/*      */       {
/* 1369 */         index++; if (index >= this.num)
/*      */           break;
/* 1371 */         if (index - 1 >= 0)
/*      */         {
/* 1373 */           this.minRange[(index - 1)] = this.minRange[index];
/* 1374 */           this.maxRange[(index - 1)] = this.maxRange[index];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1379 */       this.num -= 1;
/*      */     }
/*      */ 
/*      */     void merge(int min, int max)
/*      */     {
/* 1390 */       for (int i = 0; i < this.num; i++)
/*      */       {
/* 1393 */         if ((min >= this.minRange[i]) && (max <= this.maxRange[i]))
/*      */         {
/* 1395 */           return;
/*      */         }
/*      */ 
/* 1399 */         if ((min <= this.minRange[i]) && (max >= this.maxRange[i]))
/*      */         {
/* 1401 */           delete(i);
/* 1402 */           merge(min, max);
/* 1403 */           return;
/*      */         }
/*      */ 
/* 1407 */         if ((min >= this.minRange[i]) && (min <= this.maxRange[i]))
/*      */         {
/* 1409 */           delete(i);
/* 1410 */           min = this.minRange[i];
/* 1411 */           merge(min, max);
/* 1412 */           return;
/*      */         }
/*      */ 
/* 1416 */         if ((max >= this.minRange[i]) && (max <= this.maxRange[i]))
/*      */         {
/* 1418 */           delete(i);
/* 1419 */           max = this.maxRange[i];
/* 1420 */           merge(min, max);
/* 1421 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1426 */       if (this.num >= this.size)
/*      */       {
/* 1428 */         this.size *= 2;
/* 1429 */         int[] newMin = new int[this.size];
/* 1430 */         int[] newMax = new int[this.size];
/* 1431 */         System.arraycopy(this.minRange, 0, newMin, 0, this.num);
/* 1432 */         System.arraycopy(this.maxRange, 0, newMax, 0, this.num);
/* 1433 */         this.minRange = newMin;
/* 1434 */         this.maxRange = newMax;
/*      */       }
/* 1436 */       this.minRange[this.num] = min;
/* 1437 */       this.maxRange[this.num] = max;
/* 1438 */       this.num += 1;
/*      */     }
/*      */ 
/*      */     void remove(int min, int max)
/*      */     {
/* 1449 */       for (int i = 0; i < this.num; i++)
/*      */       {
/* 1452 */         if ((this.minRange[i] >= min) && (this.maxRange[i] <= max))
/*      */         {
/* 1454 */           delete(i);
/* 1455 */           i--;
/* 1456 */           return;
/*      */         }
/*      */ 
/* 1460 */         if ((min >= this.minRange[i]) && (max <= this.maxRange[i]))
/*      */         {
/* 1462 */           int minr = this.minRange[i];
/* 1463 */           int maxr = this.maxRange[i];
/* 1464 */           delete(i);
/* 1465 */           if (minr < min)
/*      */           {
/* 1467 */             merge(minr, min - 1);
/*      */           }
/* 1469 */           if (max < maxr)
/*      */           {
/* 1471 */             merge(max + 1, maxr);
/*      */           }
/* 1473 */           return;
/*      */         }
/*      */ 
/* 1477 */         if ((this.minRange[i] >= min) && (this.minRange[i] <= max))
/*      */         {
/* 1479 */           this.minRange[i] = (max + 1);
/* 1480 */           return;
/*      */         }
/*      */ 
/* 1484 */         if ((this.maxRange[i] >= min) && (this.maxRange[i] <= max))
/*      */         {
/* 1486 */           this.maxRange[i] = (min - 1);
/* 1487 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void include(int min, int max, boolean include)
/*      */     {
/* 1500 */       if (include)
/*      */       {
/* 1502 */         merge(min, max);
/*      */       }
/*      */       else
/*      */       {
/* 1506 */         remove(min, max);
/*      */       }
/*      */     }
/*      */ 
/*      */     void include(char minmax, boolean include)
/*      */     {
/* 1517 */       include(minmax, minmax, include);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.RECompiler
 * JD-Core Version:    0.6.2
 */