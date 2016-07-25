/*     */ package sun.tracing;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class PrintStreamProbe extends ProbeSkeleton
/*     */ {
/*     */   private PrintStreamProvider provider;
/*     */   private String name;
/*     */ 
/*     */   PrintStreamProbe(PrintStreamProvider paramPrintStreamProvider, String paramString, Class<?>[] paramArrayOfClass)
/*     */   {
/*  92 */     super(paramArrayOfClass);
/*  93 */     this.provider = paramPrintStreamProvider;
/*  94 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public boolean isEnabled() {
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   public void uncheckedTrigger(Object[] paramArrayOfObject) {
/* 102 */     StringBuffer localStringBuffer = new StringBuffer();
/* 103 */     localStringBuffer.append(this.provider.getName());
/* 104 */     localStringBuffer.append(".");
/* 105 */     localStringBuffer.append(this.name);
/* 106 */     localStringBuffer.append("(");
/* 107 */     int i = 1;
/* 108 */     for (Object localObject : paramArrayOfObject) {
/* 109 */       if (i == 0)
/* 110 */         localStringBuffer.append(",");
/*     */       else {
/* 112 */         i = 0;
/*     */       }
/* 114 */       localStringBuffer.append(localObject.toString());
/*     */     }
/* 116 */     localStringBuffer.append(")");
/* 117 */     this.provider.getStream().println(localStringBuffer.toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.PrintStreamProbe
 * JD-Core Version:    0.6.2
 */