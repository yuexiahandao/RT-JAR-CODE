/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import javax.swing.colorchooser.AbstractColorChooserPanel;
/*     */ import javax.swing.colorchooser.ColorChooserComponentFactory;
/*     */ import javax.swing.colorchooser.ColorSelectionModel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ColorChooserUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ public class BasicColorChooserUI extends ColorChooserUI
/*     */ {
/*     */   protected JColorChooser chooser;
/*     */   JTabbedPane tabbedPane;
/*     */   JPanel singlePanel;
/*     */   JPanel previewPanelHolder;
/*     */   JComponent previewPanel;
/*     */   boolean isMultiPanel;
/*  62 */   private static TransferHandler defaultTransferHandler = new ColorTransferHandler();
/*     */   protected AbstractColorChooserPanel[] defaultChoosers;
/*     */   protected ChangeListener previewListener;
/*     */   protected PropertyChangeListener propertyChangeListener;
/*     */   private Handler handler;
/*     */ 
/*     */   public BasicColorChooserUI()
/*     */   {
/*  61 */     this.isMultiPanel = false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  71 */     return new BasicColorChooserUI();
/*     */   }
/*     */ 
/*     */   protected AbstractColorChooserPanel[] createDefaultChoosers() {
/*  75 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel = ColorChooserComponentFactory.getDefaultChooserPanels();
/*  76 */     return arrayOfAbstractColorChooserPanel;
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaultChoosers() {
/*  80 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel = this.chooser.getChooserPanels();
/*  81 */     for (int i = 0; i < arrayOfAbstractColorChooserPanel.length; i++)
/*  82 */       this.chooser.removeChooserPanel(arrayOfAbstractColorChooserPanel[i]);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  87 */     this.chooser = ((JColorChooser)paramJComponent);
/*     */ 
/*  89 */     super.installUI(paramJComponent);
/*     */ 
/*  91 */     installDefaults();
/*  92 */     installListeners();
/*     */ 
/*  94 */     this.tabbedPane = new JTabbedPane();
/*  95 */     this.tabbedPane.setName("ColorChooser.tabPane");
/*  96 */     this.tabbedPane.setInheritsPopupMenu(true);
/*  97 */     this.tabbedPane.getAccessibleContext().setAccessibleDescription(this.tabbedPane.getName());
/*  98 */     this.singlePanel = new JPanel(new CenterLayout());
/*  99 */     this.singlePanel.setName("ColorChooser.panel");
/* 100 */     this.singlePanel.setInheritsPopupMenu(true);
/*     */ 
/* 102 */     this.chooser.setLayout(new BorderLayout());
/*     */ 
/* 104 */     this.defaultChoosers = createDefaultChoosers();
/* 105 */     this.chooser.setChooserPanels(this.defaultChoosers);
/*     */ 
/* 107 */     this.previewPanelHolder = new JPanel(new CenterLayout());
/* 108 */     this.previewPanelHolder.setName("ColorChooser.previewPanelHolder");
/*     */ 
/* 110 */     if (DefaultLookup.getBoolean(this.chooser, this, "ColorChooser.showPreviewPanelText", true))
/*     */     {
/* 112 */       String str = UIManager.getString("ColorChooser.previewText", this.chooser.getLocale());
/*     */ 
/* 114 */       this.previewPanelHolder.setBorder(new TitledBorder(str));
/*     */     }
/* 116 */     this.previewPanelHolder.setInheritsPopupMenu(true);
/*     */ 
/* 118 */     installPreviewPanel();
/* 119 */     this.chooser.applyComponentOrientation(paramJComponent.getComponentOrientation());
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/* 123 */     this.chooser.remove(this.tabbedPane);
/* 124 */     this.chooser.remove(this.singlePanel);
/* 125 */     this.chooser.remove(this.previewPanelHolder);
/*     */ 
/* 127 */     uninstallDefaultChoosers();
/* 128 */     uninstallListeners();
/* 129 */     uninstallPreviewPanel();
/* 130 */     uninstallDefaults();
/*     */ 
/* 132 */     this.previewPanelHolder = null;
/* 133 */     this.previewPanel = null;
/* 134 */     this.defaultChoosers = null;
/* 135 */     this.chooser = null;
/* 136 */     this.tabbedPane = null;
/*     */ 
/* 138 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected void installPreviewPanel() {
/* 142 */     JComponent localJComponent = this.chooser.getPreviewPanel();
/* 143 */     if (localJComponent == null) {
/* 144 */       localJComponent = ColorChooserComponentFactory.getPreviewPanel();
/*     */     }
/* 146 */     else if ((JPanel.class.equals(localJComponent.getClass())) && (0 == localJComponent.getComponentCount())) {
/* 147 */       localJComponent = null;
/*     */     }
/* 149 */     this.previewPanel = localJComponent;
/* 150 */     if (localJComponent != null) {
/* 151 */       this.chooser.add(this.previewPanelHolder, "South");
/* 152 */       localJComponent.setForeground(this.chooser.getColor());
/* 153 */       this.previewPanelHolder.add(localJComponent);
/* 154 */       localJComponent.addMouseListener(getHandler());
/* 155 */       localJComponent.setInheritsPopupMenu(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallPreviewPanel()
/*     */   {
/* 165 */     if (this.previewPanel != null) {
/* 166 */       this.previewPanel.removeMouseListener(getHandler());
/* 167 */       this.previewPanelHolder.remove(this.previewPanel);
/*     */     }
/* 169 */     this.chooser.remove(this.previewPanelHolder);
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 173 */     LookAndFeel.installColorsAndFont(this.chooser, "ColorChooser.background", "ColorChooser.foreground", "ColorChooser.font");
/*     */ 
/* 176 */     LookAndFeel.installProperty(this.chooser, "opaque", Boolean.TRUE);
/* 177 */     TransferHandler localTransferHandler = this.chooser.getTransferHandler();
/* 178 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource)))
/* 179 */       this.chooser.setTransferHandler(defaultTransferHandler);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 184 */     if ((this.chooser.getTransferHandler() instanceof UIResource))
/* 185 */       this.chooser.setTransferHandler(null);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 191 */     this.propertyChangeListener = createPropertyChangeListener();
/* 192 */     this.chooser.addPropertyChangeListener(this.propertyChangeListener);
/*     */ 
/* 194 */     this.previewListener = getHandler();
/* 195 */     this.chooser.getSelectionModel().addChangeListener(this.previewListener);
/*     */   }
/*     */ 
/*     */   private Handler getHandler() {
/* 199 */     if (this.handler == null) {
/* 200 */       this.handler = new Handler(null);
/*     */     }
/* 202 */     return this.handler;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 206 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 210 */     this.chooser.removePropertyChangeListener(this.propertyChangeListener);
/* 211 */     this.chooser.getSelectionModel().removeChangeListener(this.previewListener);
/* 212 */     this.previewListener = null;
/*     */   }
/*     */ 
/*     */   private void selectionChanged(ColorSelectionModel paramColorSelectionModel) {
/* 216 */     JComponent localJComponent = this.chooser.getPreviewPanel();
/* 217 */     if (localJComponent != null) {
/* 218 */       localJComponent.setForeground(paramColorSelectionModel.getSelectedColor());
/* 219 */       localJComponent.repaint();
/*     */     }
/* 221 */     AbstractColorChooserPanel[] arrayOfAbstractColorChooserPanel1 = this.chooser.getChooserPanels();
/* 222 */     if (arrayOfAbstractColorChooserPanel1 != null)
/* 223 */       for (AbstractColorChooserPanel localAbstractColorChooserPanel : arrayOfAbstractColorChooserPanel1)
/* 224 */         if (localAbstractColorChooserPanel != null)
/* 225 */           localAbstractColorChooserPanel.updateChooser();
/*     */   }
/*     */ 
/*     */   static class ColorTransferHandler extends TransferHandler
/*     */     implements UIResource
/*     */   {
/*     */     ColorTransferHandler()
/*     */     {
/* 351 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Handler
/*     */     implements ChangeListener, MouseListener, PropertyChangeListener
/*     */   {
/*     */     private Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/* 237 */       BasicColorChooserUI.this.selectionChanged((ColorSelectionModel)paramChangeEvent.getSource());
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 243 */       if (BasicColorChooserUI.this.chooser.getDragEnabled()) {
/* 244 */         TransferHandler localTransferHandler = BasicColorChooserUI.this.chooser.getTransferHandler();
/* 245 */         localTransferHandler.exportAsDrag(BasicColorChooserUI.this.chooser, paramMouseEvent, 1);
/*     */       }
/*     */     }
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/*     */     }
/*     */ 
/* 257 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { String str1 = paramPropertyChangeEvent.getPropertyName();
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 259 */       if (str1 == "chooserPanels") {
/* 260 */         localObject1 = (AbstractColorChooserPanel[])paramPropertyChangeEvent.getOldValue();
/*     */ 
/* 262 */         localObject2 = (AbstractColorChooserPanel[])paramPropertyChangeEvent.getNewValue();
/*     */         Object localObject3;
/*     */         Object localObject4;
/* 265 */         for (int i = 0; i < localObject1.length; i++) {
/* 266 */           localObject3 = localObject1[i].getParent();
/* 267 */           if (localObject3 != null) {
/* 268 */             localObject4 = ((Container)localObject3).getParent();
/* 269 */             if (localObject4 != null)
/* 270 */               ((Container)localObject4).remove((Component)localObject3);
/* 271 */             localObject1[i].uninstallChooserPanel(BasicColorChooserUI.this.chooser);
/*     */           }
/*     */         }
/*     */ 
/* 275 */         i = localObject2.length;
/* 276 */         if (i == 0) {
/* 277 */           BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.tabbedPane);
/* 278 */           return;
/*     */         }
/* 280 */         if (i == 1) {
/* 281 */           BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.tabbedPane);
/* 282 */           localObject3 = new JPanel(new CenterLayout());
/* 283 */           ((JPanel)localObject3).setInheritsPopupMenu(true);
/* 284 */           ((JPanel)localObject3).add(localObject2[0]);
/* 285 */           BasicColorChooserUI.this.singlePanel.add((Component)localObject3, "Center");
/* 286 */           BasicColorChooserUI.this.chooser.add(BasicColorChooserUI.this.singlePanel);
/*     */         }
/*     */         else {
/* 289 */           if (localObject1.length < 2) {
/* 290 */             BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.singlePanel);
/* 291 */             BasicColorChooserUI.this.chooser.add(BasicColorChooserUI.this.tabbedPane, "Center");
/*     */           }
/*     */ 
/* 294 */           for (j = 0; j < localObject2.length; j++) {
/* 295 */             localObject4 = new JPanel(new CenterLayout());
/* 296 */             ((JPanel)localObject4).setInheritsPopupMenu(true);
/* 297 */             String str2 = localObject2[j].getDisplayName();
/* 298 */             int k = localObject2[j].getMnemonic();
/* 299 */             ((JPanel)localObject4).add(localObject2[j]);
/* 300 */             BasicColorChooserUI.this.tabbedPane.addTab(str2, (Component)localObject4);
/* 301 */             if (k > 0) {
/* 302 */               BasicColorChooserUI.this.tabbedPane.setMnemonicAt(j, k);
/* 303 */               int m = localObject2[j].getDisplayedMnemonicIndex();
/* 304 */               if (m >= 0) {
/* 305 */                 BasicColorChooserUI.this.tabbedPane.setDisplayedMnemonicIndexAt(j, m);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 310 */         BasicColorChooserUI.this.chooser.applyComponentOrientation(BasicColorChooserUI.this.chooser.getComponentOrientation());
/* 311 */         for (int j = 0; j < localObject2.length; j++) {
/* 312 */           localObject2[j].installChooserPanel(BasicColorChooserUI.this.chooser);
/*     */         }
/*     */       }
/* 315 */       else if (str1 == "previewPanel") {
/* 316 */         BasicColorChooserUI.this.uninstallPreviewPanel();
/* 317 */         BasicColorChooserUI.this.installPreviewPanel();
/*     */       }
/* 319 */       else if (str1 == "selectionModel") {
/* 320 */         localObject1 = (ColorSelectionModel)paramPropertyChangeEvent.getOldValue();
/* 321 */         ((ColorSelectionModel)localObject1).removeChangeListener(BasicColorChooserUI.this.previewListener);
/* 322 */         localObject2 = (ColorSelectionModel)paramPropertyChangeEvent.getNewValue();
/* 323 */         ((ColorSelectionModel)localObject2).addChangeListener(BasicColorChooserUI.this.previewListener);
/* 324 */         BasicColorChooserUI.this.selectionChanged((ColorSelectionModel)localObject2);
/*     */       }
/* 326 */       else if (str1 == "componentOrientation") {
/* 327 */         localObject1 = (ComponentOrientation)paramPropertyChangeEvent.getNewValue();
/*     */ 
/* 329 */         localObject2 = (JColorChooser)paramPropertyChangeEvent.getSource();
/* 330 */         if (localObject1 != (ComponentOrientation)paramPropertyChangeEvent.getOldValue()) {
/* 331 */           ((JColorChooser)localObject2).applyComponentOrientation((ComponentOrientation)localObject1);
/* 332 */           ((JColorChooser)localObject2).updateUI();
/*     */         }
/*     */       } }
/*     */   }
/*     */ 
/*     */   public class PropertyHandler implements PropertyChangeListener
/*     */   {
/*     */     public PropertyHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 344 */       BasicColorChooserUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicColorChooserUI
 * JD-Core Version:    0.6.2
 */