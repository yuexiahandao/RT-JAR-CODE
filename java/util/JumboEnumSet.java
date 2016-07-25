/*     */ package java.util;
/*     */ 
/*     */ class JumboEnumSet<E extends Enum<E>> extends EnumSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 334349849919042784L;
/*     */   private long[] elements;
/*  47 */   private int size = 0;
/*     */ 
/*     */   JumboEnumSet(Class<E> paramClass, Enum[] paramArrayOfEnum) {
/*  50 */     super(paramClass, paramArrayOfEnum);
/*  51 */     this.elements = new long[paramArrayOfEnum.length + 63 >>> 6];
/*     */   }
/*     */ 
/*     */   void addRange(E paramE1, E paramE2) {
/*  55 */     int i = paramE1.ordinal() >>> 6;
/*  56 */     int j = paramE2.ordinal() >>> 6;
/*     */ 
/*  58 */     if (i == j) {
/*  59 */       this.elements[i] = (-1L >>> paramE1.ordinal() - paramE2.ordinal() - 1 << paramE1.ordinal());
/*     */     }
/*     */     else {
/*  62 */       this.elements[i] = (-1L << paramE1.ordinal());
/*  63 */       for (int k = i + 1; k < j; k++)
/*  64 */         this.elements[k] = -1L;
/*  65 */       this.elements[j] = (-1L >>> 63 - paramE2.ordinal());
/*     */     }
/*  67 */     this.size = (paramE2.ordinal() - paramE1.ordinal() + 1);
/*     */   }
/*     */ 
/*     */   void addAll() {
/*  71 */     for (int i = 0; i < this.elements.length; i++)
/*  72 */       this.elements[i] = -1L;
/*  73 */     this.elements[(this.elements.length - 1)] >>>= -this.universe.length;
/*  74 */     this.size = this.universe.length;
/*     */   }
/*     */ 
/*     */   void complement() {
/*  78 */     for (int i = 0; i < this.elements.length; i++)
/*  79 */       this.elements[i] ^= -1L;
/*  80 */     this.elements[(this.elements.length - 1)] &= -1L >>> -this.universe.length;
/*  81 */     this.size = (this.universe.length - this.size);
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/*  94 */     return new EnumSetIterator();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 158 */     return this.size;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 167 */     return this.size == 0;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 177 */     if (paramObject == null)
/* 178 */       return false;
/* 179 */     Class localClass = paramObject.getClass();
/* 180 */     if ((localClass != this.elementType) && (localClass.getSuperclass() != this.elementType)) {
/* 181 */       return false;
/*     */     }
/* 183 */     int i = ((Enum)paramObject).ordinal();
/* 184 */     return (this.elements[(i >>> 6)] & 1L << i) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 198 */     typeCheck(paramE);
/*     */ 
/* 200 */     int i = paramE.ordinal();
/* 201 */     int j = i >>> 6;
/*     */ 
/* 203 */     long l = this.elements[j];
/* 204 */     this.elements[j] |= 1L << i;
/* 205 */     boolean bool = this.elements[j] != l;
/* 206 */     if (bool)
/* 207 */       this.size += 1;
/* 208 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 218 */     if (paramObject == null)
/* 219 */       return false;
/* 220 */     Class localClass = paramObject.getClass();
/* 221 */     if ((localClass != this.elementType) && (localClass.getSuperclass() != this.elementType))
/* 222 */       return false;
/* 223 */     int i = ((Enum)paramObject).ordinal();
/* 224 */     int j = i >>> 6;
/*     */ 
/* 226 */     long l = this.elements[j];
/* 227 */     this.elements[j] &= (1L << i ^ 0xFFFFFFFF);
/* 228 */     boolean bool = this.elements[j] != l;
/* 229 */     if (bool)
/* 230 */       this.size -= 1;
/* 231 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection)
/*     */   {
/* 246 */     if (!(paramCollection instanceof JumboEnumSet)) {
/* 247 */       return super.containsAll(paramCollection);
/*     */     }
/* 249 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)paramCollection;
/* 250 */     if (localJumboEnumSet.elementType != this.elementType) {
/* 251 */       return localJumboEnumSet.isEmpty();
/*     */     }
/* 253 */     for (int i = 0; i < this.elements.length; i++)
/* 254 */       if ((localJumboEnumSet.elements[i] & (this.elements[i] ^ 0xFFFFFFFF)) != 0L)
/* 255 */         return false;
/* 256 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 268 */     if (!(paramCollection instanceof JumboEnumSet)) {
/* 269 */       return super.addAll(paramCollection);
/*     */     }
/* 271 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)paramCollection;
/* 272 */     if (localJumboEnumSet.elementType != this.elementType) {
/* 273 */       if (localJumboEnumSet.isEmpty()) {
/* 274 */         return false;
/*     */       }
/* 276 */       throw new ClassCastException(localJumboEnumSet.elementType + " != " + this.elementType);
/*     */     }
/*     */ 
/* 280 */     for (int i = 0; i < this.elements.length; i++)
/* 281 */       this.elements[i] |= localJumboEnumSet.elements[i];
/* 282 */     return recalculateSize();
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 294 */     if (!(paramCollection instanceof JumboEnumSet)) {
/* 295 */       return super.removeAll(paramCollection);
/*     */     }
/* 297 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)paramCollection;
/* 298 */     if (localJumboEnumSet.elementType != this.elementType) {
/* 299 */       return false;
/*     */     }
/* 301 */     for (int i = 0; i < this.elements.length; i++)
/* 302 */       this.elements[i] &= (localJumboEnumSet.elements[i] ^ 0xFFFFFFFF);
/* 303 */     return recalculateSize();
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection)
/*     */   {
/* 315 */     if (!(paramCollection instanceof JumboEnumSet)) {
/* 316 */       return super.retainAll(paramCollection);
/*     */     }
/* 318 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)paramCollection;
/* 319 */     if (localJumboEnumSet.elementType != this.elementType) {
/* 320 */       i = this.size != 0 ? 1 : 0;
/* 321 */       clear();
/* 322 */       return i;
/*     */     }
/*     */ 
/* 325 */     for (int i = 0; i < this.elements.length; i++)
/* 326 */       this.elements[i] &= localJumboEnumSet.elements[i];
/* 327 */     return recalculateSize();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 334 */     Arrays.fill(this.elements, 0L);
/* 335 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 348 */     if (!(paramObject instanceof JumboEnumSet)) {
/* 349 */       return super.equals(paramObject);
/*     */     }
/* 351 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)paramObject;
/* 352 */     if (localJumboEnumSet.elementType != this.elementType) {
/* 353 */       return (this.size == 0) && (localJumboEnumSet.size == 0);
/*     */     }
/* 355 */     return Arrays.equals(localJumboEnumSet.elements, this.elements);
/*     */   }
/*     */ 
/*     */   private boolean recalculateSize()
/*     */   {
/* 362 */     int i = this.size;
/* 363 */     this.size = 0;
/* 364 */     for (long l : this.elements) {
/* 365 */       this.size += Long.bitCount(l);
/*     */     }
/* 367 */     return this.size != i;
/*     */   }
/*     */ 
/*     */   public EnumSet<E> clone() {
/* 371 */     JumboEnumSet localJumboEnumSet = (JumboEnumSet)super.clone();
/* 372 */     localJumboEnumSet.elements = ((long[])localJumboEnumSet.elements.clone());
/* 373 */     return localJumboEnumSet;
/*     */   }
/*     */ 
/*     */   private class EnumSetIterator<E extends Enum<E>>
/*     */     implements Iterator<E>
/*     */   {
/*     */     long unseen;
/* 107 */     int unseenIndex = 0;
/*     */ 
/* 113 */     long lastReturned = 0L;
/*     */ 
/* 118 */     int lastReturnedIndex = 0;
/*     */ 
/*     */     EnumSetIterator() {
/* 121 */       this.unseen = JumboEnumSet.this.elements[0];
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 125 */       while ((this.unseen == 0L) && (this.unseenIndex < JumboEnumSet.this.elements.length - 1))
/* 126 */         this.unseen = JumboEnumSet.this.elements[(++this.unseenIndex)];
/* 127 */       return this.unseen != 0L;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 131 */       if (!hasNext())
/* 132 */         throw new NoSuchElementException();
/* 133 */       this.lastReturned = (this.unseen & -this.unseen);
/* 134 */       this.lastReturnedIndex = this.unseenIndex;
/* 135 */       this.unseen -= this.lastReturned;
/* 136 */       return JumboEnumSet.this.universe[((this.lastReturnedIndex << 6) + Long.numberOfTrailingZeros(this.lastReturned))];
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 141 */       if (this.lastReturned == 0L)
/* 142 */         throw new IllegalStateException();
/* 143 */       long l = JumboEnumSet.this.elements[this.lastReturnedIndex];
/* 144 */       JumboEnumSet.this.elements[this.lastReturnedIndex] &= (this.lastReturned ^ 0xFFFFFFFF);
/* 145 */       if (l != JumboEnumSet.this.elements[this.lastReturnedIndex]) {
/* 146 */         JumboEnumSet.access$110(JumboEnumSet.this);
/*     */       }
/* 148 */       this.lastReturned = 0L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.JumboEnumSet
 * JD-Core Version:    0.6.2
 */