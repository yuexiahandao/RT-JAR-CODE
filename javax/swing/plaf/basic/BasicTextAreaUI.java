/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.BoxView;
/*     */ import javax.swing.text.CompositeView;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.GlyphView;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.ParagraphView;
/*     */ import javax.swing.text.PlainView;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ import javax.swing.text.WrappedPlainView;
/*     */ 
/*     */ public class BasicTextAreaUI extends BasicTextUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  61 */     return new BasicTextAreaUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  79 */     return "TextArea";
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  83 */     super.installDefaults();
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/*  98 */     super.propertyChange(paramPropertyChangeEvent);
/*  99 */     if ((paramPropertyChangeEvent.getPropertyName().equals("lineWrap")) || (paramPropertyChangeEvent.getPropertyName().equals("wrapStyleWord")) || (paramPropertyChangeEvent.getPropertyName().equals("tabSize")))
/*     */     {
/* 103 */       modelChanged();
/* 104 */     } else if ("editable".equals(paramPropertyChangeEvent.getPropertyName()))
/* 105 */       updateFocusTraversalKeys();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 120 */     return super.getPreferredSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 134 */     return super.getMinimumSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public View create(Element paramElement)
/*     */   {
/* 146 */     Document localDocument = paramElement.getDocument();
/* 147 */     Object localObject1 = localDocument.getProperty("i18n");
/* 148 */     if ((localObject1 != null) && (localObject1.equals(Boolean.TRUE)))
/*     */     {
/* 150 */       return createI18N(paramElement);
/*     */     }
/* 152 */     JTextComponent localJTextComponent = getComponent();
/* 153 */     if ((localJTextComponent instanceof JTextArea)) {
/* 154 */       JTextArea localJTextArea = (JTextArea)localJTextComponent;
/*     */       Object localObject2;
/* 156 */       if (localJTextArea.getLineWrap())
/* 157 */         localObject2 = new WrappedPlainView(paramElement, localJTextArea.getWrapStyleWord());
/*     */       else {
/* 159 */         localObject2 = new PlainView(paramElement);
/*     */       }
/* 161 */       return localObject2;
/*     */     }
/*     */ 
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   View createI18N(Element paramElement) {
/* 168 */     String str = paramElement.getName();
/* 169 */     if (str != null) {
/* 170 */       if (str.equals("content"))
/* 171 */         return new PlainParagraph(paramElement);
/* 172 */       if (str.equals("paragraph")) {
/* 173 */         return new BoxView(paramElement, 1);
/*     */       }
/*     */     }
/* 176 */     return null;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 189 */     Object localObject1 = ((JTextComponent)paramJComponent).getDocument().getProperty("i18n");
/*     */ 
/* 191 */     Insets localInsets = paramJComponent.getInsets();
/* 192 */     if (Boolean.TRUE.equals(localObject1)) {
/* 193 */       localObject2 = getRootView((JTextComponent)paramJComponent);
/* 194 */       if (((View)localObject2).getViewCount() > 0) {
/* 195 */         paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/* 196 */         int i = localInsets.top;
/* 197 */         int j = BasicHTML.getBaseline(((View)localObject2).getView(0), paramInt1 - localInsets.left - localInsets.right, paramInt2);
/*     */ 
/* 200 */         if (j < 0) {
/* 201 */           return -1;
/*     */         }
/* 203 */         return i + j;
/*     */       }
/* 205 */       return -1;
/*     */     }
/* 207 */     Object localObject2 = paramJComponent.getFontMetrics(paramJComponent.getFont());
/* 208 */     return localInsets.top + ((FontMetrics)localObject2).getAscent();
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 221 */     super.getBaselineResizeBehavior(paramJComponent);
/* 222 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*     */   }
/*     */ 
/*     */   static class PlainParagraph extends ParagraphView
/*     */   {
/*     */     PlainParagraph(Element paramElement)
/*     */     {
/* 233 */       super();
/* 234 */       this.layoutPool = new LogicalView(paramElement);
/* 235 */       this.layoutPool.setParent(this);
/*     */     }
/*     */ 
/*     */     public void setParent(View paramView) {
/* 239 */       super.setParent(paramView);
/* 240 */       if (paramView != null)
/* 241 */         setPropertiesFromAttributes();
/*     */     }
/*     */ 
/*     */     protected void setPropertiesFromAttributes()
/*     */     {
/* 246 */       Container localContainer = getContainer();
/* 247 */       if ((localContainer != null) && (!localContainer.getComponentOrientation().isLeftToRight()))
/* 248 */         setJustification(2);
/*     */       else
/* 250 */         setJustification(0);
/*     */     }
/*     */ 
/*     */     public int getFlowSpan(int paramInt)
/*     */     {
/* 259 */       Container localContainer = getContainer();
/* 260 */       if ((localContainer instanceof JTextArea)) {
/* 261 */         JTextArea localJTextArea = (JTextArea)localContainer;
/* 262 */         if (!localJTextArea.getLineWrap())
/*     */         {
/* 264 */           return 2147483647;
/*     */         }
/*     */       }
/* 267 */       return super.getFlowSpan(paramInt);
/*     */     }
/*     */ 
/*     */     protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */     {
/* 272 */       SizeRequirements localSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/* 273 */       Container localContainer = getContainer();
/* 274 */       if ((localContainer instanceof JTextArea)) {
/* 275 */         JTextArea localJTextArea = (JTextArea)localContainer;
/* 276 */         if (!localJTextArea.getLineWrap())
/*     */         {
/* 278 */           localSizeRequirements.minimum = localSizeRequirements.preferred;
/*     */         } else {
/* 280 */           localSizeRequirements.minimum = 0;
/* 281 */           localSizeRequirements.preferred = getWidth();
/* 282 */           if (localSizeRequirements.preferred == 2147483647)
/*     */           {
/* 285 */             localSizeRequirements.preferred = 100;
/*     */           }
/*     */         }
/*     */       }
/* 289 */       return localSizeRequirements;
/*     */     }
/*     */ 
/*     */     public void setSize(float paramFloat1, float paramFloat2)
/*     */     {
/* 301 */       if ((int)paramFloat1 != getWidth()) {
/* 302 */         preferenceChanged(null, true, true);
/*     */       }
/* 304 */       super.setSize(paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/*     */     static class LogicalView extends CompositeView
/*     */     {
/*     */       LogicalView(Element paramElement)
/*     */       {
/* 317 */         super();
/*     */       }
/*     */ 
/*     */       protected int getViewIndexAtPosition(int paramInt) {
/* 321 */         Element localElement = getElement();
/* 322 */         if (localElement.getElementCount() > 0) {
/* 323 */           return localElement.getElementIndex(paramInt);
/*     */         }
/* 325 */         return 0;
/*     */       }
/*     */ 
/*     */       protected boolean updateChildren(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, ViewFactory paramViewFactory)
/*     */       {
/* 330 */         return false;
/*     */       }
/*     */ 
/*     */       protected void loadChildren(ViewFactory paramViewFactory) {
/* 334 */         Element localElement = getElement();
/* 335 */         if (localElement.getElementCount() > 0) {
/* 336 */           super.loadChildren(paramViewFactory);
/*     */         } else {
/* 338 */           GlyphView localGlyphView = new GlyphView(localElement);
/* 339 */           append(localGlyphView);
/*     */         }
/*     */       }
/*     */ 
/*     */       public float getPreferredSpan(int paramInt) {
/* 344 */         if (getViewCount() != 1) {
/* 345 */           throw new Error("One child view is assumed.");
/*     */         }
/* 347 */         View localView = getView(0);
/* 348 */         return localView.getPreferredSpan(paramInt);
/*     */       }
/*     */ 
/*     */       protected void forwardUpdateToView(View paramView, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */       {
/* 367 */         paramView.setParent(this);
/* 368 */         super.forwardUpdateToView(paramView, paramDocumentEvent, paramShape, paramViewFactory);
/*     */       }
/*     */ 
/*     */       public void paint(Graphics paramGraphics, Shape paramShape)
/*     */       {
/*     */       }
/*     */ 
/*     */       protected boolean isBefore(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*     */       {
/* 378 */         return false;
/*     */       }
/*     */ 
/*     */       protected boolean isAfter(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/* 382 */         return false;
/*     */       }
/*     */ 
/*     */       protected View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/* 386 */         return null;
/*     */       }
/*     */ 
/*     */       protected void childAllocation(int paramInt, Rectangle paramRectangle)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTextAreaUI
 * JD-Core Version:    0.6.2
 */