/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.Attributes.Name;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ public class VMSupport
/*    */ {
/* 40 */   private static Properties agentProps = null;
/*    */ 
/*    */   public static synchronized Properties getAgentProperties()
/*    */   {
/* 45 */     if (agentProps == null) {
/* 46 */       agentProps = new Properties();
/* 47 */       initAgentProperties(agentProps);
/*    */     }
/* 49 */     return agentProps;
/*    */   }
/*    */ 
/*    */   private static native Properties initAgentProperties(Properties paramProperties);
/*    */ 
/*    */   private static byte[] serializePropertiesToByteArray(Properties paramProperties)
/*    */     throws IOException
/*    */   {
/* 59 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4096);
/*    */ 
/* 61 */     Properties localProperties = new Properties();
/*    */ 
/* 64 */     Set localSet = paramProperties.stringPropertyNames();
/* 65 */     for (String str1 : localSet) {
/* 66 */       String str2 = paramProperties.getProperty(str1);
/* 67 */       localProperties.put(str1, str2);
/*    */     }
/*    */ 
/* 70 */     localProperties.store(localByteArrayOutputStream, null);
/* 71 */     return localByteArrayOutputStream.toByteArray();
/*    */   }
/*    */ 
/*    */   public static byte[] serializePropertiesToByteArray() throws IOException {
/* 75 */     return serializePropertiesToByteArray(System.getProperties());
/*    */   }
/*    */ 
/*    */   public static byte[] serializeAgentPropertiesToByteArray() throws IOException {
/* 79 */     return serializePropertiesToByteArray(getAgentProperties());
/*    */   }
/*    */ 
/*    */   public static boolean isClassPathAttributePresent(String paramString)
/*    */   {
/*    */     try
/*    */     {
/* 89 */       Manifest localManifest = new JarFile(paramString).getManifest();
/* 90 */       if ((localManifest != null) && 
/* 91 */         (localManifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH) != null)) {
/* 92 */         return true;
/*    */       }
/*    */ 
/* 95 */       return false;
/*    */     } catch (IOException localIOException) {
/* 97 */       throw new RuntimeException(localIOException.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public static native String getVMTemporaryDirectory();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.VMSupport
 * JD-Core Version:    0.6.2
 */