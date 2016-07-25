/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.ior.IORFactories;
/*    */ import com.sun.corba.se.spi.ior.IORFactory;
/*    */ import com.sun.corba.se.spi.ior.IORTemplateList;
/*    */ import com.sun.corba.se.spi.ior.ObjectId;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ 
/*    */ public abstract class ObjectReferenceProducerBase
/*    */ {
/*    */   protected transient ORB orb;
/*    */ 
/*    */   public abstract IORFactory getIORFactory();
/*    */ 
/*    */   public abstract IORTemplateList getIORTemplateList();
/*    */ 
/*    */   public ObjectReferenceProducerBase(ORB paramORB)
/*    */   {
/* 51 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object make_object(String paramString, byte[] paramArrayOfByte)
/*    */   {
/* 57 */     ObjectId localObjectId = IORFactories.makeObjectId(paramArrayOfByte);
/* 58 */     IOR localIOR = getIORFactory().makeIOR(this.orb, paramString, localObjectId);
/*    */ 
/* 60 */     return ORBUtility.makeObjectReference(localIOR);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectReferenceProducerBase
 * JD-Core Version:    0.6.2
 */