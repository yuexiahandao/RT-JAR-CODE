/*    */ package org.omg.CORBA_2_3.portable;
/*    */ 
/*    */ public abstract class ObjectImpl extends org.omg.CORBA.portable.ObjectImpl
/*    */ {
/*    */   public String _get_codebase()
/*    */   {
/* 54 */     org.omg.CORBA.portable.Delegate localDelegate = _get_delegate();
/* 55 */     if ((localDelegate instanceof Delegate))
/* 56 */       return ((Delegate)localDelegate).get_codebase(this);
/* 57 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA_2_3.portable.ObjectImpl
 * JD-Core Version:    0.6.2
 */