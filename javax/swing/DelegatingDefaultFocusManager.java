/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.AWTKeyStroke;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.awt.KeyEventDispatcher;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class DelegatingDefaultFocusManager extends DefaultFocusManager
/*     */ {
/*     */   private final KeyboardFocusManager delegate;
/*     */ 
/*     */   DelegatingDefaultFocusManager(KeyboardFocusManager paramKeyboardFocusManager)
/*     */   {
/*  45 */     this.delegate = paramKeyboardFocusManager;
/*  46 */     setDefaultFocusTraversalPolicy(this.gluePolicy);
/*     */   }
/*     */ 
/*     */   KeyboardFocusManager getDelegate() {
/*  50 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   public void processKeyEvent(Component paramComponent, KeyEvent paramKeyEvent)
/*     */   {
/*  57 */     this.delegate.processKeyEvent(paramComponent, paramKeyEvent);
/*     */   }
/*     */   public void focusNextComponent(Component paramComponent) {
/*  60 */     this.delegate.focusNextComponent(paramComponent);
/*     */   }
/*     */   public void focusPreviousComponent(Component paramComponent) {
/*  63 */     this.delegate.focusPreviousComponent(paramComponent);
/*     */   }
/*     */ 
/*     */   public Component getFocusOwner()
/*     */   {
/*  77 */     return this.delegate.getFocusOwner();
/*     */   }
/*     */   public void clearGlobalFocusOwner() {
/*  80 */     this.delegate.clearGlobalFocusOwner();
/*     */   }
/*     */   public Component getPermanentFocusOwner() {
/*  83 */     return this.delegate.getPermanentFocusOwner();
/*     */   }
/*     */   public Window getFocusedWindow() {
/*  86 */     return this.delegate.getFocusedWindow();
/*     */   }
/*     */   public Window getActiveWindow() {
/*  89 */     return this.delegate.getActiveWindow();
/*     */   }
/*     */   public FocusTraversalPolicy getDefaultFocusTraversalPolicy() {
/*  92 */     return this.delegate.getDefaultFocusTraversalPolicy();
/*     */   }
/*     */ 
/*     */   public void setDefaultFocusTraversalPolicy(FocusTraversalPolicy paramFocusTraversalPolicy) {
/*  96 */     if (this.delegate != null)
/*     */     {
/*  98 */       this.delegate.setDefaultFocusTraversalPolicy(paramFocusTraversalPolicy);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDefaultFocusTraversalKeys(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*     */   {
/* 105 */     this.delegate.setDefaultFocusTraversalKeys(paramInt, paramSet);
/*     */   }
/*     */   public Set<AWTKeyStroke> getDefaultFocusTraversalKeys(int paramInt) {
/* 108 */     return this.delegate.getDefaultFocusTraversalKeys(paramInt);
/*     */   }
/*     */   public Container getCurrentFocusCycleRoot() {
/* 111 */     return this.delegate.getCurrentFocusCycleRoot();
/*     */   }
/*     */   public void setGlobalCurrentFocusCycleRoot(Container paramContainer) {
/* 114 */     this.delegate.setGlobalCurrentFocusCycleRoot(paramContainer);
/*     */   }
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 117 */     this.delegate.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 120 */     this.delegate.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener) {
/* 124 */     this.delegate.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener) {
/* 128 */     this.delegate.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */   public void addVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener) {
/* 131 */     this.delegate.addVetoableChangeListener(paramVetoableChangeListener);
/*     */   }
/*     */   public void removeVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener) {
/* 134 */     this.delegate.removeVetoableChangeListener(paramVetoableChangeListener);
/*     */   }
/*     */ 
/*     */   public void addVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener) {
/* 138 */     this.delegate.addVetoableChangeListener(paramString, paramVetoableChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener) {
/* 142 */     this.delegate.removeVetoableChangeListener(paramString, paramVetoableChangeListener);
/*     */   }
/*     */   public void addKeyEventDispatcher(KeyEventDispatcher paramKeyEventDispatcher) {
/* 145 */     this.delegate.addKeyEventDispatcher(paramKeyEventDispatcher);
/*     */   }
/*     */   public void removeKeyEventDispatcher(KeyEventDispatcher paramKeyEventDispatcher) {
/* 148 */     this.delegate.removeKeyEventDispatcher(paramKeyEventDispatcher);
/*     */   }
/*     */   public boolean dispatchEvent(AWTEvent paramAWTEvent) {
/* 151 */     return this.delegate.dispatchEvent(paramAWTEvent);
/*     */   }
/*     */   public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
/* 154 */     return this.delegate.dispatchKeyEvent(paramKeyEvent);
/*     */   }
/*     */   public void upFocusCycle(Component paramComponent) {
/* 157 */     this.delegate.upFocusCycle(paramComponent);
/*     */   }
/*     */   public void downFocusCycle(Container paramContainer) {
/* 160 */     this.delegate.downFocusCycle(paramContainer);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DelegatingDefaultFocusManager
 * JD-Core Version:    0.6.2
 */