/*     */ package sun.swing;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Action;
/*     */ 
/*     */ public abstract class UIAction
/*     */   implements Action
/*     */ {
/*     */   private String name;
/*     */ 
/*     */   public UIAction(String paramString)
/*     */   {
/*  65 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public final String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Object getValue(String paramString) {
/*  73 */     if (paramString == "Name") {
/*  74 */       return this.name;
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public void putValue(String paramString, Object paramObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled()
/*     */   {
/*  91 */     return isEnabled(null);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(Object paramObject)
/*     */   {
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.UIAction
 * JD-Core Version:    0.6.2
 */