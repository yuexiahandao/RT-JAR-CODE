/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class Injector
/*     */ {
/*     */   private static final ReentrantReadWriteLock irwl;
/*     */   private static final Lock ir;
/*     */   private static final Lock iw;
/*     */   private static final Map<ClassLoader, WeakReference<Injector>> injectors;
/*     */   private static final Logger logger;
/* 134 */   private final Map<String, Class> classes = new HashMap();
/* 135 */   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
/* 136 */   private final Lock r = this.rwl.readLock();
/* 137 */   private final Lock w = this.rwl.writeLock();
/*     */   private final ClassLoader parent;
/*     */   private final boolean loadable;
/*     */   private static final Method defineClass;
/*     */   private static final Method resolveClass;
/*     */   private static final Method findLoadedClass;
/*     */ 
/*     */   static Class inject(ClassLoader cl, String className, byte[] image)
/*     */   {
/*  75 */     Injector injector = get(cl);
/*  76 */     if (injector != null) {
/*  77 */       return injector.inject(className, image);
/*     */     }
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   static Class find(ClassLoader cl, String className)
/*     */   {
/*  87 */     Injector injector = get(cl);
/*  88 */     if (injector != null) {
/*  89 */       return injector.find(className);
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   private static Injector get(ClassLoader cl)
/*     */   {
/* 102 */     Injector injector = null;
/*     */ 
/* 104 */     ir.lock();
/*     */     WeakReference wr;
/*     */     try
/*     */     {
/* 106 */       wr = (WeakReference)injectors.get(cl);
/*     */     } finally {
/* 108 */       ir.unlock();
/*     */     }
/* 110 */     if (wr != null) {
/* 111 */       injector = (Injector)wr.get();
/*     */     }
/* 113 */     if (injector == null) {
/*     */       try {
/* 115 */         wr = new WeakReference(injector = new Injector(cl));
/* 116 */         iw.lock();
/*     */         try {
/* 118 */           if (!injectors.containsKey(cl))
/* 119 */             injectors.put(cl, wr);
/*     */         }
/*     */         finally {
/* 122 */           iw.unlock();
/*     */         }
/*     */       } catch (SecurityException e) {
/* 125 */         logger.log(Level.FINE, "Unable to set up a back-door for the injector", e);
/* 126 */         return null;
/*     */       }
/*     */     }
/* 129 */     return injector;
/*     */   }
/*     */ 
/*     */   private Injector(ClassLoader parent)
/*     */   {
/* 171 */     this.parent = parent;
/* 172 */     assert (parent != null);
/*     */ 
/* 174 */     boolean loadableCheck = false;
/*     */     try
/*     */     {
/* 177 */       loadableCheck = parent.loadClass(Accessor.class.getName()) == Accessor.class;
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*     */     }
/* 182 */     this.loadable = loadableCheck;
/*     */   }
/*     */ 
/*     */   private Class inject(String className, byte[] image)
/*     */   {
/* 187 */     if (!this.loadable)
/*     */     {
/* 189 */       return null;
/*     */     }
/*     */ 
/* 192 */     boolean wlocked = false;
/* 193 */     boolean rlocked = false;
/*     */     try
/*     */     {
/* 196 */       this.r.lock();
/* 197 */       rlocked = true;
/*     */ 
/* 199 */       Class c = (Class)this.classes.get(className);
/*     */ 
/* 203 */       this.r.unlock();
/* 204 */       rlocked = false;
/*     */       Throwable t;
/* 207 */       if (c == null)
/*     */       {
/*     */         try {
/* 210 */           c = (Class)findLoadedClass.invoke(this.parent, new Object[] { className.replace('/', '.') });
/*     */         } catch (IllegalArgumentException e) {
/* 212 */           logger.log(Level.FINE, "Unable to find " + className, e);
/*     */         } catch (IllegalAccessException e) {
/* 214 */           logger.log(Level.FINE, "Unable to find " + className, e);
/*     */         } catch (InvocationTargetException e) {
/* 216 */           t = e.getTargetException();
/* 217 */           logger.log(Level.FINE, "Unable to find " + className, t);
/*     */         }
/*     */ 
/* 220 */         if (c != null)
/*     */         {
/* 222 */           this.w.lock();
/* 223 */           wlocked = true;
/*     */ 
/* 225 */           this.classes.put(className, c);
/*     */ 
/* 227 */           this.w.unlock();
/* 228 */           wlocked = false;
/*     */ 
/* 230 */           return c;
/*     */         }
/*     */       }
/*     */ 
/* 234 */       if (c == null)
/*     */       {
/* 236 */         this.r.lock();
/* 237 */         rlocked = true;
/*     */ 
/* 239 */         c = (Class)this.classes.get(className);
/*     */ 
/* 243 */         this.r.unlock();
/* 244 */         rlocked = false;
/*     */ 
/* 246 */         if (c == null)
/*     */         {
/*     */           try
/*     */           {
/* 250 */             c = (Class)defineClass.invoke(this.parent, new Object[] { className.replace('/', '.'), image, Integer.valueOf(0), Integer.valueOf(image.length) });
/* 251 */             resolveClass.invoke(this.parent, new Object[] { c });
/*     */           } catch (IllegalAccessException e) {
/* 253 */             logger.log(Level.FINE, "Unable to inject " + className, e);
/* 254 */             return null;
/*     */           } catch (InvocationTargetException e) {
/* 256 */             t = e.getTargetException();
/* 257 */             if ((t instanceof LinkageError))
/* 258 */               logger.log(Level.FINE, "duplicate class definition bug occured? Please report this : " + className, t);
/*     */             else {
/* 260 */               logger.log(Level.FINE, "Unable to inject " + className, t);
/*     */             }
/* 262 */             return null;
/*     */           } catch (SecurityException e) {
/* 264 */             logger.log(Level.FINE, "Unable to inject " + className, e);
/* 265 */             return null;
/*     */           }
/*     */           catch (LinkageError e)
/*     */           {
/*     */             Throwable t;
/* 267 */             logger.log(Level.FINE, "Unable to inject " + className, e);
/* 268 */             return null;
/*     */           }
/*     */ 
/* 271 */           this.w.lock();
/* 272 */           wlocked = true;
/*     */ 
/* 277 */           if (!this.classes.containsKey(className)) {
/* 278 */             this.classes.put(className, c);
/*     */           }
/*     */ 
/* 281 */           this.w.unlock();
/* 282 */           wlocked = false;
/*     */         }
/*     */       }
/* 285 */       return c;
/*     */     } finally {
/* 287 */       if (rlocked) {
/* 288 */         this.r.unlock();
/*     */       }
/* 290 */       if (wlocked)
/* 291 */         this.w.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Class find(String className)
/*     */   {
/* 297 */     this.r.lock();
/*     */     try {
/* 299 */       return (Class)this.classes.get(className);
/*     */     } finally {
/* 301 */       this.r.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  61 */     irwl = new ReentrantReadWriteLock();
/*  62 */     ir = irwl.readLock();
/*  63 */     iw = irwl.writeLock();
/*  64 */     injectors = new WeakHashMap();
/*     */ 
/*  66 */     logger = Util.getClassLogger();
/*     */     try
/*     */     {
/* 150 */       defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, [B.class, Integer.TYPE, Integer.TYPE });
/* 151 */       resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", new Class[] { Class.class });
/* 152 */       findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
/*     */     }
/*     */     catch (NoSuchMethodException e) {
/* 155 */       throw new NoSuchMethodError(e.getMessage());
/*     */     }
/* 157 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/* 162 */         Injector.defineClass.setAccessible(true);
/* 163 */         Injector.resolveClass.setAccessible(true);
/* 164 */         Injector.findLoadedClass.setAccessible(true);
/* 165 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.Injector
 * JD-Core Version:    0.6.2
 */