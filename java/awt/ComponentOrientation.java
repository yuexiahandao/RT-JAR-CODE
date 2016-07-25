/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public final class ComponentOrientation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4113291392143563828L;
/*     */   private static final int UNK_BIT = 1;
/*     */   private static final int HORIZ_BIT = 2;
/*     */   private static final int LTR_BIT = 4;
/* 107 */   public static final ComponentOrientation LEFT_TO_RIGHT = new ComponentOrientation(6);
/*     */ 
/* 114 */   public static final ComponentOrientation RIGHT_TO_LEFT = new ComponentOrientation(2);
/*     */ 
/* 122 */   public static final ComponentOrientation UNKNOWN = new ComponentOrientation(7);
/*     */   private int orientation;
/*     */ 
/*     */   public boolean isHorizontal()
/*     */   {
/* 131 */     return (this.orientation & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isLeftToRight()
/*     */   {
/* 141 */     return (this.orientation & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public static ComponentOrientation getOrientation(Locale paramLocale)
/*     */   {
/* 153 */     String str = paramLocale.getLanguage();
/* 154 */     if (("iw".equals(str)) || ("ar".equals(str)) || ("fa".equals(str)) || ("ur".equals(str)))
/*     */     {
/* 157 */       return RIGHT_TO_LEFT;
/*     */     }
/* 159 */     return LEFT_TO_RIGHT;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static ComponentOrientation getOrientation(ResourceBundle paramResourceBundle)
/*     */   {
/* 179 */     ComponentOrientation localComponentOrientation = null;
/*     */     try
/*     */     {
/* 182 */       localComponentOrientation = (ComponentOrientation)paramResourceBundle.getObject("Orientation");
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 187 */     if (localComponentOrientation == null) {
/* 188 */       localComponentOrientation = getOrientation(paramResourceBundle.getLocale());
/*     */     }
/* 190 */     if (localComponentOrientation == null) {
/* 191 */       localComponentOrientation = getOrientation(Locale.getDefault());
/*     */     }
/* 193 */     return localComponentOrientation;
/*     */   }
/*     */ 
/*     */   private ComponentOrientation(int paramInt)
/*     */   {
/* 200 */     this.orientation = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ComponentOrientation
 * JD-Core Version:    0.6.2
 */