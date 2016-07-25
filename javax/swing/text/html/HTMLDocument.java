/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.io.IOException;
/*      */ import java.io.StringReader;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.DefaultButtonModel;
/*      */ import javax.swing.JToggleButton.ToggleButtonModel;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.EventType;
/*      */ import javax.swing.event.UndoableEditEvent;
/*      */ import javax.swing.text.AbstractDocument.AbstractElement;
/*      */ import javax.swing.text.AbstractDocument.BranchElement;
/*      */ import javax.swing.text.AbstractDocument.Content;
/*      */ import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
/*      */ import javax.swing.text.AbstractDocument.ElementEdit;
/*      */ import javax.swing.text.AbstractDocument.LeafElement;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.DefaultStyledDocument;
/*      */ import javax.swing.text.DefaultStyledDocument.AttributeUndoableEdit;
/*      */ import javax.swing.text.DefaultStyledDocument.ElementSpec;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.ElementIterator;
/*      */ import javax.swing.text.GapContent;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.PlainDocument;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.undo.UndoableEdit;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class HTMLDocument extends DefaultStyledDocument
/*      */ {
/* 1752 */   private boolean frameDocument = false;
/* 1753 */   private boolean preservesUnknownTags = true;
/*      */   private HashMap<String, ButtonGroup> radioButtonGroupsMap;
/*      */   static final String TokenThreshold = "token threshold";
/*      */   private static final int MaxThreshold = 10000;
/*      */   private static final int StepThreshold = 5;
/*      */   public static final String AdditionalComments = "AdditionalComments";
/*      */   static final String StyleType = "StyleType";
/*      */   URL base;
/* 1795 */   boolean hasBaseTag = false;
/*      */ 
/* 1800 */   private String baseTarget = null;
/*      */   private HTMLEditorKit.Parser parser;
/*      */   private static AttributeSet contentAttributeSet;
/* 1816 */   static String MAP_PROPERTY = "__MAP__";
/*      */   private static char[] NEWLINE;
/*      */   private static final String I18NProperty = "i18n";
/*      */ 
/*      */   public HTMLDocument()
/*      */   {
/*  281 */     this(new GapContent(4096), new StyleSheet());
/*      */   }
/*      */ 
/*      */   public HTMLDocument(StyleSheet paramStyleSheet)
/*      */   {
/*  294 */     this(new GapContent(4096), paramStyleSheet);
/*      */   }
/*      */ 
/*      */   public HTMLDocument(AbstractDocument.Content paramContent, StyleSheet paramStyleSheet)
/*      */   {
/*  306 */     super(paramContent, paramStyleSheet);
/*      */   }
/*      */ 
/*      */   public HTMLEditorKit.ParserCallback getReader(int paramInt)
/*      */   {
/*  322 */     Object localObject = getProperty("stream");
/*  323 */     if ((localObject instanceof URL)) {
/*  324 */       setBase((URL)localObject);
/*      */     }
/*  326 */     HTMLReader localHTMLReader = new HTMLReader(paramInt);
/*  327 */     return localHTMLReader;
/*      */   }
/*      */ 
/*      */   public HTMLEditorKit.ParserCallback getReader(int paramInt1, int paramInt2, int paramInt3, HTML.Tag paramTag)
/*      */   {
/*  353 */     return getReader(paramInt1, paramInt2, paramInt3, paramTag, true);
/*      */   }
/*      */ 
/*      */   HTMLEditorKit.ParserCallback getReader(int paramInt1, int paramInt2, int paramInt3, HTML.Tag paramTag, boolean paramBoolean)
/*      */   {
/*  379 */     Object localObject = getProperty("stream");
/*  380 */     if ((localObject instanceof URL)) {
/*  381 */       setBase((URL)localObject);
/*      */     }
/*  383 */     HTMLReader localHTMLReader = new HTMLReader(paramInt1, paramInt2, paramInt3, paramTag, paramBoolean, false, true);
/*      */ 
/*  386 */     return localHTMLReader;
/*      */   }
/*      */ 
/*      */   public URL getBase()
/*      */   {
/*  398 */     return this.base;
/*      */   }
/*      */ 
/*      */   public void setBase(URL paramURL)
/*      */   {
/*  412 */     this.base = paramURL;
/*  413 */     getStyleSheet().setBase(paramURL);
/*      */   }
/*      */ 
/*      */   protected void insert(int paramInt, DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec)
/*      */     throws BadLocationException
/*      */   {
/*  431 */     super.insert(paramInt, paramArrayOfElementSpec);
/*      */   }
/*      */ 
/*      */   protected void insertUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent, AttributeSet paramAttributeSet)
/*      */   {
/*  444 */     if (paramAttributeSet == null) {
/*  445 */       paramAttributeSet = contentAttributeSet;
/*      */     }
/*  449 */     else if (paramAttributeSet.isDefined(StyleConstants.ComposedTextAttribute)) {
/*  450 */       ((MutableAttributeSet)paramAttributeSet).addAttributes(contentAttributeSet);
/*      */     }
/*      */ 
/*  453 */     if (paramAttributeSet.isDefined("CR")) {
/*  454 */       ((MutableAttributeSet)paramAttributeSet).removeAttribute("CR");
/*      */     }
/*      */ 
/*  457 */     super.insertUpdate(paramDefaultDocumentEvent, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected void create(DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec)
/*      */   {
/*  469 */     super.create(paramArrayOfElementSpec);
/*      */   }
/*      */ 
/*      */   public void setParagraphAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  488 */       writeLock();
/*      */ 
/*  490 */       int i = Math.min(paramInt1 + paramInt2, getLength());
/*  491 */       Element localElement1 = getParagraphElement(paramInt1);
/*  492 */       paramInt1 = localElement1.getStartOffset();
/*  493 */       localElement1 = getParagraphElement(i);
/*  494 */       paramInt2 = Math.max(0, localElement1.getEndOffset() - paramInt1);
/*  495 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt1, paramInt2, DocumentEvent.EventType.CHANGE);
/*      */ 
/*  498 */       AttributeSet localAttributeSet = paramAttributeSet.copyAttributes();
/*  499 */       int j = 2147483647;
/*  500 */       for (int k = paramInt1; k <= i; k = j) {
/*  501 */         Element localElement2 = getParagraphElement(k);
/*  502 */         if (j == localElement2.getEndOffset()) {
/*  503 */           j++;
/*      */         }
/*      */         else {
/*  506 */           j = localElement2.getEndOffset();
/*      */         }
/*  508 */         MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)localElement2.getAttributes();
/*      */ 
/*  510 */         localDefaultDocumentEvent.addEdit(new DefaultStyledDocument.AttributeUndoableEdit(localElement2, localAttributeSet, paramBoolean));
/*  511 */         if (paramBoolean) {
/*  512 */           localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*      */         }
/*  514 */         localMutableAttributeSet.addAttributes(paramAttributeSet);
/*      */       }
/*  516 */       localDefaultDocumentEvent.end();
/*  517 */       fireChangedUpdate(localDefaultDocumentEvent);
/*  518 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } finally {
/*  520 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public StyleSheet getStyleSheet()
/*      */   {
/*  531 */     return (StyleSheet)getAttributeContext();
/*      */   }
/*      */ 
/*      */   public Iterator getIterator(HTML.Tag paramTag)
/*      */   {
/*  545 */     if (paramTag.isBlock())
/*      */     {
/*  547 */       return null;
/*      */     }
/*  549 */     return new LeafIterator(paramTag, this);
/*      */   }
/*      */ 
/*      */   protected Element createLeafElement(Element paramElement, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
/*      */   {
/*  565 */     return new RunElement(paramElement, paramAttributeSet, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected Element createBranchElement(Element paramElement, AttributeSet paramAttributeSet)
/*      */   {
/*  578 */     return new BlockElement(paramElement, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected AbstractDocument.AbstractElement createDefaultRoot()
/*      */   {
/*  592 */     writeLock();
/*  593 */     SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*  594 */     localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.HTML);
/*  595 */     BlockElement localBlockElement1 = new BlockElement(null, localSimpleAttributeSet.copyAttributes());
/*  596 */     localSimpleAttributeSet.removeAttributes(localSimpleAttributeSet);
/*  597 */     localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.BODY);
/*  598 */     BlockElement localBlockElement2 = new BlockElement(localBlockElement1, localSimpleAttributeSet.copyAttributes());
/*  599 */     localSimpleAttributeSet.removeAttributes(localSimpleAttributeSet);
/*  600 */     localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.P);
/*  601 */     getStyleSheet().addCSSAttributeFromHTML(localSimpleAttributeSet, CSS.Attribute.MARGIN_TOP, "0");
/*  602 */     BlockElement localBlockElement3 = new BlockElement(localBlockElement2, localSimpleAttributeSet.copyAttributes());
/*  603 */     localSimpleAttributeSet.removeAttributes(localSimpleAttributeSet);
/*  604 */     localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*  605 */     RunElement localRunElement = new RunElement(localBlockElement3, localSimpleAttributeSet, 0, 1);
/*  606 */     Element[] arrayOfElement = new Element[1];
/*  607 */     arrayOfElement[0] = localRunElement;
/*  608 */     localBlockElement3.replace(0, 0, arrayOfElement);
/*  609 */     arrayOfElement[0] = localBlockElement3;
/*  610 */     localBlockElement2.replace(0, 0, arrayOfElement);
/*  611 */     arrayOfElement[0] = localBlockElement2;
/*  612 */     localBlockElement1.replace(0, 0, arrayOfElement);
/*  613 */     writeUnlock();
/*  614 */     return localBlockElement1;
/*      */   }
/*      */ 
/*      */   public void setTokenThreshold(int paramInt)
/*      */   {
/*  624 */     putProperty("token threshold", new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public int getTokenThreshold()
/*      */   {
/*  635 */     Integer localInteger = (Integer)getProperty("token threshold");
/*  636 */     if (localInteger != null) {
/*  637 */       return localInteger.intValue();
/*      */     }
/*  639 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   public void setPreservesUnknownTags(boolean paramBoolean)
/*      */   {
/*  652 */     this.preservesUnknownTags = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getPreservesUnknownTags()
/*      */   {
/*  663 */     return this.preservesUnknownTags;
/*      */   }
/*      */ 
/*      */   public void processHTMLFrameHyperlinkEvent(HTMLFrameHyperlinkEvent paramHTMLFrameHyperlinkEvent)
/*      */   {
/*  707 */     String str1 = paramHTMLFrameHyperlinkEvent.getTarget();
/*  708 */     Element localElement1 = paramHTMLFrameHyperlinkEvent.getSourceElement();
/*  709 */     String str2 = paramHTMLFrameHyperlinkEvent.getURL().toString();
/*      */ 
/*  711 */     if (str1.equals("_self"))
/*      */     {
/*  716 */       updateFrame(localElement1, str2);
/*  717 */     } else if (str1.equals("_parent"))
/*      */     {
/*  721 */       updateFrameSet(localElement1.getParentElement(), str2);
/*      */     }
/*      */     else
/*      */     {
/*  726 */       Element localElement2 = findFrame(str1);
/*  727 */       if (localElement2 != null)
/*  728 */         updateFrame(localElement2, str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Element findFrame(String paramString)
/*      */   {
/*  744 */     ElementIterator localElementIterator = new ElementIterator(this);
/*      */     Element localElement;
/*  747 */     while ((localElement = localElementIterator.next()) != null) {
/*  748 */       AttributeSet localAttributeSet = localElement.getAttributes();
/*  749 */       if (matchNameAttribute(localAttributeSet, HTML.Tag.FRAME)) {
/*  750 */         String str = (String)localAttributeSet.getAttribute(HTML.Attribute.NAME);
/*  751 */         if ((str != null) && (str.equals(paramString))) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  756 */     return localElement;
/*      */   }
/*      */ 
/*      */   static boolean matchNameAttribute(AttributeSet paramAttributeSet, HTML.Tag paramTag)
/*      */   {
/*  769 */     Object localObject = paramAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*  770 */     if ((localObject instanceof HTML.Tag)) {
/*  771 */       HTML.Tag localTag = (HTML.Tag)localObject;
/*  772 */       if (localTag == paramTag) {
/*  773 */         return true;
/*      */       }
/*      */     }
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */   private void updateFrameSet(Element paramElement, String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  788 */       int i = paramElement.getStartOffset();
/*  789 */       int j = Math.min(getLength(), paramElement.getEndOffset());
/*  790 */       String str = "<frame";
/*  791 */       if (paramString != null) {
/*  792 */         str = str + " src=\"" + paramString + "\"";
/*      */       }
/*  794 */       str = str + ">";
/*  795 */       installParserIfNecessary();
/*  796 */       setOuterHTML(paramElement, str);
/*      */     }
/*      */     catch (BadLocationException localBadLocationException)
/*      */     {
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateFrame(Element paramElement, String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  815 */       writeLock();
/*  816 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramElement.getStartOffset(), 1, DocumentEvent.EventType.CHANGE);
/*      */ 
/*  819 */       AttributeSet localAttributeSet = paramElement.getAttributes().copyAttributes();
/*  820 */       MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)paramElement.getAttributes();
/*  821 */       localDefaultDocumentEvent.addEdit(new DefaultStyledDocument.AttributeUndoableEdit(paramElement, localAttributeSet, false));
/*  822 */       localMutableAttributeSet.removeAttribute(HTML.Attribute.SRC);
/*  823 */       localMutableAttributeSet.addAttribute(HTML.Attribute.SRC, paramString);
/*  824 */       localDefaultDocumentEvent.end();
/*  825 */       fireChangedUpdate(localDefaultDocumentEvent);
/*  826 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } finally {
/*  828 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isFrameDocument()
/*      */   {
/*  838 */     return this.frameDocument;
/*      */   }
/*      */ 
/*      */   void setFrameDocumentState(boolean paramBoolean)
/*      */   {
/*  848 */     this.frameDocument = paramBoolean;
/*      */   }
/*      */ 
/*      */   void addMap(Map paramMap)
/*      */   {
/*  858 */     String str = paramMap.getName();
/*      */ 
/*  860 */     if (str != null) {
/*  861 */       Object localObject = getProperty(MAP_PROPERTY);
/*      */ 
/*  863 */       if (localObject == null) {
/*  864 */         localObject = new Hashtable(11);
/*  865 */         putProperty(MAP_PROPERTY, localObject);
/*      */       }
/*  867 */       if ((localObject instanceof Hashtable))
/*  868 */         ((Hashtable)localObject).put("#" + str, paramMap);
/*      */     }
/*      */   }
/*      */ 
/*      */   void removeMap(Map paramMap)
/*      */   {
/*  878 */     String str = paramMap.getName();
/*      */ 
/*  880 */     if (str != null) {
/*  881 */       Object localObject = getProperty(MAP_PROPERTY);
/*      */ 
/*  883 */       if ((localObject instanceof Hashtable))
/*  884 */         ((Hashtable)localObject).remove("#" + str);
/*      */     }
/*      */   }
/*      */ 
/*      */   Map getMap(String paramString)
/*      */   {
/*  896 */     if (paramString != null) {
/*  897 */       Object localObject = getProperty(MAP_PROPERTY);
/*      */ 
/*  899 */       if ((localObject != null) && ((localObject instanceof Hashtable))) {
/*  900 */         return (Map)((Hashtable)localObject).get(paramString);
/*      */       }
/*      */     }
/*  903 */     return null;
/*      */   }
/*      */ 
/*      */   Enumeration getMaps()
/*      */   {
/*  912 */     Object localObject = getProperty(MAP_PROPERTY);
/*      */ 
/*  914 */     if ((localObject instanceof Hashtable)) {
/*  915 */       return ((Hashtable)localObject).elements();
/*      */     }
/*  917 */     return null;
/*      */   }
/*      */ 
/*      */   void setDefaultStyleSheetType(String paramString)
/*      */   {
/*  927 */     putProperty("StyleType", paramString);
/*      */   }
/*      */ 
/*      */   String getDefaultStyleSheetType()
/*      */   {
/*  937 */     String str = (String)getProperty("StyleType");
/*  938 */     if (str == null) {
/*  939 */       return "text/css";
/*      */     }
/*  941 */     return str;
/*      */   }
/*      */ 
/*      */   public void setParser(HTMLEditorKit.Parser paramParser)
/*      */   {
/*  957 */     this.parser = paramParser;
/*  958 */     putProperty("__PARSER__", null);
/*      */   }
/*      */ 
/*      */   public HTMLEditorKit.Parser getParser()
/*      */   {
/*  969 */     Object localObject = getProperty("__PARSER__");
/*      */ 
/*  971 */     if ((localObject instanceof HTMLEditorKit.Parser)) {
/*  972 */       return (HTMLEditorKit.Parser)localObject;
/*      */     }
/*  974 */     return this.parser;
/*      */   }
/*      */ 
/*      */   public void setInnerHTML(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1028 */     verifyParser();
/* 1029 */     if ((paramElement != null) && (paramElement.isLeaf())) {
/* 1030 */       throw new IllegalArgumentException("Can not set inner HTML of a leaf");
/*      */     }
/*      */ 
/* 1033 */     if ((paramElement != null) && (paramString != null)) {
/* 1034 */       int i = paramElement.getElementCount();
/* 1035 */       int j = paramElement.getStartOffset();
/* 1036 */       insertHTML(paramElement, paramElement.getStartOffset(), paramString, true);
/* 1037 */       if (paramElement.getElementCount() > i)
/*      */       {
/* 1039 */         removeElements(paramElement, paramElement.getElementCount() - i, i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOuterHTML(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1105 */     verifyParser();
/* 1106 */     if ((paramElement != null) && (paramElement.getParentElement() != null) && (paramString != null))
/*      */     {
/* 1108 */       int i = paramElement.getStartOffset();
/* 1109 */       int j = paramElement.getEndOffset();
/* 1110 */       int k = getLength();
/*      */ 
/* 1113 */       boolean bool = !paramElement.isLeaf();
/* 1114 */       if ((!bool) && ((j > k) || (getText(j - 1, 1).charAt(0) == NEWLINE[0])))
/*      */       {
/* 1116 */         bool = true;
/*      */       }
/* 1118 */       Element localElement = paramElement.getParentElement();
/* 1119 */       int m = localElement.getElementCount();
/* 1120 */       insertHTML(localElement, i, paramString, bool);
/*      */ 
/* 1122 */       int n = getLength();
/* 1123 */       if (m != localElement.getElementCount()) {
/* 1124 */         int i1 = localElement.getElementIndex(i + n - k);
/*      */ 
/* 1126 */         removeElements(localElement, i1, 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertAfterStart(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1183 */     verifyParser();
/* 1184 */     if ((paramElement != null) && (paramElement.isLeaf())) {
/* 1185 */       throw new IllegalArgumentException("Can not insert HTML after start of a leaf");
/*      */     }
/*      */ 
/* 1188 */     insertHTML(paramElement, paramElement.getStartOffset(), paramString, false);
/*      */   }
/*      */ 
/*      */   public void insertBeforeEnd(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1248 */     verifyParser();
/* 1249 */     if ((paramElement != null) && (paramElement.isLeaf())) {
/* 1250 */       throw new IllegalArgumentException("Can not set inner HTML before end of leaf");
/*      */     }
/*      */ 
/* 1253 */     if (paramElement != null) {
/* 1254 */       int i = paramElement.getEndOffset();
/* 1255 */       if ((paramElement.getElement(paramElement.getElementIndex(i - 1)).isLeaf()) && (getText(i - 1, 1).charAt(0) == NEWLINE[0]))
/*      */       {
/* 1257 */         i--;
/*      */       }
/* 1259 */       insertHTML(paramElement, i, paramString, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertBeforeStart(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1311 */     verifyParser();
/* 1312 */     if (paramElement != null) {
/* 1313 */       Element localElement = paramElement.getParentElement();
/*      */ 
/* 1315 */       if (localElement != null)
/* 1316 */         insertHTML(localElement, paramElement.getStartOffset(), paramString, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertAfterEnd(Element paramElement, String paramString)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1369 */     verifyParser();
/* 1370 */     if (paramElement != null) {
/* 1371 */       Element localElement = paramElement.getParentElement();
/*      */ 
/* 1373 */       if (localElement != null) {
/* 1374 */         int i = paramElement.getEndOffset();
/* 1375 */         if (i > getLength()) {
/* 1376 */           i--;
/*      */         }
/* 1378 */         else if ((paramElement.isLeaf()) && (getText(i - 1, 1).charAt(0) == NEWLINE[0]))
/*      */         {
/* 1380 */           i--;
/*      */         }
/* 1382 */         insertHTML(localElement, i, paramString, false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Element getElement(String paramString)
/*      */   {
/* 1406 */     if (paramString == null) {
/* 1407 */       return null;
/*      */     }
/* 1409 */     return getElement(getDefaultRootElement(), HTML.Attribute.ID, paramString, true);
/*      */   }
/*      */ 
/*      */   public Element getElement(Element paramElement, Object paramObject1, Object paramObject2)
/*      */   {
/* 1428 */     return getElement(paramElement, paramObject1, paramObject2, true);
/*      */   }
/*      */ 
/*      */   private Element getElement(Element paramElement, Object paramObject1, Object paramObject2, boolean paramBoolean)
/*      */   {
/* 1450 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*      */ 
/* 1452 */     if ((localAttributeSet != null) && (localAttributeSet.isDefined(paramObject1)) && 
/* 1453 */       (paramObject2.equals(localAttributeSet.getAttribute(paramObject1))))
/* 1454 */       return paramElement;
/*      */     Object localObject2;
/* 1457 */     if (!paramElement.isLeaf()) {
/* 1458 */       int i = 0; int j = paramElement.getElementCount();
/* 1459 */       for (; i < j; i++) {
/* 1460 */         localObject2 = getElement(paramElement.getElement(i), paramObject1, paramObject2, paramBoolean);
/*      */ 
/* 1463 */         if (localObject2 != null) {
/* 1464 */           return localObject2;
/*      */         }
/*      */       }
/*      */     }
/* 1468 */     else if ((paramBoolean) && (localAttributeSet != null))
/*      */     {
/* 1471 */       Enumeration localEnumeration = localAttributeSet.getAttributeNames();
/* 1472 */       if (localEnumeration != null) {
/* 1473 */         while (localEnumeration.hasMoreElements()) {
/* 1474 */           Object localObject1 = localEnumeration.nextElement();
/* 1475 */           if (((localObject1 instanceof HTML.Tag)) && ((localAttributeSet.getAttribute(localObject1) instanceof AttributeSet)))
/*      */           {
/* 1478 */             localObject2 = (AttributeSet)localAttributeSet.getAttribute(localObject1);
/*      */ 
/* 1480 */             if ((((AttributeSet)localObject2).isDefined(paramObject1)) && (paramObject2.equals(((AttributeSet)localObject2).getAttribute(paramObject1))))
/*      */             {
/* 1482 */               return paramElement;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1488 */     return null;
/*      */   }
/*      */ 
/*      */   private void verifyParser()
/*      */   {
/* 1499 */     if (getParser() == null)
/* 1500 */       throw new IllegalStateException("No HTMLEditorKit.Parser");
/*      */   }
/*      */ 
/*      */   private void installParserIfNecessary()
/*      */   {
/* 1508 */     if (getParser() == null)
/* 1509 */       setParser(new HTMLEditorKit().getParser());
/*      */   }
/*      */ 
/*      */   private void insertHTML(Element paramElement, int paramInt, String paramString, boolean paramBoolean)
/*      */     throws BadLocationException, IOException
/*      */   {
/* 1522 */     if ((paramElement != null) && (paramString != null)) {
/* 1523 */       HTMLEditorKit.Parser localParser = getParser();
/* 1524 */       if (localParser != null) {
/* 1525 */         int i = Math.max(0, paramInt - 1);
/* 1526 */         Element localElement1 = getCharacterElement(i);
/* 1527 */         Element localElement2 = paramElement;
/* 1528 */         int j = 0;
/* 1529 */         int k = 0;
/*      */ 
/* 1531 */         if (paramElement.getStartOffset() > i) {
/* 1532 */           while ((localElement2 != null) && (localElement2.getStartOffset() > i))
/*      */           {
/* 1534 */             localElement2 = localElement2.getParentElement();
/* 1535 */             k++;
/*      */           }
/* 1537 */           if (localElement2 == null) {
/* 1538 */             throw new BadLocationException("No common parent", paramInt);
/*      */           }
/*      */         }
/*      */ 
/* 1542 */         while ((localElement1 != null) && (localElement1 != localElement2)) {
/* 1543 */           j++;
/* 1544 */           localElement1 = localElement1.getParentElement();
/*      */         }
/* 1546 */         if (localElement1 != null)
/*      */         {
/* 1548 */           HTMLReader localHTMLReader = new HTMLReader(paramInt, j - 1, k, null, false, true, paramBoolean);
/*      */ 
/* 1552 */           localParser.parse(new StringReader(paramString), localHTMLReader, true);
/* 1553 */           localHTMLReader.flush();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeElements(Element paramElement, int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/* 1569 */     writeLock();
/*      */     try {
/* 1571 */       int i = paramElement.getElement(paramInt1).getStartOffset();
/* 1572 */       int j = paramElement.getElement(paramInt1 + paramInt2 - 1).getEndOffset();
/* 1573 */       if (j > getLength()) {
/* 1574 */         removeElementsAtEnd(paramElement, paramInt1, paramInt2, i, j);
/*      */       }
/*      */       else
/* 1577 */         removeElements(paramElement, paramInt1, paramInt2, i, j);
/*      */     }
/*      */     finally {
/* 1580 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeElementsAtEnd(Element paramElement, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws BadLocationException
/*      */   {
/* 1597 */     boolean bool = paramElement.getElement(paramInt1 - 1).isLeaf();
/* 1598 */     AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt3 - 1, paramInt4 - paramInt3 + 1, DocumentEvent.EventType.REMOVE);
/*      */     Element localElement;
/* 1602 */     if (bool) {
/* 1603 */       localElement = getCharacterElement(getLength());
/*      */ 
/* 1605 */       paramInt1--;
/* 1606 */       if (localElement.getParentElement() != paramElement)
/*      */       {
/* 1609 */         replace(localDefaultDocumentEvent, paramElement, paramInt1, ++paramInt2, paramInt3, paramInt4, true, true);
/*      */       }
/*      */       else
/*      */       {
/* 1616 */         replace(localDefaultDocumentEvent, paramElement, paramInt1, paramInt2, paramInt3, paramInt4, true, false);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1622 */       localElement = paramElement.getElement(paramInt1 - 1);
/* 1623 */       while (!localElement.isLeaf()) {
/* 1624 */         localElement = localElement.getElement(localElement.getElementCount() - 1);
/*      */       }
/* 1626 */       localElement = localElement.getParentElement();
/* 1627 */       replace(localDefaultDocumentEvent, paramElement, paramInt1, paramInt2, paramInt3, paramInt4, false, false);
/* 1628 */       replace(localDefaultDocumentEvent, localElement, localElement.getElementCount() - 1, 1, paramInt3, paramInt4, true, true);
/*      */     }
/*      */ 
/* 1631 */     postRemoveUpdate(localDefaultDocumentEvent);
/* 1632 */     localDefaultDocumentEvent.end();
/* 1633 */     fireRemoveUpdate(localDefaultDocumentEvent);
/* 1634 */     fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */   }
/*      */ 
/*      */   private void replace(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent, Element paramElement, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws BadLocationException
/*      */   {
/* 1648 */     AttributeSet localAttributeSet = paramElement.getElement(paramInt1).getAttributes();
/* 1649 */     Element[] arrayOfElement2 = new Element[paramInt2];
/*      */ 
/* 1651 */     for (int i = 0; i < paramInt2; i++) {
/* 1652 */       arrayOfElement2[i] = paramElement.getElement(i + paramInt1);
/*      */     }
/* 1654 */     if (paramBoolean1) {
/* 1655 */       UndoableEdit localUndoableEdit = getContent().remove(paramInt3 - 1, paramInt4 - paramInt3);
/* 1656 */       if (localUndoableEdit != null)
/* 1657 */         paramDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */     }
/*      */     Element[] arrayOfElement1;
/* 1660 */     if (paramBoolean2) {
/* 1661 */       arrayOfElement1 = new Element[1];
/* 1662 */       arrayOfElement1[0] = createLeafElement(paramElement, localAttributeSet, paramInt3 - 1, paramInt3);
/*      */     }
/*      */     else {
/* 1665 */       arrayOfElement1 = new Element[0];
/*      */     }
/* 1667 */     paramDefaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(paramElement, paramInt1, arrayOfElement2, arrayOfElement1));
/* 1668 */     ((AbstractDocument.BranchElement)paramElement).replace(paramInt1, arrayOfElement2.length, arrayOfElement1);
/*      */   }
/*      */ 
/*      */   private void removeElements(Element paramElement, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws BadLocationException
/*      */   {
/* 1677 */     Element[] arrayOfElement1 = new Element[paramInt2];
/* 1678 */     Element[] arrayOfElement2 = new Element[0];
/* 1679 */     for (int i = 0; i < paramInt2; i++) {
/* 1680 */       arrayOfElement1[i] = paramElement.getElement(i + paramInt1);
/*      */     }
/* 1682 */     AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt3, paramInt4 - paramInt3, DocumentEvent.EventType.REMOVE);
/*      */ 
/* 1684 */     ((AbstractDocument.BranchElement)paramElement).replace(paramInt1, arrayOfElement1.length, arrayOfElement2);
/*      */ 
/* 1686 */     localDefaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(paramElement, paramInt1, arrayOfElement1, arrayOfElement2));
/* 1687 */     UndoableEdit localUndoableEdit = getContent().remove(paramInt3, paramInt4 - paramInt3);
/* 1688 */     if (localUndoableEdit != null) {
/* 1689 */       localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */     }
/* 1691 */     postRemoveUpdate(localDefaultDocumentEvent);
/* 1692 */     localDefaultDocumentEvent.end();
/* 1693 */     fireRemoveUpdate(localDefaultDocumentEvent);
/* 1694 */     if (localUndoableEdit != null)
/* 1695 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */   }
/*      */ 
/*      */   void obtainLock()
/*      */   {
/* 1703 */     writeLock();
/*      */   }
/*      */ 
/*      */   void releaseLock() {
/* 1707 */     writeUnlock();
/*      */   }
/*      */ 
/*      */   protected void fireChangedUpdate(DocumentEvent paramDocumentEvent)
/*      */   {
/* 1724 */     super.fireChangedUpdate(paramDocumentEvent);
/*      */   }
/*      */ 
/*      */   protected void fireUndoableEditUpdate(UndoableEditEvent paramUndoableEditEvent)
/*      */   {
/* 1737 */     super.fireUndoableEditUpdate(paramUndoableEditEvent);
/*      */   }
/*      */ 
/*      */   boolean hasBaseTag() {
/* 1741 */     return this.hasBaseTag;
/*      */   }
/*      */ 
/*      */   String getBaseTarget() {
/* 1745 */     return this.baseTarget;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1828 */     contentAttributeSet = new SimpleAttributeSet();
/* 1829 */     ((MutableAttributeSet)contentAttributeSet).addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/* 1832 */     NEWLINE = new char[1];
/* 1833 */     NEWLINE[0] = '\n';
/*      */   }
/*      */ 
/*      */   public class BlockElement extends AbstractDocument.BranchElement
/*      */   {
/*      */     public BlockElement(Element paramAttributeSet, AttributeSet arg3)
/*      */     {
/* 4133 */       super(paramAttributeSet, localAttributeSet);
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 4142 */       Object localObject = getAttribute(StyleConstants.NameAttribute);
/* 4143 */       if (localObject != null) {
/* 4144 */         return localObject.toString();
/*      */       }
/* 4146 */       return super.getName();
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/* 4157 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FixedLengthDocument extends PlainDocument
/*      */   {
/*      */     private int maxLength;
/*      */ 
/*      */     public FixedLengthDocument(int paramInt)
/*      */     {
/* 4170 */       this.maxLength = paramInt;
/*      */     }
/*      */ 
/*      */     public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet) throws BadLocationException
/*      */     {
/* 4175 */       if ((paramString != null) && (paramString.length() + getLength() <= this.maxLength))
/* 4176 */         super.insertString(paramInt, paramString, paramAttributeSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class HTMLReader extends HTMLEditorKit.ParserCallback
/*      */   {
/*      */     private boolean receivedEndHTML;
/*      */     private int flushCount;
/*      */     private boolean insertAfterImplied;
/*      */     private boolean wantsTrailingNewline;
/*      */     int threshold;
/*      */     int offset;
/* 3980 */     boolean inParagraph = false;
/* 3981 */     boolean impliedP = false;
/* 3982 */     boolean inPre = false;
/* 3983 */     boolean inTextArea = false;
/* 3984 */     TextAreaDocument textAreaDocument = null;
/* 3985 */     boolean inTitle = false;
/* 3986 */     boolean lastWasNewline = true;
/*      */     boolean emptyAnchor;
/*      */     boolean midInsert;
/*      */     boolean inBody;
/*      */     HTML.Tag insertTag;
/*      */     boolean insertInsertTag;
/*      */     boolean foundInsertTag;
/*      */     int insertTagDepthDelta;
/*      */     int popDepth;
/*      */     int pushDepth;
/*      */     Map lastMap;
/* 4019 */     boolean inStyle = false;
/*      */     String defaultStyle;
/*      */     Vector<Object> styles;
/* 4031 */     boolean inHead = false;
/*      */     boolean isStyleCSS;
/*      */     boolean emptyDocument;
/*      */     AttributeSet styleAttributes;
/*      */     Option option;
/* 4046 */     protected Vector<DefaultStyledDocument.ElementSpec> parseBuffer = new Vector();
/* 4047 */     protected MutableAttributeSet charAttr = new HTMLDocument.TaggedAttributeSet();
/* 4048 */     Stack<AttributeSet> charAttrStack = new Stack();
/*      */     Hashtable<HTML.Tag, TagAction> tagMap;
/* 4050 */     int inBlock = 0;
/*      */ 
/* 4057 */     private HTML.Tag nextTagAfterPImplied = null;
/*      */ 
/*      */     public HTMLReader(int arg2)
/*      */     {
/* 2169 */       this(i, 0, 0, null);
/*      */     }
/*      */ 
/*      */     public HTMLReader(int paramInt1, int paramInt2, int paramTag, HTML.Tag arg5)
/*      */     {
/* 2174 */       this(paramInt1, paramInt2, paramTag, localTag, true, false, true);
/*      */     }
/*      */ 
/*      */     HTMLReader(int paramInt1, int paramInt2, int paramTag, HTML.Tag paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean arg8)
/*      */     {
/* 2188 */       this.emptyDocument = (HTMLDocument.this.getLength() == 0);
/* 2189 */       this.isStyleCSS = "text/css".equals(HTMLDocument.this.getDefaultStyleSheetType());
/* 2190 */       this.offset = paramInt1;
/* 2191 */       this.threshold = HTMLDocument.this.getTokenThreshold();
/* 2192 */       this.tagMap = new Hashtable(57);
/* 2193 */       TagAction localTagAction = new TagAction();
/* 2194 */       BlockAction localBlockAction = new BlockAction();
/* 2195 */       ParagraphAction localParagraphAction = new ParagraphAction();
/* 2196 */       CharacterAction localCharacterAction = new CharacterAction();
/* 2197 */       SpecialAction localSpecialAction = new SpecialAction();
/* 2198 */       FormAction localFormAction = new FormAction();
/* 2199 */       HiddenAction localHiddenAction = new HiddenAction();
/* 2200 */       ConvertAction localConvertAction = new ConvertAction();
/*      */ 
/* 2203 */       this.tagMap.put(HTML.Tag.A, new AnchorAction());
/* 2204 */       this.tagMap.put(HTML.Tag.ADDRESS, localCharacterAction);
/* 2205 */       this.tagMap.put(HTML.Tag.APPLET, localHiddenAction);
/* 2206 */       this.tagMap.put(HTML.Tag.AREA, new AreaAction());
/* 2207 */       this.tagMap.put(HTML.Tag.B, localConvertAction);
/* 2208 */       this.tagMap.put(HTML.Tag.BASE, new BaseAction());
/* 2209 */       this.tagMap.put(HTML.Tag.BASEFONT, localCharacterAction);
/* 2210 */       this.tagMap.put(HTML.Tag.BIG, localCharacterAction);
/* 2211 */       this.tagMap.put(HTML.Tag.BLOCKQUOTE, localBlockAction);
/* 2212 */       this.tagMap.put(HTML.Tag.BODY, localBlockAction);
/* 2213 */       this.tagMap.put(HTML.Tag.BR, localSpecialAction);
/* 2214 */       this.tagMap.put(HTML.Tag.CAPTION, localBlockAction);
/* 2215 */       this.tagMap.put(HTML.Tag.CENTER, localBlockAction);
/* 2216 */       this.tagMap.put(HTML.Tag.CITE, localCharacterAction);
/* 2217 */       this.tagMap.put(HTML.Tag.CODE, localCharacterAction);
/* 2218 */       this.tagMap.put(HTML.Tag.DD, localBlockAction);
/* 2219 */       this.tagMap.put(HTML.Tag.DFN, localCharacterAction);
/* 2220 */       this.tagMap.put(HTML.Tag.DIR, localBlockAction);
/* 2221 */       this.tagMap.put(HTML.Tag.DIV, localBlockAction);
/* 2222 */       this.tagMap.put(HTML.Tag.DL, localBlockAction);
/* 2223 */       this.tagMap.put(HTML.Tag.DT, localParagraphAction);
/* 2224 */       this.tagMap.put(HTML.Tag.EM, localCharacterAction);
/* 2225 */       this.tagMap.put(HTML.Tag.FONT, localConvertAction);
/* 2226 */       this.tagMap.put(HTML.Tag.FORM, new FormTagAction(null));
/* 2227 */       this.tagMap.put(HTML.Tag.FRAME, localSpecialAction);
/* 2228 */       this.tagMap.put(HTML.Tag.FRAMESET, localBlockAction);
/* 2229 */       this.tagMap.put(HTML.Tag.H1, localParagraphAction);
/* 2230 */       this.tagMap.put(HTML.Tag.H2, localParagraphAction);
/* 2231 */       this.tagMap.put(HTML.Tag.H3, localParagraphAction);
/* 2232 */       this.tagMap.put(HTML.Tag.H4, localParagraphAction);
/* 2233 */       this.tagMap.put(HTML.Tag.H5, localParagraphAction);
/* 2234 */       this.tagMap.put(HTML.Tag.H6, localParagraphAction);
/* 2235 */       this.tagMap.put(HTML.Tag.HEAD, new HeadAction());
/* 2236 */       this.tagMap.put(HTML.Tag.HR, localSpecialAction);
/* 2237 */       this.tagMap.put(HTML.Tag.HTML, localBlockAction);
/* 2238 */       this.tagMap.put(HTML.Tag.I, localConvertAction);
/* 2239 */       this.tagMap.put(HTML.Tag.IMG, localSpecialAction);
/* 2240 */       this.tagMap.put(HTML.Tag.INPUT, localFormAction);
/* 2241 */       this.tagMap.put(HTML.Tag.ISINDEX, new IsindexAction());
/* 2242 */       this.tagMap.put(HTML.Tag.KBD, localCharacterAction);
/* 2243 */       this.tagMap.put(HTML.Tag.LI, localBlockAction);
/* 2244 */       this.tagMap.put(HTML.Tag.LINK, new LinkAction());
/* 2245 */       this.tagMap.put(HTML.Tag.MAP, new MapAction());
/* 2246 */       this.tagMap.put(HTML.Tag.MENU, localBlockAction);
/* 2247 */       this.tagMap.put(HTML.Tag.META, new MetaAction());
/* 2248 */       this.tagMap.put(HTML.Tag.NOBR, localCharacterAction);
/* 2249 */       this.tagMap.put(HTML.Tag.NOFRAMES, localBlockAction);
/* 2250 */       this.tagMap.put(HTML.Tag.OBJECT, localSpecialAction);
/* 2251 */       this.tagMap.put(HTML.Tag.OL, localBlockAction);
/* 2252 */       this.tagMap.put(HTML.Tag.OPTION, localFormAction);
/* 2253 */       this.tagMap.put(HTML.Tag.P, localParagraphAction);
/* 2254 */       this.tagMap.put(HTML.Tag.PARAM, new ObjectAction());
/* 2255 */       this.tagMap.put(HTML.Tag.PRE, new PreAction());
/* 2256 */       this.tagMap.put(HTML.Tag.SAMP, localCharacterAction);
/* 2257 */       this.tagMap.put(HTML.Tag.SCRIPT, localHiddenAction);
/* 2258 */       this.tagMap.put(HTML.Tag.SELECT, localFormAction);
/* 2259 */       this.tagMap.put(HTML.Tag.SMALL, localCharacterAction);
/* 2260 */       this.tagMap.put(HTML.Tag.SPAN, localCharacterAction);
/* 2261 */       this.tagMap.put(HTML.Tag.STRIKE, localConvertAction);
/* 2262 */       this.tagMap.put(HTML.Tag.S, localCharacterAction);
/* 2263 */       this.tagMap.put(HTML.Tag.STRONG, localCharacterAction);
/* 2264 */       this.tagMap.put(HTML.Tag.STYLE, new StyleAction());
/* 2265 */       this.tagMap.put(HTML.Tag.SUB, localConvertAction);
/* 2266 */       this.tagMap.put(HTML.Tag.SUP, localConvertAction);
/* 2267 */       this.tagMap.put(HTML.Tag.TABLE, localBlockAction);
/* 2268 */       this.tagMap.put(HTML.Tag.TD, localBlockAction);
/* 2269 */       this.tagMap.put(HTML.Tag.TEXTAREA, localFormAction);
/* 2270 */       this.tagMap.put(HTML.Tag.TH, localBlockAction);
/* 2271 */       this.tagMap.put(HTML.Tag.TITLE, new TitleAction());
/* 2272 */       this.tagMap.put(HTML.Tag.TR, localBlockAction);
/* 2273 */       this.tagMap.put(HTML.Tag.TT, localCharacterAction);
/* 2274 */       this.tagMap.put(HTML.Tag.U, localConvertAction);
/* 2275 */       this.tagMap.put(HTML.Tag.UL, localBlockAction);
/* 2276 */       this.tagMap.put(HTML.Tag.VAR, localCharacterAction);
/*      */ 
/* 2278 */       if (paramBoolean1 != null) {
/* 2279 */         this.insertTag = paramBoolean1;
/* 2280 */         this.popDepth = paramInt2;
/* 2281 */         this.pushDepth = paramTag;
/* 2282 */         this.insertInsertTag = paramBoolean2;
/* 2283 */         this.foundInsertTag = false;
/*      */       }
/*      */       else {
/* 2286 */         this.foundInsertTag = true;
/*      */       }
/* 2288 */       if (paramBoolean3) {
/* 2289 */         this.popDepth = paramInt2;
/* 2290 */         this.pushDepth = paramTag;
/* 2291 */         this.insertAfterImplied = true;
/* 2292 */         this.foundInsertTag = false;
/* 2293 */         this.midInsert = false;
/* 2294 */         this.insertInsertTag = true;
/*      */         boolean bool;
/* 2295 */         this.wantsTrailingNewline = bool;
/*      */       }
/*      */       else {
/* 2298 */         this.midInsert = ((!this.emptyDocument) && (paramBoolean1 == null));
/* 2299 */         if (this.midInsert) {
/* 2300 */           generateEndsSpecsForMidInsert();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2310 */       if ((!this.emptyDocument) && (!this.midInsert)) {
/* 2311 */         int i = Math.max(this.offset - 1, 0);
/* 2312 */         Element localElement = HTMLDocument.this.getCharacterElement(i);
/*      */ 
/* 2315 */         for (int j = 0; j <= this.popDepth; j++) {
/* 2316 */           localElement = localElement.getParentElement();
/*      */         }
/*      */ 
/* 2319 */         for (j = 0; j < this.pushDepth; j++) {
/* 2320 */           int k = localElement.getElementIndex(this.offset);
/* 2321 */           localElement = localElement.getElement(k);
/*      */         }
/* 2323 */         AttributeSet localAttributeSet = localElement.getAttributes();
/* 2324 */         if (localAttributeSet != null) {
/* 2325 */           HTML.Tag localTag = (HTML.Tag)localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 2327 */           if (localTag != null)
/* 2328 */             this.inParagraph = localTag.isParagraph();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void generateEndsSpecsForMidInsert()
/*      */     {
/* 2339 */       int i = heightToElementWithName(HTML.Tag.BODY, Math.max(0, this.offset - 1));
/*      */ 
/* 2341 */       int j = 0;
/*      */ 
/* 2343 */       if ((i == -1) && (this.offset > 0)) {
/* 2344 */         i = heightToElementWithName(HTML.Tag.BODY, this.offset);
/* 2345 */         if (i != -1)
/*      */         {
/* 2348 */           i = depthTo(this.offset - 1) - 1;
/* 2349 */           j = 1;
/*      */         }
/*      */       }
/* 2352 */       if (i == -1) {
/* 2353 */         throw new RuntimeException("Must insert new content into body element-");
/*      */       }
/* 2355 */       if (i != -1)
/*      */       {
/*      */         try {
/* 2358 */           if ((j == 0) && (this.offset > 0) && (!HTMLDocument.this.getText(this.offset - 1, 1).equals("\n")))
/*      */           {
/* 2360 */             SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 2361 */             localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/* 2363 */             DefaultStyledDocument.ElementSpec localElementSpec2 = new DefaultStyledDocument.ElementSpec(localSimpleAttributeSet, (short)3, HTMLDocument.NEWLINE, 0, 1);
/*      */ 
/* 2365 */             this.parseBuffer.addElement(localElementSpec2);
/*      */           }
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/* 2369 */         while (i-- > 0) {
/* 2370 */           this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(null, (short)2));
/*      */         }
/*      */ 
/* 2373 */         if (j != 0) {
/* 2374 */           DefaultStyledDocument.ElementSpec localElementSpec1 = new DefaultStyledDocument.ElementSpec(null, (short)1);
/*      */ 
/* 2377 */           localElementSpec1.setDirection((short)5);
/* 2378 */           this.parseBuffer.addElement(localElementSpec1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int depthTo(int paramInt)
/*      */     {
/* 2389 */       Element localElement = HTMLDocument.this.getDefaultRootElement();
/* 2390 */       int i = 0;
/*      */ 
/* 2392 */       while (!localElement.isLeaf()) {
/* 2393 */         i++;
/* 2394 */         localElement = localElement.getElement(localElement.getElementIndex(paramInt));
/*      */       }
/* 2396 */       return i;
/*      */     }
/*      */ 
/*      */     private int heightToElementWithName(Object paramObject, int paramInt)
/*      */     {
/* 2406 */       Element localElement = HTMLDocument.this.getCharacterElement(paramInt).getParentElement();
/* 2407 */       int i = 0;
/*      */ 
/* 2409 */       while ((localElement != null) && (localElement.getAttributes().getAttribute(StyleConstants.NameAttribute) != paramObject))
/*      */       {
/* 2411 */         i++;
/* 2412 */         localElement = localElement.getParentElement();
/*      */       }
/* 2414 */       return localElement == null ? -1 : i;
/*      */     }
/*      */ 
/*      */     private void adjustEndElement()
/*      */     {
/* 2422 */       int i = HTMLDocument.this.getLength();
/* 2423 */       if (i == 0) {
/* 2424 */         return;
/*      */       }
/* 2426 */       HTMLDocument.this.obtainLock();
/*      */       try {
/* 2428 */         Element[] arrayOfElement1 = getPathTo(i - 1);
/* 2429 */         int j = arrayOfElement1.length;
/* 2430 */         if ((j > 1) && (arrayOfElement1[1].getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) && (arrayOfElement1[1].getEndOffset() == i))
/*      */         {
/* 2433 */           String str = HTMLDocument.this.getText(i - 1, 1);
/*      */ 
/* 2439 */           Element[] arrayOfElement2 = new Element[0];
/* 2440 */           Element[] arrayOfElement3 = new Element[1];
/* 2441 */           int k = arrayOfElement1[0].getElementIndex(i);
/* 2442 */           arrayOfElement3[0] = arrayOfElement1[0].getElement(k);
/* 2443 */           ((AbstractDocument.BranchElement)arrayOfElement1[0]).replace(k, 1, arrayOfElement2);
/* 2444 */           AbstractDocument.ElementEdit localElementEdit = new AbstractDocument.ElementEdit(arrayOfElement1[0], k, arrayOfElement3, arrayOfElement2);
/*      */ 
/* 2449 */           SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 2450 */           localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/* 2452 */           localSimpleAttributeSet.addAttribute("CR", Boolean.TRUE);
/* 2453 */           arrayOfElement2 = new Element[1];
/* 2454 */           arrayOfElement2[0] = HTMLDocument.this.createLeafElement(arrayOfElement1[(j - 1)], localSimpleAttributeSet, i, i + 1);
/*      */ 
/* 2456 */           k = arrayOfElement1[(j - 1)].getElementCount();
/* 2457 */           ((AbstractDocument.BranchElement)arrayOfElement1[(j - 1)]).replace(k, 0, arrayOfElement2);
/*      */ 
/* 2459 */           AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(HTMLDocument.this, i, 1, DocumentEvent.EventType.CHANGE);
/*      */ 
/* 2461 */           localDefaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(arrayOfElement1[(j - 1)], k, new Element[0], arrayOfElement2));
/*      */ 
/* 2463 */           localDefaultDocumentEvent.addEdit(localElementEdit);
/* 2464 */           localDefaultDocumentEvent.end();
/* 2465 */           HTMLDocument.this.fireChangedUpdate(localDefaultDocumentEvent);
/* 2466 */           HTMLDocument.this.fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */ 
/* 2468 */           if (str.equals("\n"))
/*      */           {
/* 2471 */             localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(HTMLDocument.this, i - 1, 1, DocumentEvent.EventType.REMOVE);
/*      */ 
/* 2473 */             HTMLDocument.this.removeUpdate(localDefaultDocumentEvent);
/* 2474 */             UndoableEdit localUndoableEdit = HTMLDocument.this.getContent().remove(i - 1, 1);
/* 2475 */             if (localUndoableEdit != null) {
/* 2476 */               localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */             }
/* 2478 */             HTMLDocument.this.postRemoveUpdate(localDefaultDocumentEvent);
/*      */ 
/* 2480 */             localDefaultDocumentEvent.end();
/* 2481 */             HTMLDocument.this.fireRemoveUpdate(localDefaultDocumentEvent);
/* 2482 */             HTMLDocument.this.fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/*      */       finally {
/* 2490 */         HTMLDocument.this.releaseLock();
/*      */       }
/*      */     }
/*      */ 
/*      */     private Element[] getPathTo(int paramInt) {
/* 2495 */       Stack localStack = new Stack();
/* 2496 */       Element localElement = HTMLDocument.this.getDefaultRootElement();
/*      */ 
/* 2498 */       while (!localElement.isLeaf()) {
/* 2499 */         localStack.push(localElement);
/* 2500 */         localElement = localElement.getElement(localElement.getElementIndex(paramInt));
/*      */       }
/* 2502 */       Element[] arrayOfElement = new Element[localStack.size()];
/* 2503 */       localStack.copyInto(arrayOfElement);
/* 2504 */       return arrayOfElement;
/*      */     }
/*      */ 
/*      */     public void flush()
/*      */       throws BadLocationException
/*      */     {
/* 2516 */       if ((this.emptyDocument) && (!this.insertAfterImplied)) {
/* 2517 */         if ((HTMLDocument.this.getLength() > 0) || (this.parseBuffer.size() > 0))
/*      */         {
/* 2519 */           flushBuffer(true);
/* 2520 */           adjustEndElement();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2525 */         flushBuffer(true);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void handleText(char[] paramArrayOfChar, int paramInt)
/*      */     {
/* 2534 */       if ((this.receivedEndHTML) || ((this.midInsert) && (!this.inBody))) {
/* 2535 */         return;
/*      */       }
/*      */ 
/* 2539 */       if (HTMLDocument.this.getProperty("i18n").equals(Boolean.FALSE))
/*      */       {
/* 2542 */         Object localObject = HTMLDocument.this.getProperty(TextAttribute.RUN_DIRECTION);
/* 2543 */         if ((localObject != null) && (localObject.equals(TextAttribute.RUN_DIRECTION_RTL))) {
/* 2544 */           HTMLDocument.this.putProperty("i18n", Boolean.TRUE);
/*      */         }
/* 2546 */         else if (SwingUtilities2.isComplexLayout(paramArrayOfChar, 0, paramArrayOfChar.length)) {
/* 2547 */           HTMLDocument.this.putProperty("i18n", Boolean.TRUE);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2552 */       if (this.inTextArea) {
/* 2553 */         textAreaContent(paramArrayOfChar);
/* 2554 */       } else if (this.inPre) {
/* 2555 */         preContent(paramArrayOfChar);
/* 2556 */       } else if (this.inTitle) {
/* 2557 */         HTMLDocument.this.putProperty("title", new String(paramArrayOfChar));
/* 2558 */       } else if (this.option != null) {
/* 2559 */         this.option.setLabel(new String(paramArrayOfChar));
/* 2560 */       } else if (this.inStyle) {
/* 2561 */         if (this.styles != null)
/* 2562 */           this.styles.addElement(new String(paramArrayOfChar));
/*      */       }
/* 2564 */       else if (this.inBlock > 0) {
/* 2565 */         if ((!this.foundInsertTag) && (this.insertAfterImplied))
/*      */         {
/* 2567 */           foundInsertTag(false);
/* 2568 */           this.foundInsertTag = true;
/* 2569 */           this.inParagraph = (this.impliedP = 1);
/*      */         }
/* 2571 */         if (paramArrayOfChar.length >= 1)
/* 2572 */           addContent(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void handleStartTag(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*      */     {
/* 2582 */       if (this.receivedEndHTML) {
/* 2583 */         return;
/*      */       }
/* 2585 */       if ((this.midInsert) && (!this.inBody)) {
/* 2586 */         if (paramTag == HTML.Tag.BODY) {
/* 2587 */           this.inBody = true;
/*      */ 
/* 2592 */           this.inBlock += 1;
/*      */         }
/* 2594 */         return;
/*      */       }
/* 2596 */       if ((!this.inBody) && (paramTag == HTML.Tag.BODY)) {
/* 2597 */         this.inBody = true;
/*      */       }
/* 2599 */       if ((this.isStyleCSS) && (paramMutableAttributeSet.isDefined(HTML.Attribute.STYLE)))
/*      */       {
/* 2601 */         localObject = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.STYLE);
/* 2602 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.STYLE);
/* 2603 */         this.styleAttributes = HTMLDocument.this.getStyleSheet().getDeclaration((String)localObject);
/* 2604 */         paramMutableAttributeSet.addAttributes(this.styleAttributes);
/*      */       }
/*      */       else {
/* 2607 */         this.styleAttributes = null;
/*      */       }
/* 2609 */       Object localObject = (TagAction)this.tagMap.get(paramTag);
/*      */ 
/* 2611 */       if (localObject != null)
/* 2612 */         ((TagAction)localObject).start(paramTag, paramMutableAttributeSet);
/*      */     }
/*      */ 
/*      */     public void handleComment(char[] paramArrayOfChar, int paramInt)
/*      */     {
/* 2617 */       if (this.receivedEndHTML) {
/* 2618 */         addExternalComment(new String(paramArrayOfChar));
/* 2619 */         return;
/*      */       }
/* 2621 */       if (this.inStyle) {
/* 2622 */         if (this.styles != null) {
/* 2623 */           this.styles.addElement(new String(paramArrayOfChar));
/*      */         }
/*      */       }
/* 2626 */       else if (HTMLDocument.this.getPreservesUnknownTags()) {
/* 2627 */         if ((this.inBlock == 0) && ((this.foundInsertTag) || (this.insertTag != HTML.Tag.COMMENT)))
/*      */         {
/* 2631 */           addExternalComment(new String(paramArrayOfChar));
/* 2632 */           return;
/*      */         }
/* 2634 */         localObject = new SimpleAttributeSet();
/* 2635 */         ((SimpleAttributeSet)localObject).addAttribute(HTML.Attribute.COMMENT, new String(paramArrayOfChar));
/* 2636 */         addSpecialElement(HTML.Tag.COMMENT, (MutableAttributeSet)localObject);
/*      */       }
/*      */ 
/* 2639 */       Object localObject = (TagAction)this.tagMap.get(HTML.Tag.COMMENT);
/* 2640 */       if (localObject != null) {
/* 2641 */         ((TagAction)localObject).start(HTML.Tag.COMMENT, new SimpleAttributeSet());
/* 2642 */         ((TagAction)localObject).end(HTML.Tag.COMMENT);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void addExternalComment(String paramString)
/*      */     {
/* 2651 */       Object localObject = HTMLDocument.this.getProperty("AdditionalComments");
/* 2652 */       if ((localObject != null) && (!(localObject instanceof Vector)))
/*      */       {
/* 2654 */         return;
/*      */       }
/* 2656 */       if (localObject == null) {
/* 2657 */         localObject = new Vector();
/* 2658 */         HTMLDocument.this.putProperty("AdditionalComments", localObject);
/*      */       }
/* 2660 */       ((Vector)localObject).addElement(paramString);
/*      */     }
/*      */ 
/*      */     public void handleEndTag(HTML.Tag paramTag, int paramInt)
/*      */     {
/* 2668 */       if ((this.receivedEndHTML) || ((this.midInsert) && (!this.inBody))) {
/* 2669 */         return;
/*      */       }
/* 2671 */       if (paramTag == HTML.Tag.HTML) {
/* 2672 */         this.receivedEndHTML = true;
/*      */       }
/* 2674 */       if (paramTag == HTML.Tag.BODY) {
/* 2675 */         this.inBody = false;
/* 2676 */         if (this.midInsert) {
/* 2677 */           this.inBlock -= 1;
/*      */         }
/*      */       }
/* 2680 */       TagAction localTagAction = (TagAction)this.tagMap.get(paramTag);
/* 2681 */       if (localTagAction != null)
/* 2682 */         localTagAction.end(paramTag);
/*      */     }
/*      */ 
/*      */     public void handleSimpleTag(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*      */     {
/* 2691 */       if ((this.receivedEndHTML) || ((this.midInsert) && (!this.inBody))) {
/* 2692 */         return;
/*      */       }
/*      */ 
/* 2695 */       if ((this.isStyleCSS) && (paramMutableAttributeSet.isDefined(HTML.Attribute.STYLE)))
/*      */       {
/* 2697 */         localObject = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.STYLE);
/* 2698 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.STYLE);
/* 2699 */         this.styleAttributes = HTMLDocument.this.getStyleSheet().getDeclaration((String)localObject);
/* 2700 */         paramMutableAttributeSet.addAttributes(this.styleAttributes);
/*      */       }
/*      */       else {
/* 2703 */         this.styleAttributes = null;
/*      */       }
/*      */ 
/* 2706 */       Object localObject = (TagAction)this.tagMap.get(paramTag);
/* 2707 */       if (localObject != null) {
/* 2708 */         ((TagAction)localObject).start(paramTag, paramMutableAttributeSet);
/* 2709 */         ((TagAction)localObject).end(paramTag);
/*      */       }
/* 2711 */       else if (HTMLDocument.this.getPreservesUnknownTags())
/*      */       {
/* 2713 */         addSpecialElement(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void handleEndOfLineString(String paramString)
/*      */     {
/* 2726 */       if ((this.emptyDocument) && (paramString != null))
/* 2727 */         HTMLDocument.this.putProperty("__EndOfLine__", paramString);
/*      */     }
/*      */ 
/*      */     protected void registerTag(HTML.Tag paramTag, TagAction paramTagAction)
/*      */     {
/* 2741 */       this.tagMap.put(paramTag, paramTagAction);
/*      */     }
/*      */ 
/*      */     protected void pushCharacterStyle()
/*      */     {
/* 3492 */       this.charAttrStack.push(this.charAttr.copyAttributes());
/*      */     }
/*      */ 
/*      */     protected void popCharacterStyle()
/*      */     {
/* 3500 */       if (!this.charAttrStack.empty()) {
/* 3501 */         this.charAttr = ((MutableAttributeSet)this.charAttrStack.peek());
/* 3502 */         this.charAttrStack.pop();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void textAreaContent(char[] paramArrayOfChar)
/*      */     {
/*      */       try
/*      */       {
/* 3515 */         this.textAreaDocument.insertString(this.textAreaDocument.getLength(), new String(paramArrayOfChar), null);
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void preContent(char[] paramArrayOfChar)
/*      */     {
/* 3528 */       int i = 0;
/* 3529 */       for (int j = 0; j < paramArrayOfChar.length; j++) {
/* 3530 */         if (paramArrayOfChar[j] == '\n') {
/* 3531 */           addContent(paramArrayOfChar, i, j - i + 1);
/* 3532 */           blockClose(HTML.Tag.IMPLIED);
/* 3533 */           SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 3534 */           localSimpleAttributeSet.addAttribute(CSS.Attribute.WHITE_SPACE, "pre");
/* 3535 */           blockOpen(HTML.Tag.IMPLIED, localSimpleAttributeSet);
/* 3536 */           i = j + 1;
/*      */         }
/*      */       }
/* 3539 */       if (i < paramArrayOfChar.length)
/* 3540 */         addContent(paramArrayOfChar, i, paramArrayOfChar.length - i);
/*      */     }
/*      */ 
/*      */     protected void blockOpen(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet)
/*      */     {
/* 3549 */       if (this.impliedP) {
/* 3550 */         blockClose(HTML.Tag.IMPLIED);
/*      */       }
/*      */ 
/* 3553 */       this.inBlock += 1;
/*      */ 
/* 3555 */       if (!canInsertTag(paramTag, paramMutableAttributeSet, true)) {
/* 3556 */         return;
/*      */       }
/* 3558 */       if (paramMutableAttributeSet.isDefined(IMPLIED)) {
/* 3559 */         paramMutableAttributeSet.removeAttribute(IMPLIED);
/*      */       }
/* 3561 */       this.lastWasNewline = false;
/* 3562 */       paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, paramTag);
/* 3563 */       DefaultStyledDocument.ElementSpec localElementSpec = new DefaultStyledDocument.ElementSpec(paramMutableAttributeSet.copyAttributes(), (short)1);
/*      */ 
/* 3565 */       this.parseBuffer.addElement(localElementSpec);
/*      */     }
/*      */ 
/*      */     protected void blockClose(HTML.Tag paramTag)
/*      */     {
/* 3573 */       this.inBlock -= 1;
/*      */ 
/* 3575 */       if (!this.foundInsertTag) {
/* 3576 */         return;
/*      */       }
/*      */ 
/* 3585 */       if (!this.lastWasNewline) {
/* 3586 */         pushCharacterStyle();
/* 3587 */         this.charAttr.addAttribute("CR", Boolean.TRUE);
/* 3588 */         addContent(HTMLDocument.NEWLINE, 0, 1, true);
/* 3589 */         popCharacterStyle();
/* 3590 */         this.lastWasNewline = true;
/*      */       }
/*      */ 
/* 3593 */       if (this.impliedP) {
/* 3594 */         this.impliedP = false;
/* 3595 */         this.inParagraph = false;
/* 3596 */         if (paramTag != HTML.Tag.IMPLIED) {
/* 3597 */           blockClose(HTML.Tag.IMPLIED);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3602 */       Object localObject1 = this.parseBuffer.size() > 0 ? (DefaultStyledDocument.ElementSpec)this.parseBuffer.lastElement() : null;
/*      */ 
/* 3604 */       if ((localObject1 != null) && (localObject1.getType() == 1)) {
/* 3605 */         localObject2 = new char[1];
/* 3606 */         localObject2[0] = 32;
/* 3607 */         addContent((char[])localObject2, 0, 1);
/*      */       }
/* 3609 */       Object localObject2 = new DefaultStyledDocument.ElementSpec(null, (short)2);
/*      */ 
/* 3611 */       this.parseBuffer.addElement(localObject2);
/*      */     }
/*      */ 
/*      */     protected void addContent(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     {
/* 3622 */       addContent(paramArrayOfChar, paramInt1, paramInt2, true);
/*      */     }
/*      */ 
/*      */     protected void addContent(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */     {
/* 3636 */       if (!this.foundInsertTag) {
/* 3637 */         return;
/*      */       }
/*      */ 
/* 3640 */       if ((paramBoolean) && (!this.inParagraph) && (!this.inPre)) {
/* 3641 */         blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
/* 3642 */         this.inParagraph = true;
/* 3643 */         this.impliedP = true;
/*      */       }
/* 3645 */       this.emptyAnchor = false;
/* 3646 */       this.charAttr.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/* 3647 */       AttributeSet localAttributeSet = this.charAttr.copyAttributes();
/* 3648 */       DefaultStyledDocument.ElementSpec localElementSpec = new DefaultStyledDocument.ElementSpec(localAttributeSet, (short)3, paramArrayOfChar, paramInt1, paramInt2);
/*      */ 
/* 3650 */       this.parseBuffer.addElement(localElementSpec);
/*      */ 
/* 3652 */       if (this.parseBuffer.size() > this.threshold) {
/* 3653 */         if (this.threshold <= 10000)
/* 3654 */           this.threshold *= 5;
/*      */         try
/*      */         {
/* 3657 */           flushBuffer(false);
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */       }
/* 3661 */       if (paramInt2 > 0)
/* 3662 */         this.lastWasNewline = (paramArrayOfChar[(paramInt1 + paramInt2 - 1)] == '\n');
/*      */     }
/*      */ 
/*      */     protected void addSpecialElement(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet)
/*      */     {
/* 3671 */       if ((paramTag != HTML.Tag.FRAME) && (!this.inParagraph) && (!this.inPre)) {
/* 3672 */         this.nextTagAfterPImplied = paramTag;
/* 3673 */         blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
/* 3674 */         this.nextTagAfterPImplied = null;
/* 3675 */         this.inParagraph = true;
/* 3676 */         this.impliedP = true;
/*      */       }
/* 3678 */       if (!canInsertTag(paramTag, paramMutableAttributeSet, paramTag.isBlock())) {
/* 3679 */         return;
/*      */       }
/* 3681 */       if (paramMutableAttributeSet.isDefined(IMPLIED)) {
/* 3682 */         paramMutableAttributeSet.removeAttribute(IMPLIED);
/*      */       }
/* 3684 */       this.emptyAnchor = false;
/* 3685 */       paramMutableAttributeSet.addAttributes(this.charAttr);
/* 3686 */       paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, paramTag);
/* 3687 */       char[] arrayOfChar = new char[1];
/* 3688 */       arrayOfChar[0] = ' ';
/* 3689 */       DefaultStyledDocument.ElementSpec localElementSpec = new DefaultStyledDocument.ElementSpec(paramMutableAttributeSet.copyAttributes(), (short)3, arrayOfChar, 0, 1);
/*      */ 
/* 3691 */       this.parseBuffer.addElement(localElementSpec);
/*      */ 
/* 3694 */       if (paramTag == HTML.Tag.FRAME)
/* 3695 */         this.lastWasNewline = true;
/*      */     }
/*      */ 
/*      */     void flushBuffer(boolean paramBoolean)
/*      */       throws BadLocationException
/*      */     {
/* 3704 */       int i = HTMLDocument.this.getLength();
/* 3705 */       int j = this.parseBuffer.size();
/* 3706 */       if ((paramBoolean) && ((this.insertTag != null) || (this.insertAfterImplied)) && (j > 0))
/*      */       {
/* 3708 */         adjustEndSpecsForPartialInsert();
/* 3709 */         j = this.parseBuffer.size();
/*      */       }
/* 3711 */       DefaultStyledDocument.ElementSpec[] arrayOfElementSpec = new DefaultStyledDocument.ElementSpec[j];
/* 3712 */       this.parseBuffer.copyInto(arrayOfElementSpec);
/*      */ 
/* 3714 */       if ((i == 0) && (this.insertTag == null) && (!this.insertAfterImplied))
/* 3715 */         HTMLDocument.this.create(arrayOfElementSpec);
/*      */       else {
/* 3717 */         HTMLDocument.this.insert(this.offset, arrayOfElementSpec);
/*      */       }
/* 3719 */       this.parseBuffer.removeAllElements();
/* 3720 */       this.offset += HTMLDocument.this.getLength() - i;
/* 3721 */       this.flushCount += 1;
/*      */     }
/*      */ 
/*      */     private void adjustEndSpecsForPartialInsert()
/*      */     {
/* 3729 */       int i = this.parseBuffer.size();
/*      */       int j;
/* 3730 */       if (this.insertTagDepthDelta < 0)
/*      */       {
/* 3734 */         j = this.insertTagDepthDelta;
/* 3735 */         while ((j < 0) && (i >= 0) && (((DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(i - 1)).getType() == 2))
/*      */         {
/* 3738 */           this.parseBuffer.removeElementAt(--i);
/* 3739 */           j++;
/*      */         }
/*      */       }
/* 3742 */       if ((this.flushCount == 0) && ((!this.insertAfterImplied) || (!this.wantsTrailingNewline)))
/*      */       {
/* 3748 */         j = 0;
/* 3749 */         if ((this.pushDepth > 0) && 
/* 3750 */           (((DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(0)).getType() == 3))
/*      */         {
/* 3752 */           j++;
/*      */         }
/*      */ 
/* 3755 */         j += this.popDepth + this.pushDepth;
/* 3756 */         int k = 0;
/* 3757 */         int m = j;
/* 3758 */         while ((j < i) && (((DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(j)).getType() == 3))
/*      */         {
/* 3760 */           j++;
/* 3761 */           k++;
/*      */         }
/* 3763 */         if (k > 1) {
/* 3764 */           while ((j < i) && (((DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(j)).getType() == 2))
/*      */           {
/* 3766 */             j++;
/*      */           }
/* 3768 */           if (j == i) {
/* 3769 */             char[] arrayOfChar = ((DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(m + k - 1)).getArray();
/*      */ 
/* 3771 */             if ((arrayOfChar.length == 1) && (arrayOfChar[0] == HTMLDocument.NEWLINE[0])) {
/* 3772 */               j = m + k - 1;
/* 3773 */               while (i > j) {
/* 3774 */                 this.parseBuffer.removeElementAt(--i);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 3780 */       if (this.wantsTrailingNewline)
/*      */       {
/* 3782 */         for (j = this.parseBuffer.size() - 1; j >= 0; 
/* 3783 */           j--) {
/* 3784 */           DefaultStyledDocument.ElementSpec localElementSpec = (DefaultStyledDocument.ElementSpec)this.parseBuffer.elementAt(j);
/* 3785 */           if (localElementSpec.getType() == 3) {
/* 3786 */             if (localElementSpec.getArray()[(localElementSpec.getLength() - 1)] == '\n') break;
/* 3787 */             SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*      */ 
/* 3789 */             localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/* 3791 */             this.parseBuffer.insertElementAt(new DefaultStyledDocument.ElementSpec(localSimpleAttributeSet, (short)3, HTMLDocument.NEWLINE, 0, 1), j + 1);
/*      */ 
/* 3795 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void addCSSRules(String paramString)
/*      */     {
/* 3806 */       StyleSheet localStyleSheet = HTMLDocument.this.getStyleSheet();
/* 3807 */       localStyleSheet.addRule(paramString);
/*      */     }
/*      */ 
/*      */     void linkCSSStyleSheet(String paramString)
/*      */     {
/*      */       URL localURL;
/*      */       try
/*      */       {
/* 3817 */         localURL = new URL(HTMLDocument.this.base, paramString);
/*      */       } catch (MalformedURLException localMalformedURLException1) {
/*      */         try {
/* 3820 */           localURL = new URL(paramString);
/*      */         } catch (MalformedURLException localMalformedURLException2) {
/* 3822 */           localURL = null;
/*      */         }
/*      */       }
/* 3825 */       if (localURL != null)
/* 3826 */         HTMLDocument.this.getStyleSheet().importStyleSheet(localURL);
/*      */     }
/*      */ 
/*      */     private boolean canInsertTag(HTML.Tag paramTag, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */     {
/* 3837 */       if (!this.foundInsertTag) {
/* 3838 */         int i = (paramTag == HTML.Tag.IMPLIED) && (!this.inParagraph) && (!this.inPre) ? 1 : 0;
/*      */ 
/* 3841 */         if ((i != 0) && (this.nextTagAfterPImplied != null))
/*      */         {
/* 3847 */           if (this.insertTag != null) {
/* 3848 */             boolean bool = isInsertTag(this.nextTagAfterPImplied);
/*      */ 
/* 3850 */             if ((!bool) || (!this.insertInsertTag)) {
/* 3851 */               return false;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/* 3857 */         else if (((this.insertTag != null) && (!isInsertTag(paramTag))) || ((this.insertAfterImplied) && ((paramAttributeSet == null) || (paramAttributeSet.isDefined(IMPLIED)) || (paramTag == HTML.Tag.IMPLIED))))
/*      */         {
/* 3865 */           return false;
/*      */         }
/*      */ 
/* 3870 */         foundInsertTag(paramBoolean);
/* 3871 */         if (!this.insertInsertTag) {
/* 3872 */           return false;
/*      */         }
/*      */       }
/* 3875 */       return true;
/*      */     }
/*      */ 
/*      */     private boolean isInsertTag(HTML.Tag paramTag) {
/* 3879 */       return this.insertTag == paramTag;
/*      */     }
/*      */ 
/*      */     private void foundInsertTag(boolean paramBoolean) {
/* 3883 */       this.foundInsertTag = true;
/* 3884 */       if ((!this.insertAfterImplied) && ((this.popDepth > 0) || (this.pushDepth > 0)))
/*      */         try {
/* 3886 */           if ((this.offset == 0) || (!HTMLDocument.this.getText(this.offset - 1, 1).equals("\n")))
/*      */           {
/* 3888 */             SimpleAttributeSet localSimpleAttributeSet = null;
/* 3889 */             int j = 1;
/*      */ 
/* 3891 */             if (this.offset != 0)
/*      */             {
/* 3895 */               localObject1 = HTMLDocument.this.getCharacterElement(this.offset - 1);
/*      */ 
/* 3897 */               AttributeSet localAttributeSet = ((Element)localObject1).getAttributes();
/*      */ 
/* 3899 */               if (localAttributeSet.isDefined(StyleConstants.ComposedTextAttribute))
/*      */               {
/* 3901 */                 j = 0;
/*      */               }
/*      */               else {
/* 3904 */                 Object localObject2 = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 3906 */                 if ((localObject2 instanceof HTML.Tag)) {
/* 3907 */                   HTML.Tag localTag = (HTML.Tag)localObject2;
/* 3908 */                   if ((localTag == HTML.Tag.IMG) || (localTag == HTML.Tag.HR) || (localTag == HTML.Tag.COMMENT) || ((localTag instanceof HTML.UnknownTag)))
/*      */                   {
/* 3912 */                     j = 0;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/* 3917 */             if (j == 0)
/*      */             {
/* 3921 */               localSimpleAttributeSet = new SimpleAttributeSet();
/* 3922 */               ((SimpleAttributeSet)localSimpleAttributeSet).addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */             }
/*      */ 
/* 3926 */             Object localObject1 = new DefaultStyledDocument.ElementSpec(localSimpleAttributeSet, (short)3, HTMLDocument.NEWLINE, 0, HTMLDocument.NEWLINE.length);
/*      */ 
/* 3929 */             if (j != 0) {
/* 3930 */               ((DefaultStyledDocument.ElementSpec)localObject1).setDirection((short)4);
/*      */             }
/*      */ 
/* 3933 */             this.parseBuffer.addElement(localObject1);
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/* 3938 */       for (int i = 0; i < this.popDepth; i++) {
/* 3939 */         this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(null, (short)2));
/*      */       }
/*      */ 
/* 3943 */       for (i = 0; i < this.pushDepth; i++) {
/* 3944 */         DefaultStyledDocument.ElementSpec localElementSpec = new DefaultStyledDocument.ElementSpec(null, (short)1);
/*      */ 
/* 3946 */         localElementSpec.setDirection((short)5);
/* 3947 */         this.parseBuffer.addElement(localElementSpec);
/*      */       }
/* 3949 */       this.insertTagDepthDelta = (depthTo(Math.max(0, this.offset - 1)) - this.popDepth + this.pushDepth - this.inBlock);
/*      */ 
/* 3951 */       if (paramBoolean)
/*      */       {
/* 3954 */         this.insertTagDepthDelta += 1;
/*      */       }
/*      */       else
/*      */       {
/* 3959 */         this.insertTagDepthDelta -= 1;
/* 3960 */         this.inParagraph = true;
/* 3961 */         this.lastWasNewline = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     class AnchorAction extends HTMLDocument.HTMLReader.CharacterAction
/*      */     {
/*      */       AnchorAction()
/*      */       {
/* 3204 */         super();
/*      */       }
/*      */ 
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3208 */         HTMLDocument.HTMLReader.this.emptyAnchor = true;
/* 3209 */         super.start(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3213 */         if (HTMLDocument.HTMLReader.this.emptyAnchor)
/*      */         {
/* 3217 */           char[] arrayOfChar = new char[1];
/* 3218 */           arrayOfChar[0] = '\n';
/* 3219 */           HTMLDocument.HTMLReader.this.addContent(arrayOfChar, 0, 1);
/*      */         }
/* 3221 */         super.end(paramTag);
/*      */       }
/*      */     }
/*      */ 
/*      */     class AreaAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       AreaAction()
/*      */       {
/* 3036 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3039 */         if (HTMLDocument.HTMLReader.this.lastMap != null)
/* 3040 */           HTMLDocument.HTMLReader.this.lastMap.addArea(paramMutableAttributeSet.copyAttributes());
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     class BaseAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       BaseAction()
/*      */       {
/* 3243 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3246 */         String str = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.HREF);
/* 3247 */         if (str != null)
/*      */           try {
/* 3249 */             URL localURL = new URL(HTMLDocument.this.base, str);
/* 3250 */             HTMLDocument.this.setBase(localURL);
/* 3251 */             HTMLDocument.this.hasBaseTag = true;
/*      */           }
/*      */           catch (MalformedURLException localMalformedURLException) {
/*      */           }
/* 3255 */         HTMLDocument.this.baseTarget = ((String)paramMutableAttributeSet.getAttribute(HTML.Attribute.TARGET));
/*      */       }
/*      */     }
/*      */ 
/*      */     public class BlockAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       public BlockAction()
/*      */       {
/* 2776 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2779 */         HTMLDocument.HTMLReader.this.blockOpen(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 2783 */         HTMLDocument.HTMLReader.this.blockClose(paramTag);
/*      */       }
/*      */     }
/*      */ 
/*      */     public class CharacterAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       public CharacterAction()
/*      */       {
/* 3090 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3093 */         HTMLDocument.HTMLReader.this.pushCharacterStyle();
/* 3094 */         if (!HTMLDocument.HTMLReader.this.foundInsertTag)
/*      */         {
/* 3099 */           boolean bool = HTMLDocument.HTMLReader.this.canInsertTag(paramTag, paramMutableAttributeSet, false);
/* 3100 */           if ((HTMLDocument.HTMLReader.this.foundInsertTag) && 
/* 3101 */             (!HTMLDocument.HTMLReader.this.inParagraph)) {
/* 3102 */             HTMLDocument.HTMLReader.this.inParagraph = (HTMLDocument.HTMLReader.this.impliedP = 1);
/*      */           }
/*      */ 
/* 3105 */           if (!bool) {
/* 3106 */             return;
/*      */           }
/*      */         }
/* 3109 */         if (paramMutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)) {
/* 3110 */           paramMutableAttributeSet.removeAttribute(HTMLEditorKit.ParserCallback.IMPLIED);
/*      */         }
/* 3112 */         HTMLDocument.HTMLReader.this.charAttr.addAttribute(paramTag, paramMutableAttributeSet.copyAttributes());
/* 3113 */         if (HTMLDocument.HTMLReader.this.styleAttributes != null)
/* 3114 */           HTMLDocument.HTMLReader.this.charAttr.addAttributes(HTMLDocument.HTMLReader.this.styleAttributes);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/* 3119 */         HTMLDocument.HTMLReader.this.popCharacterStyle();
/*      */       }
/*      */     }
/*      */ 
/*      */     class ConvertAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       ConvertAction()
/*      */       {
/* 3128 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3131 */         HTMLDocument.HTMLReader.this.pushCharacterStyle();
/* 3132 */         if (!HTMLDocument.HTMLReader.this.foundInsertTag)
/*      */         {
/* 3137 */           boolean bool = HTMLDocument.HTMLReader.this.canInsertTag(paramTag, paramMutableAttributeSet, false);
/* 3138 */           if ((HTMLDocument.HTMLReader.this.foundInsertTag) && 
/* 3139 */             (!HTMLDocument.HTMLReader.this.inParagraph)) {
/* 3140 */             HTMLDocument.HTMLReader.this.inParagraph = (HTMLDocument.HTMLReader.this.impliedP = 1);
/*      */           }
/*      */ 
/* 3143 */           if (!bool) {
/* 3144 */             return;
/*      */           }
/*      */         }
/* 3147 */         if (paramMutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)) {
/* 3148 */           paramMutableAttributeSet.removeAttribute(HTMLEditorKit.ParserCallback.IMPLIED);
/*      */         }
/* 3150 */         if (HTMLDocument.HTMLReader.this.styleAttributes != null) {
/* 3151 */           HTMLDocument.HTMLReader.this.charAttr.addAttributes(HTMLDocument.HTMLReader.this.styleAttributes);
/*      */         }
/*      */ 
/* 3156 */         HTMLDocument.HTMLReader.this.charAttr.addAttribute(paramTag, paramMutableAttributeSet.copyAttributes());
/* 3157 */         StyleSheet localStyleSheet = HTMLDocument.this.getStyleSheet();
/* 3158 */         if (paramTag == HTML.Tag.B) {
/* 3159 */           localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.FONT_WEIGHT, "bold");
/* 3160 */         } else if (paramTag == HTML.Tag.I) {
/* 3161 */           localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.FONT_STYLE, "italic");
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject;
/*      */           String str1;
/* 3162 */           if (paramTag == HTML.Tag.U) {
/* 3163 */             localObject = HTMLDocument.HTMLReader.this.charAttr.getAttribute(CSS.Attribute.TEXT_DECORATION);
/* 3164 */             str1 = "underline";
/* 3165 */             str1 = localObject != null ? str1 + "," + localObject.toString() : str1;
/* 3166 */             localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.TEXT_DECORATION, str1);
/* 3167 */           } else if (paramTag == HTML.Tag.STRIKE) {
/* 3168 */             localObject = HTMLDocument.HTMLReader.this.charAttr.getAttribute(CSS.Attribute.TEXT_DECORATION);
/* 3169 */             str1 = "line-through";
/* 3170 */             str1 = localObject != null ? str1 + "," + localObject.toString() : str1;
/* 3171 */             localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.TEXT_DECORATION, str1);
/* 3172 */           } else if (paramTag == HTML.Tag.SUP) {
/* 3173 */             localObject = HTMLDocument.HTMLReader.this.charAttr.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
/* 3174 */             str1 = "sup";
/* 3175 */             str1 = localObject != null ? str1 + "," + localObject.toString() : str1;
/* 3176 */             localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.VERTICAL_ALIGN, str1);
/* 3177 */           } else if (paramTag == HTML.Tag.SUB) {
/* 3178 */             localObject = HTMLDocument.HTMLReader.this.charAttr.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
/* 3179 */             str1 = "sub";
/* 3180 */             str1 = localObject != null ? str1 + "," + localObject.toString() : str1;
/* 3181 */             localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.VERTICAL_ALIGN, str1);
/* 3182 */           } else if (paramTag == HTML.Tag.FONT) {
/* 3183 */             localObject = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.COLOR);
/* 3184 */             if (localObject != null) {
/* 3185 */               localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.COLOR, (String)localObject);
/*      */             }
/* 3187 */             str1 = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.FACE);
/* 3188 */             if (str1 != null) {
/* 3189 */               localStyleSheet.addCSSAttribute(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.FONT_FAMILY, str1);
/*      */             }
/* 3191 */             String str2 = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.SIZE);
/* 3192 */             if (str2 != null)
/* 3193 */               localStyleSheet.addCSSAttributeFromHTML(HTMLDocument.HTMLReader.this.charAttr, CSS.Attribute.FONT_SIZE, str2);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3199 */         HTMLDocument.HTMLReader.this.popCharacterStyle();
/*      */       }
/*      */     }
/*      */ 
/*      */     public class FormAction extends HTMLDocument.HTMLReader.SpecialAction
/*      */     {
/*      */       Object selectModel;
/*      */       int optionCount;
/*      */ 
/*      */       public FormAction()
/*      */       {
/* 3335 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3338 */         if (paramTag == HTML.Tag.INPUT) {
/* 3339 */           String str = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.TYPE);
/*      */ 
/* 3345 */           if (str == null) {
/* 3346 */             str = "text";
/* 3347 */             paramMutableAttributeSet.addAttribute(HTML.Attribute.TYPE, "text");
/*      */           }
/* 3349 */           setModel(str, paramMutableAttributeSet);
/* 3350 */         } else if (paramTag == HTML.Tag.TEXTAREA) {
/* 3351 */           HTMLDocument.HTMLReader.this.inTextArea = true;
/* 3352 */           HTMLDocument.HTMLReader.this.textAreaDocument = new TextAreaDocument();
/* 3353 */           paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, HTMLDocument.HTMLReader.this.textAreaDocument);
/*      */         }
/* 3355 */         else if (paramTag == HTML.Tag.SELECT) {
/* 3356 */           int i = HTML.getIntegerAttributeValue(paramMutableAttributeSet, HTML.Attribute.SIZE, 1);
/*      */ 
/* 3359 */           int j = paramMutableAttributeSet.getAttribute(HTML.Attribute.MULTIPLE) != null ? 1 : 0;
/* 3360 */           if ((i > 1) || (j != 0)) {
/* 3361 */             OptionListModel localOptionListModel = new OptionListModel();
/* 3362 */             if (j != 0) {
/* 3363 */               localOptionListModel.setSelectionMode(2);
/*      */             }
/* 3365 */             this.selectModel = localOptionListModel;
/*      */           } else {
/* 3367 */             this.selectModel = new OptionComboBoxModel();
/*      */           }
/* 3369 */           paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, this.selectModel);
/*      */         }
/*      */ 
/* 3375 */         if (paramTag == HTML.Tag.OPTION) {
/* 3376 */           HTMLDocument.HTMLReader.this.option = new Option(paramMutableAttributeSet);
/*      */           Object localObject;
/* 3378 */           if ((this.selectModel instanceof OptionListModel)) {
/* 3379 */             localObject = (OptionListModel)this.selectModel;
/* 3380 */             ((OptionListModel)localObject).addElement(HTMLDocument.HTMLReader.this.option);
/* 3381 */             if (HTMLDocument.HTMLReader.this.option.isSelected()) {
/* 3382 */               ((OptionListModel)localObject).addSelectionInterval(this.optionCount, this.optionCount);
/* 3383 */               ((OptionListModel)localObject).setInitialSelection(this.optionCount);
/*      */             }
/* 3385 */           } else if ((this.selectModel instanceof OptionComboBoxModel)) {
/* 3386 */             localObject = (OptionComboBoxModel)this.selectModel;
/* 3387 */             ((OptionComboBoxModel)localObject).addElement(HTMLDocument.HTMLReader.this.option);
/* 3388 */             if (HTMLDocument.HTMLReader.this.option.isSelected()) {
/* 3389 */               ((OptionComboBoxModel)localObject).setSelectedItem(HTMLDocument.HTMLReader.this.option);
/* 3390 */               ((OptionComboBoxModel)localObject).setInitialSelection(HTMLDocument.HTMLReader.this.option);
/*      */             }
/*      */           }
/* 3393 */           this.optionCount += 1;
/*      */         } else {
/* 3395 */           super.start(paramTag, paramMutableAttributeSet);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3400 */         if (paramTag == HTML.Tag.OPTION) {
/* 3401 */           HTMLDocument.HTMLReader.this.option = null;
/*      */         } else {
/* 3403 */           if (paramTag == HTML.Tag.SELECT) {
/* 3404 */             this.selectModel = null;
/* 3405 */             this.optionCount = 0;
/* 3406 */           } else if (paramTag == HTML.Tag.TEXTAREA) {
/* 3407 */             HTMLDocument.HTMLReader.this.inTextArea = false;
/*      */ 
/* 3415 */             HTMLDocument.HTMLReader.this.textAreaDocument.storeInitialText();
/*      */           }
/* 3417 */           super.end(paramTag);
/*      */         }
/*      */       }
/*      */ 
/*      */       void setModel(String paramString, MutableAttributeSet paramMutableAttributeSet) {
/* 3422 */         if ((paramString.equals("submit")) || (paramString.equals("reset")) || (paramString.equals("image")))
/*      */         {
/* 3427 */           paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, new DefaultButtonModel());
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject1;
/*      */           Object localObject2;
/* 3429 */           if ((paramString.equals("text")) || (paramString.equals("password")))
/*      */           {
/* 3432 */             int i = HTML.getIntegerAttributeValue(paramMutableAttributeSet, HTML.Attribute.MAXLENGTH, -1);
/*      */ 
/* 3436 */             if (i > 0) {
/* 3437 */               localObject1 = new HTMLDocument.FixedLengthDocument(i);
/*      */             }
/*      */             else {
/* 3440 */               localObject1 = new PlainDocument();
/*      */             }
/* 3442 */             localObject2 = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.VALUE);
/*      */             try
/*      */             {
/* 3445 */               ((Document)localObject1).insertString(0, (String)localObject2, null);
/*      */             } catch (BadLocationException localBadLocationException) {
/*      */             }
/* 3448 */             paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, localObject1);
/* 3449 */           } else if (paramString.equals("file"))
/*      */           {
/* 3451 */             paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, new PlainDocument());
/*      */           }
/* 3453 */           else if ((paramString.equals("checkbox")) || (paramString.equals("radio")))
/*      */           {
/* 3455 */             JToggleButton.ToggleButtonModel localToggleButtonModel = new JToggleButton.ToggleButtonModel();
/* 3456 */             if (paramString.equals("radio")) {
/* 3457 */               localObject1 = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.NAME);
/* 3458 */               if (HTMLDocument.this.radioButtonGroupsMap == null) {
/* 3459 */                 HTMLDocument.this.radioButtonGroupsMap = new HashMap();
/*      */               }
/* 3461 */               localObject2 = (ButtonGroup)HTMLDocument.this.radioButtonGroupsMap.get(localObject1);
/* 3462 */               if (localObject2 == null) {
/* 3463 */                 localObject2 = new ButtonGroup();
/* 3464 */                 HTMLDocument.this.radioButtonGroupsMap.put(localObject1, localObject2);
/*      */               }
/* 3466 */               localToggleButtonModel.setGroup((ButtonGroup)localObject2);
/*      */             }
/* 3468 */             boolean bool = paramMutableAttributeSet.getAttribute(HTML.Attribute.CHECKED) != null;
/* 3469 */             localToggleButtonModel.setSelected(bool);
/* 3470 */             paramMutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, localToggleButtonModel);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private class FormTagAction extends HTMLDocument.HTMLReader.BlockAction
/*      */     {
/*      */       private FormTagAction()
/*      */       {
/* 2792 */         super();
/*      */       }
/* 2794 */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) { super.start(paramTag, paramMutableAttributeSet);
/*      */ 
/* 2801 */         HTMLDocument.this.radioButtonGroupsMap = new HashMap(); }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/* 2805 */         super.end(paramTag);
/*      */ 
/* 2808 */         HTMLDocument.this.radioButtonGroupsMap = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     class HeadAction extends HTMLDocument.HTMLReader.BlockAction
/*      */     {
/*      */       HeadAction()
/*      */       {
/* 2906 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2909 */         HTMLDocument.HTMLReader.this.inHead = true;
/*      */ 
/* 2913 */         if (((HTMLDocument.HTMLReader.this.insertTag == null) && (!HTMLDocument.HTMLReader.this.insertAfterImplied)) || (HTMLDocument.HTMLReader.this.insertTag == HTML.Tag.HEAD) || ((HTMLDocument.HTMLReader.this.insertAfterImplied) && ((HTMLDocument.HTMLReader.this.foundInsertTag) || (!paramMutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)))))
/*      */         {
/* 2917 */           super.start(paramTag, paramMutableAttributeSet);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 2922 */         HTMLDocument.HTMLReader.this.inHead = (HTMLDocument.HTMLReader.this.inStyle = 0);
/*      */ 
/* 2924 */         if (HTMLDocument.HTMLReader.this.styles != null) {
/* 2925 */           boolean bool1 = HTMLDocument.HTMLReader.this.isStyleCSS;
/* 2926 */           int i = 0; int j = HTMLDocument.HTMLReader.this.styles.size();
/* 2927 */           while (i < j) {
/* 2928 */             Object localObject = HTMLDocument.HTMLReader.this.styles.elementAt(i);
/* 2929 */             if (localObject == HTML.Tag.LINK) {
/* 2930 */               handleLink((AttributeSet)HTMLDocument.HTMLReader.this.styles.elementAt(++i));
/*      */ 
/* 2932 */               i++;
/*      */             }
/*      */             else
/*      */             {
/* 2937 */               String str = (String)HTMLDocument.HTMLReader.this.styles.elementAt(++i);
/* 2938 */               boolean bool2 = str == null ? bool1 : str.equals("text/css");
/*      */               while (true) {
/* 2940 */                 i++; if ((i >= j) || (!(HTMLDocument.HTMLReader.this.styles.elementAt(i) instanceof String))) {
/*      */                   break;
/*      */                 }
/* 2943 */                 if (bool2) {
/* 2944 */                   HTMLDocument.HTMLReader.this.addCSSRules((String)HTMLDocument.HTMLReader.this.styles.elementAt(i));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2951 */         if (((HTMLDocument.HTMLReader.this.insertTag == null) && (!HTMLDocument.HTMLReader.this.insertAfterImplied)) || (HTMLDocument.HTMLReader.this.insertTag == HTML.Tag.HEAD) || ((HTMLDocument.HTMLReader.this.insertAfterImplied) && (HTMLDocument.HTMLReader.this.foundInsertTag)))
/*      */         {
/* 2954 */           super.end(paramTag);
/*      */         }
/*      */       }
/*      */ 
/*      */       boolean isEmpty(HTML.Tag paramTag) {
/* 2959 */         return false;
/*      */       }
/*      */ 
/*      */       private void handleLink(AttributeSet paramAttributeSet)
/*      */       {
/* 2964 */         String str1 = (String)paramAttributeSet.getAttribute(HTML.Attribute.TYPE);
/* 2965 */         if (str1 == null) {
/* 2966 */           str1 = HTMLDocument.this.getDefaultStyleSheetType();
/*      */         }
/*      */ 
/* 2972 */         if (str1.equals("text/css")) {
/* 2973 */           String str2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.REL);
/* 2974 */           String str3 = (String)paramAttributeSet.getAttribute(HTML.Attribute.TITLE);
/*      */ 
/* 2976 */           String str4 = (String)paramAttributeSet.getAttribute(HTML.Attribute.MEDIA);
/*      */ 
/* 2978 */           if (str4 == null) {
/* 2979 */             str4 = "all";
/*      */           }
/*      */           else {
/* 2982 */             str4 = str4.toLowerCase();
/*      */           }
/* 2984 */           if (str2 != null) {
/* 2985 */             str2 = str2.toLowerCase();
/* 2986 */             if (((str4.indexOf("all") != -1) || (str4.indexOf("screen") != -1)) && ((str2.equals("stylesheet")) || ((str2.equals("alternate stylesheet")) && (str3.equals(HTMLDocument.HTMLReader.this.defaultStyle)))))
/*      */             {
/* 2991 */               HTMLDocument.HTMLReader.this.linkCSSStyleSheet((String)paramAttributeSet.getAttribute(HTML.Attribute.HREF));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public class HiddenAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       public HiddenAction()
/*      */       {
/* 2845 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2848 */         HTMLDocument.HTMLReader.this.addSpecialElement(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 2852 */         if (!isEmpty(paramTag)) {
/* 2853 */           SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 2854 */           localSimpleAttributeSet.addAttribute(HTML.Attribute.ENDTAG, "true");
/* 2855 */           HTMLDocument.HTMLReader.this.addSpecialElement(paramTag, localSimpleAttributeSet);
/*      */         }
/*      */       }
/*      */ 
/*      */       boolean isEmpty(HTML.Tag paramTag) {
/* 2860 */         if ((paramTag == HTML.Tag.APPLET) || (paramTag == HTML.Tag.SCRIPT))
/*      */         {
/* 2862 */           return false;
/*      */         }
/* 2864 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */     public class IsindexAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       public IsindexAction()
/*      */       {
/* 2834 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2837 */         HTMLDocument.HTMLReader.this.blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
/* 2838 */         HTMLDocument.HTMLReader.this.addSpecialElement(paramTag, paramMutableAttributeSet);
/* 2839 */         HTMLDocument.HTMLReader.this.blockClose(HTML.Tag.IMPLIED);
/*      */       }
/*      */     }
/*      */ 
/*      */     class LinkAction extends HTMLDocument.HTMLReader.HiddenAction
/*      */     {
/*      */       LinkAction()
/*      */       {
/* 3005 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3008 */         String str = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.REL);
/* 3009 */         if (str != null) {
/* 3010 */           str = str.toLowerCase();
/* 3011 */           if ((str.equals("stylesheet")) || (str.equals("alternate stylesheet")))
/*      */           {
/* 3013 */             if (HTMLDocument.HTMLReader.this.styles == null) {
/* 3014 */               HTMLDocument.HTMLReader.this.styles = new Vector(3);
/*      */             }
/* 3016 */             HTMLDocument.HTMLReader.this.styles.addElement(paramTag);
/* 3017 */             HTMLDocument.HTMLReader.this.styles.addElement(paramMutableAttributeSet.copyAttributes());
/*      */           }
/*      */         }
/* 3020 */         super.start(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */     }
/*      */     class MapAction extends HTMLDocument.HTMLReader.TagAction {
/* 3024 */       MapAction() { super(); }
/*      */ 
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3027 */         HTMLDocument.HTMLReader.this.lastMap = new Map((String)paramMutableAttributeSet.getAttribute(HTML.Attribute.NAME));
/* 3028 */         HTMLDocument.this.addMap(HTMLDocument.HTMLReader.this.lastMap);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     class MetaAction extends HTMLDocument.HTMLReader.HiddenAction
/*      */     {
/*      */       MetaAction()
/*      */       {
/* 2873 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2876 */         Object localObject = paramMutableAttributeSet.getAttribute(HTML.Attribute.HTTPEQUIV);
/* 2877 */         if (localObject != null) {
/* 2878 */           localObject = ((String)localObject).toLowerCase();
/* 2879 */           if (localObject.equals("content-style-type")) {
/* 2880 */             String str = (String)paramMutableAttributeSet.getAttribute(HTML.Attribute.CONTENT);
/*      */ 
/* 2882 */             HTMLDocument.this.setDefaultStyleSheetType(str);
/* 2883 */             HTMLDocument.HTMLReader.this.isStyleCSS = "text/css".equals(HTMLDocument.this.getDefaultStyleSheetType());
/*      */           }
/* 2886 */           else if (localObject.equals("default-style")) {
/* 2887 */             HTMLDocument.HTMLReader.this.defaultStyle = ((String)paramMutableAttributeSet.getAttribute(HTML.Attribute.CONTENT));
/*      */           }
/*      */         }
/*      */ 
/* 2891 */         super.start(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       boolean isEmpty(HTML.Tag paramTag) {
/* 2895 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */     class ObjectAction extends HTMLDocument.HTMLReader.SpecialAction
/*      */     {
/*      */       ObjectAction()
/*      */       {
/* 3259 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3262 */         if (paramTag == HTML.Tag.PARAM)
/* 3263 */           addParameter(paramMutableAttributeSet);
/*      */         else
/* 3265 */           super.start(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/* 3270 */         if (paramTag != HTML.Tag.PARAM)
/* 3271 */           super.end(paramTag);
/*      */       }
/*      */ 
/*      */       void addParameter(AttributeSet paramAttributeSet)
/*      */       {
/* 3276 */         String str1 = (String)paramAttributeSet.getAttribute(HTML.Attribute.NAME);
/* 3277 */         String str2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.VALUE);
/* 3278 */         if ((str1 != null) && (str2 != null)) {
/* 3279 */           DefaultStyledDocument.ElementSpec localElementSpec = (DefaultStyledDocument.ElementSpec)HTMLDocument.HTMLReader.this.parseBuffer.lastElement();
/* 3280 */           MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)localElementSpec.getAttributes();
/* 3281 */           localMutableAttributeSet.addAttribute(str1, str2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public class ParagraphAction extends HTMLDocument.HTMLReader.BlockAction
/*      */     {
/*      */       public ParagraphAction()
/*      */       {
/* 2813 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2816 */         super.start(paramTag, paramMutableAttributeSet);
/* 2817 */         HTMLDocument.HTMLReader.this.inParagraph = true;
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 2821 */         super.end(paramTag);
/* 2822 */         HTMLDocument.HTMLReader.this.inParagraph = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     public class PreAction extends HTMLDocument.HTMLReader.BlockAction
/*      */     {
/*      */       public PreAction()
/*      */       {
/* 3072 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3075 */         HTMLDocument.HTMLReader.this.inPre = true;
/* 3076 */         HTMLDocument.HTMLReader.this.blockOpen(paramTag, paramMutableAttributeSet);
/* 3077 */         paramMutableAttributeSet.addAttribute(CSS.Attribute.WHITE_SPACE, "pre");
/* 3078 */         HTMLDocument.HTMLReader.this.blockOpen(HTML.Tag.IMPLIED, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3082 */         HTMLDocument.HTMLReader.this.blockClose(HTML.Tag.IMPLIED);
/*      */ 
/* 3085 */         HTMLDocument.HTMLReader.this.inPre = false;
/* 3086 */         HTMLDocument.HTMLReader.this.blockClose(paramTag);
/*      */       }
/*      */     }
/*      */ 
/*      */     public class SpecialAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       public SpecialAction()
/*      */       {
/* 2826 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 2829 */         HTMLDocument.HTMLReader.this.addSpecialElement(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */     }
/*      */ 
/*      */     class StyleAction extends HTMLDocument.HTMLReader.TagAction
/*      */     {
/*      */       StyleAction()
/*      */       {
/* 3049 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3052 */         if (HTMLDocument.HTMLReader.this.inHead) {
/* 3053 */           if (HTMLDocument.HTMLReader.this.styles == null) {
/* 3054 */             HTMLDocument.HTMLReader.this.styles = new Vector(3);
/*      */           }
/* 3056 */           HTMLDocument.HTMLReader.this.styles.addElement(paramTag);
/* 3057 */           HTMLDocument.HTMLReader.this.styles.addElement(paramMutableAttributeSet.getAttribute(HTML.Attribute.TYPE));
/* 3058 */           HTMLDocument.HTMLReader.this.inStyle = true;
/*      */         }
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3063 */         HTMLDocument.HTMLReader.this.inStyle = false;
/*      */       }
/*      */ 
/*      */       boolean isEmpty(HTML.Tag paramTag) {
/* 3067 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     public class TagAction
/*      */     {
/*      */       public TagAction()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     class TitleAction extends HTMLDocument.HTMLReader.HiddenAction
/*      */     {
/*      */       TitleAction()
/*      */       {
/* 3225 */         super();
/*      */       }
/*      */       public void start(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet) {
/* 3228 */         HTMLDocument.HTMLReader.this.inTitle = true;
/* 3229 */         super.start(paramTag, paramMutableAttributeSet);
/*      */       }
/*      */ 
/*      */       public void end(HTML.Tag paramTag) {
/* 3233 */         HTMLDocument.HTMLReader.this.inTitle = false;
/* 3234 */         super.end(paramTag);
/*      */       }
/*      */ 
/*      */       boolean isEmpty(HTML.Tag paramTag) {
/* 3238 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Iterator
/*      */   {
/*      */     public abstract AttributeSet getAttributes();
/*      */ 
/*      */     public abstract int getStartOffset();
/*      */ 
/*      */     public abstract int getEndOffset();
/*      */ 
/*      */     public abstract void next();
/*      */ 
/*      */     public abstract boolean isValid();
/*      */ 
/*      */     public abstract HTML.Tag getTag();
/*      */   }
/*      */ 
/*      */   static class LeafIterator extends HTMLDocument.Iterator
/*      */   {
/*      */     private int endOffset;
/*      */     private HTML.Tag tag;
/*      */     private ElementIterator pos;
/*      */ 
/*      */     LeafIterator(HTML.Tag paramTag, Document paramDocument)
/*      */     {
/* 1897 */       this.tag = paramTag;
/* 1898 */       this.pos = new ElementIterator(paramDocument);
/* 1899 */       this.endOffset = 0;
/* 1900 */       next();
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 1909 */       Element localElement = this.pos.current();
/* 1910 */       if (localElement != null) {
/* 1911 */         AttributeSet localAttributeSet = (AttributeSet)localElement.getAttributes().getAttribute(this.tag);
/*      */ 
/* 1913 */         if (localAttributeSet == null) {
/* 1914 */           localAttributeSet = localElement.getAttributes();
/*      */         }
/* 1916 */         return localAttributeSet;
/*      */       }
/* 1918 */       return null;
/*      */     }
/*      */ 
/*      */     public int getStartOffset()
/*      */     {
/* 1928 */       Element localElement = this.pos.current();
/* 1929 */       if (localElement != null) {
/* 1930 */         return localElement.getStartOffset();
/*      */       }
/* 1932 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getEndOffset()
/*      */     {
/* 1942 */       return this.endOffset;
/*      */     }
/*      */ 
/*      */     public void next()
/*      */     {
/* 1950 */       for (nextLeaf(this.pos); isValid(); nextLeaf(this.pos)) {
/* 1951 */         Element localElement = this.pos.current();
/* 1952 */         if (localElement.getStartOffset() >= this.endOffset) {
/* 1953 */           AttributeSet localAttributeSet = this.pos.current().getAttributes();
/*      */ 
/* 1955 */           if ((localAttributeSet.isDefined(this.tag)) || (localAttributeSet.getAttribute(StyleConstants.NameAttribute) == this.tag))
/*      */           {
/* 1959 */             setEndOffset();
/* 1960 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public HTML.Tag getTag()
/*      */     {
/* 1973 */       return this.tag;
/*      */     }
/*      */ 
/*      */     public boolean isValid()
/*      */     {
/* 1982 */       return this.pos.current() != null;
/*      */     }
/*      */ 
/*      */     void nextLeaf(ElementIterator paramElementIterator)
/*      */     {
/* 1990 */       for (paramElementIterator.next(); paramElementIterator.current() != null; paramElementIterator.next()) {
/* 1991 */         Element localElement = paramElementIterator.current();
/* 1992 */         if (localElement.isLeaf())
/*      */           break;
/*      */       }
/*      */     }
/*      */ 
/*      */     void setEndOffset()
/*      */     {
/* 2003 */       AttributeSet localAttributeSet1 = getAttributes();
/* 2004 */       this.endOffset = this.pos.current().getEndOffset();
/* 2005 */       ElementIterator localElementIterator = (ElementIterator)this.pos.clone();
/* 2006 */       for (nextLeaf(localElementIterator); localElementIterator.current() != null; nextLeaf(localElementIterator)) {
/* 2007 */         Element localElement = localElementIterator.current();
/* 2008 */         AttributeSet localAttributeSet2 = (AttributeSet)localElement.getAttributes().getAttribute(this.tag);
/* 2009 */         if ((localAttributeSet2 == null) || (!localAttributeSet2.equals(localAttributeSet1))) {
/*      */           break;
/*      */         }
/* 2012 */         this.endOffset = localElement.getEndOffset();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class RunElement extends AbstractDocument.LeafElement
/*      */   {
/*      */     public RunElement(Element paramAttributeSet, AttributeSet paramInt1, int paramInt2, int arg5)
/*      */     {
/* 4090 */       super(paramAttributeSet, paramInt1, paramInt2, i);
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 4099 */       Object localObject = getAttribute(StyleConstants.NameAttribute);
/* 4100 */       if (localObject != null) {
/* 4101 */         return localObject.toString();
/*      */       }
/* 4103 */       return super.getName();
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/* 4114 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TaggedAttributeSet extends SimpleAttributeSet
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HTMLDocument
 * JD-Core Version:    0.6.2
 */