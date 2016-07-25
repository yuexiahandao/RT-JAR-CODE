/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.SizeRequirements;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyleContext;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ public class CSS
/*      */   implements Serializable
/*      */ {
/*  864 */   private static final Hashtable<String, Attribute> attributeMap = new Hashtable();
/*  865 */   private static final Hashtable<String, Value> valueMap = new Hashtable();
/*      */ 
/*  877 */   private static final Hashtable<HTML.Attribute, Attribute[]> htmlAttrToCssAttrMap = new Hashtable(20);
/*      */ 
/*  884 */   private static final Hashtable<Object, Attribute> styleConstantToCssMap = new Hashtable(17);
/*      */ 
/*  886 */   private static final Hashtable<String, Value> htmlValueToCssValueMap = new Hashtable(8);
/*      */ 
/*  888 */   private static final Hashtable<String, Value> cssValueToInternalValueMap = new Hashtable(13);
/*      */   private transient Hashtable<Object, Object> valueConvertor;
/*      */   private int baseFontSize;
/* 3401 */   private transient StyleSheet styleSheet = null;
/*      */ 
/* 3403 */   static int baseFontSizeIndex = 3;
/*      */ 
/*      */   public CSS()
/*      */   {
/*  479 */     this.baseFontSize = (baseFontSizeIndex + 1);
/*      */ 
/*  481 */     this.valueConvertor = new Hashtable();
/*  482 */     this.valueConvertor.put(Attribute.FONT_SIZE, new FontSize());
/*  483 */     this.valueConvertor.put(Attribute.FONT_FAMILY, new FontFamily());
/*  484 */     this.valueConvertor.put(Attribute.FONT_WEIGHT, new FontWeight());
/*  485 */     BorderStyle localBorderStyle = new BorderStyle();
/*  486 */     this.valueConvertor.put(Attribute.BORDER_TOP_STYLE, localBorderStyle);
/*  487 */     this.valueConvertor.put(Attribute.BORDER_RIGHT_STYLE, localBorderStyle);
/*  488 */     this.valueConvertor.put(Attribute.BORDER_BOTTOM_STYLE, localBorderStyle);
/*  489 */     this.valueConvertor.put(Attribute.BORDER_LEFT_STYLE, localBorderStyle);
/*  490 */     ColorValue localColorValue = new ColorValue();
/*  491 */     this.valueConvertor.put(Attribute.COLOR, localColorValue);
/*  492 */     this.valueConvertor.put(Attribute.BACKGROUND_COLOR, localColorValue);
/*  493 */     this.valueConvertor.put(Attribute.BORDER_TOP_COLOR, localColorValue);
/*  494 */     this.valueConvertor.put(Attribute.BORDER_RIGHT_COLOR, localColorValue);
/*  495 */     this.valueConvertor.put(Attribute.BORDER_BOTTOM_COLOR, localColorValue);
/*  496 */     this.valueConvertor.put(Attribute.BORDER_LEFT_COLOR, localColorValue);
/*  497 */     LengthValue localLengthValue1 = new LengthValue();
/*  498 */     this.valueConvertor.put(Attribute.MARGIN_TOP, localLengthValue1);
/*  499 */     this.valueConvertor.put(Attribute.MARGIN_BOTTOM, localLengthValue1);
/*  500 */     this.valueConvertor.put(Attribute.MARGIN_LEFT, localLengthValue1);
/*  501 */     this.valueConvertor.put(Attribute.MARGIN_LEFT_LTR, localLengthValue1);
/*  502 */     this.valueConvertor.put(Attribute.MARGIN_LEFT_RTL, localLengthValue1);
/*  503 */     this.valueConvertor.put(Attribute.MARGIN_RIGHT, localLengthValue1);
/*  504 */     this.valueConvertor.put(Attribute.MARGIN_RIGHT_LTR, localLengthValue1);
/*  505 */     this.valueConvertor.put(Attribute.MARGIN_RIGHT_RTL, localLengthValue1);
/*  506 */     this.valueConvertor.put(Attribute.PADDING_TOP, localLengthValue1);
/*  507 */     this.valueConvertor.put(Attribute.PADDING_BOTTOM, localLengthValue1);
/*  508 */     this.valueConvertor.put(Attribute.PADDING_LEFT, localLengthValue1);
/*  509 */     this.valueConvertor.put(Attribute.PADDING_RIGHT, localLengthValue1);
/*  510 */     BorderWidthValue localBorderWidthValue = new BorderWidthValue(null, 0);
/*  511 */     this.valueConvertor.put(Attribute.BORDER_TOP_WIDTH, localBorderWidthValue);
/*  512 */     this.valueConvertor.put(Attribute.BORDER_BOTTOM_WIDTH, localBorderWidthValue);
/*  513 */     this.valueConvertor.put(Attribute.BORDER_LEFT_WIDTH, localBorderWidthValue);
/*  514 */     this.valueConvertor.put(Attribute.BORDER_RIGHT_WIDTH, localBorderWidthValue);
/*  515 */     LengthValue localLengthValue2 = new LengthValue(true);
/*  516 */     this.valueConvertor.put(Attribute.TEXT_INDENT, localLengthValue2);
/*  517 */     this.valueConvertor.put(Attribute.WIDTH, localLengthValue1);
/*  518 */     this.valueConvertor.put(Attribute.HEIGHT, localLengthValue1);
/*  519 */     this.valueConvertor.put(Attribute.BORDER_SPACING, localLengthValue1);
/*  520 */     StringValue localStringValue = new StringValue();
/*  521 */     this.valueConvertor.put(Attribute.FONT_STYLE, localStringValue);
/*  522 */     this.valueConvertor.put(Attribute.TEXT_DECORATION, localStringValue);
/*  523 */     this.valueConvertor.put(Attribute.TEXT_ALIGN, localStringValue);
/*  524 */     this.valueConvertor.put(Attribute.VERTICAL_ALIGN, localStringValue);
/*  525 */     CssValueMapper localCssValueMapper = new CssValueMapper();
/*  526 */     this.valueConvertor.put(Attribute.LIST_STYLE_TYPE, localCssValueMapper);
/*      */ 
/*  528 */     this.valueConvertor.put(Attribute.BACKGROUND_IMAGE, new BackgroundImage());
/*      */ 
/*  530 */     this.valueConvertor.put(Attribute.BACKGROUND_POSITION, new BackgroundPosition());
/*      */ 
/*  532 */     this.valueConvertor.put(Attribute.BACKGROUND_REPEAT, localCssValueMapper);
/*      */ 
/*  534 */     this.valueConvertor.put(Attribute.BACKGROUND_ATTACHMENT, localCssValueMapper);
/*      */ 
/*  536 */     CssValue localCssValue = new CssValue();
/*  537 */     int i = Attribute.allAttributes.length;
/*  538 */     for (int j = 0; j < i; j++) {
/*  539 */       Attribute localAttribute = Attribute.allAttributes[j];
/*  540 */       if (this.valueConvertor.get(localAttribute) == null)
/*  541 */         this.valueConvertor.put(localAttribute, localCssValue);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setBaseFontSize(int paramInt)
/*      */   {
/*  552 */     if (paramInt < 1)
/*  553 */       this.baseFontSize = 0;
/*  554 */     else if (paramInt > 7)
/*  555 */       this.baseFontSize = 7;
/*      */     else
/*  557 */       this.baseFontSize = paramInt;
/*      */   }
/*      */ 
/*      */   void setBaseFontSize(String paramString)
/*      */   {
/*  566 */     if (paramString != null)
/*      */     {
/*      */       int i;
/*  567 */       if (paramString.startsWith("+")) {
/*  568 */         i = Integer.valueOf(paramString.substring(1)).intValue();
/*  569 */         setBaseFontSize(this.baseFontSize + i);
/*  570 */       } else if (paramString.startsWith("-")) {
/*  571 */         i = -Integer.valueOf(paramString.substring(1)).intValue();
/*  572 */         setBaseFontSize(this.baseFontSize + i);
/*      */       } else {
/*  574 */         setBaseFontSize(Integer.valueOf(paramString).intValue());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   int getBaseFontSize()
/*      */   {
/*  583 */     return this.baseFontSize;
/*      */   }
/*      */ 
/*      */   void addInternalCSSValue(MutableAttributeSet paramMutableAttributeSet, Attribute paramAttribute, String paramString)
/*      */   {
/*  592 */     if (paramAttribute == Attribute.FONT) {
/*  593 */       ShorthandFontParser.parseShorthandFont(this, paramString, paramMutableAttributeSet);
/*      */     }
/*  595 */     else if (paramAttribute == Attribute.BACKGROUND) {
/*  596 */       ShorthandBackgroundParser.parseShorthandBackground(this, paramString, paramMutableAttributeSet);
/*      */     }
/*  599 */     else if (paramAttribute == Attribute.MARGIN) {
/*  600 */       ShorthandMarginParser.parseShorthandMargin(this, paramString, paramMutableAttributeSet, Attribute.ALL_MARGINS);
/*      */     }
/*  603 */     else if (paramAttribute == Attribute.PADDING) {
/*  604 */       ShorthandMarginParser.parseShorthandMargin(this, paramString, paramMutableAttributeSet, Attribute.ALL_PADDING);
/*      */     }
/*  607 */     else if (paramAttribute == Attribute.BORDER_WIDTH) {
/*  608 */       ShorthandMarginParser.parseShorthandMargin(this, paramString, paramMutableAttributeSet, Attribute.ALL_BORDER_WIDTHS);
/*      */     }
/*  611 */     else if (paramAttribute == Attribute.BORDER_COLOR) {
/*  612 */       ShorthandMarginParser.parseShorthandMargin(this, paramString, paramMutableAttributeSet, Attribute.ALL_BORDER_COLORS);
/*      */     }
/*  615 */     else if (paramAttribute == Attribute.BORDER_STYLE) {
/*  616 */       ShorthandMarginParser.parseShorthandMargin(this, paramString, paramMutableAttributeSet, Attribute.ALL_BORDER_STYLES);
/*      */     }
/*  619 */     else if ((paramAttribute == Attribute.BORDER) || (paramAttribute == Attribute.BORDER_TOP) || (paramAttribute == Attribute.BORDER_RIGHT) || (paramAttribute == Attribute.BORDER_BOTTOM) || (paramAttribute == Attribute.BORDER_LEFT))
/*      */     {
/*  624 */       ShorthandBorderParser.parseShorthandBorder(paramMutableAttributeSet, paramAttribute, paramString);
/*      */     }
/*      */     else {
/*  627 */       Object localObject = getInternalCSSValue(paramAttribute, paramString);
/*  628 */       if (localObject != null)
/*  629 */         paramMutableAttributeSet.addAttribute(paramAttribute, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   Object getInternalCSSValue(Attribute paramAttribute, String paramString)
/*      */   {
/*  641 */     CssValue localCssValue = (CssValue)this.valueConvertor.get(paramAttribute);
/*  642 */     Object localObject = localCssValue.parseCssValue(paramString);
/*  643 */     return localObject != null ? localObject : localCssValue.parseCssValue(paramAttribute.getDefaultValue());
/*      */   }
/*      */ 
/*      */   Attribute styleConstantsKeyToCSSKey(StyleConstants paramStyleConstants)
/*      */   {
/*  650 */     return (Attribute)styleConstantToCssMap.get(paramStyleConstants);
/*      */   }
/*      */ 
/*      */   Object styleConstantsValueToCSSValue(StyleConstants paramStyleConstants, Object paramObject)
/*      */   {
/*  658 */     Attribute localAttribute = styleConstantsKeyToCSSKey(paramStyleConstants);
/*  659 */     if (localAttribute != null) {
/*  660 */       CssValue localCssValue = (CssValue)this.valueConvertor.get(localAttribute);
/*  661 */       return localCssValue.fromStyleConstants(paramStyleConstants, paramObject);
/*      */     }
/*  663 */     return null;
/*      */   }
/*      */ 
/*      */   Object cssValueToStyleConstantsValue(StyleConstants paramStyleConstants, Object paramObject)
/*      */   {
/*  671 */     if ((paramObject instanceof CssValue)) {
/*  672 */       return ((CssValue)paramObject).toStyleConstants(paramStyleConstants, null);
/*      */     }
/*  674 */     return null;
/*      */   }
/*      */ 
/*      */   Font getFont(StyleContext paramStyleContext, AttributeSet paramAttributeSet, int paramInt, StyleSheet paramStyleSheet)
/*      */   {
/*  684 */     paramStyleSheet = getStyleSheet(paramStyleSheet);
/*  685 */     int i = getFontSize(paramAttributeSet, paramInt, paramStyleSheet);
/*      */ 
/*  691 */     StringValue localStringValue = (StringValue)paramAttributeSet.getAttribute(Attribute.VERTICAL_ALIGN);
/*      */ 
/*  693 */     if (localStringValue != null) {
/*  694 */       localObject1 = localStringValue.toString();
/*  695 */       if ((((String)localObject1).indexOf("sup") >= 0) || (((String)localObject1).indexOf("sub") >= 0))
/*      */       {
/*  697 */         i -= 2;
/*      */       }
/*      */     }
/*      */ 
/*  701 */     Object localObject1 = (FontFamily)paramAttributeSet.getAttribute(Attribute.FONT_FAMILY);
/*      */ 
/*  703 */     String str = localObject1 != null ? ((FontFamily)localObject1).getValue() : "SansSerif";
/*      */ 
/*  705 */     int j = 0;
/*  706 */     FontWeight localFontWeight = (FontWeight)paramAttributeSet.getAttribute(Attribute.FONT_WEIGHT);
/*      */ 
/*  708 */     if ((localFontWeight != null) && (localFontWeight.getValue() > 400)) {
/*  709 */       j |= 1;
/*      */     }
/*  711 */     Object localObject2 = paramAttributeSet.getAttribute(Attribute.FONT_STYLE);
/*  712 */     if ((localObject2 != null) && (localObject2.toString().indexOf("italic") >= 0)) {
/*  713 */       j |= 2;
/*      */     }
/*  715 */     if (str.equalsIgnoreCase("monospace")) {
/*  716 */       str = "Monospaced";
/*      */     }
/*  718 */     Font localFont = paramStyleContext.getFont(str, j, i);
/*  719 */     if ((localFont == null) || ((localFont.getFamily().equals("Dialog")) && (!str.equalsIgnoreCase("Dialog"))))
/*      */     {
/*  722 */       str = "SansSerif";
/*  723 */       localFont = paramStyleContext.getFont(str, j, i);
/*      */     }
/*  725 */     return localFont;
/*      */   }
/*      */ 
/*      */   static int getFontSize(AttributeSet paramAttributeSet, int paramInt, StyleSheet paramStyleSheet)
/*      */   {
/*  731 */     FontSize localFontSize = (FontSize)paramAttributeSet.getAttribute(Attribute.FONT_SIZE);
/*      */ 
/*  734 */     return localFontSize != null ? localFontSize.getValue(paramAttributeSet, paramStyleSheet) : paramInt;
/*      */   }
/*      */ 
/*      */   Color getColor(AttributeSet paramAttributeSet, Attribute paramAttribute)
/*      */   {
/*  749 */     ColorValue localColorValue = (ColorValue)paramAttributeSet.getAttribute(paramAttribute);
/*  750 */     if (localColorValue != null) {
/*  751 */       return localColorValue.getValue();
/*      */     }
/*  753 */     return null;
/*      */   }
/*      */ 
/*      */   float getPointSize(String paramString, StyleSheet paramStyleSheet)
/*      */   {
/*  764 */     paramStyleSheet = getStyleSheet(paramStyleSheet);
/*  765 */     if (paramString != null)
/*      */     {
/*      */       int i;
/*  766 */       if (paramString.startsWith("+")) {
/*  767 */         i = Integer.valueOf(paramString.substring(1)).intValue();
/*  768 */         return getPointSize(this.baseFontSize + i, paramStyleSheet);
/*  769 */       }if (paramString.startsWith("-")) {
/*  770 */         i = -Integer.valueOf(paramString.substring(1)).intValue();
/*  771 */         return getPointSize(this.baseFontSize + i, paramStyleSheet);
/*      */       }
/*  773 */       int j = Integer.valueOf(paramString).intValue();
/*  774 */       return getPointSize(j, paramStyleSheet);
/*      */     }
/*      */ 
/*  777 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   float getLength(AttributeSet paramAttributeSet, Attribute paramAttribute, StyleSheet paramStyleSheet)
/*      */   {
/*  785 */     paramStyleSheet = getStyleSheet(paramStyleSheet);
/*  786 */     LengthValue localLengthValue = (LengthValue)paramAttributeSet.getAttribute(paramAttribute);
/*  787 */     boolean bool = paramStyleSheet == null ? false : paramStyleSheet.isW3CLengthUnits();
/*  788 */     float f = localLengthValue != null ? localLengthValue.getValue(bool) : 0.0F;
/*  789 */     return f;
/*      */   }
/*      */ 
/*      */   AttributeSet translateHTMLToCSS(AttributeSet paramAttributeSet)
/*      */   {
/*  802 */     SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*  803 */     Element localElement = (Element)paramAttributeSet;
/*  804 */     HTML.Tag localTag = getHTMLTag(paramAttributeSet);
/*      */     Object localObject;
/*  805 */     if ((localTag == HTML.Tag.TD) || (localTag == HTML.Tag.TH))
/*      */     {
/*  807 */       localObject = localElement.getParentElement().getParentElement().getAttributes();
/*      */ 
/*  810 */       int i = getTableBorder((AttributeSet)localObject);
/*  811 */       if (i > 0)
/*      */       {
/*  813 */         translateAttribute(HTML.Attribute.BORDER, "1", localSimpleAttributeSet);
/*      */       }
/*  815 */       String str = (String)((AttributeSet)localObject).getAttribute(HTML.Attribute.CELLPADDING);
/*  816 */       if (str != null) {
/*  817 */         LengthValue localLengthValue = (LengthValue)getInternalCSSValue(Attribute.PADDING_TOP, str);
/*      */ 
/*  819 */         localLengthValue.span = (localLengthValue.span < 0.0F ? 0.0F : localLengthValue.span);
/*  820 */         localSimpleAttributeSet.addAttribute(Attribute.PADDING_TOP, localLengthValue);
/*  821 */         localSimpleAttributeSet.addAttribute(Attribute.PADDING_BOTTOM, localLengthValue);
/*  822 */         localSimpleAttributeSet.addAttribute(Attribute.PADDING_LEFT, localLengthValue);
/*  823 */         localSimpleAttributeSet.addAttribute(Attribute.PADDING_RIGHT, localLengthValue);
/*      */       }
/*      */     }
/*  826 */     if (localElement.isLeaf())
/*  827 */       translateEmbeddedAttributes(paramAttributeSet, localSimpleAttributeSet);
/*      */     else {
/*  829 */       translateAttributes(localTag, paramAttributeSet, localSimpleAttributeSet);
/*      */     }
/*  831 */     if (localTag == HTML.Tag.CAPTION)
/*      */     {
/*  835 */       localObject = paramAttributeSet.getAttribute(HTML.Attribute.ALIGN);
/*  836 */       if ((localObject != null) && ((localObject.equals("top")) || (localObject.equals("bottom")))) {
/*  837 */         localSimpleAttributeSet.addAttribute(Attribute.CAPTION_SIDE, localObject);
/*  838 */         localSimpleAttributeSet.removeAttribute(Attribute.TEXT_ALIGN);
/*      */       } else {
/*  840 */         localObject = paramAttributeSet.getAttribute(HTML.Attribute.VALIGN);
/*  841 */         if (localObject != null) {
/*  842 */           localSimpleAttributeSet.addAttribute(Attribute.CAPTION_SIDE, localObject);
/*      */         }
/*      */       }
/*      */     }
/*  846 */     return localSimpleAttributeSet;
/*      */   }
/*      */ 
/*      */   private static int getTableBorder(AttributeSet paramAttributeSet) {
/*  850 */     String str = (String)paramAttributeSet.getAttribute(HTML.Attribute.BORDER);
/*      */ 
/*  852 */     if ((str == "#DEFAULT") || ("".equals(str)))
/*      */     {
/*  854 */       return 1;
/*      */     }
/*      */     try
/*      */     {
/*  858 */       return Integer.parseInt(str); } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*  860 */     return 0;
/*      */   }
/*      */ 
/*      */   public static Attribute[] getAllAttributeKeys()
/*      */   {
/* 1040 */     Attribute[] arrayOfAttribute = new Attribute[Attribute.allAttributes.length];
/* 1041 */     System.arraycopy(Attribute.allAttributes, 0, arrayOfAttribute, 0, Attribute.allAttributes.length);
/* 1042 */     return arrayOfAttribute;
/*      */   }
/*      */ 
/*      */   public static final Attribute getAttribute(String paramString)
/*      */   {
/* 1057 */     return (Attribute)attributeMap.get(paramString);
/*      */   }
/*      */ 
/*      */   static final Value getValue(String paramString)
/*      */   {
/* 1073 */     return (Value)valueMap.get(paramString);
/*      */   }
/*      */ 
/*      */   static URL getURL(URL paramURL, String paramString)
/*      */   {
/* 1088 */     if (paramString == null) {
/* 1089 */       return null;
/*      */     }
/* 1091 */     if ((paramString.startsWith("url(")) && (paramString.endsWith(")")))
/*      */     {
/* 1093 */       paramString = paramString.substring(4, paramString.length() - 1);
/*      */     }
/*      */     try
/*      */     {
/* 1097 */       URL localURL1 = new URL(paramString);
/* 1098 */       if (localURL1 != null)
/* 1099 */         return localURL1;
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException1)
/*      */     {
/*      */     }
/* 1104 */     if (paramURL != null) {
/*      */       try
/*      */       {
/* 1107 */         return new URL(paramURL, paramString);
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException2)
/*      */       {
/*      */       }
/*      */     }
/* 1113 */     return null;
/*      */   }
/*      */ 
/*      */   static String colorToHex(Color paramColor)
/*      */   {
/* 1122 */     String str1 = "#";
/*      */ 
/* 1125 */     String str2 = Integer.toHexString(paramColor.getRed());
/* 1126 */     if (str2.length() > 2)
/* 1127 */       str2 = str2.substring(0, 2);
/* 1128 */     else if (str2.length() < 2)
/* 1129 */       str1 = str1 + "0" + str2;
/*      */     else {
/* 1131 */       str1 = str1 + str2;
/*      */     }
/*      */ 
/* 1134 */     str2 = Integer.toHexString(paramColor.getGreen());
/* 1135 */     if (str2.length() > 2)
/* 1136 */       str2 = str2.substring(0, 2);
/* 1137 */     else if (str2.length() < 2)
/* 1138 */       str1 = str1 + "0" + str2;
/*      */     else {
/* 1140 */       str1 = str1 + str2;
/*      */     }
/*      */ 
/* 1143 */     str2 = Integer.toHexString(paramColor.getBlue());
/* 1144 */     if (str2.length() > 2)
/* 1145 */       str2 = str2.substring(0, 2);
/* 1146 */     else if (str2.length() < 2)
/* 1147 */       str1 = str1 + "0" + str2;
/*      */     else {
/* 1149 */       str1 = str1 + str2;
/*      */     }
/* 1151 */     return str1;
/*      */   }
/*      */ 
/*      */   static final Color hexToColor(String paramString)
/*      */   {
/* 1161 */     int i = paramString.length();
/*      */     String str1;
/* 1162 */     if (paramString.startsWith("#"))
/* 1163 */       str1 = paramString.substring(1, Math.min(paramString.length(), 7));
/*      */     else {
/* 1165 */       str1 = paramString;
/* 1167 */     }String str2 = "0x" + str1;
/*      */     Color localColor;
/*      */     try {
/* 1170 */       localColor = Color.decode(str2);
/*      */     } catch (NumberFormatException localNumberFormatException) {
/* 1172 */       localColor = null;
/*      */     }
/* 1174 */     return localColor;
/*      */   }
/*      */ 
/*      */   static Color stringToColor(String paramString)
/*      */   {
/* 1184 */     if (paramString == null)
/* 1185 */       return null;
/*      */     Color localColor;
/* 1187 */     if (paramString.length() == 0)
/* 1188 */       localColor = Color.black;
/* 1189 */     else if (paramString.startsWith("rgb(")) {
/* 1190 */       localColor = parseRGB(paramString);
/*      */     }
/* 1192 */     else if (paramString.charAt(0) == '#')
/* 1193 */       localColor = hexToColor(paramString);
/* 1194 */     else if (paramString.equalsIgnoreCase("Black"))
/* 1195 */       localColor = hexToColor("#000000");
/* 1196 */     else if (paramString.equalsIgnoreCase("Silver"))
/* 1197 */       localColor = hexToColor("#C0C0C0");
/* 1198 */     else if (paramString.equalsIgnoreCase("Gray"))
/* 1199 */       localColor = hexToColor("#808080");
/* 1200 */     else if (paramString.equalsIgnoreCase("White"))
/* 1201 */       localColor = hexToColor("#FFFFFF");
/* 1202 */     else if (paramString.equalsIgnoreCase("Maroon"))
/* 1203 */       localColor = hexToColor("#800000");
/* 1204 */     else if (paramString.equalsIgnoreCase("Red"))
/* 1205 */       localColor = hexToColor("#FF0000");
/* 1206 */     else if (paramString.equalsIgnoreCase("Purple"))
/* 1207 */       localColor = hexToColor("#800080");
/* 1208 */     else if (paramString.equalsIgnoreCase("Fuchsia"))
/* 1209 */       localColor = hexToColor("#FF00FF");
/* 1210 */     else if (paramString.equalsIgnoreCase("Green"))
/* 1211 */       localColor = hexToColor("#008000");
/* 1212 */     else if (paramString.equalsIgnoreCase("Lime"))
/* 1213 */       localColor = hexToColor("#00FF00");
/* 1214 */     else if (paramString.equalsIgnoreCase("Olive"))
/* 1215 */       localColor = hexToColor("#808000");
/* 1216 */     else if (paramString.equalsIgnoreCase("Yellow"))
/* 1217 */       localColor = hexToColor("#FFFF00");
/* 1218 */     else if (paramString.equalsIgnoreCase("Navy"))
/* 1219 */       localColor = hexToColor("#000080");
/* 1220 */     else if (paramString.equalsIgnoreCase("Blue"))
/* 1221 */       localColor = hexToColor("#0000FF");
/* 1222 */     else if (paramString.equalsIgnoreCase("Teal"))
/* 1223 */       localColor = hexToColor("#008080");
/* 1224 */     else if (paramString.equalsIgnoreCase("Aqua"))
/* 1225 */       localColor = hexToColor("#00FFFF");
/* 1226 */     else if (paramString.equalsIgnoreCase("Orange"))
/* 1227 */       localColor = hexToColor("#FF8000");
/*      */     else
/* 1229 */       localColor = hexToColor(paramString);
/* 1230 */     return localColor;
/*      */   }
/*      */ 
/*      */   private static Color parseRGB(String paramString)
/*      */   {
/* 1241 */     int[] arrayOfInt = new int[1];
/*      */ 
/* 1243 */     arrayOfInt[0] = 4;
/* 1244 */     int i = getColorComponent(paramString, arrayOfInt);
/* 1245 */     int j = getColorComponent(paramString, arrayOfInt);
/* 1246 */     int k = getColorComponent(paramString, arrayOfInt);
/*      */ 
/* 1248 */     return new Color(i, j, k);
/*      */   }
/*      */ 
/*      */   private static int getColorComponent(String paramString, int[] paramArrayOfInt)
/*      */   {
/* 1258 */     int i = paramString.length();
/*      */     char c;
/* 1263 */     while ((paramArrayOfInt[0] < i) && ((c = paramString.charAt(paramArrayOfInt[0])) != '-') && (!Character.isDigit(c)) && (c != '.')) {
/* 1264 */       paramArrayOfInt[0] += 1;
/*      */     }
/*      */ 
/* 1267 */     int j = paramArrayOfInt[0];
/*      */ 
/* 1269 */     if ((j < i) && (paramString.charAt(paramArrayOfInt[0]) == '-')) {
/* 1270 */       paramArrayOfInt[0] += 1;
/*      */     }
/* 1272 */     while ((paramArrayOfInt[0] < i) && (Character.isDigit(paramString.charAt(paramArrayOfInt[0]))))
/*      */     {
/* 1274 */       paramArrayOfInt[0] += 1;
/*      */     }
/* 1276 */     if ((paramArrayOfInt[0] < i) && (paramString.charAt(paramArrayOfInt[0]) == '.'))
/*      */     {
/* 1278 */       paramArrayOfInt[0] += 1;
/* 1279 */       while ((paramArrayOfInt[0] < i) && (Character.isDigit(paramString.charAt(paramArrayOfInt[0]))))
/*      */       {
/* 1281 */         paramArrayOfInt[0] += 1;
/*      */       }
/*      */     }
/* 1284 */     if (j != paramArrayOfInt[0])
/*      */       try {
/* 1286 */         float f = Float.parseFloat(paramString.substring(j, paramArrayOfInt[0]));
/*      */ 
/* 1289 */         if ((paramArrayOfInt[0] < i) && (paramString.charAt(paramArrayOfInt[0]) == '%')) {
/* 1290 */           paramArrayOfInt[0] += 1;
/* 1291 */           f = f * 255.0F / 100.0F;
/*      */         }
/* 1293 */         return Math.min(255, Math.max(0, (int)f));
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException)
/*      */       {
/*      */       }
/* 1298 */     return 0;
/*      */   }
/*      */ 
/*      */   static int getIndexOfSize(float paramFloat, int[] paramArrayOfInt) {
/* 1302 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/* 1303 */       if (paramFloat <= paramArrayOfInt[i])
/* 1304 */         return i + 1;
/* 1305 */     return paramArrayOfInt.length;
/*      */   }
/*      */ 
/*      */   static int getIndexOfSize(float paramFloat, StyleSheet paramStyleSheet) {
/* 1309 */     int[] arrayOfInt = paramStyleSheet != null ? paramStyleSheet.getSizeMap() : StyleSheet.sizeMapDefault;
/*      */ 
/* 1311 */     return getIndexOfSize(paramFloat, arrayOfInt);
/*      */   }
/*      */ 
/*      */   static String[] parseStrings(String paramString)
/*      */   {
/* 1321 */     int k = paramString == null ? 0 : paramString.length();
/* 1322 */     Vector localVector = new Vector(4);
/*      */ 
/* 1324 */     int i = 0;
/* 1325 */     while (i < k)
/*      */     {
/* 1327 */       while ((i < k) && (Character.isWhitespace(paramString.charAt(i))))
/*      */       {
/* 1329 */         i++;
/*      */       }
/* 1331 */       int j = i;
/* 1332 */       while ((i < k) && (!Character.isWhitespace(paramString.charAt(i))))
/*      */       {
/* 1334 */         i++;
/*      */       }
/* 1336 */       if (j != i) {
/* 1337 */         localVector.addElement(paramString.substring(j, i));
/*      */       }
/* 1339 */       i++;
/*      */     }
/* 1341 */     String[] arrayOfString = new String[localVector.size()];
/* 1342 */     localVector.copyInto(arrayOfString);
/* 1343 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   float getPointSize(int paramInt, StyleSheet paramStyleSheet)
/*      */   {
/* 1351 */     paramStyleSheet = getStyleSheet(paramStyleSheet);
/* 1352 */     int[] arrayOfInt = paramStyleSheet != null ? paramStyleSheet.getSizeMap() : StyleSheet.sizeMapDefault;
/*      */ 
/* 1354 */     paramInt--;
/* 1355 */     if (paramInt < 0)
/* 1356 */       return arrayOfInt[0];
/* 1357 */     if (paramInt > arrayOfInt.length - 1) {
/* 1358 */       return arrayOfInt[(arrayOfInt.length - 1)];
/*      */     }
/* 1360 */     return arrayOfInt[paramInt];
/*      */   }
/*      */ 
/*      */   private void translateEmbeddedAttributes(AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1366 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1367 */     if (paramAttributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.HR)
/*      */     {
/* 1370 */       translateAttributes(HTML.Tag.HR, paramAttributeSet, paramMutableAttributeSet);
/*      */     }
/* 1372 */     while (localEnumeration.hasMoreElements()) {
/* 1373 */       Object localObject1 = localEnumeration.nextElement();
/* 1374 */       if ((localObject1 instanceof HTML.Tag)) {
/* 1375 */         HTML.Tag localTag = (HTML.Tag)localObject1;
/* 1376 */         Object localObject2 = paramAttributeSet.getAttribute(localTag);
/* 1377 */         if ((localObject2 != null) && ((localObject2 instanceof AttributeSet)))
/* 1378 */           translateAttributes(localTag, (AttributeSet)localObject2, paramMutableAttributeSet);
/*      */       }
/* 1380 */       else if ((localObject1 instanceof Attribute)) {
/* 1381 */         paramMutableAttributeSet.addAttribute(localObject1, paramAttributeSet.getAttribute(localObject1));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void translateAttributes(HTML.Tag paramTag, AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1389 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1390 */     while (localEnumeration.hasMoreElements()) {
/* 1391 */       Object localObject1 = localEnumeration.nextElement();
/*      */ 
/* 1393 */       if ((localObject1 instanceof HTML.Attribute)) {
/* 1394 */         HTML.Attribute localAttribute = (HTML.Attribute)localObject1;
/*      */ 
/* 1402 */         if (localAttribute == HTML.Attribute.ALIGN) {
/* 1403 */           String str = (String)paramAttributeSet.getAttribute(HTML.Attribute.ALIGN);
/* 1404 */           if (str != null) {
/* 1405 */             Attribute localAttribute1 = getCssAlignAttribute(paramTag, paramAttributeSet);
/* 1406 */             if (localAttribute1 != null) {
/* 1407 */               Object localObject2 = getCssValue(localAttribute1, str);
/* 1408 */               if (localObject2 != null) {
/* 1409 */                 paramMutableAttributeSet.addAttribute(localAttribute1, localObject2);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1414 */         else if ((localAttribute != HTML.Attribute.SIZE) || (isHTMLFontTag(paramTag)))
/*      */         {
/* 1419 */           if ((paramTag == HTML.Tag.TABLE) && (localAttribute == HTML.Attribute.BORDER)) {
/* 1420 */             int i = getTableBorder(paramAttributeSet);
/*      */ 
/* 1422 */             if (i > 0)
/* 1423 */               translateAttribute(HTML.Attribute.BORDER, Integer.toString(i), paramMutableAttributeSet);
/*      */           }
/*      */           else {
/* 1426 */             translateAttribute(localAttribute, (String)paramAttributeSet.getAttribute(localAttribute), paramMutableAttributeSet);
/*      */           }
/*      */         }
/* 1429 */       } else if ((localObject1 instanceof Attribute)) {
/* 1430 */         paramMutableAttributeSet.addAttribute(localObject1, paramAttributeSet.getAttribute(localObject1));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void translateAttribute(HTML.Attribute paramAttribute, String paramString, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1442 */     Attribute[] arrayOfAttribute1 = getCssAttribute(paramAttribute);
/*      */ 
/* 1444 */     if ((arrayOfAttribute1 == null) || (paramString == null)) {
/* 1445 */       return;
/*      */     }
/* 1447 */     for (Attribute localAttribute : arrayOfAttribute1) {
/* 1448 */       Object localObject = getCssValue(localAttribute, paramString);
/* 1449 */       if (localObject != null)
/* 1450 */         paramMutableAttributeSet.addAttribute(localAttribute, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   Object getCssValue(Attribute paramAttribute, String paramString)
/*      */   {
/* 1464 */     CssValue localCssValue = (CssValue)this.valueConvertor.get(paramAttribute);
/* 1465 */     Object localObject = localCssValue.parseHtmlValue(paramString);
/* 1466 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Attribute[] getCssAttribute(HTML.Attribute paramAttribute)
/*      */   {
/* 1476 */     return (Attribute[])htmlAttrToCssAttrMap.get(paramAttribute);
/*      */   }
/*      */ 
/*      */   private Attribute getCssAlignAttribute(HTML.Tag paramTag, AttributeSet paramAttributeSet)
/*      */   {
/* 1492 */     return Attribute.TEXT_ALIGN;
/*      */   }
/*      */ 
/*      */   private HTML.Tag getHTMLTag(AttributeSet paramAttributeSet)
/*      */   {
/* 1517 */     Object localObject = paramAttributeSet.getAttribute(StyleConstants.NameAttribute);
/* 1518 */     if ((localObject instanceof HTML.Tag)) {
/* 1519 */       HTML.Tag localTag = (HTML.Tag)localObject;
/* 1520 */       return localTag;
/*      */     }
/* 1522 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean isHTMLFontTag(HTML.Tag paramTag)
/*      */   {
/* 1527 */     return (paramTag != null) && ((paramTag == HTML.Tag.FONT) || (paramTag == HTML.Tag.BASEFONT));
/*      */   }
/*      */ 
/*      */   private boolean isFloater(String paramString)
/*      */   {
/* 1532 */     return (paramString.equals("left")) || (paramString.equals("right"));
/*      */   }
/*      */ 
/*      */   private boolean validTextAlignValue(String paramString) {
/* 1536 */     return (isFloater(paramString)) || (paramString.equals("center"));
/*      */   }
/*      */ 
/*      */   static SizeRequirements calculateTiledRequirements(LayoutIterator paramLayoutIterator, SizeRequirements paramSizeRequirements)
/*      */   {
/* 3103 */     long l1 = 0L;
/* 3104 */     long l2 = 0L;
/* 3105 */     long l3 = 0L;
/* 3106 */     int i = 0;
/* 3107 */     int j = 0;
/* 3108 */     int k = paramLayoutIterator.getCount();
/* 3109 */     for (int m = 0; m < k; m++) {
/* 3110 */       paramLayoutIterator.setIndex(m);
/* 3111 */       int n = i;
/* 3112 */       int i1 = (int)paramLayoutIterator.getLeadingCollapseSpan();
/* 3113 */       j += Math.max(n, i1);
/* 3114 */       l3 += (int)paramLayoutIterator.getPreferredSpan(0.0F);
/* 3115 */       l1 = ()((float)l1 + paramLayoutIterator.getMinimumSpan(0.0F));
/* 3116 */       l2 = ()((float)l2 + paramLayoutIterator.getMaximumSpan(0.0F));
/*      */ 
/* 3118 */       i = (int)paramLayoutIterator.getTrailingCollapseSpan();
/*      */     }
/* 3120 */     j += i;
/* 3121 */     j = (int)(j + 2.0F * paramLayoutIterator.getBorderWidth());
/*      */ 
/* 3124 */     l1 += j;
/* 3125 */     l3 += j;
/* 3126 */     l2 += j;
/*      */ 
/* 3129 */     if (paramSizeRequirements == null) {
/* 3130 */       paramSizeRequirements = new SizeRequirements();
/*      */     }
/* 3132 */     paramSizeRequirements.minimum = (l1 > 2147483647L ? 2147483647 : (int)l1);
/* 3133 */     paramSizeRequirements.preferred = (l3 > 2147483647L ? 2147483647 : (int)l3);
/* 3134 */     paramSizeRequirements.maximum = (l2 > 2147483647L ? 2147483647 : (int)l2);
/* 3135 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   static void calculateTiledLayout(LayoutIterator paramLayoutIterator, int paramInt)
/*      */   {
/* 3150 */     long l1 = 0L;
/*      */ 
/* 3152 */     int i = 0;
/* 3153 */     int j = 0;
/* 3154 */     int k = paramLayoutIterator.getCount();
/* 3155 */     int m = 3;
/*      */ 
/* 3157 */     long[] arrayOfLong1 = new long[m];
/*      */ 
/* 3159 */     long[] arrayOfLong2 = new long[m];
/*      */ 
/* 3161 */     for (int n = 0; n < m; n++)
/*      */     {
/*      */       long tmp50_49 = 0L; arrayOfLong2[n] = tmp50_49; arrayOfLong1[n] = tmp50_49;
/*      */     }
/* 3164 */     for (n = 0; n < k; n++) {
/* 3165 */       paramLayoutIterator.setIndex(n);
/* 3166 */       int i1 = i;
/* 3167 */       int i2 = (int)paramLayoutIterator.getLeadingCollapseSpan();
/*      */ 
/* 3169 */       paramLayoutIterator.setOffset(Math.max(i1, i2));
/* 3170 */       j += paramLayoutIterator.getOffset();
/*      */ 
/* 3172 */       long l2 = ()paramLayoutIterator.getPreferredSpan(paramInt);
/* 3173 */       paramLayoutIterator.setSpan((int)l2);
/* 3174 */       l1 += l2;
/* 3175 */       arrayOfLong1[paramLayoutIterator.getAdjustmentWeight()] += ()paramLayoutIterator.getMaximumSpan(paramInt) - l2;
/*      */ 
/* 3177 */       arrayOfLong2[paramLayoutIterator.getAdjustmentWeight()] += l2 - ()paramLayoutIterator.getMinimumSpan(paramInt);
/*      */ 
/* 3179 */       i = (int)paramLayoutIterator.getTrailingCollapseSpan();
/*      */     }
/* 3181 */     j += i;
/* 3182 */     j = (int)(j + 2.0F * paramLayoutIterator.getBorderWidth());
/*      */ 
/* 3184 */     for (n = 1; n < m; n++) {
/* 3185 */       arrayOfLong1[n] += arrayOfLong1[(n - 1)];
/* 3186 */       arrayOfLong2[n] += arrayOfLong2[(n - 1)];
/*      */     }
/*      */ 
/* 3196 */     n = paramInt - j;
/* 3197 */     long l3 = n - l1;
/* 3198 */     long[] arrayOfLong3 = l3 > 0L ? arrayOfLong1 : arrayOfLong2;
/* 3199 */     l3 = Math.abs(l3);
/* 3200 */     for (int i3 = 0; 
/* 3201 */       i3 <= 2; 
/* 3202 */       i3++)
/*      */     {
/* 3205 */       if (arrayOfLong3[i3] >= l3) {
/*      */         break;
/*      */       }
/*      */     }
/* 3209 */     float f1 = 0.0F;
/* 3210 */     if (i3 <= 2) {
/* 3211 */       l3 -= (i3 > 0 ? arrayOfLong3[(i3 - 1)] : 0L);
/*      */ 
/* 3213 */       if (l3 != 0L) {
/* 3214 */         float f2 = (float)(arrayOfLong3[i3] - (i3 > 0 ? arrayOfLong3[(i3 - 1)] : 0L));
/*      */ 
/* 3219 */         f1 = (float)l3 / f2;
/*      */       }
/*      */     }
/*      */ 
/* 3223 */     int i4 = (int)paramLayoutIterator.getBorderWidth();
/* 3224 */     for (int i5 = 0; i5 < k; i5++) {
/* 3225 */       paramLayoutIterator.setIndex(i5);
/* 3226 */       paramLayoutIterator.setOffset(paramLayoutIterator.getOffset() + i4);
/* 3227 */       if (paramLayoutIterator.getAdjustmentWeight() < i3) {
/* 3228 */         paramLayoutIterator.setSpan((int)(n > l1 ? Math.floor(paramLayoutIterator.getMaximumSpan(paramInt)) : Math.ceil(paramLayoutIterator.getMinimumSpan(paramInt))));
/*      */       }
/* 3234 */       else if (paramLayoutIterator.getAdjustmentWeight() == i3) {
/* 3235 */         i6 = n > l1 ? (int)paramLayoutIterator.getMaximumSpan(paramInt) - paramLayoutIterator.getSpan() : paramLayoutIterator.getSpan() - (int)paramLayoutIterator.getMinimumSpan(paramInt);
/*      */ 
/* 3238 */         i7 = (int)Math.floor(f1 * i6);
/* 3239 */         paramLayoutIterator.setSpan(paramLayoutIterator.getSpan() + (n > l1 ? i7 : -i7));
/*      */       }
/*      */ 
/* 3242 */       i4 = (int)Math.min(paramLayoutIterator.getOffset() + paramLayoutIterator.getSpan(), 2147483647L);
/*      */     }
/*      */ 
/* 3248 */     i5 = paramInt - i4 - (int)paramLayoutIterator.getTrailingCollapseSpan() - (int)paramLayoutIterator.getBorderWidth();
/*      */ 
/* 3251 */     int i6 = i5 > 0 ? 1 : -1;
/* 3252 */     i5 *= i6;
/*      */ 
/* 3254 */     int i7 = 1;
/* 3255 */     while ((i5 > 0) && (i7 != 0))
/*      */     {
/* 3257 */       i7 = 0;
/* 3258 */       int i8 = 0;
/*      */ 
/* 3260 */       for (int i9 = 0; i9 < k; i9++) {
/* 3261 */         paramLayoutIterator.setIndex(i9);
/* 3262 */         paramLayoutIterator.setOffset(paramLayoutIterator.getOffset() + i8);
/* 3263 */         int i10 = paramLayoutIterator.getSpan();
/* 3264 */         if (i5 > 0) {
/* 3265 */           int i11 = i6 > 0 ? (int)Math.floor(paramLayoutIterator.getMaximumSpan(paramInt)) - i10 : i10 - (int)Math.ceil(paramLayoutIterator.getMinimumSpan(paramInt));
/*      */ 
/* 3268 */           if (i11 >= 1) {
/* 3269 */             i7 = 1;
/* 3270 */             paramLayoutIterator.setSpan(i10 + i6);
/* 3271 */             i8 += i6;
/* 3272 */             i5--;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 3322 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 3325 */     Enumeration localEnumeration = this.valueConvertor.keys();
/* 3326 */     paramObjectOutputStream.writeInt(this.valueConvertor.size());
/* 3327 */     if (localEnumeration != null)
/* 3328 */       while (localEnumeration.hasMoreElements()) {
/* 3329 */         Object localObject1 = localEnumeration.nextElement();
/* 3330 */         Object localObject2 = this.valueConvertor.get(localObject1);
/* 3331 */         if ((!(localObject1 instanceof Serializable)) && ((localObject1 = StyleContext.getStaticAttributeKey(localObject1)) == null))
/*      */         {
/* 3334 */           localObject1 = null;
/* 3335 */           localObject2 = null;
/*      */         }
/* 3337 */         else if ((!(localObject2 instanceof Serializable)) && ((localObject2 = StyleContext.getStaticAttributeKey(localObject2)) == null))
/*      */         {
/* 3340 */           localObject1 = null;
/* 3341 */           localObject2 = null;
/*      */         }
/* 3343 */         paramObjectOutputStream.writeObject(localObject1);
/* 3344 */         paramObjectOutputStream.writeObject(localObject2);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 3352 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 3354 */     int i = paramObjectInputStream.readInt();
/* 3355 */     this.valueConvertor = new Hashtable(Math.max(1, i));
/* 3356 */     while (i-- > 0) {
/* 3357 */       Object localObject1 = paramObjectInputStream.readObject();
/* 3358 */       Object localObject2 = paramObjectInputStream.readObject();
/* 3359 */       Object localObject3 = StyleContext.getStaticAttribute(localObject1);
/* 3360 */       if (localObject3 != null) {
/* 3361 */         localObject1 = localObject3;
/*      */       }
/* 3363 */       Object localObject4 = StyleContext.getStaticAttribute(localObject2);
/* 3364 */       if (localObject4 != null) {
/* 3365 */         localObject2 = localObject4;
/*      */       }
/* 3367 */       if ((localObject1 != null) && (localObject2 != null))
/* 3368 */         this.valueConvertor.put(localObject1, localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private StyleSheet getStyleSheet(StyleSheet paramStyleSheet)
/*      */   {
/* 3386 */     if (paramStyleSheet != null) {
/* 3387 */       this.styleSheet = paramStyleSheet;
/*      */     }
/* 3389 */     return this.styleSheet;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  892 */     for (int i = 0; i < Attribute.allAttributes.length; i++) {
/*  893 */       attributeMap.put(Attribute.allAttributes[i].toString(), Attribute.allAttributes[i]);
/*      */     }
/*      */ 
/*  897 */     for (i = 0; i < Value.allValues.length; i++) {
/*  898 */       valueMap.put(Value.allValues[i].toString(), Value.allValues[i]); } 
/*      */ htmlAttrToCssAttrMap.put(HTML.Attribute.COLOR, new Attribute[] { Attribute.COLOR });
/*      */ 
/*  904 */     htmlAttrToCssAttrMap.put(HTML.Attribute.TEXT, new Attribute[] { Attribute.COLOR });
/*      */ 
/*  906 */     htmlAttrToCssAttrMap.put(HTML.Attribute.CLEAR, new Attribute[] { Attribute.CLEAR });
/*      */ 
/*  908 */     htmlAttrToCssAttrMap.put(HTML.Attribute.BACKGROUND, new Attribute[] { Attribute.BACKGROUND_IMAGE });
/*      */ 
/*  910 */     htmlAttrToCssAttrMap.put(HTML.Attribute.BGCOLOR, new Attribute[] { Attribute.BACKGROUND_COLOR });
/*      */ 
/*  912 */     htmlAttrToCssAttrMap.put(HTML.Attribute.WIDTH, new Attribute[] { Attribute.WIDTH });
/*      */ 
/*  914 */     htmlAttrToCssAttrMap.put(HTML.Attribute.HEIGHT, new Attribute[] { Attribute.HEIGHT });
/*      */ 
/*  916 */     htmlAttrToCssAttrMap.put(HTML.Attribute.BORDER, new Attribute[] { Attribute.BORDER_TOP_WIDTH, Attribute.BORDER_RIGHT_WIDTH, Attribute.BORDER_BOTTOM_WIDTH, Attribute.BORDER_LEFT_WIDTH });
/*      */ 
/*  918 */     htmlAttrToCssAttrMap.put(HTML.Attribute.CELLPADDING, new Attribute[] { Attribute.PADDING });
/*      */ 
/*  920 */     htmlAttrToCssAttrMap.put(HTML.Attribute.CELLSPACING, new Attribute[] { Attribute.BORDER_SPACING });
/*      */ 
/*  922 */     htmlAttrToCssAttrMap.put(HTML.Attribute.MARGINWIDTH, new Attribute[] { Attribute.MARGIN_LEFT, Attribute.MARGIN_RIGHT });
/*      */ 
/*  925 */     htmlAttrToCssAttrMap.put(HTML.Attribute.MARGINHEIGHT, new Attribute[] { Attribute.MARGIN_TOP, Attribute.MARGIN_BOTTOM });
/*      */ 
/*  928 */     htmlAttrToCssAttrMap.put(HTML.Attribute.HSPACE, new Attribute[] { Attribute.PADDING_LEFT, Attribute.PADDING_RIGHT });
/*      */ 
/*  931 */     htmlAttrToCssAttrMap.put(HTML.Attribute.VSPACE, new Attribute[] { Attribute.PADDING_BOTTOM, Attribute.PADDING_TOP });
/*      */ 
/*  934 */     htmlAttrToCssAttrMap.put(HTML.Attribute.FACE, new Attribute[] { Attribute.FONT_FAMILY });
/*      */ 
/*  936 */     htmlAttrToCssAttrMap.put(HTML.Attribute.SIZE, new Attribute[] { Attribute.FONT_SIZE });
/*      */ 
/*  938 */     htmlAttrToCssAttrMap.put(HTML.Attribute.VALIGN, new Attribute[] { Attribute.VERTICAL_ALIGN });
/*      */ 
/*  940 */     htmlAttrToCssAttrMap.put(HTML.Attribute.ALIGN, new Attribute[] { Attribute.VERTICAL_ALIGN, Attribute.TEXT_ALIGN, Attribute.FLOAT });
/*      */ 
/*  944 */     htmlAttrToCssAttrMap.put(HTML.Attribute.TYPE, new Attribute[] { Attribute.LIST_STYLE_TYPE });
/*      */ 
/*  946 */     htmlAttrToCssAttrMap.put(HTML.Attribute.NOWRAP, new Attribute[] { Attribute.WHITE_SPACE });
/*      */ 
/*  950 */     styleConstantToCssMap.put(StyleConstants.FontFamily, Attribute.FONT_FAMILY);
/*      */ 
/*  952 */     styleConstantToCssMap.put(StyleConstants.FontSize, Attribute.FONT_SIZE);
/*      */ 
/*  954 */     styleConstantToCssMap.put(StyleConstants.Bold, Attribute.FONT_WEIGHT);
/*      */ 
/*  956 */     styleConstantToCssMap.put(StyleConstants.Italic, Attribute.FONT_STYLE);
/*      */ 
/*  958 */     styleConstantToCssMap.put(StyleConstants.Underline, Attribute.TEXT_DECORATION);
/*      */ 
/*  960 */     styleConstantToCssMap.put(StyleConstants.StrikeThrough, Attribute.TEXT_DECORATION);
/*      */ 
/*  962 */     styleConstantToCssMap.put(StyleConstants.Superscript, Attribute.VERTICAL_ALIGN);
/*      */ 
/*  964 */     styleConstantToCssMap.put(StyleConstants.Subscript, Attribute.VERTICAL_ALIGN);
/*      */ 
/*  966 */     styleConstantToCssMap.put(StyleConstants.Foreground, Attribute.COLOR);
/*      */ 
/*  968 */     styleConstantToCssMap.put(StyleConstants.Background, Attribute.BACKGROUND_COLOR);
/*      */ 
/*  970 */     styleConstantToCssMap.put(StyleConstants.FirstLineIndent, Attribute.TEXT_INDENT);
/*      */ 
/*  972 */     styleConstantToCssMap.put(StyleConstants.LeftIndent, Attribute.MARGIN_LEFT);
/*      */ 
/*  974 */     styleConstantToCssMap.put(StyleConstants.RightIndent, Attribute.MARGIN_RIGHT);
/*      */ 
/*  976 */     styleConstantToCssMap.put(StyleConstants.SpaceAbove, Attribute.MARGIN_TOP);
/*      */ 
/*  978 */     styleConstantToCssMap.put(StyleConstants.SpaceBelow, Attribute.MARGIN_BOTTOM);
/*      */ 
/*  980 */     styleConstantToCssMap.put(StyleConstants.Alignment, Attribute.TEXT_ALIGN);
/*      */ 
/*  984 */     htmlValueToCssValueMap.put("disc", Value.DISC);
/*  985 */     htmlValueToCssValueMap.put("square", Value.SQUARE);
/*  986 */     htmlValueToCssValueMap.put("circle", Value.CIRCLE);
/*  987 */     htmlValueToCssValueMap.put("1", Value.DECIMAL);
/*  988 */     htmlValueToCssValueMap.put("a", Value.LOWER_ALPHA);
/*  989 */     htmlValueToCssValueMap.put("A", Value.UPPER_ALPHA);
/*  990 */     htmlValueToCssValueMap.put("i", Value.LOWER_ROMAN);
/*  991 */     htmlValueToCssValueMap.put("I", Value.UPPER_ROMAN);
/*      */ 
/*  994 */     cssValueToInternalValueMap.put("none", Value.NONE);
/*  995 */     cssValueToInternalValueMap.put("disc", Value.DISC);
/*  996 */     cssValueToInternalValueMap.put("square", Value.SQUARE);
/*  997 */     cssValueToInternalValueMap.put("circle", Value.CIRCLE);
/*  998 */     cssValueToInternalValueMap.put("decimal", Value.DECIMAL);
/*  999 */     cssValueToInternalValueMap.put("lower-roman", Value.LOWER_ROMAN);
/* 1000 */     cssValueToInternalValueMap.put("upper-roman", Value.UPPER_ROMAN);
/* 1001 */     cssValueToInternalValueMap.put("lower-alpha", Value.LOWER_ALPHA);
/* 1002 */     cssValueToInternalValueMap.put("upper-alpha", Value.UPPER_ALPHA);
/* 1003 */     cssValueToInternalValueMap.put("repeat", Value.BACKGROUND_REPEAT);
/* 1004 */     cssValueToInternalValueMap.put("no-repeat", Value.BACKGROUND_NO_REPEAT);
/*      */ 
/* 1006 */     cssValueToInternalValueMap.put("repeat-x", Value.BACKGROUND_REPEAT_X);
/*      */ 
/* 1008 */     cssValueToInternalValueMap.put("repeat-y", Value.BACKGROUND_REPEAT_Y);
/*      */ 
/* 1010 */     cssValueToInternalValueMap.put("scroll", Value.BACKGROUND_SCROLL);
/*      */ 
/* 1012 */     cssValueToInternalValueMap.put("fixed", Value.BACKGROUND_FIXED);
/*      */ 
/* 1016 */     Object localObject1 = Attribute.allAttributes;
/*      */     Object localObject4;
/*      */     try { for (localObject4 : localObject1)
/* 1019 */         StyleContext.registerStaticAttributeKey(localObject4);
/*      */     } catch (Throwable localThrowable1)
/*      */     {
/* 1022 */       localThrowable1.printStackTrace();
/*      */     }
/*      */ 
/* 1026 */     localObject1 = Value.allValues;
/*      */     try {
/* 1028 */       for (localObject4 : localObject1)
/* 1029 */         StyleContext.registerStaticAttributeKey(localObject4);
/*      */     }
/*      */     catch (Throwable localThrowable2) {
/* 1032 */       localThrowable2.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class Attribute
/*      */   {
/*      */     private String name;
/*      */     private String defaultValue;
/*      */     private boolean inherited;
/*  173 */     public static final Attribute BACKGROUND = new Attribute("background", null, false);
/*      */ 
/*  176 */     public static final Attribute BACKGROUND_ATTACHMENT = new Attribute("background-attachment", "scroll", false);
/*      */ 
/*  179 */     public static final Attribute BACKGROUND_COLOR = new Attribute("background-color", "transparent", false);
/*      */ 
/*  182 */     public static final Attribute BACKGROUND_IMAGE = new Attribute("background-image", "none", false);
/*      */ 
/*  185 */     public static final Attribute BACKGROUND_POSITION = new Attribute("background-position", null, false);
/*      */ 
/*  188 */     public static final Attribute BACKGROUND_REPEAT = new Attribute("background-repeat", "repeat", false);
/*      */ 
/*  191 */     public static final Attribute BORDER = new Attribute("border", null, false);
/*      */ 
/*  194 */     public static final Attribute BORDER_BOTTOM = new Attribute("border-bottom", null, false);
/*      */ 
/*  197 */     public static final Attribute BORDER_BOTTOM_COLOR = new Attribute("border-bottom-color", null, false);
/*      */ 
/*  200 */     public static final Attribute BORDER_BOTTOM_STYLE = new Attribute("border-bottom-style", "none", false);
/*      */ 
/*  203 */     public static final Attribute BORDER_BOTTOM_WIDTH = new Attribute("border-bottom-width", "medium", false);
/*      */ 
/*  206 */     public static final Attribute BORDER_COLOR = new Attribute("border-color", null, false);
/*      */ 
/*  209 */     public static final Attribute BORDER_LEFT = new Attribute("border-left", null, false);
/*      */ 
/*  212 */     public static final Attribute BORDER_LEFT_COLOR = new Attribute("border-left-color", null, false);
/*      */ 
/*  215 */     public static final Attribute BORDER_LEFT_STYLE = new Attribute("border-left-style", "none", false);
/*      */ 
/*  218 */     public static final Attribute BORDER_LEFT_WIDTH = new Attribute("border-left-width", "medium", false);
/*      */ 
/*  221 */     public static final Attribute BORDER_RIGHT = new Attribute("border-right", null, false);
/*      */ 
/*  224 */     public static final Attribute BORDER_RIGHT_COLOR = new Attribute("border-right-color", null, false);
/*      */ 
/*  227 */     public static final Attribute BORDER_RIGHT_STYLE = new Attribute("border-right-style", "none", false);
/*      */ 
/*  230 */     public static final Attribute BORDER_RIGHT_WIDTH = new Attribute("border-right-width", "medium", false);
/*      */ 
/*  233 */     public static final Attribute BORDER_STYLE = new Attribute("border-style", "none", false);
/*      */ 
/*  236 */     public static final Attribute BORDER_TOP = new Attribute("border-top", null, false);
/*      */ 
/*  239 */     public static final Attribute BORDER_TOP_COLOR = new Attribute("border-top-color", null, false);
/*      */ 
/*  242 */     public static final Attribute BORDER_TOP_STYLE = new Attribute("border-top-style", "none", false);
/*      */ 
/*  245 */     public static final Attribute BORDER_TOP_WIDTH = new Attribute("border-top-width", "medium", false);
/*      */ 
/*  248 */     public static final Attribute BORDER_WIDTH = new Attribute("border-width", "medium", false);
/*      */ 
/*  251 */     public static final Attribute CLEAR = new Attribute("clear", "none", false);
/*      */ 
/*  254 */     public static final Attribute COLOR = new Attribute("color", "black", true);
/*      */ 
/*  257 */     public static final Attribute DISPLAY = new Attribute("display", "block", false);
/*      */ 
/*  260 */     public static final Attribute FLOAT = new Attribute("float", "none", false);
/*      */ 
/*  263 */     public static final Attribute FONT = new Attribute("font", null, true);
/*      */ 
/*  266 */     public static final Attribute FONT_FAMILY = new Attribute("font-family", null, true);
/*      */ 
/*  269 */     public static final Attribute FONT_SIZE = new Attribute("font-size", "medium", true);
/*      */ 
/*  272 */     public static final Attribute FONT_STYLE = new Attribute("font-style", "normal", true);
/*      */ 
/*  275 */     public static final Attribute FONT_VARIANT = new Attribute("font-variant", "normal", true);
/*      */ 
/*  278 */     public static final Attribute FONT_WEIGHT = new Attribute("font-weight", "normal", true);
/*      */ 
/*  281 */     public static final Attribute HEIGHT = new Attribute("height", "auto", false);
/*      */ 
/*  284 */     public static final Attribute LETTER_SPACING = new Attribute("letter-spacing", "normal", true);
/*      */ 
/*  287 */     public static final Attribute LINE_HEIGHT = new Attribute("line-height", "normal", true);
/*      */ 
/*  290 */     public static final Attribute LIST_STYLE = new Attribute("list-style", null, true);
/*      */ 
/*  293 */     public static final Attribute LIST_STYLE_IMAGE = new Attribute("list-style-image", "none", true);
/*      */ 
/*  296 */     public static final Attribute LIST_STYLE_POSITION = new Attribute("list-style-position", "outside", true);
/*      */ 
/*  299 */     public static final Attribute LIST_STYLE_TYPE = new Attribute("list-style-type", "disc", true);
/*      */ 
/*  302 */     public static final Attribute MARGIN = new Attribute("margin", null, false);
/*      */ 
/*  305 */     public static final Attribute MARGIN_BOTTOM = new Attribute("margin-bottom", "0", false);
/*      */ 
/*  308 */     public static final Attribute MARGIN_LEFT = new Attribute("margin-left", "0", false);
/*      */ 
/*  311 */     public static final Attribute MARGIN_RIGHT = new Attribute("margin-right", "0", false);
/*      */ 
/*  319 */     static final Attribute MARGIN_LEFT_LTR = new Attribute("margin-left-ltr", Integer.toString(-2147483648), false);
/*      */ 
/*  323 */     static final Attribute MARGIN_LEFT_RTL = new Attribute("margin-left-rtl", Integer.toString(-2147483648), false);
/*      */ 
/*  327 */     static final Attribute MARGIN_RIGHT_LTR = new Attribute("margin-right-ltr", Integer.toString(-2147483648), false);
/*      */ 
/*  331 */     static final Attribute MARGIN_RIGHT_RTL = new Attribute("margin-right-rtl", Integer.toString(-2147483648), false);
/*      */ 
/*  336 */     public static final Attribute MARGIN_TOP = new Attribute("margin-top", "0", false);
/*      */ 
/*  339 */     public static final Attribute PADDING = new Attribute("padding", null, false);
/*      */ 
/*  342 */     public static final Attribute PADDING_BOTTOM = new Attribute("padding-bottom", "0", false);
/*      */ 
/*  345 */     public static final Attribute PADDING_LEFT = new Attribute("padding-left", "0", false);
/*      */ 
/*  348 */     public static final Attribute PADDING_RIGHT = new Attribute("padding-right", "0", false);
/*      */ 
/*  351 */     public static final Attribute PADDING_TOP = new Attribute("padding-top", "0", false);
/*      */ 
/*  354 */     public static final Attribute TEXT_ALIGN = new Attribute("text-align", null, true);
/*      */ 
/*  357 */     public static final Attribute TEXT_DECORATION = new Attribute("text-decoration", "none", true);
/*      */ 
/*  360 */     public static final Attribute TEXT_INDENT = new Attribute("text-indent", "0", true);
/*      */ 
/*  363 */     public static final Attribute TEXT_TRANSFORM = new Attribute("text-transform", "none", true);
/*      */ 
/*  366 */     public static final Attribute VERTICAL_ALIGN = new Attribute("vertical-align", "baseline", false);
/*      */ 
/*  369 */     public static final Attribute WORD_SPACING = new Attribute("word-spacing", "normal", true);
/*      */ 
/*  372 */     public static final Attribute WHITE_SPACE = new Attribute("white-space", "normal", true);
/*      */ 
/*  375 */     public static final Attribute WIDTH = new Attribute("width", "auto", false);
/*      */ 
/*  378 */     static final Attribute BORDER_SPACING = new Attribute("border-spacing", "0", true);
/*      */ 
/*  381 */     static final Attribute CAPTION_SIDE = new Attribute("caption-side", "left", true);
/*      */ 
/*  385 */     static final Attribute[] allAttributes = { BACKGROUND, BACKGROUND_ATTACHMENT, BACKGROUND_COLOR, BACKGROUND_IMAGE, BACKGROUND_POSITION, BACKGROUND_REPEAT, BORDER, BORDER_BOTTOM, BORDER_BOTTOM_WIDTH, BORDER_COLOR, BORDER_LEFT, BORDER_LEFT_WIDTH, BORDER_RIGHT, BORDER_RIGHT_WIDTH, BORDER_STYLE, BORDER_TOP, BORDER_TOP_WIDTH, BORDER_WIDTH, BORDER_TOP_STYLE, BORDER_RIGHT_STYLE, BORDER_BOTTOM_STYLE, BORDER_LEFT_STYLE, BORDER_TOP_COLOR, BORDER_RIGHT_COLOR, BORDER_BOTTOM_COLOR, BORDER_LEFT_COLOR, CLEAR, COLOR, DISPLAY, FLOAT, FONT, FONT_FAMILY, FONT_SIZE, FONT_STYLE, FONT_VARIANT, FONT_WEIGHT, HEIGHT, LETTER_SPACING, LINE_HEIGHT, LIST_STYLE, LIST_STYLE_IMAGE, LIST_STYLE_POSITION, LIST_STYLE_TYPE, MARGIN, MARGIN_BOTTOM, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, PADDING, PADDING_BOTTOM, PADDING_LEFT, PADDING_RIGHT, PADDING_TOP, TEXT_ALIGN, TEXT_DECORATION, TEXT_INDENT, TEXT_TRANSFORM, VERTICAL_ALIGN, WORD_SPACING, WHITE_SPACE, WIDTH, BORDER_SPACING, CAPTION_SIDE, MARGIN_LEFT_LTR, MARGIN_LEFT_RTL, MARGIN_RIGHT_LTR, MARGIN_RIGHT_RTL };
/*      */ 
/*  406 */     private static final Attribute[] ALL_MARGINS = { MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT };
/*      */ 
/*  408 */     private static final Attribute[] ALL_PADDING = { PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT };
/*      */ 
/*  410 */     private static final Attribute[] ALL_BORDER_WIDTHS = { BORDER_TOP_WIDTH, BORDER_RIGHT_WIDTH, BORDER_BOTTOM_WIDTH, BORDER_LEFT_WIDTH };
/*      */ 
/*  413 */     private static final Attribute[] ALL_BORDER_STYLES = { BORDER_TOP_STYLE, BORDER_RIGHT_STYLE, BORDER_BOTTOM_STYLE, BORDER_LEFT_STYLE };
/*      */ 
/*  416 */     private static final Attribute[] ALL_BORDER_COLORS = { BORDER_TOP_COLOR, BORDER_RIGHT_COLOR, BORDER_BOTTOM_COLOR, BORDER_LEFT_COLOR };
/*      */ 
/*      */     private Attribute(String paramString1, String paramString2, boolean paramBoolean)
/*      */     {
/*  137 */       this.name = paramString1;
/*  138 */       this.defaultValue = paramString2;
/*  139 */       this.inherited = paramBoolean;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  148 */       return this.name;
/*      */     }
/*      */ 
/*      */     public String getDefaultValue()
/*      */     {
/*  157 */       return this.defaultValue;
/*      */     }
/*      */ 
/*      */     public boolean isInherited()
/*      */     {
/*  165 */       return this.inherited;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BackgroundImage extends CSS.CssValue
/*      */   {
/*      */     private boolean loadedImage;
/*      */     private ImageIcon image;
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 2588 */       BackgroundImage localBackgroundImage = new BackgroundImage();
/* 2589 */       localBackgroundImage.svalue = paramString;
/* 2590 */       return localBackgroundImage;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString) {
/* 2594 */       return parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     ImageIcon getImage(URL paramURL)
/*      */     {
/* 2599 */       if (!this.loadedImage) {
/* 2600 */         synchronized (this) {
/* 2601 */           if (!this.loadedImage) {
/* 2602 */             URL localURL = CSS.getURL(paramURL, this.svalue);
/* 2603 */             this.loadedImage = true;
/* 2604 */             if (localURL != null) {
/* 2605 */               this.image = new ImageIcon();
/* 2606 */               Image localImage = Toolkit.getDefaultToolkit().createImage(localURL);
/* 2607 */               if (localImage != null) {
/* 2608 */                 this.image.setImage(localImage);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2614 */       return this.image;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BackgroundPosition extends CSS.CssValue
/*      */   {
/*      */     float horizontalPosition;
/*      */     float verticalPosition;
/*      */     short relative;
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 2460 */       String[] arrayOfString = CSS.parseStrings(paramString);
/* 2461 */       int i = arrayOfString.length;
/* 2462 */       BackgroundPosition localBackgroundPosition = new BackgroundPosition();
/* 2463 */       localBackgroundPosition.relative = 5;
/* 2464 */       localBackgroundPosition.svalue = paramString;
/*      */ 
/* 2466 */       if (i > 0)
/*      */       {
/* 2468 */         int j = 0;
/* 2469 */         int k = 0;
/*      */         Object localObject;
/* 2470 */         while (k < i)
/*      */         {
/* 2472 */           localObject = arrayOfString[(k++)];
/* 2473 */           if (((String)localObject).equals("center")) {
/* 2474 */             j = (short)(j | 0x4);
/*      */           }
/* 2478 */           else if ((j & 0x1) == 0) {
/* 2479 */             if (((String)localObject).equals("top")) {
/* 2480 */               j = (short)(j | 0x1);
/*      */             }
/* 2482 */             else if (((String)localObject).equals("bottom")) {
/* 2483 */               j = (short)(j | 0x1);
/* 2484 */               localBackgroundPosition.verticalPosition = 1.0F;
/*      */             }
/*      */ 
/*      */           }
/* 2488 */           else if ((j & 0x2) == 0) {
/* 2489 */             if (((String)localObject).equals("left")) {
/* 2490 */               j = (short)(j | 0x2);
/* 2491 */               localBackgroundPosition.horizontalPosition = 0.0F;
/*      */             }
/* 2493 */             else if (((String)localObject).equals("right")) {
/* 2494 */               j = (short)(j | 0x2);
/* 2495 */               localBackgroundPosition.horizontalPosition = 1.0F;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2500 */         if (j != 0) {
/* 2501 */           if ((j & 0x1) == 1) {
/* 2502 */             if ((j & 0x2) == 0)
/*      */             {
/* 2504 */               localBackgroundPosition.horizontalPosition = 0.5F;
/*      */             }
/*      */           }
/* 2507 */           else if ((j & 0x2) == 2)
/*      */           {
/* 2509 */             localBackgroundPosition.verticalPosition = 0.5F;
/*      */           }
/*      */           else
/*      */           {
/* 2513 */             localBackgroundPosition.horizontalPosition = (localBackgroundPosition.verticalPosition = 0.5F);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2518 */           localObject = new CSS.LengthUnit(arrayOfString[0], (short)0, 0.0F);
/*      */ 
/* 2520 */           if (((CSS.LengthUnit)localObject).type == 0) {
/* 2521 */             localBackgroundPosition.horizontalPosition = ((CSS.LengthUnit)localObject).value;
/* 2522 */             localBackgroundPosition.relative = ((short)(0x1 ^ localBackgroundPosition.relative));
/*      */           }
/* 2524 */           else if (((CSS.LengthUnit)localObject).type == 1) {
/* 2525 */             localBackgroundPosition.horizontalPosition = ((CSS.LengthUnit)localObject).value;
/*      */           }
/* 2527 */           else if (((CSS.LengthUnit)localObject).type == 3) {
/* 2528 */             localBackgroundPosition.horizontalPosition = ((CSS.LengthUnit)localObject).value;
/* 2529 */             localBackgroundPosition.relative = ((short)(0x1 ^ localBackgroundPosition.relative | 0x2));
/*      */           }
/* 2531 */           if (i > 1) {
/* 2532 */             localObject = new CSS.LengthUnit(arrayOfString[1], (short)0, 0.0F);
/*      */ 
/* 2534 */             if (((CSS.LengthUnit)localObject).type == 0) {
/* 2535 */               localBackgroundPosition.verticalPosition = ((CSS.LengthUnit)localObject).value;
/* 2536 */               localBackgroundPosition.relative = ((short)(0x4 ^ localBackgroundPosition.relative));
/*      */             }
/* 2538 */             else if (((CSS.LengthUnit)localObject).type == 1) {
/* 2539 */               localBackgroundPosition.verticalPosition = ((CSS.LengthUnit)localObject).value;
/*      */             }
/* 2541 */             else if (((CSS.LengthUnit)localObject).type == 3) {
/* 2542 */               localBackgroundPosition.verticalPosition = ((CSS.LengthUnit)localObject).value;
/* 2543 */               localBackgroundPosition.relative = ((short)(0x4 ^ localBackgroundPosition.relative | 0x8));
/*      */             }
/*      */           }
/*      */           else {
/* 2547 */             localBackgroundPosition.verticalPosition = 0.5F;
/*      */           }
/*      */         }
/*      */       }
/* 2551 */       return localBackgroundPosition;
/*      */     }
/*      */ 
/*      */     boolean isHorizontalPositionRelativeToSize() {
/* 2555 */       return (this.relative & 0x1) == 1;
/*      */     }
/*      */ 
/*      */     boolean isHorizontalPositionRelativeToFontSize() {
/* 2559 */       return (this.relative & 0x2) == 2;
/*      */     }
/*      */ 
/*      */     float getHorizontalPosition() {
/* 2563 */       return this.horizontalPosition;
/*      */     }
/*      */ 
/*      */     boolean isVerticalPositionRelativeToSize() {
/* 2567 */       return (this.relative & 0x4) == 4;
/*      */     }
/*      */ 
/*      */     boolean isVerticalPositionRelativeToFontSize() {
/* 2571 */       return (this.relative & 0x8) == 8;
/*      */     }
/*      */ 
/*      */     float getVerticalPosition() {
/* 2575 */       return this.verticalPosition;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BorderStyle extends CSS.CssValue
/*      */   {
/*      */     private transient CSS.Value style;
/*      */ 
/*      */     CSS.Value getValue()
/*      */     {
/* 2190 */       return this.style;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString) {
/* 2194 */       CSS.Value localValue = CSS.getValue(paramString);
/* 2195 */       if ((localValue != null) && (
/* 2196 */         (localValue == CSS.Value.INSET) || (localValue == CSS.Value.OUTSET) || (localValue == CSS.Value.NONE) || (localValue == CSS.Value.DOTTED) || (localValue == CSS.Value.DASHED) || (localValue == CSS.Value.SOLID) || (localValue == CSS.Value.DOUBLE) || (localValue == CSS.Value.GROOVE) || (localValue == CSS.Value.RIDGE)))
/*      */       {
/* 2206 */         BorderStyle localBorderStyle = new BorderStyle();
/* 2207 */         localBorderStyle.svalue = paramString;
/* 2208 */         localBorderStyle.style = localValue;
/* 2209 */         return localBorderStyle;
/*      */       }
/*      */ 
/* 2212 */       return null;
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */     {
/* 2217 */       paramObjectOutputStream.defaultWriteObject();
/* 2218 */       if (this.style == null) {
/* 2219 */         paramObjectOutputStream.writeObject(null);
/*      */       }
/*      */       else
/* 2222 */         paramObjectOutputStream.writeObject(this.style.toString());
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/* 2228 */       paramObjectInputStream.defaultReadObject();
/* 2229 */       Object localObject = paramObjectInputStream.readObject();
/* 2230 */       if (localObject != null)
/* 2231 */         this.style = CSS.getValue((String)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BorderWidthValue extends CSS.LengthValue
/*      */   {
/* 2407 */     private static final float[] values = { 1.0F, 2.0F, 4.0F };
/*      */ 
/*      */     BorderWidthValue(String paramString, int paramInt)
/*      */     {
/* 2378 */       this.svalue = paramString;
/* 2379 */       this.span = values[paramInt];
/* 2380 */       this.percentage = false;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString) {
/* 2384 */       if (paramString != null) {
/* 2385 */         if (paramString.equals("thick")) {
/* 2386 */           return new BorderWidthValue(paramString, 2);
/*      */         }
/* 2388 */         if (paramString.equals("medium")) {
/* 2389 */           return new BorderWidthValue(paramString, 1);
/*      */         }
/* 2391 */         if (paramString.equals("thin")) {
/* 2392 */           return new BorderWidthValue(paramString, 0);
/*      */         }
/*      */       }
/*      */ 
/* 2396 */       return super.parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString) {
/* 2400 */       if (paramString == "#DEFAULT") {
/* 2401 */         return parseCssValue("medium");
/*      */       }
/* 2403 */       return parseCssValue(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ColorValue extends CSS.CssValue
/*      */   {
/*      */     Color c;
/*      */ 
/*      */     Color getValue()
/*      */     {
/* 2134 */       return this.c;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 2139 */       Color localColor = CSS.stringToColor(paramString);
/* 2140 */       if (localColor != null) {
/* 2141 */         ColorValue localColorValue = new ColorValue();
/* 2142 */         localColorValue.svalue = paramString;
/* 2143 */         localColorValue.c = localColor;
/* 2144 */         return localColorValue;
/*      */       }
/* 2146 */       return null;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString) {
/* 2150 */       return parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 2165 */       ColorValue localColorValue = new ColorValue();
/* 2166 */       localColorValue.c = ((Color)paramObject);
/* 2167 */       localColorValue.svalue = CSS.colorToHex(localColorValue.c);
/* 2168 */       return localColorValue;
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 2181 */       return this.c;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CssValue
/*      */     implements Serializable
/*      */   {
/*      */     String svalue;
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 1575 */       return paramString;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString)
/*      */     {
/* 1588 */       return parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 1603 */       return null;
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 1619 */       return null;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1626 */       return this.svalue;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CssValueMapper extends CSS.CssValue
/*      */   {
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 2417 */       Object localObject = CSS.cssValueToInternalValueMap.get(paramString);
/* 2418 */       if (localObject == null) {
/* 2419 */         localObject = CSS.cssValueToInternalValueMap.get(paramString.toLowerCase());
/*      */       }
/* 2421 */       return localObject;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString)
/*      */     {
/* 2426 */       Object localObject = CSS.htmlValueToCssValueMap.get(paramString);
/* 2427 */       if (localObject == null) {
/* 2428 */         localObject = CSS.htmlValueToCssValueMap.get(paramString.toLowerCase());
/*      */       }
/* 2430 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FontFamily extends CSS.CssValue
/*      */   {
/*      */     String family;
/*      */ 
/*      */     String getValue()
/*      */     {
/* 1975 */       return this.family;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString) {
/* 1979 */       int i = paramString.indexOf(',');
/* 1980 */       FontFamily localFontFamily = new FontFamily();
/* 1981 */       localFontFamily.svalue = paramString;
/* 1982 */       localFontFamily.family = null;
/*      */ 
/* 1984 */       if (i == -1) {
/* 1985 */         setFontName(localFontFamily, paramString);
/*      */       }
/*      */       else {
/* 1988 */         int j = 0;
/*      */ 
/* 1990 */         int m = paramString.length();
/* 1991 */         i = 0;
/* 1992 */         while (j == 0)
/*      */         {
/* 1994 */           while ((i < m) && (Character.isWhitespace(paramString.charAt(i))))
/*      */           {
/* 1996 */             i++;
/*      */           }
/* 1998 */           int k = i;
/* 1999 */           i = paramString.indexOf(',', i);
/* 2000 */           if (i == -1) {
/* 2001 */             i = m;
/*      */           }
/* 2003 */           if (k < m) {
/* 2004 */             if (k != i) {
/* 2005 */               int n = i;
/* 2006 */               if ((i > 0) && (paramString.charAt(i - 1) == ' ')) {
/* 2007 */                 n--;
/*      */               }
/* 2009 */               setFontName(localFontFamily, paramString.substring(k, n));
/*      */ 
/* 2011 */               j = localFontFamily.family != null ? 1 : 0;
/*      */             }
/* 2013 */             i++;
/*      */           }
/*      */           else {
/* 2016 */             j = 1;
/*      */           }
/*      */         }
/*      */       }
/* 2020 */       if (localFontFamily.family == null) {
/* 2021 */         localFontFamily.family = "SansSerif";
/*      */       }
/* 2023 */       return localFontFamily;
/*      */     }
/*      */ 
/*      */     private void setFontName(FontFamily paramFontFamily, String paramString) {
/* 2027 */       paramFontFamily.family = paramString;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString)
/*      */     {
/* 2032 */       return parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 2047 */       return parseCssValue(paramObject.toString());
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 2060 */       return this.family;
/*      */     }
/*      */   }
/*      */ 
/*      */   class FontSize extends CSS.CssValue
/*      */   {
/*      */     float value;
/*      */     boolean index;
/*      */     CSS.LengthUnit lu;
/*      */ 
/*      */     FontSize()
/*      */     {
/*      */     }
/*      */ 
/*      */     int getValue(AttributeSet paramAttributeSet, StyleSheet paramStyleSheet)
/*      */     {
/* 1812 */       paramStyleSheet = CSS.this.getStyleSheet(paramStyleSheet);
/* 1813 */       if (this.index)
/*      */       {
/* 1815 */         return Math.round(CSS.this.getPointSize((int)this.value, paramStyleSheet));
/*      */       }
/* 1817 */       if (this.lu == null) {
/* 1818 */         return Math.round(this.value);
/*      */       }
/*      */ 
/* 1821 */       if (this.lu.type == 0) {
/* 1822 */         boolean bool = paramStyleSheet == null ? false : paramStyleSheet.isW3CLengthUnits();
/* 1823 */         return Math.round(this.lu.getValue(bool));
/*      */       }
/* 1825 */       if (paramAttributeSet != null) {
/* 1826 */         AttributeSet localAttributeSet = paramAttributeSet.getResolveParent();
/*      */ 
/* 1828 */         if (localAttributeSet != null) {
/* 1829 */           int i = StyleConstants.getFontSize(localAttributeSet);
/*      */           float f;
/* 1832 */           if ((this.lu.type == 1) || (this.lu.type == 3)) {
/* 1833 */             f = this.lu.value * i;
/*      */           }
/*      */           else {
/* 1836 */             f = this.lu.value + i;
/*      */           }
/* 1838 */           return Math.round(f);
/*      */         }
/*      */       }
/*      */ 
/* 1842 */       return 12;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 1847 */       FontSize localFontSize = new FontSize(CSS.this);
/* 1848 */       localFontSize.svalue = paramString;
/*      */       try {
/* 1850 */         if (paramString.equals("xx-small")) {
/* 1851 */           localFontSize.value = 1.0F;
/* 1852 */           localFontSize.index = true;
/* 1853 */         } else if (paramString.equals("x-small")) {
/* 1854 */           localFontSize.value = 2.0F;
/* 1855 */           localFontSize.index = true;
/* 1856 */         } else if (paramString.equals("small")) {
/* 1857 */           localFontSize.value = 3.0F;
/* 1858 */           localFontSize.index = true;
/* 1859 */         } else if (paramString.equals("medium")) {
/* 1860 */           localFontSize.value = 4.0F;
/* 1861 */           localFontSize.index = true;
/* 1862 */         } else if (paramString.equals("large")) {
/* 1863 */           localFontSize.value = 5.0F;
/* 1864 */           localFontSize.index = true;
/* 1865 */         } else if (paramString.equals("x-large")) {
/* 1866 */           localFontSize.value = 6.0F;
/* 1867 */           localFontSize.index = true;
/* 1868 */         } else if (paramString.equals("xx-large")) {
/* 1869 */           localFontSize.value = 7.0F;
/* 1870 */           localFontSize.index = true;
/*      */         } else {
/* 1872 */           localFontSize.lu = new CSS.LengthUnit(paramString, (short)1, 1.0F);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException)
/*      */       {
/* 1883 */         localFontSize = null;
/*      */       }
/* 1885 */       return localFontSize;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString) {
/* 1889 */       if ((paramString == null) || (paramString.length() == 0)) {
/* 1890 */         return null;
/*      */       }
/* 1892 */       FontSize localFontSize = new FontSize(CSS.this);
/* 1893 */       localFontSize.svalue = paramString;
/*      */       try
/*      */       {
/* 1900 */         int i = CSS.this.getBaseFontSize();
/*      */         int j;
/* 1901 */         if (paramString.charAt(0) == '+') {
/* 1902 */           j = Integer.valueOf(paramString.substring(1)).intValue();
/* 1903 */           localFontSize.value = (i + j);
/* 1904 */           localFontSize.index = true;
/* 1905 */         } else if (paramString.charAt(0) == '-') {
/* 1906 */           j = -Integer.valueOf(paramString.substring(1)).intValue();
/* 1907 */           localFontSize.value = (i + j);
/* 1908 */           localFontSize.index = true;
/*      */         } else {
/* 1910 */           localFontSize.value = Integer.parseInt(paramString);
/* 1911 */           if (localFontSize.value > 7.0F)
/* 1912 */             localFontSize.value = 7.0F;
/* 1913 */           else if (localFontSize.value < 0.0F) {
/* 1914 */             localFontSize.value = 0.0F;
/*      */           }
/* 1916 */           localFontSize.index = true;
/*      */         }
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException) {
/* 1920 */         localFontSize = null;
/*      */       }
/* 1922 */       return localFontSize;
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 1937 */       if ((paramObject instanceof Number)) {
/* 1938 */         FontSize localFontSize = new FontSize(CSS.this);
/*      */ 
/* 1940 */         localFontSize.value = CSS.getIndexOfSize(((Number)paramObject).floatValue(), StyleSheet.sizeMapDefault);
/* 1941 */         localFontSize.svalue = Integer.toString((int)localFontSize.value);
/* 1942 */         localFontSize.index = true;
/* 1943 */         return localFontSize;
/*      */       }
/* 1945 */       return parseCssValue(paramObject.toString());
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 1958 */       if (paramView != null) {
/* 1959 */         return Integer.valueOf(getValue(paramView.getAttributes(), null));
/*      */       }
/* 1961 */       return Integer.valueOf(getValue(null, null));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FontWeight extends CSS.CssValue
/*      */   {
/*      */     int weight;
/*      */ 
/*      */     int getValue()
/*      */     {
/* 2069 */       return this.weight;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString) {
/* 2073 */       FontWeight localFontWeight = new FontWeight();
/* 2074 */       localFontWeight.svalue = paramString;
/* 2075 */       if (paramString.equals("bold"))
/* 2076 */         localFontWeight.weight = 700;
/* 2077 */       else if (paramString.equals("normal"))
/* 2078 */         localFontWeight.weight = 400;
/*      */       else {
/*      */         try
/*      */         {
/* 2082 */           localFontWeight.weight = Integer.parseInt(paramString);
/*      */         } catch (NumberFormatException localNumberFormatException) {
/* 2084 */           localFontWeight = null;
/*      */         }
/*      */       }
/* 2087 */       return localFontWeight;
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 2102 */       if (paramObject.equals(Boolean.TRUE)) {
/* 2103 */         return parseCssValue("bold");
/*      */       }
/* 2105 */       return parseCssValue("normal");
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 2118 */       return this.weight > 500 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*      */     boolean isBold() {
/* 2122 */       return this.weight > 500;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface LayoutIterator
/*      */   {
/*      */     public static final int WorstAdjustmentWeight = 2;
/*      */ 
/*      */     public abstract void setOffset(int paramInt);
/*      */ 
/*      */     public abstract int getOffset();
/*      */ 
/*      */     public abstract void setSpan(int paramInt);
/*      */ 
/*      */     public abstract int getSpan();
/*      */ 
/*      */     public abstract int getCount();
/*      */ 
/*      */     public abstract void setIndex(int paramInt);
/*      */ 
/*      */     public abstract float getMinimumSpan(float paramFloat);
/*      */ 
/*      */     public abstract float getPreferredSpan(float paramFloat);
/*      */ 
/*      */     public abstract float getMaximumSpan(float paramFloat);
/*      */ 
/*      */     public abstract int getAdjustmentWeight();
/*      */ 
/*      */     public abstract float getBorderWidth();
/*      */ 
/*      */     public abstract float getLeadingCollapseSpan();
/*      */ 
/*      */     public abstract float getTrailingCollapseSpan();
/*      */   }
/*      */ 
/*      */   static class LengthUnit
/*      */     implements Serializable
/*      */   {
/* 2623 */     static Hashtable<String, Float> lengthMapping = new Hashtable(6);
/* 2624 */     static Hashtable<String, Float> w3cLengthMapping = new Hashtable(6);
/*      */     short type;
/*      */     float value;
/* 2746 */     String units = null;
/*      */     static final short UNINITALIZED_LENGTH = 10;
/*      */ 
/*      */     LengthUnit(String paramString, short paramShort, float paramFloat)
/*      */     {
/* 2648 */       parse(paramString, paramShort, paramFloat);
/*      */     }
/*      */ 
/*      */     void parse(String paramString, short paramShort, float paramFloat) {
/* 2652 */       this.type = paramShort;
/* 2653 */       this.value = paramFloat;
/*      */ 
/* 2655 */       int i = paramString.length();
/* 2656 */       if ((i > 0) && (paramString.charAt(i - 1) == '%'))
/*      */         try {
/* 2658 */           this.value = (Float.valueOf(paramString.substring(0, i - 1)).floatValue() / 100.0F);
/*      */ 
/* 2660 */           this.type = 1;
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException1) {
/*      */         }
/* 2664 */       if (i >= 2) {
/* 2665 */         this.units = paramString.substring(i - 2, i);
/* 2666 */         Float localFloat = (Float)lengthMapping.get(this.units);
/* 2667 */         if (localFloat != null) {
/*      */           try {
/* 2669 */             this.value = Float.valueOf(paramString.substring(0, i - 2)).floatValue();
/*      */ 
/* 2671 */             this.type = 0;
/*      */           } catch (NumberFormatException localNumberFormatException3) {
/*      */           }
/*      */         }
/* 2675 */         else if ((this.units.equals("em")) || (this.units.equals("ex")))
/*      */         {
/*      */           try {
/* 2678 */             this.value = Float.valueOf(paramString.substring(0, i - 2)).floatValue();
/*      */ 
/* 2680 */             this.type = 3;
/*      */           } catch (NumberFormatException localNumberFormatException4) {
/*      */           }
/*      */         }
/* 2684 */         else if (paramString.equals("larger")) {
/* 2685 */           this.value = 2.0F;
/* 2686 */           this.type = 2;
/*      */         }
/* 2688 */         else if (paramString.equals("smaller")) {
/* 2689 */           this.value = -2.0F;
/* 2690 */           this.type = 2;
/*      */         }
/*      */         else
/*      */         {
/*      */           try {
/* 2695 */             this.value = Float.valueOf(paramString).floatValue();
/* 2696 */             this.type = 0;
/*      */           } catch (NumberFormatException localNumberFormatException5) {
/*      */           }
/*      */         }
/* 2700 */       } else if (i > 0)
/*      */       {
/*      */         try {
/* 2703 */           this.value = Float.valueOf(paramString).floatValue();
/* 2704 */           this.type = 0; } catch (NumberFormatException localNumberFormatException2) {
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     float getValue(boolean paramBoolean) {
/* 2710 */       Hashtable localHashtable = paramBoolean ? w3cLengthMapping : lengthMapping;
/* 2711 */       float f = 1.0F;
/* 2712 */       if (this.units != null) {
/* 2713 */         Float localFloat = (Float)localHashtable.get(this.units);
/* 2714 */         if (localFloat != null) {
/* 2715 */           f = localFloat.floatValue();
/*      */         }
/*      */       }
/* 2718 */       return this.value * f;
/*      */     }
/*      */ 
/*      */     static float getValue(float paramFloat, String paramString, Boolean paramBoolean)
/*      */     {
/* 2723 */       Hashtable localHashtable = paramBoolean.booleanValue() ? w3cLengthMapping : lengthMapping;
/* 2724 */       float f = 1.0F;
/* 2725 */       if (paramString != null) {
/* 2726 */         Float localFloat = (Float)localHashtable.get(paramString);
/* 2727 */         if (localFloat != null) {
/* 2728 */           f = localFloat.floatValue();
/*      */         }
/*      */       }
/* 2731 */       return paramFloat * f;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 2735 */       return this.type + " " + this.value;
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 2626 */       lengthMapping.put("pt", new Float(1.0F));
/*      */ 
/* 2628 */       lengthMapping.put("px", new Float(1.3F));
/* 2629 */       lengthMapping.put("mm", new Float(2.83464F));
/* 2630 */       lengthMapping.put("cm", new Float(28.346399F));
/* 2631 */       lengthMapping.put("pc", new Float(12.0F));
/* 2632 */       lengthMapping.put("in", new Float(72.0F));
/* 2633 */       int i = 72;
/*      */       try {
/* 2635 */         i = Toolkit.getDefaultToolkit().getScreenResolution();
/*      */       }
/*      */       catch (HeadlessException localHeadlessException) {
/*      */       }
/* 2639 */       w3cLengthMapping.put("pt", new Float(i / 72.0F));
/* 2640 */       w3cLengthMapping.put("px", new Float(1.0F));
/* 2641 */       w3cLengthMapping.put("mm", new Float(i / 25.4F));
/* 2642 */       w3cLengthMapping.put("cm", new Float(i / 2.54F));
/* 2643 */       w3cLengthMapping.put("pc", new Float(i / 6.0F));
/* 2644 */       w3cLengthMapping.put("in", new Float(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LengthValue extends CSS.CssValue
/*      */   {
/*      */     boolean mayBeNegative;
/*      */     boolean percentage;
/*      */     float span;
/* 2368 */     String units = null;
/*      */ 
/*      */     LengthValue()
/*      */     {
/* 2247 */       this(false);
/*      */     }
/*      */ 
/*      */     LengthValue(boolean paramBoolean) {
/* 2251 */       this.mayBeNegative = paramBoolean;
/*      */     }
/*      */ 
/*      */     float getValue()
/*      */     {
/* 2258 */       return getValue(false);
/*      */     }
/*      */ 
/*      */     float getValue(boolean paramBoolean) {
/* 2262 */       return getValue(0.0F, paramBoolean);
/*      */     }
/*      */ 
/*      */     float getValue(float paramFloat)
/*      */     {
/* 2270 */       return getValue(paramFloat, false);
/*      */     }
/*      */     float getValue(float paramFloat, boolean paramBoolean) {
/* 2273 */       if (this.percentage) {
/* 2274 */         return this.span * paramFloat;
/*      */       }
/* 2276 */       return CSS.LengthUnit.getValue(this.span, this.units, Boolean.valueOf(paramBoolean));
/*      */     }
/*      */ 
/*      */     boolean isPercentage()
/*      */     {
/* 2284 */       return this.percentage;
/*      */     }
/*      */ 
/*      */     Object parseCssValue(String paramString)
/*      */     {
/*      */       CSS.LengthUnit localLengthUnit;
/*      */       try {
/* 2291 */         float f = Float.valueOf(paramString).floatValue();
/* 2292 */         localLengthValue = new LengthValue();
/* 2293 */         localLengthValue.span = f;
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException) {
/* 2296 */         localLengthUnit = new CSS.LengthUnit(paramString, (short)10, 0.0F);
/*      */ 
/* 2302 */         switch (localLengthUnit.type)
/*      */         {
/*      */         case 0:
/* 2305 */           localLengthValue = new LengthValue();
/* 2306 */           localLengthValue.span = (this.mayBeNegative ? localLengthUnit.value : Math.max(0.0F, localLengthUnit.value));
/*      */ 
/* 2308 */           localLengthValue.units = localLengthUnit.units;
/*      */         case 1:
/*      */         }
/*      */       }
/* 2312 */       LengthValue localLengthValue = new LengthValue();
/* 2313 */       localLengthValue.span = Math.max(0.0F, Math.min(1.0F, localLengthUnit.value));
/* 2314 */       localLengthValue.percentage = true;
/* 2315 */       break label151;
/*      */ 
/* 2317 */       return null;
/*      */ 
/* 2320 */       label151: localLengthValue.svalue = paramString;
/* 2321 */       return localLengthValue;
/*      */     }
/*      */ 
/*      */     Object parseHtmlValue(String paramString) {
/* 2325 */       if (paramString.equals("#DEFAULT")) {
/* 2326 */         paramString = "1";
/*      */       }
/* 2328 */       return parseCssValue(paramString);
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 2342 */       LengthValue localLengthValue = new LengthValue();
/* 2343 */       localLengthValue.svalue = paramObject.toString();
/* 2344 */       localLengthValue.span = ((Float)paramObject).floatValue();
/* 2345 */       return localLengthValue;
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 2358 */       return new Float(getValue(false));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ShorthandBackgroundParser
/*      */   {
/*      */     static void parseShorthandBackground(CSS paramCSS, String paramString, MutableAttributeSet paramMutableAttributeSet)
/*      */     {
/* 2907 */       String[] arrayOfString = CSS.parseStrings(paramString);
/* 2908 */       int i = arrayOfString.length;
/* 2909 */       int j = 0;
/*      */ 
/* 2912 */       int k = 0;
/*      */ 
/* 2914 */       while (j < i) {
/* 2915 */         String str = arrayOfString[(j++)];
/* 2916 */         if (((k & 0x1) == 0) && (isImage(str))) {
/* 2917 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_IMAGE, str);
/*      */ 
/* 2919 */           k = (short)(k | 0x1);
/*      */         }
/* 2921 */         else if (((k & 0x2) == 0) && (isRepeat(str))) {
/* 2922 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_REPEAT, str);
/*      */ 
/* 2924 */           k = (short)(k | 0x2);
/*      */         }
/* 2926 */         else if (((k & 0x4) == 0) && (isAttachment(str))) {
/* 2927 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_ATTACHMENT, str);
/*      */ 
/* 2929 */           k = (short)(k | 0x4);
/*      */         }
/* 2931 */         else if (((k & 0x8) == 0) && (isPosition(str))) {
/* 2932 */           if ((j < i) && (isPosition(arrayOfString[j]))) {
/* 2933 */             paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_POSITION, str + " " + arrayOfString[(j++)]);
/*      */           }
/*      */           else
/*      */           {
/* 2939 */             paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_POSITION, str);
/*      */           }
/*      */ 
/* 2942 */           k = (short)(k | 0x8);
/*      */         }
/* 2944 */         else if (((k & 0x10) == 0) && (isColor(str))) {
/* 2945 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_COLOR, str);
/*      */ 
/* 2947 */           k = (short)(k | 0x10);
/*      */         }
/*      */       }
/* 2950 */       if ((k & 0x1) == 0) {
/* 2951 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_IMAGE, null);
/*      */       }
/*      */ 
/* 2954 */       if ((k & 0x2) == 0) {
/* 2955 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_REPEAT, "repeat");
/*      */       }
/*      */ 
/* 2958 */       if ((k & 0x4) == 0) {
/* 2959 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_ATTACHMENT, "scroll");
/*      */       }
/*      */ 
/* 2962 */       if ((k & 0x8) == 0)
/* 2963 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.BACKGROUND_POSITION, null);
/*      */     }
/*      */ 
/*      */     static boolean isImage(String paramString)
/*      */     {
/* 2976 */       return (paramString.startsWith("url(")) && (paramString.endsWith(")"));
/*      */     }
/*      */ 
/*      */     static boolean isRepeat(String paramString) {
/* 2980 */       return (paramString.equals("repeat-x")) || (paramString.equals("repeat-y")) || (paramString.equals("repeat")) || (paramString.equals("no-repeat"));
/*      */     }
/*      */ 
/*      */     static boolean isAttachment(String paramString)
/*      */     {
/* 2985 */       return (paramString.equals("fixed")) || (paramString.equals("scroll"));
/*      */     }
/*      */ 
/*      */     static boolean isPosition(String paramString) {
/* 2989 */       return (paramString.equals("top")) || (paramString.equals("bottom")) || (paramString.equals("left")) || (paramString.equals("right")) || (paramString.equals("center")) || ((paramString.length() > 0) && (Character.isDigit(paramString.charAt(0))));
/*      */     }
/*      */ 
/*      */     static boolean isColor(String paramString)
/*      */     {
/* 2997 */       return CSS.stringToColor(paramString) != null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ShorthandBorderParser
/*      */   {
/* 3051 */     static CSS.Attribute[] keys = { CSS.Attribute.BORDER_TOP, CSS.Attribute.BORDER_RIGHT, CSS.Attribute.BORDER_BOTTOM, CSS.Attribute.BORDER_LEFT };
/*      */ 
/*      */     static void parseShorthandBorder(MutableAttributeSet paramMutableAttributeSet, CSS.Attribute paramAttribute, String paramString)
/*      */     {
/* 3058 */       Object[] arrayOfObject = new Object[CSSBorder.PARSERS.length];
/* 3059 */       String[] arrayOfString1 = CSS.parseStrings(paramString);
/* 3060 */       for (String str : arrayOfString1) {
/* 3061 */         int m = 0;
/* 3062 */         for (int n = 0; n < arrayOfObject.length; n++) {
/* 3063 */           Object localObject = CSSBorder.PARSERS[n].parseCssValue(str);
/* 3064 */           if (localObject != null) {
/* 3065 */             if (arrayOfObject[n] != null) break;
/* 3066 */             arrayOfObject[n] = localObject;
/* 3067 */             m = 1; break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3072 */         if (m == 0)
/*      */         {
/* 3074 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3079 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 3080 */         if (arrayOfObject[i] == null) {
/* 3081 */           arrayOfObject[i] = CSSBorder.DEFAULTS[i];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3086 */       for (i = 0; i < keys.length; i++)
/* 3087 */         if ((paramAttribute == CSS.Attribute.BORDER) || (paramAttribute == keys[i]))
/* 3088 */           for (??? = 0; ??? < arrayOfObject.length; ???++)
/* 3089 */             paramMutableAttributeSet.addAttribute(CSSBorder.ATTRIBUTES[???][i], arrayOfObject[???]);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ShorthandFontParser
/*      */   {
/*      */     static void parseShorthandFont(CSS paramCSS, String paramString, MutableAttributeSet paramMutableAttributeSet)
/*      */     {
/* 2768 */       String[] arrayOfString = CSS.parseStrings(paramString);
/* 2769 */       int i = arrayOfString.length;
/* 2770 */       int j = 0;
/*      */ 
/* 2772 */       int k = 0;
/* 2773 */       int m = Math.min(3, i);
/*      */ 
/* 2776 */       while (j < m) {
/* 2777 */         if (((k & 0x1) == 0) && (isFontStyle(arrayOfString[j]))) {
/* 2778 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_STYLE, arrayOfString[(j++)]);
/*      */ 
/* 2780 */           k = (short)(k | 0x1);
/*      */         }
/* 2782 */         else if (((k & 0x2) == 0) && (isFontVariant(arrayOfString[j]))) {
/* 2783 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_VARIANT, arrayOfString[(j++)]);
/*      */ 
/* 2785 */           k = (short)(k | 0x2);
/*      */         }
/* 2787 */         else if (((k & 0x4) == 0) && (isFontWeight(arrayOfString[j]))) {
/* 2788 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_WEIGHT, arrayOfString[(j++)]);
/*      */ 
/* 2790 */           k = (short)(k | 0x4);
/*      */         } else {
/* 2792 */           if (!arrayOfString[j].equals("normal")) break;
/* 2793 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2799 */       if ((k & 0x1) == 0) {
/* 2800 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_STYLE, "normal");
/*      */       }
/*      */ 
/* 2803 */       if ((k & 0x2) == 0) {
/* 2804 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_VARIANT, "normal");
/*      */       }
/*      */ 
/* 2807 */       if ((k & 0x4) == 0)
/* 2808 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_WEIGHT, "normal");
/*      */       String str;
/* 2813 */       if (j < i) {
/* 2814 */         str = arrayOfString[j];
/* 2815 */         int n = str.indexOf('/');
/*      */ 
/* 2817 */         if (n != -1) {
/* 2818 */           str = str.substring(0, n);
/* 2819 */           arrayOfString[j] = arrayOfString[j].substring(n);
/*      */         }
/*      */         else {
/* 2822 */           j++;
/*      */         }
/* 2824 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_SIZE, str);
/*      */       }
/*      */       else
/*      */       {
/* 2828 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_SIZE, "medium");
/*      */       }
/*      */ 
/* 2833 */       if ((j < i) && (arrayOfString[j].startsWith("/"))) {
/* 2834 */         str = null;
/* 2835 */         if (arrayOfString[j].equals("/")) {
/* 2836 */           j++; if (j < i)
/* 2837 */             str = arrayOfString[(j++)];
/*      */         }
/*      */         else
/*      */         {
/* 2841 */           str = arrayOfString[(j++)].substring(1);
/*      */         }
/*      */ 
/* 2844 */         if (str != null) {
/* 2845 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.LINE_HEIGHT, str);
/*      */         }
/*      */         else
/*      */         {
/* 2849 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.LINE_HEIGHT, "normal");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2854 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.LINE_HEIGHT, "normal");
/*      */       }
/*      */ 
/* 2859 */       if (j < i) {
/* 2860 */         str = arrayOfString[(j++)];
/*      */ 
/* 2862 */         while (j < i) {
/* 2863 */           str = str + " " + arrayOfString[(j++)];
/*      */         }
/* 2865 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_FAMILY, str);
/*      */       }
/*      */       else
/*      */       {
/* 2869 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, CSS.Attribute.FONT_FAMILY, "SansSerif");
/*      */       }
/*      */     }
/*      */ 
/*      */     private static boolean isFontStyle(String paramString)
/*      */     {
/* 2875 */       return (paramString.equals("italic")) || (paramString.equals("oblique"));
/*      */     }
/*      */ 
/*      */     private static boolean isFontVariant(String paramString)
/*      */     {
/* 2880 */       return paramString.equals("small-caps");
/*      */     }
/*      */ 
/*      */     private static boolean isFontWeight(String paramString) {
/* 2884 */       if ((paramString.equals("bold")) || (paramString.equals("bolder")) || (paramString.equals("italic")) || (paramString.equals("lighter")))
/*      */       {
/* 2886 */         return true;
/*      */       }
/*      */ 
/* 2889 */       return (paramString.length() == 3) && (paramString.charAt(0) >= '1') && (paramString.charAt(0) <= '9') && (paramString.charAt(1) == '0') && (paramString.charAt(2) == '0');
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ShorthandMarginParser
/*      */   {
/*      */     static void parseShorthandMargin(CSS paramCSS, String paramString, MutableAttributeSet paramMutableAttributeSet, CSS.Attribute[] paramArrayOfAttribute)
/*      */     {
/* 3014 */       String[] arrayOfString = CSS.parseStrings(paramString);
/* 3015 */       int i = arrayOfString.length;
/* 3016 */       int j = 0;
/*      */       int k;
/* 3017 */       switch (i)
/*      */       {
/*      */       case 0:
/*      */       case 1:
/* 3023 */         for (k = 0; k < 4; k++) {
/* 3024 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[k], arrayOfString[0]);
/*      */         }
/* 3026 */         break;
/*      */       case 2:
/* 3029 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[0], arrayOfString[0]);
/* 3030 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[2], arrayOfString[0]);
/* 3031 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[1], arrayOfString[1]);
/* 3032 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[3], arrayOfString[1]);
/* 3033 */         break;
/*      */       case 3:
/* 3035 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[0], arrayOfString[0]);
/* 3036 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[1], arrayOfString[1]);
/* 3037 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[2], arrayOfString[2]);
/* 3038 */         paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[3], arrayOfString[1]);
/* 3039 */         break;
/*      */       default:
/* 3041 */         for (k = 0; k < 4; k++)
/* 3042 */           paramCSS.addInternalCSSValue(paramMutableAttributeSet, paramArrayOfAttribute[k], arrayOfString[k]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StringValue extends CSS.CssValue
/*      */   {
/*      */     Object parseCssValue(String paramString)
/*      */     {
/* 1653 */       StringValue localStringValue = new StringValue();
/* 1654 */       localStringValue.svalue = paramString;
/* 1655 */       return localStringValue;
/*      */     }
/*      */ 
/*      */     Object fromStyleConstants(StyleConstants paramStyleConstants, Object paramObject)
/*      */     {
/* 1670 */       if (paramStyleConstants == StyleConstants.Italic) {
/* 1671 */         if (paramObject.equals(Boolean.TRUE)) {
/* 1672 */           return parseCssValue("italic");
/*      */         }
/* 1674 */         return parseCssValue("");
/* 1675 */       }if (paramStyleConstants == StyleConstants.Underline) {
/* 1676 */         if (paramObject.equals(Boolean.TRUE)) {
/* 1677 */           return parseCssValue("underline");
/*      */         }
/* 1679 */         return parseCssValue("");
/* 1680 */       }if (paramStyleConstants == StyleConstants.Alignment) {
/* 1681 */         int i = ((Integer)paramObject).intValue();
/*      */         String str;
/* 1683 */         switch (i) {
/*      */         case 0:
/* 1685 */           str = "left";
/* 1686 */           break;
/*      */         case 2:
/* 1688 */           str = "right";
/* 1689 */           break;
/*      */         case 1:
/* 1691 */           str = "center";
/* 1692 */           break;
/*      */         case 3:
/* 1694 */           str = "justify";
/* 1695 */           break;
/*      */         default:
/* 1697 */           str = "left";
/*      */         }
/* 1699 */         return parseCssValue(str);
/* 1700 */       }if (paramStyleConstants == StyleConstants.StrikeThrough) {
/* 1701 */         if (paramObject.equals(Boolean.TRUE)) {
/* 1702 */           return parseCssValue("line-through");
/*      */         }
/* 1704 */         return parseCssValue("");
/* 1705 */       }if (paramStyleConstants == StyleConstants.Superscript) {
/* 1706 */         if (paramObject.equals(Boolean.TRUE)) {
/* 1707 */           return parseCssValue("super");
/*      */         }
/* 1709 */         return parseCssValue("");
/* 1710 */       }if (paramStyleConstants == StyleConstants.Subscript) {
/* 1711 */         if (paramObject.equals(Boolean.TRUE)) {
/* 1712 */           return parseCssValue("sub");
/*      */         }
/* 1714 */         return parseCssValue("");
/*      */       }
/* 1716 */       return null;
/*      */     }
/*      */ 
/*      */     Object toStyleConstants(StyleConstants paramStyleConstants, View paramView)
/*      */     {
/* 1730 */       if (paramStyleConstants == StyleConstants.Italic) {
/* 1731 */         if (this.svalue.indexOf("italic") >= 0) {
/* 1732 */           return Boolean.TRUE;
/*      */         }
/* 1734 */         return Boolean.FALSE;
/* 1735 */       }if (paramStyleConstants == StyleConstants.Underline) {
/* 1736 */         if (this.svalue.indexOf("underline") >= 0) {
/* 1737 */           return Boolean.TRUE;
/*      */         }
/* 1739 */         return Boolean.FALSE;
/* 1740 */       }if (paramStyleConstants == StyleConstants.Alignment) {
/* 1741 */         if (this.svalue.equals("right"))
/* 1742 */           return new Integer(2);
/* 1743 */         if (this.svalue.equals("center"))
/* 1744 */           return new Integer(1);
/* 1745 */         if (this.svalue.equals("justify")) {
/* 1746 */           return new Integer(3);
/*      */         }
/* 1748 */         return new Integer(0);
/* 1749 */       }if (paramStyleConstants == StyleConstants.StrikeThrough) {
/* 1750 */         if (this.svalue.indexOf("line-through") >= 0) {
/* 1751 */           return Boolean.TRUE;
/*      */         }
/* 1753 */         return Boolean.FALSE;
/* 1754 */       }if (paramStyleConstants == StyleConstants.Superscript) {
/* 1755 */         if (this.svalue.indexOf("super") >= 0) {
/* 1756 */           return Boolean.TRUE;
/*      */         }
/* 1758 */         return Boolean.FALSE;
/* 1759 */       }if (paramStyleConstants == StyleConstants.Subscript) {
/* 1760 */         if (this.svalue.indexOf("sub") >= 0) {
/* 1761 */           return Boolean.TRUE;
/*      */         }
/* 1763 */         return Boolean.FALSE;
/*      */       }
/* 1765 */       return null;
/*      */     }
/*      */ 
/*      */     boolean isItalic()
/*      */     {
/* 1770 */       return this.svalue.indexOf("italic") != -1;
/*      */     }
/*      */ 
/*      */     boolean isStrike() {
/* 1774 */       return this.svalue.indexOf("line-through") != -1;
/*      */     }
/*      */ 
/*      */     boolean isUnderline() {
/* 1778 */       return this.svalue.indexOf("underline") != -1;
/*      */     }
/*      */ 
/*      */     boolean isSub() {
/* 1782 */       return this.svalue.indexOf("sub") != -1;
/*      */     }
/*      */ 
/*      */     boolean isSup() {
/* 1786 */       return this.svalue.indexOf("sup") != -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Value
/*      */   {
/*  437 */     static final Value INHERITED = new Value("inherited");
/*  438 */     static final Value NONE = new Value("none");
/*  439 */     static final Value HIDDEN = new Value("hidden");
/*  440 */     static final Value DOTTED = new Value("dotted");
/*  441 */     static final Value DASHED = new Value("dashed");
/*  442 */     static final Value SOLID = new Value("solid");
/*  443 */     static final Value DOUBLE = new Value("double");
/*  444 */     static final Value GROOVE = new Value("groove");
/*  445 */     static final Value RIDGE = new Value("ridge");
/*  446 */     static final Value INSET = new Value("inset");
/*  447 */     static final Value OUTSET = new Value("outset");
/*      */ 
/*  449 */     static final Value DISC = new Value("disc");
/*  450 */     static final Value CIRCLE = new Value("circle");
/*  451 */     static final Value SQUARE = new Value("square");
/*  452 */     static final Value DECIMAL = new Value("decimal");
/*  453 */     static final Value LOWER_ROMAN = new Value("lower-roman");
/*  454 */     static final Value UPPER_ROMAN = new Value("upper-roman");
/*  455 */     static final Value LOWER_ALPHA = new Value("lower-alpha");
/*  456 */     static final Value UPPER_ALPHA = new Value("upper-alpha");
/*      */ 
/*  458 */     static final Value BACKGROUND_NO_REPEAT = new Value("no-repeat");
/*  459 */     static final Value BACKGROUND_REPEAT = new Value("repeat");
/*  460 */     static final Value BACKGROUND_REPEAT_X = new Value("repeat-x");
/*  461 */     static final Value BACKGROUND_REPEAT_Y = new Value("repeat-y");
/*      */ 
/*  463 */     static final Value BACKGROUND_SCROLL = new Value("scroll");
/*  464 */     static final Value BACKGROUND_FIXED = new Value("fixed");
/*      */     private String name;
/*  468 */     static final Value[] allValues = { INHERITED, NONE, DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE, INSET, OUTSET, DISC, CIRCLE, SQUARE, DECIMAL, LOWER_ROMAN, UPPER_ROMAN, LOWER_ALPHA, UPPER_ALPHA, BACKGROUND_NO_REPEAT, BACKGROUND_REPEAT, BACKGROUND_REPEAT_X, BACKGROUND_REPEAT_Y, BACKGROUND_FIXED, BACKGROUND_FIXED };
/*      */ 
/*      */     private Value(String paramString)
/*      */     {
/*  425 */       this.name = paramString;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  434 */       return this.name;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.CSS
 * JD-Core Version:    0.6.2
 */