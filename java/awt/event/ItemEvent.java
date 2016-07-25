/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.ItemSelectable;
/*     */ 
/*     */ public class ItemEvent extends AWTEvent
/*     */ {
/*     */   public static final int ITEM_FIRST = 701;
/*     */   public static final int ITEM_LAST = 701;
/*     */   public static final int ITEM_STATE_CHANGED = 701;
/*     */   public static final int SELECTED = 1;
/*     */   public static final int DESELECTED = 2;
/*     */   Object item;
/*     */   int stateChange;
/*     */   private static final long serialVersionUID = -608708132447206933L;
/*     */ 
/*     */   public ItemEvent(ItemSelectable paramItemSelectable, int paramInt1, Object paramObject, int paramInt2)
/*     */   {
/* 136 */     super(paramItemSelectable, paramInt1);
/* 137 */     this.item = paramObject;
/* 138 */     this.stateChange = paramInt2;
/*     */   }
/*     */ 
/*     */   public ItemSelectable getItemSelectable()
/*     */   {
/* 147 */     return (ItemSelectable)this.source;
/*     */   }
/*     */ 
/*     */   public Object getItem()
/*     */   {
/* 156 */     return this.item;
/*     */   }
/*     */ 
/*     */   public int getStateChange()
/*     */   {
/* 169 */     return this.stateChange;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str1;
/* 180 */     switch (this.id) {
/*     */     case 701:
/* 182 */       str1 = "ITEM_STATE_CHANGED";
/* 183 */       break;
/*     */     default:
/* 185 */       str1 = "unknown type";
/*     */     }
/*     */     String str2;
/* 189 */     switch (this.stateChange) {
/*     */     case 1:
/* 191 */       str2 = "SELECTED";
/* 192 */       break;
/*     */     case 2:
/* 194 */       str2 = "DESELECTED";
/* 195 */       break;
/*     */     default:
/* 197 */       str2 = "unknown type";
/*     */     }
/* 199 */     return str1 + ",item=" + this.item + ",stateChange=" + str2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.ItemEvent
 * JD-Core Version:    0.6.2
 */