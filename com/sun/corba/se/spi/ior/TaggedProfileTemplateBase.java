/*    */ package com.sun.corba.se.spi.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.ior.EncapsulationUtility;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.util.Iterator;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public abstract class TaggedProfileTemplateBase extends IdentifiableContainerBase
/*    */   implements TaggedProfileTemplate
/*    */ {
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 42 */     EncapsulationUtility.writeEncapsulation(this, paramOutputStream);
/*    */   }
/*    */ 
/*    */   public org.omg.IOP.TaggedComponent[] getIOPComponents(ORB paramORB, int paramInt)
/*    */   {
/* 47 */     int i = 0;
/* 48 */     Iterator localIterator = iteratorById(paramInt);
/* 49 */     while (localIterator.hasNext()) {
/* 50 */       localIterator.next();
/* 51 */       i++;
/*    */     }
/*    */ 
/* 54 */     org.omg.IOP.TaggedComponent[] arrayOfTaggedComponent = new org.omg.IOP.TaggedComponent[i];
/*    */ 
/* 57 */     int j = 0;
/* 58 */     localIterator = iteratorById(paramInt);
/* 59 */     while (localIterator.hasNext()) {
/* 60 */       TaggedComponent localTaggedComponent = (TaggedComponent)localIterator.next();
/* 61 */       arrayOfTaggedComponent[(j++)] = localTaggedComponent.getIOPComponent(paramORB);
/*    */     }
/*    */ 
/* 64 */     return arrayOfTaggedComponent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.TaggedProfileTemplateBase
 * JD-Core Version:    0.6.2
 */