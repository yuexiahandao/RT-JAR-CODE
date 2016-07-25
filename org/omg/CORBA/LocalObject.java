/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ 
/*     */ public class LocalObject
/*     */   implements Object
/*     */ {
/*  59 */   private static String reason = "This is a locally constrained object.";
/*     */ 
/*     */   public boolean _is_equivalent(Object paramObject)
/*     */   {
/*  84 */     return equals(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean _non_existent()
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   public int _hash(int paramInt)
/*     */   {
/* 108 */     return hashCode();
/*     */   }
/*     */ 
/*     */   public boolean _is_a(String paramString)
/*     */   {
/* 127 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Object _duplicate()
/*     */   {
/* 141 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public void _release()
/*     */   {
/* 154 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Request _request(String paramString)
/*     */   {
/* 171 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue)
/*     */   {
/* 198 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList)
/*     */   {
/* 232 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Object _get_interface()
/*     */   {
/* 250 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Object _get_interface_def()
/*     */   {
/* 271 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public ORB _orb()
/*     */   {
/* 286 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Policy _get_policy(int paramInt)
/*     */   {
/* 304 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public DomainManager[] _get_domain_managers()
/*     */   {
/* 319 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public Object _set_policy_override(Policy[] paramArrayOfPolicy, SetOverrideType paramSetOverrideType)
/*     */   {
/* 341 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public boolean _is_local()
/*     */   {
/* 357 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public ServantObject _servant_preinvoke(String paramString, Class paramClass)
/*     */   {
/* 377 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public void _servant_postinvoke(ServantObject paramServantObject)
/*     */   {
/* 391 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public OutputStream _request(String paramString, boolean paramBoolean)
/*     */   {
/* 420 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public InputStream _invoke(OutputStream paramOutputStream)
/*     */     throws ApplicationException, RemarshalException
/*     */   {
/* 458 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public void _releaseReply(InputStream paramInputStream)
/*     */   {
/* 480 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ 
/*     */   public boolean validate_connection()
/*     */   {
/* 498 */     throw new NO_IMPLEMENT(reason);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.LocalObject
 * JD-Core Version:    0.6.2
 */