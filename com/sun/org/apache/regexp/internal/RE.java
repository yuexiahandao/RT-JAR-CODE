/*      */ package com.sun.org.apache.regexp.internal;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class RE
/*      */   implements Serializable
/*      */ {
/*      */   public static final int MATCH_NORMAL = 0;
/*      */   public static final int MATCH_CASEINDEPENDENT = 1;
/*      */   public static final int MATCH_MULTILINE = 2;
/*      */   public static final int MATCH_SINGLELINE = 4;
/*      */   static final char OP_END = 'E';
/*      */   static final char OP_BOL = '^';
/*      */   static final char OP_EOL = '$';
/*      */   static final char OP_ANY = '.';
/*      */   static final char OP_ANYOF = '[';
/*      */   static final char OP_BRANCH = '|';
/*      */   static final char OP_ATOM = 'A';
/*      */   static final char OP_STAR = '*';
/*      */   static final char OP_PLUS = '+';
/*      */   static final char OP_MAYBE = '?';
/*      */   static final char OP_ESCAPE = '\\';
/*      */   static final char OP_OPEN = '(';
/*      */   static final char OP_OPEN_CLUSTER = '<';
/*      */   static final char OP_CLOSE = ')';
/*      */   static final char OP_CLOSE_CLUSTER = '>';
/*      */   static final char OP_BACKREF = '#';
/*      */   static final char OP_GOTO = 'G';
/*      */   static final char OP_NOTHING = 'N';
/*      */   static final char OP_RELUCTANTSTAR = '8';
/*      */   static final char OP_RELUCTANTPLUS = '=';
/*      */   static final char OP_RELUCTANTMAYBE = '/';
/*      */   static final char OP_POSIXCLASS = 'P';
/*      */   static final char E_ALNUM = 'w';
/*      */   static final char E_NALNUM = 'W';
/*      */   static final char E_BOUND = 'b';
/*      */   static final char E_NBOUND = 'B';
/*      */   static final char E_SPACE = 's';
/*      */   static final char E_NSPACE = 'S';
/*      */   static final char E_DIGIT = 'd';
/*      */   static final char E_NDIGIT = 'D';
/*      */   static final char POSIX_CLASS_ALNUM = 'w';
/*      */   static final char POSIX_CLASS_ALPHA = 'a';
/*      */   static final char POSIX_CLASS_BLANK = 'b';
/*      */   static final char POSIX_CLASS_CNTRL = 'c';
/*      */   static final char POSIX_CLASS_DIGIT = 'd';
/*      */   static final char POSIX_CLASS_GRAPH = 'g';
/*      */   static final char POSIX_CLASS_LOWER = 'l';
/*      */   static final char POSIX_CLASS_PRINT = 'p';
/*      */   static final char POSIX_CLASS_PUNCT = '!';
/*      */   static final char POSIX_CLASS_SPACE = 's';
/*      */   static final char POSIX_CLASS_UPPER = 'u';
/*      */   static final char POSIX_CLASS_XDIGIT = 'x';
/*      */   static final char POSIX_CLASS_JSTART = 'j';
/*      */   static final char POSIX_CLASS_JPART = 'k';
/*      */   static final int maxNode = 65536;
/*      */   static final int MAX_PAREN = 16;
/*      */   static final int offsetOpcode = 0;
/*      */   static final int offsetOpdata = 1;
/*      */   static final int offsetNext = 2;
/*      */   static final int nodeSize = 3;
/*      */   REProgram program;
/*      */   transient CharacterIterator search;
/*      */   int matchFlags;
/*  397 */   int maxParen = 16;
/*      */   transient int parenCount;
/*      */   transient int start0;
/*      */   transient int end0;
/*      */   transient int start1;
/*      */   transient int end1;
/*      */   transient int start2;
/*      */   transient int end2;
/*      */   transient int[] startn;
/*      */   transient int[] endn;
/*      */   transient int[] startBackref;
/*      */   transient int[] endBackref;
/*      */   public static final int REPLACE_ALL = 0;
/*      */   public static final int REPLACE_FIRSTONLY = 1;
/*      */   public static final int REPLACE_BACKREFERENCES = 2;
/*      */ 
/*      */   public RE(String pattern)
/*      */     throws RESyntaxException
/*      */   {
/*  426 */     this(pattern, 0);
/*      */   }
/*      */ 
/*      */   public RE(String pattern, int matchFlags)
/*      */     throws RESyntaxException
/*      */   {
/*  442 */     this(new RECompiler().compile(pattern));
/*  443 */     setMatchFlags(matchFlags);
/*      */   }
/*      */ 
/*      */   public RE(REProgram program, int matchFlags)
/*      */   {
/*  466 */     setProgram(program);
/*  467 */     setMatchFlags(matchFlags);
/*      */   }
/*      */ 
/*      */   public RE(REProgram program)
/*      */   {
/*  480 */     this(program, 0);
/*      */   }
/*      */ 
/*      */   public RE()
/*      */   {
/*  489 */     this((REProgram)null, 0);
/*      */   }
/*      */ 
/*      */   public static String simplePatternToFullRegularExpression(String pattern)
/*      */   {
/*  500 */     StringBuffer buf = new StringBuffer();
/*  501 */     for (int i = 0; i < pattern.length(); i++)
/*      */     {
/*  503 */       char c = pattern.charAt(i);
/*  504 */       switch (c)
/*      */       {
/*      */       case '*':
/*  507 */         buf.append(".*");
/*  508 */         break;
/*      */       case '$':
/*      */       case '(':
/*      */       case ')':
/*      */       case '+':
/*      */       case '.':
/*      */       case '?':
/*      */       case '[':
/*      */       case '\\':
/*      */       case ']':
/*      */       case '^':
/*      */       case '{':
/*      */       case '|':
/*      */       case '}':
/*  523 */         buf.append('\\');
/*      */       default:
/*  525 */         buf.append(c);
/*      */       }
/*      */     }
/*      */ 
/*  529 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   public void setMatchFlags(int matchFlags)
/*      */   {
/*  544 */     this.matchFlags = matchFlags;
/*      */   }
/*      */ 
/*      */   public int getMatchFlags()
/*      */   {
/*  561 */     return this.matchFlags;
/*      */   }
/*      */ 
/*      */   public void setProgram(REProgram program)
/*      */   {
/*  574 */     this.program = program;
/*  575 */     if ((program != null) && (program.maxParens != -1))
/*  576 */       this.maxParen = program.maxParens;
/*      */     else
/*  578 */       this.maxParen = 16;
/*      */   }
/*      */ 
/*      */   public REProgram getProgram()
/*      */   {
/*  590 */     return this.program;
/*      */   }
/*      */ 
/*      */   public int getParenCount()
/*      */   {
/*  600 */     return this.parenCount;
/*      */   }
/*      */ 
/*      */   public String getParen(int which)
/*      */   {
/*      */     int start;
/*  612 */     if ((which < this.parenCount) && ((start = getParenStart(which)) >= 0))
/*      */     {
/*  614 */       return this.search.substring(start, getParenEnd(which));
/*      */     }
/*  616 */     return null;
/*      */   }
/*      */ 
/*      */   public final int getParenStart(int which)
/*      */   {
/*  627 */     if (which < this.parenCount)
/*      */     {
/*  629 */       switch (which)
/*      */       {
/*      */       case 0:
/*  632 */         return this.start0;
/*      */       case 1:
/*  635 */         return this.start1;
/*      */       case 2:
/*  638 */         return this.start2;
/*      */       }
/*      */ 
/*  641 */       if (this.startn == null)
/*      */       {
/*  643 */         allocParens();
/*      */       }
/*  645 */       return this.startn[which];
/*      */     }
/*      */ 
/*  648 */     return -1;
/*      */   }
/*      */ 
/*      */   public final int getParenEnd(int which)
/*      */   {
/*  659 */     if (which < this.parenCount)
/*      */     {
/*  661 */       switch (which)
/*      */       {
/*      */       case 0:
/*  664 */         return this.end0;
/*      */       case 1:
/*  667 */         return this.end1;
/*      */       case 2:
/*  670 */         return this.end2;
/*      */       }
/*      */ 
/*  673 */       if (this.endn == null)
/*      */       {
/*  675 */         allocParens();
/*      */       }
/*  677 */       return this.endn[which];
/*      */     }
/*      */ 
/*  680 */     return -1;
/*      */   }
/*      */ 
/*      */   public final int getParenLength(int which)
/*      */   {
/*  691 */     if (which < this.parenCount)
/*      */     {
/*  693 */       return getParenEnd(which) - getParenStart(which);
/*      */     }
/*  695 */     return -1;
/*      */   }
/*      */ 
/*      */   protected final void setParenStart(int which, int i)
/*      */   {
/*  706 */     if (which < this.parenCount)
/*      */     {
/*  708 */       switch (which)
/*      */       {
/*      */       case 0:
/*  711 */         this.start0 = i;
/*  712 */         break;
/*      */       case 1:
/*  715 */         this.start1 = i;
/*  716 */         break;
/*      */       case 2:
/*  719 */         this.start2 = i;
/*  720 */         break;
/*      */       default:
/*  723 */         if (this.startn == null)
/*      */         {
/*  725 */           allocParens();
/*      */         }
/*  727 */         this.startn[which] = i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void setParenEnd(int which, int i)
/*      */   {
/*  741 */     if (which < this.parenCount)
/*      */     {
/*  743 */       switch (which)
/*      */       {
/*      */       case 0:
/*  746 */         this.end0 = i;
/*  747 */         break;
/*      */       case 1:
/*  750 */         this.end1 = i;
/*  751 */         break;
/*      */       case 2:
/*  754 */         this.end2 = i;
/*  755 */         break;
/*      */       default:
/*  758 */         if (this.endn == null)
/*      */         {
/*  760 */           allocParens();
/*      */         }
/*  762 */         this.endn[which] = i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void internalError(String s)
/*      */     throws Error
/*      */   {
/*  777 */     throw new Error("RE internal error: " + s);
/*      */   }
/*      */ 
/*      */   private final void allocParens()
/*      */   {
/*  786 */     this.startn = new int[this.maxParen];
/*  787 */     this.endn = new int[this.maxParen];
/*      */ 
/*  790 */     for (int i = 0; i < this.maxParen; i++)
/*      */     {
/*  792 */       this.startn[i] = -1;
/*  793 */       this.endn[i] = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int matchNodes(int firstNode, int lastNode, int idxStart)
/*      */   {
/*  809 */     int idx = idxStart;
/*      */ 
/*  814 */     char[] instruction = this.program.instruction;
/*  815 */     for (int node = firstNode; node < lastNode; )
/*      */     {
/*  817 */       int opcode = instruction[(node + 0)];
/*  818 */       int next = node + (short)instruction[(node + 2)];
/*  819 */       int opdata = instruction[(node + 1)];
/*      */       int idxNew;
/*      */       int idxNew;
/*  821 */       switch (opcode)
/*      */       {
/*      */       case 47:
/*  825 */         int once = 0;
/*      */         do
/*      */         {
/*  829 */           if ((idxNew = matchNodes(next, 65536, idx)) != -1)
/*      */           {
/*  831 */             return idxNew;
/*      */           }
/*      */         }
/*  834 */         while ((once++ == 0) && ((idx = matchNodes(node + 3, next, idx)) != -1));
/*  835 */         return -1;
/*      */       case 61:
/*  839 */         while ((idx = matchNodes(node + 3, next, idx)) != -1)
/*      */         {
/*  842 */           if ((idxNew = matchNodes(next, 65536, idx)) != -1)
/*      */           {
/*  844 */             return idxNew;
/*      */           }
/*      */         }
/*  847 */         return -1;
/*      */       case 56:
/*      */         do
/*      */         {
/*  853 */           if ((idxNew = matchNodes(next, 65536, idx)) != -1)
/*      */           {
/*  855 */             return idxNew;
/*      */           }
/*      */         }
/*  858 */         while ((idx = matchNodes(node + 3, next, idx)) != -1);
/*  859 */         return -1;
/*      */       case 40:
/*  864 */         if ((this.program.flags & 0x1) != 0)
/*      */         {
/*  866 */           this.startBackref[opdata] = idx;
/*      */         }
/*  868 */         if ((idxNew = matchNodes(next, 65536, idx)) != -1)
/*      */         {
/*  871 */           if (opdata + 1 > this.parenCount)
/*      */           {
/*  873 */             this.parenCount = (opdata + 1);
/*      */           }
/*      */ 
/*  877 */           if (getParenStart(opdata) == -1)
/*      */           {
/*  879 */             setParenStart(opdata, idx);
/*      */           }
/*      */         }
/*  882 */         return idxNew;
/*      */       case 41:
/*  887 */         if ((this.program.flags & 0x1) != 0)
/*      */         {
/*  889 */           this.endBackref[opdata] = idx;
/*      */         }
/*  891 */         if ((idxNew = matchNodes(next, 65536, idx)) != -1)
/*      */         {
/*  894 */           if (opdata + 1 > this.parenCount)
/*      */           {
/*  896 */             this.parenCount = (opdata + 1);
/*      */           }
/*      */ 
/*  900 */           if (getParenEnd(opdata) == -1)
/*      */           {
/*  902 */             setParenEnd(opdata, idx);
/*      */           }
/*      */         }
/*  905 */         return idxNew;
/*      */       case 60:
/*      */       case 62:
/*  910 */         return matchNodes(next, 65536, idx);
/*      */       case 35:
/*  915 */         int s = this.startBackref[opdata];
/*  916 */         int e = this.endBackref[opdata];
/*      */ 
/*  919 */         if ((s == -1) || (e == -1))
/*      */         {
/*  921 */           return -1;
/*      */         }
/*      */ 
/*  925 */         if (s != e)
/*      */         {
/*  931 */           int l = e - s;
/*      */ 
/*  934 */           if (this.search.isEnd(idx + l - 1))
/*      */           {
/*  936 */             return -1;
/*      */           }
/*      */ 
/*  940 */           boolean caseFold = (this.matchFlags & 0x1) != 0;
/*      */ 
/*  943 */           for (int i = 0; i < l; i++)
/*      */           {
/*  945 */             if (compareChars(this.search.charAt(idx++), this.search.charAt(s + i), caseFold) != 0)
/*      */             {
/*  947 */               return -1;
/*      */             }
/*      */           }
/*      */         }
/*  951 */         break;
/*      */       case 94:
/*  956 */         if (idx != 0)
/*      */         {
/*  959 */           if ((this.matchFlags & 0x2) == 2)
/*      */           {
/*  962 */             if ((idx <= 0) || (!isNewline(idx - 1))) {
/*  963 */               return -1;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  968 */             return -1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       case 36:
/*  975 */         if ((!this.search.isEnd(0)) && (!this.search.isEnd(idx)))
/*      */         {
/*  978 */           if ((this.matchFlags & 0x2) == 2)
/*      */           {
/*  981 */             if (!isNewline(idx)) {
/*  982 */               return -1;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  987 */             return -1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       case 92:
/*  994 */         switch (opdata)
/*      */         {
/*      */         case 66:
/*      */         case 98:
/* 1000 */           char cLast = idx == 0 ? '\n' : this.search.charAt(idx - 1);
/* 1001 */           char cNext = this.search.isEnd(idx) ? '\n' : this.search.charAt(idx);
/* 1002 */           if ((Character.isLetterOrDigit(cLast) == Character.isLetterOrDigit(cNext) ? 1 : 0) == (opdata == 98 ? 1 : 0))
/*      */           {
/* 1004 */             return -1;
/*      */           }
/*      */ 
/* 1007 */           break;
/*      */         case 68:
/*      */         case 83:
/*      */         case 87:
/*      */         case 100:
/*      */         case 115:
/*      */         case 119:
/* 1018 */           if (this.search.isEnd(idx))
/*      */           {
/* 1020 */             return -1;
/*      */           }
/*      */ 
/* 1023 */           char c = this.search.charAt(idx);
/*      */ 
/* 1026 */           switch (opdata)
/*      */           {
/*      */           case 87:
/*      */           case 119:
/* 1030 */             if (((Character.isLetterOrDigit(c)) || (c == '_') ? 1 : 0) != (opdata == 119 ? 1 : 0))
/*      */             {
/* 1032 */               return -1;
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 68:
/*      */           case 100:
/* 1038 */             if (Character.isDigit(c) != (opdata == 100))
/*      */             {
/* 1040 */               return -1;
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 83:
/*      */           case 115:
/* 1046 */             if (Character.isWhitespace(c) != (opdata == 115))
/*      */             {
/* 1048 */               return -1;
/*      */             }
/*      */             break;
/*      */           }
/* 1052 */           idx++;
/* 1053 */           break;
/*      */         default:
/* 1056 */           internalError("Unrecognized escape '" + opdata + "'");
/*      */         }
/* 1058 */         break;
/*      */       case 46:
/* 1062 */         if ((this.matchFlags & 0x4) == 4)
/*      */         {
/* 1064 */           if (this.search.isEnd(idx))
/*      */           {
/* 1066 */             return -1;
/*      */           }
/*      */ 
/*      */         }
/* 1072 */         else if ((this.search.isEnd(idx)) || (isNewline(idx)))
/*      */         {
/* 1074 */           return -1;
/*      */         }
/*      */ 
/* 1077 */         idx++;
/* 1078 */         break;
/*      */       case 65:
/* 1083 */         if (this.search.isEnd(idx))
/*      */         {
/* 1085 */           return -1;
/*      */         }
/*      */ 
/* 1089 */         int lenAtom = opdata;
/* 1090 */         int startAtom = node + 3;
/*      */ 
/* 1093 */         if (this.search.isEnd(lenAtom + idx - 1))
/*      */         {
/* 1095 */           return -1;
/*      */         }
/*      */ 
/* 1099 */         boolean caseFold = (this.matchFlags & 0x1) != 0;
/*      */ 
/* 1102 */         for (int i = 0; i < lenAtom; i++)
/*      */         {
/* 1104 */           if (compareChars(this.search.charAt(idx++), instruction[(startAtom + i)], caseFold) != 0)
/*      */           {
/* 1106 */             return -1;
/*      */           }
/*      */         }
/*      */ 
/* 1110 */         break;
/*      */       case 80:
/* 1115 */         if (this.search.isEnd(idx))
/*      */         {
/* 1117 */           return -1;
/*      */         }
/*      */ 
/* 1120 */         switch (opdata)
/*      */         {
/*      */         case 119:
/* 1123 */           if (!Character.isLetterOrDigit(this.search.charAt(idx)))
/*      */           {
/* 1125 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 97:
/* 1130 */           if (!Character.isLetter(this.search.charAt(idx)))
/*      */           {
/* 1132 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 100:
/* 1137 */           if (!Character.isDigit(this.search.charAt(idx)))
/*      */           {
/* 1139 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 98:
/* 1144 */           if (!Character.isSpaceChar(this.search.charAt(idx)))
/*      */           {
/* 1146 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 115:
/* 1151 */           if (!Character.isWhitespace(this.search.charAt(idx)))
/*      */           {
/* 1153 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 99:
/* 1158 */           if (Character.getType(this.search.charAt(idx)) != 15)
/*      */           {
/* 1160 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 103:
/* 1165 */           switch (Character.getType(this.search.charAt(idx)))
/*      */           {
/*      */           case 25:
/*      */           case 26:
/*      */           case 27:
/*      */           case 28:
/* 1171 */             break;
/*      */           default:
/* 1174 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 108:
/* 1179 */           if (Character.getType(this.search.charAt(idx)) != 2)
/*      */           {
/* 1181 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 117:
/* 1186 */           if (Character.getType(this.search.charAt(idx)) != 1)
/*      */           {
/* 1188 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 112:
/* 1193 */           if (Character.getType(this.search.charAt(idx)) == 15)
/*      */           {
/* 1195 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 33:
/* 1201 */           int type = Character.getType(this.search.charAt(idx));
/* 1202 */           switch (type)
/*      */           {
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/* 1209 */             break;
/*      */           default:
/* 1212 */             return -1;
/*      */           }
/*      */ 
/* 1215 */           break;
/*      */         case 120:
/* 1219 */           boolean isXDigit = ((this.search.charAt(idx) >= '0') && (this.search.charAt(idx) <= '9')) || ((this.search.charAt(idx) >= 'a') && (this.search.charAt(idx) <= 'f')) || ((this.search.charAt(idx) >= 'A') && (this.search.charAt(idx) <= 'F'));
/*      */ 
/* 1222 */           if (!isXDigit)
/*      */           {
/* 1224 */             return -1;
/*      */           }
/*      */ 
/* 1227 */           break;
/*      */         case 106:
/* 1230 */           if (!Character.isJavaIdentifierStart(this.search.charAt(idx)))
/*      */           {
/* 1232 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 107:
/* 1237 */           if (!Character.isJavaIdentifierPart(this.search.charAt(idx)))
/*      */           {
/* 1239 */             return -1;
/*      */           }
/*      */ 
/*      */           break;
/*      */         default:
/* 1244 */           internalError("Bad posix class");
/*      */         }
/*      */ 
/* 1249 */         idx++;
/*      */ 
/* 1251 */         break;
/*      */       case 91:
/* 1256 */         if (this.search.isEnd(idx))
/*      */         {
/* 1258 */           return -1;
/*      */         }
/*      */ 
/* 1262 */         char c = this.search.charAt(idx);
/* 1263 */         boolean caseFold = (this.matchFlags & 0x1) != 0;
/*      */ 
/* 1265 */         int idxRange = node + 3;
/* 1266 */         int idxEnd = idxRange + opdata * 2;
/* 1267 */         boolean match = false;
/* 1268 */         for (int i = idxRange; (!match) && (i < idxEnd); )
/*      */         {
/* 1271 */           char s = instruction[(i++)];
/* 1272 */           char e = instruction[(i++)];
/*      */ 
/* 1274 */           match = (compareChars(c, s, caseFold) >= 0) && (compareChars(c, e, caseFold) <= 0);
/*      */         }
/*      */ 
/* 1279 */         if (!match)
/*      */         {
/* 1281 */           return -1;
/*      */         }
/* 1283 */         idx++;
/*      */ 
/* 1285 */         break;
/*      */       case 124:
/* 1290 */         if (instruction[(next + 0)] != '|')
/*      */         {
/* 1293 */           node += 3;
/*      */         }
/*      */         else
/*      */         {
/*      */           short nextBranch;
/*      */           do
/*      */           {
/* 1302 */             if ((idxNew = matchNodes(node + 3, 65536, idx)) != -1)
/*      */             {
/* 1304 */               return idxNew;
/*      */             }
/*      */ 
/* 1308 */             nextBranch = (short)instruction[(node + 2)];
/* 1309 */             node += nextBranch;
/*      */           }
/* 1311 */           while ((nextBranch != 0) && (instruction[(node + 0)] == '|'));
/*      */ 
/* 1314 */           return -1;
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 71:
/*      */       case 78:
/* 1321 */         break;
/*      */       case 69:
/* 1326 */         setParenEnd(0, idx);
/* 1327 */         return idx;
/*      */       default:
/* 1332 */         internalError("Invalid opcode '" + opcode + "'");
/*      */ 
/* 1336 */         node = next;
/*      */       }
/*      */     }
/*      */ 
/* 1340 */     internalError("Corrupt program");
/* 1341 */     return -1;
/*      */   }
/*      */ 
/*      */   protected boolean matchAt(int i)
/*      */   {
/* 1355 */     this.start0 = -1;
/* 1356 */     this.end0 = -1;
/* 1357 */     this.start1 = -1;
/* 1358 */     this.end1 = -1;
/* 1359 */     this.start2 = -1;
/* 1360 */     this.end2 = -1;
/* 1361 */     this.startn = null;
/* 1362 */     this.endn = null;
/* 1363 */     this.parenCount = 1;
/* 1364 */     setParenStart(0, i);
/*      */ 
/* 1367 */     if ((this.program.flags & 0x1) != 0)
/*      */     {
/* 1369 */       this.startBackref = new int[this.maxParen];
/* 1370 */       this.endBackref = new int[this.maxParen];
/*      */     }
/*      */     int idx;
/* 1375 */     if ((idx = matchNodes(0, 65536, i)) != -1)
/*      */     {
/* 1377 */       setParenEnd(0, idx);
/* 1378 */       return true;
/*      */     }
/*      */ 
/* 1382 */     this.parenCount = 0;
/* 1383 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean match(String search, int i)
/*      */   {
/* 1396 */     return match(new StringCharacterIterator(search), i);
/*      */   }
/*      */ 
/*      */   public boolean match(CharacterIterator search, int i)
/*      */   {
/* 1410 */     if (this.program == null)
/*      */     {
/* 1414 */       internalError("No RE program to run!");
/*      */     }
/*      */ 
/* 1418 */     this.search = search;
/*      */ 
/* 1421 */     if (this.program.prefix == null)
/*      */     {
/* 1424 */       for (; !search.isEnd(i - 1); i++)
/*      */       {
/* 1427 */         if (matchAt(i))
/*      */         {
/* 1429 */           return true;
/*      */         }
/*      */       }
/* 1432 */       return false;
/*      */     }
/*      */ 
/* 1437 */     boolean caseIndependent = (this.matchFlags & 0x1) != 0;
/* 1438 */     char[] prefix = this.program.prefix;
/* 1439 */     for (; !search.isEnd(i + prefix.length - 1); i++)
/*      */     {
/* 1441 */       int j = i;
/* 1442 */       int k = 0;
/*      */       boolean match;
/*      */       do
/* 1447 */         match = compareChars(search.charAt(j++), prefix[(k++)], caseIndependent) == 0;
/* 1448 */       while ((match) && (k < prefix.length));
/*      */ 
/* 1451 */       if (k == prefix.length)
/*      */       {
/* 1454 */         if (matchAt(i))
/*      */         {
/* 1456 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1460 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean match(String search)
/*      */   {
/* 1472 */     return match(search, 0);
/*      */   }
/*      */ 
/*      */   public String[] split(String s)
/*      */   {
/* 1492 */     Vector v = new Vector();
/*      */ 
/* 1495 */     int pos = 0;
/* 1496 */     int len = s.length();
/*      */ 
/* 1499 */     while ((pos < len) && (match(s, pos)))
/*      */     {
/* 1502 */       int start = getParenStart(0);
/*      */ 
/* 1505 */       int newpos = getParenEnd(0);
/*      */ 
/* 1508 */       if (newpos == pos)
/*      */       {
/* 1510 */         v.addElement(s.substring(pos, start + 1));
/* 1511 */         newpos++;
/*      */       }
/*      */       else
/*      */       {
/* 1515 */         v.addElement(s.substring(pos, start));
/*      */       }
/*      */ 
/* 1519 */       pos = newpos;
/*      */     }
/*      */ 
/* 1523 */     String remainder = s.substring(pos);
/* 1524 */     if (remainder.length() != 0)
/*      */     {
/* 1526 */       v.addElement(remainder);
/*      */     }
/*      */ 
/* 1530 */     String[] ret = new String[v.size()];
/* 1531 */     v.copyInto(ret);
/* 1532 */     return ret;
/*      */   }
/*      */ 
/*      */   public String subst(String substituteIn, String substitution)
/*      */   {
/* 1568 */     return subst(substituteIn, substitution, 0);
/*      */   }
/*      */ 
/*      */   public String subst(String substituteIn, String substitution, int flags)
/*      */   {
/* 1602 */     StringBuffer ret = new StringBuffer();
/*      */ 
/* 1605 */     int pos = 0;
/* 1606 */     int len = substituteIn.length();
/*      */ 
/* 1609 */     while ((pos < len) && (match(substituteIn, pos)))
/*      */     {
/* 1612 */       ret.append(substituteIn.substring(pos, getParenStart(0)));
/*      */ 
/* 1614 */       if ((flags & 0x2) != 0)
/*      */       {
/* 1617 */         int lCurrentPosition = 0;
/* 1618 */         int lLastPosition = -2;
/* 1619 */         int lLength = substitution.length();
/* 1620 */         boolean bAddedPrefix = false;
/*      */ 
/* 1622 */         while ((lCurrentPosition = substitution.indexOf("$", lCurrentPosition)) >= 0)
/*      */         {
/* 1624 */           if (((lCurrentPosition == 0) || (substitution.charAt(lCurrentPosition - 1) != '\\')) && (lCurrentPosition + 1 < lLength))
/*      */           {
/* 1627 */             char c = substitution.charAt(lCurrentPosition + 1);
/* 1628 */             if ((c >= '0') && (c <= '9'))
/*      */             {
/* 1630 */               if (!bAddedPrefix)
/*      */               {
/* 1634 */                 ret.append(substitution.substring(0, lCurrentPosition));
/* 1635 */                 bAddedPrefix = true;
/*      */               }
/*      */               else
/*      */               {
/* 1640 */                 ret.append(substitution.substring(lLastPosition + 2, lCurrentPosition));
/*      */               }
/*      */ 
/* 1646 */               ret.append(getParen(c - '0'));
/* 1647 */               lLastPosition = lCurrentPosition;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1652 */           lCurrentPosition++;
/*      */         }
/*      */ 
/* 1656 */         ret.append(substitution.substring(lLastPosition + 2, lLength));
/*      */       }
/*      */       else
/*      */       {
/* 1661 */         ret.append(substitution);
/*      */       }
/*      */ 
/* 1665 */       int newpos = getParenEnd(0);
/*      */ 
/* 1668 */       if (newpos == pos)
/*      */       {
/* 1670 */         newpos++;
/*      */       }
/*      */ 
/* 1674 */       pos = newpos;
/*      */ 
/* 1677 */       if ((flags & 0x1) != 0)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1684 */     if (pos < len)
/*      */     {
/* 1686 */       ret.append(substituteIn.substring(pos));
/*      */     }
/*      */ 
/* 1690 */     return ret.toString();
/*      */   }
/*      */ 
/*      */   public String[] grep(Object[] search)
/*      */   {
/* 1705 */     Vector v = new Vector();
/*      */ 
/* 1708 */     for (int i = 0; i < search.length; i++)
/*      */     {
/* 1711 */       String s = search[i].toString();
/*      */ 
/* 1714 */       if (match(s))
/*      */       {
/* 1716 */         v.addElement(s);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1721 */     String[] ret = new String[v.size()];
/* 1722 */     v.copyInto(ret);
/* 1723 */     return ret;
/*      */   }
/*      */ 
/*      */   private boolean isNewline(int i)
/*      */   {
/* 1731 */     char nextChar = this.search.charAt(i);
/*      */ 
/* 1733 */     if ((nextChar == '\n') || (nextChar == '\r') || (nextChar == '') || (nextChar == ' ') || (nextChar == ' '))
/*      */     {
/* 1736 */       return true;
/*      */     }
/*      */ 
/* 1739 */     return false;
/*      */   }
/*      */ 
/*      */   private int compareChars(char c1, char c2, boolean caseIndependent)
/*      */   {
/* 1753 */     if (caseIndependent)
/*      */     {
/* 1755 */       c1 = Character.toLowerCase(c1);
/* 1756 */       c2 = Character.toLowerCase(c2);
/*      */     }
/* 1758 */     return c1 - c2;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.RE
 * JD-Core Version:    0.6.2
 */