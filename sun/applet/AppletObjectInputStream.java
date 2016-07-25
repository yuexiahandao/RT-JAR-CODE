/*     */ package sun.applet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ class AppletObjectInputStream extends ObjectInputStream
/*     */ {
/*     */   private AppletClassLoader loader;
/*     */ 
/*     */   public AppletObjectInputStream(InputStream paramInputStream, AppletClassLoader paramAppletClassLoader)
/*     */     throws IOException, StreamCorruptedException
/*     */   {
/*  50 */     super(paramInputStream);
/*  51 */     if (paramAppletClassLoader == null) {
/*  52 */       throw new AppletIllegalArgumentException("appletillegalargumentexception.objectinputstream");
/*     */     }
/*     */ 
/*  55 */     this.loader = paramAppletClassLoader;
/*     */   }
/*     */ 
/*     */   private Class primitiveType(char paramChar)
/*     */   {
/*  63 */     switch (paramChar) { case 'B':
/*  64 */       return Byte.TYPE;
/*     */     case 'C':
/*  65 */       return Character.TYPE;
/*     */     case 'D':
/*  66 */       return Double.TYPE;
/*     */     case 'F':
/*  67 */       return Float.TYPE;
/*     */     case 'I':
/*  68 */       return Integer.TYPE;
/*     */     case 'J':
/*  69 */       return Long.TYPE;
/*     */     case 'S':
/*  70 */       return Short.TYPE;
/*     */     case 'Z':
/*  71 */       return Boolean.TYPE;
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
/*  72 */     case 'Y': } return null;
/*     */   }
/*     */ 
/*     */   protected Class resolveClass(ObjectStreamClass paramObjectStreamClass)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  82 */     String str = paramObjectStreamClass.getName();
/*  83 */     if (str.startsWith("["))
/*     */     {
/*  87 */       for (int i = 1; str.charAt(i) == '['; i++);
/*     */       Class localClass;
/*  88 */       if (str.charAt(i) == 'L') {
/*  89 */         localClass = this.loader.loadClass(str.substring(i + 1, str.length() - 1));
/*     */       }
/*     */       else {
/*  92 */         if (str.length() != i + 1) {
/*  93 */           throw new ClassNotFoundException(str);
/*     */         }
/*  95 */         localClass = primitiveType(str.charAt(i));
/*     */       }
/*  97 */       int[] arrayOfInt = new int[i];
/*  98 */       for (int j = 0; j < i; j++) {
/*  99 */         arrayOfInt[j] = 0;
/*     */       }
/* 101 */       return Array.newInstance(localClass, arrayOfInt).getClass();
/*     */     }
/* 103 */     return this.loader.loadClass(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletObjectInputStream
 * JD-Core Version:    0.6.2
 */