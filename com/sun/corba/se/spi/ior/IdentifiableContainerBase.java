/*    */ package com.sun.corba.se.spi.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.ior.FreezableList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class IdentifiableContainerBase extends FreezableList
/*    */ {
/*    */   public IdentifiableContainerBase()
/*    */   {
/* 50 */     super(new ArrayList());
/*    */   }
/*    */ 
/*    */   public Iterator iteratorById(final int paramInt)
/*    */   {
/* 58 */     return new Iterator() {
/* 59 */       Iterator iter = IdentifiableContainerBase.this.iterator();
/* 60 */       Object current = advance();
/*    */ 
/*    */       private Object advance()
/*    */       {
/* 64 */         while (this.iter.hasNext()) {
/* 65 */           Identifiable localIdentifiable = (Identifiable)this.iter.next();
/* 66 */           if (localIdentifiable.getId() == paramInt) {
/* 67 */             return localIdentifiable;
/*    */           }
/*    */         }
/* 70 */         return null;
/*    */       }
/*    */ 
/*    */       public boolean hasNext()
/*    */       {
/* 75 */         return this.current != null;
/*    */       }
/*    */ 
/*    */       public Object next()
/*    */       {
/* 80 */         Object localObject = this.current;
/* 81 */         this.current = advance();
/* 82 */         return localObject;
/*    */       }
/*    */ 
/*    */       public void remove()
/*    */       {
/* 87 */         this.iter.remove();
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.IdentifiableContainerBase
 * JD-Core Version:    0.6.2
 */