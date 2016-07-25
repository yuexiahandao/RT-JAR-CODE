/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ 
/*     */ public class TextEvent extends AWTEvent
/*     */ {
/*     */   public static final int TEXT_FIRST = 900;
/*     */   public static final int TEXT_LAST = 900;
/*     */   public static final int TEXT_VALUE_CHANGED = 900;
/*     */   private static final long serialVersionUID = 6269902291250941179L;
/*     */ 
/*     */   public TextEvent(Object paramObject, int paramInt)
/*     */   {
/*  92 */     super(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str;
/* 104 */     switch (this.id) {
/*     */     case 900:
/* 106 */       str = "TEXT_VALUE_CHANGED";
/* 107 */       break;
/*     */     default:
/* 109 */       str = "unknown type";
/*     */     }
/* 111 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.TextEvent
 * JD-Core Version:    0.6.2
 */