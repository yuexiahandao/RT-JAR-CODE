/*    */ package sun.font;
/*    */ 
/*    */ import java.awt.font.TextAttribute;
/*    */ import java.text.AttributedCharacterIterator.Attribute;
/*    */ 
/*    */ public enum EAttribute
/*    */ {
/* 45 */   EFAMILY(TextAttribute.FAMILY), 
/* 46 */   EWEIGHT(TextAttribute.WEIGHT), 
/* 47 */   EWIDTH(TextAttribute.WIDTH), 
/* 48 */   EPOSTURE(TextAttribute.POSTURE), 
/* 49 */   ESIZE(TextAttribute.SIZE), 
/* 50 */   ETRANSFORM(TextAttribute.TRANSFORM), 
/* 51 */   ESUPERSCRIPT(TextAttribute.SUPERSCRIPT), 
/* 52 */   EFONT(TextAttribute.FONT), 
/* 53 */   ECHAR_REPLACEMENT(TextAttribute.CHAR_REPLACEMENT), 
/* 54 */   EFOREGROUND(TextAttribute.FOREGROUND), 
/* 55 */   EBACKGROUND(TextAttribute.BACKGROUND), 
/* 56 */   EUNDERLINE(TextAttribute.UNDERLINE), 
/* 57 */   ESTRIKETHROUGH(TextAttribute.STRIKETHROUGH), 
/* 58 */   ERUN_DIRECTION(TextAttribute.RUN_DIRECTION), 
/* 59 */   EBIDI_EMBEDDING(TextAttribute.BIDI_EMBEDDING), 
/* 60 */   EJUSTIFICATION(TextAttribute.JUSTIFICATION), 
/* 61 */   EINPUT_METHOD_HIGHLIGHT(TextAttribute.INPUT_METHOD_HIGHLIGHT), 
/* 62 */   EINPUT_METHOD_UNDERLINE(TextAttribute.INPUT_METHOD_UNDERLINE), 
/* 63 */   ESWAP_COLORS(TextAttribute.SWAP_COLORS), 
/* 64 */   ENUMERIC_SHAPING(TextAttribute.NUMERIC_SHAPING), 
/* 65 */   EKERNING(TextAttribute.KERNING), 
/* 66 */   ELIGATURES(TextAttribute.LIGATURES), 
/* 67 */   ETRACKING(TextAttribute.TRACKING), 
/* 68 */   EBASELINE_TRANSFORM(null);
/*    */ 
/*    */   final int mask;
/*    */   final TextAttribute att;
/* 78 */   static final EAttribute[] atts = (EAttribute[])EAttribute.class.getEnumConstants();
/*    */ 
/*    */   private EAttribute(TextAttribute paramTextAttribute)
/*    */   {
/* 74 */     this.mask = (1 << ordinal());
/* 75 */     this.att = paramTextAttribute;
/*    */   }
/*    */ 
/*    */   public static EAttribute forAttribute(AttributedCharacterIterator.Attribute paramAttribute)
/*    */   {
/* 81 */     for (EAttribute localEAttribute : atts) {
/* 82 */       if (localEAttribute.att == paramAttribute) {
/* 83 */         return localEAttribute;
/*    */       }
/*    */     }
/* 86 */     return null;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 90 */     return name().substring(1).toLowerCase();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.EAttribute
 * JD-Core Version:    0.6.2
 */