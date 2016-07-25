/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class MultiLookAndFeel extends LookAndFeel
/*     */ {
/*     */   public String getName()
/*     */   {
/*  72 */     return "Multiplexing Look and Feel";
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/*  82 */     return "Multiplex";
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/*  91 */     return "Allows multiple UI instances per component instance";
/*     */   }
/*     */ 
/*     */   public boolean isNativeLookAndFeel()
/*     */   {
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSupportedLookAndFeel()
/*     */   {
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */   public UIDefaults getDefaults()
/*     */   {
/* 128 */     String str = "javax.swing.plaf.multi.Multi";
/* 129 */     Object[] arrayOfObject = { "ButtonUI", str + "ButtonUI", "CheckBoxMenuItemUI", str + "MenuItemUI", "CheckBoxUI", str + "ButtonUI", "ColorChooserUI", str + "ColorChooserUI", "ComboBoxUI", str + "ComboBoxUI", "DesktopIconUI", str + "DesktopIconUI", "DesktopPaneUI", str + "DesktopPaneUI", "EditorPaneUI", str + "TextUI", "FileChooserUI", str + "FileChooserUI", "FormattedTextFieldUI", str + "TextUI", "InternalFrameUI", str + "InternalFrameUI", "LabelUI", str + "LabelUI", "ListUI", str + "ListUI", "MenuBarUI", str + "MenuBarUI", "MenuItemUI", str + "MenuItemUI", "MenuUI", str + "MenuItemUI", "OptionPaneUI", str + "OptionPaneUI", "PanelUI", str + "PanelUI", "PasswordFieldUI", str + "TextUI", "PopupMenuSeparatorUI", str + "SeparatorUI", "PopupMenuUI", str + "PopupMenuUI", "ProgressBarUI", str + "ProgressBarUI", "RadioButtonMenuItemUI", str + "MenuItemUI", "RadioButtonUI", str + "ButtonUI", "RootPaneUI", str + "RootPaneUI", "ScrollBarUI", str + "ScrollBarUI", "ScrollPaneUI", str + "ScrollPaneUI", "SeparatorUI", str + "SeparatorUI", "SliderUI", str + "SliderUI", "SpinnerUI", str + "SpinnerUI", "SplitPaneUI", str + "SplitPaneUI", "TabbedPaneUI", str + "TabbedPaneUI", "TableHeaderUI", str + "TableHeaderUI", "TableUI", str + "TableUI", "TextAreaUI", str + "TextUI", "TextFieldUI", str + "TextUI", "TextPaneUI", str + "TextUI", "ToggleButtonUI", str + "ButtonUI", "ToolBarSeparatorUI", str + "SeparatorUI", "ToolBarUI", str + "ToolBarUI", "ToolTipUI", str + "ToolTipUI", "TreeUI", str + "TreeUI", "ViewportUI", str + "ViewportUI" };
/*     */ 
/* 175 */     MultiUIDefaults localMultiUIDefaults = new MultiUIDefaults(arrayOfObject.length / 2, 0.75F);
/* 176 */     localMultiUIDefaults.putDefaults(arrayOfObject);
/* 177 */     return localMultiUIDefaults;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUIs(ComponentUI paramComponentUI, Vector paramVector, JComponent paramJComponent)
/*     */   {
/* 229 */     ComponentUI localComponentUI = UIManager.getDefaults().getUI(paramJComponent);
/* 230 */     if (localComponentUI != null) {
/* 231 */       paramVector.addElement(localComponentUI);
/*     */ 
/* 233 */       LookAndFeel[] arrayOfLookAndFeel = UIManager.getAuxiliaryLookAndFeels();
/* 234 */       if (arrayOfLookAndFeel != null)
/* 235 */         for (int i = 0; i < arrayOfLookAndFeel.length; i++) {
/* 236 */           localComponentUI = arrayOfLookAndFeel[i].getDefaults().getUI(paramJComponent);
/* 237 */           if (localComponentUI != null)
/* 238 */             paramVector.addElement(localComponentUI);
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/* 243 */       return null;
/*     */     }
/*     */ 
/* 249 */     if (paramVector.size() == 1) {
/* 250 */       return (ComponentUI)paramVector.elementAt(0);
/*     */     }
/* 252 */     return paramComponentUI;
/*     */   }
/*     */ 
/*     */   protected static ComponentUI[] uisToArray(Vector paramVector)
/*     */   {
/* 272 */     if (paramVector == null) {
/* 273 */       return new ComponentUI[0];
/*     */     }
/* 275 */     int i = paramVector.size();
/* 276 */     if (i > 0) {
/* 277 */       ComponentUI[] arrayOfComponentUI = new ComponentUI[i];
/* 278 */       for (int j = 0; j < i; j++) {
/* 279 */         arrayOfComponentUI[j] = ((ComponentUI)paramVector.elementAt(j));
/*     */       }
/* 281 */       return arrayOfComponentUI;
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiLookAndFeel
 * JD-Core Version:    0.6.2
 */