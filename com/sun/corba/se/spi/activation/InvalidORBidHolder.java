/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class InvalidORBidHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public InvalidORBid value = null;
/*    */ 
/*    */   public InvalidORBidHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidORBidHolder(InvalidORBid paramInvalidORBid)
/*    */   {
/* 20 */     this.value = paramInvalidORBid;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = InvalidORBidHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     InvalidORBidHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return InvalidORBidHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InvalidORBidHolder
 * JD-Core Version:    0.6.2
 */