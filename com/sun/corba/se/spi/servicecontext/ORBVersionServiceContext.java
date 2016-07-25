/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import com.sun.corba.se.spi.orb.ORBVersion;
/*    */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*    */ import org.omg.CORBA.SystemException;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class ORBVersionServiceContext extends ServiceContext
/*    */ {
/*    */   public static final int SERVICE_CONTEXT_ID = 1313165056;
/* 77 */   private ORBVersion version = ORBVersionFactory.getORBVersion();
/*    */ 
/*    */   public ORBVersionServiceContext()
/*    */   {
/* 43 */     this.version = ORBVersionFactory.getORBVersion();
/*    */   }
/*    */ 
/*    */   public ORBVersionServiceContext(ORBVersion paramORBVersion)
/*    */   {
/* 48 */     this.version = paramORBVersion;
/*    */   }
/*    */ 
/*    */   public ORBVersionServiceContext(InputStream paramInputStream, GIOPVersion paramGIOPVersion)
/*    */   {
/* 53 */     super(paramInputStream, paramGIOPVersion);
/*    */ 
/* 59 */     this.version = ORBVersionFactory.create(this.in);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 64 */     return 1313165056;
/*    */   }
/*    */ 
/*    */   public void writeData(OutputStream paramOutputStream) throws SystemException {
/* 68 */     this.version.write(paramOutputStream);
/*    */   }
/*    */ 
/*    */   public ORBVersion getVersion()
/*    */   {
/* 73 */     return this.version;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 81 */     return "ORBVersionServiceContext[ version=" + this.version + " ]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext
 * JD-Core Version:    0.6.2
 */