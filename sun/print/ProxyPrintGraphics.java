/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.PrintGraphics;
/*     */ import java.awt.PrintJob;
/*     */ 
/*     */ public class ProxyPrintGraphics extends ProxyGraphics
/*     */   implements PrintGraphics
/*     */ {
/*     */   private PrintJob printJob;
/*     */ 
/*     */   public ProxyPrintGraphics(Graphics paramGraphics, PrintJob paramPrintJob)
/*     */   {
/*  44 */     super(paramGraphics);
/*  45 */     this.printJob = paramPrintJob;
/*     */   }
/*     */ 
/*     */   public PrintJob getPrintJob()
/*     */   {
/*  53 */     return this.printJob;
/*     */   }
/*     */ 
/*     */   public Graphics create()
/*     */   {
/*  63 */     return new ProxyPrintGraphics(getGraphics().create(), this.printJob);
/*     */   }
/*     */ 
/*     */   public Graphics create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  84 */     Graphics localGraphics = getGraphics().create(paramInt1, paramInt2, paramInt3, paramInt4);
/*  85 */     return new ProxyPrintGraphics(localGraphics, this.printJob);
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics() {
/*  89 */     return super.getGraphics();
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 100 */     super.dispose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ProxyPrintGraphics
 * JD-Core Version:    0.6.2
 */