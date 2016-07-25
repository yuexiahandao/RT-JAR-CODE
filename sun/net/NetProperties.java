/*     */ package sun.net;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class NetProperties
/*     */ {
/*  42 */   private static Properties props = new Properties();
/*     */ 
/*     */   private static void loadDefaultProperties()
/*     */   {
/*  60 */     String str = System.getProperty("java.home");
/*  61 */     if (str == null)
/*  62 */       throw new Error("Can't find java.home ??");
/*     */     try
/*     */     {
/*  65 */       File localFile = new File(str, "lib");
/*  66 */       localFile = new File(localFile, "net.properties");
/*  67 */       str = localFile.getCanonicalPath();
/*  68 */       FileInputStream localFileInputStream = new FileInputStream(str);
/*  69 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
/*  70 */       props.load(localBufferedInputStream);
/*  71 */       localBufferedInputStream.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String get(String paramString)
/*     */   {
/*  90 */     String str = props.getProperty(paramString);
/*     */     try {
/*  92 */       return System.getProperty(paramString, str);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     } catch (NullPointerException localNullPointerException) {
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public static Integer getInteger(String paramString, int paramInt)
/*     */   {
/* 112 */     String str = null;
/*     */     try
/*     */     {
/* 115 */       str = System.getProperty(paramString, props.getProperty(paramString));
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/*     */     catch (NullPointerException localNullPointerException) {
/*     */     }
/* 120 */     if (str != null)
/*     */       try {
/* 122 */         return Integer.decode(str);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 126 */     return new Integer(paramInt);
/*     */   }
/*     */ 
/*     */   public static Boolean getBoolean(String paramString)
/*     */   {
/* 141 */     String str = null;
/*     */     try
/*     */     {
/* 144 */       str = System.getProperty(paramString, props.getProperty(paramString));
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/*     */     catch (NullPointerException localNullPointerException) {
/*     */     }
/* 149 */     if (str != null)
/*     */       try {
/* 151 */         return Boolean.valueOf(str);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  44 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/*  47 */         NetProperties.access$000();
/*  48 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.NetProperties
 * JD-Core Version:    0.6.2
 */