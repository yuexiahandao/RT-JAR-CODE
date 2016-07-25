/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ 
/*    */ public class GenericTaggedComponent extends GenericIdentifiable
/*    */   implements com.sun.corba.se.spi.ior.TaggedComponent
/*    */ {
/*    */   public GenericTaggedComponent(int paramInt, InputStream paramInputStream)
/*    */   {
/* 45 */     super(paramInt, paramInputStream);
/*    */   }
/*    */ 
/*    */   public GenericTaggedComponent(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 50 */     super(paramInt, paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public org.omg.IOP.TaggedComponent getIOPComponent(ORB paramORB)
/*    */   {
/* 60 */     return new org.omg.IOP.TaggedComponent(getId(), getData());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.GenericTaggedComponent
 * JD-Core Version:    0.6.2
 */