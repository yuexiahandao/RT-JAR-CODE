/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.RowSorter;
/*     */ import javax.swing.RowSorter.SortKey;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTableHeaderUI;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import sun.swing.table.DefaultTableCellHeaderRenderer;
/*     */ 
/*     */ public class SynthTableHeaderUI extends BasicTableHeaderUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private TableCellRenderer prevRenderer;
/*     */   private SynthStyle style;
/*     */ 
/*     */   public SynthTableHeaderUI()
/*     */   {
/*  52 */     this.prevRenderer = null;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     return new SynthTableHeaderUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  71 */     this.prevRenderer = this.header.getDefaultRenderer();
/*  72 */     if ((this.prevRenderer instanceof UIResource)) {
/*  73 */       this.header.setDefaultRenderer(new HeaderRenderer());
/*     */     }
/*  75 */     updateStyle(this.header);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTableHeader paramJTableHeader) {
/*  79 */     SynthContext localSynthContext = getContext(paramJTableHeader, 1);
/*  80 */     SynthStyle localSynthStyle = this.style;
/*  81 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  82 */     if ((this.style != localSynthStyle) && 
/*  83 */       (localSynthStyle != null)) {
/*  84 */       uninstallKeyboardActions();
/*  85 */       installKeyboardActions();
/*     */     }
/*     */ 
/*  88 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  96 */     super.installListeners();
/*  97 */     this.header.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 105 */     if ((this.header.getDefaultRenderer() instanceof HeaderRenderer)) {
/* 106 */       this.header.setDefaultRenderer(this.prevRenderer);
/*     */     }
/*     */ 
/* 109 */     SynthContext localSynthContext = getContext(this.header, 1);
/*     */ 
/* 111 */     this.style.uninstallDefaults(localSynthContext);
/* 112 */     localSynthContext.dispose();
/* 113 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 121 */     this.header.removePropertyChangeListener(this);
/* 122 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 139 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 141 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 142 */     localSynthContext.getPainter().paintTableHeaderBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 144 */     paint(localSynthContext, paramGraphics);
/* 145 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 159 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 161 */     paint(localSynthContext, paramGraphics);
/* 162 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 173 */     super.paint(paramGraphics, paramSynthContext.getComponent());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 182 */     paramSynthContext.getPainter().paintTableHeaderBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 192 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 196 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   protected void rolloverColumnUpdated(int paramInt1, int paramInt2)
/*     */   {
/* 205 */     this.header.repaint(this.header.getHeaderRect(paramInt1));
/* 206 */     this.header.repaint(this.header.getHeaderRect(paramInt2));
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 214 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 215 */       updateStyle((JTableHeader)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ 
/*     */   private class HeaderRenderer extends DefaultTableCellHeaderRenderer
/*     */   {
/*     */     HeaderRenderer() {
/* 221 */       setHorizontalAlignment(10);
/* 222 */       setName("TableHeader.renderer");
/*     */     }
/*     */ 
/*     */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */     {
/* 231 */       boolean bool = paramInt2 == SynthTableHeaderUI.this.getRolloverColumn();
/* 232 */       if ((paramBoolean1) || (bool) || (paramBoolean2)) {
/* 233 */         SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), paramBoolean1, paramBoolean2, paramJTable.isEnabled(), bool);
/*     */       }
/*     */       else
/*     */       {
/* 238 */         SynthLookAndFeel.resetSelectedUI();
/*     */       }
/*     */ 
/* 243 */       RowSorter localRowSorter = paramJTable == null ? null : paramJTable.getRowSorter();
/* 244 */       List localList = localRowSorter == null ? null : localRowSorter.getSortKeys();
/* 245 */       if ((localList != null) && (localList.size() > 0) && (((RowSorter.SortKey)localList.get(0)).getColumn() == paramJTable.convertColumnIndexToModel(paramInt2)));
/* 247 */       switch (SynthTableHeaderUI.1.$SwitchMap$javax$swing$SortOrder[((RowSorter.SortKey)localList.get(0)).getSortOrder().ordinal()]) {
/*     */       case 1:
/* 249 */         putClientProperty("Table.sortOrder", "ASCENDING");
/* 250 */         break;
/*     */       case 2:
/* 252 */         putClientProperty("Table.sortOrder", "DESCENDING");
/* 253 */         break;
/*     */       case 3:
/* 255 */         putClientProperty("Table.sortOrder", "UNSORTED");
/* 256 */         break;
/*     */       default:
/* 258 */         throw new AssertionError("Cannot happen");
/*     */ 
/* 261 */         putClientProperty("Table.sortOrder", "UNSORTED");
/*     */       }
/*     */ 
/* 264 */       super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*     */ 
/* 267 */       return this;
/*     */     }
/*     */ 
/*     */     public void setBorder(Border paramBorder)
/*     */     {
/* 272 */       if ((paramBorder instanceof SynthBorder))
/* 273 */         super.setBorder(paramBorder);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTableHeaderUI
 * JD-Core Version:    0.6.2
 */