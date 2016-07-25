/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import java.awt.Insets;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class ScrollPaneWheelScroller
/*     */ {
/*  42 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.ScrollPaneWheelScroller");
/*     */ 
/*     */   public static void handleWheelScrolling(ScrollPane paramScrollPane, MouseWheelEvent paramMouseWheelEvent)
/*     */   {
/*  50 */     if (log.isLoggable(400)) {
/*  51 */       log.finer("x = " + paramMouseWheelEvent.getX() + ", y = " + paramMouseWheelEvent.getY() + ", src is " + paramMouseWheelEvent.getSource());
/*     */     }
/*  53 */     int i = 0;
/*     */ 
/*  55 */     if ((paramScrollPane != null) && (paramMouseWheelEvent.getScrollAmount() != 0)) {
/*  56 */       Adjustable localAdjustable = getAdjustableToScroll(paramScrollPane);
/*  57 */       if (localAdjustable != null) {
/*  58 */         i = getIncrementFromAdjustable(localAdjustable, paramMouseWheelEvent);
/*  59 */         if (log.isLoggable(400)) {
/*  60 */           log.finer("increment from adjustable(" + localAdjustable.getClass() + ") : " + i);
/*     */         }
/*  62 */         scrollAdjustable(localAdjustable, i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Adjustable getAdjustableToScroll(ScrollPane paramScrollPane)
/*     */   {
/*  72 */     int i = paramScrollPane.getScrollbarDisplayPolicy();
/*     */ 
/*  75 */     if ((i == 1) || (i == 2))
/*     */     {
/*  77 */       if (log.isLoggable(400)) {
/*  78 */         log.finer("using vertical scrolling due to scrollbar policy");
/*     */       }
/*  80 */       return paramScrollPane.getVAdjustable();
/*     */     }
/*     */ 
/*  85 */     Insets localInsets = paramScrollPane.getInsets();
/*  86 */     int j = paramScrollPane.getVScrollbarWidth();
/*     */ 
/*  88 */     if (log.isLoggable(400)) {
/*  89 */       log.finer("insets: l = " + localInsets.left + ", r = " + localInsets.right + ", t = " + localInsets.top + ", b = " + localInsets.bottom);
/*     */ 
/*  91 */       log.finer("vertScrollWidth = " + j);
/*     */     }
/*     */ 
/*  96 */     if (localInsets.right >= j) {
/*  97 */       if (log.isLoggable(400)) {
/*  98 */         log.finer("using vertical scrolling because scrollbar is present");
/*     */       }
/* 100 */       return paramScrollPane.getVAdjustable();
/*     */     }
/*     */ 
/* 103 */     int k = paramScrollPane.getHScrollbarHeight();
/* 104 */     if (localInsets.bottom >= k) {
/* 105 */       if (log.isLoggable(400)) {
/* 106 */         log.finer("using horiz scrolling because scrollbar is present");
/*     */       }
/* 108 */       return paramScrollPane.getHAdjustable();
/*     */     }
/*     */ 
/* 111 */     if (log.isLoggable(400)) {
/* 112 */       log.finer("using NO scrollbar becsause neither is present");
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   public static int getIncrementFromAdjustable(Adjustable paramAdjustable, MouseWheelEvent paramMouseWheelEvent)
/*     */   {
/* 127 */     if ((log.isLoggable(500)) && 
/* 128 */       (paramAdjustable == null)) {
/* 129 */       log.fine("Assertion (adj != null) failed");
/*     */     }
/*     */ 
/* 133 */     int i = 0;
/*     */ 
/* 135 */     if (paramMouseWheelEvent.getScrollType() == 0) {
/* 136 */       i = paramMouseWheelEvent.getUnitsToScroll() * paramAdjustable.getUnitIncrement();
/*     */     }
/* 138 */     else if (paramMouseWheelEvent.getScrollType() == 1) {
/* 139 */       i = paramAdjustable.getBlockIncrement() * paramMouseWheelEvent.getWheelRotation();
/*     */     }
/* 141 */     return i;
/*     */   }
/*     */ 
/*     */   public static void scrollAdjustable(Adjustable paramAdjustable, int paramInt)
/*     */   {
/* 149 */     if (log.isLoggable(500)) {
/* 150 */       if (paramAdjustable == null) {
/* 151 */         log.fine("Assertion (adj != null) failed");
/*     */       }
/* 153 */       if (paramInt == 0) {
/* 154 */         log.fine("Assertion (amount != 0) failed");
/*     */       }
/*     */     }
/*     */ 
/* 158 */     int i = paramAdjustable.getValue();
/* 159 */     int j = paramAdjustable.getMaximum() - paramAdjustable.getVisibleAmount();
/* 160 */     if (log.isLoggable(400)) {
/* 161 */       log.finer("doScrolling by " + paramInt);
/*     */     }
/*     */ 
/* 164 */     if ((paramInt > 0) && (i < j))
/*     */     {
/* 166 */       if (i + paramInt < j) {
/* 167 */         paramAdjustable.setValue(i + paramInt);
/* 168 */         return;
/*     */       }
/*     */ 
/* 171 */       paramAdjustable.setValue(j);
/* 172 */       return;
/*     */     }
/*     */ 
/* 175 */     if ((paramInt < 0) && (i > paramAdjustable.getMinimum()))
/*     */     {
/* 177 */       if (i + paramInt > paramAdjustable.getMinimum()) {
/* 178 */         paramAdjustable.setValue(i + paramInt);
/* 179 */         return;
/*     */       }
/*     */ 
/* 182 */       paramAdjustable.setValue(paramAdjustable.getMinimum());
/* 183 */       return;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.ScrollPaneWheelScroller
 * JD-Core Version:    0.6.2
 */