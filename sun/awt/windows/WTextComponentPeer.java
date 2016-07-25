/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TextComponent;
/*     */ import java.awt.event.TextEvent;
/*     */ import java.awt.peer.TextComponentPeer;
/*     */ 
/*     */ abstract class WTextComponentPeer extends WComponentPeer
/*     */   implements TextComponentPeer
/*     */ {
/*     */   public void setEditable(boolean paramBoolean)
/*     */   {
/*  42 */     enableEditing(paramBoolean);
/*  43 */     setBackground(((TextComponent)this.target).getBackground()); } 
/*     */   public native String getText();
/*     */ 
/*     */   public native void setText(String paramString);
/*     */ 
/*     */   public native int getSelectionStart();
/*     */ 
/*     */   public native int getSelectionEnd();
/*     */ 
/*     */   public native void select(int paramInt1, int paramInt2);
/*     */ 
/*  54 */   WTextComponentPeer(TextComponent paramTextComponent) { super(paramTextComponent); }
/*     */ 
/*     */   void initialize()
/*     */   {
/*  58 */     TextComponent localTextComponent = (TextComponent)this.target;
/*  59 */     String str = localTextComponent.getText();
/*     */ 
/*  61 */     if (str != null) {
/*  62 */       setText(str);
/*     */     }
/*  64 */     select(localTextComponent.getSelectionStart(), localTextComponent.getSelectionEnd());
/*  65 */     setEditable(localTextComponent.isEditable());
/*     */ 
/*  70 */     super.initialize();
/*     */   }
/*     */ 
/*     */   native void enableEditing(boolean paramBoolean);
/*     */ 
/*     */   public boolean isFocusable() {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   public void setCaretPosition(int paramInt)
/*     */   {
/*  85 */     select(paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public int getCaretPosition()
/*     */   {
/*  93 */     return getSelectionStart();
/*     */   }
/*     */ 
/*     */   public void valueChanged()
/*     */   {
/* 100 */     postEvent(new TextEvent(this.target, 900));
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public int getIndexAtPoint(int paramInt1, int paramInt2)
/*     */   {
/* 109 */     return -1; } 
/* 110 */   public Rectangle getCharacterBounds(int paramInt) { return null; } 
/* 111 */   public long filterEvents(long paramLong) { return 0L; }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint() {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  36 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WTextComponentPeer
 * JD-Core Version:    0.6.2
 */