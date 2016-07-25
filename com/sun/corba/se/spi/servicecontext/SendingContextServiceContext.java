/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.impl.ior.IORImpl;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import org.omg.CORBA.SystemException;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class SendingContextServiceContext extends ServiceContext
/*    */ {
/*    */   public static final int SERVICE_CONTEXT_ID = 6;
/* 63 */   private IOR ior = null;
/*    */ 
/*    */   public SendingContextServiceContext(IOR paramIOR)
/*    */   {
/* 40 */     this.ior = paramIOR;
/*    */   }
/*    */ 
/*    */   public SendingContextServiceContext(InputStream paramInputStream, GIOPVersion paramGIOPVersion)
/*    */   {
/* 45 */     super(paramInputStream, paramGIOPVersion);
/* 46 */     this.ior = new IORImpl(this.in);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 51 */     return 6;
/*    */   }
/*    */ 
/*    */   public void writeData(OutputStream paramOutputStream) throws SystemException {
/* 55 */     this.ior.write(paramOutputStream);
/*    */   }
/*    */ 
/*    */   public IOR getIOR()
/*    */   {
/* 60 */     return this.ior;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 67 */     return "SendingContexServiceContext[ ior=" + this.ior + " ]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.SendingContextServiceContext
 * JD-Core Version:    0.6.2
 */