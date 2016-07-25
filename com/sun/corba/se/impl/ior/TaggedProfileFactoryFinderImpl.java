/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.Identifiable;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ 
/*    */ public class TaggedProfileFactoryFinderImpl extends IdentifiableFactoryFinderBase
/*    */ {
/*    */   public TaggedProfileFactoryFinderImpl(ORB paramORB)
/*    */   {
/* 44 */     super(paramORB);
/*    */   }
/*    */ 
/*    */   public Identifiable handleMissingFactory(int paramInt, InputStream paramInputStream)
/*    */   {
/* 49 */     return new GenericTaggedProfile(paramInt, paramInputStream);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.TaggedProfileFactoryFinderImpl
 * JD-Core Version:    0.6.2
 */