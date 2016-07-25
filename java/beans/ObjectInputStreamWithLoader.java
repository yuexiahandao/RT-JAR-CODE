/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.finder.ClassFinder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ 
/*     */ class ObjectInputStreamWithLoader extends ObjectInputStream
/*     */ {
/*     */   private ClassLoader loader;
/*     */ 
/*     */   public ObjectInputStreamWithLoader(InputStream paramInputStream, ClassLoader paramClassLoader)
/*     */     throws IOException, StreamCorruptedException
/*     */   {
/* 470 */     super(paramInputStream);
/* 471 */     if (paramClassLoader == null) {
/* 472 */       throw new IllegalArgumentException("Illegal null argument to ObjectInputStreamWithLoader");
/*     */     }
/* 474 */     this.loader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   protected Class resolveClass(ObjectStreamClass paramObjectStreamClass)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 483 */     String str = paramObjectStreamClass.getName();
/* 484 */     return ClassFinder.resolveClass(str, this.loader);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.ObjectInputStreamWithLoader
 * JD-Core Version:    0.6.2
 */