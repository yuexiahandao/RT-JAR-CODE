/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import javax.rmi.CORBA.Tie;
/*    */ import org.omg.CORBA.portable.ServantObject;
/*    */ 
/*    */ public class JIDLLocalCRDImpl extends LocalClientRequestDispatcherBase
/*    */ {
/*    */   protected ServantObject servant;
/*    */ 
/*    */   public JIDLLocalCRDImpl(ORB paramORB, int paramInt, IOR paramIOR)
/*    */   {
/* 47 */     super(paramORB, paramInt, paramIOR);
/*    */   }
/*    */ 
/*    */   public ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass)
/*    */   {
/* 56 */     if (!checkForCompatibleServant(this.servant, paramClass)) {
/* 57 */       return null;
/*    */     }
/* 59 */     return this.servant;
/*    */   }
/*    */ 
/*    */   public void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void setServant(java.lang.Object paramObject)
/*    */   {
/* 71 */     if ((paramObject != null) && ((paramObject instanceof Tie))) {
/* 72 */       this.servant = new ServantObject();
/* 73 */       this.servant.servant = ((Tie)paramObject).getTarget();
/*    */     } else {
/* 75 */       this.servant = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void unexport()
/*    */   {
/* 86 */     this.servant = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl
 * JD-Core Version:    0.6.2
 */