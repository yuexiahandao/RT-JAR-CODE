/*    */ package sun.java2d.cmm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class ProfileDeferralInfo extends InputStream
/*    */ {
/*    */   public int colorSpaceType;
/*    */   public int numComponents;
/*    */   public int profileClass;
/*    */   public String filename;
/*    */ 
/*    */   public ProfileDeferralInfo(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 46 */     this.filename = paramString;
/* 47 */     this.colorSpaceType = paramInt1;
/* 48 */     this.numComponents = paramInt2;
/* 49 */     this.profileClass = paramInt3;
/*    */   }
/*    */ 
/*    */   public int read()
/*    */     throws IOException
/*    */   {
/* 58 */     return 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.ProfileDeferralInfo
 * JD-Core Version:    0.6.2
 */