/*     */ package java.beans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PropertyChangeSupport
/*     */   implements Serializable
/*     */ {
/*  82 */   private PropertyChangeListenerMap map = new PropertyChangeListenerMap(null);
/*     */   private Object source;
/* 490 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("children", Hashtable.class), new ObjectStreamField("source", Object.class), new ObjectStreamField("propertyChangeSupportSerializedDataVersion", Integer.TYPE) };
/*     */   static final long serialVersionUID = 6401253773779951803L;
/*     */ 
/*     */   public PropertyChangeSupport(Object paramObject)
/*     */   {
/*  90 */     if (paramObject == null) {
/*  91 */       throw new NullPointerException();
/*     */     }
/*  93 */     this.source = paramObject;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 107 */     if (paramPropertyChangeListener == null) {
/* 108 */       return;
/*     */     }
/* 110 */     if ((paramPropertyChangeListener instanceof PropertyChangeListenerProxy)) {
/* 111 */       PropertyChangeListenerProxy localPropertyChangeListenerProxy = (PropertyChangeListenerProxy)paramPropertyChangeListener;
/*     */ 
/* 114 */       addPropertyChangeListener(localPropertyChangeListenerProxy.getPropertyName(), (PropertyChangeListener)localPropertyChangeListenerProxy.getListener());
/*     */     }
/*     */     else {
/* 117 */       this.map.add(null, paramPropertyChangeListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 133 */     if (paramPropertyChangeListener == null) {
/* 134 */       return;
/*     */     }
/* 136 */     if ((paramPropertyChangeListener instanceof PropertyChangeListenerProxy)) {
/* 137 */       PropertyChangeListenerProxy localPropertyChangeListenerProxy = (PropertyChangeListenerProxy)paramPropertyChangeListener;
/*     */ 
/* 140 */       removePropertyChangeListener(localPropertyChangeListenerProxy.getPropertyName(), (PropertyChangeListener)localPropertyChangeListenerProxy.getListener());
/*     */     }
/*     */     else {
/* 143 */       this.map.remove(null, paramPropertyChangeListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 179 */     return (PropertyChangeListener[])this.map.getListeners();
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 198 */     if ((paramPropertyChangeListener == null) || (paramString == null)) {
/* 199 */       return;
/*     */     }
/* 201 */     paramPropertyChangeListener = this.map.extract(paramPropertyChangeListener);
/* 202 */     if (paramPropertyChangeListener != null)
/* 203 */       this.map.add(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 223 */     if ((paramPropertyChangeListener == null) || (paramString == null)) {
/* 224 */       return;
/*     */     }
/* 226 */     paramPropertyChangeListener = this.map.extract(paramPropertyChangeListener);
/* 227 */     if (paramPropertyChangeListener != null)
/* 228 */       this.map.remove(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*     */   {
/* 244 */     return (PropertyChangeListener[])this.map.getListeners(paramString);
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 262 */     if ((paramObject1 == null) || (paramObject2 == null) || (!paramObject1.equals(paramObject2)))
/* 263 */       firePropertyChange(new PropertyChangeEvent(this.source, paramString, paramObject1, paramObject2));
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 282 */     if (paramInt1 != paramInt2)
/* 283 */       firePropertyChange(paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 302 */     if (paramBoolean1 != paramBoolean2)
/* 303 */       firePropertyChange(paramString, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2));
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 317 */     Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 318 */     Object localObject2 = paramPropertyChangeEvent.getNewValue();
/* 319 */     if ((localObject1 == null) || (localObject2 == null) || (!localObject1.equals(localObject2))) {
/* 320 */       String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 322 */       PropertyChangeListener[] arrayOfPropertyChangeListener1 = (PropertyChangeListener[])this.map.get(null);
/* 323 */       PropertyChangeListener[] arrayOfPropertyChangeListener2 = str != null ? (PropertyChangeListener[])this.map.get(str) : null;
/*     */ 
/* 327 */       fire(arrayOfPropertyChangeListener1, paramPropertyChangeEvent);
/* 328 */       fire(arrayOfPropertyChangeListener2, paramPropertyChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void fire(PropertyChangeListener[] paramArrayOfPropertyChangeListener, PropertyChangeEvent paramPropertyChangeEvent) {
/* 333 */     if (paramArrayOfPropertyChangeListener != null)
/* 334 */       for (PropertyChangeListener localPropertyChangeListener : paramArrayOfPropertyChangeListener)
/* 335 */         localPropertyChangeListener.propertyChange(paramPropertyChangeEvent);
/*     */   }
/*     */ 
/*     */   public void fireIndexedPropertyChange(String paramString, int paramInt, Object paramObject1, Object paramObject2)
/*     */   {
/* 357 */     if ((paramObject1 == null) || (paramObject2 == null) || (!paramObject1.equals(paramObject2)))
/* 358 */       firePropertyChange(new IndexedPropertyChangeEvent(this.source, paramString, paramObject1, paramObject2, paramInt));
/*     */   }
/*     */ 
/*     */   public void fireIndexedPropertyChange(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 379 */     if (paramInt2 != paramInt3)
/* 380 */       fireIndexedPropertyChange(paramString, paramInt1, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3));
/*     */   }
/*     */ 
/*     */   public void fireIndexedPropertyChange(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 401 */     if (paramBoolean1 != paramBoolean2)
/* 402 */       fireIndexedPropertyChange(paramString, paramInt, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2));
/*     */   }
/*     */ 
/*     */   public boolean hasListeners(String paramString)
/*     */   {
/* 415 */     return this.map.hasListeners(paramString);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 425 */     Hashtable localHashtable = null;
/* 426 */     PropertyChangeListener[] arrayOfPropertyChangeListener = null;
/*     */     Object localObject1;
/*     */     PropertyChangeSupport localPropertyChangeSupport;
/* 427 */     synchronized (this.map) {
/* 428 */       for (localObject1 = this.map.getEntries().iterator(); ((Iterator)localObject1).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject1).next();
/* 429 */         String str = (String)localEntry.getKey();
/* 430 */         if (str == null) {
/* 431 */           arrayOfPropertyChangeListener = (PropertyChangeListener[])localEntry.getValue();
/*     */         } else {
/* 433 */           if (localHashtable == null) {
/* 434 */             localHashtable = new Hashtable();
/*     */           }
/* 436 */           localPropertyChangeSupport = new PropertyChangeSupport(this.source);
/* 437 */           localPropertyChangeSupport.map.set(null, (EventListener[])localEntry.getValue());
/* 438 */           localHashtable.put(str, localPropertyChangeSupport);
/*     */         }
/*     */       }
/*     */     }
/* 442 */     ??? = paramObjectOutputStream.putFields();
/* 443 */     ((ObjectOutputStream.PutField)???).put("children", localHashtable);
/* 444 */     ((ObjectOutputStream.PutField)???).put("source", this.source);
/* 445 */     ((ObjectOutputStream.PutField)???).put("propertyChangeSupportSerializedDataVersion", 2);
/* 446 */     paramObjectOutputStream.writeFields();
/*     */ 
/* 448 */     if (arrayOfPropertyChangeListener != null) {
/* 449 */       for (localPropertyChangeSupport : arrayOfPropertyChangeListener) {
/* 450 */         if ((localPropertyChangeSupport instanceof Serializable)) {
/* 451 */           paramObjectOutputStream.writeObject(localPropertyChangeSupport);
/*     */         }
/*     */       }
/*     */     }
/* 455 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException {
/* 459 */     this.map = new PropertyChangeListenerMap(null);
/*     */ 
/* 461 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 463 */     Hashtable localHashtable = (Hashtable)localGetField.get("children", null);
/* 464 */     this.source = localGetField.get("source", null);
/* 465 */     localGetField.get("propertyChangeSupportSerializedDataVersion", 2);
/*     */     Object localObject;
/* 468 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 469 */       this.map.add(null, (PropertyChangeListener)localObject);
/*     */     }
/* 471 */     if (localHashtable != null)
/* 472 */       for (Map.Entry localEntry : localHashtable.entrySet())
/* 473 */         for (PropertyChangeListener localPropertyChangeListener : ((PropertyChangeSupport)localEntry.getValue()).getPropertyChangeListeners())
/* 474 */           this.map.add((String)localEntry.getKey(), localPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   private static final class PropertyChangeListenerMap extends ChangeListenerMap<PropertyChangeListener>
/*     */   {
/* 506 */     private static final PropertyChangeListener[] EMPTY = new PropertyChangeListener[0];
/*     */ 
/*     */     protected PropertyChangeListener[] newArray(int paramInt)
/*     */     {
/* 518 */       return 0 < paramInt ? new PropertyChangeListener[paramInt] : EMPTY;
/*     */     }
/*     */ 
/*     */     protected PropertyChangeListener newProxy(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */     {
/* 533 */       return new PropertyChangeListenerProxy(paramString, paramPropertyChangeListener);
/*     */     }
/*     */ 
/*     */     public final PropertyChangeListener extract(PropertyChangeListener paramPropertyChangeListener)
/*     */     {
/* 540 */       while ((paramPropertyChangeListener instanceof PropertyChangeListenerProxy)) {
/* 541 */         paramPropertyChangeListener = (PropertyChangeListener)((PropertyChangeListenerProxy)paramPropertyChangeListener).getListener();
/*     */       }
/* 543 */       return paramPropertyChangeListener;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyChangeSupport
 * JD-Core Version:    0.6.2
 */