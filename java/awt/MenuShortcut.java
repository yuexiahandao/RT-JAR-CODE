/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MenuShortcut
/*     */   implements Serializable
/*     */ {
/*     */   int key;
/*     */   boolean usesShift;
/*     */   private static final long serialVersionUID = 143448358473180225L;
/*     */ 
/*     */   public MenuShortcut(int paramInt)
/*     */   {
/* 102 */     this(paramInt, false);
/*     */   }
/*     */ 
/*     */   public MenuShortcut(int paramInt, boolean paramBoolean)
/*     */   {
/* 115 */     this.key = paramInt;
/* 116 */     this.usesShift = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getKey()
/*     */   {
/* 126 */     return this.key;
/*     */   }
/*     */ 
/*     */   public boolean usesShiftModifier()
/*     */   {
/* 136 */     return this.usesShift;
/*     */   }
/*     */ 
/*     */   public boolean equals(MenuShortcut paramMenuShortcut)
/*     */   {
/* 149 */     return (paramMenuShortcut != null) && (paramMenuShortcut.getKey() == this.key) && (paramMenuShortcut.usesShiftModifier() == this.usesShift);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 163 */     if ((paramObject instanceof MenuShortcut)) {
/* 164 */       return equals((MenuShortcut)paramObject);
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 175 */     return this.usesShift ? this.key ^ 0xFFFFFFFF : this.key;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 184 */     int i = 0;
/* 185 */     if (!GraphicsEnvironment.isHeadless()) {
/* 186 */       i = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
/*     */     }
/* 188 */     if (usesShiftModifier()) {
/* 189 */       i |= 1;
/*     */     }
/* 191 */     return KeyEvent.getKeyModifiersText(i) + "+" + KeyEvent.getKeyText(this.key);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 202 */     String str = "key=" + this.key;
/* 203 */     if (usesShiftModifier()) {
/* 204 */       str = str + ",usesShiftModifier";
/*     */     }
/* 206 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MenuShortcut
 * JD-Core Version:    0.6.2
 */