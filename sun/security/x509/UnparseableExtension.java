/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ 
/*     */ class UnparseableExtension extends Extension
/*     */ {
/*     */   private String name;
/*     */   private Throwable why;
/*     */ 
/*     */   public UnparseableExtension(Extension paramExtension, Throwable paramThrowable)
/*     */   {
/* 359 */     super(paramExtension);
/*     */ 
/* 361 */     this.name = "";
/*     */     try {
/* 363 */       Class localClass = OIDMap.getClass(paramExtension.getExtensionId());
/* 364 */       if (localClass != null) {
/* 365 */         Field localField = localClass.getDeclaredField("NAME");
/* 366 */         this.name = ((String)localField.get(null) + " ");
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 372 */     this.why = paramThrowable;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 376 */     return super.toString() + "Unparseable " + this.name + "extension due to\n" + this.why + "\n\n" + new HexDumpEncoder().encodeBuffer(getExtensionValue());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.UnparseableExtension
 * JD-Core Version:    0.6.2
 */