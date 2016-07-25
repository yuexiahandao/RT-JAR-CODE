/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.BevelBorder;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.Style;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyleContext;
/*      */ import javax.swing.text.StyleContext.SmallAttributeSet;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class StyleSheet extends StyleContext
/*      */ {
/* 1750 */   static final Border noBorder = new EmptyBorder(0, 0, 0, 0);
/*      */   static final int DEFAULT_FONT_SIZE = 3;
/*      */   private CSS css;
/*      */   private SelectorMapping selectorMapping;
/*      */   private Hashtable<String, ResolvedStyle> resolvedStyles;
/*      */   private Vector<StyleSheet> linkedStyleSheets;
/*      */   private URL base;
/* 3334 */   static final int[] sizeMapDefault = { 8, 10, 12, 14, 18, 24, 36 };
/*      */ 
/* 3336 */   private int[] sizeMap = sizeMapDefault;
/* 3337 */   private boolean w3cLengthUnits = false;
/*      */ 
/*      */   public StyleSheet()
/*      */   {
/*  167 */     this.selectorMapping = new SelectorMapping(0);
/*  168 */     this.resolvedStyles = new Hashtable();
/*  169 */     if (this.css == null)
/*  170 */       this.css = new CSS();
/*      */   }
/*      */ 
/*      */   public Style getRule(HTML.Tag paramTag, Element paramElement)
/*      */   {
/*  190 */     SearchBuffer localSearchBuffer = SearchBuffer.obtainSearchBuffer();
/*      */     try
/*      */     {
/*  194 */       Vector localVector = localSearchBuffer.getVector();
/*      */ 
/*  196 */       for (Element localElement = paramElement; localElement != null; localElement = localElement.getParentElement()) {
/*  197 */         localVector.addElement(localElement);
/*      */       }
/*      */ 
/*  201 */       int i = localVector.size();
/*  202 */       StringBuffer localStringBuffer = localSearchBuffer.getStringBuffer();
/*      */ 
/*  208 */       for (int j = i - 1; j >= 1; j--) {
/*  209 */         paramElement = (Element)localVector.elementAt(j);
/*  210 */         localAttributeSet = paramElement.getAttributes();
/*  211 */         Object localObject1 = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*  212 */         String str = localObject1.toString();
/*  213 */         localStringBuffer.append(str);
/*  214 */         if (localAttributeSet != null) {
/*  215 */           if (localAttributeSet.isDefined(HTML.Attribute.ID)) {
/*  216 */             localStringBuffer.append('#');
/*  217 */             localStringBuffer.append(localAttributeSet.getAttribute(HTML.Attribute.ID));
/*      */           }
/*  220 */           else if (localAttributeSet.isDefined(HTML.Attribute.CLASS)) {
/*  221 */             localStringBuffer.append('.');
/*  222 */             localStringBuffer.append(localAttributeSet.getAttribute(HTML.Attribute.CLASS));
/*      */           }
/*      */         }
/*      */ 
/*  226 */         localStringBuffer.append(' ');
/*      */       }
/*  228 */       localStringBuffer.append(paramTag.toString());
/*  229 */       paramElement = (Element)localVector.elementAt(0);
/*  230 */       AttributeSet localAttributeSet = paramElement.getAttributes();
/*  231 */       if (paramElement.isLeaf())
/*      */       {
/*  233 */         localObject2 = localAttributeSet.getAttribute(paramTag);
/*  234 */         if ((localObject2 instanceof AttributeSet)) {
/*  235 */           localAttributeSet = (AttributeSet)localObject2;
/*      */         }
/*      */         else {
/*  238 */           localAttributeSet = null;
/*      */         }
/*      */       }
/*  241 */       if (localAttributeSet != null) {
/*  242 */         if (localAttributeSet.isDefined(HTML.Attribute.ID)) {
/*  243 */           localStringBuffer.append('#');
/*  244 */           localStringBuffer.append(localAttributeSet.getAttribute(HTML.Attribute.ID));
/*      */         }
/*  246 */         else if (localAttributeSet.isDefined(HTML.Attribute.CLASS)) {
/*  247 */           localStringBuffer.append('.');
/*  248 */           localStringBuffer.append(localAttributeSet.getAttribute(HTML.Attribute.CLASS));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  253 */       Object localObject2 = getResolvedStyle(localStringBuffer.toString(), localVector, paramTag);
/*      */ 
/*  255 */       return localObject2;
/*      */     }
/*      */     finally {
/*  258 */       SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Style getRule(String paramString)
/*      */   {
/*  274 */     paramString = cleanSelectorString(paramString);
/*  275 */     if (paramString != null) {
/*  276 */       Style localStyle = getResolvedStyle(paramString);
/*  277 */       return localStyle;
/*      */     }
/*  279 */     return null;
/*      */   }
/*      */ 
/*      */   public void addRule(String paramString)
/*      */   {
/*  288 */     if (paramString != null)
/*      */     {
/*  295 */       if (paramString == "BASE_SIZE_DISABLE") {
/*  296 */         this.sizeMap = sizeMapDefault;
/*  297 */       } else if (paramString.startsWith("BASE_SIZE ")) {
/*  298 */         rebaseSizeMap(Integer.parseInt(paramString.substring("BASE_SIZE ".length())));
/*      */       }
/*  300 */       else if (paramString == "W3C_LENGTH_UNITS_ENABLE") {
/*  301 */         this.w3cLengthUnits = true;
/*  302 */       } else if (paramString == "W3C_LENGTH_UNITS_DISABLE") {
/*  303 */         this.w3cLengthUnits = false;
/*      */       } else {
/*  305 */         CssParser localCssParser = new CssParser();
/*      */         try {
/*  307 */           localCssParser.parse(getBase(), new StringReader(paramString), false, false);
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributeSet getDeclaration(String paramString)
/*      */   {
/*  319 */     if (paramString == null) {
/*  320 */       return SimpleAttributeSet.EMPTY;
/*      */     }
/*  322 */     CssParser localCssParser = new CssParser();
/*  323 */     return localCssParser.parseDeclaration(paramString);
/*      */   }
/*      */ 
/*      */   public void loadRules(Reader paramReader, URL paramURL)
/*      */     throws IOException
/*      */   {
/*  338 */     CssParser localCssParser = new CssParser();
/*  339 */     localCssParser.parse(paramURL, paramReader, false, false);
/*      */   }
/*      */ 
/*      */   public AttributeSet getViewAttributes(View paramView)
/*      */   {
/*  348 */     return new ViewAttributeSet(paramView);
/*      */   }
/*      */ 
/*      */   public void removeStyle(String paramString)
/*      */   {
/*  357 */     Style localStyle1 = getStyle(paramString);
/*      */ 
/*  359 */     if (localStyle1 != null) {
/*  360 */       String str = cleanSelectorString(paramString);
/*  361 */       String[] arrayOfString = getSimpleSelectors(str);
/*  362 */       synchronized (this) {
/*  363 */         SelectorMapping localSelectorMapping = getRootSelectorMapping();
/*  364 */         for (int i = arrayOfString.length - 1; i >= 0; i--) {
/*  365 */           localSelectorMapping = localSelectorMapping.getChildSelectorMapping(arrayOfString[i], true);
/*      */         }
/*      */ 
/*  368 */         Style localStyle2 = localSelectorMapping.getStyle();
/*  369 */         if (localStyle2 != null) {
/*  370 */           localSelectorMapping.setStyle(null);
/*  371 */           if (this.resolvedStyles.size() > 0) {
/*  372 */             Enumeration localEnumeration = this.resolvedStyles.elements();
/*  373 */             while (localEnumeration.hasMoreElements()) {
/*  374 */               ResolvedStyle localResolvedStyle = (ResolvedStyle)localEnumeration.nextElement();
/*  375 */               localResolvedStyle.removeStyle(localStyle2);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  381 */     super.removeStyle(paramString);
/*      */   }
/*      */ 
/*      */   public void addStyleSheet(StyleSheet paramStyleSheet)
/*      */   {
/*  393 */     synchronized (this) {
/*  394 */       if (this.linkedStyleSheets == null) {
/*  395 */         this.linkedStyleSheets = new Vector();
/*      */       }
/*  397 */       if (!this.linkedStyleSheets.contains(paramStyleSheet)) {
/*  398 */         int i = 0;
/*  399 */         if (((paramStyleSheet instanceof UIResource)) && (this.linkedStyleSheets.size() > 1))
/*      */         {
/*  401 */           i = this.linkedStyleSheets.size() - 1;
/*      */         }
/*  403 */         this.linkedStyleSheets.insertElementAt(paramStyleSheet, i);
/*  404 */         linkStyleSheetAt(paramStyleSheet, i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeStyleSheet(StyleSheet paramStyleSheet)
/*      */   {
/*  415 */     synchronized (this) {
/*  416 */       if (this.linkedStyleSheets != null) {
/*  417 */         int i = this.linkedStyleSheets.indexOf(paramStyleSheet);
/*  418 */         if (i != -1) {
/*  419 */           this.linkedStyleSheets.removeElementAt(i);
/*  420 */           unlinkStyleSheet(paramStyleSheet, i);
/*  421 */           if ((i == 0) && (this.linkedStyleSheets.size() == 0))
/*  422 */             this.linkedStyleSheets = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public StyleSheet[] getStyleSheets()
/*      */   {
/*      */     StyleSheet[] arrayOfStyleSheet;
/*  442 */     synchronized (this) {
/*  443 */       if (this.linkedStyleSheets != null) {
/*  444 */         arrayOfStyleSheet = new StyleSheet[this.linkedStyleSheets.size()];
/*  445 */         this.linkedStyleSheets.copyInto(arrayOfStyleSheet);
/*      */       }
/*      */       else {
/*  448 */         arrayOfStyleSheet = null;
/*      */       }
/*      */     }
/*  451 */     return arrayOfStyleSheet;
/*      */   }
/*      */ 
/*      */   public void importStyleSheet(URL paramURL)
/*      */   {
/*      */     try
/*      */     {
/*  466 */       InputStream localInputStream = paramURL.openStream();
/*  467 */       BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
/*  468 */       CssParser localCssParser = new CssParser();
/*  469 */       localCssParser.parse(paramURL, localBufferedReader, false, true);
/*  470 */       localBufferedReader.close();
/*  471 */       localInputStream.close();
/*      */     }
/*      */     catch (Throwable localThrowable)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBase(URL paramURL)
/*      */   {
/*  485 */     this.base = paramURL;
/*      */   }
/*      */ 
/*      */   public URL getBase()
/*      */   {
/*  494 */     return this.base;
/*      */   }
/*      */ 
/*      */   public void addCSSAttribute(MutableAttributeSet paramMutableAttributeSet, CSS.Attribute paramAttribute, String paramString)
/*      */   {
/*  504 */     this.css.addInternalCSSValue(paramMutableAttributeSet, paramAttribute, paramString);
/*      */   }
/*      */ 
/*      */   public boolean addCSSAttributeFromHTML(MutableAttributeSet paramMutableAttributeSet, CSS.Attribute paramAttribute, String paramString)
/*      */   {
/*  514 */     Object localObject = this.css.getCssValue(paramAttribute, paramString);
/*  515 */     if (localObject != null) {
/*  516 */       paramMutableAttributeSet.addAttribute(paramAttribute, localObject);
/*  517 */       return true;
/*      */     }
/*  519 */     return false;
/*      */   }
/*      */ 
/*      */   public AttributeSet translateHTMLToCSS(AttributeSet paramAttributeSet)
/*      */   {
/*  531 */     AttributeSet localAttributeSet = this.css.translateHTMLToCSS(paramAttributeSet);
/*      */ 
/*  533 */     Style localStyle = addStyle(null, null);
/*  534 */     localStyle.addAttributes(localAttributeSet);
/*      */ 
/*  536 */     return localStyle;
/*      */   }
/*      */ 
/*      */   public AttributeSet addAttribute(AttributeSet paramAttributeSet, Object paramObject1, Object paramObject2)
/*      */   {
/*  555 */     if (this.css == null)
/*      */     {
/*  558 */       this.css = new CSS();
/*      */     }
/*  560 */     if ((paramObject1 instanceof StyleConstants)) {
/*  561 */       HTML.Tag localTag = HTML.getTagForStyleConstantsKey((StyleConstants)paramObject1);
/*      */ 
/*  564 */       if ((localTag != null) && (paramAttributeSet.isDefined(localTag))) {
/*  565 */         paramAttributeSet = removeAttribute(paramAttributeSet, localTag);
/*      */       }
/*      */ 
/*  568 */       Object localObject = this.css.styleConstantsValueToCSSValue((StyleConstants)paramObject1, paramObject2);
/*      */ 
/*  570 */       if (localObject != null) {
/*  571 */         CSS.Attribute localAttribute = this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject1);
/*      */ 
/*  573 */         if (localAttribute != null) {
/*  574 */           return super.addAttribute(paramAttributeSet, localAttribute, localObject);
/*      */         }
/*      */       }
/*      */     }
/*  578 */     return super.addAttribute(paramAttributeSet, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public AttributeSet addAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */   {
/*  592 */     if (!(paramAttributeSet2 instanceof HTMLDocument.TaggedAttributeSet)) {
/*  593 */       paramAttributeSet1 = removeHTMLTags(paramAttributeSet1, paramAttributeSet2);
/*      */     }
/*  595 */     return super.addAttributes(paramAttributeSet1, convertAttributeSet(paramAttributeSet2));
/*      */   }
/*      */ 
/*      */   public AttributeSet removeAttribute(AttributeSet paramAttributeSet, Object paramObject)
/*      */   {
/*  609 */     if ((paramObject instanceof StyleConstants)) {
/*  610 */       HTML.Tag localTag = HTML.getTagForStyleConstantsKey((StyleConstants)paramObject);
/*      */ 
/*  612 */       if (localTag != null) {
/*  613 */         paramAttributeSet = super.removeAttribute(paramAttributeSet, localTag);
/*      */       }
/*      */ 
/*  616 */       CSS.Attribute localAttribute = this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*  617 */       if (localAttribute != null) {
/*  618 */         return super.removeAttribute(paramAttributeSet, localAttribute);
/*      */       }
/*      */     }
/*  621 */     return super.removeAttribute(paramAttributeSet, paramObject);
/*      */   }
/*      */ 
/*      */   public AttributeSet removeAttributes(AttributeSet paramAttributeSet, Enumeration<?> paramEnumeration)
/*      */   {
/*  638 */     return super.removeAttributes(paramAttributeSet, paramEnumeration);
/*      */   }
/*      */ 
/*      */   public AttributeSet removeAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */   {
/*  652 */     if (paramAttributeSet1 != paramAttributeSet2) {
/*  653 */       paramAttributeSet1 = removeHTMLTags(paramAttributeSet1, paramAttributeSet2);
/*      */     }
/*  655 */     return super.removeAttributes(paramAttributeSet1, convertAttributeSet(paramAttributeSet2));
/*      */   }
/*      */ 
/*      */   protected StyleContext.SmallAttributeSet createSmallAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  669 */     return new SmallConversionSet(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected MutableAttributeSet createLargeAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  685 */     return new LargeConversionSet(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   private AttributeSet removeHTMLTags(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */   {
/*  693 */     if ((!(paramAttributeSet2 instanceof LargeConversionSet)) && (!(paramAttributeSet2 instanceof SmallConversionSet)))
/*      */     {
/*  695 */       Enumeration localEnumeration = paramAttributeSet2.getAttributeNames();
/*      */ 
/*  697 */       while (localEnumeration.hasMoreElements()) {
/*  698 */         Object localObject = localEnumeration.nextElement();
/*      */ 
/*  700 */         if ((localObject instanceof StyleConstants)) {
/*  701 */           HTML.Tag localTag = HTML.getTagForStyleConstantsKey((StyleConstants)localObject);
/*      */ 
/*  704 */           if ((localTag != null) && (paramAttributeSet1.isDefined(localTag))) {
/*  705 */             paramAttributeSet1 = super.removeAttribute(paramAttributeSet1, localTag);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  710 */     return paramAttributeSet1;
/*      */   }
/*      */ 
/*      */   AttributeSet convertAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  720 */     if (((paramAttributeSet instanceof LargeConversionSet)) || ((paramAttributeSet instanceof SmallConversionSet)))
/*      */     {
/*  723 */       return paramAttributeSet;
/*      */     }
/*      */ 
/*  728 */     Enumeration localEnumeration1 = paramAttributeSet.getAttributeNames();
/*  729 */     while (localEnumeration1.hasMoreElements()) {
/*  730 */       Object localObject1 = localEnumeration1.nextElement();
/*  731 */       if ((localObject1 instanceof StyleConstants))
/*      */       {
/*  734 */         LargeConversionSet localLargeConversionSet = new LargeConversionSet();
/*  735 */         Enumeration localEnumeration2 = paramAttributeSet.getAttributeNames();
/*  736 */         while (localEnumeration2.hasMoreElements()) {
/*  737 */           Object localObject2 = localEnumeration2.nextElement();
/*  738 */           Object localObject3 = null;
/*  739 */           if ((localObject2 instanceof StyleConstants))
/*      */           {
/*  741 */             CSS.Attribute localAttribute = this.css.styleConstantsKeyToCSSKey((StyleConstants)localObject2);
/*      */ 
/*  743 */             if (localAttribute != null) {
/*  744 */               Object localObject4 = paramAttributeSet.getAttribute(localObject2);
/*  745 */               localObject3 = this.css.styleConstantsValueToCSSValue((StyleConstants)localObject2, localObject4);
/*      */ 
/*  747 */               if (localObject3 != null) {
/*  748 */                 localLargeConversionSet.addAttribute(localAttribute, localObject3);
/*      */               }
/*      */             }
/*      */           }
/*  752 */           if (localObject3 == null) {
/*  753 */             localLargeConversionSet.addAttribute(localObject2, paramAttributeSet.getAttribute(localObject2));
/*      */           }
/*      */         }
/*  756 */         return localLargeConversionSet;
/*      */       }
/*      */     }
/*  759 */     return paramAttributeSet;
/*      */   }
/*      */ 
/*      */   public Font getFont(AttributeSet paramAttributeSet)
/*      */   {
/*  884 */     return this.css.getFont(this, paramAttributeSet, 12, this);
/*      */   }
/*      */ 
/*      */   public Color getForeground(AttributeSet paramAttributeSet)
/*      */   {
/*  896 */     Color localColor = this.css.getColor(paramAttributeSet, CSS.Attribute.COLOR);
/*  897 */     if (localColor == null) {
/*  898 */       return Color.black;
/*      */     }
/*  900 */     return localColor;
/*      */   }
/*      */ 
/*      */   public Color getBackground(AttributeSet paramAttributeSet)
/*      */   {
/*  912 */     return this.css.getColor(paramAttributeSet, CSS.Attribute.BACKGROUND_COLOR);
/*      */   }
/*      */ 
/*      */   public BoxPainter getBoxPainter(AttributeSet paramAttributeSet)
/*      */   {
/*  920 */     return new BoxPainter(paramAttributeSet, this.css, this);
/*      */   }
/*      */ 
/*      */   public ListPainter getListPainter(AttributeSet paramAttributeSet)
/*      */   {
/*  928 */     return new ListPainter(paramAttributeSet, this);
/*      */   }
/*      */ 
/*      */   public void setBaseFontSize(int paramInt)
/*      */   {
/*  935 */     this.css.setBaseFontSize(paramInt);
/*      */   }
/*      */ 
/*      */   public void setBaseFontSize(String paramString)
/*      */   {
/*  944 */     this.css.setBaseFontSize(paramString);
/*      */   }
/*      */ 
/*      */   public static int getIndexOfSize(float paramFloat) {
/*  948 */     return CSS.getIndexOfSize(paramFloat, sizeMapDefault);
/*      */   }
/*      */ 
/*      */   public float getPointSize(int paramInt)
/*      */   {
/*  955 */     return this.css.getPointSize(paramInt, this);
/*      */   }
/*      */ 
/*      */   public float getPointSize(String paramString)
/*      */   {
/*  963 */     return this.css.getPointSize(paramString, this);
/*      */   }
/*      */ 
/*      */   public Color stringToColor(String paramString)
/*      */   {
/*  973 */     return CSS.stringToColor(paramString);
/*      */   }
/*      */ 
/*      */   ImageIcon getBackgroundImage(AttributeSet paramAttributeSet)
/*      */   {
/*  981 */     Object localObject = paramAttributeSet.getAttribute(CSS.Attribute.BACKGROUND_IMAGE);
/*      */ 
/*  983 */     if (localObject != null) {
/*  984 */       return ((CSS.BackgroundImage)localObject).getImage(getBase());
/*      */     }
/*  986 */     return null;
/*      */   }
/*      */ 
/*      */   void addRule(String[] paramArrayOfString, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */   {
/* 1000 */     int i = paramArrayOfString.length;
/* 1001 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1002 */     localStringBuilder.append(paramArrayOfString[0]);
/* 1003 */     for (int j = 1; j < i; j++) {
/* 1004 */       localStringBuilder.append(' ');
/* 1005 */       localStringBuilder.append(paramArrayOfString[j]);
/*      */     }
/* 1007 */     String str = localStringBuilder.toString();
/* 1008 */     Object localObject1 = getStyle(str);
/* 1009 */     if (localObject1 == null)
/*      */     {
/* 1015 */       Style localStyle = addStyle(str, null);
/* 1016 */       synchronized (this) {
/* 1017 */         SelectorMapping localSelectorMapping = getRootSelectorMapping();
/* 1018 */         for (int k = i - 1; k >= 0; k--) {
/* 1019 */           localSelectorMapping = localSelectorMapping.getChildSelectorMapping(paramArrayOfString[k], true);
/*      */         }
/*      */ 
/* 1022 */         localObject1 = localSelectorMapping.getStyle();
/* 1023 */         if (localObject1 == null) {
/* 1024 */           localObject1 = localStyle;
/* 1025 */           localSelectorMapping.setStyle((Style)localObject1);
/* 1026 */           refreshResolvedRules(str, paramArrayOfString, (Style)localObject1, localSelectorMapping.getSpecificity());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1031 */     if (paramBoolean) {
/* 1032 */       localObject1 = getLinkedStyle((Style)localObject1);
/*      */     }
/* 1034 */     ((Style)localObject1).addAttributes(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   private synchronized void linkStyleSheetAt(StyleSheet paramStyleSheet, int paramInt)
/*      */   {
/* 1047 */     if (this.resolvedStyles.size() > 0) {
/* 1048 */       Enumeration localEnumeration = this.resolvedStyles.elements();
/* 1049 */       while (localEnumeration.hasMoreElements()) {
/* 1050 */         ResolvedStyle localResolvedStyle = (ResolvedStyle)localEnumeration.nextElement();
/* 1051 */         localResolvedStyle.insertExtendedStyleAt(paramStyleSheet.getRule(localResolvedStyle.getName()), paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void unlinkStyleSheet(StyleSheet paramStyleSheet, int paramInt)
/*      */   {
/* 1063 */     if (this.resolvedStyles.size() > 0) {
/* 1064 */       Enumeration localEnumeration = this.resolvedStyles.elements();
/* 1065 */       while (localEnumeration.hasMoreElements()) {
/* 1066 */         ResolvedStyle localResolvedStyle = (ResolvedStyle)localEnumeration.nextElement();
/* 1067 */         localResolvedStyle.removeExtendedStyleAt(paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   String[] getSimpleSelectors(String paramString)
/*      */   {
/* 1077 */     paramString = cleanSelectorString(paramString);
/* 1078 */     SearchBuffer localSearchBuffer = SearchBuffer.obtainSearchBuffer();
/* 1079 */     Vector localVector = localSearchBuffer.getVector();
/* 1080 */     int i = 0;
/* 1081 */     int j = paramString.length();
/* 1082 */     while (i != -1) {
/* 1083 */       int k = paramString.indexOf(' ', i);
/* 1084 */       if (k != -1) {
/* 1085 */         localVector.addElement(paramString.substring(i, k));
/* 1086 */         k++; if (k == j) {
/* 1087 */           i = -1;
/*      */         }
/*      */         else
/* 1090 */           i = k;
/*      */       }
/*      */       else
/*      */       {
/* 1094 */         localVector.addElement(paramString.substring(i));
/* 1095 */         i = -1;
/*      */       }
/*      */     }
/* 1098 */     String[] arrayOfString = new String[localVector.size()];
/* 1099 */     localVector.copyInto(arrayOfString);
/* 1100 */     SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/* 1101 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   String cleanSelectorString(String paramString)
/*      */   {
/* 1109 */     int i = 1;
/* 1110 */     int j = 0; int k = paramString.length();
/* 1111 */     for (; j < k; j++) {
/* 1112 */       switch (paramString.charAt(j)) {
/*      */       case ' ':
/* 1114 */         if (i != 0) {
/* 1115 */           return _cleanSelectorString(paramString);
/*      */         }
/* 1117 */         i = 1;
/* 1118 */         break;
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\r':
/* 1122 */         return _cleanSelectorString(paramString);
/*      */       default:
/* 1124 */         i = 0;
/*      */       }
/*      */     }
/* 1127 */     if (i != 0) {
/* 1128 */       return _cleanSelectorString(paramString);
/*      */     }
/*      */ 
/* 1131 */     return paramString;
/*      */   }
/*      */ 
/*      */   private String _cleanSelectorString(String paramString)
/*      */   {
/* 1139 */     SearchBuffer localSearchBuffer = SearchBuffer.obtainSearchBuffer();
/* 1140 */     StringBuffer localStringBuffer = localSearchBuffer.getStringBuffer();
/* 1141 */     int i = 1;
/* 1142 */     int j = 0;
/* 1143 */     char[] arrayOfChar = paramString.toCharArray();
/* 1144 */     int k = arrayOfChar.length;
/* 1145 */     String str = null;
/*      */     try {
/* 1147 */       for (int m = 0; m < k; m++) {
/* 1148 */         switch (arrayOfChar[m]) {
/*      */         case ' ':
/* 1150 */           if (i == 0) {
/* 1151 */             i = 1;
/* 1152 */             if (j < m) {
/* 1153 */               localStringBuffer.append(arrayOfChar, j, 1 + m - j);
/*      */             }
/*      */           }
/*      */ 
/* 1157 */           j = m + 1;
/* 1158 */           break;
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\r':
/* 1162 */           if (i == 0) {
/* 1163 */             i = 1;
/* 1164 */             if (j < m) {
/* 1165 */               localStringBuffer.append(arrayOfChar, j, m - j);
/*      */ 
/* 1167 */               localStringBuffer.append(' ');
/*      */             }
/*      */           }
/* 1170 */           j = m + 1;
/* 1171 */           break;
/*      */         default:
/* 1173 */           i = 0;
/*      */         }
/*      */       }
/*      */ 
/* 1177 */       if ((i != 0) && (localStringBuffer.length() > 0))
/*      */       {
/* 1179 */         localStringBuffer.setLength(localStringBuffer.length() - 1);
/*      */       }
/* 1181 */       else if (j < k) {
/* 1182 */         localStringBuffer.append(arrayOfChar, j, k - j);
/*      */       }
/* 1184 */       str = localStringBuffer.toString();
/*      */     }
/*      */     finally {
/* 1187 */       SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/*      */     }
/* 1189 */     return str;
/*      */   }
/*      */ 
/*      */   private SelectorMapping getRootSelectorMapping()
/*      */   {
/* 1197 */     return this.selectorMapping;
/*      */   }
/*      */ 
/*      */   static int getSpecificity(String paramString)
/*      */   {
/* 1208 */     int i = 0;
/* 1209 */     int j = 1;
/*      */ 
/* 1211 */     int k = 0; int m = paramString.length();
/* 1212 */     for (; k < m; k++)
/* 1213 */       switch (paramString.charAt(k)) {
/*      */       case '.':
/* 1215 */         i += 100;
/* 1216 */         break;
/*      */       case '#':
/* 1218 */         i += 10000;
/* 1219 */         break;
/*      */       case ' ':
/* 1221 */         j = 1;
/* 1222 */         break;
/*      */       default:
/* 1224 */         if (j != 0) {
/* 1225 */           j = 0;
/* 1226 */           i++;
/*      */         }
/*      */         break;
/*      */       }
/* 1230 */     return i;
/*      */   }
/*      */ 
/*      */   private Style getLinkedStyle(Style paramStyle)
/*      */   {
/* 1244 */     Style localStyle = (Style)paramStyle.getResolveParent();
/* 1245 */     if (localStyle == null) {
/* 1246 */       localStyle = addStyle(null, null);
/* 1247 */       paramStyle.setResolveParent(localStyle);
/*      */     }
/* 1249 */     return localStyle;
/*      */   }
/*      */ 
/*      */   private synchronized Style getResolvedStyle(String paramString, Vector paramVector, HTML.Tag paramTag)
/*      */   {
/* 1259 */     Style localStyle = (Style)this.resolvedStyles.get(paramString);
/* 1260 */     if (localStyle == null) {
/* 1261 */       localStyle = createResolvedStyle(paramString, paramVector, paramTag);
/*      */     }
/* 1263 */     return localStyle;
/*      */   }
/*      */ 
/*      */   private synchronized Style getResolvedStyle(String paramString)
/*      */   {
/* 1271 */     Style localStyle = (Style)this.resolvedStyles.get(paramString);
/* 1272 */     if (localStyle == null) {
/* 1273 */       localStyle = createResolvedStyle(paramString);
/*      */     }
/* 1275 */     return localStyle;
/*      */   }
/*      */ 
/*      */   private void addSortedStyle(SelectorMapping paramSelectorMapping, Vector<SelectorMapping> paramVector)
/*      */   {
/* 1284 */     int i = paramVector.size();
/*      */ 
/* 1286 */     if (i > 0) {
/* 1287 */       int j = paramSelectorMapping.getSpecificity();
/*      */ 
/* 1289 */       for (int k = 0; k < i; k++) {
/* 1290 */         if (j >= ((SelectorMapping)paramVector.elementAt(k)).getSpecificity()) {
/* 1291 */           paramVector.insertElementAt(paramSelectorMapping, k);
/* 1292 */           return;
/*      */         }
/*      */       }
/*      */     }
/* 1296 */     paramVector.addElement(paramSelectorMapping);
/*      */   }
/*      */ 
/*      */   private synchronized void getStyles(SelectorMapping paramSelectorMapping, Vector<SelectorMapping> paramVector, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, int paramInt1, int paramInt2, Hashtable<SelectorMapping, SelectorMapping> paramHashtable)
/*      */   {
/* 1310 */     if (paramHashtable.contains(paramSelectorMapping)) {
/* 1311 */       return;
/*      */     }
/* 1313 */     paramHashtable.put(paramSelectorMapping, paramSelectorMapping);
/* 1314 */     Style localStyle = paramSelectorMapping.getStyle();
/* 1315 */     if (localStyle != null) {
/* 1316 */       addSortedStyle(paramSelectorMapping, paramVector);
/*      */     }
/* 1318 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 1319 */       String str1 = paramArrayOfString1[i];
/* 1320 */       if (str1 != null) {
/* 1321 */         SelectorMapping localSelectorMapping = paramSelectorMapping.getChildSelectorMapping(str1, false);
/*      */ 
/* 1323 */         if (localSelectorMapping != null)
/* 1324 */           getStyles(localSelectorMapping, paramVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, i + 1, paramInt2, paramHashtable);
/*      */         String str2;
/* 1327 */         if (paramArrayOfString3[i] != null) {
/* 1328 */           str2 = paramArrayOfString3[i];
/* 1329 */           localSelectorMapping = paramSelectorMapping.getChildSelectorMapping(str1 + "." + str2, false);
/*      */ 
/* 1331 */           if (localSelectorMapping != null) {
/* 1332 */             getStyles(localSelectorMapping, paramVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, i + 1, paramInt2, paramHashtable);
/*      */           }
/*      */ 
/* 1335 */           localSelectorMapping = paramSelectorMapping.getChildSelectorMapping("." + str2, false);
/*      */ 
/* 1337 */           if (localSelectorMapping != null) {
/* 1338 */             getStyles(localSelectorMapping, paramVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, i + 1, paramInt2, paramHashtable);
/*      */           }
/*      */         }
/*      */ 
/* 1342 */         if (paramArrayOfString2[i] != null) {
/* 1343 */           str2 = paramArrayOfString2[i];
/* 1344 */           localSelectorMapping = paramSelectorMapping.getChildSelectorMapping(str1 + "#" + str2, false);
/*      */ 
/* 1346 */           if (localSelectorMapping != null) {
/* 1347 */             getStyles(localSelectorMapping, paramVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, i + 1, paramInt2, paramHashtable);
/*      */           }
/*      */ 
/* 1350 */           localSelectorMapping = paramSelectorMapping.getChildSelectorMapping("#" + str2, false);
/*      */ 
/* 1352 */           if (localSelectorMapping != null)
/* 1353 */             getStyles(localSelectorMapping, paramVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, i + 1, paramInt2, paramHashtable);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized Style createResolvedStyle(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3)
/*      */   {
/* 1368 */     SearchBuffer localSearchBuffer = SearchBuffer.obtainSearchBuffer();
/* 1369 */     Vector localVector = localSearchBuffer.getVector();
/* 1370 */     Hashtable localHashtable = localSearchBuffer.getHashtable();
/*      */     try
/*      */     {
/* 1374 */       SelectorMapping localSelectorMapping1 = getRootSelectorMapping();
/* 1375 */       int i = paramArrayOfString1.length;
/* 1376 */       String str1 = paramArrayOfString1[0];
/* 1377 */       SelectorMapping localSelectorMapping2 = localSelectorMapping1.getChildSelectorMapping(str1, false);
/*      */ 
/* 1379 */       if (localSelectorMapping2 != null)
/* 1380 */         getStyles(localSelectorMapping2, localVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, 1, i, localHashtable);
/*      */       String str2;
/* 1383 */       if (paramArrayOfString3[0] != null) {
/* 1384 */         str2 = paramArrayOfString3[0];
/* 1385 */         localSelectorMapping2 = localSelectorMapping1.getChildSelectorMapping(str1 + "." + str2, false);
/*      */ 
/* 1387 */         if (localSelectorMapping2 != null) {
/* 1388 */           getStyles(localSelectorMapping2, localVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, 1, i, localHashtable);
/*      */         }
/*      */ 
/* 1391 */         localSelectorMapping2 = localSelectorMapping1.getChildSelectorMapping("." + str2, false);
/*      */ 
/* 1393 */         if (localSelectorMapping2 != null) {
/* 1394 */           getStyles(localSelectorMapping2, localVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, 1, i, localHashtable);
/*      */         }
/*      */       }
/*      */ 
/* 1398 */       if (paramArrayOfString2[0] != null) {
/* 1399 */         str2 = paramArrayOfString2[0];
/* 1400 */         localSelectorMapping2 = localSelectorMapping1.getChildSelectorMapping(str1 + "#" + str2, false);
/*      */ 
/* 1402 */         if (localSelectorMapping2 != null) {
/* 1403 */           getStyles(localSelectorMapping2, localVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, 1, i, localHashtable);
/*      */         }
/*      */ 
/* 1406 */         localSelectorMapping2 = localSelectorMapping1.getChildSelectorMapping("#" + str2, false);
/*      */ 
/* 1408 */         if (localSelectorMapping2 != null) {
/* 1409 */           getStyles(localSelectorMapping2, localVector, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, 1, i, localHashtable);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1415 */       int j = this.linkedStyleSheets != null ? this.linkedStyleSheets.size() : 0;
/*      */ 
/* 1417 */       int k = localVector.size();
/* 1418 */       AttributeSet[] arrayOfAttributeSet = new AttributeSet[k + j];
/* 1419 */       for (int m = 0; m < k; m++)
/* 1420 */         arrayOfAttributeSet[m] = ((SelectorMapping)localVector.elementAt(m)).getStyle();
/*      */       Object localObject1;
/* 1423 */       for (m = 0; m < j; m++) {
/* 1424 */         localObject1 = ((StyleSheet)this.linkedStyleSheets.elementAt(m)).getRule(paramString);
/* 1425 */         if (localObject1 == null) {
/* 1426 */           arrayOfAttributeSet[(m + k)] = SimpleAttributeSet.EMPTY;
/*      */         }
/*      */         else {
/* 1429 */           arrayOfAttributeSet[(m + k)] = localObject1;
/*      */         }
/*      */       }
/* 1432 */       ResolvedStyle localResolvedStyle = new ResolvedStyle(paramString, arrayOfAttributeSet, k);
/*      */ 
/* 1434 */       this.resolvedStyles.put(paramString, localResolvedStyle);
/* 1435 */       return localResolvedStyle;
/*      */     }
/*      */     finally {
/* 1438 */       SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Style createResolvedStyle(String paramString, Vector paramVector, HTML.Tag paramTag)
/*      */   {
/* 1455 */     int i = paramVector.size();
/*      */ 
/* 1458 */     String[] arrayOfString1 = new String[i];
/* 1459 */     String[] arrayOfString2 = new String[i];
/* 1460 */     String[] arrayOfString3 = new String[i];
/* 1461 */     for (int j = 0; j < i; j++) {
/* 1462 */       Element localElement = (Element)paramVector.elementAt(j);
/* 1463 */       AttributeSet localAttributeSet = localElement.getAttributes();
/*      */       Object localObject;
/* 1464 */       if ((j == 0) && (localElement.isLeaf()))
/*      */       {
/* 1466 */         localObject = localAttributeSet.getAttribute(paramTag);
/* 1467 */         if ((localObject instanceof AttributeSet)) {
/* 1468 */           localAttributeSet = (AttributeSet)localObject;
/*      */         }
/*      */         else {
/* 1471 */           localAttributeSet = null;
/*      */         }
/*      */       }
/* 1474 */       if (localAttributeSet != null) {
/* 1475 */         localObject = (HTML.Tag)localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 1477 */         if (localObject != null) {
/* 1478 */           arrayOfString1[j] = ((HTML.Tag)localObject).toString();
/*      */         }
/*      */         else {
/* 1481 */           arrayOfString1[j] = null;
/*      */         }
/* 1483 */         if (localAttributeSet.isDefined(HTML.Attribute.CLASS)) {
/* 1484 */           arrayOfString3[j] = localAttributeSet.getAttribute(HTML.Attribute.CLASS).toString();
/*      */         }
/*      */         else
/*      */         {
/* 1488 */           arrayOfString3[j] = null;
/*      */         }
/* 1490 */         if (localAttributeSet.isDefined(HTML.Attribute.ID)) {
/* 1491 */           arrayOfString2[j] = localAttributeSet.getAttribute(HTML.Attribute.ID).toString();
/*      */         }
/*      */         else
/*      */         {
/* 1495 */           arrayOfString2[j] = null;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */          tmp245_244 = (arrayOfString3[j] =  = null); arrayOfString2[j] = tmp245_244; arrayOfString1[j] = tmp245_244;
/*      */       }
/*      */     }
/* 1502 */     arrayOfString1[0] = paramTag.toString();
/* 1503 */     return createResolvedStyle(paramString, arrayOfString1, arrayOfString2, arrayOfString3);
/*      */   }
/*      */ 
/*      */   private Style createResolvedStyle(String paramString)
/*      */   {
/* 1512 */     SearchBuffer localSearchBuffer = SearchBuffer.obtainSearchBuffer();
/*      */ 
/* 1514 */     Vector localVector = localSearchBuffer.getVector();
/*      */     try
/*      */     {
/* 1517 */       int i = 0;
/*      */ 
/* 1519 */       int k = 0;
/* 1520 */       int m = 0;
/* 1521 */       int n = paramString.length();
/* 1522 */       while (m < n) {
/* 1523 */         if (i == m) {
/* 1524 */           i = paramString.indexOf('.', m);
/*      */         }
/* 1526 */         if (k == m) {
/* 1527 */           k = paramString.indexOf('#', m);
/*      */         }
/* 1529 */         int j = paramString.indexOf(' ', m);
/* 1530 */         if (j == -1) {
/* 1531 */           j = n;
/*      */         }
/* 1533 */         if ((i != -1) && (k != -1) && (i < j) && (k < j))
/*      */         {
/* 1535 */           if (k < i)
/*      */           {
/* 1537 */             if (m == k) {
/* 1538 */               localVector.addElement("");
/*      */             }
/*      */             else {
/* 1541 */               localVector.addElement(paramString.substring(m, k));
/*      */             }
/*      */ 
/* 1544 */             if (i + 1 < j) {
/* 1545 */               localVector.addElement(paramString.substring(i + 1, j));
/*      */             }
/*      */             else
/*      */             {
/* 1549 */               localVector.addElement(null);
/*      */             }
/* 1551 */             if (k + 1 == i) {
/* 1552 */               localVector.addElement(null);
/*      */             }
/*      */             else {
/* 1555 */               localVector.addElement(paramString.substring(k + 1, i));
/*      */             }
/*      */ 
/*      */           }
/* 1559 */           else if (k < j)
/*      */           {
/* 1561 */             if (m == i) {
/* 1562 */               localVector.addElement("");
/*      */             }
/*      */             else {
/* 1565 */               localVector.addElement(paramString.substring(m, i));
/*      */             }
/*      */ 
/* 1568 */             if (i + 1 < k) {
/* 1569 */               localVector.addElement(paramString.substring(i + 1, k));
/*      */             }
/*      */             else
/*      */             {
/* 1573 */               localVector.addElement(null);
/*      */             }
/* 1575 */             if (k + 1 == j) {
/* 1576 */               localVector.addElement(null);
/*      */             }
/*      */             else {
/* 1579 */               localVector.addElement(paramString.substring(k + 1, j));
/*      */             }
/*      */           }
/*      */ 
/* 1583 */           i = k = j + 1;
/*      */         }
/* 1585 */         else if ((i != -1) && (i < j))
/*      */         {
/* 1587 */           if (i == m) {
/* 1588 */             localVector.addElement("");
/*      */           }
/*      */           else {
/* 1591 */             localVector.addElement(paramString.substring(m, i));
/*      */           }
/*      */ 
/* 1594 */           if (i + 1 == j) {
/* 1595 */             localVector.addElement(null);
/*      */           }
/*      */           else {
/* 1598 */             localVector.addElement(paramString.substring(i + 1, j));
/*      */           }
/*      */ 
/* 1601 */           localVector.addElement(null);
/* 1602 */           i = j + 1;
/*      */         }
/* 1604 */         else if ((k != -1) && (k < j))
/*      */         {
/* 1606 */           if (k == m) {
/* 1607 */             localVector.addElement("");
/*      */           }
/*      */           else {
/* 1610 */             localVector.addElement(paramString.substring(m, k));
/*      */           }
/*      */ 
/* 1613 */           localVector.addElement(null);
/* 1614 */           if (k + 1 == j) {
/* 1615 */             localVector.addElement(null);
/*      */           }
/*      */           else {
/* 1618 */             localVector.addElement(paramString.substring(k + 1, j));
/*      */           }
/*      */ 
/* 1621 */           k = j + 1;
/*      */         }
/*      */         else
/*      */         {
/* 1625 */           localVector.addElement(paramString.substring(m, j));
/*      */ 
/* 1627 */           localVector.addElement(null);
/* 1628 */           localVector.addElement(null);
/*      */         }
/* 1630 */         m = j + 1;
/*      */       }
/*      */ 
/* 1633 */       int i1 = localVector.size();
/* 1634 */       int i2 = i1 / 3;
/* 1635 */       String[] arrayOfString1 = new String[i2];
/* 1636 */       String[] arrayOfString2 = new String[i2];
/* 1637 */       String[] arrayOfString3 = new String[i2];
/* 1638 */       int i3 = 0; for (int i4 = i1 - 3; i3 < i2; 
/* 1639 */         i4 -= 3) {
/* 1640 */         arrayOfString1[i3] = ((String)localVector.elementAt(i4));
/* 1641 */         arrayOfString3[i3] = ((String)localVector.elementAt(i4 + 1));
/* 1642 */         arrayOfString2[i3] = ((String)localVector.elementAt(i4 + 2));
/*      */ 
/* 1639 */         i3++;
/*      */       }
/*      */ 
/* 1644 */       return createResolvedStyle(paramString, arrayOfString1, arrayOfString2, arrayOfString3);
/*      */     }
/*      */     finally {
/* 1647 */       SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void refreshResolvedRules(String paramString, String[] paramArrayOfString, Style paramStyle, int paramInt)
/*      */   {
/* 1660 */     if (this.resolvedStyles.size() > 0) {
/* 1661 */       Enumeration localEnumeration = this.resolvedStyles.elements();
/* 1662 */       while (localEnumeration.hasMoreElements()) {
/* 1663 */         ResolvedStyle localResolvedStyle = (ResolvedStyle)localEnumeration.nextElement();
/* 1664 */         if (localResolvedStyle.matches(paramString))
/* 1665 */           localResolvedStyle.insertStyle(paramStyle, paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void rebaseSizeMap(int paramInt)
/*      */   {
/* 3314 */     this.sizeMap = new int[sizeMapDefault.length];
/* 3315 */     for (int i = 0; i < sizeMapDefault.length; i++)
/* 3316 */       this.sizeMap[i] = Math.max(paramInt * sizeMapDefault[i] / sizeMapDefault[CSS.baseFontSizeIndex], 4);
/*      */   }
/*      */ 
/*      */   int[] getSizeMap()
/*      */   {
/* 3324 */     return this.sizeMap;
/*      */   }
/*      */   boolean isW3CLengthUnits() {
/* 3327 */     return this.w3cLengthUnits;
/*      */   }
/*      */ 
/*      */   static class BackgroundImagePainter
/*      */     implements Serializable
/*      */   {
/*      */     ImageIcon backgroundImage;
/*      */     float hPosition;
/*      */     float vPosition;
/*      */     short flags;
/*      */     private int paintX;
/*      */     private int paintY;
/*      */     private int paintMaxX;
/*      */     private int paintMaxY;
/*      */ 
/*      */     BackgroundImagePainter(AttributeSet paramAttributeSet, CSS paramCSS, StyleSheet paramStyleSheet)
/*      */     {
/* 2465 */       this.backgroundImage = paramStyleSheet.getBackgroundImage(paramAttributeSet);
/*      */ 
/* 2467 */       CSS.BackgroundPosition localBackgroundPosition = (CSS.BackgroundPosition)paramAttributeSet.getAttribute(CSS.Attribute.BACKGROUND_POSITION);
/*      */ 
/* 2469 */       if (localBackgroundPosition != null) {
/* 2470 */         this.hPosition = localBackgroundPosition.getHorizontalPosition();
/* 2471 */         this.vPosition = localBackgroundPosition.getVerticalPosition();
/* 2472 */         if (localBackgroundPosition.isHorizontalPositionRelativeToSize()) {
/* 2473 */           this.flags = ((short)(this.flags | 0x4));
/*      */         }
/* 2475 */         else if (localBackgroundPosition.isHorizontalPositionRelativeToSize()) {
/* 2476 */           this.hPosition *= CSS.getFontSize(paramAttributeSet, 12, paramStyleSheet);
/*      */         }
/* 2478 */         if (localBackgroundPosition.isVerticalPositionRelativeToSize()) {
/* 2479 */           this.flags = ((short)(this.flags | 0x8));
/*      */         }
/* 2481 */         else if (localBackgroundPosition.isVerticalPositionRelativeToFontSize()) {
/* 2482 */           this.vPosition *= CSS.getFontSize(paramAttributeSet, 12, paramStyleSheet);
/*      */         }
/*      */       }
/*      */ 
/* 2486 */       CSS.Value localValue = (CSS.Value)paramAttributeSet.getAttribute(CSS.Attribute.BACKGROUND_REPEAT);
/*      */ 
/* 2488 */       if ((localValue == null) || (localValue == CSS.Value.BACKGROUND_REPEAT)) {
/* 2489 */         this.flags = ((short)(this.flags | 0x3));
/*      */       }
/* 2491 */       else if (localValue == CSS.Value.BACKGROUND_REPEAT_X) {
/* 2492 */         this.flags = ((short)(this.flags | 0x1));
/*      */       }
/* 2494 */       else if (localValue == CSS.Value.BACKGROUND_REPEAT_Y)
/* 2495 */         this.flags = ((short)(this.flags | 0x2));
/*      */     }
/*      */ 
/*      */     void paint(Graphics paramGraphics, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, View paramView)
/*      */     {
/* 2500 */       Rectangle localRectangle = paramGraphics.getClipRect();
/* 2501 */       if (localRectangle != null)
/*      */       {
/* 2504 */         paramGraphics.clipRect((int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4);
/*      */       }
/*      */       int i;
/*      */       int j;
/* 2506 */       if ((this.flags & 0x3) == 0)
/*      */       {
/* 2508 */         i = this.backgroundImage.getIconWidth();
/* 2509 */         j = this.backgroundImage.getIconWidth();
/* 2510 */         if ((this.flags & 0x4) == 4) {
/* 2511 */           this.paintX = ((int)(paramFloat1 + paramFloat3 * this.hPosition - i * this.hPosition));
/*      */         }
/*      */         else
/*      */         {
/* 2515 */           this.paintX = ((int)paramFloat1 + (int)this.hPosition);
/*      */         }
/* 2517 */         if ((this.flags & 0x8) == 8) {
/* 2518 */           this.paintY = ((int)(paramFloat2 + paramFloat4 * this.vPosition - j * this.vPosition));
/*      */         }
/*      */         else
/*      */         {
/* 2522 */           this.paintY = ((int)paramFloat2 + (int)this.vPosition);
/*      */         }
/* 2524 */         if ((localRectangle == null) || ((this.paintX + i > localRectangle.x) && (this.paintY + j > localRectangle.y) && (this.paintX < localRectangle.x + localRectangle.width) && (this.paintY < localRectangle.y + localRectangle.height)))
/*      */         {
/* 2529 */           this.backgroundImage.paintIcon(null, paramGraphics, this.paintX, this.paintY);
/*      */         }
/*      */       }
/*      */       else {
/* 2533 */         i = this.backgroundImage.getIconWidth();
/* 2534 */         j = this.backgroundImage.getIconHeight();
/* 2535 */         if ((i > 0) && (j > 0)) {
/* 2536 */           this.paintX = ((int)paramFloat1);
/* 2537 */           this.paintY = ((int)paramFloat2);
/* 2538 */           this.paintMaxX = ((int)(paramFloat1 + paramFloat3));
/* 2539 */           this.paintMaxY = ((int)(paramFloat2 + paramFloat4));
/* 2540 */           if (updatePaintCoordinates(localRectangle, i, j)) {
/* 2541 */             while (this.paintX < this.paintMaxX) {
/* 2542 */               int k = this.paintY;
/* 2543 */               while (k < this.paintMaxY) {
/* 2544 */                 this.backgroundImage.paintIcon(null, paramGraphics, this.paintX, k);
/*      */ 
/* 2546 */                 k += j;
/*      */               }
/* 2548 */               this.paintX += i;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2553 */       if (localRectangle != null)
/*      */       {
/* 2555 */         paramGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */     }
/*      */ 
/*      */     private boolean updatePaintCoordinates(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */     {
/* 2561 */       if ((this.flags & 0x3) == 1) {
/* 2562 */         this.paintMaxY = (this.paintY + 1);
/*      */       }
/* 2564 */       else if ((this.flags & 0x3) == 2) {
/* 2565 */         this.paintMaxX = (this.paintX + 1);
/*      */       }
/* 2567 */       if (paramRectangle != null) {
/* 2568 */         if (((this.flags & 0x3) == 1) && ((this.paintY + paramInt2 <= paramRectangle.y) || (this.paintY > paramRectangle.y + paramRectangle.height)))
/*      */         {
/* 2571 */           return false;
/*      */         }
/* 2573 */         if (((this.flags & 0x3) == 2) && ((this.paintX + paramInt1 <= paramRectangle.x) || (this.paintX > paramRectangle.x + paramRectangle.width)))
/*      */         {
/* 2576 */           return false;
/*      */         }
/* 2578 */         if ((this.flags & 0x1) == 1) {
/* 2579 */           if (paramRectangle.x + paramRectangle.width < this.paintMaxX) {
/* 2580 */             if ((paramRectangle.x + paramRectangle.width - this.paintX) % paramInt1 == 0) {
/* 2581 */               this.paintMaxX = (paramRectangle.x + paramRectangle.width);
/*      */             }
/*      */             else {
/* 2584 */               this.paintMaxX = (((paramRectangle.x + paramRectangle.width - this.paintX) / paramInt1 + 1) * paramInt1 + this.paintX);
/*      */             }
/*      */           }
/*      */ 
/* 2588 */           if (paramRectangle.x > this.paintX) {
/* 2589 */             this.paintX = ((paramRectangle.x - this.paintX) / paramInt1 * paramInt1 + this.paintX);
/*      */           }
/*      */         }
/* 2592 */         if ((this.flags & 0x2) == 2) {
/* 2593 */           if (paramRectangle.y + paramRectangle.height < this.paintMaxY) {
/* 2594 */             if ((paramRectangle.y + paramRectangle.height - this.paintY) % paramInt2 == 0) {
/* 2595 */               this.paintMaxY = (paramRectangle.y + paramRectangle.height);
/*      */             }
/*      */             else {
/* 2598 */               this.paintMaxY = (((paramRectangle.y + paramRectangle.height - this.paintY) / paramInt2 + 1) * paramInt2 + this.paintY);
/*      */             }
/*      */           }
/*      */ 
/* 2602 */           if (paramRectangle.y > this.paintY) {
/* 2603 */             this.paintY = ((paramRectangle.y - this.paintY) / paramInt2 * paramInt2 + this.paintY);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2608 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BoxPainter
/*      */     implements Serializable
/*      */   {
/*      */     float topMargin;
/*      */     float bottomMargin;
/*      */     float leftMargin;
/*      */     float rightMargin;
/*      */     short marginFlags;
/*      */     Border border;
/*      */     Insets binsets;
/*      */     CSS css;
/*      */     StyleSheet ss;
/*      */     Color bg;
/*      */     StyleSheet.BackgroundImagePainter bgPainter;
/*      */ 
/*      */     BoxPainter(AttributeSet paramAttributeSet, CSS paramCSS, StyleSheet paramStyleSheet)
/*      */     {
/* 1766 */       this.ss = paramStyleSheet;
/* 1767 */       this.css = paramCSS;
/* 1768 */       this.border = getBorder(paramAttributeSet);
/* 1769 */       this.binsets = this.border.getBorderInsets(null);
/* 1770 */       this.topMargin = getLength(CSS.Attribute.MARGIN_TOP, paramAttributeSet);
/* 1771 */       this.bottomMargin = getLength(CSS.Attribute.MARGIN_BOTTOM, paramAttributeSet);
/* 1772 */       this.leftMargin = getLength(CSS.Attribute.MARGIN_LEFT, paramAttributeSet);
/* 1773 */       this.rightMargin = getLength(CSS.Attribute.MARGIN_RIGHT, paramAttributeSet);
/* 1774 */       this.bg = paramStyleSheet.getBackground(paramAttributeSet);
/* 1775 */       if (paramStyleSheet.getBackgroundImage(paramAttributeSet) != null)
/* 1776 */         this.bgPainter = new StyleSheet.BackgroundImagePainter(paramAttributeSet, paramCSS, paramStyleSheet);
/*      */     }
/*      */ 
/*      */     Border getBorder(AttributeSet paramAttributeSet)
/*      */     {
/* 1786 */       return new CSSBorder(paramAttributeSet);
/*      */     }
/*      */ 
/*      */     Color getBorderColor(AttributeSet paramAttributeSet)
/*      */     {
/* 1796 */       Color localColor = this.css.getColor(paramAttributeSet, CSS.Attribute.BORDER_COLOR);
/* 1797 */       if (localColor == null) {
/* 1798 */         localColor = this.css.getColor(paramAttributeSet, CSS.Attribute.COLOR);
/* 1799 */         if (localColor == null) {
/* 1800 */           return Color.black;
/*      */         }
/*      */       }
/* 1803 */       return localColor;
/*      */     }
/*      */ 
/*      */     public float getInset(int paramInt, View paramView)
/*      */     {
/* 1819 */       AttributeSet localAttributeSet = paramView.getAttributes();
/* 1820 */       float f = 0.0F;
/* 1821 */       switch (paramInt) {
/*      */       case 2:
/* 1823 */         f += getOrientationMargin(HorizontalMargin.LEFT, this.leftMargin, localAttributeSet, isLeftToRight(paramView));
/*      */ 
/* 1825 */         f += this.binsets.left;
/* 1826 */         f += getLength(CSS.Attribute.PADDING_LEFT, localAttributeSet);
/* 1827 */         break;
/*      */       case 4:
/* 1829 */         f += getOrientationMargin(HorizontalMargin.RIGHT, this.rightMargin, localAttributeSet, isLeftToRight(paramView));
/*      */ 
/* 1831 */         f += this.binsets.right;
/* 1832 */         f += getLength(CSS.Attribute.PADDING_RIGHT, localAttributeSet);
/* 1833 */         break;
/*      */       case 1:
/* 1835 */         f += this.topMargin;
/* 1836 */         f += this.binsets.top;
/* 1837 */         f += getLength(CSS.Attribute.PADDING_TOP, localAttributeSet);
/* 1838 */         break;
/*      */       case 3:
/* 1840 */         f += this.bottomMargin;
/* 1841 */         f += this.binsets.bottom;
/* 1842 */         f += getLength(CSS.Attribute.PADDING_BOTTOM, localAttributeSet);
/* 1843 */         break;
/*      */       default:
/* 1845 */         throw new IllegalArgumentException("Invalid side: " + paramInt);
/*      */       }
/* 1847 */       return f;
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, View paramView)
/*      */     {
/* 1871 */       float f1 = 0.0F;
/* 1872 */       float f2 = 0.0F;
/* 1873 */       float f3 = 0.0F;
/* 1874 */       float f4 = 0.0F;
/* 1875 */       AttributeSet localAttributeSet = paramView.getAttributes();
/* 1876 */       boolean bool = isLeftToRight(paramView);
/* 1877 */       float f5 = getOrientationMargin(HorizontalMargin.LEFT, this.leftMargin, localAttributeSet, bool);
/*      */ 
/* 1880 */       float f6 = getOrientationMargin(HorizontalMargin.RIGHT, this.rightMargin, localAttributeSet, bool);
/*      */ 
/* 1883 */       if (!(paramView instanceof HTMLEditorKit.HTMLFactory.BodyBlockView)) {
/* 1884 */         f1 = f5;
/* 1885 */         f2 = this.topMargin;
/* 1886 */         f3 = -(f5 + f6);
/* 1887 */         f4 = -(this.topMargin + this.bottomMargin);
/*      */       }
/* 1889 */       if (this.bg != null) {
/* 1890 */         paramGraphics.setColor(this.bg);
/* 1891 */         paramGraphics.fillRect((int)(paramFloat1 + f1), (int)(paramFloat2 + f2), (int)(paramFloat3 + f3), (int)(paramFloat4 + f4));
/*      */       }
/*      */ 
/* 1896 */       if (this.bgPainter != null) {
/* 1897 */         this.bgPainter.paint(paramGraphics, paramFloat1 + f1, paramFloat2 + f2, paramFloat3 + f3, paramFloat4 + f4, paramView);
/*      */       }
/* 1899 */       paramFloat1 += f5;
/* 1900 */       paramFloat2 += this.topMargin;
/* 1901 */       paramFloat3 -= f5 + f6;
/* 1902 */       paramFloat4 -= this.topMargin + this.bottomMargin;
/* 1903 */       if ((this.border instanceof BevelBorder))
/*      */       {
/* 1905 */         int i = (int)getLength(CSS.Attribute.BORDER_TOP_WIDTH, localAttributeSet);
/* 1906 */         for (int j = i - 1; j >= 0; j--)
/* 1907 */           this.border.paintBorder(null, paramGraphics, (int)paramFloat1 + j, (int)paramFloat2 + j, (int)paramFloat3 - 2 * j, (int)paramFloat4 - 2 * j);
/*      */       }
/*      */       else
/*      */       {
/* 1911 */         this.border.paintBorder(null, paramGraphics, (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4);
/*      */       }
/*      */     }
/*      */ 
/*      */     float getLength(CSS.Attribute paramAttribute, AttributeSet paramAttributeSet) {
/* 1916 */       return this.css.getLength(paramAttributeSet, paramAttribute, this.ss);
/*      */     }
/*      */ 
/*      */     static boolean isLeftToRight(View paramView) {
/* 1920 */       boolean bool = true;
/* 1921 */       if (isOrientationAware(paramView))
/*      */       {
/*      */         Container localContainer;
/* 1923 */         if ((paramView != null) && ((localContainer = paramView.getContainer()) != null)) {
/* 1924 */           bool = localContainer.getComponentOrientation().isLeftToRight();
/*      */         }
/*      */       }
/* 1927 */       return bool;
/*      */     }
/*      */ 
/*      */     static boolean isOrientationAware(View paramView)
/*      */     {
/* 1937 */       boolean bool = false;
/*      */       AttributeSet localAttributeSet;
/*      */       Object localObject;
/* 1940 */       if ((paramView != null) && ((localAttributeSet = paramView.getElement().getAttributes()) != null) && (((localObject = localAttributeSet.getAttribute(StyleConstants.NameAttribute)) instanceof HTML.Tag)) && ((localObject == HTML.Tag.DIR) || (localObject == HTML.Tag.MENU) || (localObject == HTML.Tag.UL) || (localObject == HTML.Tag.OL)))
/*      */       {
/* 1947 */         bool = true;
/*      */       }
/*      */ 
/* 1950 */       return bool;
/*      */     }
/*      */ 
/*      */     float getOrientationMargin(HorizontalMargin paramHorizontalMargin, float paramFloat, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */     {
/* 1971 */       float f1 = paramFloat;
/* 1972 */       float f2 = paramFloat;
/* 1973 */       Object localObject = null;
/* 1974 */       switch (StyleSheet.1.$SwitchMap$javax$swing$text$html$StyleSheet$BoxPainter$HorizontalMargin[paramHorizontalMargin.ordinal()])
/*      */       {
/*      */       case 1:
/* 1977 */         f2 = paramBoolean ? getLength(CSS.Attribute.MARGIN_RIGHT_LTR, paramAttributeSet) : getLength(CSS.Attribute.MARGIN_RIGHT_RTL, paramAttributeSet);
/*      */ 
/* 1980 */         localObject = paramAttributeSet.getAttribute(CSS.Attribute.MARGIN_RIGHT);
/*      */ 
/* 1982 */         break;
/*      */       case 2:
/* 1985 */         f2 = paramBoolean ? getLength(CSS.Attribute.MARGIN_LEFT_LTR, paramAttributeSet) : getLength(CSS.Attribute.MARGIN_LEFT_RTL, paramAttributeSet);
/*      */ 
/* 1988 */         localObject = paramAttributeSet.getAttribute(CSS.Attribute.MARGIN_LEFT);
/*      */       }
/*      */ 
/* 1993 */       if ((localObject == null) && (f2 != -2.147484E+009F))
/*      */       {
/* 1995 */         f1 = f2;
/*      */       }
/* 1997 */       return f1;
/*      */     }
/*      */ 
/*      */     static enum HorizontalMargin
/*      */     {
/* 1953 */       LEFT, RIGHT;
/*      */     }
/*      */   }
/*      */ 
/*      */   class CssParser
/*      */     implements CSSParser.CSSParserCallback
/*      */   {
/* 3297 */     Vector<String[]> selectors = new Vector();
/* 3298 */     Vector<String> selectorTokens = new Vector();
/*      */     String propertyName;
/* 3301 */     MutableAttributeSet declaration = new SimpleAttributeSet();
/*      */     boolean parsingDeclaration;
/*      */     boolean isLink;
/*      */     URL base;
/* 3309 */     CSSParser parser = new CSSParser();
/*      */ 
/*      */     CssParser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public AttributeSet parseDeclaration(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 3162 */         return parseDeclaration(new StringReader(paramString)); } catch (IOException localIOException) {
/*      */       }
/* 3164 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet parseDeclaration(Reader paramReader)
/*      */       throws IOException
/*      */     {
/* 3171 */       parse(this.base, paramReader, true, false);
/* 3172 */       return this.declaration.copyAttributes();
/*      */     }
/*      */ 
/*      */     public void parse(URL paramURL, Reader paramReader, boolean paramBoolean1, boolean paramBoolean2)
/*      */       throws IOException
/*      */     {
/* 3180 */       this.base = paramURL;
/* 3181 */       this.isLink = paramBoolean2;
/* 3182 */       this.parsingDeclaration = paramBoolean1;
/* 3183 */       this.declaration.removeAttributes(this.declaration);
/* 3184 */       this.selectorTokens.removeAllElements();
/* 3185 */       this.selectors.removeAllElements();
/* 3186 */       this.propertyName = null;
/* 3187 */       this.parser.parse(paramReader, this, paramBoolean1);
/*      */     }
/*      */ 
/*      */     public void handleImport(String paramString)
/*      */     {
/* 3201 */       URL localURL = CSS.getURL(this.base, paramString);
/* 3202 */       if (localURL != null)
/* 3203 */         StyleSheet.this.importStyleSheet(localURL);
/*      */     }
/*      */ 
/*      */     public void handleSelector(String paramString)
/*      */     {
/* 3212 */       if ((!paramString.startsWith(".")) && (!paramString.startsWith("#")))
/*      */       {
/* 3214 */         paramString = paramString.toLowerCase();
/*      */       }
/* 3216 */       int i = paramString.length();
/*      */ 
/* 3218 */       if (paramString.endsWith(",")) {
/* 3219 */         if (i > 1) {
/* 3220 */           paramString = paramString.substring(0, i - 1);
/* 3221 */           this.selectorTokens.addElement(paramString);
/*      */         }
/* 3223 */         addSelector();
/*      */       }
/* 3225 */       else if (i > 0) {
/* 3226 */         this.selectorTokens.addElement(paramString);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void startRule()
/*      */     {
/* 3234 */       if (this.selectorTokens.size() > 0) {
/* 3235 */         addSelector();
/*      */       }
/* 3237 */       this.propertyName = null;
/*      */     }
/*      */ 
/*      */     public void handleProperty(String paramString)
/*      */     {
/* 3244 */       this.propertyName = paramString;
/*      */     }
/*      */ 
/*      */     public void handleValue(String paramString)
/*      */     {
/* 3251 */       if ((this.propertyName != null) && (paramString != null) && (paramString.length() > 0)) {
/* 3252 */         CSS.Attribute localAttribute = CSS.getAttribute(this.propertyName);
/* 3253 */         if (localAttribute != null)
/*      */         {
/* 3259 */           if ((localAttribute == CSS.Attribute.LIST_STYLE_IMAGE) && 
/* 3260 */             (paramString != null) && (!paramString.equals("none"))) {
/* 3261 */             URL localURL = CSS.getURL(this.base, paramString);
/*      */ 
/* 3263 */             if (localURL != null) {
/* 3264 */               paramString = localURL.toString();
/*      */             }
/*      */           }
/*      */ 
/* 3268 */           StyleSheet.this.addCSSAttribute(this.declaration, localAttribute, paramString);
/*      */         }
/* 3270 */         this.propertyName = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endRule()
/*      */     {
/* 3278 */       int i = this.selectors.size();
/* 3279 */       for (int j = 0; j < i; j++) {
/* 3280 */         String[] arrayOfString = (String[])this.selectors.elementAt(j);
/* 3281 */         if (arrayOfString.length > 0) {
/* 3282 */           StyleSheet.this.addRule(arrayOfString, this.declaration, this.isLink);
/*      */         }
/*      */       }
/* 3285 */       this.declaration.removeAttributes(this.declaration);
/* 3286 */       this.selectors.removeAllElements();
/*      */     }
/*      */ 
/*      */     private void addSelector() {
/* 3290 */       String[] arrayOfString = new String[this.selectorTokens.size()];
/* 3291 */       this.selectorTokens.copyInto(arrayOfString);
/* 3292 */       this.selectors.addElement(arrayOfString);
/* 3293 */       this.selectorTokens.removeAllElements();
/*      */     }
/*      */   }
/*      */ 
/*      */   class LargeConversionSet extends SimpleAttributeSet
/*      */   {
/*      */     public LargeConversionSet(AttributeSet arg2)
/*      */     {
/*  774 */       super();
/*      */     }
/*      */ 
/*      */     public LargeConversionSet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/*  789 */       if ((paramObject instanceof StyleConstants)) {
/*  790 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/*  792 */         if (localAttribute != null) {
/*  793 */           return super.isDefined(localAttribute);
/*      */         }
/*      */       }
/*  796 */       return super.isDefined(paramObject);
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/*  807 */       if ((paramObject instanceof StyleConstants)) {
/*  808 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/*  810 */         if (localAttribute != null) {
/*  811 */           Object localObject = super.getAttribute(localAttribute);
/*  812 */           if (localObject != null) {
/*  813 */             return StyleSheet.this.css.cssValueToStyleConstantsValue((StyleConstants)paramObject, localObject);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  818 */       return super.getAttribute(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ListPainter
/*      */     implements Serializable
/*      */   {
/* 2377 */     static final char[][] romanChars = { { 'i', 'v' }, { 'x', 'l' }, { 'c', 'd' }, { 'm', '?' } };
/*      */     private Rectangle paintRect;
/*      */     private boolean checkedForStart;
/*      */     private int start;
/*      */     private CSS.Value type;
/*      */     URL imageurl;
/* 2441 */     private StyleSheet ss = null;
/* 2442 */     Icon img = null;
/* 2443 */     private int bulletgap = 5;
/*      */     private boolean isLeftToRight;
/*      */ 
/*      */     ListPainter(AttributeSet paramAttributeSet, StyleSheet paramStyleSheet)
/*      */     {
/* 2025 */       this.ss = paramStyleSheet;
/*      */ 
/* 2027 */       String str1 = (String)paramAttributeSet.getAttribute(CSS.Attribute.LIST_STYLE_IMAGE);
/*      */ 
/* 2029 */       this.type = null;
/* 2030 */       if ((str1 != null) && (!str1.equals("none"))) {
/* 2031 */         String str2 = null;
/*      */         try {
/* 2033 */           StringTokenizer localStringTokenizer = new StringTokenizer(str1, "()");
/* 2034 */           if (localStringTokenizer.hasMoreTokens())
/* 2035 */             str2 = localStringTokenizer.nextToken();
/* 2036 */           if (localStringTokenizer.hasMoreTokens())
/* 2037 */             str2 = localStringTokenizer.nextToken();
/* 2038 */           localURL = new URL(str2);
/* 2039 */           this.img = new ImageIcon(localURL);
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException1)
/*      */         {
/*      */           URL localURL;
/* 2041 */           if ((str2 != null) && (paramStyleSheet != null) && (paramStyleSheet.getBase() != null)) {
/*      */             try {
/* 2043 */               localURL = new URL(paramStyleSheet.getBase(), str2);
/* 2044 */               this.img = new ImageIcon(localURL);
/*      */             } catch (MalformedURLException localMalformedURLException2) {
/* 2046 */               this.img = null;
/*      */             }
/*      */           }
/*      */           else {
/* 2050 */             this.img = null;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2056 */       if (this.img == null) {
/* 2057 */         this.type = ((CSS.Value)paramAttributeSet.getAttribute(CSS.Attribute.LIST_STYLE_TYPE));
/*      */       }
/*      */ 
/* 2060 */       this.start = 1;
/*      */ 
/* 2062 */       this.paintRect = new Rectangle();
/*      */     }
/*      */ 
/*      */     private CSS.Value getChildType(View paramView)
/*      */     {
/* 2074 */       CSS.Value localValue = (CSS.Value)paramView.getAttributes().getAttribute(CSS.Attribute.LIST_STYLE_TYPE);
/*      */ 
/* 2077 */       if (localValue == null) {
/* 2078 */         if (this.type == null)
/*      */         {
/* 2080 */           View localView = paramView.getParent();
/* 2081 */           HTMLDocument localHTMLDocument = (HTMLDocument)localView.getDocument();
/* 2082 */           if (HTMLDocument.matchNameAttribute(localView.getElement().getAttributes(), HTML.Tag.OL))
/*      */           {
/* 2084 */             localValue = CSS.Value.DECIMAL;
/*      */           }
/* 2086 */           else localValue = CSS.Value.DISC; 
/*      */         }
/*      */         else
/*      */         {
/* 2089 */           localValue = this.type;
/*      */         }
/*      */       }
/* 2092 */       return localValue;
/*      */     }
/*      */ 
/*      */     private void getStart(View paramView)
/*      */     {
/* 2099 */       this.checkedForStart = true;
/* 2100 */       Element localElement = paramView.getElement();
/* 2101 */       if (localElement != null) {
/* 2102 */         AttributeSet localAttributeSet = localElement.getAttributes();
/*      */         Object localObject;
/* 2104 */         if ((localAttributeSet != null) && (localAttributeSet.isDefined(HTML.Attribute.START)) && ((localObject = localAttributeSet.getAttribute(HTML.Attribute.START)) != null) && ((localObject instanceof String)))
/*      */         {
/*      */           try
/*      */           {
/* 2110 */             this.start = Integer.parseInt((String)localObject);
/*      */           }
/*      */           catch (NumberFormatException localNumberFormatException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getRenderIndex(View paramView, int paramInt)
/*      */     {
/* 2125 */       if (!this.checkedForStart) {
/* 2126 */         getStart(paramView);
/*      */       }
/* 2128 */       int i = paramInt;
/* 2129 */       for (int j = paramInt; j >= 0; j--) {
/* 2130 */         AttributeSet localAttributeSet = paramView.getElement().getElement(j).getAttributes();
/*      */ 
/* 2132 */         if (localAttributeSet.getAttribute(StyleConstants.NameAttribute) != HTML.Tag.LI)
/*      */         {
/* 2134 */           i--;
/* 2135 */         } else if (localAttributeSet.isDefined(HTML.Attribute.VALUE)) {
/* 2136 */           Object localObject = localAttributeSet.getAttribute(HTML.Attribute.VALUE);
/* 2137 */           if ((localObject != null) && ((localObject instanceof String)))
/*      */             try
/*      */             {
/* 2140 */               int k = Integer.parseInt((String)localObject);
/* 2141 */               return i - j + k;
/*      */             }
/*      */             catch (NumberFormatException localNumberFormatException) {
/*      */             }
/*      */         }
/*      */       }
/* 2147 */       return i + this.start;
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, View paramView, int paramInt)
/*      */     {
/* 2164 */       View localView = paramView.getView(paramInt);
/* 2165 */       Container localContainer = paramView.getContainer();
/* 2166 */       Object localObject1 = localView.getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 2170 */       if ((!(localObject1 instanceof HTML.Tag)) || (localObject1 != HTML.Tag.LI))
/*      */       {
/* 2172 */         return;
/*      */       }
/*      */ 
/* 2175 */       this.isLeftToRight = localContainer.getComponentOrientation().isLeftToRight();
/*      */ 
/* 2183 */       float f = 0.0F;
/* 2184 */       if (localView.getViewCount() > 0) {
/* 2185 */         localObject2 = localView.getView(0);
/* 2186 */         localObject3 = ((View)localObject2).getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 2188 */         if (((localObject3 == HTML.Tag.P) || (localObject3 == HTML.Tag.IMPLIED)) && (((View)localObject2).getViewCount() > 0))
/*      */         {
/* 2190 */           this.paintRect.setBounds((int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4);
/* 2191 */           localObject4 = localView.getChildAllocation(0, this.paintRect);
/* 2192 */           if ((localObject4 != null) && ((localObject4 = ((View)localObject2).getView(0).getChildAllocation(0, (Shape)localObject4)) != null))
/*      */           {
/* 2194 */             Rectangle localRectangle = (localObject4 instanceof Rectangle) ? (Rectangle)localObject4 : ((Shape)localObject4).getBounds();
/*      */ 
/* 2197 */             f = ((View)localObject2).getView(0).getAlignment(1);
/* 2198 */             paramFloat2 = localRectangle.y;
/* 2199 */             paramFloat4 = localRectangle.height;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2205 */       Object localObject2 = localContainer.isEnabled() ? localContainer.getForeground() : this.ss != null ? this.ss.getForeground(localView.getAttributes()) : UIManager.getColor("textInactiveText");
/*      */ 
/* 2210 */       paramGraphics.setColor((Color)localObject2);
/*      */ 
/* 2212 */       if (this.img != null) {
/* 2213 */         drawIcon(paramGraphics, (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, localContainer);
/* 2214 */         return;
/*      */       }
/* 2216 */       Object localObject3 = getChildType(localView);
/* 2217 */       Object localObject4 = ((StyledDocument)localView.getDocument()).getFont(localView.getAttributes());
/*      */ 
/* 2219 */       if (localObject4 != null) {
/* 2220 */         paramGraphics.setFont((Font)localObject4);
/*      */       }
/* 2222 */       if ((localObject3 == CSS.Value.SQUARE) || (localObject3 == CSS.Value.CIRCLE) || (localObject3 == CSS.Value.DISC))
/*      */       {
/* 2224 */         drawShape(paramGraphics, (CSS.Value)localObject3, (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f);
/*      */       }
/* 2226 */       else if (localObject3 == CSS.Value.DECIMAL) {
/* 2227 */         drawLetter(paramGraphics, '1', (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, getRenderIndex(paramView, paramInt));
/*      */       }
/* 2229 */       else if (localObject3 == CSS.Value.LOWER_ALPHA) {
/* 2230 */         drawLetter(paramGraphics, 'a', (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, getRenderIndex(paramView, paramInt));
/*      */       }
/* 2232 */       else if (localObject3 == CSS.Value.UPPER_ALPHA) {
/* 2233 */         drawLetter(paramGraphics, 'A', (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, getRenderIndex(paramView, paramInt));
/*      */       }
/* 2235 */       else if (localObject3 == CSS.Value.LOWER_ROMAN) {
/* 2236 */         drawLetter(paramGraphics, 'i', (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, getRenderIndex(paramView, paramInt));
/*      */       }
/* 2238 */       else if (localObject3 == CSS.Value.UPPER_ROMAN)
/* 2239 */         drawLetter(paramGraphics, 'I', (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, f, getRenderIndex(paramView, paramInt));
/*      */     }
/*      */ 
/*      */     void drawIcon(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, Component paramComponent)
/*      */     {
/* 2257 */       int i = this.isLeftToRight ? -(this.img.getIconWidth() + this.bulletgap) : paramInt3 + this.bulletgap;
/*      */ 
/* 2259 */       int j = paramInt1 + i;
/* 2260 */       int k = Math.max(paramInt2, paramInt2 + (int)(paramFloat * paramInt4) - this.img.getIconHeight());
/*      */ 
/* 2262 */       this.img.paintIcon(paramComponent, paramGraphics, j, k);
/*      */     }
/*      */ 
/*      */     void drawShape(Graphics paramGraphics, CSS.Value paramValue, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
/*      */     {
/* 2279 */       int i = this.isLeftToRight ? -(this.bulletgap + 8) : paramInt3 + this.bulletgap;
/* 2280 */       int j = paramInt1 + i;
/* 2281 */       int k = Math.max(paramInt2, paramInt2 + (int)(paramFloat * paramInt4) - 8);
/*      */ 
/* 2283 */       if (paramValue == CSS.Value.SQUARE)
/* 2284 */         paramGraphics.drawRect(j, k, 8, 8);
/* 2285 */       else if (paramValue == CSS.Value.CIRCLE)
/* 2286 */         paramGraphics.drawOval(j, k, 8, 8);
/*      */       else
/* 2288 */         paramGraphics.fillOval(j, k, 8, 8);
/*      */     }
/*      */ 
/*      */     void drawLetter(Graphics paramGraphics, char paramChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, int paramInt5)
/*      */     {
/* 2305 */       String str = formatItemNum(paramInt5, paramChar);
/* 2306 */       str = "." + str;
/* 2307 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(null, paramGraphics);
/* 2308 */       int i = SwingUtilities2.stringWidth(null, localFontMetrics, str);
/* 2309 */       int j = this.isLeftToRight ? -(i + this.bulletgap) : paramInt3 + this.bulletgap;
/*      */ 
/* 2311 */       int k = paramInt1 + j;
/* 2312 */       int m = Math.max(paramInt2 + localFontMetrics.getAscent(), paramInt2 + (int)(paramInt4 * paramFloat));
/* 2313 */       SwingUtilities2.drawString(null, paramGraphics, str, k, m);
/*      */     }
/*      */ 
/*      */     String formatItemNum(int paramInt, char paramChar)
/*      */     {
/* 2324 */       String str1 = "1";
/*      */ 
/* 2326 */       int i = 0;
/*      */       String str2;
/* 2330 */       switch (paramChar) {
/*      */       case '1':
/*      */       default:
/* 2333 */         str2 = String.valueOf(paramInt);
/* 2334 */         break;
/*      */       case 'A':
/* 2337 */         i = 1;
/*      */       case 'a':
/* 2340 */         str2 = formatAlphaNumerals(paramInt);
/* 2341 */         break;
/*      */       case 'I':
/* 2344 */         i = 1;
/*      */       case 'i':
/* 2347 */         str2 = formatRomanNumerals(paramInt);
/*      */       }
/*      */ 
/* 2350 */       if (i != 0) {
/* 2351 */         str2 = str2.toUpperCase();
/*      */       }
/*      */ 
/* 2354 */       return str2;
/*      */     }
/*      */ 
/*      */     String formatAlphaNumerals(int paramInt)
/*      */     {
/*      */       String str;
/* 2365 */       if (paramInt > 26) {
/* 2366 */         str = formatAlphaNumerals(paramInt / 26) + formatAlphaNumerals(paramInt % 26);
/*      */       }
/*      */       else
/*      */       {
/* 2370 */         str = String.valueOf((char)(97 + paramInt - 1));
/*      */       }
/*      */ 
/* 2373 */       return str;
/*      */     }
/*      */ 
/*      */     String formatRomanNumerals(int paramInt)
/*      */     {
/* 2390 */       return formatRomanNumerals(0, paramInt);
/*      */     }
/*      */ 
/*      */     String formatRomanNumerals(int paramInt1, int paramInt2)
/*      */     {
/* 2399 */       if (paramInt2 < 10) {
/* 2400 */         return formatRomanDigit(paramInt1, paramInt2);
/*      */       }
/* 2402 */       return formatRomanNumerals(paramInt1 + 1, paramInt2 / 10) + formatRomanDigit(paramInt1, paramInt2 % 10);
/*      */     }
/*      */ 
/*      */     String formatRomanDigit(int paramInt1, int paramInt2)
/*      */     {
/* 2415 */       String str = "";
/* 2416 */       if (paramInt2 == 9) {
/* 2417 */         str = str + romanChars[paramInt1][0];
/* 2418 */         str = str + romanChars[(paramInt1 + 1)][0];
/* 2419 */         return str;
/* 2420 */       }if (paramInt2 == 4) {
/* 2421 */         str = str + romanChars[paramInt1][0];
/* 2422 */         str = str + romanChars[paramInt1][1];
/* 2423 */         return str;
/* 2424 */       }if (paramInt2 >= 5) {
/* 2425 */         str = str + romanChars[paramInt1][1];
/* 2426 */         paramInt2 -= 5;
/*      */       }
/*      */ 
/* 2429 */       for (int i = 0; i < paramInt2; i++) {
/* 2430 */         str = str + romanChars[paramInt1][0];
/*      */       }
/*      */ 
/* 2433 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ResolvedStyle extends MuxingAttributeSet
/*      */     implements Serializable, Style
/*      */   {
/*      */     String name;
/*      */     private int extendedIndex;
/*      */ 
/*      */     ResolvedStyle(String paramString, AttributeSet[] paramArrayOfAttributeSet, int paramInt)
/*      */     {
/* 2785 */       super();
/* 2786 */       this.name = paramString;
/* 2787 */       this.extendedIndex = paramInt;
/*      */     }
/*      */ 
/*      */     synchronized void insertStyle(Style paramStyle, int paramInt)
/*      */     {
/* 2797 */       AttributeSet[] arrayOfAttributeSet = getAttributes();
/* 2798 */       int i = arrayOfAttributeSet.length;
/* 2799 */       int j = 0;
/* 2800 */       while ((j < this.extendedIndex) && 
/* 2801 */         (paramInt <= StyleSheet.getSpecificity(((Style)arrayOfAttributeSet[j]).getName()))) {
/* 2800 */         j++;
/*      */       }
/*      */ 
/* 2806 */       insertAttributeSetAt(paramStyle, j);
/* 2807 */       this.extendedIndex += 1;
/*      */     }
/*      */ 
/*      */     synchronized void removeStyle(Style paramStyle)
/*      */     {
/* 2815 */       AttributeSet[] arrayOfAttributeSet = getAttributes();
/*      */ 
/* 2817 */       for (int i = arrayOfAttributeSet.length - 1; i >= 0; i--)
/* 2818 */         if (arrayOfAttributeSet[i] == paramStyle) {
/* 2819 */           removeAttributeSetAt(i);
/* 2820 */           if (i >= this.extendedIndex) break;
/* 2821 */           this.extendedIndex -= 1; break;
/*      */         }
/*      */     }
/*      */ 
/*      */     synchronized void insertExtendedStyleAt(Style paramStyle, int paramInt)
/*      */     {
/* 2833 */       insertAttributeSetAt(paramStyle, this.extendedIndex + paramInt);
/*      */     }
/*      */ 
/*      */     synchronized void addExtendedStyle(Style paramStyle)
/*      */     {
/* 2841 */       insertAttributeSetAt(paramStyle, getAttributes().length);
/*      */     }
/*      */ 
/*      */     synchronized void removeExtendedStyleAt(int paramInt)
/*      */     {
/* 2849 */       removeAttributeSetAt(this.extendedIndex + paramInt);
/*      */     }
/*      */ 
/*      */     protected boolean matches(String paramString)
/*      */     {
/* 2858 */       int i = paramString.length();
/*      */ 
/* 2860 */       if (i == 0) {
/* 2861 */         return false;
/*      */       }
/* 2863 */       int j = this.name.length();
/* 2864 */       int k = paramString.lastIndexOf(' ');
/* 2865 */       int m = this.name.lastIndexOf(' ');
/* 2866 */       if (k >= 0) {
/* 2867 */         k++;
/*      */       }
/* 2869 */       if (m >= 0) {
/* 2870 */         m++;
/*      */       }
/* 2872 */       if (!matches(paramString, k, i, m, j)) {
/* 2873 */         return false;
/*      */       }
/* 2875 */       while (k != -1) {
/* 2876 */         i = k - 1;
/* 2877 */         k = paramString.lastIndexOf(' ', i - 1);
/* 2878 */         if (k >= 0) {
/* 2879 */           k++;
/*      */         }
/* 2881 */         boolean bool = false;
/* 2882 */         while ((!bool) && (m != -1)) {
/* 2883 */           j = m - 1;
/* 2884 */           m = this.name.lastIndexOf(' ', j - 1);
/* 2885 */           if (m >= 0) {
/* 2886 */             m++;
/*      */           }
/* 2888 */           bool = matches(paramString, k, i, m, j);
/*      */         }
/*      */ 
/* 2891 */         if (!bool) {
/* 2892 */           return false;
/*      */         }
/*      */       }
/* 2895 */       return true;
/*      */     }
/*      */ 
/*      */     boolean matches(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2905 */       paramInt1 = Math.max(paramInt1, 0);
/* 2906 */       paramInt3 = Math.max(paramInt3, 0);
/* 2907 */       int i = boundedIndexOf(this.name, '.', paramInt3, paramInt4);
/*      */ 
/* 2909 */       int j = boundedIndexOf(this.name, '#', paramInt3, paramInt4);
/*      */ 
/* 2911 */       int k = boundedIndexOf(paramString, '.', paramInt1, paramInt2);
/* 2912 */       int m = boundedIndexOf(paramString, '#', paramInt1, paramInt2);
/* 2913 */       if (k != -1)
/*      */       {
/* 2917 */         if (i == -1) {
/* 2918 */           return false;
/*      */         }
/* 2920 */         if (paramInt1 == k) {
/* 2921 */           if ((paramInt4 - i != paramInt2 - k) || (!paramString.regionMatches(paramInt1, this.name, i, paramInt4 - i)))
/*      */           {
/* 2924 */             return false;
/*      */           }
/*      */ 
/*      */         }
/* 2929 */         else if ((paramInt2 - paramInt1 != paramInt4 - paramInt3) || (!paramString.regionMatches(paramInt1, this.name, paramInt3, paramInt4 - paramInt3)))
/*      */         {
/* 2932 */           return false;
/*      */         }
/*      */ 
/* 2935 */         return true;
/*      */       }
/* 2937 */       if (m != -1)
/*      */       {
/* 2941 */         if (j == -1) {
/* 2942 */           return false;
/*      */         }
/* 2944 */         if (paramInt1 == m) {
/* 2945 */           if ((paramInt4 - j != paramInt2 - m) || (!paramString.regionMatches(paramInt1, this.name, j, paramInt4 - j)))
/*      */           {
/* 2948 */             return false;
/*      */           }
/*      */ 
/*      */         }
/* 2953 */         else if ((paramInt2 - paramInt1 != paramInt4 - paramInt3) || (!paramString.regionMatches(paramInt1, this.name, paramInt3, paramInt4 - paramInt3)))
/*      */         {
/* 2956 */           return false;
/*      */         }
/*      */ 
/* 2959 */         return true;
/*      */       }
/* 2961 */       if (i != -1)
/*      */       {
/* 2963 */         return (i - paramInt3 == paramInt2 - paramInt1) && (paramString.regionMatches(paramInt1, this.name, paramInt3, i - paramInt3));
/*      */       }
/*      */ 
/* 2967 */       if (j != -1)
/*      */       {
/* 2969 */         return (j - paramInt3 == paramInt2 - paramInt1) && (paramString.regionMatches(paramInt1, this.name, paramInt3, j - paramInt3));
/*      */       }
/*      */ 
/* 2974 */       return (paramInt4 - paramInt3 == paramInt2 - paramInt1) && (paramString.regionMatches(paramInt1, this.name, paramInt3, paramInt4 - paramInt3));
/*      */     }
/*      */ 
/*      */     int boundedIndexOf(String paramString, char paramChar, int paramInt1, int paramInt2)
/*      */     {
/* 2986 */       int i = paramString.indexOf(paramChar, paramInt1);
/* 2987 */       if (i >= paramInt2) {
/* 2988 */         return -1;
/*      */       }
/* 2990 */       return i;
/*      */     }
/*      */     public void addAttribute(Object paramObject1, Object paramObject2) {
/*      */     }
/*      */     public void addAttributes(AttributeSet paramAttributeSet) {  } 
/*      */     public void removeAttribute(Object paramObject) {  } 
/*      */     public void removeAttributes(Enumeration<?> paramEnumeration) {  } 
/*      */     public void removeAttributes(AttributeSet paramAttributeSet) {  } 
/*      */     public void setResolveParent(AttributeSet paramAttributeSet) {  } 
/* 2999 */     public String getName() { return this.name; } 
/*      */     public void addChangeListener(ChangeListener paramChangeListener) {
/*      */     }
/*      */     public void removeChangeListener(ChangeListener paramChangeListener) {  } 
/* 3003 */     public ChangeListener[] getChangeListeners() { return new ChangeListener[0]; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class SearchBuffer
/*      */   {
/* 1682 */     static Stack<SearchBuffer> searchBuffers = new Stack();
/*      */ 
/* 1684 */     Vector vector = null;
/* 1685 */     StringBuffer stringBuffer = null;
/* 1686 */     Hashtable hashtable = null;
/*      */ 
/*      */     static SearchBuffer obtainSearchBuffer()
/*      */     {
/*      */       SearchBuffer localSearchBuffer;
/*      */       try
/*      */       {
/* 1695 */         if (!searchBuffers.empty())
/* 1696 */           localSearchBuffer = (SearchBuffer)searchBuffers.pop();
/*      */         else
/* 1698 */           localSearchBuffer = new SearchBuffer();
/*      */       }
/*      */       catch (EmptyStackException localEmptyStackException) {
/* 1701 */         localSearchBuffer = new SearchBuffer();
/*      */       }
/* 1703 */       return localSearchBuffer;
/*      */     }
/*      */ 
/*      */     static void releaseSearchBuffer(SearchBuffer paramSearchBuffer)
/*      */     {
/* 1711 */       paramSearchBuffer.empty();
/* 1712 */       searchBuffers.push(paramSearchBuffer);
/*      */     }
/*      */ 
/*      */     StringBuffer getStringBuffer() {
/* 1716 */       if (this.stringBuffer == null) {
/* 1717 */         this.stringBuffer = new StringBuffer();
/*      */       }
/* 1719 */       return this.stringBuffer;
/*      */     }
/*      */ 
/*      */     Vector getVector() {
/* 1723 */       if (this.vector == null) {
/* 1724 */         this.vector = new Vector();
/*      */       }
/* 1726 */       return this.vector;
/*      */     }
/*      */ 
/*      */     Hashtable getHashtable() {
/* 1730 */       if (this.hashtable == null) {
/* 1731 */         this.hashtable = new Hashtable();
/*      */       }
/* 1733 */       return this.hashtable;
/*      */     }
/*      */ 
/*      */     void empty() {
/* 1737 */       if (this.stringBuffer != null) {
/* 1738 */         this.stringBuffer.setLength(0);
/*      */       }
/* 1740 */       if (this.vector != null) {
/* 1741 */         this.vector.removeAllElements();
/*      */       }
/* 1743 */       if (this.hashtable != null)
/* 1744 */         this.hashtable.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SelectorMapping
/*      */     implements Serializable
/*      */   {
/*      */     private int specificity;
/*      */     private Style style;
/*      */     private HashMap<String, SelectorMapping> children;
/*      */ 
/*      */     public SelectorMapping(int paramInt)
/*      */     {
/* 3025 */       this.specificity = paramInt;
/*      */     }
/*      */ 
/*      */     public int getSpecificity()
/*      */     {
/* 3032 */       return this.specificity;
/*      */     }
/*      */ 
/*      */     public void setStyle(Style paramStyle)
/*      */     {
/* 3039 */       this.style = paramStyle;
/*      */     }
/*      */ 
/*      */     public Style getStyle()
/*      */     {
/* 3046 */       return this.style;
/*      */     }
/*      */ 
/*      */     public SelectorMapping getChildSelectorMapping(String paramString, boolean paramBoolean)
/*      */     {
/* 3057 */       SelectorMapping localSelectorMapping = null;
/*      */ 
/* 3059 */       if (this.children != null) {
/* 3060 */         localSelectorMapping = (SelectorMapping)this.children.get(paramString);
/*      */       }
/* 3062 */       else if (paramBoolean) {
/* 3063 */         this.children = new HashMap(7);
/*      */       }
/* 3065 */       if ((localSelectorMapping == null) && (paramBoolean)) {
/* 3066 */         int i = getChildSpecificity(paramString);
/*      */ 
/* 3068 */         localSelectorMapping = createChildSelectorMapping(i);
/* 3069 */         this.children.put(paramString, localSelectorMapping);
/*      */       }
/* 3071 */       return localSelectorMapping;
/*      */     }
/*      */ 
/*      */     protected SelectorMapping createChildSelectorMapping(int paramInt)
/*      */     {
/* 3079 */       return new SelectorMapping(paramInt);
/*      */     }
/*      */ 
/*      */     protected int getChildSpecificity(String paramString)
/*      */     {
/* 3089 */       int i = paramString.charAt(0);
/* 3090 */       int j = getSpecificity();
/*      */ 
/* 3092 */       if (i == 46) {
/* 3093 */         j += 100;
/*      */       }
/* 3095 */       else if (i == 35) {
/* 3096 */         j += 10000;
/*      */       }
/*      */       else {
/* 3099 */         j++;
/* 3100 */         if (paramString.indexOf('.') != -1) {
/* 3101 */           j += 100;
/*      */         }
/* 3103 */         if (paramString.indexOf('#') != -1) {
/* 3104 */           j += 10000;
/*      */         }
/*      */       }
/* 3107 */       return j;
/*      */     }
/*      */   }
/*      */ 
/*      */   class SmallConversionSet extends StyleContext.SmallAttributeSet
/*      */   {
/*      */     public SmallConversionSet(AttributeSet arg2)
/*      */     {
/*  834 */       super(localAttributeSet);
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/*  845 */       if ((paramObject instanceof StyleConstants)) {
/*  846 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/*  848 */         if (localAttribute != null) {
/*  849 */           return super.isDefined(localAttribute);
/*      */         }
/*      */       }
/*  852 */       return super.isDefined(paramObject);
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/*  863 */       if ((paramObject instanceof StyleConstants)) {
/*  864 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/*  866 */         if (localAttribute != null) {
/*  867 */           Object localObject = super.getAttribute(localAttribute);
/*  868 */           if (localObject != null) {
/*  869 */             return StyleSheet.this.css.cssValueToStyleConstantsValue((StyleConstants)paramObject, localObject);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  874 */       return super.getAttribute(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   class ViewAttributeSet extends MuxingAttributeSet
/*      */   {
/*      */     View host;
/*      */ 
/*      */     ViewAttributeSet(View arg2)
/*      */     {
/*      */       Object localObject1;
/* 2620 */       this.host = localObject1;
/*      */ 
/* 2624 */       Document localDocument = localObject1.getDocument();
/* 2625 */       StyleSheet.SearchBuffer localSearchBuffer = StyleSheet.SearchBuffer.obtainSearchBuffer();
/* 2626 */       Vector localVector = localSearchBuffer.getVector();
/*      */       try {
/* 2628 */         if ((localDocument instanceof HTMLDocument)) {
/* 2629 */           localObject2 = StyleSheet.this;
/* 2630 */           Element localElement = localObject1.getElement();
/* 2631 */           AttributeSet localAttributeSet1 = localElement.getAttributes();
/* 2632 */           AttributeSet localAttributeSet2 = ((StyleSheet)localObject2).translateHTMLToCSS(localAttributeSet1);
/*      */ 
/* 2634 */           if (localAttributeSet2.getAttributeCount() != 0)
/* 2635 */             localVector.addElement(localAttributeSet2);
/*      */           Object localObject3;
/*      */           Object localObject4;
/* 2637 */           if (localElement.isLeaf()) {
/* 2638 */             localObject3 = localAttributeSet1.getAttributeNames();
/* 2639 */             while (((Enumeration)localObject3).hasMoreElements()) {
/* 2640 */               localObject4 = ((Enumeration)localObject3).nextElement();
/* 2641 */               if ((localObject4 instanceof HTML.Tag))
/*      */               {
/*      */                 Object localObject5;
/* 2642 */                 if (localObject4 == HTML.Tag.A) {
/* 2643 */                   localObject5 = localAttributeSet1.getAttribute(localObject4);
/*      */ 
/* 2657 */                   if ((localObject5 != null) && ((localObject5 instanceof AttributeSet))) {
/* 2658 */                     AttributeSet localAttributeSet3 = (AttributeSet)localObject5;
/* 2659 */                     if (localAttributeSet3.getAttribute(HTML.Attribute.HREF) == null)
/*      */                       continue;
/*      */                   }
/*      */                 }
/*      */                 else {
/* 2664 */                   localObject5 = ((StyleSheet)localObject2).getRule((HTML.Tag)localObject4, localElement);
/* 2665 */                   if (localObject5 != null)
/* 2666 */                     localVector.addElement(localObject5);
/*      */                 }
/*      */               }
/*      */             }
/*      */           } else {
/* 2671 */             localObject3 = (HTML.Tag)localAttributeSet1.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 2673 */             localObject4 = ((StyleSheet)localObject2).getRule((HTML.Tag)localObject3, localElement);
/* 2674 */             if (localObject4 != null) {
/* 2675 */               localVector.addElement(localObject4);
/*      */             }
/*      */           }
/*      */         }
/* 2679 */         Object localObject2 = new AttributeSet[localVector.size()];
/* 2680 */         localVector.copyInto((Object[])localObject2);
/* 2681 */         setAttributes((AttributeSet[])localObject2);
/*      */       }
/*      */       finally {
/* 2684 */         StyleSheet.SearchBuffer.releaseSearchBuffer(localSearchBuffer);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/* 2701 */       if ((paramObject instanceof StyleConstants)) {
/* 2702 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/* 2704 */         if (localAttribute != null) {
/* 2705 */           paramObject = localAttribute;
/*      */         }
/*      */       }
/* 2708 */       return super.isDefined(paramObject);
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/* 2721 */       if ((paramObject instanceof StyleConstants)) {
/* 2722 */         CSS.Attribute localAttribute = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants)paramObject);
/*      */ 
/* 2724 */         if (localAttribute != null) {
/* 2725 */           Object localObject = doGetAttribute(localAttribute);
/* 2726 */           if ((localObject instanceof CSS.CssValue)) {
/* 2727 */             return ((CSS.CssValue)localObject).toStyleConstants((StyleConstants)paramObject, this.host);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2732 */       return doGetAttribute(paramObject);
/*      */     }
/*      */ 
/*      */     Object doGetAttribute(Object paramObject) {
/* 2736 */       Object localObject = super.getAttribute(paramObject);
/* 2737 */       if (localObject != null) {
/* 2738 */         return localObject;
/*      */       }
/*      */ 
/* 2742 */       if ((paramObject instanceof CSS.Attribute)) {
/* 2743 */         CSS.Attribute localAttribute = (CSS.Attribute)paramObject;
/* 2744 */         if (localAttribute.isInherited()) {
/* 2745 */           AttributeSet localAttributeSet = getResolveParent();
/* 2746 */           if (localAttributeSet != null)
/* 2747 */             return localAttributeSet.getAttribute(paramObject);
/*      */         }
/*      */       }
/* 2750 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/* 2761 */       if (this.host == null) {
/* 2762 */         return null;
/*      */       }
/* 2764 */       View localView = this.host.getParent();
/* 2765 */       return localView != null ? localView.getAttributes() : null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.StyleSheet
 * JD-Core Version:    0.6.2
 */