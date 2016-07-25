/*      */ package java.text;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.IOException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.MissingResourceException;
/*      */ import sun.text.CompactByteArray;
/*      */ import sun.text.SupplementaryCharacterData;
/*      */ 
/*      */ class RuleBasedBreakIterator extends BreakIterator
/*      */ {
/*      */   protected static final byte IGNORE = -1;
/*      */   private static final short START_STATE = 1;
/*      */   private static final short STOP_STATE = 0;
/*  242 */   static final byte[] LABEL = { 66, 73, 100, 97, 116, 97, 0 };
/*      */ 
/*  246 */   static final int LABEL_LENGTH = LABEL.length;
/*      */   static final byte supportedVersion = 1;
/*      */   private static final int HEADER_LENGTH = 36;
/*      */   private static final int BMP_INDICES_LENGTH = 512;
/*  266 */   private CompactByteArray charCategoryTable = null;
/*  267 */   private SupplementaryCharacterData supplementaryCharCategoryTable = null;
/*      */ 
/*  272 */   private short[] stateTable = null;
/*      */ 
/*  278 */   private short[] backwardsStateTable = null;
/*      */ 
/*  284 */   private boolean[] endStates = null;
/*      */ 
/*  290 */   private boolean[] lookaheadStates = null;
/*      */ 
/*  296 */   private byte[] additionalData = null;
/*      */   private int numCategories;
/*  307 */   private CharacterIterator text = null;
/*      */   private long checksum;
/*  624 */   private int cachedLastKnownBreak = -1;
/*      */ 
/*      */   public RuleBasedBreakIterator(String paramString)
/*      */     throws IOException, MissingResourceException
/*      */   {
/*  324 */     readTables(paramString);
/*      */   }
/*      */ 
/*      */   protected void readTables(String paramString)
/*      */     throws IOException, MissingResourceException
/*      */   {
/*  378 */     byte[] arrayOfByte1 = readFile(paramString);
/*      */ 
/*  381 */     int i = BreakIterator.getInt(arrayOfByte1, 0);
/*  382 */     int j = BreakIterator.getInt(arrayOfByte1, 4);
/*  383 */     int k = BreakIterator.getInt(arrayOfByte1, 8);
/*  384 */     int m = BreakIterator.getInt(arrayOfByte1, 12);
/*  385 */     int n = BreakIterator.getInt(arrayOfByte1, 16);
/*  386 */     int i1 = BreakIterator.getInt(arrayOfByte1, 20);
/*  387 */     int i2 = BreakIterator.getInt(arrayOfByte1, 24);
/*  388 */     this.checksum = BreakIterator.getLong(arrayOfByte1, 28);
/*      */ 
/*  391 */     this.stateTable = new short[i];
/*  392 */     int i3 = 36;
/*  393 */     for (int i4 = 0; i4 < i; i3 += 2) {
/*  394 */       this.stateTable[i4] = BreakIterator.getShort(arrayOfByte1, i3);
/*      */ 
/*  393 */       i4++;
/*      */     }
/*      */ 
/*  398 */     this.backwardsStateTable = new short[j];
/*  399 */     for (i4 = 0; i4 < j; i3 += 2) {
/*  400 */       this.backwardsStateTable[i4] = BreakIterator.getShort(arrayOfByte1, i3);
/*      */ 
/*  399 */       i4++;
/*      */     }
/*      */ 
/*  404 */     this.endStates = new boolean[k];
/*  405 */     for (i4 = 0; i4 < k; i3++) {
/*  406 */       this.endStates[i4] = (arrayOfByte1[i3] == 1 ? 1 : false);
/*      */ 
/*  405 */       i4++;
/*      */     }
/*      */ 
/*  410 */     this.lookaheadStates = new boolean[m];
/*  411 */     for (i4 = 0; i4 < m; i3++) {
/*  412 */       this.lookaheadStates[i4] = (arrayOfByte1[i3] == 1 ? 1 : false);
/*      */ 
/*  411 */       i4++;
/*      */     }
/*      */ 
/*  416 */     short[] arrayOfShort = new short[512];
/*  417 */     for (int i5 = 0; i5 < 512; i3 += 2) {
/*  418 */       arrayOfShort[i5] = BreakIterator.getShort(arrayOfByte1, i3);
/*      */ 
/*  417 */       i5++;
/*      */     }
/*      */ 
/*  420 */     byte[] arrayOfByte2 = new byte[n];
/*  421 */     System.arraycopy(arrayOfByte1, i3, arrayOfByte2, 0, n);
/*  422 */     i3 += n;
/*  423 */     this.charCategoryTable = new CompactByteArray(arrayOfShort, arrayOfByte2);
/*      */ 
/*  426 */     int[] arrayOfInt = new int[i1];
/*  427 */     for (int i6 = 0; i6 < i1; i3 += 4) {
/*  428 */       arrayOfInt[i6] = BreakIterator.getInt(arrayOfByte1, i3);
/*      */ 
/*  427 */       i6++;
/*      */     }
/*      */ 
/*  430 */     this.supplementaryCharCategoryTable = new SupplementaryCharacterData(arrayOfInt);
/*      */ 
/*  433 */     if (i2 > 0) {
/*  434 */       this.additionalData = new byte[i2];
/*  435 */       System.arraycopy(arrayOfByte1, i3, this.additionalData, 0, i2);
/*      */     }
/*      */ 
/*  439 */     this.numCategories = (this.stateTable.length / this.endStates.length);
/*      */   }
/*      */ 
/*      */   protected byte[] readFile(final String paramString) throws IOException, MissingResourceException
/*      */   {
/*      */     BufferedInputStream localBufferedInputStream;
/*      */     try
/*      */     {
/*  447 */       localBufferedInputStream = (BufferedInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Object run() throws Exception {
/*  450 */           return new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/" + paramString));
/*      */         }
/*      */       });
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException)
/*      */     {
/*  456 */       throw new InternalError(localPrivilegedActionException.toString());
/*      */     }
/*      */ 
/*  459 */     int i = 0;
/*      */ 
/*  462 */     int j = LABEL_LENGTH + 5;
/*  463 */     byte[] arrayOfByte = new byte[j];
/*  464 */     if (localBufferedInputStream.read(arrayOfByte) != j) {
/*  465 */       throw new MissingResourceException("Wrong header length", paramString, "");
/*      */     }
/*      */ 
/*  470 */     for (int k = 0; k < LABEL_LENGTH; i++) {
/*  471 */       if (arrayOfByte[i] != LABEL[i])
/*  472 */         throw new MissingResourceException("Wrong magic number", paramString, "");
/*  470 */       k++;
/*      */     }
/*      */ 
/*  478 */     if (arrayOfByte[i] != 1) {
/*  479 */       throw new MissingResourceException("Unsupported version(" + arrayOfByte[i] + ")", paramString, "");
/*      */     }
/*      */ 
/*  484 */     j = BreakIterator.getInt(arrayOfByte, ++i);
/*  485 */     arrayOfByte = new byte[j];
/*  486 */     if (localBufferedInputStream.read(arrayOfByte) != j) {
/*  487 */       throw new MissingResourceException("Wrong data length", paramString, "");
/*      */     }
/*      */ 
/*  491 */     localBufferedInputStream.close();
/*      */ 
/*  493 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   byte[] getAdditionalData() {
/*  497 */     return this.additionalData;
/*      */   }
/*      */ 
/*      */   void setAdditionalData(byte[] paramArrayOfByte) {
/*  501 */     this.additionalData = paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  513 */     RuleBasedBreakIterator localRuleBasedBreakIterator = (RuleBasedBreakIterator)super.clone();
/*  514 */     if (this.text != null) {
/*  515 */       localRuleBasedBreakIterator.text = ((CharacterIterator)this.text.clone());
/*      */     }
/*  517 */     return localRuleBasedBreakIterator;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/*  526 */       if (paramObject == null) {
/*  527 */         return false;
/*      */       }
/*      */ 
/*  530 */       RuleBasedBreakIterator localRuleBasedBreakIterator = (RuleBasedBreakIterator)paramObject;
/*  531 */       if (this.checksum != localRuleBasedBreakIterator.checksum) {
/*  532 */         return false;
/*      */       }
/*  534 */       if (this.text == null) {
/*  535 */         return localRuleBasedBreakIterator.text == null;
/*      */       }
/*  537 */       return this.text.equals(localRuleBasedBreakIterator.text);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*      */     }
/*  541 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  549 */     StringBuffer localStringBuffer = new StringBuffer();
/*  550 */     localStringBuffer.append('[');
/*  551 */     localStringBuffer.append("checksum=0x" + Long.toHexString(this.checksum));
/*  552 */     localStringBuffer.append(']');
/*  553 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  561 */     return (int)this.checksum;
/*      */   }
/*      */ 
/*      */   public int first()
/*      */   {
/*  574 */     CharacterIterator localCharacterIterator = getText();
/*      */ 
/*  576 */     localCharacterIterator.first();
/*  577 */     return localCharacterIterator.getIndex();
/*      */   }
/*      */ 
/*      */   public int last()
/*      */   {
/*  586 */     CharacterIterator localCharacterIterator = getText();
/*      */ 
/*  590 */     localCharacterIterator.setIndex(localCharacterIterator.getEndIndex());
/*  591 */     return localCharacterIterator.getIndex();
/*      */   }
/*      */ 
/*      */   public int next(int paramInt)
/*      */   {
/*  604 */     int i = current();
/*  605 */     while (paramInt > 0) {
/*  606 */       i = handleNext();
/*  607 */       paramInt--;
/*      */     }
/*  609 */     while (paramInt < 0) {
/*  610 */       i = previous();
/*  611 */       paramInt++;
/*      */     }
/*  613 */     return i;
/*      */   }
/*      */ 
/*      */   public int next()
/*      */   {
/*  621 */     return handleNext();
/*      */   }
/*      */ 
/*      */   public int previous()
/*      */   {
/*  632 */     CharacterIterator localCharacterIterator = getText();
/*  633 */     if (current() == localCharacterIterator.getBeginIndex()) {
/*  634 */       return -1;
/*      */     }
/*      */ 
/*  642 */     int i = current();
/*  643 */     int j = this.cachedLastKnownBreak;
/*  644 */     if ((j >= i) || (j <= -1)) {
/*  645 */       getPrevious();
/*  646 */       j = handlePrevious();
/*      */     }
/*      */     else
/*      */     {
/*  651 */       localCharacterIterator.setIndex(j);
/*      */     }
/*  653 */     int k = j;
/*      */ 
/*  658 */     while ((k != -1) && (k < i)) {
/*  659 */       j = k;
/*  660 */       k = handleNext();
/*      */     }
/*      */ 
/*  665 */     localCharacterIterator.setIndex(j);
/*  666 */     this.cachedLastKnownBreak = j;
/*  667 */     return j;
/*      */   }
/*      */ 
/*      */   private int getPrevious()
/*      */   {
/*  674 */     char c1 = this.text.previous();
/*  675 */     if ((Character.isLowSurrogate(c1)) && (this.text.getIndex() > this.text.getBeginIndex()))
/*      */     {
/*  677 */       char c2 = this.text.previous();
/*  678 */       if (Character.isHighSurrogate(c2)) {
/*  679 */         return Character.toCodePoint(c2, c1);
/*      */       }
/*  681 */       this.text.next();
/*      */     }
/*      */ 
/*  684 */     return c1;
/*      */   }
/*      */ 
/*      */   int getCurrent()
/*      */   {
/*  691 */     char c1 = this.text.current();
/*  692 */     if ((Character.isHighSurrogate(c1)) && (this.text.getIndex() < this.text.getEndIndex()))
/*      */     {
/*  694 */       char c2 = this.text.next();
/*  695 */       this.text.previous();
/*  696 */       if (Character.isLowSurrogate(c2)) {
/*  697 */         return Character.toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*  700 */     return c1;
/*      */   }
/*      */ 
/*      */   private int getCurrentCodePointCount()
/*      */   {
/*  707 */     char c1 = this.text.current();
/*  708 */     if ((Character.isHighSurrogate(c1)) && (this.text.getIndex() < this.text.getEndIndex()))
/*      */     {
/*  710 */       char c2 = this.text.next();
/*  711 */       this.text.previous();
/*  712 */       if (Character.isLowSurrogate(c2)) {
/*  713 */         return 2;
/*      */       }
/*      */     }
/*  716 */     return 1;
/*      */   }
/*      */ 
/*      */   int getNext()
/*      */   {
/*  723 */     int i = this.text.getIndex();
/*  724 */     int j = this.text.getEndIndex();
/*  725 */     if ((i == j) || (i += getCurrentCodePointCount() >= j))
/*      */     {
/*  727 */       return 65535;
/*      */     }
/*  729 */     this.text.setIndex(i);
/*  730 */     return getCurrent();
/*      */   }
/*      */ 
/*      */   private int getNextIndex()
/*      */   {
/*  737 */     int i = this.text.getIndex() + getCurrentCodePointCount();
/*  738 */     int j = this.text.getEndIndex();
/*  739 */     if (i > j) {
/*  740 */       return j;
/*      */     }
/*  742 */     return i;
/*      */   }
/*      */ 
/*      */   protected static final void checkOffset(int paramInt, CharacterIterator paramCharacterIterator)
/*      */   {
/*  750 */     if ((paramInt < paramCharacterIterator.getBeginIndex()) || (paramInt > paramCharacterIterator.getEndIndex()))
/*  751 */       throw new IllegalArgumentException("offset out of bounds");
/*      */   }
/*      */ 
/*      */   public int following(int paramInt)
/*      */   {
/*  763 */     CharacterIterator localCharacterIterator = getText();
/*  764 */     checkOffset(paramInt, localCharacterIterator);
/*      */ 
/*  769 */     localCharacterIterator.setIndex(paramInt);
/*  770 */     if (paramInt == localCharacterIterator.getBeginIndex()) {
/*  771 */       this.cachedLastKnownBreak = handleNext();
/*  772 */       return this.cachedLastKnownBreak;
/*      */     }
/*      */ 
/*  782 */     int i = this.cachedLastKnownBreak;
/*  783 */     if ((i >= paramInt) || (i <= -1)) {
/*  784 */       i = handlePrevious();
/*      */     }
/*      */     else
/*      */     {
/*  789 */       localCharacterIterator.setIndex(i);
/*      */     }
/*  791 */     while ((i != -1) && (i <= paramInt)) {
/*  792 */       i = handleNext();
/*      */     }
/*  794 */     this.cachedLastKnownBreak = i;
/*  795 */     return i;
/*      */   }
/*      */ 
/*      */   public int preceding(int paramInt)
/*      */   {
/*  808 */     CharacterIterator localCharacterIterator = getText();
/*  809 */     checkOffset(paramInt, localCharacterIterator);
/*  810 */     localCharacterIterator.setIndex(paramInt);
/*  811 */     return previous();
/*      */   }
/*      */ 
/*      */   public boolean isBoundary(int paramInt)
/*      */   {
/*  822 */     CharacterIterator localCharacterIterator = getText();
/*  823 */     checkOffset(paramInt, localCharacterIterator);
/*  824 */     if (paramInt == localCharacterIterator.getBeginIndex()) {
/*  825 */       return true;
/*      */     }
/*      */ 
/*  832 */     return following(paramInt - 1) == paramInt;
/*      */   }
/*      */ 
/*      */   public int current()
/*      */   {
/*  841 */     return getText().getIndex();
/*      */   }
/*      */ 
/*      */   public CharacterIterator getText()
/*      */   {
/*  855 */     if (this.text == null) {
/*  856 */       this.text = new StringCharacterIterator("");
/*      */     }
/*  858 */     return this.text;
/*      */   }
/*      */ 
/*      */   public void setText(CharacterIterator paramCharacterIterator)
/*      */   {
/*  872 */     int i = paramCharacterIterator.getEndIndex();
/*      */     int j;
/*      */     try
/*      */     {
/*  875 */       paramCharacterIterator.setIndex(i);
/*  876 */       j = paramCharacterIterator.getIndex() == i ? 1 : 0;
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/*  879 */       j = 0;
/*      */     }
/*      */ 
/*  882 */     if (j != 0) {
/*  883 */       this.text = paramCharacterIterator;
/*      */     }
/*      */     else {
/*  886 */       this.text = new SafeCharIterator(paramCharacterIterator);
/*      */     }
/*  888 */     this.text.first();
/*      */ 
/*  890 */     this.cachedLastKnownBreak = -1;
/*      */   }
/*      */ 
/*      */   protected int handleNext()
/*      */   {
/*  907 */     CharacterIterator localCharacterIterator = getText();
/*  908 */     if (localCharacterIterator.getIndex() == localCharacterIterator.getEndIndex()) {
/*  909 */       return -1;
/*      */     }
/*      */ 
/*  913 */     int i = getNextIndex();
/*  914 */     int j = 0;
/*      */ 
/*  917 */     int k = 1;
/*      */ 
/*  919 */     int n = getCurrent();
/*      */ 
/*  922 */     while ((n != 65535) && (k != 0))
/*      */     {
/*  926 */       int m = lookupCategory(n);
/*      */ 
/*  930 */       if (m != -1) {
/*  931 */         k = lookupState(k, m);
/*      */       }
/*      */ 
/*  938 */       if (this.lookaheadStates[k] != 0) {
/*  939 */         if (this.endStates[k] != 0) {
/*  940 */           i = j;
/*      */         }
/*      */         else {
/*  943 */           j = getNextIndex();
/*      */         }
/*      */ 
/*      */       }
/*  950 */       else if (this.endStates[k] != 0) {
/*  951 */         i = getNextIndex();
/*      */       }
/*      */ 
/*  955 */       n = getNext();
/*      */     }
/*      */ 
/*  962 */     if ((n == 65535) && (j == localCharacterIterator.getEndIndex())) {
/*  963 */       i = j;
/*      */     }
/*      */ 
/*  966 */     localCharacterIterator.setIndex(i);
/*  967 */     return i;
/*      */   }
/*      */ 
/*      */   protected int handlePrevious()
/*      */   {
/*  978 */     CharacterIterator localCharacterIterator = getText();
/*  979 */     int i = 1;
/*  980 */     int j = 0;
/*  981 */     int k = 0;
/*  982 */     int m = getCurrent();
/*      */ 
/*  985 */     while ((m != 65535) && (i != 0))
/*      */     {
/*  989 */       k = j;
/*  990 */       j = lookupCategory(m);
/*      */ 
/*  994 */       if (j != -1) {
/*  995 */         i = lookupBackwardState(i, j);
/*      */       }
/*      */ 
/*  999 */       m = getPrevious();
/*      */     }
/*      */ 
/* 1007 */     if (m != 65535) {
/* 1008 */       if (k != -1) {
/* 1009 */         getNext();
/* 1010 */         getNext();
/*      */       }
/*      */       else {
/* 1013 */         getNext();
/*      */       }
/*      */     }
/* 1016 */     return localCharacterIterator.getIndex();
/*      */   }
/*      */ 
/*      */   protected int lookupCategory(int paramInt)
/*      */   {
/* 1024 */     if (paramInt < 65536) {
/* 1025 */       return this.charCategoryTable.elementAt((char)paramInt);
/*      */     }
/* 1027 */     return this.supplementaryCharCategoryTable.getValue(paramInt);
/*      */   }
/*      */ 
/*      */   protected int lookupState(int paramInt1, int paramInt2)
/*      */   {
/* 1036 */     return this.stateTable[(paramInt1 * this.numCategories + paramInt2)];
/*      */   }
/*      */ 
/*      */   protected int lookupBackwardState(int paramInt1, int paramInt2)
/*      */   {
/* 1044 */     return this.backwardsStateTable[(paramInt1 * this.numCategories + paramInt2)];
/*      */   }
/*      */ 
/*      */   private static final class SafeCharIterator
/*      */     implements CharacterIterator, Cloneable
/*      */   {
/*      */     private CharacterIterator base;
/*      */     private int rangeStart;
/*      */     private int rangeLimit;
/*      */     private int currentIndex;
/*      */ 
/*      */     SafeCharIterator(CharacterIterator paramCharacterIterator)
/*      */     {
/* 1065 */       this.base = paramCharacterIterator;
/* 1066 */       this.rangeStart = paramCharacterIterator.getBeginIndex();
/* 1067 */       this.rangeLimit = paramCharacterIterator.getEndIndex();
/* 1068 */       this.currentIndex = paramCharacterIterator.getIndex();
/*      */     }
/*      */ 
/*      */     public char first() {
/* 1072 */       return setIndex(this.rangeStart);
/*      */     }
/*      */ 
/*      */     public char last() {
/* 1076 */       return setIndex(this.rangeLimit - 1);
/*      */     }
/*      */ 
/*      */     public char current() {
/* 1080 */       if ((this.currentIndex < this.rangeStart) || (this.currentIndex >= this.rangeLimit)) {
/* 1081 */         return 65535;
/*      */       }
/*      */ 
/* 1084 */       return this.base.setIndex(this.currentIndex);
/*      */     }
/*      */ 
/*      */     public char next()
/*      */     {
/* 1090 */       this.currentIndex += 1;
/* 1091 */       if (this.currentIndex >= this.rangeLimit) {
/* 1092 */         this.currentIndex = this.rangeLimit;
/* 1093 */         return 65535;
/*      */       }
/*      */ 
/* 1096 */       return this.base.setIndex(this.currentIndex);
/*      */     }
/*      */ 
/*      */     public char previous()
/*      */     {
/* 1102 */       this.currentIndex -= 1;
/* 1103 */       if (this.currentIndex < this.rangeStart) {
/* 1104 */         this.currentIndex = this.rangeStart;
/* 1105 */         return 65535;
/*      */       }
/*      */ 
/* 1108 */       return this.base.setIndex(this.currentIndex);
/*      */     }
/*      */ 
/*      */     public char setIndex(int paramInt)
/*      */     {
/* 1114 */       if ((paramInt < this.rangeStart) || (paramInt > this.rangeLimit)) {
/* 1115 */         throw new IllegalArgumentException("Invalid position");
/*      */       }
/* 1117 */       this.currentIndex = paramInt;
/* 1118 */       return current();
/*      */     }
/*      */ 
/*      */     public int getBeginIndex() {
/* 1122 */       return this.rangeStart;
/*      */     }
/*      */ 
/*      */     public int getEndIndex() {
/* 1126 */       return this.rangeLimit;
/*      */     }
/*      */ 
/*      */     public int getIndex() {
/* 1130 */       return this.currentIndex;
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/* 1135 */       SafeCharIterator localSafeCharIterator = null;
/*      */       try {
/* 1137 */         localSafeCharIterator = (SafeCharIterator)super.clone();
/*      */       }
/*      */       catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 1140 */         throw new Error("Clone not supported: " + localCloneNotSupportedException);
/*      */       }
/*      */ 
/* 1143 */       CharacterIterator localCharacterIterator = (Cloneable)this.base.clone();
/* 1144 */       localSafeCharIterator.base = localCharacterIterator;
/* 1145 */       return localSafeCharIterator;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.RuleBasedBreakIterator
 * JD-Core Version:    0.6.2
 */