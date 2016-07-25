/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*    */ import com.sun.corba.se.spi.ior.Identifiable;
/*    */ import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.IOP.TaggedComponentHelper;
/*    */ import sun.corba.OutputStreamFactory;
/*    */ 
/*    */ public class TaggedComponentFactoryFinderImpl extends IdentifiableFactoryFinderBase
/*    */   implements TaggedComponentFactoryFinder
/*    */ {
/*    */   public TaggedComponentFactoryFinderImpl(com.sun.corba.se.spi.orb.ORB paramORB)
/*    */   {
/* 54 */     super(paramORB);
/*    */   }
/*    */ 
/*    */   public Identifiable handleMissingFactory(int paramInt, InputStream paramInputStream) {
/* 58 */     return new GenericTaggedComponent(paramInt, paramInputStream);
/*    */   }
/*    */ 
/*    */   public com.sun.corba.se.spi.ior.TaggedComponent create(org.omg.CORBA.ORB paramORB, org.omg.IOP.TaggedComponent paramTaggedComponent)
/*    */   {
/* 64 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB)paramORB);
/*    */ 
/* 66 */     TaggedComponentHelper.write(localEncapsOutputStream, paramTaggedComponent);
/* 67 */     InputStream localInputStream = (InputStream)localEncapsOutputStream.create_input_stream();
/*    */ 
/* 69 */     localInputStream.read_ulong();
/*    */ 
/* 71 */     return (com.sun.corba.se.spi.ior.TaggedComponent)create(paramTaggedComponent.tag, localInputStream);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.TaggedComponentFactoryFinderImpl
 * JD-Core Version:    0.6.2
 */