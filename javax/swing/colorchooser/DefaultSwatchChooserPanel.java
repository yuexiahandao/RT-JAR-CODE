/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ 
/*     */ class DefaultSwatchChooserPanel extends AbstractColorChooserPanel
/*     */ {
/*     */   SwatchPanel swatchPanel;
/*     */   RecentSwatchPanel recentSwatchPanel;
/*     */   MouseListener mainSwatchListener;
/*     */   MouseListener recentSwatchListener;
/*     */   private KeyListener mainSwatchKeyListener;
/*     */   private KeyListener recentSwatchKeyListener;
/*     */ 
/*     */   public DefaultSwatchChooserPanel()
/*     */   {
/*  65 */     setInheritsPopupMenu(true);
/*     */   }
/*     */ 
/*     */   public String getDisplayName() {
/*  69 */     return UIManager.getString("ColorChooser.swatchesNameText", getLocale());
/*     */   }
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/*  92 */     return getInt("ColorChooser.swatchesMnemonic", -1);
/*     */   }
/*     */ 
/*     */   public int getDisplayedMnemonicIndex()
/*     */   {
/* 120 */     return getInt("ColorChooser.swatchesDisplayedMnemonicIndex", -1);
/*     */   }
/*     */ 
/*     */   public Icon getSmallDisplayIcon() {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   public Icon getLargeDisplayIcon() {
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   public void installChooserPanel(JColorChooser paramJColorChooser)
/*     */   {
/* 136 */     super.installChooserPanel(paramJColorChooser);
/*     */   }
/*     */ 
/*     */   protected void buildChooser()
/*     */   {
/* 141 */     String str = UIManager.getString("ColorChooser.swatchesRecentText", getLocale());
/*     */ 
/* 143 */     GridBagLayout localGridBagLayout = new GridBagLayout();
/* 144 */     GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/* 145 */     JPanel localJPanel1 = new JPanel(localGridBagLayout);
/*     */ 
/* 147 */     this.swatchPanel = new MainSwatchPanel();
/* 148 */     this.swatchPanel.putClientProperty("AccessibleName", getDisplayName());
/*     */ 
/* 150 */     this.swatchPanel.setInheritsPopupMenu(true);
/*     */ 
/* 152 */     this.recentSwatchPanel = new RecentSwatchPanel();
/* 153 */     this.recentSwatchPanel.putClientProperty("AccessibleName", str);
/*     */ 
/* 156 */     this.mainSwatchKeyListener = new MainSwatchKeyListener(null);
/* 157 */     this.mainSwatchListener = new MainSwatchListener();
/* 158 */     this.swatchPanel.addMouseListener(this.mainSwatchListener);
/* 159 */     this.swatchPanel.addKeyListener(this.mainSwatchKeyListener);
/* 160 */     this.recentSwatchListener = new RecentSwatchListener();
/* 161 */     this.recentSwatchKeyListener = new RecentSwatchKeyListener(null);
/* 162 */     this.recentSwatchPanel.addMouseListener(this.recentSwatchListener);
/* 163 */     this.recentSwatchPanel.addKeyListener(this.recentSwatchKeyListener);
/*     */ 
/* 165 */     JPanel localJPanel2 = new JPanel(new BorderLayout());
/* 166 */     CompoundBorder localCompoundBorder = new CompoundBorder(new LineBorder(Color.black), new LineBorder(Color.white));
/*     */ 
/* 168 */     localJPanel2.setBorder(localCompoundBorder);
/* 169 */     localJPanel2.add(this.swatchPanel, "Center");
/*     */ 
/* 171 */     localGridBagConstraints.anchor = 25;
/* 172 */     localGridBagConstraints.gridwidth = 1;
/* 173 */     localGridBagConstraints.gridheight = 2;
/* 174 */     Insets localInsets = localGridBagConstraints.insets;
/* 175 */     localGridBagConstraints.insets = new Insets(0, 0, 0, 10);
/* 176 */     localJPanel1.add(localJPanel2, localGridBagConstraints);
/* 177 */     localGridBagConstraints.insets = localInsets;
/*     */ 
/* 179 */     this.recentSwatchPanel.setInheritsPopupMenu(true);
/* 180 */     JPanel localJPanel3 = new JPanel(new BorderLayout());
/* 181 */     localJPanel3.setBorder(localCompoundBorder);
/* 182 */     localJPanel3.setInheritsPopupMenu(true);
/* 183 */     localJPanel3.add(this.recentSwatchPanel, "Center");
/*     */ 
/* 185 */     JLabel localJLabel = new JLabel(str);
/* 186 */     localJLabel.setLabelFor(this.recentSwatchPanel);
/*     */ 
/* 188 */     localGridBagConstraints.gridwidth = 0;
/* 189 */     localGridBagConstraints.gridheight = 1;
/* 190 */     localGridBagConstraints.weighty = 1.0D;
/* 191 */     localJPanel1.add(localJLabel, localGridBagConstraints);
/*     */ 
/* 193 */     localGridBagConstraints.weighty = 0.0D;
/* 194 */     localGridBagConstraints.gridheight = 0;
/* 195 */     localGridBagConstraints.insets = new Insets(0, 0, 0, 2);
/* 196 */     localJPanel1.add(localJPanel3, localGridBagConstraints);
/* 197 */     localJPanel1.setInheritsPopupMenu(true);
/*     */ 
/* 199 */     add(localJPanel1);
/*     */   }
/*     */ 
/*     */   public void uninstallChooserPanel(JColorChooser paramJColorChooser) {
/* 203 */     super.uninstallChooserPanel(paramJColorChooser);
/* 204 */     this.swatchPanel.removeMouseListener(this.mainSwatchListener);
/* 205 */     this.swatchPanel.removeKeyListener(this.mainSwatchKeyListener);
/* 206 */     this.recentSwatchPanel.removeMouseListener(this.recentSwatchListener);
/* 207 */     this.recentSwatchPanel.removeKeyListener(this.recentSwatchKeyListener);
/*     */ 
/* 209 */     this.swatchPanel = null;
/* 210 */     this.recentSwatchPanel = null;
/* 211 */     this.mainSwatchListener = null;
/* 212 */     this.mainSwatchKeyListener = null;
/* 213 */     this.recentSwatchListener = null;
/* 214 */     this.recentSwatchKeyListener = null;
/*     */ 
/* 216 */     removeAll();
/*     */   }
/*     */ 
/*     */   public void updateChooser()
/*     */   {
/*     */   }
/*     */ 
/*     */   private class MainSwatchKeyListener extends KeyAdapter
/*     */   {
/*     */     private MainSwatchKeyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent paramKeyEvent)
/*     */     {
/* 235 */       if (32 == paramKeyEvent.getKeyCode()) {
/* 236 */         Color localColor = DefaultSwatchChooserPanel.this.swatchPanel.getSelectedColor();
/* 237 */         DefaultSwatchChooserPanel.this.setSelectedColor(localColor);
/* 238 */         DefaultSwatchChooserPanel.this.recentSwatchPanel.setMostRecentColor(localColor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class MainSwatchListener extends MouseAdapter
/*     */     implements Serializable
/*     */   {
/*     */     MainSwatchListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 256 */       if (DefaultSwatchChooserPanel.this.isEnabled()) {
/* 257 */         Color localColor = DefaultSwatchChooserPanel.this.swatchPanel.getColorForLocation(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 258 */         DefaultSwatchChooserPanel.this.setSelectedColor(localColor);
/* 259 */         DefaultSwatchChooserPanel.this.swatchPanel.setSelectedColorFromLocation(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 260 */         DefaultSwatchChooserPanel.this.recentSwatchPanel.setMostRecentColor(localColor);
/* 261 */         DefaultSwatchChooserPanel.this.swatchPanel.requestFocusInWindow();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class RecentSwatchKeyListener extends KeyAdapter
/*     */   {
/*     */     private RecentSwatchKeyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent paramKeyEvent)
/*     */     {
/* 226 */       if (32 == paramKeyEvent.getKeyCode()) {
/* 227 */         Color localColor = DefaultSwatchChooserPanel.this.recentSwatchPanel.getSelectedColor();
/* 228 */         DefaultSwatchChooserPanel.this.setSelectedColor(localColor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class RecentSwatchListener extends MouseAdapter
/*     */     implements Serializable
/*     */   {
/*     */     RecentSwatchListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 245 */       if (DefaultSwatchChooserPanel.this.isEnabled()) {
/* 246 */         Color localColor = DefaultSwatchChooserPanel.this.recentSwatchPanel.getColorForLocation(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 247 */         DefaultSwatchChooserPanel.this.recentSwatchPanel.setSelectedColorFromLocation(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 248 */         DefaultSwatchChooserPanel.this.setSelectedColor(localColor);
/* 249 */         DefaultSwatchChooserPanel.this.recentSwatchPanel.requestFocusInWindow();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.DefaultSwatchChooserPanel
 * JD-Core Version:    0.6.2
 */