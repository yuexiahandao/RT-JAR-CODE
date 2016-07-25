/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.EventListener;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class EventListenerList
/*     */   implements Serializable
/*     */ {
/* 102 */   private static final Object[] NULL_ARRAY = new Object[0];
/*     */ 
/* 104 */   protected transient Object[] listenerList = NULL_ARRAY;
/*     */ 
/*     */   public Object[] getListenerList()
/*     */   {
/* 124 */     return this.listenerList;
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 136 */     Object[] arrayOfObject = this.listenerList;
/* 137 */     int i = getListenerCount(arrayOfObject, paramClass);
/* 138 */     EventListener[] arrayOfEventListener = (EventListener[])Array.newInstance(paramClass, i);
/* 139 */     int j = 0;
/* 140 */     for (int k = arrayOfObject.length - 2; k >= 0; k -= 2) {
/* 141 */       if (arrayOfObject[k] == paramClass) {
/* 142 */         arrayOfEventListener[(j++)] = ((EventListener)arrayOfObject[(k + 1)]);
/*     */       }
/*     */     }
/* 145 */     return arrayOfEventListener;
/*     */   }
/*     */ 
/*     */   public int getListenerCount()
/*     */   {
/* 152 */     return this.listenerList.length / 2;
/*     */   }
/*     */ 
/*     */   public int getListenerCount(Class<?> paramClass)
/*     */   {
/* 160 */     Object[] arrayOfObject = this.listenerList;
/* 161 */     return getListenerCount(arrayOfObject, paramClass);
/*     */   }
/*     */ 
/*     */   private int getListenerCount(Object[] paramArrayOfObject, Class paramClass) {
/* 165 */     int i = 0;
/* 166 */     for (int j = 0; j < paramArrayOfObject.length; j += 2) {
/* 167 */       if (paramClass == (Class)paramArrayOfObject[j])
/* 168 */         i++;
/*     */     }
/* 170 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized <T extends EventListener> void add(Class<T> paramClass, T paramT)
/*     */   {
/* 179 */     if (paramT == null)
/*     */     {
/* 183 */       return;
/*     */     }
/* 185 */     if (!paramClass.isInstance(paramT)) {
/* 186 */       throw new IllegalArgumentException("Listener " + paramT + " is not of type " + paramClass);
/*     */     }
/*     */ 
/* 189 */     if (this.listenerList == NULL_ARRAY)
/*     */     {
/* 192 */       this.listenerList = new Object[] { paramClass, paramT };
/*     */     }
/*     */     else {
/* 195 */       int i = this.listenerList.length;
/* 196 */       Object[] arrayOfObject = new Object[i + 2];
/* 197 */       System.arraycopy(this.listenerList, 0, arrayOfObject, 0, i);
/*     */ 
/* 199 */       arrayOfObject[i] = paramClass;
/* 200 */       arrayOfObject[(i + 1)] = paramT;
/*     */ 
/* 202 */       this.listenerList = arrayOfObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized <T extends EventListener> void remove(Class<T> paramClass, T paramT)
/*     */   {
/* 212 */     if (paramT == null)
/*     */     {
/* 216 */       return;
/*     */     }
/* 218 */     if (!paramClass.isInstance(paramT)) {
/* 219 */       throw new IllegalArgumentException("Listener " + paramT + " is not of type " + paramClass);
/*     */     }
/*     */ 
/* 223 */     int i = -1;
/* 224 */     for (int j = this.listenerList.length - 2; j >= 0; j -= 2) {
/* 225 */       if ((this.listenerList[j] == paramClass) && (this.listenerList[(j + 1)].equals(paramT) == true)) {
/* 226 */         i = j;
/* 227 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 232 */     if (i != -1) {
/* 233 */       Object[] arrayOfObject = new Object[this.listenerList.length - 2];
/*     */ 
/* 235 */       System.arraycopy(this.listenerList, 0, arrayOfObject, 0, i);
/*     */ 
/* 239 */       if (i < arrayOfObject.length) {
/* 240 */         System.arraycopy(this.listenerList, i + 2, arrayOfObject, i, arrayOfObject.length - i);
/*     */       }
/*     */ 
/* 243 */       this.listenerList = (arrayOfObject.length == 0 ? NULL_ARRAY : arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 249 */     Object[] arrayOfObject = this.listenerList;
/* 250 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 253 */     for (int i = 0; i < arrayOfObject.length; i += 2) {
/* 254 */       Class localClass = (Class)arrayOfObject[i];
/* 255 */       EventListener localEventListener = (EventListener)arrayOfObject[(i + 1)];
/* 256 */       if ((localEventListener != null) && ((localEventListener instanceof Serializable))) {
/* 257 */         paramObjectOutputStream.writeObject(localClass.getName());
/* 258 */         paramObjectOutputStream.writeObject(localEventListener);
/*     */       }
/*     */     }
/*     */ 
/* 262 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 267 */     this.listenerList = NULL_ARRAY;
/* 268 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 271 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 272 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 273 */       EventListener localEventListener = (EventListener)paramObjectInputStream.readObject();
/* 274 */       String str = (String)localObject;
/* 275 */       ReflectUtil.checkPackageAccess(str);
/* 276 */       add(Class.forName(str, true, localClassLoader), localEventListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 284 */     Object[] arrayOfObject = this.listenerList;
/* 285 */     String str = "EventListenerList: ";
/* 286 */     str = str + arrayOfObject.length / 2 + " listeners: ";
/* 287 */     for (int i = 0; i <= arrayOfObject.length - 2; i += 2) {
/* 288 */       str = str + " type " + ((Class)arrayOfObject[i]).getName();
/* 289 */       str = str + " listener " + arrayOfObject[(i + 1)];
/*     */     }
/* 291 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.EventListenerList
 * JD-Core Version:    0.6.2
 */