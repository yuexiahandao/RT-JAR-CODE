/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public abstract class RhinoException extends RuntimeException
/*     */ {
/* 363 */   private static boolean useMozillaStackStyle = false;
/*     */   private String sourceName;
/*     */   private int lineNumber;
/*     */   private String lineSource;
/*     */   private int columnNumber;
/*     */   Object interpreterStackInfo;
/*     */   int[] interpreterLineData;
/*     */ 
/*     */   RhinoException()
/*     */   {
/*  62 */     Evaluator localEvaluator = Context.createInterpreter();
/*  63 */     if (localEvaluator != null)
/*  64 */       localEvaluator.captureStackInfo(this);
/*     */   }
/*     */ 
/*     */   RhinoException(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*  70 */     Evaluator localEvaluator = Context.createInterpreter();
/*  71 */     if (localEvaluator != null)
/*  72 */       localEvaluator.captureStackInfo(this);
/*     */   }
/*     */ 
/*     */   public final String getMessage()
/*     */   {
/*  78 */     String str = details();
/*  79 */     if ((this.sourceName == null) || (this.lineNumber <= 0)) {
/*  80 */       return str;
/*     */     }
/*  82 */     StringBuffer localStringBuffer = new StringBuffer(str);
/*  83 */     localStringBuffer.append(" (");
/*  84 */     if (this.sourceName != null) {
/*  85 */       localStringBuffer.append(this.sourceName);
/*     */     }
/*  87 */     if (this.lineNumber > 0) {
/*  88 */       localStringBuffer.append('#');
/*  89 */       localStringBuffer.append(this.lineNumber);
/*     */     }
/*  91 */     localStringBuffer.append(')');
/*  92 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String details()
/*     */   {
/*  97 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public final String sourceName()
/*     */   {
/* 106 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */   public final void initSourceName(String paramString)
/*     */   {
/* 119 */     if (paramString == null) throw new IllegalArgumentException();
/* 120 */     if (this.sourceName != null) throw new IllegalStateException();
/* 121 */     this.sourceName = paramString;
/*     */   }
/*     */ 
/*     */   public final int lineNumber()
/*     */   {
/* 130 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public final void initLineNumber(int paramInt)
/*     */   {
/* 143 */     if (paramInt <= 0) throw new IllegalArgumentException(String.valueOf(paramInt));
/* 144 */     if (this.lineNumber > 0) throw new IllegalStateException();
/* 145 */     this.lineNumber = paramInt;
/*     */   }
/*     */ 
/*     */   public final int columnNumber()
/*     */   {
/* 153 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public final void initColumnNumber(int paramInt)
/*     */   {
/* 166 */     if (paramInt <= 0) throw new IllegalArgumentException(String.valueOf(paramInt));
/* 167 */     if (this.columnNumber > 0) throw new IllegalStateException();
/* 168 */     this.columnNumber = paramInt;
/*     */   }
/*     */ 
/*     */   public final String lineSource()
/*     */   {
/* 176 */     return this.lineSource;
/*     */   }
/*     */ 
/*     */   public final void initLineSource(String paramString)
/*     */   {
/* 189 */     if (paramString == null) throw new IllegalArgumentException();
/* 190 */     if (this.lineSource != null) throw new IllegalStateException();
/* 191 */     this.lineSource = paramString;
/*     */   }
/*     */ 
/*     */   final void recordErrorOrigin(String paramString1, int paramInt1, String paramString2, int paramInt2)
/*     */   {
/* 198 */     if (paramInt1 == -1) {
/* 199 */       paramInt1 = 0;
/*     */     }
/*     */ 
/* 202 */     if (paramString1 != null) {
/* 203 */       initSourceName(paramString1);
/*     */     }
/* 205 */     if (paramInt1 != 0) {
/* 206 */       initLineNumber(paramInt1);
/*     */     }
/* 208 */     if (paramString2 != null) {
/* 209 */       initLineSource(paramString2);
/*     */     }
/* 211 */     if (paramInt2 != 0)
/* 212 */       initColumnNumber(paramInt2);
/*     */   }
/*     */ 
/*     */   private String generateStackTrace()
/*     */   {
/* 219 */     CharArrayWriter localCharArrayWriter = new CharArrayWriter();
/* 220 */     super.printStackTrace(new PrintWriter(localCharArrayWriter));
/* 221 */     String str = localCharArrayWriter.toString();
/* 222 */     Evaluator localEvaluator = Context.createInterpreter();
/* 223 */     if (localEvaluator != null)
/* 224 */       return localEvaluator.getPatchedStack(this, str);
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   public String getScriptStackTrace()
/*     */   {
/* 238 */     StringBuilder localStringBuilder = new StringBuilder();
/* 239 */     String str = SecurityUtilities.getSystemProperty("line.separator");
/* 240 */     ScriptStackElement[] arrayOfScriptStackElement1 = getScriptStack();
/* 241 */     for (ScriptStackElement localScriptStackElement : arrayOfScriptStackElement1) {
/* 242 */       if (useMozillaStackStyle)
/* 243 */         localScriptStackElement.renderMozillaStyle(localStringBuilder);
/*     */       else {
/* 245 */         localScriptStackElement.renderJavaStyle(localStringBuilder);
/*     */       }
/* 247 */       localStringBuilder.append(str);
/*     */     }
/* 249 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getScriptStackTrace(FilenameFilter paramFilenameFilter)
/*     */   {
/* 263 */     return getScriptStackTrace();
/*     */   }
/*     */ 
/*     */   public ScriptStackElement[] getScriptStack()
/*     */   {
/* 276 */     ArrayList localArrayList = new ArrayList();
/* 277 */     ScriptStackElement[][] arrayOfScriptStackElement = (ScriptStackElement[][])null;
/* 278 */     if (this.interpreterStackInfo != null) {
/* 279 */       Evaluator localEvaluator = Context.createInterpreter();
/* 280 */       if ((localEvaluator instanceof Interpreter))
/* 281 */         arrayOfScriptStackElement = ((Interpreter)localEvaluator).getScriptStackElements(this);
/*     */     }
/* 283 */     int i = 0;
/* 284 */     StackTraceElement[] arrayOfStackTraceElement1 = getStackTrace();
/*     */ 
/* 288 */     Pattern localPattern = Pattern.compile("_c_(.*)_\\d+");
/* 289 */     for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1) {
/* 290 */       String str = localStackTraceElement.getFileName();
/*     */       Object localObject1;
/* 291 */       if ((localStackTraceElement.getMethodName().startsWith("_c_")) && (localStackTraceElement.getLineNumber() > -1) && (str != null) && (!str.endsWith(".java")))
/*     */       {
/* 295 */         localObject1 = localStackTraceElement.getMethodName();
/* 296 */         Matcher localMatcher = localPattern.matcher((CharSequence)localObject1);
/*     */ 
/* 299 */         localObject1 = (!"_c_script_0".equals(localObject1)) && (localMatcher.find()) ? localMatcher.group(1) : null;
/*     */ 
/* 301 */         localArrayList.add(new ScriptStackElement(str, (String)localObject1, localStackTraceElement.getLineNumber()));
/* 302 */       } else if (("sun.org.mozilla.javascript.internal.Interpreter".equals(localStackTraceElement.getClassName())) && ("interpretLoop".equals(localStackTraceElement.getMethodName())) && (arrayOfScriptStackElement != null) && (arrayOfScriptStackElement.length > i))
/*     */       {
/* 306 */         for (Object localObject2 : arrayOfScriptStackElement[(i++)]) {
/* 307 */           localArrayList.add(localObject2);
/*     */         }
/*     */       }
/*     */     }
/* 311 */     return (ScriptStackElement[])localArrayList.toArray(new ScriptStackElement[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 318 */     if (this.interpreterStackInfo == null)
/* 319 */       super.printStackTrace(paramPrintWriter);
/*     */     else
/* 321 */       paramPrintWriter.print(generateStackTrace());
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 328 */     if (this.interpreterStackInfo == null)
/* 329 */       super.printStackTrace(paramPrintStream);
/*     */     else
/* 331 */       paramPrintStream.print(generateStackTrace());
/*     */   }
/*     */ 
/*     */   public static boolean usesMozillaStackStyle()
/*     */   {
/* 346 */     return useMozillaStackStyle;
/*     */   }
/*     */ 
/*     */   public static void useMozillaStackStyle(boolean paramBoolean)
/*     */   {
/* 360 */     useMozillaStackStyle = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.RhinoException
 * JD-Core Version:    0.6.2
 */