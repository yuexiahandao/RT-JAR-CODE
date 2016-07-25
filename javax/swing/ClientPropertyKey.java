/*     */ package javax.swing;
/*     */ 
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ClientPropertyKeyAccessor;
/*     */ 
/*     */  enum ClientPropertyKey
/*     */ {
/*  65 */   JComponent_INPUT_VERIFIER(true), 
/*     */ 
/*  70 */   JComponent_TRANSFER_HANDLER(true), 
/*     */ 
/*  75 */   JComponent_ANCESTOR_NOTIFIER(true), 
/*     */ 
/*  81 */   PopupFactory_FORCE_HEAVYWEIGHT_POPUP(true);
/*     */ 
/*     */   private final boolean reportValueNotSerializable;
/*     */ 
/*     */   private ClientPropertyKey()
/*     */   {
/* 105 */     this(false);
/*     */   }
/*     */ 
/*     */   private ClientPropertyKey(boolean paramBoolean)
/*     */   {
/* 113 */     this.reportValueNotSerializable = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getReportValueNotSerializable()
/*     */   {
/* 122 */     return this.reportValueNotSerializable;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  92 */     AWTAccessor.setClientPropertyKeyAccessor(new AWTAccessor.ClientPropertyKeyAccessor()
/*     */     {
/*     */       public Object getJComponent_TRANSFER_HANDLER() {
/*  95 */         return ClientPropertyKey.JComponent_TRANSFER_HANDLER;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ClientPropertyKey
 * JD-Core Version:    0.6.2
 */