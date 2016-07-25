/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.zip.Deflater;
/*      */ import java.util.zip.DeflaterOutputStream;
/*      */ 
/*      */ class CodingChooser
/*      */ {
/*      */   int verbose;
/*      */   int effort;
/*   49 */   boolean optUseHistogram = true;
/*   50 */   boolean optUsePopulationCoding = true;
/*   51 */   boolean optUseAdaptiveCoding = true;
/*      */   boolean disablePopCoding;
/*      */   boolean disableRunCoding;
/*   54 */   boolean topLevel = true;
/*      */   double fuzz;
/*      */   Coding[] allCodingChoices;
/*      */   Choice[] choices;
/*      */   ByteArrayOutputStream context;
/*      */   CodingChooser popHelper;
/*      */   CodingChooser runHelper;
/*      */   Random stress;
/*      */   private int[] values;
/*      */   private int start;
/*      */   private int end;
/*      */   private int[] deltas;
/*      */   private int min;
/*      */   private int max;
/*      */   private Histogram vHist;
/*      */   private Histogram dHist;
/*      */   private int searchOrder;
/*      */   private Choice regularChoice;
/*      */   private Choice bestChoice;
/*      */   private CodingMethod bestMethod;
/*      */   private int bestByteSize;
/*      */   private int bestZipSize;
/*      */   private int targetSize;
/*      */   public static final int MIN_EFFORT = 1;
/*      */   public static final int MID_EFFORT = 5;
/*      */   public static final int MAX_EFFORT = 9;
/*      */   public static final int POP_EFFORT = 4;
/*      */   public static final int RUN_EFFORT = 3;
/*      */   public static final int BYTE_SIZE = 0;
/*      */   public static final int ZIP_SIZE = 1;
/* 1181 */   private Sizer zipSizer = new Sizer();
/* 1182 */   private Deflater zipDef = new Deflater();
/* 1183 */   private DeflaterOutputStream zipOut = new DeflaterOutputStream(this.zipSizer, this.zipDef);
/* 1184 */   private Sizer byteSizer = new Sizer(this.zipOut);
/* 1185 */   private Sizer byteOnlySizer = new Sizer();
/*      */ 
/*      */   CodingChooser(int paramInt, Coding[] paramArrayOfCoding)
/*      */   {
/*  117 */     PropMap localPropMap = Utils.currentPropMap();
/*  118 */     if (localPropMap != null) {
/*  119 */       this.verbose = Math.max(localPropMap.getInteger("com.sun.java.util.jar.pack.verbose"), localPropMap.getInteger("com.sun.java.util.jar.pack.verbose.coding"));
/*      */ 
/*  122 */       this.optUseHistogram = (!localPropMap.getBoolean("com.sun.java.util.jar.pack.no.histogram"));
/*      */ 
/*  124 */       this.optUsePopulationCoding = (!localPropMap.getBoolean("com.sun.java.util.jar.pack.no.population.coding"));
/*      */ 
/*  126 */       this.optUseAdaptiveCoding = (!localPropMap.getBoolean("com.sun.java.util.jar.pack.no.adaptive.coding"));
/*      */ 
/*  128 */       i = localPropMap.getInteger("com.sun.java.util.jar.pack.stress.coding");
/*      */ 
/*  130 */       if (i != 0) {
/*  131 */         this.stress = new Random(i);
/*      */       }
/*      */     }
/*  134 */     this.effort = paramInt;
/*      */ 
/*  139 */     this.allCodingChoices = paramArrayOfCoding;
/*      */ 
/*  145 */     this.fuzz = (1.0D + 0.0025D * (paramInt - 5));
/*      */ 
/*  147 */     int i = 0;
/*  148 */     for (int j = 0; j < paramArrayOfCoding.length; j++) {
/*  149 */       if (paramArrayOfCoding[j] != null)
/*  150 */         i++;
/*      */     }
/*  152 */     this.choices = new Choice[i];
/*  153 */     i = 0;
/*      */     Object localObject;
/*  154 */     for (j = 0; j < paramArrayOfCoding.length; j++)
/*  155 */       if (paramArrayOfCoding[j] != null) {
/*  156 */         localObject = new int[this.choices.length];
/*  157 */         this.choices[(i++)] = new Choice(paramArrayOfCoding[j], j, (int[])localObject);
/*      */       }
/*  159 */     for (j = 0; j < this.choices.length; j++) {
/*  160 */       localObject = this.choices[j].coding;
/*  161 */       assert (((Coding)localObject).distanceFrom((Coding)localObject) == 0);
/*  162 */       for (int k = 0; k < j; k++) {
/*  163 */         Coding localCoding = this.choices[k].coding;
/*  164 */         int m = ((Coding)localObject).distanceFrom(localCoding);
/*  165 */         assert (m > 0);
/*  166 */         assert (m == localCoding.distanceFrom((Coding)localObject));
/*  167 */         this.choices[j].distance[k] = m;
/*  168 */         this.choices[k].distance[j] = m;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Choice makeExtraChoice(Coding paramCoding) {
/*  174 */     int[] arrayOfInt = new int[this.choices.length];
/*  175 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  176 */       Coding localCoding = this.choices[i].coding;
/*  177 */       int j = paramCoding.distanceFrom(localCoding);
/*  178 */       assert (j > 0);
/*  179 */       assert (j == localCoding.distanceFrom(paramCoding));
/*  180 */       arrayOfInt[i] = j;
/*      */     }
/*  182 */     Choice localChoice = new Choice(paramCoding, -1, arrayOfInt);
/*  183 */     localChoice.reset();
/*  184 */     return localChoice;
/*      */   }
/*      */ 
/*      */   ByteArrayOutputStream getContext() {
/*  188 */     if (this.context == null)
/*  189 */       this.context = new ByteArrayOutputStream(65536);
/*  190 */     return this.context;
/*      */   }
/*      */ 
/*      */   private void reset(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  209 */     this.values = paramArrayOfInt;
/*  210 */     this.start = paramInt1;
/*  211 */     this.end = paramInt2;
/*  212 */     this.deltas = null;
/*  213 */     this.min = 2147483647;
/*  214 */     this.max = -2147483648;
/*  215 */     this.vHist = null;
/*  216 */     this.dHist = null;
/*  217 */     this.searchOrder = 0;
/*  218 */     this.regularChoice = null;
/*  219 */     this.bestChoice = null;
/*  220 */     this.bestMethod = null;
/*  221 */     this.bestZipSize = 2147483647;
/*  222 */     this.bestByteSize = 2147483647;
/*  223 */     this.targetSize = 2147483647;
/*      */   }
/*      */ 
/*      */   CodingMethod choose(int[] paramArrayOfInt1, int paramInt1, int paramInt2, Coding paramCoding, int[] paramArrayOfInt2)
/*      */   {
/*  238 */     reset(paramArrayOfInt1, paramInt1, paramInt2);
/*      */ 
/*  240 */     if ((this.effort <= 1) || (paramInt1 >= paramInt2)) {
/*  241 */       if (paramArrayOfInt2 != null) {
/*  242 */         int[] arrayOfInt = computeSizePrivate(paramCoding);
/*  243 */         paramArrayOfInt2[0] = arrayOfInt[0];
/*  244 */         paramArrayOfInt2[1] = arrayOfInt[1];
/*      */       }
/*  246 */       return paramCoding;
/*      */     }
/*      */ 
/*  249 */     if (this.optUseHistogram) {
/*  250 */       getValueHistogram();
/*  251 */       getDeltaHistogram();
/*      */     }
/*      */     int j;
/*  254 */     for (int i = paramInt1; i < paramInt2; i++) {
/*  255 */       j = paramArrayOfInt1[i];
/*  256 */       if (this.min > j) this.min = j;
/*  257 */       if (this.max < j) this.max = j;
/*      */ 
/*      */     }
/*      */ 
/*  261 */     i = markUsableChoices(paramCoding);
/*      */ 
/*  263 */     if (this.stress != null)
/*      */     {
/*  265 */       j = this.stress.nextInt(i * 2 + 4);
/*  266 */       Object localObject1 = null;
/*  267 */       for (k = 0; k < this.choices.length; k++) {
/*  268 */         Choice localChoice = this.choices[k];
/*  269 */         if ((localChoice.searchOrder >= 0) && (j-- == 0)) {
/*  270 */           localObject1 = localChoice.coding;
/*  271 */           break;
/*      */         }
/*      */       }
/*  274 */       if (localObject1 == null) {
/*  275 */         if ((j & 0x7) != 0) {
/*  276 */           localObject1 = paramCoding;
/*      */         }
/*      */         else {
/*  279 */           localObject1 = stressCoding(this.min, this.max);
/*      */         }
/*      */       }
/*  282 */       if ((!this.disablePopCoding) && (this.optUsePopulationCoding) && (this.effort >= 4))
/*      */       {
/*  285 */         localObject1 = stressPopCoding((CodingMethod)localObject1);
/*      */       }
/*  287 */       if ((!this.disableRunCoding) && (this.optUseAdaptiveCoding) && (this.effort >= 3))
/*      */       {
/*  290 */         localObject1 = stressAdaptiveCoding((CodingMethod)localObject1);
/*      */       }
/*  292 */       return localObject1;
/*      */     }
/*      */ 
/*  295 */     double d = 1.0D;
/*  296 */     for (int k = this.effort; k < 9; k++) {
/*  297 */       d /= 1.414D;
/*      */     }
/*  299 */     k = (int)Math.ceil(i * d);
/*      */ 
/*  302 */     this.bestChoice = this.regularChoice;
/*  303 */     evaluate(this.regularChoice);
/*  304 */     int m = updateDistances(this.regularChoice);
/*      */ 
/*  307 */     int n = this.bestZipSize;
/*  308 */     int i1 = this.bestByteSize;
/*      */ 
/*  310 */     if ((this.regularChoice.coding == paramCoding) && (this.topLevel))
/*      */     {
/*  317 */       i2 = BandStructure.encodeEscapeValue(115, paramCoding);
/*  318 */       if (paramCoding.canRepresentSigned(i2)) {
/*  319 */         int i3 = paramCoding.getLength(i2);
/*      */ 
/*  322 */         this.regularChoice.zipSize -= i3;
/*  323 */         this.bestByteSize = this.regularChoice.byteSize;
/*  324 */         this.bestZipSize = this.regularChoice.zipSize;
/*      */       }
/*      */     }
/*      */ 
/*  328 */     int i2 = 1;
/*      */ 
/*  330 */     while (this.searchOrder < k)
/*      */     {
/*  332 */       if (i2 > m) i2 = 1;
/*  333 */       int i4 = m / i2;
/*  334 */       int i5 = m / (i2 *= 2) + 1;
/*  335 */       localObject2 = findChoiceNear(this.bestChoice, i4, i5);
/*  336 */       if (localObject2 != null) {
/*  337 */         assert (((Choice)localObject2).coding.canRepresent(this.min, this.max));
/*  338 */         evaluate((Choice)localObject2);
/*  339 */         int i6 = updateDistances((Choice)localObject2);
/*  340 */         if (localObject2 == this.bestChoice) {
/*  341 */           m = i6;
/*  342 */           if (this.verbose > 5) Utils.log.info("maxd = " + m);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  347 */     Object localObject2 = this.bestChoice.coding;
/*  348 */     assert (localObject2 == this.bestMethod);
/*      */ 
/*  350 */     if (this.verbose > 2) {
/*  351 */       Utils.log.info("chooser: plain result=" + this.bestChoice + " after " + this.bestChoice.searchOrder + " rounds, " + (this.regularChoice.zipSize - this.bestZipSize) + " fewer bytes than regular " + paramCoding);
/*      */     }
/*  353 */     this.bestChoice = null;
/*      */ 
/*  355 */     if ((!this.disablePopCoding) && (this.optUsePopulationCoding) && (this.effort >= 4) && ((this.bestMethod instanceof Coding)))
/*      */     {
/*  359 */       tryPopulationCoding((Coding)localObject2);
/*      */     }
/*      */ 
/*  362 */     if ((!this.disableRunCoding) && (this.optUseAdaptiveCoding) && (this.effort >= 3) && ((this.bestMethod instanceof Coding)))
/*      */     {
/*  366 */       tryAdaptiveCoding((Coding)localObject2);
/*      */     }
/*      */ 
/*  370 */     if (paramArrayOfInt2 != null) {
/*  371 */       paramArrayOfInt2[0] = this.bestByteSize;
/*  372 */       paramArrayOfInt2[1] = this.bestZipSize;
/*      */     }
/*  374 */     if (this.verbose > 1) {
/*  375 */       Utils.log.info("chooser: result=" + this.bestMethod + " " + (n - this.bestZipSize) + " fewer bytes than regular " + paramCoding + "; win=" + pct(n - this.bestZipSize, n));
/*      */     }
/*      */ 
/*  380 */     CodingMethod localCodingMethod = this.bestMethod;
/*  381 */     reset(null, 0, 0);
/*  382 */     return localCodingMethod;
/*      */   }
/*      */   CodingMethod choose(int[] paramArrayOfInt, int paramInt1, int paramInt2, Coding paramCoding) {
/*  385 */     return choose(paramArrayOfInt, paramInt1, paramInt2, paramCoding, null);
/*      */   }
/*      */   CodingMethod choose(int[] paramArrayOfInt1, Coding paramCoding, int[] paramArrayOfInt2) {
/*  388 */     return choose(paramArrayOfInt1, 0, paramArrayOfInt1.length, paramCoding, paramArrayOfInt2);
/*      */   }
/*      */   CodingMethod choose(int[] paramArrayOfInt, Coding paramCoding) {
/*  391 */     return choose(paramArrayOfInt, 0, paramArrayOfInt.length, paramCoding, null);
/*      */   }
/*      */ 
/*      */   private int markUsableChoices(Coding paramCoding) {
/*  395 */     int i = 0;
/*      */     Choice localChoice;
/*  396 */     for (int j = 0; j < this.choices.length; j++) {
/*  397 */       localChoice = this.choices[j];
/*  398 */       localChoice.reset();
/*  399 */       if (!localChoice.coding.canRepresent(this.min, this.max))
/*      */       {
/*  401 */         localChoice.searchOrder = -1;
/*  402 */         if ((this.verbose > 1) && (localChoice.coding == paramCoding))
/*  403 */           Utils.log.info("regular coding cannot represent [" + this.min + ".." + this.max + "]: " + paramCoding);
/*      */       }
/*      */       else
/*      */       {
/*  407 */         if (localChoice.coding == paramCoding)
/*  408 */           this.regularChoice = localChoice;
/*  409 */         i++;
/*      */       }
/*      */     }
/*  411 */     if ((this.regularChoice == null) && (paramCoding.canRepresent(this.min, this.max))) {
/*  412 */       this.regularChoice = makeExtraChoice(paramCoding);
/*  413 */       if (this.verbose > 1) {
/*  414 */         Utils.log.info("*** regular choice is extra: " + this.regularChoice.coding);
/*      */       }
/*      */     }
/*  417 */     if (this.regularChoice == null) {
/*  418 */       for (j = 0; j < this.choices.length; j++) {
/*  419 */         localChoice = this.choices[j];
/*  420 */         if (localChoice.searchOrder != -1) {
/*  421 */           this.regularChoice = localChoice;
/*  422 */           break;
/*      */         }
/*      */       }
/*  425 */       if (this.verbose > 1) {
/*  426 */         Utils.log.info("*** regular choice does not apply " + paramCoding);
/*  427 */         Utils.log.info("    using instead " + this.regularChoice.coding);
/*      */       }
/*      */     }
/*  430 */     if (this.verbose > 2) {
/*  431 */       Utils.log.info("chooser: #choices=" + i + " [" + this.min + ".." + this.max + "]");
/*  432 */       if (this.verbose > 4) {
/*  433 */         for (j = 0; j < this.choices.length; j++) {
/*  434 */           localChoice = this.choices[j];
/*  435 */           if (localChoice.searchOrder >= 0)
/*  436 */             Utils.log.info("  " + localChoice);
/*      */         }
/*      */       }
/*      */     }
/*  440 */     return i;
/*      */   }
/*      */ 
/*      */   private Choice findChoiceNear(Choice paramChoice, int paramInt1, int paramInt2)
/*      */   {
/*  447 */     if (this.verbose > 5)
/*  448 */       Utils.log.info("findChoice " + paramInt1 + ".." + paramInt2 + " near: " + paramChoice);
/*  449 */     int[] arrayOfInt = paramChoice.distance;
/*  450 */     Object localObject = null;
/*  451 */     for (int i = 0; i < this.choices.length; i++) {
/*  452 */       Choice localChoice = this.choices[i];
/*  453 */       if (localChoice.searchOrder >= this.searchOrder)
/*      */       {
/*  456 */         if ((arrayOfInt[i] >= paramInt2) && (arrayOfInt[i] <= paramInt1))
/*      */         {
/*  458 */           if ((localChoice.minDistance >= paramInt2) && (localChoice.minDistance <= paramInt1)) {
/*  459 */             if (this.verbose > 5)
/*  460 */               Utils.log.info("findChoice => good " + localChoice);
/*  461 */             return localChoice;
/*      */           }
/*  463 */           localObject = localChoice;
/*      */         }
/*      */       }
/*      */     }
/*  466 */     if (this.verbose > 5)
/*  467 */       Utils.log.info("findChoice => found " + localObject);
/*  468 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void evaluate(Choice paramChoice) {
/*  472 */     assert (paramChoice.searchOrder == 2147483647);
/*  473 */     paramChoice.searchOrder = (this.searchOrder++);
/*      */     int i;
/*      */     Object localObject;
/*  475 */     if ((paramChoice == this.bestChoice) || (paramChoice.isExtra())) {
/*  476 */       i = 1;
/*  477 */     } else if (this.optUseHistogram) {
/*  478 */       localObject = getHistogram(paramChoice.coding.isDelta());
/*  479 */       paramChoice.histSize = ((int)Math.ceil(((Histogram)localObject).getBitLength(paramChoice.coding) / 8.0D));
/*  480 */       paramChoice.byteSize = paramChoice.histSize;
/*  481 */       i = paramChoice.byteSize <= this.targetSize ? 1 : 0;
/*      */     } else {
/*  483 */       i = 1;
/*      */     }
/*  485 */     if (i != 0) {
/*  486 */       localObject = computeSizePrivate(paramChoice.coding);
/*  487 */       paramChoice.byteSize = localObject[0];
/*  488 */       paramChoice.zipSize = localObject[1];
/*  489 */       if (noteSizes(paramChoice.coding, paramChoice.byteSize, paramChoice.zipSize))
/*  490 */         this.bestChoice = paramChoice;
/*      */     }
/*  492 */     if ((paramChoice.histSize >= 0) && 
/*  493 */       (!$assertionsDisabled) && (paramChoice.byteSize != paramChoice.histSize)) throw new AssertionError();
/*      */ 
/*  495 */     if (this.verbose > 4)
/*  496 */       Utils.log.info("evaluated " + paramChoice);
/*      */   }
/*      */ 
/*      */   private boolean noteSizes(CodingMethod paramCodingMethod, int paramInt1, int paramInt2)
/*      */   {
/*  501 */     assert ((paramInt2 > 0) && (paramInt1 > 0));
/*  502 */     int i = paramInt2 < this.bestZipSize ? 1 : 0;
/*  503 */     if (this.verbose > 3) {
/*  504 */       Utils.log.info("computed size " + paramCodingMethod + " " + paramInt1 + "/zs=" + paramInt2 + ((i != 0) && (this.bestMethod != null) ? " better by " + pct(this.bestZipSize - paramInt2, paramInt2) : ""));
/*      */     }
/*      */ 
/*  508 */     if (i != 0) {
/*  509 */       this.bestMethod = paramCodingMethod;
/*  510 */       this.bestZipSize = paramInt2;
/*  511 */       this.bestByteSize = paramInt1;
/*  512 */       this.targetSize = ((int)(paramInt1 * this.fuzz));
/*  513 */       return true;
/*      */     }
/*  515 */     return false;
/*      */   }
/*      */ 
/*      */   private int updateDistances(Choice paramChoice)
/*      */   {
/*  522 */     int[] arrayOfInt = paramChoice.distance;
/*  523 */     int i = 0;
/*  524 */     for (int j = 0; j < this.choices.length; j++) {
/*  525 */       Choice localChoice = this.choices[j];
/*  526 */       if (localChoice.searchOrder >= this.searchOrder)
/*      */       {
/*  528 */         int k = arrayOfInt[j];
/*  529 */         if (this.verbose > 5)
/*  530 */           Utils.log.info("evaluate dist " + k + " to " + localChoice);
/*  531 */         int m = localChoice.minDistance;
/*  532 */         if (m > k)
/*  533 */           localChoice.minDistance = (m = k);
/*  534 */         if (i < k) {
/*  535 */           i = k;
/*      */         }
/*      */       }
/*      */     }
/*  539 */     if (this.verbose > 5)
/*  540 */       Utils.log.info("evaluate maxd => " + i);
/*  541 */     return i;
/*      */   }
/*      */ 
/*      */   public void computeSize(CodingMethod paramCodingMethod, int[] paramArrayOfInt1, int paramInt1, int paramInt2, int[] paramArrayOfInt2)
/*      */   {
/*  548 */     if (paramInt2 <= paramInt1)
/*      */     {
/*      */       int tmp13_12 = 0; paramArrayOfInt2[1] = tmp13_12; paramArrayOfInt2[0] = tmp13_12;
/*  550 */       return;
/*      */     }
/*      */     try {
/*  553 */       resetData();
/*  554 */       paramCodingMethod.writeArrayTo(this.byteSizer, paramArrayOfInt1, paramInt1, paramInt2);
/*  555 */       paramArrayOfInt2[0] = getByteSize();
/*  556 */       paramArrayOfInt2[1] = getZipSize();
/*      */     } catch (IOException localIOException) {
/*  558 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*  562 */   public void computeSize(CodingMethod paramCodingMethod, int[] paramArrayOfInt1, int[] paramArrayOfInt2) { computeSize(paramCodingMethod, paramArrayOfInt1, 0, paramArrayOfInt1.length, paramArrayOfInt2); }
/*      */ 
/*      */   public int[] computeSize(CodingMethod paramCodingMethod, int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*  565 */     int[] arrayOfInt = { 0, 0 };
/*  566 */     computeSize(paramCodingMethod, paramArrayOfInt, paramInt1, paramInt2, arrayOfInt);
/*  567 */     return arrayOfInt;
/*      */   }
/*      */   public int[] computeSize(CodingMethod paramCodingMethod, int[] paramArrayOfInt) {
/*  570 */     return computeSize(paramCodingMethod, paramArrayOfInt, 0, paramArrayOfInt.length);
/*      */   }
/*      */ 
/*      */   private int[] computeSizePrivate(CodingMethod paramCodingMethod) {
/*  574 */     int[] arrayOfInt = { 0, 0 };
/*  575 */     computeSize(paramCodingMethod, this.values, this.start, this.end, arrayOfInt);
/*  576 */     return arrayOfInt;
/*      */   }
/*      */   public int computeByteSize(CodingMethod paramCodingMethod, int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*  579 */     int i = paramInt2 - paramInt1;
/*  580 */     if (i < 0) {
/*  581 */       return 0;
/*      */     }
/*  583 */     if ((paramCodingMethod instanceof Coding)) {
/*  584 */       Coding localCoding = (Coding)paramCodingMethod;
/*  585 */       int j = localCoding.getLength(paramArrayOfInt, paramInt1, paramInt2);
/*      */       int k;
/*  588 */       assert (j == (k = countBytesToSizer(paramCodingMethod, paramArrayOfInt, paramInt1, paramInt2))) : (paramCodingMethod + " : " + j + " != " + k);
/*  589 */       return j;
/*      */     }
/*  591 */     return countBytesToSizer(paramCodingMethod, paramArrayOfInt, paramInt1, paramInt2);
/*      */   }
/*      */   private int countBytesToSizer(CodingMethod paramCodingMethod, int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*      */     try {
/*  595 */       this.byteOnlySizer.reset();
/*  596 */       paramCodingMethod.writeArrayTo(this.byteOnlySizer, paramArrayOfInt, paramInt1, paramInt2);
/*  597 */       return this.byteOnlySizer.getSize();
/*      */     } catch (IOException localIOException) {
/*  599 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   int[] getDeltas(int paramInt1, int paramInt2) {
/*  604 */     if ((paramInt1 | paramInt2) != 0)
/*  605 */       return Coding.makeDeltas(this.values, this.start, this.end, paramInt1, paramInt2);
/*  606 */     if (this.deltas == null) {
/*  607 */       this.deltas = Coding.makeDeltas(this.values, this.start, this.end, 0, 0);
/*      */     }
/*  609 */     return this.deltas;
/*      */   }
/*      */   Histogram getValueHistogram() {
/*  612 */     if (this.vHist == null) {
/*  613 */       this.vHist = new Histogram(this.values, this.start, this.end);
/*  614 */       if (this.verbose > 3)
/*  615 */         this.vHist.print("vHist", System.out);
/*  616 */       else if (this.verbose > 1) {
/*  617 */         this.vHist.print("vHist", null, System.out);
/*      */       }
/*      */     }
/*  620 */     return this.vHist;
/*      */   }
/*      */   Histogram getDeltaHistogram() {
/*  623 */     if (this.dHist == null) {
/*  624 */       this.dHist = new Histogram(getDeltas(0, 0));
/*  625 */       if (this.verbose > 3)
/*  626 */         this.dHist.print("dHist", System.out);
/*  627 */       else if (this.verbose > 1) {
/*  628 */         this.dHist.print("dHist", null, System.out);
/*      */       }
/*      */     }
/*  631 */     return this.dHist;
/*      */   }
/*      */   Histogram getHistogram(boolean paramBoolean) {
/*  634 */     return paramBoolean ? getDeltaHistogram() : getValueHistogram();
/*      */   }
/*      */ 
/*      */   private void tryPopulationCoding(Coding paramCoding)
/*      */   {
/*  639 */     Histogram localHistogram = getValueHistogram();
/*      */ 
/*  642 */     Coding localCoding1 = paramCoding.getValueCoding();
/*  643 */     Coding localCoding2 = BandStructure.UNSIGNED5.setL(64);
/*  644 */     Coding localCoding3 = paramCoding.getValueCoding();
/*      */ 
/*  655 */     int i = 4 + Math.max(localCoding1.getLength(this.min), localCoding1.getLength(this.max));
/*      */ 
/*  659 */     int m = localCoding2.getLength(0);
/*  660 */     int j = m * (this.end - this.start);
/*      */ 
/*  662 */     int k = (int)Math.ceil(localHistogram.getBitLength(localCoding3) / 8.0D);
/*      */ 
/*  664 */     int n = i + j + k;
/*  665 */     Coding localCoding4 = 0;
/*      */ 
/*  668 */     int[] arrayOfInt1 = new int[1 + localHistogram.getTotalLength()];
/*      */ 
/*  672 */     int i1 = -1;
/*  673 */     int i2 = -1;
/*      */ 
/*  676 */     int[][] arrayOfInt = localHistogram.getMatrix();
/*  677 */     int i3 = -1;
/*  678 */     int i4 = 1;
/*  679 */     int i5 = 0;
/*  680 */     for (Coding localCoding5 = 1; localCoding5 <= localHistogram.getTotalLength(); localCoding5++)
/*      */     {
/*  684 */       if (i4 == 1) {
/*  685 */         i3++;
/*  686 */         i5 = arrayOfInt[i3][0];
/*  687 */         i4 = arrayOfInt[i3].length;
/*      */       }
/*  689 */       int i6 = arrayOfInt[i3][(--i4)];
/*  690 */       arrayOfInt1[localCoding5] = i6;
/*  691 */       int i7 = localCoding1.getLength(i6);
/*  692 */       i += i7;
/*      */ 
/*  694 */       int i8 = i5;
/*  695 */       int i9 = localCoding5;
/*  696 */       j += (localCoding2.getLength(i9) - m) * i8;
/*      */ 
/*  700 */       k -= i7 * i8;
/*  701 */       int i10 = i + j + k;
/*      */ 
/*  703 */       if (n > i10) {
/*  704 */         if (i10 <= this.targetSize) {
/*  705 */           i2 = localCoding5;
/*  706 */           if (i1 < 0)
/*  707 */             i1 = localCoding5;
/*  708 */           if (this.verbose > 4) {
/*  709 */             Utils.log.info("better pop-size at fvc=" + localCoding5 + " by " + pct(n - i10, n));
/*      */           }
/*      */         }
/*      */ 
/*  713 */         n = i10;
/*  714 */         localCoding4 = localCoding5;
/*      */       }
/*      */     }
/*  717 */     if (i1 < 0) {
/*  718 */       if (this.verbose > 1)
/*      */       {
/*  720 */         if (this.verbose > 1) {
/*  721 */           Utils.log.info("no good pop-size; best was " + n + " at " + localCoding4 + " worse by " + pct(n - this.bestByteSize, this.bestByteSize));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  727 */       return;
/*      */     }
/*  729 */     if (this.verbose > 1) {
/*  730 */       Utils.log.info("initial best pop-size at fvc=" + localCoding4 + " in [" + i1 + ".." + i2 + "]" + " by " + pct(this.bestByteSize - n, this.bestByteSize));
/*      */     }
/*      */ 
/*  734 */     localCoding5 = this.bestZipSize;
/*      */ 
/*  745 */     int[] arrayOfInt2 = PopulationCoding.LValuesCoded;
/*  746 */     ArrayList localArrayList1 = new ArrayList();
/*  747 */     ArrayList localArrayList2 = new ArrayList();
/*  748 */     ArrayList localArrayList3 = new ArrayList();
/*      */     int i11;
/*      */     Coding localCoding7;
/*      */     Object localObject2;
/*  750 */     if (localCoding4 <= 255) {
/*  751 */       localArrayList1.add(BandStructure.BYTE1);
/*      */     } else {
/*  753 */       i11 = 5;
/*  754 */       int i12 = this.effort > 4 ? 1 : 0;
/*  755 */       if (i12 != 0)
/*  756 */         localArrayList2.add(BandStructure.BYTE1.setS(1));
/*  757 */       for (int i14 = arrayOfInt2.length - 1; i14 >= 1; i14--) {
/*  758 */         int i15 = arrayOfInt2[i14];
/*  759 */         Coding localCoding6 = PopulationCoding.fitTokenCoding(i1, i15);
/*  760 */         localCoding7 = PopulationCoding.fitTokenCoding(localCoding4, i15);
/*  761 */         localObject2 = PopulationCoding.fitTokenCoding(i2, i15);
/*  762 */         if (localCoding7 != null) {
/*  763 */           if (!localArrayList1.contains(localCoding7))
/*  764 */             localArrayList1.add(localCoding7);
/*  765 */           if (i11 > localCoding7.B())
/*  766 */             i11 = localCoding7.B();
/*      */         }
/*  768 */         if (i12 != 0) {
/*  769 */           if (localObject2 == null) localObject2 = localCoding7;
/*  770 */           for (int i18 = localCoding6.B(); i18 <= ((Coding)localObject2).B(); i18++) {
/*  771 */             if ((i18 != localCoding7.B()) && 
/*  772 */               (i18 != 1)) {
/*  773 */               Coding localCoding8 = ((Coding)localObject2).setB(i18).setS(1);
/*  774 */               if (!localArrayList2.contains(localCoding8))
/*  775 */                 localArrayList2.add(localCoding8);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  780 */       for (localIterator2 = localArrayList1.iterator(); localIterator2.hasNext(); ) {
/*  781 */         localObject1 = (Coding)localIterator2.next();
/*  782 */         if (((Coding)localObject1).B() > i11) {
/*  783 */           localIterator2.remove();
/*  784 */           localArrayList3.add(0, localObject1);
/*      */         }
/*      */       }
/*      */     }
/*  788 */     ArrayList localArrayList4 = new ArrayList();
/*  789 */     Iterator localIterator1 = localArrayList1.iterator();
/*  790 */     Iterator localIterator2 = localArrayList2.iterator();
/*  791 */     Object localObject1 = localArrayList3.iterator();
/*  792 */     while ((localIterator1.hasNext()) || (localIterator2.hasNext()) || (((Iterator)localObject1).hasNext())) {
/*  793 */       if (localIterator1.hasNext()) localArrayList4.add(localIterator1.next());
/*  794 */       if (localIterator2.hasNext()) localArrayList4.add(localIterator2.next());
/*  795 */       if (((Iterator)localObject1).hasNext()) localArrayList4.add(((Iterator)localObject1).next());
/*      */     }
/*  797 */     localArrayList1.clear();
/*  798 */     localArrayList2.clear();
/*  799 */     localArrayList3.clear();
/*  800 */     int i13 = localArrayList4.size();
/*  801 */     if (this.effort == 4) {
/*  802 */       i13 = 2;
/*  803 */     } else if (i13 > 4) {
/*  804 */       i13 -= 4;
/*  805 */       i13 = i13 * (this.effort - 4) / 5;
/*      */ 
/*  807 */       i13 += 4;
/*      */     }
/*  809 */     if (localArrayList4.size() > i13) {
/*  810 */       if (this.verbose > 4)
/*  811 */         Utils.log.info("allFits before clip: " + localArrayList4);
/*  812 */       localArrayList4.subList(i13, localArrayList4.size()).clear();
/*      */     }
/*  814 */     if (this.verbose > 3)
/*  815 */       Utils.log.info("allFits: " + localArrayList4);
/*  816 */     for (localIterator2 = localArrayList4.iterator(); localIterator2.hasNext(); ) { localObject1 = (Coding)localIterator2.next();
/*  817 */       int i16 = 0;
/*  818 */       if (((Coding)localObject1).S() == 1)
/*      */       {
/*  820 */         i16 = 1;
/*  821 */         localObject1 = ((Coding)localObject1).setS(0);
/*      */       }
/*      */       int i17;
/*  824 */       if (i16 == 0) {
/*  825 */         localCoding7 = localCoding4;
/*  826 */         assert (((Coding)localObject1).umax() >= localCoding7);
/*  827 */         if ((!$assertionsDisabled) && (((Coding)localObject1).B() != 1) && (((Coding)localObject1).setB(((Coding)localObject1).B() - 1).umax() >= localCoding7)) throw new AssertionError(); 
/*      */       }
/*  829 */       else { i17 = Math.min(((Coding)localObject1).umax(), i2);
/*  830 */         if ((i17 < i1) || 
/*  832 */           (i17 == localCoding4))
/*      */           continue;
/*      */       }
/*  835 */       localObject2 = new PopulationCoding();
/*  836 */       ((PopulationCoding)localObject2).setHistogram(localHistogram);
/*  837 */       ((PopulationCoding)localObject2).setL(((Coding)localObject1).L());
/*  838 */       ((PopulationCoding)localObject2).setFavoredValues(arrayOfInt1, i17);
/*  839 */       assert (((PopulationCoding)localObject2).tokenCoding == localObject1);
/*  840 */       ((PopulationCoding)localObject2).resortFavoredValues();
/*  841 */       int[] arrayOfInt3 = computePopSizePrivate((PopulationCoding)localObject2, localCoding1, localCoding3);
/*      */ 
/*  844 */       noteSizes((CodingMethod)localObject2, arrayOfInt3[0], 4 + arrayOfInt3[1]);
/*      */     }
/*  846 */     if (this.verbose > 3) {
/*  847 */       Utils.log.info("measured best pop, size=" + this.bestByteSize + "/zs=" + this.bestZipSize + " better by " + pct(localCoding5 - this.bestZipSize, localCoding5));
/*      */ 
/*  851 */       if (this.bestZipSize < localCoding5)
/*  852 */         Utils.log.info(">>> POP WINS BY " + (localCoding5 - this.bestZipSize));
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] computePopSizePrivate(PopulationCoding paramPopulationCoding, Coding paramCoding1, Coding paramCoding2)
/*      */   {
/*  862 */     if (this.popHelper == null) {
/*  863 */       this.popHelper = new CodingChooser(this.effort, this.allCodingChoices);
/*  864 */       if (this.stress != null)
/*  865 */         this.popHelper.addStressSeed(this.stress.nextInt());
/*  866 */       this.popHelper.topLevel = false;
/*  867 */       this.popHelper.verbose -= 1;
/*  868 */       this.popHelper.disablePopCoding = true;
/*  869 */       this.popHelper.disableRunCoding = this.disableRunCoding;
/*  870 */       if (this.effort < 5)
/*      */       {
/*  872 */         this.popHelper.disableRunCoding = true;
/*      */       }
/*      */     }
/*  874 */     int i = paramPopulationCoding.fVlen;
/*  875 */     if (this.verbose > 2) {
/*  876 */       Utils.log.info("computePopSizePrivate fvlen=" + i + " tc=" + paramPopulationCoding.tokenCoding);
/*      */ 
/*  878 */       Utils.log.info("{ //BEGIN");
/*      */     }
/*      */ 
/*  882 */     int[] arrayOfInt1 = paramPopulationCoding.fValues;
/*  883 */     int[][] arrayOfInt = paramPopulationCoding.encodeValues(this.values, this.start, this.end);
/*  884 */     int[] arrayOfInt2 = arrayOfInt[0];
/*  885 */     int[] arrayOfInt3 = arrayOfInt[1];
/*  886 */     if (this.verbose > 2)
/*  887 */       Utils.log.info("-- refine on fv[" + i + "] fc=" + paramCoding1);
/*  888 */     paramPopulationCoding.setFavoredCoding(this.popHelper.choose(arrayOfInt1, 1, 1 + i, paramCoding1));
/*      */     Object localObject;
/*  889 */     if (((paramPopulationCoding.tokenCoding instanceof Coding)) && ((this.stress == null) || (this.stress.nextBoolean())))
/*      */     {
/*  891 */       if (this.verbose > 2)
/*  892 */         Utils.log.info("-- refine on tv[" + arrayOfInt2.length + "] tc=" + paramPopulationCoding.tokenCoding);
/*  893 */       localObject = this.popHelper.choose(arrayOfInt2, (Coding)paramPopulationCoding.tokenCoding);
/*  894 */       if (localObject != paramPopulationCoding.tokenCoding) {
/*  895 */         if (this.verbose > 2)
/*  896 */           Utils.log.info(">>> refined tc=" + localObject);
/*  897 */         paramPopulationCoding.setTokenCoding((CodingMethod)localObject);
/*      */       }
/*      */     }
/*  900 */     if (arrayOfInt3.length == 0) {
/*  901 */       paramPopulationCoding.setUnfavoredCoding(null);
/*      */     } else {
/*  903 */       if (this.verbose > 2)
/*  904 */         Utils.log.info("-- refine on uv[" + arrayOfInt3.length + "] uc=" + paramPopulationCoding.unfavoredCoding);
/*  905 */       paramPopulationCoding.setUnfavoredCoding(this.popHelper.choose(arrayOfInt3, paramCoding2));
/*      */     }
/*  907 */     if (this.verbose > 3) {
/*  908 */       Utils.log.info("finish computePopSizePrivate fvlen=" + i + " fc=" + paramPopulationCoding.favoredCoding + " tc=" + paramPopulationCoding.tokenCoding + " uc=" + paramPopulationCoding.unfavoredCoding);
/*      */ 
/*  913 */       localObject = new StringBuilder();
/*  914 */       ((StringBuilder)localObject).append("fv = {");
/*  915 */       for (int j = 1; j <= i; j++) {
/*  916 */         if (j % 10 == 0)
/*  917 */           ((StringBuilder)localObject).append('\n');
/*  918 */         ((StringBuilder)localObject).append(" ").append(arrayOfInt1[j]);
/*      */       }
/*  920 */       ((StringBuilder)localObject).append('\n');
/*  921 */       ((StringBuilder)localObject).append("}");
/*  922 */       Utils.log.info(((StringBuilder)localObject).toString());
/*      */     }
/*  924 */     if (this.verbose > 2) {
/*  925 */       Utils.log.info("} //END");
/*      */     }
/*  927 */     if (this.stress != null) {
/*  928 */       return null;
/*      */     }
/*      */     try
/*      */     {
/*  932 */       resetData();
/*      */ 
/*  934 */       paramPopulationCoding.writeSequencesTo(this.byteSizer, arrayOfInt2, arrayOfInt3);
/*  935 */       localObject = new int[] { getByteSize(), getZipSize() };
/*      */     } catch (IOException localIOException) {
/*  937 */       throw new RuntimeException(localIOException);
/*      */     }
/*  939 */     int[] arrayOfInt4 = null;
/*  940 */     assert ((arrayOfInt4 = computeSizePrivate(paramPopulationCoding)) != null);
/*      */ 
/*  942 */     assert (arrayOfInt4[0] == localObject[0]) : (arrayOfInt4[0] + " != " + localObject[0]);
/*  943 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void tryAdaptiveCoding(Coding paramCoding) {
/*  947 */     int i = this.bestZipSize;
/*      */ 
/*  952 */     int j = this.start;
/*  953 */     int k = this.end;
/*  954 */     int[] arrayOfInt1 = this.values;
/*  955 */     int m = k - j;
/*  956 */     if (paramCoding.isDelta()) {
/*  957 */       arrayOfInt1 = getDeltas(0, 0);
/*  958 */       j = 0;
/*  959 */       k = arrayOfInt1.length;
/*      */     }
/*  961 */     int[] arrayOfInt2 = new int[m + 1];
/*  962 */     int n = 0;
/*  963 */     int i1 = 0;
/*  964 */     for (int i2 = j; i2 < k; i2++) {
/*  965 */       int i3 = arrayOfInt1[i2];
/*  966 */       arrayOfInt2[(n++)] = i1;
/*  967 */       int i4 = paramCoding.getLength(i3);
/*  968 */       assert (i4 < 2147483647);
/*      */ 
/*  970 */       i1 += i4;
/*      */     }
/*  972 */     arrayOfInt2[(n++)] = i1;
/*  973 */     assert (n == arrayOfInt2.length);
/*  974 */     double d1 = i1 / m;
/*      */     double d2;
/*  978 */     if (this.effort >= 5) {
/*  979 */       if (this.effort > 6)
/*  980 */         d2 = 1.001D;
/*      */       else
/*  982 */         d2 = 1.003D;
/*      */     }
/*  984 */     else if (this.effort > 3)
/*  985 */       d2 = 1.01D;
/*      */     else {
/*  987 */       d2 = 1.03D;
/*      */     }
/*      */ 
/*  990 */     d2 *= d2;
/*  991 */     double d3 = d2 * d2;
/*  992 */     double d4 = d2 * d2 * d2;
/*      */ 
/*  994 */     double[] arrayOfDouble1 = new double[1 + (this.effort - 3)];
/*  995 */     double d5 = Math.log(m);
/*  996 */     for (int i5 = 0; i5 < arrayOfDouble1.length; i5++) {
/*  997 */       arrayOfDouble1[i5] = Math.exp(d5 * (i5 + 1) / (arrayOfDouble1.length + 1));
/*      */     }
/*  999 */     int[] arrayOfInt3 = new int[arrayOfDouble1.length];
/* 1000 */     int i6 = 0;
/* 1001 */     for (int i7 = 0; i7 < arrayOfDouble1.length; i7++) {
/* 1002 */       int i8 = (int)Math.round(arrayOfDouble1[i7]);
/* 1003 */       i8 = AdaptiveCoding.getNextK(i8 - 1);
/* 1004 */       if ((i8 > 0) && (i8 < m) && (
/* 1005 */         (i6 <= 0) || (i8 != arrayOfInt3[(i6 - 1)])))
/* 1006 */         arrayOfInt3[(i6++)] = i8;
/*      */     }
/* 1008 */     arrayOfInt3 = BandStructure.realloc(arrayOfInt3, i6);
/*      */ 
/* 1012 */     int[] arrayOfInt4 = new int[arrayOfInt3.length];
/* 1013 */     double[] arrayOfDouble2 = new double[arrayOfInt3.length];
/*      */     int i10;
/* 1014 */     for (int i9 = 0; i9 < arrayOfInt3.length; i9++) {
/* 1015 */       i10 = arrayOfInt3[i9];
/*      */       double d6;
/* 1017 */       if (i10 < 10)
/* 1018 */         d6 = d4;
/* 1019 */       else if (i10 < 100)
/* 1020 */         d6 = d3;
/*      */       else
/* 1022 */         d6 = d2;
/* 1023 */       arrayOfDouble2[i9] = d6;
/* 1024 */       arrayOfInt4[i9] = (4 + (int)Math.ceil(i10 * d1 * d6));
/*      */     }
/* 1026 */     if (this.verbose > 1) {
/* 1027 */       System.out.print("tryAdaptiveCoding [" + m + "]" + " avgS=" + d1 + " fuzz=" + d2 + " meshes: {");
/*      */ 
/* 1030 */       for (i9 = 0; i9 < arrayOfInt3.length; i9++) {
/* 1031 */         System.out.print(" " + arrayOfInt3[i9] + "(" + arrayOfInt4[i9] + ")");
/*      */       }
/* 1033 */       Utils.log.info(" }");
/*      */     }
/* 1035 */     if (this.runHelper == null) {
/* 1036 */       this.runHelper = new CodingChooser(this.effort, this.allCodingChoices);
/* 1037 */       if (this.stress != null)
/* 1038 */         this.runHelper.addStressSeed(this.stress.nextInt());
/* 1039 */       this.runHelper.topLevel = false;
/* 1040 */       this.runHelper.verbose -= 1;
/* 1041 */       this.runHelper.disableRunCoding = true;
/* 1042 */       this.runHelper.disablePopCoding = this.disablePopCoding;
/* 1043 */       if (this.effort < 5)
/*      */       {
/* 1045 */         this.runHelper.disablePopCoding = true;
/*      */       }
/*      */     }
/* 1047 */     for (i9 = 0; i9 < m; i9++) {
/* 1048 */       i9 = AdaptiveCoding.getNextK(i9 - 1);
/* 1049 */       if (i9 > m) i9 = m;
/* 1050 */       for (i10 = arrayOfInt3.length - 1; i10 >= 0; i10--) {
/* 1051 */         int i11 = arrayOfInt3[i10];
/* 1052 */         int i12 = arrayOfInt4[i10];
/* 1053 */         if (i9 + i11 <= m) {
/* 1054 */           int i13 = arrayOfInt2[(i9 + i11)] - arrayOfInt2[i9];
/* 1055 */           if (i13 >= i12)
/*      */           {
/* 1057 */             int i14 = i9 + i11;
/* 1058 */             int i15 = i13;
/* 1059 */             double d7 = d1 * arrayOfDouble2[i10];
/* 1060 */             while ((i14 < m) && (i14 - i9 <= m / 2)) {
/* 1061 */               i16 = i14;
/* 1062 */               int i17 = i15;
/* 1063 */               i14 += i11;
/* 1064 */               i14 = i9 + AdaptiveCoding.getNextK(i14 - i9 - 1);
/* 1065 */               if ((i14 < 0) || (i14 > m))
/* 1066 */                 i14 = m;
/* 1067 */               i15 = arrayOfInt2[i14] - arrayOfInt2[i9];
/* 1068 */               if (i15 < 4.0D + (i14 - i9) * d7) {
/* 1069 */                 i15 = i17;
/* 1070 */                 i14 = i16;
/* 1071 */                 break;
/*      */               }
/*      */             }
/* 1074 */             int i16 = i14;
/* 1075 */             if (this.verbose > 2) {
/* 1076 */               Utils.log.info("bulge at " + i9 + "[" + (i14 - i9) + "] of " + pct(i15 - d1 * (i14 - i9), d1 * (i14 - i9)));
/*      */ 
/* 1079 */               Utils.log.info("{ //BEGIN");
/*      */             }
/*      */ 
/* 1082 */             CodingMethod localCodingMethod = this.runHelper.choose(this.values, this.start + i9, this.start + i14, paramCoding);
/*      */             Object localObject1;
/*      */             Object localObject2;
/* 1086 */             if (localCodingMethod == paramCoding)
/*      */             {
/* 1088 */               localObject1 = paramCoding;
/* 1089 */               localObject2 = paramCoding;
/*      */             } else {
/* 1091 */               localObject1 = this.runHelper.choose(this.values, this.start, this.start + i9, paramCoding);
/*      */ 
/* 1095 */               localObject2 = this.runHelper.choose(this.values, this.start + i14, this.start + m, paramCoding);
/*      */             }
/*      */ 
/* 1100 */             if (this.verbose > 2)
/* 1101 */               Utils.log.info("} //END");
/* 1102 */             if ((localObject1 == localCodingMethod) && (i9 > 0) && (AdaptiveCoding.isCodableLength(i14)))
/*      */             {
/* 1104 */               i9 = 0;
/*      */             }
/* 1106 */             if ((localCodingMethod == localObject2) && (i14 < m)) {
/* 1107 */               i14 = m;
/*      */             }
/* 1109 */             if ((localObject1 != paramCoding) || (localCodingMethod != paramCoding) || (localObject2 != paramCoding))
/*      */             {
/* 1113 */               int i18 = 0;
/*      */               Object localObject3;
/* 1114 */               if (i14 == m) {
/* 1115 */                 localObject3 = localCodingMethod;
/*      */               } else {
/* 1117 */                 localObject3 = new AdaptiveCoding(i14 - i9, localCodingMethod, (CodingMethod)localObject2);
/* 1118 */                 i18 += 4;
/*      */               }
/* 1120 */               if (i9 > 0) {
/* 1121 */                 localObject3 = new AdaptiveCoding(i9, (CodingMethod)localObject1, (CodingMethod)localObject3);
/* 1122 */                 i18 += 4;
/*      */               }
/* 1124 */               int[] arrayOfInt5 = computeSizePrivate((CodingMethod)localObject3);
/* 1125 */               noteSizes((CodingMethod)localObject3, arrayOfInt5[0], arrayOfInt5[1] + i18);
/*      */             }
/*      */ 
/* 1129 */             i9 = i16;
/* 1130 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1134 */     if ((this.verbose > 3) && 
/* 1135 */       (this.bestZipSize < i))
/* 1136 */       Utils.log.info(">>> RUN WINS BY " + (i - this.bestZipSize));
/*      */   }
/*      */ 
/*      */   private static String pct(double paramDouble1, double paramDouble2)
/*      */   {
/* 1144 */     return Math.round(paramDouble1 / paramDouble2 * 10000.0D) / 100.0D + "%";
/*      */   }
/*      */ 
/*      */   private void resetData()
/*      */   {
/* 1188 */     flushData();
/* 1189 */     this.zipDef.reset();
/* 1190 */     if (this.context != null) {
/*      */       try
/*      */       {
/* 1193 */         this.context.writeTo(this.byteSizer);
/*      */       } catch (IOException localIOException) {
/* 1195 */         throw new RuntimeException(localIOException);
/*      */       }
/*      */     }
/* 1198 */     this.zipSizer.reset();
/* 1199 */     this.byteSizer.reset();
/*      */   }
/*      */   private void flushData() {
/*      */     try {
/* 1203 */       this.zipOut.finish();
/*      */     } catch (IOException localIOException) {
/* 1205 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/* 1209 */   private int getByteSize() { return this.byteSizer.getSize(); }
/*      */ 
/*      */   private int getZipSize() {
/* 1212 */     flushData();
/* 1213 */     return this.zipSizer.getSize();
/*      */   }
/*      */ 
/*      */   void addStressSeed(int paramInt)
/*      */   {
/* 1220 */     if (this.stress == null) return;
/* 1221 */     this.stress.setSeed(paramInt + (this.stress.nextInt() << 32));
/*      */   }
/*      */ 
/*      */   private CodingMethod stressPopCoding(CodingMethod paramCodingMethod)
/*      */   {
/* 1226 */     assert (this.stress != null);
/*      */ 
/* 1228 */     if (!(paramCodingMethod instanceof Coding)) return paramCodingMethod;
/* 1229 */     Coding localCoding = ((Coding)paramCodingMethod).getValueCoding();
/* 1230 */     Histogram localHistogram = getValueHistogram();
/* 1231 */     int i = stressLen(localHistogram.getTotalLength());
/* 1232 */     if (i == 0) return paramCodingMethod;
/* 1233 */     ArrayList localArrayList = new ArrayList();
/*      */     Object localObject1;
/*      */     int k;
/* 1234 */     if (this.stress.nextBoolean())
/*      */     {
/* 1236 */       localObject1 = new HashSet();
/* 1237 */       for (k = this.start; k < this.end; k++)
/* 1238 */         if (((Set)localObject1).add(Integer.valueOf(this.values[k]))) localArrayList.add(Integer.valueOf(this.values[k])); 
/*      */     }
/*      */     else
/*      */     {
/* 1241 */       localObject1 = localHistogram.getMatrix();
/* 1242 */       for (k = 0; k < localObject1.length; k++) {
/* 1243 */         Object localObject2 = localObject1[k];
/* 1244 */         for (int n = 1; n < localObject2.length; n++) {
/* 1245 */           localArrayList.add(Integer.valueOf(localObject2[n]));
/*      */         }
/*      */       }
/*      */     }
/* 1249 */     int j = this.stress.nextInt();
/* 1250 */     if ((j & 0x7) <= 2)
/*      */     {
/* 1252 */       Collections.shuffle(localArrayList, this.stress);
/*      */     }
/*      */     else {
/* 1255 */       if ((j >>>= 3 & 0x7) <= 2) Collections.sort(localArrayList);
/* 1256 */       if ((j >>>= 3 & 0x7) <= 2) Collections.reverse(localArrayList);
/* 1257 */       if ((j >>>= 3 & 0x7) <= 2) Collections.rotate(localArrayList, stressLen(localArrayList.size()));
/*      */     }
/* 1259 */     if (localArrayList.size() > i)
/*      */     {
/* 1261 */       if ((j >>>= 3 & 0x7) <= 2)
/*      */       {
/* 1263 */         localArrayList.subList(i, localArrayList.size()).clear();
/*      */       }
/*      */       else {
/* 1266 */         localArrayList.subList(0, localArrayList.size() - i).clear();
/*      */       }
/*      */     }
/* 1269 */     i = localArrayList.size();
/* 1270 */     int[] arrayOfInt1 = new int[1 + i];
/* 1271 */     for (int m = 0; m < i; m++) {
/* 1272 */       arrayOfInt1[(1 + m)] = ((Integer)localArrayList.get(m)).intValue();
/*      */     }
/* 1274 */     PopulationCoding localPopulationCoding = new PopulationCoding();
/* 1275 */     localPopulationCoding.setFavoredValues(arrayOfInt1, i);
/* 1276 */     int[] arrayOfInt2 = PopulationCoding.LValuesCoded;
/*      */     int i2;
/* 1277 */     for (int i1 = 0; i1 < arrayOfInt2.length / 2; i1++) {
/* 1278 */       i2 = arrayOfInt2[this.stress.nextInt(arrayOfInt2.length)];
/* 1279 */       if ((i2 >= 0) && 
/* 1280 */         (PopulationCoding.fitTokenCoding(i, i2) != null)) {
/* 1281 */         localPopulationCoding.setL(i2);
/* 1282 */         break;
/*      */       }
/*      */     }
/* 1285 */     if (localPopulationCoding.tokenCoding == null) {
/* 1286 */       i1 = arrayOfInt1[1]; i2 = i1;
/* 1287 */       for (int i3 = 2; i3 <= i; i3++) {
/* 1288 */         int i4 = arrayOfInt1[i3];
/* 1289 */         if (i1 > i4) i1 = i4;
/* 1290 */         if (i2 < i4) i2 = i4;
/*      */       }
/* 1292 */       localPopulationCoding.tokenCoding = stressCoding(i1, i2);
/*      */     }
/*      */ 
/* 1295 */     computePopSizePrivate(localPopulationCoding, localCoding, localCoding);
/* 1296 */     return localPopulationCoding;
/*      */   }
/*      */ 
/*      */   private CodingMethod stressAdaptiveCoding(CodingMethod paramCodingMethod)
/*      */   {
/* 1301 */     assert (this.stress != null);
/*      */ 
/* 1303 */     if (!(paramCodingMethod instanceof Coding)) return paramCodingMethod;
/* 1304 */     Coding localCoding = (Coding)paramCodingMethod;
/* 1305 */     int i = this.end - this.start;
/* 1306 */     if (i < 2) return paramCodingMethod;
/*      */ 
/* 1308 */     int j = stressLen(i - 1) + 1;
/* 1309 */     if (j == i) return paramCodingMethod; try
/*      */     {
/* 1311 */       assert (!this.disableRunCoding);
/* 1312 */       this.disableRunCoding = true;
/* 1313 */       int[] arrayOfInt = (int[])this.values.clone();
/* 1314 */       Object localObject1 = null;
/* 1315 */       int k = this.end;
/* 1316 */       int m = this.start;
/*      */       int n;
/* 1317 */       for (; k > m; k = n)
/*      */       {
/* 1319 */         int i2 = k - m < 100 ? -1 : this.stress.nextInt();
/*      */         int i1;
/* 1320 */         if ((i2 & 0x7) != 0) {
/* 1321 */           i1 = j == 1 ? j : stressLen(j - 1) + 1;
/*      */         }
/*      */         else {
/* 1324 */           int i3 = i2 >>>= 3 & 0x3;
/* 1325 */           int i4 = i2 >>>= 3 & 0xFF;
/*      */           while (true) {
/* 1327 */             i1 = AdaptiveCoding.decodeK(i3, i4);
/* 1328 */             if (i1 <= k - m)
/*      */               break;
/* 1330 */             if (i4 != 3)
/* 1331 */               i4 = 3;
/*      */             else {
/* 1333 */               i3--;
/*      */             }
/*      */           }
/* 1336 */           assert (AdaptiveCoding.isCodableLength(i1));
/*      */         }
/* 1338 */         if (i1 > k - m) i1 = k - m;
/* 1339 */         while (!AdaptiveCoding.isCodableLength(i1)) {
/* 1340 */           i1--;
/*      */         }
/* 1342 */         n = k - i1;
/* 1343 */         assert (n < k);
/* 1344 */         assert (n >= m);
/*      */ 
/* 1346 */         CodingMethod localCodingMethod = choose(arrayOfInt, n, k, localCoding);
/* 1347 */         if (localObject1 == null)
/* 1348 */           localObject1 = localCodingMethod;
/*      */         else {
/* 1350 */           localObject1 = new AdaptiveCoding(k - n, localCodingMethod, (CodingMethod)localObject1);
/*      */         }
/*      */       }
/* 1353 */       return localObject1;
/*      */     } finally {
/* 1355 */       this.disableRunCoding = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private Coding stressCoding(int paramInt1, int paramInt2)
/*      */   {
/* 1361 */     assert (this.stress != null);
/* 1362 */     for (int i = 0; i < 100; i++) {
/* 1363 */       Coding localCoding1 = Coding.of(this.stress.nextInt(5) + 1, this.stress.nextInt(256) + 1, this.stress.nextInt(3));
/*      */ 
/* 1366 */       if (localCoding1.B() == 1) localCoding1 = localCoding1.setH(256);
/* 1367 */       if ((localCoding1.H() == 256) && (localCoding1.B() >= 5)) localCoding1 = localCoding1.setB(4);
/* 1368 */       if (this.stress.nextBoolean()) {
/* 1369 */         Coding localCoding2 = localCoding1.setD(1);
/* 1370 */         if (localCoding2.canRepresent(paramInt1, paramInt2)) return localCoding2;
/*      */       }
/* 1372 */       if (localCoding1.canRepresent(paramInt1, paramInt2)) return localCoding1;
/*      */     }
/* 1374 */     return BandStructure.UNSIGNED5;
/*      */   }
/*      */ 
/*      */   private int stressLen(int paramInt)
/*      */   {
/* 1379 */     assert (this.stress != null);
/* 1380 */     assert (paramInt >= 0);
/* 1381 */     int i = this.stress.nextInt(100);
/* 1382 */     if (i < 20)
/* 1383 */       return Math.min(paramInt / 5, i);
/* 1384 */     if (i < 40) {
/* 1385 */       return paramInt;
/*      */     }
/* 1387 */     return this.stress.nextInt(paramInt);
/*      */   }
/*      */ 
/*      */   static class Choice
/*      */   {
/*      */     final Coding coding;
/*      */     final int index;
/*      */     final int[] distance;
/*      */     int searchOrder;
/*      */     int minDistance;
/*      */     int zipSize;
/*      */     int byteSize;
/*      */     int histSize;
/*      */ 
/*      */     Choice(Coding paramCoding, int paramInt, int[] paramArrayOfInt)
/*      */     {
/*   75 */       this.coding = paramCoding;
/*   76 */       this.index = paramInt;
/*   77 */       this.distance = paramArrayOfInt;
/*      */     }
/*      */ 
/*      */     void reset()
/*      */     {
/*   87 */       this.searchOrder = 2147483647;
/*   88 */       this.minDistance = 2147483647;
/*   89 */       this.zipSize = (this.byteSize = this.histSize = -1);
/*      */     }
/*      */ 
/*      */     boolean isExtra() {
/*   93 */       return this.index < 0;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*   97 */       return stringForDebug();
/*      */     }
/*      */ 
/*      */     private String stringForDebug() {
/*  101 */       String str = "";
/*  102 */       if (this.searchOrder < 2147483647)
/*  103 */         str = str + " so: " + this.searchOrder;
/*  104 */       if (this.minDistance < 2147483647)
/*  105 */         str = str + " md: " + this.minDistance;
/*  106 */       if (this.zipSize > 0)
/*  107 */         str = str + " zs: " + this.zipSize;
/*  108 */       if (this.byteSize > 0)
/*  109 */         str = str + " bs: " + this.byteSize;
/*  110 */       if (this.histSize > 0)
/*  111 */         str = str + " hs: " + this.histSize;
/*  112 */       return "Choice[" + this.index + "] " + str + " " + this.coding;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Sizer extends OutputStream
/*      */   {
/*      */     final OutputStream out;
/*      */     private int count;
/*      */ 
/*      */     Sizer(OutputStream paramOutputStream)
/*      */     {
/* 1151 */       this.out = paramOutputStream;
/*      */     }
/*      */     Sizer() {
/* 1154 */       this(null);
/*      */     }
/*      */ 
/*      */     public void write(int paramInt) throws IOException {
/* 1158 */       this.count += 1;
/* 1159 */       if (this.out != null) this.out.write(paramInt); 
/*      */     }
/*      */ 
/* 1162 */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException { this.count += paramInt2;
/* 1163 */       if (this.out != null) this.out.write(paramArrayOfByte, paramInt1, paramInt2);  } 
/*      */     public void reset()
/*      */     {
/* 1166 */       this.count = 0;
/*      */     }
/* 1168 */     public int getSize() { return this.count; }
/*      */ 
/*      */     public String toString() {
/* 1171 */       String str = super.toString();
/*      */ 
/* 1173 */       assert ((str = stringForDebug()) != null);
/* 1174 */       return str;
/*      */     }
/*      */     String stringForDebug() {
/* 1177 */       return "<Sizer " + getSize() + ">";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.CodingChooser
 * JD-Core Version:    0.6.2
 */