/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.ItemSelectable;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.Transient;
/*      */ import java.io.Serializable;
/*      */ import java.text.BreakIterator;
/*      */ import java.util.Enumeration;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleExtendedComponent;
/*      */ import javax.accessibility.AccessibleIcon;
/*      */ import javax.accessibility.AccessibleKeyBinding;
/*      */ import javax.accessibility.AccessibleRelation;
/*      */ import javax.accessibility.AccessibleRelationSet;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ButtonUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ public abstract class AbstractButton extends JComponent
/*      */   implements ItemSelectable, SwingConstants
/*      */ {
/*      */   public static final String MODEL_CHANGED_PROPERTY = "model";
/*      */   public static final String TEXT_CHANGED_PROPERTY = "text";
/*      */   public static final String MNEMONIC_CHANGED_PROPERTY = "mnemonic";
/*      */   public static final String MARGIN_CHANGED_PROPERTY = "margin";
/*      */   public static final String VERTICAL_ALIGNMENT_CHANGED_PROPERTY = "verticalAlignment";
/*      */   public static final String HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY = "horizontalAlignment";
/*      */   public static final String VERTICAL_TEXT_POSITION_CHANGED_PROPERTY = "verticalTextPosition";
/*      */   public static final String HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY = "horizontalTextPosition";
/*      */   public static final String BORDER_PAINTED_CHANGED_PROPERTY = "borderPainted";
/*      */   public static final String FOCUS_PAINTED_CHANGED_PROPERTY = "focusPainted";
/*      */   public static final String ROLLOVER_ENABLED_CHANGED_PROPERTY = "rolloverEnabled";
/*      */   public static final String CONTENT_AREA_FILLED_CHANGED_PROPERTY = "contentAreaFilled";
/*      */   public static final String ICON_CHANGED_PROPERTY = "icon";
/*      */   public static final String PRESSED_ICON_CHANGED_PROPERTY = "pressedIcon";
/*      */   public static final String SELECTED_ICON_CHANGED_PROPERTY = "selectedIcon";
/*      */   public static final String ROLLOVER_ICON_CHANGED_PROPERTY = "rolloverIcon";
/*      */   public static final String ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY = "rolloverSelectedIcon";
/*      */   public static final String DISABLED_ICON_CHANGED_PROPERTY = "disabledIcon";
/*      */   public static final String DISABLED_SELECTED_ICON_CHANGED_PROPERTY = "disabledSelectedIcon";
/*      */   protected ButtonModel model;
/*      */   private String text;
/*      */   private Insets margin;
/*      */   private Insets defaultMargin;
/*      */   private Icon defaultIcon;
/*      */   private Icon pressedIcon;
/*      */   private Icon disabledIcon;
/*      */   private Icon selectedIcon;
/*      */   private Icon disabledSelectedIcon;
/*      */   private Icon rolloverIcon;
/*      */   private Icon rolloverSelectedIcon;
/*      */   private boolean paintBorder;
/*      */   private boolean paintFocus;
/*      */   private boolean rolloverEnabled;
/*      */   private boolean contentAreaFilled;
/*      */   private int verticalAlignment;
/*      */   private int horizontalAlignment;
/*      */   private int verticalTextPosition;
/*      */   private int horizontalTextPosition;
/*      */   private int iconTextGap;
/*      */   private int mnemonic;
/*      */   private int mnemonicIndex;
/*      */   private long multiClickThreshhold;
/*      */   private boolean borderPaintedSet;
/*      */   private boolean rolloverEnabledSet;
/*      */   private boolean iconTextGapSet;
/*      */   private boolean contentAreaFilledSet;
/*      */   private boolean setLayout;
/*      */   boolean defaultCapable;
/*      */   private Handler handler;
/*      */   protected ChangeListener changeListener;
/*      */   protected ActionListener actionListener;
/*      */   protected ItemListener itemListener;
/*      */   protected transient ChangeEvent changeEvent;
/*      */   private boolean hideActionText;
/*      */   private Action action;
/*      */   private PropertyChangeListener actionPropertyChangeListener;
/*      */ 
/*      */   public AbstractButton()
/*      */   {
/*  161 */     this.model = null;
/*      */ 
/*  163 */     this.text = "";
/*  164 */     this.margin = null;
/*  165 */     this.defaultMargin = null;
/*      */ 
/*  169 */     this.defaultIcon = null;
/*  170 */     this.pressedIcon = null;
/*  171 */     this.disabledIcon = null;
/*      */ 
/*  173 */     this.selectedIcon = null;
/*  174 */     this.disabledSelectedIcon = null;
/*      */ 
/*  176 */     this.rolloverIcon = null;
/*  177 */     this.rolloverSelectedIcon = null;
/*      */ 
/*  180 */     this.paintBorder = true;
/*  181 */     this.paintFocus = true;
/*  182 */     this.rolloverEnabled = false;
/*  183 */     this.contentAreaFilled = true;
/*      */ 
/*  186 */     this.verticalAlignment = 0;
/*  187 */     this.horizontalAlignment = 0;
/*      */ 
/*  189 */     this.verticalTextPosition = 0;
/*  190 */     this.horizontalTextPosition = 11;
/*      */ 
/*  192 */     this.iconTextGap = 4;
/*      */ 
/*  195 */     this.mnemonicIndex = -1;
/*      */ 
/*  197 */     this.multiClickThreshhold = 0L;
/*      */ 
/*  199 */     this.borderPaintedSet = false;
/*  200 */     this.rolloverEnabledSet = false;
/*  201 */     this.iconTextGapSet = false;
/*  202 */     this.contentAreaFilledSet = false;
/*      */ 
/*  205 */     this.setLayout = false;
/*      */ 
/*  209 */     this.defaultCapable = true;
/*      */ 
/*  219 */     this.changeListener = null;
/*      */ 
/*  223 */     this.actionListener = null;
/*      */ 
/*  227 */     this.itemListener = null;
/*      */ 
/*  237 */     this.hideActionText = false;
/*      */   }
/*      */ 
/*      */   public void setHideActionText(boolean paramBoolean)
/*      */   {
/*  259 */     if (paramBoolean != this.hideActionText) {
/*  260 */       this.hideActionText = paramBoolean;
/*  261 */       if (getAction() != null) {
/*  262 */         setTextFromAction(getAction(), false);
/*      */       }
/*  264 */       firePropertyChange("hideActionText", !paramBoolean, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getHideActionText()
/*      */   {
/*  281 */     return this.hideActionText;
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */   {
/*  290 */     return this.text;
/*      */   }
/*      */ 
/*      */   public void setText(String paramString)
/*      */   {
/*  304 */     String str = this.text;
/*  305 */     this.text = paramString;
/*  306 */     firePropertyChange("text", str, paramString);
/*  307 */     updateDisplayedMnemonicIndex(paramString, getMnemonic());
/*      */ 
/*  309 */     if (this.accessibleContext != null) {
/*  310 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", str, paramString);
/*      */     }
/*      */ 
/*  314 */     if ((paramString == null) || (str == null) || (!paramString.equals(str))) {
/*  315 */       revalidate();
/*  316 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isSelected()
/*      */   {
/*  327 */     return this.model.isSelected();
/*      */   }
/*      */ 
/*      */   public void setSelected(boolean paramBoolean)
/*      */   {
/*  338 */     boolean bool = isSelected();
/*      */ 
/*  348 */     this.model.setSelected(paramBoolean);
/*      */   }
/*      */ 
/*      */   public void doClick()
/*      */   {
/*  356 */     doClick(68);
/*      */   }
/*      */ 
/*      */   public void doClick(int paramInt)
/*      */   {
/*  368 */     Dimension localDimension = getSize();
/*  369 */     this.model.setArmed(true);
/*  370 */     this.model.setPressed(true);
/*  371 */     paintImmediately(new Rectangle(0, 0, localDimension.width, localDimension.height));
/*      */     try {
/*  373 */       Thread.currentThread(); Thread.sleep(paramInt);
/*      */     } catch (InterruptedException localInterruptedException) {
/*      */     }
/*  376 */     this.model.setPressed(false);
/*  377 */     this.model.setArmed(false);
/*      */   }
/*      */ 
/*      */   public void setMargin(Insets paramInsets)
/*      */   {
/*  399 */     if ((paramInsets instanceof UIResource))
/*  400 */       this.defaultMargin = paramInsets;
/*  401 */     else if ((this.margin instanceof UIResource)) {
/*  402 */       this.defaultMargin = this.margin;
/*      */     }
/*      */ 
/*  407 */     if ((paramInsets == null) && (this.defaultMargin != null)) {
/*  408 */       paramInsets = this.defaultMargin;
/*      */     }
/*      */ 
/*  411 */     Insets localInsets = this.margin;
/*  412 */     this.margin = paramInsets;
/*  413 */     firePropertyChange("margin", localInsets, paramInsets);
/*  414 */     if ((localInsets == null) || (!localInsets.equals(paramInsets))) {
/*  415 */       revalidate();
/*  416 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Insets getMargin()
/*      */   {
/*  429 */     return this.margin == null ? null : (Insets)this.margin.clone();
/*      */   }
/*      */ 
/*      */   public Icon getIcon()
/*      */   {
/*  438 */     return this.defaultIcon;
/*      */   }
/*      */ 
/*      */   public void setIcon(Icon paramIcon)
/*      */   {
/*  455 */     Icon localIcon = this.defaultIcon;
/*  456 */     this.defaultIcon = paramIcon;
/*      */ 
/*  463 */     if ((paramIcon != localIcon) && ((this.disabledIcon instanceof UIResource))) {
/*  464 */       this.disabledIcon = null;
/*      */     }
/*      */ 
/*  467 */     firePropertyChange("icon", localIcon, paramIcon);
/*  468 */     if (this.accessibleContext != null) {
/*  469 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  473 */     if (paramIcon != localIcon) {
/*  474 */       if ((paramIcon == null) || (localIcon == null) || (paramIcon.getIconWidth() != localIcon.getIconWidth()) || (paramIcon.getIconHeight() != localIcon.getIconHeight()))
/*      */       {
/*  477 */         revalidate();
/*      */       }
/*  479 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Icon getPressedIcon()
/*      */   {
/*  489 */     return this.pressedIcon;
/*      */   }
/*      */ 
/*      */   public void setPressedIcon(Icon paramIcon)
/*      */   {
/*  502 */     Icon localIcon = this.pressedIcon;
/*  503 */     this.pressedIcon = paramIcon;
/*  504 */     firePropertyChange("pressedIcon", localIcon, paramIcon);
/*  505 */     if (this.accessibleContext != null) {
/*  506 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  510 */     if ((paramIcon != localIcon) && 
/*  511 */       (getModel().isPressed()))
/*  512 */       repaint();
/*      */   }
/*      */ 
/*      */   public Icon getSelectedIcon()
/*      */   {
/*  523 */     return this.selectedIcon;
/*      */   }
/*      */ 
/*      */   public void setSelectedIcon(Icon paramIcon)
/*      */   {
/*  536 */     Icon localIcon = this.selectedIcon;
/*  537 */     this.selectedIcon = paramIcon;
/*      */ 
/*  544 */     if ((paramIcon != localIcon) && ((this.disabledSelectedIcon instanceof UIResource)))
/*      */     {
/*  547 */       this.disabledSelectedIcon = null;
/*      */     }
/*      */ 
/*  550 */     firePropertyChange("selectedIcon", localIcon, paramIcon);
/*  551 */     if (this.accessibleContext != null) {
/*  552 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  556 */     if ((paramIcon != localIcon) && 
/*  557 */       (isSelected()))
/*  558 */       repaint();
/*      */   }
/*      */ 
/*      */   public Icon getRolloverIcon()
/*      */   {
/*  569 */     return this.rolloverIcon;
/*      */   }
/*      */ 
/*      */   public void setRolloverIcon(Icon paramIcon)
/*      */   {
/*  582 */     Icon localIcon = this.rolloverIcon;
/*  583 */     this.rolloverIcon = paramIcon;
/*  584 */     firePropertyChange("rolloverIcon", localIcon, paramIcon);
/*  585 */     if (this.accessibleContext != null) {
/*  586 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  590 */     setRolloverEnabled(true);
/*  591 */     if (paramIcon != localIcon)
/*      */     {
/*  594 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Icon getRolloverSelectedIcon()
/*      */   {
/*  605 */     return this.rolloverSelectedIcon;
/*      */   }
/*      */ 
/*      */   public void setRolloverSelectedIcon(Icon paramIcon)
/*      */   {
/*  619 */     Icon localIcon = this.rolloverSelectedIcon;
/*  620 */     this.rolloverSelectedIcon = paramIcon;
/*  621 */     firePropertyChange("rolloverSelectedIcon", localIcon, paramIcon);
/*  622 */     if (this.accessibleContext != null) {
/*  623 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  627 */     setRolloverEnabled(true);
/*  628 */     if (paramIcon != localIcon)
/*      */     {
/*  631 */       if (isSelected())
/*  632 */         repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Icon getDisabledIcon()
/*      */   {
/*  652 */     if (this.disabledIcon == null) {
/*  653 */       this.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, getIcon());
/*  654 */       if (this.disabledIcon != null) {
/*  655 */         firePropertyChange("disabledIcon", null, this.disabledIcon);
/*      */       }
/*      */     }
/*  658 */     return this.disabledIcon;
/*      */   }
/*      */ 
/*      */   public void setDisabledIcon(Icon paramIcon)
/*      */   {
/*  671 */     Icon localIcon = this.disabledIcon;
/*  672 */     this.disabledIcon = paramIcon;
/*  673 */     firePropertyChange("disabledIcon", localIcon, paramIcon);
/*  674 */     if (this.accessibleContext != null) {
/*  675 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  679 */     if ((paramIcon != localIcon) && 
/*  680 */       (!isEnabled()))
/*  681 */       repaint();
/*      */   }
/*      */ 
/*      */   public Icon getDisabledSelectedIcon()
/*      */   {
/*  702 */     if (this.disabledSelectedIcon == null) {
/*  703 */       if (this.selectedIcon != null) {
/*  704 */         this.disabledSelectedIcon = UIManager.getLookAndFeel().getDisabledSelectedIcon(this, getSelectedIcon());
/*      */       }
/*      */       else {
/*  707 */         return getDisabledIcon();
/*      */       }
/*      */     }
/*  710 */     return this.disabledSelectedIcon;
/*      */   }
/*      */ 
/*      */   public void setDisabledSelectedIcon(Icon paramIcon)
/*      */   {
/*  724 */     Icon localIcon = this.disabledSelectedIcon;
/*  725 */     this.disabledSelectedIcon = paramIcon;
/*  726 */     firePropertyChange("disabledSelectedIcon", localIcon, paramIcon);
/*  727 */     if (this.accessibleContext != null) {
/*  728 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */     }
/*      */ 
/*  732 */     if (paramIcon != localIcon) {
/*  733 */       if ((paramIcon == null) || (localIcon == null) || (paramIcon.getIconWidth() != localIcon.getIconWidth()) || (paramIcon.getIconHeight() != localIcon.getIconHeight()))
/*      */       {
/*  736 */         revalidate();
/*      */       }
/*  738 */       if ((!isEnabled()) && (isSelected()))
/*  739 */         repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getVerticalAlignment()
/*      */   {
/*  756 */     return this.verticalAlignment;
/*      */   }
/*      */ 
/*      */   public void setVerticalAlignment(int paramInt)
/*      */   {
/*  778 */     if (paramInt == this.verticalAlignment) return;
/*  779 */     int i = this.verticalAlignment;
/*  780 */     this.verticalAlignment = checkVerticalKey(paramInt, "verticalAlignment");
/*  781 */     firePropertyChange("verticalAlignment", i, this.verticalAlignment); repaint();
/*      */   }
/*      */ 
/*      */   public int getHorizontalAlignment()
/*      */   {
/*  800 */     return this.horizontalAlignment;
/*      */   }
/*      */ 
/*      */   public void setHorizontalAlignment(int paramInt)
/*      */   {
/*  829 */     if (paramInt == this.horizontalAlignment) return;
/*  830 */     int i = this.horizontalAlignment;
/*  831 */     this.horizontalAlignment = checkHorizontalKey(paramInt, "horizontalAlignment");
/*      */ 
/*  833 */     firePropertyChange("horizontalAlignment", i, this.horizontalAlignment);
/*      */ 
/*  835 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getVerticalTextPosition()
/*      */   {
/*  850 */     return this.verticalTextPosition;
/*      */   }
/*      */ 
/*      */   public void setVerticalTextPosition(int paramInt)
/*      */   {
/*  870 */     if (paramInt == this.verticalTextPosition) return;
/*  871 */     int i = this.verticalTextPosition;
/*  872 */     this.verticalTextPosition = checkVerticalKey(paramInt, "verticalTextPosition");
/*  873 */     firePropertyChange("verticalTextPosition", i, this.verticalTextPosition);
/*  874 */     revalidate();
/*  875 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getHorizontalTextPosition()
/*      */   {
/*  891 */     return this.horizontalTextPosition;
/*      */   }
/*      */ 
/*      */   public void setHorizontalTextPosition(int paramInt)
/*      */   {
/*  917 */     if (paramInt == this.horizontalTextPosition) return;
/*  918 */     int i = this.horizontalTextPosition;
/*  919 */     this.horizontalTextPosition = checkHorizontalKey(paramInt, "horizontalTextPosition");
/*      */ 
/*  921 */     firePropertyChange("horizontalTextPosition", i, this.horizontalTextPosition);
/*      */ 
/*  924 */     revalidate();
/*  925 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getIconTextGap()
/*      */   {
/*  938 */     return this.iconTextGap;
/*      */   }
/*      */ 
/*      */   public void setIconTextGap(int paramInt)
/*      */   {
/*  958 */     int i = this.iconTextGap;
/*  959 */     this.iconTextGap = paramInt;
/*  960 */     this.iconTextGapSet = true;
/*  961 */     firePropertyChange("iconTextGap", i, paramInt);
/*  962 */     if (paramInt != i) {
/*  963 */       revalidate();
/*  964 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int checkHorizontalKey(int paramInt, String paramString)
/*      */   {
/*  990 */     if ((paramInt == 2) || (paramInt == 0) || (paramInt == 4) || (paramInt == 10) || (paramInt == 11))
/*      */     {
/*  995 */       return paramInt;
/*      */     }
/*  997 */     throw new IllegalArgumentException(paramString);
/*      */   }
/*      */ 
/*      */   protected int checkVerticalKey(int paramInt, String paramString)
/*      */   {
/* 1018 */     if ((paramInt == 1) || (paramInt == 0) || (paramInt == 3)) {
/* 1019 */       return paramInt;
/*      */     }
/* 1021 */     throw new IllegalArgumentException(paramString);
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 1031 */     super.removeNotify();
/* 1032 */     if (isRolloverEnabled())
/* 1033 */       getModel().setRollover(false);
/*      */   }
/*      */ 
/*      */   public void setActionCommand(String paramString)
/*      */   {
/* 1042 */     getModel().setActionCommand(paramString);
/*      */   }
/*      */ 
/*      */   public String getActionCommand()
/*      */   {
/* 1050 */     String str = getModel().getActionCommand();
/* 1051 */     if (str == null) {
/* 1052 */       str = getText();
/*      */     }
/* 1054 */     return str;
/*      */   }
/*      */ 
/*      */   public void setAction(Action paramAction)
/*      */   {
/* 1099 */     Action localAction = getAction();
/* 1100 */     if ((this.action == null) || (!this.action.equals(paramAction))) {
/* 1101 */       this.action = paramAction;
/* 1102 */       if (localAction != null) {
/* 1103 */         removeActionListener(localAction);
/* 1104 */         localAction.removePropertyChangeListener(this.actionPropertyChangeListener);
/* 1105 */         this.actionPropertyChangeListener = null;
/*      */       }
/* 1107 */       configurePropertiesFromAction(this.action);
/* 1108 */       if (this.action != null)
/*      */       {
/* 1110 */         if (!isListener(ActionListener.class, this.action)) {
/* 1111 */           addActionListener(this.action);
/*      */         }
/*      */ 
/* 1114 */         this.actionPropertyChangeListener = createActionPropertyChangeListener(this.action);
/* 1115 */         this.action.addPropertyChangeListener(this.actionPropertyChangeListener);
/*      */       }
/* 1117 */       firePropertyChange("action", localAction, this.action);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isListener(Class paramClass, ActionListener paramActionListener) {
/* 1122 */     boolean bool = false;
/* 1123 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1124 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
/* 1125 */       if ((arrayOfObject[i] == paramClass) && (arrayOfObject[(i + 1)] == paramActionListener)) {
/* 1126 */         bool = true;
/*      */       }
/*      */     }
/* 1129 */     return bool;
/*      */   }
/*      */ 
/*      */   public Action getAction()
/*      */   {
/* 1144 */     return this.action;
/*      */   }
/*      */ 
/*      */   protected void configurePropertiesFromAction(Action paramAction)
/*      */   {
/* 1160 */     setMnemonicFromAction(paramAction);
/* 1161 */     setTextFromAction(paramAction, false);
/* 1162 */     AbstractAction.setToolTipTextFromAction(this, paramAction);
/* 1163 */     setIconFromAction(paramAction);
/* 1164 */     setActionCommandFromAction(paramAction);
/* 1165 */     AbstractAction.setEnabledFromAction(this, paramAction);
/* 1166 */     if ((AbstractAction.hasSelectedKey(paramAction)) && (shouldUpdateSelectedStateFromAction()))
/*      */     {
/* 1168 */       setSelectedFromAction(paramAction);
/*      */     }
/* 1170 */     setDisplayedMnemonicIndexFromAction(paramAction, false);
/*      */   }
/*      */ 
/*      */   void clientPropertyChanged(Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1175 */     if (paramObject1 == "hideActionText") {
/* 1176 */       boolean bool = (paramObject3 instanceof Boolean) ? ((Boolean)paramObject3).booleanValue() : false;
/*      */ 
/* 1178 */       if (getHideActionText() != bool)
/* 1179 */         setHideActionText(bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean shouldUpdateSelectedStateFromAction()
/*      */   {
/* 1190 */     return false;
/*      */   }
/*      */ 
/*      */   protected void actionPropertyChanged(Action paramAction, String paramString)
/*      */   {
/* 1213 */     if (paramString == "Name")
/* 1214 */       setTextFromAction(paramAction, true);
/* 1215 */     else if (paramString == "enabled")
/* 1216 */       AbstractAction.setEnabledFromAction(this, paramAction);
/* 1217 */     else if (paramString == "ShortDescription")
/* 1218 */       AbstractAction.setToolTipTextFromAction(this, paramAction);
/* 1219 */     else if (paramString == "SmallIcon")
/* 1220 */       smallIconChanged(paramAction);
/* 1221 */     else if (paramString == "MnemonicKey")
/* 1222 */       setMnemonicFromAction(paramAction);
/* 1223 */     else if (paramString == "ActionCommandKey")
/* 1224 */       setActionCommandFromAction(paramAction);
/* 1225 */     else if ((paramString == "SwingSelectedKey") && (AbstractAction.hasSelectedKey(paramAction)) && (shouldUpdateSelectedStateFromAction()))
/*      */     {
/* 1228 */       setSelectedFromAction(paramAction);
/* 1229 */     } else if (paramString == "SwingDisplayedMnemonicIndexKey")
/* 1230 */       setDisplayedMnemonicIndexFromAction(paramAction, true);
/* 1231 */     else if (paramString == "SwingLargeIconKey")
/* 1232 */       largeIconChanged(paramAction);
/*      */   }
/*      */ 
/*      */   private void setDisplayedMnemonicIndexFromAction(Action paramAction, boolean paramBoolean)
/*      */   {
/* 1238 */     Integer localInteger = paramAction == null ? null : (Integer)paramAction.getValue("SwingDisplayedMnemonicIndexKey");
/*      */ 
/* 1240 */     if ((paramBoolean) || (localInteger != null))
/*      */     {
/*      */       int i;
/* 1242 */       if (localInteger == null) {
/* 1243 */         i = -1;
/*      */       } else {
/* 1245 */         i = localInteger.intValue();
/* 1246 */         String str = getText();
/* 1247 */         if ((str == null) || (i >= str.length())) {
/* 1248 */           i = -1;
/*      */         }
/*      */       }
/* 1251 */       setDisplayedMnemonicIndex(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setMnemonicFromAction(Action paramAction) {
/* 1256 */     Integer localInteger = paramAction == null ? null : (Integer)paramAction.getValue("MnemonicKey");
/*      */ 
/* 1258 */     setMnemonic(localInteger == null ? 0 : localInteger.intValue());
/*      */   }
/*      */ 
/*      */   private void setTextFromAction(Action paramAction, boolean paramBoolean) {
/* 1262 */     boolean bool = getHideActionText();
/* 1263 */     if (!paramBoolean) {
/* 1264 */       setText((paramAction != null) && (!bool) ? (String)paramAction.getValue("Name") : null);
/*      */     }
/* 1267 */     else if (!bool)
/* 1268 */       setText((String)paramAction.getValue("Name"));
/*      */   }
/*      */ 
/*      */   void setIconFromAction(Action paramAction)
/*      */   {
/* 1273 */     Icon localIcon = null;
/* 1274 */     if (paramAction != null) {
/* 1275 */       localIcon = (Icon)paramAction.getValue("SwingLargeIconKey");
/* 1276 */       if (localIcon == null) {
/* 1277 */         localIcon = (Icon)paramAction.getValue("SmallIcon");
/*      */       }
/*      */     }
/* 1280 */     setIcon(localIcon);
/*      */   }
/*      */ 
/*      */   void smallIconChanged(Action paramAction) {
/* 1284 */     if (paramAction.getValue("SwingLargeIconKey") == null)
/* 1285 */       setIconFromAction(paramAction);
/*      */   }
/*      */ 
/*      */   void largeIconChanged(Action paramAction)
/*      */   {
/* 1290 */     setIconFromAction(paramAction);
/*      */   }
/*      */ 
/*      */   private void setActionCommandFromAction(Action paramAction) {
/* 1294 */     setActionCommand(paramAction != null ? (String)paramAction.getValue("ActionCommandKey") : null);
/*      */   }
/*      */ 
/*      */   private void setSelectedFromAction(Action paramAction)
/*      */   {
/* 1307 */     boolean bool = false;
/* 1308 */     if (paramAction != null) {
/* 1309 */       bool = AbstractAction.isSelected(paramAction);
/*      */     }
/* 1311 */     if (bool != isSelected())
/*      */     {
/* 1314 */       setSelected(bool);
/*      */ 
/* 1316 */       if ((!bool) && (isSelected()) && 
/* 1317 */         ((getModel() instanceof DefaultButtonModel))) {
/* 1318 */         ButtonGroup localButtonGroup = ((DefaultButtonModel)getModel()).getGroup();
/* 1319 */         if (localButtonGroup != null)
/* 1320 */           localButtonGroup.clearSelection();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createActionPropertyChangeListener(Action paramAction)
/*      */   {
/* 1342 */     return createActionPropertyChangeListener0(paramAction);
/*      */   }
/*      */ 
/*      */   PropertyChangeListener createActionPropertyChangeListener0(Action paramAction)
/*      */   {
/* 1347 */     return new ButtonActionPropertyChangeListener(this, paramAction);
/*      */   }
/*      */ 
/*      */   public boolean isBorderPainted()
/*      */   {
/* 1373 */     return this.paintBorder;
/*      */   }
/*      */ 
/*      */   public void setBorderPainted(boolean paramBoolean)
/*      */   {
/* 1395 */     boolean bool = this.paintBorder;
/* 1396 */     this.paintBorder = paramBoolean;
/* 1397 */     this.borderPaintedSet = true;
/* 1398 */     firePropertyChange("borderPainted", bool, this.paintBorder);
/* 1399 */     if (paramBoolean != bool) {
/* 1400 */       revalidate();
/* 1401 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintBorder(Graphics paramGraphics)
/*      */   {
/* 1414 */     if (isBorderPainted())
/* 1415 */       super.paintBorder(paramGraphics);
/*      */   }
/*      */ 
/*      */   public boolean isFocusPainted()
/*      */   {
/* 1426 */     return this.paintFocus;
/*      */   }
/*      */ 
/*      */   public void setFocusPainted(boolean paramBoolean)
/*      */   {
/* 1445 */     boolean bool = this.paintFocus;
/* 1446 */     this.paintFocus = paramBoolean;
/* 1447 */     firePropertyChange("focusPainted", bool, this.paintFocus);
/* 1448 */     if ((paramBoolean != bool) && (isFocusOwner())) {
/* 1449 */       revalidate();
/* 1450 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isContentAreaFilled()
/*      */   {
/* 1461 */     return this.contentAreaFilled;
/*      */   }
/*      */ 
/*      */   public void setContentAreaFilled(boolean paramBoolean)
/*      */   {
/* 1489 */     boolean bool = this.contentAreaFilled;
/* 1490 */     this.contentAreaFilled = paramBoolean;
/* 1491 */     this.contentAreaFilledSet = true;
/* 1492 */     firePropertyChange("contentAreaFilled", bool, this.contentAreaFilled);
/* 1493 */     if (paramBoolean != bool)
/* 1494 */       repaint();
/*      */   }
/*      */ 
/*      */   public boolean isRolloverEnabled()
/*      */   {
/* 1505 */     return this.rolloverEnabled;
/*      */   }
/*      */ 
/*      */   public void setRolloverEnabled(boolean paramBoolean)
/*      */   {
/* 1524 */     boolean bool = this.rolloverEnabled;
/* 1525 */     this.rolloverEnabled = paramBoolean;
/* 1526 */     this.rolloverEnabledSet = true;
/* 1527 */     firePropertyChange("rolloverEnabled", bool, this.rolloverEnabled);
/* 1528 */     if (paramBoolean != bool)
/* 1529 */       repaint();
/*      */   }
/*      */ 
/*      */   public int getMnemonic()
/*      */   {
/* 1538 */     return this.mnemonic;
/*      */   }
/*      */ 
/*      */   public void setMnemonic(int paramInt)
/*      */   {
/* 1572 */     int i = getMnemonic();
/* 1573 */     this.model.setMnemonic(paramInt);
/* 1574 */     updateMnemonicProperties();
/*      */   }
/*      */ 
/*      */   public void setMnemonic(char paramChar)
/*      */   {
/* 1591 */     int i = paramChar;
/* 1592 */     if ((i >= 97) && (i <= 122))
/* 1593 */       i -= 32;
/* 1594 */     setMnemonic(i);
/*      */   }
/*      */ 
/*      */   public void setDisplayedMnemonicIndex(int paramInt)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1627 */     int i = this.mnemonicIndex;
/* 1628 */     if (paramInt == -1) {
/* 1629 */       this.mnemonicIndex = -1;
/*      */     } else {
/* 1631 */       String str = getText();
/* 1632 */       int j = str == null ? 0 : str.length();
/* 1633 */       if ((paramInt < -1) || (paramInt >= j)) {
/* 1634 */         throw new IllegalArgumentException("index == " + paramInt);
/*      */       }
/*      */     }
/* 1637 */     this.mnemonicIndex = paramInt;
/* 1638 */     firePropertyChange("displayedMnemonicIndex", i, paramInt);
/* 1639 */     if (paramInt != i) {
/* 1640 */       revalidate();
/* 1641 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getDisplayedMnemonicIndex()
/*      */   {
/* 1654 */     return this.mnemonicIndex;
/*      */   }
/*      */ 
/*      */   private void updateDisplayedMnemonicIndex(String paramString, int paramInt)
/*      */   {
/* 1664 */     setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(paramString, paramInt));
/*      */   }
/*      */ 
/*      */   private void updateMnemonicProperties()
/*      */   {
/* 1674 */     int i = this.model.getMnemonic();
/* 1675 */     if (this.mnemonic != i) {
/* 1676 */       int j = this.mnemonic;
/* 1677 */       this.mnemonic = i;
/* 1678 */       firePropertyChange("mnemonic", j, this.mnemonic);
/*      */ 
/* 1680 */       updateDisplayedMnemonicIndex(getText(), this.mnemonic);
/* 1681 */       revalidate();
/* 1682 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMultiClickThreshhold(long paramLong)
/*      */   {
/* 1706 */     if (paramLong < 0L) {
/* 1707 */       throw new IllegalArgumentException("threshhold must be >= 0");
/*      */     }
/* 1709 */     this.multiClickThreshhold = paramLong;
/*      */   }
/*      */ 
/*      */   public long getMultiClickThreshhold()
/*      */   {
/* 1723 */     return this.multiClickThreshhold;
/*      */   }
/*      */ 
/*      */   public ButtonModel getModel()
/*      */   {
/* 1732 */     return this.model;
/*      */   }
/*      */ 
/*      */   public void setModel(ButtonModel paramButtonModel)
/*      */   {
/* 1745 */     ButtonModel localButtonModel = getModel();
/*      */ 
/* 1747 */     if (localButtonModel != null) {
/* 1748 */       localButtonModel.removeChangeListener(this.changeListener);
/* 1749 */       localButtonModel.removeActionListener(this.actionListener);
/* 1750 */       localButtonModel.removeItemListener(this.itemListener);
/* 1751 */       this.changeListener = null;
/* 1752 */       this.actionListener = null;
/* 1753 */       this.itemListener = null;
/*      */     }
/*      */ 
/* 1756 */     this.model = paramButtonModel;
/*      */ 
/* 1758 */     if (paramButtonModel != null) {
/* 1759 */       this.changeListener = createChangeListener();
/* 1760 */       this.actionListener = createActionListener();
/* 1761 */       this.itemListener = createItemListener();
/* 1762 */       paramButtonModel.addChangeListener(this.changeListener);
/* 1763 */       paramButtonModel.addActionListener(this.actionListener);
/* 1764 */       paramButtonModel.addItemListener(this.itemListener);
/*      */ 
/* 1766 */       updateMnemonicProperties();
/*      */ 
/* 1770 */       super.setEnabled(paramButtonModel.isEnabled());
/*      */     }
/*      */     else {
/* 1773 */       this.mnemonic = 0;
/*      */     }
/*      */ 
/* 1776 */     updateDisplayedMnemonicIndex(getText(), this.mnemonic);
/*      */ 
/* 1778 */     firePropertyChange("model", localButtonModel, paramButtonModel);
/* 1779 */     if (paramButtonModel != localButtonModel) {
/* 1780 */       revalidate();
/* 1781 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ButtonUI getUI()
/*      */   {
/* 1792 */     return (ButtonUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(ButtonUI paramButtonUI)
/*      */   {
/* 1807 */     super.setUI(paramButtonUI);
/*      */ 
/* 1809 */     if ((this.disabledIcon instanceof UIResource)) {
/* 1810 */       setDisabledIcon(null);
/*      */     }
/* 1812 */     if ((this.disabledSelectedIcon instanceof UIResource))
/* 1813 */       setDisabledSelectedIcon(null);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/* 1850 */     if (!this.setLayout) {
/* 1851 */       setLayout(new OverlayLayout(this));
/*      */     }
/* 1853 */     super.addImpl(paramComponent, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void setLayout(LayoutManager paramLayoutManager)
/*      */   {
/* 1865 */     this.setLayout = true;
/* 1866 */     super.setLayout(paramLayoutManager);
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/* 1874 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/* 1882 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/* 1894 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/* 1905 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 1908 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1909 */       if (arrayOfObject[i] == ChangeListener.class)
/*      */       {
/* 1911 */         if (this.changeEvent == null)
/* 1912 */           this.changeEvent = new ChangeEvent(this);
/* 1913 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addActionListener(ActionListener paramActionListener)
/*      */   {
/* 1923 */     this.listenerList.add(ActionListener.class, paramActionListener);
/*      */   }
/*      */ 
/*      */   public void removeActionListener(ActionListener paramActionListener)
/*      */   {
/* 1935 */     if ((paramActionListener != null) && (getAction() == paramActionListener))
/* 1936 */       setAction(null);
/*      */     else
/* 1938 */       this.listenerList.remove(ActionListener.class, paramActionListener);
/*      */   }
/*      */ 
/*      */   public ActionListener[] getActionListeners()
/*      */   {
/* 1951 */     return (ActionListener[])this.listenerList.getListeners(ActionListener.class);
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener()
/*      */   {
/* 1962 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected void fireActionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 2000 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2001 */     ActionEvent localActionEvent = null;
/*      */ 
/* 2004 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 2005 */       if (arrayOfObject[i] == ActionListener.class)
/*      */       {
/* 2007 */         if (localActionEvent == null) {
/* 2008 */           String str = paramActionEvent.getActionCommand();
/* 2009 */           if (str == null) {
/* 2010 */             str = getActionCommand();
/*      */           }
/* 2012 */           localActionEvent = new ActionEvent(this, 1001, str, paramActionEvent.getWhen(), paramActionEvent.getModifiers());
/*      */         }
/*      */ 
/* 2018 */         ((ActionListener)arrayOfObject[(i + 1)]).actionPerformed(localActionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void fireItemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/* 2033 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2034 */     ItemEvent localItemEvent = null;
/*      */ 
/* 2037 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
/* 2038 */       if (arrayOfObject[i] == ItemListener.class)
/*      */       {
/* 2040 */         if (localItemEvent == null) {
/* 2041 */           localItemEvent = new ItemEvent(this, 701, this, paramItemEvent.getStateChange());
/*      */         }
/*      */ 
/* 2046 */         ((ItemListener)arrayOfObject[(i + 1)]).itemStateChanged(localItemEvent);
/*      */       }
/*      */     }
/* 2049 */     if (this.accessibleContext != null)
/* 2050 */       if (paramItemEvent.getStateChange() == 1) {
/* 2051 */         this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.SELECTED);
/*      */ 
/* 2054 */         this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(0), Integer.valueOf(1));
/*      */       }
/*      */       else
/*      */       {
/* 2058 */         this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.SELECTED, null);
/*      */ 
/* 2061 */         this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(1), Integer.valueOf(0));
/*      */       }
/*      */   }
/*      */ 
/*      */   protected ActionListener createActionListener()
/*      */   {
/* 2070 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ItemListener createItemListener()
/*      */   {
/* 2075 */     return getHandler();
/*      */   }
/*      */ 
/*      */   public void setEnabled(boolean paramBoolean)
/*      */   {
/* 2084 */     if ((!paramBoolean) && (this.model.isRollover())) {
/* 2085 */       this.model.setRollover(false);
/*      */     }
/* 2087 */     super.setEnabled(paramBoolean);
/* 2088 */     this.model.setEnabled(paramBoolean);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String getLabel()
/*      */   {
/* 2101 */     return getText();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setLabel(String paramString)
/*      */   {
/* 2115 */     setText(paramString);
/*      */   }
/*      */ 
/*      */   public void addItemListener(ItemListener paramItemListener)
/*      */   {
/* 2123 */     this.listenerList.add(ItemListener.class, paramItemListener);
/*      */   }
/*      */ 
/*      */   public void removeItemListener(ItemListener paramItemListener)
/*      */   {
/* 2131 */     this.listenerList.remove(ItemListener.class, paramItemListener);
/*      */   }
/*      */ 
/*      */   public ItemListener[] getItemListeners()
/*      */   {
/* 2143 */     return (ItemListener[])this.listenerList.getListeners(ItemListener.class);
/*      */   }
/*      */ 
/*      */   public Object[] getSelectedObjects()
/*      */   {
/* 2154 */     if (!isSelected()) {
/* 2155 */       return null;
/*      */     }
/* 2157 */     Object[] arrayOfObject = new Object[1];
/* 2158 */     arrayOfObject[0] = getText();
/* 2159 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   protected void init(String paramString, Icon paramIcon) {
/* 2163 */     if (paramString != null) {
/* 2164 */       setText(paramString);
/*      */     }
/*      */ 
/* 2167 */     if (paramIcon != null) {
/* 2168 */       setIcon(paramIcon);
/*      */     }
/*      */ 
/* 2172 */     updateUI();
/*      */ 
/* 2174 */     setAlignmentX(0.0F);
/* 2175 */     setAlignmentY(0.5F);
/*      */   }
/*      */ 
/*      */   public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2196 */     Icon localIcon = getIcon();
/* 2197 */     if (localIcon == null) {
/* 2198 */       return false;
/*      */     }
/*      */ 
/* 2201 */     if (!this.model.isEnabled()) {
/* 2202 */       if (this.model.isSelected())
/* 2203 */         localIcon = getDisabledSelectedIcon();
/*      */       else
/* 2205 */         localIcon = getDisabledIcon();
/*      */     }
/* 2207 */     else if ((this.model.isPressed()) && (this.model.isArmed()))
/* 2208 */       localIcon = getPressedIcon();
/* 2209 */     else if ((isRolloverEnabled()) && (this.model.isRollover())) {
/* 2210 */       if (this.model.isSelected())
/* 2211 */         localIcon = getRolloverSelectedIcon();
/*      */       else
/* 2213 */         localIcon = getRolloverIcon();
/*      */     }
/* 2215 */     else if (this.model.isSelected()) {
/* 2216 */       localIcon = getSelectedIcon();
/*      */     }
/*      */ 
/* 2219 */     if (!SwingUtilities.doesIconReferenceImage(localIcon, paramImage))
/*      */     {
/* 2222 */       return false;
/*      */     }
/* 2224 */     return super.imageUpdate(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   void setUIProperty(String paramString, Object paramObject) {
/* 2228 */     if (paramString == "borderPainted") {
/* 2229 */       if (!this.borderPaintedSet) {
/* 2230 */         setBorderPainted(((Boolean)paramObject).booleanValue());
/* 2231 */         this.borderPaintedSet = false;
/*      */       }
/* 2233 */     } else if (paramString == "rolloverEnabled") {
/* 2234 */       if (!this.rolloverEnabledSet) {
/* 2235 */         setRolloverEnabled(((Boolean)paramObject).booleanValue());
/* 2236 */         this.rolloverEnabledSet = false;
/*      */       }
/* 2238 */     } else if (paramString == "iconTextGap") {
/* 2239 */       if (!this.iconTextGapSet) {
/* 2240 */         setIconTextGap(((Number)paramObject).intValue());
/* 2241 */         this.iconTextGapSet = false;
/*      */       }
/* 2243 */     } else if (paramString == "contentAreaFilled") {
/* 2244 */       if (!this.contentAreaFilledSet) {
/* 2245 */         setContentAreaFilled(((Boolean)paramObject).booleanValue());
/* 2246 */         this.contentAreaFilledSet = false;
/*      */       }
/*      */     }
/* 2249 */     else super.setUIProperty(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 2267 */     String str1 = (this.defaultIcon != null) && (this.defaultIcon != this) ? this.defaultIcon.toString() : "";
/*      */ 
/* 2270 */     String str2 = (this.pressedIcon != null) && (this.pressedIcon != this) ? this.pressedIcon.toString() : "";
/*      */ 
/* 2273 */     String str3 = (this.disabledIcon != null) && (this.disabledIcon != this) ? this.disabledIcon.toString() : "";
/*      */ 
/* 2276 */     String str4 = (this.selectedIcon != null) && (this.selectedIcon != this) ? this.selectedIcon.toString() : "";
/*      */ 
/* 2279 */     String str5 = (this.disabledSelectedIcon != null) && (this.disabledSelectedIcon != this) ? this.disabledSelectedIcon.toString() : "";
/*      */ 
/* 2283 */     String str6 = (this.rolloverIcon != null) && (this.rolloverIcon != this) ? this.rolloverIcon.toString() : "";
/*      */ 
/* 2286 */     String str7 = (this.rolloverSelectedIcon != null) && (this.rolloverSelectedIcon != this) ? this.rolloverSelectedIcon.toString() : "";
/*      */ 
/* 2290 */     String str8 = this.paintBorder ? "true" : "false";
/* 2291 */     String str9 = this.paintFocus ? "true" : "false";
/* 2292 */     String str10 = this.rolloverEnabled ? "true" : "false";
/*      */ 
/* 2294 */     return super.paramString() + ",defaultIcon=" + str1 + ",disabledIcon=" + str3 + ",disabledSelectedIcon=" + str5 + ",margin=" + this.margin + ",paintBorder=" + str8 + ",paintFocus=" + str9 + ",pressedIcon=" + str2 + ",rolloverEnabled=" + str10 + ",rolloverIcon=" + str6 + ",rolloverSelectedIcon=" + str7 + ",selectedIcon=" + str4 + ",text=" + this.text;
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/* 2311 */     if (this.handler == null) {
/* 2312 */       this.handler = new Handler();
/*      */     }
/* 2314 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected abstract class AccessibleAbstractButton extends JComponent.AccessibleJComponent
/*      */     implements AccessibleAction, AccessibleValue, AccessibleText, AccessibleExtendedComponent
/*      */   {
/*      */     protected AccessibleAbstractButton()
/*      */     {
/* 2382 */       super();
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/* 2394 */       String str = this.accessibleName;
/*      */ 
/* 2396 */       if (str == null) {
/* 2397 */         str = (String)AbstractButton.this.getClientProperty("AccessibleName");
/*      */       }
/* 2399 */       if (str == null) {
/* 2400 */         str = AbstractButton.this.getText();
/*      */       }
/* 2402 */       if (str == null) {
/* 2403 */         str = super.getAccessibleName();
/*      */       }
/* 2405 */       return str;
/*      */     }
/*      */ 
/*      */     public AccessibleIcon[] getAccessibleIcon()
/*      */     {
/* 2414 */       Icon localIcon = AbstractButton.this.getIcon();
/*      */ 
/* 2416 */       if ((localIcon instanceof Accessible)) {
/* 2417 */         AccessibleContext localAccessibleContext = ((Accessible)localIcon).getAccessibleContext();
/*      */ 
/* 2419 */         if ((localAccessibleContext != null) && ((localAccessibleContext instanceof AccessibleIcon))) {
/* 2420 */           return new AccessibleIcon[] { (AccessibleIcon)localAccessibleContext };
/*      */         }
/*      */       }
/* 2423 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 2434 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 2435 */       if (AbstractButton.this.getModel().isArmed()) {
/* 2436 */         localAccessibleStateSet.add(AccessibleState.ARMED);
/*      */       }
/* 2438 */       if (AbstractButton.this.isFocusOwner()) {
/* 2439 */         localAccessibleStateSet.add(AccessibleState.FOCUSED);
/*      */       }
/* 2441 */       if (AbstractButton.this.getModel().isPressed()) {
/* 2442 */         localAccessibleStateSet.add(AccessibleState.PRESSED);
/*      */       }
/* 2444 */       if (AbstractButton.this.isSelected()) {
/* 2445 */         localAccessibleStateSet.add(AccessibleState.CHECKED);
/*      */       }
/* 2447 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRelationSet getAccessibleRelationSet()
/*      */     {
/* 2460 */       AccessibleRelationSet localAccessibleRelationSet = super.getAccessibleRelationSet();
/*      */ 
/* 2463 */       if (!localAccessibleRelationSet.contains(AccessibleRelation.MEMBER_OF))
/*      */       {
/* 2465 */         ButtonModel localButtonModel = AbstractButton.this.getModel();
/* 2466 */         if ((localButtonModel != null) && ((localButtonModel instanceof DefaultButtonModel))) {
/* 2467 */           ButtonGroup localButtonGroup = ((DefaultButtonModel)localButtonModel).getGroup();
/* 2468 */           if (localButtonGroup != null)
/*      */           {
/* 2471 */             int i = localButtonGroup.getButtonCount();
/* 2472 */             Object[] arrayOfObject = new Object[i];
/* 2473 */             Enumeration localEnumeration = localButtonGroup.getElements();
/* 2474 */             for (int j = 0; j < i; j++) {
/* 2475 */               if (localEnumeration.hasMoreElements()) {
/* 2476 */                 arrayOfObject[j] = localEnumeration.nextElement();
/*      */               }
/*      */             }
/* 2479 */             AccessibleRelation localAccessibleRelation = new AccessibleRelation(AccessibleRelation.MEMBER_OF);
/*      */ 
/* 2481 */             localAccessibleRelation.setTarget(arrayOfObject);
/* 2482 */             localAccessibleRelationSet.add(localAccessibleRelation);
/*      */           }
/*      */         }
/*      */       }
/* 2486 */       return localAccessibleRelationSet;
/*      */     }
/*      */ 
/*      */     public AccessibleAction getAccessibleAction()
/*      */     {
/* 2498 */       return this;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 2510 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleActionCount()
/*      */     {
/* 2521 */       return 1;
/*      */     }
/*      */ 
/*      */     public String getAccessibleActionDescription(int paramInt)
/*      */     {
/* 2530 */       if (paramInt == 0) {
/* 2531 */         return UIManager.getString("AbstractButton.clickText");
/*      */       }
/* 2533 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean doAccessibleAction(int paramInt)
/*      */     {
/* 2544 */       if (paramInt == 0) {
/* 2545 */         AbstractButton.this.doClick();
/* 2546 */         return true;
/*      */       }
/* 2548 */       return false;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 2560 */       if (AbstractButton.this.isSelected()) {
/* 2561 */         return Integer.valueOf(1);
/*      */       }
/* 2563 */       return Integer.valueOf(0);
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/* 2574 */       if (paramNumber == null) {
/* 2575 */         return false;
/*      */       }
/* 2577 */       int i = paramNumber.intValue();
/* 2578 */       if (i == 0)
/* 2579 */         AbstractButton.this.setSelected(false);
/*      */       else {
/* 2581 */         AbstractButton.this.setSelected(true);
/*      */       }
/* 2583 */       return true;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 2592 */       return Integer.valueOf(0);
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 2601 */       return Integer.valueOf(1);
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/* 2608 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2609 */       if (localView != null) {
/* 2610 */         return this;
/*      */       }
/* 2612 */       return null;
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/* 2632 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2633 */       if (localView != null) {
/* 2634 */         Rectangle localRectangle = getTextRectangle();
/* 2635 */         if (localRectangle == null) {
/* 2636 */           return -1;
/*      */         }
/* 2638 */         Rectangle2D.Float localFloat = new Rectangle2D.Float(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 2640 */         Position.Bias[] arrayOfBias = new Position.Bias[1];
/* 2641 */         return localView.viewToModel(paramPoint.x, paramPoint.y, localFloat, arrayOfBias);
/*      */       }
/* 2643 */       return -1;
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/* 2664 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2665 */       if (localView != null) {
/* 2666 */         Rectangle localRectangle = getTextRectangle();
/* 2667 */         if (localRectangle == null) {
/* 2668 */           return null;
/*      */         }
/* 2670 */         Rectangle2D.Float localFloat = new Rectangle2D.Float(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */         try
/*      */         {
/* 2673 */           Shape localShape = localView.modelToView(paramInt, localFloat, Position.Bias.Forward);
/*      */ 
/* 2675 */           return localShape.getBounds();
/*      */         } catch (BadLocationException localBadLocationException) {
/* 2677 */           return null;
/*      */         }
/*      */       }
/* 2680 */       return null;
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/* 2691 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2692 */       if (localView != null) {
/* 2693 */         Document localDocument = localView.getDocument();
/* 2694 */         if ((localDocument instanceof StyledDocument)) {
/* 2695 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 2696 */           return localStyledDocument.getLength();
/*      */         }
/*      */       }
/* 2699 */       return AbstractButton.this.accessibleContext.getAccessibleName().length();
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/* 2712 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2726 */       if ((paramInt2 < 0) || (paramInt2 >= getCharCount()))
/* 2727 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/* 2729 */       switch (paramInt1) {
/*      */       case 1:
/*      */         try {
/* 2732 */           return getText(paramInt2, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 2734 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 2738 */           String str1 = getText(0, getCharCount());
/* 2739 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 2740 */           localBreakIterator.setText(str1);
/* 2741 */           i = localBreakIterator.following(paramInt2);
/* 2742 */           return str1.substring(localBreakIterator.previous(), i);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 2744 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 2748 */           String str2 = getText(0, getCharCount());
/* 2749 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 2751 */           localBreakIterator.setText(str2);
/* 2752 */           i = localBreakIterator.following(paramInt2);
/* 2753 */           return str2.substring(localBreakIterator.previous(), i);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 2755 */           return null;
/*      */         }
/*      */       }
/* 2758 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2773 */       if ((paramInt2 < 0) || (paramInt2 >= getCharCount()))
/* 2774 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 2776 */       switch (paramInt1) {
/*      */       case 1:
/* 2778 */         if (paramInt2 + 1 >= getCharCount())
/* 2779 */           return null;
/*      */         try
/*      */         {
/* 2782 */           return getText(paramInt2 + 1, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 2784 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 2788 */           String str1 = getText(0, getCharCount());
/* 2789 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 2790 */           localBreakIterator.setText(str1);
/* 2791 */           i = localBreakIterator.following(paramInt2);
/* 2792 */           if ((i == -1) || (i >= str1.length())) {
/* 2793 */             return null;
/*      */           }
/* 2795 */           j = localBreakIterator.following(i);
/* 2796 */           if ((j == -1) || (j >= str1.length())) {
/* 2797 */             return null;
/*      */           }
/* 2799 */           return str1.substring(i, j);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 2801 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 2805 */           String str2 = getText(0, getCharCount());
/* 2806 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 2808 */           localBreakIterator.setText(str2);
/* 2809 */           i = localBreakIterator.following(paramInt2);
/* 2810 */           if ((i == -1) || (i > str2.length())) {
/* 2811 */             return null;
/*      */           }
/* 2813 */           j = localBreakIterator.following(i);
/* 2814 */           if ((j == -1) || (j > str2.length())) {
/* 2815 */             return null;
/*      */           }
/* 2817 */           return str2.substring(i, j);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 2819 */           return null;
/*      */         }
/*      */       }
/* 2822 */       return null;
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2837 */       if ((paramInt2 < 0) || (paramInt2 > getCharCount() - 1))
/* 2838 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 2840 */       switch (paramInt1) {
/*      */       case 1:
/* 2842 */         if (paramInt2 == 0)
/* 2843 */           return null;
/*      */         try
/*      */         {
/* 2846 */           return getText(paramInt2 - 1, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 2848 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 2852 */           String str1 = getText(0, getCharCount());
/* 2853 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 2854 */           localBreakIterator.setText(str1);
/* 2855 */           i = localBreakIterator.following(paramInt2);
/* 2856 */           i = localBreakIterator.previous();
/* 2857 */           j = localBreakIterator.previous();
/* 2858 */           if (j == -1) {
/* 2859 */             return null;
/*      */           }
/* 2861 */           return str1.substring(j, i);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 2863 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 2867 */           String str2 = getText(0, getCharCount());
/* 2868 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 2870 */           localBreakIterator.setText(str2);
/* 2871 */           i = localBreakIterator.following(paramInt2);
/* 2872 */           i = localBreakIterator.previous();
/* 2873 */           j = localBreakIterator.previous();
/* 2874 */           if (j == -1) {
/* 2875 */             return null;
/*      */           }
/* 2877 */           return str2.substring(j, i);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 2879 */           return null;
/*      */         }
/*      */       }
/* 2882 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/* 2894 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2895 */       if (localView != null) {
/* 2896 */         Document localDocument = localView.getDocument();
/* 2897 */         if ((localDocument instanceof StyledDocument)) {
/* 2898 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 2899 */           Element localElement = localStyledDocument.getCharacterElement(paramInt);
/* 2900 */           if (localElement != null) {
/* 2901 */             return localElement.getAttributes();
/*      */           }
/*      */         }
/*      */       }
/* 2905 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 2918 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 2931 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 2942 */       return null;
/*      */     }
/*      */ 
/*      */     private String getText(int paramInt1, int paramInt2)
/*      */       throws BadLocationException
/*      */     {
/* 2952 */       View localView = (View)AbstractButton.this.getClientProperty("html");
/* 2953 */       if (localView != null) {
/* 2954 */         Document localDocument = localView.getDocument();
/* 2955 */         if ((localDocument instanceof StyledDocument)) {
/* 2956 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 2957 */           return localStyledDocument.getText(paramInt1, paramInt2);
/*      */         }
/*      */       }
/* 2960 */       return null;
/*      */     }
/*      */ 
/*      */     private Rectangle getTextRectangle()
/*      */     {
/* 2968 */       String str1 = AbstractButton.this.getText();
/* 2969 */       Icon localIcon = AbstractButton.this.isEnabled() ? AbstractButton.this.getIcon() : AbstractButton.this.getDisabledIcon();
/*      */ 
/* 2971 */       if ((localIcon == null) && (str1 == null)) {
/* 2972 */         return null;
/*      */       }
/*      */ 
/* 2975 */       Rectangle localRectangle1 = new Rectangle();
/* 2976 */       Rectangle localRectangle2 = new Rectangle();
/* 2977 */       Rectangle localRectangle3 = new Rectangle();
/* 2978 */       Insets localInsets = new Insets(0, 0, 0, 0);
/*      */ 
/* 2980 */       localInsets = AbstractButton.this.getInsets(localInsets);
/* 2981 */       localRectangle3.x = localInsets.left;
/* 2982 */       localRectangle3.y = localInsets.top;
/* 2983 */       localRectangle3.width = (AbstractButton.this.getWidth() - (localInsets.left + localInsets.right));
/* 2984 */       localRectangle3.height = (AbstractButton.this.getHeight() - (localInsets.top + localInsets.bottom));
/*      */ 
/* 2986 */       String str2 = SwingUtilities.layoutCompoundLabel(AbstractButton.this, getFontMetrics(getFont()), str1, localIcon, AbstractButton.this.getVerticalAlignment(), AbstractButton.this.getHorizontalAlignment(), AbstractButton.this.getVerticalTextPosition(), AbstractButton.this.getHorizontalTextPosition(), localRectangle3, localRectangle1, localRectangle2, 0);
/*      */ 
/* 3000 */       return localRectangle2;
/*      */     }
/*      */ 
/*      */     AccessibleExtendedComponent getAccessibleExtendedComponent()
/*      */     {
/* 3011 */       return this;
/*      */     }
/*      */ 
/*      */     public String getToolTipText()
/*      */     {
/* 3022 */       return AbstractButton.this.getToolTipText();
/*      */     }
/*      */ 
/*      */     public String getTitledBorderText()
/*      */     {
/* 3033 */       return super.getTitledBorderText();
/*      */     }
/*      */ 
/*      */     public AccessibleKeyBinding getAccessibleKeyBinding()
/*      */     {
/* 3045 */       int i = AbstractButton.this.getMnemonic();
/* 3046 */       if (i == 0) {
/* 3047 */         return null;
/*      */       }
/* 3049 */       return new ButtonKeyBinding(i);
/*      */     }
/*      */ 
/*      */     class ButtonKeyBinding
/*      */       implements AccessibleKeyBinding
/*      */     {
/*      */       int mnemonic;
/*      */ 
/*      */       ButtonKeyBinding(int arg2)
/*      */       {
/*      */         int i;
/* 3056 */         this.mnemonic = i;
/*      */       }
/*      */ 
/*      */       public int getAccessibleKeyBindingCount()
/*      */       {
/* 3065 */         return 1;
/*      */       }
/*      */ 
/*      */       public Object getAccessibleKeyBinding(int paramInt)
/*      */       {
/* 3094 */         if (paramInt != 0) {
/* 3095 */           throw new IllegalArgumentException();
/*      */         }
/* 3097 */         return KeyStroke.getKeyStroke(this.mnemonic, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ButtonActionPropertyChangeListener extends ActionPropertyChangeListener<AbstractButton>
/*      */   {
/*      */     ButtonActionPropertyChangeListener(AbstractButton paramAbstractButton, Action paramAction)
/*      */     {
/* 1353 */       super(paramAction);
/*      */     }
/*      */ 
/*      */     protected void actionPropertyChanged(AbstractButton paramAbstractButton, Action paramAction, PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1358 */       if (AbstractAction.shouldReconfigure(paramPropertyChangeEvent))
/* 1359 */         paramAbstractButton.configurePropertiesFromAction(paramAction);
/*      */       else
/* 1361 */         paramAbstractButton.actionPropertyChanged(paramAction, paramPropertyChangeEvent.getPropertyName());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ButtonChangeListener
/*      */     implements ChangeListener, Serializable
/*      */   {
/*      */     ButtonChangeListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1984 */       AbstractButton.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   class Handler
/*      */     implements ActionListener, ChangeListener, ItemListener, Serializable
/*      */   {
/*      */     Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 2327 */       Object localObject = paramChangeEvent.getSource();
/*      */ 
/* 2329 */       AbstractButton.this.updateMnemonicProperties();
/* 2330 */       if (AbstractButton.this.isEnabled() != AbstractButton.this.model.isEnabled()) {
/* 2331 */         AbstractButton.this.setEnabled(AbstractButton.this.model.isEnabled());
/*      */       }
/* 2333 */       AbstractButton.this.fireStateChanged();
/* 2334 */       AbstractButton.this.repaint();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2341 */       AbstractButton.this.fireActionPerformed(paramActionEvent);
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/* 2348 */       AbstractButton.this.fireItemStateChanged(paramItemEvent);
/* 2349 */       if (AbstractButton.this.shouldUpdateSelectedStateFromAction()) {
/* 2350 */         Action localAction = AbstractButton.this.getAction();
/* 2351 */         if ((localAction != null) && (AbstractAction.hasSelectedKey(localAction))) {
/* 2352 */           boolean bool1 = AbstractButton.this.isSelected();
/* 2353 */           boolean bool2 = AbstractAction.isSelected(localAction);
/*      */ 
/* 2355 */           if (bool2 != bool1)
/* 2356 */             localAction.putValue("SwingSelectedKey", Boolean.valueOf(bool1));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AbstractButton
 * JD-Core Version:    0.6.2
 */