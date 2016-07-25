/*    */ package com.sun.corba.se.impl.copyobject;
/*    */ 
/*    */ import com.sun.corba.se.impl.util.Utility;
/*    */ import com.sun.corba.se.spi.copyobject.ObjectCopier;
/*    */ import java.io.Serializable;
/*    */ import java.rmi.Remote;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class ORBStreamObjectCopierImpl
/*    */   implements ObjectCopier
/*    */ {
/*    */   private ORB orb;
/*    */ 
/*    */   public ORBStreamObjectCopierImpl(ORB paramORB)
/*    */   {
/* 46 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   public Object copy(Object paramObject) {
/* 50 */     if ((paramObject instanceof Remote))
/*    */     {
/* 53 */       return Utility.autoConnect(paramObject, this.orb, true);
/*    */     }
/*    */ 
/* 56 */     OutputStream localOutputStream = (OutputStream)this.orb.create_output_stream();
/* 57 */     localOutputStream.write_value((Serializable)paramObject);
/* 58 */     InputStream localInputStream = (InputStream)localOutputStream.create_input_stream();
/* 59 */     return localInputStream.read_value();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.copyobject.ORBStreamObjectCopierImpl
 * JD-Core Version:    0.6.2
 */