/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleHyperlink;
/*      */ import javax.accessibility.AccessibleHypertext;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.HyperlinkEvent;
/*      */ import javax.swing.event.HyperlinkEvent.EventType;
/*      */ import javax.swing.event.HyperlinkListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.BoxView;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.ChangedCharSetException;
/*      */ import javax.swing.text.CompositeView;
/*      */ import javax.swing.text.DefaultEditorKit;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.EditorKit;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.ElementIterator;
/*      */ import javax.swing.text.GlyphView;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.JTextComponent.AccessibleJTextComponent;
/*      */ import javax.swing.text.ParagraphView;
/*      */ import javax.swing.text.StyledEditorKit;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import javax.swing.text.WrappedPlainView;
/*      */ import javax.swing.text.html.HTML.Attribute;
/*      */ import javax.swing.text.html.HTML.Tag;
/*      */ import javax.swing.text.html.HTMLDocument;
/*      */ import javax.swing.text.html.HTMLDocument.Iterator;
/*      */ import javax.swing.text.html.HTMLEditorKit;
/*      */ 
/*      */ public class JEditorPane extends JTextComponent
/*      */ {
/*      */   private SwingWorker<URL, Object> pageLoader;
/*      */   private EditorKit kit;
/*      */   private boolean isUserSetEditorKit;
/*      */   private Hashtable<String, Object> pageProperties;
/*      */   static final String PostDataProperty = "javax.swing.JEditorPane.postdata";
/*      */   private Hashtable<String, EditorKit> typeHandlers;
/* 1536 */   private static final Object kitRegistryKey = new StringBuffer("JEditorPane.kitRegistry");
/*      */ 
/* 1538 */   private static final Object kitTypeRegistryKey = new StringBuffer("JEditorPane.kitTypeRegistry");
/*      */ 
/* 1540 */   private static final Object kitLoaderRegistryKey = new StringBuffer("JEditorPane.kitLoaderRegistry");
/*      */   private static final String uiClassID = "EditorPaneUI";
/*      */   public static final String W3C_LENGTH_UNITS = "JEditorPane.w3cLengthUnits";
/*      */   public static final String HONOR_DISPLAY_PROPERTIES = "JEditorPane.honorDisplayProperties";
/* 1577 */   static final Map<String, String> defaultEditorKitMap = new HashMap(0);
/*      */ 
/*      */   public JEditorPane()
/*      */   {
/*  200 */     setFocusCycleRoot(true);
/*  201 */     setFocusTraversalPolicy(new LayoutFocusTraversalPolicy()
/*      */     {
/*      */       public Component getComponentAfter(Container paramAnonymousContainer, Component paramAnonymousComponent) {
/*  204 */         if ((paramAnonymousContainer != JEditorPane.this) || ((!JEditorPane.this.isEditable()) && (JEditorPane.this.getComponentCount() > 0)))
/*      */         {
/*  206 */           return super.getComponentAfter(paramAnonymousContainer, paramAnonymousComponent);
/*      */         }
/*      */ 
/*  209 */         Container localContainer = JEditorPane.this.getFocusCycleRootAncestor();
/*  210 */         return localContainer != null ? localContainer.getFocusTraversalPolicy().getComponentAfter(localContainer, JEditorPane.this) : null;
/*      */       }
/*      */ 
/*      */       public Component getComponentBefore(Container paramAnonymousContainer, Component paramAnonymousComponent)
/*      */       {
/*  219 */         if ((paramAnonymousContainer != JEditorPane.this) || ((!JEditorPane.this.isEditable()) && (JEditorPane.this.getComponentCount() > 0)))
/*      */         {
/*  221 */           return super.getComponentBefore(paramAnonymousContainer, paramAnonymousComponent);
/*      */         }
/*      */ 
/*  224 */         Container localContainer = JEditorPane.this.getFocusCycleRootAncestor();
/*  225 */         return localContainer != null ? localContainer.getFocusTraversalPolicy().getComponentBefore(localContainer, JEditorPane.this) : null;
/*      */       }
/*      */ 
/*      */       public Component getDefaultComponent(Container paramAnonymousContainer)
/*      */       {
/*  234 */         return (paramAnonymousContainer != JEditorPane.this) || ((!JEditorPane.this.isEditable()) && (JEditorPane.this.getComponentCount() > 0)) ? super.getDefaultComponent(paramAnonymousContainer) : null;
/*      */       }
/*      */ 
/*      */       protected boolean accept(Component paramAnonymousComponent)
/*      */       {
/*  240 */         return paramAnonymousComponent != JEditorPane.this ? super.accept(paramAnonymousComponent) : false;
/*      */       }
/*      */     });
/*  245 */     LookAndFeel.installProperty(this, "focusTraversalKeysForward", JComponent.getManagingFocusForwardTraversalKeys());
/*      */ 
/*  249 */     LookAndFeel.installProperty(this, "focusTraversalKeysBackward", JComponent.getManagingFocusBackwardTraversalKeys());
/*      */   }
/*      */ 
/*      */   public JEditorPane(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  263 */     this();
/*  264 */     setPage(paramURL);
/*      */   }
/*      */ 
/*      */   public JEditorPane(String paramString)
/*      */     throws IOException
/*      */   {
/*  276 */     this();
/*  277 */     setPage(paramString);
/*      */   }
/*      */ 
/*      */   public JEditorPane(String paramString1, String paramString2)
/*      */   {
/*  291 */     this();
/*  292 */     setContentType(paramString1);
/*  293 */     setText(paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized void addHyperlinkListener(HyperlinkListener paramHyperlinkListener)
/*      */   {
/*  303 */     this.listenerList.add(HyperlinkListener.class, paramHyperlinkListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removeHyperlinkListener(HyperlinkListener paramHyperlinkListener)
/*      */   {
/*  312 */     this.listenerList.remove(HyperlinkListener.class, paramHyperlinkListener);
/*      */   }
/*      */ 
/*      */   public synchronized HyperlinkListener[] getHyperlinkListeners()
/*      */   {
/*  324 */     return (HyperlinkListener[])this.listenerList.getListeners(HyperlinkListener.class);
/*      */   }
/*      */ 
/*      */   public void fireHyperlinkUpdate(HyperlinkEvent paramHyperlinkEvent)
/*      */   {
/*  340 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  343 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  344 */       if (arrayOfObject[i] == HyperlinkListener.class)
/*  345 */         ((HyperlinkListener)arrayOfObject[(i + 1)]).hyperlinkUpdate(paramHyperlinkEvent);
/*      */   }
/*      */ 
/*      */   public void setPage(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  417 */     if (paramURL == null) {
/*  418 */       throw new IOException("invalid url");
/*      */     }
/*  420 */     URL localURL = getPage();
/*      */ 
/*  424 */     if ((!paramURL.equals(localURL)) && (paramURL.getRef() == null)) {
/*  425 */       scrollRectToVisible(new Rectangle(0, 0, 1, 1));
/*      */     }
/*  427 */     int i = 0;
/*  428 */     Object localObject1 = getPostData();
/*  429 */     if ((localURL == null) || (!localURL.sameFile(paramURL)) || (localObject1 != null))
/*      */     {
/*  432 */       int j = getAsynchronousLoadPriority(getDocument());
/*  433 */       if (j < 0)
/*      */       {
/*  435 */         InputStream localInputStream = getStream(paramURL);
/*  436 */         if (this.kit != null) {
/*  437 */           Document localDocument = initializeModel(this.kit, paramURL);
/*      */ 
/*  443 */           j = getAsynchronousLoadPriority(localDocument);
/*  444 */           if (j >= 0)
/*      */           {
/*  446 */             setDocument(localDocument);
/*  447 */             synchronized (this) {
/*  448 */               this.pageLoader = new PageLoader(localDocument, localInputStream, localURL, paramURL);
/*  449 */               this.pageLoader.execute();
/*      */             }
/*  451 */             return;
/*      */           }
/*  453 */           read(localInputStream, localDocument);
/*  454 */           setDocument(localDocument);
/*  455 */           i = 1;
/*      */         }
/*      */       }
/*      */       else {
/*  459 */         if (this.pageLoader != null) {
/*  460 */           this.pageLoader.cancel(true);
/*      */         }
/*      */ 
/*  465 */         this.pageLoader = new PageLoader(null, null, localURL, paramURL);
/*  466 */         this.pageLoader.execute();
/*  467 */         return;
/*      */       }
/*      */     }
/*  470 */     final String str = paramURL.getRef();
/*  471 */     if (str != null) {
/*  472 */       if (i == 0) {
/*  473 */         scrollToReference(str);
/*      */       }
/*      */       else
/*      */       {
/*  477 */         SwingUtilities.invokeLater(new Runnable() {
/*      */           public void run() {
/*  479 */             JEditorPane.this.scrollToReference(str);
/*      */           }
/*      */         });
/*      */       }
/*  483 */       getDocument().putProperty("stream", paramURL);
/*      */     }
/*  485 */     firePropertyChange("page", localURL, paramURL);
/*      */   }
/*      */ 
/*      */   private Document initializeModel(EditorKit paramEditorKit, URL paramURL)
/*      */   {
/*  492 */     Document localDocument = paramEditorKit.createDefaultDocument();
/*  493 */     if (this.pageProperties != null)
/*      */     {
/*  496 */       for (Enumeration localEnumeration = this.pageProperties.keys(); localEnumeration.hasMoreElements(); ) {
/*  497 */         String str = (String)localEnumeration.nextElement();
/*  498 */         localDocument.putProperty(str, this.pageProperties.get(str));
/*      */       }
/*  500 */       this.pageProperties.clear();
/*      */     }
/*  502 */     if (localDocument.getProperty("stream") == null) {
/*  503 */       localDocument.putProperty("stream", paramURL);
/*      */     }
/*  505 */     return localDocument;
/*      */   }
/*      */ 
/*      */   private int getAsynchronousLoadPriority(Document paramDocument)
/*      */   {
/*  512 */     return (paramDocument instanceof AbstractDocument) ? ((AbstractDocument)paramDocument).getAsynchronousLoadPriority() : -1;
/*      */   }
/*      */ 
/*      */   public void read(InputStream paramInputStream, Object paramObject)
/*      */     throws IOException
/*      */   {
/*      */     Object localObject;
/*  533 */     if (((paramObject instanceof HTMLDocument)) && ((this.kit instanceof HTMLEditorKit)))
/*      */     {
/*  535 */       localObject = (HTMLDocument)paramObject;
/*  536 */       setDocument((Document)localObject);
/*  537 */       read(paramInputStream, (Document)localObject);
/*      */     } else {
/*  539 */       localObject = (String)getClientProperty("charset");
/*  540 */       InputStreamReader localInputStreamReader = localObject != null ? new InputStreamReader(paramInputStream, (String)localObject) : new InputStreamReader(paramInputStream);
/*      */ 
/*  542 */       super.read(localInputStreamReader, paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   void read(InputStream paramInputStream, Document paramDocument)
/*      */     throws IOException
/*      */   {
/*  559 */     if (!Boolean.TRUE.equals(paramDocument.getProperty("IgnoreCharsetDirective")))
/*      */     {
/*  561 */       paramInputStream = new BufferedInputStream(paramInputStream, 10240);
/*  562 */       paramInputStream.mark(10240);
/*      */     }
/*      */     try {
/*  565 */       String str = (String)getClientProperty("charset");
/*  566 */       localObject = str != null ? new InputStreamReader(paramInputStream, str) : new InputStreamReader(paramInputStream);
/*      */ 
/*  568 */       this.kit.read((Reader)localObject, paramDocument, 0);
/*      */     } catch (BadLocationException localBadLocationException1) {
/*  570 */       throw new IOException(localBadLocationException1.getMessage());
/*      */     } catch (ChangedCharSetException localChangedCharSetException) {
/*  572 */       Object localObject = localChangedCharSetException.getCharSetSpec();
/*  573 */       if (localChangedCharSetException.keyEqualsCharSet())
/*  574 */         putClientProperty("charset", localObject);
/*      */       else
/*  576 */         setCharsetFromContentTypeParameters((String)localObject);
/*      */       try
/*      */       {
/*  579 */         paramInputStream.reset();
/*      */       }
/*      */       catch (IOException localIOException) {
/*  582 */         paramInputStream.close();
/*  583 */         URL localURL = (URL)paramDocument.getProperty("stream");
/*  584 */         if (localURL != null) {
/*  585 */           URLConnection localURLConnection = localURL.openConnection();
/*  586 */           paramInputStream = localURLConnection.getInputStream();
/*      */         }
/*      */         else {
/*  589 */           throw localChangedCharSetException;
/*      */         }
/*      */       }
/*      */       try {
/*  593 */         paramDocument.remove(0, paramDocument.getLength()); } catch (BadLocationException localBadLocationException2) {
/*      */       }
/*  595 */       paramDocument.putProperty("IgnoreCharsetDirective", Boolean.valueOf(true));
/*  596 */       read(paramInputStream, paramDocument);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected InputStream getStream(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  728 */     final URLConnection localURLConnection = paramURL.openConnection();
/*  729 */     if ((localURLConnection instanceof HttpURLConnection)) {
/*  730 */       HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURLConnection;
/*  731 */       localHttpURLConnection.setInstanceFollowRedirects(false);
/*  732 */       Object localObject = getPostData();
/*  733 */       if (localObject != null) {
/*  734 */         handlePostData(localHttpURLConnection, localObject);
/*      */       }
/*  736 */       int i = localHttpURLConnection.getResponseCode();
/*  737 */       int j = (i >= 300) && (i <= 399) ? 1 : 0;
/*      */ 
/*  743 */       if (j != 0) {
/*  744 */         String str = localURLConnection.getHeaderField("Location");
/*  745 */         if (str.startsWith("http", 0))
/*  746 */           paramURL = new URL(str);
/*      */         else {
/*  748 */           paramURL = new URL(paramURL, str);
/*      */         }
/*  750 */         return getStream(paramURL);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  756 */     if (SwingUtilities.isEventDispatchThread())
/*  757 */       handleConnectionProperties(localURLConnection);
/*      */     else {
/*      */       try {
/*  760 */         SwingUtilities.invokeAndWait(new Runnable() {
/*      */           public void run() {
/*  762 */             JEditorPane.this.handleConnectionProperties(localURLConnection);
/*      */           } } );
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {
/*  766 */         throw new RuntimeException(localInterruptedException);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/*  768 */         throw new RuntimeException(localInvocationTargetException);
/*      */       }
/*      */     }
/*  771 */     return localURLConnection.getInputStream();
/*      */   }
/*      */ 
/*      */   private void handleConnectionProperties(URLConnection paramURLConnection)
/*      */   {
/*  778 */     if (this.pageProperties == null) {
/*  779 */       this.pageProperties = new Hashtable();
/*      */     }
/*  781 */     String str1 = paramURLConnection.getContentType();
/*  782 */     if (str1 != null) {
/*  783 */       setContentType(str1);
/*  784 */       this.pageProperties.put("content-type", str1);
/*      */     }
/*  786 */     this.pageProperties.put("stream", paramURLConnection.getURL());
/*  787 */     String str2 = paramURLConnection.getContentEncoding();
/*  788 */     if (str2 != null)
/*  789 */       this.pageProperties.put("content-encoding", str2);
/*      */   }
/*      */ 
/*      */   private Object getPostData()
/*      */   {
/*  794 */     return getDocument().getProperty("javax.swing.JEditorPane.postdata");
/*      */   }
/*      */ 
/*      */   private void handlePostData(HttpURLConnection paramHttpURLConnection, Object paramObject) throws IOException
/*      */   {
/*  799 */     paramHttpURLConnection.setDoOutput(true);
/*  800 */     DataOutputStream localDataOutputStream = null;
/*      */     try {
/*  802 */       paramHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*      */ 
/*  804 */       localDataOutputStream = new DataOutputStream(paramHttpURLConnection.getOutputStream());
/*  805 */       localDataOutputStream.writeBytes((String)paramObject);
/*      */     } finally {
/*  807 */       if (localDataOutputStream != null)
/*  808 */         localDataOutputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void scrollToReference(String paramString)
/*      */   {
/*  830 */     Document localDocument = getDocument();
/*  831 */     if ((localDocument instanceof HTMLDocument)) {
/*  832 */       HTMLDocument localHTMLDocument = (HTMLDocument)localDocument;
/*  833 */       HTMLDocument.Iterator localIterator = localHTMLDocument.getIterator(HTML.Tag.A);
/*  834 */       for (; localIterator.isValid(); localIterator.next()) {
/*  835 */         AttributeSet localAttributeSet = localIterator.getAttributes();
/*  836 */         String str = (String)localAttributeSet.getAttribute(HTML.Attribute.NAME);
/*  837 */         if ((str != null) && (str.equals(paramString)))
/*      */           try
/*      */           {
/*  840 */             int i = localIterator.getStartOffset();
/*  841 */             Rectangle localRectangle1 = modelToView(i);
/*  842 */             if (localRectangle1 != null)
/*      */             {
/*  845 */               Rectangle localRectangle2 = getVisibleRect();
/*      */ 
/*  847 */               localRectangle1.height = localRectangle2.height;
/*  848 */               scrollRectToVisible(localRectangle1);
/*  849 */               setCaretPosition(i);
/*      */             }
/*      */           } catch (BadLocationException localBadLocationException) {
/*  852 */             UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public URL getPage()
/*      */   {
/*  868 */     return (URL)getDocument().getProperty("stream");
/*      */   }
/*      */ 
/*      */   public void setPage(String paramString)
/*      */     throws IOException
/*      */   {
/*  879 */     if (paramString == null) {
/*  880 */       throw new IOException("invalid url");
/*      */     }
/*  882 */     URL localURL = new URL(paramString);
/*  883 */     setPage(localURL);
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  894 */     return "EditorPaneUI";
/*      */   }
/*      */ 
/*      */   protected EditorKit createDefaultEditorKit()
/*      */   {
/*  904 */     return new PlainEditorKit();
/*      */   }
/*      */ 
/*      */   public EditorKit getEditorKit()
/*      */   {
/*  915 */     if (this.kit == null) {
/*  916 */       this.kit = createDefaultEditorKit();
/*  917 */       this.isUserSetEditorKit = false;
/*      */     }
/*  919 */     return this.kit;
/*      */   }
/*      */ 
/*      */   public final String getContentType()
/*      */   {
/*  931 */     return this.kit != null ? this.kit.getContentType() : null;
/*      */   }
/*      */ 
/*      */   public final void setContentType(String paramString)
/*      */   {
/*  967 */     int i = paramString.indexOf(";");
/*      */     Object localObject;
/*  968 */     if (i > -1)
/*      */     {
/*  970 */       localObject = paramString.substring(i);
/*      */ 
/*  972 */       paramString = paramString.substring(0, i).trim();
/*  973 */       if (paramString.toLowerCase().startsWith("text/")) {
/*  974 */         setCharsetFromContentTypeParameters((String)localObject);
/*      */       }
/*      */     }
/*  977 */     if ((this.kit == null) || (!paramString.equals(this.kit.getContentType())) || (!this.isUserSetEditorKit))
/*      */     {
/*  979 */       localObject = getEditorKitForContentType(paramString);
/*  980 */       if ((localObject != null) && (localObject != this.kit)) {
/*  981 */         setEditorKit((EditorKit)localObject);
/*  982 */         this.isUserSetEditorKit = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setCharsetFromContentTypeParameters(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  996 */       int i = paramString.indexOf(';');
/*  997 */       if ((i > -1) && (i < paramString.length() - 1)) {
/*  998 */         paramString = paramString.substring(i + 1);
/*      */       }
/*      */ 
/* 1001 */       if (paramString.length() > 0)
/*      */       {
/* 1004 */         HeaderParser localHeaderParser = new HeaderParser(paramString);
/* 1005 */         String str = localHeaderParser.findValue("charset");
/* 1006 */         if (str != null) {
/* 1007 */           putClientProperty("charset", str);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*      */     {
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1019 */       System.err.println("JEditorPane.getCharsetFromContentTypeParameters failed on: " + paramString);
/* 1020 */       localException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEditorKit(EditorKit paramEditorKit)
/*      */   {
/* 1050 */     EditorKit localEditorKit = this.kit;
/* 1051 */     this.isUserSetEditorKit = true;
/* 1052 */     if (localEditorKit != null) {
/* 1053 */       localEditorKit.deinstall(this);
/*      */     }
/* 1055 */     this.kit = paramEditorKit;
/* 1056 */     if (this.kit != null) {
/* 1057 */       this.kit.install(this);
/* 1058 */       setDocument(this.kit.createDefaultDocument());
/*      */     }
/* 1060 */     firePropertyChange("editorKit", localEditorKit, paramEditorKit);
/*      */   }
/*      */ 
/*      */   public EditorKit getEditorKitForContentType(String paramString)
/*      */   {
/* 1083 */     if (this.typeHandlers == null) {
/* 1084 */       this.typeHandlers = new Hashtable(3);
/*      */     }
/* 1086 */     EditorKit localEditorKit = (EditorKit)this.typeHandlers.get(paramString);
/* 1087 */     if (localEditorKit == null) {
/* 1088 */       localEditorKit = createEditorKitForContentType(paramString);
/* 1089 */       if (localEditorKit != null) {
/* 1090 */         setEditorKitForContentType(paramString, localEditorKit);
/*      */       }
/*      */     }
/* 1093 */     if (localEditorKit == null) {
/* 1094 */       localEditorKit = createDefaultEditorKit();
/*      */     }
/* 1096 */     return localEditorKit;
/*      */   }
/*      */ 
/*      */   public void setEditorKitForContentType(String paramString, EditorKit paramEditorKit)
/*      */   {
/* 1109 */     if (this.typeHandlers == null) {
/* 1110 */       this.typeHandlers = new Hashtable(3);
/*      */     }
/* 1112 */     this.typeHandlers.put(paramString, paramEditorKit);
/*      */   }
/*      */ 
/*      */   public void replaceSelection(String paramString)
/*      */   {
/* 1130 */     if (!isEditable()) {
/* 1131 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/* 1132 */       return;
/*      */     }
/* 1134 */     EditorKit localEditorKit = getEditorKit();
/* 1135 */     if ((localEditorKit instanceof StyledEditorKit)) {
/*      */       try {
/* 1137 */         Document localDocument = getDocument();
/* 1138 */         Caret localCaret = getCaret();
/* 1139 */         boolean bool = saveComposedText(localCaret.getDot());
/* 1140 */         int i = Math.min(localCaret.getDot(), localCaret.getMark());
/* 1141 */         int j = Math.max(localCaret.getDot(), localCaret.getMark());
/* 1142 */         if ((localDocument instanceof AbstractDocument)) {
/* 1143 */           ((AbstractDocument)localDocument).replace(i, j - i, paramString, ((StyledEditorKit)localEditorKit).getInputAttributes());
/*      */         }
/*      */         else
/*      */         {
/* 1147 */           if (i != j) {
/* 1148 */             localDocument.remove(i, j - i);
/*      */           }
/* 1150 */           if ((paramString != null) && (paramString.length() > 0)) {
/* 1151 */             localDocument.insertString(i, paramString, ((StyledEditorKit)localEditorKit).getInputAttributes());
/*      */           }
/*      */         }
/*      */ 
/* 1155 */         if (bool)
/* 1156 */           restoreComposedText();
/*      */       }
/*      */       catch (BadLocationException localBadLocationException) {
/* 1159 */         UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */       }
/*      */     }
/*      */     else
/* 1163 */       super.replaceSelection(paramString);
/*      */   }
/*      */ 
/*      */   public static EditorKit createEditorKitForContentType(String paramString)
/*      */   {
/* 1185 */     Hashtable localHashtable = getKitRegisty();
/* 1186 */     EditorKit localEditorKit = (EditorKit)localHashtable.get(paramString);
/* 1187 */     if (localEditorKit == null)
/*      */     {
/* 1189 */       String str = (String)getKitTypeRegistry().get(paramString);
/* 1190 */       ClassLoader localClassLoader = (ClassLoader)getKitLoaderRegistry().get(paramString);
/*      */       try
/*      */       {
/*      */         Class localClass;
/* 1193 */         if (localClassLoader != null) {
/* 1194 */           localClass = localClassLoader.loadClass(str);
/*      */         }
/*      */         else
/*      */         {
/* 1198 */           localClass = Class.forName(str, true, Thread.currentThread().getContextClassLoader());
/*      */         }
/*      */ 
/* 1201 */         localEditorKit = (EditorKit)localClass.newInstance();
/* 1202 */         localHashtable.put(paramString, localEditorKit);
/*      */       } catch (Throwable localThrowable) {
/* 1204 */         localEditorKit = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1210 */     if (localEditorKit != null) {
/* 1211 */       return (EditorKit)localEditorKit.clone();
/*      */     }
/* 1213 */     return null;
/*      */   }
/*      */ 
/*      */   public static void registerEditorKitForContentType(String paramString1, String paramString2)
/*      */   {
/* 1229 */     registerEditorKitForContentType(paramString1, paramString2, Thread.currentThread().getContextClassLoader());
/*      */   }
/*      */ 
/*      */   public static void registerEditorKitForContentType(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*      */   {
/* 1246 */     getKitTypeRegistry().put(paramString1, paramString2);
/* 1247 */     getKitLoaderRegistry().put(paramString1, paramClassLoader);
/* 1248 */     getKitRegisty().remove(paramString1);
/*      */   }
/*      */ 
/*      */   public static String getEditorKitClassNameForContentType(String paramString)
/*      */   {
/* 1260 */     return (String)getKitTypeRegistry().get(paramString);
/*      */   }
/*      */ 
/*      */   private static Hashtable<String, String> getKitTypeRegistry() {
/* 1264 */     loadDefaultKitsIfNecessary();
/* 1265 */     return (Hashtable)SwingUtilities.appContextGet(kitTypeRegistryKey);
/*      */   }
/*      */ 
/*      */   private static Hashtable<String, ClassLoader> getKitLoaderRegistry() {
/* 1269 */     loadDefaultKitsIfNecessary();
/* 1270 */     return (Hashtable)SwingUtilities.appContextGet(kitLoaderRegistryKey);
/*      */   }
/*      */ 
/*      */   private static Hashtable<String, EditorKit> getKitRegisty() {
/* 1274 */     Hashtable localHashtable = (Hashtable)SwingUtilities.appContextGet(kitRegistryKey);
/* 1275 */     if (localHashtable == null) {
/* 1276 */       localHashtable = new Hashtable(3);
/* 1277 */       SwingUtilities.appContextPut(kitRegistryKey, localHashtable);
/*      */     }
/* 1279 */     return localHashtable;
/*      */   }
/*      */ 
/*      */   private static void loadDefaultKitsIfNecessary()
/*      */   {
/* 1289 */     if (SwingUtilities.appContextGet(kitTypeRegistryKey) == null) {
/* 1290 */       synchronized (defaultEditorKitMap) {
/* 1291 */         if (defaultEditorKitMap.size() == 0) {
/* 1292 */           defaultEditorKitMap.put("text/plain", "javax.swing.JEditorPane$PlainEditorKit");
/*      */ 
/* 1294 */           defaultEditorKitMap.put("text/html", "javax.swing.text.html.HTMLEditorKit");
/*      */ 
/* 1296 */           defaultEditorKitMap.put("text/rtf", "javax.swing.text.rtf.RTFEditorKit");
/*      */ 
/* 1298 */           defaultEditorKitMap.put("application/rtf", "javax.swing.text.rtf.RTFEditorKit");
/*      */         }
/*      */       }
/*      */ 
/* 1302 */       ??? = new Hashtable();
/* 1303 */       SwingUtilities.appContextPut(kitTypeRegistryKey, ???);
/* 1304 */       ??? = new Hashtable();
/* 1305 */       SwingUtilities.appContextPut(kitLoaderRegistryKey, ???);
/* 1306 */       for (String str : defaultEditorKitMap.keySet())
/* 1307 */         registerEditorKitForContentType(str, (String)defaultEditorKitMap.get(str));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/* 1332 */     Dimension localDimension1 = super.getPreferredSize();
/* 1333 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 1334 */     if ((localContainer instanceof JViewport)) {
/* 1335 */       JViewport localJViewport = (JViewport)localContainer;
/* 1336 */       TextUI localTextUI = getUI();
/* 1337 */       int i = localDimension1.width;
/* 1338 */       int j = localDimension1.height;
/*      */       int k;
/*      */       Dimension localDimension2;
/* 1339 */       if (!getScrollableTracksViewportWidth()) {
/* 1340 */         k = localJViewport.getWidth();
/* 1341 */         localDimension2 = localTextUI.getMinimumSize(this);
/* 1342 */         if ((k != 0) && (k < localDimension2.width))
/*      */         {
/* 1344 */           i = localDimension2.width;
/*      */         }
/*      */       }
/* 1347 */       if (!getScrollableTracksViewportHeight()) {
/* 1348 */         k = localJViewport.getHeight();
/* 1349 */         localDimension2 = localTextUI.getMinimumSize(this);
/* 1350 */         if ((k != 0) && (k < localDimension2.height))
/*      */         {
/* 1352 */           j = localDimension2.height;
/*      */         }
/*      */       }
/* 1355 */       if ((i != localDimension1.width) || (j != localDimension1.height)) {
/* 1356 */         localDimension1 = new Dimension(i, j);
/*      */       }
/*      */     }
/* 1359 */     return localDimension1;
/*      */   }
/*      */ 
/*      */   public void setText(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1408 */       Document localDocument = getDocument();
/* 1409 */       localDocument.remove(0, localDocument.getLength());
/* 1410 */       if ((paramString == null) || (paramString.equals(""))) {
/* 1411 */         return;
/*      */       }
/* 1413 */       StringReader localStringReader = new StringReader(paramString);
/* 1414 */       EditorKit localEditorKit = getEditorKit();
/* 1415 */       localEditorKit.read(localStringReader, localDocument, 0);
/*      */     } catch (IOException localIOException) {
/* 1417 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */     } catch (BadLocationException localBadLocationException) {
/* 1419 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */   {
/*      */     String str;
/*      */     try
/*      */     {
/* 1437 */       StringWriter localStringWriter = new StringWriter();
/* 1438 */       write(localStringWriter);
/* 1439 */       str = localStringWriter.toString();
/*      */     } catch (IOException localIOException) {
/* 1441 */       str = null;
/*      */     }
/* 1443 */     return str;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 1456 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 1457 */     if ((localContainer instanceof JViewport)) {
/* 1458 */       JViewport localJViewport = (JViewport)localContainer;
/* 1459 */       TextUI localTextUI = getUI();
/* 1460 */       int i = localJViewport.getWidth();
/* 1461 */       Dimension localDimension1 = localTextUI.getMinimumSize(this);
/* 1462 */       Dimension localDimension2 = localTextUI.getMaximumSize(this);
/* 1463 */       if ((i >= localDimension1.width) && (i <= localDimension2.width)) {
/* 1464 */         return true;
/*      */       }
/*      */     }
/* 1467 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportHeight()
/*      */   {
/* 1479 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 1480 */     if ((localContainer instanceof JViewport)) {
/* 1481 */       JViewport localJViewport = (JViewport)localContainer;
/* 1482 */       TextUI localTextUI = getUI();
/* 1483 */       int i = localJViewport.getHeight();
/* 1484 */       Dimension localDimension1 = localTextUI.getMinimumSize(this);
/* 1485 */       if (i >= localDimension1.height) {
/* 1486 */         Dimension localDimension2 = localTextUI.getMaximumSize(this);
/* 1487 */         if (i <= localDimension2.height) {
/* 1488 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1492 */     return false;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1503 */     paramObjectOutputStream.defaultWriteObject();
/* 1504 */     if (getUIClassID().equals("EditorPaneUI")) {
/* 1505 */       byte b = JComponent.getWriteObjCounter(this);
/* 1506 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1507 */       if ((b == 0) && (this.ui != null))
/* 1508 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1590 */     String str1 = this.kit != null ? this.kit.toString() : "";
/*      */ 
/* 1592 */     String str2 = this.typeHandlers != null ? this.typeHandlers.toString() : "";
/*      */ 
/* 1595 */     return super.paramString() + ",kit=" + str1 + ",typeHandlers=" + str2;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1616 */     if ((getEditorKit() instanceof HTMLEditorKit)) {
/* 1617 */       if ((this.accessibleContext == null) || (this.accessibleContext.getClass() != AccessibleJEditorPaneHTML.class))
/*      */       {
/* 1619 */         this.accessibleContext = new AccessibleJEditorPaneHTML();
/*      */       }
/* 1621 */     } else if ((this.accessibleContext == null) || (this.accessibleContext.getClass() != AccessibleJEditorPane.class))
/*      */     {
/* 1623 */       this.accessibleContext = new AccessibleJEditorPane();
/*      */     }
/* 1625 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJEditorPane extends JTextComponent.AccessibleJTextComponent
/*      */   {
/*      */     protected AccessibleJEditorPane()
/*      */     {
/* 1643 */       super();
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription()
/*      */     {
/* 1656 */       String str = this.accessibleDescription;
/*      */ 
/* 1659 */       if (str == null) {
/* 1660 */         str = (String)JEditorPane.this.getClientProperty("AccessibleDescription");
/*      */       }
/* 1662 */       if (str == null) {
/* 1663 */         str = JEditorPane.this.getContentType();
/*      */       }
/* 1665 */       return str;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1676 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1677 */       localAccessibleStateSet.add(AccessibleState.MULTI_LINE);
/* 1678 */       return localAccessibleStateSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class AccessibleJEditorPaneHTML extends JEditorPane.AccessibleJEditorPane
/*      */   {
/*      */     private AccessibleContext accessibleContext;
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/* 1702 */       return new JEditorPane.JEditorPaneAccessibleHypertextSupport(JEditorPane.this);
/*      */     }
/*      */     protected AccessibleJEditorPaneHTML() {
/* 1705 */       super();
/* 1706 */       HTMLEditorKit localHTMLEditorKit = (HTMLEditorKit)JEditorPane.this.getEditorKit();
/* 1707 */       this.accessibleContext = localHTMLEditorKit.getAccessibleContext();
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1716 */       if (this.accessibleContext != null) {
/* 1717 */         return this.accessibleContext.getAccessibleChildrenCount();
/*      */       }
/* 1719 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1734 */       if (this.accessibleContext != null) {
/* 1735 */         return this.accessibleContext.getAccessibleChild(paramInt);
/*      */       }
/* 1737 */       return null;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 1750 */       if ((this.accessibleContext != null) && (paramPoint != null)) {
/*      */         try {
/* 1752 */           AccessibleComponent localAccessibleComponent = this.accessibleContext.getAccessibleComponent();
/*      */ 
/* 1754 */           if (localAccessibleComponent != null) {
/* 1755 */             return localAccessibleComponent.getAccessibleAt(paramPoint);
/*      */           }
/* 1757 */           return null;
/*      */         }
/*      */         catch (IllegalComponentStateException localIllegalComponentStateException) {
/* 1760 */           return null;
/*      */         }
/*      */       }
/* 1763 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class HeaderParser
/*      */   {
/*      */     String raw;
/*      */     String[][] tab;
/*      */ 
/*      */     public HeaderParser(String paramString)
/*      */     {
/* 2292 */       this.raw = paramString;
/* 2293 */       this.tab = new String[10][2];
/* 2294 */       parse();
/*      */     }
/*      */ 
/*      */     private void parse()
/*      */     {
/* 2299 */       if (this.raw != null) {
/* 2300 */         this.raw = this.raw.trim();
/* 2301 */         char[] arrayOfChar = this.raw.toCharArray();
/* 2302 */         int i = 0; int j = 0; int k = 0;
/* 2303 */         int m = 1;
/* 2304 */         int n = 0;
/* 2305 */         int i1 = arrayOfChar.length;
/* 2306 */         while (j < i1) {
/* 2307 */           int i2 = arrayOfChar[j];
/* 2308 */           if (i2 == 61) {
/* 2309 */             this.tab[k][0] = new String(arrayOfChar, i, j - i).toLowerCase();
/* 2310 */             m = 0;
/* 2311 */             j++;
/* 2312 */             i = j;
/* 2313 */           } else if (i2 == 34) {
/* 2314 */             if (n != 0) {
/* 2315 */               this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/* 2316 */               n = 0;
/*      */               do
/* 2318 */                 j++;
/* 2319 */               while ((j < i1) && ((arrayOfChar[j] == ' ') || (arrayOfChar[j] == ',')));
/* 2320 */               m = 1;
/* 2321 */               i = j;
/*      */             } else {
/* 2323 */               n = 1;
/* 2324 */               j++;
/* 2325 */               i = j;
/*      */             }
/* 2327 */           } else if ((i2 == 32) || (i2 == 44)) {
/* 2328 */             if (n != 0) {
/* 2329 */               j++;
/*      */             } else {
/* 2331 */               if (m != 0)
/* 2332 */                 this.tab[(k++)][0] = new String(arrayOfChar, i, j - i).toLowerCase();
/*      */               else {
/* 2334 */                 this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/*      */               }
/* 2336 */               while ((j < i1) && ((arrayOfChar[j] == ' ') || (arrayOfChar[j] == ','))) {
/* 2337 */                 j++;
/*      */               }
/* 2339 */               m = 1;
/* 2340 */               i = j;
/*      */             }
/*      */           } else { j++; }
/*      */ 
/*      */         }
/*      */ 
/* 2346 */         j--; if (j > i) {
/* 2347 */           if (m == 0) {
/* 2348 */             if (arrayOfChar[j] == '"')
/* 2349 */               this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/*      */             else
/* 2351 */               this.tab[(k++)][1] = new String(arrayOfChar, i, j - i + 1);
/*      */           }
/*      */           else
/* 2354 */             this.tab[k][0] = new String(arrayOfChar, i, j - i + 1).toLowerCase();
/*      */         }
/* 2356 */         else if (j == i)
/* 2357 */           if (m == 0) {
/* 2358 */             if (arrayOfChar[j] == '"')
/* 2359 */               this.tab[(k++)][1] = String.valueOf(arrayOfChar[(j - 1)]);
/*      */             else
/* 2361 */               this.tab[(k++)][1] = String.valueOf(arrayOfChar[j]);
/*      */           }
/*      */           else
/* 2364 */             this.tab[k][0] = String.valueOf(arrayOfChar[j]).toLowerCase();
/*      */       }
/*      */     }
/*      */ 
/*      */     public String findKey(int paramInt)
/*      */     {
/* 2372 */       if ((paramInt < 0) || (paramInt > 10))
/* 2373 */         return null;
/* 2374 */       return this.tab[paramInt][0];
/*      */     }
/*      */ 
/*      */     public String findValue(int paramInt) {
/* 2378 */       if ((paramInt < 0) || (paramInt > 10))
/* 2379 */         return null;
/* 2380 */       return this.tab[paramInt][1];
/*      */     }
/*      */ 
/*      */     public String findValue(String paramString) {
/* 2384 */       return findValue(paramString, null);
/*      */     }
/*      */ 
/*      */     public String findValue(String paramString1, String paramString2) {
/* 2388 */       if (paramString1 == null)
/* 2389 */         return paramString2;
/* 2390 */       paramString1 = paramString1.toLowerCase();
/* 2391 */       for (int i = 0; i < 10; i++) {
/* 2392 */         if (this.tab[i][0] == null)
/* 2393 */           return paramString2;
/* 2394 */         if (paramString1.equals(this.tab[i][0])) {
/* 2395 */           return this.tab[i][1];
/*      */         }
/*      */       }
/* 2398 */       return paramString2;
/*      */     }
/*      */ 
/*      */     public int findInt(String paramString, int paramInt) {
/*      */       try {
/* 2403 */         return Integer.parseInt(findValue(paramString, String.valueOf(paramInt))); } catch (Throwable localThrowable) {
/*      */       }
/* 2405 */       return paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class JEditorPaneAccessibleHypertextSupport extends JEditorPane.AccessibleJEditorPane
/*      */     implements AccessibleHypertext
/*      */   {
/*      */     LinkVector hyperlinks;
/* 1938 */     boolean linksValid = false;
/*      */ 
/*      */     private void buildLinkTable()
/*      */     {
/* 1944 */       this.hyperlinks.removeAllElements();
/* 1945 */       Document localDocument = JEditorPane.this.getDocument();
/* 1946 */       if (localDocument != null) {
/* 1947 */         ElementIterator localElementIterator = new ElementIterator(localDocument);
/*      */         Element localElement;
/* 1952 */         while ((localElement = localElementIterator.next()) != null) {
/* 1953 */           if (localElement.isLeaf()) {
/* 1954 */             AttributeSet localAttributeSet1 = localElement.getAttributes();
/* 1955 */             AttributeSet localAttributeSet2 = (AttributeSet)localAttributeSet1.getAttribute(HTML.Tag.A);
/* 1956 */             Object localObject = localAttributeSet2 != null ? (String)localAttributeSet2.getAttribute(HTML.Attribute.HREF) : null;
/*      */ 
/* 1958 */             if (localObject != null) {
/* 1959 */               this.hyperlinks.addElement(new HTMLLink(localElement));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1964 */       this.linksValid = true;
/*      */     }
/*      */ 
/*      */     public JEditorPaneAccessibleHypertextSupport()
/*      */     {
/* 1970 */       super();
/* 1971 */       this.hyperlinks = new LinkVector(null);
/* 1972 */       Document localDocument = JEditorPane.this.getDocument();
/* 1973 */       if (localDocument != null)
/* 1974 */         localDocument.addDocumentListener(new DocumentListener() {
/*      */           public void changedUpdate(DocumentEvent paramAnonymousDocumentEvent) {
/* 1976 */             JEditorPane.JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
/*      */           }
/*      */           public void insertUpdate(DocumentEvent paramAnonymousDocumentEvent) {
/* 1979 */             JEditorPane.JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
/*      */           }
/*      */           public void removeUpdate(DocumentEvent paramAnonymousDocumentEvent) {
/* 1982 */             JEditorPane.JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
/*      */           }
/*      */         });
/*      */     }
/*      */ 
/*      */     public int getLinkCount()
/*      */     {
/* 1994 */       if (!this.linksValid) {
/* 1995 */         buildLinkTable();
/*      */       }
/* 1997 */       return this.hyperlinks.size();
/*      */     }
/*      */ 
/*      */     public int getLinkIndex(int paramInt)
/*      */     {
/* 2009 */       if (!this.linksValid) {
/* 2010 */         buildLinkTable();
/*      */       }
/* 2012 */       Element localElement = null;
/* 2013 */       Document localDocument = JEditorPane.this.getDocument();
/* 2014 */       if (localDocument != null) {
/* 2015 */         for (localElement = localDocument.getDefaultRootElement(); !localElement.isLeaf(); ) {
/* 2016 */           int i = localElement.getElementIndex(paramInt);
/* 2017 */           localElement = localElement.getElement(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2024 */       return this.hyperlinks.baseElementIndex(localElement);
/*      */     }
/*      */ 
/*      */     public AccessibleHyperlink getLink(int paramInt)
/*      */     {
/* 2036 */       if (!this.linksValid) {
/* 2037 */         buildLinkTable();
/*      */       }
/* 2039 */       if ((paramInt >= 0) && (paramInt < this.hyperlinks.size())) {
/* 2040 */         return (AccessibleHyperlink)this.hyperlinks.elementAt(paramInt);
/*      */       }
/* 2042 */       return null;
/*      */     }
/*      */ 
/*      */     public String getLinkText(int paramInt)
/*      */     {
/* 2054 */       if (!this.linksValid) {
/* 2055 */         buildLinkTable();
/*      */       }
/* 2057 */       Element localElement = (Element)this.hyperlinks.elementAt(paramInt);
/* 2058 */       if (localElement != null) {
/* 2059 */         Document localDocument = JEditorPane.this.getDocument();
/* 2060 */         if (localDocument != null) {
/*      */           try {
/* 2062 */             return localDocument.getText(localElement.getStartOffset(), localElement.getEndOffset() - localElement.getStartOffset());
/*      */           }
/*      */           catch (BadLocationException localBadLocationException) {
/* 2065 */             return null;
/*      */           }
/*      */         }
/*      */       }
/* 2069 */       return null;
/*      */     }
/*      */ 
/*      */     public class HTMLLink extends AccessibleHyperlink
/*      */     {
/*      */       Element element;
/*      */ 
/*      */       public HTMLLink(Element arg2)
/*      */       {
/*      */         Object localObject;
/* 1784 */         this.element = localObject;
/*      */       }
/*      */ 
/*      */       public boolean isValid()
/*      */       {
/* 1796 */         return JEditorPane.JEditorPaneAccessibleHypertextSupport.this.linksValid;
/*      */       }
/*      */ 
/*      */       public int getAccessibleActionCount()
/*      */       {
/* 1808 */         return 1;
/*      */       }
/*      */ 
/*      */       public boolean doAccessibleAction(int paramInt)
/*      */       {
/* 1819 */         if ((paramInt == 0) && (isValid() == true)) {
/* 1820 */           URL localURL = (URL)getAccessibleActionObject(paramInt);
/* 1821 */           if (localURL != null) {
/* 1822 */             HyperlinkEvent localHyperlinkEvent = new HyperlinkEvent(JEditorPane.this, HyperlinkEvent.EventType.ACTIVATED, localURL);
/*      */ 
/* 1824 */             JEditorPane.this.fireHyperlinkUpdate(localHyperlinkEvent);
/* 1825 */             return true;
/*      */           }
/*      */         }
/* 1828 */         return false;
/*      */       }
/*      */ 
/*      */       public String getAccessibleActionDescription(int paramInt)
/*      */       {
/* 1842 */         if ((paramInt == 0) && (isValid() == true)) {
/* 1843 */           Document localDocument = JEditorPane.this.getDocument();
/* 1844 */           if (localDocument != null) {
/*      */             try {
/* 1846 */               return localDocument.getText(getStartIndex(), getEndIndex() - getStartIndex());
/*      */             }
/*      */             catch (BadLocationException localBadLocationException) {
/* 1849 */               return null;
/*      */             }
/*      */           }
/*      */         }
/* 1853 */         return null;
/*      */       }
/*      */ 
/*      */       public Object getAccessibleActionObject(int paramInt)
/*      */       {
/* 1864 */         if ((paramInt == 0) && (isValid() == true)) {
/* 1865 */           AttributeSet localAttributeSet1 = this.element.getAttributes();
/* 1866 */           AttributeSet localAttributeSet2 = (AttributeSet)localAttributeSet1.getAttribute(HTML.Tag.A);
/*      */ 
/* 1868 */           String str = localAttributeSet2 != null ? (String)localAttributeSet2.getAttribute(HTML.Attribute.HREF) : null;
/*      */ 
/* 1870 */           if (str != null) {
/*      */             URL localURL;
/*      */             try {
/* 1873 */               localURL = new URL(JEditorPane.this.getPage(), str);
/*      */             } catch (MalformedURLException localMalformedURLException) {
/* 1875 */               localURL = null;
/*      */             }
/* 1877 */             return localURL;
/*      */           }
/*      */         }
/* 1880 */         return null;
/*      */       }
/*      */ 
/*      */       public Object getAccessibleActionAnchor(int paramInt)
/*      */       {
/* 1899 */         return getAccessibleActionDescription(paramInt);
/*      */       }
/*      */ 
/*      */       public int getStartIndex()
/*      */       {
/* 1910 */         return this.element.getStartOffset();
/*      */       }
/*      */ 
/*      */       public int getEndIndex()
/*      */       {
/* 1920 */         return this.element.getEndOffset();
/*      */       }
/*      */     }
/*      */     private class LinkVector extends Vector<JEditorPane.JEditorPaneAccessibleHypertextSupport.HTMLLink> {
/*      */       private LinkVector() {
/*      */       }
/*      */ 
/* 1927 */       public int baseElementIndex(Element paramElement) { for (int i = 0; i < this.elementCount; i++) {
/* 1928 */           JEditorPane.JEditorPaneAccessibleHypertextSupport.HTMLLink localHTMLLink = (JEditorPane.JEditorPaneAccessibleHypertextSupport.HTMLLink)elementAt(i);
/* 1929 */           if (localHTMLLink.element == paramElement) {
/* 1930 */             return i;
/*      */           }
/*      */         }
/* 1933 */         return -1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class PageLoader extends SwingWorker<URL, Object>
/*      */   {
/*      */     InputStream in;
/*      */     URL old;
/*      */     URL page;
/*      */     Document doc;
/*      */ 
/*      */     PageLoader(Document paramInputStream, InputStream paramURL1, URL paramURL2, URL arg5)
/*      */     {
/*  610 */       this.in = paramURL1;
/*  611 */       this.old = paramURL2;
/*      */       Object localObject;
/*  612 */       this.page = localObject;
/*  613 */       this.doc = paramInputStream;
/*      */     }
/*      */ 
/*      */     protected URL doInBackground()
/*      */     {
/*  622 */       int i = 0;
/*      */       try {
/*  624 */         if (this.in == null) {
/*  625 */           this.in = JEditorPane.this.getStream(this.page);
/*  626 */           if (JEditorPane.this.kit == null)
/*      */           {
/*  628 */             UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
/*      */ 
/*  630 */             URL localURL1 = this.old;
/*      */ 
/*  681 */             return this.old;
/*      */           }
/*      */         }
/*  634 */         if (this.doc == null)
/*      */           try {
/*  636 */             SwingUtilities.invokeAndWait(new Runnable() {
/*      */               public void run() {
/*  638 */                 JEditorPane.PageLoader.this.doc = JEditorPane.this.initializeModel(JEditorPane.this.kit, JEditorPane.PageLoader.this.page);
/*  639 */                 JEditorPane.this.setDocument(JEditorPane.PageLoader.this.doc);
/*      */               } } );
/*      */           }
/*      */           catch (InvocationTargetException localInvocationTargetException) {
/*  643 */             UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
/*      */ 
/*  645 */             localObject1 = this.old;
/*      */ 
/*  674 */             if (i != 0) {
/*  675 */               SwingUtilities.invokeLater(new Runnable() {
/*      */                 public void run() {
/*  677 */                   JEditorPane.this.firePropertyChange("page", JEditorPane.PageLoader.this.old, JEditorPane.PageLoader.this.page);
/*      */                 }
/*      */               });
/*      */             }
/*  681 */             return i != 0 ? this.page : this.old;
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*  647 */             UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
/*      */ 
/*  649 */             localObject1 = this.old;
/*      */ 
/*  674 */             if (i != 0) {
/*  675 */               SwingUtilities.invokeLater(new Runnable() {
/*      */                 public void run() {
/*  677 */                   JEditorPane.this.firePropertyChange("page", JEditorPane.PageLoader.this.old, JEditorPane.PageLoader.this.page);
/*      */                 }
/*      */               });
/*      */             }
/*  681 */             return i != 0 ? this.page : this.old;
/*      */           }
/*  653 */         JEditorPane.this.read(this.in, this.doc);
/*  654 */         URL localURL2 = (URL)this.doc.getProperty("stream");
/*  655 */         Object localObject1 = localURL2.getRef();
/*  656 */         if (localObject1 != null)
/*      */         {
/*  660 */           Runnable local2 = new Runnable() {
/*      */             public void run() {
/*  662 */               URL localURL = (URL)JEditorPane.this.getDocument().getProperty("stream");
/*      */ 
/*  664 */               String str = localURL.getRef();
/*  665 */               JEditorPane.this.scrollToReference(str);
/*      */             }
/*      */           };
/*  668 */           SwingUtilities.invokeLater(local2);
/*      */         }
/*  670 */         i = 1;
/*      */ 
/*  681 */         return this.old;
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  672 */         UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
/*      */ 
/*  681 */         return this.old;
/*      */       }
/*      */       finally
/*      */       {
/*  674 */         if (i != 0) {
/*  675 */           SwingUtilities.invokeLater(new Runnable() {
/*      */             public void run() {
/*  677 */               JEditorPane.this.firePropertyChange("page", JEditorPane.PageLoader.this.old, JEditorPane.PageLoader.this.page);
/*      */             }
/*      */           });
/*      */         }
/*  681 */         if (i != 0) tmpTernaryOp = this.page;  } return this.old;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PlainEditorKit extends DefaultEditorKit
/*      */     implements ViewFactory
/*      */   {
/*      */     public ViewFactory getViewFactory()
/*      */     {
/* 2084 */       return this;
/*      */     }
/*      */ 
/*      */     public View create(Element paramElement)
/*      */     {
/* 2096 */       Document localDocument = paramElement.getDocument();
/* 2097 */       Object localObject = localDocument.getProperty("i18n");
/*      */ 
/* 2099 */       if ((localObject != null) && (localObject.equals(Boolean.TRUE)))
/*      */       {
/* 2101 */         return createI18N(paramElement);
/*      */       }
/* 2103 */       return new WrappedPlainView(paramElement);
/*      */     }
/*      */ 
/*      */     View createI18N(Element paramElement)
/*      */     {
/* 2108 */       String str = paramElement.getName();
/* 2109 */       if (str != null) {
/* 2110 */         if (str.equals("content"))
/* 2111 */           return new PlainParagraph(paramElement);
/* 2112 */         if (str.equals("paragraph")) {
/* 2113 */           return new BoxView(paramElement, 1);
/*      */         }
/*      */       }
/* 2116 */       return null;
/*      */     }
/*      */ 
/*      */     static class PlainParagraph extends ParagraphView
/*      */     {
/*      */       PlainParagraph(Element paramElement)
/*      */       {
/* 2126 */         super();
/* 2127 */         this.layoutPool = new LogicalView(paramElement);
/* 2128 */         this.layoutPool.setParent(this);
/*      */       }
/*      */ 
/*      */       protected void setPropertiesFromAttributes() {
/* 2132 */         Container localContainer = getContainer();
/* 2133 */         if ((localContainer != null) && (!localContainer.getComponentOrientation().isLeftToRight()))
/*      */         {
/* 2136 */           setJustification(2);
/*      */         }
/* 2138 */         else setJustification(0);
/*      */       }
/*      */ 
/*      */       public int getFlowSpan(int paramInt)
/*      */       {
/* 2147 */         Container localContainer = getContainer();
/* 2148 */         if ((localContainer instanceof JTextArea)) {
/* 2149 */           JTextArea localJTextArea = (JTextArea)localContainer;
/* 2150 */           if (!localJTextArea.getLineWrap())
/*      */           {
/* 2152 */             return 2147483647;
/*      */           }
/*      */         }
/* 2155 */         return super.getFlowSpan(paramInt);
/*      */       }
/*      */ 
/*      */       protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */       {
/* 2161 */         SizeRequirements localSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/*      */ 
/* 2163 */         Container localContainer = getContainer();
/* 2164 */         if ((localContainer instanceof JTextArea)) {
/* 2165 */           JTextArea localJTextArea = (JTextArea)localContainer;
/* 2166 */           if (!localJTextArea.getLineWrap())
/*      */           {
/* 2168 */             localSizeRequirements.minimum = localSizeRequirements.preferred;
/*      */           }
/*      */         }
/* 2171 */         return localSizeRequirements;
/*      */       }
/*      */ 
/*      */       static class LogicalView extends CompositeView
/*      */       {
/*      */         LogicalView(Element paramElement)
/*      */         {
/* 2184 */           super();
/*      */         }
/*      */ 
/*      */         protected int getViewIndexAtPosition(int paramInt) {
/* 2188 */           Element localElement = getElement();
/* 2189 */           if (localElement.getElementCount() > 0) {
/* 2190 */             return localElement.getElementIndex(paramInt);
/*      */           }
/* 2192 */           return 0;
/*      */         }
/*      */ 
/*      */         protected boolean updateChildren(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, ViewFactory paramViewFactory)
/*      */         {
/* 2199 */           return false;
/*      */         }
/*      */ 
/*      */         protected void loadChildren(ViewFactory paramViewFactory) {
/* 2203 */           Element localElement = getElement();
/* 2204 */           if (localElement.getElementCount() > 0) {
/* 2205 */             super.loadChildren(paramViewFactory);
/*      */           } else {
/* 2207 */             GlyphView localGlyphView = new GlyphView(localElement);
/* 2208 */             append(localGlyphView);
/*      */           }
/*      */         }
/*      */ 
/*      */         public float getPreferredSpan(int paramInt) {
/* 2213 */           if (getViewCount() != 1) {
/* 2214 */             throw new Error("One child view is assumed.");
/*      */           }
/* 2216 */           View localView = getView(0);
/*      */ 
/* 2218 */           return localView.getPreferredSpan(paramInt);
/*      */         }
/*      */ 
/*      */         protected void forwardUpdateToView(View paramView, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */         {
/* 2238 */           paramView.setParent(this);
/* 2239 */           super.forwardUpdateToView(paramView, paramDocumentEvent, paramShape, paramViewFactory);
/*      */         }
/*      */ 
/*      */         public void paint(Graphics paramGraphics, Shape paramShape)
/*      */         {
/*      */         }
/*      */ 
/*      */         protected boolean isBefore(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*      */         {
/* 2249 */           return false;
/*      */         }
/*      */ 
/*      */         protected boolean isAfter(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/* 2253 */           return false;
/*      */         }
/*      */ 
/*      */         protected View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/* 2257 */           return null;
/*      */         }
/*      */ 
/*      */         protected void childAllocation(int paramInt, Rectangle paramRectangle)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JEditorPane
 * JD-Core Version:    0.6.2
 */