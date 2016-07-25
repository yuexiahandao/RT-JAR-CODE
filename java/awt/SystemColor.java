/*     */ package java.awt;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class SystemColor extends Color
/*     */   implements Serializable
/*     */ {
/*     */   public static final int DESKTOP = 0;
/*     */   public static final int ACTIVE_CAPTION = 1;
/*     */   public static final int ACTIVE_CAPTION_TEXT = 2;
/*     */   public static final int ACTIVE_CAPTION_BORDER = 3;
/*     */   public static final int INACTIVE_CAPTION = 4;
/*     */   public static final int INACTIVE_CAPTION_TEXT = 5;
/*     */   public static final int INACTIVE_CAPTION_BORDER = 6;
/*     */   public static final int WINDOW = 7;
/*     */   public static final int WINDOW_BORDER = 8;
/*     */   public static final int WINDOW_TEXT = 9;
/*     */   public static final int MENU = 10;
/*     */   public static final int MENU_TEXT = 11;
/*     */   public static final int TEXT = 12;
/*     */   public static final int TEXT_TEXT = 13;
/*     */   public static final int TEXT_HIGHLIGHT = 14;
/*     */   public static final int TEXT_HIGHLIGHT_TEXT = 15;
/*     */   public static final int TEXT_INACTIVE_TEXT = 16;
/*     */   public static final int CONTROL = 17;
/*     */   public static final int CONTROL_TEXT = 18;
/*     */   public static final int CONTROL_HIGHLIGHT = 19;
/*     */   public static final int CONTROL_LT_HIGHLIGHT = 20;
/*     */   public static final int CONTROL_SHADOW = 21;
/*     */   public static final int CONTROL_DK_SHADOW = 22;
/*     */   public static final int SCROLLBAR = 23;
/*     */   public static final int INFO = 24;
/*     */   public static final int INFO_TEXT = 25;
/*     */   public static final int NUM_COLORS = 26;
/* 247 */   private static int[] systemColors = { -16753572, -16777088, -1, -4144960, -8355712, -4144960, -4144960, -1, -16777216, -16777216, -4144960, -16777216, -4144960, -16777216, -16777088, -1, -8355712, -4144960, -16777216, -1, -2039584, -8355712, -16777216, -2039584, -2039808, -16777216 };
/*     */ 
/* 279 */   public static final SystemColor desktop = new SystemColor((byte)0);
/*     */ 
/* 284 */   public static final SystemColor activeCaption = new SystemColor((byte)1);
/*     */ 
/* 289 */   public static final SystemColor activeCaptionText = new SystemColor((byte)2);
/*     */ 
/* 294 */   public static final SystemColor activeCaptionBorder = new SystemColor((byte)3);
/*     */ 
/* 299 */   public static final SystemColor inactiveCaption = new SystemColor((byte)4);
/*     */ 
/* 304 */   public static final SystemColor inactiveCaptionText = new SystemColor((byte)5);
/*     */ 
/* 309 */   public static final SystemColor inactiveCaptionBorder = new SystemColor((byte)6);
/*     */ 
/* 314 */   public static final SystemColor window = new SystemColor((byte)7);
/*     */ 
/* 319 */   public static final SystemColor windowBorder = new SystemColor((byte)8);
/*     */ 
/* 324 */   public static final SystemColor windowText = new SystemColor((byte)9);
/*     */ 
/* 329 */   public static final SystemColor menu = new SystemColor((byte)10);
/*     */ 
/* 334 */   public static final SystemColor menuText = new SystemColor((byte)11);
/*     */ 
/* 340 */   public static final SystemColor text = new SystemColor((byte)12);
/*     */ 
/* 346 */   public static final SystemColor textText = new SystemColor((byte)13);
/*     */ 
/* 352 */   public static final SystemColor textHighlight = new SystemColor((byte)14);
/*     */ 
/* 358 */   public static final SystemColor textHighlightText = new SystemColor((byte)15);
/*     */ 
/* 363 */   public static final SystemColor textInactiveText = new SystemColor((byte)16);
/*     */ 
/* 369 */   public static final SystemColor control = new SystemColor((byte)17);
/*     */ 
/* 375 */   public static final SystemColor controlText = new SystemColor((byte)18);
/*     */ 
/* 382 */   public static final SystemColor controlHighlight = new SystemColor((byte)19);
/*     */ 
/* 389 */   public static final SystemColor controlLtHighlight = new SystemColor((byte)20);
/*     */ 
/* 396 */   public static final SystemColor controlShadow = new SystemColor((byte)21);
/*     */ 
/* 403 */   public static final SystemColor controlDkShadow = new SystemColor((byte)22);
/*     */ 
/* 408 */   public static final SystemColor scrollbar = new SystemColor((byte)23);
/*     */ 
/* 413 */   public static final SystemColor info = new SystemColor((byte)24);
/*     */ 
/* 418 */   public static final SystemColor infoText = new SystemColor((byte)25);
/*     */   private static final long serialVersionUID = 4503142729533789064L;
/*     */   private transient int index;
/* 430 */   private static SystemColor[] systemColorObjects = { desktop, activeCaption, activeCaptionText, activeCaptionBorder, inactiveCaption, inactiveCaptionText, inactiveCaptionBorder, window, windowBorder, windowText, menu, menuText, text, textText, textHighlight, textHighlightText, textInactiveText, control, controlText, controlHighlight, controlLtHighlight, controlShadow, controlDkShadow, scrollbar, info, infoText };
/*     */ 
/*     */   private static void updateSystemColors()
/*     */   {
/* 467 */     if (!GraphicsEnvironment.isHeadless()) {
/* 468 */       Toolkit.getDefaultToolkit().loadSystemColors(systemColors);
/*     */     }
/* 470 */     for (int i = 0; i < systemColors.length; i++)
/* 471 */       systemColorObjects[i].value = systemColors[i];
/*     */   }
/*     */ 
/*     */   private SystemColor(byte paramByte)
/*     */   {
/* 480 */     super(systemColors[paramByte]);
/* 481 */     this.index = paramByte;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 494 */     return getClass().getName() + "[i=" + this.index + "]";
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */   {
/* 518 */     return systemColorObjects[this.value];
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/* 539 */     SystemColor localSystemColor = new SystemColor((byte)this.index);
/* 540 */     localSystemColor.value = this.index;
/* 541 */     return localSystemColor;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 460 */     updateSystemColors();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.SystemColor
 * JD-Core Version:    0.6.2
 */