/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.ParagraphView;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ import javax.swing.text.html.HTML.Tag;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.HTMLEditorKit;
/*     */ import javax.swing.text.html.HTMLEditorKit.HTMLFactory;
/*     */ import javax.swing.text.html.ImageView;
/*     */ import javax.swing.text.html.StyleSheet;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicHTML
/*     */ {
/*     */   private static final String htmlDisable = "html.disable";
/*     */   public static final String propertyKey = "html";
/*     */   public static final String documentBaseKey = "html.base";
/*     */   private static BasicEditorKit basicHTMLFactory;
/*     */   private static ViewFactory basicHTMLViewFactory;
/*     */   private static final String styleChanges = "p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }";
/*     */ 
/*     */   public static View createHTMLView(JComponent paramJComponent, String paramString)
/*     */   {
/*  53 */     BasicEditorKit localBasicEditorKit = getFactory();
/*  54 */     Document localDocument = localBasicEditorKit.createDefaultDocument(paramJComponent.getFont(), paramJComponent.getForeground());
/*     */ 
/*  56 */     Object localObject = paramJComponent.getClientProperty("html.base");
/*  57 */     if ((localObject instanceof URL)) {
/*  58 */       ((HTMLDocument)localDocument).setBase((URL)localObject);
/*     */     }
/*  60 */     StringReader localStringReader = new StringReader(paramString);
/*     */     try {
/*  62 */       localBasicEditorKit.read(localStringReader, localDocument, 0);
/*     */     } catch (Throwable localThrowable) {
/*     */     }
/*  65 */     ViewFactory localViewFactory = localBasicEditorKit.getViewFactory();
/*  66 */     View localView = localViewFactory.create(localDocument.getDefaultRootElement());
/*  67 */     Renderer localRenderer = new Renderer(paramJComponent, localViewFactory, localView);
/*  68 */     return localRenderer;
/*     */   }
/*     */ 
/*     */   public static int getHTMLBaseline(View paramView, int paramInt1, int paramInt2)
/*     */   {
/*  85 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  86 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*     */     }
/*     */ 
/*  89 */     if ((paramView instanceof Renderer)) {
/*  90 */       return getBaseline(paramView.getView(0), paramInt1, paramInt2);
/*     */     }
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */   static int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 102 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 103 */     if (localView != null) {
/* 104 */       int i = getHTMLBaseline(localView, paramInt3, paramInt4);
/* 105 */       if (i < 0) {
/* 106 */         return i;
/*     */       }
/* 108 */       return paramInt1 + i;
/*     */     }
/* 110 */     return paramInt1 + paramInt2;
/*     */   }
/*     */ 
/*     */   static int getBaseline(View paramView, int paramInt1, int paramInt2)
/*     */   {
/* 117 */     if (hasParagraph(paramView)) {
/* 118 */       paramView.setSize(paramInt1, paramInt2);
/* 119 */       return getBaseline(paramView, new Rectangle(0, 0, paramInt1, paramInt2));
/*     */     }
/* 121 */     return -1;
/*     */   }
/*     */ 
/*     */   private static int getBaseline(View paramView, Shape paramShape) {
/* 125 */     if (paramView.getViewCount() == 0) {
/* 126 */       return -1;
/*     */     }
/* 128 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/* 129 */     Object localObject = null;
/* 130 */     if (localAttributeSet != null) {
/* 131 */       localObject = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*     */     }
/* 133 */     int i = 0;
/* 134 */     if ((localObject == HTML.Tag.HTML) && (paramView.getViewCount() > 1))
/*     */     {
/* 136 */       i++;
/*     */     }
/* 138 */     paramShape = paramView.getChildAllocation(i, paramShape);
/* 139 */     if (paramShape == null) {
/* 140 */       return -1;
/*     */     }
/* 142 */     View localView = paramView.getView(i);
/* 143 */     if ((paramView instanceof ParagraphView))
/*     */     {
/*     */       Rectangle localRectangle;
/* 145 */       if ((paramShape instanceof Rectangle)) {
/* 146 */         localRectangle = (Rectangle)paramShape;
/*     */       }
/*     */       else {
/* 149 */         localRectangle = paramShape.getBounds();
/*     */       }
/* 151 */       return localRectangle.y + (int)(localRectangle.height * localView.getAlignment(1));
/*     */     }
/*     */ 
/* 154 */     return getBaseline(localView, paramShape);
/*     */   }
/*     */ 
/*     */   private static boolean hasParagraph(View paramView) {
/* 158 */     if ((paramView instanceof ParagraphView)) {
/* 159 */       return true;
/*     */     }
/* 161 */     if (paramView.getViewCount() == 0) {
/* 162 */       return false;
/*     */     }
/* 164 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/* 165 */     Object localObject = null;
/* 166 */     if (localAttributeSet != null) {
/* 167 */       localObject = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*     */     }
/* 169 */     int i = 0;
/* 170 */     if ((localObject == HTML.Tag.HTML) && (paramView.getViewCount() > 1))
/*     */     {
/* 172 */       i = 1;
/*     */     }
/* 174 */     return hasParagraph(paramView.getView(i));
/*     */   }
/*     */ 
/*     */   public static boolean isHTMLString(String paramString)
/*     */   {
/* 183 */     if ((paramString != null) && 
/* 184 */       (paramString.length() >= 6) && (paramString.charAt(0) == '<') && (paramString.charAt(5) == '>')) {
/* 185 */       String str = paramString.substring(1, 5);
/* 186 */       return str.equalsIgnoreCase("html");
/*     */     }
/*     */ 
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   public static void updateRenderer(JComponent paramJComponent, String paramString)
/*     */   {
/* 203 */     View localView1 = null;
/* 204 */     View localView2 = (View)paramJComponent.getClientProperty("html");
/* 205 */     Boolean localBoolean = (Boolean)paramJComponent.getClientProperty("html.disable");
/* 206 */     if ((localBoolean != Boolean.TRUE) && (isHTMLString(paramString))) {
/* 207 */       localView1 = createHTMLView(paramJComponent, paramString);
/*     */     }
/* 209 */     if ((localView1 != localView2) && (localView2 != null)) {
/* 210 */       for (int i = 0; i < localView2.getViewCount(); i++) {
/* 211 */         localView2.getView(i).setParent(null);
/*     */       }
/*     */     }
/* 214 */     paramJComponent.putClientProperty("html", localView1);
/*     */   }
/*     */ 
/*     */   static BasicEditorKit getFactory()
/*     */   {
/* 242 */     if (basicHTMLFactory == null) {
/* 243 */       basicHTMLViewFactory = new BasicHTMLViewFactory();
/* 244 */       basicHTMLFactory = new BasicEditorKit();
/*     */     }
/* 246 */     return basicHTMLFactory;
/*     */   }
/*     */ 
/*     */   static class BasicDocument extends HTMLDocument
/*     */   {
/*     */     BasicDocument(StyleSheet paramStyleSheet, Font paramFont, Color paramColor)
/*     */     {
/* 353 */       super();
/* 354 */       setPreservesUnknownTags(false);
/* 355 */       setFontAndColor(paramFont, paramColor);
/*     */     }
/*     */ 
/*     */     private void setFontAndColor(Font paramFont, Color paramColor)
/*     */     {
/* 365 */       getStyleSheet().addRule(SwingUtilities2.displayPropertiesToCSS(paramFont, paramColor));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class BasicEditorKit extends HTMLEditorKit
/*     */   {
/*     */     private static StyleSheet defaultStyles;
/*     */ 
/*     */     public StyleSheet getStyleSheet()
/*     */     {
/* 287 */       if (defaultStyles == null) {
/* 288 */         defaultStyles = new StyleSheet();
/* 289 */         StringReader localStringReader = new StringReader("p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }");
/*     */         try {
/* 291 */           defaultStyles.loadRules(localStringReader, null);
/*     */         }
/*     */         catch (Throwable localThrowable)
/*     */         {
/*     */         }
/* 296 */         localStringReader.close();
/* 297 */         defaultStyles.addStyleSheet(super.getStyleSheet());
/*     */       }
/* 299 */       return defaultStyles;
/*     */     }
/*     */ 
/*     */     public Document createDefaultDocument(Font paramFont, Color paramColor)
/*     */     {
/* 308 */       StyleSheet localStyleSheet1 = getStyleSheet();
/* 309 */       StyleSheet localStyleSheet2 = new StyleSheet();
/* 310 */       localStyleSheet2.addStyleSheet(localStyleSheet1);
/* 311 */       BasicHTML.BasicDocument localBasicDocument = new BasicHTML.BasicDocument(localStyleSheet2, paramFont, paramColor);
/* 312 */       localBasicDocument.setAsynchronousLoadPriority(2147483647);
/* 313 */       localBasicDocument.setPreservesUnknownTags(false);
/* 314 */       return localBasicDocument;
/*     */     }
/*     */ 
/*     */     public ViewFactory getViewFactory()
/*     */     {
/* 322 */       return BasicHTML.basicHTMLViewFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class BasicHTMLViewFactory extends HTMLEditorKit.HTMLFactory
/*     */   {
/*     */     public View create(Element paramElement)
/*     */     {
/* 333 */       View localView = super.create(paramElement);
/*     */ 
/* 335 */       if ((localView instanceof ImageView)) {
/* 336 */         ((ImageView)localView).setLoadsSynchronously(true);
/*     */       }
/* 338 */       return localView;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Renderer extends View
/*     */   {
/*     */     private int width;
/*     */     private View view;
/*     */     private ViewFactory factory;
/*     */     private JComponent host;
/*     */ 
/*     */     Renderer(JComponent paramJComponent, ViewFactory paramViewFactory, View paramView)
/*     */     {
/* 377 */       super();
/* 378 */       this.host = paramJComponent;
/* 379 */       this.factory = paramViewFactory;
/* 380 */       this.view = paramView;
/* 381 */       this.view.setParent(this);
/*     */ 
/* 383 */       setSize(this.view.getPreferredSpan(0), this.view.getPreferredSpan(1));
/*     */     }
/*     */ 
/*     */     public AttributeSet getAttributes()
/*     */     {
/* 392 */       return null;
/*     */     }
/*     */ 
/*     */     public float getPreferredSpan(int paramInt)
/*     */     {
/* 405 */       if (paramInt == 0)
/*     */       {
/* 407 */         return this.width;
/*     */       }
/* 409 */       return this.view.getPreferredSpan(paramInt);
/*     */     }
/*     */ 
/*     */     public float getMinimumSpan(int paramInt)
/*     */     {
/* 422 */       return this.view.getMinimumSpan(paramInt);
/*     */     }
/*     */ 
/*     */     public float getMaximumSpan(int paramInt)
/*     */     {
/* 435 */       return 2.147484E+009F;
/*     */     }
/*     */ 
/*     */     public void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 457 */       this.host.revalidate();
/* 458 */       this.host.repaint();
/*     */     }
/*     */ 
/*     */     public float getAlignment(int paramInt)
/*     */     {
/* 469 */       return this.view.getAlignment(paramInt);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, Shape paramShape)
/*     */     {
/* 479 */       Rectangle localRectangle = paramShape.getBounds();
/* 480 */       this.view.setSize(localRectangle.width, localRectangle.height);
/* 481 */       this.view.paint(paramGraphics, paramShape);
/*     */     }
/*     */ 
/*     */     public void setParent(View paramView)
/*     */     {
/* 490 */       throw new Error("Can't set parent on root view");
/*     */     }
/*     */ 
/*     */     public int getViewCount()
/*     */     {
/* 502 */       return 1;
/*     */     }
/*     */ 
/*     */     public View getView(int paramInt)
/*     */     {
/* 512 */       return this.view;
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */       throws BadLocationException
/*     */     {
/* 524 */       return this.view.modelToView(paramInt, paramShape, paramBias);
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt1, Position.Bias paramBias1, int paramInt2, Position.Bias paramBias2, Shape paramShape)
/*     */       throws BadLocationException
/*     */     {
/* 548 */       return this.view.modelToView(paramInt1, paramBias1, paramInt2, paramBias2, paramShape);
/*     */     }
/*     */ 
/*     */     public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */     {
/* 562 */       return this.view.viewToModel(paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/*     */     }
/*     */ 
/*     */     public Document getDocument()
/*     */     {
/* 571 */       return this.view.getDocument();
/*     */     }
/*     */ 
/*     */     public int getStartOffset()
/*     */     {
/* 580 */       return this.view.getStartOffset();
/*     */     }
/*     */ 
/*     */     public int getEndOffset()
/*     */     {
/* 589 */       return this.view.getEndOffset();
/*     */     }
/*     */ 
/*     */     public Element getElement()
/*     */     {
/* 598 */       return this.view.getElement();
/*     */     }
/*     */ 
/*     */     public void setSize(float paramFloat1, float paramFloat2)
/*     */     {
/* 608 */       this.width = ((int)paramFloat1);
/* 609 */       this.view.setSize(paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/*     */     public Container getContainer()
/*     */     {
/* 621 */       return this.host;
/*     */     }
/*     */ 
/*     */     public ViewFactory getViewFactory()
/*     */     {
/* 635 */       return this.factory;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicHTML
 * JD-Core Version:    0.6.2
 */