/*      */ package sun.swing;
/*      */ 
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ public class MenuItemLayoutHelper
/*      */ {
/*   44 */   public static final StringUIClientPropertyKey MAX_ARROW_WIDTH = new StringUIClientPropertyKey("maxArrowWidth");
/*      */ 
/*   46 */   public static final StringUIClientPropertyKey MAX_CHECK_WIDTH = new StringUIClientPropertyKey("maxCheckWidth");
/*      */ 
/*   48 */   public static final StringUIClientPropertyKey MAX_ICON_WIDTH = new StringUIClientPropertyKey("maxIconWidth");
/*      */ 
/*   50 */   public static final StringUIClientPropertyKey MAX_TEXT_WIDTH = new StringUIClientPropertyKey("maxTextWidth");
/*      */ 
/*   52 */   public static final StringUIClientPropertyKey MAX_ACC_WIDTH = new StringUIClientPropertyKey("maxAccWidth");
/*      */ 
/*   54 */   public static final StringUIClientPropertyKey MAX_LABEL_WIDTH = new StringUIClientPropertyKey("maxLabelWidth");
/*      */   private JMenuItem mi;
/*      */   private JComponent miParent;
/*      */   private Font font;
/*      */   private Font accFont;
/*      */   private FontMetrics fm;
/*      */   private FontMetrics accFm;
/*      */   private Icon icon;
/*      */   private Icon checkIcon;
/*      */   private Icon arrowIcon;
/*      */   private String text;
/*      */   private String accText;
/*      */   private boolean isColumnLayout;
/*      */   private boolean useCheckAndArrow;
/*      */   private boolean isLeftToRight;
/*      */   private boolean isTopLevelMenu;
/*      */   private View htmlView;
/*      */   private int verticalAlignment;
/*      */   private int horizontalAlignment;
/*      */   private int verticalTextPosition;
/*      */   private int horizontalTextPosition;
/*      */   private int gap;
/*      */   private int leadingGap;
/*      */   private int afterCheckIconGap;
/*      */   private int minTextOffset;
/*      */   private int leftTextExtraWidth;
/*      */   private Rectangle viewRect;
/*      */   private RectSize iconSize;
/*      */   private RectSize textSize;
/*      */   private RectSize accSize;
/*      */   private RectSize checkSize;
/*      */   private RectSize arrowSize;
/*      */   private RectSize labelSize;
/*      */ 
/*      */   protected MenuItemLayoutHelper()
/*      */   {
/*      */   }
/*      */ 
/*      */   public MenuItemLayoutHelper(JMenuItem paramJMenuItem, Icon paramIcon1, Icon paramIcon2, Rectangle paramRectangle, int paramInt, String paramString1, boolean paramBoolean1, Font paramFont1, Font paramFont2, boolean paramBoolean2, String paramString2)
/*      */   {
/*  107 */     reset(paramJMenuItem, paramIcon1, paramIcon2, paramRectangle, paramInt, paramString1, paramBoolean1, paramFont1, paramFont2, paramBoolean2, paramString2);
/*      */   }
/*      */ 
/*      */   protected void reset(JMenuItem paramJMenuItem, Icon paramIcon1, Icon paramIcon2, Rectangle paramRectangle, int paramInt, String paramString1, boolean paramBoolean1, Font paramFont1, Font paramFont2, boolean paramBoolean2, String paramString2)
/*      */   {
/*  115 */     this.mi = paramJMenuItem;
/*  116 */     this.miParent = getMenuItemParent(paramJMenuItem);
/*  117 */     this.accText = getAccText(paramString1);
/*  118 */     this.verticalAlignment = paramJMenuItem.getVerticalAlignment();
/*  119 */     this.horizontalAlignment = paramJMenuItem.getHorizontalAlignment();
/*  120 */     this.verticalTextPosition = paramJMenuItem.getVerticalTextPosition();
/*  121 */     this.horizontalTextPosition = paramJMenuItem.getHorizontalTextPosition();
/*  122 */     this.useCheckAndArrow = paramBoolean2;
/*  123 */     this.font = paramFont1;
/*  124 */     this.accFont = paramFont2;
/*  125 */     this.fm = paramJMenuItem.getFontMetrics(paramFont1);
/*  126 */     this.accFm = paramJMenuItem.getFontMetrics(paramFont2);
/*  127 */     this.isLeftToRight = paramBoolean1;
/*  128 */     this.isColumnLayout = isColumnLayout(paramBoolean1, this.horizontalAlignment, this.horizontalTextPosition, this.verticalTextPosition);
/*      */ 
/*  131 */     this.isTopLevelMenu = (this.miParent == null);
/*  132 */     this.checkIcon = paramIcon1;
/*  133 */     this.icon = getIcon(paramString2);
/*  134 */     this.arrowIcon = paramIcon2;
/*  135 */     this.text = paramJMenuItem.getText();
/*  136 */     this.gap = paramInt;
/*  137 */     this.afterCheckIconGap = getAfterCheckIconGap(paramString2);
/*  138 */     this.minTextOffset = getMinTextOffset(paramString2);
/*  139 */     this.htmlView = ((View)paramJMenuItem.getClientProperty("html"));
/*  140 */     this.viewRect = paramRectangle;
/*      */ 
/*  142 */     this.iconSize = new RectSize();
/*  143 */     this.textSize = new RectSize();
/*  144 */     this.accSize = new RectSize();
/*  145 */     this.checkSize = new RectSize();
/*  146 */     this.arrowSize = new RectSize();
/*  147 */     this.labelSize = new RectSize();
/*  148 */     calcExtraWidths();
/*  149 */     calcWidthsAndHeights();
/*  150 */     setOriginalWidths();
/*  151 */     calcMaxWidths();
/*      */ 
/*  153 */     this.leadingGap = getLeadingGap(paramString2);
/*  154 */     calcMaxTextOffset(paramRectangle);
/*      */   }
/*      */ 
/*      */   private void calcExtraWidths() {
/*  158 */     this.leftTextExtraWidth = getLeftExtraWidth(this.text);
/*      */   }
/*      */ 
/*      */   private int getLeftExtraWidth(String paramString) {
/*  162 */     int i = SwingUtilities2.getLeftSideBearing(this.mi, this.fm, paramString);
/*  163 */     if (i < 0) {
/*  164 */       return -i;
/*      */     }
/*  166 */     return 0;
/*      */   }
/*      */ 
/*      */   private void setOriginalWidths()
/*      */   {
/*  171 */     this.iconSize.origWidth = this.iconSize.width;
/*  172 */     this.textSize.origWidth = this.textSize.width;
/*  173 */     this.accSize.origWidth = this.accSize.width;
/*  174 */     this.checkSize.origWidth = this.checkSize.width;
/*  175 */     this.arrowSize.origWidth = this.arrowSize.width;
/*      */   }
/*      */ 
/*      */   private String getAccText(String paramString) {
/*  179 */     String str = "";
/*  180 */     KeyStroke localKeyStroke = this.mi.getAccelerator();
/*  181 */     if (localKeyStroke != null) {
/*  182 */       int i = localKeyStroke.getModifiers();
/*  183 */       if (i > 0) {
/*  184 */         str = KeyEvent.getKeyModifiersText(i);
/*  185 */         str = str + paramString;
/*      */       }
/*  187 */       int j = localKeyStroke.getKeyCode();
/*  188 */       if (j != 0)
/*  189 */         str = str + KeyEvent.getKeyText(j);
/*      */       else {
/*  191 */         str = str + localKeyStroke.getKeyChar();
/*      */       }
/*      */     }
/*  194 */     return str;
/*      */   }
/*      */ 
/*      */   private Icon getIcon(String paramString)
/*      */   {
/*  201 */     Icon localIcon = null;
/*  202 */     MenuItemCheckIconFactory localMenuItemCheckIconFactory = (MenuItemCheckIconFactory)UIManager.get(paramString + ".checkIconFactory");
/*      */ 
/*  205 */     if ((!this.isColumnLayout) || (!this.useCheckAndArrow) || (localMenuItemCheckIconFactory == null) || (!localMenuItemCheckIconFactory.isCompatible(this.checkIcon, paramString)))
/*      */     {
/*  207 */       localIcon = this.mi.getIcon();
/*      */     }
/*  209 */     return localIcon;
/*      */   }
/*      */ 
/*      */   private int getMinTextOffset(String paramString) {
/*  213 */     int i = 0;
/*  214 */     Object localObject = UIManager.get(paramString + ".minimumTextOffset");
/*      */ 
/*  216 */     if ((localObject instanceof Integer)) {
/*  217 */       i = ((Integer)localObject).intValue();
/*      */     }
/*  219 */     return i;
/*      */   }
/*      */ 
/*      */   private int getAfterCheckIconGap(String paramString) {
/*  223 */     int i = this.gap;
/*  224 */     Object localObject = UIManager.get(paramString + ".afterCheckIconGap");
/*      */ 
/*  226 */     if ((localObject instanceof Integer)) {
/*  227 */       i = ((Integer)localObject).intValue();
/*      */     }
/*  229 */     return i;
/*      */   }
/*      */ 
/*      */   private int getLeadingGap(String paramString) {
/*  233 */     if (this.checkSize.getMaxWidth() > 0) {
/*  234 */       return getCheckOffset(paramString);
/*      */     }
/*  236 */     return this.gap;
/*      */   }
/*      */ 
/*      */   private int getCheckOffset(String paramString)
/*      */   {
/*  241 */     int i = this.gap;
/*  242 */     Object localObject = UIManager.get(paramString + ".checkIconOffset");
/*      */ 
/*  244 */     if ((localObject instanceof Integer)) {
/*  245 */       i = ((Integer)localObject).intValue();
/*      */     }
/*  247 */     return i;
/*      */   }
/*      */ 
/*      */   protected void calcWidthsAndHeights()
/*      */   {
/*  252 */     if (this.icon != null) {
/*  253 */       this.iconSize.width = this.icon.getIconWidth();
/*  254 */       this.iconSize.height = this.icon.getIconHeight();
/*      */     }
/*      */ 
/*  258 */     if (!this.accText.equals("")) {
/*  259 */       this.accSize.width = SwingUtilities2.stringWidth(this.mi, this.accFm, this.accText);
/*  260 */       this.accSize.height = this.accFm.getHeight();
/*      */     }
/*      */ 
/*  264 */     if (this.text == null)
/*  265 */       this.text = "";
/*  266 */     else if (!this.text.equals("")) {
/*  267 */       if (this.htmlView != null)
/*      */       {
/*  269 */         this.textSize.width = ((int)this.htmlView.getPreferredSpan(0));
/*      */ 
/*  271 */         this.textSize.height = ((int)this.htmlView.getPreferredSpan(1));
/*      */       }
/*      */       else
/*      */       {
/*  275 */         this.textSize.width = SwingUtilities2.stringWidth(this.mi, this.fm, this.text);
/*  276 */         this.textSize.height = this.fm.getHeight();
/*      */       }
/*      */     }
/*      */ 
/*  280 */     if (this.useCheckAndArrow)
/*      */     {
/*  282 */       if (this.checkIcon != null) {
/*  283 */         this.checkSize.width = this.checkIcon.getIconWidth();
/*  284 */         this.checkSize.height = this.checkIcon.getIconHeight();
/*      */       }
/*      */ 
/*  287 */       if (this.arrowIcon != null) {
/*  288 */         this.arrowSize.width = this.arrowIcon.getIconWidth();
/*  289 */         this.arrowSize.height = this.arrowIcon.getIconHeight();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  294 */     if (this.isColumnLayout) {
/*  295 */       this.labelSize.width = (this.iconSize.width + this.textSize.width + this.gap);
/*  296 */       this.labelSize.height = max(new int[] { this.checkSize.height, this.iconSize.height, this.textSize.height, this.accSize.height, this.arrowSize.height });
/*      */     }
/*      */     else {
/*  299 */       Rectangle localRectangle1 = new Rectangle();
/*  300 */       Rectangle localRectangle2 = new Rectangle();
/*  301 */       SwingUtilities.layoutCompoundLabel(this.mi, this.fm, this.text, this.icon, this.verticalAlignment, this.horizontalAlignment, this.verticalTextPosition, this.horizontalTextPosition, this.viewRect, localRectangle2, localRectangle1, this.gap);
/*      */ 
/*  305 */       localRectangle1.width += this.leftTextExtraWidth;
/*  306 */       Rectangle localRectangle3 = localRectangle2.union(localRectangle1);
/*  307 */       this.labelSize.height = localRectangle3.height;
/*  308 */       this.labelSize.width = localRectangle3.width;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void calcMaxWidths() {
/*  313 */     calcMaxWidth(this.checkSize, MAX_CHECK_WIDTH);
/*  314 */     calcMaxWidth(this.arrowSize, MAX_ARROW_WIDTH);
/*  315 */     calcMaxWidth(this.accSize, MAX_ACC_WIDTH);
/*      */     int i;
/*  317 */     if (this.isColumnLayout) {
/*  318 */       calcMaxWidth(this.iconSize, MAX_ICON_WIDTH);
/*  319 */       calcMaxWidth(this.textSize, MAX_TEXT_WIDTH);
/*  320 */       i = this.gap;
/*  321 */       if ((this.iconSize.getMaxWidth() == 0) || (this.textSize.getMaxWidth() == 0))
/*      */       {
/*  323 */         i = 0;
/*      */       }
/*  325 */       this.labelSize.maxWidth = calcMaxValue(MAX_LABEL_WIDTH, this.iconSize.maxWidth + this.textSize.maxWidth + i);
/*      */     }
/*      */     else
/*      */     {
/*  331 */       this.iconSize.maxWidth = getParentIntProperty(MAX_ICON_WIDTH);
/*  332 */       calcMaxWidth(this.labelSize, MAX_LABEL_WIDTH);
/*      */ 
/*  336 */       i = this.labelSize.maxWidth - this.iconSize.maxWidth;
/*  337 */       if (this.iconSize.maxWidth > 0) {
/*  338 */         i -= this.gap;
/*      */       }
/*  340 */       this.textSize.maxWidth = calcMaxValue(MAX_TEXT_WIDTH, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void calcMaxWidth(RectSize paramRectSize, Object paramObject) {
/*  345 */     paramRectSize.maxWidth = calcMaxValue(paramObject, paramRectSize.width);
/*      */   }
/*      */ 
/*      */   protected int calcMaxValue(Object paramObject, int paramInt)
/*      */   {
/*  358 */     int i = getParentIntProperty(paramObject);
/*      */ 
/*  360 */     if (paramInt > i) {
/*  361 */       if (this.miParent != null) {
/*  362 */         this.miParent.putClientProperty(paramObject, Integer.valueOf(paramInt));
/*      */       }
/*  364 */       return paramInt;
/*      */     }
/*  366 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getParentIntProperty(Object paramObject)
/*      */   {
/*  376 */     Object localObject = null;
/*  377 */     if (this.miParent != null) {
/*  378 */       localObject = this.miParent.getClientProperty(paramObject);
/*      */     }
/*  380 */     if ((localObject == null) || (!(localObject instanceof Integer))) {
/*  381 */       localObject = Integer.valueOf(0);
/*      */     }
/*  383 */     return ((Integer)localObject).intValue();
/*      */   }
/*      */ 
/*      */   public static boolean isColumnLayout(boolean paramBoolean, JMenuItem paramJMenuItem)
/*      */   {
/*  388 */     assert (paramJMenuItem != null);
/*  389 */     return isColumnLayout(paramBoolean, paramJMenuItem.getHorizontalAlignment(), paramJMenuItem.getHorizontalTextPosition(), paramJMenuItem.getVerticalTextPosition());
/*      */   }
/*      */ 
/*      */   public static boolean isColumnLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  402 */     if (paramInt3 != 0) {
/*  403 */       return false;
/*      */     }
/*  405 */     if (paramBoolean) {
/*  406 */       if ((paramInt1 != 10) && (paramInt1 != 2))
/*      */       {
/*  408 */         return false;
/*      */       }
/*  410 */       if ((paramInt2 != 11) && (paramInt2 != 4))
/*      */       {
/*  412 */         return false;
/*      */       }
/*      */     } else {
/*  415 */       if ((paramInt1 != 10) && (paramInt1 != 4))
/*      */       {
/*  417 */         return false;
/*      */       }
/*  419 */       if ((paramInt2 != 11) && (paramInt2 != 2))
/*      */       {
/*  421 */         return false;
/*      */       }
/*      */     }
/*  424 */     return true;
/*      */   }
/*      */ 
/*      */   private void calcMaxTextOffset(Rectangle paramRectangle)
/*      */   {
/*  436 */     if ((!this.isColumnLayout) || (!this.isLeftToRight)) {
/*  437 */       return;
/*      */     }
/*      */ 
/*  441 */     int i = paramRectangle.x + this.leadingGap + this.checkSize.maxWidth + this.afterCheckIconGap + this.iconSize.maxWidth + this.gap;
/*      */ 
/*  443 */     if (this.checkSize.maxWidth == 0) {
/*  444 */       i -= this.afterCheckIconGap;
/*      */     }
/*  446 */     if (this.iconSize.maxWidth == 0) {
/*  447 */       i -= this.gap;
/*      */     }
/*      */ 
/*  451 */     if (i < this.minTextOffset) {
/*  452 */       i = this.minTextOffset;
/*      */     }
/*      */ 
/*  456 */     calcMaxValue(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET, i);
/*      */   }
/*      */ 
/*      */   public LayoutResult layoutMenuItem()
/*      */   {
/*  476 */     LayoutResult localLayoutResult = createLayoutResult();
/*  477 */     prepareForLayout(localLayoutResult);
/*      */ 
/*  479 */     if (isColumnLayout()) {
/*  480 */       if (isLeftToRight())
/*  481 */         doLTRColumnLayout(localLayoutResult, getLTRColumnAlignment());
/*      */       else {
/*  483 */         doRTLColumnLayout(localLayoutResult, getRTLColumnAlignment());
/*      */       }
/*      */     }
/*  486 */     else if (isLeftToRight())
/*  487 */       doLTRComplexLayout(localLayoutResult, getLTRColumnAlignment());
/*      */     else {
/*  489 */       doRTLComplexLayout(localLayoutResult, getRTLColumnAlignment());
/*      */     }
/*      */ 
/*  493 */     alignAccCheckAndArrowVertically(localLayoutResult);
/*  494 */     return localLayoutResult;
/*      */   }
/*      */ 
/*      */   private LayoutResult createLayoutResult() {
/*  498 */     return new LayoutResult(new Rectangle(this.iconSize.width, this.iconSize.height), new Rectangle(this.textSize.width, this.textSize.height), new Rectangle(this.accSize.width, this.accSize.height), new Rectangle(this.checkSize.width, this.checkSize.height), new Rectangle(this.arrowSize.width, this.arrowSize.height), new Rectangle(this.labelSize.width, this.labelSize.height));
/*      */   }
/*      */ 
/*      */   public ColumnAlignment getLTRColumnAlignment()
/*      */   {
/*  509 */     return ColumnAlignment.LEFT_ALIGNMENT;
/*      */   }
/*      */ 
/*      */   public ColumnAlignment getRTLColumnAlignment() {
/*  513 */     return ColumnAlignment.RIGHT_ALIGNMENT;
/*      */   }
/*      */ 
/*      */   protected void prepareForLayout(LayoutResult paramLayoutResult) {
/*  517 */     paramLayoutResult.checkRect.width = this.checkSize.maxWidth;
/*  518 */     paramLayoutResult.accRect.width = this.accSize.maxWidth;
/*  519 */     paramLayoutResult.arrowRect.width = this.arrowSize.maxWidth;
/*      */   }
/*      */ 
/*      */   private void alignAccCheckAndArrowVertically(LayoutResult paramLayoutResult)
/*      */   {
/*  527 */     paramLayoutResult.accRect.y = ((int)(paramLayoutResult.labelRect.y + paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.accRect.height / 2.0F));
/*      */ 
/*  530 */     fixVerticalAlignment(paramLayoutResult, paramLayoutResult.accRect);
/*  531 */     if (this.useCheckAndArrow) {
/*  532 */       paramLayoutResult.arrowRect.y = ((int)(paramLayoutResult.labelRect.y + paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.arrowRect.height / 2.0F));
/*      */ 
/*  535 */       paramLayoutResult.checkRect.y = ((int)(paramLayoutResult.labelRect.y + paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.checkRect.height / 2.0F));
/*      */ 
/*  538 */       fixVerticalAlignment(paramLayoutResult, paramLayoutResult.arrowRect);
/*  539 */       fixVerticalAlignment(paramLayoutResult, paramLayoutResult.checkRect);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fixVerticalAlignment(LayoutResult paramLayoutResult, Rectangle paramRectangle)
/*      */   {
/*  548 */     int i = 0;
/*  549 */     if (paramRectangle.y < this.viewRect.y)
/*  550 */       i = this.viewRect.y - paramRectangle.y;
/*  551 */     else if (paramRectangle.y + paramRectangle.height > this.viewRect.y + this.viewRect.height) {
/*  552 */       i = this.viewRect.y + this.viewRect.height - paramRectangle.y - paramRectangle.height;
/*      */     }
/*  554 */     if (i != 0) {
/*  555 */       paramLayoutResult.checkRect.y += i;
/*  556 */       paramLayoutResult.iconRect.y += i;
/*  557 */       paramLayoutResult.textRect.y += i;
/*  558 */       paramLayoutResult.accRect.y += i;
/*  559 */       paramLayoutResult.arrowRect.y += i;
/*  560 */       paramLayoutResult.labelRect.y += i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doLTRColumnLayout(LayoutResult paramLayoutResult, ColumnAlignment paramColumnAlignment)
/*      */   {
/*  567 */     paramLayoutResult.iconRect.width = this.iconSize.maxWidth;
/*  568 */     paramLayoutResult.textRect.width = this.textSize.maxWidth;
/*      */ 
/*  572 */     calcXPositionsLTR(this.viewRect.x, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.checkRect, paramLayoutResult.iconRect, paramLayoutResult.textRect });
/*      */ 
/*  576 */     if (paramLayoutResult.checkRect.width > 0) {
/*  577 */       paramLayoutResult.iconRect.x += this.afterCheckIconGap - this.gap;
/*  578 */       paramLayoutResult.textRect.x += this.afterCheckIconGap - this.gap;
/*      */     }
/*      */ 
/*  581 */     calcXPositionsRTL(this.viewRect.x + this.viewRect.width, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.arrowRect, paramLayoutResult.accRect });
/*      */ 
/*  585 */     int i = paramLayoutResult.textRect.x - this.viewRect.x;
/*  586 */     if ((!this.isTopLevelMenu) && (i < this.minTextOffset)) {
/*  587 */       paramLayoutResult.textRect.x += this.minTextOffset - i;
/*      */     }
/*      */ 
/*  590 */     alignRects(paramLayoutResult, paramColumnAlignment);
/*      */ 
/*  595 */     calcTextAndIconYPositions(paramLayoutResult);
/*      */ 
/*  598 */     paramLayoutResult.setLabelRect(paramLayoutResult.textRect.union(paramLayoutResult.iconRect));
/*      */   }
/*      */ 
/*      */   private void doLTRComplexLayout(LayoutResult paramLayoutResult, ColumnAlignment paramColumnAlignment) {
/*  602 */     paramLayoutResult.labelRect.width = this.labelSize.maxWidth;
/*      */ 
/*  605 */     calcXPositionsLTR(this.viewRect.x, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.checkRect, paramLayoutResult.labelRect });
/*      */ 
/*  609 */     if (paramLayoutResult.checkRect.width > 0) {
/*  610 */       paramLayoutResult.labelRect.x += this.afterCheckIconGap - this.gap;
/*      */     }
/*      */ 
/*  613 */     calcXPositionsRTL(this.viewRect.x + this.viewRect.width, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.arrowRect, paramLayoutResult.accRect });
/*      */ 
/*  617 */     int i = paramLayoutResult.labelRect.x - this.viewRect.x;
/*  618 */     if ((!this.isTopLevelMenu) && (i < this.minTextOffset)) {
/*  619 */       paramLayoutResult.labelRect.x += this.minTextOffset - i;
/*      */     }
/*      */ 
/*  622 */     alignRects(paramLayoutResult, paramColumnAlignment);
/*      */ 
/*  625 */     calcLabelYPosition(paramLayoutResult);
/*      */ 
/*  627 */     layoutIconAndTextInLabelRect(paramLayoutResult);
/*      */   }
/*      */ 
/*      */   private void doRTLColumnLayout(LayoutResult paramLayoutResult, ColumnAlignment paramColumnAlignment)
/*      */   {
/*  633 */     paramLayoutResult.iconRect.width = this.iconSize.maxWidth;
/*  634 */     paramLayoutResult.textRect.width = this.textSize.maxWidth;
/*      */ 
/*  637 */     calcXPositionsRTL(this.viewRect.x + this.viewRect.width, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.checkRect, paramLayoutResult.iconRect, paramLayoutResult.textRect });
/*      */ 
/*  641 */     if (paramLayoutResult.checkRect.width > 0) {
/*  642 */       paramLayoutResult.iconRect.x -= this.afterCheckIconGap - this.gap;
/*  643 */       paramLayoutResult.textRect.x -= this.afterCheckIconGap - this.gap;
/*      */     }
/*      */ 
/*  646 */     calcXPositionsLTR(this.viewRect.x, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.arrowRect, paramLayoutResult.accRect });
/*      */ 
/*  650 */     int i = this.viewRect.x + this.viewRect.width - (paramLayoutResult.textRect.x + paramLayoutResult.textRect.width);
/*      */ 
/*  652 */     if ((!this.isTopLevelMenu) && (i < this.minTextOffset)) {
/*  653 */       paramLayoutResult.textRect.x -= this.minTextOffset - i;
/*      */     }
/*      */ 
/*  656 */     alignRects(paramLayoutResult, paramColumnAlignment);
/*      */ 
/*  661 */     calcTextAndIconYPositions(paramLayoutResult);
/*      */ 
/*  664 */     paramLayoutResult.setLabelRect(paramLayoutResult.textRect.union(paramLayoutResult.iconRect));
/*      */   }
/*      */ 
/*      */   private void doRTLComplexLayout(LayoutResult paramLayoutResult, ColumnAlignment paramColumnAlignment) {
/*  668 */     paramLayoutResult.labelRect.width = this.labelSize.maxWidth;
/*      */ 
/*  671 */     calcXPositionsRTL(this.viewRect.x + this.viewRect.width, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.checkRect, paramLayoutResult.labelRect });
/*      */ 
/*  675 */     if (paramLayoutResult.checkRect.width > 0) {
/*  676 */       paramLayoutResult.labelRect.x -= this.afterCheckIconGap - this.gap;
/*      */     }
/*      */ 
/*  679 */     calcXPositionsLTR(this.viewRect.x, this.leadingGap, this.gap, new Rectangle[] { paramLayoutResult.arrowRect, paramLayoutResult.accRect });
/*      */ 
/*  682 */     int i = this.viewRect.x + this.viewRect.width - (paramLayoutResult.labelRect.x + paramLayoutResult.labelRect.width);
/*      */ 
/*  684 */     if ((!this.isTopLevelMenu) && (i < this.minTextOffset)) {
/*  685 */       paramLayoutResult.labelRect.x -= this.minTextOffset - i;
/*      */     }
/*      */ 
/*  688 */     alignRects(paramLayoutResult, paramColumnAlignment);
/*      */ 
/*  691 */     calcLabelYPosition(paramLayoutResult);
/*      */ 
/*  693 */     layoutIconAndTextInLabelRect(paramLayoutResult);
/*      */   }
/*      */ 
/*      */   private void alignRects(LayoutResult paramLayoutResult, ColumnAlignment paramColumnAlignment) {
/*  697 */     alignRect(paramLayoutResult.checkRect, paramColumnAlignment.getCheckAlignment(), this.checkSize.getOrigWidth());
/*      */ 
/*  699 */     alignRect(paramLayoutResult.iconRect, paramColumnAlignment.getIconAlignment(), this.iconSize.getOrigWidth());
/*      */ 
/*  701 */     alignRect(paramLayoutResult.textRect, paramColumnAlignment.getTextAlignment(), this.textSize.getOrigWidth());
/*      */ 
/*  703 */     alignRect(paramLayoutResult.accRect, paramColumnAlignment.getAccAlignment(), this.accSize.getOrigWidth());
/*      */ 
/*  705 */     alignRect(paramLayoutResult.arrowRect, paramColumnAlignment.getArrowAlignment(), this.arrowSize.getOrigWidth());
/*      */   }
/*      */ 
/*      */   private void alignRect(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/*  710 */     if (paramInt1 == 4) {
/*  711 */       paramRectangle.x = (paramRectangle.x + paramRectangle.width - paramInt2);
/*      */     }
/*  713 */     paramRectangle.width = paramInt2;
/*      */   }
/*      */ 
/*      */   protected void layoutIconAndTextInLabelRect(LayoutResult paramLayoutResult) {
/*  717 */     paramLayoutResult.setTextRect(new Rectangle());
/*  718 */     paramLayoutResult.setIconRect(new Rectangle());
/*  719 */     SwingUtilities.layoutCompoundLabel(this.mi, this.fm, this.text, this.icon, this.verticalAlignment, this.horizontalAlignment, this.verticalTextPosition, this.horizontalTextPosition, paramLayoutResult.labelRect, paramLayoutResult.iconRect, paramLayoutResult.textRect, this.gap);
/*      */   }
/*      */ 
/*      */   private void calcXPositionsLTR(int paramInt1, int paramInt2, int paramInt3, Rectangle[] paramArrayOfRectangle)
/*      */   {
/*  727 */     int i = paramInt1 + paramInt2;
/*  728 */     for (Rectangle localRectangle : paramArrayOfRectangle) {
/*  729 */       localRectangle.x = i;
/*  730 */       if (localRectangle.width > 0)
/*  731 */         i += localRectangle.width + paramInt3;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void calcXPositionsRTL(int paramInt1, int paramInt2, int paramInt3, Rectangle[] paramArrayOfRectangle)
/*      */   {
/*  738 */     int i = paramInt1 - paramInt2;
/*  739 */     for (Rectangle localRectangle : paramArrayOfRectangle) {
/*  740 */       localRectangle.x = (i - localRectangle.width);
/*  741 */       if (localRectangle.width > 0)
/*  742 */         i -= localRectangle.width + paramInt3;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void calcTextAndIconYPositions(LayoutResult paramLayoutResult)
/*      */   {
/*  752 */     if (this.verticalAlignment == 1) {
/*  753 */       paramLayoutResult.textRect.y = ((int)(this.viewRect.y + paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.textRect.height / 2.0F));
/*      */ 
/*  756 */       paramLayoutResult.iconRect.y = ((int)(this.viewRect.y + paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.iconRect.height / 2.0F));
/*      */     }
/*  759 */     else if (this.verticalAlignment == 0) {
/*  760 */       paramLayoutResult.textRect.y = ((int)(this.viewRect.y + this.viewRect.height / 2.0F - paramLayoutResult.textRect.height / 2.0F));
/*      */ 
/*  763 */       paramLayoutResult.iconRect.y = ((int)(this.viewRect.y + this.viewRect.height / 2.0F - paramLayoutResult.iconRect.height / 2.0F));
/*      */     }
/*  767 */     else if (this.verticalAlignment == 3) {
/*  768 */       paramLayoutResult.textRect.y = ((int)(this.viewRect.y + this.viewRect.height - paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.textRect.height / 2.0F));
/*      */ 
/*  772 */       paramLayoutResult.iconRect.y = ((int)(this.viewRect.y + this.viewRect.height - paramLayoutResult.labelRect.height / 2.0F - paramLayoutResult.iconRect.height / 2.0F));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void calcLabelYPosition(LayoutResult paramLayoutResult)
/*      */   {
/*  784 */     if (this.verticalAlignment == 1)
/*  785 */       paramLayoutResult.labelRect.y = this.viewRect.y;
/*  786 */     else if (this.verticalAlignment == 0) {
/*  787 */       paramLayoutResult.labelRect.y = ((int)(this.viewRect.y + this.viewRect.height / 2.0F - paramLayoutResult.labelRect.height / 2.0F));
/*      */     }
/*  790 */     else if (this.verticalAlignment == 3)
/*  791 */       paramLayoutResult.labelRect.y = (this.viewRect.y + this.viewRect.height - paramLayoutResult.labelRect.height);
/*      */   }
/*      */ 
/*      */   public static JComponent getMenuItemParent(JMenuItem paramJMenuItem)
/*      */   {
/*  804 */     Container localContainer = paramJMenuItem.getParent();
/*  805 */     if (((localContainer instanceof JComponent)) && ((!(paramJMenuItem instanceof JMenu)) || (!((JMenu)paramJMenuItem).isTopLevelMenu())))
/*      */     {
/*  808 */       return (JComponent)localContainer;
/*      */     }
/*  810 */     return null;
/*      */   }
/*      */ 
/*      */   public static void clearUsedParentClientProperties(JMenuItem paramJMenuItem)
/*      */   {
/*  815 */     clearUsedClientProperties(getMenuItemParent(paramJMenuItem));
/*      */   }
/*      */ 
/*      */   public static void clearUsedClientProperties(JComponent paramJComponent) {
/*  819 */     if (paramJComponent != null) {
/*  820 */       paramJComponent.putClientProperty(MAX_ARROW_WIDTH, null);
/*  821 */       paramJComponent.putClientProperty(MAX_CHECK_WIDTH, null);
/*  822 */       paramJComponent.putClientProperty(MAX_ACC_WIDTH, null);
/*  823 */       paramJComponent.putClientProperty(MAX_TEXT_WIDTH, null);
/*  824 */       paramJComponent.putClientProperty(MAX_ICON_WIDTH, null);
/*  825 */       paramJComponent.putClientProperty(MAX_LABEL_WIDTH, null);
/*  826 */       paramJComponent.putClientProperty(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int max(int[] paramArrayOfInt)
/*      */   {
/*  836 */     int i = -2147483648;
/*  837 */     for (int m : paramArrayOfInt) {
/*  838 */       if (m > i) {
/*  839 */         i = m;
/*      */       }
/*      */     }
/*  842 */     return i;
/*      */   }
/*      */ 
/*      */   public static Rectangle createMaxRect() {
/*  846 */     return new Rectangle(0, 0, 2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   public static void addMaxWidth(RectSize paramRectSize, int paramInt, Dimension paramDimension) {
/*  850 */     if (paramRectSize.maxWidth > 0)
/*  851 */       paramDimension.width += paramRectSize.maxWidth + paramInt;
/*      */   }
/*      */ 
/*      */   public static void addWidth(int paramInt1, int paramInt2, Dimension paramDimension)
/*      */   {
/*  856 */     if (paramInt1 > 0)
/*  857 */       paramDimension.width += paramInt1 + paramInt2;
/*      */   }
/*      */ 
/*      */   public JMenuItem getMenuItem()
/*      */   {
/*  862 */     return this.mi;
/*      */   }
/*      */ 
/*      */   public JComponent getMenuItemParent() {
/*  866 */     return this.miParent;
/*      */   }
/*      */ 
/*      */   public Font getFont() {
/*  870 */     return this.font;
/*      */   }
/*      */ 
/*      */   public Font getAccFont() {
/*  874 */     return this.accFont;
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics() {
/*  878 */     return this.fm;
/*      */   }
/*      */ 
/*      */   public FontMetrics getAccFontMetrics() {
/*  882 */     return this.accFm;
/*      */   }
/*      */ 
/*      */   public Icon getIcon() {
/*  886 */     return this.icon;
/*      */   }
/*      */ 
/*      */   public Icon getCheckIcon() {
/*  890 */     return this.checkIcon;
/*      */   }
/*      */ 
/*      */   public Icon getArrowIcon() {
/*  894 */     return this.arrowIcon;
/*      */   }
/*      */ 
/*      */   public String getText() {
/*  898 */     return this.text;
/*      */   }
/*      */ 
/*      */   public String getAccText() {
/*  902 */     return this.accText;
/*      */   }
/*      */ 
/*      */   public boolean isColumnLayout() {
/*  906 */     return this.isColumnLayout;
/*      */   }
/*      */ 
/*      */   public boolean useCheckAndArrow() {
/*  910 */     return this.useCheckAndArrow;
/*      */   }
/*      */ 
/*      */   public boolean isLeftToRight() {
/*  914 */     return this.isLeftToRight;
/*      */   }
/*      */ 
/*      */   public boolean isTopLevelMenu() {
/*  918 */     return this.isTopLevelMenu;
/*      */   }
/*      */ 
/*      */   public View getHtmlView() {
/*  922 */     return this.htmlView;
/*      */   }
/*      */ 
/*      */   public int getVerticalAlignment() {
/*  926 */     return this.verticalAlignment;
/*      */   }
/*      */ 
/*      */   public int getHorizontalAlignment() {
/*  930 */     return this.horizontalAlignment;
/*      */   }
/*      */ 
/*      */   public int getVerticalTextPosition() {
/*  934 */     return this.verticalTextPosition;
/*      */   }
/*      */ 
/*      */   public int getHorizontalTextPosition() {
/*  938 */     return this.horizontalTextPosition;
/*      */   }
/*      */ 
/*      */   public int getGap() {
/*  942 */     return this.gap;
/*      */   }
/*      */ 
/*      */   public int getLeadingGap() {
/*  946 */     return this.leadingGap;
/*      */   }
/*      */ 
/*      */   public int getAfterCheckIconGap() {
/*  950 */     return this.afterCheckIconGap;
/*      */   }
/*      */ 
/*      */   public int getMinTextOffset() {
/*  954 */     return this.minTextOffset;
/*      */   }
/*      */ 
/*      */   public Rectangle getViewRect() {
/*  958 */     return this.viewRect;
/*      */   }
/*      */ 
/*      */   public RectSize getIconSize() {
/*  962 */     return this.iconSize;
/*      */   }
/*      */ 
/*      */   public RectSize getTextSize() {
/*  966 */     return this.textSize;
/*      */   }
/*      */ 
/*      */   public RectSize getAccSize() {
/*  970 */     return this.accSize;
/*      */   }
/*      */ 
/*      */   public RectSize getCheckSize() {
/*  974 */     return this.checkSize;
/*      */   }
/*      */ 
/*      */   public RectSize getArrowSize() {
/*  978 */     return this.arrowSize;
/*      */   }
/*      */ 
/*      */   public RectSize getLabelSize() {
/*  982 */     return this.labelSize;
/*      */   }
/*      */ 
/*      */   protected void setMenuItem(JMenuItem paramJMenuItem) {
/*  986 */     this.mi = paramJMenuItem;
/*      */   }
/*      */ 
/*      */   protected void setMenuItemParent(JComponent paramJComponent) {
/*  990 */     this.miParent = paramJComponent;
/*      */   }
/*      */ 
/*      */   protected void setFont(Font paramFont) {
/*  994 */     this.font = paramFont;
/*      */   }
/*      */ 
/*      */   protected void setAccFont(Font paramFont) {
/*  998 */     this.accFont = paramFont;
/*      */   }
/*      */ 
/*      */   protected void setFontMetrics(FontMetrics paramFontMetrics) {
/* 1002 */     this.fm = paramFontMetrics;
/*      */   }
/*      */ 
/*      */   protected void setAccFontMetrics(FontMetrics paramFontMetrics) {
/* 1006 */     this.accFm = paramFontMetrics;
/*      */   }
/*      */ 
/*      */   protected void setIcon(Icon paramIcon) {
/* 1010 */     this.icon = paramIcon;
/*      */   }
/*      */ 
/*      */   protected void setCheckIcon(Icon paramIcon) {
/* 1014 */     this.checkIcon = paramIcon;
/*      */   }
/*      */ 
/*      */   protected void setArrowIcon(Icon paramIcon) {
/* 1018 */     this.arrowIcon = paramIcon;
/*      */   }
/*      */ 
/*      */   protected void setText(String paramString) {
/* 1022 */     this.text = paramString;
/*      */   }
/*      */ 
/*      */   protected void setAccText(String paramString) {
/* 1026 */     this.accText = paramString;
/*      */   }
/*      */ 
/*      */   protected void setColumnLayout(boolean paramBoolean) {
/* 1030 */     this.isColumnLayout = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void setUseCheckAndArrow(boolean paramBoolean) {
/* 1034 */     this.useCheckAndArrow = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void setLeftToRight(boolean paramBoolean) {
/* 1038 */     this.isLeftToRight = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void setTopLevelMenu(boolean paramBoolean) {
/* 1042 */     this.isTopLevelMenu = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void setHtmlView(View paramView) {
/* 1046 */     this.htmlView = paramView;
/*      */   }
/*      */ 
/*      */   protected void setVerticalAlignment(int paramInt) {
/* 1050 */     this.verticalAlignment = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setHorizontalAlignment(int paramInt) {
/* 1054 */     this.horizontalAlignment = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setVerticalTextPosition(int paramInt) {
/* 1058 */     this.verticalTextPosition = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setHorizontalTextPosition(int paramInt) {
/* 1062 */     this.horizontalTextPosition = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setGap(int paramInt) {
/* 1066 */     this.gap = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setLeadingGap(int paramInt) {
/* 1070 */     this.leadingGap = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setAfterCheckIconGap(int paramInt) {
/* 1074 */     this.afterCheckIconGap = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setMinTextOffset(int paramInt) {
/* 1078 */     this.minTextOffset = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setViewRect(Rectangle paramRectangle) {
/* 1082 */     this.viewRect = paramRectangle;
/*      */   }
/*      */ 
/*      */   protected void setIconSize(RectSize paramRectSize) {
/* 1086 */     this.iconSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   protected void setTextSize(RectSize paramRectSize) {
/* 1090 */     this.textSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   protected void setAccSize(RectSize paramRectSize) {
/* 1094 */     this.accSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   protected void setCheckSize(RectSize paramRectSize) {
/* 1098 */     this.checkSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   protected void setArrowSize(RectSize paramRectSize) {
/* 1102 */     this.arrowSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   protected void setLabelSize(RectSize paramRectSize) {
/* 1106 */     this.labelSize = paramRectSize;
/*      */   }
/*      */ 
/*      */   public int getLeftTextExtraWidth() {
/* 1110 */     return this.leftTextExtraWidth;
/*      */   }
/*      */ 
/*      */   public static boolean useCheckAndArrow(JMenuItem paramJMenuItem)
/*      */   {
/* 1118 */     boolean bool = true;
/* 1119 */     if (((paramJMenuItem instanceof JMenu)) && (((JMenu)paramJMenuItem).isTopLevelMenu()))
/*      */     {
/* 1121 */       bool = false;
/*      */     }
/* 1123 */     return bool;
/*      */   }
/*      */ 
/*      */   public static class ColumnAlignment
/*      */   {
/*      */     private int checkAlignment;
/*      */     private int iconAlignment;
/*      */     private int textAlignment;
/*      */     private int accAlignment;
/*      */     private int arrowAlignment;
/* 1221 */     public static final ColumnAlignment LEFT_ALIGNMENT = new ColumnAlignment(2, 2, 2, 2, 2);
/*      */ 
/* 1230 */     public static final ColumnAlignment RIGHT_ALIGNMENT = new ColumnAlignment(4, 4, 4, 4, 4);
/*      */ 
/*      */     public ColumnAlignment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/* 1242 */       this.checkAlignment = paramInt1;
/* 1243 */       this.iconAlignment = paramInt2;
/* 1244 */       this.textAlignment = paramInt3;
/* 1245 */       this.accAlignment = paramInt4;
/* 1246 */       this.arrowAlignment = paramInt5;
/*      */     }
/*      */ 
/*      */     public int getCheckAlignment() {
/* 1250 */       return this.checkAlignment;
/*      */     }
/*      */ 
/*      */     public int getIconAlignment() {
/* 1254 */       return this.iconAlignment;
/*      */     }
/*      */ 
/*      */     public int getTextAlignment() {
/* 1258 */       return this.textAlignment;
/*      */     }
/*      */ 
/*      */     public int getAccAlignment() {
/* 1262 */       return this.accAlignment;
/*      */     }
/*      */ 
/*      */     public int getArrowAlignment() {
/* 1266 */       return this.arrowAlignment;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class LayoutResult
/*      */   {
/*      */     private Rectangle iconRect;
/*      */     private Rectangle textRect;
/*      */     private Rectangle accRect;
/*      */     private Rectangle checkRect;
/*      */     private Rectangle arrowRect;
/*      */     private Rectangle labelRect;
/*      */ 
/*      */     public LayoutResult()
/*      */     {
/* 1135 */       this.iconRect = new Rectangle();
/* 1136 */       this.textRect = new Rectangle();
/* 1137 */       this.accRect = new Rectangle();
/* 1138 */       this.checkRect = new Rectangle();
/* 1139 */       this.arrowRect = new Rectangle();
/* 1140 */       this.labelRect = new Rectangle();
/*      */     }
/*      */ 
/*      */     public LayoutResult(Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, Rectangle paramRectangle4, Rectangle paramRectangle5, Rectangle paramRectangle6)
/*      */     {
/* 1146 */       this.iconRect = paramRectangle1;
/* 1147 */       this.textRect = paramRectangle2;
/* 1148 */       this.accRect = paramRectangle3;
/* 1149 */       this.checkRect = paramRectangle4;
/* 1150 */       this.arrowRect = paramRectangle5;
/* 1151 */       this.labelRect = paramRectangle6;
/*      */     }
/*      */ 
/*      */     public Rectangle getIconRect() {
/* 1155 */       return this.iconRect;
/*      */     }
/*      */ 
/*      */     public void setIconRect(Rectangle paramRectangle) {
/* 1159 */       this.iconRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Rectangle getTextRect() {
/* 1163 */       return this.textRect;
/*      */     }
/*      */ 
/*      */     public void setTextRect(Rectangle paramRectangle) {
/* 1167 */       this.textRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Rectangle getAccRect() {
/* 1171 */       return this.accRect;
/*      */     }
/*      */ 
/*      */     public void setAccRect(Rectangle paramRectangle) {
/* 1175 */       this.accRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Rectangle getCheckRect() {
/* 1179 */       return this.checkRect;
/*      */     }
/*      */ 
/*      */     public void setCheckRect(Rectangle paramRectangle) {
/* 1183 */       this.checkRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Rectangle getArrowRect() {
/* 1187 */       return this.arrowRect;
/*      */     }
/*      */ 
/*      */     public void setArrowRect(Rectangle paramRectangle) {
/* 1191 */       this.arrowRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Rectangle getLabelRect() {
/* 1195 */       return this.labelRect;
/*      */     }
/*      */ 
/*      */     public void setLabelRect(Rectangle paramRectangle) {
/* 1199 */       this.labelRect = paramRectangle;
/*      */     }
/*      */ 
/*      */     public Map<String, Rectangle> getAllRects() {
/* 1203 */       HashMap localHashMap = new HashMap();
/* 1204 */       localHashMap.put("checkRect", this.checkRect);
/* 1205 */       localHashMap.put("iconRect", this.iconRect);
/* 1206 */       localHashMap.put("textRect", this.textRect);
/* 1207 */       localHashMap.put("accRect", this.accRect);
/* 1208 */       localHashMap.put("arrowRect", this.arrowRect);
/* 1209 */       localHashMap.put("labelRect", this.labelRect);
/* 1210 */       return localHashMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class RectSize
/*      */   {
/*      */     private int width;
/*      */     private int height;
/*      */     private int origWidth;
/*      */     private int maxWidth;
/*      */ 
/*      */     public RectSize()
/*      */     {
/*      */     }
/*      */ 
/*      */     public RectSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1280 */       this.width = paramInt1;
/* 1281 */       this.height = paramInt2;
/* 1282 */       this.origWidth = paramInt3;
/* 1283 */       this.maxWidth = paramInt4;
/*      */     }
/*      */ 
/*      */     public int getWidth() {
/* 1287 */       return this.width;
/*      */     }
/*      */ 
/*      */     public int getHeight() {
/* 1291 */       return this.height;
/*      */     }
/*      */ 
/*      */     public int getOrigWidth() {
/* 1295 */       return this.origWidth;
/*      */     }
/*      */ 
/*      */     public int getMaxWidth() {
/* 1299 */       return this.maxWidth;
/*      */     }
/*      */ 
/*      */     public void setWidth(int paramInt) {
/* 1303 */       this.width = paramInt;
/*      */     }
/*      */ 
/*      */     public void setHeight(int paramInt) {
/* 1307 */       this.height = paramInt;
/*      */     }
/*      */ 
/*      */     public void setOrigWidth(int paramInt) {
/* 1311 */       this.origWidth = paramInt;
/*      */     }
/*      */ 
/*      */     public void setMaxWidth(int paramInt) {
/* 1315 */       this.maxWidth = paramInt;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1319 */       return "[w=" + this.width + ",h=" + this.height + ",ow=" + this.origWidth + ",mw=" + this.maxWidth + "]";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.MenuItemLayoutHelper
 * JD-Core Version:    0.6.2
 */