/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Shape;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkEvent.EventType;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.ComponentView;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ import sun.swing.text.html.FrameEditorPaneTag;
/*     */ 
/*     */ class FrameView extends ComponentView
/*     */   implements HyperlinkListener
/*     */ {
/*     */   JEditorPane htmlPane;
/*     */   JScrollPane scroller;
/*     */   boolean editable;
/*     */   float width;
/*     */   float height;
/*     */   URL src;
/*     */   private boolean createdComponent;
/*     */ 
/*     */   public FrameView(Element paramElement)
/*     */   {
/*  63 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected Component createComponent()
/*     */   {
/*  68 */     Element localElement = getElement();
/*  69 */     AttributeSet localAttributeSet = localElement.getAttributes();
/*  70 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.SRC);
/*     */ 
/*  72 */     if ((str != null) && (!str.equals(""))) {
/*     */       try {
/*  74 */         URL localURL = ((HTMLDocument)localElement.getDocument()).getBase();
/*  75 */         this.src = new URL(localURL, str);
/*  76 */         this.htmlPane = new FrameEditorPane();
/*  77 */         this.htmlPane.addHyperlinkListener(this);
/*  78 */         JEditorPane localJEditorPane = getHostPane();
/*  79 */         boolean bool = true;
/*  80 */         if (localJEditorPane != null) {
/*  81 */           this.htmlPane.setEditable(localJEditorPane.isEditable());
/*  82 */           localObject1 = (String)localJEditorPane.getClientProperty("charset");
/*  83 */           if (localObject1 != null) {
/*  84 */             this.htmlPane.putClientProperty("charset", localObject1);
/*     */           }
/*  86 */           localObject2 = (HTMLEditorKit)localJEditorPane.getEditorKit();
/*  87 */           if (localObject2 != null) {
/*  88 */             bool = ((HTMLEditorKit)localObject2).isAutoFormSubmission();
/*     */           }
/*     */         }
/*  91 */         this.htmlPane.setPage(this.src);
/*  92 */         Object localObject1 = (HTMLEditorKit)this.htmlPane.getEditorKit();
/*  93 */         if (localObject1 != null) {
/*  94 */           ((HTMLEditorKit)localObject1).setAutoFormSubmission(bool);
/*     */         }
/*     */ 
/*  97 */         Object localObject2 = this.htmlPane.getDocument();
/*  98 */         if ((localObject2 instanceof HTMLDocument)) {
/*  99 */           ((HTMLDocument)localObject2).setFrameDocumentState(true);
/*     */         }
/* 101 */         setMargin();
/* 102 */         createScrollPane();
/* 103 */         setBorder();
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 105 */         localMalformedURLException.printStackTrace();
/*     */       } catch (IOException localIOException) {
/* 107 */         localIOException.printStackTrace();
/*     */       }
/*     */     }
/* 110 */     this.createdComponent = true;
/* 111 */     return this.scroller;
/*     */   }
/*     */ 
/*     */   JEditorPane getHostPane() {
/* 115 */     Container localContainer = getContainer();
/* 116 */     while ((localContainer != null) && (!(localContainer instanceof JEditorPane))) {
/* 117 */       localContainer = localContainer.getParent();
/*     */     }
/* 119 */     return (JEditorPane)localContainer;
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/* 132 */     if (paramView != null) {
/* 133 */       JTextComponent localJTextComponent = (JTextComponent)paramView.getContainer();
/* 134 */       this.editable = localJTextComponent.isEditable();
/*     */     }
/* 136 */     super.setParent(paramView);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 151 */     Container localContainer = getContainer();
/* 152 */     if ((localContainer != null) && (this.htmlPane != null) && (this.htmlPane.isEditable() != ((JTextComponent)localContainer).isEditable()))
/*     */     {
/* 154 */       this.editable = ((JTextComponent)localContainer).isEditable();
/* 155 */       this.htmlPane.setEditable(this.editable);
/*     */     }
/* 157 */     super.paint(paramGraphics, paramShape);
/*     */   }
/*     */ 
/*     */   private void setMargin()
/*     */   {
/* 166 */     int i = 0;
/* 167 */     Insets localInsets1 = this.htmlPane.getMargin();
/*     */ 
/* 169 */     int j = 0;
/* 170 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 171 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.MARGINWIDTH);
/*     */     Insets localInsets2;
/* 172 */     if (localInsets1 != null)
/* 173 */       localInsets2 = new Insets(localInsets1.top, localInsets1.left, localInsets1.right, localInsets1.bottom);
/*     */     else {
/* 175 */       localInsets2 = new Insets(0, 0, 0, 0);
/*     */     }
/* 177 */     if (str != null) {
/* 178 */       i = Integer.parseInt(str);
/* 179 */       if (i > 0) {
/* 180 */         localInsets2.left = i;
/* 181 */         localInsets2.right = i;
/* 182 */         j = 1;
/*     */       }
/*     */     }
/* 185 */     str = (String)localAttributeSet.getAttribute(HTML.Attribute.MARGINHEIGHT);
/* 186 */     if (str != null) {
/* 187 */       i = Integer.parseInt(str);
/* 188 */       if (i > 0) {
/* 189 */         localInsets2.top = i;
/* 190 */         localInsets2.bottom = i;
/* 191 */         j = 1;
/*     */       }
/*     */     }
/* 194 */     if (j != 0)
/* 195 */       this.htmlPane.setMargin(localInsets2);
/*     */   }
/*     */ 
/*     */   private void setBorder()
/*     */   {
/* 206 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 207 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.FRAMEBORDER);
/* 208 */     if ((str != null) && ((str.equals("no")) || (str.equals("0"))))
/*     */     {
/* 211 */       this.scroller.setBorder(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createScrollPane()
/*     */   {
/* 222 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 223 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.SCROLLING);
/* 224 */     if (str == null) {
/* 225 */       str = "auto";
/*     */     }
/*     */ 
/* 228 */     if (!str.equals("no")) {
/* 229 */       if (str.equals("yes")) {
/* 230 */         this.scroller = new JScrollPane(22, 32);
/*     */       }
/*     */       else
/*     */       {
/* 235 */         this.scroller = new JScrollPane();
/*     */       }
/*     */     }
/* 238 */     else this.scroller = new JScrollPane(21, 31);
/*     */ 
/* 242 */     JViewport localJViewport = this.scroller.getViewport();
/* 243 */     localJViewport.add(this.htmlPane);
/* 244 */     localJViewport.setBackingStoreEnabled(true);
/* 245 */     this.scroller.setMinimumSize(new Dimension(5, 5));
/* 246 */     this.scroller.setMaximumSize(new Dimension(2147483647, 2147483647));
/*     */   }
/*     */ 
/*     */   JEditorPane getOutermostJEditorPane()
/*     */   {
/* 256 */     View localView = getParent();
/* 257 */     FrameSetView localFrameSetView = null;
/* 258 */     while (localView != null) {
/* 259 */       if ((localView instanceof FrameSetView)) {
/* 260 */         localFrameSetView = (FrameSetView)localView;
/*     */       }
/* 262 */       localView = localView.getParent();
/*     */     }
/* 264 */     if (localFrameSetView != null) {
/* 265 */       return (JEditorPane)localFrameSetView.getContainer();
/*     */     }
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean inNestedFrameSet()
/*     */   {
/* 276 */     FrameSetView localFrameSetView = (FrameSetView)getParent();
/* 277 */     return localFrameSetView.getParent() instanceof FrameSetView;
/*     */   }
/*     */ 
/*     */   public void hyperlinkUpdate(HyperlinkEvent paramHyperlinkEvent)
/*     */   {
/* 296 */     JEditorPane localJEditorPane = getOutermostJEditorPane();
/* 297 */     if (localJEditorPane == null) {
/* 298 */       return;
/*     */     }
/*     */ 
/* 301 */     if (!(paramHyperlinkEvent instanceof HTMLFrameHyperlinkEvent)) {
/* 302 */       localJEditorPane.fireHyperlinkUpdate(paramHyperlinkEvent);
/* 303 */       return;
/*     */     }
/*     */ 
/* 306 */     HTMLFrameHyperlinkEvent localHTMLFrameHyperlinkEvent = (HTMLFrameHyperlinkEvent)paramHyperlinkEvent;
/*     */ 
/* 308 */     if (localHTMLFrameHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
/* 309 */       String str1 = localHTMLFrameHyperlinkEvent.getTarget();
/* 310 */       String str2 = str1;
/*     */ 
/* 312 */       if ((str1.equals("_parent")) && (!inNestedFrameSet())) {
/* 313 */         str1 = "_top";
/*     */       }
/*     */ 
/* 316 */       if ((paramHyperlinkEvent instanceof FormSubmitEvent)) {
/* 317 */         HTMLEditorKit localHTMLEditorKit = (HTMLEditorKit)localJEditorPane.getEditorKit();
/* 318 */         if ((localHTMLEditorKit != null) && (localHTMLEditorKit.isAutoFormSubmission())) {
/* 319 */           if (str1.equals("_top")) {
/*     */             try {
/* 321 */               movePostData(localJEditorPane, str2);
/* 322 */               localJEditorPane.setPage(localHTMLFrameHyperlinkEvent.getURL());
/*     */             } catch (IOException localIOException2) {
/*     */             }
/*     */           }
/*     */           else {
/* 327 */             HTMLDocument localHTMLDocument = (HTMLDocument)localJEditorPane.getDocument();
/* 328 */             localHTMLDocument.processHTMLFrameHyperlinkEvent(localHTMLFrameHyperlinkEvent);
/*     */           }
/*     */         }
/* 331 */         else localJEditorPane.fireHyperlinkUpdate(paramHyperlinkEvent);
/*     */ 
/* 333 */         return;
/*     */       }
/*     */ 
/* 336 */       if (str1.equals("_top")) {
/*     */         try {
/* 338 */           localJEditorPane.setPage(localHTMLFrameHyperlinkEvent.getURL());
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/*     */       }
/* 344 */       if (!localJEditorPane.isEditable())
/* 345 */         localJEditorPane.fireHyperlinkUpdate(new HTMLFrameHyperlinkEvent(localJEditorPane, localHTMLFrameHyperlinkEvent.getEventType(), localHTMLFrameHyperlinkEvent.getURL(), localHTMLFrameHyperlinkEvent.getDescription(), getElement(), localHTMLFrameHyperlinkEvent.getInputEvent(), str1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 368 */     Element localElement = getElement();
/* 369 */     AttributeSet localAttributeSet = localElement.getAttributes();
/*     */ 
/* 371 */     URL localURL1 = this.src;
/*     */ 
/* 373 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.SRC);
/* 374 */     URL localURL2 = ((HTMLDocument)localElement.getDocument()).getBase();
/*     */     try {
/* 376 */       if (!this.createdComponent) {
/* 377 */         return;
/*     */       }
/*     */ 
/* 380 */       Object localObject = movePostData(this.htmlPane, null);
/* 381 */       this.src = new URL(localURL2, str);
/* 382 */       if ((localURL1.equals(this.src)) && (this.src.getRef() == null) && (localObject == null)) {
/* 383 */         return;
/*     */       }
/*     */ 
/* 386 */       this.htmlPane.setPage(this.src);
/* 387 */       Document localDocument = this.htmlPane.getDocument();
/* 388 */       if ((localDocument instanceof HTMLDocument))
/* 389 */         ((HTMLDocument)localDocument).setFrameDocumentState(true);
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException)
/*     */     {
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object movePostData(JEditorPane paramJEditorPane, String paramString)
/*     */   {
/* 406 */     Object localObject = null;
/* 407 */     JEditorPane localJEditorPane = getOutermostJEditorPane();
/* 408 */     if (localJEditorPane != null) {
/* 409 */       if (paramString == null) {
/* 410 */         paramString = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
/*     */       }
/*     */ 
/* 413 */       if (paramString != null) {
/* 414 */         String str = "javax.swing.JEditorPane.postdata." + paramString;
/* 415 */         Document localDocument = localJEditorPane.getDocument();
/* 416 */         localObject = localDocument.getProperty(str);
/* 417 */         if (localObject != null) {
/* 418 */           paramJEditorPane.getDocument().putProperty("javax.swing.JEditorPane.postdata", localObject);
/*     */ 
/* 420 */           localDocument.putProperty(str, null);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 425 */     return localObject;
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 440 */     return 5.0F;
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 455 */     return 2.147484E+009F;
/*     */   }
/*     */ 
/*     */   class FrameEditorPane extends JEditorPane implements FrameEditorPaneTag {
/*     */     FrameEditorPane() {
/*     */     }
/*     */ 
/*     */     public EditorKit getEditorKitForContentType(String paramString) {
/* 463 */       EditorKit localEditorKit1 = super.getEditorKitForContentType(paramString);
/* 464 */       JEditorPane localJEditorPane = null;
/* 465 */       if ((localJEditorPane = FrameView.this.getOutermostJEditorPane()) != null) {
/* 466 */         EditorKit localEditorKit2 = localJEditorPane.getEditorKitForContentType(paramString);
/* 467 */         if (!localEditorKit1.getClass().equals(localEditorKit2.getClass())) {
/* 468 */           localEditorKit1 = (EditorKit)localEditorKit2.clone();
/* 469 */           setEditorKitForContentType(paramString, localEditorKit1);
/*     */         }
/*     */       }
/* 472 */       return localEditorKit1;
/*     */     }
/*     */ 
/*     */     FrameView getFrameView() {
/* 476 */       return FrameView.this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.FrameView
 * JD-Core Version:    0.6.2
 */