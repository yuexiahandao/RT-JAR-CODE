/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public class RepaintArea
/*     */ {
/*     */   private static final int MAX_BENEFIT_RATIO = 4;
/*     */   private static final int HORIZONTAL = 0;
/*     */   private static final int VERTICAL = 1;
/*     */   private static final int UPDATE = 2;
/*     */   private static final int RECT_COUNT = 3;
/*  58 */   private Rectangle[] paintRects = new Rectangle[3];
/*     */ 
/*     */   public RepaintArea()
/*     */   {
/*     */   }
/*     */ 
/*     */   private RepaintArea(RepaintArea paramRepaintArea)
/*     */   {
/*  79 */     for (int i = 0; i < 3; i++)
/*  80 */       this.paintRects[i] = paramRepaintArea.paintRects[i];
/*     */   }
/*     */ 
/*     */   public synchronized void add(Rectangle paramRectangle, int paramInt)
/*     */   {
/*  96 */     if (paramRectangle.isEmpty()) {
/*  97 */       return;
/*     */     }
/*  99 */     int i = 2;
/* 100 */     if (paramInt == 800) {
/* 101 */       i = paramRectangle.width > paramRectangle.height ? 0 : 1;
/*     */     }
/* 103 */     if (this.paintRects[i] != null)
/* 104 */       this.paintRects[i].add(paramRectangle);
/*     */     else
/* 106 */       this.paintRects[i] = new Rectangle(paramRectangle);
/*     */   }
/*     */ 
/*     */   private synchronized RepaintArea cloneAndReset()
/*     */   {
/* 121 */     RepaintArea localRepaintArea = new RepaintArea(this);
/* 122 */     for (int i = 0; i < 3; i++) {
/* 123 */       this.paintRects[i] = null;
/*     */     }
/* 125 */     return localRepaintArea;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 129 */     for (int i = 0; i < 3; i++) {
/* 130 */       if (this.paintRects[i] != null) {
/* 131 */         return false;
/*     */       }
/*     */     }
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized void constrain(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 141 */     for (int i = 0; i < 3; i++) {
/* 142 */       Rectangle localRectangle = this.paintRects[i];
/* 143 */       if (localRectangle != null) {
/* 144 */         if (localRectangle.x < paramInt1) {
/* 145 */           localRectangle.width -= paramInt1 - localRectangle.x;
/* 146 */           localRectangle.x = paramInt1;
/*     */         }
/* 148 */         if (localRectangle.y < paramInt2) {
/* 149 */           localRectangle.height -= paramInt2 - localRectangle.y;
/* 150 */           localRectangle.y = paramInt2;
/*     */         }
/* 152 */         int j = localRectangle.x + localRectangle.width - paramInt1 - paramInt3;
/* 153 */         if (j > 0) {
/* 154 */           localRectangle.width -= j;
/*     */         }
/* 156 */         int k = localRectangle.y + localRectangle.height - paramInt2 - paramInt4;
/* 157 */         if (k > 0) {
/* 158 */           localRectangle.height -= k;
/*     */         }
/* 160 */         if ((localRectangle.width <= 0) || (localRectangle.height <= 0))
/* 161 */           this.paintRects[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void subtract(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 172 */     Rectangle localRectangle = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
/* 173 */     for (int i = 0; i < 3; i++)
/* 174 */       if ((subtract(this.paintRects[i], localRectangle)) && 
/* 175 */         (this.paintRects[i] != null) && (this.paintRects[i].isEmpty()))
/* 176 */         this.paintRects[i] = null;
/*     */   }
/*     */ 
/*     */   public void paint(Object paramObject, boolean paramBoolean)
/*     */   {
/* 193 */     Component localComponent = (Component)paramObject;
/*     */ 
/* 195 */     if (isEmpty()) {
/* 196 */       return;
/*     */     }
/*     */ 
/* 199 */     if (!localComponent.isVisible()) {
/* 200 */       return;
/*     */     }
/*     */ 
/* 203 */     RepaintArea localRepaintArea = cloneAndReset();
/*     */ 
/* 205 */     if (!subtract(localRepaintArea.paintRects[1], localRepaintArea.paintRects[0])) {
/* 206 */       subtract(localRepaintArea.paintRects[0], localRepaintArea.paintRects[1]);
/*     */     }
/*     */ 
/* 209 */     if ((localRepaintArea.paintRects[0] != null) && (localRepaintArea.paintRects[1] != null)) {
/* 210 */       Rectangle localRectangle = localRepaintArea.paintRects[0].union(localRepaintArea.paintRects[1]);
/* 211 */       int j = localRectangle.width * localRectangle.height;
/* 212 */       int k = j - localRepaintArea.paintRects[0].width * localRepaintArea.paintRects[0].height - localRepaintArea.paintRects[1].width * localRepaintArea.paintRects[1].height;
/*     */ 
/* 216 */       if (4 * k < j) {
/* 217 */         localRepaintArea.paintRects[0] = localRectangle;
/* 218 */         localRepaintArea.paintRects[1] = null;
/*     */       }
/*     */     }
/* 221 */     for (int i = 0; i < this.paintRects.length; i++)
/* 222 */       if ((localRepaintArea.paintRects[i] != null) && (!localRepaintArea.paintRects[i].isEmpty()))
/*     */       {
/* 227 */         Graphics localGraphics = localComponent.getGraphics();
/* 228 */         if (localGraphics != null)
/*     */           try {
/* 230 */             localGraphics.setClip(localRepaintArea.paintRects[i]);
/* 231 */             if (i == 2) {
/* 232 */               updateComponent(localComponent, localGraphics);
/*     */             } else {
/* 234 */               if (paramBoolean) {
/* 235 */                 localGraphics.clearRect(localRepaintArea.paintRects[i].x, localRepaintArea.paintRects[i].y, localRepaintArea.paintRects[i].width, localRepaintArea.paintRects[i].height);
/*     */               }
/*     */ 
/* 240 */               paintComponent(localComponent, localGraphics);
/*     */             }
/*     */           } finally {
/* 243 */             localGraphics.dispose();
/*     */           }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void updateComponent(Component paramComponent, Graphics paramGraphics)
/*     */   {
/* 254 */     if (paramComponent != null)
/* 255 */       paramComponent.update(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected void paintComponent(Component paramComponent, Graphics paramGraphics)
/*     */   {
/* 263 */     if (paramComponent != null)
/* 264 */       paramComponent.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   static boolean subtract(Rectangle paramRectangle1, Rectangle paramRectangle2)
/*     */   {
/* 273 */     if ((paramRectangle1 == null) || (paramRectangle2 == null)) {
/* 274 */       return true;
/*     */     }
/* 276 */     Rectangle localRectangle = paramRectangle1.intersection(paramRectangle2);
/* 277 */     if (localRectangle.isEmpty()) {
/* 278 */       return true;
/*     */     }
/* 280 */     if ((paramRectangle1.x == localRectangle.x) && (paramRectangle1.y == localRectangle.y)) {
/* 281 */       if (paramRectangle1.width == localRectangle.width) {
/* 282 */         paramRectangle1.y += localRectangle.height;
/* 283 */         paramRectangle1.height -= localRectangle.height;
/* 284 */         return true;
/*     */       }
/* 286 */       if (paramRectangle1.height == localRectangle.height) {
/* 287 */         paramRectangle1.x += localRectangle.width;
/* 288 */         paramRectangle1.width -= localRectangle.width;
/* 289 */         return true;
/*     */       }
/*     */     }
/* 292 */     else if ((paramRectangle1.x + paramRectangle1.width == localRectangle.x + localRectangle.width) && (paramRectangle1.y + paramRectangle1.height == localRectangle.y + localRectangle.height))
/*     */     {
/* 295 */       if (paramRectangle1.width == localRectangle.width) {
/* 296 */         paramRectangle1.height -= localRectangle.height;
/* 297 */         return true;
/*     */       }
/* 299 */       if (paramRectangle1.height == localRectangle.height) {
/* 300 */         paramRectangle1.width -= localRectangle.width;
/* 301 */         return true;
/*     */       }
/*     */     }
/* 304 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 308 */     return super.toString() + "[ horizontal=" + this.paintRects[0] + " vertical=" + this.paintRects[1] + " update=" + this.paintRects[2] + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.RepaintArea
 * JD-Core Version:    0.6.2
 */