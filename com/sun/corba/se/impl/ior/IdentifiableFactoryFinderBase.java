/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.IORSystemException;
/*    */ import com.sun.corba.se.spi.ior.Identifiable;
/*    */ import com.sun.corba.se.spi.ior.IdentifiableFactory;
/*    */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ 
/*    */ public abstract class IdentifiableFactoryFinderBase
/*    */   implements IdentifiableFactoryFinder
/*    */ {
/*    */   private ORB orb;
/*    */   private Map map;
/*    */   protected IORSystemException wrapper;
/*    */ 
/*    */   protected IdentifiableFactoryFinderBase(ORB paramORB)
/*    */   {
/* 52 */     this.map = new HashMap();
/* 53 */     this.orb = paramORB;
/* 54 */     this.wrapper = IORSystemException.get(paramORB, "oa.ior");
/*    */   }
/*    */ 
/*    */   protected IdentifiableFactory getFactory(int paramInt)
/*    */   {
/* 60 */     Integer localInteger = new Integer(paramInt);
/* 61 */     IdentifiableFactory localIdentifiableFactory = (IdentifiableFactory)this.map.get(localInteger);
/*    */ 
/* 63 */     return localIdentifiableFactory;
/*    */   }
/*    */ 
/*    */   public abstract Identifiable handleMissingFactory(int paramInt, InputStream paramInputStream);
/*    */ 
/*    */   public Identifiable create(int paramInt, InputStream paramInputStream)
/*    */   {
/* 70 */     IdentifiableFactory localIdentifiableFactory = getFactory(paramInt);
/*    */ 
/* 72 */     if (localIdentifiableFactory != null) {
/* 73 */       return localIdentifiableFactory.create(paramInputStream);
/*    */     }
/* 75 */     return handleMissingFactory(paramInt, paramInputStream);
/*    */   }
/*    */ 
/*    */   public void registerFactory(IdentifiableFactory paramIdentifiableFactory)
/*    */   {
/* 80 */     Integer localInteger = new Integer(paramIdentifiableFactory.getId());
/* 81 */     this.map.put(localInteger, paramIdentifiableFactory);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.IdentifiableFactoryFinderBase
 * JD-Core Version:    0.6.2
 */