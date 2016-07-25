/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Window;
/*     */ import java.awt.Window.Type;
/*     */ import sun.awt.ModalExclude;
/*     */ 
/*     */ public class Popup
/*     */ {
/*     */   private Component component;
/*     */ 
/*     */   protected Popup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/*  84 */     this();
/*  85 */     if (paramComponent2 == null) {
/*  86 */       throw new IllegalArgumentException("Contents must be non-null");
/*     */     }
/*  88 */     reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected Popup()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void show()
/*     */   {
/* 102 */     Component localComponent = getComponent();
/*     */ 
/* 104 */     if (localComponent != null)
/* 105 */       localComponent.show();
/*     */   }
/*     */ 
/*     */   public void hide()
/*     */   {
/* 118 */     Component localComponent = getComponent();
/*     */ 
/* 120 */     if ((localComponent instanceof JWindow)) {
/* 121 */       localComponent.hide();
/* 122 */       ((JWindow)localComponent).getContentPane().removeAll();
/*     */     }
/* 124 */     dispose();
/*     */   }
/*     */ 
/*     */   void dispose()
/*     */   {
/* 131 */     Component localComponent = getComponent();
/* 132 */     Window localWindow = SwingUtilities.getWindowAncestor(localComponent);
/*     */ 
/* 134 */     if ((localComponent instanceof JWindow)) {
/* 135 */       ((Window)localComponent).dispose();
/* 136 */       localComponent = null;
/*     */     }
/*     */ 
/* 139 */     if ((localWindow instanceof DefaultFrame))
/* 140 */       localWindow.dispose();
/*     */   }
/*     */ 
/*     */   void reset(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 148 */     if (getComponent() == null) {
/* 149 */       this.component = createComponent(paramComponent1);
/*     */     }
/*     */ 
/* 152 */     Component localComponent = getComponent();
/*     */ 
/* 154 */     if ((localComponent instanceof JWindow)) {
/* 155 */       JWindow localJWindow = (JWindow)getComponent();
/*     */ 
/* 157 */       localJWindow.setLocation(paramInt1, paramInt2);
/* 158 */       localJWindow.getContentPane().add(paramComponent2, "Center");
/* 159 */       localJWindow.invalidate();
/* 160 */       localJWindow.validate();
/* 161 */       if (localJWindow.isVisible())
/*     */       {
/* 164 */         pack();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void pack()
/*     */   {
/* 175 */     Component localComponent = getComponent();
/*     */ 
/* 177 */     if ((localComponent instanceof Window))
/* 178 */       ((Window)localComponent).pack();
/*     */   }
/*     */ 
/*     */   private Window getParentWindow(Component paramComponent)
/*     */   {
/* 188 */     Object localObject = null;
/*     */ 
/* 190 */     if ((paramComponent instanceof Window)) {
/* 191 */       localObject = (Window)paramComponent;
/*     */     }
/* 193 */     else if (paramComponent != null) {
/* 194 */       localObject = SwingUtilities.getWindowAncestor(paramComponent);
/*     */     }
/* 196 */     if (localObject == null) {
/* 197 */       localObject = new DefaultFrame();
/*     */     }
/* 199 */     return localObject;
/*     */   }
/*     */ 
/*     */   Component createComponent(Component paramComponent)
/*     */   {
/* 208 */     if (GraphicsEnvironment.isHeadless())
/*     */     {
/* 210 */       return null;
/*     */     }
/* 212 */     return new HeavyWeightWindow(getParentWindow(paramComponent));
/*     */   }
/*     */ 
/*     */   Component getComponent()
/*     */   {
/* 220 */     return this.component;
/*     */   }
/*     */ 
/*     */   static class DefaultFrame extends Frame
/*     */   {
/*     */   }
/*     */ 
/*     */   static class HeavyWeightWindow extends JWindow implements ModalExclude {
/*     */     HeavyWeightWindow(Window paramWindow) {
/* 229 */       super();
/* 230 */       setFocusableWindowState(false);
/* 231 */       setType(Window.Type.POPUP);
/*     */ 
/* 235 */       getRootPane().setUseTrueDoubleBuffering(false);
/*     */       try
/*     */       {
/* 240 */         setAlwaysOnTop(true);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void update(Graphics paramGraphics) {
/* 248 */       paint(paramGraphics);
/*     */     }
/*     */ 
/*     */     public void show() {
/* 252 */       pack();
/* 253 */       if ((getWidth() > 0) && (getHeight() > 0))
/* 254 */         super.show();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Popup
 * JD-Core Version:    0.6.2
 */