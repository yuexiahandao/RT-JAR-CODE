/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Choice;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.awt.peer.ChoicePeer;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ class WChoicePeer extends WComponentPeer
/*     */   implements ChoicePeer
/*     */ {
/*     */   private WindowListener windowListener;
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  40 */     FontMetrics localFontMetrics = getFontMetrics(((Choice)this.target).getFont());
/*  41 */     Choice localChoice = (Choice)this.target;
/*  42 */     int i = 0;
/*  43 */     for (int j = localChoice.getItemCount(); j-- > 0; ) {
/*  44 */       i = Math.max(localFontMetrics.stringWidth(localChoice.getItem(j)), i);
/*     */     }
/*  46 */     return new Dimension(28 + i, Math.max(localFontMetrics.getHeight() + 6, 15));
/*     */   }
/*     */   public boolean isFocusable() {
/*  49 */     return true;
/*     */   }
/*     */ 
/*     */   public native void select(int paramInt);
/*     */ 
/*     */   public void add(String paramString, int paramInt)
/*     */   {
/*  57 */     addItem(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint() {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   public native void removeAll();
/*     */ 
/*     */   public native void remove(int paramInt);
/*     */ 
/*     */   public void addItem(String paramString, int paramInt)
/*     */   {
/*  71 */     addItems(new String[] { paramString }, paramInt);
/*     */   }
/*     */ 
/*     */   public native void addItems(String[] paramArrayOfString, int paramInt);
/*     */ 
/*     */   public synchronized native void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   WChoicePeer(Choice paramChoice)
/*     */   {
/*  82 */     super(paramChoice);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/*  88 */     Choice localChoice = (Choice)this.target;
/*  89 */     int i = localChoice.getItemCount();
/*  90 */     if (i > 0) {
/*  91 */       localObject = new String[i];
/*  92 */       for (int j = 0; j < i; j++) {
/*  93 */         localObject[j] = localChoice.getItem(j);
/*     */       }
/*  95 */       addItems((String[])localObject, 0);
/*  96 */       if (localChoice.getSelectedIndex() >= 0) {
/*  97 */         select(localChoice.getSelectedIndex());
/*     */       }
/*     */     }
/*     */ 
/* 101 */     Object localObject = SunToolkit.getContainingWindow((Component)this.target);
/* 102 */     if (localObject != null) {
/* 103 */       WWindowPeer localWWindowPeer = (WWindowPeer)((Window)localObject).getPeer();
/* 104 */       if (localWWindowPeer != null) {
/* 105 */         this.windowListener = new WindowAdapter() {
/*     */           public void windowIconified(WindowEvent paramAnonymousWindowEvent) {
/* 107 */             WChoicePeer.this.closeList();
/*     */           }
/*     */           public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/* 110 */             WChoicePeer.this.closeList();
/*     */           }
/*     */         };
/* 113 */         localWWindowPeer.addWindowListener(this.windowListener);
/*     */       }
/*     */     }
/* 116 */     super.initialize();
/*     */   }
/*     */ 
/*     */   protected void disposeImpl()
/*     */   {
/* 122 */     Window localWindow = SunToolkit.getContainingWindow((Component)this.target);
/* 123 */     if (localWindow != null) {
/* 124 */       WWindowPeer localWWindowPeer = (WWindowPeer)localWindow.getPeer();
/* 125 */       if (localWWindowPeer != null) {
/* 126 */         localWWindowPeer.removeWindowListener(this.windowListener);
/*     */       }
/*     */     }
/* 129 */     super.disposeImpl();
/*     */   }
/*     */ 
/*     */   void handleAction(final int paramInt)
/*     */   {
/* 135 */     final Choice localChoice = (Choice)this.target;
/* 136 */     WToolkit.executeOnEventHandlerThread(localChoice, new Runnable() {
/*     */       public void run() {
/* 138 */         localChoice.select(paramInt);
/* 139 */         WChoicePeer.this.postEvent(new ItemEvent(localChoice, 701, localChoice.getItem(paramInt), 1));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   int getDropDownHeight()
/*     */   {
/* 146 */     Choice localChoice = (Choice)this.target;
/* 147 */     FontMetrics localFontMetrics = getFontMetrics(localChoice.getFont());
/* 148 */     int i = Math.min(localChoice.getItemCount(), 8);
/* 149 */     return localFontMetrics.getHeight() * i;
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/* 156 */     return getMinimumSize();
/*     */   }
/*     */ 
/*     */   native void closeList();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WChoicePeer
 * JD-Core Version:    0.6.2
 */