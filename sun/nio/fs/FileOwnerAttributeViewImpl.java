/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.attribute.AclFileAttributeView;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ import java.nio.file.attribute.FileOwnerAttributeView;
/*    */ import java.nio.file.attribute.PosixFileAttributeView;
/*    */ import java.nio.file.attribute.PosixFileAttributes;
/*    */ import java.nio.file.attribute.UserPrincipal;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class FileOwnerAttributeViewImpl
/*    */   implements FileOwnerAttributeView, DynamicFileAttributeView
/*    */ {
/*    */   private static final String OWNER_NAME = "owner";
/*    */   private final FileAttributeView view;
/*    */   private final boolean isPosixView;
/*    */ 
/*    */   FileOwnerAttributeViewImpl(PosixFileAttributeView paramPosixFileAttributeView)
/*    */   {
/* 46 */     this.view = paramPosixFileAttributeView;
/* 47 */     this.isPosixView = true;
/*    */   }
/*    */ 
/*    */   FileOwnerAttributeViewImpl(AclFileAttributeView paramAclFileAttributeView) {
/* 51 */     this.view = paramAclFileAttributeView;
/* 52 */     this.isPosixView = false;
/*    */   }
/*    */ 
/*    */   public String name()
/*    */   {
/* 57 */     return "owner";
/*    */   }
/*    */ 
/*    */   public void setAttribute(String paramString, Object paramObject)
/*    */     throws IOException
/*    */   {
/* 64 */     if (paramString.equals("owner"))
/* 65 */       setOwner((UserPrincipal)paramObject);
/*    */     else
/* 67 */       throw new IllegalArgumentException("'" + name() + ":" + paramString + "' not recognized");
/*    */   }
/*    */ 
/*    */   public Map<String, Object> readAttributes(String[] paramArrayOfString)
/*    */     throws IOException
/*    */   {
/* 74 */     HashMap localHashMap = new HashMap();
/* 75 */     for (String str : paramArrayOfString) {
/* 76 */       if ((str.equals("*")) || (str.equals("owner")))
/* 77 */         localHashMap.put("owner", getOwner());
/*    */       else {
/* 79 */         throw new IllegalArgumentException("'" + name() + ":" + str + "' not recognized");
/*    */       }
/*    */     }
/*    */ 
/* 83 */     return localHashMap;
/*    */   }
/*    */ 
/*    */   public UserPrincipal getOwner() throws IOException
/*    */   {
/* 88 */     if (this.isPosixView) {
/* 89 */       return ((PosixFileAttributeView)this.view).readAttributes().owner();
/*    */     }
/* 91 */     return ((AclFileAttributeView)this.view).getOwner();
/*    */   }
/*    */ 
/*    */   public void setOwner(UserPrincipal paramUserPrincipal)
/*    */     throws IOException
/*    */   {
/* 99 */     if (this.isPosixView)
/* 100 */       ((PosixFileAttributeView)this.view).setOwner(paramUserPrincipal);
/*    */     else
/* 102 */       ((AclFileAttributeView)this.view).setOwner(paramUserPrincipal);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.FileOwnerAttributeViewImpl
 * JD-Core Version:    0.6.2
 */