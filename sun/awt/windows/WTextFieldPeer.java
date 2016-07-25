/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.TextField;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.im.InputMethodRequests;
/*     */ import java.awt.peer.TextFieldPeer;
/*     */ 
/*     */ class WTextFieldPeer extends WTextComponentPeer
/*     */   implements TextFieldPeer
/*     */ {
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  39 */     FontMetrics localFontMetrics = getFontMetrics(((TextField)this.target).getFont());
/*  40 */     return new Dimension(localFontMetrics.stringWidth(getText()) + 24, localFontMetrics.getHeight() + 8);
/*     */   }
/*     */ 
/*     */   public boolean handleJavaKeyEvent(KeyEvent paramKeyEvent)
/*     */   {
/*  45 */     switch (paramKeyEvent.getID()) {
/*     */     case 400:
/*  47 */       if ((paramKeyEvent.getKeyChar() == '\n') && (!paramKeyEvent.isAltDown()) && (!paramKeyEvent.isControlDown())) {
/*  48 */         postEvent(new ActionEvent(this.target, 1001, getText(), paramKeyEvent.getWhen(), paramKeyEvent.getModifiers()));
/*     */ 
/*  50 */         return true;
/*     */       }
/*     */       break;
/*     */     }
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   public void setEchoChar(char paramChar)
/*     */   {
/*  62 */     setEchoCharacter(paramChar);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(int paramInt) {
/*  66 */     return getMinimumSize(paramInt);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(int paramInt) {
/*  70 */     FontMetrics localFontMetrics = getFontMetrics(((TextField)this.target).getFont());
/*  71 */     return new Dimension(localFontMetrics.charWidth('0') * paramInt + 24, localFontMetrics.getHeight() + 8);
/*     */   }
/*     */ 
/*     */   public InputMethodRequests getInputMethodRequests() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   WTextFieldPeer(TextField paramTextField)
/*     */   {
/*  83 */     super(paramTextField);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/*  89 */     TextField localTextField = (TextField)this.target;
/*  90 */     if (localTextField.echoCharIsSet()) {
/*  91 */       setEchoChar(localTextField.getEchoChar());
/*     */     }
/*  93 */     super.initialize();
/*     */   }
/*     */ 
/*     */   public native void setEchoCharacter(char paramChar);
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/* 107 */     return getMinimumSize();
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize(int paramInt)
/*     */   {
/* 114 */     return getMinimumSize(paramInt);
/*     */   }
/*     */ 
/*     */   public Dimension preferredSize(int paramInt)
/*     */   {
/* 121 */     return getPreferredSize(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WTextFieldPeer
 * JD-Core Version:    0.6.2
 */