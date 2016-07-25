/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Style;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.StyleSheet;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicEditorPaneUI extends BasicTextUI
/*     */ {
/*     */   private static final String FONT_ATTRIBUTE_KEY = "FONT_ATTRIBUTE_KEY";
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  62 */     return new BasicEditorPaneUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  80 */     return "EditorPane";
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  89 */     super.installUI(paramJComponent);
/*  90 */     updateDisplayProperties(paramJComponent.getFont(), paramJComponent.getForeground());
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 100 */     cleanDisplayProperties();
/* 101 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public EditorKit getEditorKit(JTextComponent paramJTextComponent)
/*     */   {
/* 112 */     JEditorPane localJEditorPane = (JEditorPane)getComponent();
/* 113 */     return localJEditorPane.getEditorKit();
/*     */   }
/*     */ 
/*     */   ActionMap getActionMap()
/*     */   {
/* 121 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/* 122 */     localActionMapUIResource.put("requestFocus", new BasicTextUI.FocusAction(this));
/* 123 */     EditorKit localEditorKit = getEditorKit(getComponent());
/* 124 */     if (localEditorKit != null) {
/* 125 */       Action[] arrayOfAction = localEditorKit.getActions();
/* 126 */       if (arrayOfAction != null) {
/* 127 */         addActions(localActionMapUIResource, arrayOfAction);
/*     */       }
/*     */     }
/* 130 */     localActionMapUIResource.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
/*     */ 
/* 132 */     localActionMapUIResource.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
/*     */ 
/* 134 */     localActionMapUIResource.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
/*     */ 
/* 136 */     return localActionMapUIResource;
/*     */   }
/*     */ 
/*     */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 150 */     super.propertyChange(paramPropertyChangeEvent);
/* 151 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 152 */     if ("editorKit".equals(str)) {
/* 153 */       localObject1 = SwingUtilities.getUIActionMap(getComponent());
/* 154 */       if (localObject1 != null) {
/* 155 */         localObject2 = paramPropertyChangeEvent.getOldValue();
/* 156 */         if ((localObject2 instanceof EditorKit)) {
/* 157 */           localObject3 = ((EditorKit)localObject2).getActions();
/* 158 */           if (localObject3 != null) {
/* 159 */             removeActions((ActionMap)localObject1, (Action[])localObject3);
/*     */           }
/*     */         }
/* 162 */         Object localObject3 = paramPropertyChangeEvent.getNewValue();
/* 163 */         if ((localObject3 instanceof EditorKit)) {
/* 164 */           Action[] arrayOfAction = ((EditorKit)localObject3).getActions();
/* 165 */           if (arrayOfAction != null) {
/* 166 */             addActions((ActionMap)localObject1, arrayOfAction);
/*     */           }
/*     */         }
/*     */       }
/* 170 */       updateFocusTraversalKeys();
/* 171 */     } else if ("editable".equals(str)) {
/* 172 */       updateFocusTraversalKeys();
/* 173 */     } else if (("foreground".equals(str)) || ("font".equals(str)) || ("document".equals(str)) || ("JEditorPane.w3cLengthUnits".equals(str)) || ("JEditorPane.honorDisplayProperties".equals(str)))
/*     */     {
/* 179 */       localObject1 = getComponent();
/* 180 */       updateDisplayProperties(((JComponent)localObject1).getFont(), ((JComponent)localObject1).getForeground());
/* 181 */       if (("JEditorPane.w3cLengthUnits".equals(str)) || ("JEditorPane.honorDisplayProperties".equals(str)))
/*     */       {
/* 183 */         modelChanged();
/*     */       }
/* 185 */       if ("foreground".equals(str)) {
/* 186 */         localObject2 = ((JComponent)localObject1).getClientProperty("JEditorPane.honorDisplayProperties");
/*     */ 
/* 188 */         boolean bool = false;
/* 189 */         if ((localObject2 instanceof Boolean)) {
/* 190 */           bool = ((Boolean)localObject2).booleanValue();
/*     */         }
/*     */ 
/* 193 */         if (bool)
/* 194 */           modelChanged();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeActions(ActionMap paramActionMap, Action[] paramArrayOfAction)
/*     */   {
/* 203 */     int i = paramArrayOfAction.length;
/* 204 */     for (int j = 0; j < i; j++) {
/* 205 */       Action localAction = paramArrayOfAction[j];
/* 206 */       paramActionMap.remove(localAction.getValue("Name"));
/*     */     }
/*     */   }
/*     */ 
/*     */   void addActions(ActionMap paramActionMap, Action[] paramArrayOfAction) {
/* 211 */     int i = paramArrayOfAction.length;
/* 212 */     for (int j = 0; j < i; j++) {
/* 213 */       Action localAction = paramArrayOfAction[j];
/* 214 */       paramActionMap.put(localAction.getValue("Name"), localAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   void updateDisplayProperties(Font paramFont, Color paramColor) {
/* 219 */     JTextComponent localJTextComponent = getComponent();
/* 220 */     Object localObject1 = localJTextComponent.getClientProperty("JEditorPane.honorDisplayProperties");
/*     */ 
/* 222 */     boolean bool1 = false;
/* 223 */     Object localObject2 = localJTextComponent.getClientProperty("JEditorPane.w3cLengthUnits");
/*     */ 
/* 225 */     boolean bool2 = false;
/* 226 */     if ((localObject1 instanceof Boolean)) {
/* 227 */       bool1 = ((Boolean)localObject1).booleanValue();
/*     */     }
/*     */ 
/* 230 */     if ((localObject2 instanceof Boolean))
/* 231 */       bool2 = ((Boolean)localObject2).booleanValue();
/*     */     Document localDocument;
/* 233 */     if (((this instanceof BasicTextPaneUI)) || (bool1))
/*     */     {
/* 236 */       localDocument = getComponent().getDocument();
/* 237 */       if ((localDocument instanceof StyledDocument))
/* 238 */         if (((localDocument instanceof HTMLDocument)) && (bool1))
/*     */         {
/* 240 */           updateCSS(paramFont, paramColor);
/*     */         }
/* 242 */         else updateStyle(paramFont, paramColor);
/*     */     }
/*     */     else
/*     */     {
/* 246 */       cleanDisplayProperties();
/*     */     }
/*     */     StyleSheet localStyleSheet;
/* 248 */     if (bool2) {
/* 249 */       localDocument = getComponent().getDocument();
/* 250 */       if ((localDocument instanceof HTMLDocument)) {
/* 251 */         localStyleSheet = ((HTMLDocument)localDocument).getStyleSheet();
/*     */ 
/* 253 */         localStyleSheet.addRule("W3C_LENGTH_UNITS_ENABLE");
/*     */       }
/*     */     } else {
/* 256 */       localDocument = getComponent().getDocument();
/* 257 */       if ((localDocument instanceof HTMLDocument)) {
/* 258 */         localStyleSheet = ((HTMLDocument)localDocument).getStyleSheet();
/*     */ 
/* 260 */         localStyleSheet.addRule("W3C_LENGTH_UNITS_DISABLE");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void cleanDisplayProperties()
/*     */   {
/* 274 */     Document localDocument = getComponent().getDocument();
/* 275 */     if ((localDocument instanceof HTMLDocument)) {
/* 276 */       StyleSheet localStyleSheet1 = ((HTMLDocument)localDocument).getStyleSheet();
/*     */ 
/* 278 */       StyleSheet[] arrayOfStyleSheet = localStyleSheet1.getStyleSheets();
/* 279 */       if (arrayOfStyleSheet != null) {
/* 280 */         for (StyleSheet localStyleSheet2 : arrayOfStyleSheet) {
/* 281 */           if ((localStyleSheet2 instanceof StyleSheetUIResource)) {
/* 282 */             localStyleSheet1.removeStyleSheet(localStyleSheet2);
/* 283 */             localStyleSheet1.addRule("BASE_SIZE_DISABLE");
/* 284 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 288 */       ??? = ((StyledDocument)localDocument).getStyle("default");
/* 289 */       if (((Style)???).getAttribute("FONT_ATTRIBUTE_KEY") != null)
/* 290 */         ((Style)???).removeAttribute("FONT_ATTRIBUTE_KEY");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateCSS(Font paramFont, Color paramColor)
/*     */   {
/* 299 */     JTextComponent localJTextComponent = getComponent();
/* 300 */     Document localDocument = localJTextComponent.getDocument();
/* 301 */     if ((localDocument instanceof HTMLDocument)) {
/* 302 */       StyleSheetUIResource localStyleSheetUIResource = new StyleSheetUIResource();
/* 303 */       StyleSheet localStyleSheet1 = ((HTMLDocument)localDocument).getStyleSheet();
/*     */ 
/* 305 */       StyleSheet[] arrayOfStyleSheet = localStyleSheet1.getStyleSheets();
/* 306 */       if (arrayOfStyleSheet != null) {
/* 307 */         for (StyleSheet localStyleSheet2 : arrayOfStyleSheet) {
/* 308 */           if ((localStyleSheet2 instanceof StyleSheetUIResource)) {
/* 309 */             localStyleSheet1.removeStyleSheet(localStyleSheet2);
/*     */           }
/*     */         }
/*     */       }
/* 313 */       ??? = SwingUtilities2.displayPropertiesToCSS(paramFont, paramColor);
/*     */ 
/* 316 */       localStyleSheetUIResource.addRule((String)???);
/* 317 */       localStyleSheet1.addStyleSheet(localStyleSheetUIResource);
/* 318 */       localStyleSheet1.addRule("BASE_SIZE " + localJTextComponent.getFont().getSize());
/*     */ 
/* 320 */       Style localStyle = ((StyledDocument)localDocument).getStyle("default");
/* 321 */       if (!paramFont.equals(localStyle.getAttribute("FONT_ATTRIBUTE_KEY")))
/* 322 */         localStyle.addAttribute("FONT_ATTRIBUTE_KEY", paramFont);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateStyle(Font paramFont, Color paramColor)
/*     */   {
/* 328 */     updateFont(paramFont);
/* 329 */     updateForeground(paramColor);
/*     */   }
/*     */ 
/*     */   private void updateForeground(Color paramColor)
/*     */   {
/* 339 */     StyledDocument localStyledDocument = (StyledDocument)getComponent().getDocument();
/* 340 */     Style localStyle = localStyledDocument.getStyle("default");
/*     */ 
/* 342 */     if (localStyle == null) {
/* 343 */       return;
/*     */     }
/*     */ 
/* 346 */     if (paramColor == null) {
/* 347 */       if (localStyle.getAttribute(StyleConstants.Foreground) != null) {
/* 348 */         localStyle.removeAttribute(StyleConstants.Foreground);
/*     */       }
/*     */     }
/* 351 */     else if (!paramColor.equals(StyleConstants.getForeground(localStyle)))
/* 352 */       StyleConstants.setForeground(localStyle, paramColor);
/*     */   }
/*     */ 
/*     */   private void updateFont(Font paramFont)
/*     */   {
/* 364 */     StyledDocument localStyledDocument = (StyledDocument)getComponent().getDocument();
/* 365 */     Style localStyle = localStyledDocument.getStyle("default");
/*     */ 
/* 367 */     if (localStyle == null) {
/* 368 */       return;
/*     */     }
/*     */ 
/* 371 */     String str = (String)localStyle.getAttribute(StyleConstants.FontFamily);
/* 372 */     Integer localInteger = (Integer)localStyle.getAttribute(StyleConstants.FontSize);
/* 373 */     Boolean localBoolean1 = (Boolean)localStyle.getAttribute(StyleConstants.Bold);
/* 374 */     Boolean localBoolean2 = (Boolean)localStyle.getAttribute(StyleConstants.Italic);
/* 375 */     Font localFont = (Font)localStyle.getAttribute("FONT_ATTRIBUTE_KEY");
/* 376 */     if (paramFont == null) {
/* 377 */       if (str != null) {
/* 378 */         localStyle.removeAttribute(StyleConstants.FontFamily);
/*     */       }
/* 380 */       if (localInteger != null) {
/* 381 */         localStyle.removeAttribute(StyleConstants.FontSize);
/*     */       }
/* 383 */       if (localBoolean1 != null) {
/* 384 */         localStyle.removeAttribute(StyleConstants.Bold);
/*     */       }
/* 386 */       if (localBoolean2 != null) {
/* 387 */         localStyle.removeAttribute(StyleConstants.Italic);
/*     */       }
/* 389 */       if (localFont != null)
/* 390 */         localStyle.removeAttribute("FONT_ATTRIBUTE_KEY");
/*     */     }
/*     */     else {
/* 393 */       if (!paramFont.getName().equals(str)) {
/* 394 */         StyleConstants.setFontFamily(localStyle, paramFont.getName());
/*     */       }
/* 396 */       if ((localInteger == null) || (localInteger.intValue() != paramFont.getSize()))
/*     */       {
/* 398 */         StyleConstants.setFontSize(localStyle, paramFont.getSize());
/*     */       }
/* 400 */       if ((localBoolean1 == null) || (localBoolean1.booleanValue() != paramFont.isBold()))
/*     */       {
/* 402 */         StyleConstants.setBold(localStyle, paramFont.isBold());
/*     */       }
/* 404 */       if ((localBoolean2 == null) || (localBoolean2.booleanValue() != paramFont.isItalic()))
/*     */       {
/* 406 */         StyleConstants.setItalic(localStyle, paramFont.isItalic());
/*     */       }
/* 408 */       if (!paramFont.equals(localFont))
/* 409 */         localStyle.addAttribute("FONT_ATTRIBUTE_KEY", paramFont);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StyleSheetUIResource extends StyleSheet
/*     */     implements UIResource
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicEditorPaneUI
 * JD-Core Version:    0.6.2
 */