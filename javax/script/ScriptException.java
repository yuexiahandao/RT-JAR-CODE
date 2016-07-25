/*     */ package javax.script;
/*     */ 
/*     */ public class ScriptException extends Exception
/*     */ {
/*     */   private String fileName;
/*     */   private int lineNumber;
/*     */   private int columnNumber;
/*     */ 
/*     */   public ScriptException(String paramString)
/*     */   {
/*  50 */     super(paramString);
/*  51 */     this.fileName = null;
/*  52 */     this.lineNumber = -1;
/*  53 */     this.columnNumber = -1;
/*     */   }
/*     */ 
/*     */   public ScriptException(Exception paramException)
/*     */   {
/*  63 */     super(paramException);
/*  64 */     this.fileName = null;
/*  65 */     this.lineNumber = -1;
/*  66 */     this.columnNumber = -1;
/*     */   }
/*     */ 
/*     */   public ScriptException(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  82 */     super(paramString1);
/*  83 */     this.fileName = paramString2;
/*  84 */     this.lineNumber = paramInt;
/*  85 */     this.columnNumber = -1;
/*     */   }
/*     */ 
/*     */   public ScriptException(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/* 100 */     super(paramString1);
/* 101 */     this.fileName = paramString2;
/* 102 */     this.lineNumber = paramInt1;
/* 103 */     this.columnNumber = paramInt2;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 112 */     String str = super.getMessage();
/* 113 */     if (this.fileName != null) {
/* 114 */       str = str + " in " + this.fileName;
/* 115 */       if (this.lineNumber != -1) {
/* 116 */         str = str + " at line number " + this.lineNumber;
/*     */       }
/*     */ 
/* 119 */       if (this.columnNumber != -1) {
/* 120 */         str = str + " at column number " + this.columnNumber;
/*     */       }
/*     */     }
/*     */ 
/* 124 */     return str;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 132 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 140 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public String getFileName()
/*     */   {
/* 150 */     return this.fileName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.ScriptException
 * JD-Core Version:    0.6.2
 */