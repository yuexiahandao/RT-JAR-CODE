/*      */ package com.sun.org.apache.xml.internal.utils;
/*      */ 
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class FastStringBuffer
/*      */ {
/*      */   static final int DEBUG_FORCE_INIT_BITS = 0;
/*      */   static final boolean DEBUG_FORCE_FIXED_CHUNKSIZE = true;
/*      */   public static final int SUPPRESS_LEADING_WS = 1;
/*      */   public static final int SUPPRESS_TRAILING_WS = 2;
/*      */   public static final int SUPPRESS_BOTH = 3;
/*      */   private static final int CARRY_WS = 4;
/*  103 */   int m_chunkBits = 15;
/*      */ 
/*  110 */   int m_maxChunkBits = 15;
/*      */ 
/*  120 */   int m_rebundleBits = 2;
/*      */   int m_chunkSize;
/*      */   int m_chunkMask;
/*      */   char[][] m_array;
/*  155 */   int m_lastChunk = 0;
/*      */ 
/*  164 */   int m_firstFree = 0;
/*      */ 
/*  173 */   FastStringBuffer m_innerFSB = null;
/*      */ 
/* 1085 */   static final char[] SINGLE_SPACE = { ' ' };
/*      */ 
/*      */   public FastStringBuffer(int initChunkBits, int maxChunkBits, int rebundleBits)
/*      */   {
/*  209 */     maxChunkBits = initChunkBits;
/*      */ 
/*  212 */     this.m_array = new char[16][];
/*      */ 
/*  215 */     if (initChunkBits > maxChunkBits) {
/*  216 */       initChunkBits = maxChunkBits;
/*      */     }
/*  218 */     this.m_chunkBits = initChunkBits;
/*  219 */     this.m_maxChunkBits = maxChunkBits;
/*  220 */     this.m_rebundleBits = rebundleBits;
/*  221 */     this.m_chunkSize = (1 << initChunkBits);
/*  222 */     this.m_chunkMask = (this.m_chunkSize - 1);
/*  223 */     this.m_array[0] = new char[this.m_chunkSize];
/*      */   }
/*      */ 
/*      */   public FastStringBuffer(int initChunkBits, int maxChunkBits)
/*      */   {
/*  234 */     this(initChunkBits, maxChunkBits, 2);
/*      */   }
/*      */ 
/*      */   public FastStringBuffer(int initChunkBits)
/*      */   {
/*  248 */     this(initChunkBits, 15, 2);
/*      */   }
/*      */ 
/*      */   public FastStringBuffer()
/*      */   {
/*  262 */     this(10, 15, 2);
/*      */   }
/*      */ 
/*      */   public final int size()
/*      */   {
/*  272 */     return (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
/*      */   }
/*      */ 
/*      */   public final int length()
/*      */   {
/*  282 */     return (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
/*      */   }
/*      */ 
/*      */   public final void reset()
/*      */   {
/*  293 */     this.m_lastChunk = 0;
/*  294 */     this.m_firstFree = 0;
/*      */ 
/*  297 */     FastStringBuffer innermost = this;
/*      */ 
/*  299 */     while (innermost.m_innerFSB != null)
/*      */     {
/*  301 */       innermost = innermost.m_innerFSB;
/*      */     }
/*      */ 
/*  304 */     this.m_chunkBits = innermost.m_chunkBits;
/*  305 */     this.m_chunkSize = innermost.m_chunkSize;
/*  306 */     this.m_chunkMask = innermost.m_chunkMask;
/*      */ 
/*  309 */     this.m_innerFSB = null;
/*  310 */     this.m_array = new char[16][0];
/*  311 */     this.m_array[0] = new char[this.m_chunkSize];
/*      */   }
/*      */ 
/*      */   public final void setLength(int l)
/*      */   {
/*  328 */     this.m_lastChunk = (l >>> this.m_chunkBits);
/*      */ 
/*  330 */     if ((this.m_lastChunk == 0) && (this.m_innerFSB != null))
/*      */     {
/*  333 */       this.m_innerFSB.setLength(l, this);
/*      */     }
/*      */     else
/*      */     {
/*  337 */       this.m_firstFree = (l & this.m_chunkMask);
/*      */ 
/*  344 */       if ((this.m_firstFree == 0) && (this.m_lastChunk > 0))
/*      */       {
/*  346 */         this.m_lastChunk -= 1;
/*  347 */         this.m_firstFree = this.m_chunkSize;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void setLength(int l, FastStringBuffer rootFSB)
/*      */   {
/*  362 */     this.m_lastChunk = (l >>> this.m_chunkBits);
/*      */ 
/*  364 */     if ((this.m_lastChunk == 0) && (this.m_innerFSB != null))
/*      */     {
/*  366 */       this.m_innerFSB.setLength(l, rootFSB);
/*      */     }
/*      */     else
/*      */     {
/*  373 */       rootFSB.m_chunkBits = this.m_chunkBits;
/*  374 */       rootFSB.m_maxChunkBits = this.m_maxChunkBits;
/*  375 */       rootFSB.m_rebundleBits = this.m_rebundleBits;
/*  376 */       rootFSB.m_chunkSize = this.m_chunkSize;
/*  377 */       rootFSB.m_chunkMask = this.m_chunkMask;
/*  378 */       rootFSB.m_array = this.m_array;
/*  379 */       rootFSB.m_innerFSB = this.m_innerFSB;
/*  380 */       rootFSB.m_lastChunk = this.m_lastChunk;
/*      */ 
/*  383 */       rootFSB.m_firstFree = (l & this.m_chunkMask);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final String toString()
/*      */   {
/*  403 */     int length = (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
/*      */ 
/*  405 */     return getString(new StringBuffer(length), 0, 0, length).toString();
/*      */   }
/*      */ 
/*      */   public final void append(char value)
/*      */   {
/*  425 */     boolean lastchunk = this.m_lastChunk + 1 == this.m_array.length;
/*      */     char[] chunk;
/*      */     char[] chunk;
/*  427 */     if (this.m_firstFree < this.m_chunkSize) {
/*  428 */       chunk = this.m_array[this.m_lastChunk];
/*      */     }
/*      */     else
/*      */     {
/*  433 */       int i = this.m_array.length;
/*      */ 
/*  435 */       if (this.m_lastChunk + 1 == i)
/*      */       {
/*  437 */         char[][] newarray = new char[i + 16][];
/*      */ 
/*  439 */         System.arraycopy(this.m_array, 0, newarray, 0, i);
/*      */ 
/*  441 */         this.m_array = newarray;
/*      */       }
/*      */ 
/*  445 */       chunk = this.m_array[(++this.m_lastChunk)];
/*      */ 
/*  447 */       if (chunk == null)
/*      */       {
/*  451 */         if ((this.m_lastChunk == 1 << this.m_rebundleBits) && (this.m_chunkBits < this.m_maxChunkBits))
/*      */         {
/*  457 */           this.m_innerFSB = new FastStringBuffer(this);
/*      */         }
/*      */ 
/*  461 */         chunk = this.m_array[this.m_lastChunk] =  = new char[this.m_chunkSize];
/*      */       }
/*      */ 
/*  464 */       this.m_firstFree = 0;
/*      */     }
/*      */ 
/*  468 */     chunk[(this.m_firstFree++)] = value;
/*      */   }
/*      */ 
/*      */   public final void append(String value)
/*      */   {
/*  483 */     if (value == null)
/*  484 */       return;
/*  485 */     int strlen = value.length();
/*      */ 
/*  487 */     if (0 == strlen) {
/*  488 */       return;
/*      */     }
/*  490 */     int copyfrom = 0;
/*  491 */     char[] chunk = this.m_array[this.m_lastChunk];
/*  492 */     int available = this.m_chunkSize - this.m_firstFree;
/*      */ 
/*  495 */     while (strlen > 0)
/*      */     {
/*  499 */       if (available > strlen) {
/*  500 */         available = strlen;
/*      */       }
/*  502 */       value.getChars(copyfrom, copyfrom + available, this.m_array[this.m_lastChunk], this.m_firstFree);
/*      */ 
/*  505 */       strlen -= available;
/*  506 */       copyfrom += available;
/*      */ 
/*  509 */       if (strlen > 0)
/*      */       {
/*  513 */         int i = this.m_array.length;
/*      */ 
/*  515 */         if (this.m_lastChunk + 1 == i)
/*      */         {
/*  517 */           char[][] newarray = new char[i + 16][];
/*      */ 
/*  519 */           System.arraycopy(this.m_array, 0, newarray, 0, i);
/*      */ 
/*  521 */           this.m_array = newarray;
/*      */         }
/*      */ 
/*  525 */         chunk = this.m_array[(++this.m_lastChunk)];
/*      */ 
/*  527 */         if (chunk == null)
/*      */         {
/*  531 */           if ((this.m_lastChunk == 1 << this.m_rebundleBits) && (this.m_chunkBits < this.m_maxChunkBits))
/*      */           {
/*  537 */             this.m_innerFSB = new FastStringBuffer(this);
/*      */           }
/*      */ 
/*  541 */           chunk = this.m_array[this.m_lastChunk] =  = new char[this.m_chunkSize];
/*      */         }
/*      */ 
/*  544 */         available = this.m_chunkSize;
/*  545 */         this.m_firstFree = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  550 */     this.m_firstFree += available;
/*      */   }
/*      */ 
/*      */   public final void append(StringBuffer value)
/*      */   {
/*  565 */     if (value == null)
/*  566 */       return;
/*  567 */     int strlen = value.length();
/*      */ 
/*  569 */     if (0 == strlen) {
/*  570 */       return;
/*      */     }
/*  572 */     int copyfrom = 0;
/*  573 */     char[] chunk = this.m_array[this.m_lastChunk];
/*  574 */     int available = this.m_chunkSize - this.m_firstFree;
/*      */ 
/*  577 */     while (strlen > 0)
/*      */     {
/*  581 */       if (available > strlen) {
/*  582 */         available = strlen;
/*      */       }
/*  584 */       value.getChars(copyfrom, copyfrom + available, this.m_array[this.m_lastChunk], this.m_firstFree);
/*      */ 
/*  587 */       strlen -= available;
/*  588 */       copyfrom += available;
/*      */ 
/*  591 */       if (strlen > 0)
/*      */       {
/*  595 */         int i = this.m_array.length;
/*      */ 
/*  597 */         if (this.m_lastChunk + 1 == i)
/*      */         {
/*  599 */           char[][] newarray = new char[i + 16][];
/*      */ 
/*  601 */           System.arraycopy(this.m_array, 0, newarray, 0, i);
/*      */ 
/*  603 */           this.m_array = newarray;
/*      */         }
/*      */ 
/*  607 */         chunk = this.m_array[(++this.m_lastChunk)];
/*      */ 
/*  609 */         if (chunk == null)
/*      */         {
/*  613 */           if ((this.m_lastChunk == 1 << this.m_rebundleBits) && (this.m_chunkBits < this.m_maxChunkBits))
/*      */           {
/*  619 */             this.m_innerFSB = new FastStringBuffer(this);
/*      */           }
/*      */ 
/*  623 */           chunk = this.m_array[this.m_lastChunk] =  = new char[this.m_chunkSize];
/*      */         }
/*      */ 
/*  626 */         available = this.m_chunkSize;
/*  627 */         this.m_firstFree = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  632 */     this.m_firstFree += available;
/*      */   }
/*      */ 
/*      */   public final void append(char[] chars, int start, int length)
/*      */   {
/*  650 */     int strlen = length;
/*      */ 
/*  652 */     if (0 == strlen) {
/*  653 */       return;
/*      */     }
/*  655 */     int copyfrom = start;
/*  656 */     char[] chunk = this.m_array[this.m_lastChunk];
/*  657 */     int available = this.m_chunkSize - this.m_firstFree;
/*      */ 
/*  660 */     while (strlen > 0)
/*      */     {
/*  664 */       if (available > strlen) {
/*  665 */         available = strlen;
/*      */       }
/*  667 */       System.arraycopy(chars, copyfrom, this.m_array[this.m_lastChunk], this.m_firstFree, available);
/*      */ 
/*  670 */       strlen -= available;
/*  671 */       copyfrom += available;
/*      */ 
/*  674 */       if (strlen > 0)
/*      */       {
/*  678 */         int i = this.m_array.length;
/*      */ 
/*  680 */         if (this.m_lastChunk + 1 == i)
/*      */         {
/*  682 */           char[][] newarray = new char[i + 16][];
/*      */ 
/*  684 */           System.arraycopy(this.m_array, 0, newarray, 0, i);
/*      */ 
/*  686 */           this.m_array = newarray;
/*      */         }
/*      */ 
/*  690 */         chunk = this.m_array[(++this.m_lastChunk)];
/*      */ 
/*  692 */         if (chunk == null)
/*      */         {
/*  696 */           if ((this.m_lastChunk == 1 << this.m_rebundleBits) && (this.m_chunkBits < this.m_maxChunkBits))
/*      */           {
/*  702 */             this.m_innerFSB = new FastStringBuffer(this);
/*      */           }
/*      */ 
/*  706 */           chunk = this.m_array[this.m_lastChunk] =  = new char[this.m_chunkSize];
/*      */         }
/*      */ 
/*  709 */         available = this.m_chunkSize;
/*  710 */         this.m_firstFree = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  715 */     this.m_firstFree += available;
/*      */   }
/*      */ 
/*      */   public final void append(FastStringBuffer value)
/*      */   {
/*  735 */     if (value == null)
/*  736 */       return;
/*  737 */     int strlen = value.length();
/*      */ 
/*  739 */     if (0 == strlen) {
/*  740 */       return;
/*      */     }
/*  742 */     int copyfrom = 0;
/*  743 */     char[] chunk = this.m_array[this.m_lastChunk];
/*  744 */     int available = this.m_chunkSize - this.m_firstFree;
/*      */ 
/*  747 */     while (strlen > 0)
/*      */     {
/*  751 */       if (available > strlen) {
/*  752 */         available = strlen;
/*      */       }
/*  754 */       int sourcechunk = copyfrom + value.m_chunkSize - 1 >>> value.m_chunkBits;
/*      */ 
/*  756 */       int sourcecolumn = copyfrom & value.m_chunkMask;
/*  757 */       int runlength = value.m_chunkSize - sourcecolumn;
/*      */ 
/*  759 */       if (runlength > available) {
/*  760 */         runlength = available;
/*      */       }
/*  762 */       System.arraycopy(value.m_array[sourcechunk], sourcecolumn, this.m_array[this.m_lastChunk], this.m_firstFree, runlength);
/*      */ 
/*  765 */       if (runlength != available) {
/*  766 */         System.arraycopy(value.m_array[(sourcechunk + 1)], 0, this.m_array[this.m_lastChunk], this.m_firstFree + runlength, available - runlength);
/*      */       }
/*      */ 
/*  770 */       strlen -= available;
/*  771 */       copyfrom += available;
/*      */ 
/*  774 */       if (strlen > 0)
/*      */       {
/*  778 */         int i = this.m_array.length;
/*      */ 
/*  780 */         if (this.m_lastChunk + 1 == i)
/*      */         {
/*  782 */           char[][] newarray = new char[i + 16][];
/*      */ 
/*  784 */           System.arraycopy(this.m_array, 0, newarray, 0, i);
/*      */ 
/*  786 */           this.m_array = newarray;
/*      */         }
/*      */ 
/*  790 */         chunk = this.m_array[(++this.m_lastChunk)];
/*      */ 
/*  792 */         if (chunk == null)
/*      */         {
/*  796 */           if ((this.m_lastChunk == 1 << this.m_rebundleBits) && (this.m_chunkBits < this.m_maxChunkBits))
/*      */           {
/*  802 */             this.m_innerFSB = new FastStringBuffer(this);
/*      */           }
/*      */ 
/*  806 */           chunk = this.m_array[this.m_lastChunk] =  = new char[this.m_chunkSize];
/*      */         }
/*      */ 
/*  809 */         available = this.m_chunkSize;
/*  810 */         this.m_firstFree = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  815 */     this.m_firstFree += available;
/*      */   }
/*      */ 
/*      */   public boolean isWhitespace(int start, int length)
/*      */   {
/*  830 */     int sourcechunk = start >>> this.m_chunkBits;
/*  831 */     int sourcecolumn = start & this.m_chunkMask;
/*  832 */     int available = this.m_chunkSize - sourcecolumn;
/*      */ 
/*  835 */     while (length > 0)
/*      */     {
/*  837 */       int runlength = length <= available ? length : available;
/*      */       boolean chunkOK;
/*      */       boolean chunkOK;
/*  839 */       if ((sourcechunk == 0) && (this.m_innerFSB != null))
/*  840 */         chunkOK = this.m_innerFSB.isWhitespace(sourcecolumn, runlength);
/*      */       else {
/*  842 */         chunkOK = XMLCharacterRecognizer.isWhiteSpace(this.m_array[sourcechunk], sourcecolumn, runlength);
/*      */       }
/*      */ 
/*  845 */       if (!chunkOK) {
/*  846 */         return false;
/*      */       }
/*  848 */       length -= runlength;
/*      */ 
/*  850 */       sourcechunk++;
/*      */ 
/*  852 */       sourcecolumn = 0;
/*  853 */       available = this.m_chunkSize;
/*      */     }
/*      */ 
/*  856 */     return true;
/*      */   }
/*      */ 
/*      */   public String getString(int start, int length)
/*      */   {
/*  867 */     int startColumn = start & this.m_chunkMask;
/*  868 */     int startChunk = start >>> this.m_chunkBits;
/*  869 */     if ((startColumn + length < this.m_chunkMask) && (this.m_innerFSB == null)) {
/*  870 */       return getOneChunkString(startChunk, startColumn, length);
/*      */     }
/*  872 */     return getString(new StringBuffer(length), startChunk, startColumn, length).toString();
/*      */   }
/*      */ 
/*      */   protected String getOneChunkString(int startChunk, int startColumn, int length)
/*      */   {
/*  878 */     return new String(this.m_array[startChunk], startColumn, length);
/*      */   }
/*      */ 
/*      */   StringBuffer getString(StringBuffer sb, int start, int length)
/*      */   {
/*  889 */     return getString(sb, start >>> this.m_chunkBits, start & this.m_chunkMask, length);
/*      */   }
/*      */ 
/*      */   StringBuffer getString(StringBuffer sb, int startChunk, int startColumn, int length)
/*      */   {
/*  920 */     int stop = (startChunk << this.m_chunkBits) + startColumn + length;
/*  921 */     int stopChunk = stop >>> this.m_chunkBits;
/*  922 */     int stopColumn = stop & this.m_chunkMask;
/*      */ 
/*  926 */     for (int i = startChunk; i < stopChunk; i++)
/*      */     {
/*  928 */       if ((i == 0) && (this.m_innerFSB != null))
/*  929 */         this.m_innerFSB.getString(sb, startColumn, this.m_chunkSize - startColumn);
/*      */       else {
/*  931 */         sb.append(this.m_array[i], startColumn, this.m_chunkSize - startColumn);
/*      */       }
/*  933 */       startColumn = 0;
/*      */     }
/*      */ 
/*  936 */     if ((stopChunk == 0) && (this.m_innerFSB != null))
/*  937 */       this.m_innerFSB.getString(sb, startColumn, stopColumn - startColumn);
/*  938 */     else if (stopColumn > startColumn) {
/*  939 */       sb.append(this.m_array[stopChunk], startColumn, stopColumn - startColumn);
/*      */     }
/*  941 */     return sb;
/*      */   }
/*      */ 
/*      */   public char charAt(int pos)
/*      */   {
/*  953 */     int startChunk = pos >>> this.m_chunkBits;
/*      */ 
/*  955 */     if ((startChunk == 0) && (this.m_innerFSB != null)) {
/*  956 */       return this.m_innerFSB.charAt(pos & this.m_chunkMask);
/*      */     }
/*  958 */     return this.m_array[startChunk][(pos & this.m_chunkMask)];
/*      */   }
/*      */ 
/*      */   public void sendSAXcharacters(ContentHandler ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  985 */     int startChunk = start >>> this.m_chunkBits;
/*  986 */     int startColumn = start & this.m_chunkMask;
/*  987 */     if ((startColumn + length < this.m_chunkMask) && (this.m_innerFSB == null)) {
/*  988 */       ch.characters(this.m_array[startChunk], startColumn, length);
/*  989 */       return;
/*      */     }
/*      */ 
/*  992 */     int stop = start + length;
/*  993 */     int stopChunk = stop >>> this.m_chunkBits;
/*  994 */     int stopColumn = stop & this.m_chunkMask;
/*      */ 
/*  996 */     for (int i = startChunk; i < stopChunk; i++)
/*      */     {
/*  998 */       if ((i == 0) && (this.m_innerFSB != null)) {
/*  999 */         this.m_innerFSB.sendSAXcharacters(ch, startColumn, this.m_chunkSize - startColumn);
/*      */       }
/*      */       else {
/* 1002 */         ch.characters(this.m_array[i], startColumn, this.m_chunkSize - startColumn);
/*      */       }
/* 1004 */       startColumn = 0;
/*      */     }
/*      */ 
/* 1008 */     if ((stopChunk == 0) && (this.m_innerFSB != null))
/* 1009 */       this.m_innerFSB.sendSAXcharacters(ch, startColumn, stopColumn - startColumn);
/* 1010 */     else if (stopColumn > startColumn)
/*      */     {
/* 1012 */       ch.characters(this.m_array[stopChunk], startColumn, stopColumn - startColumn);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int sendNormalizedSAXcharacters(ContentHandler ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1048 */     int stateForNextChunk = 1;
/*      */ 
/* 1050 */     int stop = start + length;
/* 1051 */     int startChunk = start >>> this.m_chunkBits;
/* 1052 */     int startColumn = start & this.m_chunkMask;
/* 1053 */     int stopChunk = stop >>> this.m_chunkBits;
/* 1054 */     int stopColumn = stop & this.m_chunkMask;
/*      */ 
/* 1056 */     for (int i = startChunk; i < stopChunk; i++)
/*      */     {
/* 1058 */       if ((i == 0) && (this.m_innerFSB != null)) {
/* 1059 */         stateForNextChunk = this.m_innerFSB.sendNormalizedSAXcharacters(ch, startColumn, this.m_chunkSize - startColumn);
/*      */       }
/*      */       else
/*      */       {
/* 1063 */         stateForNextChunk = sendNormalizedSAXcharacters(this.m_array[i], startColumn, this.m_chunkSize - startColumn, ch, stateForNextChunk);
/*      */       }
/*      */ 
/* 1068 */       startColumn = 0;
/*      */     }
/*      */ 
/* 1072 */     if ((stopChunk == 0) && (this.m_innerFSB != null)) {
/* 1073 */       stateForNextChunk = this.m_innerFSB.sendNormalizedSAXcharacters(ch, startColumn, stopColumn - startColumn);
/*      */     }
/* 1075 */     else if (stopColumn > startColumn)
/*      */     {
/* 1077 */       stateForNextChunk = sendNormalizedSAXcharacters(this.m_array[stopChunk], startColumn, stopColumn - startColumn, ch, stateForNextChunk | 0x2);
/*      */     }
/*      */ 
/* 1082 */     return stateForNextChunk;
/*      */   }
/*      */ 
/*      */   static int sendNormalizedSAXcharacters(char[] ch, int start, int length, ContentHandler handler, int edgeTreatmentFlags)
/*      */     throws SAXException
/*      */   {
/* 1136 */     boolean processingLeadingWhitespace = (edgeTreatmentFlags & 0x1) != 0;
/*      */ 
/* 1138 */     boolean seenWhitespace = (edgeTreatmentFlags & 0x4) != 0;
/* 1139 */     boolean suppressTrailingWhitespace = (edgeTreatmentFlags & 0x2) != 0;
/*      */ 
/* 1141 */     int currPos = start;
/* 1142 */     int limit = start + length;
/*      */ 
/* 1145 */     if (processingLeadingWhitespace)
/*      */     {
/* 1147 */       while ((currPos < limit) && (XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
/* 1148 */         currPos++;
/*      */       }
/*      */ 
/* 1152 */       if (currPos == limit) {
/* 1153 */         return edgeTreatmentFlags;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1158 */     while (currPos < limit) {
/* 1159 */       int startNonWhitespace = currPos;
/*      */ 
/* 1163 */       while ((currPos < limit) && (!XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
/* 1164 */         currPos++;
/*      */       }
/*      */ 
/* 1168 */       if (startNonWhitespace != currPos) {
/* 1169 */         if (seenWhitespace) {
/* 1170 */           handler.characters(SINGLE_SPACE, 0, 1);
/* 1171 */           seenWhitespace = false;
/*      */         }
/* 1173 */         handler.characters(ch, startNonWhitespace, currPos - startNonWhitespace);
/*      */       }
/*      */ 
/* 1177 */       int startWhitespace = currPos;
/*      */ 
/* 1181 */       while ((currPos < limit) && (XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
/* 1182 */         currPos++;
/*      */       }
/* 1184 */       if (startWhitespace != currPos) {
/* 1185 */         seenWhitespace = true;
/*      */       }
/*      */     }
/*      */ 
/* 1189 */     return (seenWhitespace ? 4 : 0) | edgeTreatmentFlags & 0x2;
/*      */   }
/*      */ 
/*      */   public static void sendNormalizedSAXcharacters(char[] ch, int start, int length, ContentHandler handler)
/*      */     throws SAXException
/*      */   {
/* 1208 */     sendNormalizedSAXcharacters(ch, start, length, handler, 3);
/*      */   }
/*      */ 
/*      */   public void sendSAXComment(LexicalHandler ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1230 */     String comment = getString(start, length);
/* 1231 */     ch.comment(comment.toCharArray(), 0, length);
/*      */   }
/*      */ 
/*      */   private void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
/*      */   {
/*      */   }
/*      */ 
/*      */   private FastStringBuffer(FastStringBuffer source)
/*      */   {
/* 1272 */     this.m_chunkBits = source.m_chunkBits;
/* 1273 */     this.m_maxChunkBits = source.m_maxChunkBits;
/* 1274 */     this.m_rebundleBits = source.m_rebundleBits;
/* 1275 */     this.m_chunkSize = source.m_chunkSize;
/* 1276 */     this.m_chunkMask = source.m_chunkMask;
/* 1277 */     this.m_array = source.m_array;
/* 1278 */     this.m_innerFSB = source.m_innerFSB;
/*      */ 
/* 1282 */     source.m_lastChunk -= 1;
/* 1283 */     this.m_firstFree = source.m_chunkSize;
/*      */ 
/* 1286 */     source.m_array = new char[16][];
/* 1287 */     source.m_innerFSB = this;
/*      */ 
/* 1292 */     source.m_lastChunk = 1;
/* 1293 */     source.m_firstFree = 0;
/* 1294 */     source.m_chunkBits += this.m_rebundleBits;
/* 1295 */     source.m_chunkSize = (1 << source.m_chunkBits);
/* 1296 */     source.m_chunkMask = (source.m_chunkSize - 1);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.FastStringBuffer
 * JD-Core Version:    0.6.2
 */