/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.im.spi.InputMethod;
/*     */ 
/*     */ public abstract class InputMethodAdapter
/*     */   implements InputMethod
/*     */ {
/*     */   private Component clientComponent;
/*     */ 
/*     */   void setClientComponent(Component paramComponent)
/*     */   {
/*  53 */     this.clientComponent = paramComponent;
/*     */   }
/*     */ 
/*     */   protected Component getClientComponent() {
/*  57 */     return this.clientComponent;
/*     */   }
/*     */ 
/*     */   protected boolean haveActiveClient() {
/*  61 */     return (this.clientComponent != null) && (this.clientComponent.getInputMethodRequests() != null);
/*     */   }
/*     */ 
/*     */   protected void setAWTFocussedComponent(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected boolean supportsBelowTheSpot()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   protected void stopListening()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void notifyClientWindowChange(Rectangle paramRectangle)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void reconvert()
/*     */   {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract void disableInputMethod();
/*     */ 
/*     */   public abstract String getNativeInputMethodInfo();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputMethodAdapter
 * JD-Core Version:    0.6.2
 */