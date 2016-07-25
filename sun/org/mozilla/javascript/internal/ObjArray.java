/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class ObjArray
/*     */ {
/*     */   private int size;
/*     */   private boolean sealed;
/*     */   private static final int FIELDS_STORE_SIZE = 5;
/*     */   private transient Object f0;
/*     */   private transient Object f1;
/*     */   private transient Object f2;
/*     */   private transient Object f3;
/*     */   private transient Object f4;
/*     */   private transient Object[] data;
/*     */ 
/*     */   public final boolean isSealed()
/*     */   {
/*  53 */     return this.sealed;
/*     */   }
/*     */ 
/*     */   public final void seal()
/*     */   {
/*  58 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   public final boolean isEmpty()
/*     */   {
/*  63 */     return this.size == 0;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  68 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final void setSize(int paramInt)
/*     */   {
/*  73 */     if (paramInt < 0) throw new IllegalArgumentException();
/*  74 */     if (this.sealed) throw onSeledMutation();
/*  75 */     int i = this.size;
/*  76 */     if (paramInt < i) {
/*  77 */       for (int j = paramInt; j != i; j++)
/*  78 */         setImpl(j, null);
/*     */     }
/*  80 */     else if ((paramInt > i) && 
/*  81 */       (paramInt > 5)) {
/*  82 */       ensureCapacity(paramInt);
/*     */     }
/*     */ 
/*  85 */     this.size = paramInt;
/*     */   }
/*     */ 
/*     */   public final Object get(int paramInt)
/*     */   {
/*  90 */     if ((0 > paramInt) || (paramInt >= this.size)) throw onInvalidIndex(paramInt, this.size);
/*  91 */     return getImpl(paramInt);
/*     */   }
/*     */ 
/*     */   public final void set(int paramInt, Object paramObject)
/*     */   {
/*  96 */     if ((0 > paramInt) || (paramInt >= this.size)) throw onInvalidIndex(paramInt, this.size);
/*  97 */     if (this.sealed) throw onSeledMutation();
/*  98 */     setImpl(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   private Object getImpl(int paramInt)
/*     */   {
/* 103 */     switch (paramInt) { case 0:
/* 104 */       return this.f0;
/*     */     case 1:
/* 105 */       return this.f1;
/*     */     case 2:
/* 106 */       return this.f2;
/*     */     case 3:
/* 107 */       return this.f3;
/*     */     case 4:
/* 108 */       return this.f4;
/*     */     }
/* 110 */     return this.data[(paramInt - 5)];
/*     */   }
/*     */ 
/*     */   private void setImpl(int paramInt, Object paramObject)
/*     */   {
/* 115 */     switch (paramInt) { case 0:
/* 116 */       this.f0 = paramObject; break;
/*     */     case 1:
/* 117 */       this.f1 = paramObject; break;
/*     */     case 2:
/* 118 */       this.f2 = paramObject; break;
/*     */     case 3:
/* 119 */       this.f3 = paramObject; break;
/*     */     case 4:
/* 120 */       this.f4 = paramObject; break;
/*     */     default:
/* 121 */       this.data[(paramInt - 5)] = paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 128 */     int i = this.size;
/* 129 */     for (int j = 0; j != i; j++) {
/* 130 */       Object localObject = getImpl(j);
/* 131 */       if ((localObject == paramObject) || ((localObject != null) && (localObject.equals(paramObject)))) {
/* 132 */         return j;
/*     */       }
/*     */     }
/* 135 */     return -1;
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 140 */     for (int i = this.size; i != 0; ) {
/* 141 */       i--;
/* 142 */       Object localObject = getImpl(i);
/* 143 */       if ((localObject == paramObject) || ((localObject != null) && (localObject.equals(paramObject)))) {
/* 144 */         return i;
/*     */       }
/*     */     }
/* 147 */     return -1;
/*     */   }
/*     */ 
/*     */   public final Object peek()
/*     */   {
/* 152 */     int i = this.size;
/* 153 */     if (i == 0) throw onEmptyStackTopRead();
/* 154 */     return getImpl(i - 1);
/*     */   }
/*     */ 
/*     */   public final Object pop()
/*     */   {
/* 159 */     if (this.sealed) throw onSeledMutation();
/* 160 */     int i = this.size;
/* 161 */     i--;
/*     */     Object localObject;
/* 163 */     switch (i) { case -1:
/* 164 */       throw onEmptyStackTopRead();
/*     */     case 0:
/* 165 */       localObject = this.f0; this.f0 = null; break;
/*     */     case 1:
/* 166 */       localObject = this.f1; this.f1 = null; break;
/*     */     case 2:
/* 167 */       localObject = this.f2; this.f2 = null; break;
/*     */     case 3:
/* 168 */       localObject = this.f3; this.f3 = null; break;
/*     */     case 4:
/* 169 */       localObject = this.f4; this.f4 = null; break;
/*     */     default:
/* 171 */       localObject = this.data[(i - 5)];
/* 172 */       this.data[(i - 5)] = null;
/*     */     }
/* 174 */     this.size = i;
/* 175 */     return localObject;
/*     */   }
/*     */ 
/*     */   public final void push(Object paramObject)
/*     */   {
/* 180 */     add(paramObject);
/*     */   }
/*     */ 
/*     */   public final void add(Object paramObject)
/*     */   {
/* 185 */     if (this.sealed) throw onSeledMutation();
/* 186 */     int i = this.size;
/* 187 */     if (i >= 5) {
/* 188 */       ensureCapacity(i + 1);
/*     */     }
/* 190 */     this.size = (i + 1);
/* 191 */     setImpl(i, paramObject);
/*     */   }
/*     */ 
/*     */   public final void add(int paramInt, Object paramObject)
/*     */   {
/* 196 */     int i = this.size;
/* 197 */     if ((0 > paramInt) || (paramInt > i)) throw onInvalidIndex(paramInt, i + 1);
/* 198 */     if (this.sealed) throw onSeledMutation();
/*     */     Object localObject;
/* 200 */     switch (paramInt) {
/*     */     case 0:
/* 202 */       if (i == 0) { this.f0 = paramObject; } else {
/* 203 */         localObject = this.f0; this.f0 = paramObject; paramObject = localObject; } break;
/*     */     case 1:
/* 205 */       if (i == 1) { this.f1 = paramObject; } else {
/* 206 */         localObject = this.f1; this.f1 = paramObject; paramObject = localObject; } break;
/*     */     case 2:
/* 208 */       if (i == 2) { this.f2 = paramObject; } else {
/* 209 */         localObject = this.f2; this.f2 = paramObject; paramObject = localObject; } break;
/*     */     case 3:
/* 211 */       if (i == 3) { this.f3 = paramObject; } else {
/* 212 */         localObject = this.f3; this.f3 = paramObject; paramObject = localObject; } break;
/*     */     case 4:
/* 214 */       if (i == 4) { this.f4 = paramObject; } else {
/* 215 */         localObject = this.f4; this.f4 = paramObject; paramObject = localObject;
/*     */ 
/* 217 */         paramInt = 5; } break;
/*     */     default:
/* 219 */       ensureCapacity(i + 1);
/* 220 */       if (paramInt != i) {
/* 221 */         System.arraycopy(this.data, paramInt - 5, this.data, paramInt - 5 + 1, i - paramInt);
/*     */       }
/*     */ 
/* 225 */       this.data[(paramInt - 5)] = paramObject;
/*     */     }
/* 227 */     this.size = (i + 1);
/*     */   }
/*     */ 
/*     */   public final void remove(int paramInt)
/*     */   {
/* 232 */     int i = this.size;
/* 233 */     if ((0 > paramInt) || (paramInt >= i)) throw onInvalidIndex(paramInt, i);
/* 234 */     if (this.sealed) throw onSeledMutation();
/* 235 */     i--;
/* 236 */     switch (paramInt) {
/*     */     case 0:
/* 238 */       if (i == 0) this.f0 = null; else
/* 239 */         this.f0 = this.f1; break;
/*     */     case 1:
/* 241 */       if (i == 1) this.f1 = null; else
/* 242 */         this.f1 = this.f2; break;
/*     */     case 2:
/* 244 */       if (i == 2) this.f2 = null; else
/* 245 */         this.f2 = this.f3; break;
/*     */     case 3:
/* 247 */       if (i == 3) this.f3 = null; else
/* 248 */         this.f3 = this.f4; break;
/*     */     case 4:
/* 250 */       if (i == 4) { this.f4 = null; } else {
/* 251 */         this.f4 = this.data[0];
/*     */ 
/* 253 */         paramInt = 5; } break;
/*     */     default:
/* 255 */       if (paramInt != i) {
/* 256 */         System.arraycopy(this.data, paramInt - 5 + 1, this.data, paramInt - 5, i - paramInt);
/*     */       }
/*     */ 
/* 260 */       this.data[(i - 5)] = null;
/*     */     }
/* 262 */     this.size = i;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 267 */     if (this.sealed) throw onSeledMutation();
/* 268 */     int i = this.size;
/* 269 */     for (int j = 0; j != i; j++) {
/* 270 */       setImpl(j, null);
/*     */     }
/* 272 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public final Object[] toArray()
/*     */   {
/* 277 */     Object[] arrayOfObject = new Object[this.size];
/* 278 */     toArray(arrayOfObject, 0);
/* 279 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public final void toArray(Object[] paramArrayOfObject)
/*     */   {
/* 284 */     toArray(paramArrayOfObject, 0);
/*     */   }
/*     */ 
/*     */   public final void toArray(Object[] paramArrayOfObject, int paramInt)
/*     */   {
/* 289 */     int i = this.size;
/* 290 */     switch (i) {
/*     */     default:
/* 292 */       System.arraycopy(this.data, 0, paramArrayOfObject, paramInt + 5, i - 5);
/*     */     case 5:
/* 294 */       paramArrayOfObject[(paramInt + 4)] = this.f4;
/*     */     case 4:
/* 295 */       paramArrayOfObject[(paramInt + 3)] = this.f3;
/*     */     case 3:
/* 296 */       paramArrayOfObject[(paramInt + 2)] = this.f2;
/*     */     case 2:
/* 297 */       paramArrayOfObject[(paramInt + 1)] = this.f1;
/*     */     case 1:
/* 298 */       paramArrayOfObject[(paramInt + 0)] = this.f0;
/*     */     case 0:
/*     */     }
/*     */   }
/*     */ 
/*     */   private void ensureCapacity(int paramInt)
/*     */   {
/* 305 */     int i = paramInt - 5;
/* 306 */     if (i <= 0) throw new IllegalArgumentException();
/*     */     int j;
/* 307 */     if (this.data == null) {
/* 308 */       j = 10;
/* 309 */       if (j < i) {
/* 310 */         j = i;
/*     */       }
/* 312 */       this.data = new Object[j];
/*     */     } else {
/* 314 */       j = this.data.length;
/* 315 */       if (j < i) {
/* 316 */         if (j <= 5)
/* 317 */           j = 10;
/*     */         else {
/* 319 */           j *= 2;
/*     */         }
/* 321 */         if (j < i) {
/* 322 */           j = i;
/*     */         }
/* 324 */         Object[] arrayOfObject = new Object[j];
/* 325 */         if (this.size > 5) {
/* 326 */           System.arraycopy(this.data, 0, arrayOfObject, 0, this.size - 5);
/*     */         }
/*     */ 
/* 329 */         this.data = arrayOfObject;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static RuntimeException onInvalidIndex(int paramInt1, int paramInt2)
/*     */   {
/* 337 */     String str = paramInt1 + " ∉ [0, " + paramInt2 + ')';
/* 338 */     throw new IndexOutOfBoundsException(str);
/*     */   }
/*     */ 
/*     */   private static RuntimeException onEmptyStackTopRead()
/*     */   {
/* 343 */     throw new RuntimeException("Empty stack");
/*     */   }
/*     */ 
/*     */   private static RuntimeException onSeledMutation()
/*     */   {
/* 348 */     throw new IllegalStateException("Attempt to modify sealed array");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ObjArray
 * JD-Core Version:    0.6.2
 */