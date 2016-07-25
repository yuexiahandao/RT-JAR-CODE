/*     */ package java.lang.reflect;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
/*     */ 
/*     */ public class Modifier
/*     */ {
/*     */   public static final int PUBLIC = 1;
/*     */   public static final int PRIVATE = 2;
/*     */   public static final int PROTECTED = 4;
/*     */   public static final int STATIC = 8;
/*     */   public static final int FINAL = 16;
/*     */   public static final int SYNCHRONIZED = 32;
/*     */   public static final int VOLATILE = 64;
/*     */   public static final int TRANSIENT = 128;
/*     */   public static final int NATIVE = 256;
/*     */   public static final int INTERFACE = 512;
/*     */   public static final int ABSTRACT = 1024;
/*     */   public static final int STRICT = 2048;
/*     */   static final int BRIDGE = 64;
/*     */   static final int VARARGS = 128;
/*     */   static final int SYNTHETIC = 4096;
/*     */   static final int ANNOTATION = 8192;
/*     */   static final int ENUM = 16384;
/*     */   private static final int CLASS_MODIFIERS = 3103;
/*     */   private static final int INTERFACE_MODIFIERS = 3087;
/*     */   private static final int CONSTRUCTOR_MODIFIERS = 7;
/*     */   private static final int METHOD_MODIFIERS = 3391;
/*     */   private static final int FIELD_MODIFIERS = 223;
/*     */ 
/*     */   public static boolean isPublic(int paramInt)
/*     */   {
/*  69 */     return (paramInt & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isPrivate(int paramInt)
/*     */   {
/*  81 */     return (paramInt & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isProtected(int paramInt)
/*     */   {
/*  93 */     return (paramInt & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isStatic(int paramInt)
/*     */   {
/* 105 */     return (paramInt & 0x8) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isFinal(int paramInt)
/*     */   {
/* 117 */     return (paramInt & 0x10) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isSynchronized(int paramInt)
/*     */   {
/* 129 */     return (paramInt & 0x20) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isVolatile(int paramInt)
/*     */   {
/* 141 */     return (paramInt & 0x40) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isTransient(int paramInt)
/*     */   {
/* 153 */     return (paramInt & 0x80) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isNative(int paramInt)
/*     */   {
/* 165 */     return (paramInt & 0x100) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isInterface(int paramInt)
/*     */   {
/* 177 */     return (paramInt & 0x200) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isAbstract(int paramInt)
/*     */   {
/* 189 */     return (paramInt & 0x400) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isStrict(int paramInt)
/*     */   {
/* 201 */     return (paramInt & 0x800) != 0;
/*     */   }
/*     */ 
/*     */   public static String toString(int paramInt)
/*     */   {
/* 236 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 239 */     if ((paramInt & 0x1) != 0) localStringBuffer.append("public ");
/* 240 */     if ((paramInt & 0x4) != 0) localStringBuffer.append("protected ");
/* 241 */     if ((paramInt & 0x2) != 0) localStringBuffer.append("private ");
/*     */ 
/* 244 */     if ((paramInt & 0x400) != 0) localStringBuffer.append("abstract ");
/* 245 */     if ((paramInt & 0x8) != 0) localStringBuffer.append("static ");
/* 246 */     if ((paramInt & 0x10) != 0) localStringBuffer.append("final ");
/* 247 */     if ((paramInt & 0x80) != 0) localStringBuffer.append("transient ");
/* 248 */     if ((paramInt & 0x40) != 0) localStringBuffer.append("volatile ");
/* 249 */     if ((paramInt & 0x20) != 0) localStringBuffer.append("synchronized ");
/* 250 */     if ((paramInt & 0x100) != 0) localStringBuffer.append("native ");
/* 251 */     if ((paramInt & 0x800) != 0) localStringBuffer.append("strictfp ");
/* 252 */     if ((paramInt & 0x200) != 0) localStringBuffer.append("interface ");
/*     */     int i;
/* 254 */     if ((i = localStringBuffer.length()) > 0)
/* 255 */       return localStringBuffer.toString().substring(0, i - 1);
/* 256 */     return "";
/*     */   }
/*     */ 
/*     */   static boolean isSynthetic(int paramInt)
/*     */   {
/* 346 */     return (paramInt & 0x1000) != 0;
/*     */   }
/*     */ 
/*     */   public static int classModifiers()
/*     */   {
/* 397 */     return 3103;
/*     */   }
/*     */ 
/*     */   public static int interfaceModifiers()
/*     */   {
/* 410 */     return 3087;
/*     */   }
/*     */ 
/*     */   public static int constructorModifiers()
/*     */   {
/* 423 */     return 7;
/*     */   }
/*     */ 
/*     */   public static int methodModifiers()
/*     */   {
/* 436 */     return 3391;
/*     */   }
/*     */ 
/*     */   public static int fieldModifiers()
/*     */   {
/* 450 */     return 223;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  54 */     ReflectionFactory localReflectionFactory = (ReflectionFactory)AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
/*     */ 
/*  57 */     localReflectionFactory.setLangReflectAccess(new ReflectAccess());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Modifier
 * JD-Core Version:    0.6.2
 */