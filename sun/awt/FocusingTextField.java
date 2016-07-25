/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Event;
/*     */ import java.awt.TextField;
/*     */ 
/*     */ public class FocusingTextField extends TextField
/*     */ {
/*     */   TextField next;
/*     */   boolean willSelect;
/*     */ 
/*     */   public FocusingTextField(int paramInt)
/*     */   {
/*  50 */     super("", paramInt);
/*     */   }
/*     */ 
/*     */   public FocusingTextField(int paramInt, boolean paramBoolean)
/*     */   {
/*  60 */     this(paramInt);
/*  61 */     this.willSelect = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setWillSelect(boolean paramBoolean) {
/*  65 */     this.willSelect = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getWillSelect() {
/*  69 */     return this.willSelect;
/*     */   }
/*     */ 
/*     */   public void setNextField(TextField paramTextField)
/*     */   {
/*  77 */     this.next = paramTextField;
/*     */   }
/*     */ 
/*     */   public boolean gotFocus(Event paramEvent, Object paramObject)
/*     */   {
/*  84 */     if (this.willSelect) {
/*  85 */       select(0, getText().length());
/*     */     }
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean lostFocus(Event paramEvent, Object paramObject)
/*     */   {
/*  94 */     if (this.willSelect) {
/*  95 */       select(0, 0);
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   public void nextFocus()
/*     */   {
/* 104 */     if (this.next != null) {
/* 105 */       this.next.requestFocus();
/*     */     }
/* 107 */     super.nextFocus();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.FocusingTextField
 * JD-Core Version:    0.6.2
 */