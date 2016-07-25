/*     */ package javax.swing;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import javax.swing.event.SwingPropertyChangeSupport;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class AbstractAction
/*     */   implements Action, Cloneable, Serializable
/*     */ {
/*     */   private static Boolean RECONFIGURE_ON_NULL;
/*  69 */   protected boolean enabled = true;
/*     */   private transient ArrayTable arrayTable;
/*     */   protected SwingPropertyChangeSupport changeSupport;
/*     */ 
/*     */   static boolean shouldReconfigure(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/*  82 */     if (paramPropertyChangeEvent.getPropertyName() == null) {
/*  83 */       synchronized (AbstractAction.class) {
/*  84 */         if (RECONFIGURE_ON_NULL == null) {
/*  85 */           RECONFIGURE_ON_NULL = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("swing.actions.reconfigureOnNull", "false")));
/*     */         }
/*     */ 
/*  89 */         return RECONFIGURE_ON_NULL.booleanValue();
/*     */       }
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   static void setEnabledFromAction(JComponent paramJComponent, Action paramAction)
/*     */   {
/* 102 */     paramJComponent.setEnabled(paramAction != null ? paramAction.isEnabled() : true);
/*     */   }
/*     */ 
/*     */   static void setToolTipTextFromAction(JComponent paramJComponent, Action paramAction)
/*     */   {
/* 112 */     paramJComponent.setToolTipText(paramAction != null ? (String)paramAction.getValue("ShortDescription") : null);
/*     */   }
/*     */ 
/*     */   static boolean hasSelectedKey(Action paramAction)
/*     */   {
/* 117 */     return (paramAction != null) && (paramAction.getValue("SwingSelectedKey") != null);
/*     */   }
/*     */ 
/*     */   static boolean isSelected(Action paramAction) {
/* 121 */     return Boolean.TRUE.equals(paramAction.getValue("SwingSelectedKey"));
/*     */   }
/*     */ 
/*     */   public AbstractAction()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AbstractAction(String paramString)
/*     */   {
/* 139 */     putValue("Name", paramString);
/*     */   }
/*     */ 
/*     */   public AbstractAction(String paramString, Icon paramIcon)
/*     */   {
/* 151 */     this(paramString);
/* 152 */     putValue("SmallIcon", paramIcon);
/*     */   }
/*     */ 
/*     */   public Object getValue(String paramString)
/*     */   {
/* 164 */     if (paramString == "enabled") {
/* 165 */       return Boolean.valueOf(this.enabled);
/*     */     }
/* 167 */     if (this.arrayTable == null) {
/* 168 */       return null;
/*     */     }
/* 170 */     return this.arrayTable.get(paramString);
/*     */   }
/*     */ 
/*     */   public void putValue(String paramString, Object paramObject)
/*     */   {
/* 181 */     Object localObject = null;
/* 182 */     if (paramString == "enabled")
/*     */     {
/* 192 */       if ((paramObject == null) || (!(paramObject instanceof Boolean))) {
/* 193 */         paramObject = Boolean.valueOf(false);
/*     */       }
/* 195 */       localObject = Boolean.valueOf(this.enabled);
/* 196 */       this.enabled = ((Boolean)paramObject).booleanValue();
/*     */     } else {
/* 198 */       if (this.arrayTable == null) {
/* 199 */         this.arrayTable = new ArrayTable();
/*     */       }
/* 201 */       if (this.arrayTable.containsKey(paramString)) {
/* 202 */         localObject = this.arrayTable.get(paramString);
/*     */       }
/*     */ 
/* 205 */       if (paramObject == null)
/* 206 */         this.arrayTable.remove(paramString);
/*     */       else {
/* 208 */         this.arrayTable.put(paramString, paramObject);
/*     */       }
/*     */     }
/* 211 */     firePropertyChange(paramString, localObject, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 221 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/* 232 */     boolean bool = this.enabled;
/*     */ 
/* 234 */     if (bool != paramBoolean) {
/* 235 */       this.enabled = paramBoolean;
/* 236 */       firePropertyChange("enabled", Boolean.valueOf(bool), Boolean.valueOf(paramBoolean));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getKeys()
/*     */   {
/* 251 */     if (this.arrayTable == null) {
/* 252 */       return null;
/*     */     }
/* 254 */     Object[] arrayOfObject = new Object[this.arrayTable.size()];
/* 255 */     this.arrayTable.getKeys(arrayOfObject);
/* 256 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 272 */     if ((this.changeSupport == null) || ((paramObject1 != null) && (paramObject2 != null) && (paramObject1.equals(paramObject2))))
/*     */     {
/* 274 */       return;
/*     */     }
/* 276 */     this.changeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 296 */     if (this.changeSupport == null) {
/* 297 */       this.changeSupport = new SwingPropertyChangeSupport(this);
/*     */     }
/* 299 */     this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 313 */     if (this.changeSupport == null) {
/* 314 */       return;
/*     */     }
/* 316 */     this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 329 */     if (this.changeSupport == null) {
/* 330 */       return new PropertyChangeListener[0];
/*     */     }
/* 332 */     return this.changeSupport.getPropertyChangeListeners();
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 343 */     AbstractAction localAbstractAction = (AbstractAction)super.clone();
/* 344 */     synchronized (this) {
/* 345 */       if (this.arrayTable != null) {
/* 346 */         localAbstractAction.arrayTable = ((ArrayTable)this.arrayTable.clone());
/*     */       }
/*     */     }
/* 349 */     return localAbstractAction;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 354 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 357 */     ArrayTable.writeArrayTable(paramObjectOutputStream, this.arrayTable);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException
/*     */   {
/* 362 */     paramObjectInputStream.defaultReadObject();
/* 363 */     for (int i = paramObjectInputStream.readInt() - 1; i >= 0; i--)
/* 364 */       putValue((String)paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AbstractAction
 * JD-Core Version:    0.6.2
 */