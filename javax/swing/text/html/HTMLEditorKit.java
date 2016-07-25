/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.io.Writer;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JEditorPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.SizeRequirements;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.HyperlinkEvent;
/*      */ import javax.swing.event.HyperlinkEvent.EventType;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.BoxView;
/*      */ import javax.swing.text.ComponentView;
/*      */ import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.EditorKit;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.ElementIterator;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.IconView;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.LabelView;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.StyledEditorKit;
/*      */ import javax.swing.text.StyledEditorKit.StyledTextAction;
/*      */ import javax.swing.text.TextAction;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import sun.awt.AppContext;
/*      */ 
/*      */ public class HTMLEditorKit extends StyledEditorKit
/*      */   implements Accessible
/*      */ {
/*      */   private JEditorPane theEditor;
/*      */   public static final String DEFAULT_CSS = "default.css";
/*      */   private AccessibleContext accessibleContext;
/*  624 */   private static final Cursor MoveCursor = Cursor.getPredefinedCursor(12);
/*      */ 
/*  626 */   private static final Cursor DefaultCursor = Cursor.getPredefinedCursor(0);
/*      */ 
/*  630 */   private static final ViewFactory defaultFactory = new HTMLFactory();
/*      */   MutableAttributeSet input;
/*  633 */   private static final Object DEFAULT_STYLES_KEY = new Object();
/*  634 */   private LinkController linkHandler = new LinkController();
/*  635 */   private static Parser defaultParser = null;
/*  636 */   private Cursor defaultCursor = DefaultCursor;
/*  637 */   private Cursor linkCursor = MoveCursor;
/*  638 */   private boolean isAutoFormSubmission = true;
/*      */   public static final String BOLD_ACTION = "html-bold-action";
/*      */   public static final String ITALIC_ACTION = "html-italic-action";
/*      */   public static final String PARA_INDENT_LEFT = "html-para-indent-left";
/*      */   public static final String PARA_INDENT_RIGHT = "html-para-indent-right";
/*      */   public static final String FONT_CHANGE_BIGGER = "html-font-bigger";
/*      */   public static final String FONT_CHANGE_SMALLER = "html-font-smaller";
/*      */   public static final String COLOR_ACTION = "html-color-action";
/*      */   public static final String LOGICAL_STYLE_ACTION = "html-logical-style-action";
/*      */   public static final String IMG_ALIGN_TOP = "html-image-align-top";
/*      */   public static final String IMG_ALIGN_MIDDLE = "html-image-align-middle";
/*      */   public static final String IMG_ALIGN_BOTTOM = "html-image-align-bottom";
/*      */   public static final String IMG_BORDER = "html-image-border";
/*      */   private static final String INSERT_TABLE_HTML = "<table border=1><tr><td></td></tr></table>";
/*      */   private static final String INSERT_UL_HTML = "<ul><li></li></ul>";
/*      */   private static final String INSERT_OL_HTML = "<ol><li></li></ol>";
/*      */   private static final String INSERT_HR_HTML = "<hr>";
/*      */   private static final String INSERT_PRE_HTML = "<pre></pre>";
/* 1436 */   private static final NavigateLinkAction nextLinkAction = new NavigateLinkAction("next-link-action");
/*      */ 
/* 1439 */   private static final NavigateLinkAction previousLinkAction = new NavigateLinkAction("previous-link-action");
/*      */ 
/* 1442 */   private static final ActivateLinkAction activateLinkAction = new ActivateLinkAction("activate-link-action");
/*      */ 
/* 1445 */   private static final Action[] defaultActions = { new InsertHTMLTextAction("InsertTable", "<table border=1><tr><td></td></tr></table>", HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertTableRow", "<table border=1><tr><td></td></tr></table>", HTML.Tag.TABLE, HTML.Tag.TR, HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertTableDataCell", "<table border=1><tr><td></td></tr></table>", HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertUnorderedList", "<ul><li></li></ul>", HTML.Tag.BODY, HTML.Tag.UL), new InsertHTMLTextAction("InsertUnorderedListItem", "<ul><li></li></ul>", HTML.Tag.UL, HTML.Tag.LI, HTML.Tag.BODY, HTML.Tag.UL), new InsertHTMLTextAction("InsertOrderedList", "<ol><li></li></ol>", HTML.Tag.BODY, HTML.Tag.OL), new InsertHTMLTextAction("InsertOrderedListItem", "<ol><li></li></ol>", HTML.Tag.OL, HTML.Tag.LI, HTML.Tag.BODY, HTML.Tag.OL), new InsertHRAction(), new InsertHTMLTextAction("InsertPre", "<pre></pre>", HTML.Tag.BODY, HTML.Tag.PRE), nextLinkAction, previousLinkAction, activateLinkAction, new BeginAction("caret-begin", false), new BeginAction("selection-begin", true) };
/*      */ 
/* 1474 */   private boolean foundLink = false;
/* 1475 */   private int prevHypertextOffset = -1;
/*      */   private Object linkNavigationTag;
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  184 */     return "text/html";
/*      */   }
/*      */ 
/*      */   public ViewFactory getViewFactory()
/*      */   {
/*  195 */     return defaultFactory;
/*      */   }
/*      */ 
/*      */   public Document createDefaultDocument()
/*      */   {
/*  205 */     StyleSheet localStyleSheet1 = getStyleSheet();
/*  206 */     StyleSheet localStyleSheet2 = new StyleSheet();
/*      */ 
/*  208 */     localStyleSheet2.addStyleSheet(localStyleSheet1);
/*      */ 
/*  210 */     HTMLDocument localHTMLDocument = new HTMLDocument(localStyleSheet2);
/*  211 */     localHTMLDocument.setParser(getParser());
/*  212 */     localHTMLDocument.setAsynchronousLoadPriority(4);
/*  213 */     localHTMLDocument.setTokenThreshold(100);
/*  214 */     return localHTMLDocument;
/*      */   }
/*      */ 
/*      */   private Parser ensureParser(HTMLDocument paramHTMLDocument)
/*      */     throws IOException
/*      */   {
/*  223 */     Parser localParser = paramHTMLDocument.getParser();
/*  224 */     if (localParser == null) {
/*  225 */       localParser = getParser();
/*      */     }
/*  227 */     if (localParser == null) {
/*  228 */       throw new IOException("Can't load parser");
/*      */     }
/*  230 */     return localParser;
/*      */   }
/*      */ 
/*      */   public void read(Reader paramReader, Document paramDocument, int paramInt)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  253 */     if ((paramDocument instanceof HTMLDocument)) {
/*  254 */       HTMLDocument localHTMLDocument = (HTMLDocument)paramDocument;
/*  255 */       if (paramInt > paramDocument.getLength()) {
/*  256 */         throw new BadLocationException("Invalid location", paramInt);
/*      */       }
/*      */ 
/*  259 */       Parser localParser = ensureParser(localHTMLDocument);
/*  260 */       ParserCallback localParserCallback = localHTMLDocument.getReader(paramInt);
/*  261 */       Boolean localBoolean = (Boolean)paramDocument.getProperty("IgnoreCharsetDirective");
/*  262 */       localParser.parse(paramReader, localParserCallback, localBoolean == null ? false : localBoolean.booleanValue());
/*  263 */       localParserCallback.flush();
/*      */     } else {
/*  265 */       super.read(paramReader, paramDocument, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertHTML(HTMLDocument paramHTMLDocument, int paramInt1, String paramString, int paramInt2, int paramInt3, HTML.Tag paramTag)
/*      */     throws BadLocationException, IOException
/*      */   {
/*  287 */     if (paramInt1 > paramHTMLDocument.getLength()) {
/*  288 */       throw new BadLocationException("Invalid location", paramInt1);
/*      */     }
/*      */ 
/*  291 */     Parser localParser = ensureParser(paramHTMLDocument);
/*  292 */     ParserCallback localParserCallback = paramHTMLDocument.getReader(paramInt1, paramInt2, paramInt3, paramTag);
/*      */ 
/*  294 */     Boolean localBoolean = (Boolean)paramHTMLDocument.getProperty("IgnoreCharsetDirective");
/*      */ 
/*  296 */     localParser.parse(new StringReader(paramString), localParserCallback, localBoolean == null ? false : localBoolean.booleanValue());
/*      */ 
/*  298 */     localParserCallback.flush();
/*      */   }
/*      */ 
/*      */   public void write(Writer paramWriter, Document paramDocument, int paramInt1, int paramInt2)
/*      */     throws IOException, BadLocationException
/*      */   {
/*      */     Object localObject;
/*  317 */     if ((paramDocument instanceof HTMLDocument)) {
/*  318 */       localObject = new HTMLWriter(paramWriter, (HTMLDocument)paramDocument, paramInt1, paramInt2);
/*  319 */       ((HTMLWriter)localObject).write();
/*  320 */     } else if ((paramDocument instanceof StyledDocument)) {
/*  321 */       localObject = new MinimalHTMLWriter(paramWriter, (StyledDocument)paramDocument, paramInt1, paramInt2);
/*  322 */       ((MinimalHTMLWriter)localObject).write();
/*      */     } else {
/*  324 */       super.write(paramWriter, paramDocument, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void install(JEditorPane paramJEditorPane)
/*      */   {
/*  335 */     paramJEditorPane.addMouseListener(this.linkHandler);
/*  336 */     paramJEditorPane.addMouseMotionListener(this.linkHandler);
/*  337 */     paramJEditorPane.addCaretListener(nextLinkAction);
/*  338 */     super.install(paramJEditorPane);
/*  339 */     this.theEditor = paramJEditorPane;
/*      */   }
/*      */ 
/*      */   public void deinstall(JEditorPane paramJEditorPane)
/*      */   {
/*  350 */     paramJEditorPane.removeMouseListener(this.linkHandler);
/*  351 */     paramJEditorPane.removeMouseMotionListener(this.linkHandler);
/*  352 */     paramJEditorPane.removeCaretListener(nextLinkAction);
/*  353 */     super.deinstall(paramJEditorPane);
/*  354 */     this.theEditor = null;
/*      */   }
/*      */ 
/*      */   public void setStyleSheet(StyleSheet paramStyleSheet)
/*      */   {
/*  374 */     if (paramStyleSheet == null)
/*  375 */       AppContext.getAppContext().remove(DEFAULT_STYLES_KEY);
/*      */     else
/*  377 */       AppContext.getAppContext().put(DEFAULT_STYLES_KEY, paramStyleSheet);
/*      */   }
/*      */ 
/*      */   public StyleSheet getStyleSheet()
/*      */   {
/*  388 */     AppContext localAppContext = AppContext.getAppContext();
/*  389 */     StyleSheet localStyleSheet = (StyleSheet)localAppContext.get(DEFAULT_STYLES_KEY);
/*      */ 
/*  391 */     if (localStyleSheet == null) {
/*  392 */       localStyleSheet = new StyleSheet();
/*  393 */       localAppContext.put(DEFAULT_STYLES_KEY, localStyleSheet);
/*      */       try {
/*  395 */         InputStream localInputStream = getResourceAsStream("default.css");
/*  396 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "ISO-8859-1"));
/*      */ 
/*  398 */         localStyleSheet.loadRules(localBufferedReader, null);
/*  399 */         localBufferedReader.close();
/*      */       }
/*      */       catch (Throwable localThrowable)
/*      */       {
/*      */       }
/*      */     }
/*  405 */     return localStyleSheet;
/*      */   }
/*      */ 
/*      */   static InputStream getResourceAsStream(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  420 */       return ResourceLoader.getResourceAsStream(paramString);
/*      */     }
/*      */     catch (Throwable localThrowable) {
/*      */     }
/*  424 */     return HTMLEditorKit.class.getResourceAsStream(paramString);
/*      */   }
/*      */ 
/*      */   public Action[] getActions()
/*      */   {
/*  437 */     return TextAction.augmentList(super.getActions(), defaultActions);
/*      */   }
/*      */ 
/*      */   protected void createInputAttributes(Element paramElement, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/*  451 */     paramMutableAttributeSet.removeAttributes(paramMutableAttributeSet);
/*  452 */     paramMutableAttributeSet.addAttributes(paramElement.getAttributes());
/*  453 */     paramMutableAttributeSet.removeAttribute(StyleConstants.ComposedTextAttribute);
/*      */ 
/*  455 */     Object localObject = paramMutableAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*  456 */     if ((localObject instanceof HTML.Tag)) {
/*  457 */       HTML.Tag localTag = (HTML.Tag)localObject;
/*      */ 
/*  460 */       if (localTag == HTML.Tag.IMG)
/*      */       {
/*  462 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.SRC);
/*  463 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.HEIGHT);
/*  464 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.WIDTH);
/*  465 */         paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */       }
/*  468 */       else if ((localTag == HTML.Tag.HR) || (localTag == HTML.Tag.BR))
/*      */       {
/*  470 */         paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */       }
/*  473 */       else if (localTag == HTML.Tag.COMMENT)
/*      */       {
/*  475 */         paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/*  477 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.COMMENT);
/*      */       }
/*  479 */       else if (localTag == HTML.Tag.INPUT)
/*      */       {
/*  481 */         paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/*  483 */         paramMutableAttributeSet.removeAttribute(HTML.Tag.INPUT);
/*      */       }
/*  485 */       else if ((localTag instanceof HTML.UnknownTag))
/*      */       {
/*  487 */         paramMutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
/*      */ 
/*  489 */         paramMutableAttributeSet.removeAttribute(HTML.Attribute.ENDTAG);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public MutableAttributeSet getInputAttributes()
/*      */   {
/*  501 */     if (this.input == null) {
/*  502 */       this.input = getStyleSheet().addStyle(null, null);
/*      */     }
/*  504 */     return this.input;
/*      */   }
/*      */ 
/*      */   public void setDefaultCursor(Cursor paramCursor)
/*      */   {
/*  513 */     this.defaultCursor = paramCursor;
/*      */   }
/*      */ 
/*      */   public Cursor getDefaultCursor()
/*      */   {
/*  522 */     return this.defaultCursor;
/*      */   }
/*      */ 
/*      */   public void setLinkCursor(Cursor paramCursor)
/*      */   {
/*  531 */     this.linkCursor = paramCursor;
/*      */   }
/*      */ 
/*      */   public Cursor getLinkCursor()
/*      */   {
/*  539 */     return this.linkCursor;
/*      */   }
/*      */ 
/*      */   public boolean isAutoFormSubmission()
/*      */   {
/*  553 */     return this.isAutoFormSubmission;
/*      */   }
/*      */ 
/*      */   public void setAutoFormSubmission(boolean paramBoolean)
/*      */   {
/*  566 */     this.isAutoFormSubmission = paramBoolean;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  575 */     HTMLEditorKit localHTMLEditorKit = (HTMLEditorKit)super.clone();
/*  576 */     if (localHTMLEditorKit != null) {
/*  577 */       localHTMLEditorKit.input = null;
/*  578 */       localHTMLEditorKit.linkHandler = new LinkController();
/*      */     }
/*  580 */     return localHTMLEditorKit;
/*      */   }
/*      */ 
/*      */   protected Parser getParser()
/*      */   {
/*  592 */     if (defaultParser == null)
/*      */       try {
/*  594 */         Class localClass = Class.forName("javax.swing.text.html.parser.ParserDelegator");
/*  595 */         defaultParser = (Parser)localClass.newInstance();
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*      */       }
/*  599 */     return defaultParser;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  612 */     if (this.theEditor == null) {
/*  613 */       return null;
/*      */     }
/*  615 */     if (this.accessibleContext == null) {
/*  616 */       AccessibleHTML localAccessibleHTML = new AccessibleHTML(this.theEditor);
/*  617 */       this.accessibleContext = localAccessibleHTML.getAccessibleContext();
/*      */     }
/*  619 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   private static Object getAttrValue(AttributeSet paramAttributeSet, HTML.Attribute paramAttribute)
/*      */   {
/* 1859 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1860 */     while (localEnumeration.hasMoreElements()) {
/* 1861 */       Object localObject1 = localEnumeration.nextElement();
/* 1862 */       Object localObject2 = paramAttributeSet.getAttribute(localObject1);
/* 1863 */       if ((localObject2 instanceof AttributeSet)) {
/* 1864 */         Object localObject3 = getAttrValue((AttributeSet)localObject2, paramAttribute);
/* 1865 */         if (localObject3 != null)
/* 1866 */           return localObject3;
/*      */       }
/* 1868 */       else if (localObject1 == paramAttribute) {
/* 1869 */         return localObject2;
/*      */       }
/*      */     }
/* 1872 */     return null;
/*      */   }
/*      */ 
/*      */   private static int getBodyElementStart(JTextComponent paramJTextComponent)
/*      */   {
/* 2248 */     Element localElement1 = paramJTextComponent.getDocument().getRootElements()[0];
/* 2249 */     for (int i = 0; i < localElement1.getElementCount(); i++) {
/* 2250 */       Element localElement2 = localElement1.getElement(i);
/* 2251 */       if ("body".equals(localElement2.getName())) {
/* 2252 */         return localElement2.getStartOffset();
/*      */       }
/*      */     }
/* 2255 */     return 0;
/*      */   }
/*      */ 
/*      */   static class ActivateLinkAction extends TextAction
/*      */   {
/*      */     public ActivateLinkAction(String paramString)
/*      */     {
/* 2096 */       super();
/*      */     }
/*      */ 
/*      */     private void activateLink(String paramString, HTMLDocument paramHTMLDocument, JEditorPane paramJEditorPane, int paramInt)
/*      */     {
/*      */       try
/*      */       {
/* 2105 */         URL localURL1 = (URL)paramHTMLDocument.getProperty("stream");
/*      */ 
/* 2107 */         URL localURL2 = new URL(localURL1, paramString);
/* 2108 */         HyperlinkEvent localHyperlinkEvent = new HyperlinkEvent(paramJEditorPane, HyperlinkEvent.EventType.ACTIVATED, localURL2, localURL2.toExternalForm(), paramHTMLDocument.getCharacterElement(paramInt));
/*      */ 
/* 2112 */         paramJEditorPane.fireHyperlinkUpdate(localHyperlinkEvent);
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     private void doObjectAction(JEditorPane paramJEditorPane, Element paramElement)
/*      */     {
/* 2121 */       View localView = getView(paramJEditorPane, paramElement);
/* 2122 */       if ((localView != null) && ((localView instanceof ObjectView))) {
/* 2123 */         Component localComponent = ((ObjectView)localView).getComponent();
/* 2124 */         if ((localComponent != null) && ((localComponent instanceof Accessible))) {
/* 2125 */           AccessibleContext localAccessibleContext = localComponent.getAccessibleContext();
/* 2126 */           if (localAccessibleContext != null) {
/* 2127 */             AccessibleAction localAccessibleAction = localAccessibleContext.getAccessibleAction();
/* 2128 */             if (localAccessibleAction != null)
/* 2129 */               localAccessibleAction.doAccessibleAction(0);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private View getRootView(JEditorPane paramJEditorPane)
/*      */     {
/* 2140 */       return paramJEditorPane.getUI().getRootView(paramJEditorPane);
/*      */     }
/*      */ 
/*      */     private View getView(JEditorPane paramJEditorPane, Element paramElement)
/*      */     {
/* 2147 */       Object localObject1 = lock(paramJEditorPane);
/*      */       try {
/* 2149 */         View localView1 = getRootView(paramJEditorPane);
/* 2150 */         int i = paramElement.getStartOffset();
/*      */         View localView2;
/* 2151 */         if (localView1 != null) {
/* 2152 */           return getView(localView1, paramElement, i);
/*      */         }
/* 2154 */         return null;
/*      */       } finally {
/* 2156 */         unlock(localObject1);
/*      */       }
/*      */     }
/*      */ 
/*      */     private View getView(View paramView, Element paramElement, int paramInt) {
/* 2161 */       if (paramView.getElement() == paramElement) {
/* 2162 */         return paramView;
/*      */       }
/* 2164 */       int i = paramView.getViewIndex(paramInt, Position.Bias.Forward);
/*      */ 
/* 2166 */       if ((i != -1) && (i < paramView.getViewCount())) {
/* 2167 */         return getView(paramView.getView(i), paramElement, paramInt);
/*      */       }
/* 2169 */       return null;
/*      */     }
/*      */ 
/*      */     private Object lock(JEditorPane paramJEditorPane)
/*      */     {
/* 2178 */       Document localDocument = paramJEditorPane.getDocument();
/*      */ 
/* 2180 */       if ((localDocument instanceof AbstractDocument)) {
/* 2181 */         ((AbstractDocument)localDocument).readLock();
/* 2182 */         return localDocument;
/*      */       }
/* 2184 */       return null;
/*      */     }
/*      */ 
/*      */     private void unlock(Object paramObject)
/*      */     {
/* 2191 */       if (paramObject != null)
/* 2192 */         ((AbstractDocument)paramObject).readUnlock();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2201 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2202 */       if ((localJTextComponent.isEditable()) || (!(localJTextComponent instanceof JEditorPane))) {
/* 2203 */         return;
/*      */       }
/* 2205 */       JEditorPane localJEditorPane = (JEditorPane)localJTextComponent;
/*      */ 
/* 2207 */       Document localDocument = localJEditorPane.getDocument();
/* 2208 */       if ((localDocument == null) || (!(localDocument instanceof HTMLDocument))) {
/* 2209 */         return;
/*      */       }
/* 2211 */       HTMLDocument localHTMLDocument = (HTMLDocument)localDocument;
/*      */ 
/* 2213 */       ElementIterator localElementIterator = new ElementIterator(localHTMLDocument);
/* 2214 */       int i = localJEditorPane.getCaretPosition();
/*      */ 
/* 2217 */       Object localObject1 = null;
/* 2218 */       Object localObject2 = null;
/*      */       Element localElement;
/* 2220 */       while ((localElement = localElementIterator.next()) != null) {
/* 2221 */         String str = localElement.getName();
/* 2222 */         AttributeSet localAttributeSet = localElement.getAttributes();
/*      */ 
/* 2224 */         Object localObject3 = HTMLEditorKit.getAttrValue(localAttributeSet, HTML.Attribute.HREF);
/* 2225 */         if (localObject3 != null) {
/* 2226 */           if ((i >= localElement.getStartOffset()) && (i <= localElement.getEndOffset()))
/*      */           {
/* 2229 */             activateLink((String)localObject3, localHTMLDocument, localJEditorPane, i);
/*      */           }
/*      */         }
/* 2232 */         else if (str.equals(HTML.Tag.OBJECT.toString())) {
/* 2233 */           Object localObject4 = HTMLEditorKit.getAttrValue(localAttributeSet, HTML.Attribute.CLASSID);
/* 2234 */           if ((localObject4 != null) && 
/* 2235 */             (i >= localElement.getStartOffset()) && (i <= localElement.getEndOffset()))
/*      */           {
/* 2238 */             doObjectAction(localJEditorPane, localElement);
/* 2239 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BeginAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     BeginAction(String paramString, boolean paramBoolean)
/*      */     {
/* 2268 */       super();
/* 2269 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2274 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2275 */       int i = HTMLEditorKit.getBodyElementStart(localJTextComponent);
/*      */ 
/* 2277 */       if (localJTextComponent != null)
/* 2278 */         if (this.select)
/* 2279 */           localJTextComponent.moveCaretPosition(i);
/*      */         else
/* 2281 */           localJTextComponent.setCaretPosition(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class HTMLFactory
/*      */     implements ViewFactory
/*      */   {
/*      */     public View create(Element paramElement)
/*      */     {
/* 1125 */       AttributeSet localAttributeSet = paramElement.getAttributes();
/* 1126 */       Object localObject1 = localAttributeSet.getAttribute("$ename");
/*      */ 
/* 1128 */       Object localObject2 = localObject1 != null ? null : localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/* 1130 */       if ((localObject2 instanceof HTML.Tag)) {
/* 1131 */         localObject3 = (HTML.Tag)localObject2;
/* 1132 */         if (localObject3 == HTML.Tag.CONTENT)
/* 1133 */           return new InlineView(paramElement);
/* 1134 */         if (localObject3 == HTML.Tag.IMPLIED) {
/* 1135 */           String str = (String)paramElement.getAttributes().getAttribute(CSS.Attribute.WHITE_SPACE);
/*      */ 
/* 1137 */           if ((str != null) && (str.equals("pre"))) {
/* 1138 */             return new LineView(paramElement);
/*      */           }
/* 1140 */           return new ParagraphView(paramElement);
/* 1141 */         }if ((localObject3 == HTML.Tag.P) || (localObject3 == HTML.Tag.H1) || (localObject3 == HTML.Tag.H2) || (localObject3 == HTML.Tag.H3) || (localObject3 == HTML.Tag.H4) || (localObject3 == HTML.Tag.H5) || (localObject3 == HTML.Tag.H6) || (localObject3 == HTML.Tag.DT))
/*      */         {
/* 1150 */           return new ParagraphView(paramElement);
/* 1151 */         }if ((localObject3 == HTML.Tag.MENU) || (localObject3 == HTML.Tag.DIR) || (localObject3 == HTML.Tag.UL) || (localObject3 == HTML.Tag.OL))
/*      */         {
/* 1155 */           return new ListView(paramElement);
/* 1156 */         }if (localObject3 == HTML.Tag.BODY)
/* 1157 */           return new BodyBlockView(paramElement);
/* 1158 */         if (localObject3 == HTML.Tag.HTML)
/* 1159 */           return new BlockView(paramElement, 1);
/* 1160 */         if ((localObject3 == HTML.Tag.LI) || (localObject3 == HTML.Tag.CENTER) || (localObject3 == HTML.Tag.DL) || (localObject3 == HTML.Tag.DD) || (localObject3 == HTML.Tag.DIV) || (localObject3 == HTML.Tag.BLOCKQUOTE) || (localObject3 == HTML.Tag.PRE) || (localObject3 == HTML.Tag.FORM))
/*      */         {
/* 1169 */           return new BlockView(paramElement, 1);
/* 1170 */         }if (localObject3 == HTML.Tag.NOFRAMES)
/* 1171 */           return new NoFramesView(paramElement, 1);
/* 1172 */         if (localObject3 == HTML.Tag.IMG)
/* 1173 */           return new ImageView(paramElement);
/* 1174 */         if (localObject3 == HTML.Tag.ISINDEX)
/* 1175 */           return new IsindexView(paramElement);
/* 1176 */         if (localObject3 == HTML.Tag.HR)
/* 1177 */           return new HRuleView(paramElement);
/* 1178 */         if (localObject3 == HTML.Tag.BR)
/* 1179 */           return new BRView(paramElement);
/* 1180 */         if (localObject3 == HTML.Tag.TABLE)
/* 1181 */           return new TableView(paramElement);
/* 1182 */         if ((localObject3 == HTML.Tag.INPUT) || (localObject3 == HTML.Tag.SELECT) || (localObject3 == HTML.Tag.TEXTAREA))
/*      */         {
/* 1185 */           return new FormView(paramElement);
/* 1186 */         }if (localObject3 == HTML.Tag.OBJECT)
/* 1187 */           return new ObjectView(paramElement);
/* 1188 */         if (localObject3 == HTML.Tag.FRAMESET) {
/* 1189 */           if (paramElement.getAttributes().isDefined(HTML.Attribute.ROWS))
/* 1190 */             return new FrameSetView(paramElement, 1);
/* 1191 */           if (paramElement.getAttributes().isDefined(HTML.Attribute.COLS)) {
/* 1192 */             return new FrameSetView(paramElement, 0);
/*      */           }
/* 1194 */           throw new RuntimeException("Can't build a" + localObject3 + ", " + paramElement + ":" + "no ROWS or COLS defined.");
/*      */         }
/* 1196 */         if (localObject3 == HTML.Tag.FRAME)
/* 1197 */           return new FrameView(paramElement);
/* 1198 */         if ((localObject3 instanceof HTML.UnknownTag))
/* 1199 */           return new HiddenTagView(paramElement);
/* 1200 */         if (localObject3 == HTML.Tag.COMMENT)
/* 1201 */           return new CommentView(paramElement);
/* 1202 */         if (localObject3 == HTML.Tag.HEAD)
/*      */         {
/* 1207 */           return new BlockView(paramElement, 0) {
/*      */             public float getPreferredSpan(int paramAnonymousInt) {
/* 1209 */               return 0.0F;
/*      */             }
/*      */             public float getMinimumSpan(int paramAnonymousInt) {
/* 1212 */               return 0.0F;
/*      */             }
/*      */             public float getMaximumSpan(int paramAnonymousInt) {
/* 1215 */               return 0.0F;
/*      */             }
/*      */             protected void loadChildren(ViewFactory paramAnonymousViewFactory) {
/*      */             }
/*      */ 
/*      */             public Shape modelToView(int paramAnonymousInt, Shape paramAnonymousShape, Position.Bias paramAnonymousBias) throws BadLocationException {
/* 1221 */               return paramAnonymousShape;
/*      */             }
/*      */ 
/*      */             public int getNextVisualPositionFrom(int paramAnonymousInt1, Position.Bias paramAnonymousBias, Shape paramAnonymousShape, int paramAnonymousInt2, Position.Bias[] paramAnonymousArrayOfBias)
/*      */             {
/* 1226 */               return getElement().getEndOffset();
/*      */             } } ;
/*      */         }
/* 1229 */         if ((localObject3 == HTML.Tag.TITLE) || (localObject3 == HTML.Tag.META) || (localObject3 == HTML.Tag.LINK) || (localObject3 == HTML.Tag.STYLE) || (localObject3 == HTML.Tag.SCRIPT) || (localObject3 == HTML.Tag.AREA) || (localObject3 == HTML.Tag.MAP) || (localObject3 == HTML.Tag.PARAM) || (localObject3 == HTML.Tag.APPLET))
/*      */         {
/* 1238 */           return new HiddenTagView(paramElement);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1243 */       Object localObject3 = localObject1 != null ? (String)localObject1 : paramElement.getName();
/*      */ 
/* 1245 */       if (localObject3 != null) {
/* 1246 */         if (((String)localObject3).equals("content"))
/* 1247 */           return new LabelView(paramElement);
/* 1248 */         if (((String)localObject3).equals("paragraph"))
/* 1249 */           return new ParagraphView(paramElement);
/* 1250 */         if (((String)localObject3).equals("section"))
/* 1251 */           return new BoxView(paramElement, 1);
/* 1252 */         if (((String)localObject3).equals("component"))
/* 1253 */           return new ComponentView(paramElement);
/* 1254 */         if (((String)localObject3).equals("icon")) {
/* 1255 */           return new IconView(paramElement);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1260 */       return new LabelView(paramElement);
/*      */     }
/*      */ 
/*      */     static class BodyBlockView extends BlockView
/*      */       implements ComponentListener
/*      */     {
/* 1364 */       private Reference<JViewport> cachedViewPort = null;
/* 1365 */       private boolean isListening = false;
/* 1366 */       private int viewVisibleWidth = 2147483647;
/* 1367 */       private int componentVisibleWidth = 2147483647;
/*      */ 
/*      */       public BodyBlockView(Element paramElement)
/*      */       {
/* 1265 */         super(1);
/*      */       }
/*      */ 
/*      */       protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */       {
/* 1271 */         paramSizeRequirements = super.calculateMajorAxisRequirements(paramInt, paramSizeRequirements);
/* 1272 */         paramSizeRequirements.maximum = 2147483647;
/* 1273 */         return paramSizeRequirements;
/*      */       }
/*      */ 
/*      */       protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
/* 1277 */         Container localContainer1 = getContainer();
/*      */         Container localContainer2;
/*      */         JViewport localJViewport;
/* 1279 */         if ((localContainer1 != null) && ((localContainer1 instanceof JEditorPane)) && ((localContainer2 = localContainer1.getParent()) != null) && ((localContainer2 instanceof JViewport)))
/*      */         {
/* 1283 */           localJViewport = (JViewport)localContainer2;
/*      */           Object localObject;
/* 1284 */           if (this.cachedViewPort != null) {
/* 1285 */             localObject = (JViewport)this.cachedViewPort.get();
/* 1286 */             if (localObject != null) {
/* 1287 */               if (localObject != localJViewport)
/* 1288 */                 ((JViewport)localObject).removeComponentListener(this);
/*      */             }
/*      */             else {
/* 1291 */               this.cachedViewPort = null;
/*      */             }
/*      */           }
/* 1294 */           if (this.cachedViewPort == null) {
/* 1295 */             localJViewport.addComponentListener(this);
/* 1296 */             this.cachedViewPort = new WeakReference(localJViewport);
/*      */           }
/*      */ 
/* 1299 */           this.componentVisibleWidth = localJViewport.getExtentSize().width;
/* 1300 */           if (this.componentVisibleWidth > 0) {
/* 1301 */             localObject = localContainer1.getInsets();
/* 1302 */             this.viewVisibleWidth = (this.componentVisibleWidth - ((Insets)localObject).left - getLeftInset());
/*      */ 
/* 1304 */             paramInt1 = Math.min(paramInt1, this.viewVisibleWidth);
/*      */           }
/*      */         }
/* 1307 */         else if (this.cachedViewPort != null) {
/* 1308 */           localJViewport = (JViewport)this.cachedViewPort.get();
/* 1309 */           if (localJViewport != null) {
/* 1310 */             localJViewport.removeComponentListener(this);
/*      */           }
/* 1312 */           this.cachedViewPort = null;
/*      */         }
/*      */ 
/* 1315 */         super.layoutMinorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*      */       }
/*      */ 
/*      */       public void setParent(View paramView)
/*      */       {
/* 1320 */         if ((paramView == null) && 
/* 1321 */           (this.cachedViewPort != null))
/*      */         {
/*      */           Object localObject;
/* 1323 */           if ((localObject = this.cachedViewPort.get()) != null) {
/* 1324 */             ((JComponent)localObject).removeComponentListener(this);
/*      */           }
/* 1326 */           this.cachedViewPort = null;
/*      */         }
/*      */ 
/* 1329 */         super.setParent(paramView);
/*      */       }
/*      */ 
/*      */       public void componentResized(ComponentEvent paramComponentEvent) {
/* 1333 */         if (!(paramComponentEvent.getSource() instanceof JViewport)) {
/* 1334 */           return;
/*      */         }
/* 1336 */         JViewport localJViewport = (JViewport)paramComponentEvent.getSource();
/* 1337 */         if (this.componentVisibleWidth != localJViewport.getExtentSize().width) {
/* 1338 */           Document localDocument = getDocument();
/* 1339 */           if ((localDocument instanceof AbstractDocument)) {
/* 1340 */             AbstractDocument localAbstractDocument = (AbstractDocument)getDocument();
/* 1341 */             localAbstractDocument.readLock();
/*      */             try {
/* 1343 */               layoutChanged(0);
/* 1344 */               preferenceChanged(null, true, true);
/*      */             } finally {
/* 1346 */               localAbstractDocument.readUnlock();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public void componentHidden(ComponentEvent paramComponentEvent)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void componentMoved(ComponentEvent paramComponentEvent)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void componentShown(ComponentEvent paramComponentEvent)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class HTMLTextAction extends StyledEditorKit.StyledTextAction
/*      */   {
/*      */     public HTMLTextAction(String paramString)
/*      */     {
/* 1488 */       super();
/*      */     }
/*      */ 
/*      */     protected HTMLDocument getHTMLDocument(JEditorPane paramJEditorPane)
/*      */     {
/* 1495 */       Document localDocument = paramJEditorPane.getDocument();
/* 1496 */       if ((localDocument instanceof HTMLDocument)) {
/* 1497 */         return (HTMLDocument)localDocument;
/*      */       }
/* 1499 */       throw new IllegalArgumentException("document must be HTMLDocument");
/*      */     }
/*      */ 
/*      */     protected HTMLEditorKit getHTMLEditorKit(JEditorPane paramJEditorPane)
/*      */     {
/* 1506 */       EditorKit localEditorKit = paramJEditorPane.getEditorKit();
/* 1507 */       if ((localEditorKit instanceof HTMLEditorKit)) {
/* 1508 */         return (HTMLEditorKit)localEditorKit;
/*      */       }
/* 1510 */       throw new IllegalArgumentException("EditorKit must be HTMLEditorKit");
/*      */     }
/*      */ 
/*      */     protected Element[] getElementsAt(HTMLDocument paramHTMLDocument, int paramInt)
/*      */     {
/* 1518 */       return getElementsAt(paramHTMLDocument.getDefaultRootElement(), paramInt, 0);
/*      */     }
/*      */ 
/*      */     private Element[] getElementsAt(Element paramElement, int paramInt1, int paramInt2)
/*      */     {
/* 1526 */       if (paramElement.isLeaf()) {
/* 1527 */         arrayOfElement = new Element[paramInt2 + 1];
/* 1528 */         arrayOfElement[paramInt2] = paramElement;
/* 1529 */         return arrayOfElement;
/*      */       }
/* 1531 */       Element[] arrayOfElement = getElementsAt(paramElement.getElement(paramElement.getElementIndex(paramInt1)), paramInt1, paramInt2 + 1);
/*      */ 
/* 1533 */       arrayOfElement[paramInt2] = paramElement;
/* 1534 */       return arrayOfElement;
/*      */     }
/*      */ 
/*      */     protected int elementCountToTag(HTMLDocument paramHTMLDocument, int paramInt, HTML.Tag paramTag)
/*      */     {
/* 1546 */       int i = -1;
/* 1547 */       Element localElement = paramHTMLDocument.getCharacterElement(paramInt);
/* 1548 */       while ((localElement != null) && (localElement.getAttributes().getAttribute(StyleConstants.NameAttribute) != paramTag))
/*      */       {
/* 1550 */         localElement = localElement.getParentElement();
/* 1551 */         i++;
/*      */       }
/* 1553 */       if (localElement == null) {
/* 1554 */         return -1;
/*      */       }
/* 1556 */       return i;
/*      */     }
/*      */ 
/*      */     protected Element findElementMatchingTag(HTMLDocument paramHTMLDocument, int paramInt, HTML.Tag paramTag)
/*      */     {
/* 1565 */       Element localElement1 = paramHTMLDocument.getDefaultRootElement();
/* 1566 */       Element localElement2 = null;
/* 1567 */       while (localElement1 != null) {
/* 1568 */         if (localElement1.getAttributes().getAttribute(StyleConstants.NameAttribute) == paramTag)
/*      */         {
/* 1570 */           localElement2 = localElement1;
/*      */         }
/* 1572 */         localElement1 = localElement1.getElement(localElement1.getElementIndex(paramInt));
/*      */       }
/* 1574 */       return localElement2;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class InsertHRAction extends HTMLEditorKit.InsertHTMLTextAction
/*      */   {
/*      */     InsertHRAction()
/*      */     {
/* 1829 */       super("<hr>", null, HTML.Tag.IMPLIED, null, null, false);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1839 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 1840 */       if (localJEditorPane != null) {
/* 1841 */         HTMLDocument localHTMLDocument = getHTMLDocument(localJEditorPane);
/* 1842 */         int i = localJEditorPane.getSelectionStart();
/* 1843 */         Element localElement = localHTMLDocument.getParagraphElement(i);
/* 1844 */         if (localElement.getParentElement() != null) {
/* 1845 */           this.parentTag = ((HTML.Tag)localElement.getParentElement().getAttributes().getAttribute(StyleConstants.NameAttribute));
/*      */ 
/* 1848 */           super.actionPerformed(paramActionEvent);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InsertHTMLTextAction extends HTMLEditorKit.HTMLTextAction
/*      */   {
/*      */     protected String html;
/*      */     protected HTML.Tag parentTag;
/*      */     protected HTML.Tag addTag;
/*      */     protected HTML.Tag alternateParentTag;
/*      */     protected HTML.Tag alternateAddTag;
/*      */     boolean adjustSelection;
/*      */ 
/*      */     public InsertHTMLTextAction(String paramString1, String paramString2, HTML.Tag paramTag1, HTML.Tag paramTag2)
/*      */     {
/* 1599 */       this(paramString1, paramString2, paramTag1, paramTag2, null, null);
/*      */     }
/*      */ 
/*      */     public InsertHTMLTextAction(String paramString1, String paramString2, HTML.Tag paramTag1, HTML.Tag paramTag2, HTML.Tag paramTag3, HTML.Tag paramTag4)
/*      */     {
/* 1607 */       this(paramString1, paramString2, paramTag1, paramTag2, paramTag3, paramTag4, true);
/*      */     }
/*      */ 
/*      */     InsertHTMLTextAction(String paramString1, String paramString2, HTML.Tag paramTag1, HTML.Tag paramTag2, HTML.Tag paramTag3, HTML.Tag paramTag4, boolean paramBoolean)
/*      */     {
/* 1618 */       super();
/* 1619 */       this.html = paramString2;
/* 1620 */       this.parentTag = paramTag1;
/* 1621 */       this.addTag = paramTag2;
/* 1622 */       this.alternateParentTag = paramTag3;
/* 1623 */       this.alternateAddTag = paramTag4;
/* 1624 */       this.adjustSelection = paramBoolean;
/*      */     }
/*      */ 
/*      */     protected void insertHTML(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, int paramInt1, String paramString, int paramInt2, int paramInt3, HTML.Tag paramTag)
/*      */     {
/*      */       try
/*      */       {
/* 1635 */         getHTMLEditorKit(paramJEditorPane).insertHTML(paramHTMLDocument, paramInt1, paramString, paramInt2, paramInt3, paramTag);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1639 */         throw new RuntimeException("Unable to insert: " + localIOException);
/*      */       } catch (BadLocationException localBadLocationException) {
/* 1641 */         throw new RuntimeException("Unable to insert: " + localBadLocationException);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void insertAtBoundary(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, int paramInt, Element paramElement, String paramString, HTML.Tag paramTag1, HTML.Tag paramTag2)
/*      */     {
/* 1655 */       insertAtBoundry(paramJEditorPane, paramHTMLDocument, paramInt, paramElement, paramString, paramTag1, paramTag2);
/*      */     }
/*      */ 
/*      */     @Deprecated
/*      */     protected void insertAtBoundry(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, int paramInt, Element paramElement, String paramString, HTML.Tag paramTag1, HTML.Tag paramTag2)
/*      */     {
/* 1673 */       int i = paramInt == 0 ? 1 : 0;
/*      */       Object localObject;
/*      */       Element localElement;
/* 1675 */       if ((paramInt > 0) || (paramElement == null)) {
/* 1676 */         localObject = paramHTMLDocument.getDefaultRootElement();
/* 1677 */         while ((localObject != null) && (((Element)localObject).getStartOffset() != paramInt) && (!((Element)localObject).isLeaf()))
/*      */         {
/* 1679 */           localObject = ((Element)localObject).getElement(((Element)localObject).getElementIndex(paramInt));
/*      */         }
/* 1681 */         localElement = localObject != null ? ((Element)localObject).getParentElement() : null;
/*      */       }
/*      */       else
/*      */       {
/* 1686 */         localElement = paramElement;
/*      */       }
/* 1688 */       if (localElement != null)
/*      */       {
/* 1690 */         int j = 0;
/* 1691 */         int k = 0;
/* 1692 */         if ((i != 0) && (paramElement != null))
/* 1693 */           localObject = localElement;
/* 1694 */         while ((localObject != null) && (!((Element)localObject).isLeaf())) {
/* 1695 */           localObject = ((Element)localObject).getElement(((Element)localObject).getElementIndex(paramInt));
/* 1696 */           j++; continue;
/*      */ 
/* 1700 */           localObject = localElement;
/* 1701 */           paramInt--;
/* 1702 */           while ((localObject != null) && (!((Element)localObject).isLeaf())) {
/* 1703 */             localObject = ((Element)localObject).getElement(((Element)localObject).getElementIndex(paramInt));
/* 1704 */             j++;
/*      */           }
/*      */ 
/* 1708 */           localObject = localElement;
/* 1709 */           paramInt++;
/* 1710 */           while ((localObject != null) && (localObject != paramElement)) {
/* 1711 */             localObject = ((Element)localObject).getElement(((Element)localObject).getElementIndex(paramInt));
/* 1712 */             k++;
/*      */           }
/*      */         }
/* 1715 */         j = Math.max(0, j - 1);
/*      */ 
/* 1718 */         insertHTML(paramJEditorPane, paramHTMLDocument, paramInt, paramString, j, k, paramTag2);
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean insertIntoTag(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, int paramInt, HTML.Tag paramTag1, HTML.Tag paramTag2)
/*      */     {
/* 1731 */       Element localElement = findElementMatchingTag(paramHTMLDocument, paramInt, paramTag1);
/* 1732 */       if ((localElement != null) && (localElement.getStartOffset() == paramInt)) {
/* 1733 */         insertAtBoundary(paramJEditorPane, paramHTMLDocument, paramInt, localElement, this.html, paramTag1, paramTag2);
/*      */ 
/* 1735 */         return true;
/*      */       }
/* 1737 */       if (paramInt > 0) {
/* 1738 */         int i = elementCountToTag(paramHTMLDocument, paramInt - 1, paramTag1);
/* 1739 */         if (i != -1) {
/* 1740 */           insertHTML(paramJEditorPane, paramHTMLDocument, paramInt, this.html, i, 0, paramTag2);
/* 1741 */           return true;
/*      */         }
/*      */       }
/* 1744 */       return false;
/*      */     }
/*      */ 
/*      */     void adjustSelection(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, int paramInt1, int paramInt2)
/*      */     {
/* 1753 */       int i = paramHTMLDocument.getLength();
/* 1754 */       if ((i != paramInt2) && (paramInt1 < i))
/* 1755 */         if (paramInt1 > 0) {
/*      */           String str;
/*      */           try {
/* 1758 */             str = paramHTMLDocument.getText(paramInt1 - 1, 1);
/*      */           } catch (BadLocationException localBadLocationException) {
/* 1760 */             str = null;
/*      */           }
/* 1762 */           if ((str != null) && (str.length() > 0) && (str.charAt(0) == '\n'))
/*      */           {
/* 1764 */             paramJEditorPane.select(paramInt1, paramInt1);
/*      */           }
/*      */           else
/* 1767 */             paramJEditorPane.select(paramInt1 + 1, paramInt1 + 1);
/*      */         }
/*      */         else
/*      */         {
/* 1771 */           paramJEditorPane.select(1, 1);
/*      */         }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1782 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 1783 */       if (localJEditorPane != null) {
/* 1784 */         HTMLDocument localHTMLDocument = getHTMLDocument(localJEditorPane);
/* 1785 */         int i = localJEditorPane.getSelectionStart();
/* 1786 */         int j = localHTMLDocument.getLength();
/*      */         boolean bool;
/* 1789 */         if ((!insertIntoTag(localJEditorPane, localHTMLDocument, i, this.parentTag, this.addTag)) && (this.alternateParentTag != null))
/*      */         {
/* 1792 */           bool = insertIntoTag(localJEditorPane, localHTMLDocument, i, this.alternateParentTag, this.alternateAddTag);
/*      */         }
/*      */         else
/*      */         {
/* 1797 */           bool = true;
/*      */         }
/* 1799 */         if ((this.adjustSelection) && (bool))
/* 1800 */           adjustSelection(localJEditorPane, localHTMLDocument, i, j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class LinkController extends MouseAdapter
/*      */     implements MouseMotionListener, Serializable
/*      */   {
/*  645 */     private Element curElem = null;
/*      */ 
/*  649 */     private boolean curElemImage = false;
/*  650 */     private String href = null;
/*      */ 
/*  653 */     private transient Position.Bias[] bias = new Position.Bias[1];
/*      */     private int curOffset;
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*  669 */       JEditorPane localJEditorPane = (JEditorPane)paramMouseEvent.getSource();
/*      */ 
/*  671 */       if ((!localJEditorPane.isEditable()) && (localJEditorPane.isEnabled()) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)))
/*      */       {
/*  673 */         Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/*  674 */         int i = localJEditorPane.viewToModel(localPoint);
/*  675 */         if (i >= 0)
/*  676 */           activateLink(i, localJEditorPane, paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/*  687 */       JEditorPane localJEditorPane = (JEditorPane)paramMouseEvent.getSource();
/*  688 */       if (!localJEditorPane.isEnabled()) {
/*  689 */         return;
/*      */       }
/*      */ 
/*  692 */       HTMLEditorKit localHTMLEditorKit = (HTMLEditorKit)localJEditorPane.getEditorKit();
/*  693 */       int i = 1;
/*  694 */       Cursor localCursor = localHTMLEditorKit.getDefaultCursor();
/*  695 */       if (!localJEditorPane.isEditable()) {
/*  696 */         Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/*  697 */         int j = localJEditorPane.getUI().viewToModel(localJEditorPane, localPoint, this.bias);
/*  698 */         if ((this.bias[0] == Position.Bias.Backward) && (j > 0)) {
/*  699 */           j--;
/*      */         }
/*  701 */         if ((j >= 0) && ((localJEditorPane.getDocument() instanceof HTMLDocument))) {
/*  702 */           HTMLDocument localHTMLDocument = (HTMLDocument)localJEditorPane.getDocument();
/*  703 */           Element localElement1 = localHTMLDocument.getCharacterElement(j);
/*  704 */           if (!doesElementContainLocation(localJEditorPane, localElement1, j, paramMouseEvent.getX(), paramMouseEvent.getY()))
/*      */           {
/*  706 */             localElement1 = null;
/*      */           }
/*  708 */           if ((this.curElem != localElement1) || (this.curElemImage)) {
/*  709 */             Element localElement2 = this.curElem;
/*  710 */             this.curElem = localElement1;
/*  711 */             String str = null;
/*  712 */             this.curElemImage = false;
/*  713 */             if (localElement1 != null) {
/*  714 */               AttributeSet localAttributeSet1 = localElement1.getAttributes();
/*  715 */               AttributeSet localAttributeSet2 = (AttributeSet)localAttributeSet1.getAttribute(HTML.Tag.A);
/*      */ 
/*  717 */               if (localAttributeSet2 == null) {
/*  718 */                 this.curElemImage = (localAttributeSet1.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.IMG);
/*      */ 
/*  720 */                 if (this.curElemImage) {
/*  721 */                   str = getMapHREF(localJEditorPane, localHTMLDocument, localElement1, localAttributeSet1, j, paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/*  726 */                 str = (String)localAttributeSet2.getAttribute(HTML.Attribute.HREF);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  731 */             if (str != this.href)
/*      */             {
/*  733 */               fireEvents(localJEditorPane, localHTMLDocument, str, localElement2, paramMouseEvent);
/*  734 */               this.href = str;
/*  735 */               if (str != null)
/*  736 */                 localCursor = localHTMLEditorKit.getLinkCursor();
/*      */             }
/*      */             else
/*      */             {
/*  740 */               i = 0;
/*      */             }
/*      */           }
/*      */           else {
/*  744 */             i = 0;
/*      */           }
/*  746 */           this.curOffset = j;
/*      */         }
/*      */       }
/*  749 */       if ((i != 0) && (localJEditorPane.getCursor() != localCursor))
/*  750 */         localJEditorPane.setCursor(localCursor);
/*      */     }
/*      */ 
/*      */     private String getMapHREF(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, Element paramElement, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/*  761 */       Object localObject = paramAttributeSet.getAttribute(HTML.Attribute.USEMAP);
/*  762 */       if ((localObject != null) && ((localObject instanceof String))) {
/*  763 */         Map localMap = paramHTMLDocument.getMap((String)localObject);
/*  764 */         if ((localMap != null) && (paramInt1 < paramHTMLDocument.getLength())) {
/*  766 */           TextUI localTextUI = paramJEditorPane.getUI();
/*      */           Rectangle localRectangle1;
/*      */           try { Rectangle localRectangle2 = localTextUI.modelToView(paramJEditorPane, paramInt1, Position.Bias.Forward);
/*      */ 
/*  770 */             Rectangle localRectangle3 = localTextUI.modelToView(paramJEditorPane, paramInt1 + 1, Position.Bias.Backward);
/*      */ 
/*  772 */             localRectangle1 = localRectangle2.getBounds();
/*  773 */             localRectangle1.add((localRectangle3 instanceof Rectangle) ? (Rectangle)localRectangle3 : localRectangle3.getBounds());
/*      */           } catch (BadLocationException localBadLocationException)
/*      */           {
/*  776 */             localRectangle1 = null;
/*      */           }
/*  778 */           if (localRectangle1 != null) {
/*  779 */             AttributeSet localAttributeSet = localMap.getArea(paramInt2 - localRectangle1.x, paramInt3 - localRectangle1.y, localRectangle1.width, localRectangle1.height);
/*      */ 
/*  783 */             if (localAttributeSet != null) {
/*  784 */               return (String)localAttributeSet.getAttribute(HTML.Attribute.HREF);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  790 */       return null;
/*      */     }
/*      */ 
/*      */     private boolean doesElementContainLocation(JEditorPane paramJEditorPane, Element paramElement, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/*  801 */       if ((paramElement != null) && (paramInt1 > 0) && (paramElement.getStartOffset() == paramInt1))
/*      */         try {
/*  803 */           TextUI localTextUI = paramJEditorPane.getUI();
/*  804 */           Rectangle localRectangle1 = localTextUI.modelToView(paramJEditorPane, paramInt1, Position.Bias.Forward);
/*      */ 
/*  806 */           if (localRectangle1 == null) {
/*  807 */             return false;
/*      */           }
/*  809 */           Rectangle localRectangle2 = (localRectangle1 instanceof Rectangle) ? (Rectangle)localRectangle1 : localRectangle1.getBounds();
/*      */ 
/*  811 */           Rectangle localRectangle3 = localTextUI.modelToView(paramJEditorPane, paramElement.getEndOffset(), Position.Bias.Backward);
/*      */ 
/*  813 */           if (localRectangle3 != null) {
/*  814 */             Rectangle localRectangle4 = (localRectangle3 instanceof Rectangle) ? (Rectangle)localRectangle3 : localRectangle3.getBounds();
/*      */ 
/*  816 */             localRectangle2.add(localRectangle4);
/*      */           }
/*  818 */           return localRectangle2.contains(paramInt2, paramInt3);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/*  822 */       return true;
/*      */     }
/*      */ 
/*      */     protected void activateLink(int paramInt, JEditorPane paramJEditorPane)
/*      */     {
/*  835 */       activateLink(paramInt, paramJEditorPane, null);
/*      */     }
/*      */ 
/*      */     void activateLink(int paramInt, JEditorPane paramJEditorPane, MouseEvent paramMouseEvent)
/*      */     {
/*  849 */       Document localDocument = paramJEditorPane.getDocument();
/*  850 */       if ((localDocument instanceof HTMLDocument)) {
/*  851 */         HTMLDocument localHTMLDocument = (HTMLDocument)localDocument;
/*  852 */         Element localElement = localHTMLDocument.getCharacterElement(paramInt);
/*  853 */         AttributeSet localAttributeSet1 = localElement.getAttributes();
/*  854 */         AttributeSet localAttributeSet2 = (AttributeSet)localAttributeSet1.getAttribute(HTML.Tag.A);
/*  855 */         HyperlinkEvent localHyperlinkEvent = null;
/*      */ 
/*  857 */         int i = -1;
/*  858 */         int j = -1;
/*      */ 
/*  860 */         if (paramMouseEvent != null) {
/*  861 */           i = paramMouseEvent.getX();
/*  862 */           j = paramMouseEvent.getY();
/*      */         }
/*      */ 
/*  865 */         if (localAttributeSet2 == null) {
/*  866 */           this.href = getMapHREF(paramJEditorPane, localHTMLDocument, localElement, localAttributeSet1, paramInt, i, j);
/*      */         }
/*      */         else {
/*  869 */           this.href = ((String)localAttributeSet2.getAttribute(HTML.Attribute.HREF));
/*      */         }
/*      */ 
/*  872 */         if (this.href != null) {
/*  873 */           localHyperlinkEvent = createHyperlinkEvent(paramJEditorPane, localHTMLDocument, this.href, localAttributeSet2, localElement, paramMouseEvent);
/*      */         }
/*      */ 
/*  876 */         if (localHyperlinkEvent != null)
/*  877 */           paramJEditorPane.fireHyperlinkUpdate(localHyperlinkEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     HyperlinkEvent createHyperlinkEvent(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, String paramString, AttributeSet paramAttributeSet, Element paramElement, MouseEvent paramMouseEvent)
/*      */     {
/*      */       URL localURL1;
/*      */       String str1;
/*      */       try
/*      */       {
/*  894 */         URL localURL2 = paramHTMLDocument.getBase();
/*  895 */         localURL1 = new URL(localURL2, paramString);
/*      */ 
/*  899 */         if ((paramString != null) && ("file".equals(localURL1.getProtocol())) && (paramString.startsWith("#")))
/*      */         {
/*  901 */           str1 = localURL2.getFile();
/*  902 */           String str2 = localURL1.getFile();
/*  903 */           if ((str1 != null) && (str2 != null) && (!str2.startsWith(str1)))
/*      */           {
/*  905 */             localURL1 = new URL(localURL2, str1 + paramString);
/*      */           }
/*      */         }
/*      */       } catch (MalformedURLException localMalformedURLException) {
/*  909 */         localURL1 = null;
/*      */       }
/*      */       Object localObject;
/*  913 */       if (!paramHTMLDocument.isFrameDocument()) {
/*  914 */         localObject = new HyperlinkEvent(paramJEditorPane, HyperlinkEvent.EventType.ACTIVATED, localURL1, paramString, paramElement, paramMouseEvent);
/*      */       }
/*      */       else
/*      */       {
/*  918 */         str1 = paramAttributeSet != null ? (String)paramAttributeSet.getAttribute(HTML.Attribute.TARGET) : null;
/*      */ 
/*  920 */         if ((str1 == null) || (str1.equals(""))) {
/*  921 */           str1 = paramHTMLDocument.getBaseTarget();
/*      */         }
/*  923 */         if ((str1 == null) || (str1.equals(""))) {
/*  924 */           str1 = "_self";
/*      */         }
/*  926 */         localObject = new HTMLFrameHyperlinkEvent(paramJEditorPane, HyperlinkEvent.EventType.ACTIVATED, localURL1, paramString, paramElement, paramMouseEvent, str1);
/*      */       }
/*      */ 
/*  930 */       return localObject;
/*      */     }
/*      */ 
/*      */     void fireEvents(JEditorPane paramJEditorPane, HTMLDocument paramHTMLDocument, String paramString, Element paramElement, MouseEvent paramMouseEvent)
/*      */     {
/*      */       URL localURL;
/*  935 */       if (this.href != null)
/*      */       {
/*      */         try
/*      */         {
/*  939 */           localURL = new URL(paramHTMLDocument.getBase(), this.href);
/*      */         } catch (MalformedURLException localMalformedURLException1) {
/*  941 */           localURL = null;
/*      */         }
/*  943 */         HyperlinkEvent localHyperlinkEvent1 = new HyperlinkEvent(paramJEditorPane, HyperlinkEvent.EventType.EXITED, localURL, this.href, paramElement, paramMouseEvent);
/*      */ 
/*  946 */         paramJEditorPane.fireHyperlinkUpdate(localHyperlinkEvent1);
/*      */       }
/*  948 */       if (paramString != null)
/*      */       {
/*      */         try
/*      */         {
/*  952 */           localURL = new URL(paramHTMLDocument.getBase(), paramString);
/*      */         } catch (MalformedURLException localMalformedURLException2) {
/*  954 */           localURL = null;
/*      */         }
/*  956 */         HyperlinkEvent localHyperlinkEvent2 = new HyperlinkEvent(paramJEditorPane, HyperlinkEvent.EventType.ENTERED, localURL, paramString, this.curElem, paramMouseEvent);
/*      */ 
/*  959 */         paramJEditorPane.fireHyperlinkUpdate(localHyperlinkEvent2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NavigateLinkAction extends TextAction
/*      */     implements CaretListener
/*      */   {
/* 1883 */     private static final FocusHighlightPainter focusPainter = new FocusHighlightPainter(null);
/*      */     private final boolean focusBack;
/*      */ 
/*      */     public NavigateLinkAction(String paramString)
/*      */     {
/* 1891 */       super();
/* 1892 */       this.focusBack = "previous-link-action".equals(paramString);
/*      */     }
/*      */ 
/*      */     public void caretUpdate(CaretEvent paramCaretEvent)
/*      */     {
/* 1901 */       Object localObject = paramCaretEvent.getSource();
/* 1902 */       if ((localObject instanceof JTextComponent)) {
/* 1903 */         JTextComponent localJTextComponent = (JTextComponent)localObject;
/* 1904 */         HTMLEditorKit localHTMLEditorKit = getHTMLEditorKit(localJTextComponent);
/* 1905 */         if ((localHTMLEditorKit != null) && (localHTMLEditorKit.foundLink)) {
/* 1906 */           localHTMLEditorKit.foundLink = false;
/*      */ 
/* 1910 */           localJTextComponent.getAccessibleContext().firePropertyChange("AccessibleHypertextOffset", Integer.valueOf(localHTMLEditorKit.prevHypertextOffset), Integer.valueOf(paramCaretEvent.getDot()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1922 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1923 */       if ((localJTextComponent == null) || (localJTextComponent.isEditable())) {
/* 1924 */         return;
/*      */       }
/*      */ 
/* 1927 */       Document localDocument = localJTextComponent.getDocument();
/* 1928 */       HTMLEditorKit localHTMLEditorKit = getHTMLEditorKit(localJTextComponent);
/* 1929 */       if ((localDocument == null) || (localHTMLEditorKit == null)) {
/* 1930 */         return;
/*      */       }
/*      */ 
/* 1935 */       ElementIterator localElementIterator = new ElementIterator(localDocument);
/* 1936 */       int i = localJTextComponent.getCaretPosition();
/* 1937 */       int j = -1;
/* 1938 */       int k = -1;
/*      */       Element localElement;
/* 1942 */       while ((localElement = localElementIterator.next()) != null) {
/* 1943 */         String str = localElement.getName();
/* 1944 */         AttributeSet localAttributeSet = localElement.getAttributes();
/*      */ 
/* 1946 */         Object localObject = HTMLEditorKit.getAttrValue(localAttributeSet, HTML.Attribute.HREF);
/* 1947 */         if ((str.equals(HTML.Tag.OBJECT.toString())) || (localObject != null))
/*      */         {
/* 1951 */           int m = localElement.getStartOffset();
/* 1952 */           if (this.focusBack) {
/* 1953 */             if ((m >= i) && (j >= 0))
/*      */             {
/* 1956 */               localHTMLEditorKit.foundLink = true;
/* 1957 */               localJTextComponent.setCaretPosition(j);
/* 1958 */               moveCaretPosition(localJTextComponent, localHTMLEditorKit, j, k);
/*      */ 
/* 1960 */               localHTMLEditorKit.prevHypertextOffset = j;
/*      */             }
/*      */ 
/*      */           }
/* 1964 */           else if (m > i)
/*      */           {
/* 1966 */             localHTMLEditorKit.foundLink = true;
/* 1967 */             localJTextComponent.setCaretPosition(m);
/* 1968 */             moveCaretPosition(localJTextComponent, localHTMLEditorKit, m, localElement.getEndOffset());
/*      */ 
/* 1970 */             localHTMLEditorKit.prevHypertextOffset = m;
/* 1971 */             return;
/*      */           }
/*      */ 
/* 1974 */           j = localElement.getStartOffset();
/* 1975 */           k = localElement.getEndOffset();
/*      */         }
/*      */       }
/* 1977 */       if ((this.focusBack) && (j >= 0)) {
/* 1978 */         localHTMLEditorKit.foundLink = true;
/* 1979 */         localJTextComponent.setCaretPosition(j);
/* 1980 */         moveCaretPosition(localJTextComponent, localHTMLEditorKit, j, k);
/* 1981 */         localHTMLEditorKit.prevHypertextOffset = j;
/*      */       }
/*      */     }
/*      */ 
/*      */     private void moveCaretPosition(JTextComponent paramJTextComponent, HTMLEditorKit paramHTMLEditorKit, int paramInt1, int paramInt2)
/*      */     {
/* 1990 */       Highlighter localHighlighter = paramJTextComponent.getHighlighter();
/* 1991 */       if (localHighlighter != null) {
/* 1992 */         int i = Math.min(paramInt2, paramInt1);
/* 1993 */         int j = Math.max(paramInt2, paramInt1);
/*      */         try {
/* 1995 */           if (paramHTMLEditorKit.linkNavigationTag != null)
/* 1996 */             localHighlighter.changeHighlight(paramHTMLEditorKit.linkNavigationTag, i, j);
/*      */           else
/* 1998 */             paramHTMLEditorKit.linkNavigationTag = localHighlighter.addHighlight(i, j, focusPainter);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private HTMLEditorKit getHTMLEditorKit(JTextComponent paramJTextComponent) {
/* 2007 */       if ((paramJTextComponent instanceof JEditorPane)) {
/* 2008 */         EditorKit localEditorKit = ((JEditorPane)paramJTextComponent).getEditorKit();
/* 2009 */         if ((localEditorKit instanceof HTMLEditorKit)) {
/* 2010 */           return (HTMLEditorKit)localEditorKit;
/*      */         }
/*      */       }
/* 2013 */       return null;
/*      */     }
/*      */ 
/*      */     static class FocusHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
/*      */     {
/*      */       FocusHighlightPainter(Color paramColor)
/*      */       {
/* 2024 */         super();
/*      */       }
/*      */ 
/*      */       public Shape paintLayer(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent, View paramView)
/*      */       {
/* 2042 */         Color localColor = getColor();
/*      */ 
/* 2044 */         if (localColor == null) {
/* 2045 */           paramGraphics.setColor(paramJTextComponent.getSelectionColor());
/*      */         }
/*      */         else
/* 2048 */           paramGraphics.setColor(localColor);
/*      */         Object localObject;
/* 2050 */         if ((paramInt1 == paramView.getStartOffset()) && (paramInt2 == paramView.getEndOffset()))
/*      */         {
/* 2054 */           if ((paramShape instanceof Rectangle)) {
/* 2055 */             localObject = (Rectangle)paramShape;
/*      */           }
/*      */           else {
/* 2058 */             localObject = paramShape.getBounds();
/*      */           }
/* 2060 */           paramGraphics.drawRect(((Rectangle)localObject).x, ((Rectangle)localObject).y, ((Rectangle)localObject).width - 1, ((Rectangle)localObject).height);
/* 2061 */           return localObject;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 2067 */           localObject = paramView.modelToView(paramInt1, Position.Bias.Forward, paramInt2, Position.Bias.Backward, paramShape);
/*      */ 
/* 2070 */           Rectangle localRectangle = (localObject instanceof Rectangle) ? (Rectangle)localObject : ((Shape)localObject).getBounds();
/*      */ 
/* 2072 */           paramGraphics.drawRect(localRectangle.x, localRectangle.y, localRectangle.width - 1, localRectangle.height);
/* 2073 */           return localRectangle;
/*      */         }
/*      */         catch (BadLocationException localBadLocationException)
/*      */         {
/*      */         }
/*      */ 
/* 2079 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Parser
/*      */   {
/*      */     public abstract void parse(Reader paramReader, HTMLEditorKit.ParserCallback paramParserCallback, boolean paramBoolean)
/*      */       throws IOException;
/*      */   }
/*      */ 
/*      */   public static class ParserCallback
/*      */   {
/*  999 */     public static final Object IMPLIED = "_implied_";
/*      */ 
/*      */     public void flush()
/*      */       throws BadLocationException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleText(char[] paramArrayOfChar, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleComment(char[] paramArrayOfChar, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleStartTag(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleEndTag(HTML.Tag paramTag, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleSimpleTag(HTML.Tag paramTag, MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleError(String paramString, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleEndOfLineString(String paramString)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HTMLEditorKit
 * JD-Core Version:    0.6.2
 */