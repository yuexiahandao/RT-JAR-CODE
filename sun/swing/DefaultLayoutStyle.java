/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.LayoutStyle;
/*     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class DefaultLayoutStyle extends LayoutStyle
/*     */ {
/*  41 */   private static final DefaultLayoutStyle INSTANCE = new DefaultLayoutStyle();
/*     */ 
/*     */   public static LayoutStyle getInstance()
/*     */   {
/*  45 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public int getPreferredGap(JComponent paramJComponent1, JComponent paramJComponent2, LayoutStyle.ComponentPlacement paramComponentPlacement, int paramInt, Container paramContainer)
/*     */   {
/*  51 */     if ((paramJComponent1 == null) || (paramJComponent2 == null) || (paramComponentPlacement == null)) {
/*  52 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*  55 */     checkPosition(paramInt);
/*     */ 
/*  57 */     if ((paramComponentPlacement == LayoutStyle.ComponentPlacement.INDENT) && ((paramInt == 3) || (paramInt == 7)))
/*     */     {
/*  60 */       int i = getIndent(paramJComponent1, paramInt);
/*  61 */       if (i > 0) {
/*  62 */         return i;
/*     */       }
/*     */     }
/*  65 */     return paramComponentPlacement == LayoutStyle.ComponentPlacement.UNRELATED ? 12 : 6;
/*     */   }
/*     */ 
/*     */   public int getContainerGap(JComponent paramJComponent, int paramInt, Container paramContainer)
/*     */   {
/*  71 */     if (paramJComponent == null) {
/*  72 */       throw new NullPointerException();
/*     */     }
/*  74 */     checkPosition(paramInt);
/*  75 */     return 6;
/*     */   }
/*     */ 
/*     */   protected boolean isLabelAndNonlabel(JComponent paramJComponent1, JComponent paramJComponent2, int paramInt)
/*     */   {
/*  84 */     if ((paramInt == 3) || (paramInt == 7))
/*     */     {
/*  86 */       boolean bool1 = paramJComponent1 instanceof JLabel;
/*  87 */       boolean bool2 = paramJComponent2 instanceof JLabel;
/*  88 */       return ((bool1) || (bool2)) && (bool1 != bool2);
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   protected int getButtonGap(JComponent paramJComponent1, JComponent paramJComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 108 */     paramInt2 -= getButtonGap(paramJComponent1, paramInt1);
/* 109 */     if (paramInt2 > 0) {
/* 110 */       paramInt2 -= getButtonGap(paramJComponent2, flipDirection(paramInt1));
/*     */     }
/* 112 */     if (paramInt2 < 0) {
/* 113 */       return 0;
/*     */     }
/* 115 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   protected int getButtonGap(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 131 */     paramInt2 -= getButtonGap(paramJComponent, paramInt1);
/* 132 */     return Math.max(paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public int getButtonGap(JComponent paramJComponent, int paramInt)
/*     */   {
/* 140 */     String str = paramJComponent.getUIClassID();
/* 141 */     if (((str == "CheckBoxUI") || (str == "RadioButtonUI")) && (!((AbstractButton)paramJComponent).isBorderPainted()))
/*     */     {
/* 143 */       Border localBorder = paramJComponent.getBorder();
/* 144 */       if ((localBorder instanceof UIResource)) {
/* 145 */         return getInset(paramJComponent, paramInt);
/*     */       }
/*     */     }
/* 148 */     return 0;
/*     */   }
/*     */ 
/*     */   private void checkPosition(int paramInt) {
/* 152 */     if ((paramInt != 1) && (paramInt != 5) && (paramInt != 7) && (paramInt != 3))
/*     */     {
/* 156 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int flipDirection(int paramInt) {
/* 161 */     switch (paramInt) {
/*     */     case 1:
/* 163 */       return 5;
/*     */     case 5:
/* 165 */       return 1;
/*     */     case 3:
/* 167 */       return 7;
/*     */     case 7:
/* 169 */       return 3;
/*     */     case 2:
/*     */     case 4:
/* 171 */     case 6: } if (!$assertionsDisabled) throw new AssertionError();
/* 172 */     return 0;
/*     */   }
/*     */ 
/*     */   protected int getIndent(JComponent paramJComponent, int paramInt)
/*     */   {
/* 181 */     String str = paramJComponent.getUIClassID();
/* 182 */     if ((str == "CheckBoxUI") || (str == "RadioButtonUI")) {
/* 183 */       AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 184 */       Insets localInsets = paramJComponent.getInsets();
/* 185 */       Icon localIcon = getIcon(localAbstractButton);
/* 186 */       int i = localAbstractButton.getIconTextGap();
/* 187 */       if (isLeftAligned(localAbstractButton, paramInt))
/* 188 */         return localInsets.left + localIcon.getIconWidth() + i;
/* 189 */       if (isRightAligned(localAbstractButton, paramInt)) {
/* 190 */         return localInsets.right + localIcon.getIconWidth() + i;
/*     */       }
/*     */     }
/* 193 */     return 0;
/*     */   }
/*     */ 
/*     */   private Icon getIcon(AbstractButton paramAbstractButton) {
/* 197 */     Icon localIcon = paramAbstractButton.getIcon();
/* 198 */     if (localIcon != null) {
/* 199 */       return localIcon;
/*     */     }
/* 201 */     String str = null;
/* 202 */     if ((paramAbstractButton instanceof JCheckBox))
/* 203 */       str = "CheckBox.icon";
/* 204 */     else if ((paramAbstractButton instanceof JRadioButton)) {
/* 205 */       str = "RadioButton.icon";
/*     */     }
/* 207 */     if (str != null) {
/* 208 */       Object localObject = UIManager.get(str);
/* 209 */       if ((localObject instanceof Icon)) {
/* 210 */         return (Icon)localObject;
/*     */       }
/*     */     }
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean isLeftAligned(AbstractButton paramAbstractButton, int paramInt) {
/* 217 */     if (paramInt == 7) {
/* 218 */       boolean bool = paramAbstractButton.getComponentOrientation().isLeftToRight();
/* 219 */       int i = paramAbstractButton.getHorizontalAlignment();
/* 220 */       return ((bool) && ((i == 2) || (i == 10))) || ((!bool) && (i == 11));
/*     */     }
/*     */ 
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isRightAligned(AbstractButton paramAbstractButton, int paramInt) {
/* 228 */     if (paramInt == 3) {
/* 229 */       boolean bool = paramAbstractButton.getComponentOrientation().isLeftToRight();
/* 230 */       int i = paramAbstractButton.getHorizontalAlignment();
/* 231 */       return ((bool) && ((i == 4) || (i == 11))) || ((!bool) && (i == 10));
/*     */     }
/*     */ 
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   private int getInset(JComponent paramJComponent, int paramInt) {
/* 239 */     return getInset(paramJComponent.getInsets(), paramInt);
/*     */   }
/*     */ 
/*     */   private int getInset(Insets paramInsets, int paramInt) {
/* 243 */     if (paramInsets == null) {
/* 244 */       return 0;
/*     */     }
/* 246 */     switch (paramInt) {
/*     */     case 1:
/* 248 */       return paramInsets.top;
/*     */     case 5:
/* 250 */       return paramInsets.bottom;
/*     */     case 3:
/* 252 */       return paramInsets.right;
/*     */     case 7:
/* 254 */       return paramInsets.left;
/*     */     case 2:
/*     */     case 4:
/* 256 */     case 6: } if (!$assertionsDisabled) throw new AssertionError();
/* 257 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.DefaultLayoutStyle
 * JD-Core Version:    0.6.2
 */