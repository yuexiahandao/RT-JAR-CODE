/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public class DefaultColorSelectionModel
/*     */   implements ColorSelectionModel, Serializable
/*     */ {
/*  47 */   protected transient ChangeEvent changeEvent = null;
/*     */ 
/*  49 */   protected EventListenerList listenerList = new EventListenerList();
/*     */   private Color selectedColor;
/*     */ 
/*     */   public DefaultColorSelectionModel()
/*     */   {
/*  59 */     this.selectedColor = Color.white;
/*     */   }
/*     */ 
/*     */   public DefaultColorSelectionModel(Color paramColor)
/*     */   {
/*  72 */     this.selectedColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getSelectedColor()
/*     */   {
/*  82 */     return this.selectedColor;
/*     */   }
/*     */ 
/*     */   public void setSelectedColor(Color paramColor)
/*     */   {
/*  97 */     if ((paramColor != null) && (!this.selectedColor.equals(paramColor))) {
/*  98 */       this.selectedColor = paramColor;
/*  99 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 110 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 118 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 131 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 143 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 144 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 145 */       if (arrayOfObject[i] == ChangeListener.class) {
/* 146 */         if (this.changeEvent == null) {
/* 147 */           this.changeEvent = new ChangeEvent(this);
/*     */         }
/* 149 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.DefaultColorSelectionModel
 * JD-Core Version:    0.6.2
 */