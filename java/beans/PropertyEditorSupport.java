/*     */ package java.beans;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class PropertyEditorSupport
/*     */   implements PropertyEditor
/*     */ {
/*     */   private Object value;
/*     */   private Object source;
/*     */   private Vector listeners;
/*     */ 
/*     */   public PropertyEditorSupport()
/*     */   {
/*  44 */     setSource(this);
/*     */   }
/*     */ 
/*     */   public PropertyEditorSupport(Object paramObject)
/*     */   {
/*  54 */     if (paramObject == null) {
/*  55 */       throw new NullPointerException();
/*     */     }
/*  57 */     setSource(paramObject);
/*     */   }
/*     */ 
/*     */   public Object getSource()
/*     */   {
/*  70 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setSource(Object paramObject)
/*     */   {
/*  84 */     this.source = paramObject;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/*  96 */     this.value = paramObject;
/*  97 */     firePropertyChange();
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 106 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean isPaintable()
/*     */   {
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   public void paintValue(Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getJavaInitializationString()
/*     */   {
/* 149 */     return "???";
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 165 */     return this.value != null ? this.value.toString() : null;
/*     */   }
/*     */ 
/*     */   public void setAsText(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 179 */     if ((this.value instanceof String)) {
/* 180 */       setValue(paramString);
/* 181 */       return;
/*     */     }
/* 183 */     throw new IllegalArgumentException(paramString);
/*     */   }
/*     */ 
/*     */   public String[] getTags()
/*     */   {
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getCustomEditor()
/*     */   {
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean supportsCustomEditor()
/*     */   {
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 253 */     if (this.listeners == null) {
/* 254 */       this.listeners = new Vector();
/*     */     }
/* 256 */     this.listeners.addElement(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 271 */     if (this.listeners == null) {
/* 272 */       return;
/*     */     }
/* 274 */     this.listeners.removeElement(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void firePropertyChange()
/*     */   {
/*     */     Vector localVector;
/* 282 */     synchronized (this) {
/* 283 */       if (this.listeners == null) {
/* 284 */         return;
/*     */       }
/* 286 */       localVector = (Vector)this.listeners.clone();
/*     */     }
/*     */ 
/* 289 */     ??? = new PropertyChangeEvent(this.source, null, null, null);
/*     */ 
/* 291 */     for (int i = 0; i < localVector.size(); i++) {
/* 292 */       PropertyChangeListener localPropertyChangeListener = (PropertyChangeListener)localVector.elementAt(i);
/* 293 */       localPropertyChangeListener.propertyChange((PropertyChangeEvent)???);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyEditorSupport
 * JD-Core Version:    0.6.2
 */