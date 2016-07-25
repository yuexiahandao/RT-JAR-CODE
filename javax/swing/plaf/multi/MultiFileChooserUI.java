/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.filechooser.FileView;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.FileChooserUI;
/*     */ 
/*     */ public class MultiFileChooserUI extends FileChooserUI
/*     */ {
/*  55 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  67 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public FileFilter getAcceptAllFileFilter(JFileChooser paramJFileChooser)
/*     */   {
/*  81 */     FileFilter localFileFilter = ((FileChooserUI)this.uis.elementAt(0)).getAcceptAllFileFilter(paramJFileChooser);
/*     */ 
/*  83 */     for (int i = 1; i < this.uis.size(); i++) {
/*  84 */       ((FileChooserUI)this.uis.elementAt(i)).getAcceptAllFileFilter(paramJFileChooser);
/*     */     }
/*  86 */     return localFileFilter;
/*     */   }
/*     */ 
/*     */   public FileView getFileView(JFileChooser paramJFileChooser)
/*     */   {
/*  96 */     FileView localFileView = ((FileChooserUI)this.uis.elementAt(0)).getFileView(paramJFileChooser);
/*     */ 
/*  98 */     for (int i = 1; i < this.uis.size(); i++) {
/*  99 */       ((FileChooserUI)this.uis.elementAt(i)).getFileView(paramJFileChooser);
/*     */     }
/* 101 */     return localFileView;
/*     */   }
/*     */ 
/*     */   public String getApproveButtonText(JFileChooser paramJFileChooser)
/*     */   {
/* 111 */     String str = ((FileChooserUI)this.uis.elementAt(0)).getApproveButtonText(paramJFileChooser);
/*     */ 
/* 113 */     for (int i = 1; i < this.uis.size(); i++) {
/* 114 */       ((FileChooserUI)this.uis.elementAt(i)).getApproveButtonText(paramJFileChooser);
/*     */     }
/* 116 */     return str;
/*     */   }
/*     */ 
/*     */   public String getDialogTitle(JFileChooser paramJFileChooser)
/*     */   {
/* 126 */     String str = ((FileChooserUI)this.uis.elementAt(0)).getDialogTitle(paramJFileChooser);
/*     */ 
/* 128 */     for (int i = 1; i < this.uis.size(); i++) {
/* 129 */       ((FileChooserUI)this.uis.elementAt(i)).getDialogTitle(paramJFileChooser);
/*     */     }
/* 131 */     return str;
/*     */   }
/*     */ 
/*     */   public void rescanCurrentDirectory(JFileChooser paramJFileChooser)
/*     */   {
/* 138 */     for (int i = 0; i < this.uis.size(); i++)
/* 139 */       ((FileChooserUI)this.uis.elementAt(i)).rescanCurrentDirectory(paramJFileChooser);
/*     */   }
/*     */ 
/*     */   public void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile)
/*     */   {
/* 147 */     for (int i = 0; i < this.uis.size(); i++)
/* 148 */       ((FileChooserUI)this.uis.elementAt(i)).ensureFileIsVisible(paramJFileChooser, paramFile);
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 163 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 165 */     for (int i = 1; i < this.uis.size(); i++) {
/* 166 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 168 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 175 */     for (int i = 0; i < this.uis.size(); i++)
/* 176 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 186 */     MultiFileChooserUI localMultiFileChooserUI = new MultiFileChooserUI();
/* 187 */     return MultiLookAndFeel.createUIs(localMultiFileChooserUI, ((MultiFileChooserUI)localMultiFileChooserUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 196 */     for (int i = 0; i < this.uis.size(); i++)
/* 197 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 205 */     for (int i = 0; i < this.uis.size(); i++)
/* 206 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 214 */     for (int i = 0; i < this.uis.size(); i++)
/* 215 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 226 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 228 */     for (int i = 1; i < this.uis.size(); i++) {
/* 229 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 231 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 241 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 243 */     for (int i = 1; i < this.uis.size(); i++) {
/* 244 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 246 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 256 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 258 */     for (int i = 1; i < this.uis.size(); i++) {
/* 259 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 261 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 271 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 273 */     for (int j = 1; j < this.uis.size(); j++) {
/* 274 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 276 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 286 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 288 */     for (int i = 1; i < this.uis.size(); i++) {
/* 289 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 291 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiFileChooserUI
 * JD-Core Version:    0.6.2
 */