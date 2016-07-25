/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import sun.net.www.ParseUtil;
/*    */ 
/*    */ public class FileURLMapper
/*    */ {
/*    */   URL url;
/*    */   String file;
/*    */ 
/*    */   public FileURLMapper(URL paramURL)
/*    */   {
/* 45 */     this.url = paramURL;
/*    */   }
/*    */ 
/*    */   public String getPath()
/*    */   {
/* 54 */     if (this.file != null) {
/* 55 */       return this.file;
/*    */     }
/* 57 */     String str1 = this.url.getHost();
/* 58 */     if ((str1 != null) && (!str1.equals("")) && (!"localhost".equalsIgnoreCase(str1)))
/*    */     {
/* 60 */       str2 = this.url.getFile();
/* 61 */       String str3 = str1 + ParseUtil.decode(this.url.getFile());
/* 62 */       this.file = ("\\\\" + str3.replace('/', '\\'));
/* 63 */       return this.file;
/*    */     }
/* 65 */     String str2 = this.url.getFile().replace('/', '\\');
/* 66 */     this.file = ParseUtil.decode(str2);
/* 67 */     return this.file;
/*    */   }
/*    */ 
/*    */   public boolean exists() {
/* 71 */     String str = getPath();
/* 72 */     File localFile = new File(str);
/* 73 */     return localFile.exists();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FileURLMapper
 * JD-Core Version:    0.6.2
 */