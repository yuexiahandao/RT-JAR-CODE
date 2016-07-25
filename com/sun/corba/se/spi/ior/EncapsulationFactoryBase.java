/*    */ package com.sun.corba.se.spi.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.ior.EncapsulationUtility;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ 
/*    */ public abstract class EncapsulationFactoryBase
/*    */   implements IdentifiableFactory
/*    */ {
/*    */   private int id;
/*    */ 
/*    */   public int getId()
/*    */   {
/* 37 */     return this.id;
/*    */   }
/*    */ 
/*    */   public EncapsulationFactoryBase(int paramInt)
/*    */   {
/* 42 */     this.id = paramInt;
/*    */   }
/*    */ 
/*    */   public final Identifiable create(InputStream paramInputStream)
/*    */   {
/* 47 */     InputStream localInputStream = EncapsulationUtility.getEncapsulationStream(paramInputStream);
/* 48 */     return readContents(localInputStream);
/*    */   }
/*    */ 
/*    */   protected abstract Identifiable readContents(InputStream paramInputStream);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.EncapsulationFactoryBase
 * JD-Core Version:    0.6.2
 */