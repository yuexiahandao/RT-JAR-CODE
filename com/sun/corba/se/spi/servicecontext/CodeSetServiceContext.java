/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;
/*    */ import com.sun.corba.se.impl.encoding.MarshalInputStream;
/*    */ import com.sun.corba.se.impl.encoding.MarshalOutputStream;
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import org.omg.CORBA.SystemException;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class CodeSetServiceContext extends ServiceContext
/*    */ {
/*    */   public static final int SERVICE_CONTEXT_ID = 1;
/*    */   private CodeSetComponentInfo.CodeSetContext csc;
/*    */ 
/*    */   public CodeSetServiceContext(CodeSetComponentInfo.CodeSetContext paramCodeSetContext)
/*    */   {
/* 39 */     this.csc = paramCodeSetContext;
/*    */   }
/*    */ 
/*    */   public CodeSetServiceContext(InputStream paramInputStream, GIOPVersion paramGIOPVersion)
/*    */   {
/* 44 */     super(paramInputStream, paramGIOPVersion);
/* 45 */     this.csc = new CodeSetComponentInfo.CodeSetContext();
/* 46 */     this.csc.read((MarshalInputStream)this.in);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 51 */     return 1;
/*    */   }
/*    */ 
/*    */   public void writeData(OutputStream paramOutputStream) throws SystemException {
/* 55 */     this.csc.write((MarshalOutputStream)paramOutputStream);
/*    */   }
/*    */ 
/*    */   public CodeSetComponentInfo.CodeSetContext getCodeSetContext()
/*    */   {
/* 60 */     return this.csc;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 67 */     return "CodeSetServiceContext[ csc=" + this.csc + " ]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.CodeSetServiceContext
 * JD-Core Version:    0.6.2
 */