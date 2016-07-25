/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.misc.VM;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ class Util
/*     */ {
/*  48 */   private static final int TEMP_BUF_POOL_SIZE = IOUtil.IOV_MAX;
/*     */ 
/*  51 */   private static ThreadLocal<BufferCache> bufferCache = new ThreadLocal()
/*     */   {
/*     */     protected Util.BufferCache initialValue()
/*     */     {
/*  56 */       return new Util.BufferCache();
/*     */     }
/*  51 */   };
/*     */ 
/* 244 */   private static ThreadLocal<SoftReference<SelectorWrapper>> localSelector = new ThreadLocal();
/*     */ 
/* 248 */   private static ThreadLocal<SelectorWrapper> localSelectorWrapper = new ThreadLocal();
/*     */ 
/* 332 */   private static Unsafe unsafe = Unsafe.getUnsafe();
/*     */ 
/* 350 */   private static int pageSize = -1;
/*     */ 
/* 358 */   private static volatile Constructor directByteBufferConstructor = null;
/*     */ 
/* 408 */   private static volatile Constructor directByteBufferRConstructor = null;
/*     */ 
/* 461 */   private static volatile String bugLevel = null;
/*     */ 
/* 478 */   private static boolean loaded = false;
/*     */ 
/*     */   static ByteBuffer getTemporaryDirectBuffer(int paramInt)
/*     */   {
/* 162 */     BufferCache localBufferCache = (BufferCache)bufferCache.get();
/* 163 */     ByteBuffer localByteBuffer = localBufferCache.get(paramInt);
/* 164 */     if (localByteBuffer != null) {
/* 165 */       return localByteBuffer;
/*     */     }
/*     */ 
/* 170 */     if (!localBufferCache.isEmpty()) {
/* 171 */       localByteBuffer = localBufferCache.removeFirst();
/* 172 */       free(localByteBuffer);
/*     */     }
/* 174 */     return ByteBuffer.allocateDirect(paramInt);
/*     */   }
/*     */ 
/*     */   static void releaseTemporaryDirectBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 182 */     offerFirstTemporaryDirectBuffer(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   static void offerFirstTemporaryDirectBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 191 */     assert (paramByteBuffer != null);
/* 192 */     BufferCache localBufferCache = (BufferCache)bufferCache.get();
/* 193 */     if (!localBufferCache.offerFirst(paramByteBuffer))
/*     */     {
/* 195 */       free(paramByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void offerLastTemporaryDirectBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 206 */     assert (paramByteBuffer != null);
/* 207 */     BufferCache localBufferCache = (BufferCache)bufferCache.get();
/* 208 */     if (!localBufferCache.offerLast(paramByteBuffer))
/*     */     {
/* 210 */       free(paramByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void free(ByteBuffer paramByteBuffer)
/*     */   {
/* 218 */     ((DirectBuffer)paramByteBuffer).cleaner().clean();
/*     */   }
/*     */ 
/*     */   static Selector getTemporarySelector(SelectableChannel paramSelectableChannel)
/*     */     throws IOException
/*     */   {
/* 257 */     SoftReference localSoftReference = (SoftReference)localSelector.get();
/* 258 */     SelectorWrapper localSelectorWrapper1 = null;
/* 259 */     Object localObject = null;
/* 260 */     if ((localSoftReference == null) || ((localSelectorWrapper1 = (SelectorWrapper)localSoftReference.get()) == null) || ((localObject = localSelectorWrapper1.get()) == null) || (((Selector)localObject).provider() != paramSelectableChannel.provider()))
/*     */     {
/* 264 */       localObject = paramSelectableChannel.provider().openSelector();
/* 265 */       localSelectorWrapper1 = new SelectorWrapper((Selector)localObject, null);
/* 266 */       localSelector.set(new SoftReference(localSelectorWrapper1));
/*     */     }
/* 268 */     localSelectorWrapper.set(localSelectorWrapper1);
/* 269 */     return localObject;
/*     */   }
/*     */ 
/*     */   static void releaseTemporarySelector(Selector paramSelector)
/*     */     throws IOException
/*     */   {
/* 276 */     paramSelector.selectNow();
/* 277 */     assert (paramSelector.keys().isEmpty()) : "Temporary selector not empty";
/* 278 */     localSelectorWrapper.set(null);
/*     */   }
/*     */ 
/*     */   static ByteBuffer[] subsequence(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*     */   {
/* 285 */     if ((paramInt1 == 0) && (paramInt2 == paramArrayOfByteBuffer.length))
/* 286 */       return paramArrayOfByteBuffer;
/* 287 */     int i = paramInt2;
/* 288 */     ByteBuffer[] arrayOfByteBuffer = new ByteBuffer[i];
/* 289 */     for (int j = 0; j < i; j++)
/* 290 */       arrayOfByteBuffer[j] = paramArrayOfByteBuffer[(paramInt1 + j)];
/* 291 */     return arrayOfByteBuffer;
/*     */   }
/*     */ 
/*     */   static <E> Set<E> ungrowableSet(Set<E> paramSet) {
/* 295 */     return new Set() {
/*     */       public int size() {
/* 297 */         return this.val$s.size(); } 
/* 298 */       public boolean isEmpty() { return this.val$s.isEmpty(); } 
/* 299 */       public boolean contains(Object paramAnonymousObject) { return this.val$s.contains(paramAnonymousObject); } 
/* 300 */       public Object[] toArray() { return this.val$s.toArray(); } 
/* 301 */       public <T> T[] toArray(T[] paramAnonymousArrayOfT) { return this.val$s.toArray(paramAnonymousArrayOfT); } 
/* 302 */       public String toString() { return this.val$s.toString(); } 
/* 303 */       public Iterator<E> iterator() { return this.val$s.iterator(); } 
/* 304 */       public boolean equals(Object paramAnonymousObject) { return this.val$s.equals(paramAnonymousObject); } 
/* 305 */       public int hashCode() { return this.val$s.hashCode(); } 
/* 306 */       public void clear() { this.val$s.clear(); } 
/* 307 */       public boolean remove(Object paramAnonymousObject) { return this.val$s.remove(paramAnonymousObject); }
/*     */ 
/*     */       public boolean containsAll(Collection<?> paramAnonymousCollection) {
/* 310 */         return this.val$s.containsAll(paramAnonymousCollection);
/*     */       }
/*     */       public boolean removeAll(Collection<?> paramAnonymousCollection) {
/* 313 */         return this.val$s.removeAll(paramAnonymousCollection);
/*     */       }
/*     */       public boolean retainAll(Collection<?> paramAnonymousCollection) {
/* 316 */         return this.val$s.retainAll(paramAnonymousCollection);
/*     */       }
/*     */ 
/*     */       public boolean add(E paramAnonymousE) {
/* 320 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean addAll(Collection<? extends E> paramAnonymousCollection) {
/* 323 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static byte _get(long paramLong)
/*     */   {
/* 335 */     return unsafe.getByte(paramLong);
/*     */   }
/*     */ 
/*     */   private static void _put(long paramLong, byte paramByte) {
/* 339 */     unsafe.putByte(paramLong, paramByte);
/*     */   }
/*     */ 
/*     */   static void erase(ByteBuffer paramByteBuffer) {
/* 343 */     unsafe.setMemory(((DirectBuffer)paramByteBuffer).address(), paramByteBuffer.capacity(), (byte)0);
/*     */   }
/*     */ 
/*     */   static Unsafe unsafe() {
/* 347 */     return unsafe;
/*     */   }
/*     */ 
/*     */   static int pageSize()
/*     */   {
/* 353 */     if (pageSize == -1)
/* 354 */       pageSize = unsafe().pageSize();
/* 355 */     return pageSize;
/*     */   }
/*     */ 
/*     */   private static void initDBBConstructor()
/*     */   {
/* 361 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*     */         try {
/* 364 */           Class localClass = Class.forName("java.nio.DirectByteBuffer");
/* 365 */           Constructor localConstructor = localClass.getDeclaredConstructor(new Class[] { Integer.TYPE, Long.TYPE, FileDescriptor.class, Runnable.class });
/*     */ 
/* 370 */           localConstructor.setAccessible(true);
/* 371 */           Util.access$302(localConstructor);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 373 */           throw new InternalError();
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 375 */           throw new InternalError();
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 377 */           throw new InternalError();
/*     */         } catch (ClassCastException localClassCastException) {
/* 379 */           throw new InternalError();
/*     */         }
/* 381 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static MappedByteBuffer newMappedByteBuffer(int paramInt, long paramLong, FileDescriptor paramFileDescriptor, Runnable paramRunnable)
/*     */   {
/* 390 */     if (directByteBufferConstructor == null)
/* 391 */       initDBBConstructor(); MappedByteBuffer localMappedByteBuffer;
/*     */     try {
/* 393 */       localMappedByteBuffer = (MappedByteBuffer)directByteBufferConstructor.newInstance(new Object[] { new Integer(paramInt), new Long(paramLong), paramFileDescriptor, paramRunnable });
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 399 */       throw new InternalError();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 401 */       throw new InternalError();
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 403 */       throw new InternalError();
/*     */     }
/* 405 */     return localMappedByteBuffer;
/*     */   }
/*     */ 
/*     */   private static void initDBBRConstructor()
/*     */   {
/* 411 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*     */         try {
/* 414 */           Class localClass = Class.forName("java.nio.DirectByteBufferR");
/* 415 */           Constructor localConstructor = localClass.getDeclaredConstructor(new Class[] { Integer.TYPE, Long.TYPE, FileDescriptor.class, Runnable.class });
/*     */ 
/* 420 */           localConstructor.setAccessible(true);
/* 421 */           Util.access$402(localConstructor);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 423 */           throw new InternalError();
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 425 */           throw new InternalError();
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 427 */           throw new InternalError();
/*     */         } catch (ClassCastException localClassCastException) {
/* 429 */           throw new InternalError();
/*     */         }
/* 431 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static MappedByteBuffer newMappedByteBufferR(int paramInt, long paramLong, FileDescriptor paramFileDescriptor, Runnable paramRunnable)
/*     */   {
/* 440 */     if (directByteBufferRConstructor == null)
/* 441 */       initDBBRConstructor(); MappedByteBuffer localMappedByteBuffer;
/*     */     try {
/* 443 */       localMappedByteBuffer = (MappedByteBuffer)directByteBufferRConstructor.newInstance(new Object[] { new Integer(paramInt), new Long(paramLong), paramFileDescriptor, paramRunnable });
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 449 */       throw new InternalError();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 451 */       throw new InternalError();
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 453 */       throw new InternalError();
/*     */     }
/* 455 */     return localMappedByteBuffer;
/*     */   }
/*     */ 
/*     */   static boolean atBugLevel(String paramString)
/*     */   {
/* 464 */     if (bugLevel == null) {
/* 465 */       if (!VM.isBooted())
/* 466 */         return false;
/* 467 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.bugLevel"));
/*     */ 
/* 469 */       bugLevel = str != null ? str : "";
/*     */     }
/* 471 */     return bugLevel.equals(paramString);
/*     */   }
/*     */ 
/*     */   static void load()
/*     */   {
/* 481 */     synchronized (Util.class) {
/* 482 */       if (loaded)
/* 483 */         return;
/* 484 */       loaded = true;
/* 485 */       AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */ 
/* 487 */       AccessController.doPrivileged(new LoadLibraryAction("nio"));
/*     */ 
/* 491 */       IOUtil.initIDs();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class BufferCache
/*     */   {
/*     */     private ByteBuffer[] buffers;
/*     */     private int count;
/*     */     private int start;
/*     */ 
/*     */     private int next(int paramInt)
/*     */     {
/*  74 */       return (paramInt + 1) % Util.TEMP_BUF_POOL_SIZE;
/*     */     }
/*     */ 
/*     */     BufferCache() {
/*  78 */       this.buffers = new ByteBuffer[Util.TEMP_BUF_POOL_SIZE];
/*     */     }
/*     */ 
/*     */     ByteBuffer get(int paramInt)
/*     */     {
/*  86 */       if (this.count == 0) {
/*  87 */         return null;
/*     */       }
/*  89 */       ByteBuffer[] arrayOfByteBuffer = this.buffers;
/*     */ 
/*  92 */       Object localObject = arrayOfByteBuffer[this.start];
/*  93 */       if (((ByteBuffer)localObject).capacity() < paramInt) {
/*  94 */         localObject = null;
/*  95 */         int i = this.start;
/*  96 */         while ((i = next(i)) != this.start) {
/*  97 */           ByteBuffer localByteBuffer = arrayOfByteBuffer[i];
/*  98 */           if (localByteBuffer == null)
/*     */             break;
/* 100 */           if (localByteBuffer.capacity() >= paramInt) {
/* 101 */             localObject = localByteBuffer;
/* 102 */             break;
/*     */           }
/*     */         }
/* 105 */         if (localObject == null) {
/* 106 */           return null;
/*     */         }
/* 108 */         arrayOfByteBuffer[i] = arrayOfByteBuffer[this.start];
/*     */       }
/*     */ 
/* 112 */       arrayOfByteBuffer[this.start] = null;
/* 113 */       this.start = next(this.start);
/* 114 */       this.count -= 1;
/*     */ 
/* 117 */       ((ByteBuffer)localObject).rewind();
/* 118 */       ((ByteBuffer)localObject).limit(paramInt);
/* 119 */       return localObject;
/*     */     }
/*     */ 
/*     */     boolean offerFirst(ByteBuffer paramByteBuffer) {
/* 123 */       if (this.count >= Util.TEMP_BUF_POOL_SIZE) {
/* 124 */         return false;
/*     */       }
/* 126 */       this.start = ((this.start + Util.TEMP_BUF_POOL_SIZE - 1) % Util.TEMP_BUF_POOL_SIZE);
/* 127 */       this.buffers[this.start] = paramByteBuffer;
/* 128 */       this.count += 1;
/* 129 */       return true;
/*     */     }
/*     */ 
/*     */     boolean offerLast(ByteBuffer paramByteBuffer)
/*     */     {
/* 134 */       if (this.count >= Util.TEMP_BUF_POOL_SIZE) {
/* 135 */         return false;
/*     */       }
/* 137 */       int i = (this.start + this.count) % Util.TEMP_BUF_POOL_SIZE;
/* 138 */       this.buffers[i] = paramByteBuffer;
/* 139 */       this.count += 1;
/* 140 */       return true;
/*     */     }
/*     */ 
/*     */     boolean isEmpty()
/*     */     {
/* 145 */       return this.count == 0;
/*     */     }
/*     */ 
/*     */     ByteBuffer removeFirst() {
/* 149 */       assert (this.count > 0);
/* 150 */       ByteBuffer localByteBuffer = this.buffers[this.start];
/* 151 */       this.buffers[this.start] = null;
/* 152 */       this.start = next(this.start);
/* 153 */       this.count -= 1;
/* 154 */       return localByteBuffer;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SelectorWrapper
/*     */   {
/*     */     private Selector sel;
/*     */ 
/*     */     private SelectorWrapper(Selector paramSelector)
/*     */     {
/* 224 */       this.sel = paramSelector;
/* 225 */       Cleaner.create(this, new Closer(paramSelector, null));
/*     */     }
/*     */ 
/*     */     public Selector get()
/*     */     {
/* 240 */       return this.sel;
/*     */     }
/*     */ 
/*     */     private static class Closer
/*     */       implements Runnable
/*     */     {
/*     */       private Selector sel;
/*     */ 
/*     */       private Closer(Selector paramSelector)
/*     */       {
/* 230 */         this.sel = paramSelector;
/*     */       }
/*     */       public void run() {
/*     */         try {
/* 234 */           this.sel.close();
/*     */         } catch (Throwable localThrowable) {
/* 236 */           throw new Error(localThrowable);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Util
 * JD-Core Version:    0.6.2
 */