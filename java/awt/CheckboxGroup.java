/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class CheckboxGroup
/*     */   implements Serializable
/*     */ {
/*  64 */   Checkbox selectedCheckbox = null;
/*     */   private static final long serialVersionUID = 3729780091441768983L;
/*     */ 
/*     */   public Checkbox getSelectedCheckbox()
/*     */   {
/*  90 */     return getCurrent();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Checkbox getCurrent()
/*     */   {
/*  99 */     return this.selectedCheckbox;
/*     */   }
/*     */ 
/*     */   public void setSelectedCheckbox(Checkbox paramCheckbox)
/*     */   {
/* 119 */     setCurrent(paramCheckbox);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void setCurrent(Checkbox paramCheckbox)
/*     */   {
/* 128 */     if ((paramCheckbox != null) && (paramCheckbox.group != this)) {
/* 129 */       return;
/*     */     }
/* 131 */     Checkbox localCheckbox = this.selectedCheckbox;
/* 132 */     this.selectedCheckbox = paramCheckbox;
/* 133 */     if ((localCheckbox != null) && (localCheckbox != paramCheckbox) && (localCheckbox.group == this)) {
/* 134 */       localCheckbox.setState(false);
/*     */     }
/* 136 */     if ((paramCheckbox != null) && (localCheckbox != paramCheckbox) && (!paramCheckbox.getState()))
/* 137 */       paramCheckbox.setStateInternal(true);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 147 */     return getClass().getName() + "[selectedCheckbox=" + this.selectedCheckbox + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.CheckboxGroup
 * JD-Core Version:    0.6.2
 */