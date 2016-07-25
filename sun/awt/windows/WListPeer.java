/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.List;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.peer.ListPeer;
/*     */ 
/*     */ class WListPeer extends WComponentPeer
/*     */   implements ListPeer
/*     */ {
/*     */   private FontMetrics fm;
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/*  37 */     return minimumSize(4);
/*     */   }
/*     */   public boolean isFocusable() {
/*  40 */     return true;
/*     */   }
/*     */ 
/*     */   public int[] getSelectedIndexes()
/*     */   {
/*  46 */     List localList = (List)this.target;
/*  47 */     int i = localList.countItems();
/*  48 */     int[] arrayOfInt1 = new int[i];
/*  49 */     int j = 0;
/*  50 */     for (int k = 0; k < i; k++) {
/*  51 */       if (isSelected(k)) {
/*  52 */         arrayOfInt1[(j++)] = k;
/*     */       }
/*     */     }
/*  55 */     int[] arrayOfInt2 = new int[j];
/*  56 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, j);
/*  57 */     return arrayOfInt2;
/*     */   }
/*     */ 
/*     */   public void add(String paramString, int paramInt)
/*     */   {
/*  62 */     addItem(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/*  67 */     clear();
/*     */   }
/*     */ 
/*     */   public void setMultipleMode(boolean paramBoolean)
/*     */   {
/*  72 */     setMultipleSelections(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(int paramInt)
/*     */   {
/*  77 */     return preferredSize(paramInt);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(int paramInt)
/*     */   {
/*  82 */     return minimumSize(paramInt);
/*     */   }
/*     */ 
/*     */   public void addItem(String paramString, int paramInt)
/*     */   {
/*  87 */     addItems(new String[] { paramString }, paramInt, this.fm.stringWidth(paramString));
/*     */   }
/*     */   native void addItems(String[] paramArrayOfString, int paramInt1, int paramInt2);
/*     */ 
/*     */   public native void delItems(int paramInt1, int paramInt2);
/*     */ 
/*  93 */   public void clear() { List localList = (List)this.target;
/*  94 */     delItems(0, localList.countItems()); } 
/*     */   public native void select(int paramInt);
/*     */ 
/*     */   public native void deselect(int paramInt);
/*     */ 
/*     */   public native void makeVisible(int paramInt);
/*     */ 
/*     */   public native void setMultipleSelections(boolean paramBoolean);
/*     */ 
/*     */   public native int getMaxWidth();
/*     */ 
/* 103 */   public Dimension preferredSize(int paramInt) { if (this.fm == null) {
/* 104 */       localObject = (List)this.target;
/* 105 */       this.fm = getFontMetrics(((List)localObject).getFont());
/*     */     }
/* 107 */     Object localObject = minimumSize(paramInt);
/* 108 */     ((Dimension)localObject).width = Math.max(((Dimension)localObject).width, getMaxWidth() + 20);
/* 109 */     return localObject; }
/*     */ 
/*     */   public Dimension minimumSize(int paramInt) {
/* 112 */     return new Dimension(20 + this.fm.stringWidth("0123456789abcde"), this.fm.getHeight() * paramInt + 4);
/*     */   }
/*     */ 
/*     */   WListPeer(List paramList)
/*     */   {
/* 119 */     super(paramList);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/* 125 */     List localList = (List)this.target;
/*     */ 
/* 127 */     this.fm = getFontMetrics(localList.getFont());
/*     */ 
/* 130 */     Font localFont = localList.getFont();
/* 131 */     if (localFont != null) {
/* 132 */       setFont(localFont);
/*     */     }
/*     */ 
/* 136 */     int i = localList.countItems();
/* 137 */     if (i > 0) {
/* 138 */       localObject = new String[i];
/* 139 */       j = 0;
/* 140 */       int k = 0;
/* 141 */       for (int m = 0; m < i; m++) {
/* 142 */         localObject[m] = localList.getItem(m);
/* 143 */         k = this.fm.stringWidth(localObject[m]);
/* 144 */         if (k > j) {
/* 145 */           j = k;
/*     */         }
/*     */       }
/* 148 */       addItems((String[])localObject, 0, j);
/*     */     }
/*     */ 
/* 152 */     setMultipleSelections(localList.allowsMultipleSelections());
/*     */ 
/* 155 */     Object localObject = localList.getSelectedIndexes();
/* 156 */     for (int j = 0; j < localObject.length; j++) {
/* 157 */       select(localObject[j]);
/*     */     }
/*     */ 
/* 166 */     j = localList.getVisibleIndex();
/* 167 */     if ((j < 0) && (localObject.length > 0)) {
/* 168 */       j = localObject[0];
/*     */     }
/* 170 */     if (j >= 0) {
/* 171 */       makeVisible(j);
/*     */     }
/*     */ 
/* 174 */     super.initialize();
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint() {
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */   private native void updateMaxItemWidth();
/*     */ 
/*     */   native boolean isSelected(int paramInt);
/*     */ 
/*     */   public synchronized void _setFont(Font paramFont)
/*     */   {
/* 188 */     super._setFont(paramFont);
/* 189 */     this.fm = getFontMetrics(((List)this.target).getFont());
/* 190 */     updateMaxItemWidth();
/*     */   }
/*     */ 
/*     */   void handleAction(final int paramInt1, final long paramLong, int paramInt2)
/*     */   {
/* 196 */     final List localList = (List)this.target;
/* 197 */     WToolkit.executeOnEventHandlerThread(localList, new Runnable() {
/*     */       public void run() {
/* 199 */         localList.select(paramInt1);
/* 200 */         WListPeer.this.postEvent(new ActionEvent(WListPeer.this.target, 1001, localList.getItem(paramInt1), paramLong, this.val$modifiers));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   void handleListChanged(final int paramInt)
/*     */   {
/* 207 */     final List localList = (List)this.target;
/* 208 */     WToolkit.executeOnEventHandlerThread(localList, new Runnable() {
/*     */       public void run() {
/* 210 */         WListPeer.this.postEvent(new ItemEvent(localList, 701, Integer.valueOf(paramInt), WListPeer.this.isSelected(paramInt) ? 1 : 2));
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WListPeer
 * JD-Core Version:    0.6.2
 */