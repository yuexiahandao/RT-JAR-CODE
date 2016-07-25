/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public final class ScriptStackElement
/*    */ {
/*    */   public final String fileName;
/*    */   public final String functionName;
/*    */   public final int lineNumber;
/*    */ 
/*    */   public ScriptStackElement(String paramString1, String paramString2, int paramInt)
/*    */   {
/* 16 */     this.fileName = paramString1;
/* 17 */     this.functionName = paramString2;
/* 18 */     this.lineNumber = paramInt;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 22 */     StringBuilder localStringBuilder = new StringBuilder();
/* 23 */     renderMozillaStyle(localStringBuilder);
/* 24 */     return localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   public void renderJavaStyle(StringBuilder paramStringBuilder)
/*    */   {
/* 33 */     paramStringBuilder.append("\tat ").append(this.fileName);
/* 34 */     if (this.lineNumber > -1) {
/* 35 */       paramStringBuilder.append(':').append(this.lineNumber);
/*    */     }
/* 37 */     if (this.functionName != null)
/* 38 */       paramStringBuilder.append(" (").append(this.functionName).append(')');
/*    */   }
/*    */ 
/*    */   public void renderMozillaStyle(StringBuilder paramStringBuilder)
/*    */   {
/* 48 */     if (this.functionName != null) {
/* 49 */       paramStringBuilder.append(this.functionName).append("()");
/*    */     }
/* 51 */     paramStringBuilder.append('@').append(this.fileName);
/* 52 */     if (this.lineNumber > -1)
/* 53 */       paramStringBuilder.append(':').append(this.lineNumber);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ScriptStackElement
 * JD-Core Version:    0.6.2
 */