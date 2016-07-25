/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.ObjectId;
/*    */ import java.util.Arrays;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public final class ObjectIdImpl
/*    */   implements ObjectId
/*    */ {
/*    */   private byte[] id;
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 41 */     if (!(paramObject instanceof ObjectIdImpl)) {
/* 42 */       return false;
/*    */     }
/* 44 */     ObjectIdImpl localObjectIdImpl = (ObjectIdImpl)paramObject;
/*    */ 
/* 46 */     return Arrays.equals(this.id, localObjectIdImpl.id);
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 51 */     int i = 17;
/* 52 */     for (int j = 0; j < this.id.length; j++)
/* 53 */       i = 37 * i + this.id[j];
/* 54 */     return i;
/*    */   }
/*    */ 
/*    */   public ObjectIdImpl(byte[] paramArrayOfByte)
/*    */   {
/* 59 */     this.id = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public byte[] getId()
/*    */   {
/* 64 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 69 */     paramOutputStream.write_long(this.id.length);
/* 70 */     paramOutputStream.write_octet_array(this.id, 0, this.id.length);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectIdImpl
 * JD-Core Version:    0.6.2
 */