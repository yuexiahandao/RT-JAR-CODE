/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTError;
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.AccessibleAWTComponent;
/*     */ import java.awt.Container.AccessibleAWTContainer;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import java.beans.ConstructorProperties;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class Box extends JComponent
/*     */   implements Accessible
/*     */ {
/*     */   public Box(int paramInt)
/*     */   {
/*  95 */     super.setLayout(new BoxLayout(this, paramInt));
/*     */   }
/*     */ 
/*     */   public static Box createHorizontalBox()
/*     */   {
/* 111 */     return new Box(0);
/*     */   }
/*     */ 
/*     */   public static Box createVerticalBox()
/*     */   {
/* 127 */     return new Box(1);
/*     */   }
/*     */ 
/*     */   public static Component createRigidArea(Dimension paramDimension)
/*     */   {
/* 141 */     return new Filler(paramDimension, paramDimension, paramDimension);
/*     */   }
/*     */ 
/*     */   public static Component createHorizontalStrut(int paramInt)
/*     */   {
/* 164 */     return new Filler(new Dimension(paramInt, 0), new Dimension(paramInt, 0), new Dimension(paramInt, 32767));
/*     */   }
/*     */ 
/*     */   public static Component createVerticalStrut(int paramInt)
/*     */   {
/* 188 */     return new Filler(new Dimension(0, paramInt), new Dimension(0, paramInt), new Dimension(32767, paramInt));
/*     */   }
/*     */ 
/*     */   public static Component createGlue()
/*     */   {
/* 237 */     return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(32767, 32767));
/*     */   }
/*     */ 
/*     */   public static Component createHorizontalGlue()
/*     */   {
/* 247 */     return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(32767, 0));
/*     */   }
/*     */ 
/*     */   public static Component createVerticalGlue()
/*     */   {
/* 257 */     return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 267 */     throw new AWTError("Illegal request");
/*     */   }
/*     */ 
/*     */   protected void paintComponent(Graphics paramGraphics)
/*     */   {
/* 281 */     if (this.ui != null)
/*     */     {
/* 283 */       super.paintComponent(paramGraphics);
/* 284 */     } else if (isOpaque()) {
/* 285 */       paramGraphics.setColor(getBackground());
/* 286 */       paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 413 */     if (this.accessibleContext == null) {
/* 414 */       this.accessibleContext = new AccessibleBox();
/*     */     }
/* 416 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleBox extends Container.AccessibleAWTContainer
/*     */   {
/*     */     protected AccessibleBox()
/*     */     {
/* 423 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 434 */       return AccessibleRole.FILLER;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Filler extends JComponent
/*     */     implements Accessible
/*     */   {
/*     */     @ConstructorProperties({"minimumSize", "preferredSize", "maximumSize"})
/*     */     public Filler(Dimension paramDimension1, Dimension paramDimension2, Dimension paramDimension3)
/*     */     {
/* 315 */       setMinimumSize(paramDimension1);
/* 316 */       setPreferredSize(paramDimension2);
/* 317 */       setMaximumSize(paramDimension3);
/*     */     }
/*     */ 
/*     */     public void changeShape(Dimension paramDimension1, Dimension paramDimension2, Dimension paramDimension3)
/*     */     {
/* 330 */       setMinimumSize(paramDimension1);
/* 331 */       setPreferredSize(paramDimension2);
/* 332 */       setMaximumSize(paramDimension3);
/* 333 */       revalidate();
/*     */     }
/*     */ 
/*     */     protected void paintComponent(Graphics paramGraphics)
/*     */     {
/* 350 */       if (this.ui != null)
/*     */       {
/* 352 */         super.paintComponent(paramGraphics);
/* 353 */       } else if (isOpaque()) {
/* 354 */         paramGraphics.setColor(getBackground());
/* 355 */         paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */       }
/*     */     }
/*     */ 
/*     */     public AccessibleContext getAccessibleContext()
/*     */     {
/* 373 */       if (this.accessibleContext == null) {
/* 374 */         this.accessibleContext = new AccessibleBoxFiller();
/*     */       }
/* 376 */       return this.accessibleContext;
/*     */     }
/*     */ 
/*     */     protected class AccessibleBoxFiller extends Component.AccessibleAWTComponent
/*     */     {
/*     */       protected AccessibleBoxFiller()
/*     */       {
/* 383 */         super();
/*     */       }
/*     */ 
/*     */       public AccessibleRole getAccessibleRole()
/*     */       {
/* 394 */         return AccessibleRole.FILLER;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Box
 * JD-Core Version:    0.6.2
 */