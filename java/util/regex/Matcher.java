/*      */ package java.util.regex;
/*      */ 
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class Matcher
/*      */   implements MatchResult
/*      */ {
/*      */   Pattern parentPattern;
/*      */   int[] groups;
/*      */   int from;
/*      */   int to;
/*      */   int lookbehindTo;
/*      */   CharSequence text;
/*      */   static final int ENDANCHOR = 1;
/*      */   static final int NOANCHOR = 0;
/*  142 */   int acceptMode = 0;
/*      */ 
/*  150 */   int first = -1; int last = 0;
/*      */ 
/*  155 */   int oldLast = -1;
/*      */ 
/*  160 */   int lastAppendPosition = 0;
/*      */   int[] locals;
/*      */   boolean hitEnd;
/*      */   boolean requireEnd;
/*  201 */   boolean transparentBounds = false;
/*      */ 
/*  207 */   boolean anchoringBounds = true;
/*      */ 
/*      */   Matcher()
/*      */   {
/*      */   }
/*      */ 
/*      */   Matcher(Pattern paramPattern, CharSequence paramCharSequence)
/*      */   {
/*  219 */     this.parentPattern = paramPattern;
/*  220 */     this.text = paramCharSequence;
/*      */ 
/*  223 */     int i = Math.max(paramPattern.capturingGroupCount, 10);
/*  224 */     this.groups = new int[i * 2];
/*  225 */     this.locals = new int[paramPattern.localCount];
/*      */ 
/*  228 */     reset();
/*      */   }
/*      */ 
/*      */   public Pattern pattern()
/*      */   {
/*  237 */     return this.parentPattern;
/*      */   }
/*      */ 
/*      */   public MatchResult toMatchResult()
/*      */   {
/*  249 */     Matcher localMatcher = new Matcher(this.parentPattern, this.text.toString());
/*  250 */     localMatcher.first = this.first;
/*  251 */     localMatcher.last = this.last;
/*  252 */     localMatcher.groups = ((int[])this.groups.clone());
/*  253 */     return localMatcher;
/*      */   }
/*      */ 
/*      */   public Matcher usePattern(Pattern paramPattern)
/*      */   {
/*  273 */     if (paramPattern == null)
/*  274 */       throw new IllegalArgumentException("Pattern cannot be null");
/*  275 */     this.parentPattern = paramPattern;
/*      */ 
/*  278 */     int i = Math.max(paramPattern.capturingGroupCount, 10);
/*  279 */     this.groups = new int[i * 2];
/*  280 */     this.locals = new int[paramPattern.localCount];
/*  281 */     for (int j = 0; j < this.groups.length; j++)
/*  282 */       this.groups[j] = -1;
/*  283 */     for (j = 0; j < this.locals.length; j++)
/*  284 */       this.locals[j] = -1;
/*  285 */     return this;
/*      */   }
/*      */ 
/*      */   public Matcher reset()
/*      */   {
/*  299 */     this.first = -1;
/*  300 */     this.last = 0;
/*  301 */     this.oldLast = -1;
/*  302 */     for (int i = 0; i < this.groups.length; i++)
/*  303 */       this.groups[i] = -1;
/*  304 */     for (i = 0; i < this.locals.length; i++)
/*  305 */       this.locals[i] = -1;
/*  306 */     this.lastAppendPosition = 0;
/*  307 */     this.from = 0;
/*  308 */     this.to = getTextLength();
/*  309 */     return this;
/*      */   }
/*      */ 
/*      */   public Matcher reset(CharSequence paramCharSequence)
/*      */   {
/*  327 */     this.text = paramCharSequence;
/*  328 */     return reset();
/*      */   }
/*      */ 
/*      */   public int start()
/*      */   {
/*  341 */     if (this.first < 0)
/*  342 */       throw new IllegalStateException("No match available");
/*  343 */     return this.first;
/*      */   }
/*      */ 
/*      */   public int start(int paramInt)
/*      */   {
/*  371 */     if (this.first < 0)
/*  372 */       throw new IllegalStateException("No match available");
/*  373 */     if (paramInt > groupCount())
/*  374 */       throw new IndexOutOfBoundsException("No group " + paramInt);
/*  375 */     return this.groups[(paramInt * 2)];
/*      */   }
/*      */ 
/*      */   public int end()
/*      */   {
/*  388 */     if (this.first < 0)
/*  389 */       throw new IllegalStateException("No match available");
/*  390 */     return this.last;
/*      */   }
/*      */ 
/*      */   public int end(int paramInt)
/*      */   {
/*  418 */     if (this.first < 0)
/*  419 */       throw new IllegalStateException("No match available");
/*  420 */     if (paramInt > groupCount())
/*  421 */       throw new IndexOutOfBoundsException("No group " + paramInt);
/*  422 */     return this.groups[(paramInt * 2 + 1)];
/*      */   }
/*      */ 
/*      */   public String group()
/*      */   {
/*  445 */     return group(0);
/*      */   }
/*      */ 
/*      */   public String group(int paramInt)
/*      */   {
/*  484 */     if (this.first < 0)
/*  485 */       throw new IllegalStateException("No match found");
/*  486 */     if ((paramInt < 0) || (paramInt > groupCount()))
/*  487 */       throw new IndexOutOfBoundsException("No group " + paramInt);
/*  488 */     if ((this.groups[(paramInt * 2)] == -1) || (this.groups[(paramInt * 2 + 1)] == -1))
/*  489 */       return null;
/*  490 */     return getSubSequence(this.groups[(paramInt * 2)], this.groups[(paramInt * 2 + 1)]).toString();
/*      */   }
/*      */ 
/*      */   public String group(String paramString)
/*      */   {
/*  521 */     if (paramString == null)
/*  522 */       throw new NullPointerException("Null group name");
/*  523 */     if (this.first < 0)
/*  524 */       throw new IllegalStateException("No match found");
/*  525 */     if (!this.parentPattern.namedGroups().containsKey(paramString))
/*  526 */       throw new IllegalArgumentException("No group with name <" + paramString + ">");
/*  527 */     int i = ((Integer)this.parentPattern.namedGroups().get(paramString)).intValue();
/*  528 */     if ((this.groups[(i * 2)] == -1) || (this.groups[(i * 2 + 1)] == -1))
/*  529 */       return null;
/*  530 */     return getSubSequence(this.groups[(i * 2)], this.groups[(i * 2 + 1)]).toString();
/*      */   }
/*      */ 
/*      */   public int groupCount()
/*      */   {
/*  546 */     return this.parentPattern.capturingGroupCount - 1;
/*      */   }
/*      */ 
/*      */   public boolean matches()
/*      */   {
/*  559 */     return match(this.from, 1);
/*      */   }
/*      */ 
/*      */   public boolean find()
/*      */   {
/*  578 */     int i = this.last;
/*  579 */     if (i == this.first) {
/*  580 */       i++;
/*      */     }
/*      */ 
/*  583 */     if (i < this.from) {
/*  584 */       i = this.from;
/*      */     }
/*      */ 
/*  587 */     if (i > this.to) {
/*  588 */       for (int j = 0; j < this.groups.length; j++)
/*  589 */         this.groups[j] = -1;
/*  590 */       return false;
/*      */     }
/*  592 */     return search(i);
/*      */   }
/*      */ 
/*      */   public boolean find(int paramInt)
/*      */   {
/*  614 */     int i = getTextLength();
/*  615 */     if ((paramInt < 0) || (paramInt > i))
/*  616 */       throw new IndexOutOfBoundsException("Illegal start index");
/*  617 */     reset();
/*  618 */     return search(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean lookingAt()
/*      */   {
/*  636 */     return match(this.from, 0);
/*      */   }
/*      */ 
/*      */   public static String quoteReplacement(String paramString)
/*      */   {
/*  655 */     if ((paramString.indexOf('\\') == -1) && (paramString.indexOf('$') == -1))
/*  656 */       return paramString;
/*  657 */     StringBuilder localStringBuilder = new StringBuilder();
/*  658 */     for (int i = 0; i < paramString.length(); i++) {
/*  659 */       char c = paramString.charAt(i);
/*  660 */       if ((c == '\\') || (c == '$')) {
/*  661 */         localStringBuilder.append('\\');
/*      */       }
/*  663 */       localStringBuilder.append(c);
/*      */     }
/*  665 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public Matcher appendReplacement(StringBuffer paramStringBuffer, String paramString)
/*      */   {
/*  751 */     if (this.first < 0) {
/*  752 */       throw new IllegalStateException("No match available");
/*      */     }
/*      */ 
/*  755 */     int i = 0;
/*  756 */     StringBuilder localStringBuilder1 = new StringBuilder();
/*      */ 
/*  758 */     while (i < paramString.length()) {
/*  759 */       char c = paramString.charAt(i);
/*  760 */       if (c == '\\') {
/*  761 */         i++;
/*  762 */         c = paramString.charAt(i);
/*  763 */         localStringBuilder1.append(c);
/*  764 */         i++;
/*  765 */       } else if (c == '$')
/*      */       {
/*  767 */         i++;
/*      */ 
/*  772 */         c = paramString.charAt(i);
/*  773 */         int j = -1;
/*  774 */         if (c == '{') {
/*  775 */           i++;
/*  776 */           StringBuilder localStringBuilder2 = new StringBuilder();
/*  777 */           while (i < paramString.length()) {
/*  778 */             c = paramString.charAt(i);
/*  779 */             if ((!ASCII.isLower(c)) && (!ASCII.isUpper(c)) && (!ASCII.isDigit(c))) {
/*      */               break;
/*      */             }
/*  782 */             localStringBuilder2.append(c);
/*  783 */             i++;
/*      */           }
/*      */ 
/*  788 */           if (localStringBuilder2.length() == 0) {
/*  789 */             throw new IllegalArgumentException("named capturing group has 0 length name");
/*      */           }
/*  791 */           if (c != '}') {
/*  792 */             throw new IllegalArgumentException("named capturing group is missing trailing '}'");
/*      */           }
/*  794 */           String str = localStringBuilder2.toString();
/*  795 */           if (ASCII.isDigit(str.charAt(0))) {
/*  796 */             throw new IllegalArgumentException("capturing group name {" + str + "} starts with digit character");
/*      */           }
/*      */ 
/*  799 */           if (!this.parentPattern.namedGroups().containsKey(str)) {
/*  800 */             throw new IllegalArgumentException("No group with name {" + str + "}");
/*      */           }
/*  802 */           j = ((Integer)this.parentPattern.namedGroups().get(str)).intValue();
/*  803 */           i++;
/*      */         }
/*      */         else {
/*  806 */           j = c - '0';
/*  807 */           if ((j < 0) || (j > 9)) {
/*  808 */             throw new IllegalArgumentException("Illegal group reference");
/*      */           }
/*  810 */           i++;
/*      */ 
/*  812 */           int k = 0;
/*  813 */           while ((k == 0) && 
/*  814 */             (i < paramString.length()))
/*      */           {
/*  817 */             int m = paramString.charAt(i) - '0';
/*  818 */             if ((m < 0) || (m > 9)) {
/*      */               break;
/*      */             }
/*  821 */             int n = j * 10 + m;
/*  822 */             if (groupCount() < n) {
/*  823 */               k = 1;
/*      */             } else {
/*  825 */               j = n;
/*  826 */               i++;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  831 */         if ((start(j) != -1) && (end(j) != -1))
/*  832 */           localStringBuilder1.append(this.text, start(j), end(j));
/*      */       } else {
/*  834 */         localStringBuilder1.append(c);
/*  835 */         i++;
/*      */       }
/*      */     }
/*      */ 
/*  839 */     paramStringBuffer.append(this.text, this.lastAppendPosition, this.first);
/*      */ 
/*  841 */     paramStringBuffer.append(localStringBuilder1);
/*      */ 
/*  843 */     this.lastAppendPosition = this.last;
/*  844 */     return this;
/*      */   }
/*      */ 
/*      */   public StringBuffer appendTail(StringBuffer paramStringBuffer)
/*      */   {
/*  862 */     paramStringBuffer.append(this.text, this.lastAppendPosition, getTextLength());
/*  863 */     return paramStringBuffer;
/*      */   }
/*      */ 
/*      */   public String replaceAll(String paramString)
/*      */   {
/*  901 */     reset();
/*  902 */     boolean bool = find();
/*  903 */     if (bool) {
/*  904 */       StringBuffer localStringBuffer = new StringBuffer();
/*      */       do {
/*  906 */         appendReplacement(localStringBuffer, paramString);
/*  907 */         bool = find();
/*  908 */       }while (bool);
/*  909 */       appendTail(localStringBuffer);
/*  910 */       return localStringBuffer.toString();
/*      */     }
/*  912 */     return this.text.toString();
/*      */   }
/*      */ 
/*      */   public String replaceFirst(String paramString)
/*      */   {
/*  949 */     if (paramString == null)
/*  950 */       throw new NullPointerException("replacement");
/*  951 */     reset();
/*  952 */     if (!find())
/*  953 */       return this.text.toString();
/*  954 */     StringBuffer localStringBuffer = new StringBuffer();
/*  955 */     appendReplacement(localStringBuffer, paramString);
/*  956 */     appendTail(localStringBuffer);
/*  957 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public Matcher region(int paramInt1, int paramInt2)
/*      */   {
/*  986 */     if ((paramInt1 < 0) || (paramInt1 > getTextLength()))
/*  987 */       throw new IndexOutOfBoundsException("start");
/*  988 */     if ((paramInt2 < 0) || (paramInt2 > getTextLength()))
/*  989 */       throw new IndexOutOfBoundsException("end");
/*  990 */     if (paramInt1 > paramInt2)
/*  991 */       throw new IndexOutOfBoundsException("start > end");
/*  992 */     reset();
/*  993 */     this.from = paramInt1;
/*  994 */     this.to = paramInt2;
/*  995 */     return this;
/*      */   }
/*      */ 
/*      */   public int regionStart()
/*      */   {
/* 1008 */     return this.from;
/*      */   }
/*      */ 
/*      */   public int regionEnd()
/*      */   {
/* 1021 */     return this.to;
/*      */   }
/*      */ 
/*      */   public boolean hasTransparentBounds()
/*      */   {
/* 1042 */     return this.transparentBounds;
/*      */   }
/*      */ 
/*      */   public Matcher useTransparentBounds(boolean paramBoolean)
/*      */   {
/* 1072 */     this.transparentBounds = paramBoolean;
/* 1073 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean hasAnchoringBounds()
/*      */   {
/* 1093 */     return this.anchoringBounds;
/*      */   }
/*      */ 
/*      */   public Matcher useAnchoringBounds(boolean paramBoolean)
/*      */   {
/* 1118 */     this.anchoringBounds = paramBoolean;
/* 1119 */     return this;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1131 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1132 */     localStringBuilder.append("java.util.regex.Matcher");
/* 1133 */     localStringBuilder.append("[pattern=" + pattern());
/* 1134 */     localStringBuilder.append(" region=");
/* 1135 */     localStringBuilder.append(regionStart() + "," + regionEnd());
/* 1136 */     localStringBuilder.append(" lastmatch=");
/* 1137 */     if ((this.first >= 0) && (group() != null)) {
/* 1138 */       localStringBuilder.append(group());
/*      */     }
/* 1140 */     localStringBuilder.append("]");
/* 1141 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public boolean hitEnd()
/*      */   {
/* 1156 */     return this.hitEnd;
/*      */   }
/*      */ 
/*      */   public boolean requireEnd()
/*      */   {
/* 1174 */     return this.requireEnd;
/*      */   }
/*      */ 
/*      */   boolean search(int paramInt)
/*      */   {
/* 1191 */     this.hitEnd = false;
/* 1192 */     this.requireEnd = false;
/* 1193 */     paramInt = paramInt < 0 ? 0 : paramInt;
/* 1194 */     this.first = paramInt;
/* 1195 */     this.oldLast = (this.oldLast < 0 ? paramInt : this.oldLast);
/* 1196 */     for (int i = 0; i < this.groups.length; i++)
/* 1197 */       this.groups[i] = -1;
/* 1198 */     this.acceptMode = 0;
/* 1199 */     boolean bool = this.parentPattern.root.match(this, paramInt, this.text);
/* 1200 */     if (!bool)
/* 1201 */       this.first = -1;
/* 1202 */     this.oldLast = this.last;
/* 1203 */     return bool;
/*      */   }
/*      */ 
/*      */   boolean match(int paramInt1, int paramInt2)
/*      */   {
/* 1213 */     this.hitEnd = false;
/* 1214 */     this.requireEnd = false;
/* 1215 */     paramInt1 = paramInt1 < 0 ? 0 : paramInt1;
/* 1216 */     this.first = paramInt1;
/* 1217 */     this.oldLast = (this.oldLast < 0 ? paramInt1 : this.oldLast);
/* 1218 */     for (int i = 0; i < this.groups.length; i++)
/* 1219 */       this.groups[i] = -1;
/* 1220 */     this.acceptMode = paramInt2;
/* 1221 */     boolean bool = this.parentPattern.matchRoot.match(this, paramInt1, this.text);
/* 1222 */     if (!bool)
/* 1223 */       this.first = -1;
/* 1224 */     this.oldLast = this.last;
/* 1225 */     return bool;
/*      */   }
/*      */ 
/*      */   int getTextLength()
/*      */   {
/* 1234 */     return this.text.length();
/*      */   }
/*      */ 
/*      */   CharSequence getSubSequence(int paramInt1, int paramInt2)
/*      */   {
/* 1245 */     return this.text.subSequence(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   char charAt(int paramInt)
/*      */   {
/* 1254 */     return this.text.charAt(paramInt);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.regex.Matcher
 * JD-Core Version:    0.6.2
 */