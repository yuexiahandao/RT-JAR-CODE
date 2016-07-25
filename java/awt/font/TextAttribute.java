/*      */ package java.awt.font;
/*      */ 
/*      */ import java.io.InvalidObjectException;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class TextAttribute extends AttributedCharacterIterator.Attribute
/*      */ {
/*  260 */   private static final Map instanceMap = new HashMap(29);
/*      */   static final long serialVersionUID = 7744112784117861702L;
/*  328 */   public static final TextAttribute FAMILY = new TextAttribute("family");
/*      */ 
/*  351 */   public static final TextAttribute WEIGHT = new TextAttribute("weight");
/*      */ 
/*  358 */   public static final Float WEIGHT_EXTRA_LIGHT = Float.valueOf(0.5F);
/*      */ 
/*  365 */   public static final Float WEIGHT_LIGHT = Float.valueOf(0.75F);
/*      */ 
/*  373 */   public static final Float WEIGHT_DEMILIGHT = Float.valueOf(0.875F);
/*      */ 
/*  380 */   public static final Float WEIGHT_REGULAR = Float.valueOf(1.0F);
/*      */ 
/*  387 */   public static final Float WEIGHT_SEMIBOLD = Float.valueOf(1.25F);
/*      */ 
/*  395 */   public static final Float WEIGHT_MEDIUM = Float.valueOf(1.5F);
/*      */ 
/*  402 */   public static final Float WEIGHT_DEMIBOLD = Float.valueOf(1.75F);
/*      */ 
/*  409 */   public static final Float WEIGHT_BOLD = Float.valueOf(2.0F);
/*      */ 
/*  416 */   public static final Float WEIGHT_HEAVY = Float.valueOf(2.25F);
/*      */ 
/*  423 */   public static final Float WEIGHT_EXTRABOLD = Float.valueOf(2.5F);
/*      */ 
/*  430 */   public static final Float WEIGHT_ULTRABOLD = Float.valueOf(2.75F);
/*      */ 
/*  448 */   public static final TextAttribute WIDTH = new TextAttribute("width");
/*      */ 
/*  455 */   public static final Float WIDTH_CONDENSED = Float.valueOf(0.75F);
/*      */ 
/*  462 */   public static final Float WIDTH_SEMI_CONDENSED = Float.valueOf(0.875F);
/*      */ 
/*  470 */   public static final Float WIDTH_REGULAR = Float.valueOf(1.0F);
/*      */ 
/*  477 */   public static final Float WIDTH_SEMI_EXTENDED = Float.valueOf(1.25F);
/*      */ 
/*  484 */   public static final Float WIDTH_EXTENDED = Float.valueOf(1.5F);
/*      */ 
/*  508 */   public static final TextAttribute POSTURE = new TextAttribute("posture");
/*      */ 
/*  516 */   public static final Float POSTURE_REGULAR = Float.valueOf(0.0F);
/*      */ 
/*  523 */   public static final Float POSTURE_OBLIQUE = Float.valueOf(0.2F);
/*      */ 
/*  541 */   public static final TextAttribute SIZE = new TextAttribute("size");
/*      */ 
/*  579 */   public static final TextAttribute TRANSFORM = new TextAttribute("transform");
/*      */ 
/*  600 */   public static final TextAttribute SUPERSCRIPT = new TextAttribute("superscript");
/*      */ 
/*  607 */   public static final Integer SUPERSCRIPT_SUPER = Integer.valueOf(1);
/*      */ 
/*  614 */   public static final Integer SUPERSCRIPT_SUB = Integer.valueOf(-1);
/*      */ 
/*  677 */   public static final TextAttribute FONT = new TextAttribute("font");
/*      */ 
/*  701 */   public static final TextAttribute CHAR_REPLACEMENT = new TextAttribute("char_replacement");
/*      */ 
/*  721 */   public static final TextAttribute FOREGROUND = new TextAttribute("foreground");
/*      */ 
/*  740 */   public static final TextAttribute BACKGROUND = new TextAttribute("background");
/*      */ 
/*  753 */   public static final TextAttribute UNDERLINE = new TextAttribute("underline");
/*      */ 
/*  761 */   public static final Integer UNDERLINE_ON = Integer.valueOf(0);
/*      */ 
/*  774 */   public static final TextAttribute STRIKETHROUGH = new TextAttribute("strikethrough");
/*      */ 
/*  782 */   public static final Boolean STRIKETHROUGH_ON = Boolean.TRUE;
/*      */ 
/*  809 */   public static final TextAttribute RUN_DIRECTION = new TextAttribute("run_direction");
/*      */ 
/*  816 */   public static final Boolean RUN_DIRECTION_LTR = Boolean.FALSE;
/*      */ 
/*  823 */   public static final Boolean RUN_DIRECTION_RTL = Boolean.TRUE;
/*      */ 
/*  847 */   public static final TextAttribute BIDI_EMBEDDING = new TextAttribute("bidi_embedding");
/*      */ 
/*  871 */   public static final TextAttribute JUSTIFICATION = new TextAttribute("justification");
/*      */ 
/*  879 */   public static final Float JUSTIFICATION_FULL = Float.valueOf(1.0F);
/*      */ 
/*  886 */   public static final Float JUSTIFICATION_NONE = Float.valueOf(0.0F);
/*      */ 
/*  916 */   public static final TextAttribute INPUT_METHOD_HIGHLIGHT = new TextAttribute("input method highlight");
/*      */ 
/*  939 */   public static final TextAttribute INPUT_METHOD_UNDERLINE = new TextAttribute("input method underline");
/*      */ 
/*  947 */   public static final Integer UNDERLINE_LOW_ONE_PIXEL = Integer.valueOf(1);
/*      */ 
/*  955 */   public static final Integer UNDERLINE_LOW_TWO_PIXEL = Integer.valueOf(2);
/*      */ 
/*  963 */   public static final Integer UNDERLINE_LOW_DOTTED = Integer.valueOf(3);
/*      */ 
/*  971 */   public static final Integer UNDERLINE_LOW_GRAY = Integer.valueOf(4);
/*      */ 
/*  979 */   public static final Integer UNDERLINE_LOW_DASHED = Integer.valueOf(5);
/*      */ 
/* 1001 */   public static final TextAttribute SWAP_COLORS = new TextAttribute("swap_colors");
/*      */ 
/* 1009 */   public static final Boolean SWAP_COLORS_ON = Boolean.TRUE;
/*      */ 
/* 1028 */   public static final TextAttribute NUMERIC_SHAPING = new TextAttribute("numeric_shaping");
/*      */ 
/* 1047 */   public static final TextAttribute KERNING = new TextAttribute("kerning");
/*      */ 
/* 1055 */   public static final Integer KERNING_ON = Integer.valueOf(1);
/*      */ 
/* 1070 */   public static final TextAttribute LIGATURES = new TextAttribute("ligatures");
/*      */ 
/* 1078 */   public static final Integer LIGATURES_ON = Integer.valueOf(1);
/*      */ 
/* 1099 */   public static final TextAttribute TRACKING = new TextAttribute("tracking");
/*      */ 
/* 1107 */   public static final Float TRACKING_TIGHT = Float.valueOf(-0.04F);
/*      */ 
/* 1115 */   public static final Float TRACKING_LOOSE = Float.valueOf(0.04F);
/*      */ 
/*      */   protected TextAttribute(String paramString)
/*      */   {
/*  268 */     super(paramString);
/*  269 */     if (getClass() == TextAttribute.class)
/*  270 */       instanceMap.put(paramString, this);
/*      */   }
/*      */ 
/*      */   protected Object readResolve()
/*      */     throws InvalidObjectException
/*      */   {
/*  278 */     if (getClass() != TextAttribute.class) {
/*  279 */       throw new InvalidObjectException("subclass didn't correctly implement readResolve");
/*      */     }
/*      */ 
/*  283 */     TextAttribute localTextAttribute = (TextAttribute)instanceMap.get(getName());
/*  284 */     if (localTextAttribute != null) {
/*  285 */       return localTextAttribute;
/*      */     }
/*  287 */     throw new InvalidObjectException("unknown attribute name");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TextAttribute
 * JD-Core Version:    0.6.2
 */