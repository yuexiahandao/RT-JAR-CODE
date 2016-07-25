/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingConstants;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI.BorderListener;
/*     */ 
/*     */ public class MetalInternalFrameUI extends BasicInternalFrameUI
/*     */ {
/*  46 */   private static final PropertyChangeListener metalPropertyChangeListener = new MetalPropertyChangeHandler(null);
/*     */ 
/*  49 */   private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
/*     */ 
/*  51 */   protected static String IS_PALETTE = "JInternalFrame.isPalette";
/*  52 */   private static String IS_PALETTE_KEY = "JInternalFrame.isPalette";
/*  53 */   private static String FRAME_TYPE = "JInternalFrame.frameType";
/*  54 */   private static String NORMAL_FRAME = "normal";
/*  55 */   private static String PALETTE_FRAME = "palette";
/*  56 */   private static String OPTION_DIALOG = "optionDialog";
/*     */ 
/*     */   public MetalInternalFrameUI(JInternalFrame paramJInternalFrame) {
/*  59 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  63 */     return new MetalInternalFrameUI((JInternalFrame)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  67 */     super.installUI(paramJComponent);
/*     */ 
/*  69 */     Object localObject = paramJComponent.getClientProperty(IS_PALETTE_KEY);
/*  70 */     if (localObject != null) {
/*  71 */       setPalette(((Boolean)localObject).booleanValue());
/*     */     }
/*     */ 
/*  74 */     Container localContainer = this.frame.getContentPane();
/*  75 */     stripContentBorder(localContainer);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  80 */     this.frame = ((JInternalFrame)paramJComponent);
/*     */ 
/*  82 */     Container localContainer = ((JInternalFrame)paramJComponent).getContentPane();
/*  83 */     if ((localContainer instanceof JComponent)) {
/*  84 */       JComponent localJComponent = (JComponent)localContainer;
/*  85 */       if (localJComponent.getBorder() == handyEmptyBorder) {
/*  86 */         localJComponent.setBorder(null);
/*     */       }
/*     */     }
/*  89 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  93 */     super.installListeners();
/*  94 */     this.frame.addPropertyChangeListener(metalPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/*  98 */     this.frame.removePropertyChangeListener(metalPropertyChangeListener);
/*  99 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions() {
/* 103 */     super.installKeyboardActions();
/* 104 */     ActionMap localActionMap = SwingUtilities.getUIActionMap(this.frame);
/* 105 */     if (localActionMap != null)
/*     */     {
/* 108 */       localActionMap.remove("showSystemMenu");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 113 */     super.uninstallKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents() {
/* 117 */     this.titlePane = null;
/* 118 */     super.uninstallComponents();
/*     */   }
/*     */ 
/*     */   private void stripContentBorder(Object paramObject) {
/* 122 */     if ((paramObject instanceof JComponent)) {
/* 123 */       JComponent localJComponent = (JComponent)paramObject;
/* 124 */       Border localBorder = localJComponent.getBorder();
/* 125 */       if ((localBorder == null) || ((localBorder instanceof UIResource)))
/* 126 */         localJComponent.setBorder(handyEmptyBorder);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JComponent createNorthPane(JInternalFrame paramJInternalFrame)
/*     */   {
/* 133 */     return new MetalInternalFrameTitlePane(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   private void setFrameType(String paramString)
/*     */   {
/* 139 */     if (paramString.equals(OPTION_DIALOG))
/*     */     {
/* 141 */       LookAndFeel.installBorder(this.frame, "InternalFrame.optionDialogBorder");
/* 142 */       ((MetalInternalFrameTitlePane)this.titlePane).setPalette(false);
/*     */     }
/* 144 */     else if (paramString.equals(PALETTE_FRAME))
/*     */     {
/* 146 */       LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
/* 147 */       ((MetalInternalFrameTitlePane)this.titlePane).setPalette(true);
/*     */     }
/*     */     else
/*     */     {
/* 151 */       LookAndFeel.installBorder(this.frame, "InternalFrame.border");
/* 152 */       ((MetalInternalFrameTitlePane)this.titlePane).setPalette(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPalette(boolean paramBoolean)
/*     */   {
/* 158 */     if (paramBoolean)
/* 159 */       LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
/*     */     else {
/* 161 */       LookAndFeel.installBorder(this.frame, "InternalFrame.border");
/*     */     }
/* 163 */     ((MetalInternalFrameTitlePane)this.titlePane).setPalette(paramBoolean);
/*     */   }
/*     */ 
/*     */   protected MouseInputAdapter createBorderListener(JInternalFrame paramJInternalFrame)
/*     */   {
/* 253 */     return new BorderListener1(null);
/*     */   }
/*     */ 
/*     */   private class BorderListener1 extends BasicInternalFrameUI.BorderListener
/*     */     implements SwingConstants
/*     */   {
/*     */     private BorderListener1()
/*     */     {
/* 205 */       super();
/*     */     }
/*     */ 
/*     */     Rectangle getIconBounds() {
/* 209 */       boolean bool = MetalUtils.isLeftToRight(MetalInternalFrameUI.this.frame);
/* 210 */       int i = bool ? 5 : MetalInternalFrameUI.this.titlePane.getWidth() - 5;
/* 211 */       Rectangle localRectangle = null;
/*     */ 
/* 213 */       Icon localIcon = MetalInternalFrameUI.this.frame.getFrameIcon();
/* 214 */       if (localIcon != null) {
/* 215 */         if (!bool) {
/* 216 */           i -= localIcon.getIconWidth();
/*     */         }
/* 218 */         int j = MetalInternalFrameUI.this.titlePane.getHeight() / 2 - localIcon.getIconHeight() / 2;
/* 219 */         localRectangle = new Rectangle(i, j, localIcon.getIconWidth(), localIcon.getIconHeight());
/*     */       }
/*     */ 
/* 222 */       return localRectangle;
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 226 */       if ((paramMouseEvent.getClickCount() == 2) && (paramMouseEvent.getSource() == MetalInternalFrameUI.this.getNorthPane()) && (MetalInternalFrameUI.this.frame.isClosable()) && (!MetalInternalFrameUI.this.frame.isIcon()))
/*     */       {
/* 228 */         Rectangle localRectangle = getIconBounds();
/* 229 */         if ((localRectangle != null) && (localRectangle.contains(paramMouseEvent.getX(), paramMouseEvent.getY()))) {
/* 230 */           MetalInternalFrameUI.this.frame.doDefaultCloseAction();
/*     */         }
/*     */         else
/* 233 */           super.mouseClicked(paramMouseEvent);
/*     */       }
/*     */       else
/*     */       {
/* 237 */         super.mouseClicked(paramMouseEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MetalPropertyChangeHandler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 172 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 173 */       JInternalFrame localJInternalFrame = (JInternalFrame)paramPropertyChangeEvent.getSource();
/*     */ 
/* 175 */       if (!(localJInternalFrame.getUI() instanceof MetalInternalFrameUI)) {
/* 176 */         return;
/*     */       }
/*     */ 
/* 179 */       MetalInternalFrameUI localMetalInternalFrameUI = (MetalInternalFrameUI)localJInternalFrame.getUI();
/*     */ 
/* 181 */       if (str.equals(MetalInternalFrameUI.FRAME_TYPE))
/*     */       {
/* 183 */         if ((paramPropertyChangeEvent.getNewValue() instanceof String))
/*     */         {
/* 185 */           localMetalInternalFrameUI.setFrameType((String)paramPropertyChangeEvent.getNewValue());
/*     */         }
/*     */       }
/* 188 */       else if (str.equals(MetalInternalFrameUI.IS_PALETTE_KEY))
/*     */       {
/* 190 */         if (paramPropertyChangeEvent.getNewValue() != null)
/*     */         {
/* 192 */           localMetalInternalFrameUI.setPalette(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/*     */         }
/*     */         else
/*     */         {
/* 196 */           localMetalInternalFrameUI.setPalette(false);
/*     */         }
/* 198 */       } else if (str.equals("contentPane"))
/* 199 */         localMetalInternalFrameUI.stripContentBorder(paramPropertyChangeEvent.getNewValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalInternalFrameUI
 * JD-Core Version:    0.6.2
 */