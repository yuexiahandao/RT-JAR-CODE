/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.debug.DebuggableScript;
/*     */ 
/*     */ final class InterpreterData
/*     */   implements DebuggableScript
/*     */ {
/*     */   static final int INITIAL_MAX_ICODE_LENGTH = 1024;
/*     */   static final int INITIAL_STRINGTABLE_SIZE = 64;
/*     */   static final int INITIAL_NUMBERTABLE_SIZE = 64;
/*     */   String itsName;
/*     */   String itsSourceFile;
/*     */   boolean itsNeedsActivation;
/*     */   int itsFunctionType;
/*     */   String[] itsStringTable;
/*     */   double[] itsDoubleTable;
/*     */   InterpreterData[] itsNestedFunctions;
/*     */   Object[] itsRegExpLiterals;
/*     */   byte[] itsICode;
/*     */   int[] itsExceptionTable;
/*     */   int itsMaxVars;
/*     */   int itsMaxLocals;
/*     */   int itsMaxStack;
/*     */   int itsMaxFrameArray;
/*     */   String[] argNames;
/*     */   boolean[] argIsConst;
/*     */   int argCount;
/*     */   int itsMaxCalleeArgs;
/*     */   String encodedSource;
/*     */   int encodedSourceStart;
/*     */   int encodedSourceEnd;
/*     */   int languageVersion;
/*     */   boolean useDynamicScope;
/*     */   boolean isStrict;
/*     */   boolean topLevel;
/*     */   Object[] literalIds;
/*     */   UintMap longJumps;
/* 117 */   int firstLinePC = -1;
/*     */   InterpreterData parentData;
/*     */   boolean evalScriptFlag;
/*     */ 
/*     */   InterpreterData(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/*  54 */     this.languageVersion = paramInt;
/*  55 */     this.itsSourceFile = paramString1;
/*  56 */     this.encodedSource = paramString2;
/*  57 */     this.isStrict = paramBoolean;
/*  58 */     init();
/*     */   }
/*     */ 
/*     */   InterpreterData(InterpreterData paramInterpreterData)
/*     */   {
/*  63 */     this.parentData = paramInterpreterData;
/*  64 */     this.languageVersion = paramInterpreterData.languageVersion;
/*  65 */     this.itsSourceFile = paramInterpreterData.itsSourceFile;
/*  66 */     this.encodedSource = paramInterpreterData.encodedSource;
/*     */ 
/*  68 */     init();
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/*  73 */     this.itsICode = new byte[1024];
/*  74 */     this.itsStringTable = new String[64];
/*     */   }
/*     */ 
/*     */   public boolean isTopLevel()
/*     */   {
/* 125 */     return this.topLevel;
/*     */   }
/*     */ 
/*     */   public boolean isFunction()
/*     */   {
/* 130 */     return this.itsFunctionType != 0;
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 135 */     return this.itsName;
/*     */   }
/*     */ 
/*     */   public int getParamCount()
/*     */   {
/* 140 */     return this.argCount;
/*     */   }
/*     */ 
/*     */   public int getParamAndVarCount()
/*     */   {
/* 145 */     return this.argNames.length;
/*     */   }
/*     */ 
/*     */   public String getParamOrVarName(int paramInt)
/*     */   {
/* 150 */     return this.argNames[paramInt];
/*     */   }
/*     */ 
/*     */   public boolean getParamOrVarConst(int paramInt)
/*     */   {
/* 155 */     return this.argIsConst[paramInt];
/*     */   }
/*     */ 
/*     */   public String getSourceName()
/*     */   {
/* 160 */     return this.itsSourceFile;
/*     */   }
/*     */ 
/*     */   public boolean isGeneratedScript()
/*     */   {
/* 165 */     return ScriptRuntime.isGeneratedScript(this.itsSourceFile);
/*     */   }
/*     */ 
/*     */   public int[] getLineNumbers()
/*     */   {
/* 170 */     return Interpreter.getLineNumbers(this);
/*     */   }
/*     */ 
/*     */   public int getFunctionCount()
/*     */   {
/* 175 */     return this.itsNestedFunctions == null ? 0 : this.itsNestedFunctions.length;
/*     */   }
/*     */ 
/*     */   public DebuggableScript getFunction(int paramInt)
/*     */   {
/* 180 */     return this.itsNestedFunctions[paramInt];
/*     */   }
/*     */ 
/*     */   public DebuggableScript getParent()
/*     */   {
/* 185 */     return this.parentData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.InterpreterData
 * JD-Core Version:    0.6.2
 */