/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*      */ import sun.org.mozilla.javascript.internal.debug.DebugFrame;
/*      */ import sun.org.mozilla.javascript.internal.debug.Debugger;
/*      */ 
/*      */ public final class Interpreter extends Icode
/*      */   implements Evaluator
/*      */ {
/*      */   InterpreterData itsData;
/*      */   static final int EXCEPTION_TRY_START_SLOT = 0;
/*      */   static final int EXCEPTION_TRY_END_SLOT = 1;
/*      */   static final int EXCEPTION_HANDLER_SLOT = 2;
/*      */   static final int EXCEPTION_TYPE_SLOT = 3;
/*      */   static final int EXCEPTION_LOCAL_SLOT = 4;
/*      */   static final int EXCEPTION_SCOPE_SLOT = 5;
/*      */   static final int EXCEPTION_SLOT_SIZE = 6;
/*      */ 
/*      */   private static CallFrame captureFrameForGenerator(CallFrame paramCallFrame)
/*      */   {
/*  196 */     paramCallFrame.frozen = true;
/*  197 */     CallFrame localCallFrame = paramCallFrame.cloneFrozen();
/*  198 */     paramCallFrame.frozen = false;
/*      */ 
/*  201 */     localCallFrame.parentFrame = null;
/*  202 */     localCallFrame.frameIndex = 0;
/*      */ 
/*  204 */     return localCallFrame;
/*      */   }
/*      */ 
/*      */   public Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean)
/*      */   {
/*  227 */     CodeGenerator localCodeGenerator = new CodeGenerator();
/*  228 */     this.itsData = localCodeGenerator.compile(paramCompilerEnvirons, paramScriptNode, paramString, paramBoolean);
/*  229 */     return this.itsData;
/*      */   }
/*      */ 
/*      */   public Script createScriptObject(Object paramObject1, Object paramObject2)
/*      */   {
/*  234 */     if (paramObject1 != this.itsData)
/*      */     {
/*  236 */       Kit.codeBug();
/*      */     }
/*  238 */     return InterpretedFunction.createScript(this.itsData, paramObject2);
/*      */   }
/*      */ 
/*      */   public void setEvalScriptFlag(Script paramScript)
/*      */   {
/*  243 */     ((InterpretedFunction)paramScript).idata.evalScriptFlag = true;
/*      */   }
/*      */ 
/*      */   public Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2)
/*      */   {
/*  250 */     if (paramObject1 != this.itsData)
/*      */     {
/*  252 */       Kit.codeBug();
/*      */     }
/*  254 */     return InterpretedFunction.createFunction(paramContext, paramScriptable, this.itsData, paramObject2);
/*      */   }
/*      */ 
/*      */   private static int getShort(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  259 */     return paramArrayOfByte[paramInt] << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF;
/*      */   }
/*      */ 
/*      */   private static int getIndex(byte[] paramArrayOfByte, int paramInt) {
/*  263 */     return (paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF;
/*      */   }
/*      */ 
/*      */   private static int getInt(byte[] paramArrayOfByte, int paramInt) {
/*  267 */     return paramArrayOfByte[paramInt] << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF;
/*      */   }
/*      */ 
/*      */   private static int getExceptionHandler(CallFrame paramCallFrame, boolean paramBoolean)
/*      */   {
/*  274 */     int[] arrayOfInt = paramCallFrame.idata.itsExceptionTable;
/*  275 */     if (arrayOfInt == null)
/*      */     {
/*  277 */       return -1;
/*      */     }
/*      */ 
/*  283 */     int i = paramCallFrame.pc - 1;
/*      */ 
/*  286 */     int j = -1; int k = 0; int m = 0;
/*  287 */     for (int n = 0; n != arrayOfInt.length; n += 6) {
/*  288 */       int i1 = arrayOfInt[(n + 0)];
/*  289 */       int i2 = arrayOfInt[(n + 1)];
/*  290 */       if ((i1 <= i) && (i < i2))
/*      */       {
/*  293 */         if ((!paramBoolean) || (arrayOfInt[(n + 3)] == 1))
/*      */         {
/*  296 */           if (j >= 0)
/*      */           {
/*  300 */             if (m < i2)
/*      */             {
/*      */               continue;
/*      */             }
/*  304 */             if (k > i1) Kit.codeBug();
/*  305 */             if (m == i2) Kit.codeBug();
/*      */           }
/*  307 */           j = n;
/*  308 */           k = i1;
/*  309 */           m = i2;
/*      */         }
/*      */       }
/*      */     }
/*  311 */     return j;
/*      */   }
/*      */ 
/*      */   static void dumpICode(InterpreterData paramInterpreterData)
/*      */   {
/*      */   }
/*      */ 
/*      */   private static int bytecodeSpan(int paramInt)
/*      */   {
/*  527 */     switch (paramInt)
/*      */     {
/*      */     case -63:
/*      */     case -62:
/*      */     case 50:
/*      */     case 72:
/*  533 */       return 3;
/*      */     case -54:
/*      */     case -23:
/*      */     case -6:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  542 */       return 3;
/*      */     case -21:
/*  548 */       return 5;
/*      */     case 57:
/*  552 */       return 2;
/*      */     case -11:
/*      */     case -10:
/*      */     case -9:
/*      */     case -8:
/*      */     case -7:
/*  560 */       return 2;
/*      */     case -27:
/*  564 */       return 3;
/*      */     case -28:
/*  568 */       return 5;
/*      */     case -38:
/*  572 */       return 2;
/*      */     case -39:
/*  576 */       return 3;
/*      */     case -40:
/*  580 */       return 5;
/*      */     case -45:
/*  584 */       return 2;
/*      */     case -46:
/*  588 */       return 3;
/*      */     case -47:
/*  592 */       return 5;
/*      */     case -61:
/*      */     case -49:
/*      */     case -48:
/*  598 */       return 2;
/*      */     case -26:
/*  602 */       return 3;
/*      */     }
/*  604 */     if (!validBytecode(paramInt)) throw Kit.codeBug();
/*  605 */     return 1;
/*      */   }
/*      */ 
/*      */   static int[] getLineNumbers(InterpreterData paramInterpreterData)
/*      */   {
/*  610 */     UintMap localUintMap = new UintMap();
/*      */ 
/*  612 */     byte[] arrayOfByte = paramInterpreterData.itsICode;
/*  613 */     int i = arrayOfByte.length;
/*  614 */     for (int j = 0; j != i; ) {
/*  615 */       int k = arrayOfByte[j];
/*  616 */       int m = bytecodeSpan(k);
/*  617 */       if (k == -26) {
/*  618 */         if (m != 3) Kit.codeBug();
/*  619 */         int n = getIndex(arrayOfByte, j + 1);
/*  620 */         localUintMap.put(n, 0);
/*      */       }
/*  622 */       j += m;
/*      */     }
/*      */ 
/*  625 */     return localUintMap.getKeys();
/*      */   }
/*      */ 
/*      */   public void captureStackInfo(RhinoException paramRhinoException)
/*      */   {
/*  630 */     Context localContext = Context.getCurrentContext();
/*  631 */     if ((localContext == null) || (localContext.lastInterpreterFrame == null))
/*      */     {
/*  633 */       paramRhinoException.interpreterStackInfo = null;
/*  634 */       paramRhinoException.interpreterLineData = null;
/*      */       return;
/*      */     }
/*      */     CallFrame[] arrayOfCallFrame;
/*  639 */     if ((localContext.previousInterpreterInvocations == null) || (localContext.previousInterpreterInvocations.size() == 0))
/*      */     {
/*  642 */       arrayOfCallFrame = new CallFrame[1];
/*      */     } else {
/*  644 */       i = localContext.previousInterpreterInvocations.size();
/*  645 */       if (localContext.previousInterpreterInvocations.peek() == localContext.lastInterpreterFrame)
/*      */       {
/*  652 */         i--;
/*      */       }
/*  654 */       arrayOfCallFrame = new CallFrame[i + 1];
/*  655 */       localContext.previousInterpreterInvocations.toArray(arrayOfCallFrame);
/*      */     }
/*  657 */     arrayOfCallFrame[(arrayOfCallFrame.length - 1)] = ((CallFrame)localContext.lastInterpreterFrame);
/*      */ 
/*  659 */     int i = 0;
/*  660 */     for (int j = 0; j != arrayOfCallFrame.length; j++) {
/*  661 */       i += 1 + arrayOfCallFrame[j].frameIndex;
/*      */     }
/*      */ 
/*  664 */     int[] arrayOfInt = new int[i];
/*      */ 
/*  667 */     int k = i;
/*  668 */     for (int m = arrayOfCallFrame.length; m != 0; ) {
/*  669 */       m--;
/*  670 */       CallFrame localCallFrame = arrayOfCallFrame[m];
/*  671 */       while (localCallFrame != null) {
/*  672 */         k--;
/*  673 */         arrayOfInt[k] = localCallFrame.pcSourceLineStart;
/*  674 */         localCallFrame = localCallFrame.parentFrame;
/*      */       }
/*      */     }
/*  677 */     if (k != 0) Kit.codeBug();
/*      */ 
/*  679 */     paramRhinoException.interpreterStackInfo = arrayOfCallFrame;
/*  680 */     paramRhinoException.interpreterLineData = arrayOfInt;
/*      */   }
/*      */ 
/*      */   public String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfInt)
/*      */   {
/*  685 */     CallFrame localCallFrame = (CallFrame)paramContext.lastInterpreterFrame;
/*  686 */     InterpreterData localInterpreterData = localCallFrame.idata;
/*  687 */     if (localCallFrame.pcSourceLineStart >= 0)
/*  688 */       paramArrayOfInt[0] = getIndex(localInterpreterData.itsICode, localCallFrame.pcSourceLineStart);
/*      */     else {
/*  690 */       paramArrayOfInt[0] = 0;
/*      */     }
/*  692 */     return localInterpreterData.itsSourceFile;
/*      */   }
/*      */ 
/*      */   public String getPatchedStack(RhinoException paramRhinoException, String paramString)
/*      */   {
/*  698 */     String str1 = "sun.org.mozilla.javascript.internal.Interpreter.interpretLoop";
/*  699 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length() + 1000);
/*  700 */     String str2 = SecurityUtilities.getSystemProperty("line.separator");
/*      */ 
/*  702 */     CallFrame[] arrayOfCallFrame = (CallFrame[])paramRhinoException.interpreterStackInfo;
/*  703 */     int[] arrayOfInt = paramRhinoException.interpreterLineData;
/*  704 */     int i = arrayOfCallFrame.length;
/*  705 */     int j = arrayOfInt.length;
/*  706 */     int k = 0;
/*  707 */     while (i != 0) {
/*  708 */       i--;
/*  709 */       int m = paramString.indexOf(str1, k);
/*  710 */       if (m < 0)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  715 */       m += str1.length();
/*      */ 
/*  717 */       for (; m != paramString.length(); m++) {
/*  718 */         int n = paramString.charAt(m);
/*  719 */         if ((n == 10) || (n == 13)) {
/*      */           break;
/*      */         }
/*      */       }
/*  723 */       localStringBuffer.append(paramString.substring(k, m));
/*  724 */       k = m;
/*      */ 
/*  726 */       CallFrame localCallFrame = arrayOfCallFrame[i];
/*  727 */       while (localCallFrame != null) {
/*  728 */         if (j == 0) Kit.codeBug();
/*  729 */         j--;
/*  730 */         InterpreterData localInterpreterData = localCallFrame.idata;
/*  731 */         localStringBuffer.append(str2);
/*  732 */         localStringBuffer.append("\tat script");
/*  733 */         if ((localInterpreterData.itsName != null) && (localInterpreterData.itsName.length() != 0)) {
/*  734 */           localStringBuffer.append('.');
/*  735 */           localStringBuffer.append(localInterpreterData.itsName);
/*      */         }
/*  737 */         localStringBuffer.append('(');
/*  738 */         localStringBuffer.append(localInterpreterData.itsSourceFile);
/*  739 */         int i1 = arrayOfInt[j];
/*  740 */         if (i1 >= 0)
/*      */         {
/*  742 */           localStringBuffer.append(':');
/*  743 */           localStringBuffer.append(getIndex(localInterpreterData.itsICode, i1));
/*      */         }
/*  745 */         localStringBuffer.append(')');
/*  746 */         localCallFrame = localCallFrame.parentFrame;
/*      */       }
/*      */     }
/*  749 */     localStringBuffer.append(paramString.substring(k));
/*      */ 
/*  751 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public List<String> getScriptStack(RhinoException paramRhinoException) {
/*  755 */     ScriptStackElement[][] arrayOfScriptStackElement1 = getScriptStackElements(paramRhinoException);
/*  756 */     ArrayList localArrayList = new ArrayList(arrayOfScriptStackElement1.length);
/*  757 */     String str = SecurityUtilities.getSystemProperty("line.separator");
/*      */ 
/*  759 */     for (ScriptStackElement[] arrayOfScriptStackElement3 : arrayOfScriptStackElement1) {
/*  760 */       StringBuilder localStringBuilder = new StringBuilder();
/*  761 */       for (ScriptStackElement localScriptStackElement : arrayOfScriptStackElement3) {
/*  762 */         localScriptStackElement.renderJavaStyle(localStringBuilder);
/*  763 */         localStringBuilder.append(str);
/*      */       }
/*  765 */       localArrayList.add(localStringBuilder.toString());
/*      */     }
/*  767 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public ScriptStackElement[][] getScriptStackElements(RhinoException paramRhinoException)
/*      */   {
/*  772 */     if (paramRhinoException.interpreterStackInfo == null) {
/*  773 */       return (ScriptStackElement[][])null;
/*      */     }
/*      */ 
/*  776 */     ArrayList localArrayList1 = new ArrayList();
/*      */ 
/*  778 */     CallFrame[] arrayOfCallFrame = (CallFrame[])paramRhinoException.interpreterStackInfo;
/*  779 */     int[] arrayOfInt = paramRhinoException.interpreterLineData;
/*  780 */     int i = arrayOfCallFrame.length;
/*  781 */     int j = arrayOfInt.length;
/*  782 */     while (i != 0) {
/*  783 */       i--;
/*  784 */       CallFrame localCallFrame = arrayOfCallFrame[i];
/*  785 */       ArrayList localArrayList2 = new ArrayList();
/*  786 */       while (localCallFrame != null) {
/*  787 */         if (j == 0) Kit.codeBug();
/*  788 */         j--;
/*  789 */         InterpreterData localInterpreterData = localCallFrame.idata;
/*  790 */         String str1 = localInterpreterData.itsSourceFile;
/*  791 */         String str2 = null;
/*  792 */         int k = -1;
/*  793 */         int m = arrayOfInt[j];
/*  794 */         if (m >= 0) {
/*  795 */           k = getIndex(localInterpreterData.itsICode, m);
/*      */         }
/*  797 */         if ((localInterpreterData.itsName != null) && (localInterpreterData.itsName.length() != 0)) {
/*  798 */           str2 = localInterpreterData.itsName;
/*      */         }
/*  800 */         localCallFrame = localCallFrame.parentFrame;
/*  801 */         localArrayList2.add(new ScriptStackElement(str1, str2, k));
/*      */       }
/*  803 */       localArrayList1.add(localArrayList2.toArray(new ScriptStackElement[localArrayList2.size()]));
/*      */     }
/*  805 */     return (ScriptStackElement[][])localArrayList1.toArray(new ScriptStackElement[localArrayList1.size()][]);
/*      */   }
/*      */ 
/*      */   static String getEncodedSource(InterpreterData paramInterpreterData)
/*      */   {
/*  810 */     if (paramInterpreterData.encodedSource == null) {
/*  811 */       return null;
/*      */     }
/*  813 */     return paramInterpreterData.encodedSource.substring(paramInterpreterData.encodedSourceStart, paramInterpreterData.encodedSourceEnd);
/*      */   }
/*      */ 
/*      */   private static void initFunction(Context paramContext, Scriptable paramScriptable, InterpretedFunction paramInterpretedFunction, int paramInt)
/*      */   {
/*  821 */     InterpretedFunction localInterpretedFunction = InterpretedFunction.createFunction(paramContext, paramScriptable, paramInterpretedFunction, paramInt);
/*  822 */     ScriptRuntime.initFunction(paramContext, paramScriptable, localInterpretedFunction, localInterpretedFunction.idata.itsFunctionType, paramInterpretedFunction.idata.evalScriptFlag);
/*      */   }
/*      */ 
/*      */   static Object interpret(InterpretedFunction paramInterpretedFunction, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*  830 */     if (!ScriptRuntime.hasTopCall(paramContext)) Kit.codeBug();
/*      */ 
/*  832 */     if (paramContext.interpreterSecurityDomain != paramInterpretedFunction.securityDomain) {
/*  833 */       localObject1 = paramContext.interpreterSecurityDomain;
/*  834 */       paramContext.interpreterSecurityDomain = paramInterpretedFunction.securityDomain;
/*      */       try {
/*  836 */         return paramInterpretedFunction.securityController.callWithDomain(paramInterpretedFunction.securityDomain, paramContext, paramInterpretedFunction, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */       }
/*      */       finally {
/*  839 */         paramContext.interpreterSecurityDomain = localObject1;
/*      */       }
/*      */     }
/*      */ 
/*  843 */     Object localObject1 = new CallFrame(null);
/*  844 */     initFrame(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, null, 0, paramArrayOfObject.length, paramInterpretedFunction, null, (CallFrame)localObject1);
/*      */ 
/*  846 */     ((CallFrame)localObject1).isContinuationsTopFrame = paramContext.isContinuationsTopCall;
/*  847 */     paramContext.isContinuationsTopCall = false;
/*      */ 
/*  849 */     return interpretLoop(paramContext, (CallFrame)localObject1, null);
/*      */   }
/*      */ 
/*      */   public static Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2)
/*      */   {
/*  868 */     CallFrame localCallFrame = (CallFrame)paramObject1;
/*  869 */     GeneratorState localGeneratorState = new GeneratorState(paramInt, paramObject2);
/*  870 */     if (paramInt == 2)
/*      */       try {
/*  872 */         return interpretLoop(paramContext, localCallFrame, localGeneratorState);
/*      */       }
/*      */       catch (RuntimeException localRuntimeException) {
/*  875 */         if (localRuntimeException != paramObject2) {
/*  876 */           throw localRuntimeException;
/*      */         }
/*  878 */         return Undefined.instance;
/*      */       }
/*  880 */     Object localObject = interpretLoop(paramContext, localCallFrame, localGeneratorState);
/*  881 */     if (localGeneratorState.returnedException != null)
/*  882 */       throw localGeneratorState.returnedException;
/*  883 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object restartContinuation(NativeContinuation paramNativeContinuation, Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/*  889 */     if (!ScriptRuntime.hasTopCall(paramContext))
/*  890 */       return ScriptRuntime.doTopCall(paramNativeContinuation, paramContext, paramScriptable, null, paramArrayOfObject);
/*      */     Object localObject;
/*  894 */     if (paramArrayOfObject.length == 0)
/*  895 */       localObject = Undefined.instance;
/*      */     else {
/*  897 */       localObject = paramArrayOfObject[0];
/*      */     }
/*      */ 
/*  900 */     CallFrame localCallFrame = (CallFrame)paramNativeContinuation.getImplementation();
/*  901 */     if (localCallFrame == null)
/*      */     {
/*  903 */       return localObject;
/*      */     }
/*      */ 
/*  906 */     ContinuationJump localContinuationJump = new ContinuationJump(paramNativeContinuation, null);
/*      */ 
/*  908 */     localContinuationJump.result = localObject;
/*  909 */     return interpretLoop(paramContext, null, localContinuationJump);
/*      */   }
/*      */ 
/*      */   private static Object interpretLoop(Context paramContext, CallFrame paramCallFrame, Object paramObject)
/*      */   {
/*  919 */     UniqueTag localUniqueTag = UniqueTag.DOUBLE_MARK;
/*  920 */     Object localObject1 = Undefined.instance;
/*      */ 
/*  922 */     boolean bool1 = paramContext.instructionThreshold != 0;
/*      */ 
/*  929 */     String str = null;
/*  930 */     int i = -1;
/*      */ 
/*  932 */     if (paramContext.lastInterpreterFrame != null)
/*      */     {
/*  935 */       if (paramContext.previousInterpreterInvocations == null) {
/*  936 */         paramContext.previousInterpreterInvocations = new ObjArray();
/*      */       }
/*  938 */       paramContext.previousInterpreterInvocations.push(paramContext.lastInterpreterFrame);
/*      */     }
/*      */ 
/*  948 */     GeneratorState localGeneratorState = null;
/*  949 */     if (paramObject != null) {
/*  950 */       if ((paramObject instanceof GeneratorState)) {
/*  951 */         localGeneratorState = (GeneratorState)paramObject;
/*      */ 
/*  954 */         enterFrame(paramContext, paramCallFrame, ScriptRuntime.emptyArgs, true);
/*  955 */         paramObject = null;
/*  956 */       } else if (!(paramObject instanceof ContinuationJump))
/*      */       {
/*  958 */         Kit.codeBug();
/*      */       }
/*      */     }
/*      */ 
/*  962 */     Object localObject2 = null;
/*  963 */     double d1 = 0.0D;
/*      */     try
/*      */     {
/*      */       while (true)
/*      */       {
/*  968 */         if (paramObject != null)
/*      */         {
/*  972 */           paramCallFrame = processThrowable(paramContext, paramObject, paramCallFrame, i, bool1);
/*      */ 
/*  974 */           paramObject = paramCallFrame.throwable;
/*  975 */           paramCallFrame.throwable = null;
/*      */         }
/*  977 */         else if ((localGeneratorState == null) && (paramCallFrame.frozen)) { Kit.codeBug(); }
/*      */ 
/*      */ 
/*  982 */         Object[] arrayOfObject1 = paramCallFrame.stack;
/*  983 */         double[] arrayOfDouble1 = paramCallFrame.sDbl;
/*  984 */         Object[] arrayOfObject2 = paramCallFrame.varSource.stack;
/*  985 */         double[] arrayOfDouble2 = paramCallFrame.varSource.sDbl;
/*  986 */         localObject3 = paramCallFrame.varSource.stackAttributes;
/*  987 */         byte[] arrayOfByte = paramCallFrame.idata.itsICode;
/*  988 */         String[] arrayOfString = paramCallFrame.idata.itsStringTable;
/*      */ 
/*  994 */         int k = paramCallFrame.savedStackTop;
/*      */ 
/*  997 */         paramContext.lastInterpreterFrame = paramCallFrame;
/*      */         while (true)
/*      */         {
/* 1004 */           int m = arrayOfByte[(paramCallFrame.pc++)];
/*      */           Object localObject4;
/*      */           Object localObject12;
/*      */           Object localObject5;
/*      */           Object localObject13;
/*      */           double d6;
/*      */           boolean bool5;
/*      */           label1628: boolean bool3;
/*      */           Object localObject6;
/*      */           int i1;
/*      */           double d2;
/*      */           Object localObject7;
/*      */           Object localObject14;
/*      */           Object localObject19;
/*      */           Object localObject22;
/*      */           Object localObject23;
/*      */           Object localObject24;
/*      */           Object localObject8;
/*      */           Object localObject15;
/*      */           Object localObject20;
/*      */           Object localObject21;
/*      */           Object localObject10;
/*      */           Object localObject16;
/*      */           int i10;
/*      */           Object localObject17;
/*      */           Object localObject11;
/*      */           int i5;
/* 1008 */           switch (m) {
/*      */           case -62:
/* 1010 */             if (!paramCallFrame.frozen)
/*      */             {
/* 1013 */               paramCallFrame.pc -= 1;
/* 1014 */               localObject4 = captureFrameForGenerator(paramCallFrame);
/* 1015 */               ((CallFrame)localObject4).frozen = true;
/* 1016 */               localObject12 = new NativeGenerator(paramCallFrame.scope, ((CallFrame)localObject4).fnOrScript, localObject4);
/*      */ 
/* 1018 */               paramCallFrame.result = localObject12;
/* 1019 */             }break;
/*      */           case 72:
/* 1026 */             if (!paramCallFrame.frozen) {
/* 1027 */               return freezeGenerator(paramContext, paramCallFrame, k, localGeneratorState);
/*      */             }
/* 1029 */             localObject4 = thawGenerator(paramCallFrame, k, localGeneratorState, m);
/* 1030 */             if (localObject4 != Scriptable.NOT_FOUND)
/* 1031 */               paramObject = localObject4;
/* 1032 */             break;
/*      */           case -63:
/* 1039 */             paramCallFrame.frozen = true;
/* 1040 */             int n = getIndex(arrayOfByte, paramCallFrame.pc);
/* 1041 */             localGeneratorState.returnedException = new JavaScriptException(NativeIterator.getStopIterationObject(paramCallFrame.scope), paramCallFrame.idata.itsSourceFile, n);
/*      */ 
/* 1044 */             break;
/*      */           case 73:
/* 1047 */             localObject5 = arrayOfObject1[k];
/* 1048 */             if (localObject5 == localUniqueTag) localObject5 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1049 */             k--;
/* 1050 */             localObject12 = (Scriptable)arrayOfObject1[k];
/* 1051 */             if (localObject12 != null) {
/* 1052 */               arrayOfObject1[k] = ScriptRuntime.setName((Scriptable)localObject12, localObject5, paramContext, paramCallFrame.scope, str);
/*      */             }
/*      */             else
/*      */             {
/* 1056 */               arrayOfObject1[k] = paramContext.newObject(paramCallFrame.scope, "ReferenceError", new Object[] { str });
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 50:
/* 1061 */             localObject5 = arrayOfObject1[k];
/* 1062 */             if (localObject5 == localUniqueTag) localObject5 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1063 */             k--;
/*      */ 
/* 1065 */             int i6 = getIndex(arrayOfByte, paramCallFrame.pc);
/* 1066 */             paramObject = new JavaScriptException(localObject5, paramCallFrame.idata.itsSourceFile, i6);
/*      */ 
/* 1069 */             break;
/*      */           case 51:
/* 1072 */             i += paramCallFrame.localShift;
/* 1073 */             paramObject = arrayOfObject1[i];
/* 1074 */             break;
/*      */           case 14:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/* 1080 */             k--;
/* 1081 */             localObject5 = arrayOfObject1[(k + 1)];
/* 1082 */             localObject13 = arrayOfObject1[k];
/*      */             double d9;
/* 1089 */             if (localObject5 == localUniqueTag) {
/* 1090 */               d6 = arrayOfDouble1[(k + 1)];
/* 1091 */               d9 = stack_double(paramCallFrame, k); } else {
/* 1092 */               if (localObject13 != localUniqueTag) break label1628;
/* 1093 */               d6 = ScriptRuntime.toNumber(localObject5);
/* 1094 */               d9 = arrayOfDouble1[k];
/*      */             }
/*      */ 
/* 1098 */             switch (m) {
/*      */             case 17:
/* 1100 */               bool5 = d9 >= d6;
/* 1101 */               break;
/*      */             case 15:
/* 1103 */               bool5 = d9 <= d6;
/* 1104 */               break;
/*      */             case 16:
/* 1106 */               bool5 = d9 > d6;
/* 1107 */               break;
/*      */             case 14:
/* 1109 */               bool5 = d9 < d6;
/* 1110 */               break;
/*      */             default:
/* 1112 */               throw Kit.codeBug();
/*      */             }
/*      */ 
/* 1115 */             switch (m) {
/*      */             case 17:
/* 1117 */               bool5 = ScriptRuntime.cmp_LE(localObject5, localObject13);
/* 1118 */               break;
/*      */             case 15:
/* 1120 */               bool5 = ScriptRuntime.cmp_LE(localObject13, localObject5);
/* 1121 */               break;
/*      */             case 16:
/* 1123 */               bool5 = ScriptRuntime.cmp_LT(localObject5, localObject13);
/* 1124 */               break;
/*      */             case 14:
/* 1126 */               bool5 = ScriptRuntime.cmp_LT(localObject13, localObject5);
/* 1127 */               break;
/*      */             default:
/* 1129 */               throw Kit.codeBug();
/*      */             }
/*      */ 
/* 1132 */             arrayOfObject1[k] = ScriptRuntime.wrapBoolean(bool5);
/* 1133 */             break;
/*      */           case 52:
/*      */           case 53:
/* 1137 */             localObject5 = arrayOfObject1[k];
/* 1138 */             if (localObject5 == localUniqueTag) localObject5 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1139 */             k--;
/* 1140 */             localObject13 = arrayOfObject1[k];
/* 1141 */             if (localObject13 == localUniqueTag) localObject13 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/*      */ 
/* 1143 */             if (m == 52)
/* 1144 */               bool5 = ScriptRuntime.in(localObject13, localObject5, paramContext);
/*      */             else {
/* 1146 */               bool5 = ScriptRuntime.instanceOf(localObject13, localObject5, paramContext);
/*      */             }
/* 1148 */             arrayOfObject1[k] = ScriptRuntime.wrapBoolean(bool5);
/* 1149 */             break;
/*      */           case 12:
/*      */           case 13:
/* 1153 */             k--;
/*      */ 
/* 1155 */             localObject13 = arrayOfObject1[(k + 1)];
/* 1156 */             Object localObject18 = arrayOfObject1[k];
/* 1157 */             if (localObject13 == localUniqueTag) {
/* 1158 */               if (localObject18 == localUniqueTag)
/* 1159 */                 bool3 = arrayOfDouble1[k] == arrayOfDouble1[(k + 1)];
/*      */               else {
/* 1161 */                 bool3 = ScriptRuntime.eqNumber(arrayOfDouble1[(k + 1)], localObject18);
/*      */               }
/*      */             }
/* 1164 */             else if (localObject18 == localUniqueTag)
/* 1165 */               bool3 = ScriptRuntime.eqNumber(arrayOfDouble1[k], localObject13);
/*      */             else {
/* 1167 */               bool3 = ScriptRuntime.eq(localObject18, localObject13);
/*      */             }
/*      */ 
/* 1170 */             bool3 ^= m == 13;
/* 1171 */             arrayOfObject1[k] = ScriptRuntime.wrapBoolean(bool3);
/* 1172 */             break;
/*      */           case 46:
/*      */           case 47:
/* 1176 */             k--;
/* 1177 */             bool3 = shallowEquals(arrayOfObject1, arrayOfDouble1, k);
/* 1178 */             bool3 ^= m == 47;
/* 1179 */             arrayOfObject1[k] = ScriptRuntime.wrapBoolean(bool3);
/* 1180 */             break;
/*      */           case 7:
/* 1183 */             if (stack_boolean(paramCallFrame, k--))
/* 1184 */               paramCallFrame.pc += 2;
/* 1185 */             break;
/*      */           case 6:
/* 1189 */             if (!stack_boolean(paramCallFrame, k--))
/* 1190 */               paramCallFrame.pc += 2;
/* 1191 */             break;
/*      */           case -6:
/* 1195 */             if (!stack_boolean(paramCallFrame, k--)) {
/* 1196 */               paramCallFrame.pc += 2;
/*      */             }
/*      */             else
/* 1199 */               arrayOfObject1[(k--)] = null;
/* 1200 */             break;
/*      */           case 5:
/* 1202 */             break;
/*      */           case -23:
/* 1204 */             k++;
/* 1205 */             arrayOfObject1[k] = localUniqueTag;
/* 1206 */             arrayOfDouble1[k] = (paramCallFrame.pc + 2);
/* 1207 */             break;
/*      */           case -24:
/* 1209 */             if (k == paramCallFrame.emptyStackTop + 1)
/*      */             {
/* 1211 */               i += paramCallFrame.localShift;
/* 1212 */               arrayOfObject1[i] = arrayOfObject1[k];
/* 1213 */               arrayOfDouble1[i] = arrayOfDouble1[k];
/* 1214 */               k--;
/*      */             }
/* 1218 */             else if (k != paramCallFrame.emptyStackTop) { Kit.codeBug(); } break;
/*      */           case -25:
/* 1223 */             if (bool1) {
/* 1224 */               addInstructionCount(paramContext, paramCallFrame, 0);
/*      */             }
/* 1226 */             i += paramCallFrame.localShift;
/* 1227 */             localObject6 = arrayOfObject1[i];
/* 1228 */             if (localObject6 != localUniqueTag)
/*      */             {
/* 1230 */               paramObject = localObject6;
/* 1231 */               break label7582;
/*      */             }
/*      */ 
/* 1234 */             paramCallFrame.pc = ((int)arrayOfDouble1[i]);
/* 1235 */             if (bool1)
/* 1236 */               paramCallFrame.pcPrevBranch = paramCallFrame.pc; break;
/*      */           case -4:
/* 1241 */             arrayOfObject1[k] = null;
/* 1242 */             k--;
/* 1243 */             break;
/*      */           case -5:
/* 1245 */             paramCallFrame.result = arrayOfObject1[k];
/* 1246 */             paramCallFrame.resultDbl = arrayOfDouble1[k];
/* 1247 */             arrayOfObject1[k] = null;
/* 1248 */             k--;
/* 1249 */             break;
/*      */           case -1:
/* 1251 */             arrayOfObject1[(k + 1)] = arrayOfObject1[k];
/* 1252 */             arrayOfDouble1[(k + 1)] = arrayOfDouble1[k];
/* 1253 */             k++;
/* 1254 */             break;
/*      */           case -2:
/* 1256 */             arrayOfObject1[(k + 1)] = arrayOfObject1[(k - 1)];
/* 1257 */             arrayOfDouble1[(k + 1)] = arrayOfDouble1[(k - 1)];
/* 1258 */             arrayOfObject1[(k + 2)] = arrayOfObject1[k];
/* 1259 */             arrayOfDouble1[(k + 2)] = arrayOfDouble1[k];
/* 1260 */             k += 2;
/* 1261 */             break;
/*      */           case -3:
/* 1263 */             localObject6 = arrayOfObject1[k];
/* 1264 */             arrayOfObject1[k] = arrayOfObject1[(k - 1)];
/* 1265 */             arrayOfObject1[(k - 1)] = localObject6;
/* 1266 */             double d3 = arrayOfDouble1[k];
/* 1267 */             arrayOfDouble1[k] = arrayOfDouble1[(k - 1)];
/* 1268 */             arrayOfDouble1[(k - 1)] = d3;
/* 1269 */             break;
/*      */           case 4:
/* 1272 */             paramCallFrame.result = arrayOfObject1[k];
/* 1273 */             paramCallFrame.resultDbl = arrayOfDouble1[k];
/* 1274 */             k--;
/* 1275 */             break;
/*      */           case 64:
/* 1277 */             break;
/*      */           case -22:
/* 1279 */             paramCallFrame.result = localObject1;
/* 1280 */             break;
/*      */           case 27:
/* 1282 */             i1 = stack_int32(paramCallFrame, k);
/* 1283 */             arrayOfObject1[k] = localUniqueTag;
/* 1284 */             arrayOfDouble1[k] = (i1 ^ 0xFFFFFFFF);
/* 1285 */             break;
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*      */           case 18:
/*      */           case 19:
/* 1292 */             i1 = stack_int32(paramCallFrame, k - 1);
/* 1293 */             int i7 = stack_int32(paramCallFrame, k);
/* 1294 */             arrayOfObject1[(--k)] = localUniqueTag;
/* 1295 */             switch (m) {
/*      */             case 11:
/* 1297 */               i1 &= i7;
/* 1298 */               break;
/*      */             case 9:
/* 1300 */               i1 |= i7;
/* 1301 */               break;
/*      */             case 10:
/* 1303 */               i1 ^= i7;
/* 1304 */               break;
/*      */             case 18:
/* 1306 */               i1 <<= i7;
/* 1307 */               break;
/*      */             case 19:
/* 1309 */               i1 >>= i7;
/*      */             case 12:
/*      */             case 13:
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/* 1312 */             case 17: } arrayOfDouble1[k] = i1;
/* 1313 */             break;
/*      */           case 20:
/* 1316 */             d2 = stack_double(paramCallFrame, k - 1);
/* 1317 */             int i11 = stack_int32(paramCallFrame, k) & 0x1F;
/* 1318 */             arrayOfObject1[(--k)] = localUniqueTag;
/* 1319 */             arrayOfDouble1[k] = (ScriptRuntime.toUint32(d2) >>> i11);
/* 1320 */             break;
/*      */           case 28:
/*      */           case 29:
/* 1324 */             d2 = stack_double(paramCallFrame, k);
/* 1325 */             arrayOfObject1[k] = localUniqueTag;
/* 1326 */             if (m == 29) {
/* 1327 */               d2 = -d2;
/*      */             }
/* 1329 */             arrayOfDouble1[k] = d2;
/* 1330 */             break;
/*      */           case 21:
/* 1333 */             k--;
/* 1334 */             do_add(arrayOfObject1, arrayOfDouble1, k, paramContext);
/* 1335 */             break;
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/* 1340 */             d2 = stack_double(paramCallFrame, k);
/* 1341 */             k--;
/* 1342 */             double d4 = stack_double(paramCallFrame, k);
/* 1343 */             arrayOfObject1[k] = localUniqueTag;
/* 1344 */             switch (m) {
/*      */             case 22:
/* 1346 */               d4 -= d2;
/* 1347 */               break;
/*      */             case 23:
/* 1349 */               d4 *= d2;
/* 1350 */               break;
/*      */             case 24:
/* 1352 */               d4 /= d2;
/* 1353 */               break;
/*      */             case 25:
/* 1355 */               d4 %= d2;
/*      */             }
/*      */ 
/* 1358 */             arrayOfDouble1[k] = d4;
/* 1359 */             break;
/*      */           case 26:
/* 1362 */             arrayOfObject1[k] = ScriptRuntime.wrapBoolean(!stack_boolean(paramCallFrame, k) ? 1 : false);
/*      */ 
/* 1364 */             break;
/*      */           case 49:
/* 1366 */             arrayOfObject1[(++k)] = ScriptRuntime.bind(paramContext, paramCallFrame.scope, str);
/* 1367 */             break;
/*      */           case 8:
/* 1369 */             localObject7 = arrayOfObject1[k];
/* 1370 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1371 */             k--;
/* 1372 */             localObject14 = (Scriptable)arrayOfObject1[k];
/* 1373 */             arrayOfObject1[k] = ScriptRuntime.setName((Scriptable)localObject14, localObject7, paramContext, paramCallFrame.scope, str);
/*      */ 
/* 1375 */             break;
/*      */           case -59:
/* 1378 */             localObject7 = arrayOfObject1[k];
/* 1379 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1380 */             k--;
/* 1381 */             localObject14 = (Scriptable)arrayOfObject1[k];
/* 1382 */             arrayOfObject1[k] = ScriptRuntime.setConst((Scriptable)localObject14, localObject7, paramContext, str);
/* 1383 */             break;
/*      */           case 31:
/* 1386 */             localObject7 = arrayOfObject1[k];
/* 1387 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1388 */             k--;
/* 1389 */             localObject14 = arrayOfObject1[k];
/* 1390 */             if (localObject14 == localUniqueTag) localObject14 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1391 */             arrayOfObject1[k] = ScriptRuntime.delete(localObject14, localObject7, paramContext);
/* 1392 */             break;
/*      */           case 34:
/* 1395 */             localObject7 = arrayOfObject1[k];
/* 1396 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1397 */             arrayOfObject1[k] = ScriptRuntime.getObjectPropNoWarn(localObject7, str, paramContext);
/* 1398 */             break;
/*      */           case 33:
/* 1401 */             localObject7 = arrayOfObject1[k];
/* 1402 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1403 */             arrayOfObject1[k] = ScriptRuntime.getObjectProp(localObject7, str, paramContext, paramCallFrame.scope);
/* 1404 */             break;
/*      */           case 35:
/* 1407 */             localObject7 = arrayOfObject1[k];
/* 1408 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1409 */             k--;
/* 1410 */             localObject14 = arrayOfObject1[k];
/* 1411 */             if (localObject14 == localUniqueTag) localObject14 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1412 */             arrayOfObject1[k] = ScriptRuntime.setObjectProp(localObject14, str, localObject7, paramContext);
/*      */ 
/* 1414 */             break;
/*      */           case -9:
/* 1417 */             localObject7 = arrayOfObject1[k];
/* 1418 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1419 */             arrayOfObject1[k] = ScriptRuntime.propIncrDecr(localObject7, str, paramContext, arrayOfByte[paramCallFrame.pc]);
/*      */ 
/* 1421 */             paramCallFrame.pc += 1;
/* 1422 */             break;
/*      */           case 36:
/* 1425 */             k--;
/* 1426 */             localObject7 = arrayOfObject1[k];
/* 1427 */             if (localObject7 == localUniqueTag) {
/* 1428 */               localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/*      */             }
/*      */ 
/* 1431 */             localObject19 = arrayOfObject1[(k + 1)];
/* 1432 */             if (localObject19 != localUniqueTag) {
/* 1433 */               localObject14 = ScriptRuntime.getObjectElem(localObject7, localObject19, paramContext, paramCallFrame.scope);
/*      */             } else {
/* 1435 */               d6 = arrayOfDouble1[(k + 1)];
/* 1436 */               localObject14 = ScriptRuntime.getObjectIndex(localObject7, d6, paramContext);
/*      */             }
/* 1438 */             arrayOfObject1[k] = localObject14;
/* 1439 */             break;
/*      */           case 37:
/* 1442 */             k -= 2;
/* 1443 */             localObject7 = arrayOfObject1[(k + 2)];
/* 1444 */             if (localObject7 == localUniqueTag) {
/* 1445 */               localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[(k + 2)]);
/*      */             }
/* 1447 */             localObject14 = arrayOfObject1[k];
/* 1448 */             if (localObject14 == localUniqueTag) {
/* 1449 */               localObject14 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/*      */             }
/*      */ 
/* 1452 */             localObject22 = arrayOfObject1[(k + 1)];
/* 1453 */             if (localObject22 != localUniqueTag) {
/* 1454 */               localObject19 = ScriptRuntime.setObjectElem(localObject14, localObject22, localObject7, paramContext);
/*      */             } else {
/* 1456 */               double d7 = arrayOfDouble1[(k + 1)];
/* 1457 */               localObject19 = ScriptRuntime.setObjectIndex(localObject14, d7, localObject7, paramContext);
/*      */             }
/* 1459 */             arrayOfObject1[k] = localObject19;
/* 1460 */             break;
/*      */           case -10:
/* 1463 */             localObject7 = arrayOfObject1[k];
/* 1464 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1465 */             k--;
/* 1466 */             localObject14 = arrayOfObject1[k];
/* 1467 */             if (localObject14 == localUniqueTag) localObject14 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1468 */             arrayOfObject1[k] = ScriptRuntime.elemIncrDecr(localObject14, localObject7, paramContext, arrayOfByte[paramCallFrame.pc]);
/*      */ 
/* 1470 */             paramCallFrame.pc += 1;
/* 1471 */             break;
/*      */           case 67:
/* 1474 */             localObject7 = (Ref)arrayOfObject1[k];
/* 1475 */             arrayOfObject1[k] = ScriptRuntime.refGet((Ref)localObject7, paramContext);
/* 1476 */             break;
/*      */           case 68:
/* 1479 */             localObject7 = arrayOfObject1[k];
/* 1480 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1481 */             k--;
/* 1482 */             localObject14 = (Ref)arrayOfObject1[k];
/* 1483 */             arrayOfObject1[k] = ScriptRuntime.refSet((Ref)localObject14, localObject7, paramContext);
/* 1484 */             break;
/*      */           case 69:
/* 1487 */             localObject7 = (Ref)arrayOfObject1[k];
/* 1488 */             arrayOfObject1[k] = ScriptRuntime.refDel((Ref)localObject7, paramContext);
/* 1489 */             break;
/*      */           case -11:
/* 1492 */             localObject7 = (Ref)arrayOfObject1[k];
/* 1493 */             arrayOfObject1[k] = ScriptRuntime.refIncrDecr((Ref)localObject7, paramContext, arrayOfByte[paramCallFrame.pc]);
/* 1494 */             paramCallFrame.pc += 1;
/* 1495 */             break;
/*      */           case 54:
/* 1498 */             k++;
/* 1499 */             i += paramCallFrame.localShift;
/* 1500 */             arrayOfObject1[k] = arrayOfObject1[i];
/* 1501 */             arrayOfDouble1[k] = arrayOfDouble1[i];
/* 1502 */             break;
/*      */           case -56:
/* 1504 */             i += paramCallFrame.localShift;
/* 1505 */             arrayOfObject1[i] = null;
/* 1506 */             break;
/*      */           case -15:
/* 1509 */             k++;
/* 1510 */             arrayOfObject1[k] = ScriptRuntime.getNameFunctionAndThis(str, paramContext, paramCallFrame.scope);
/*      */ 
/* 1512 */             k++;
/* 1513 */             arrayOfObject1[k] = ScriptRuntime.lastStoredScriptable(paramContext);
/* 1514 */             break;
/*      */           case -16:
/* 1516 */             localObject7 = arrayOfObject1[k];
/* 1517 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/*      */ 
/* 1519 */             arrayOfObject1[k] = ScriptRuntime.getPropFunctionAndThis(localObject7, str, paramContext, paramCallFrame.scope);
/*      */ 
/* 1521 */             k++;
/* 1522 */             arrayOfObject1[k] = ScriptRuntime.lastStoredScriptable(paramContext);
/* 1523 */             break;
/*      */           case -17:
/* 1526 */             localObject7 = arrayOfObject1[(k - 1)];
/* 1527 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[(k - 1)]);
/* 1528 */             localObject14 = arrayOfObject1[k];
/* 1529 */             if (localObject14 == localUniqueTag) localObject14 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1530 */             arrayOfObject1[(k - 1)] = ScriptRuntime.getElemFunctionAndThis(localObject7, localObject14, paramContext);
/* 1531 */             arrayOfObject1[k] = ScriptRuntime.lastStoredScriptable(paramContext);
/* 1532 */             break;
/*      */           case -18:
/* 1535 */             localObject7 = arrayOfObject1[k];
/* 1536 */             if (localObject7 == localUniqueTag) localObject7 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1537 */             arrayOfObject1[k] = ScriptRuntime.getValueFunctionAndThis(localObject7, paramContext);
/* 1538 */             k++;
/* 1539 */             arrayOfObject1[k] = ScriptRuntime.lastStoredScriptable(paramContext);
/* 1540 */             break;
/*      */           case -21:
/* 1543 */             if (bool1) {
/* 1544 */               paramContext.instructionCount += 100;
/*      */             }
/* 1546 */             int i2 = arrayOfByte[paramCallFrame.pc] & 0xFF;
/* 1547 */             int i8 = arrayOfByte[(paramCallFrame.pc + 1)] != 0 ? 1 : 0;
/* 1548 */             int i12 = getIndex(arrayOfByte, paramCallFrame.pc + 2);
/*      */ 
/* 1551 */             if (i8 != 0)
/*      */             {
/* 1553 */               k -= i;
/*      */ 
/* 1555 */               localObject22 = arrayOfObject1[k];
/* 1556 */               if (localObject22 == localUniqueTag)
/* 1557 */                 localObject22 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1558 */               localObject23 = getArgsArray(arrayOfObject1, arrayOfDouble1, k + 1, i);
/*      */ 
/* 1560 */               arrayOfObject1[k] = ScriptRuntime.newSpecial(paramContext, localObject22, (Object[])localObject23, paramCallFrame.scope, i2);
/*      */             }
/*      */             else
/*      */             {
/* 1564 */               k -= 1 + i;
/*      */ 
/* 1568 */               localObject22 = (Scriptable)arrayOfObject1[(k + 1)];
/* 1569 */               localObject23 = (Callable)arrayOfObject1[k];
/* 1570 */               localObject24 = getArgsArray(arrayOfObject1, arrayOfDouble1, k + 2, i);
/*      */ 
/* 1572 */               arrayOfObject1[k] = ScriptRuntime.callSpecial(paramContext, (Callable)localObject23, (Scriptable)localObject22, (Object[])localObject24, paramCallFrame.scope, paramCallFrame.thisObj, i2, paramCallFrame.idata.itsSourceFile, i12);
/*      */             }
/*      */ 
/* 1577 */             paramCallFrame.pc += 4;
/* 1578 */             break;
/*      */           case -55:
/*      */           case 38:
/*      */           case 70:
/* 1583 */             if (bool1) {
/* 1584 */               paramContext.instructionCount += 100;
/*      */             }
/*      */ 
/* 1588 */             k -= 1 + i;
/*      */ 
/* 1592 */             localObject8 = (Callable)arrayOfObject1[k];
/* 1593 */             localObject15 = (Scriptable)arrayOfObject1[(k + 1)];
/* 1594 */             if (m == 70) {
/* 1595 */               localObject20 = getArgsArray(arrayOfObject1, arrayOfDouble1, k + 2, i);
/*      */ 
/* 1597 */               arrayOfObject1[k] = ScriptRuntime.callRef((Callable)localObject8, (Scriptable)localObject15, (Object[])localObject20, paramContext);
/*      */             }
/*      */             else
/*      */             {
/* 1601 */               localObject20 = paramCallFrame.scope;
/* 1602 */               if (paramCallFrame.useActivation) {
/* 1603 */                 localObject20 = ScriptableObject.getTopLevelScope(paramCallFrame.scope);
/*      */               }
/* 1605 */               if ((localObject8 instanceof InterpretedFunction)) {
/* 1606 */                 localObject22 = (InterpretedFunction)localObject8;
/* 1607 */                 if (paramCallFrame.fnOrScript.securityDomain == ((InterpretedFunction)localObject22).securityDomain) {
/* 1608 */                   localObject23 = paramCallFrame;
/* 1609 */                   localObject24 = new CallFrame(null);
/* 1610 */                   if (m == -55)
/*      */                   {
/* 1626 */                     localObject23 = paramCallFrame.parentFrame;
/*      */ 
/* 1629 */                     exitFrame(paramContext, paramCallFrame, null);
/*      */                   }
/* 1631 */                   initFrame(paramContext, (Scriptable)localObject20, (Scriptable)localObject15, arrayOfObject1, arrayOfDouble1, k + 2, i, (InterpretedFunction)localObject22, (CallFrame)localObject23, (CallFrame)localObject24);
/*      */ 
/* 1634 */                   if (m != -55) {
/* 1635 */                     paramCallFrame.savedStackTop = k;
/* 1636 */                     paramCallFrame.savedCallOp = m;
/*      */                   }
/* 1638 */                   paramCallFrame = (CallFrame)localObject24;
/* 1639 */                   break;
/*      */                 }
/*      */               }
/*      */ 
/* 1643 */               if ((localObject8 instanceof NativeContinuation))
/*      */               {
/* 1646 */                 localObject22 = new ContinuationJump((NativeContinuation)localObject8, paramCallFrame);
/*      */ 
/* 1650 */                 if (i == 0) {
/* 1651 */                   ((ContinuationJump)localObject22).result = localObject1;
/*      */                 } else {
/* 1653 */                   ((ContinuationJump)localObject22).result = arrayOfObject1[(k + 2)];
/* 1654 */                   ((ContinuationJump)localObject22).resultDbl = arrayOfDouble1[(k + 2)];
/*      */                 }
/*      */ 
/* 1658 */                 paramObject = localObject22;
/* 1659 */                 break label7582;
/*      */               }
/*      */ 
/* 1662 */               if ((localObject8 instanceof IdFunctionObject)) {
/* 1663 */                 localObject22 = (IdFunctionObject)localObject8;
/* 1664 */                 if (NativeContinuation.isContinuationConstructor((IdFunctionObject)localObject22)) {
/* 1665 */                   paramCallFrame.stack[k] = captureContinuation(paramContext, paramCallFrame.parentFrame, false);
/*      */ 
/* 1667 */                   continue;
/*      */                 }
/*      */ 
/* 1671 */                 if (BaseFunction.isApplyOrCall((IdFunctionObject)localObject22)) {
/* 1672 */                   localObject23 = ScriptRuntime.getCallable((Scriptable)localObject15);
/* 1673 */                   if ((localObject23 instanceof InterpretedFunction)) {
/* 1674 */                     localObject24 = (InterpretedFunction)localObject23;
/* 1675 */                     if (paramCallFrame.fnOrScript.securityDomain == ((InterpretedFunction)localObject24).securityDomain) {
/* 1676 */                       paramCallFrame = initFrameForApplyOrCall(paramContext, paramCallFrame, i, arrayOfObject1, arrayOfDouble1, k, m, (Scriptable)localObject20, (IdFunctionObject)localObject22, (InterpretedFunction)localObject24);
/*      */ 
/* 1679 */                       break;
/*      */                     }
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 1687 */               if ((localObject8 instanceof ScriptRuntime.NoSuchMethodShim))
/*      */               {
/* 1689 */                 localObject22 = (ScriptRuntime.NoSuchMethodShim)localObject8;
/* 1690 */                 localObject23 = ((ScriptRuntime.NoSuchMethodShim)localObject22).noSuchMethodMethod;
/*      */ 
/* 1692 */                 if ((localObject23 instanceof InterpretedFunction)) {
/* 1693 */                   localObject24 = (InterpretedFunction)localObject23;
/* 1694 */                   if (paramCallFrame.fnOrScript.securityDomain == ((InterpretedFunction)localObject24).securityDomain) {
/* 1695 */                     paramCallFrame = initFrameForNoSuchMethod(paramContext, paramCallFrame, i, arrayOfObject1, arrayOfDouble1, k, m, (Scriptable)localObject15, (Scriptable)localObject20, (ScriptRuntime.NoSuchMethodShim)localObject22, (InterpretedFunction)localObject24);
/*      */ 
/* 1698 */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/*      */ 
/* 1703 */               paramContext.lastInterpreterFrame = paramCallFrame;
/* 1704 */               paramCallFrame.savedCallOp = m;
/* 1705 */               paramCallFrame.savedStackTop = k;
/* 1706 */               arrayOfObject1[k] = ((Callable)localObject8).call(paramContext, (Scriptable)localObject20, (Scriptable)localObject15, getArgsArray(arrayOfObject1, arrayOfDouble1, k + 2, i));
/*      */             }
/*      */ 
/* 1709 */             break;
/*      */           case 30:
/* 1712 */             if (bool1) {
/* 1713 */               paramContext.instructionCount += 100;
/*      */             }
/*      */ 
/* 1717 */             k -= i;
/*      */ 
/* 1719 */             localObject8 = arrayOfObject1[k];
/* 1720 */             if ((localObject8 instanceof InterpretedFunction)) {
/* 1721 */               localObject15 = (InterpretedFunction)localObject8;
/* 1722 */               if (paramCallFrame.fnOrScript.securityDomain == ((InterpretedFunction)localObject15).securityDomain) {
/* 1723 */                 localObject20 = ((InterpretedFunction)localObject15).createObject(paramContext, paramCallFrame.scope);
/* 1724 */                 localObject22 = new CallFrame(null);
/* 1725 */                 initFrame(paramContext, paramCallFrame.scope, (Scriptable)localObject20, arrayOfObject1, arrayOfDouble1, k + 1, i, (InterpretedFunction)localObject15, paramCallFrame, (CallFrame)localObject22);
/*      */ 
/* 1729 */                 arrayOfObject1[k] = localObject20;
/* 1730 */                 paramCallFrame.savedStackTop = k;
/* 1731 */                 paramCallFrame.savedCallOp = m;
/* 1732 */                 paramCallFrame = (CallFrame)localObject22;
/* 1733 */                 break;
/*      */               }
/*      */             }
/* 1736 */             if (!(localObject8 instanceof Function)) {
/* 1737 */               if (localObject8 == localUniqueTag) localObject8 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1738 */               throw ScriptRuntime.notFunctionError(localObject8);
/*      */             }
/* 1740 */             localObject15 = (Function)localObject8;
/*      */ 
/* 1742 */             if ((localObject15 instanceof IdFunctionObject)) {
/* 1743 */               localObject20 = (IdFunctionObject)localObject15;
/* 1744 */               if (NativeContinuation.isContinuationConstructor((IdFunctionObject)localObject20)) {
/* 1745 */                 paramCallFrame.stack[k] = captureContinuation(paramContext, paramCallFrame.parentFrame, false);
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1751 */               localObject20 = getArgsArray(arrayOfObject1, arrayOfDouble1, k + 1, i);
/* 1752 */               arrayOfObject1[k] = ((Function)localObject15).construct(paramContext, paramCallFrame.scope, (Object[])localObject20);
/* 1753 */             }break;
/*      */           case 32:
/* 1756 */             localObject8 = arrayOfObject1[k];
/* 1757 */             if (localObject8 == localUniqueTag) localObject8 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1758 */             arrayOfObject1[k] = ScriptRuntime.typeof(localObject8);
/* 1759 */             break;
/*      */           case -14:
/* 1762 */             arrayOfObject1[(++k)] = ScriptRuntime.typeofName(paramCallFrame.scope, str);
/* 1763 */             break;
/*      */           case 41:
/* 1765 */             arrayOfObject1[(++k)] = str;
/* 1766 */             break;
/*      */           case -27:
/* 1768 */             k++;
/* 1769 */             arrayOfObject1[k] = localUniqueTag;
/* 1770 */             arrayOfDouble1[k] = getShort(arrayOfByte, paramCallFrame.pc);
/* 1771 */             paramCallFrame.pc += 2;
/* 1772 */             break;
/*      */           case -28:
/* 1774 */             k++;
/* 1775 */             arrayOfObject1[k] = localUniqueTag;
/* 1776 */             arrayOfDouble1[k] = getInt(arrayOfByte, paramCallFrame.pc);
/* 1777 */             paramCallFrame.pc += 4;
/* 1778 */             break;
/*      */           case 40:
/* 1780 */             k++;
/* 1781 */             arrayOfObject1[k] = localUniqueTag;
/* 1782 */             arrayOfDouble1[k] = paramCallFrame.idata.itsDoubleTable[i];
/* 1783 */             break;
/*      */           case 39:
/* 1785 */             arrayOfObject1[(++k)] = ScriptRuntime.name(paramContext, paramCallFrame.scope, str);
/* 1786 */             break;
/*      */           case -8:
/* 1788 */             arrayOfObject1[(++k)] = ScriptRuntime.nameIncrDecr(paramCallFrame.scope, str, paramContext, arrayOfByte[paramCallFrame.pc]);
/*      */ 
/* 1790 */             paramCallFrame.pc += 1;
/* 1791 */             break;
/*      */           case -61:
/* 1793 */             i = arrayOfByte[(paramCallFrame.pc++)];
/*      */           case 156:
/* 1796 */             if (!paramCallFrame.useActivation) {
/* 1797 */               if ((localObject3[i] & 0x1) == 0) {
/* 1798 */                 throw Context.reportRuntimeError1("msg.var.redecl", paramCallFrame.idata.argNames[i]);
/*      */               }
/*      */ 
/* 1801 */               if ((localObject3[i] & 0x8) != 0)
/*      */               {
/* 1804 */                 arrayOfObject2[i] = arrayOfObject1[k];
/* 1805 */                 localObject3[i] &= -9;
/* 1806 */                 arrayOfDouble2[i] = arrayOfDouble1[k];
/*      */               }
/*      */             } else {
/* 1809 */               localObject8 = arrayOfObject1[k];
/* 1810 */               if (localObject8 == localUniqueTag) localObject8 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1811 */               str = paramCallFrame.idata.argNames[i];
/* 1812 */               if ((paramCallFrame.scope instanceof ConstProperties)) {
/* 1813 */                 localObject15 = (ConstProperties)paramCallFrame.scope;
/* 1814 */                 ((ConstProperties)localObject15).putConst(str, paramCallFrame.scope, localObject8);
/*      */               } else {
/* 1816 */                 throw Kit.codeBug();
/*      */               }
/*      */             }
/* 1818 */             break;
/*      */           case -49:
/* 1820 */             i = arrayOfByte[(paramCallFrame.pc++)];
/*      */           case 56:
/* 1823 */             if (!paramCallFrame.useActivation) {
/* 1824 */               if ((localObject3[i] & 0x1) == 0) {
/* 1825 */                 arrayOfObject2[i] = arrayOfObject1[k];
/* 1826 */                 arrayOfDouble2[i] = arrayOfDouble1[k];
/*      */               }
/*      */             } else {
/* 1829 */               localObject8 = arrayOfObject1[k];
/* 1830 */               if (localObject8 == localUniqueTag) localObject8 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1831 */               str = paramCallFrame.idata.argNames[i];
/* 1832 */               paramCallFrame.scope.put(str, paramCallFrame.scope, localObject8);
/*      */             }
/* 1834 */             break;
/*      */           case -48:
/* 1836 */             i = arrayOfByte[(paramCallFrame.pc++)];
/*      */           case 55:
/* 1839 */             k++;
/* 1840 */             if (!paramCallFrame.useActivation) {
/* 1841 */               arrayOfObject1[k] = arrayOfObject2[i];
/* 1842 */               arrayOfDouble1[k] = arrayOfDouble2[i];
/*      */             } else {
/* 1844 */               str = paramCallFrame.idata.argNames[i];
/* 1845 */               arrayOfObject1[k] = paramCallFrame.scope.get(str, paramCallFrame.scope);
/*      */             }
/* 1847 */             break;
/*      */           case -7:
/* 1850 */             k++;
/* 1851 */             int i3 = arrayOfByte[paramCallFrame.pc];
/* 1852 */             if (!paramCallFrame.useActivation) {
/* 1853 */               arrayOfObject1[k] = localUniqueTag;
/* 1854 */               localObject15 = arrayOfObject2[i];
/*      */               double d5;
/* 1856 */               if (localObject15 == localUniqueTag) {
/* 1857 */                 d5 = arrayOfDouble2[i];
/*      */               } else {
/* 1859 */                 d5 = ScriptRuntime.toNumber(localObject15);
/* 1860 */                 arrayOfObject2[i] = localUniqueTag;
/*      */               }
/* 1862 */               double d8 = (i3 & 0x1) == 0 ? d5 + 1.0D : d5 - 1.0D;
/*      */ 
/* 1864 */               arrayOfDouble2[i] = d8;
/* 1865 */               arrayOfDouble1[k] = ((i3 & 0x2) == 0 ? d8 : d5);
/*      */             } else {
/* 1867 */               localObject15 = paramCallFrame.idata.argNames[i];
/* 1868 */               arrayOfObject1[k] = ScriptRuntime.nameIncrDecr(paramCallFrame.scope, (String)localObject15, paramContext, i3);
/*      */             }
/*      */ 
/* 1871 */             paramCallFrame.pc += 1;
/* 1872 */             break;
/*      */           case -51:
/* 1875 */             k++;
/* 1876 */             arrayOfObject1[k] = localUniqueTag;
/* 1877 */             arrayOfDouble1[k] = 0.0D;
/* 1878 */             break;
/*      */           case -52:
/* 1880 */             k++;
/* 1881 */             arrayOfObject1[k] = localUniqueTag;
/* 1882 */             arrayOfDouble1[k] = 1.0D;
/* 1883 */             break;
/*      */           case 42:
/* 1885 */             arrayOfObject1[(++k)] = null;
/* 1886 */             break;
/*      */           case 43:
/* 1888 */             arrayOfObject1[(++k)] = paramCallFrame.thisObj;
/* 1889 */             break;
/*      */           case 63:
/* 1891 */             arrayOfObject1[(++k)] = paramCallFrame.fnOrScript;
/* 1892 */             break;
/*      */           case 44:
/* 1894 */             arrayOfObject1[(++k)] = Boolean.FALSE;
/* 1895 */             break;
/*      */           case 45:
/* 1897 */             arrayOfObject1[(++k)] = Boolean.TRUE;
/* 1898 */             break;
/*      */           case -50:
/* 1900 */             arrayOfObject1[(++k)] = localObject1;
/* 1901 */             break;
/*      */           case 2:
/* 1903 */             Object localObject9 = arrayOfObject1[k];
/* 1904 */             if (localObject9 == localUniqueTag) localObject9 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1905 */             k--;
/* 1906 */             paramCallFrame.scope = ScriptRuntime.enterWith(localObject9, paramContext, paramCallFrame.scope);
/* 1907 */             break;
/*      */           case 3:
/* 1910 */             paramCallFrame.scope = ScriptRuntime.leaveWith(paramCallFrame.scope);
/* 1911 */             break;
/*      */           case 57:
/* 1916 */             k--;
/* 1917 */             i += paramCallFrame.localShift;
/*      */ 
/* 1919 */             int i4 = paramCallFrame.idata.itsICode[paramCallFrame.pc] != 0 ? 1 : 0;
/* 1920 */             localObject15 = (Throwable)arrayOfObject1[(k + 1)];
/*      */ 
/* 1922 */             if (i4 == 0)
/* 1923 */               localObject21 = null;
/*      */             else {
/* 1925 */               localObject21 = (Scriptable)arrayOfObject1[i];
/*      */             }
/* 1927 */             arrayOfObject1[i] = ScriptRuntime.newCatchScope((Throwable)localObject15, (Scriptable)localObject21, str, paramContext, paramCallFrame.scope);
/*      */ 
/* 1930 */             paramCallFrame.pc += 1;
/* 1931 */             break;
/*      */           case 58:
/*      */           case 59:
/*      */           case 60:
/* 1936 */             localObject10 = arrayOfObject1[k];
/* 1937 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1938 */             k--;
/* 1939 */             i += paramCallFrame.localShift;
/* 1940 */             int i9 = m == 59 ? 1 : m == 58 ? 0 : 2;
/*      */ 
/* 1945 */             arrayOfObject1[i] = ScriptRuntime.enumInit(localObject10, paramContext, i9);
/* 1946 */             break;
/*      */           case 61:
/*      */           case 62:
/* 1950 */             i += paramCallFrame.localShift;
/* 1951 */             localObject10 = arrayOfObject1[i];
/* 1952 */             k++;
/* 1953 */             arrayOfObject1[k] = (m == 61 ? ScriptRuntime.enumNext(localObject10) : ScriptRuntime.enumId(localObject10, paramContext));
/*      */ 
/* 1956 */             break;
/*      */           case 71:
/* 1960 */             localObject10 = arrayOfObject1[k];
/* 1961 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1962 */             arrayOfObject1[k] = ScriptRuntime.specialRef(localObject10, str, paramContext);
/* 1963 */             break;
/*      */           case 77:
/* 1967 */             localObject10 = arrayOfObject1[k];
/* 1968 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1969 */             k--;
/* 1970 */             localObject16 = arrayOfObject1[k];
/* 1971 */             if (localObject16 == localUniqueTag) localObject16 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1972 */             arrayOfObject1[k] = ScriptRuntime.memberRef(localObject16, localObject10, paramContext, i);
/* 1973 */             break;
/*      */           case 78:
/* 1977 */             localObject10 = arrayOfObject1[k];
/* 1978 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1979 */             k--;
/* 1980 */             localObject16 = arrayOfObject1[k];
/* 1981 */             if (localObject16 == localUniqueTag) localObject16 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1982 */             k--;
/* 1983 */             localObject21 = arrayOfObject1[k];
/* 1984 */             if (localObject21 == localUniqueTag) localObject21 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1985 */             arrayOfObject1[k] = ScriptRuntime.memberRef(localObject21, localObject16, localObject10, paramContext, i);
/* 1986 */             break;
/*      */           case 79:
/* 1990 */             localObject10 = arrayOfObject1[k];
/* 1991 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 1992 */             arrayOfObject1[k] = ScriptRuntime.nameRef(localObject10, paramContext, paramCallFrame.scope, i);
/*      */ 
/* 1994 */             break;
/*      */           case 80:
/* 1998 */             localObject10 = arrayOfObject1[k];
/* 1999 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 2000 */             k--;
/* 2001 */             localObject16 = arrayOfObject1[k];
/* 2002 */             if (localObject16 == localUniqueTag) localObject16 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 2003 */             arrayOfObject1[k] = ScriptRuntime.nameRef(localObject16, localObject10, paramContext, paramCallFrame.scope, i);
/*      */ 
/* 2005 */             break;
/*      */           case -12:
/* 2008 */             i += paramCallFrame.localShift;
/* 2009 */             paramCallFrame.scope = ((Scriptable)arrayOfObject1[i]);
/* 2010 */             break;
/*      */           case -13:
/* 2012 */             i += paramCallFrame.localShift;
/* 2013 */             arrayOfObject1[i] = paramCallFrame.scope;
/* 2014 */             break;
/*      */           case -19:
/* 2016 */             arrayOfObject1[(++k)] = InterpretedFunction.createFunction(paramContext, paramCallFrame.scope, paramCallFrame.fnOrScript, i);
/*      */ 
/* 2019 */             break;
/*      */           case -20:
/* 2021 */             initFunction(paramContext, paramCallFrame.scope, paramCallFrame.fnOrScript, i);
/* 2022 */             break;
/*      */           case 48:
/* 2024 */             arrayOfObject1[(++k)] = paramCallFrame.scriptRegExps[i];
/* 2025 */             break;
/*      */           case -29:
/* 2028 */             k++;
/* 2029 */             arrayOfObject1[k] = new int[i];
/* 2030 */             k++;
/* 2031 */             arrayOfObject1[k] = new Object[i];
/* 2032 */             arrayOfDouble1[k] = 0.0D;
/* 2033 */             break;
/*      */           case -30:
/* 2035 */             localObject10 = arrayOfObject1[k];
/* 2036 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 2037 */             k--;
/* 2038 */             i10 = (int)arrayOfDouble1[k];
/* 2039 */             ((Object[])arrayOfObject1[k])[i10] = localObject10;
/* 2040 */             arrayOfDouble1[k] = (i10 + 1);
/* 2041 */             break;
/*      */           case -57:
/* 2044 */             localObject10 = arrayOfObject1[k];
/* 2045 */             k--;
/* 2046 */             i10 = (int)arrayOfDouble1[k];
/* 2047 */             ((Object[])arrayOfObject1[k])[i10] = localObject10;
/* 2048 */             ((int[])arrayOfObject1[(k - 1)])[i10] = -1;
/* 2049 */             arrayOfDouble1[k] = (i10 + 1);
/* 2050 */             break;
/*      */           case -58:
/* 2053 */             localObject10 = arrayOfObject1[k];
/* 2054 */             k--;
/* 2055 */             i10 = (int)arrayOfDouble1[k];
/* 2056 */             ((Object[])arrayOfObject1[k])[i10] = localObject10;
/* 2057 */             ((int[])arrayOfObject1[(k - 1)])[i10] = 1;
/* 2058 */             arrayOfDouble1[k] = (i10 + 1);
/* 2059 */             break;
/*      */           case -31:
/*      */           case 65:
/*      */           case 66:
/* 2064 */             localObject10 = (Object[])arrayOfObject1[k];
/* 2065 */             k--;
/* 2066 */             localObject17 = (int[])arrayOfObject1[k];
/*      */ 
/* 2068 */             if (m == 66) {
/* 2069 */               localObject22 = (Object[])paramCallFrame.idata.literalIds[i];
/* 2070 */               localObject21 = ScriptRuntime.newObjectLiteral((Object[])localObject22, (Object[])localObject10, (int[])localObject17, paramContext, paramCallFrame.scope);
/*      */             }
/*      */             else {
/* 2073 */               localObject22 = null;
/* 2074 */               if (m == -31) {
/* 2075 */                 localObject22 = (int[])paramCallFrame.idata.literalIds[i];
/*      */               }
/* 2077 */               localObject21 = ScriptRuntime.newArrayLiteral((Object[])localObject10, (int[])localObject22, paramContext, paramCallFrame.scope);
/*      */             }
/*      */ 
/* 2080 */             arrayOfObject1[k] = localObject21;
/* 2081 */             break;
/*      */           case -53:
/* 2084 */             localObject10 = arrayOfObject1[k];
/* 2085 */             if (localObject10 == localUniqueTag) localObject10 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 2086 */             k--;
/* 2087 */             paramCallFrame.scope = ScriptRuntime.enterDotQuery(localObject10, paramCallFrame.scope);
/* 2088 */             break;
/*      */           case -54:
/* 2091 */             boolean bool4 = stack_boolean(paramCallFrame, k);
/* 2092 */             localObject17 = ScriptRuntime.updateDotQuery(bool4, paramCallFrame.scope);
/* 2093 */             if (localObject17 != null) {
/* 2094 */               arrayOfObject1[k] = localObject17;
/* 2095 */               paramCallFrame.scope = ScriptRuntime.leaveDotQuery(paramCallFrame.scope);
/* 2096 */               paramCallFrame.pc += 2;
/*      */             }
/*      */             else
/*      */             {
/* 2100 */               k--;
/* 2101 */             }break;
/*      */           case 74:
/* 2104 */             localObject11 = arrayOfObject1[k];
/* 2105 */             if (localObject11 == localUniqueTag) localObject11 = ScriptRuntime.wrapNumber(arrayOfDouble1[k]);
/* 2106 */             arrayOfObject1[k] = ScriptRuntime.setDefaultNamespace(localObject11, paramContext);
/* 2107 */             break;
/*      */           case 75:
/* 2110 */             localObject11 = arrayOfObject1[k];
/* 2111 */             if (localObject11 != localUniqueTag)
/* 2112 */               arrayOfObject1[k] = ScriptRuntime.escapeAttributeValue(localObject11, paramContext); break;
/*      */           case 76:
/* 2117 */             localObject11 = arrayOfObject1[k];
/* 2118 */             if (localObject11 != localUniqueTag)
/* 2119 */               arrayOfObject1[k] = ScriptRuntime.escapeTextValue(localObject11, paramContext); break;
/*      */           case -64:
/* 2124 */             if (paramCallFrame.debuggerFrame != null)
/* 2125 */               paramCallFrame.debuggerFrame.onDebuggerStatement(paramContext); break;
/*      */           case -26:
/* 2129 */             paramCallFrame.pcSourceLineStart = paramCallFrame.pc;
/* 2130 */             if (paramCallFrame.debuggerFrame != null) {
/* 2131 */               i5 = getIndex(arrayOfByte, paramCallFrame.pc);
/* 2132 */               paramCallFrame.debuggerFrame.onLineChange(paramContext, i5);
/*      */             }
/* 2134 */             paramCallFrame.pc += 2;
/* 2135 */             break;
/*      */           case -32:
/* 2137 */             i = 0;
/* 2138 */             break;
/*      */           case -33:
/* 2140 */             i = 1;
/* 2141 */             break;
/*      */           case -34:
/* 2143 */             i = 2;
/* 2144 */             break;
/*      */           case -35:
/* 2146 */             i = 3;
/* 2147 */             break;
/*      */           case -36:
/* 2149 */             i = 4;
/* 2150 */             break;
/*      */           case -37:
/* 2152 */             i = 5;
/* 2153 */             break;
/*      */           case -38:
/* 2155 */             i = 0xFF & arrayOfByte[paramCallFrame.pc];
/* 2156 */             paramCallFrame.pc += 1;
/* 2157 */             break;
/*      */           case -39:
/* 2159 */             i = getIndex(arrayOfByte, paramCallFrame.pc);
/* 2160 */             paramCallFrame.pc += 2;
/* 2161 */             break;
/*      */           case -40:
/* 2163 */             i = getInt(arrayOfByte, paramCallFrame.pc);
/* 2164 */             paramCallFrame.pc += 4;
/* 2165 */             break;
/*      */           case -41:
/* 2167 */             str = arrayOfString[0];
/* 2168 */             break;
/*      */           case -42:
/* 2170 */             str = arrayOfString[1];
/* 2171 */             break;
/*      */           case -43:
/* 2173 */             str = arrayOfString[2];
/* 2174 */             break;
/*      */           case -44:
/* 2176 */             str = arrayOfString[3];
/* 2177 */             break;
/*      */           case -45:
/* 2179 */             str = arrayOfString[(0xFF & arrayOfByte[paramCallFrame.pc])];
/* 2180 */             paramCallFrame.pc += 1;
/* 2181 */             break;
/*      */           case -46:
/* 2183 */             str = arrayOfString[getIndex(arrayOfByte, paramCallFrame.pc)];
/* 2184 */             paramCallFrame.pc += 2;
/* 2185 */             break;
/*      */           case -47:
/* 2187 */             str = arrayOfString[getInt(arrayOfByte, paramCallFrame.pc)];
/* 2188 */             paramCallFrame.pc += 4;
/* 2189 */             break;
/*      */           case -60:
/*      */           case 0:
/*      */           case 1:
/*      */           case 81:
/*      */           case 82:
/*      */           case 83:
/*      */           case 84:
/*      */           case 85:
/*      */           case 86:
/*      */           case 87:
/*      */           case 88:
/*      */           case 89:
/*      */           case 90:
/*      */           case 91:
/*      */           case 92:
/*      */           case 93:
/*      */           case 94:
/*      */           case 95:
/*      */           case 96:
/*      */           case 97:
/*      */           case 98:
/*      */           case 99:
/*      */           case 100:
/*      */           case 101:
/*      */           case 102:
/*      */           case 103:
/*      */           case 104:
/*      */           case 105:
/*      */           case 106:
/*      */           case 107:
/*      */           case 108:
/*      */           case 109:
/*      */           case 110:
/*      */           case 111:
/*      */           case 112:
/*      */           case 113:
/*      */           case 114:
/*      */           case 115:
/*      */           case 116:
/*      */           case 117:
/*      */           case 118:
/*      */           case 119:
/*      */           case 120:
/*      */           case 121:
/*      */           case 122:
/*      */           case 123:
/*      */           case 124:
/*      */           case 125:
/*      */           case 126:
/*      */           case 127:
/*      */           case 128:
/*      */           case 129:
/*      */           case 130:
/*      */           case 131:
/*      */           case 132:
/*      */           case 133:
/*      */           case 134:
/*      */           case 135:
/*      */           case 136:
/*      */           case 137:
/*      */           case 138:
/*      */           case 139:
/*      */           case 140:
/*      */           case 141:
/*      */           case 142:
/*      */           case 143:
/*      */           case 144:
/*      */           case 145:
/*      */           case 146:
/*      */           case 147:
/*      */           case 148:
/*      */           case 149:
/*      */           case 150:
/*      */           case 151:
/*      */           case 152:
/*      */           case 153:
/*      */           case 154:
/*      */           case 155:
/*      */           default:
/* 2191 */             dumpICode(paramCallFrame.idata);
/* 2192 */             throw new RuntimeException("Unknown icode : " + m + " @ pc : " + (paramCallFrame.pc - 1));
/*      */ 
/* 2200 */             if (bool1) {
/* 2201 */               addInstructionCount(paramContext, paramCallFrame, 2);
/*      */             }
/* 2203 */             i5 = getShort(arrayOfByte, paramCallFrame.pc);
/* 2204 */             if (i5 != 0)
/*      */             {
/* 2206 */               paramCallFrame.pc += i5 - 1;
/*      */             }
/* 2208 */             else paramCallFrame.pc = paramCallFrame.idata.longJumps.getExistingInt(paramCallFrame.pc);
/*      */ 
/* 2211 */             if (bool1) {
/* 2212 */               paramCallFrame.pcPrevBranch = paramCallFrame.pc;
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/* 2218 */         exitFrame(paramContext, paramCallFrame, null);
/* 2219 */         localObject2 = paramCallFrame.result;
/* 2220 */         d1 = paramCallFrame.resultDbl;
/* 2221 */         if (paramCallFrame.parentFrame == null) break;
/* 2222 */         paramCallFrame = paramCallFrame.parentFrame;
/* 2223 */         if (paramCallFrame.frozen) {
/* 2224 */           paramCallFrame = paramCallFrame.cloneFrozen();
/*      */         }
/* 2226 */         setCallResult(paramCallFrame, localObject2, d1);
/*      */ 
/* 2228 */         localObject2 = null;
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/*      */       Object localObject3;
/*      */       while (true) {
/* 2235 */         if (paramObject != null)
/*      */         {
/* 2237 */           localThrowable1.printStackTrace(System.err);
/* 2238 */           throw new IllegalStateException();
/*      */         }
/* 2240 */         paramObject = localThrowable1;
/*      */ 
/* 2246 */         label7582: if (paramObject == null) Kit.codeBug();
/*      */ 
/* 2254 */         localObject3 = null;
/*      */         int j;
/* 2256 */         if ((localGeneratorState != null) && (localGeneratorState.operation == 2) && (paramObject == localGeneratorState.value))
/*      */         {
/* 2260 */           j = 1;
/* 2261 */         } else if ((paramObject instanceof JavaScriptException)) {
/* 2262 */           j = 2;
/* 2263 */         } else if ((paramObject instanceof EcmaError))
/*      */         {
/* 2265 */           j = 2;
/* 2266 */         } else if ((paramObject instanceof EvaluatorException)) {
/* 2267 */           j = 2;
/* 2268 */         } else if ((paramObject instanceof RuntimeException)) {
/* 2269 */           j = paramContext.hasFeature(13) ? 2 : 1;
/*      */         }
/* 2272 */         else if ((paramObject instanceof Error)) {
/* 2273 */           j = paramContext.hasFeature(13) ? 2 : 0;
/*      */         }
/* 2276 */         else if ((paramObject instanceof ContinuationJump))
/*      */         {
/* 2278 */           j = 1;
/* 2279 */           localObject3 = (ContinuationJump)paramObject;
/*      */         } else {
/* 2281 */           j = paramContext.hasFeature(13) ? 2 : 1;
/*      */         }
/*      */ 
/* 2286 */         if (bool1) {
/*      */           try {
/* 2288 */             addInstructionCount(paramContext, paramCallFrame, 100);
/*      */           } catch (RuntimeException localRuntimeException1) {
/* 2290 */             paramObject = localRuntimeException1;
/* 2291 */             j = 1;
/*      */           }
/*      */           catch (Error localError)
/*      */           {
/* 2295 */             paramObject = localError;
/* 2296 */             localObject3 = null;
/* 2297 */             j = 0;
/*      */           }
/*      */         }
/* 2300 */         if ((paramCallFrame.debuggerFrame != null) && ((paramObject instanceof RuntimeException)))
/*      */         {
/* 2304 */           RuntimeException localRuntimeException2 = (RuntimeException)paramObject;
/*      */           try {
/* 2306 */             paramCallFrame.debuggerFrame.onExceptionThrown(paramContext, localRuntimeException2);
/*      */           }
/*      */           catch (Throwable localThrowable2)
/*      */           {
/* 2310 */             paramObject = localThrowable2;
/* 2311 */             localObject3 = null;
/* 2312 */             j = 0;
/*      */           }
/*      */         }
/*      */         do
/*      */         {
/* 2317 */           if (j != 0) {
/* 2318 */             boolean bool2 = j != 2;
/* 2319 */             i = getExceptionHandler(paramCallFrame, bool2);
/* 2320 */             if (i >= 0)
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2330 */           exitFrame(paramContext, paramCallFrame, paramObject);
/*      */ 
/* 2332 */           paramCallFrame = paramCallFrame.parentFrame;
/* 2333 */           if (paramCallFrame == null) break label7903; 
/* 2334 */         }while ((localObject3 == null) || (((ContinuationJump)localObject3).branchFrame != paramCallFrame));
/*      */ 
/* 2337 */         i = -1;
/* 2338 */         continue;
/*      */ 
/* 2343 */         label7903: if (localObject3 == null) break label7953;
/* 2344 */         if (((ContinuationJump)localObject3).branchFrame != null)
/*      */         {
/* 2346 */           Kit.codeBug();
/*      */         }
/* 2348 */         if (((ContinuationJump)localObject3).capturedFrame == null)
/*      */           break;
/* 2350 */         i = -1;
/*      */       }
/*      */ 
/* 2354 */       localObject2 = ((ContinuationJump)localObject3).result;
/* 2355 */       d1 = ((ContinuationJump)localObject3).resultDbl;
/* 2356 */       paramObject = null;
/*      */     }
/*      */ 
/* 2364 */     label7953: if ((paramContext.previousInterpreterInvocations != null) && (paramContext.previousInterpreterInvocations.size() != 0))
/*      */     {
/* 2367 */       paramContext.lastInterpreterFrame = paramContext.previousInterpreterInvocations.pop();
/*      */     }
/*      */     else
/*      */     {
/* 2371 */       paramContext.lastInterpreterFrame = null;
/*      */ 
/* 2373 */       paramContext.previousInterpreterInvocations = null;
/*      */     }
/*      */ 
/* 2376 */     if (paramObject != null) {
/* 2377 */       if ((paramObject instanceof RuntimeException)) {
/* 2378 */         throw ((RuntimeException)paramObject);
/*      */       }
/*      */ 
/* 2381 */       throw ((Error)paramObject);
/*      */     }
/*      */ 
/* 2385 */     return localObject2 != localUniqueTag ? localObject2 : ScriptRuntime.wrapNumber(d1);
/*      */   }
/*      */ 
/*      */   private static CallFrame initFrameForNoSuchMethod(Context paramContext, CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt2, int paramInt3, Scriptable paramScriptable1, Scriptable paramScriptable2, ScriptRuntime.NoSuchMethodShim paramNoSuchMethodShim, InterpretedFunction paramInterpretedFunction)
/*      */   {
/* 2399 */     Object[] arrayOfObject1 = null;
/*      */ 
/* 2402 */     int i = paramInt2 + 2;
/* 2403 */     Object[] arrayOfObject2 = new Object[paramInt1];
/* 2404 */     for (int j = 0; j < paramInt1; i++) {
/* 2405 */       localObject = paramArrayOfObject[i];
/* 2406 */       if (localObject == UniqueTag.DOUBLE_MARK) {
/* 2407 */         localObject = ScriptRuntime.wrapNumber(paramArrayOfDouble[i]);
/*      */       }
/* 2409 */       arrayOfObject2[j] = localObject;
/*      */ 
/* 2404 */       j++;
/*      */     }
/*      */ 
/* 2411 */     arrayOfObject1 = new Object[2];
/* 2412 */     arrayOfObject1[0] = paramNoSuchMethodShim.methodName;
/* 2413 */     arrayOfObject1[1] = paramContext.newArray(paramScriptable2, arrayOfObject2);
/*      */ 
/* 2416 */     CallFrame localCallFrame = paramCallFrame;
/* 2417 */     Object localObject = new CallFrame(null);
/* 2418 */     if (paramInt3 == -55) {
/* 2419 */       localCallFrame = paramCallFrame.parentFrame;
/* 2420 */       exitFrame(paramContext, paramCallFrame, null);
/*      */     }
/*      */ 
/* 2424 */     initFrame(paramContext, paramScriptable2, paramScriptable1, arrayOfObject1, null, 0, 2, paramInterpretedFunction, localCallFrame, (CallFrame)localObject);
/*      */ 
/* 2426 */     if (paramInt3 != -55) {
/* 2427 */       paramCallFrame.savedStackTop = paramInt2;
/* 2428 */       paramCallFrame.savedCallOp = paramInt3;
/*      */     }
/* 2430 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static boolean shallowEquals(Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt)
/*      */   {
/* 2436 */     Object localObject1 = paramArrayOfObject[(paramInt + 1)];
/* 2437 */     Object localObject2 = paramArrayOfObject[paramInt];
/* 2438 */     UniqueTag localUniqueTag = UniqueTag.DOUBLE_MARK;
/*      */     double d1;
/*      */     double d2;
/* 2440 */     if (localObject1 == localUniqueTag) {
/* 2441 */       d1 = paramArrayOfDouble[(paramInt + 1)];
/* 2442 */       if (localObject2 == localUniqueTag)
/* 2443 */         d2 = paramArrayOfDouble[paramInt];
/* 2444 */       else if ((localObject2 instanceof Number))
/* 2445 */         d2 = ((Number)localObject2).doubleValue();
/*      */       else
/* 2447 */         return false;
/*      */     }
/* 2449 */     else if (localObject2 == localUniqueTag) {
/* 2450 */       d2 = paramArrayOfDouble[paramInt];
/* 2451 */       if (localObject1 == localUniqueTag)
/* 2452 */         d1 = paramArrayOfDouble[(paramInt + 1)];
/* 2453 */       else if ((localObject1 instanceof Number))
/* 2454 */         d1 = ((Number)localObject1).doubleValue();
/*      */       else
/* 2456 */         return false;
/*      */     }
/*      */     else {
/* 2459 */       return ScriptRuntime.shallowEq(localObject2, localObject1);
/*      */     }
/* 2461 */     return d2 == d1;
/*      */   }
/*      */ 
/*      */   private static CallFrame processThrowable(Context paramContext, Object paramObject, CallFrame paramCallFrame, int paramInt, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/*      */     int i;
/*      */     int j;
/* 2471 */     if (paramInt >= 0)
/*      */     {
/* 2475 */       if (paramCallFrame.frozen)
/*      */       {
/* 2477 */         paramCallFrame = paramCallFrame.cloneFrozen();
/*      */       }
/*      */ 
/* 2480 */       localObject = paramCallFrame.idata.itsExceptionTable;
/*      */ 
/* 2482 */       paramCallFrame.pc = localObject[(paramInt + 2)];
/* 2483 */       if (paramBoolean) {
/* 2484 */         paramCallFrame.pcPrevBranch = paramCallFrame.pc;
/*      */       }
/*      */ 
/* 2487 */       paramCallFrame.savedStackTop = paramCallFrame.emptyStackTop;
/* 2488 */       i = paramCallFrame.localShift + localObject[(paramInt + 5)];
/*      */ 
/* 2491 */       j = paramCallFrame.localShift + localObject[(paramInt + 4)];
/*      */ 
/* 2494 */       paramCallFrame.scope = ((Scriptable)paramCallFrame.stack[i]);
/* 2495 */       paramCallFrame.stack[j] = paramObject;
/*      */ 
/* 2497 */       paramObject = null;
/*      */     }
/*      */     else {
/* 2500 */       localObject = (ContinuationJump)paramObject;
/*      */ 
/* 2503 */       paramObject = null;
/*      */ 
/* 2505 */       if (((ContinuationJump)localObject).branchFrame != paramCallFrame) Kit.codeBug();
/*      */ 
/* 2510 */       if (((ContinuationJump)localObject).capturedFrame == null) Kit.codeBug();
/*      */ 
/* 2514 */       i = ((ContinuationJump)localObject).capturedFrame.frameIndex + 1;
/* 2515 */       if (((ContinuationJump)localObject).branchFrame != null) {
/* 2516 */         i -= ((ContinuationJump)localObject).branchFrame.frameIndex;
/*      */       }
/*      */ 
/* 2519 */       j = 0;
/* 2520 */       CallFrame[] arrayOfCallFrame = null;
/*      */ 
/* 2522 */       CallFrame localCallFrame = ((ContinuationJump)localObject).capturedFrame;
/* 2523 */       for (int k = 0; k != i; k++) {
/* 2524 */         if (!localCallFrame.frozen) Kit.codeBug();
/* 2525 */         if (isFrameEnterExitRequired(localCallFrame)) {
/* 2526 */           if (arrayOfCallFrame == null)
/*      */           {
/* 2530 */             arrayOfCallFrame = new CallFrame[i - k];
/*      */           }
/*      */ 
/* 2533 */           arrayOfCallFrame[j] = localCallFrame;
/* 2534 */           j++;
/*      */         }
/* 2536 */         localCallFrame = localCallFrame.parentFrame;
/*      */       }
/*      */ 
/* 2539 */       while (j != 0)
/*      */       {
/* 2543 */         j--;
/* 2544 */         localCallFrame = arrayOfCallFrame[j];
/* 2545 */         enterFrame(paramContext, localCallFrame, ScriptRuntime.emptyArgs, true);
/*      */       }
/*      */ 
/* 2552 */       paramCallFrame = ((ContinuationJump)localObject).capturedFrame.cloneFrozen();
/* 2553 */       setCallResult(paramCallFrame, ((ContinuationJump)localObject).result, ((ContinuationJump)localObject).resultDbl);
/*      */     }
/*      */ 
/* 2556 */     paramCallFrame.throwable = paramObject;
/* 2557 */     return paramCallFrame;
/*      */   }
/*      */ 
/*      */   private static Object freezeGenerator(Context paramContext, CallFrame paramCallFrame, int paramInt, GeneratorState paramGeneratorState)
/*      */   {
/* 2564 */     if (paramGeneratorState.operation == 2)
/*      */     {
/* 2566 */       throw ScriptRuntime.typeError0("msg.yield.closing");
/*      */     }
/*      */ 
/* 2569 */     paramCallFrame.frozen = true;
/* 2570 */     paramCallFrame.result = paramCallFrame.stack[paramInt];
/* 2571 */     paramCallFrame.resultDbl = paramCallFrame.sDbl[paramInt];
/* 2572 */     paramCallFrame.savedStackTop = paramInt;
/* 2573 */     paramCallFrame.pc -= 1;
/* 2574 */     ScriptRuntime.exitActivationFunction(paramContext);
/* 2575 */     return paramCallFrame.result != UniqueTag.DOUBLE_MARK ? paramCallFrame.result : ScriptRuntime.wrapNumber(paramCallFrame.resultDbl);
/*      */   }
/*      */ 
/*      */   private static Object thawGenerator(CallFrame paramCallFrame, int paramInt1, GeneratorState paramGeneratorState, int paramInt2)
/*      */   {
/* 2584 */     paramCallFrame.frozen = false;
/* 2585 */     int i = getIndex(paramCallFrame.idata.itsICode, paramCallFrame.pc);
/* 2586 */     paramCallFrame.pc += 2;
/* 2587 */     if (paramGeneratorState.operation == 1)
/*      */     {
/* 2590 */       return new JavaScriptException(paramGeneratorState.value, paramCallFrame.idata.itsSourceFile, i);
/*      */     }
/*      */ 
/* 2594 */     if (paramGeneratorState.operation == 2) {
/* 2595 */       return paramGeneratorState.value;
/*      */     }
/* 2597 */     if (paramGeneratorState.operation != 0)
/* 2598 */       throw Kit.codeBug();
/* 2599 */     if (paramInt2 == 72)
/* 2600 */       paramCallFrame.stack[paramInt1] = paramGeneratorState.value;
/* 2601 */     return Scriptable.NOT_FOUND;
/*      */   }
/*      */ 
/*      */   private static CallFrame initFrameForApplyOrCall(Context paramContext, CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt2, int paramInt3, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, InterpretedFunction paramInterpretedFunction)
/*      */   {
/*      */     Scriptable localScriptable;
/* 2610 */     if (paramInt1 != 0) {
/* 2611 */       localObject = paramArrayOfObject[(paramInt2 + 2)];
/* 2612 */       if (localObject == UniqueTag.DOUBLE_MARK)
/* 2613 */         localObject = ScriptRuntime.wrapNumber(paramArrayOfDouble[(paramInt2 + 2)]);
/* 2614 */       localScriptable = ScriptRuntime.toObjectOrNull(paramContext, localObject);
/*      */     }
/*      */     else {
/* 2617 */       localScriptable = null;
/*      */     }
/* 2619 */     if (localScriptable == null)
/*      */     {
/* 2621 */       localScriptable = ScriptRuntime.getTopCallScope(paramContext);
/*      */     }
/* 2623 */     if (paramInt3 == -55) {
/* 2624 */       exitFrame(paramContext, paramCallFrame, null);
/* 2625 */       paramCallFrame = paramCallFrame.parentFrame;
/*      */     }
/*      */     else {
/* 2628 */       paramCallFrame.savedStackTop = paramInt2;
/* 2629 */       paramCallFrame.savedCallOp = paramInt3;
/*      */     }
/* 2631 */     Object localObject = new CallFrame(null);
/* 2632 */     if (BaseFunction.isApply(paramIdFunctionObject)) {
/* 2633 */       Object[] arrayOfObject = paramInt1 < 2 ? ScriptRuntime.emptyArgs : ScriptRuntime.getApplyArguments(paramContext, paramArrayOfObject[(paramInt2 + 3)]);
/*      */ 
/* 2635 */       initFrame(paramContext, paramScriptable, localScriptable, arrayOfObject, null, 0, arrayOfObject.length, paramInterpretedFunction, paramCallFrame, (CallFrame)localObject);
/*      */     }
/*      */     else
/*      */     {
/* 2640 */       for (int i = 1; i < paramInt1; i++) {
/* 2641 */         paramArrayOfObject[(paramInt2 + 1 + i)] = paramArrayOfObject[(paramInt2 + 2 + i)];
/* 2642 */         paramArrayOfDouble[(paramInt2 + 1 + i)] = paramArrayOfDouble[(paramInt2 + 2 + i)];
/*      */       }
/* 2644 */       i = paramInt1 < 2 ? 0 : paramInt1 - 1;
/* 2645 */       initFrame(paramContext, paramScriptable, localScriptable, paramArrayOfObject, paramArrayOfDouble, paramInt2 + 2, i, paramInterpretedFunction, paramCallFrame, (CallFrame)localObject);
/*      */     }
/*      */ 
/* 2649 */     paramCallFrame = (CallFrame)localObject;
/* 2650 */     return paramCallFrame;
/*      */   }
/*      */ 
/*      */   private static void initFrame(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt1, int paramInt2, InterpretedFunction paramInterpretedFunction, CallFrame paramCallFrame1, CallFrame paramCallFrame2)
/*      */   {
/* 2660 */     InterpreterData localInterpreterData1 = paramInterpretedFunction.idata;
/*      */ 
/* 2662 */     boolean bool = localInterpreterData1.itsNeedsActivation;
/* 2663 */     DebugFrame localDebugFrame = null;
/* 2664 */     if (paramContext.debugger != null) {
/* 2665 */       localDebugFrame = paramContext.debugger.getFrame(paramContext, localInterpreterData1);
/* 2666 */       if (localDebugFrame != null) {
/* 2667 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 2671 */     if (bool)
/*      */     {
/* 2674 */       if (paramArrayOfDouble != null) {
/* 2675 */         paramArrayOfObject = getArgsArray(paramArrayOfObject, paramArrayOfDouble, paramInt1, paramInt2);
/*      */       }
/* 2677 */       paramInt1 = 0;
/* 2678 */       paramArrayOfDouble = null;
/*      */     }
/*      */     Scriptable localScriptable;
/* 2682 */     if (localInterpreterData1.itsFunctionType != 0) {
/* 2683 */       if (!localInterpreterData1.useDynamicScope)
/* 2684 */         localScriptable = paramInterpretedFunction.getParentScope();
/*      */       else {
/* 2686 */         localScriptable = paramScriptable1;
/*      */       }
/*      */ 
/* 2689 */       if (bool)
/* 2690 */         localScriptable = ScriptRuntime.createFunctionActivation(paramInterpretedFunction, localScriptable, paramArrayOfObject);
/*      */     }
/*      */     else
/*      */     {
/* 2694 */       localScriptable = paramScriptable1;
/* 2695 */       ScriptRuntime.initScript(paramInterpretedFunction, paramScriptable2, paramContext, localScriptable, paramInterpretedFunction.idata.evalScriptFlag);
/*      */     }
/*      */ 
/* 2699 */     if (localInterpreterData1.itsNestedFunctions != null) {
/* 2700 */       if ((localInterpreterData1.itsFunctionType != 0) && (!localInterpreterData1.itsNeedsActivation))
/* 2701 */         Kit.codeBug();
/* 2702 */       for (int i = 0; i < localInterpreterData1.itsNestedFunctions.length; i++) {
/* 2703 */         InterpreterData localInterpreterData2 = localInterpreterData1.itsNestedFunctions[i];
/* 2704 */         if (localInterpreterData2.itsFunctionType == 1) {
/* 2705 */           initFunction(paramContext, localScriptable, paramInterpretedFunction, i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2710 */     Scriptable[] arrayOfScriptable = null;
/* 2711 */     if (localInterpreterData1.itsRegExpLiterals != null)
/*      */     {
/* 2716 */       if (localInterpreterData1.itsFunctionType != 0)
/* 2717 */         arrayOfScriptable = paramInterpretedFunction.functionRegExps;
/*      */       else {
/* 2719 */         arrayOfScriptable = paramInterpretedFunction.createRegExpWraps(paramContext, localScriptable);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2725 */     int j = localInterpreterData1.itsMaxVars + localInterpreterData1.itsMaxLocals - 1;
/* 2726 */     int k = localInterpreterData1.itsMaxFrameArray;
/* 2727 */     if (k != j + localInterpreterData1.itsMaxStack + 1)
/* 2728 */       Kit.codeBug();
/*      */     int m;
/*      */     Object[] arrayOfObject;
/*      */     int[] arrayOfInt;
/*      */     double[] arrayOfDouble;
/* 2734 */     if ((paramCallFrame2.stack != null) && (k <= paramCallFrame2.stack.length))
/*      */     {
/* 2736 */       m = 1;
/* 2737 */       arrayOfObject = paramCallFrame2.stack;
/* 2738 */       arrayOfInt = paramCallFrame2.stackAttributes;
/* 2739 */       arrayOfDouble = paramCallFrame2.sDbl;
/*      */     } else {
/* 2741 */       m = 0;
/* 2742 */       arrayOfObject = new Object[k];
/* 2743 */       arrayOfInt = new int[k];
/* 2744 */       arrayOfDouble = new double[k];
/*      */     }
/*      */ 
/* 2747 */     int n = localInterpreterData1.getParamAndVarCount();
/* 2748 */     for (int i1 = 0; i1 < n; i1++) {
/* 2749 */       if (localInterpreterData1.getParamOrVarConst(i1))
/* 2750 */         arrayOfInt[i1] = 13;
/*      */     }
/* 2752 */     i1 = localInterpreterData1.argCount;
/* 2753 */     if (i1 > paramInt2) i1 = paramInt2;
/*      */ 
/* 2757 */     paramCallFrame2.parentFrame = paramCallFrame1;
/* 2758 */     paramCallFrame2.frameIndex = (paramCallFrame1 == null ? 0 : paramCallFrame1.frameIndex + 1);
/*      */ 
/* 2760 */     if (paramCallFrame2.frameIndex > paramContext.getMaximumInterpreterStackDepth())
/*      */     {
/* 2762 */       throw Context.reportRuntimeError("Exceeded maximum stack depth");
/*      */     }
/* 2764 */     paramCallFrame2.frozen = false;
/*      */ 
/* 2766 */     paramCallFrame2.fnOrScript = paramInterpretedFunction;
/* 2767 */     paramCallFrame2.idata = localInterpreterData1;
/*      */ 
/* 2769 */     paramCallFrame2.stack = arrayOfObject;
/* 2770 */     paramCallFrame2.stackAttributes = arrayOfInt;
/* 2771 */     paramCallFrame2.sDbl = arrayOfDouble;
/* 2772 */     paramCallFrame2.varSource = paramCallFrame2;
/* 2773 */     paramCallFrame2.localShift = localInterpreterData1.itsMaxVars;
/* 2774 */     paramCallFrame2.emptyStackTop = j;
/*      */ 
/* 2776 */     paramCallFrame2.debuggerFrame = localDebugFrame;
/* 2777 */     paramCallFrame2.useActivation = bool;
/*      */ 
/* 2779 */     paramCallFrame2.thisObj = paramScriptable2;
/* 2780 */     paramCallFrame2.scriptRegExps = arrayOfScriptable;
/*      */ 
/* 2784 */     paramCallFrame2.result = Undefined.instance;
/* 2785 */     paramCallFrame2.pc = 0;
/* 2786 */     paramCallFrame2.pcPrevBranch = 0;
/* 2787 */     paramCallFrame2.pcSourceLineStart = localInterpreterData1.firstLinePC;
/* 2788 */     paramCallFrame2.scope = localScriptable;
/*      */ 
/* 2790 */     paramCallFrame2.savedStackTop = j;
/* 2791 */     paramCallFrame2.savedCallOp = 0;
/*      */ 
/* 2793 */     System.arraycopy(paramArrayOfObject, paramInt1, arrayOfObject, 0, i1);
/* 2794 */     if (paramArrayOfDouble != null) {
/* 2795 */       System.arraycopy(paramArrayOfDouble, paramInt1, arrayOfDouble, 0, i1);
/*      */     }
/* 2797 */     for (int i2 = i1; i2 != localInterpreterData1.itsMaxVars; i2++) {
/* 2798 */       arrayOfObject[i2] = Undefined.instance;
/*      */     }
/* 2800 */     if (m != 0)
/*      */     {
/* 2803 */       for (i2 = j + 1; i2 != arrayOfObject.length; i2++) {
/* 2804 */         arrayOfObject[i2] = null;
/*      */       }
/*      */     }
/*      */ 
/* 2808 */     enterFrame(paramContext, paramCallFrame2, paramArrayOfObject, false);
/*      */   }
/*      */ 
/*      */   private static boolean isFrameEnterExitRequired(CallFrame paramCallFrame)
/*      */   {
/* 2813 */     return (paramCallFrame.debuggerFrame != null) || (paramCallFrame.idata.itsNeedsActivation);
/*      */   }
/*      */ 
/*      */   private static void enterFrame(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, boolean paramBoolean)
/*      */   {
/* 2819 */     boolean bool = paramCallFrame.idata.itsNeedsActivation;
/* 2820 */     int i = paramCallFrame.debuggerFrame != null ? 1 : 0;
/* 2821 */     if ((bool) || (i != 0)) {
/* 2822 */       Scriptable localScriptable = paramCallFrame.scope;
/* 2823 */       if (localScriptable == null)
/* 2824 */         Kit.codeBug();
/* 2825 */       else if (paramBoolean)
/*      */       {
/* 2835 */         while ((localScriptable instanceof NativeWith)) {
/* 2836 */           localScriptable = localScriptable.getParentScope();
/* 2837 */           if ((localScriptable == null) || ((paramCallFrame.parentFrame != null) && (paramCallFrame.parentFrame.scope == localScriptable)))
/*      */           {
/* 2843 */             Kit.codeBug();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2852 */       if (i != 0) {
/* 2853 */         paramCallFrame.debuggerFrame.onEnter(paramContext, localScriptable, paramCallFrame.thisObj, paramArrayOfObject);
/*      */       }
/*      */ 
/* 2858 */       if (bool)
/* 2859 */         ScriptRuntime.enterActivationFunction(paramContext, localScriptable);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void exitFrame(Context paramContext, CallFrame paramCallFrame, Object paramObject)
/*      */   {
/* 2867 */     if (paramCallFrame.idata.itsNeedsActivation) {
/* 2868 */       ScriptRuntime.exitActivationFunction(paramContext);
/*      */     }
/*      */ 
/* 2871 */     if (paramCallFrame.debuggerFrame != null)
/*      */       try {
/* 2873 */         if ((paramObject instanceof Throwable)) {
/* 2874 */           paramCallFrame.debuggerFrame.onExit(paramContext, true, paramObject);
/*      */         }
/*      */         else {
/* 2877 */           ContinuationJump localContinuationJump = (ContinuationJump)paramObject;
/*      */           Object localObject;
/* 2878 */           if (localContinuationJump == null)
/* 2879 */             localObject = paramCallFrame.result;
/*      */           else {
/* 2881 */             localObject = localContinuationJump.result;
/*      */           }
/* 2883 */           if (localObject == UniqueTag.DOUBLE_MARK)
/*      */           {
/*      */             double d;
/* 2885 */             if (localContinuationJump == null)
/* 2886 */               d = paramCallFrame.resultDbl;
/*      */             else {
/* 2888 */               d = localContinuationJump.resultDbl;
/*      */             }
/* 2890 */             localObject = ScriptRuntime.wrapNumber(d);
/*      */           }
/* 2892 */           paramCallFrame.debuggerFrame.onExit(paramContext, false, localObject);
/*      */         }
/*      */       } catch (Throwable localThrowable) {
/* 2895 */         System.err.println("RHINO USAGE WARNING: onExit terminated with exception");
/*      */ 
/* 2897 */         localThrowable.printStackTrace(System.err);
/*      */       }
/*      */   }
/*      */ 
/*      */   private static void setCallResult(CallFrame paramCallFrame, Object paramObject, double paramDouble)
/*      */   {
/* 2906 */     if (paramCallFrame.savedCallOp == 38) {
/* 2907 */       paramCallFrame.stack[paramCallFrame.savedStackTop] = paramObject;
/* 2908 */       paramCallFrame.sDbl[paramCallFrame.savedStackTop] = paramDouble;
/* 2909 */     } else if (paramCallFrame.savedCallOp == 30)
/*      */     {
/* 2913 */       if ((paramObject instanceof Scriptable))
/* 2914 */         paramCallFrame.stack[paramCallFrame.savedStackTop] = paramObject;
/*      */     }
/*      */     else {
/* 2917 */       Kit.codeBug();
/*      */     }
/* 2919 */     paramCallFrame.savedCallOp = 0;
/*      */   }
/*      */ 
/*      */   public static NativeContinuation captureContinuation(Context paramContext) {
/* 2923 */     if ((paramContext.lastInterpreterFrame == null) || (!(paramContext.lastInterpreterFrame instanceof CallFrame)))
/*      */     {
/* 2926 */       throw new IllegalStateException("Interpreter frames not found");
/*      */     }
/* 2928 */     return captureContinuation(paramContext, (CallFrame)paramContext.lastInterpreterFrame, true);
/*      */   }
/*      */ 
/*      */   private static NativeContinuation captureContinuation(Context paramContext, CallFrame paramCallFrame, boolean paramBoolean)
/*      */   {
/* 2934 */     NativeContinuation localNativeContinuation = new NativeContinuation();
/* 2935 */     ScriptRuntime.setObjectProtoAndParent(localNativeContinuation, ScriptRuntime.getTopCallScope(paramContext));
/*      */ 
/* 2939 */     CallFrame localCallFrame1 = paramCallFrame;
/* 2940 */     CallFrame localCallFrame2 = paramCallFrame;
/* 2941 */     while ((localCallFrame1 != null) && (!localCallFrame1.frozen)) {
/* 2942 */       localCallFrame1.frozen = true;
/*      */ 
/* 2944 */       for (int i = localCallFrame1.savedStackTop + 1; i != localCallFrame1.stack.length; i++)
/*      */       {
/* 2946 */         localCallFrame1.stack[i] = null;
/* 2947 */         localCallFrame1.stackAttributes[i] = 0;
/*      */       }
/* 2949 */       if (localCallFrame1.savedCallOp == 38)
/*      */       {
/* 2951 */         localCallFrame1.stack[localCallFrame1.savedStackTop] = null;
/*      */       }
/* 2953 */       else if (localCallFrame1.savedCallOp != 30) Kit.codeBug();
/*      */ 
/* 2958 */       localCallFrame2 = localCallFrame1;
/* 2959 */       localCallFrame1 = localCallFrame1.parentFrame;
/*      */     }
/*      */ 
/* 2962 */     if (paramBoolean) {
/* 2963 */       while (localCallFrame2.parentFrame != null) {
/* 2964 */         localCallFrame2 = localCallFrame2.parentFrame;
/*      */       }
/* 2966 */       if (!localCallFrame2.isContinuationsTopFrame) {
/* 2967 */         throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2974 */     localNativeContinuation.initImplementation(paramCallFrame);
/* 2975 */     return localNativeContinuation;
/*      */   }
/*      */ 
/*      */   private static int stack_int32(CallFrame paramCallFrame, int paramInt)
/*      */   {
/* 2980 */     Object localObject = paramCallFrame.stack[paramInt];
/*      */     double d;
/* 2982 */     if (localObject == UniqueTag.DOUBLE_MARK)
/* 2983 */       d = paramCallFrame.sDbl[paramInt];
/*      */     else {
/* 2985 */       d = ScriptRuntime.toNumber(localObject);
/*      */     }
/* 2987 */     return ScriptRuntime.toInt32(d);
/*      */   }
/*      */ 
/*      */   private static double stack_double(CallFrame paramCallFrame, int paramInt)
/*      */   {
/* 2992 */     Object localObject = paramCallFrame.stack[paramInt];
/* 2993 */     if (localObject != UniqueTag.DOUBLE_MARK) {
/* 2994 */       return ScriptRuntime.toNumber(localObject);
/*      */     }
/* 2996 */     return paramCallFrame.sDbl[paramInt];
/*      */   }
/*      */ 
/*      */   private static boolean stack_boolean(CallFrame paramCallFrame, int paramInt)
/*      */   {
/* 3002 */     Object localObject = paramCallFrame.stack[paramInt];
/* 3003 */     if (localObject == Boolean.TRUE)
/* 3004 */       return true;
/* 3005 */     if (localObject == Boolean.FALSE)
/* 3006 */       return false;
/*      */     double d;
/* 3007 */     if (localObject == UniqueTag.DOUBLE_MARK) {
/* 3008 */       d = paramCallFrame.sDbl[paramInt];
/* 3009 */       return (d == d) && (d != 0.0D);
/* 3010 */     }if ((localObject == null) || (localObject == Undefined.instance))
/* 3011 */       return false;
/* 3012 */     if ((localObject instanceof Number)) {
/* 3013 */       d = ((Number)localObject).doubleValue();
/* 3014 */       return (d == d) && (d != 0.0D);
/* 3015 */     }if ((localObject instanceof Boolean)) {
/* 3016 */       return ((Boolean)localObject).booleanValue();
/*      */     }
/* 3018 */     return ScriptRuntime.toBoolean(localObject);
/*      */   }
/*      */ 
/*      */   private static void do_add(Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt, Context paramContext)
/*      */   {
/* 3025 */     Object localObject1 = paramArrayOfObject[(paramInt + 1)];
/* 3026 */     Object localObject2 = paramArrayOfObject[paramInt];
/*      */     double d1;
/*      */     int i;
/*      */     String str2;
/* 3029 */     if (localObject1 == UniqueTag.DOUBLE_MARK) {
/* 3030 */       d1 = paramArrayOfDouble[(paramInt + 1)];
/* 3031 */       if (localObject2 == UniqueTag.DOUBLE_MARK) {
/* 3032 */         paramArrayOfDouble[paramInt] += d1;
/* 3033 */         return;
/*      */       }
/* 3035 */       i = 1;
/*      */     }
/* 3037 */     else if (localObject2 == UniqueTag.DOUBLE_MARK) {
/* 3038 */       d1 = paramArrayOfDouble[paramInt];
/* 3039 */       localObject2 = localObject1;
/* 3040 */       i = 0;
/*      */     }
/*      */     else {
/* 3043 */       if (((localObject2 instanceof Scriptable)) || ((localObject1 instanceof Scriptable))) {
/* 3044 */         paramArrayOfObject[paramInt] = ScriptRuntime.add(localObject2, localObject1, paramContext);
/*      */       }
/*      */       else
/*      */       {
/*      */         String str1;
/* 3045 */         if ((localObject2 instanceof String)) {
/* 3046 */           str1 = (String)localObject2;
/* 3047 */           str2 = ScriptRuntime.toString(localObject1);
/* 3048 */           paramArrayOfObject[paramInt] = str1.concat(str2);
/* 3049 */         } else if ((localObject1 instanceof String)) {
/* 3050 */           str1 = ScriptRuntime.toString(localObject2);
/* 3051 */           str2 = (String)localObject1;
/* 3052 */           paramArrayOfObject[paramInt] = str1.concat(str2);
/*      */         } else {
/* 3054 */           double d2 = (localObject2 instanceof Number) ? ((Number)localObject2).doubleValue() : ScriptRuntime.toNumber(localObject2);
/*      */ 
/* 3056 */           double d4 = (localObject1 instanceof Number) ? ((Number)localObject1).doubleValue() : ScriptRuntime.toNumber(localObject1);
/*      */ 
/* 3058 */           paramArrayOfObject[paramInt] = UniqueTag.DOUBLE_MARK;
/* 3059 */           paramArrayOfDouble[paramInt] = (d2 + d4);
/*      */         }
/*      */       }
/*      */       return;
/*      */     }
/*      */     Object localObject3;
/* 3065 */     if ((localObject2 instanceof Scriptable)) {
/* 3066 */       localObject1 = ScriptRuntime.wrapNumber(d1);
/* 3067 */       if (i == 0) {
/* 3068 */         localObject3 = localObject2;
/* 3069 */         localObject2 = localObject1;
/* 3070 */         localObject1 = localObject3;
/*      */       }
/* 3072 */       paramArrayOfObject[paramInt] = ScriptRuntime.add(localObject2, localObject1, paramContext);
/* 3073 */     } else if ((localObject2 instanceof String)) {
/* 3074 */       localObject3 = (String)localObject2;
/* 3075 */       str2 = ScriptRuntime.toString(d1);
/* 3076 */       if (i != 0)
/* 3077 */         paramArrayOfObject[paramInt] = ((String)localObject3).concat(str2);
/*      */       else
/* 3079 */         paramArrayOfObject[paramInt] = str2.concat((String)localObject3);
/*      */     }
/*      */     else {
/* 3082 */       double d3 = (localObject2 instanceof Number) ? ((Number)localObject2).doubleValue() : ScriptRuntime.toNumber(localObject2);
/*      */ 
/* 3084 */       paramArrayOfObject[paramInt] = UniqueTag.DOUBLE_MARK;
/* 3085 */       paramArrayOfDouble[paramInt] = (d3 + d1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Object[] getArgsArray(Object[] paramArrayOfObject, double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/* 3092 */     if (paramInt2 == 0) {
/* 3093 */       return ScriptRuntime.emptyArgs;
/*      */     }
/* 3095 */     Object[] arrayOfObject = new Object[paramInt2];
/* 3096 */     for (int i = 0; i != paramInt2; paramInt1++) {
/* 3097 */       Object localObject = paramArrayOfObject[paramInt1];
/* 3098 */       if (localObject == UniqueTag.DOUBLE_MARK) {
/* 3099 */         localObject = ScriptRuntime.wrapNumber(paramArrayOfDouble[paramInt1]);
/*      */       }
/* 3101 */       arrayOfObject[i] = localObject;
/*      */ 
/* 3096 */       i++;
/*      */     }
/*      */ 
/* 3103 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   private static void addInstructionCount(Context paramContext, CallFrame paramCallFrame, int paramInt)
/*      */   {
/* 3109 */     paramContext.instructionCount += paramCallFrame.pc - paramCallFrame.pcPrevBranch + paramInt;
/* 3110 */     if (paramContext.instructionCount > paramContext.instructionThreshold) {
/* 3111 */       paramContext.observeInstructionCount(paramContext.instructionCount);
/* 3112 */       paramContext.instructionCount = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CallFrame
/*      */     implements Cloneable
/*      */   {
/*      */     CallFrame parentFrame;
/*      */     int frameIndex;
/*      */     boolean frozen;
/*      */     InterpretedFunction fnOrScript;
/*      */     InterpreterData idata;
/*      */     Object[] stack;
/*      */     int[] stackAttributes;
/*      */     double[] sDbl;
/*      */     CallFrame varSource;
/*      */     int localShift;
/*      */     int emptyStackTop;
/*      */     DebugFrame debuggerFrame;
/*      */     boolean useActivation;
/*      */     boolean isContinuationsTopFrame;
/*      */     Scriptable thisObj;
/*      */     Scriptable[] scriptRegExps;
/*      */     Object result;
/*      */     double resultDbl;
/*      */     int pc;
/*      */     int pcPrevBranch;
/*      */     int pcSourceLineStart;
/*      */     Scriptable scope;
/*      */     int savedStackTop;
/*      */     int savedCallOp;
/*      */     Object throwable;
/*      */ 
/*      */     CallFrame cloneFrozen()
/*      */     {
/*  122 */       if (!this.frozen) Kit.codeBug();
/*      */       CallFrame localCallFrame;
/*      */       try
/*      */       {
/*  126 */         localCallFrame = (CallFrame)clone();
/*      */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  128 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*  134 */       localCallFrame.stack = ((Object[])this.stack.clone());
/*  135 */       localCallFrame.stackAttributes = ((int[])this.stackAttributes.clone());
/*  136 */       localCallFrame.sDbl = ((double[])this.sDbl.clone());
/*      */ 
/*  138 */       localCallFrame.frozen = false;
/*  139 */       return localCallFrame;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ContinuationJump {
/*      */     Interpreter.CallFrame capturedFrame;
/*      */     Interpreter.CallFrame branchFrame;
/*      */     Object result;
/*      */     double resultDbl;
/*      */ 
/*  152 */     ContinuationJump(NativeContinuation paramNativeContinuation, Interpreter.CallFrame paramCallFrame) { this.capturedFrame = ((Interpreter.CallFrame)paramNativeContinuation.getImplementation());
/*  153 */       if ((this.capturedFrame == null) || (paramCallFrame == null))
/*      */       {
/*  157 */         this.branchFrame = null;
/*      */       }
/*      */       else
/*      */       {
/*  161 */         Interpreter.CallFrame localCallFrame1 = this.capturedFrame;
/*  162 */         Interpreter.CallFrame localCallFrame2 = paramCallFrame;
/*      */ 
/*  166 */         int i = localCallFrame1.frameIndex - localCallFrame2.frameIndex;
/*  167 */         if (i != 0) {
/*  168 */           if (i < 0)
/*      */           {
/*  171 */             localCallFrame1 = paramCallFrame;
/*  172 */             localCallFrame2 = this.capturedFrame;
/*  173 */             i = -i;
/*      */           }
/*      */           do {
/*  176 */             localCallFrame1 = localCallFrame1.parentFrame;
/*  177 */             i--; } while (i != 0);
/*  178 */           if (localCallFrame1.frameIndex != localCallFrame2.frameIndex) Kit.codeBug();
/*      */ 
/*      */         }
/*      */ 
/*  183 */         while ((localCallFrame1 != localCallFrame2) && (localCallFrame1 != null)) {
/*  184 */           localCallFrame1 = localCallFrame1.parentFrame;
/*  185 */           localCallFrame2 = localCallFrame2.parentFrame;
/*      */         }
/*      */ 
/*  188 */         this.branchFrame = localCallFrame1;
/*  189 */         if ((this.branchFrame != null) && (!this.branchFrame.frozen))
/*  190 */           Kit.codeBug();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class GeneratorState
/*      */   {
/*      */     int operation;
/*      */     Object value;
/*      */     RuntimeException returnedException;
/*      */ 
/*      */     GeneratorState(int paramInt, Object paramObject)
/*      */     {
/*  854 */       this.operation = paramInt;
/*  855 */       this.value = paramObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Interpreter
 * JD-Core Version:    0.6.2
 */