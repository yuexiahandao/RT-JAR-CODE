/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*    */ import com.sun.corba.se.spi.ior.ObjectId;
/*    */ import com.sun.corba.se.spi.ior.ObjectKey;
/*    */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*    */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ import sun.corba.OutputStreamFactory;
/*    */ 
/*    */ public class ObjectKeyImpl
/*    */   implements ObjectKey
/*    */ {
/*    */   private ObjectKeyTemplate oktemp;
/*    */   private ObjectId id;
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 50 */     if (paramObject == null) {
/* 51 */       return false;
/*    */     }
/* 53 */     if (!(paramObject instanceof ObjectKeyImpl)) {
/* 54 */       return false;
/*    */     }
/* 56 */     ObjectKeyImpl localObjectKeyImpl = (ObjectKeyImpl)paramObject;
/*    */ 
/* 58 */     return (this.oktemp.equals(localObjectKeyImpl.oktemp)) && (this.id.equals(localObjectKeyImpl.id));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 64 */     return this.oktemp.hashCode() ^ this.id.hashCode();
/*    */   }
/*    */ 
/*    */   public ObjectKeyTemplate getTemplate()
/*    */   {
/* 69 */     return this.oktemp;
/*    */   }
/*    */ 
/*    */   public ObjectId getId()
/*    */   {
/* 74 */     return this.id;
/*    */   }
/*    */ 
/*    */   public ObjectKeyImpl(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId)
/*    */   {
/* 79 */     this.oktemp = paramObjectKeyTemplate;
/* 80 */     this.id = paramObjectId;
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 85 */     this.oktemp.write(this.id, paramOutputStream);
/*    */   }
/*    */ 
/*    */   public byte[] getBytes(org.omg.CORBA.ORB paramORB)
/*    */   {
/* 90 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB)paramORB);
/*    */ 
/* 92 */     write(localEncapsOutputStream);
/* 93 */     return localEncapsOutputStream.toByteArray();
/*    */   }
/*    */ 
/*    */   public CorbaServerRequestDispatcher getServerRequestDispatcher(com.sun.corba.se.spi.orb.ORB paramORB)
/*    */   {
/* 98 */     return this.oktemp.getServerRequestDispatcher(paramORB, this.id);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectKeyImpl
 * JD-Core Version:    0.6.2
 */