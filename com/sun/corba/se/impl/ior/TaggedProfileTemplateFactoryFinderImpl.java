/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.IORSystemException;
/*    */ import com.sun.corba.se.spi.ior.Identifiable;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ 
/*    */ public class TaggedProfileTemplateFactoryFinderImpl extends IdentifiableFactoryFinderBase
/*    */ {
/*    */   public TaggedProfileTemplateFactoryFinderImpl(ORB paramORB)
/*    */   {
/* 46 */     super(paramORB);
/*    */   }
/*    */ 
/*    */   public Identifiable handleMissingFactory(int paramInt, InputStream paramInputStream)
/*    */   {
/* 51 */     throw this.wrapper.taggedProfileTemplateFactoryNotFound(new Integer(paramInt));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.TaggedProfileTemplateFactoryFinderImpl
 * JD-Core Version:    0.6.2
 */