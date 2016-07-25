/*      */ package com.sun.org.apache.xerces.internal.xpointer;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
/*      */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public final class XPointerHandler extends XIncludeHandler
/*      */   implements XPointerProcessor
/*      */ {
/*   68 */   protected Vector fXPointerParts = null;
/*      */ 
/*   71 */   protected XPointerPart fXPointerPart = null;
/*      */ 
/*   74 */   protected boolean fFoundMatchingPtrPart = false;
/*      */   protected XMLErrorReporter fXPointerErrorReporter;
/*      */   protected XMLErrorHandler fErrorHandler;
/*   83 */   protected SymbolTable fSymbolTable = null;
/*      */ 
/*   86 */   private final String ELEMENT_SCHEME_NAME = "element";
/*      */ 
/*   89 */   protected boolean fIsXPointerResolved = false;
/*      */ 
/*   92 */   protected boolean fFixupBase = false;
/*   93 */   protected boolean fFixupLang = false;
/*      */ 
/*      */   public XPointerHandler()
/*      */   {
/*  105 */     this.fXPointerParts = new Vector();
/*  106 */     this.fSymbolTable = new SymbolTable();
/*      */   }
/*      */ 
/*      */   public XPointerHandler(SymbolTable symbolTable, XMLErrorHandler errorHandler, XMLErrorReporter errorReporter)
/*      */   {
/*  113 */     this.fXPointerParts = new Vector();
/*  114 */     this.fSymbolTable = symbolTable;
/*  115 */     this.fErrorHandler = errorHandler;
/*  116 */     this.fXPointerErrorReporter = errorReporter; } 
/*      */   public void parseXPointer(String xpointer) throws XNIException { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 331	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:init	()V
/*      */     //   4: new 181	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens
/*      */     //   7: dup
/*      */     //   8: aload_0
/*      */     //   9: aload_0
/*      */     //   10: getfield 302	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*      */     //   13: aconst_null
/*      */     //   14: invokespecial 345	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:<init>	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$1;)V
/*      */     //   17: astore_2
/*      */     //   18: new 179	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$1
/*      */     //   21: dup
/*      */     //   22: aload_0
/*      */     //   23: aload_0
/*      */     //   24: getfield 302	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*      */     //   27: invokespecial 339	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$1:<init>	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;)V
/*      */     //   30: astore_3
/*      */     //   31: aload_1
/*      */     //   32: invokevirtual 349	java/lang/String:length	()I
/*      */     //   35: istore 4
/*      */     //   37: aload_3
/*      */     //   38: aload_0
/*      */     //   39: getfield 302	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*      */     //   42: aload_2
/*      */     //   43: aload_1
/*      */     //   44: iconst_0
/*      */     //   45: iload 4
/*      */     //   47: invokestatic 340	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Scanner:access$400	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Scanner;Lcom/sun/org/apache/xerces/internal/util/SymbolTable;Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;Ljava/lang/String;II)Z
/*      */     //   50: istore 5
/*      */     //   52: iload 5
/*      */     //   54: ifne +17 -> 71
/*      */     //   57: aload_0
/*      */     //   58: ldc 1
/*      */     //   60: iconst_1
/*      */     //   61: anewarray 187	java/lang/Object
/*      */     //   64: dup
/*      */     //   65: iconst_0
/*      */     //   66: aload_1
/*      */     //   67: aastore
/*      */     //   68: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   71: aload_2
/*      */     //   72: invokestatic 343	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$500	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)Z
/*      */     //   75: ifeq +505 -> 580
/*      */     //   78: aload_2
/*      */     //   79: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   82: istore 6
/*      */     //   84: iload 6
/*      */     //   86: lookupswitch	default:+477->563, 2:+26->112, 3:+94->180
/*      */     //   113: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   116: istore 6
/*      */     //   118: aload_2
/*      */     //   119: iload 6
/*      */     //   121: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   124: astore 7
/*      */     //   126: aload 7
/*      */     //   128: ifnonnull +17 -> 145
/*      */     //   131: aload_0
/*      */     //   132: ldc 1
/*      */     //   134: iconst_1
/*      */     //   135: anewarray 187	java/lang/Object
/*      */     //   138: dup
/*      */     //   139: iconst_0
/*      */     //   140: aload_1
/*      */     //   141: aastore
/*      */     //   142: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   145: new 176	com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer
/*      */     //   148: dup
/*      */     //   149: aload_0
/*      */     //   150: getfield 302	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*      */     //   153: invokespecial 329	com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer:<init>	(Lcom/sun/org/apache/xerces/internal/util/SymbolTable;)V
/*      */     //   156: astore 8
/*      */     //   158: aload 8
/*      */     //   160: aload 7
/*      */     //   162: invokeinterface 364 2 0
/*      */     //   167: aload_0
/*      */     //   168: getfield 307	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fXPointerParts	Ljava/util/Vector;
/*      */     //   171: aload 8
/*      */     //   173: invokevirtual 358	java/util/Vector:add	(Ljava/lang/Object;)Z
/*      */     //   176: pop
/*      */     //   177: goto +400 -> 577
/*      */     //   180: aload_2
/*      */     //   181: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   184: istore 6
/*      */     //   186: aload_2
/*      */     //   187: iload 6
/*      */     //   189: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   192: astore 7
/*      */     //   194: aload_2
/*      */     //   195: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   198: istore 6
/*      */     //   200: aload_2
/*      */     //   201: iload 6
/*      */     //   203: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   206: astore 8
/*      */     //   208: new 189	java/lang/StringBuilder
/*      */     //   211: dup
/*      */     //   212: invokespecial 351	java/lang/StringBuilder:<init>	()V
/*      */     //   215: aload 7
/*      */     //   217: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   220: aload 8
/*      */     //   222: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   225: invokevirtual 352	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   228: astore 9
/*      */     //   230: iconst_0
/*      */     //   231: istore 10
/*      */     //   233: iconst_0
/*      */     //   234: istore 11
/*      */     //   236: aload_2
/*      */     //   237: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   240: istore 6
/*      */     //   242: aload_2
/*      */     //   243: iload 6
/*      */     //   245: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   248: astore 12
/*      */     //   250: aload 12
/*      */     //   252: ldc 7
/*      */     //   254: if_acmpeq +40 -> 294
/*      */     //   257: iload 6
/*      */     //   259: iconst_2
/*      */     //   260: if_icmpne +20 -> 280
/*      */     //   263: aload_0
/*      */     //   264: ldc 2
/*      */     //   266: iconst_1
/*      */     //   267: anewarray 187	java/lang/Object
/*      */     //   270: dup
/*      */     //   271: iconst_0
/*      */     //   272: aload_1
/*      */     //   273: aastore
/*      */     //   274: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   277: goto +17 -> 294
/*      */     //   280: aload_0
/*      */     //   281: ldc 1
/*      */     //   283: iconst_1
/*      */     //   284: anewarray 187	java/lang/Object
/*      */     //   287: dup
/*      */     //   288: iconst_0
/*      */     //   289: aload_1
/*      */     //   290: aastore
/*      */     //   291: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   294: iinc 10 1
/*      */     //   297: aconst_null
/*      */     //   298: astore 13
/*      */     //   300: aload_2
/*      */     //   301: invokestatic 343	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$500	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)Z
/*      */     //   304: ifeq +33 -> 337
/*      */     //   307: aload_2
/*      */     //   308: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   311: istore 6
/*      */     //   313: aload_2
/*      */     //   314: iload 6
/*      */     //   316: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   319: astore 13
/*      */     //   321: aload 13
/*      */     //   323: ldc 7
/*      */     //   325: if_acmpeq +6 -> 331
/*      */     //   328: goto +9 -> 337
/*      */     //   331: iinc 10 1
/*      */     //   334: goto -34 -> 300
/*      */     //   337: aload_2
/*      */     //   338: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   341: istore 6
/*      */     //   343: aload_2
/*      */     //   344: iload 6
/*      */     //   346: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   349: astore 13
/*      */     //   351: aload_2
/*      */     //   352: invokestatic 341	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$600	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   355: istore 6
/*      */     //   357: aload_2
/*      */     //   358: iload 6
/*      */     //   360: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   363: astore 14
/*      */     //   365: aload 14
/*      */     //   367: ldc 6
/*      */     //   369: if_acmpeq +17 -> 386
/*      */     //   372: aload_0
/*      */     //   373: ldc 3
/*      */     //   375: iconst_1
/*      */     //   376: anewarray 187	java/lang/Object
/*      */     //   379: dup
/*      */     //   380: iconst_0
/*      */     //   381: aload_1
/*      */     //   382: aastore
/*      */     //   383: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   386: iinc 11 1
/*      */     //   389: aload_2
/*      */     //   390: invokestatic 343	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$500	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)Z
/*      */     //   393: ifeq +25 -> 418
/*      */     //   396: aload_2
/*      */     //   397: aload_2
/*      */     //   398: invokestatic 342	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$700	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;)I
/*      */     //   401: invokestatic 344	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens:access$200	(Lcom/sun/org/apache/xerces/internal/xpointer/XPointerHandler$Tokens;I)Ljava/lang/String;
/*      */     //   404: ldc 7
/*      */     //   406: if_acmpeq +6 -> 412
/*      */     //   409: goto +9 -> 418
/*      */     //   412: iinc 11 1
/*      */     //   415: goto -26 -> 389
/*      */     //   418: iload 10
/*      */     //   420: iload 11
/*      */     //   422: if_icmpeq +41 -> 463
/*      */     //   425: aload_0
/*      */     //   426: ldc 5
/*      */     //   428: iconst_3
/*      */     //   429: anewarray 187	java/lang/Object
/*      */     //   432: dup
/*      */     //   433: iconst_0
/*      */     //   434: aload_1
/*      */     //   435: aastore
/*      */     //   436: dup
/*      */     //   437: iconst_1
/*      */     //   438: new 186	java/lang/Integer
/*      */     //   441: dup
/*      */     //   442: iload 10
/*      */     //   444: invokespecial 348	java/lang/Integer:<init>	(I)V
/*      */     //   447: aastore
/*      */     //   448: dup
/*      */     //   449: iconst_2
/*      */     //   450: new 186	java/lang/Integer
/*      */     //   453: dup
/*      */     //   454: iload 11
/*      */     //   456: invokespecial 348	java/lang/Integer:<init>	(I)V
/*      */     //   459: aastore
/*      */     //   460: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   463: aload 9
/*      */     //   465: ldc 8
/*      */     //   467: invokevirtual 350	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   470: ifeq +75 -> 545
/*      */     //   473: new 175	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer
/*      */     //   476: dup
/*      */     //   477: aload_0
/*      */     //   478: getfield 302	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fSymbolTable	Lcom/sun/org/apache/xerces/internal/util/SymbolTable;
/*      */     //   481: aload_0
/*      */     //   482: getfield 300	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fErrorReporter	Lcom/sun/org/apache/xerces/internal/impl/XMLErrorReporter;
/*      */     //   485: invokespecial 328	com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer:<init>	(Lcom/sun/org/apache/xerces/internal/util/SymbolTable;Lcom/sun/org/apache/xerces/internal/impl/XMLErrorReporter;)V
/*      */     //   488: astore 15
/*      */     //   490: aload 15
/*      */     //   492: aload 9
/*      */     //   494: invokeinterface 364 2 0
/*      */     //   499: aload 15
/*      */     //   501: aload 13
/*      */     //   503: invokeinterface 363 2 0
/*      */     //   508: aload 15
/*      */     //   510: aload 13
/*      */     //   512: invokeinterface 362 2 0
/*      */     //   517: aload_0
/*      */     //   518: getfield 307	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:fXPointerParts	Ljava/util/Vector;
/*      */     //   521: aload 15
/*      */     //   523: invokevirtual 358	java/util/Vector:add	(Ljava/lang/Object;)Z
/*      */     //   526: pop
/*      */     //   527: goto +15 -> 542
/*      */     //   530: astore 16
/*      */     //   532: new 172	com/sun/org/apache/xerces/internal/xni/XNIException
/*      */     //   535: dup
/*      */     //   536: aload 16
/*      */     //   538: invokespecial 326	com/sun/org/apache/xerces/internal/xni/XNIException:<init>	(Ljava/lang/Exception;)V
/*      */     //   541: athrow
/*      */     //   542: goto +35 -> 577
/*      */     //   545: aload_0
/*      */     //   546: ldc 4
/*      */     //   548: iconst_1
/*      */     //   549: anewarray 187	java/lang/Object
/*      */     //   552: dup
/*      */     //   553: iconst_0
/*      */     //   554: aload 9
/*      */     //   556: aastore
/*      */     //   557: invokespecial 337	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportWarning	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   560: goto +17 -> 577
/*      */     //   563: aload_0
/*      */     //   564: ldc 1
/*      */     //   566: iconst_1
/*      */     //   567: anewarray 187	java/lang/Object
/*      */     //   570: dup
/*      */     //   571: iconst_0
/*      */     //   572: aload_1
/*      */     //   573: aastore
/*      */     //   574: invokespecial 336	com/sun/org/apache/xerces/internal/xpointer/XPointerHandler:reportError	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   577: goto -506 -> 71
/*      */     //   580: return
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   508	527	530	com/sun/org/apache/xerces/internal/xni/XNIException } 
/*  288 */   public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException { boolean resolved = false;
/*      */ 
/*  297 */     if (!this.fFoundMatchingPtrPart)
/*      */     {
/*  301 */       for (int i = 0; i < this.fXPointerParts.size(); i++)
/*      */       {
/*  303 */         this.fXPointerPart = ((XPointerPart)this.fXPointerParts.get(i));
/*      */ 
/*  305 */         if (this.fXPointerPart.resolveXPointer(element, attributes, augs, event))
/*      */         {
/*  307 */           this.fFoundMatchingPtrPart = true;
/*  308 */           resolved = true;
/*      */         }
/*      */       }
/*      */     }
/*  312 */     else if (this.fXPointerPart.resolveXPointer(element, attributes, augs, event)) {
/*  313 */       resolved = true;
/*      */     }
/*      */ 
/*  317 */     if (!this.fIsXPointerResolved) {
/*  318 */       this.fIsXPointerResolved = resolved;
/*      */     }
/*      */ 
/*  321 */     return resolved;
/*      */   }
/*      */ 
/*      */   public boolean isFragmentResolved()
/*      */     throws XNIException
/*      */   {
/*  330 */     boolean resolved = this.fXPointerPart != null ? this.fXPointerPart.isFragmentResolved() : false;
/*      */ 
/*  333 */     if (!this.fIsXPointerResolved) {
/*  334 */       this.fIsXPointerResolved = resolved;
/*      */     }
/*      */ 
/*  337 */     return resolved;
/*      */   }
/*      */ 
/*      */   public boolean isChildFragmentResolved()
/*      */     throws XNIException
/*      */   {
/*  348 */     boolean resolved = this.fXPointerPart != null ? this.fXPointerPart.isChildFragmentResolved() : false;
/*      */ 
/*  350 */     return resolved;
/*      */   }
/*      */ 
/*      */   public boolean isXPointerResolved()
/*      */     throws XNIException
/*      */   {
/*  359 */     return this.fIsXPointerResolved;
/*      */   }
/*      */ 
/*      */   public XPointerPart getXPointerPart()
/*      */   {
/*  368 */     return this.fXPointerPart;
/*      */   }
/*      */ 
/*      */   private void reportError(String key, Object[] arguments)
/*      */     throws XNIException
/*      */   {
/*  382 */     throw new XNIException(this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(this.fErrorReporter.getLocale(), key, arguments));
/*      */   }
/*      */ 
/*      */   private void reportWarning(String key, Object[] arguments)
/*      */     throws XNIException
/*      */   {
/*  393 */     this.fXPointerErrorReporter.reportError("http://www.w3.org/TR/XPTR", key, arguments, (short)0);
/*      */   }
/*      */ 
/*      */   protected void initErrorReporter()
/*      */   {
/*  403 */     if (this.fXPointerErrorReporter == null) {
/*  404 */       this.fXPointerErrorReporter = new XMLErrorReporter();
/*      */     }
/*  406 */     if (this.fErrorHandler == null) {
/*  407 */       this.fErrorHandler = new XPointerErrorHandler();
/*      */     }
/*      */ 
/*  413 */     this.fXPointerErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
/*      */   }
/*      */ 
/*      */   protected void init()
/*      */   {
/*  422 */     this.fXPointerParts.clear();
/*  423 */     this.fXPointerPart = null;
/*  424 */     this.fFoundMatchingPtrPart = false;
/*  425 */     this.fIsXPointerResolved = false;
/*      */ 
/*  429 */     initErrorReporter();
/*      */   }
/*      */ 
/*      */   public Vector getPointerParts()
/*      */   {
/*  438 */     return this.fXPointerParts;
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  994 */     if (!isChildFragmentResolved()) {
/*  995 */       return;
/*      */     }
/*  997 */     super.comment(text, augs);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1020 */     if (!isChildFragmentResolved()) {
/* 1021 */       return;
/*      */     }
/* 1023 */     super.processingInstruction(target, data, augs);
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1038 */     if (!resolveXPointer(element, attributes, augs, 0))
/*      */     {
/* 1042 */       if (this.fFixupBase) {
/* 1043 */         processXMLBaseAttributes(attributes);
/*      */       }
/* 1045 */       if (this.fFixupLang) {
/* 1046 */         processXMLLangAttributes(attributes);
/*      */       }
/*      */ 
/* 1050 */       this.fNamespaceContext.setContextInvalid();
/*      */ 
/* 1052 */       return;
/*      */     }
/* 1054 */     super.startElement(element, attributes, augs);
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1069 */     if (!resolveXPointer(element, attributes, augs, 2))
/*      */     {
/* 1072 */       if (this.fFixupBase) {
/* 1073 */         processXMLBaseAttributes(attributes);
/*      */       }
/* 1075 */       if (this.fFixupLang) {
/* 1076 */         processXMLLangAttributes(attributes);
/*      */       }
/*      */ 
/* 1081 */       this.fNamespaceContext.setContextInvalid();
/* 1082 */       return;
/*      */     }
/* 1084 */     super.emptyElement(element, attributes, augs);
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1098 */     if (!isChildFragmentResolved()) {
/* 1099 */       return;
/*      */     }
/* 1101 */     super.characters(text, augs);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1120 */     if (!isChildFragmentResolved()) {
/* 1121 */       return;
/*      */     }
/* 1123 */     super.ignorableWhitespace(text, augs);
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1137 */     if (!resolveXPointer(element, null, augs, 1))
/*      */     {
/* 1141 */       return;
/*      */     }
/* 1143 */     super.endElement(element, augs);
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1155 */     if (!isChildFragmentResolved()) {
/* 1156 */       return;
/*      */     }
/* 1158 */     super.startCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1170 */     if (!isChildFragmentResolved()) {
/* 1171 */       return;
/*      */     }
/* 1173 */     super.endCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1200 */     if (propertyId == "http://apache.org/xml/properties/internal/error-reporter")
/*      */     {
/* 1202 */       if (value != null)
/* 1203 */         this.fXPointerErrorReporter = ((XMLErrorReporter)value);
/*      */       else {
/* 1205 */         this.fXPointerErrorReporter = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1210 */     if (propertyId == "http://apache.org/xml/properties/internal/error-handler")
/*      */     {
/* 1212 */       if (value != null)
/* 1213 */         this.fErrorHandler = ((XMLErrorHandler)value);
/*      */       else {
/* 1215 */         this.fErrorHandler = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1220 */     if (propertyId == "http://apache.org/xml/features/xinclude/fixup-language")
/*      */     {
/* 1222 */       if (value != null)
/* 1223 */         this.fFixupLang = ((Boolean)value).booleanValue();
/*      */       else {
/* 1225 */         this.fFixupLang = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1230 */     if (propertyId == "http://apache.org/xml/features/xinclude/fixup-base-uris")
/*      */     {
/* 1232 */       if (value != null)
/* 1233 */         this.fFixupBase = ((Boolean)value).booleanValue();
/*      */       else {
/* 1235 */         this.fFixupBase = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1240 */     if (propertyId == "http://apache.org/xml/properties/internal/namespace-context")
/*      */     {
/* 1242 */       this.fNamespaceContext = ((XIncludeNamespaceSupport)value);
/*      */     }
/*      */ 
/* 1245 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   private class Scanner
/*      */   {
/*      */     private static final byte CHARTYPE_INVALID = 0;
/*      */     private static final byte CHARTYPE_OTHER = 1;
/*      */     private static final byte CHARTYPE_WHITESPACE = 2;
/*      */     private static final byte CHARTYPE_CARRET = 3;
/*      */     private static final byte CHARTYPE_OPEN_PAREN = 4;
/*      */     private static final byte CHARTYPE_CLOSE_PAREN = 5;
/*      */     private static final byte CHARTYPE_MINUS = 6;
/*      */     private static final byte CHARTYPE_PERIOD = 7;
/*      */     private static final byte CHARTYPE_SLASH = 8;
/*      */     private static final byte CHARTYPE_DIGIT = 9;
/*      */     private static final byte CHARTYPE_COLON = 10;
/*      */     private static final byte CHARTYPE_EQUAL = 11;
/*      */     private static final byte CHARTYPE_LETTER = 12;
/*      */     private static final byte CHARTYPE_UNDERSCORE = 13;
/*      */     private static final byte CHARTYPE_NONASCII = 14;
/*  643 */     private final byte[] fASCIICharMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 1, 1, 11, 1, 1, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 3, 13, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 1, 1 };
/*      */     private SymbolTable fSymbolTable;
/*      */ 
/*      */     private Scanner(SymbolTable symbolTable)
/*      */     {
/*  665 */       this.fSymbolTable = symbolTable;
/*      */     }
/*      */ 
/*      */     private boolean scanExpr(SymbolTable symbolTable, XPointerHandler.Tokens tokens, String data, int currentOffset, int endOffset)
/*      */       throws XNIException
/*      */     {
/*  678 */       int openParen = 0;
/*  679 */       int closeParen = 0;
/*      */ 
/*  681 */       boolean isQName = false;
/*  682 */       String name = null;
/*  683 */       String prefix = null;
/*  684 */       String schemeData = null;
/*  685 */       StringBuffer schemeDataBuff = new StringBuffer();
/*      */ 
/*  689 */       while (currentOffset != endOffset)
/*      */       {
/*  692 */         int ch = data.charAt(currentOffset);
/*      */ 
/*  695 */         while ((ch == 32) || (ch == 10) || (ch == 9) || (ch == 13)) {
/*  696 */           currentOffset++; if (currentOffset == endOffset) {
/*      */             break;
/*      */           }
/*  699 */           ch = data.charAt(currentOffset);
/*      */         }
/*  701 */         if (currentOffset == endOffset)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  721 */         byte chartype = ch >= 128 ? 14 : this.fASCIICharMap[ch];
/*      */ 
/*  724 */         switch (chartype)
/*      */         {
/*      */         case 4:
/*  727 */           addToken(tokens, 0);
/*  728 */           openParen++;
/*  729 */           currentOffset++;
/*  730 */           break;
/*      */         case 5:
/*  733 */           addToken(tokens, 1);
/*  734 */           closeParen++;
/*  735 */           currentOffset++;
/*  736 */           break;
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*  751 */           if (openParen == 0) {
/*  752 */             int nameOffset = currentOffset;
/*  753 */             currentOffset = scanNCName(data, endOffset, currentOffset);
/*      */ 
/*  756 */             if (currentOffset == nameOffset) {
/*  757 */               XPointerHandler.this.reportError("InvalidShortHandPointer", new Object[] { data });
/*      */ 
/*  759 */               return false;
/*      */             }
/*      */ 
/*  762 */             if (currentOffset < endOffset)
/*  763 */               ch = data.charAt(currentOffset);
/*      */             else {
/*  765 */               ch = -1;
/*      */             }
/*      */ 
/*  768 */             name = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*      */ 
/*  770 */             prefix = XMLSymbols.EMPTY_STRING;
/*      */ 
/*  773 */             if (ch == 58) {
/*  774 */               currentOffset++; if (currentOffset == endOffset) {
/*  775 */                 return false;
/*      */               }
/*      */ 
/*  778 */               ch = data.charAt(currentOffset);
/*  779 */               prefix = name;
/*  780 */               nameOffset = currentOffset;
/*  781 */               currentOffset = scanNCName(data, endOffset, currentOffset);
/*      */ 
/*  784 */               if (currentOffset == nameOffset) {
/*  785 */                 return false;
/*      */               }
/*      */ 
/*  788 */               if (currentOffset < endOffset)
/*  789 */                 ch = data.charAt(currentOffset);
/*      */               else {
/*  791 */                 ch = -1;
/*      */               }
/*      */ 
/*  794 */               isQName = true;
/*  795 */               name = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*      */             }
/*      */ 
/*  800 */             if (currentOffset != endOffset) {
/*  801 */               addToken(tokens, 3);
/*  802 */               XPointerHandler.Tokens.access$800(tokens, prefix);
/*  803 */               XPointerHandler.Tokens.access$800(tokens, name);
/*  804 */               isQName = false;
/*  805 */             } else if (currentOffset == endOffset)
/*      */             {
/*  807 */               addToken(tokens, 2);
/*  808 */               XPointerHandler.Tokens.access$800(tokens, name);
/*  809 */               isQName = false;
/*      */             }
/*      */ 
/*  813 */             closeParen = 0;
/*      */           }
/*  817 */           else if ((openParen > 0) && (closeParen == 0) && (name != null))
/*      */           {
/*  819 */             int dataOffset = currentOffset;
/*  820 */             currentOffset = scanData(data, schemeDataBuff, endOffset, currentOffset);
/*      */ 
/*  823 */             if (currentOffset == dataOffset) {
/*  824 */               XPointerHandler.this.reportError("InvalidSchemeDataInXPointer", new Object[] { data });
/*      */ 
/*  826 */               return false;
/*      */             }
/*      */ 
/*  829 */             if (currentOffset < endOffset)
/*  830 */               ch = data.charAt(currentOffset);
/*      */             else {
/*  832 */               ch = -1;
/*      */             }
/*      */ 
/*  835 */             schemeData = symbolTable.addSymbol(schemeDataBuff.toString());
/*      */ 
/*  837 */             addToken(tokens, 4);
/*  838 */             XPointerHandler.Tokens.access$800(tokens, schemeData);
/*      */ 
/*  841 */             openParen = 0;
/*  842 */             schemeDataBuff.delete(0, schemeDataBuff.length());
/*      */           }
/*      */           else
/*      */           {
/*  847 */             return false;
/*      */           }break;
/*      */         }
/*      */       }
/*  851 */       return true;
/*      */     }
/*      */ 
/*      */     private int scanNCName(String data, int endOffset, int currentOffset)
/*      */     {
/*  865 */       int ch = data.charAt(currentOffset);
/*  866 */       if (ch >= 128) {
/*  867 */         if (!XMLChar.isNameStart(ch))
/*  868 */           return currentOffset;
/*      */       }
/*      */       else {
/*  871 */         byte chartype = this.fASCIICharMap[ch];
/*  872 */         if ((chartype != 12) && (chartype != 13))
/*      */         {
/*  874 */           return currentOffset;
/*      */         }
/*      */       }
/*      */       while (true)
/*      */       {
/*  879 */         currentOffset++; if (currentOffset >= endOffset) break;
/*  880 */         ch = data.charAt(currentOffset);
/*  881 */         if (ch >= 128) {
/*  882 */           if (!XMLChar.isName(ch))
/*  883 */             break;
/*      */         }
/*      */         else {
/*  886 */           byte chartype = this.fASCIICharMap[ch];
/*  887 */           if ((chartype != 12) && (chartype != 9) && (chartype != 7) && (chartype != 6) && (chartype != 13))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  896 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     private int scanData(String data, StringBuffer schemeData, int endOffset, int currentOffset)
/*      */     {
/*  911 */       while (currentOffset != endOffset)
/*      */       {
/*  915 */         int ch = data.charAt(currentOffset);
/*  916 */         byte chartype = ch >= 128 ? 14 : this.fASCIICharMap[ch];
/*      */ 
/*  919 */         if (chartype == 4) {
/*  920 */           schemeData.append(ch);
/*      */ 
/*  922 */           currentOffset = scanData(data, schemeData, endOffset, ++currentOffset);
/*      */ 
/*  924 */           if (currentOffset == endOffset) {
/*  925 */             return currentOffset;
/*      */           }
/*      */ 
/*  928 */           ch = data.charAt(currentOffset);
/*  929 */           chartype = ch >= 128 ? 14 : this.fASCIICharMap[ch];
/*      */ 
/*  932 */           if (chartype != 5) {
/*  933 */             return endOffset;
/*      */           }
/*  935 */           schemeData.append((char)ch);
/*  936 */           currentOffset++;
/*      */         } else {
/*  938 */           if (chartype == 5) {
/*  939 */             return currentOffset;
/*      */           }
/*  941 */           if (chartype == 3) {
/*  942 */             ch = data.charAt(++currentOffset);
/*  943 */             chartype = ch >= 128 ? 14 : this.fASCIICharMap[ch];
/*      */ 
/*  946 */             if ((chartype != 3) && (chartype != 4) && (chartype != 5))
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*  951 */             schemeData.append((char)ch);
/*  952 */             currentOffset++;
/*      */           }
/*      */           else {
/*  955 */             schemeData.append((char)ch);
/*  956 */             currentOffset++;
/*      */           }
/*      */         }
/*      */       }
/*  960 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     protected void addToken(XPointerHandler.Tokens tokens, int token)
/*      */       throws XNIException
/*      */     {
/*  976 */       XPointerHandler.Tokens.access$900(tokens, token);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class Tokens
/*      */   {
/*      */     private static final int XPTRTOKEN_OPEN_PAREN = 0;
/*      */     private static final int XPTRTOKEN_CLOSE_PAREN = 1;
/*      */     private static final int XPTRTOKEN_SHORTHAND = 2;
/*      */     private static final int XPTRTOKEN_SCHEMENAME = 3;
/*      */     private static final int XPTRTOKEN_SCHEMEDATA = 4;
/*  467 */     private final String[] fgTokenNames = { "XPTRTOKEN_OPEN_PAREN", "XPTRTOKEN_CLOSE_PAREN", "XPTRTOKEN_SHORTHAND", "XPTRTOKEN_SCHEMENAME", "XPTRTOKEN_SCHEMEDATA" };
/*      */     private static final int INITIAL_TOKEN_COUNT = 256;
/*  474 */     private int[] fTokens = new int[256];
/*      */ 
/*  476 */     private int fTokenCount = 0;
/*      */     private int fCurrentTokenIndex;
/*      */     private SymbolTable fSymbolTable;
/*  483 */     private Hashtable fTokenNames = new Hashtable();
/*      */ 
/*      */     private Tokens(SymbolTable symbolTable)
/*      */     {
/*  491 */       this.fSymbolTable = symbolTable;
/*      */ 
/*  493 */       this.fTokenNames.put(new Integer(0), "XPTRTOKEN_OPEN_PAREN");
/*      */ 
/*  495 */       this.fTokenNames.put(new Integer(1), "XPTRTOKEN_CLOSE_PAREN");
/*      */ 
/*  497 */       this.fTokenNames.put(new Integer(2), "XPTRTOKEN_SHORTHAND");
/*      */ 
/*  499 */       this.fTokenNames.put(new Integer(3), "XPTRTOKEN_SCHEMENAME");
/*      */ 
/*  501 */       this.fTokenNames.put(new Integer(4), "XPTRTOKEN_SCHEMEDATA");
/*      */     }
/*      */ 
/*      */     private String getTokenString(int token)
/*      */     {
/*  511 */       return (String)this.fTokenNames.get(new Integer(token));
/*      */     }
/*      */ 
/*      */     private void addToken(String tokenStr)
/*      */     {
/*  520 */       Integer tokenInt = (Integer)this.fTokenNames.get(tokenStr);
/*  521 */       if (tokenInt == null) {
/*  522 */         tokenInt = new Integer(this.fTokenNames.size());
/*  523 */         this.fTokenNames.put(tokenInt, tokenStr);
/*      */       }
/*  525 */       addToken(tokenInt.intValue());
/*      */     }
/*      */ 
/*      */     private void addToken(int token)
/*      */     {
/*      */       try
/*      */       {
/*  535 */         this.fTokens[this.fTokenCount] = token;
/*      */       } catch (ArrayIndexOutOfBoundsException ex) {
/*  537 */         int[] oldList = this.fTokens;
/*  538 */         this.fTokens = new int[this.fTokenCount << 1];
/*  539 */         System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
/*  540 */         this.fTokens[this.fTokenCount] = token;
/*      */       }
/*  542 */       this.fTokenCount += 1;
/*      */     }
/*      */ 
/*      */     private void rewind()
/*      */     {
/*  549 */       this.fCurrentTokenIndex = 0;
/*      */     }
/*      */ 
/*      */     private boolean hasMore()
/*      */     {
/*  557 */       return this.fCurrentTokenIndex < this.fTokenCount;
/*      */     }
/*      */ 
/*      */     private int nextToken()
/*      */       throws XNIException
/*      */     {
/*  568 */       if (this.fCurrentTokenIndex == this.fTokenCount) {
/*  569 */         XPointerHandler.this.reportError("XPointerProcessingError", null);
/*      */       }
/*  571 */       return this.fTokens[(this.fCurrentTokenIndex++)];
/*      */     }
/*      */ 
/*      */     private int peekToken()
/*      */       throws XNIException
/*      */     {
/*  582 */       if (this.fCurrentTokenIndex == this.fTokenCount) {
/*  583 */         XPointerHandler.this.reportError("XPointerProcessingError", null);
/*      */       }
/*  585 */       return this.fTokens[this.fCurrentTokenIndex];
/*      */     }
/*      */ 
/*      */     private String nextTokenAsString()
/*      */       throws XNIException
/*      */     {
/*  597 */       String tokenStrint = getTokenString(nextToken());
/*  598 */       if (tokenStrint == null) {
/*  599 */         XPointerHandler.this.reportError("XPointerProcessingError", null);
/*      */       }
/*  601 */       return tokenStrint;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xpointer.XPointerHandler
 * JD-Core Version:    0.6.2
 */