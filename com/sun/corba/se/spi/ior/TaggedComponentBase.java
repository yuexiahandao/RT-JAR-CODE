/*    */ package com.sun.corba.se.spi.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.IOP.TaggedComponentHelper;
/*    */ import sun.corba.OutputStreamFactory;
/*    */ 
/*    */ public abstract class TaggedComponentBase extends IdentifiableBase
/*    */   implements TaggedComponent
/*    */ {
/*    */   public org.omg.IOP.TaggedComponent getIOPComponent(org.omg.CORBA.ORB paramORB)
/*    */   {
/* 45 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB)paramORB);
/*    */ 
/* 47 */     write(localEncapsOutputStream);
/* 48 */     InputStream localInputStream = (InputStream)localEncapsOutputStream.create_input_stream();
/* 49 */     return TaggedComponentHelper.read(localInputStream);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.TaggedComponentBase
 * JD-Core Version:    0.6.2
 */