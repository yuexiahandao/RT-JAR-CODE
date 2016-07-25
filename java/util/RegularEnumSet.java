/*     */ package java.util;
/*     */ 
/*     */ class RegularEnumSet<E extends Enum<E>> extends EnumSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 3411599620347842686L;
/*  42 */   private long elements = 0L;
/*     */ 
/*     */   RegularEnumSet(Class<E> paramClass, Enum[] paramArrayOfEnum) {
/*  45 */     super(paramClass, paramArrayOfEnum);
/*     */   }
/*     */ 
/*     */   void addRange(E paramE1, E paramE2) {
/*  49 */     this.elements = (-1L >>> paramE1.ordinal() - paramE2.ordinal() - 1 << paramE1.ordinal());
/*     */   }
/*     */ 
/*     */   void addAll() {
/*  53 */     if (this.universe.length != 0)
/*  54 */       this.elements = (-1L >>> -this.universe.length);
/*     */   }
/*     */ 
/*     */   void complement() {
/*  58 */     if (this.universe.length != 0) {
/*  59 */       this.elements ^= -1L;
/*  60 */       this.elements &= -1L >>> -this.universe.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/*  75 */     return new EnumSetIterator();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 121 */     return Long.bitCount(this.elements);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 130 */     return this.elements == 0L;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 140 */     if (paramObject == null)
/* 141 */       return false;
/* 142 */     Class localClass = paramObject.getClass();
/* 143 */     if ((localClass != this.elementType) && (localClass.getSuperclass() != this.elementType)) {
/* 144 */       return false;
/*     */     }
/* 146 */     return (this.elements & 1L << ((Enum)paramObject).ordinal()) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 160 */     typeCheck(paramE);
/*     */ 
/* 162 */     long l = this.elements;
/* 163 */     this.elements |= 1L << paramE.ordinal();
/* 164 */     return this.elements != l;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 174 */     if (paramObject == null)
/* 175 */       return false;
/* 176 */     Class localClass = paramObject.getClass();
/* 177 */     if ((localClass != this.elementType) && (localClass.getSuperclass() != this.elementType)) {
/* 178 */       return false;
/*     */     }
/* 180 */     long l = this.elements;
/* 181 */     this.elements &= (1L << ((Enum)paramObject).ordinal() ^ 0xFFFFFFFF);
/* 182 */     return this.elements != l;
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection)
/*     */   {
/* 197 */     if (!(paramCollection instanceof RegularEnumSet)) {
/* 198 */       return super.containsAll(paramCollection);
/*     */     }
/* 200 */     RegularEnumSet localRegularEnumSet = (RegularEnumSet)paramCollection;
/* 201 */     if (localRegularEnumSet.elementType != this.elementType) {
/* 202 */       return localRegularEnumSet.isEmpty();
/*     */     }
/* 204 */     return (localRegularEnumSet.elements & (this.elements ^ 0xFFFFFFFF)) == 0L;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 216 */     if (!(paramCollection instanceof RegularEnumSet)) {
/* 217 */       return super.addAll(paramCollection);
/*     */     }
/* 219 */     RegularEnumSet localRegularEnumSet = (RegularEnumSet)paramCollection;
/* 220 */     if (localRegularEnumSet.elementType != this.elementType) {
/* 221 */       if (localRegularEnumSet.isEmpty()) {
/* 222 */         return false;
/*     */       }
/* 224 */       throw new ClassCastException(localRegularEnumSet.elementType + " != " + this.elementType);
/*     */     }
/*     */ 
/* 228 */     long l = this.elements;
/* 229 */     this.elements |= localRegularEnumSet.elements;
/* 230 */     return this.elements != l;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 242 */     if (!(paramCollection instanceof RegularEnumSet)) {
/* 243 */       return super.removeAll(paramCollection);
/*     */     }
/* 245 */     RegularEnumSet localRegularEnumSet = (RegularEnumSet)paramCollection;
/* 246 */     if (localRegularEnumSet.elementType != this.elementType) {
/* 247 */       return false;
/*     */     }
/* 249 */     long l = this.elements;
/* 250 */     this.elements &= (localRegularEnumSet.elements ^ 0xFFFFFFFF);
/* 251 */     return this.elements != l;
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection)
/*     */   {
/* 263 */     if (!(paramCollection instanceof RegularEnumSet)) {
/* 264 */       return super.retainAll(paramCollection);
/*     */     }
/* 266 */     RegularEnumSet localRegularEnumSet = (RegularEnumSet)paramCollection;
/* 267 */     if (localRegularEnumSet.elementType != this.elementType) {
/* 268 */       boolean bool = this.elements != 0L;
/* 269 */       this.elements = 0L;
/* 270 */       return bool;
/*     */     }
/*     */ 
/* 273 */     long l = this.elements;
/* 274 */     this.elements &= localRegularEnumSet.elements;
/* 275 */     return this.elements != l;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 282 */     this.elements = 0L;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 295 */     if (!(paramObject instanceof RegularEnumSet)) {
/* 296 */       return super.equals(paramObject);
/*     */     }
/* 298 */     RegularEnumSet localRegularEnumSet = (RegularEnumSet)paramObject;
/* 299 */     if (localRegularEnumSet.elementType != this.elementType)
/* 300 */       return (this.elements == 0L) && (localRegularEnumSet.elements == 0L);
/* 301 */     return localRegularEnumSet.elements == this.elements;
/*     */   }
/*     */ 
/*     */   private class EnumSetIterator<E extends Enum<E>>
/*     */     implements Iterator<E>
/*     */   {
/*     */     long unseen;
/*  89 */     long lastReturned = 0L;
/*     */ 
/*     */     EnumSetIterator() {
/*  92 */       this.unseen = RegularEnumSet.this.elements;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/*  96 */       return this.unseen != 0L;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 100 */       if (this.unseen == 0L)
/* 101 */         throw new NoSuchElementException();
/* 102 */       this.lastReturned = (this.unseen & -this.unseen);
/* 103 */       this.unseen -= this.lastReturned;
/* 104 */       return RegularEnumSet.this.universe[Long.numberOfTrailingZeros(this.lastReturned)];
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 108 */       if (this.lastReturned == 0L)
/* 109 */         throw new IllegalStateException();
/* 110 */       RegularEnumSet.access$074(RegularEnumSet.this, this.lastReturned ^ 0xFFFFFFFF);
/* 111 */       this.lastReturned = 0L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.RegularEnumSet
 * JD-Core Version:    0.6.2
 */