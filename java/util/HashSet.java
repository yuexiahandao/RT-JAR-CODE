/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class HashSet<E> extends AbstractSet<E>
/*     */   implements Set<E>, Cloneable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5024744406713321676L;
/*     */   private transient HashMap<E, Object> map;
/*  96 */   private static final Object PRESENT = new Object();
/*     */ 
/*     */   public HashSet()
/*     */   {
/* 103 */     this.map = new HashMap();
/*     */   }
/*     */ 
/*     */   public HashSet(Collection<? extends E> paramCollection)
/*     */   {
/* 116 */     this.map = new HashMap(Math.max((int)(paramCollection.size() / 0.75F) + 1, 16));
/* 117 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public HashSet(int paramInt, float paramFloat)
/*     */   {
/* 130 */     this.map = new HashMap(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public HashSet(int paramInt)
/*     */   {
/* 142 */     this.map = new HashMap(paramInt);
/*     */   }
/*     */ 
/*     */   HashSet(int paramInt, float paramFloat, boolean paramBoolean)
/*     */   {
/* 159 */     this.map = new LinkedHashMap(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 170 */     return this.map.keySet().iterator();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 179 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 188 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 201 */     return this.map.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 217 */     return this.map.put(paramE, PRESENT) == null;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 233 */     return this.map.remove(paramObject) == PRESENT;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 241 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 252 */       HashSet localHashSet = (HashSet)super.clone();
/* 253 */       localHashSet.map = ((HashMap)this.map.clone());
/* 254 */       return localHashSet; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 256 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 273 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 276 */     paramObjectOutputStream.writeInt(this.map.capacity());
/* 277 */     paramObjectOutputStream.writeFloat(this.map.loadFactor());
/*     */ 
/* 280 */     paramObjectOutputStream.writeInt(this.map.size());
/*     */ 
/* 283 */     for (Iterator localIterator = this.map.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 284 */       paramObjectOutputStream.writeObject(localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 294 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 297 */     int i = paramObjectInputStream.readInt();
/* 298 */     float f = paramObjectInputStream.readFloat();
/* 299 */     this.map = ((this instanceof LinkedHashSet) ? new LinkedHashMap(i, f) : new HashMap(i, f));
/*     */ 
/* 304 */     int j = paramObjectInputStream.readInt();
/*     */ 
/* 307 */     for (int k = 0; k < j; k++) {
/* 308 */       Object localObject = paramObjectInputStream.readObject();
/* 309 */       this.map.put(localObject, PRESENT);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.HashSet
 * JD-Core Version:    0.6.2
 */