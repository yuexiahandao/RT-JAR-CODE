/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class ConcurrentSkipListSet<E> extends AbstractSet<E>
/*     */   implements NavigableSet<E>, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2479143111061671589L;
/*     */   private final ConcurrentNavigableMap<E, Object> m;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long mapOffset;
/*     */ 
/*     */   public ConcurrentSkipListSet()
/*     */   {
/* 103 */     this.m = new ConcurrentSkipListMap();
/*     */   }
/*     */ 
/*     */   public ConcurrentSkipListSet(Comparator<? super E> paramComparator)
/*     */   {
/* 115 */     this.m = new ConcurrentSkipListMap(paramComparator);
/*     */   }
/*     */ 
/*     */   public ConcurrentSkipListSet(Collection<? extends E> paramCollection)
/*     */   {
/* 130 */     this.m = new ConcurrentSkipListMap();
/* 131 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public ConcurrentSkipListSet(SortedSet<E> paramSortedSet)
/*     */   {
/* 143 */     this.m = new ConcurrentSkipListMap(paramSortedSet.comparator());
/* 144 */     addAll(paramSortedSet);
/*     */   }
/*     */ 
/*     */   ConcurrentSkipListSet(ConcurrentNavigableMap<E, Object> paramConcurrentNavigableMap)
/*     */   {
/* 151 */     this.m = paramConcurrentNavigableMap;
/*     */   }
/*     */ 
/*     */   public ConcurrentSkipListSet<E> clone()
/*     */   {
/* 161 */     ConcurrentSkipListSet localConcurrentSkipListSet = null;
/*     */     try {
/* 163 */       localConcurrentSkipListSet = (ConcurrentSkipListSet)super.clone();
/* 164 */       localConcurrentSkipListSet.setMap(new ConcurrentSkipListMap(this.m));
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 166 */       throw new InternalError();
/*     */     }
/*     */ 
/* 169 */     return localConcurrentSkipListSet;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 191 */     return this.m.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 199 */     return this.m.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 214 */     return this.m.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 232 */     return this.m.putIfAbsent(paramE, Boolean.TRUE) == null;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 250 */     return this.m.remove(paramObject, Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 257 */     this.m.clear();
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 266 */     return this.m.navigableKeySet().iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<E> descendingIterator()
/*     */   {
/* 275 */     return this.m.descendingKeySet().iterator();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 295 */     if (paramObject == this)
/* 296 */       return true;
/* 297 */     if (!(paramObject instanceof Set))
/* 298 */       return false;
/* 299 */     Collection localCollection = (Collection)paramObject;
/*     */     try {
/* 301 */       return (containsAll(localCollection)) && (localCollection.containsAll(this));
/*     */     } catch (ClassCastException localClassCastException) {
/* 303 */       return false; } catch (NullPointerException localNullPointerException) {
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 324 */     boolean bool = false;
/* 325 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); )
/* 326 */       if (remove(localIterator.next()))
/* 327 */         bool = true;
/* 328 */     return bool;
/*     */   }
/*     */ 
/*     */   public E lower(E paramE)
/*     */   {
/* 338 */     return this.m.lowerKey(paramE);
/*     */   }
/*     */ 
/*     */   public E floor(E paramE)
/*     */   {
/* 346 */     return this.m.floorKey(paramE);
/*     */   }
/*     */ 
/*     */   public E ceiling(E paramE)
/*     */   {
/* 354 */     return this.m.ceilingKey(paramE);
/*     */   }
/*     */ 
/*     */   public E higher(E paramE)
/*     */   {
/* 362 */     return this.m.higherKey(paramE);
/*     */   }
/*     */ 
/*     */   public E pollFirst() {
/* 366 */     Map.Entry localEntry = this.m.pollFirstEntry();
/* 367 */     return localEntry == null ? null : localEntry.getKey();
/*     */   }
/*     */ 
/*     */   public E pollLast() {
/* 371 */     Map.Entry localEntry = this.m.pollLastEntry();
/* 372 */     return localEntry == null ? null : localEntry.getKey();
/*     */   }
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 380 */     return this.m.comparator();
/*     */   }
/*     */ 
/*     */   public E first()
/*     */   {
/* 387 */     return this.m.firstKey();
/*     */   }
/*     */ 
/*     */   public E last()
/*     */   {
/* 394 */     return this.m.lastKey();
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2)
/*     */   {
/* 407 */     return new ConcurrentSkipListSet(this.m.subMap(paramE1, paramBoolean1, paramE2, paramBoolean2));
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> headSet(E paramE, boolean paramBoolean)
/*     */   {
/* 418 */     return new ConcurrentSkipListSet(this.m.headMap(paramE, paramBoolean));
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> tailSet(E paramE, boolean paramBoolean)
/*     */   {
/* 427 */     return new ConcurrentSkipListSet(this.m.tailMap(paramE, paramBoolean));
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> subSet(E paramE1, E paramE2)
/*     */   {
/* 437 */     return subSet(paramE1, true, paramE2, false);
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> headSet(E paramE)
/*     */   {
/* 446 */     return headSet(paramE, false);
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> tailSet(E paramE)
/*     */   {
/* 455 */     return tailSet(paramE, true);
/*     */   }
/*     */ 
/*     */   public NavigableSet<E> descendingSet()
/*     */   {
/* 471 */     return new ConcurrentSkipListSet(this.m.descendingMap());
/*     */   }
/*     */ 
/*     */   private void setMap(ConcurrentNavigableMap<E, Object> paramConcurrentNavigableMap)
/*     */   {
/* 476 */     UNSAFE.putObjectVolatile(this, mapOffset, paramConcurrentNavigableMap);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 483 */       UNSAFE = Unsafe.getUnsafe();
/* 484 */       ConcurrentSkipListSet localConcurrentSkipListSet = ConcurrentSkipListSet.class;
/* 485 */       mapOffset = UNSAFE.objectFieldOffset(localConcurrentSkipListSet.getDeclaredField("m"));
/*     */     }
/*     */     catch (Exception localException) {
/* 488 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentSkipListSet
 * JD-Core Version:    0.6.2
 */