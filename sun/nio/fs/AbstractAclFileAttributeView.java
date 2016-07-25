/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.attribute.AclFileAttributeView;
/*    */ import java.nio.file.attribute.UserPrincipal;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ abstract class AbstractAclFileAttributeView
/*    */   implements AclFileAttributeView, DynamicFileAttributeView
/*    */ {
/*    */   private static final String OWNER_NAME = "owner";
/*    */   private static final String ACL_NAME = "acl";
/*    */ 
/*    */   public final String name()
/*    */   {
/* 44 */     return "acl";
/*    */   }
/*    */ 
/*    */   public final void setAttribute(String paramString, Object paramObject)
/*    */     throws IOException
/*    */   {
/* 52 */     if (paramString.equals("owner")) {
/* 53 */       setOwner((UserPrincipal)paramObject);
/* 54 */       return;
/*    */     }
/* 56 */     if (paramString.equals("acl")) {
/* 57 */       setAcl((List)paramObject);
/* 58 */       return;
/*    */     }
/* 60 */     throw new IllegalArgumentException("'" + name() + ":" + paramString + "' not recognized");
/*    */   }
/*    */ 
/*    */   public final Map<String, Object> readAttributes(String[] paramArrayOfString)
/*    */     throws IOException
/*    */   {
/* 68 */     int i = 0;
/* 69 */     int j = 0;
/* 70 */     for (String str : paramArrayOfString) {
/* 71 */       if (str.equals("*")) {
/* 72 */         j = 1;
/* 73 */         i = 1;
/*    */       }
/* 76 */       else if (str.equals("acl")) {
/* 77 */         i = 1;
/*    */       }
/* 80 */       else if (str.equals("owner")) {
/* 81 */         j = 1;
/*    */       }
/*    */       else {
/* 84 */         throw new IllegalArgumentException("'" + name() + ":" + str + "' not recognized");
/*    */       }
/*    */     }
/* 87 */     ??? = new HashMap(2);
/* 88 */     if (i != 0)
/* 89 */       ((Map)???).put("acl", getAcl());
/* 90 */     if (j != 0)
/* 91 */       ((Map)???).put("owner", getOwner());
/* 92 */     return Collections.unmodifiableMap((Map)???);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractAclFileAttributeView
 * JD-Core Version:    0.6.2
 */