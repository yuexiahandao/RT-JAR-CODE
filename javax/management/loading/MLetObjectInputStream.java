/*     */ package javax.management.loading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ class MLetObjectInputStream extends ObjectInputStream
/*     */ {
/*     */   private MLet loader;
/*     */ 
/*     */   public MLetObjectInputStream(InputStream paramInputStream, MLet paramMLet)
/*     */     throws IOException, StreamCorruptedException
/*     */   {
/*  51 */     super(paramInputStream);
/*  52 */     if (paramMLet == null) {
/*  53 */       throw new IllegalArgumentException("Illegal null argument to MLetObjectInputStream");
/*     */     }
/*  55 */     this.loader = paramMLet;
/*     */   }
/*     */ 
/*     */   private Class<?> primitiveType(char paramChar) {
/*  59 */     switch (paramChar) {
/*     */     case 'B':
/*  61 */       return Byte.TYPE;
/*     */     case 'C':
/*  64 */       return Character.TYPE;
/*     */     case 'D':
/*  67 */       return Double.TYPE;
/*     */     case 'F':
/*  70 */       return Float.TYPE;
/*     */     case 'I':
/*  73 */       return Integer.TYPE;
/*     */     case 'J':
/*  76 */       return Long.TYPE;
/*     */     case 'S':
/*  79 */       return Short.TYPE;
/*     */     case 'Z':
/*  82 */       return Boolean.TYPE;
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'L':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/*  84 */     case 'Y': } return null;
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  94 */     String str = paramObjectStreamClass.getName();
/*  95 */     if (str.startsWith("["))
/*     */     {
/*  97 */       for (int i = 1; str.charAt(i) == '['; i++);
/*     */       Class localClass;
/*  99 */       if (str.charAt(i) == 'L') {
/* 100 */         localClass = this.loader.loadClass(str.substring(i + 1, str.length() - 1));
/*     */       } else {
/* 102 */         if (str.length() != i + 1)
/* 103 */           throw new ClassNotFoundException(str);
/* 104 */         localClass = primitiveType(str.charAt(i));
/*     */       }
/* 106 */       int[] arrayOfInt = new int[i];
/* 107 */       for (int j = 0; j < i; j++) {
/* 108 */         arrayOfInt[j] = 0;
/*     */       }
/* 110 */       return Array.newInstance(localClass, arrayOfInt).getClass();
/*     */     }
/* 112 */     return this.loader.loadClass(str);
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 120 */     return this.loader;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.MLetObjectInputStream
 * JD-Core Version:    0.6.2
 */