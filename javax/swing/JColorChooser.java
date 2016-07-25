/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.colorchooser.AbstractColorChooserPanel;
/*     */ import javax.swing.colorchooser.ColorChooserComponentFactory;
/*     */ import javax.swing.colorchooser.ColorSelectionModel;
/*     */ import javax.swing.colorchooser.DefaultColorSelectionModel;
/*     */ import javax.swing.plaf.ColorChooserUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JColorChooser extends JComponent
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "ColorChooserUI";
/*     */   private ColorSelectionModel selectionModel;
/*  96 */   private JComponent previewPanel = ColorChooserComponentFactory.getPreviewPanel();
/*     */ 
/*  98 */   private AbstractColorChooserPanel[] chooserPanels = new AbstractColorChooserPanel[0];
/*     */   private boolean dragEnabled;
/*     */   public static final String SELECTION_MODEL_PROPERTY = "selectionModel";
/*     */   public static final String PREVIEW_PANEL_PROPERTY = "previewPanel";
/*     */   public static final String CHOOSER_PANELS_PROPERTY = "chooserPanels";
/* 561 */   protected AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public static Color showDialog(Component paramComponent, String paramString, Color paramColor)
/*     */     throws HeadlessException
/*     */   {
/* 137 */     JColorChooser localJColorChooser = new JColorChooser(paramColor != null ? paramColor : Color.white);
/*     */ 
/* 140 */     ColorTracker localColorTracker = new ColorTracker(localJColorChooser);
/* 141 */     JDialog localJDialog = createDialog(paramComponent, paramString, true, localJColorChooser, localColorTracker, null);
/*     */ 
/* 143 */     localJDialog.addComponentListener(new ColorChooserDialog.DisposeOnClose());
/*     */ 
/* 145 */     localJDialog.show();
/*     */ 
/* 147 */     return localColorTracker.getColor();
/*     */   }
/*     */ 
/*     */   public static JDialog createDialog(Component paramComponent, String paramString, boolean paramBoolean, JColorChooser paramJColorChooser, ActionListener paramActionListener1, ActionListener paramActionListener2)
/*     */     throws HeadlessException
/*     */   {
/* 176 */     Window localWindow = JOptionPane.getWindowForComponent(paramComponent);
/*     */     ColorChooserDialog localColorChooserDialog;
/* 178 */     if ((localWindow instanceof Frame)) {
/* 179 */       localColorChooserDialog = new ColorChooserDialog((Frame)localWindow, paramString, paramBoolean, paramComponent, paramJColorChooser, paramActionListener1, paramActionListener2);
/*     */     }
/*     */     else {
/* 182 */       localColorChooserDialog = new ColorChooserDialog((Dialog)localWindow, paramString, paramBoolean, paramComponent, paramJColorChooser, paramActionListener1, paramActionListener2);
/*     */     }
/*     */ 
/* 185 */     localColorChooserDialog.getAccessibleContext().setAccessibleDescription(paramString);
/* 186 */     return localColorChooserDialog;
/*     */   }
/*     */ 
/*     */   public JColorChooser()
/*     */   {
/* 193 */     this(Color.white);
/*     */   }
/*     */ 
/*     */   public JColorChooser(Color paramColor)
/*     */   {
/* 202 */     this(new DefaultColorSelectionModel(paramColor));
/*     */   }
/*     */ 
/*     */   public JColorChooser(ColorSelectionModel paramColorSelectionModel)
/*     */   {
/* 213 */     this.selectionModel = paramColorSelectionModel;
/* 214 */     updateUI();
/* 215 */     this.dragEnabled = false;
/*     */   }
/*     */ 
/*     */   public ColorChooserUI getUI()
/*     */   {
/* 225 */     return (ColorChooserUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(ColorChooserUI paramColorChooserUI)
/*     */   {
/* 240 */     super.setUI(paramColorChooserUI);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 251 */     setUI((ColorChooserUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 262 */     return "ColorChooserUI";
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 272 */     return this.selectionModel.getSelectedColor();
/*     */   }
/*     */ 
/*     */   public void setColor(Color paramColor)
/*     */   {
/* 287 */     this.selectionModel.setSelectedColor(paramColor);
/*     */   }
/*     */ 
/*     */   public void setColor(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 303 */     setColor(new Color(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public void setColor(int paramInt)
/*     */   {
/* 316 */     setColor(paramInt >> 16 & 0xFF, paramInt >> 8 & 0xFF, paramInt & 0xFF);
/*     */   }
/*     */ 
/*     */   public void setDragEnabled(boolean paramBoolean)
/*     */   {
/* 359 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/* 360 */       throw new HeadlessException();
/*     */     }
/* 362 */     this.dragEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getDragEnabled()
/*     */   {
/* 373 */     return this.dragEnabled;
/*     */   }
/*     */ 
/*     */   public void setPreviewPanel(JComponent paramJComponent)
/*     */   {
/* 391 */     if (this.previewPanel != paramJComponent) {
/* 392 */       JComponent localJComponent = this.previewPanel;
/* 393 */       this.previewPanel = paramJComponent;
/* 394 */       firePropertyChange("previewPanel", localJComponent, paramJComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JComponent getPreviewPanel()
/*     */   {
/* 404 */     return this.previewPanel;
/*     */   }
/*     */ 
/*     */   public void addChooserPanel(AbstractColorChooserPanel paramAbstractColorChooserPanel)
/*     */   {
/* 413 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel1 = getChooserPanels();
/* 414 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel2 = new AbstractColorChooserPanel[arrayOfAbstractColorChooserPanel1.length + 1];
/* 415 */     System.arraycopy(arrayOfAbstractColorChooserPanel1, 0, arrayOfAbstractColorChooserPanel2, 0, arrayOfAbstractColorChooserPanel1.length);
/* 416 */     arrayOfAbstractColorChooserPanel2[(arrayOfAbstractColorChooserPanel2.length - 1)] = paramAbstractColorChooserPanel;
/* 417 */     setChooserPanels(arrayOfAbstractColorChooserPanel2);
/*     */   }
/*     */ 
/*     */   public AbstractColorChooserPanel removeChooserPanel(AbstractColorChooserPanel paramAbstractColorChooserPanel)
/*     */   {
/* 431 */     int i = -1;
/*     */ 
/* 433 */     for (int j = 0; j < this.chooserPanels.length; j++) {
/* 434 */       if (this.chooserPanels[j] == paramAbstractColorChooserPanel) {
/* 435 */         i = j;
/* 436 */         break;
/*     */       }
/*     */     }
/* 439 */     if (i == -1) {
/* 440 */       throw new IllegalArgumentException("chooser panel not in this chooser");
/*     */     }
/*     */ 
/* 443 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel = new AbstractColorChooserPanel[this.chooserPanels.length - 1];
/*     */ 
/* 445 */     if (i == this.chooserPanels.length - 1) {
/* 446 */       System.arraycopy(this.chooserPanels, 0, arrayOfAbstractColorChooserPanel, 0, arrayOfAbstractColorChooserPanel.length);
/*     */     }
/* 448 */     else if (i == 0) {
/* 449 */       System.arraycopy(this.chooserPanels, 1, arrayOfAbstractColorChooserPanel, 0, arrayOfAbstractColorChooserPanel.length);
/*     */     }
/*     */     else {
/* 452 */       System.arraycopy(this.chooserPanels, 0, arrayOfAbstractColorChooserPanel, 0, i);
/* 453 */       System.arraycopy(this.chooserPanels, i + 1, arrayOfAbstractColorChooserPanel, i, this.chooserPanels.length - i - 1);
/*     */     }
/*     */ 
/* 457 */     setChooserPanels(arrayOfAbstractColorChooserPanel);
/*     */ 
/* 459 */     return paramAbstractColorChooserPanel;
/*     */   }
/*     */ 
/*     */   public void setChooserPanels(AbstractColorChooserPanel[] paramArrayOfAbstractColorChooserPanel)
/*     */   {
/* 475 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel = this.chooserPanels;
/* 476 */     this.chooserPanels = paramArrayOfAbstractColorChooserPanel;
/* 477 */     firePropertyChange("chooserPanels", arrayOfAbstractColorChooserPanel, paramArrayOfAbstractColorChooserPanel);
/*     */   }
/*     */ 
/*     */   public AbstractColorChooserPanel[] getChooserPanels()
/*     */   {
/* 486 */     return this.chooserPanels;
/*     */   }
/*     */ 
/*     */   public ColorSelectionModel getSelectionModel()
/*     */   {
/* 495 */     return this.selectionModel;
/*     */   }
/*     */ 
/*     */   public void setSelectionModel(ColorSelectionModel paramColorSelectionModel)
/*     */   {
/* 510 */     ColorSelectionModel localColorSelectionModel = this.selectionModel;
/* 511 */     this.selectionModel = paramColorSelectionModel;
/* 512 */     firePropertyChange("selectionModel", localColorSelectionModel, paramColorSelectionModel);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 522 */     paramObjectOutputStream.defaultWriteObject();
/* 523 */     if (getUIClassID().equals("ColorChooserUI")) {
/* 524 */       byte b = JComponent.getWriteObjCounter(this);
/* 525 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 526 */       if ((b == 0) && (this.ui != null))
/* 527 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 544 */     StringBuffer localStringBuffer = new StringBuffer("");
/* 545 */     for (int i = 0; i < this.chooserPanels.length; i++) {
/* 546 */       localStringBuffer.append("[" + this.chooserPanels[i].toString() + "]");
/*     */     }
/*     */ 
/* 549 */     String str = this.previewPanel != null ? this.previewPanel.toString() : "";
/*     */ 
/* 552 */     return super.paramString() + ",chooserPanels=" + localStringBuffer.toString() + ",previewPanel=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 573 */     if (this.accessibleContext == null) {
/* 574 */       this.accessibleContext = new AccessibleJColorChooser();
/*     */     }
/* 576 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJColorChooser extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJColorChooser()
/*     */     {
/* 585 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 595 */       return AccessibleRole.COLOR_CHOOSER;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JColorChooser
 * JD-Core Version:    0.6.2
 */