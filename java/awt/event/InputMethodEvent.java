/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.EventQueueAccessor;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class InputMethodEvent extends AWTEvent
/*     */ {
/*     */   private static final long serialVersionUID = 4727190874778922661L;
/*     */   public static final int INPUT_METHOD_FIRST = 1100;
/*     */   public static final int INPUT_METHOD_TEXT_CHANGED = 1100;
/*     */   public static final int CARET_POSITION_CHANGED = 1101;
/*     */   public static final int INPUT_METHOD_LAST = 1101;
/*     */   long when;
/*     */   private transient AttributedCharacterIterator text;
/*     */   private transient int committedCharacterCount;
/*     */   private transient TextHitInfo caret;
/*     */   private transient TextHitInfo visiblePosition;
/*     */ 
/*     */   public InputMethodEvent(Component paramComponent, int paramInt1, long paramLong, AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt2, TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*     */   {
/* 154 */     super(paramComponent, paramInt1);
/* 155 */     if ((paramInt1 < 1100) || (paramInt1 > 1101)) {
/* 156 */       throw new IllegalArgumentException("id outside of valid range");
/*     */     }
/*     */ 
/* 159 */     if ((paramInt1 == 1101) && (paramAttributedCharacterIterator != null)) {
/* 160 */       throw new IllegalArgumentException("text must be null for CARET_POSITION_CHANGED");
/*     */     }
/*     */ 
/* 163 */     this.when = paramLong;
/* 164 */     this.text = paramAttributedCharacterIterator;
/* 165 */     int i = 0;
/* 166 */     if (paramAttributedCharacterIterator != null) {
/* 167 */       i = paramAttributedCharacterIterator.getEndIndex() - paramAttributedCharacterIterator.getBeginIndex();
/*     */     }
/*     */ 
/* 170 */     if ((paramInt2 < 0) || (paramInt2 > i)) {
/* 171 */       throw new IllegalArgumentException("committedCharacterCount outside of valid range");
/*     */     }
/* 173 */     this.committedCharacterCount = paramInt2;
/*     */ 
/* 175 */     this.caret = paramTextHitInfo1;
/* 176 */     this.visiblePosition = paramTextHitInfo2;
/*     */   }
/*     */ 
/*     */   public InputMethodEvent(Component paramComponent, int paramInt1, AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt2, TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*     */   {
/* 224 */     this(paramComponent, paramInt1, getMostRecentEventTimeForSource(paramComponent), paramAttributedCharacterIterator, paramInt2, paramTextHitInfo1, paramTextHitInfo2);
/*     */   }
/*     */ 
/*     */   public InputMethodEvent(Component paramComponent, int paramInt, TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*     */   {
/* 267 */     this(paramComponent, paramInt, getMostRecentEventTimeForSource(paramComponent), null, 0, paramTextHitInfo1, paramTextHitInfo2);
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getText()
/*     */   {
/* 282 */     return this.text;
/*     */   }
/*     */ 
/*     */   public int getCommittedCharacterCount()
/*     */   {
/* 289 */     return this.committedCharacterCount;
/*     */   }
/*     */ 
/*     */   public TextHitInfo getCaret()
/*     */   {
/* 305 */     return this.caret;
/*     */   }
/*     */ 
/*     */   public TextHitInfo getVisiblePosition()
/*     */   {
/* 321 */     return this.visiblePosition;
/*     */   }
/*     */ 
/*     */   public void consume()
/*     */   {
/* 329 */     this.consumed = true;
/*     */   }
/*     */ 
/*     */   public boolean isConsumed()
/*     */   {
/* 337 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   public long getWhen()
/*     */   {
/* 347 */     return this.when;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str1;
/* 362 */     switch (this.id) {
/*     */     case 1100:
/* 364 */       str1 = "INPUT_METHOD_TEXT_CHANGED";
/* 365 */       break;
/*     */     case 1101:
/* 367 */       str1 = "CARET_POSITION_CHANGED";
/* 368 */       break;
/*     */     default:
/* 370 */       str1 = "unknown type";
/*     */     }
/*     */     String str2;
/* 374 */     if (this.text == null) {
/* 375 */       str2 = "no text";
/*     */     } else {
/* 377 */       localObject = new StringBuilder("\"");
/* 378 */       int i = this.committedCharacterCount;
/* 379 */       int j = this.text.first();
/* 380 */       while (i-- > 0) {
/* 381 */         ((StringBuilder)localObject).append(j);
/* 382 */         j = this.text.next();
/*     */       }
/* 384 */       ((StringBuilder)localObject).append("\" + \"");
/* 385 */       while (j != 65535) {
/* 386 */         ((StringBuilder)localObject).append(j);
/* 387 */         int k = this.text.next();
/*     */       }
/* 389 */       ((StringBuilder)localObject).append("\"");
/* 390 */       str2 = ((StringBuilder)localObject).toString();
/*     */     }
/*     */ 
/* 393 */     Object localObject = this.committedCharacterCount + " characters committed";
/*     */     String str3;
/* 396 */     if (this.caret == null)
/* 397 */       str3 = "no caret";
/*     */     else
/* 399 */       str3 = "caret: " + this.caret.toString();
/*     */     String str4;
/* 403 */     if (this.visiblePosition == null)
/* 404 */       str4 = "no visible position";
/*     */     else {
/* 406 */       str4 = "visible position: " + this.visiblePosition.toString();
/*     */     }
/*     */ 
/* 409 */     return str1 + ", " + str2 + ", " + (String)localObject + ", " + str3 + ", " + str4;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 418 */     paramObjectInputStream.defaultReadObject();
/* 419 */     if (this.when == 0L)
/*     */     {
/* 421 */       this.when = EventQueue.getMostRecentEventTime();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static long getMostRecentEventTimeForSource(Object paramObject)
/*     */   {
/* 434 */     if (paramObject == null)
/*     */     {
/* 436 */       throw new IllegalArgumentException("null source");
/*     */     }
/* 438 */     AppContext localAppContext = SunToolkit.targetToAppContext(paramObject);
/* 439 */     EventQueue localEventQueue = SunToolkit.getSystemEventQueueImplPP(localAppContext);
/* 440 */     return AWTAccessor.getEventQueueAccessor().getMostRecentEventTime(localEventQueue);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.InputMethodEvent
 * JD-Core Version:    0.6.2
 */