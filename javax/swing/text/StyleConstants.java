/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import javax.swing.Icon;
/*     */ 
/*     */ public class StyleConstants
/*     */ {
/*     */   public static final String ComponentElementName = "component";
/*     */   public static final String IconElementName = "icon";
/*  65 */   public static final Object NameAttribute = new StyleConstants("name");
/*     */ 
/*  71 */   public static final Object ResolveAttribute = new StyleConstants("resolver");
/*     */ 
/*  77 */   public static final Object ModelAttribute = new StyleConstants("model");
/*     */ 
/*  94 */   public static final Object BidiLevel = new CharacterConstants("bidiLevel", null);
/*     */ 
/*  99 */   public static final Object FontFamily = new FontConstants("family", null);
/*     */ 
/* 106 */   public static final Object Family = FontFamily;
/*     */ 
/* 111 */   public static final Object FontSize = new FontConstants("size", null);
/*     */ 
/* 118 */   public static final Object Size = FontSize;
/*     */ 
/* 123 */   public static final Object Bold = new FontConstants("bold", null);
/*     */ 
/* 128 */   public static final Object Italic = new FontConstants("italic", null);
/*     */ 
/* 133 */   public static final Object Underline = new CharacterConstants("underline", null);
/*     */ 
/* 138 */   public static final Object StrikeThrough = new CharacterConstants("strikethrough", null);
/*     */ 
/* 143 */   public static final Object Superscript = new CharacterConstants("superscript", null);
/*     */ 
/* 148 */   public static final Object Subscript = new CharacterConstants("subscript", null);
/*     */ 
/* 153 */   public static final Object Foreground = new ColorConstants("foreground", null);
/*     */ 
/* 158 */   public static final Object Background = new ColorConstants("background", null);
/*     */ 
/* 163 */   public static final Object ComponentAttribute = new CharacterConstants("component", null);
/*     */ 
/* 168 */   public static final Object IconAttribute = new CharacterConstants("icon", null);
/*     */ 
/* 175 */   public static final Object ComposedTextAttribute = new StyleConstants("composed text");
/*     */ 
/* 184 */   public static final Object FirstLineIndent = new ParagraphConstants("FirstLineIndent", null);
/*     */ 
/* 191 */   public static final Object LeftIndent = new ParagraphConstants("LeftIndent", null);
/*     */ 
/* 198 */   public static final Object RightIndent = new ParagraphConstants("RightIndent", null);
/*     */ 
/* 205 */   public static final Object LineSpacing = new ParagraphConstants("LineSpacing", null);
/*     */ 
/* 211 */   public static final Object SpaceAbove = new ParagraphConstants("SpaceAbove", null);
/*     */ 
/* 217 */   public static final Object SpaceBelow = new ParagraphConstants("SpaceBelow", null);
/*     */ 
/* 230 */   public static final Object Alignment = new ParagraphConstants("Alignment", null);
/*     */ 
/* 236 */   public static final Object TabSet = new ParagraphConstants("TabSet", null);
/*     */ 
/* 241 */   public static final Object Orientation = new ParagraphConstants("Orientation", null);
/*     */   public static final int ALIGN_LEFT = 0;
/*     */   public static final int ALIGN_CENTER = 1;
/*     */   public static final int ALIGN_RIGHT = 2;
/*     */   public static final int ALIGN_JUSTIFIED = 3;
/* 783 */   static Object[] keys = { NameAttribute, ResolveAttribute, BidiLevel, FontFamily, FontSize, Bold, Italic, Underline, StrikeThrough, Superscript, Subscript, Foreground, Background, ComponentAttribute, IconAttribute, FirstLineIndent, LeftIndent, RightIndent, LineSpacing, SpaceAbove, SpaceBelow, Alignment, TabSet, Orientation, ModelAttribute, ComposedTextAttribute };
/*     */   private String representation;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  85 */     return this.representation;
/*     */   }
/*     */ 
/*     */   public static int getBidiLevel(AttributeSet paramAttributeSet)
/*     */   {
/* 285 */     Integer localInteger = (Integer)paramAttributeSet.getAttribute(BidiLevel);
/* 286 */     if (localInteger != null) {
/* 287 */       return localInteger.intValue();
/*     */     }
/* 289 */     return 0;
/*     */   }
/*     */ 
/*     */   public static void setBidiLevel(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */   {
/* 299 */     paramMutableAttributeSet.addAttribute(BidiLevel, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static Component getComponent(AttributeSet paramAttributeSet)
/*     */   {
/* 309 */     return (Component)paramAttributeSet.getAttribute(ComponentAttribute);
/*     */   }
/*     */ 
/*     */   public static void setComponent(MutableAttributeSet paramMutableAttributeSet, Component paramComponent)
/*     */   {
/* 319 */     paramMutableAttributeSet.addAttribute("$ename", "component");
/* 320 */     paramMutableAttributeSet.addAttribute(ComponentAttribute, paramComponent);
/*     */   }
/*     */ 
/*     */   public static Icon getIcon(AttributeSet paramAttributeSet)
/*     */   {
/* 330 */     return (Icon)paramAttributeSet.getAttribute(IconAttribute);
/*     */   }
/*     */ 
/*     */   public static void setIcon(MutableAttributeSet paramMutableAttributeSet, Icon paramIcon)
/*     */   {
/* 340 */     paramMutableAttributeSet.addAttribute("$ename", "icon");
/* 341 */     paramMutableAttributeSet.addAttribute(IconAttribute, paramIcon);
/*     */   }
/*     */ 
/*     */   public static String getFontFamily(AttributeSet paramAttributeSet)
/*     */   {
/* 351 */     String str = (String)paramAttributeSet.getAttribute(FontFamily);
/* 352 */     if (str == null) {
/* 353 */       str = "Monospaced";
/*     */     }
/* 355 */     return str;
/*     */   }
/*     */ 
/*     */   public static void setFontFamily(MutableAttributeSet paramMutableAttributeSet, String paramString)
/*     */   {
/* 365 */     paramMutableAttributeSet.addAttribute(FontFamily, paramString);
/*     */   }
/*     */ 
/*     */   public static int getFontSize(AttributeSet paramAttributeSet)
/*     */   {
/* 375 */     Integer localInteger = (Integer)paramAttributeSet.getAttribute(FontSize);
/* 376 */     if (localInteger != null) {
/* 377 */       return localInteger.intValue();
/*     */     }
/* 379 */     return 12;
/*     */   }
/*     */ 
/*     */   public static void setFontSize(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */   {
/* 389 */     paramMutableAttributeSet.addAttribute(FontSize, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static boolean isBold(AttributeSet paramAttributeSet)
/*     */   {
/* 399 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(Bold);
/* 400 */     if (localBoolean != null) {
/* 401 */       return localBoolean.booleanValue();
/*     */     }
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setBold(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 413 */     paramMutableAttributeSet.addAttribute(Bold, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static boolean isItalic(AttributeSet paramAttributeSet)
/*     */   {
/* 423 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(Italic);
/* 424 */     if (localBoolean != null) {
/* 425 */       return localBoolean.booleanValue();
/*     */     }
/* 427 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setItalic(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 437 */     paramMutableAttributeSet.addAttribute(Italic, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static boolean isUnderline(AttributeSet paramAttributeSet)
/*     */   {
/* 447 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(Underline);
/* 448 */     if (localBoolean != null) {
/* 449 */       return localBoolean.booleanValue();
/*     */     }
/* 451 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isStrikeThrough(AttributeSet paramAttributeSet)
/*     */   {
/* 461 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(StrikeThrough);
/* 462 */     if (localBoolean != null) {
/* 463 */       return localBoolean.booleanValue();
/*     */     }
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isSuperscript(AttributeSet paramAttributeSet)
/*     */   {
/* 476 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(Superscript);
/* 477 */     if (localBoolean != null) {
/* 478 */       return localBoolean.booleanValue();
/*     */     }
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isSubscript(AttributeSet paramAttributeSet)
/*     */   {
/* 491 */     Boolean localBoolean = (Boolean)paramAttributeSet.getAttribute(Subscript);
/* 492 */     if (localBoolean != null) {
/* 493 */       return localBoolean.booleanValue();
/*     */     }
/* 495 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setUnderline(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 506 */     paramMutableAttributeSet.addAttribute(Underline, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static void setStrikeThrough(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 516 */     paramMutableAttributeSet.addAttribute(StrikeThrough, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static void setSuperscript(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 526 */     paramMutableAttributeSet.addAttribute(Superscript, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static void setSubscript(MutableAttributeSet paramMutableAttributeSet, boolean paramBoolean)
/*     */   {
/* 536 */     paramMutableAttributeSet.addAttribute(Subscript, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public static Color getForeground(AttributeSet paramAttributeSet)
/*     */   {
/* 547 */     Color localColor = (Color)paramAttributeSet.getAttribute(Foreground);
/* 548 */     if (localColor == null) {
/* 549 */       localColor = Color.black;
/*     */     }
/* 551 */     return localColor;
/*     */   }
/*     */ 
/*     */   public static void setForeground(MutableAttributeSet paramMutableAttributeSet, Color paramColor)
/*     */   {
/* 561 */     paramMutableAttributeSet.addAttribute(Foreground, paramColor);
/*     */   }
/*     */ 
/*     */   public static Color getBackground(AttributeSet paramAttributeSet)
/*     */   {
/* 571 */     Color localColor = (Color)paramAttributeSet.getAttribute(Background);
/* 572 */     if (localColor == null) {
/* 573 */       localColor = Color.black;
/*     */     }
/* 575 */     return localColor;
/*     */   }
/*     */ 
/*     */   public static void setBackground(MutableAttributeSet paramMutableAttributeSet, Color paramColor)
/*     */   {
/* 585 */     paramMutableAttributeSet.addAttribute(Background, paramColor);
/*     */   }
/*     */ 
/*     */   public static float getFirstLineIndent(AttributeSet paramAttributeSet)
/*     */   {
/* 598 */     Float localFloat = (Float)paramAttributeSet.getAttribute(FirstLineIndent);
/* 599 */     if (localFloat != null) {
/* 600 */       return localFloat.floatValue();
/*     */     }
/* 602 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setFirstLineIndent(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 612 */     paramMutableAttributeSet.addAttribute(FirstLineIndent, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static float getRightIndent(AttributeSet paramAttributeSet)
/*     */   {
/* 622 */     Float localFloat = (Float)paramAttributeSet.getAttribute(RightIndent);
/* 623 */     if (localFloat != null) {
/* 624 */       return localFloat.floatValue();
/*     */     }
/* 626 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setRightIndent(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 636 */     paramMutableAttributeSet.addAttribute(RightIndent, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static float getLeftIndent(AttributeSet paramAttributeSet)
/*     */   {
/* 646 */     Float localFloat = (Float)paramAttributeSet.getAttribute(LeftIndent);
/* 647 */     if (localFloat != null) {
/* 648 */       return localFloat.floatValue();
/*     */     }
/* 650 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setLeftIndent(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 660 */     paramMutableAttributeSet.addAttribute(LeftIndent, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static float getLineSpacing(AttributeSet paramAttributeSet)
/*     */   {
/* 670 */     Float localFloat = (Float)paramAttributeSet.getAttribute(LineSpacing);
/* 671 */     if (localFloat != null) {
/* 672 */       return localFloat.floatValue();
/*     */     }
/* 674 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setLineSpacing(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 684 */     paramMutableAttributeSet.addAttribute(LineSpacing, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static float getSpaceAbove(AttributeSet paramAttributeSet)
/*     */   {
/* 694 */     Float localFloat = (Float)paramAttributeSet.getAttribute(SpaceAbove);
/* 695 */     if (localFloat != null) {
/* 696 */       return localFloat.floatValue();
/*     */     }
/* 698 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setSpaceAbove(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 708 */     paramMutableAttributeSet.addAttribute(SpaceAbove, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static float getSpaceBelow(AttributeSet paramAttributeSet)
/*     */   {
/* 718 */     Float localFloat = (Float)paramAttributeSet.getAttribute(SpaceBelow);
/* 719 */     if (localFloat != null) {
/* 720 */       return localFloat.floatValue();
/*     */     }
/* 722 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public static void setSpaceBelow(MutableAttributeSet paramMutableAttributeSet, float paramFloat)
/*     */   {
/* 732 */     paramMutableAttributeSet.addAttribute(SpaceBelow, new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public static int getAlignment(AttributeSet paramAttributeSet)
/*     */   {
/* 742 */     Integer localInteger = (Integer)paramAttributeSet.getAttribute(Alignment);
/* 743 */     if (localInteger != null) {
/* 744 */       return localInteger.intValue();
/*     */     }
/* 746 */     return 0;
/*     */   }
/*     */ 
/*     */   public static void setAlignment(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */   {
/* 756 */     paramMutableAttributeSet.addAttribute(Alignment, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static TabSet getTabSet(AttributeSet paramAttributeSet)
/*     */   {
/* 766 */     TabSet localTabSet = (TabSet)paramAttributeSet.getAttribute(TabSet);
/*     */ 
/* 768 */     return localTabSet;
/*     */   }
/*     */ 
/*     */   public static void setTabSet(MutableAttributeSet paramMutableAttributeSet, TabSet paramTabSet)
/*     */   {
/* 778 */     paramMutableAttributeSet.addAttribute(TabSet, paramTabSet);
/*     */   }
/*     */ 
/*     */   StyleConstants(String paramString)
/*     */   {
/* 794 */     this.representation = paramString;
/*     */   }
/*     */ 
/*     */   public static class CharacterConstants extends StyleConstants
/*     */     implements AttributeSet.CharacterAttribute
/*     */   {
/*     */     private CharacterConstants(String paramString)
/*     */     {
/* 821 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ColorConstants extends StyleConstants
/*     */     implements AttributeSet.ColorAttribute, AttributeSet.CharacterAttribute
/*     */   {
/*     */     private ColorConstants(String paramString)
/*     */     {
/* 834 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FontConstants extends StyleConstants
/*     */     implements AttributeSet.FontAttribute, AttributeSet.CharacterAttribute
/*     */   {
/*     */     private FontConstants(String paramString)
/*     */     {
/* 847 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ParagraphConstants extends StyleConstants
/*     */     implements AttributeSet.ParagraphAttribute
/*     */   {
/*     */     private ParagraphConstants(String paramString)
/*     */     {
/* 808 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.StyleConstants
 * JD-Core Version:    0.6.2
 */