/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ 
/*     */ class Autoscroller
/*     */   implements ActionListener
/*     */ {
/*  44 */   private static Autoscroller sharedInstance = new Autoscroller();
/*     */   private static MouseEvent event;
/*     */   private static Timer timer;
/*     */   private static JComponent component;
/*     */ 
/*     */   public static void stop(JComponent paramJComponent)
/*     */   {
/*  60 */     sharedInstance._stop(paramJComponent);
/*     */   }
/*     */ 
/*     */   public static boolean isRunning(JComponent paramJComponent)
/*     */   {
/*  67 */     return sharedInstance._isRunning(paramJComponent);
/*     */   }
/*     */ 
/*     */   public static void processMouseDragged(MouseEvent paramMouseEvent)
/*     */   {
/*  75 */     sharedInstance._processMouseDragged(paramMouseEvent);
/*     */   }
/*     */ 
/*     */   private void start(JComponent paramJComponent, MouseEvent paramMouseEvent)
/*     */   {
/*  86 */     Point localPoint = paramJComponent.getLocationOnScreen();
/*     */ 
/*  88 */     if (component != paramJComponent) {
/*  89 */       _stop(component);
/*     */     }
/*  91 */     component = paramJComponent;
/*  92 */     event = new MouseEvent(component, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getX() + localPoint.x, paramMouseEvent.getY() + localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*     */ 
/* 100 */     if (timer == null) {
/* 101 */       timer = new Timer(100, this);
/*     */     }
/*     */ 
/* 104 */     if (!timer.isRunning())
/* 105 */       timer.start();
/*     */   }
/*     */ 
/*     */   private void _stop(JComponent paramJComponent)
/*     */   {
/* 117 */     if (component == paramJComponent) {
/* 118 */       if (timer != null) {
/* 119 */         timer.stop();
/*     */       }
/* 121 */       timer = null;
/* 122 */       event = null;
/* 123 */       component = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean _isRunning(JComponent paramJComponent)
/*     */   {
/* 132 */     return (paramJComponent == component) && (timer != null) && (timer.isRunning());
/*     */   }
/*     */ 
/*     */   private void _processMouseDragged(MouseEvent paramMouseEvent)
/*     */   {
/* 139 */     JComponent localJComponent = (JComponent)paramMouseEvent.getComponent();
/* 140 */     boolean bool = true;
/* 141 */     if (localJComponent.isShowing()) {
/* 142 */       Rectangle localRectangle = localJComponent.getVisibleRect();
/* 143 */       bool = localRectangle.contains(paramMouseEvent.getX(), paramMouseEvent.getY());
/*     */     }
/* 145 */     if (bool)
/* 146 */       _stop(localJComponent);
/*     */     else
/* 148 */       start(localJComponent, paramMouseEvent);
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 160 */     JComponent localJComponent = component;
/*     */ 
/* 162 */     if ((localJComponent == null) || (!localJComponent.isShowing()) || (event == null)) {
/* 163 */       _stop(localJComponent);
/* 164 */       return;
/*     */     }
/* 166 */     Point localPoint = localJComponent.getLocationOnScreen();
/* 167 */     MouseEvent localMouseEvent = new MouseEvent(localJComponent, event.getID(), event.getWhen(), event.getModifiers(), event.getX() - localPoint.x, event.getY() - localPoint.y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(), event.isPopupTrigger(), 0);
/*     */ 
/* 176 */     localJComponent.superProcessMouseMotionEvent(localMouseEvent);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Autoscroller
 * JD-Core Version:    0.6.2
 */