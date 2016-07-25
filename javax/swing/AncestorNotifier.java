/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.event.AncestorEvent;
/*     */ import javax.swing.event.AncestorListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ class AncestorNotifier
/*     */   implements ComponentListener, PropertyChangeListener, Serializable
/*     */ {
/*     */   transient Component firstInvisibleAncestor;
/*  48 */   EventListenerList listenerList = new EventListenerList();
/*     */   JComponent root;
/*     */ 
/*     */   AncestorNotifier(JComponent paramJComponent)
/*     */   {
/*  52 */     this.root = paramJComponent;
/*  53 */     addListeners(paramJComponent, true);
/*     */   }
/*     */ 
/*     */   void addAncestorListener(AncestorListener paramAncestorListener) {
/*  57 */     this.listenerList.add(AncestorListener.class, paramAncestorListener);
/*     */   }
/*     */ 
/*     */   void removeAncestorListener(AncestorListener paramAncestorListener) {
/*  61 */     this.listenerList.remove(AncestorListener.class, paramAncestorListener);
/*     */   }
/*     */ 
/*     */   AncestorListener[] getAncestorListeners() {
/*  65 */     return (AncestorListener[])this.listenerList.getListeners(AncestorListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireAncestorAdded(JComponent paramJComponent, int paramInt, Container paramContainer1, Container paramContainer2)
/*     */   {
/*  77 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/*  80 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  81 */       if (arrayOfObject[i] == AncestorListener.class)
/*     */       {
/*  83 */         AncestorEvent localAncestorEvent = new AncestorEvent(paramJComponent, paramInt, paramContainer1, paramContainer2);
/*     */ 
/*  85 */         ((AncestorListener)arrayOfObject[(i + 1)]).ancestorAdded(localAncestorEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireAncestorRemoved(JComponent paramJComponent, int paramInt, Container paramContainer1, Container paramContainer2)
/*     */   {
/*  99 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 102 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 103 */       if (arrayOfObject[i] == AncestorListener.class)
/*     */       {
/* 105 */         AncestorEvent localAncestorEvent = new AncestorEvent(paramJComponent, paramInt, paramContainer1, paramContainer2);
/*     */ 
/* 107 */         ((AncestorListener)arrayOfObject[(i + 1)]).ancestorRemoved(localAncestorEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireAncestorMoved(JComponent paramJComponent, int paramInt, Container paramContainer1, Container paramContainer2)
/*     */   {
/* 120 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 123 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 124 */       if (arrayOfObject[i] == AncestorListener.class)
/*     */       {
/* 126 */         AncestorEvent localAncestorEvent = new AncestorEvent(paramJComponent, paramInt, paramContainer1, paramContainer2);
/*     */ 
/* 128 */         ((AncestorListener)arrayOfObject[(i + 1)]).ancestorMoved(localAncestorEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   void removeAllListeners()
/*     */   {
/* 134 */     removeListeners(this.root);
/*     */   }
/*     */ 
/*     */   void addListeners(Component paramComponent, boolean paramBoolean)
/*     */   {
/* 140 */     this.firstInvisibleAncestor = null;
/* 141 */     for (Object localObject = paramComponent; 
/* 142 */       this.firstInvisibleAncestor == null; 
/* 143 */       localObject = ((Component)localObject).getParent()) {
/* 144 */       if ((paramBoolean) || (localObject != paramComponent)) {
/* 145 */         ((Component)localObject).addComponentListener(this);
/*     */ 
/* 147 */         if ((localObject instanceof JComponent)) {
/* 148 */           JComponent localJComponent = (JComponent)localObject;
/*     */ 
/* 150 */           localJComponent.addPropertyChangeListener(this);
/*     */         }
/*     */       }
/* 153 */       if ((!((Component)localObject).isVisible()) || (((Component)localObject).getParent() == null) || ((localObject instanceof Window))) {
/* 154 */         this.firstInvisibleAncestor = ((Component)localObject);
/*     */       }
/*     */     }
/* 157 */     if (((this.firstInvisibleAncestor instanceof Window)) && (this.firstInvisibleAncestor.isVisible()))
/*     */     {
/* 159 */       this.firstInvisibleAncestor = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeListeners(Component paramComponent)
/*     */   {
/* 165 */     for (Object localObject = paramComponent; localObject != null; localObject = ((Component)localObject).getParent()) {
/* 166 */       ((Component)localObject).removeComponentListener(this);
/* 167 */       if ((localObject instanceof JComponent)) {
/* 168 */         JComponent localJComponent = (JComponent)localObject;
/* 169 */         localJComponent.removePropertyChangeListener(this);
/*     */       }
/* 171 */       if ((localObject == this.firstInvisibleAncestor) || ((localObject instanceof Window)))
/*     */         break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void componentResized(ComponentEvent paramComponentEvent) {
/*     */   }
/*     */ 
/*     */   public void componentMoved(ComponentEvent paramComponentEvent) {
/* 180 */     Component localComponent = paramComponentEvent.getComponent();
/*     */ 
/* 182 */     fireAncestorMoved(this.root, 3, (Container)localComponent, localComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void componentShown(ComponentEvent paramComponentEvent)
/*     */   {
/* 187 */     Component localComponent = paramComponentEvent.getComponent();
/*     */ 
/* 189 */     if (localComponent == this.firstInvisibleAncestor) {
/* 190 */       addListeners(localComponent, false);
/* 191 */       if (this.firstInvisibleAncestor == null)
/* 192 */         fireAncestorAdded(this.root, 1, (Container)localComponent, localComponent.getParent());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void componentHidden(ComponentEvent paramComponentEvent)
/*     */   {
/* 199 */     Component localComponent = paramComponentEvent.getComponent();
/* 200 */     int i = this.firstInvisibleAncestor == null ? 1 : 0;
/*     */ 
/* 202 */     if (!(localComponent instanceof Window)) {
/* 203 */       removeListeners(localComponent.getParent());
/*     */     }
/* 205 */     this.firstInvisibleAncestor = localComponent;
/* 206 */     if (i != 0)
/* 207 */       fireAncestorRemoved(this.root, 2, (Container)localComponent, localComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 213 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 215 */     if ((str != null) && ((str.equals("parent")) || (str.equals("ancestor")))) {
/* 216 */       JComponent localJComponent = (JComponent)paramPropertyChangeEvent.getSource();
/*     */ 
/* 218 */       if (paramPropertyChangeEvent.getNewValue() != null) {
/* 219 */         if (localJComponent == this.firstInvisibleAncestor) {
/* 220 */           addListeners(localJComponent, false);
/* 221 */           if (this.firstInvisibleAncestor == null)
/* 222 */             fireAncestorAdded(this.root, 1, localJComponent, localJComponent.getParent());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 227 */         int i = this.firstInvisibleAncestor == null ? 1 : 0;
/* 228 */         Container localContainer = (Container)paramPropertyChangeEvent.getOldValue();
/*     */ 
/* 230 */         removeListeners(localContainer);
/* 231 */         this.firstInvisibleAncestor = localJComponent;
/* 232 */         if (i != 0)
/* 233 */           fireAncestorRemoved(this.root, 2, localJComponent, localContainer);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AncestorNotifier
 * JD-Core Version:    0.6.2
 */