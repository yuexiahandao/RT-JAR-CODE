/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.im.InputMethodRequests;
/*     */ import java.awt.peer.TextAreaPeer;
/*     */ 
/*     */ class WTextAreaPeer extends WTextComponentPeer
/*     */   implements TextAreaPeer
/*     */ {
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  38 */     return getMinimumSize(10, 60);
/*     */   }
/*     */ 
/*     */   public void insert(String paramString, int paramInt)
/*     */   {
/*  45 */     insertText(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public void replaceRange(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*  50 */     replaceText(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(int paramInt1, int paramInt2) {
/*  54 */     return getMinimumSize(paramInt1, paramInt2);
/*     */   }
/*     */   public Dimension getMinimumSize(int paramInt1, int paramInt2) {
/*  57 */     FontMetrics localFontMetrics = getFontMetrics(((TextArea)this.target).getFont());
/*  58 */     return new Dimension(localFontMetrics.charWidth('0') * paramInt2 + 20, localFontMetrics.getHeight() * paramInt1 + 20);
/*     */   }
/*     */ 
/*     */   public InputMethodRequests getInputMethodRequests() {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   WTextAreaPeer(TextArea paramTextArea)
/*     */   {
/*  68 */     super(paramTextArea);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   public native void insertText(String paramString, int paramInt);
/*     */ 
/*     */   public native void replaceText(String paramString, int paramInt1, int paramInt2);
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/*  92 */     return getMinimumSize();
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize(int paramInt1, int paramInt2)
/*     */   {
/*  99 */     return getMinimumSize(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Dimension preferredSize(int paramInt1, int paramInt2)
/*     */   {
/* 106 */     return getPreferredSize(paramInt1, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WTextAreaPeer
 * JD-Core Version:    0.6.2
 */