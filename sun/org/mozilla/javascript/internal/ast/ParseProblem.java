/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ParseProblem
/*     */ {
/*     */   private Type type;
/*     */   private String message;
/*     */   private String sourceName;
/*     */   private int offset;
/*     */   private int length;
/*     */ 
/*     */   public ParseProblem(Type paramType, String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/*  59 */     setType(paramType);
/*  60 */     setMessage(paramString1);
/*  61 */     setSourceName(paramString2);
/*  62 */     setFileOffset(paramInt1);
/*  63 */     setLength(paramInt2);
/*     */   }
/*     */ 
/*     */   public Type getType() {
/*  67 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(Type paramType) {
/*  71 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   public String getMessage() {
/*  75 */     return this.message;
/*     */   }
/*     */ 
/*     */   public void setMessage(String paramString) {
/*  79 */     this.message = paramString;
/*     */   }
/*     */ 
/*     */   public String getSourceName() {
/*  83 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */   public void setSourceName(String paramString) {
/*  87 */     this.sourceName = paramString;
/*     */   }
/*     */ 
/*     */   public int getFileOffset() {
/*  91 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setFileOffset(int paramInt) {
/*  95 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/*  99 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int paramInt) {
/* 103 */     this.length = paramInt;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     StringBuilder localStringBuilder = new StringBuilder(200);
/* 109 */     localStringBuilder.append(this.sourceName).append(":");
/* 110 */     localStringBuilder.append("offset=").append(this.offset).append(",");
/* 111 */     localStringBuilder.append("length=").append(this.length).append(",");
/* 112 */     localStringBuilder.append(this.type == Type.Error ? "error: " : "warning: ");
/* 113 */     localStringBuilder.append(this.message);
/* 114 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  46 */     Error, Warning;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ParseProblem
 * JD-Core Version:    0.6.2
 */