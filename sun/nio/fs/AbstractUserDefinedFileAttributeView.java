/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.attribute.UserDefinedFileAttributeView;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ abstract class AbstractUserDefinedFileAttributeView
/*     */   implements UserDefinedFileAttributeView, DynamicFileAttributeView
/*     */ {
/*     */   protected void checkAccess(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  46 */     assert ((paramBoolean1) || (paramBoolean2));
/*  47 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  48 */     if (localSecurityManager != null) {
/*  49 */       if (paramBoolean1)
/*  50 */         localSecurityManager.checkRead(paramString);
/*  51 */       if (paramBoolean2)
/*  52 */         localSecurityManager.checkWrite(paramString);
/*  53 */       localSecurityManager.checkPermission(new RuntimePermission("accessUserDefinedAttributes"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String name()
/*     */   {
/*  59 */     return "user";
/*     */   }
/*     */ 
/*     */   public final void setAttribute(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/*     */     ByteBuffer localByteBuffer;
/*  67 */     if ((paramObject instanceof byte[]))
/*  68 */       localByteBuffer = ByteBuffer.wrap((byte[])paramObject);
/*     */     else {
/*  70 */       localByteBuffer = (ByteBuffer)paramObject;
/*     */     }
/*  72 */     write(paramString, localByteBuffer);
/*     */   }
/*     */ 
/*     */   public final Map<String, Object> readAttributes(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/*  80 */     Object localObject1 = new ArrayList();
/*  81 */     for (Object localObject3 : paramArrayOfString) {
/*  82 */       if (localObject3.equals("*")) {
/*  83 */         localObject1 = list();
/*  84 */         break;
/*     */       }
/*  86 */       if (localObject3.length() == 0)
/*  87 */         throw new IllegalArgumentException();
/*  88 */       ((List)localObject1).add(localObject3);
/*     */     }
/*     */ 
/*  93 */     ??? = new HashMap();
/*  94 */     for (String str : (List)localObject1) {
/*  95 */       int k = size(str);
/*  96 */       byte[] arrayOfByte1 = new byte[k];
/*  97 */       int m = read(str, ByteBuffer.wrap(arrayOfByte1));
/*  98 */       byte[] arrayOfByte2 = m == k ? arrayOfByte1 : Arrays.copyOf(arrayOfByte1, m);
/*  99 */       ((Map)???).put(str, arrayOfByte2);
/*     */     }
/* 101 */     return ???;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractUserDefinedFileAttributeView
 * JD-Core Version:    0.6.2
 */