/*      */ package sun.text.normalizer;
/*      */ 
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Iterator;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ public class UnicodeSet
/*      */   implements UnicodeMatcher
/*      */ {
/*      */   private static final int LOW = 0;
/*      */   private static final int HIGH = 1114112;
/*      */   public static final int MIN_VALUE = 0;
/*      */   public static final int MAX_VALUE = 1114111;
/*      */   private int len;
/*      */   private int[] list;
/*      */   private int[] rangeList;
/*      */   private int[] buffer;
/*  301 */   TreeSet strings = new TreeSet();
/*      */ 
/*  312 */   private String pat = null;
/*      */   private static final int START_EXTRA = 16;
/*      */   private static final int GROW_EXTRA = 16;
/*  323 */   private static UnicodeSet[] INCLUSIONS = null;
/*      */ 
/* 1580 */   static final VersionInfo NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
/*      */   public static final int IGNORE_SPACE = 1;
/*      */ 
/*      */   public UnicodeSet()
/*      */   {
/*  334 */     this.list = new int[17];
/*  335 */     this.list[(this.len++)] = 1114112;
/*      */   }
/*      */ 
/*      */   public UnicodeSet(int paramInt1, int paramInt2)
/*      */   {
/*  347 */     this();
/*  348 */     complement(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public UnicodeSet(String paramString)
/*      */   {
/*  360 */     this();
/*  361 */     applyPattern(paramString, null, null, 1);
/*      */   }
/*      */ 
/*      */   public UnicodeSet set(UnicodeSet paramUnicodeSet)
/*      */   {
/*  371 */     this.list = ((int[])paramUnicodeSet.list.clone());
/*  372 */     this.len = paramUnicodeSet.len;
/*  373 */     this.pat = paramUnicodeSet.pat;
/*  374 */     this.strings = ((TreeSet)paramUnicodeSet.strings.clone());
/*  375 */     return this;
/*      */   }
/*      */ 
/*      */   public final UnicodeSet applyPattern(String paramString)
/*      */   {
/*  388 */     return applyPattern(paramString, null, null, 1);
/*      */   }
/*      */ 
/*      */   private static void _appendToPat(StringBuffer paramStringBuffer, String paramString, boolean paramBoolean)
/*      */   {
/*  396 */     for (int i = 0; i < paramString.length(); i += UTF16.getCharCount(i))
/*  397 */       _appendToPat(paramStringBuffer, UTF16.charAt(paramString, i), paramBoolean);
/*      */   }
/*      */ 
/*      */   private static void _appendToPat(StringBuffer paramStringBuffer, int paramInt, boolean paramBoolean)
/*      */   {
/*  406 */     if ((paramBoolean) && (Utility.isUnprintable(paramInt)))
/*      */     {
/*  409 */       if (Utility.escapeUnprintable(paramStringBuffer, paramInt)) {
/*  410 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  414 */     switch (paramInt) {
/*      */     case 36:
/*      */     case 38:
/*      */     case 45:
/*      */     case 58:
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 123:
/*      */     case 125:
/*  425 */       paramStringBuffer.append('\\');
/*  426 */       break;
/*      */     default:
/*  429 */       if (UCharacterProperty.isRuleWhiteSpace(paramInt)) {
/*  430 */         paramStringBuffer.append('\\');
/*      */       }
/*      */       break;
/*      */     }
/*  434 */     UTF16.append(paramStringBuffer, paramInt);
/*      */   }
/*      */ 
/*      */   private StringBuffer _toPattern(StringBuffer paramStringBuffer, boolean paramBoolean)
/*      */   {
/*  444 */     if (this.pat != null)
/*      */     {
/*  446 */       int j = 0;
/*  447 */       for (int i = 0; i < this.pat.length(); ) {
/*  448 */         int k = UTF16.charAt(this.pat, i);
/*  449 */         i += UTF16.getCharCount(k);
/*  450 */         if ((paramBoolean) && (Utility.isUnprintable(k)))
/*      */         {
/*  455 */           if (j % 2 == 1) {
/*  456 */             paramStringBuffer.setLength(paramStringBuffer.length() - 1);
/*      */           }
/*  458 */           Utility.escapeUnprintable(paramStringBuffer, k);
/*  459 */           j = 0;
/*      */         } else {
/*  461 */           UTF16.append(paramStringBuffer, k);
/*  462 */           if (k == 92)
/*  463 */             j++;
/*      */           else {
/*  465 */             j = 0;
/*      */           }
/*      */         }
/*      */       }
/*  469 */       return paramStringBuffer;
/*      */     }
/*      */ 
/*  472 */     return _generatePattern(paramStringBuffer, paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public StringBuffer _generatePattern(StringBuffer paramStringBuffer, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  484 */     paramStringBuffer.append('[');
/*      */ 
/*  486 */     int i = getRangeCount();
/*      */     int j;
/*      */     int k;
/*      */     int m;
/*  491 */     if ((i > 1) && (getRangeStart(0) == 0) && (getRangeEnd(i - 1) == 1114111))
/*      */     {
/*  496 */       paramStringBuffer.append('^');
/*      */ 
/*  498 */       for (j = 1; j < i; j++) {
/*  499 */         k = getRangeEnd(j - 1) + 1;
/*  500 */         m = getRangeStart(j) - 1;
/*  501 */         _appendToPat(paramStringBuffer, k, paramBoolean1);
/*  502 */         if (k != m) {
/*  503 */           if (k + 1 != m) {
/*  504 */             paramStringBuffer.append('-');
/*      */           }
/*  506 */           _appendToPat(paramStringBuffer, m, paramBoolean1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  513 */       for (j = 0; j < i; j++) {
/*  514 */         k = getRangeStart(j);
/*  515 */         m = getRangeEnd(j);
/*  516 */         _appendToPat(paramStringBuffer, k, paramBoolean1);
/*  517 */         if (k != m) {
/*  518 */           if (k + 1 != m) {
/*  519 */             paramStringBuffer.append('-');
/*      */           }
/*  521 */           _appendToPat(paramStringBuffer, m, paramBoolean1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  526 */     if ((paramBoolean2) && (this.strings.size() > 0)) {
/*  527 */       Iterator localIterator = this.strings.iterator();
/*  528 */       while (localIterator.hasNext()) {
/*  529 */         paramStringBuffer.append('{');
/*  530 */         _appendToPat(paramStringBuffer, (String)localIterator.next(), paramBoolean1);
/*  531 */         paramStringBuffer.append('}');
/*      */       }
/*      */     }
/*  534 */     return paramStringBuffer.append(']');
/*      */   }
/*      */ 
/*      */   private UnicodeSet add_unchecked(int paramInt1, int paramInt2)
/*      */   {
/*  539 */     if ((paramInt1 < 0) || (paramInt1 > 1114111)) {
/*  540 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt1, 6));
/*      */     }
/*  542 */     if ((paramInt2 < 0) || (paramInt2 > 1114111)) {
/*  543 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt2, 6));
/*      */     }
/*  545 */     if (paramInt1 < paramInt2)
/*  546 */       add(range(paramInt1, paramInt2), 2, 0);
/*  547 */     else if (paramInt1 == paramInt2) {
/*  548 */       add(paramInt1);
/*      */     }
/*  550 */     return this;
/*      */   }
/*      */ 
/*      */   public final UnicodeSet add(int paramInt)
/*      */   {
/*  560 */     return add_unchecked(paramInt);
/*      */   }
/*      */ 
/*      */   private final UnicodeSet add_unchecked(int paramInt)
/*      */   {
/*  565 */     if ((paramInt < 0) || (paramInt > 1114111)) {
/*  566 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt, 6));
/*      */     }
/*      */ 
/*  572 */     int i = findCodePoint(paramInt);
/*      */ 
/*  575 */     if ((i & 0x1) != 0) return this;
/*      */ 
/*  589 */     if (paramInt == this.list[i] - 1)
/*      */     {
/*  591 */       this.list[i] = paramInt;
/*      */ 
/*  593 */       if (paramInt == 1114111) {
/*  594 */         ensureCapacity(this.len + 1);
/*  595 */         this.list[(this.len++)] = 1114112;
/*      */       }
/*  597 */       if ((i > 0) && (paramInt == this.list[(i - 1)]))
/*      */       {
/*  603 */         System.arraycopy(this.list, i + 1, this.list, i - 1, this.len - i - 1);
/*  604 */         this.len -= 2;
/*      */       }
/*      */ 
/*      */     }
/*  608 */     else if ((i > 0) && (paramInt == this.list[(i - 1)]))
/*      */     {
/*  610 */       this.list[(i - 1)] += 1;
/*      */     }
/*      */     else
/*      */     {
/*  630 */       if (this.len + 2 > this.list.length) {
/*  631 */         int[] arrayOfInt = new int[this.len + 2 + 16];
/*  632 */         if (i != 0) System.arraycopy(this.list, 0, arrayOfInt, 0, i);
/*  633 */         System.arraycopy(this.list, i, arrayOfInt, i + 2, this.len - i);
/*  634 */         this.list = arrayOfInt;
/*      */       } else {
/*  636 */         System.arraycopy(this.list, i, this.list, i + 2, this.len - i);
/*      */       }
/*      */ 
/*  639 */       this.list[i] = paramInt;
/*  640 */       this.list[(i + 1)] = (paramInt + 1);
/*  641 */       this.len += 2;
/*      */     }
/*      */ 
/*  644 */     this.pat = null;
/*  645 */     return this;
/*      */   }
/*      */ 
/*      */   public final UnicodeSet add(String paramString)
/*      */   {
/*  659 */     int i = getSingleCP(paramString);
/*  660 */     if (i < 0) {
/*  661 */       this.strings.add(paramString);
/*  662 */       this.pat = null;
/*      */     } else {
/*  664 */       add_unchecked(i, i);
/*      */     }
/*  666 */     return this;
/*      */   }
/*      */ 
/*      */   private static int getSingleCP(String paramString)
/*      */   {
/*  675 */     if (paramString.length() < 1) {
/*  676 */       throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet");
/*      */     }
/*  678 */     if (paramString.length() > 2) return -1;
/*  679 */     if (paramString.length() == 1) return paramString.charAt(0);
/*      */ 
/*  682 */     int i = UTF16.charAt(paramString, 0);
/*  683 */     if (i > 65535) {
/*  684 */       return i;
/*      */     }
/*  686 */     return -1;
/*      */   }
/*      */ 
/*      */   public UnicodeSet complement(int paramInt1, int paramInt2)
/*      */   {
/*  702 */     if ((paramInt1 < 0) || (paramInt1 > 1114111)) {
/*  703 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt1, 6));
/*      */     }
/*  705 */     if ((paramInt2 < 0) || (paramInt2 > 1114111)) {
/*  706 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt2, 6));
/*      */     }
/*  708 */     if (paramInt1 <= paramInt2) {
/*  709 */       xor(range(paramInt1, paramInt2), 2, 0);
/*      */     }
/*  711 */     this.pat = null;
/*  712 */     return this;
/*      */   }
/*      */ 
/*      */   public UnicodeSet complement()
/*      */   {
/*  721 */     if (this.list[0] == 0) {
/*  722 */       System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
/*  723 */       this.len -= 1;
/*      */     } else {
/*  725 */       ensureCapacity(this.len + 1);
/*  726 */       System.arraycopy(this.list, 0, this.list, 1, this.len);
/*  727 */       this.list[0] = 0;
/*  728 */       this.len += 1;
/*      */     }
/*  730 */     this.pat = null;
/*  731 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean contains(int paramInt)
/*      */   {
/*  741 */     if ((paramInt < 0) || (paramInt > 1114111)) {
/*  742 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(paramInt, 6));
/*      */     }
/*      */ 
/*  754 */     int i = findCodePoint(paramInt);
/*      */ 
/*  756 */     return (i & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   private final int findCodePoint(int paramInt)
/*      */   {
/*  781 */     if (paramInt < this.list[0]) return 0;
/*      */ 
/*  784 */     if ((this.len >= 2) && (paramInt >= this.list[(this.len - 2)])) return this.len - 1;
/*  785 */     int i = 0;
/*  786 */     int j = this.len - 1;
/*      */     while (true)
/*      */     {
/*  790 */       int k = i + j >>> 1;
/*  791 */       if (k == i) return j;
/*  792 */       if (paramInt < this.list[k])
/*  793 */         j = k;
/*      */       else
/*  795 */         i = k;
/*      */     }
/*      */   }
/*      */ 
/*      */   public UnicodeSet addAll(UnicodeSet paramUnicodeSet)
/*      */   {
/*  811 */     add(paramUnicodeSet.list, paramUnicodeSet.len, 0);
/*  812 */     this.strings.addAll(paramUnicodeSet.strings);
/*  813 */     return this;
/*      */   }
/*      */ 
/*      */   public UnicodeSet retainAll(UnicodeSet paramUnicodeSet)
/*      */   {
/*  827 */     retain(paramUnicodeSet.list, paramUnicodeSet.len, 0);
/*  828 */     this.strings.retainAll(paramUnicodeSet.strings);
/*  829 */     return this;
/*      */   }
/*      */ 
/*      */   public UnicodeSet removeAll(UnicodeSet paramUnicodeSet)
/*      */   {
/*  843 */     retain(paramUnicodeSet.list, paramUnicodeSet.len, 2);
/*  844 */     this.strings.removeAll(paramUnicodeSet.strings);
/*  845 */     return this;
/*      */   }
/*      */ 
/*      */   public UnicodeSet clear()
/*      */   {
/*  854 */     this.list[0] = 1114112;
/*  855 */     this.len = 1;
/*  856 */     this.pat = null;
/*  857 */     this.strings.clear();
/*  858 */     return this;
/*      */   }
/*      */ 
/*      */   public int getRangeCount()
/*      */   {
/*  869 */     return this.len / 2;
/*      */   }
/*      */ 
/*      */   public int getRangeStart(int paramInt)
/*      */   {
/*  882 */     return this.list[(paramInt * 2)];
/*      */   }
/*      */ 
/*      */   public int getRangeEnd(int paramInt)
/*      */   {
/*  895 */     return this.list[(paramInt * 2 + 1)] - 1;
/*      */   }
/*      */ 
/*      */   UnicodeSet applyPattern(String paramString, ParsePosition paramParsePosition, SymbolTable paramSymbolTable, int paramInt)
/*      */   {
/*  931 */     int i = paramParsePosition == null ? 1 : 0;
/*  932 */     if (i != 0) {
/*  933 */       paramParsePosition = new ParsePosition(0);
/*      */     }
/*      */ 
/*  936 */     StringBuffer localStringBuffer = new StringBuffer();
/*  937 */     RuleCharacterIterator localRuleCharacterIterator = new RuleCharacterIterator(paramString, paramSymbolTable, paramParsePosition);
/*      */ 
/*  939 */     applyPattern(localRuleCharacterIterator, paramSymbolTable, localStringBuffer, paramInt);
/*  940 */     if (localRuleCharacterIterator.inVariable()) {
/*  941 */       syntaxError(localRuleCharacterIterator, "Extra chars in variable value");
/*      */     }
/*  943 */     this.pat = localStringBuffer.toString();
/*  944 */     if (i != 0) {
/*  945 */       int j = paramParsePosition.getIndex();
/*      */ 
/*  948 */       if ((paramInt & 0x1) != 0) {
/*  949 */         j = Utility.skipWhitespace(paramString, j);
/*      */       }
/*      */ 
/*  952 */       if (j != paramString.length()) {
/*  953 */         throw new IllegalArgumentException("Parse of \"" + paramString + "\" failed at " + j);
/*      */       }
/*      */     }
/*      */ 
/*  957 */     return this;
/*      */   }
/*      */ 
/*      */   void applyPattern(RuleCharacterIterator paramRuleCharacterIterator, SymbolTable paramSymbolTable, StringBuffer paramStringBuffer, int paramInt)
/*      */   {
/*  980 */     int i = 3;
/*      */ 
/*  982 */     if ((paramInt & 0x1) != 0) {
/*  983 */       i |= 4;
/*      */     }
/*      */ 
/*  986 */     StringBuffer localStringBuffer1 = new StringBuffer(); StringBuffer localStringBuffer2 = null;
/*  987 */     int j = 0;
/*  988 */     UnicodeSet localUnicodeSet1 = null;
/*  989 */     Object localObject = null;
/*      */ 
/*  993 */     int k = 0; int m = 0; int n = 0;
/*  994 */     char c = '\000';
/*      */ 
/*  996 */     int i1 = 0;
/*      */ 
/*  998 */     clear();
/*      */ 
/* 1000 */     while ((n != 2) && (!paramRuleCharacterIterator.atEnd()))
/*      */     {
/* 1010 */       int i2 = 0;
/* 1011 */       boolean bool = false;
/* 1012 */       UnicodeSet localUnicodeSet2 = null;
/*      */ 
/* 1017 */       int i3 = 0;
/* 1018 */       if (resemblesPropertyPattern(paramRuleCharacterIterator, i)) {
/* 1019 */         i3 = 2;
/*      */       }
/*      */       else
/*      */       {
/* 1032 */         localObject = paramRuleCharacterIterator.getPos(localObject);
/* 1033 */         i2 = paramRuleCharacterIterator.next(i);
/* 1034 */         bool = paramRuleCharacterIterator.isEscaped();
/*      */ 
/* 1036 */         if ((i2 == 91) && (!bool)) {
/* 1037 */           if (n == 1) {
/* 1038 */             paramRuleCharacterIterator.setPos(localObject);
/* 1039 */             i3 = 1; break label289;
/*      */           }
/*      */ 
/* 1042 */           n = 1;
/* 1043 */           localStringBuffer1.append('[');
/* 1044 */           localObject = paramRuleCharacterIterator.getPos(localObject);
/* 1045 */           i2 = paramRuleCharacterIterator.next(i);
/* 1046 */           bool = paramRuleCharacterIterator.isEscaped();
/* 1047 */           if ((i2 == 94) && (!bool)) {
/* 1048 */             i1 = 1;
/* 1049 */             localStringBuffer1.append('^');
/* 1050 */             localObject = paramRuleCharacterIterator.getPos(localObject);
/* 1051 */             i2 = paramRuleCharacterIterator.next(i);
/* 1052 */             bool = paramRuleCharacterIterator.isEscaped();
/*      */           }
/*      */ 
/* 1056 */           if (i2 == 45) {
/* 1057 */             bool = true; break label289;
/*      */           }
/*      */ 
/* 1060 */           paramRuleCharacterIterator.setPos(localObject);
/* 1061 */           continue;
/*      */         }
/*      */ 
/* 1064 */         if (paramSymbolTable != null) {
/* 1065 */           UnicodeMatcher localUnicodeMatcher = paramSymbolTable.lookupMatcher(i2);
/* 1066 */           if (localUnicodeMatcher != null) {
/*      */             try {
/* 1068 */               localUnicodeSet2 = (UnicodeSet)localUnicodeMatcher;
/* 1069 */               i3 = 3;
/*      */             } catch (ClassCastException localClassCastException) {
/* 1071 */               syntaxError(paramRuleCharacterIterator, "Syntax error");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1082 */       label289: if (i3 != 0) {
/* 1083 */         if (k == 1) {
/* 1084 */           if (c != 0) {
/* 1085 */             syntaxError(paramRuleCharacterIterator, "Char expected after operator");
/*      */           }
/* 1087 */           add_unchecked(m, m);
/* 1088 */           _appendToPat(localStringBuffer1, m, false);
/* 1089 */           k = c = 0;
/*      */         }
/*      */ 
/* 1092 */         if ((c == '-') || (c == '&')) {
/* 1093 */           localStringBuffer1.append(c);
/*      */         }
/*      */ 
/* 1096 */         if (localUnicodeSet2 == null) {
/* 1097 */           if (localUnicodeSet1 == null) localUnicodeSet1 = new UnicodeSet();
/* 1098 */           localUnicodeSet2 = localUnicodeSet1;
/*      */         }
/* 1100 */         switch (i3) {
/*      */         case 1:
/* 1102 */           localUnicodeSet2.applyPattern(paramRuleCharacterIterator, paramSymbolTable, localStringBuffer1, paramInt);
/* 1103 */           break;
/*      */         case 2:
/* 1105 */           paramRuleCharacterIterator.skipIgnored(i);
/* 1106 */           localUnicodeSet2.applyPropertyPattern(paramRuleCharacterIterator, localStringBuffer1, paramSymbolTable);
/* 1107 */           break;
/*      */         case 3:
/* 1109 */           localUnicodeSet2._toPattern(localStringBuffer1, false);
/*      */         }
/*      */ 
/* 1113 */         j = 1;
/*      */ 
/* 1115 */         if (n == 0)
/*      */         {
/* 1117 */           set(localUnicodeSet2);
/* 1118 */           n = 2;
/* 1119 */           break;
/*      */         }
/*      */ 
/* 1122 */         switch (c) {
/*      */         case '-':
/* 1124 */           removeAll(localUnicodeSet2);
/* 1125 */           break;
/*      */         case '&':
/* 1127 */           retainAll(localUnicodeSet2);
/* 1128 */           break;
/*      */         case '\000':
/* 1130 */           addAll(localUnicodeSet2);
/*      */         }
/*      */ 
/* 1134 */         c = '\000';
/* 1135 */         k = 2;
/*      */       }
/*      */       else
/*      */       {
/* 1140 */         if (n == 0) {
/* 1141 */           syntaxError(paramRuleCharacterIterator, "Missing '['");
/*      */         }
/*      */ 
/* 1148 */         if (!bool) {
/* 1149 */           switch (i2) {
/*      */           case 93:
/* 1151 */             if (k == 1) {
/* 1152 */               add_unchecked(m, m);
/* 1153 */               _appendToPat(localStringBuffer1, m, false);
/*      */             }
/*      */ 
/* 1156 */             if (c == '-') {
/* 1157 */               add_unchecked(c, c);
/* 1158 */               localStringBuffer1.append(c);
/* 1159 */             } else if (c == '&') {
/* 1160 */               syntaxError(paramRuleCharacterIterator, "Trailing '&'");
/*      */             }
/* 1162 */             localStringBuffer1.append(']');
/* 1163 */             n = 2;
/* 1164 */             break;
/*      */           case 45:
/* 1166 */             if (c == 0) {
/* 1167 */               if (k != 0) {
/* 1168 */                 c = (char)i2;
/* 1169 */                 continue;
/*      */               }
/*      */ 
/* 1172 */               add_unchecked(i2, i2);
/* 1173 */               i2 = paramRuleCharacterIterator.next(i);
/* 1174 */               bool = paramRuleCharacterIterator.isEscaped();
/* 1175 */               if ((i2 == 93) && (!bool)) {
/* 1176 */                 localStringBuffer1.append("-]");
/* 1177 */                 n = 2;
/* 1178 */                 continue;
/*      */               }
/*      */             }
/*      */ 
/* 1182 */             syntaxError(paramRuleCharacterIterator, "'-' not after char or set");
/*      */           case 38:
/* 1184 */             if ((k == 2) && (c == 0)) {
/* 1185 */               c = (char)i2;
/* 1186 */               continue;
/*      */             }
/* 1188 */             syntaxError(paramRuleCharacterIterator, "'&' not after set");
/*      */           case 94:
/* 1190 */             syntaxError(paramRuleCharacterIterator, "'^' not after '['");
/*      */           case 123:
/* 1192 */             if (c != 0) {
/* 1193 */               syntaxError(paramRuleCharacterIterator, "Missing operand after operator");
/*      */             }
/* 1195 */             if (k == 1) {
/* 1196 */               add_unchecked(m, m);
/* 1197 */               _appendToPat(localStringBuffer1, m, false);
/*      */             }
/* 1199 */             k = 0;
/* 1200 */             if (localStringBuffer2 == null)
/* 1201 */               localStringBuffer2 = new StringBuffer();
/*      */             else {
/* 1203 */               localStringBuffer2.setLength(0);
/*      */             }
/* 1205 */             int i4 = 0;
/* 1206 */             while (!paramRuleCharacterIterator.atEnd()) {
/* 1207 */               i2 = paramRuleCharacterIterator.next(i);
/* 1208 */               bool = paramRuleCharacterIterator.isEscaped();
/* 1209 */               if ((i2 == 125) && (!bool)) {
/* 1210 */                 i4 = 1;
/* 1211 */                 break;
/*      */               }
/* 1213 */               UTF16.append(localStringBuffer2, i2);
/*      */             }
/* 1215 */             if ((localStringBuffer2.length() < 1) || (i4 == 0)) {
/* 1216 */               syntaxError(paramRuleCharacterIterator, "Invalid multicharacter string");
/*      */             }
/*      */ 
/* 1221 */             add(localStringBuffer2.toString());
/* 1222 */             localStringBuffer1.append('{');
/* 1223 */             _appendToPat(localStringBuffer1, localStringBuffer2.toString(), false);
/* 1224 */             localStringBuffer1.append('}');
/* 1225 */             break;
/*      */           case 36:
/* 1233 */             localObject = paramRuleCharacterIterator.getPos(localObject);
/* 1234 */             i2 = paramRuleCharacterIterator.next(i);
/* 1235 */             bool = paramRuleCharacterIterator.isEscaped();
/* 1236 */             int i5 = (i2 == 93) && (!bool) ? 1 : 0;
/* 1237 */             if ((paramSymbolTable == null) && (i5 == 0)) {
/* 1238 */               i2 = 36;
/* 1239 */               paramRuleCharacterIterator.setPos(localObject);
/*      */             }
/*      */             else {
/* 1242 */               if ((i5 != 0) && (c == 0)) {
/* 1243 */                 if (k == 1) {
/* 1244 */                   add_unchecked(m, m);
/* 1245 */                   _appendToPat(localStringBuffer1, m, false);
/*      */                 }
/* 1247 */                 add_unchecked(65535);
/* 1248 */                 j = 1;
/* 1249 */                 localStringBuffer1.append('$').append(']');
/* 1250 */                 n = 2;
/* 1251 */                 continue;
/*      */               }
/* 1253 */               syntaxError(paramRuleCharacterIterator, "Unquoted '$'");
/*      */             }
/*      */ 
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1263 */           switch (k) {
/*      */           case 0:
/* 1265 */             k = 1;
/* 1266 */             m = i2;
/* 1267 */             break;
/*      */           case 1:
/* 1269 */             if (c == '-') {
/* 1270 */               if (m >= i2)
/*      */               {
/* 1273 */                 syntaxError(paramRuleCharacterIterator, "Invalid range");
/*      */               }
/* 1275 */               add_unchecked(m, i2);
/* 1276 */               _appendToPat(localStringBuffer1, m, false);
/* 1277 */               localStringBuffer1.append(c);
/* 1278 */               _appendToPat(localStringBuffer1, i2, false);
/* 1279 */               k = c = 0;
/*      */             } else {
/* 1281 */               add_unchecked(m, m);
/* 1282 */               _appendToPat(localStringBuffer1, m, false);
/* 1283 */               m = i2;
/*      */             }
/* 1285 */             break;
/*      */           case 2:
/* 1287 */             if (c != 0) {
/* 1288 */               syntaxError(paramRuleCharacterIterator, "Set expected after operator");
/*      */             }
/* 1290 */             m = i2;
/* 1291 */             k = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1296 */     if (n != 2) {
/* 1297 */       syntaxError(paramRuleCharacterIterator, "Missing ']'");
/*      */     }
/*      */ 
/* 1300 */     paramRuleCharacterIterator.skipIgnored(i);
/*      */ 
/* 1302 */     if (i1 != 0) {
/* 1303 */       complement();
/*      */     }
/*      */ 
/* 1308 */     if (j != 0)
/* 1309 */       paramStringBuffer.append(localStringBuffer1.toString());
/*      */     else
/* 1311 */       _generatePattern(paramStringBuffer, false, true);
/*      */   }
/*      */ 
/*      */   private static void syntaxError(RuleCharacterIterator paramRuleCharacterIterator, String paramString)
/*      */   {
/* 1316 */     throw new IllegalArgumentException("Error: " + paramString + " at \"" + Utility.escape(paramRuleCharacterIterator.toString()) + '"');
/*      */   }
/*      */ 
/*      */   private void ensureCapacity(int paramInt)
/*      */   {
/* 1326 */     if (paramInt <= this.list.length) return;
/* 1327 */     int[] arrayOfInt = new int[paramInt + 16];
/* 1328 */     System.arraycopy(this.list, 0, arrayOfInt, 0, this.len);
/* 1329 */     this.list = arrayOfInt;
/*      */   }
/*      */ 
/*      */   private void ensureBufferCapacity(int paramInt) {
/* 1333 */     if ((this.buffer != null) && (paramInt <= this.buffer.length)) return;
/* 1334 */     this.buffer = new int[paramInt + 16];
/*      */   }
/*      */ 
/*      */   private int[] range(int paramInt1, int paramInt2)
/*      */   {
/* 1341 */     if (this.rangeList == null) {
/* 1342 */       this.rangeList = new int[] { paramInt1, paramInt2 + 1, 1114112 };
/*      */     } else {
/* 1344 */       this.rangeList[0] = paramInt1;
/* 1345 */       this.rangeList[1] = (paramInt2 + 1);
/*      */     }
/* 1347 */     return this.rangeList;
/*      */   }
/*      */ 
/*      */   private UnicodeSet xor(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1358 */     ensureBufferCapacity(this.len + paramInt1);
/* 1359 */     int i = 0; int j = 0; int k = 0;
/* 1360 */     int m = this.list[(i++)];
/*      */     int n;
/* 1362 */     if ((paramInt2 == 1) || (paramInt2 == 2)) {
/* 1363 */       n = 0;
/* 1364 */       if (paramArrayOfInt[j] == 0) {
/* 1365 */         j++;
/* 1366 */         n = paramArrayOfInt[j];
/*      */       }
/*      */     } else {
/* 1369 */       n = paramArrayOfInt[(j++)];
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/* 1374 */       if (m < n) {
/* 1375 */         this.buffer[(k++)] = m;
/* 1376 */         m = this.list[(i++)];
/* 1377 */       } else if (n < m) {
/* 1378 */         this.buffer[(k++)] = n;
/* 1379 */         n = paramArrayOfInt[(j++)]; } else {
/* 1380 */         if (m == 1114112)
/*      */           break;
/* 1382 */         m = this.list[(i++)];
/* 1383 */         n = paramArrayOfInt[(j++)];
/*      */       }
/*      */     }
/* 1385 */     this.buffer[(k++)] = 1114112;
/* 1386 */     this.len = k;
/*      */ 
/* 1391 */     int[] arrayOfInt = this.list;
/* 1392 */     this.list = this.buffer;
/* 1393 */     this.buffer = arrayOfInt;
/* 1394 */     this.pat = null;
/* 1395 */     return this;
/*      */   }
/*      */ 
/*      */   private UnicodeSet add(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1404 */     ensureBufferCapacity(this.len + paramInt1);
/* 1405 */     int i = 0; int j = 0; int k = 0;
/* 1406 */     int m = this.list[(i++)];
/* 1407 */     int n = paramArrayOfInt[(j++)];
/*      */     while (true)
/*      */     {
/* 1412 */       switch (paramInt2) {
/*      */       case 0:
/* 1414 */         if (m < n)
/*      */         {
/* 1416 */           if ((k > 0) && (m <= this.buffer[(k - 1)]))
/*      */           {
/* 1418 */             m = max(this.list[i], this.buffer[(--k)]);
/*      */           }
/*      */           else {
/* 1421 */             this.buffer[(k++)] = m;
/* 1422 */             m = this.list[i];
/*      */           }
/* 1424 */           i++;
/* 1425 */           paramInt2 ^= 1;
/* 1426 */         } else if (n < m) {
/* 1427 */           if ((k > 0) && (n <= this.buffer[(k - 1)])) {
/* 1428 */             n = max(paramArrayOfInt[j], this.buffer[(--k)]);
/*      */           } else {
/* 1430 */             this.buffer[(k++)] = n;
/* 1431 */             n = paramArrayOfInt[j];
/*      */           }
/* 1433 */           j++;
/* 1434 */           paramInt2 ^= 2;
/*      */         } else {
/* 1436 */           if (m == 1114112) {
/*      */             break label620;
/*      */           }
/* 1439 */           if ((k > 0) && (m <= this.buffer[(k - 1)])) {
/* 1440 */             m = max(this.list[i], this.buffer[(--k)]);
/*      */           }
/*      */           else {
/* 1443 */             this.buffer[(k++)] = m;
/* 1444 */             m = this.list[i];
/*      */           }
/* 1446 */           i++;
/* 1447 */           paramInt2 ^= 1;
/* 1448 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/* 1450 */         break;
/*      */       case 3:
/* 1452 */         if (n <= m) {
/* 1453 */           if (m == 1114112) break label620;
/* 1454 */           this.buffer[(k++)] = m;
/*      */         } else {
/* 1456 */           if (n == 1114112) break label620;
/* 1457 */           this.buffer[(k++)] = n;
/*      */         }
/* 1459 */         m = this.list[(i++)]; paramInt2 ^= 1;
/* 1460 */         n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/* 1461 */         break;
/*      */       case 1:
/* 1463 */         if (m < n) {
/* 1464 */           this.buffer[(k++)] = m; m = this.list[(i++)]; paramInt2 ^= 1;
/* 1465 */         } else if (n < m) {
/* 1466 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         } else {
/* 1468 */           if (m == 1114112) break label620;
/* 1469 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1470 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/* 1472 */         break;
/*      */       case 2:
/* 1474 */         if (n < m) {
/* 1475 */           this.buffer[(k++)] = n; n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/* 1476 */         } else if (m < n) {
/* 1477 */           m = this.list[(i++)]; paramInt2 ^= 1;
/*      */         } else {
/* 1479 */           if (m == 1114112) break label620;
/* 1480 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1481 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/* 1486 */     label620: this.buffer[(k++)] = 1114112;
/* 1487 */     this.len = k;
/*      */ 
/* 1489 */     int[] arrayOfInt = this.list;
/* 1490 */     this.list = this.buffer;
/* 1491 */     this.buffer = arrayOfInt;
/* 1492 */     this.pat = null;
/* 1493 */     return this;
/*      */   }
/*      */ 
/*      */   private UnicodeSet retain(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1502 */     ensureBufferCapacity(this.len + paramInt1);
/* 1503 */     int i = 0; int j = 0; int k = 0;
/* 1504 */     int m = this.list[(i++)];
/* 1505 */     int n = paramArrayOfInt[(j++)];
/*      */     while (true)
/*      */     {
/* 1510 */       switch (paramInt2) {
/*      */       case 0:
/* 1512 */         if (m < n) {
/* 1513 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1514 */         } else if (n < m) {
/* 1515 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         } else {
/* 1517 */           if (m == 1114112) break label508;
/* 1518 */           this.buffer[(k++)] = m; m = this.list[(i++)]; paramInt2 ^= 1;
/* 1519 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/* 1521 */         break;
/*      */       case 3:
/* 1523 */         if (m < n) {
/* 1524 */           this.buffer[(k++)] = m; m = this.list[(i++)]; paramInt2 ^= 1;
/* 1525 */         } else if (n < m) {
/* 1526 */           this.buffer[(k++)] = n; n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         } else {
/* 1528 */           if (m == 1114112) break label508;
/* 1529 */           this.buffer[(k++)] = m; m = this.list[(i++)]; paramInt2 ^= 1;
/* 1530 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/* 1532 */         break;
/*      */       case 1:
/* 1534 */         if (m < n) {
/* 1535 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1536 */         } else if (n < m) {
/* 1537 */           this.buffer[(k++)] = n; n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         } else {
/* 1539 */           if (m == 1114112) break label508;
/* 1540 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1541 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/* 1543 */         break;
/*      */       case 2:
/* 1545 */         if (n < m) {
/* 1546 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/* 1547 */         } else if (m < n) {
/* 1548 */           this.buffer[(k++)] = m; m = this.list[(i++)]; paramInt2 ^= 1;
/*      */         } else {
/* 1550 */           if (m == 1114112) break label508;
/* 1551 */           m = this.list[(i++)]; paramInt2 ^= 1;
/* 1552 */           n = paramArrayOfInt[(j++)]; paramInt2 ^= 2;
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/* 1557 */     label508: this.buffer[(k++)] = 1114112;
/* 1558 */     this.len = k;
/*      */ 
/* 1560 */     int[] arrayOfInt = this.list;
/* 1561 */     this.list = this.buffer;
/* 1562 */     this.buffer = arrayOfInt;
/* 1563 */     this.pat = null;
/* 1564 */     return this;
/*      */   }
/*      */ 
/*      */   private static final int max(int paramInt1, int paramInt2) {
/* 1568 */     return paramInt1 > paramInt2 ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */   private static synchronized UnicodeSet getInclusions(int paramInt)
/*      */   {
/* 1597 */     if (INCLUSIONS == null) {
/* 1598 */       INCLUSIONS = new UnicodeSet[9];
/*      */     }
/* 1600 */     if (INCLUSIONS[paramInt] == null) {
/* 1601 */       UnicodeSet localUnicodeSet = new UnicodeSet();
/* 1602 */       switch (paramInt) {
/*      */       case 2:
/* 1604 */         UCharacterProperty.getInstance().upropsvec_addPropertyStarts(localUnicodeSet);
/* 1605 */         break;
/*      */       default:
/* 1607 */         throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + paramInt + ")");
/*      */       }
/* 1609 */       INCLUSIONS[paramInt] = localUnicodeSet;
/*      */     }
/* 1611 */     return INCLUSIONS[paramInt];
/*      */   }
/*      */ 
/*      */   private UnicodeSet applyFilter(Filter paramFilter, int paramInt)
/*      */   {
/* 1632 */     clear();
/*      */ 
/* 1634 */     int i = -1;
/* 1635 */     UnicodeSet localUnicodeSet = getInclusions(paramInt);
/* 1636 */     int j = localUnicodeSet.getRangeCount();
/*      */ 
/* 1638 */     for (int k = 0; k < j; k++)
/*      */     {
/* 1640 */       int m = localUnicodeSet.getRangeStart(k);
/* 1641 */       int n = localUnicodeSet.getRangeEnd(k);
/*      */ 
/* 1644 */       for (int i1 = m; i1 <= n; i1++)
/*      */       {
/* 1647 */         if (paramFilter.contains(i1)) {
/* 1648 */           if (i < 0)
/* 1649 */             i = i1;
/*      */         }
/* 1651 */         else if (i >= 0) {
/* 1652 */           add_unchecked(i, i1 - 1);
/* 1653 */           i = -1;
/*      */         }
/*      */       }
/*      */     }
/* 1657 */     if (i >= 0) {
/* 1658 */       add_unchecked(i, 1114111);
/*      */     }
/*      */ 
/* 1661 */     return this;
/*      */   }
/*      */ 
/*      */   private static String mungeCharName(String paramString)
/*      */   {
/* 1671 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1672 */     for (int i = 0; i < paramString.length(); ) {
/* 1673 */       int j = UTF16.charAt(paramString, i);
/* 1674 */       i += UTF16.getCharCount(j);
/* 1675 */       if (UCharacterProperty.isRuleWhiteSpace(j)) {
/* 1676 */         if ((localStringBuffer.length() != 0) && (localStringBuffer.charAt(localStringBuffer.length() - 1) != ' '))
/*      */         {
/* 1680 */           j = 32;
/*      */         }
/*      */       } else UTF16.append(localStringBuffer, j);
/*      */     }
/* 1684 */     if ((localStringBuffer.length() != 0) && (localStringBuffer.charAt(localStringBuffer.length() - 1) == ' '))
/*      */     {
/* 1686 */       localStringBuffer.setLength(localStringBuffer.length() - 1);
/*      */     }
/* 1688 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public UnicodeSet applyPropertyAlias(String paramString1, String paramString2, SymbolTable paramSymbolTable)
/*      */   {
/* 1704 */     if ((paramString2.length() > 0) && 
/* 1705 */       (paramString1.equals("Age")))
/*      */     {
/* 1709 */       VersionInfo localVersionInfo = VersionInfo.getInstance(mungeCharName(paramString2));
/* 1710 */       applyFilter(new VersionFilter(localVersionInfo), 2);
/* 1711 */       return this;
/*      */     }
/*      */ 
/* 1714 */     throw new IllegalArgumentException("Unsupported property: " + paramString1);
/*      */   }
/*      */ 
/*      */   private static boolean resemblesPropertyPattern(RuleCharacterIterator paramRuleCharacterIterator, int paramInt)
/*      */   {
/* 1727 */     boolean bool = false;
/* 1728 */     paramInt &= -3;
/* 1729 */     Object localObject = paramRuleCharacterIterator.getPos(null);
/* 1730 */     int i = paramRuleCharacterIterator.next(paramInt);
/* 1731 */     if ((i == 91) || (i == 92)) {
/* 1732 */       int j = paramRuleCharacterIterator.next(paramInt & 0xFFFFFFFB);
/* 1733 */       bool = j == 58;
/*      */     }
/*      */ 
/* 1736 */     paramRuleCharacterIterator.setPos(localObject);
/* 1737 */     return bool;
/*      */   }
/*      */ 
/*      */   private UnicodeSet applyPropertyPattern(String paramString, ParsePosition paramParsePosition, SymbolTable paramSymbolTable)
/*      */   {
/* 1745 */     int i = paramParsePosition.getIndex();
/*      */ 
/* 1750 */     if (i + 5 > paramString.length()) {
/* 1751 */       return null;
/*      */     }
/*      */ 
/* 1754 */     int j = 0;
/* 1755 */     int k = 0;
/* 1756 */     int m = 0;
/*      */ 
/* 1759 */     if (paramString.regionMatches(i, "[:", 0, 2)) {
/* 1760 */       j = 1;
/* 1761 */       i = Utility.skipWhitespace(paramString, i + 2);
/* 1762 */       if ((i < paramString.length()) && (paramString.charAt(i) == '^')) {
/* 1763 */         i++;
/* 1764 */         m = 1;
/*      */       }
/* 1766 */     } else if ((paramString.regionMatches(true, i, "\\p", 0, 2)) || (paramString.regionMatches(i, "\\N", 0, 2)))
/*      */     {
/* 1768 */       n = paramString.charAt(i + 1);
/* 1769 */       m = n == 80 ? 1 : 0;
/* 1770 */       k = n == 78 ? 1 : 0;
/* 1771 */       i = Utility.skipWhitespace(paramString, i + 2);
/* 1772 */       if ((i == paramString.length()) || (paramString.charAt(i++) != '{'))
/*      */       {
/* 1774 */         return null;
/*      */       }
/*      */     }
/*      */     else {
/* 1778 */       return null;
/*      */     }
/*      */ 
/* 1782 */     int n = paramString.indexOf(j != 0 ? ":]" : "}", i);
/* 1783 */     if (n < 0)
/*      */     {
/* 1785 */       return null;
/*      */     }
/*      */ 
/* 1791 */     int i1 = paramString.indexOf('=', i);
/*      */     String str1;
/*      */     String str2;
/* 1793 */     if ((i1 >= 0) && (i1 < n) && (k == 0))
/*      */     {
/* 1795 */       str1 = paramString.substring(i, i1);
/* 1796 */       str2 = paramString.substring(i1 + 1, n);
/*      */     }
/*      */     else
/*      */     {
/* 1801 */       str1 = paramString.substring(i, n);
/* 1802 */       str2 = "";
/*      */ 
/* 1805 */       if (k != 0)
/*      */       {
/* 1811 */         str2 = str1;
/* 1812 */         str1 = "na";
/*      */       }
/*      */     }
/*      */ 
/* 1816 */     applyPropertyAlias(str1, str2, paramSymbolTable);
/*      */ 
/* 1818 */     if (m != 0) {
/* 1819 */       complement();
/*      */     }
/*      */ 
/* 1823 */     paramParsePosition.setIndex(n + (j != 0 ? 2 : 1));
/*      */ 
/* 1825 */     return this;
/*      */   }
/*      */ 
/*      */   private void applyPropertyPattern(RuleCharacterIterator paramRuleCharacterIterator, StringBuffer paramStringBuffer, SymbolTable paramSymbolTable)
/*      */   {
/* 1840 */     String str = paramRuleCharacterIterator.lookahead();
/* 1841 */     ParsePosition localParsePosition = new ParsePosition(0);
/* 1842 */     applyPropertyPattern(str, localParsePosition, paramSymbolTable);
/* 1843 */     if (localParsePosition.getIndex() == 0) {
/* 1844 */       syntaxError(paramRuleCharacterIterator, "Invalid property pattern");
/*      */     }
/* 1846 */     paramRuleCharacterIterator.jumpahead(localParsePosition.getIndex());
/* 1847 */     paramStringBuffer.append(str.substring(0, localParsePosition.getIndex()));
/*      */   }
/*      */ 
/*      */   private static abstract interface Filter
/*      */   {
/*      */     public abstract boolean contains(int paramInt);
/*      */   }
/*      */ 
/*      */   private static class VersionFilter
/*      */     implements UnicodeSet.Filter
/*      */   {
/*      */     VersionInfo version;
/*      */ 
/*      */     VersionFilter(VersionInfo paramVersionInfo)
/*      */     {
/* 1585 */       this.version = paramVersionInfo;
/*      */     }
/*      */     public boolean contains(int paramInt) {
/* 1588 */       VersionInfo localVersionInfo = UCharacter.getAge(paramInt);
/*      */ 
/* 1591 */       return (localVersionInfo != UnicodeSet.NO_VERSION) && (localVersionInfo.compareTo(this.version) <= 0);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UnicodeSet
 * JD-Core Version:    0.6.2
 */