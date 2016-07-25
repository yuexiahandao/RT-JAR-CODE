/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Checkbox;
/*     */ import java.awt.CheckboxGroup;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.peer.CheckboxPeer;
/*     */ 
/*     */ public class WCheckboxPeer extends WComponentPeer
/*     */   implements CheckboxPeer
/*     */ {
/*     */   public native void setState(boolean paramBoolean);
/*     */ 
/*     */   public native void setCheckboxGroup(CheckboxGroup paramCheckboxGroup);
/*     */ 
/*     */   public native void setLabel(String paramString);
/*     */ 
/*     */   private static native int getCheckMarkSize();
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  42 */     String str = ((Checkbox)this.target).getLabel();
/*  43 */     int i = getCheckMarkSize();
/*  44 */     if (str == null) {
/*  45 */       str = "";
/*     */     }
/*  47 */     FontMetrics localFontMetrics = getFontMetrics(((Checkbox)this.target).getFont());
/*     */ 
/*  53 */     return new Dimension(localFontMetrics.stringWidth(str) + i / 2 + i, Math.max(localFontMetrics.getHeight() + 8, i));
/*     */   }
/*     */ 
/*     */   public boolean isFocusable()
/*     */   {
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   WCheckboxPeer(Checkbox paramCheckbox)
/*     */   {
/*  64 */     super(paramCheckbox);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/*  70 */     Checkbox localCheckbox = (Checkbox)this.target;
/*  71 */     setState(localCheckbox.getState());
/*  72 */     setCheckboxGroup(localCheckbox.getCheckboxGroup());
/*     */ 
/*  74 */     Color localColor = ((Component)this.target).getBackground();
/*  75 */     if (localColor != null) {
/*  76 */       setBackground(localColor);
/*     */     }
/*     */ 
/*  79 */     super.initialize();
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   void handleAction(final boolean paramBoolean)
/*     */   {
/*  89 */     final Checkbox localCheckbox = (Checkbox)this.target;
/*  90 */     WToolkit.executeOnEventHandlerThread(localCheckbox, new Runnable() {
/*     */       public void run() {
/*  92 */         CheckboxGroup localCheckboxGroup = localCheckbox.getCheckboxGroup();
/*  93 */         if ((localCheckboxGroup != null) && (localCheckbox == localCheckboxGroup.getSelectedCheckbox()) && (localCheckbox.getState())) {
/*  94 */           return;
/*     */         }
/*  96 */         localCheckbox.setState(paramBoolean);
/*  97 */         WCheckboxPeer.this.postEvent(new ItemEvent(localCheckbox, 701, localCheckbox.getLabel(), paramBoolean ? 1 : 2));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/* 108 */     return getMinimumSize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WCheckboxPeer
 * JD-Core Version:    0.6.2
 */