/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public abstract class AbstractColorChooserPanel extends JPanel
/*     */ {
/*  52 */   private final PropertyChangeListener enabledListener = new PropertyChangeListener() {
/*     */     public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  54 */       Object localObject = paramAnonymousPropertyChangeEvent.getNewValue();
/*  55 */       if ((localObject instanceof Boolean))
/*  56 */         AbstractColorChooserPanel.this.setEnabled(((Boolean)localObject).booleanValue());
/*     */     }
/*  52 */   };
/*     */   private JColorChooser chooser;
/*     */ 
/*     */   public abstract void updateChooser();
/*     */ 
/*     */   protected abstract void buildChooser();
/*     */ 
/*     */   public abstract String getDisplayName();
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/* 103 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getDisplayedMnemonicIndex()
/*     */   {
/* 129 */     return -1;
/*     */   }
/*     */ 
/*     */   public abstract Icon getSmallDisplayIcon();
/*     */ 
/*     */   public abstract Icon getLargeDisplayIcon();
/*     */ 
/*     */   public void installChooserPanel(JColorChooser paramJColorChooser)
/*     */   {
/* 152 */     if (this.chooser != null) {
/* 153 */       throw new RuntimeException("This chooser panel is already installed");
/*     */     }
/* 155 */     this.chooser = paramJColorChooser;
/* 156 */     this.chooser.addPropertyChangeListener("enabled", this.enabledListener);
/* 157 */     setEnabled(this.chooser.isEnabled());
/* 158 */     buildChooser();
/* 159 */     updateChooser();
/*     */   }
/*     */ 
/*     */   public void uninstallChooserPanel(JColorChooser paramJColorChooser)
/*     */   {
/* 167 */     this.chooser.removePropertyChangeListener("enabled", this.enabledListener);
/* 168 */     this.chooser = null;
/*     */   }
/*     */ 
/*     */   public ColorSelectionModel getColorSelectionModel()
/*     */   {
/* 177 */     return this.chooser != null ? this.chooser.getSelectionModel() : null;
/*     */   }
/*     */ 
/*     */   protected Color getColorFromModel()
/*     */   {
/* 187 */     ColorSelectionModel localColorSelectionModel = getColorSelectionModel();
/* 188 */     return localColorSelectionModel != null ? localColorSelectionModel.getSelectedColor() : null;
/*     */   }
/*     */ 
/*     */   void setSelectedColor(Color paramColor)
/*     */   {
/* 194 */     ColorSelectionModel localColorSelectionModel = getColorSelectionModel();
/* 195 */     if (localColorSelectionModel != null)
/* 196 */       localColorSelectionModel.setSelectedColor(paramColor);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 205 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   int getInt(Object paramObject, int paramInt)
/*     */   {
/* 219 */     Object localObject = UIManager.get(paramObject, getLocale());
/*     */ 
/* 221 */     if ((localObject instanceof Integer)) {
/* 222 */       return ((Integer)localObject).intValue();
/*     */     }
/* 224 */     if ((localObject instanceof String))
/*     */       try {
/* 226 */         return Integer.parseInt((String)localObject);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 229 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.AbstractColorChooserPanel
 * JD-Core Version:    0.6.2
 */