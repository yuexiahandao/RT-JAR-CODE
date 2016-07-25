/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ 
/*     */ public class ActionEvent extends AWTEvent
/*     */ {
/*     */   public static final int SHIFT_MASK = 1;
/*     */   public static final int CTRL_MASK = 2;
/*     */   public static final int META_MASK = 4;
/*     */   public static final int ALT_MASK = 8;
/*     */   public static final int ACTION_FIRST = 1001;
/*     */   public static final int ACTION_LAST = 1001;
/*     */   public static final int ACTION_PERFORMED = 1001;
/*     */   String actionCommand;
/*     */   long when;
/*     */   int modifiers;
/*     */   private static final long serialVersionUID = -7671078796273832149L;
/*     */ 
/*     */   public ActionEvent(Object paramObject, int paramInt, String paramString)
/*     */   {
/* 159 */     this(paramObject, paramInt, paramString, 0);
/*     */   }
/*     */ 
/*     */   public ActionEvent(Object paramObject, int paramInt1, String paramString, int paramInt2)
/*     */   {
/* 188 */     this(paramObject, paramInt1, paramString, 0L, paramInt2);
/*     */   }
/*     */ 
/*     */   public ActionEvent(Object paramObject, int paramInt1, String paramString, long paramLong, int paramInt2)
/*     */   {
/* 225 */     super(paramObject, paramInt1);
/* 226 */     this.actionCommand = paramString;
/* 227 */     this.when = paramLong;
/* 228 */     this.modifiers = paramInt2;
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 246 */     return this.actionCommand;
/*     */   }
/*     */ 
/*     */   public long getWhen()
/*     */   {
/* 258 */     return this.when;
/*     */   }
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/* 267 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str;
/* 278 */     switch (this.id) {
/*     */     case 1001:
/* 280 */       str = "ACTION_PERFORMED";
/* 281 */       break;
/*     */     default:
/* 283 */       str = "unknown type";
/*     */     }
/* 285 */     return str + ",cmd=" + this.actionCommand + ",when=" + this.when + ",modifiers=" + KeyEvent.getKeyModifiersText(this.modifiers);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.ActionEvent
 * JD-Core Version:    0.6.2
 */