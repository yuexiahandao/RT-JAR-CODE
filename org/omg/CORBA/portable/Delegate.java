/*     */ package org.omg.CORBA.portable;
/*     */ 
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ContextList;
/*     */ import org.omg.CORBA.DomainManager;
/*     */ import org.omg.CORBA.ExceptionList;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.Request;
/*     */ import org.omg.CORBA.SetOverrideType;
/*     */ 
/*     */ public abstract class Delegate
/*     */ {
/*     */   public abstract org.omg.CORBA.Object get_interface_def(org.omg.CORBA.Object paramObject);
/*     */ 
/*     */   public abstract org.omg.CORBA.Object duplicate(org.omg.CORBA.Object paramObject);
/*     */ 
/*     */   public abstract void release(org.omg.CORBA.Object paramObject);
/*     */ 
/*     */   public abstract boolean is_a(org.omg.CORBA.Object paramObject, String paramString);
/*     */ 
/*     */   public abstract boolean non_existent(org.omg.CORBA.Object paramObject);
/*     */ 
/*     */   public abstract boolean is_equivalent(org.omg.CORBA.Object paramObject1, org.omg.CORBA.Object paramObject2);
/*     */ 
/*     */   public abstract int hash(org.omg.CORBA.Object paramObject, int paramInt);
/*     */ 
/*     */   public abstract Request request(org.omg.CORBA.Object paramObject, String paramString);
/*     */ 
/*     */   public abstract Request create_request(org.omg.CORBA.Object paramObject, Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue);
/*     */ 
/*     */   public abstract Request create_request(org.omg.CORBA.Object paramObject, Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList);
/*     */ 
/*     */   public ORB orb(org.omg.CORBA.Object paramObject)
/*     */   {
/* 170 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Policy get_policy(org.omg.CORBA.Object paramObject, int paramInt)
/*     */   {
/* 190 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public DomainManager[] get_domain_managers(org.omg.CORBA.Object paramObject)
/*     */   {
/* 211 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object set_policy_override(org.omg.CORBA.Object paramObject, Policy[] paramArrayOfPolicy, SetOverrideType paramSetOverrideType)
/*     */   {
/* 237 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public boolean is_local(org.omg.CORBA.Object paramObject)
/*     */   {
/* 251 */     return false;
/*     */   }
/*     */ 
/*     */   public ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass)
/*     */   {
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   public void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   public OutputStream request(org.omg.CORBA.Object paramObject, String paramString, boolean paramBoolean)
/*     */   {
/* 323 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public InputStream invoke(org.omg.CORBA.Object paramObject, OutputStream paramOutputStream)
/*     */     throws ApplicationException, RemarshalException
/*     */   {
/* 348 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public void releaseReply(org.omg.CORBA.Object paramObject, InputStream paramInputStream)
/*     */   {
/* 366 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public String toString(org.omg.CORBA.Object paramObject)
/*     */   {
/* 380 */     return paramObject.getClass().getName() + ":" + toString();
/*     */   }
/*     */ 
/*     */   public int hashCode(org.omg.CORBA.Object paramObject)
/*     */   {
/* 393 */     return System.identityHashCode(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(org.omg.CORBA.Object paramObject, java.lang.Object paramObject1)
/*     */   {
/* 406 */     return paramObject == paramObject1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.portable.Delegate
 * JD-Core Version:    0.6.2
 */