/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Vector;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ 
/*    */ public class ServiceContextRegistry
/*    */ {
/*    */   private ORB orb;
/*    */   private Vector scCollection;
/*    */ 
/*    */   private void dprint(String paramString)
/*    */   {
/* 42 */     ORBUtility.dprint(this, paramString);
/*    */   }
/*    */ 
/*    */   public ServiceContextRegistry(ORB paramORB)
/*    */   {
/* 47 */     this.scCollection = new Vector();
/* 48 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   public void register(Class paramClass)
/*    */   {
/* 64 */     if (ORB.ORBInitDebug) {
/* 65 */       dprint("Registering service context class " + paramClass);
/*    */     }
/* 67 */     ServiceContextData localServiceContextData = new ServiceContextData(paramClass);
/*    */ 
/* 69 */     if (findServiceContextData(localServiceContextData.getId()) == null)
/* 70 */       this.scCollection.addElement(localServiceContextData);
/*    */     else
/* 72 */       throw new BAD_PARAM("Tried to register duplicate service context");
/*    */   }
/*    */ 
/*    */   public ServiceContextData findServiceContextData(int paramInt)
/*    */   {
/* 77 */     if (ORB.ORBInitDebug) {
/* 78 */       dprint("Searching registry for service context id " + paramInt);
/*    */     }
/* 80 */     Enumeration localEnumeration = this.scCollection.elements();
/* 81 */     while (localEnumeration.hasMoreElements()) {
/* 82 */       ServiceContextData localServiceContextData = (ServiceContextData)localEnumeration.nextElement();
/*    */ 
/* 84 */       if (localServiceContextData.getId() == paramInt) {
/* 85 */         if (ORB.ORBInitDebug) {
/* 86 */           dprint("Service context data found: " + localServiceContextData);
/*    */         }
/* 88 */         return localServiceContextData;
/*    */       }
/*    */     }
/*    */ 
/* 92 */     if (ORB.ORBInitDebug) {
/* 93 */       dprint("Service context data not found");
/*    */     }
/* 95 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.ServiceContextRegistry
 * JD-Core Version:    0.6.2
 */