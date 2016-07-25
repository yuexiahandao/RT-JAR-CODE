/*      */ package com.sun.org.apache.xpath.internal.axes;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*      */ import com.sun.org.apache.xpath.internal.Expression;
/*      */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*      */ import com.sun.org.apache.xpath.internal.compiler.OpMap;
/*      */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*      */ import com.sun.org.apache.xpath.internal.patterns.ContextMatchStepPattern;
/*      */ import com.sun.org.apache.xpath.internal.patterns.FunctionPattern;
/*      */ import com.sun.org.apache.xpath.internal.patterns.StepPattern;
/*      */ import java.io.PrintStream;
/*      */ import javax.xml.transform.TransformerException;
/*      */ 
/*      */ public class WalkerFactory
/*      */ {
/*      */   static final boolean DEBUG_PATTERN_CREATION = false;
/*      */   static final boolean DEBUG_WALKER_CREATION = false;
/*      */   static final boolean DEBUG_ITERATOR_CREATION = false;
/*      */   public static final int BITS_COUNT = 255;
/*      */   public static final int BITS_RESERVED = 3840;
/*      */   public static final int BIT_PREDICATE = 4096;
/*      */   public static final int BIT_ANCESTOR = 8192;
/*      */   public static final int BIT_ANCESTOR_OR_SELF = 16384;
/*      */   public static final int BIT_ATTRIBUTE = 32768;
/*      */   public static final int BIT_CHILD = 65536;
/*      */   public static final int BIT_DESCENDANT = 131072;
/*      */   public static final int BIT_DESCENDANT_OR_SELF = 262144;
/*      */   public static final int BIT_FOLLOWING = 524288;
/*      */   public static final int BIT_FOLLOWING_SIBLING = 1048576;
/*      */   public static final int BIT_NAMESPACE = 2097152;
/*      */   public static final int BIT_PARENT = 4194304;
/*      */   public static final int BIT_PRECEDING = 8388608;
/*      */   public static final int BIT_PRECEDING_SIBLING = 16777216;
/*      */   public static final int BIT_SELF = 33554432;
/*      */   public static final int BIT_FILTER = 67108864;
/*      */   public static final int BIT_ROOT = 134217728;
/*      */   public static final int BITMASK_TRAVERSES_OUTSIDE_SUBTREE = 234381312;
/*      */   public static final int BIT_BACKWARDS_SELF = 268435456;
/*      */   public static final int BIT_ANY_DESCENDANT_FROM_ROOT = 536870912;
/*      */   public static final int BIT_NODETEST_ANY = 1073741824;
/*      */   public static final int BIT_MATCH_PATTERN = -2147483648;
/*      */ 
/*      */   static AxesWalker loadOneWalker(WalkingIterator lpi, Compiler compiler, int stepOpCodePos)
/*      */     throws TransformerException
/*      */   {
/*   67 */     AxesWalker firstWalker = null;
/*   68 */     int stepType = compiler.getOp(stepOpCodePos);
/*      */ 
/*   70 */     if (stepType != -1)
/*      */     {
/*   75 */       firstWalker = createDefaultWalker(compiler, stepType, lpi, 0);
/*      */ 
/*   77 */       firstWalker.init(compiler, stepOpCodePos, stepType);
/*      */     }
/*      */ 
/*   80 */     return firstWalker;
/*      */   }
/*      */ 
/*      */   static AxesWalker loadWalkers(WalkingIterator lpi, Compiler compiler, int stepOpCodePos, int stepIndex)
/*      */     throws TransformerException
/*      */   {
/*  103 */     AxesWalker firstWalker = null;
/*  104 */     AxesWalker prevWalker = null;
/*      */ 
/*  106 */     int analysis = analyze(compiler, stepOpCodePos, stepIndex);
/*      */     int stepType;
/*  108 */     while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
/*      */     {
/*  110 */       AxesWalker walker = createDefaultWalker(compiler, stepOpCodePos, lpi, analysis);
/*      */ 
/*  112 */       walker.init(compiler, stepOpCodePos, stepType);
/*  113 */       walker.exprSetParent(lpi);
/*      */ 
/*  116 */       if (null == firstWalker)
/*      */       {
/*  118 */         firstWalker = walker;
/*      */       }
/*      */       else
/*      */       {
/*  122 */         prevWalker.setNextWalker(walker);
/*  123 */         walker.setPrevWalker(prevWalker);
/*      */       }
/*      */ 
/*  126 */       prevWalker = walker;
/*  127 */       stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
/*      */ 
/*  129 */       if (stepOpCodePos < 0) {
/*  130 */         break;
/*      */       }
/*      */     }
/*  133 */     return firstWalker;
/*      */   }
/*      */ 
/*      */   public static boolean isSet(int analysis, int bits)
/*      */   {
/*  138 */     return 0 != (analysis & bits);
/*      */   }
/*      */ 
/*      */   public static void diagnoseIterator(String name, int analysis, Compiler compiler)
/*      */   {
/*  143 */     System.out.println(compiler.toString() + ", " + name + ", " + Integer.toBinaryString(analysis) + ", " + getAnalysisString(analysis));
/*      */   }
/*      */ 
/*      */   public static DTMIterator newDTMIterator(Compiler compiler, int opPos, boolean isTopLevel)
/*      */     throws TransformerException
/*      */   {
/*  166 */     int firstStepPos = OpMap.getFirstChildPos(opPos);
/*  167 */     int analysis = analyze(compiler, firstStepPos, 0);
/*  168 */     boolean isOneStep = isOneStep(analysis);
/*      */     DTMIterator iter;
/*      */     DTMIterator iter;
/*  172 */     if ((isOneStep) && (walksSelfOnly(analysis)) && (isWild(analysis)) && (!hasPredicate(analysis)))
/*      */     {
/*  180 */       iter = new SelfIteratorNoPredicate(compiler, opPos, analysis);
/*      */     }
/*      */     else
/*      */     {
/*      */       DTMIterator iter;
/*  183 */       if ((walksChildrenOnly(analysis)) && (isOneStep))
/*      */       {
/*      */         DTMIterator iter;
/*  187 */         if ((isWild(analysis)) && (!hasPredicate(analysis)))
/*      */         {
/*  193 */           iter = new ChildIterator(compiler, opPos, analysis);
/*      */         }
/*      */         else
/*      */         {
/*  201 */           iter = new ChildTestIterator(compiler, opPos, analysis);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         DTMIterator iter;
/*  205 */         if ((isOneStep) && (walksAttributes(analysis)))
/*      */         {
/*  212 */           iter = new AttributeIterator(compiler, opPos, analysis);
/*      */         }
/*      */         else
/*      */         {
/*      */           DTMIterator iter;
/*  214 */           if ((isOneStep) && (!walksFilteredList(analysis)))
/*      */           {
/*      */             DTMIterator iter;
/*  216 */             if ((!walksNamespaces(analysis)) && ((walksInDocOrder(analysis)) || (isSet(analysis, 4194304))))
/*      */             {
/*  224 */               iter = new OneStepIteratorForward(compiler, opPos, analysis);
/*      */             }
/*      */             else
/*      */             {
/*  233 */               iter = new OneStepIterator(compiler, opPos, analysis);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*      */             DTMIterator iter;
/*  249 */             if (isOptimizableForDescendantIterator(compiler, firstStepPos, 0))
/*      */             {
/*  258 */               iter = new DescendantIterator(compiler, opPos, analysis);
/*      */             }
/*      */             else
/*      */             {
/*      */               DTMIterator iter;
/*  262 */               if (isNaturalDocOrder(compiler, firstStepPos, 0, analysis))
/*      */               {
/*  269 */                 iter = new WalkingIterator(compiler, opPos, analysis, true);
/*      */               }
/*      */               else
/*      */               {
/*  280 */                 iter = new WalkingIteratorSorted(compiler, opPos, analysis, true);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  283 */     if ((iter instanceof LocPathIterator)) {
/*  284 */       ((LocPathIterator)iter).setIsTopLevel(isTopLevel);
/*      */     }
/*  286 */     return iter;
/*      */   }
/*      */ 
/*      */   public static int getAxisFromStep(Compiler compiler, int stepOpCodePos)
/*      */     throws TransformerException
/*      */   {
/*  307 */     int stepType = compiler.getOp(stepOpCodePos);
/*      */ 
/*  309 */     switch (stepType)
/*      */     {
/*      */     case 43:
/*  312 */       return 6;
/*      */     case 44:
/*  314 */       return 7;
/*      */     case 46:
/*  316 */       return 11;
/*      */     case 47:
/*  318 */       return 12;
/*      */     case 45:
/*  320 */       return 10;
/*      */     case 49:
/*  322 */       return 9;
/*      */     case 37:
/*  324 */       return 0;
/*      */     case 38:
/*  326 */       return 1;
/*      */     case 39:
/*  328 */       return 2;
/*      */     case 50:
/*  330 */       return 19;
/*      */     case 40:
/*  332 */       return 3;
/*      */     case 42:
/*  334 */       return 5;
/*      */     case 41:
/*  336 */       return 4;
/*      */     case 48:
/*  338 */       return 13;
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*  343 */       return 20;
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*  346 */     case 36: } throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */   }
/*      */ 
/*      */   public static int getAnalysisBitFromAxes(int axis)
/*      */   {
/*  357 */     switch (axis)
/*      */     {
/*      */     case 0:
/*  360 */       return 8192;
/*      */     case 1:
/*  362 */       return 16384;
/*      */     case 2:
/*  364 */       return 32768;
/*      */     case 3:
/*  366 */       return 65536;
/*      */     case 4:
/*  368 */       return 131072;
/*      */     case 5:
/*  370 */       return 262144;
/*      */     case 6:
/*  372 */       return 524288;
/*      */     case 7:
/*  374 */       return 1048576;
/*      */     case 8:
/*      */     case 9:
/*  377 */       return 2097152;
/*      */     case 10:
/*  379 */       return 4194304;
/*      */     case 11:
/*  381 */       return 8388608;
/*      */     case 12:
/*  383 */       return 16777216;
/*      */     case 13:
/*  385 */       return 33554432;
/*      */     case 14:
/*  387 */       return 262144;
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  392 */       return 536870912;
/*      */     case 19:
/*  394 */       return 134217728;
/*      */     case 20:
/*  396 */       return 67108864;
/*      */     case 15:
/*  398 */     }return 67108864;
/*      */   }
/*      */ 
/*      */   static boolean functionProximateOrContainsProximate(Compiler compiler, int opPos)
/*      */   {
/*  405 */     int endFunc = opPos + compiler.getOp(opPos + 1) - 1;
/*  406 */     opPos = OpMap.getFirstChildPos(opPos);
/*  407 */     int funcID = compiler.getOp(opPos);
/*      */ 
/*  411 */     switch (funcID)
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*  415 */       return true;
/*      */     }
/*  417 */     opPos++;
/*  418 */     int i = 0;
/*  419 */     for (int p = opPos; p < endFunc; i++)
/*      */     {
/*  421 */       int innerExprOpPos = p + 2;
/*  422 */       int argOp = compiler.getOp(innerExprOpPos);
/*  423 */       boolean prox = isProximateInnerExpr(compiler, innerExprOpPos);
/*  424 */       if (prox)
/*  425 */         return true;
/*  419 */       p = compiler.getNextOpPos(p);
/*      */     }
/*      */ 
/*  429 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean isProximateInnerExpr(Compiler compiler, int opPos)
/*      */   {
/*  434 */     int op = compiler.getOp(opPos);
/*  435 */     int innerExprOpPos = opPos + 2;
/*      */     boolean isProx;
/*  436 */     switch (op)
/*      */     {
/*      */     case 26:
/*  439 */       if (isProximateInnerExpr(compiler, innerExprOpPos))
/*  440 */         return true;
/*      */       break;
/*      */     case 21:
/*      */     case 22:
/*      */     case 27:
/*      */     case 28:
/*  446 */       break;
/*      */     case 25:
/*  448 */       isProx = functionProximateOrContainsProximate(compiler, opPos);
/*  449 */       if (isProx)
/*  450 */         return true;
/*      */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*  457 */       int leftPos = OpMap.getFirstChildPos(op);
/*  458 */       int rightPos = compiler.getNextOpPos(leftPos);
/*  459 */       isProx = isProximateInnerExpr(compiler, leftPos);
/*  460 */       if (isProx)
/*  461 */         return true;
/*  462 */       isProx = isProximateInnerExpr(compiler, rightPos);
/*  463 */       if (isProx)
/*  464 */         return true; break;
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 23:
/*      */     case 24:
/*      */     default:
/*  467 */       return true;
/*      */     }
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean mightBeProximate(Compiler compiler, int opPos, int stepType)
/*      */     throws TransformerException
/*      */   {
/*  479 */     boolean mightBeProximate = false;
/*      */     int argLen;
/*  482 */     switch (stepType)
/*      */     {
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*  488 */       argLen = compiler.getArgLength(opPos);
/*  489 */       break;
/*      */     default:
/*  491 */       argLen = compiler.getArgLengthOfStep(opPos);
/*      */     }
/*      */ 
/*  494 */     int predPos = compiler.getFirstPredicateOpPos(opPos);
/*  495 */     int count = 0;
/*      */ 
/*  497 */     while (29 == compiler.getOp(predPos))
/*      */     {
/*  499 */       count++;
/*      */ 
/*  501 */       int innerExprOpPos = predPos + 2;
/*  502 */       int predOp = compiler.getOp(innerExprOpPos);
/*      */       boolean isProx;
/*  504 */       switch (predOp)
/*      */       {
/*      */       case 22:
/*  507 */         return true;
/*      */       case 28:
/*  510 */         break;
/*      */       case 19:
/*      */       case 27:
/*  513 */         return true;
/*      */       case 25:
/*  515 */         isProx = functionProximateOrContainsProximate(compiler, innerExprOpPos);
/*      */ 
/*  517 */         if (isProx)
/*  518 */           return true;
/*      */         break;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*  525 */         int leftPos = OpMap.getFirstChildPos(innerExprOpPos);
/*  526 */         int rightPos = compiler.getNextOpPos(leftPos);
/*  527 */         isProx = isProximateInnerExpr(compiler, leftPos);
/*  528 */         if (isProx)
/*  529 */           return true;
/*  530 */         isProx = isProximateInnerExpr(compiler, rightPos);
/*  531 */         if (isProx)
/*  532 */           return true; break;
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 20:
/*      */       case 21:
/*      */       case 23:
/*      */       case 24:
/*      */       case 26:
/*      */       default:
/*  535 */         return true;
/*      */       }
/*      */ 
/*  538 */       predPos = compiler.getNextOpPos(predPos);
/*      */     }
/*      */ 
/*  541 */     return mightBeProximate;
/*      */   }
/*      */ 
/*      */   private static boolean isOptimizableForDescendantIterator(Compiler compiler, int stepOpCodePos, int stepIndex)
/*      */     throws TransformerException
/*      */   {
/*  564 */     int stepCount = 0;
/*  565 */     boolean foundDorDS = false;
/*  566 */     boolean foundSelf = false;
/*  567 */     boolean foundDS = false;
/*      */ 
/*  569 */     int nodeTestType = 1033;
/*      */     int stepType;
/*  571 */     while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
/*      */     {
/*  575 */       if ((nodeTestType != 1033) && (nodeTestType != 35)) {
/*  576 */         return false;
/*      */       }
/*  578 */       stepCount++;
/*  579 */       if (stepCount > 3) {
/*  580 */         return false;
/*      */       }
/*  582 */       boolean mightBeProximate = mightBeProximate(compiler, stepOpCodePos, stepType);
/*  583 */       if (mightBeProximate) {
/*  584 */         return false;
/*      */       }
/*  586 */       switch (stepType)
/*      */       {
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 37:
/*      */       case 38:
/*      */       case 39:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 46:
/*      */       case 47:
/*      */       case 49:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*  604 */         return false;
/*      */       case 50:
/*  606 */         if (1 != stepCount)
/*  607 */           return false;
/*      */         break;
/*      */       case 40:
/*  610 */         if ((!foundDS) && ((!foundDorDS) || (!foundSelf)))
/*  611 */           return false;
/*      */         break;
/*      */       case 42:
/*  614 */         foundDS = true;
/*      */       case 41:
/*  616 */         if (3 == stepCount)
/*  617 */           return false;
/*  618 */         foundDorDS = true;
/*  619 */         break;
/*      */       case 48:
/*  621 */         if (1 != stepCount)
/*  622 */           return false;
/*  623 */         foundSelf = true;
/*  624 */         break;
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       default:
/*  626 */         throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */       }
/*      */ 
/*  630 */       nodeTestType = compiler.getStepTestType(stepOpCodePos);
/*      */ 
/*  632 */       int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
/*      */ 
/*  634 */       if (nextStepOpCodePos < 0) {
/*      */         break;
/*      */       }
/*  637 */       if (-1 != compiler.getOp(nextStepOpCodePos))
/*      */       {
/*  639 */         if (compiler.countPredicates(stepOpCodePos) > 0)
/*      */         {
/*  641 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  645 */       stepOpCodePos = nextStepOpCodePos;
/*      */     }
/*      */ 
/*  648 */     return true;
/*      */   }
/*      */ 
/*      */   private static int analyze(Compiler compiler, int stepOpCodePos, int stepIndex)
/*      */     throws TransformerException
/*      */   {
/*  672 */     int stepCount = 0;
/*  673 */     int analysisResult = 0;
/*      */     int stepType;
/*  675 */     while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
/*      */     {
/*  677 */       stepCount++;
/*      */ 
/*  684 */       boolean predAnalysis = analyzePredicate(compiler, stepOpCodePos, stepType);
/*      */ 
/*  687 */       if (predAnalysis) {
/*  688 */         analysisResult |= 4096;
/*      */       }
/*  690 */       switch (stepType)
/*      */       {
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*  696 */         analysisResult |= 67108864;
/*  697 */         break;
/*      */       case 50:
/*  699 */         analysisResult |= 134217728;
/*  700 */         break;
/*      */       case 37:
/*  702 */         analysisResult |= 8192;
/*  703 */         break;
/*      */       case 38:
/*  705 */         analysisResult |= 16384;
/*  706 */         break;
/*      */       case 39:
/*  708 */         analysisResult |= 32768;
/*  709 */         break;
/*      */       case 49:
/*  711 */         analysisResult |= 2097152;
/*  712 */         break;
/*      */       case 40:
/*  714 */         analysisResult |= 65536;
/*  715 */         break;
/*      */       case 41:
/*  717 */         analysisResult |= 131072;
/*  718 */         break;
/*      */       case 42:
/*  722 */         if ((2 == stepCount) && (134217728 == analysisResult))
/*      */         {
/*  724 */           analysisResult |= 536870912;
/*      */         }
/*      */ 
/*  727 */         analysisResult |= 262144;
/*  728 */         break;
/*      */       case 43:
/*  730 */         analysisResult |= 524288;
/*  731 */         break;
/*      */       case 44:
/*  733 */         analysisResult |= 1048576;
/*  734 */         break;
/*      */       case 46:
/*  736 */         analysisResult |= 8388608;
/*  737 */         break;
/*      */       case 47:
/*  739 */         analysisResult |= 16777216;
/*  740 */         break;
/*      */       case 45:
/*  742 */         analysisResult |= 4194304;
/*  743 */         break;
/*      */       case 48:
/*  745 */         analysisResult |= 33554432;
/*  746 */         break;
/*      */       case 51:
/*  748 */         analysisResult |= -2147450880;
/*  749 */         break;
/*      */       case 52:
/*  751 */         analysisResult |= -2147475456;
/*  752 */         break;
/*      */       case 53:
/*  754 */         analysisResult |= -2143289344;
/*  755 */         break;
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       default:
/*  757 */         throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */       }
/*      */ 
/*  761 */       if (1033 == compiler.getOp(stepOpCodePos + 3))
/*      */       {
/*  763 */         analysisResult |= 1073741824;
/*      */       }
/*      */ 
/*  766 */       stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
/*      */ 
/*  768 */       if (stepOpCodePos < 0) {
/*      */         break;
/*      */       }
/*      */     }
/*  772 */     analysisResult |= stepCount & 0xFF;
/*      */ 
/*  774 */     return analysisResult;
/*      */   }
/*      */ 
/*      */   public static boolean isDownwardAxisOfMany(int axis)
/*      */   {
/*  787 */     return (5 == axis) || (4 == axis) || (6 == axis) || (11 == axis);
/*      */   }
/*      */ 
/*      */   static StepPattern loadSteps(MatchPatternIterator mpi, Compiler compiler, int stepOpCodePos, int stepIndex)
/*      */     throws TransformerException
/*      */   {
/*  832 */     StepPattern step = null;
/*  833 */     StepPattern firstStep = null; StepPattern prevStep = null;
/*  834 */     int analysis = analyze(compiler, stepOpCodePos, stepIndex);
/*      */     int stepType;
/*  836 */     while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
/*      */     {
/*  838 */       step = createDefaultStepPattern(compiler, stepOpCodePos, mpi, analysis, firstStep, prevStep);
/*      */ 
/*  841 */       if (null == firstStep)
/*      */       {
/*  843 */         firstStep = step;
/*      */       }
/*      */       else
/*      */       {
/*  849 */         step.setRelativePathPattern(prevStep);
/*      */       }
/*      */ 
/*  852 */       prevStep = step;
/*  853 */       stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
/*      */ 
/*  855 */       if (stepOpCodePos < 0) {
/*  856 */         break;
/*      */       }
/*      */     }
/*  859 */     int axis = 13;
/*  860 */     int paxis = 13;
/*  861 */     StepPattern tail = step;
/*  862 */     for (StepPattern pat = step; null != pat; 
/*  863 */       pat = pat.getRelativePathPattern())
/*      */     {
/*  865 */       int nextAxis = pat.getAxis();
/*      */ 
/*  867 */       pat.setAxis(axis);
/*      */ 
/*  892 */       int whatToShow = pat.getWhatToShow();
/*  893 */       if ((whatToShow == 2) || (whatToShow == 4096))
/*      */       {
/*  896 */         int newAxis = whatToShow == 2 ? 2 : 9;
/*      */ 
/*  898 */         if (isDownwardAxisOfMany(axis))
/*      */         {
/*  900 */           StepPattern attrPat = new StepPattern(whatToShow, pat.getNamespace(), pat.getLocalName(), newAxis, 0);
/*      */ 
/*  905 */           XNumber score = pat.getStaticScore();
/*  906 */           pat.setNamespace(null);
/*  907 */           pat.setLocalName("*");
/*  908 */           attrPat.setPredicates(pat.getPredicates());
/*  909 */           pat.setPredicates(null);
/*  910 */           pat.setWhatToShow(1);
/*  911 */           StepPattern rel = pat.getRelativePathPattern();
/*  912 */           pat.setRelativePathPattern(attrPat);
/*  913 */           attrPat.setRelativePathPattern(rel);
/*  914 */           attrPat.setStaticScore(score);
/*      */ 
/*  920 */           if (11 == pat.getAxis()) {
/*  921 */             pat.setAxis(15);
/*      */           }
/*  923 */           else if (4 == pat.getAxis()) {
/*  924 */             pat.setAxis(5);
/*      */           }
/*  926 */           pat = attrPat;
/*      */         }
/*  928 */         else if (3 == pat.getAxis())
/*      */         {
/*  932 */           pat.setAxis(2);
/*      */         }
/*      */       }
/*  935 */       axis = nextAxis;
/*      */ 
/*  937 */       tail = pat;
/*      */     }
/*      */ 
/*  940 */     if (axis < 16)
/*      */     {
/*  942 */       StepPattern selfPattern = new ContextMatchStepPattern(axis, paxis);
/*      */ 
/*  944 */       XNumber score = tail.getStaticScore();
/*  945 */       tail.setRelativePathPattern(selfPattern);
/*  946 */       tail.setStaticScore(score);
/*  947 */       selfPattern.setStaticScore(score);
/*      */     }
/*      */ 
/*  956 */     return step;
/*      */   }
/*      */ 
/*      */   private static StepPattern createDefaultStepPattern(Compiler compiler, int opPos, MatchPatternIterator mpi, int analysis, StepPattern tail, StepPattern head)
/*      */     throws TransformerException
/*      */   {
/*  986 */     int stepType = compiler.getOp(opPos);
/*  987 */     boolean simpleInit = false;
/*  988 */     boolean prevIsOneStepDown = true;
/*      */ 
/*  990 */     int whatToShow = compiler.getWhatToShow(opPos);
/*  991 */     StepPattern ai = null;
/*      */     int axis;
/*      */     int predicateAxis;
/*  994 */     switch (stepType)
/*      */     {
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/* 1000 */       prevIsOneStepDown = false;
/*      */       Expression expr;
/* 1004 */       switch (stepType)
/*      */       {
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/* 1010 */         expr = compiler.compile(opPos);
/* 1011 */         break;
/*      */       default:
/* 1013 */         expr = compiler.compile(opPos + 2);
/*      */       }
/*      */ 
/* 1016 */       axis = 20;
/* 1017 */       predicateAxis = 20;
/* 1018 */       ai = new FunctionPattern(expr, axis, predicateAxis);
/* 1019 */       simpleInit = true;
/* 1020 */       break;
/*      */     case 50:
/* 1022 */       whatToShow = 1280;
/*      */ 
/* 1025 */       axis = 19;
/* 1026 */       predicateAxis = 19;
/* 1027 */       ai = new StepPattern(1280, axis, predicateAxis);
/*      */ 
/* 1030 */       break;
/*      */     case 39:
/* 1032 */       whatToShow = 2;
/* 1033 */       axis = 10;
/* 1034 */       predicateAxis = 2;
/*      */ 
/* 1036 */       break;
/*      */     case 49:
/* 1038 */       whatToShow = 4096;
/* 1039 */       axis = 10;
/* 1040 */       predicateAxis = 9;
/*      */ 
/* 1042 */       break;
/*      */     case 37:
/* 1044 */       axis = 4;
/* 1045 */       predicateAxis = 0;
/* 1046 */       break;
/*      */     case 40:
/* 1048 */       axis = 10;
/* 1049 */       predicateAxis = 3;
/* 1050 */       break;
/*      */     case 38:
/* 1052 */       axis = 5;
/* 1053 */       predicateAxis = 1;
/* 1054 */       break;
/*      */     case 48:
/* 1056 */       axis = 13;
/* 1057 */       predicateAxis = 13;
/* 1058 */       break;
/*      */     case 45:
/* 1060 */       axis = 3;
/* 1061 */       predicateAxis = 10;
/* 1062 */       break;
/*      */     case 47:
/* 1064 */       axis = 7;
/* 1065 */       predicateAxis = 12;
/* 1066 */       break;
/*      */     case 46:
/* 1068 */       axis = 6;
/* 1069 */       predicateAxis = 11;
/* 1070 */       break;
/*      */     case 44:
/* 1072 */       axis = 12;
/* 1073 */       predicateAxis = 7;
/* 1074 */       break;
/*      */     case 43:
/* 1076 */       axis = 11;
/* 1077 */       predicateAxis = 6;
/* 1078 */       break;
/*      */     case 42:
/* 1080 */       axis = 1;
/* 1081 */       predicateAxis = 5;
/* 1082 */       break;
/*      */     case 41:
/* 1084 */       axis = 0;
/* 1085 */       predicateAxis = 4;
/* 1086 */       break;
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     default:
/* 1088 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */     }
/*      */ 
/* 1091 */     if (null == ai)
/*      */     {
/* 1093 */       whatToShow = compiler.getWhatToShow(opPos);
/* 1094 */       ai = new StepPattern(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos), axis, predicateAxis);
/*      */     }
/*      */ 
/* 1109 */     int argLen = compiler.getFirstPredicateOpPos(opPos);
/*      */ 
/* 1111 */     ai.setPredicates(compiler.getCompiledPredicates(argLen));
/*      */ 
/* 1113 */     return ai;
/*      */   }
/*      */ 
/*      */   static boolean analyzePredicate(Compiler compiler, int opPos, int stepType)
/*      */     throws TransformerException
/*      */   {
/*      */     int argLen;
/* 1135 */     switch (stepType)
/*      */     {
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/* 1141 */       argLen = compiler.getArgLength(opPos);
/* 1142 */       break;
/*      */     default:
/* 1144 */       argLen = compiler.getArgLengthOfStep(opPos);
/*      */     }
/*      */ 
/* 1147 */     int pos = compiler.getFirstPredicateOpPos(opPos);
/* 1148 */     int nPredicates = compiler.countPredicates(pos);
/*      */ 
/* 1150 */     return nPredicates > 0;
/*      */   }
/*      */ 
/*      */   private static AxesWalker createDefaultWalker(Compiler compiler, int opPos, WalkingIterator lpi, int analysis)
/*      */   {
/* 1170 */     AxesWalker ai = null;
/* 1171 */     int stepType = compiler.getOp(opPos);
/*      */ 
/* 1181 */     boolean simpleInit = false;
/* 1182 */     int totalNumberWalkers = analysis & 0xFF;
/* 1183 */     boolean prevIsOneStepDown = true;
/*      */ 
/* 1185 */     switch (stepType)
/*      */     {
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/* 1191 */       prevIsOneStepDown = false;
/*      */ 
/* 1197 */       ai = new FilterExprWalker(lpi);
/* 1198 */       simpleInit = true;
/* 1199 */       break;
/*      */     case 50:
/* 1201 */       ai = new AxesWalker(lpi, 19);
/* 1202 */       break;
/*      */     case 37:
/* 1204 */       prevIsOneStepDown = false;
/* 1205 */       ai = new ReverseAxesWalker(lpi, 0);
/* 1206 */       break;
/*      */     case 38:
/* 1208 */       prevIsOneStepDown = false;
/* 1209 */       ai = new ReverseAxesWalker(lpi, 1);
/* 1210 */       break;
/*      */     case 39:
/* 1212 */       ai = new AxesWalker(lpi, 2);
/* 1213 */       break;
/*      */     case 49:
/* 1215 */       ai = new AxesWalker(lpi, 9);
/* 1216 */       break;
/*      */     case 40:
/* 1218 */       ai = new AxesWalker(lpi, 3);
/* 1219 */       break;
/*      */     case 41:
/* 1221 */       prevIsOneStepDown = false;
/* 1222 */       ai = new AxesWalker(lpi, 4);
/* 1223 */       break;
/*      */     case 42:
/* 1225 */       prevIsOneStepDown = false;
/* 1226 */       ai = new AxesWalker(lpi, 5);
/* 1227 */       break;
/*      */     case 43:
/* 1229 */       prevIsOneStepDown = false;
/* 1230 */       ai = new AxesWalker(lpi, 6);
/* 1231 */       break;
/*      */     case 44:
/* 1233 */       prevIsOneStepDown = false;
/* 1234 */       ai = new AxesWalker(lpi, 7);
/* 1235 */       break;
/*      */     case 46:
/* 1237 */       prevIsOneStepDown = false;
/* 1238 */       ai = new ReverseAxesWalker(lpi, 11);
/* 1239 */       break;
/*      */     case 47:
/* 1241 */       prevIsOneStepDown = false;
/* 1242 */       ai = new ReverseAxesWalker(lpi, 12);
/* 1243 */       break;
/*      */     case 45:
/* 1245 */       prevIsOneStepDown = false;
/* 1246 */       ai = new ReverseAxesWalker(lpi, 10);
/* 1247 */       break;
/*      */     case 48:
/* 1249 */       ai = new AxesWalker(lpi, 13);
/* 1250 */       break;
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     default:
/* 1252 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */     }
/*      */ 
/* 1256 */     if (simpleInit)
/*      */     {
/* 1258 */       ai.initNodeTest(-1);
/*      */     }
/*      */     else
/*      */     {
/* 1262 */       int whatToShow = compiler.getWhatToShow(opPos);
/*      */ 
/* 1271 */       if ((0 == (whatToShow & 0x1043)) || (whatToShow == -1))
/*      */       {
/* 1274 */         ai.initNodeTest(whatToShow);
/*      */       }
/*      */       else {
/* 1277 */         ai.initNodeTest(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1282 */     return ai;
/*      */   }
/*      */ 
/*      */   public static String getAnalysisString(int analysis)
/*      */   {
/* 1287 */     StringBuffer buf = new StringBuffer();
/* 1288 */     buf.append("count: ").append(getStepCount(analysis)).append(' ');
/* 1289 */     if ((analysis & 0x40000000) != 0)
/*      */     {
/* 1291 */       buf.append("NTANY|");
/*      */     }
/* 1293 */     if ((analysis & 0x1000) != 0)
/*      */     {
/* 1295 */       buf.append("PRED|");
/*      */     }
/* 1297 */     if ((analysis & 0x2000) != 0)
/*      */     {
/* 1299 */       buf.append("ANC|");
/*      */     }
/* 1301 */     if ((analysis & 0x4000) != 0)
/*      */     {
/* 1303 */       buf.append("ANCOS|");
/*      */     }
/* 1305 */     if ((analysis & 0x8000) != 0)
/*      */     {
/* 1307 */       buf.append("ATTR|");
/*      */     }
/* 1309 */     if ((analysis & 0x10000) != 0)
/*      */     {
/* 1311 */       buf.append("CH|");
/*      */     }
/* 1313 */     if ((analysis & 0x20000) != 0)
/*      */     {
/* 1315 */       buf.append("DESC|");
/*      */     }
/* 1317 */     if ((analysis & 0x40000) != 0)
/*      */     {
/* 1319 */       buf.append("DESCOS|");
/*      */     }
/* 1321 */     if ((analysis & 0x80000) != 0)
/*      */     {
/* 1323 */       buf.append("FOL|");
/*      */     }
/* 1325 */     if ((analysis & 0x100000) != 0)
/*      */     {
/* 1327 */       buf.append("FOLS|");
/*      */     }
/* 1329 */     if ((analysis & 0x200000) != 0)
/*      */     {
/* 1331 */       buf.append("NS|");
/*      */     }
/* 1333 */     if ((analysis & 0x400000) != 0)
/*      */     {
/* 1335 */       buf.append("P|");
/*      */     }
/* 1337 */     if ((analysis & 0x800000) != 0)
/*      */     {
/* 1339 */       buf.append("PREC|");
/*      */     }
/* 1341 */     if ((analysis & 0x1000000) != 0)
/*      */     {
/* 1343 */       buf.append("PRECS|");
/*      */     }
/* 1345 */     if ((analysis & 0x2000000) != 0)
/*      */     {
/* 1347 */       buf.append(".|");
/*      */     }
/* 1349 */     if ((analysis & 0x4000000) != 0)
/*      */     {
/* 1351 */       buf.append("FLT|");
/*      */     }
/* 1353 */     if ((analysis & 0x8000000) != 0)
/*      */     {
/* 1355 */       buf.append("R|");
/*      */     }
/* 1357 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   public static boolean hasPredicate(int analysis)
/*      */   {
/* 1371 */     return 0 != (analysis & 0x1000);
/*      */   }
/*      */ 
/*      */   public static boolean isWild(int analysis)
/*      */   {
/* 1376 */     return 0 != (analysis & 0x40000000);
/*      */   }
/*      */ 
/*      */   public static boolean walksAncestors(int analysis)
/*      */   {
/* 1381 */     return isSet(analysis, 24576);
/*      */   }
/*      */ 
/*      */   public static boolean walksAttributes(int analysis)
/*      */   {
/* 1386 */     return 0 != (analysis & 0x8000);
/*      */   }
/*      */ 
/*      */   public static boolean walksNamespaces(int analysis)
/*      */   {
/* 1391 */     return 0 != (analysis & 0x200000);
/*      */   }
/*      */ 
/*      */   public static boolean walksChildren(int analysis)
/*      */   {
/* 1396 */     return 0 != (analysis & 0x10000);
/*      */   }
/*      */ 
/*      */   public static boolean walksDescendants(int analysis)
/*      */   {
/* 1401 */     return isSet(analysis, 393216);
/*      */   }
/*      */ 
/*      */   public static boolean walksSubtree(int analysis)
/*      */   {
/* 1406 */     return isSet(analysis, 458752);
/*      */   }
/*      */ 
/*      */   public static boolean walksSubtreeOnlyMaybeAbsolute(int analysis)
/*      */   {
/* 1411 */     return (walksSubtree(analysis)) && (!walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksSubtreeOnly(int analysis)
/*      */   {
/* 1420 */     return (walksSubtreeOnlyMaybeAbsolute(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksFilteredList(int analysis)
/*      */   {
/* 1427 */     return isSet(analysis, 67108864);
/*      */   }
/*      */ 
/*      */   public static boolean walksSubtreeOnlyFromRootOrContext(int analysis)
/*      */   {
/* 1432 */     return (walksSubtree(analysis)) && (!walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isSet(analysis, 67108864));
/*      */   }
/*      */ 
/*      */   public static boolean walksInDocOrder(int analysis)
/*      */   {
/* 1442 */     return ((walksSubtreeOnlyMaybeAbsolute(analysis)) || (walksExtraNodesOnly(analysis)) || (walksFollowingOnlyMaybeAbsolute(analysis))) && (!isSet(analysis, 67108864));
/*      */   }
/*      */ 
/*      */   public static boolean walksFollowingOnlyMaybeAbsolute(int analysis)
/*      */   {
/* 1451 */     return (isSet(analysis, 35127296)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksUp(int analysis)
/*      */   {
/* 1460 */     return isSet(analysis, 4218880);
/*      */   }
/*      */ 
/*      */   public static boolean walksSideways(int analysis)
/*      */   {
/* 1465 */     return isSet(analysis, 26738688);
/*      */   }
/*      */ 
/*      */   public static boolean walksExtraNodes(int analysis)
/*      */   {
/* 1471 */     return isSet(analysis, 2129920);
/*      */   }
/*      */ 
/*      */   public static boolean walksExtraNodesOnly(int analysis)
/*      */   {
/* 1476 */     return (walksExtraNodes(analysis)) && (!isSet(analysis, 33554432)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean isAbsolute(int analysis)
/*      */   {
/* 1487 */     return isSet(analysis, 201326592);
/*      */   }
/*      */ 
/*      */   public static boolean walksChildrenOnly(int analysis)
/*      */   {
/* 1492 */     return (walksChildren(analysis)) && (!isSet(analysis, 33554432)) && (!walksExtraNodes(analysis)) && (!walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
/*      */   }
/*      */ 
/*      */   public static boolean walksChildrenAndExtraAndSelfOnly(int analysis)
/*      */   {
/* 1504 */     return (walksChildren(analysis)) && (!walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
/*      */   }
/*      */ 
/*      */   public static boolean walksDescendantsAndExtraAndSelfOnly(int analysis)
/*      */   {
/* 1514 */     return (!walksChildren(analysis)) && (walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
/*      */   }
/*      */ 
/*      */   public static boolean walksSelfOnly(int analysis)
/*      */   {
/* 1524 */     return (isSet(analysis, 33554432)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksUpOnly(int analysis)
/*      */   {
/* 1535 */     return (!walksSubtree(analysis)) && (walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksDownOnly(int analysis)
/*      */   {
/* 1544 */     return (walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean walksDownExtraOnly(int analysis)
/*      */   {
/* 1553 */     return (walksSubtree(analysis)) && (walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
/*      */   }
/*      */ 
/*      */   public static boolean canSkipSubtrees(int analysis)
/*      */   {
/* 1562 */     return isSet(analysis, 65536) | walksSideways(analysis);
/*      */   }
/*      */ 
/*      */   public static boolean canCrissCross(int analysis)
/*      */   {
/* 1568 */     if (walksSelfOnly(analysis))
/* 1569 */       return false;
/* 1570 */     if ((walksDownOnly(analysis)) && (!canSkipSubtrees(analysis)))
/* 1571 */       return false;
/* 1572 */     if (walksChildrenAndExtraAndSelfOnly(analysis))
/* 1573 */       return false;
/* 1574 */     if (walksDescendantsAndExtraAndSelfOnly(analysis))
/* 1575 */       return false;
/* 1576 */     if (walksUpOnly(analysis))
/* 1577 */       return false;
/* 1578 */     if (walksExtraNodesOnly(analysis))
/* 1579 */       return false;
/* 1580 */     if ((walksSubtree(analysis)) && ((walksSideways(analysis)) || (walksUp(analysis)) || (canSkipSubtrees(analysis))))
/*      */     {
/* 1584 */       return true;
/*      */     }
/* 1586 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isNaturalDocOrder(int analysis)
/*      */   {
/* 1601 */     if ((canCrissCross(analysis)) || (isSet(analysis, 2097152)) || (walksFilteredList(analysis)))
/*      */     {
/* 1603 */       return false;
/*      */     }
/* 1605 */     if (walksInDocOrder(analysis)) {
/* 1606 */       return true;
/*      */     }
/* 1608 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isNaturalDocOrder(Compiler compiler, int stepOpCodePos, int stepIndex, int analysis)
/*      */     throws TransformerException
/*      */   {
/* 1629 */     if (canCrissCross(analysis)) {
/* 1630 */       return false;
/*      */     }
/*      */ 
/* 1634 */     if (isSet(analysis, 2097152)) {
/* 1635 */       return false;
/*      */     }
/*      */ 
/* 1643 */     if ((isSet(analysis, 1572864)) && (isSet(analysis, 25165824)))
/*      */     {
/* 1645 */       return false;
/*      */     }
/*      */ 
/* 1653 */     int stepCount = 0;
/* 1654 */     boolean foundWildAttribute = false;
/*      */ 
/* 1659 */     int potentialDuplicateMakingStepCount = 0;
/*      */     int stepType;
/* 1661 */     while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
/*      */     {
/* 1663 */       stepCount++;
/*      */ 
/* 1665 */       switch (stepType)
/*      */       {
/*      */       case 39:
/*      */       case 51:
/* 1669 */         if (foundWildAttribute) {
/* 1670 */           return false;
/*      */         }
/*      */ 
/* 1675 */         String localName = compiler.getStepLocalName(stepOpCodePos);
/*      */ 
/* 1677 */         if (localName.equals("*"))
/*      */         {
/* 1679 */           foundWildAttribute = true; } break;
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 37:
/*      */       case 38:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 46:
/*      */       case 47:
/*      */       case 49:
/*      */       case 52:
/*      */       case 53:
/* 1698 */         if (potentialDuplicateMakingStepCount > 0)
/* 1699 */           return false;
/* 1700 */         potentialDuplicateMakingStepCount++;
/*      */       case 40:
/*      */       case 48:
/*      */       case 50:
/* 1704 */         if (foundWildAttribute)
/* 1705 */           return false; break;
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       default:
/* 1708 */         throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
/*      */       }
/*      */ 
/* 1712 */       int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
/*      */ 
/* 1714 */       if (nextStepOpCodePos < 0) {
/*      */         break;
/*      */       }
/* 1717 */       stepOpCodePos = nextStepOpCodePos;
/*      */     }
/*      */ 
/* 1720 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isOneStep(int analysis)
/*      */   {
/* 1725 */     return (analysis & 0xFF) == 1;
/*      */   }
/*      */ 
/*      */   public static int getStepCount(int analysis)
/*      */   {
/* 1730 */     return analysis & 0xFF;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.WalkerFactory
 * JD-Core Version:    0.6.2
 */