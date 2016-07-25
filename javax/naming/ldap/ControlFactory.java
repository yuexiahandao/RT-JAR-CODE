/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.naming.internal.FactoryEnumeration;
/*     */ import com.sun.naming.internal.ResourceManager;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public abstract class ControlFactory
/*     */ {
/*     */   public abstract Control getControlInstance(Control paramControl)
/*     */     throws NamingException;
/*     */ 
/*     */   public static Control getControlInstance(Control paramControl, Context paramContext, Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 140 */     FactoryEnumeration localFactoryEnumeration = ResourceManager.getFactories("java.naming.factory.control", paramHashtable, paramContext);
/*     */ 
/* 143 */     if (localFactoryEnumeration == null) {
/* 144 */       return paramControl;
/*     */     }
/*     */ 
/* 148 */     Control localControl = null;
/*     */ 
/* 150 */     while ((localControl == null) && (localFactoryEnumeration.hasMore())) {
/* 151 */       ControlFactory localControlFactory = (ControlFactory)localFactoryEnumeration.next();
/* 152 */       localControl = localControlFactory.getControlInstance(paramControl);
/*     */     }
/*     */ 
/* 155 */     return localControl != null ? localControl : paramControl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.ControlFactory
 * JD-Core Version:    0.6.2
 */