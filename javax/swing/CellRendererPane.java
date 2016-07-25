/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Container.AccessibleAWTContainer;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class CellRendererPane extends Container
/*     */   implements Accessible
/*     */ {
/* 191 */   protected AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public CellRendererPane()
/*     */   {
/*  73 */     setLayout(null);
/*  74 */     setVisible(false);
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 102 */     if (paramComponent.getParent() == this) {
/* 103 */       return;
/*     */     }
/*     */ 
/* 106 */     super.addImpl(paramComponent, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics, Component paramComponent, Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 123 */     if (paramComponent == null) {
/* 124 */       if (paramContainer != null) {
/* 125 */         Color localColor = paramGraphics.getColor();
/* 126 */         paramGraphics.setColor(paramContainer.getBackground());
/* 127 */         paramGraphics.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/* 128 */         paramGraphics.setColor(localColor);
/*     */       }
/* 130 */       return;
/*     */     }
/*     */ 
/* 133 */     if (paramComponent.getParent() != this) {
/* 134 */       add(paramComponent);
/*     */     }
/*     */ 
/* 137 */     paramComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 139 */     if (paramBoolean) {
/* 140 */       paramComponent.validate();
/*     */     }
/*     */ 
/* 143 */     int i = 0;
/* 144 */     if (((paramComponent instanceof JComponent)) && (((JComponent)paramComponent).isDoubleBuffered())) {
/* 145 */       i = 1;
/* 146 */       ((JComponent)paramComponent).setDoubleBuffered(false);
/*     */     }
/*     */ 
/* 149 */     Graphics localGraphics = paramGraphics.create(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     try {
/* 151 */       paramComponent.paint(localGraphics);
/*     */     }
/*     */     finally {
/* 154 */       localGraphics.dispose();
/*     */     }
/*     */ 
/* 157 */     if ((i != 0) && ((paramComponent instanceof JComponent))) {
/* 158 */       ((JComponent)paramComponent).setDoubleBuffered(true);
/*     */     }
/*     */ 
/* 161 */     paramComponent.setBounds(-paramInt3, -paramInt4, 0, 0);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics, Component paramComponent, Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 169 */     paintComponent(paramGraphics, paramComponent, paramContainer, paramInt1, paramInt2, paramInt3, paramInt4, false);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics, Component paramComponent, Container paramContainer, Rectangle paramRectangle)
/*     */   {
/* 177 */     paintComponent(paramGraphics, paramComponent, paramContainer, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 182 */     removeAll();
/* 183 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 203 */     if (this.accessibleContext == null) {
/* 204 */       this.accessibleContext = new AccessibleCellRendererPane();
/*     */     }
/* 206 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleCellRendererPane extends Container.AccessibleAWTContainer
/*     */   {
/*     */     protected AccessibleCellRendererPane()
/*     */     {
/* 213 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 224 */       return AccessibleRole.PANEL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.CellRendererPane
 * JD-Core Version:    0.6.2
 */