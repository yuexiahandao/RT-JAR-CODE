/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.UIDefaults;
/*     */ 
/*     */ class MultiUIDefaults extends UIDefaults
/*     */ {
/*     */   MultiUIDefaults(int paramInt, float paramFloat)
/*     */   {
/* 298 */     super(paramInt, paramFloat);
/*     */   }
/*     */   protected void getUIError(String paramString) {
/* 301 */     System.err.println("Multiplexing LAF:  " + paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiUIDefaults
 * JD-Core Version:    0.6.2
 */