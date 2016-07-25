/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract interface Line extends AutoCloseable
/*     */ {
/*     */   public abstract Info getLineInfo();
/*     */ 
/*     */   public abstract void open()
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   public abstract void close();
/*     */ 
/*     */   public abstract boolean isOpen();
/*     */ 
/*     */   public abstract Control[] getControls();
/*     */ 
/*     */   public abstract boolean isControlSupported(Control.Type paramType);
/*     */ 
/*     */   public abstract Control getControl(Control.Type paramType);
/*     */ 
/*     */   public abstract void addLineListener(LineListener paramLineListener);
/*     */ 
/*     */   public abstract void removeLineListener(LineListener paramLineListener);
/*     */ 
/*     */   public static class Info
/*     */   {
/*     */     private final Class lineClass;
/*     */ 
/*     */     public Info(Class<?> paramClass)
/*     */     {
/* 256 */       if (paramClass == null)
/* 257 */         this.lineClass = Line.class;
/*     */       else
/* 259 */         this.lineClass = paramClass;
/*     */     }
/*     */ 
/*     */     public Class<?> getLineClass()
/*     */     {
/* 270 */       return this.lineClass;
/*     */     }
/*     */ 
/*     */     public boolean matches(Info paramInfo)
/*     */     {
/* 319 */       if (!getClass().isInstance(paramInfo)) {
/* 320 */         return false;
/*     */       }
/*     */ 
/* 328 */       if (!getLineClass().isAssignableFrom(paramInfo.getLineClass())) {
/* 329 */         return false;
/*     */       }
/*     */ 
/* 332 */       return true;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 342 */       String str1 = "javax.sound.sampled.";
/* 343 */       String str2 = new String(getLineClass().toString());
/*     */ 
/* 346 */       int i = str2.indexOf(str1);
/*     */       String str3;
/* 348 */       if (i != -1)
/* 349 */         str3 = str2.substring(0, i) + str2.substring(i + str1.length(), str2.length());
/*     */       else {
/* 351 */         str3 = str2;
/*     */       }
/*     */ 
/* 354 */       return str3;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.Line
 * JD-Core Version:    0.6.2
 */