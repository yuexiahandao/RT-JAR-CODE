/*     */ package sun.swing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ public abstract class AccumulativeRunnable<T>
/*     */   implements Runnable
/*     */ {
/*  94 */   private List<T> arguments = null;
/*     */ 
/*     */   protected abstract void run(List<T> paramList);
/*     */ 
/*     */   public final void run()
/*     */   {
/* 112 */     run(flush());
/*     */   }
/*     */ 
/*     */   @SafeVarargs
/*     */   public final synchronized void add(T[] paramArrayOfT)
/*     */   {
/* 125 */     int i = 1;
/* 126 */     if (this.arguments == null) {
/* 127 */       i = 0;
/* 128 */       this.arguments = new ArrayList();
/*     */     }
/* 130 */     Collections.addAll(this.arguments, paramArrayOfT);
/* 131 */     if (i == 0)
/* 132 */       submit();
/*     */   }
/*     */ 
/*     */   protected void submit()
/*     */   {
/* 146 */     SwingUtilities.invokeLater(this);
/*     */   }
/*     */ 
/*     */   private final synchronized List<T> flush()
/*     */   {
/* 155 */     List localList = this.arguments;
/* 156 */     this.arguments = null;
/* 157 */     return localList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.AccumulativeRunnable
 * JD-Core Version:    0.6.2
 */