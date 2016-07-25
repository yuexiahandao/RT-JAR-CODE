/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.Set;
/*     */ import sun.org.mozilla.javascript.internal.ast.ErrorCollector;
/*     */ 
/*     */ public class CompilerEnvirons
/*     */ {
/*     */   private ErrorReporter errorReporter;
/*     */   private int languageVersion;
/*     */   private boolean generateDebugInfo;
/*     */   private boolean useDynamicScope;
/*     */   private boolean reservedKeywordAsIdentifier;
/*     */   private boolean allowMemberExprAsFunctionName;
/*     */   private boolean xmlAvailable;
/*     */   private int optimizationLevel;
/*     */   private boolean generatingSource;
/*     */   private boolean strictMode;
/*     */   private boolean warningAsError;
/*     */   private boolean generateObserverCount;
/*     */   private boolean recordingComments;
/*     */   private boolean recordingLocalJsDocComments;
/*     */   private boolean recoverFromErrors;
/*     */   private boolean warnTrailingComma;
/*     */   private boolean ideMode;
/*     */   private boolean allowSharpComments;
/*     */   Set<String> activationNames;
/*     */ 
/*     */   public CompilerEnvirons()
/*     */   {
/*  51 */     this.errorReporter = DefaultErrorReporter.instance;
/*  52 */     this.languageVersion = 0;
/*  53 */     this.generateDebugInfo = true;
/*  54 */     this.useDynamicScope = false;
/*  55 */     this.reservedKeywordAsIdentifier = false;
/*  56 */     this.allowMemberExprAsFunctionName = false;
/*  57 */     this.xmlAvailable = true;
/*  58 */     this.optimizationLevel = 0;
/*  59 */     this.generatingSource = true;
/*  60 */     this.strictMode = false;
/*  61 */     this.warningAsError = false;
/*  62 */     this.generateObserverCount = false;
/*  63 */     this.allowSharpComments = false;
/*     */   }
/*     */ 
/*     */   public void initFromContext(Context paramContext)
/*     */   {
/*  68 */     setErrorReporter(paramContext.getErrorReporter());
/*  69 */     this.languageVersion = paramContext.getLanguageVersion();
/*  70 */     this.useDynamicScope = paramContext.compileFunctionsWithDynamicScopeFlag;
/*  71 */     this.generateDebugInfo = ((!paramContext.isGeneratingDebugChanged()) || (paramContext.isGeneratingDebug()));
/*     */ 
/*  73 */     this.reservedKeywordAsIdentifier = paramContext.hasFeature(3);
/*     */ 
/*  75 */     this.allowMemberExprAsFunctionName = paramContext.hasFeature(2);
/*     */ 
/*  77 */     this.strictMode = paramContext.hasFeature(11);
/*     */ 
/*  79 */     this.warningAsError = paramContext.hasFeature(12);
/*  80 */     this.xmlAvailable = paramContext.hasFeature(6);
/*     */ 
/*  83 */     this.optimizationLevel = paramContext.getOptimizationLevel();
/*     */ 
/*  85 */     this.generatingSource = paramContext.isGeneratingSource();
/*  86 */     this.activationNames = paramContext.activationNames;
/*     */ 
/*  89 */     this.generateObserverCount = paramContext.generateObserverCount;
/*     */   }
/*     */ 
/*     */   public final ErrorReporter getErrorReporter()
/*     */   {
/*  94 */     return this.errorReporter;
/*     */   }
/*     */ 
/*     */   public void setErrorReporter(ErrorReporter paramErrorReporter)
/*     */   {
/*  99 */     if (paramErrorReporter == null) throw new IllegalArgumentException();
/* 100 */     this.errorReporter = paramErrorReporter;
/*     */   }
/*     */ 
/*     */   public final int getLanguageVersion()
/*     */   {
/* 105 */     return this.languageVersion;
/*     */   }
/*     */ 
/*     */   public void setLanguageVersion(int paramInt)
/*     */   {
/* 110 */     Context.checkLanguageVersion(paramInt);
/* 111 */     this.languageVersion = paramInt;
/*     */   }
/*     */ 
/*     */   public final boolean isGenerateDebugInfo()
/*     */   {
/* 116 */     return this.generateDebugInfo;
/*     */   }
/*     */ 
/*     */   public void setGenerateDebugInfo(boolean paramBoolean)
/*     */   {
/* 121 */     this.generateDebugInfo = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final boolean isUseDynamicScope()
/*     */   {
/* 126 */     return this.useDynamicScope;
/*     */   }
/*     */ 
/*     */   public final boolean isReservedKeywordAsIdentifier()
/*     */   {
/* 131 */     return this.reservedKeywordAsIdentifier;
/*     */   }
/*     */ 
/*     */   public void setReservedKeywordAsIdentifier(boolean paramBoolean)
/*     */   {
/* 136 */     this.reservedKeywordAsIdentifier = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final boolean isAllowMemberExprAsFunctionName()
/*     */   {
/* 145 */     return this.allowMemberExprAsFunctionName;
/*     */   }
/*     */ 
/*     */   public void setAllowMemberExprAsFunctionName(boolean paramBoolean)
/*     */   {
/* 150 */     this.allowMemberExprAsFunctionName = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final boolean isXmlAvailable()
/*     */   {
/* 155 */     return this.xmlAvailable;
/*     */   }
/*     */ 
/*     */   public void setXmlAvailable(boolean paramBoolean)
/*     */   {
/* 160 */     this.xmlAvailable = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final int getOptimizationLevel()
/*     */   {
/* 165 */     return this.optimizationLevel;
/*     */   }
/*     */ 
/*     */   public void setOptimizationLevel(int paramInt)
/*     */   {
/* 170 */     Context.checkOptimizationLevel(paramInt);
/* 171 */     this.optimizationLevel = paramInt;
/*     */   }
/*     */ 
/*     */   public final boolean isGeneratingSource()
/*     */   {
/* 176 */     return this.generatingSource;
/*     */   }
/*     */ 
/*     */   public boolean getWarnTrailingComma() {
/* 180 */     return this.warnTrailingComma;
/*     */   }
/*     */ 
/*     */   public void setWarnTrailingComma(boolean paramBoolean) {
/* 184 */     this.warnTrailingComma = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final boolean isStrictMode()
/*     */   {
/* 189 */     return this.strictMode;
/*     */   }
/*     */ 
/*     */   public void setStrictMode(boolean paramBoolean)
/*     */   {
/* 194 */     this.strictMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final boolean reportWarningAsError()
/*     */   {
/* 199 */     return this.warningAsError;
/*     */   }
/*     */ 
/*     */   public void setGeneratingSource(boolean paramBoolean)
/*     */   {
/* 213 */     this.generatingSource = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isGenerateObserverCount()
/*     */   {
/* 221 */     return this.generateObserverCount;
/*     */   }
/*     */ 
/*     */   public void setGenerateObserverCount(boolean paramBoolean)
/*     */   {
/* 236 */     this.generateObserverCount = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isRecordingComments() {
/* 240 */     return this.recordingComments;
/*     */   }
/*     */ 
/*     */   public void setRecordingComments(boolean paramBoolean) {
/* 244 */     this.recordingComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isRecordingLocalJsDocComments() {
/* 248 */     return this.recordingLocalJsDocComments;
/*     */   }
/*     */ 
/*     */   public void setRecordingLocalJsDocComments(boolean paramBoolean) {
/* 252 */     this.recordingLocalJsDocComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setRecoverFromErrors(boolean paramBoolean)
/*     */   {
/* 261 */     this.recoverFromErrors = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean recoverFromErrors() {
/* 265 */     return this.recoverFromErrors;
/*     */   }
/*     */ 
/*     */   public void setIdeMode(boolean paramBoolean)
/*     */   {
/* 273 */     this.ideMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isIdeMode() {
/* 277 */     return this.ideMode;
/*     */   }
/*     */ 
/*     */   public Set<String> getActivationNames() {
/* 281 */     return this.activationNames;
/*     */   }
/*     */ 
/*     */   public void setActivationNames(Set<String> paramSet) {
/* 285 */     this.activationNames = paramSet;
/*     */   }
/*     */ 
/*     */   public void setAllowSharpComments(boolean paramBoolean)
/*     */   {
/* 292 */     this.allowSharpComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getAllowSharpComments() {
/* 296 */     return this.allowSharpComments;
/*     */   }
/*     */ 
/*     */   public static CompilerEnvirons ideEnvirons()
/*     */   {
/* 305 */     CompilerEnvirons localCompilerEnvirons = new CompilerEnvirons();
/* 306 */     localCompilerEnvirons.setRecoverFromErrors(true);
/* 307 */     localCompilerEnvirons.setRecordingComments(true);
/* 308 */     localCompilerEnvirons.setStrictMode(true);
/* 309 */     localCompilerEnvirons.setWarnTrailingComma(true);
/* 310 */     localCompilerEnvirons.setLanguageVersion(170);
/* 311 */     localCompilerEnvirons.setReservedKeywordAsIdentifier(true);
/* 312 */     localCompilerEnvirons.setIdeMode(true);
/* 313 */     localCompilerEnvirons.setErrorReporter(new ErrorCollector());
/* 314 */     return localCompilerEnvirons;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.CompilerEnvirons
 * JD-Core Version:    0.6.2
 */