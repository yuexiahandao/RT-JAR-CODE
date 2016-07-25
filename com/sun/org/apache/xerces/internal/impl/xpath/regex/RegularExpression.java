/*      */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.IntStack;
/*      */ import java.io.Serializable;
/*      */ import java.text.CharacterIterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Stack;
/*      */ 
/*      */ public class RegularExpression
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 6242499334195006401L;
/*      */   static final boolean DEBUG = false;
/*      */   String regex;
/*      */   int options;
/*      */   int nofparen;
/*      */   Token tokentree;
/* 1738 */   boolean hasBackReferences = false;
/*      */   transient int minlength;
/* 1741 */   transient Op operations = null;
/*      */   transient int numberOfClosures;
/* 1743 */   transient Context context = null;
/* 1744 */   transient RangeToken firstChar = null;
/*      */ 
/* 1746 */   transient String fixedString = null;
/*      */   transient int fixedStringOptions;
/* 1748 */   transient BMPattern fixedStringTable = null;
/* 1749 */   transient boolean fixedStringOnly = false;
/*      */   static final int IGNORE_CASE = 2;
/*      */   static final int SINGLE_LINE = 4;
/*      */   static final int MULTIPLE_LINES = 8;
/*      */   static final int EXTENDED_COMMENT = 16;
/*      */   static final int USE_UNICODE_CATEGORY = 32;
/*      */   static final int UNICODE_WORD_BOUNDARY = 64;
/*      */   static final int PROHIBIT_HEAD_CHARACTER_OPTIMIZATION = 128;
/*      */   static final int PROHIBIT_FIXED_STRING_OPTIMIZATION = 256;
/*      */   static final int XMLSCHEMA_MODE = 512;
/*      */   static final int SPECIAL_COMMA = 1024;
/*      */   private static final int WT_IGNORE = 0;
/*      */   private static final int WT_LETTER = 1;
/*      */   private static final int WT_OTHER = 2;
/*      */   static final int LINE_FEED = 10;
/*      */   static final int CARRIAGE_RETURN = 13;
/*      */   static final int LINE_SEPARATOR = 8232;
/*      */   static final int PARAGRAPH_SEPARATOR = 8233;
/*      */ 
/*      */   private synchronized void compile(Token tok)
/*      */   {
/*  501 */     if (this.operations != null)
/*  502 */       return;
/*  503 */     this.numberOfClosures = 0;
/*  504 */     this.operations = compile(tok, null, false);
/*      */   }
/*      */ 
/*      */   private Op compile(Token tok, Op next, boolean reverse)
/*      */   {
/*      */     Op ret;
/*      */     Op ret;
/*      */     Op ret;
/*  512 */     switch (tok.type) {
/*      */     case 11:
/*  514 */       ret = Op.createDot();
/*  515 */       ret.next = next;
/*  516 */       break;
/*      */     case 0:
/*  519 */       ret = Op.createChar(tok.getChar());
/*  520 */       ret.next = next;
/*  521 */       break;
/*      */     case 8:
/*  524 */       ret = Op.createAnchor(tok.getChar());
/*  525 */       ret.next = next;
/*  526 */       break;
/*      */     case 4:
/*      */     case 5:
/*  530 */       ret = Op.createRange(tok);
/*  531 */       ret.next = next;
/*  532 */       break;
/*      */     case 1:
/*  535 */       ret = next;
/*  536 */       if (!reverse) {
/*  537 */         for (int i = tok.size() - 1; i >= 0; i--)
/*  538 */           ret = compile(tok.getChild(i), ret, false);
/*      */       }
/*      */       else {
/*  541 */         for (int i = 0; i < tok.size(); i++) {
/*  542 */           ret = compile(tok.getChild(i), ret, true);
/*      */         }
/*      */       }
/*  545 */       break;
/*      */     case 2:
/*  548 */       Op.UnionOp uni = Op.createUnion(tok.size());
/*  549 */       for (int i = 0; i < tok.size(); i++) {
/*  550 */         uni.addElement(compile(tok.getChild(i), next, reverse));
/*      */       }
/*  552 */       ret = uni;
/*  553 */       break;
/*      */     case 3:
/*      */     case 9:
/*  557 */       Token child = tok.getChild(0);
/*  558 */       int min = tok.getMin();
/*  559 */       int max = tok.getMax();
/*  560 */       if ((min >= 0) && (min == max)) {
/*  561 */         ret = next;
/*  562 */         for (int i = 0; i < min; i++)
/*  563 */           ret = compile(child, ret, reverse);
/*      */       }
/*      */       else
/*      */       {
/*  567 */         if ((min > 0) && (max > 0))
/*  568 */           max -= min;
/*  569 */         if (max > 0)
/*      */         {
/*  571 */           Op ret = next;
/*  572 */           for (int i = 0; i < max; i++) {
/*  573 */             Op.ChildOp q = Op.createQuestion(tok.type == 9);
/*  574 */             q.next = next;
/*  575 */             q.setChild(compile(child, ret, reverse));
/*  576 */             ret = q;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           Op.ChildOp op;
/*      */           Op.ChildOp op;
/*  580 */           if (tok.type == 9)
/*  581 */             op = Op.createNonGreedyClosure();
/*      */           else {
/*  583 */             op = Op.createClosure(this.numberOfClosures++);
/*      */           }
/*  585 */           op.next = next;
/*  586 */           op.setChild(compile(child, op, reverse));
/*  587 */           ret = op;
/*      */         }
/*  589 */         if (min > 0)
/*  590 */           for (int i = 0; i < min; i++)
/*  591 */             ret = compile(child, ret, reverse);
/*      */       }
/*  590 */       break;
/*      */     case 7:
/*  597 */       ret = next;
/*  598 */       break;
/*      */     case 10:
/*  601 */       ret = Op.createString(tok.getString());
/*  602 */       ret.next = next;
/*  603 */       break;
/*      */     case 12:
/*  606 */       ret = Op.createBackReference(tok.getReferenceNumber());
/*  607 */       ret.next = next;
/*  608 */       break;
/*      */     case 6:
/*  611 */       if (tok.getParenNumber() == 0) {
/*  612 */         ret = compile(tok.getChild(0), next, reverse);
/*      */       }
/*      */       else
/*      */       {
/*      */         Op ret;
/*  613 */         if (reverse) {
/*  614 */           next = Op.createCapture(tok.getParenNumber(), next);
/*  615 */           next = compile(tok.getChild(0), next, reverse);
/*  616 */           ret = Op.createCapture(-tok.getParenNumber(), next);
/*      */         } else {
/*  618 */           next = Op.createCapture(-tok.getParenNumber(), next);
/*  619 */           next = compile(tok.getChild(0), next, reverse);
/*  620 */           ret = Op.createCapture(tok.getParenNumber(), next);
/*      */         }
/*      */       }
/*  622 */       break;
/*      */     case 20:
/*  625 */       ret = Op.createLook(20, next, compile(tok.getChild(0), null, false));
/*  626 */       break;
/*      */     case 21:
/*  628 */       ret = Op.createLook(21, next, compile(tok.getChild(0), null, false));
/*  629 */       break;
/*      */     case 22:
/*  631 */       ret = Op.createLook(22, next, compile(tok.getChild(0), null, true));
/*  632 */       break;
/*      */     case 23:
/*  634 */       ret = Op.createLook(23, next, compile(tok.getChild(0), null, true));
/*  635 */       break;
/*      */     case 24:
/*  638 */       ret = Op.createIndependent(next, compile(tok.getChild(0), null, reverse));
/*  639 */       break;
/*      */     case 25:
/*  642 */       ret = Op.createModifier(next, compile(tok.getChild(0), null, reverse), ((Token.ModifierToken)tok).getOptions(), ((Token.ModifierToken)tok).getOptionsMask());
/*      */ 
/*  645 */       break;
/*      */     case 26:
/*  648 */       Token.ConditionToken ctok = (Token.ConditionToken)tok;
/*  649 */       int ref = ctok.refNumber;
/*  650 */       Op condition = ctok.condition == null ? null : compile(ctok.condition, null, reverse);
/*  651 */       Op yes = compile(ctok.yes, next, reverse);
/*  652 */       Op no = ctok.no == null ? null : compile(ctok.no, next, reverse);
/*  653 */       ret = Op.createCondition(next, ref, condition, yes, no);
/*  654 */       break;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     default:
/*  657 */       throw new RuntimeException("Unknown token type: " + tok.type);
/*      */     }
/*  659 */     return ret;
/*      */   }
/*      */ 
/*      */   public boolean matches(char[] target)
/*      */   {
/*  671 */     return matches(target, 0, target.length, (Match)null);
/*      */   }
/*      */ 
/*      */   public boolean matches(char[] target, int start, int end)
/*      */   {
/*  683 */     return matches(target, start, end, (Match)null);
/*      */   }
/*      */ 
/*      */   public boolean matches(char[] target, Match match)
/*      */   {
/*  693 */     return matches(target, 0, target.length, match);
/*      */   }
/*      */ 
/*      */   public boolean matches(char[] target, int start, int end, Match match)
/*      */   {
/*  708 */     synchronized (this) {
/*  709 */       if (this.operations == null)
/*  710 */         prepare();
/*  711 */       if (this.context == null)
/*  712 */         this.context = new Context();
/*      */     }
/*  714 */     Context con = null;
/*  715 */     synchronized (this.context) {
/*  716 */       con = this.context.inuse ? new Context() : this.context;
/*  717 */       con.reset(target, start, end, this.numberOfClosures);
/*      */     }
/*  719 */     if (match != null) {
/*  720 */       match.setNumberOfGroups(this.nofparen);
/*  721 */       match.setSource(target);
/*  722 */     } else if (this.hasBackReferences) {
/*  723 */       match = new Match();
/*  724 */       match.setNumberOfGroups(this.nofparen);
/*      */     }
/*      */ 
/*  728 */     con.match = match;
/*      */ 
/*  730 */     if (isSet(this.options, 512)) {
/*  731 */       int matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */ 
/*  733 */       if (matchEnd == con.limit) {
/*  734 */         if (con.match != null) {
/*  735 */           con.match.setBeginning(0, con.start);
/*  736 */           con.match.setEnd(0, matchEnd);
/*      */         }
/*  738 */         con.setInUse(false);
/*  739 */         return true;
/*      */       }
/*  741 */       return false;
/*      */     }
/*      */ 
/*  748 */     if (this.fixedStringOnly)
/*      */     {
/*  750 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/*  751 */       if (o >= 0) {
/*  752 */         if (con.match != null) {
/*  753 */           con.match.setBeginning(0, o);
/*  754 */           con.match.setEnd(0, o + this.fixedString.length());
/*      */         }
/*  756 */         con.setInUse(false);
/*  757 */         return true;
/*      */       }
/*  759 */       con.setInUse(false);
/*  760 */       return false;
/*      */     }
/*      */ 
/*  768 */     if (this.fixedString != null) {
/*  769 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/*  770 */       if (o < 0)
/*      */       {
/*  772 */         con.setInUse(false);
/*  773 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  777 */     int limit = con.limit - this.minlength;
/*      */ 
/*  779 */     int matchEnd = -1;
/*      */     int matchStart;
/*  784 */     if ((this.operations != null) && (this.operations.type == 7) && (this.operations.getChild().type == 0))
/*      */     {
/*  786 */       if (isSet(this.options, 4)) {
/*  787 */         int matchStart = con.start;
/*  788 */         matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */       } else {
/*  790 */         boolean previousIsEOL = true;
/*  791 */         for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/*  792 */           int ch = target[matchStart];
/*  793 */           if (isEOLChar(ch)) {
/*  794 */             previousIsEOL = true;
/*      */           } else {
/*  796 */             if ((previousIsEOL) && 
/*  797 */               (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */             {
/*      */               break;
/*      */             }
/*  801 */             previousIsEOL = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  810 */     else if (this.firstChar != null)
/*      */     {
/*  812 */       RangeToken range = this.firstChar;
/*  813 */       for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/*  814 */         int ch = target[matchStart];
/*  815 */         if ((REUtil.isHighSurrogate(ch)) && (matchStart + 1 < con.limit)) {
/*  816 */           ch = REUtil.composeFromSurrogates(ch, target[(matchStart + 1)]);
/*      */         }
/*  818 */         if ((range.match(ch)) && 
/*  821 */           (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  832 */       for (matchStart = con.start; (matchStart <= limit) && 
/*  833 */         (0 > (matchEnd = match(con, this.operations, matchStart, 1, this.options))); matchStart++);
/*      */     }
/*      */ 
/*  838 */     if (matchEnd >= 0) {
/*  839 */       if (con.match != null) {
/*  840 */         con.match.setBeginning(0, matchStart);
/*  841 */         con.match.setEnd(0, matchEnd);
/*      */       }
/*  843 */       con.setInUse(false);
/*  844 */       return true;
/*      */     }
/*  846 */     con.setInUse(false);
/*  847 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean matches(String target)
/*      */   {
/*  857 */     return matches(target, 0, target.length(), (Match)null);
/*      */   }
/*      */ 
/*      */   public boolean matches(String target, int start, int end)
/*      */   {
/*  869 */     return matches(target, start, end, (Match)null);
/*      */   }
/*      */ 
/*      */   public boolean matches(String target, Match match)
/*      */   {
/*  879 */     return matches(target, 0, target.length(), match);
/*      */   }
/*      */ 
/*      */   public boolean matches(String target, int start, int end, Match match)
/*      */   {
/*  893 */     synchronized (this) {
/*  894 */       if (this.operations == null)
/*  895 */         prepare();
/*  896 */       if (this.context == null)
/*  897 */         this.context = new Context();
/*      */     }
/*  899 */     Context con = null;
/*  900 */     synchronized (this.context) {
/*  901 */       con = this.context.inuse ? new Context() : this.context;
/*  902 */       con.reset(target, start, end, this.numberOfClosures);
/*      */     }
/*  904 */     if (match != null) {
/*  905 */       match.setNumberOfGroups(this.nofparen);
/*  906 */       match.setSource(target);
/*  907 */     } else if (this.hasBackReferences) {
/*  908 */       match = new Match();
/*  909 */       match.setNumberOfGroups(this.nofparen);
/*      */     }
/*      */ 
/*  913 */     con.match = match;
/*      */ 
/*  915 */     if (isSet(this.options, 512))
/*      */     {
/*  919 */       int matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */ 
/*  924 */       if (matchEnd == con.limit) {
/*  925 */         if (con.match != null) {
/*  926 */           con.match.setBeginning(0, con.start);
/*  927 */           con.match.setEnd(0, matchEnd);
/*      */         }
/*  929 */         con.setInUse(false);
/*  930 */         return true;
/*      */       }
/*  932 */       return false;
/*      */     }
/*      */ 
/*  939 */     if (this.fixedStringOnly)
/*      */     {
/*  941 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/*  942 */       if (o >= 0) {
/*  943 */         if (con.match != null) {
/*  944 */           con.match.setBeginning(0, o);
/*  945 */           con.match.setEnd(0, o + this.fixedString.length());
/*      */         }
/*  947 */         con.setInUse(false);
/*  948 */         return true;
/*      */       }
/*  950 */       con.setInUse(false);
/*  951 */       return false;
/*      */     }
/*      */ 
/*  959 */     if (this.fixedString != null) {
/*  960 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/*  961 */       if (o < 0)
/*      */       {
/*  963 */         con.setInUse(false);
/*  964 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  968 */     int limit = con.limit - this.minlength;
/*      */ 
/*  970 */     int matchEnd = -1;
/*      */     int matchStart;
/*  975 */     if ((this.operations != null) && (this.operations.type == 7) && (this.operations.getChild().type == 0))
/*      */     {
/*  977 */       if (isSet(this.options, 4)) {
/*  978 */         int matchStart = con.start;
/*  979 */         matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */       } else {
/*  981 */         boolean previousIsEOL = true;
/*  982 */         for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/*  983 */           int ch = target.charAt(matchStart);
/*  984 */           if (isEOLChar(ch)) {
/*  985 */             previousIsEOL = true;
/*      */           } else {
/*  987 */             if ((previousIsEOL) && 
/*  988 */               (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */             {
/*      */               break;
/*      */             }
/*  992 */             previousIsEOL = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1001 */     else if (this.firstChar != null)
/*      */     {
/* 1003 */       RangeToken range = this.firstChar;
/* 1004 */       for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/* 1005 */         int ch = target.charAt(matchStart);
/* 1006 */         if ((REUtil.isHighSurrogate(ch)) && (matchStart + 1 < con.limit)) {
/* 1007 */           ch = REUtil.composeFromSurrogates(ch, target.charAt(matchStart + 1));
/*      */         }
/* 1009 */         if ((range.match(ch)) && 
/* 1012 */           (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1023 */       for (matchStart = con.start; (matchStart <= limit) && 
/* 1024 */         (0 > (matchEnd = match(con, this.operations, matchStart, 1, this.options))); matchStart++);
/*      */     }
/*      */ 
/* 1029 */     if (matchEnd >= 0) {
/* 1030 */       if (con.match != null) {
/* 1031 */         con.match.setBeginning(0, matchStart);
/* 1032 */         con.match.setEnd(0, matchEnd);
/*      */       }
/* 1034 */       con.setInUse(false);
/* 1035 */       return true;
/*      */     }
/* 1037 */     con.setInUse(false);
/* 1038 */     return false;
/*      */   }
/*      */ 
/*      */   private int match(Context con, Op op, int offset, int dx, int opts)
/*      */   {
/* 1046 */     ExpressionTarget target = con.target;
/* 1047 */     Stack opStack = new Stack();
/* 1048 */     IntStack dataStack = new IntStack();
/* 1049 */     boolean isSetIgnoreCase = isSet(opts, 2);
/* 1050 */     int retValue = -1;
/* 1051 */     boolean returned = false;
/*      */     while (true)
/*      */     {
/* 1054 */       if ((op == null) || (offset > con.limit) || (offset < con.start)) {
/* 1055 */         if (op == null) {
/* 1056 */           retValue = (isSet(opts, 512)) && (offset != con.limit) ? -1 : offset;
/*      */         }
/*      */         else {
/* 1059 */           retValue = -1;
/*      */         }
/* 1061 */         returned = true;
/*      */       }
/*      */       else {
/* 1064 */         retValue = -1;
/*      */ 
/* 1066 */         switch (op.type)
/*      */         {
/*      */         case 1:
/* 1069 */           int o1 = dx > 0 ? offset : offset - 1;
/* 1070 */           if ((o1 >= con.limit) || (o1 < 0) || (!matchChar(op.getData(), target.charAt(o1), isSetIgnoreCase))) {
/* 1071 */             returned = true;
/*      */           }
/*      */           else {
/* 1074 */             offset += dx;
/* 1075 */             op = op.next;
/*      */           }
/* 1077 */           break;
/*      */         case 0:
/* 1081 */           int o1 = dx > 0 ? offset : offset - 1;
/* 1082 */           if ((o1 >= con.limit) || (o1 < 0)) {
/* 1083 */             returned = true;
/*      */           }
/*      */           else {
/* 1086 */             if (isSet(opts, 4)) {
/* 1087 */               if ((REUtil.isHighSurrogate(target.charAt(o1))) && (o1 + dx >= 0) && (o1 + dx < con.limit))
/* 1088 */                 o1 += dx;
/*      */             }
/*      */             else
/*      */             {
/* 1092 */               int ch = target.charAt(o1);
/* 1093 */               if ((REUtil.isHighSurrogate(ch)) && (o1 + dx >= 0) && (o1 + dx < con.limit)) {
/* 1094 */                 o1 += dx;
/* 1095 */                 ch = REUtil.composeFromSurrogates(ch, target.charAt(o1));
/*      */               }
/* 1097 */               if (isEOLChar(ch)) {
/* 1098 */                 returned = true;
/* 1099 */                 break;
/*      */               }
/*      */             }
/* 1102 */             offset = dx > 0 ? o1 + 1 : o1;
/* 1103 */             op = op.next;
/*      */           }
/* 1105 */           break;
/*      */         case 3:
/*      */         case 4:
/* 1110 */           int o1 = dx > 0 ? offset : offset - 1;
/* 1111 */           if ((o1 >= con.limit) || (o1 < 0)) {
/* 1112 */             returned = true;
/*      */           }
/*      */           else {
/* 1115 */             int ch = target.charAt(offset);
/* 1116 */             if ((REUtil.isHighSurrogate(ch)) && (o1 + dx < con.limit) && (o1 + dx >= 0)) {
/* 1117 */               o1 += dx;
/* 1118 */               ch = REUtil.composeFromSurrogates(ch, target.charAt(o1));
/*      */             }
/* 1120 */             RangeToken tok = op.getToken();
/* 1121 */             if (!tok.match(ch)) {
/* 1122 */               returned = true;
/*      */             }
/*      */             else {
/* 1125 */               offset = dx > 0 ? o1 + 1 : o1;
/* 1126 */               op = op.next;
/*      */             }
/*      */           }
/* 1128 */           break;
/*      */         case 5:
/* 1132 */           if (!matchAnchor(target, op, con, offset, opts)) {
/* 1133 */             returned = true;
/*      */           }
/*      */           else {
/* 1136 */             op = op.next;
/*      */           }
/* 1138 */           break;
/*      */         case 16:
/* 1142 */           int refno = op.getData();
/* 1143 */           if ((refno <= 0) || (refno >= this.nofparen)) {
/* 1144 */             throw new RuntimeException("Internal Error: Reference number must be more than zero: " + refno);
/*      */           }
/* 1146 */           if ((con.match.getBeginning(refno) < 0) || (con.match.getEnd(refno) < 0)) {
/* 1147 */             returned = true;
/*      */           }
/*      */           else {
/* 1150 */             int o2 = con.match.getBeginning(refno);
/* 1151 */             int literallen = con.match.getEnd(refno) - o2;
/* 1152 */             if (dx > 0) {
/* 1153 */               if (!target.regionMatches(isSetIgnoreCase, offset, con.limit, o2, literallen)) {
/* 1154 */                 returned = true;
/* 1155 */                 break;
/*      */               }
/* 1157 */               offset += literallen;
/*      */             }
/*      */             else {
/* 1160 */               if (!target.regionMatches(isSetIgnoreCase, offset - literallen, con.limit, o2, literallen)) {
/* 1161 */                 returned = true;
/* 1162 */                 break;
/*      */               }
/* 1164 */               offset -= literallen;
/*      */             }
/* 1166 */             op = op.next;
/*      */           }
/* 1168 */           break;
/*      */         case 6:
/* 1172 */           String literal = op.getString();
/* 1173 */           int literallen = literal.length();
/* 1174 */           if (dx > 0) {
/* 1175 */             if (!target.regionMatches(isSetIgnoreCase, offset, con.limit, literal, literallen)) {
/* 1176 */               returned = true;
/* 1177 */               break;
/*      */             }
/* 1179 */             offset += literallen;
/*      */           }
/*      */           else {
/* 1182 */             if (!target.regionMatches(isSetIgnoreCase, offset - literallen, con.limit, literal, literallen)) {
/* 1183 */               returned = true;
/* 1184 */               break;
/*      */             }
/* 1186 */             offset -= literallen;
/*      */           }
/* 1188 */           op = op.next;
/*      */ 
/* 1190 */           break;
/*      */         case 7:
/* 1195 */           int id = op.getData();
/* 1196 */           if (con.closureContexts[id].contains(offset)) {
/* 1197 */             returned = true;
/*      */           }
/*      */           else
/*      */           {
/* 1201 */             con.closureContexts[id].addOffset(offset);
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 9:
/* 1207 */           opStack.push(op);
/* 1208 */           dataStack.push(offset);
/* 1209 */           op = op.getChild();
/*      */ 
/* 1211 */           break;
/*      */         case 8:
/*      */         case 10:
/* 1216 */           opStack.push(op);
/* 1217 */           dataStack.push(offset);
/* 1218 */           op = op.next;
/*      */ 
/* 1220 */           break;
/*      */         case 11:
/* 1223 */           if (op.size() == 0) {
/* 1224 */             returned = true;
/*      */           }
/*      */           else {
/* 1227 */             opStack.push(op);
/* 1228 */             dataStack.push(0);
/* 1229 */             dataStack.push(offset);
/* 1230 */             op = op.elementAt(0);
/*      */           }
/* 1232 */           break;
/*      */         case 15:
/* 1236 */           int refno = op.getData();
/* 1237 */           if (con.match != null) {
/* 1238 */             if (refno > 0) {
/* 1239 */               dataStack.push(con.match.getBeginning(refno));
/* 1240 */               con.match.setBeginning(refno, offset);
/*      */             }
/*      */             else {
/* 1243 */               int index = -refno;
/* 1244 */               dataStack.push(con.match.getEnd(index));
/* 1245 */               con.match.setEnd(index, offset);
/*      */             }
/* 1247 */             opStack.push(op);
/* 1248 */             dataStack.push(offset);
/*      */           }
/* 1250 */           op = op.next;
/*      */ 
/* 1252 */           break;
/*      */         case 20:
/*      */         case 21:
/*      */         case 22:
/*      */         case 23:
/* 1259 */           opStack.push(op);
/* 1260 */           dataStack.push(dx);
/* 1261 */           dataStack.push(offset);
/* 1262 */           dx = (op.type == 20) || (op.type == 21) ? 1 : -1;
/* 1263 */           op = op.getChild();
/*      */ 
/* 1265 */           break;
/*      */         case 24:
/* 1269 */           opStack.push(op);
/* 1270 */           dataStack.push(offset);
/* 1271 */           op = op.getChild();
/*      */ 
/* 1273 */           break;
/*      */         case 25:
/* 1277 */           int localopts = opts;
/* 1278 */           localopts |= op.getData();
/* 1279 */           localopts &= (op.getData2() ^ 0xFFFFFFFF);
/* 1280 */           opStack.push(op);
/* 1281 */           dataStack.push(opts);
/* 1282 */           dataStack.push(offset);
/* 1283 */           opts = localopts;
/* 1284 */           op = op.getChild();
/*      */ 
/* 1286 */           break;
/*      */         case 26:
/* 1290 */           Op.ConditionOp cop = (Op.ConditionOp)op;
/* 1291 */           if (cop.refNumber > 0) {
/* 1292 */             if (cop.refNumber >= this.nofparen) {
/* 1293 */               throw new RuntimeException("Internal Error: Reference number must be more than zero: " + cop.refNumber);
/*      */             }
/* 1295 */             if ((con.match.getBeginning(cop.refNumber) >= 0) && (con.match.getEnd(cop.refNumber) >= 0))
/*      */             {
/* 1297 */               op = cop.yes;
/*      */             }
/* 1299 */             else if (cop.no != null) {
/* 1300 */               op = cop.no;
/*      */             }
/*      */             else
/* 1303 */               op = cop.next;
/*      */           }
/*      */           else
/*      */           {
/* 1307 */             opStack.push(op);
/* 1308 */             dataStack.push(offset);
/* 1309 */             op = cop.condition;
/*      */           }
/*      */ 
/* 1312 */           break;
/*      */         case 2:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         default:
/* 1315 */           throw new RuntimeException("Unknown operation type: " + op.type);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1320 */       while (returned)
/*      */       {
/* 1322 */         if (opStack.isEmpty()) {
/* 1323 */           return retValue;
/*      */         }
/*      */ 
/* 1326 */         op = (Op)opStack.pop();
/* 1327 */         offset = dataStack.pop();
/*      */ 
/* 1329 */         switch (op.type) {
/*      */         case 7:
/*      */         case 9:
/* 1332 */           if (retValue < 0) {
/* 1333 */             op = op.next;
/* 1334 */             returned = false; } break;
/*      */         case 8:
/*      */         case 10:
/* 1340 */           if (retValue < 0) {
/* 1341 */             op = op.getChild();
/* 1342 */             returned = false; } break;
/*      */         case 11:
/* 1348 */           int unionIndex = dataStack.pop();
/*      */ 
/* 1353 */           if (retValue < 0) {
/* 1354 */             unionIndex++; if (unionIndex < op.size()) {
/* 1355 */               opStack.push(op);
/* 1356 */               dataStack.push(unionIndex);
/* 1357 */               dataStack.push(offset);
/* 1358 */               op = op.elementAt(unionIndex);
/* 1359 */               returned = false;
/*      */             }
/*      */             else {
/* 1362 */               retValue = -1;
/*      */             }
/*      */           }
/*      */ 
/* 1366 */           break;
/*      */         case 15:
/* 1369 */           int refno = op.getData();
/* 1370 */           int saved = dataStack.pop();
/* 1371 */           if (retValue < 0)
/* 1372 */             if (refno > 0) {
/* 1373 */               con.match.setBeginning(refno, saved);
/*      */             }
/*      */             else
/* 1376 */               con.match.setEnd(-refno, saved); 
/* 1376 */           break;
/*      */         case 20:
/*      */         case 22:
/* 1384 */           dx = dataStack.pop();
/* 1385 */           if (0 <= retValue) {
/* 1386 */             op = op.next;
/* 1387 */             returned = false;
/*      */           }
/* 1389 */           retValue = -1;
/*      */ 
/* 1391 */           break;
/*      */         case 21:
/*      */         case 23:
/* 1396 */           dx = dataStack.pop();
/* 1397 */           if (0 > retValue) {
/* 1398 */             op = op.next;
/* 1399 */             returned = false;
/*      */           }
/* 1401 */           retValue = -1;
/*      */ 
/* 1403 */           break;
/*      */         case 25:
/* 1406 */           opts = dataStack.pop();
/*      */         case 24:
/* 1410 */           if (retValue >= 0) {
/* 1411 */             offset = retValue;
/* 1412 */             op = op.next;
/* 1413 */             returned = false; } break;
/*      */         case 26:
/* 1419 */           Op.ConditionOp cop = (Op.ConditionOp)op;
/* 1420 */           if (0 <= retValue) {
/* 1421 */             op = cop.yes;
/*      */           }
/* 1423 */           else if (cop.no != null) {
/* 1424 */             op = cop.no;
/*      */           }
/*      */           else {
/* 1427 */             op = cop.next;
/*      */           }
/*      */ 
/* 1430 */           returned = false;
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19: }  } 
/*      */     }
/*      */   }
/*      */ 
/* 1441 */   private boolean matchChar(int ch, int other, boolean ignoreCase) { return ch == other ? true : ignoreCase ? matchIgnoreCase(ch, other) : false; }
/*      */ 
/*      */   boolean matchAnchor(ExpressionTarget target, Op op, Context con, int offset, int opts)
/*      */   {
/* 1445 */     boolean go = false;
/* 1446 */     switch (op.getData()) {
/*      */     case 94:
/* 1448 */       if (isSet(opts, 8)) {
/* 1449 */         if ((offset != con.start) && ((offset <= con.start) || (offset >= con.limit) || (!isEOLChar(target.charAt(offset - 1)))))
/*      */         {
/* 1451 */           return false;
/*      */         }
/* 1453 */       } else if (offset != con.start) {
/* 1454 */         return false;
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 64:
/* 1460 */       if ((offset != con.start) && ((offset <= con.start) || (!isEOLChar(target.charAt(offset - 1)))))
/*      */       {
/* 1462 */         return false;
/*      */       }
/*      */       break;
/*      */     case 36:
/* 1466 */       if (isSet(opts, 8)) {
/* 1467 */         if ((offset != con.limit) && ((offset >= con.limit) || (!isEOLChar(target.charAt(offset)))))
/*      */         {
/* 1469 */           return false;
/*      */         }
/* 1471 */       } else if ((offset != con.limit) && ((offset + 1 != con.limit) || (!isEOLChar(target.charAt(offset)))) && ((offset + 2 != con.limit) || (target.charAt(offset) != '\r') || (target.charAt(offset + 1) != '\n')))
/*      */       {
/* 1475 */         return false;
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 65:
/* 1480 */       if (offset != con.start) return false;
/*      */ 
/*      */       break;
/*      */     case 90:
/* 1484 */       if ((offset != con.limit) && ((offset + 1 != con.limit) || (!isEOLChar(target.charAt(offset)))) && ((offset + 2 != con.limit) || (target.charAt(offset) != '\r') || (target.charAt(offset + 1) != '\n')))
/*      */       {
/* 1488 */         return false;
/*      */       }
/*      */       break;
/*      */     case 122:
/* 1492 */       if (offset != con.limit) return false;
/*      */ 
/*      */       break;
/*      */     case 98:
/* 1496 */       if (con.length == 0) {
/* 1497 */         return false;
/*      */       }
/* 1499 */       int after = getWordType(target, con.start, con.limit, offset, opts);
/* 1500 */       if (after == 0) return false;
/* 1501 */       int before = getPreviousWordType(target, con.start, con.limit, offset, opts);
/* 1502 */       if (after == before) return false;
/*      */ 
/* 1504 */       break;
/*      */     case 66:
/* 1507 */       if (con.length == 0) {
/* 1508 */         go = true;
/*      */       } else {
/* 1510 */         int after = getWordType(target, con.start, con.limit, offset, opts);
/* 1511 */         go = (after == 0) || (after == getPreviousWordType(target, con.start, con.limit, offset, opts));
/*      */       }
/*      */ 
/* 1514 */       if (!go) return false;
/*      */ 
/*      */       break;
/*      */     case 60:
/* 1518 */       if ((con.length == 0) || (offset == con.limit)) return false;
/* 1519 */       if ((getWordType(target, con.start, con.limit, offset, opts) != 1) || (getPreviousWordType(target, con.start, con.limit, offset, opts) != 2))
/*      */       {
/* 1521 */         return false;
/*      */       }
/*      */       break;
/*      */     case 62:
/* 1525 */       if ((con.length == 0) || (offset == con.start)) return false;
/* 1526 */       if ((getWordType(target, con.start, con.limit, offset, opts) != 2) || (getPreviousWordType(target, con.start, con.limit, offset, opts) != 1))
/*      */       {
/* 1528 */         return false;
/*      */       }
/*      */       break;
/*      */     }
/* 1532 */     return true;
/*      */   }
/*      */ 
/*      */   private static final int getPreviousWordType(ExpressionTarget target, int begin, int end, int offset, int opts)
/*      */   {
/* 1537 */     int ret = getWordType(target, begin, end, --offset, opts);
/* 1538 */     while (ret == 0)
/* 1539 */       ret = getWordType(target, begin, end, --offset, opts);
/* 1540 */     return ret;
/*      */   }
/*      */ 
/*      */   private static final int getWordType(ExpressionTarget target, int begin, int end, int offset, int opts)
/*      */   {
/* 1545 */     if ((offset < begin) || (offset >= end)) return 2;
/* 1546 */     return getWordType0(target.charAt(offset), opts);
/*      */   }
/*      */ 
/*      */   public boolean matches(CharacterIterator target)
/*      */   {
/* 1556 */     return matches(target, (Match)null);
/*      */   }
/*      */ 
/*      */   public boolean matches(CharacterIterator target, Match match)
/*      */   {
/* 1567 */     int start = target.getBeginIndex();
/* 1568 */     int end = target.getEndIndex();
/*      */ 
/* 1572 */     synchronized (this) {
/* 1573 */       if (this.operations == null)
/* 1574 */         prepare();
/* 1575 */       if (this.context == null)
/* 1576 */         this.context = new Context();
/*      */     }
/* 1578 */     Context con = null;
/* 1579 */     synchronized (this.context) {
/* 1580 */       con = this.context.inuse ? new Context() : this.context;
/* 1581 */       con.reset(target, start, end, this.numberOfClosures);
/*      */     }
/* 1583 */     if (match != null) {
/* 1584 */       match.setNumberOfGroups(this.nofparen);
/* 1585 */       match.setSource(target);
/* 1586 */     } else if (this.hasBackReferences) {
/* 1587 */       match = new Match();
/* 1588 */       match.setNumberOfGroups(this.nofparen);
/*      */     }
/*      */ 
/* 1592 */     con.match = match;
/*      */ 
/* 1594 */     if (isSet(this.options, 512)) {
/* 1595 */       int matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */ 
/* 1597 */       if (matchEnd == con.limit) {
/* 1598 */         if (con.match != null) {
/* 1599 */           con.match.setBeginning(0, con.start);
/* 1600 */           con.match.setEnd(0, matchEnd);
/*      */         }
/* 1602 */         con.setInUse(false);
/* 1603 */         return true;
/*      */       }
/* 1605 */       return false;
/*      */     }
/*      */ 
/* 1612 */     if (this.fixedStringOnly)
/*      */     {
/* 1614 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/* 1615 */       if (o >= 0) {
/* 1616 */         if (con.match != null) {
/* 1617 */           con.match.setBeginning(0, o);
/* 1618 */           con.match.setEnd(0, o + this.fixedString.length());
/*      */         }
/* 1620 */         con.setInUse(false);
/* 1621 */         return true;
/*      */       }
/* 1623 */       con.setInUse(false);
/* 1624 */       return false;
/*      */     }
/*      */ 
/* 1632 */     if (this.fixedString != null) {
/* 1633 */       int o = this.fixedStringTable.matches(target, con.start, con.limit);
/* 1634 */       if (o < 0)
/*      */       {
/* 1636 */         con.setInUse(false);
/* 1637 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1641 */     int limit = con.limit - this.minlength;
/*      */ 
/* 1643 */     int matchEnd = -1;
/*      */     int matchStart;
/* 1648 */     if ((this.operations != null) && (this.operations.type == 7) && (this.operations.getChild().type == 0))
/*      */     {
/* 1650 */       if (isSet(this.options, 4)) {
/* 1651 */         int matchStart = con.start;
/* 1652 */         matchEnd = match(con, this.operations, con.start, 1, this.options);
/*      */       } else {
/* 1654 */         boolean previousIsEOL = true;
/* 1655 */         for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/* 1656 */           int ch = target.setIndex(matchStart);
/* 1657 */           if (isEOLChar(ch)) {
/* 1658 */             previousIsEOL = true;
/*      */           } else {
/* 1660 */             if ((previousIsEOL) && 
/* 1661 */               (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */             {
/*      */               break;
/*      */             }
/* 1665 */             previousIsEOL = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1674 */     else if (this.firstChar != null)
/*      */     {
/* 1676 */       RangeToken range = this.firstChar;
/* 1677 */       for (int matchStart = con.start; matchStart <= limit; matchStart++) {
/* 1678 */         int ch = target.setIndex(matchStart);
/* 1679 */         if ((REUtil.isHighSurrogate(ch)) && (matchStart + 1 < con.limit)) {
/* 1680 */           ch = REUtil.composeFromSurrogates(ch, target.setIndex(matchStart + 1));
/*      */         }
/* 1682 */         if ((range.match(ch)) && 
/* 1685 */           (0 <= (matchEnd = match(con, this.operations, matchStart, 1, this.options))))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1696 */       for (matchStart = con.start; (matchStart <= limit) && 
/* 1697 */         (0 > (matchEnd = match(con, this.operations, matchStart, 1, this.options))); matchStart++);
/*      */     }
/*      */ 
/* 1702 */     if (matchEnd >= 0) {
/* 1703 */       if (con.match != null) {
/* 1704 */         con.match.setBeginning(0, matchStart);
/* 1705 */         con.match.setEnd(0, matchEnd);
/*      */       }
/* 1707 */       con.setInUse(false);
/* 1708 */       return true;
/*      */     }
/* 1710 */     con.setInUse(false);
/* 1711 */     return false;
/*      */   }
/*      */ 
/*      */   void prepare()
/*      */   {
/* 2095 */     compile(this.tokentree);
/*      */ 
/* 2105 */     this.minlength = this.tokentree.getMinLength();
/*      */ 
/* 2107 */     this.firstChar = null;
/* 2108 */     if ((!isSet(this.options, 128)) && (!isSet(this.options, 512)))
/*      */     {
/* 2110 */       RangeToken firstChar = Token.createRange();
/* 2111 */       int fresult = this.tokentree.analyzeFirstCharacter(firstChar, this.options);
/* 2112 */       if (fresult == 1) {
/* 2113 */         firstChar.compactRanges();
/* 2114 */         this.firstChar = firstChar;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2120 */     if ((this.operations != null) && ((this.operations.type == 6) || (this.operations.type == 1)) && (this.operations.next == null))
/*      */     {
/* 2125 */       this.fixedStringOnly = true;
/* 2126 */       if (this.operations.type == 6) {
/* 2127 */         this.fixedString = this.operations.getString();
/* 2128 */       } else if (this.operations.getData() >= 65536) {
/* 2129 */         this.fixedString = REUtil.decomposeToSurrogates(this.operations.getData());
/*      */       } else {
/* 2131 */         char[] ac = new char[1];
/* 2132 */         ac[0] = ((char)this.operations.getData());
/* 2133 */         this.fixedString = new String(ac);
/*      */       }
/* 2135 */       this.fixedStringOptions = this.options;
/* 2136 */       this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
/*      */     }
/* 2138 */     else if ((!isSet(this.options, 256)) && (!isSet(this.options, 512)))
/*      */     {
/* 2140 */       Token.FixedStringContainer container = new Token.FixedStringContainer();
/* 2141 */       this.tokentree.findFixedString(container, this.options);
/* 2142 */       this.fixedString = (container.token == null ? null : container.token.getString());
/* 2143 */       this.fixedStringOptions = container.options;
/* 2144 */       if ((this.fixedString != null) && (this.fixedString.length() < 2)) {
/* 2145 */         this.fixedString = null;
/*      */       }
/* 2147 */       if (this.fixedString != null)
/* 2148 */         this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final boolean isSet(int options, int flag)
/*      */   {
/* 2234 */     return (options & flag) == flag;
/*      */   }
/*      */ 
/*      */   public RegularExpression(String regex)
/*      */     throws ParseException
/*      */   {
/* 2244 */     this(regex, null);
/*      */   }
/*      */ 
/*      */   public RegularExpression(String regex, String options)
/*      */     throws ParseException
/*      */   {
/* 2255 */     setPattern(regex, options);
/*      */   }
/*      */ 
/*      */   public RegularExpression(String regex, String options, Locale locale)
/*      */     throws ParseException
/*      */   {
/* 2266 */     setPattern(regex, options, locale);
/*      */   }
/*      */ 
/*      */   RegularExpression(String regex, Token tok, int parens, boolean hasBackReferences, int options) {
/* 2270 */     this.regex = regex;
/* 2271 */     this.tokentree = tok;
/* 2272 */     this.nofparen = parens;
/* 2273 */     this.options = options;
/* 2274 */     this.hasBackReferences = hasBackReferences;
/*      */   }
/*      */ 
/*      */   public void setPattern(String newPattern)
/*      */     throws ParseException
/*      */   {
/* 2281 */     setPattern(newPattern, Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public void setPattern(String newPattern, Locale locale) throws ParseException {
/* 2285 */     setPattern(newPattern, this.options, locale);
/*      */   }
/*      */ 
/*      */   private void setPattern(String newPattern, int options, Locale locale) throws ParseException {
/* 2289 */     this.regex = newPattern;
/* 2290 */     this.options = options;
/* 2291 */     RegexParser rp = isSet(this.options, 512) ? new ParserForXMLSchema(locale) : new RegexParser(locale);
/*      */ 
/* 2293 */     this.tokentree = rp.parse(this.regex, this.options);
/* 2294 */     this.nofparen = rp.parennumber;
/* 2295 */     this.hasBackReferences = rp.hasBackReferences;
/*      */ 
/* 2297 */     this.operations = null;
/* 2298 */     this.context = null;
/*      */   }
/*      */ 
/*      */   public void setPattern(String newPattern, String options)
/*      */     throws ParseException
/*      */   {
/* 2304 */     setPattern(newPattern, options, Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public void setPattern(String newPattern, String options, Locale locale) throws ParseException {
/* 2308 */     setPattern(newPattern, REUtil.parseOptions(options), locale);
/*      */   }
/*      */ 
/*      */   public String getPattern()
/*      */   {
/* 2315 */     return this.regex;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2322 */     return this.tokentree.toString(this.options);
/*      */   }
/*      */ 
/*      */   public String getOptions()
/*      */   {
/* 2334 */     return REUtil.createOptionString(this.options);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 2341 */     if (obj == null) return false;
/* 2342 */     if (!(obj instanceof RegularExpression))
/* 2343 */       return false;
/* 2344 */     RegularExpression r = (RegularExpression)obj;
/* 2345 */     return (this.regex.equals(r.regex)) && (this.options == r.options);
/*      */   }
/*      */ 
/*      */   boolean equals(String pattern, int options) {
/* 2349 */     return (this.regex.equals(pattern)) && (this.options == options);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2356 */     return (this.regex + "/" + getOptions()).hashCode();
/*      */   }
/*      */ 
/*      */   public int getNumberOfGroups()
/*      */   {
/* 2365 */     return this.nofparen;
/*      */   }
/*      */ 
/*      */   private static final int getWordType0(char ch, int opts)
/*      */   {
/* 2374 */     if (!isSet(opts, 64)) {
/* 2375 */       if (isSet(opts, 32)) {
/* 2376 */         return Token.getRange("IsWord", true).match(ch) ? 1 : 2;
/*      */       }
/* 2378 */       return isWordChar(ch) ? 1 : 2;
/*      */     }
/*      */ 
/* 2381 */     switch (Character.getType(ch)) {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/* 2391 */       return 1;
/*      */     case 6:
/*      */     case 7:
/*      */     case 16:
/* 2396 */       return 0;
/*      */     case 15:
/* 2399 */       switch (ch) {
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\013':
/*      */       case '\f':
/*      */       case '\r':
/* 2405 */         return 2;
/*      */       }
/* 2407 */       return 0;
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/* 2411 */     }return 2;
/*      */   }
/*      */ 
/*      */   private static final boolean isEOLChar(int ch)
/*      */   {
/* 2423 */     return (ch == 10) || (ch == 13) || (ch == 8232) || (ch == 8233);
/*      */   }
/*      */ 
/*      */   private static final boolean isWordChar(int ch)
/*      */   {
/* 2428 */     if (ch == 95) return true;
/* 2429 */     if (ch < 48) return false;
/* 2430 */     if (ch > 122) return false;
/* 2431 */     if (ch <= 57) return true;
/* 2432 */     if (ch < 65) return false;
/* 2433 */     if (ch <= 90) return true;
/* 2434 */     if (ch < 97) return false;
/* 2435 */     return true;
/*      */   }
/*      */ 
/*      */   private static final boolean matchIgnoreCase(int chardata, int ch) {
/* 2439 */     if (chardata == ch) return true;
/* 2440 */     if ((chardata > 65535) || (ch > 65535)) return false;
/* 2441 */     char uch1 = Character.toUpperCase((char)chardata);
/* 2442 */     char uch2 = Character.toUpperCase((char)ch);
/* 2443 */     if (uch1 == uch2) return true;
/* 2444 */     return Character.toLowerCase(uch1) == Character.toLowerCase(uch2);
/*      */   }
/*      */ 
/*      */   static final class CharArrayTarget extends RegularExpression.ExpressionTarget
/*      */   {
/*      */     char[] target;
/*      */ 
/*      */     CharArrayTarget(char[] target)
/*      */     {
/* 1796 */       this.target = target;
/*      */     }
/*      */ 
/*      */     final void resetTarget(char[] target) {
/* 1800 */       this.target = target;
/*      */     }
/*      */ 
/*      */     char charAt(int index) {
/* 1804 */       return this.target[index];
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen)
/*      */     {
/* 1809 */       if ((offset < 0) || (limit - offset < partlen)) {
/* 1810 */         return false;
/*      */       }
/* 1812 */       return ignoreCase ? regionMatchesIgnoreCase(offset, limit, part, partlen) : regionMatches(offset, limit, part, partlen);
/*      */     }
/*      */ 
/*      */     private final boolean regionMatches(int offset, int limit, String part, int partlen)
/*      */     {
/* 1817 */       int i = 0;
/* 1818 */       while (partlen-- > 0) {
/* 1819 */         if (this.target[(offset++)] != part.charAt(i++)) {
/* 1820 */           return false;
/*      */         }
/*      */       }
/* 1823 */       return true;
/*      */     }
/*      */ 
/*      */     private final boolean regionMatchesIgnoreCase(int offset, int limit, String part, int partlen) {
/* 1827 */       int i = 0;
/* 1828 */       while (partlen-- > 0) {
/* 1829 */         char ch1 = this.target[(offset++)];
/* 1830 */         char ch2 = part.charAt(i++);
/* 1831 */         if (ch1 != ch2)
/*      */         {
/* 1834 */           char uch1 = Character.toUpperCase(ch1);
/* 1835 */           char uch2 = Character.toUpperCase(ch2);
/* 1836 */           if (uch1 != uch2)
/*      */           {
/* 1839 */             if (Character.toLowerCase(uch1) != Character.toLowerCase(uch2))
/* 1840 */               return false; 
/*      */           }
/*      */         }
/*      */       }
/* 1843 */       return true;
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen) {
/* 1847 */       if ((offset < 0) || (limit - offset < partlen)) {
/* 1848 */         return false;
/*      */       }
/* 1850 */       return ignoreCase ? regionMatchesIgnoreCase(offset, limit, offset2, partlen) : regionMatches(offset, limit, offset2, partlen);
/*      */     }
/*      */ 
/*      */     private final boolean regionMatches(int offset, int limit, int offset2, int partlen)
/*      */     {
/* 1855 */       int i = offset2;
/* 1856 */       while (partlen-- > 0) {
/* 1857 */         if (this.target[(offset++)] != this.target[(i++)])
/* 1858 */           return false;
/*      */       }
/* 1860 */       return true;
/*      */     }
/*      */ 
/*      */     private final boolean regionMatchesIgnoreCase(int offset, int limit, int offset2, int partlen) {
/* 1864 */       int i = offset2;
/* 1865 */       while (partlen-- > 0) {
/* 1866 */         char ch1 = this.target[(offset++)];
/* 1867 */         char ch2 = this.target[(i++)];
/* 1868 */         if (ch1 != ch2)
/*      */         {
/* 1871 */           char uch1 = Character.toUpperCase(ch1);
/* 1872 */           char uch2 = Character.toUpperCase(ch2);
/* 1873 */           if (uch1 != uch2)
/*      */           {
/* 1876 */             if (Character.toLowerCase(uch1) != Character.toLowerCase(uch2))
/* 1877 */               return false; 
/*      */           }
/*      */         }
/*      */       }
/* 1880 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class CharacterIteratorTarget extends RegularExpression.ExpressionTarget {
/*      */     CharacterIterator target;
/*      */ 
/*      */     CharacterIteratorTarget(CharacterIterator target) {
/* 1888 */       this.target = target;
/*      */     }
/*      */ 
/*      */     final void resetTarget(CharacterIterator target) {
/* 1892 */       this.target = target;
/*      */     }
/*      */ 
/*      */     final char charAt(int index) {
/* 1896 */       return this.target.setIndex(index);
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen)
/*      */     {
/* 1901 */       if ((offset < 0) || (limit - offset < partlen)) {
/* 1902 */         return false;
/*      */       }
/* 1904 */       return ignoreCase ? regionMatchesIgnoreCase(offset, limit, part, partlen) : regionMatches(offset, limit, part, partlen);
/*      */     }
/*      */ 
/*      */     private final boolean regionMatches(int offset, int limit, String part, int partlen)
/*      */     {
/* 1909 */       int i = 0;
/* 1910 */       while (partlen-- > 0) {
/* 1911 */         if (this.target.setIndex(offset++) != part.charAt(i++)) {
/* 1912 */           return false;
/*      */         }
/*      */       }
/* 1915 */       return true;
/*      */     }
/*      */ 
/*      */     private final boolean regionMatchesIgnoreCase(int offset, int limit, String part, int partlen) {
/* 1919 */       int i = 0;
/* 1920 */       while (partlen-- > 0) {
/* 1921 */         char ch1 = this.target.setIndex(offset++);
/* 1922 */         char ch2 = part.charAt(i++);
/* 1923 */         if (ch1 != ch2)
/*      */         {
/* 1926 */           char uch1 = Character.toUpperCase(ch1);
/* 1927 */           char uch2 = Character.toUpperCase(ch2);
/* 1928 */           if (uch1 != uch2)
/*      */           {
/* 1931 */             if (Character.toLowerCase(uch1) != Character.toLowerCase(uch2))
/* 1932 */               return false; 
/*      */           }
/*      */         }
/*      */       }
/* 1935 */       return true;
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen) {
/* 1939 */       if ((offset < 0) || (limit - offset < partlen)) {
/* 1940 */         return false;
/*      */       }
/* 1942 */       return ignoreCase ? regionMatchesIgnoreCase(offset, limit, offset2, partlen) : regionMatches(offset, limit, offset2, partlen);
/*      */     }
/*      */ 
/*      */     private final boolean regionMatches(int offset, int limit, int offset2, int partlen)
/*      */     {
/* 1947 */       int i = offset2;
/* 1948 */       while (partlen-- > 0) {
/* 1949 */         if (this.target.setIndex(offset++) != this.target.setIndex(i++)) {
/* 1950 */           return false;
/*      */         }
/*      */       }
/* 1953 */       return true;
/*      */     }
/*      */ 
/*      */     private final boolean regionMatchesIgnoreCase(int offset, int limit, int offset2, int partlen) {
/* 1957 */       int i = offset2;
/* 1958 */       while (partlen-- > 0) {
/* 1959 */         char ch1 = this.target.setIndex(offset++);
/* 1960 */         char ch2 = this.target.setIndex(i++);
/* 1961 */         if (ch1 != ch2)
/*      */         {
/* 1964 */           char uch1 = Character.toUpperCase(ch1);
/* 1965 */           char uch2 = Character.toUpperCase(ch2);
/* 1966 */           if (uch1 != uch2)
/*      */           {
/* 1969 */             if (Character.toLowerCase(uch1) != Character.toLowerCase(uch2))
/* 1970 */               return false; 
/*      */           }
/*      */         }
/*      */       }
/* 1973 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class ClosureContext
/*      */   {
/* 1979 */     int[] offsets = new int[4];
/* 1980 */     int currentIndex = 0;
/*      */ 
/*      */     boolean contains(int offset) {
/* 1983 */       for (int i = 0; i < this.currentIndex; i++) {
/* 1984 */         if (this.offsets[i] == offset) {
/* 1985 */           return true;
/*      */         }
/*      */       }
/* 1988 */       return false;
/*      */     }
/*      */ 
/*      */     void reset() {
/* 1992 */       this.currentIndex = 0;
/*      */     }
/*      */ 
/*      */     void addOffset(int offset)
/*      */     {
/* 1997 */       if (this.currentIndex == this.offsets.length) {
/* 1998 */         this.offsets = expandOffsets();
/*      */       }
/* 2000 */       this.offsets[(this.currentIndex++)] = offset;
/*      */     }
/*      */ 
/*      */     private int[] expandOffsets() {
/* 2004 */       int len = this.offsets.length;
/* 2005 */       int newLen = len << 1;
/* 2006 */       int[] newOffsets = new int[newLen];
/*      */ 
/* 2008 */       System.arraycopy(this.offsets, 0, newOffsets, 0, this.currentIndex);
/* 2009 */       return newOffsets; } 
/*      */   }
/*      */   static final class Context { int start;
/*      */     int limit;
/*      */     int length;
/*      */     Match match;
/* 2018 */     boolean inuse = false;
/*      */     RegularExpression.ClosureContext[] closureContexts;
/*      */     private RegularExpression.StringTarget stringTarget;
/*      */     private RegularExpression.CharArrayTarget charArrayTarget;
/*      */     private RegularExpression.CharacterIteratorTarget characterIteratorTarget;
/*      */     RegularExpression.ExpressionTarget target;
/*      */ 
/* 2031 */     private void resetCommon(int nofclosures) { this.length = (this.limit - this.start);
/* 2032 */       setInUse(true);
/* 2033 */       this.match = null;
/* 2034 */       if ((this.closureContexts == null) || (this.closureContexts.length != nofclosures)) {
/* 2035 */         this.closureContexts = new RegularExpression.ClosureContext[nofclosures];
/*      */       }
/* 2037 */       for (int i = 0; i < nofclosures; i++)
/* 2038 */         if (this.closureContexts[i] == null) {
/* 2039 */           this.closureContexts[i] = new RegularExpression.ClosureContext();
/*      */         }
/*      */         else
/* 2042 */           this.closureContexts[i].reset();
/*      */     }
/*      */ 
/*      */     void reset(CharacterIterator target, int start, int limit, int nofclosures)
/*      */     {
/* 2048 */       if (this.characterIteratorTarget == null) {
/* 2049 */         this.characterIteratorTarget = new RegularExpression.CharacterIteratorTarget(target);
/*      */       }
/*      */       else {
/* 2052 */         this.characterIteratorTarget.resetTarget(target);
/*      */       }
/* 2054 */       this.target = this.characterIteratorTarget;
/* 2055 */       this.start = start;
/* 2056 */       this.limit = limit;
/* 2057 */       resetCommon(nofclosures);
/*      */     }
/*      */ 
/*      */     void reset(String target, int start, int limit, int nofclosures) {
/* 2061 */       if (this.stringTarget == null) {
/* 2062 */         this.stringTarget = new RegularExpression.StringTarget(target);
/*      */       }
/*      */       else {
/* 2065 */         this.stringTarget.resetTarget(target);
/*      */       }
/* 2067 */       this.target = this.stringTarget;
/* 2068 */       this.start = start;
/* 2069 */       this.limit = limit;
/* 2070 */       resetCommon(nofclosures);
/*      */     }
/*      */ 
/*      */     void reset(char[] target, int start, int limit, int nofclosures) {
/* 2074 */       if (this.charArrayTarget == null) {
/* 2075 */         this.charArrayTarget = new RegularExpression.CharArrayTarget(target);
/*      */       }
/*      */       else {
/* 2078 */         this.charArrayTarget.resetTarget(target);
/*      */       }
/* 2080 */       this.target = this.charArrayTarget;
/* 2081 */       this.start = start;
/* 2082 */       this.limit = limit;
/* 2083 */       resetCommon(nofclosures);
/*      */     }
/*      */     synchronized void setInUse(boolean inUse) {
/* 2086 */       this.inuse = inUse;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class ExpressionTarget
/*      */   {
/*      */     abstract char charAt(int paramInt);
/*      */ 
/*      */     abstract boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, int paramInt3);
/*      */ 
/*      */     abstract boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */   }
/*      */ 
/*      */   static final class StringTarget extends RegularExpression.ExpressionTarget
/*      */   {
/*      */     private String target;
/*      */ 
/*      */     StringTarget(String target)
/*      */     {
/* 1762 */       this.target = target;
/*      */     }
/*      */ 
/*      */     final void resetTarget(String target) {
/* 1766 */       this.target = target;
/*      */     }
/*      */ 
/*      */     final char charAt(int index) {
/* 1770 */       return this.target.charAt(index);
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen)
/*      */     {
/* 1775 */       if (limit - offset < partlen) {
/* 1776 */         return false;
/*      */       }
/* 1778 */       return ignoreCase ? this.target.regionMatches(true, offset, part, 0, partlen) : this.target.regionMatches(offset, part, 0, partlen);
/*      */     }
/*      */ 
/*      */     final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen)
/*      */     {
/* 1783 */       if (limit - offset < partlen) {
/* 1784 */         return false;
/*      */       }
/* 1786 */       return ignoreCase ? this.target.regionMatches(true, offset, this.target, offset2, partlen) : this.target.regionMatches(offset, this.target, offset2, partlen);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression
 * JD-Core Version:    0.6.2
 */