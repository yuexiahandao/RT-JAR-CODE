/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ public final class SwingPropertyChangeSupport extends PropertyChangeSupport
/*     */ {
/*     */   static final long serialVersionUID = 7162625831330845068L;
/*     */   private final boolean notifyOnEDT;
/*     */ 
/*     */   public SwingPropertyChangeSupport(Object paramObject)
/*     */   {
/*  53 */     this(paramObject, false);
/*     */   }
/*     */ 
/*     */   public SwingPropertyChangeSupport(Object paramObject, boolean paramBoolean)
/*     */   {
/*  68 */     super(paramObject);
/*  69 */     this.notifyOnEDT = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(final PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/*  87 */     if (paramPropertyChangeEvent == null) {
/*  88 */       throw new NullPointerException();
/*     */     }
/*  90 */     if ((!isNotifyOnEDT()) || (SwingUtilities.isEventDispatchThread()))
/*     */     {
/*  92 */       super.firePropertyChange(paramPropertyChangeEvent);
/*     */     }
/*  94 */     else SwingUtilities.invokeLater(new Runnable()
/*     */       {
/*     */         public void run() {
/*  97 */           SwingPropertyChangeSupport.this.firePropertyChange(paramPropertyChangeEvent);
/*     */         }
/*     */       });
/*     */   }
/*     */ 
/*     */   public final boolean isNotifyOnEDT()
/*     */   {
/* 111 */     return this.notifyOnEDT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.SwingPropertyChangeSupport
 * JD-Core Version:    0.6.2
 */