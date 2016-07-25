/*     */ package java.util.jar;
/*     */ 
/*     */ import com.sun.java.util.jar.pack.PackerImpl;
/*     */ import com.sun.java.util.jar.pack.UnpackerImpl;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.SortedMap;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class Pack200
/*     */ {
/*     */   private static final String PACK_PROVIDER = "java.util.jar.Pack200.Packer";
/*     */   private static final String UNPACK_PROVIDER = "java.util.jar.Pack200.Unpacker";
/*     */   private static Class packerImpl;
/*     */   private static Class unpackerImpl;
/*     */ 
/*     */   public static synchronized Packer newPacker()
/*     */   {
/* 134 */     return (Packer)newInstance("java.util.jar.Pack200.Packer");
/*     */   }
/*     */ 
/*     */   public static Unpacker newUnpacker()
/*     */   {
/* 160 */     return (Unpacker)newInstance("java.util.jar.Pack200.Unpacker");
/*     */   }
/*     */ 
/*     */   private static synchronized Object newInstance(String paramString)
/*     */   {
/* 733 */     String str = "(unknown)";
/*     */     try {
/* 735 */       Object localObject = "java.util.jar.Pack200.Packer".equals(paramString) ? packerImpl : unpackerImpl;
/* 736 */       if (localObject == null)
/*     */       {
/* 738 */         str = (String)AccessController.doPrivileged(new GetPropertyAction(paramString, ""));
/*     */ 
/* 740 */         if ((str != null) && (!str.equals("")))
/* 741 */           localObject = Class.forName(str);
/* 742 */         else if ("java.util.jar.Pack200.Packer".equals(paramString))
/* 743 */           localObject = PackerImpl.class;
/*     */         else {
/* 745 */           localObject = UnpackerImpl.class;
/*     */         }
/*     */       }
/* 748 */       return ((Class)localObject).newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 750 */       throw new Error("Class not found: " + str + ":\ncheck property " + paramString + " in your properties file.", localClassNotFoundException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 754 */       throw new Error("Could not instantiate: " + str + ":\ncheck property " + paramString + " in your properties file.", localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 758 */       throw new Error("Cannot access class: " + str + ":\ncheck property " + paramString + " in your properties file.", localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Packer
/*     */   {
/*     */     public static final String SEGMENT_LIMIT = "pack.segment.limit";
/*     */     public static final String KEEP_FILE_ORDER = "pack.keep.file.order";
/*     */     public static final String EFFORT = "pack.effort";
/*     */     public static final String DEFLATE_HINT = "pack.deflate.hint";
/*     */     public static final String MODIFICATION_TIME = "pack.modification.time";
/*     */     public static final String PASS_FILE_PFX = "pack.pass.file.";
/*     */     public static final String UNKNOWN_ATTRIBUTE = "pack.unknown.attribute";
/*     */     public static final String CLASS_ATTRIBUTE_PFX = "pack.class.attribute.";
/*     */     public static final String FIELD_ATTRIBUTE_PFX = "pack.field.attribute.";
/*     */     public static final String METHOD_ATTRIBUTE_PFX = "pack.method.attribute.";
/*     */     public static final String CODE_ATTRIBUTE_PFX = "pack.code.attribute.";
/*     */     public static final String PROGRESS = "pack.progress";
/*     */     public static final String KEEP = "keep";
/*     */     public static final String PASS = "pass";
/*     */     public static final String STRIP = "strip";
/*     */     public static final String ERROR = "error";
/*     */     public static final String TRUE = "true";
/*     */     public static final String FALSE = "false";
/*     */     public static final String LATEST = "latest";
/*     */ 
/*     */     public abstract SortedMap<String, String> properties();
/*     */ 
/*     */     public abstract void pack(JarFile paramJarFile, OutputStream paramOutputStream)
/*     */       throws IOException;
/*     */ 
/*     */     public abstract void pack(JarInputStream paramJarInputStream, OutputStream paramOutputStream)
/*     */       throws IOException;
/*     */ 
/*     */     public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
/*     */ 
/*     */     public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public static abstract interface Unpacker
/*     */   {
/*     */     public static final String KEEP = "keep";
/*     */     public static final String TRUE = "true";
/*     */     public static final String FALSE = "false";
/*     */     public static final String DEFLATE_HINT = "unpack.deflate.hint";
/*     */     public static final String PROGRESS = "unpack.progress";
/*     */ 
/*     */     public abstract SortedMap<String, String> properties();
/*     */ 
/*     */     public abstract void unpack(InputStream paramInputStream, JarOutputStream paramJarOutputStream)
/*     */       throws IOException;
/*     */ 
/*     */     public abstract void unpack(File paramFile, JarOutputStream paramJarOutputStream)
/*     */       throws IOException;
/*     */ 
/*     */     public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
/*     */ 
/*     */     public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.Pack200
 * JD-Core Version:    0.6.2
 */