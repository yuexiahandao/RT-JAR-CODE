/*     */ package com.sun.org.apache.xerces.internal.xpointer;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class ElementSchemePointer
/*     */   implements XPointerPart
/*     */ {
/*     */   private String fSchemeName;
/*     */   private String fSchemeData;
/*     */   private String fShortHandPointerName;
/*  59 */   private boolean fIsResolveElement = false;
/*     */ 
/*  62 */   private boolean fIsElementFound = false;
/*     */ 
/*  65 */   private boolean fWasOnlyEmptyElementFound = false;
/*     */ 
/*  68 */   boolean fIsShortHand = false;
/*     */ 
/*  71 */   int fFoundDepth = 0;
/*     */   private int[] fChildSequence;
/*  77 */   private int fCurrentChildPosition = 1;
/*     */ 
/*  80 */   private int fCurrentChildDepth = 0;
/*     */   private int[] fCurrentChildSequence;
/*  86 */   private boolean fIsFragmentResolved = false;
/*     */   private ShortHandPointer fShortHandPointer;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLErrorHandler fErrorHandler;
/*     */   private SymbolTable fSymbolTable;
/*     */ 
/*     */   public ElementSchemePointer()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ElementSchemePointer(SymbolTable symbolTable)
/*     */   {
/* 107 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public ElementSchemePointer(SymbolTable symbolTable, XMLErrorReporter errorReporter)
/*     */   {
/* 112 */     this.fSymbolTable = symbolTable;
/* 113 */     this.fErrorReporter = errorReporter; } 
/*     */   public void parseXPointer(String xpointer) throws XNIException { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 202	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:init	()V
/*     */     //   4: new 105	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: aload_0
/*     */     //   10: getfield 191	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*     */     //   13: aconst_null
/*     */     //   14: invokespecial 213	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:<init>	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$1;)V
/*     */     //   17: astore_2
/*     */     //   18: new 103	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$1
/*     */     //   21: dup
/*     */     //   22: aload_0
/*     */     //   23: aload_0
/*     */     //   24: getfield 191	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*     */     //   27: invokespecial 207	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$1:<init>	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;)V
/*     */     //   30: astore_3
/*     */     //   31: aload_1
/*     */     //   32: invokevirtual 221	java/lang/String:length	()I
/*     */     //   35: istore 4
/*     */     //   37: aload_3
/*     */     //   38: aload_0
/*     */     //   39: getfield 191	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*     */     //   42: aload_2
/*     */     //   43: aload_1
/*     */     //   44: iconst_0
/*     */     //   45: iload 4
/*     */     //   47: invokestatic 208	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Scanner:access$300	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Scanner;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;Ljava/lang/String;II)Z
/*     */     //   50: istore 5
/*     */     //   52: iload 5
/*     */     //   54: ifne +17 -> 71
/*     */     //   57: aload_0
/*     */     //   58: ldc 1
/*     */     //   60: iconst_1
/*     */     //   61: anewarray 110	java/lang/Object
/*     */     //   64: dup
/*     */     //   65: iconst_0
/*     */     //   66: aload_1
/*     */     //   67: aastore
/*     */     //   68: invokevirtual 206	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   71: aload_2
/*     */     //   72: invokestatic 209	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$400	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;)I
/*     */     //   75: iconst_2
/*     */     //   76: idiv
/*     */     //   77: iconst_1
/*     */     //   78: iadd
/*     */     //   79: newarray int
/*     */     //   81: astore 6
/*     */     //   83: iconst_0
/*     */     //   84: istore 7
/*     */     //   86: aload_2
/*     */     //   87: invokestatic 211	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$500	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;)Z
/*     */     //   90: ifeq +115 -> 205
/*     */     //   93: aload_2
/*     */     //   94: invokestatic 210	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;)I
/*     */     //   97: istore 8
/*     */     //   99: iload 8
/*     */     //   101: lookupswitch	default:+87->188, 0:+27->128, 1:+72->173
/*     */     //   129: invokestatic 210	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;)I
/*     */     //   132: istore 8
/*     */     //   134: aload_0
/*     */     //   135: aload_2
/*     */     //   136: iload 8
/*     */     //   138: invokestatic 212	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;I)Ljava/lang/String;
/*     */     //   141: putfield 196	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fShortHandPointerName	Ljava/lang/String;
/*     */     //   144: aload_0
/*     */     //   145: new 106	com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer
/*     */     //   148: dup
/*     */     //   149: aload_0
/*     */     //   150: getfield 191	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*     */     //   153: invokespecial 215	com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer:<init>	(Lcom/sun/org/apache/xerces/internal/util/SymbolTable;)V
/*     */     //   156: putfield 193	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fShortHandPointer	Lcom/sun/org/apache/xerces/internal/xpointer/ShortHandPointer;
/*     */     //   159: aload_0
/*     */     //   160: getfield 193	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fShortHandPointer	Lcom/sun/org/apache/xerces/internal/xpointer/ShortHandPointer;
/*     */     //   163: aload_0
/*     */     //   164: getfield 196	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fShortHandPointerName	Ljava/lang/String;
/*     */     //   167: invokevirtual 216	com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer:setSchemeName	(Ljava/lang/String;)V
/*     */     //   170: goto +32 -> 202
/*     */     //   173: aload 6
/*     */     //   175: iload 7
/*     */     //   177: aload_2
/*     */     //   178: invokestatic 210	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens;)I
/*     */     //   181: iastore
/*     */     //   182: iinc 7 1
/*     */     //   185: goto +17 -> 202
/*     */     //   188: aload_0
/*     */     //   189: ldc 1
/*     */     //   191: iconst_1
/*     */     //   192: anewarray 110	java/lang/Object
/*     */     //   195: dup
/*     */     //   196: iconst_0
/*     */     //   197: aload_1
/*     */     //   198: aastore
/*     */     //   199: invokevirtual 206	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   202: goto -116 -> 86
/*     */     //   205: aload_0
/*     */     //   206: iload 7
/*     */     //   208: newarray int
/*     */     //   210: putfield 188	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fChildSequence	[I
/*     */     //   213: aload_0
/*     */     //   214: iload 7
/*     */     //   216: newarray int
/*     */     //   218: putfield 189	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fCurrentChildSequence	[I
/*     */     //   221: aload 6
/*     */     //   223: iconst_0
/*     */     //   224: aload_0
/*     */     //   225: getfield 188	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:fChildSequence	[I
/*     */     //   228: iconst_0
/*     */     //   229: iload 7
/*     */     //   231: invokestatic 222	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*     */     //   234: return } 
/* 207 */   public String getSchemeName() { return this.fSchemeName; }
/*     */ 
/*     */ 
/*     */   public String getSchemeData()
/*     */   {
/* 216 */     return this.fSchemeData;
/*     */   }
/*     */ 
/*     */   public void setSchemeName(String schemeName)
/*     */   {
/* 225 */     this.fSchemeName = schemeName;
/*     */   }
/*     */ 
/*     */   public void setSchemeData(String schemeData)
/*     */   {
/* 235 */     this.fSchemeData = schemeData;
/*     */   }
/*     */ 
/*     */   public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event)
/*     */     throws XNIException
/*     */   {
/* 248 */     boolean isShortHandPointerResolved = false;
/*     */ 
/* 253 */     if (this.fShortHandPointerName != null)
/*     */     {
/* 255 */       isShortHandPointerResolved = this.fShortHandPointer.resolveXPointer(element, attributes, augs, event);
/*     */ 
/* 257 */       if (isShortHandPointerResolved) {
/* 258 */         this.fIsResolveElement = true;
/* 259 */         this.fIsShortHand = true;
/*     */       } else {
/* 261 */         this.fIsResolveElement = false;
/*     */       }
/*     */     } else {
/* 264 */       this.fIsResolveElement = true;
/*     */     }
/*     */ 
/* 269 */     if (this.fChildSequence.length > 0)
/* 270 */       this.fIsFragmentResolved = matchChildSequence(element, event);
/* 271 */     else if ((isShortHandPointerResolved) && (this.fChildSequence.length <= 0))
/*     */     {
/* 273 */       this.fIsFragmentResolved = isShortHandPointerResolved;
/*     */     }
/* 275 */     else this.fIsFragmentResolved = false;
/*     */ 
/* 278 */     return this.fIsFragmentResolved;
/*     */   }
/*     */ 
/*     */   protected boolean matchChildSequence(QName element, int event)
/*     */     throws XNIException
/*     */   {
/* 294 */     if (this.fCurrentChildDepth >= this.fCurrentChildSequence.length) {
/* 295 */       int[] tmpCurrentChildSequence = new int[this.fCurrentChildSequence.length];
/* 296 */       System.arraycopy(this.fCurrentChildSequence, 0, tmpCurrentChildSequence, 0, this.fCurrentChildSequence.length);
/*     */ 
/* 300 */       this.fCurrentChildSequence = new int[this.fCurrentChildDepth * 2];
/* 301 */       System.arraycopy(tmpCurrentChildSequence, 0, this.fCurrentChildSequence, 0, tmpCurrentChildSequence.length);
/*     */     }
/*     */ 
/* 306 */     if (this.fIsResolveElement)
/*     */     {
/* 308 */       this.fWasOnlyEmptyElementFound = false;
/* 309 */       if (event == 0) {
/* 310 */         this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
/* 311 */         this.fCurrentChildDepth += 1;
/*     */ 
/* 314 */         this.fCurrentChildPosition = 1;
/*     */ 
/* 317 */         if ((this.fCurrentChildDepth <= this.fFoundDepth) || (this.fFoundDepth == 0)) {
/* 318 */           if (checkMatch()) {
/* 319 */             this.fIsElementFound = true;
/* 320 */             this.fFoundDepth = this.fCurrentChildDepth;
/*     */           } else {
/* 322 */             this.fIsElementFound = false;
/* 323 */             this.fFoundDepth = 0;
/*     */           }
/*     */         }
/*     */       }
/* 327 */       else if (event == 1) {
/* 328 */         if (this.fCurrentChildDepth == this.fFoundDepth)
/* 329 */           this.fIsElementFound = true;
/* 330 */         else if (((this.fCurrentChildDepth < this.fFoundDepth) && (this.fFoundDepth != 0)) || ((this.fCurrentChildDepth > this.fFoundDepth) && (this.fFoundDepth == 0)))
/*     */         {
/* 333 */           this.fIsElementFound = false;
/*     */         }
/*     */ 
/* 337 */         this.fCurrentChildSequence[this.fCurrentChildDepth] = 0;
/*     */ 
/* 339 */         this.fCurrentChildDepth -= 1;
/* 340 */         this.fCurrentChildPosition = (this.fCurrentChildSequence[this.fCurrentChildDepth] + 1);
/*     */       }
/* 342 */       else if (event == 2)
/*     */       {
/* 344 */         this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
/* 345 */         this.fCurrentChildPosition += 1;
/*     */ 
/* 350 */         if (checkMatch()) {
/* 351 */           this.fIsElementFound = true;
/* 352 */           this.fWasOnlyEmptyElementFound = true;
/*     */         } else {
/* 354 */           this.fIsElementFound = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 361 */     return this.fIsElementFound;
/*     */   }
/*     */ 
/*     */   protected boolean checkMatch()
/*     */   {
/* 374 */     if (!this.fIsShortHand)
/*     */     {
/* 377 */       if (this.fChildSequence.length <= this.fCurrentChildDepth + 1)
/*     */       {
/* 379 */         for (int i = 0; i < this.fChildSequence.length; i++) {
/* 380 */           if (this.fChildSequence[i] != this.fCurrentChildSequence[i])
/* 381 */             return false;
/*     */         }
/*     */       }
/*     */       else {
/* 385 */         return false;
/*     */       }
/*     */ 
/*     */     }
/* 391 */     else if (this.fChildSequence.length <= this.fCurrentChildDepth + 1)
/*     */     {
/* 393 */       for (int i = 0; i < this.fChildSequence.length; i++)
/*     */       {
/* 395 */         if (this.fCurrentChildSequence.length < i + 2) {
/* 396 */           return false;
/*     */         }
/*     */ 
/* 400 */         if (this.fChildSequence[i] != this.fCurrentChildSequence[(i + 1)])
/* 401 */           return false;
/*     */       }
/*     */     }
/*     */     else {
/* 405 */       return false;
/*     */     }
/*     */ 
/* 410 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isFragmentResolved()
/*     */     throws XNIException
/*     */   {
/* 422 */     return this.fIsFragmentResolved;
/*     */   }
/*     */ 
/*     */   public boolean isChildFragmentResolved()
/*     */   {
/* 434 */     if ((this.fIsShortHand) && (this.fShortHandPointer != null) && (this.fChildSequence.length <= 0)) {
/* 435 */       return this.fShortHandPointer.isChildFragmentResolved();
/*     */     }
/* 437 */     return !this.fWasOnlyEmptyElementFound;
/*     */   }
/*     */ 
/*     */   protected void reportError(String key, Object[] arguments)
/*     */     throws XNIException
/*     */   {
/* 450 */     throw new XNIException(this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(this.fErrorReporter.getLocale(), key, arguments));
/*     */   }
/*     */ 
/*     */   protected void initErrorReporter()
/*     */   {
/* 459 */     if (this.fErrorReporter == null) {
/* 460 */       this.fErrorReporter = new XMLErrorReporter();
/*     */     }
/* 462 */     if (this.fErrorHandler == null) {
/* 463 */       this.fErrorHandler = new XPointerErrorHandler();
/*     */     }
/* 465 */     this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/* 474 */     this.fSchemeName = null;
/* 475 */     this.fSchemeData = null;
/* 476 */     this.fShortHandPointerName = null;
/* 477 */     this.fIsResolveElement = false;
/* 478 */     this.fIsElementFound = false;
/* 479 */     this.fWasOnlyEmptyElementFound = false;
/* 480 */     this.fFoundDepth = 0;
/* 481 */     this.fCurrentChildPosition = 1;
/* 482 */     this.fCurrentChildDepth = 0;
/* 483 */     this.fIsFragmentResolved = false;
/* 484 */     this.fShortHandPointer = null;
/*     */ 
/* 486 */     initErrorReporter();
/*     */   }
/*     */ 
/*     */   private class Scanner
/*     */   {
/*     */     private static final byte CHARTYPE_INVALID = 0;
/*     */     private static final byte CHARTYPE_OTHER = 1;
/*     */     private static final byte CHARTYPE_MINUS = 2;
/*     */     private static final byte CHARTYPE_PERIOD = 3;
/*     */     private static final byte CHARTYPE_SLASH = 4;
/*     */     private static final byte CHARTYPE_DIGIT = 5;
/*     */     private static final byte CHARTYPE_LETTER = 6;
/*     */     private static final byte CHARTYPE_UNDERSCORE = 7;
/*     */     private static final byte CHARTYPE_NONASCII = 8;
/* 692 */     private final byte[] fASCIICharMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 7, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 1 };
/*     */     private SymbolTable fSymbolTable;
/*     */ 
/*     */     private Scanner(SymbolTable symbolTable)
/*     */     {
/* 721 */       this.fSymbolTable = symbolTable;
/*     */     }
/*     */ 
/*     */     private boolean scanExpr(SymbolTable symbolTable, ElementSchemePointer.Tokens tokens, String data, int currentOffset, int endOffset)
/*     */       throws XNIException
/*     */     {
/* 735 */       String nameHandle = null;
/*     */ 
/* 738 */       while (currentOffset != endOffset)
/*     */       {
/* 742 */         int ch = data.charAt(currentOffset);
/* 743 */         byte chartype = ch >= 128 ? 8 : this.fASCIICharMap[ch];
/*     */ 
/* 751 */         switch (chartype)
/*     */         {
/*     */         case 4:
/* 755 */           currentOffset++; if (currentOffset == endOffset) {
/* 756 */             return false;
/*     */           }
/*     */ 
/* 759 */           addToken(tokens, 1);
/* 760 */           ch = data.charAt(currentOffset);
/*     */ 
/* 763 */           int child = 0;
/* 764 */           while ((ch >= 48) && (ch <= 57)) {
/* 765 */             child = child * 10 + (ch - 48);
/* 766 */             currentOffset++; if (currentOffset == endOffset) {
/*     */               break;
/*     */             }
/* 769 */             ch = data.charAt(currentOffset);
/*     */           }
/*     */ 
/* 773 */           if (child == 0) {
/* 774 */             ElementSchemePointer.this.reportError("InvalidChildSequenceCharacter", new Object[] { new Character((char)ch) });
/*     */ 
/* 776 */             return false;
/*     */           }
/*     */ 
/* 779 */           ElementSchemePointer.Tokens.access$700(tokens, child);
/*     */ 
/* 781 */           break;
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/*     */         case 8:
/* 791 */           int nameOffset = currentOffset;
/* 792 */           currentOffset = scanNCName(data, endOffset, currentOffset);
/*     */ 
/* 794 */           if (currentOffset == nameOffset)
/*     */           {
/* 796 */             ElementSchemePointer.this.reportError("InvalidNCNameInElementSchemeData", new Object[] { data });
/*     */ 
/* 798 */             return false;
/*     */           }
/*     */ 
/* 801 */           if (currentOffset < endOffset)
/* 802 */             ch = data.charAt(currentOffset);
/*     */           else {
/* 804 */             ch = -1;
/*     */           }
/*     */ 
/* 807 */           nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*     */ 
/* 809 */           addToken(tokens, 0);
/* 810 */           ElementSchemePointer.Tokens.access$800(tokens, nameHandle);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 815 */       return true;
/*     */     }
/*     */ 
/*     */     private int scanNCName(String data, int endOffset, int currentOffset)
/*     */     {
/* 829 */       int ch = data.charAt(currentOffset);
/* 830 */       if (ch >= 128) {
/* 831 */         if (!XMLChar.isNameStart(ch))
/* 832 */           return currentOffset;
/*     */       }
/*     */       else {
/* 835 */         byte chartype = this.fASCIICharMap[ch];
/* 836 */         if ((chartype != 6) && (chartype != 7))
/*     */         {
/* 838 */           return currentOffset;
/*     */         }
/*     */       }
/*     */       while (true) { currentOffset++; if (currentOffset >= endOffset) break;
/* 842 */         ch = data.charAt(currentOffset);
/* 843 */         if (ch >= 128) {
/* 844 */           if (!XMLChar.isName(ch))
/* 845 */             break;
/*     */         }
/*     */         else {
/* 848 */           byte chartype = this.fASCIICharMap[ch];
/* 849 */           if ((chartype != 6) && (chartype != 5) && (chartype != 3) && (chartype != 2) && (chartype != 7))
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 858 */       return currentOffset;
/*     */     }
/*     */ 
/*     */     protected void addToken(ElementSchemePointer.Tokens tokens, int token)
/*     */       throws XNIException
/*     */     {
/* 874 */       ElementSchemePointer.Tokens.access$700(tokens, token);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class Tokens
/*     */   {
/*     */     private static final int XPTRTOKEN_ELEM_NCNAME = 0;
/*     */     private static final int XPTRTOKEN_ELEM_CHILD = 1;
/* 514 */     private final String[] fgTokenNames = { "XPTRTOKEN_ELEM_NCNAME", "XPTRTOKEN_ELEM_CHILD" };
/*     */     private static final int INITIAL_TOKEN_COUNT = 256;
/* 520 */     private int[] fTokens = new int[256];
/*     */ 
/* 522 */     private int fTokenCount = 0;
/*     */     private int fCurrentTokenIndex;
/*     */     private SymbolTable fSymbolTable;
/* 529 */     private Hashtable fTokenNames = new Hashtable();
/*     */ 
/*     */     private Tokens(SymbolTable symbolTable)
/*     */     {
/* 537 */       this.fSymbolTable = symbolTable;
/*     */ 
/* 539 */       this.fTokenNames.put(new Integer(0), "XPTRTOKEN_ELEM_NCNAME");
/*     */ 
/* 541 */       this.fTokenNames.put(new Integer(1), "XPTRTOKEN_ELEM_CHILD");
/*     */     }
/*     */ 
/*     */     private String getTokenString(int token)
/*     */     {
/* 551 */       return (String)this.fTokenNames.get(new Integer(token));
/*     */     }
/*     */ 
/*     */     private Integer getToken(int token)
/*     */     {
/* 560 */       return (Integer)this.fTokenNames.get(new Integer(token));
/*     */     }
/*     */ 
/*     */     private void addToken(String tokenStr)
/*     */     {
/* 569 */       Integer tokenInt = (Integer)this.fTokenNames.get(tokenStr);
/* 570 */       if (tokenInt == null) {
/* 571 */         tokenInt = new Integer(this.fTokenNames.size());
/* 572 */         this.fTokenNames.put(tokenInt, tokenStr);
/*     */       }
/* 574 */       addToken(tokenInt.intValue());
/*     */     }
/*     */ 
/*     */     private void addToken(int token)
/*     */     {
/*     */       try
/*     */       {
/* 584 */         this.fTokens[this.fTokenCount] = token;
/*     */       } catch (ArrayIndexOutOfBoundsException ex) {
/* 586 */         int[] oldList = this.fTokens;
/* 587 */         this.fTokens = new int[this.fTokenCount << 1];
/* 588 */         System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
/* 589 */         this.fTokens[this.fTokenCount] = token;
/*     */       }
/* 591 */       this.fTokenCount += 1;
/*     */     }
/*     */ 
/*     */     private void rewind()
/*     */     {
/* 598 */       this.fCurrentTokenIndex = 0;
/*     */     }
/*     */ 
/*     */     private boolean hasMore()
/*     */     {
/* 606 */       return this.fCurrentTokenIndex < this.fTokenCount;
/*     */     }
/*     */ 
/*     */     private int nextToken()
/*     */       throws XNIException
/*     */     {
/* 617 */       if (this.fCurrentTokenIndex == this.fTokenCount)
/* 618 */         ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
/* 619 */       return this.fTokens[(this.fCurrentTokenIndex++)];
/*     */     }
/*     */ 
/*     */     private int peekToken()
/*     */       throws XNIException
/*     */     {
/* 630 */       if (this.fCurrentTokenIndex == this.fTokenCount)
/* 631 */         ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
/* 632 */       return this.fTokens[this.fCurrentTokenIndex];
/*     */     }
/*     */ 
/*     */     private String nextTokenAsString()
/*     */       throws XNIException
/*     */     {
/* 644 */       String s = getTokenString(nextToken());
/* 645 */       if (s == null)
/* 646 */         ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
/* 647 */       return s;
/*     */     }
/*     */ 
/*     */     private int getTokenCount()
/*     */     {
/* 655 */       return this.fTokenCount;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer
 * JD-Core Version:    0.6.2
 */