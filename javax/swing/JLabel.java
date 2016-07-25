/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.text.BreakIterator;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleExtendedComponent;
/*      */ import javax.accessibility.AccessibleIcon;
/*      */ import javax.accessibility.AccessibleKeyBinding;
/*      */ import javax.accessibility.AccessibleRelation;
/*      */ import javax.accessibility.AccessibleRelationSet;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.LabelUI;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ public class JLabel extends JComponent
/*      */   implements SwingConstants, Accessible
/*      */ {
/*      */   private static final String uiClassID = "LabelUI";
/*  115 */   private int mnemonic = 0;
/*  116 */   private int mnemonicIndex = -1;
/*      */ 
/*  118 */   private String text = "";
/*  119 */   private Icon defaultIcon = null;
/*  120 */   private Icon disabledIcon = null;
/*  121 */   private boolean disabledIconSet = false;
/*      */ 
/*  123 */   private int verticalAlignment = 0;
/*  124 */   private int horizontalAlignment = 10;
/*  125 */   private int verticalTextPosition = 0;
/*  126 */   private int horizontalTextPosition = 11;
/*  127 */   private int iconTextGap = 4;
/*      */ 
/*  129 */   protected Component labelFor = null;
/*      */   static final String LABELED_BY_PROPERTY = "labeledBy";
/*      */ 
/*      */   public JLabel(String paramString, Icon paramIcon, int paramInt)
/*      */   {
/*  160 */     setText(paramString);
/*  161 */     setIcon(paramIcon);
/*  162 */     setHorizontalAlignment(paramInt);
/*  163 */     updateUI();
/*  164 */     setAlignmentX(0.0F);
/*      */   }
/*      */ 
/*      */   public JLabel(String paramString, int paramInt)
/*      */   {
/*  182 */     this(paramString, null, paramInt);
/*      */   }
/*      */ 
/*      */   public JLabel(String paramString)
/*      */   {
/*  193 */     this(paramString, null, 10);
/*      */   }
/*      */ 
/*      */   public JLabel(Icon paramIcon, int paramInt)
/*      */   {
/*  211 */     this(null, paramIcon, paramInt);
/*      */   }
/*      */ 
/*      */   public JLabel(Icon paramIcon)
/*      */   {
/*  222 */     this(null, paramIcon, 0);
/*      */   }
/*      */ 
/*      */   public JLabel()
/*      */   {
/*  234 */     this("", null, 10);
/*      */   }
/*      */ 
/*      */   public LabelUI getUI()
/*      */   {
/*  244 */     return (LabelUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(LabelUI paramLabelUI)
/*      */   {
/*  260 */     super.setUI(paramLabelUI);
/*      */ 
/*  262 */     if ((!this.disabledIconSet) && (this.disabledIcon != null))
/*  263 */       setDisabledIcon(null);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  274 */     setUI((LabelUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  288 */     return "LabelUI";
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */   {
/*  299 */     return this.text;
/*      */   }
/*      */ 
/*      */   public void setText(String paramString)
/*      */   {
/*  322 */     String str1 = null;
/*  323 */     if (this.accessibleContext != null) {
/*  324 */       str1 = this.accessibleContext.getAccessibleName();
/*      */     }
/*      */ 
/*  327 */     String str2 = this.text;
/*  328 */     this.text = paramString;
/*  329 */     firePropertyChange("text", str2, paramString);
/*      */ 
/*  331 */     setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(paramString, getDisplayedMnemonic()));
/*      */ 
/*  335 */     if ((this.accessibleContext != null) && (this.accessibleContext.getAccessibleName() != str1))
/*      */     {
/*  337 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", str1, this.accessibleContext.getAccessibleName());
/*      */     }
/*      */ 
/*  342 */     if ((paramString == null) || (str2 == null) || (!paramString.equals(str2))) {
/*  343 */       revalidate();
/*  344 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Icon getIcon()
/*      */   {
/*  356 */     return this.defaultIcon;
/*      */   }
/*      */ 
/*      */   public void setIcon(Icon paramIcon)
/*      */   {
/*  377 */     Icon localIcon = this.defaultIcon;
/*  378 */     this.defaultIcon = paramIcon;
/*      */ 
/*  385 */     if ((this.defaultIcon != localIcon) && (!this.disabledIconSet)) {
/*  386 */       this.disabledIcon = null;
/*      */     }
/*      */ 
/*  389 */     firePropertyChange("icon", localIcon, this.defaultIcon);
/*      */ 
/*  391 */     if ((this.accessibleContext != null) && (localIcon != this.defaultIcon)) {
/*  392 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, this.defaultIcon);
/*      */     }
/*      */ 
/*  401 */     if (this.defaultIcon != localIcon) {
/*  402 */       if ((this.defaultIcon == null) || (localIcon == null) || (this.defaultIcon.getIconWidth() != localIcon.getIconWidth()) || (this.defaultIcon.getIconHeight() != localIcon.getIconHeight()))
/*      */       {
/*  406 */         revalidate();
/*      */       }
/*  408 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Icon getDisabledIcon()
/*      */   {
/*  428 */     if ((!this.disabledIconSet) && (this.disabledIcon == null) && (this.defaultIcon != null)) {
/*  429 */       this.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, this.defaultIcon);
/*  430 */       if (this.disabledIcon != null) {
/*  431 */         firePropertyChange("disabledIcon", null, this.disabledIcon);
/*      */       }
/*      */     }
/*  434 */     return this.disabledIcon;
/*      */   }
/*      */ 
/*      */   public void setDisabledIcon(Icon paramIcon)
/*      */   {
/*  453 */     Icon localIcon = this.disabledIcon;
/*  454 */     this.disabledIcon = paramIcon;
/*  455 */     this.disabledIconSet = (paramIcon != null);
/*  456 */     firePropertyChange("disabledIcon", localIcon, paramIcon);
/*  457 */     if (paramIcon != localIcon) {
/*  458 */       if ((paramIcon == null) || (localIcon == null) || (paramIcon.getIconWidth() != localIcon.getIconWidth()) || (paramIcon.getIconHeight() != localIcon.getIconHeight()))
/*      */       {
/*  461 */         revalidate();
/*      */       }
/*  463 */       if (!isEnabled())
/*  464 */         repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDisplayedMnemonic(int paramInt)
/*      */   {
/*  485 */     int i = this.mnemonic;
/*  486 */     this.mnemonic = paramInt;
/*  487 */     firePropertyChange("displayedMnemonic", i, this.mnemonic);
/*      */ 
/*  489 */     setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(getText(), this.mnemonic));
/*      */ 
/*  492 */     if (paramInt != i) {
/*  493 */       revalidate();
/*  494 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDisplayedMnemonic(char paramChar)
/*      */   {
/*  506 */     int i = KeyEvent.getExtendedKeyCodeForChar(paramChar);
/*  507 */     if (i != 0)
/*  508 */       setDisplayedMnemonic(i);
/*      */   }
/*      */ 
/*      */   public int getDisplayedMnemonic()
/*      */   {
/*  526 */     return this.mnemonic;
/*      */   }
/*      */ 
/*      */   public void setDisplayedMnemonicIndex(int paramInt)
/*      */     throws IllegalArgumentException
/*      */   {
/*  558 */     int i = this.mnemonicIndex;
/*  559 */     if (paramInt == -1) {
/*  560 */       this.mnemonicIndex = -1;
/*      */     } else {
/*  562 */       String str = getText();
/*  563 */       int j = str == null ? 0 : str.length();
/*  564 */       if ((paramInt < -1) || (paramInt >= j)) {
/*  565 */         throw new IllegalArgumentException("index == " + paramInt);
/*      */       }
/*      */     }
/*  568 */     this.mnemonicIndex = paramInt;
/*  569 */     firePropertyChange("displayedMnemonicIndex", i, paramInt);
/*  570 */     if (paramInt != i) {
/*  571 */       revalidate();
/*  572 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getDisplayedMnemonicIndex()
/*      */   {
/*  585 */     return this.mnemonicIndex;
/*      */   }
/*      */ 
/*      */   protected int checkHorizontalKey(int paramInt, String paramString)
/*      */   {
/*  599 */     if ((paramInt == 2) || (paramInt == 0) || (paramInt == 4) || (paramInt == 10) || (paramInt == 11))
/*      */     {
/*  604 */       return paramInt;
/*      */     }
/*      */ 
/*  607 */     throw new IllegalArgumentException(paramString);
/*      */   }
/*      */ 
/*      */   protected int checkVerticalKey(int paramInt, String paramString)
/*      */   {
/*  623 */     if ((paramInt == 1) || (paramInt == 0) || (paramInt == 3)) {
/*  624 */       return paramInt;
/*      */     }
/*      */ 
/*  627 */     throw new IllegalArgumentException(paramString);
/*      */   }
/*      */ 
/*      */   public int getIconTextGap()
/*      */   {
/*  641 */     return this.iconTextGap;
/*      */   }
/*      */ 
/*      */   public void setIconTextGap(int paramInt)
/*      */   {
/*  661 */     int i = this.iconTextGap;
/*  662 */     this.iconTextGap = paramInt;
/*  663 */     firePropertyChange("iconTextGap", i, paramInt);
/*  664 */     if (paramInt != i) {
/*  665 */       revalidate();
/*  666 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getVerticalAlignment()
/*      */   {
/*  685 */     return this.verticalAlignment;
/*      */   }
/*      */ 
/*      */   public void setVerticalAlignment(int paramInt)
/*      */   {
/*  711 */     if (paramInt == this.verticalAlignment) return;
/*  712 */     int i = this.verticalAlignment;
/*  713 */     this.verticalAlignment = checkVerticalKey(paramInt, "verticalAlignment");
/*  714 */     firePropertyChange("verticalAlignment", i, this.verticalAlignment);
/*  715 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getHorizontalAlignment()
/*      */   {
/*  734 */     return this.horizontalAlignment;
/*      */   }
/*      */ 
/*      */   public void setHorizontalAlignment(int paramInt)
/*      */   {
/*  763 */     if (paramInt == this.horizontalAlignment) return;
/*  764 */     int i = this.horizontalAlignment;
/*  765 */     this.horizontalAlignment = checkHorizontalKey(paramInt, "horizontalAlignment");
/*      */ 
/*  767 */     firePropertyChange("horizontalAlignment", i, this.horizontalAlignment);
/*      */ 
/*  769 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getVerticalTextPosition()
/*      */   {
/*  787 */     return this.verticalTextPosition;
/*      */   }
/*      */ 
/*      */   public void setVerticalTextPosition(int paramInt)
/*      */   {
/*  817 */     if (paramInt == this.verticalTextPosition) return;
/*  818 */     int i = this.verticalTextPosition;
/*  819 */     this.verticalTextPosition = checkVerticalKey(paramInt, "verticalTextPosition");
/*      */ 
/*  821 */     firePropertyChange("verticalTextPosition", i, this.verticalTextPosition);
/*  822 */     revalidate();
/*  823 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getHorizontalTextPosition()
/*      */   {
/*  842 */     return this.horizontalTextPosition;
/*      */   }
/*      */ 
/*      */   public void setHorizontalTextPosition(int paramInt)
/*      */   {
/*  873 */     int i = this.horizontalTextPosition;
/*  874 */     this.horizontalTextPosition = checkHorizontalKey(paramInt, "horizontalTextPosition");
/*      */ 
/*  876 */     firePropertyChange("horizontalTextPosition", i, this.horizontalTextPosition);
/*      */ 
/*  878 */     revalidate();
/*  879 */     repaint();
/*      */   }
/*      */ 
/*      */   public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  894 */     if ((!isShowing()) || ((!SwingUtilities.doesIconReferenceImage(getIcon(), paramImage)) && (!SwingUtilities.doesIconReferenceImage(this.disabledIcon, paramImage))))
/*      */     {
/*  898 */       return false;
/*      */     }
/*  900 */     return super.imageUpdate(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  909 */     paramObjectOutputStream.defaultWriteObject();
/*  910 */     if (getUIClassID().equals("LabelUI")) {
/*  911 */       byte b = JComponent.getWriteObjCounter(this);
/*  912 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/*  913 */       if ((b == 0) && (this.ui != null))
/*  914 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*  930 */     String str1 = this.text != null ? this.text : "";
/*      */ 
/*  932 */     String str2 = (this.defaultIcon != null) && (this.defaultIcon != this) ? this.defaultIcon.toString() : "";
/*      */ 
/*  935 */     String str3 = (this.disabledIcon != null) && (this.disabledIcon != this) ? this.disabledIcon.toString() : "";
/*      */ 
/*  938 */     String str4 = this.labelFor != null ? this.labelFor.toString() : "";
/*      */     String str5;
/*  941 */     if (this.verticalAlignment == 1)
/*  942 */       str5 = "TOP";
/*  943 */     else if (this.verticalAlignment == 0)
/*  944 */       str5 = "CENTER";
/*  945 */     else if (this.verticalAlignment == 3)
/*  946 */       str5 = "BOTTOM";
/*  947 */     else str5 = "";
/*      */     String str6;
/*  949 */     if (this.horizontalAlignment == 2)
/*  950 */       str6 = "LEFT";
/*  951 */     else if (this.horizontalAlignment == 0)
/*  952 */       str6 = "CENTER";
/*  953 */     else if (this.horizontalAlignment == 4)
/*  954 */       str6 = "RIGHT";
/*  955 */     else if (this.horizontalAlignment == 10)
/*  956 */       str6 = "LEADING";
/*  957 */     else if (this.horizontalAlignment == 11)
/*  958 */       str6 = "TRAILING";
/*  959 */     else str6 = "";
/*      */     String str7;
/*  961 */     if (this.verticalTextPosition == 1)
/*  962 */       str7 = "TOP";
/*  963 */     else if (this.verticalTextPosition == 0)
/*  964 */       str7 = "CENTER";
/*  965 */     else if (this.verticalTextPosition == 3)
/*  966 */       str7 = "BOTTOM";
/*  967 */     else str7 = "";
/*      */     String str8;
/*  969 */     if (this.horizontalTextPosition == 2)
/*  970 */       str8 = "LEFT";
/*  971 */     else if (this.horizontalTextPosition == 0)
/*  972 */       str8 = "CENTER";
/*  973 */     else if (this.horizontalTextPosition == 4)
/*  974 */       str8 = "RIGHT";
/*  975 */     else if (this.horizontalTextPosition == 10)
/*  976 */       str8 = "LEADING";
/*  977 */     else if (this.horizontalTextPosition == 11)
/*  978 */       str8 = "TRAILING";
/*  979 */     else str8 = "";
/*      */ 
/*  981 */     return super.paramString() + ",defaultIcon=" + str2 + ",disabledIcon=" + str3 + ",horizontalAlignment=" + str6 + ",horizontalTextPosition=" + str8 + ",iconTextGap=" + this.iconTextGap + ",labelFor=" + str4 + ",text=" + str1 + ",verticalAlignment=" + str5 + ",verticalTextPosition=" + str7;
/*      */   }
/*      */ 
/*      */   public Component getLabelFor()
/*      */   {
/* 1010 */     return this.labelFor;
/*      */   }
/*      */ 
/*      */   public void setLabelFor(Component paramComponent)
/*      */   {
/* 1031 */     Component localComponent = this.labelFor;
/* 1032 */     this.labelFor = paramComponent;
/* 1033 */     firePropertyChange("labelFor", localComponent, paramComponent);
/*      */ 
/* 1035 */     if ((localComponent instanceof JComponent)) {
/* 1036 */       ((JComponent)localComponent).putClientProperty("labeledBy", null);
/*      */     }
/* 1038 */     if ((paramComponent instanceof JComponent))
/* 1039 */       ((JComponent)paramComponent).putClientProperty("labeledBy", this);
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1052 */     if (this.accessibleContext == null) {
/* 1053 */       this.accessibleContext = new AccessibleJLabel();
/*      */     }
/* 1055 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJLabel extends JComponent.AccessibleJComponent
/*      */     implements AccessibleText, AccessibleExtendedComponent
/*      */   {
/*      */     protected AccessibleJLabel()
/*      */     {
/* 1070 */       super();
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/* 1081 */       String str = this.accessibleName;
/*      */ 
/* 1083 */       if (str == null) {
/* 1084 */         str = (String)JLabel.this.getClientProperty("AccessibleName");
/*      */       }
/* 1086 */       if (str == null) {
/* 1087 */         str = JLabel.this.getText();
/*      */       }
/* 1089 */       if (str == null) {
/* 1090 */         str = super.getAccessibleName();
/*      */       }
/* 1092 */       return str;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1103 */       return AccessibleRole.LABEL;
/*      */     }
/*      */ 
/*      */     public AccessibleIcon[] getAccessibleIcon()
/*      */     {
/* 1112 */       Icon localIcon = JLabel.this.getIcon();
/* 1113 */       if ((localIcon instanceof Accessible)) {
/* 1114 */         AccessibleContext localAccessibleContext = ((Accessible)localIcon).getAccessibleContext();
/*      */ 
/* 1116 */         if ((localAccessibleContext != null) && ((localAccessibleContext instanceof AccessibleIcon))) {
/* 1117 */           return new AccessibleIcon[] { (AccessibleIcon)localAccessibleContext };
/*      */         }
/*      */       }
/* 1120 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleRelationSet getAccessibleRelationSet()
/*      */     {
/* 1132 */       AccessibleRelationSet localAccessibleRelationSet = super.getAccessibleRelationSet();
/*      */ 
/* 1135 */       if (!localAccessibleRelationSet.contains(AccessibleRelation.LABEL_FOR)) {
/* 1136 */         Component localComponent = JLabel.this.getLabelFor();
/* 1137 */         if (localComponent != null) {
/* 1138 */           AccessibleRelation localAccessibleRelation = new AccessibleRelation(AccessibleRelation.LABEL_FOR);
/*      */ 
/* 1140 */           localAccessibleRelation.setTarget(localComponent);
/* 1141 */           localAccessibleRelationSet.add(localAccessibleRelation);
/*      */         }
/*      */       }
/* 1144 */       return localAccessibleRelationSet;
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/* 1151 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1152 */       if (localView != null) {
/* 1153 */         return this;
/*      */       }
/* 1155 */       return null;
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/* 1170 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1171 */       if (localView != null) {
/* 1172 */         Rectangle localRectangle = getTextRectangle();
/* 1173 */         if (localRectangle == null) {
/* 1174 */           return -1;
/*      */         }
/* 1176 */         Rectangle2D.Float localFloat = new Rectangle2D.Float(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 1178 */         Position.Bias[] arrayOfBias = new Position.Bias[1];
/* 1179 */         return localView.viewToModel(paramPoint.x, paramPoint.y, localFloat, arrayOfBias);
/*      */       }
/* 1181 */       return -1;
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/* 1197 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1198 */       if (localView != null) {
/* 1199 */         Rectangle localRectangle = getTextRectangle();
/* 1200 */         if (localRectangle == null) {
/* 1201 */           return null;
/*      */         }
/* 1203 */         Rectangle2D.Float localFloat = new Rectangle2D.Float(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */         try
/*      */         {
/* 1206 */           Shape localShape = localView.modelToView(paramInt, localFloat, Position.Bias.Forward);
/*      */ 
/* 1208 */           return localShape.getBounds();
/*      */         } catch (BadLocationException localBadLocationException) {
/* 1210 */           return null;
/*      */         }
/*      */       }
/* 1213 */       return null;
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/* 1224 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1225 */       if (localView != null) {
/* 1226 */         Document localDocument = localView.getDocument();
/* 1227 */         if ((localDocument instanceof StyledDocument)) {
/* 1228 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 1229 */           return localStyledDocument.getLength();
/*      */         }
/*      */       }
/* 1232 */       return JLabel.this.accessibleContext.getAccessibleName().length();
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/* 1245 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1259 */       if ((paramInt2 < 0) || (paramInt2 >= getCharCount()))
/* 1260 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/* 1262 */       switch (paramInt1) {
/*      */       case 1:
/*      */         try {
/* 1265 */           return getText(paramInt2, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 1267 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 1271 */           String str1 = getText(0, getCharCount());
/* 1272 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 1273 */           localBreakIterator.setText(str1);
/* 1274 */           i = localBreakIterator.following(paramInt2);
/* 1275 */           return str1.substring(localBreakIterator.previous(), i);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 1277 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 1281 */           String str2 = getText(0, getCharCount());
/* 1282 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 1284 */           localBreakIterator.setText(str2);
/* 1285 */           i = localBreakIterator.following(paramInt2);
/* 1286 */           return str2.substring(localBreakIterator.previous(), i);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 1288 */           return null;
/*      */         }
/*      */       }
/* 1291 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1306 */       if ((paramInt2 < 0) || (paramInt2 >= getCharCount()))
/* 1307 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 1309 */       switch (paramInt1) {
/*      */       case 1:
/* 1311 */         if (paramInt2 + 1 >= getCharCount())
/* 1312 */           return null;
/*      */         try
/*      */         {
/* 1315 */           return getText(paramInt2 + 1, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 1317 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 1321 */           String str1 = getText(0, getCharCount());
/* 1322 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 1323 */           localBreakIterator.setText(str1);
/* 1324 */           i = localBreakIterator.following(paramInt2);
/* 1325 */           if ((i == -1) || (i >= str1.length())) {
/* 1326 */             return null;
/*      */           }
/* 1328 */           j = localBreakIterator.following(i);
/* 1329 */           if ((j == -1) || (j >= str1.length())) {
/* 1330 */             return null;
/*      */           }
/* 1332 */           return str1.substring(i, j);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 1334 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 1338 */           String str2 = getText(0, getCharCount());
/* 1339 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 1341 */           localBreakIterator.setText(str2);
/* 1342 */           i = localBreakIterator.following(paramInt2);
/* 1343 */           if ((i == -1) || (i > str2.length())) {
/* 1344 */             return null;
/*      */           }
/* 1346 */           j = localBreakIterator.following(i);
/* 1347 */           if ((j == -1) || (j > str2.length())) {
/* 1348 */             return null;
/*      */           }
/* 1350 */           return str2.substring(i, j);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 1352 */           return null;
/*      */         }
/*      */       }
/* 1355 */       return null;
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1370 */       if ((paramInt2 < 0) || (paramInt2 > getCharCount() - 1))
/* 1371 */         return null;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 1373 */       switch (paramInt1) {
/*      */       case 1:
/* 1375 */         if (paramInt2 == 0)
/* 1376 */           return null;
/*      */         try
/*      */         {
/* 1379 */           return getText(paramInt2 - 1, 1);
/*      */         } catch (BadLocationException localBadLocationException1) {
/* 1381 */           return null;
/*      */         }
/*      */       case 2:
/*      */         try {
/* 1385 */           String str1 = getText(0, getCharCount());
/* 1386 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 1387 */           localBreakIterator.setText(str1);
/* 1388 */           i = localBreakIterator.following(paramInt2);
/* 1389 */           i = localBreakIterator.previous();
/* 1390 */           j = localBreakIterator.previous();
/* 1391 */           if (j == -1) {
/* 1392 */             return null;
/*      */           }
/* 1394 */           return str1.substring(j, i);
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 1396 */           return null;
/*      */         }
/*      */       case 3:
/*      */         try {
/* 1400 */           String str2 = getText(0, getCharCount());
/* 1401 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/*      */ 
/* 1403 */           localBreakIterator.setText(str2);
/* 1404 */           i = localBreakIterator.following(paramInt2);
/* 1405 */           i = localBreakIterator.previous();
/* 1406 */           j = localBreakIterator.previous();
/* 1407 */           if (j == -1) {
/* 1408 */             return null;
/*      */           }
/* 1410 */           return str2.substring(j, i);
/*      */         } catch (BadLocationException localBadLocationException3) {
/* 1412 */           return null;
/*      */         }
/*      */       }
/* 1415 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/* 1427 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1428 */       if (localView != null) {
/* 1429 */         Document localDocument = localView.getDocument();
/* 1430 */         if ((localDocument instanceof StyledDocument)) {
/* 1431 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 1432 */           Element localElement = localStyledDocument.getCharacterElement(paramInt);
/* 1433 */           if (localElement != null) {
/* 1434 */             return localElement.getAttributes();
/*      */           }
/*      */         }
/*      */       }
/* 1438 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 1451 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 1464 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 1475 */       return null;
/*      */     }
/*      */ 
/*      */     private String getText(int paramInt1, int paramInt2)
/*      */       throws BadLocationException
/*      */     {
/* 1485 */       View localView = (View)JLabel.this.getClientProperty("html");
/* 1486 */       if (localView != null) {
/* 1487 */         Document localDocument = localView.getDocument();
/* 1488 */         if ((localDocument instanceof StyledDocument)) {
/* 1489 */           StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 1490 */           return localStyledDocument.getText(paramInt1, paramInt2);
/*      */         }
/*      */       }
/* 1493 */       return null;
/*      */     }
/*      */ 
/*      */     private Rectangle getTextRectangle()
/*      */     {
/* 1501 */       String str1 = JLabel.this.getText();
/* 1502 */       Icon localIcon = JLabel.this.isEnabled() ? JLabel.this.getIcon() : JLabel.this.getDisabledIcon();
/*      */ 
/* 1504 */       if ((localIcon == null) && (str1 == null)) {
/* 1505 */         return null;
/*      */       }
/*      */ 
/* 1508 */       Rectangle localRectangle1 = new Rectangle();
/* 1509 */       Rectangle localRectangle2 = new Rectangle();
/* 1510 */       Rectangle localRectangle3 = new Rectangle();
/* 1511 */       Insets localInsets = new Insets(0, 0, 0, 0);
/*      */ 
/* 1513 */       localInsets = JLabel.this.getInsets(localInsets);
/* 1514 */       localRectangle3.x = localInsets.left;
/* 1515 */       localRectangle3.y = localInsets.top;
/* 1516 */       localRectangle3.width = (JLabel.this.getWidth() - (localInsets.left + localInsets.right));
/* 1517 */       localRectangle3.height = (JLabel.this.getHeight() - (localInsets.top + localInsets.bottom));
/*      */ 
/* 1519 */       String str2 = SwingUtilities.layoutCompoundLabel(JLabel.this, getFontMetrics(getFont()), str1, localIcon, JLabel.this.getVerticalAlignment(), JLabel.this.getHorizontalAlignment(), JLabel.this.getVerticalTextPosition(), JLabel.this.getHorizontalTextPosition(), localRectangle3, localRectangle1, localRectangle2, JLabel.this.getIconTextGap());
/*      */ 
/* 1533 */       return localRectangle2;
/*      */     }
/*      */ 
/*      */     AccessibleExtendedComponent getAccessibleExtendedComponent()
/*      */     {
/* 1544 */       return this;
/*      */     }
/*      */ 
/*      */     public String getToolTipText()
/*      */     {
/* 1555 */       return JLabel.this.getToolTipText();
/*      */     }
/*      */ 
/*      */     public String getTitledBorderText()
/*      */     {
/* 1566 */       return super.getTitledBorderText();
/*      */     }
/*      */ 
/*      */     public AccessibleKeyBinding getAccessibleKeyBinding()
/*      */     {
/* 1578 */       int i = JLabel.this.getDisplayedMnemonic();
/* 1579 */       if (i == 0) {
/* 1580 */         return null;
/*      */       }
/* 1582 */       return new LabelKeyBinding(i);
/*      */     }
/*      */ 
/*      */     class LabelKeyBinding
/*      */       implements AccessibleKeyBinding
/*      */     {
/*      */       int mnemonic;
/*      */ 
/*      */       LabelKeyBinding(int arg2)
/*      */       {
/*      */         int i;
/* 1589 */         this.mnemonic = i;
/*      */       }
/*      */ 
/*      */       public int getAccessibleKeyBindingCount()
/*      */       {
/* 1598 */         return 1;
/*      */       }
/*      */ 
/*      */       public Object getAccessibleKeyBinding(int paramInt)
/*      */       {
/* 1627 */         if (paramInt != 0) {
/* 1628 */           throw new IllegalArgumentException();
/*      */         }
/* 1630 */         return KeyStroke.getKeyStroke(this.mnemonic, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JLabel
 * JD-Core Version:    0.6.2
 */