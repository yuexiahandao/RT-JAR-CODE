/*     */ package org.omg.CORBA.portable;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.omg.CORBA.BAD_OPERATION;
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
/*     */ public abstract class ObjectImpl
/*     */   implements org.omg.CORBA.Object
/*     */ {
/*     */   private transient Delegate __delegate;
/*     */ 
/*     */   public Delegate _get_delegate()
/*     */   {
/*  70 */     if (this.__delegate == null)
/*  71 */       throw new BAD_OPERATION("The delegate has not been set!");
/*  72 */     return this.__delegate;
/*     */   }
/*     */ 
/*     */   public void _set_delegate(Delegate paramDelegate)
/*     */   {
/*  87 */     this.__delegate = paramDelegate;
/*     */   }
/*     */ 
/*     */   public abstract String[] _ids();
/*     */ 
/*     */   public org.omg.CORBA.Object _duplicate()
/*     */   {
/* 109 */     return _get_delegate().duplicate(this);
/*     */   }
/*     */ 
/*     */   public void _release()
/*     */   {
/* 116 */     _get_delegate().release(this);
/*     */   }
/*     */ 
/*     */   public boolean _is_a(String paramString)
/*     */   {
/* 130 */     return _get_delegate().is_a(this, paramString);
/*     */   }
/*     */ 
/*     */   public boolean _is_equivalent(org.omg.CORBA.Object paramObject)
/*     */   {
/* 144 */     return _get_delegate().is_equivalent(this, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean _non_existent()
/*     */   {
/* 155 */     return _get_delegate().non_existent(this);
/*     */   }
/*     */ 
/*     */   public int _hash(int paramInt)
/*     */   {
/* 168 */     return _get_delegate().hash(this, paramInt);
/*     */   }
/*     */ 
/*     */   public Request _request(String paramString)
/*     */   {
/* 181 */     return _get_delegate().request(this, paramString);
/*     */   }
/*     */ 
/*     */   public Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue)
/*     */   {
/* 203 */     return _get_delegate().create_request(this, paramContext, paramString, paramNVList, paramNamedValue);
/*     */   }
/*     */ 
/*     */   public Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList)
/*     */   {
/* 244 */     return _get_delegate().create_request(this, paramContext, paramString, paramNVList, paramNamedValue, paramExceptionList, paramContextList);
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object _get_interface_def()
/*     */   {
/* 268 */     Delegate localDelegate = _get_delegate();
/*     */     try
/*     */     {
/* 273 */       return localDelegate.get_interface_def(this);
/*     */     }
/*     */     catch (NO_IMPLEMENT localNO_IMPLEMENT)
/*     */     {
/*     */       try {
/* 278 */         Class[] arrayOfClass = { org.omg.CORBA.Object.class };
/* 279 */         localObject = localDelegate.getClass().getMethod("get_interface", arrayOfClass);
/*     */ 
/* 281 */         java.lang.Object[] arrayOfObject = { this };
/* 282 */         return (org.omg.CORBA.Object)((Method)localObject).invoke(localDelegate, arrayOfObject);
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException) {
/* 285 */         java.lang.Object localObject = localInvocationTargetException.getTargetException();
/* 286 */         if ((localObject instanceof Error)) {
/* 287 */           throw ((Error)localObject);
/*     */         }
/* 289 */         if ((localObject instanceof RuntimeException)) {
/* 290 */           throw ((RuntimeException)localObject);
/*     */         }
/*     */ 
/* 293 */         throw new NO_IMPLEMENT();
/*     */       }
/*     */       catch (RuntimeException localRuntimeException) {
/* 296 */         throw localRuntimeException; } catch (Exception localException) {  }
/*     */     }
/* 298 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public ORB _orb()
/*     */   {
/* 313 */     return _get_delegate().orb(this);
/*     */   }
/*     */ 
/*     */   public Policy _get_policy(int paramInt)
/*     */   {
/* 328 */     return _get_delegate().get_policy(this, paramInt);
/*     */   }
/*     */ 
/*     */   public DomainManager[] _get_domain_managers()
/*     */   {
/* 339 */     return _get_delegate().get_domain_managers(this);
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object _set_policy_override(Policy[] paramArrayOfPolicy, SetOverrideType paramSetOverrideType)
/*     */   {
/* 360 */     return _get_delegate().set_policy_override(this, paramArrayOfPolicy, paramSetOverrideType);
/*     */   }
/*     */ 
/*     */   public boolean _is_local()
/*     */   {
/* 372 */     return _get_delegate().is_local(this);
/*     */   }
/*     */ 
/*     */   public ServantObject _servant_preinvoke(String paramString, Class paramClass)
/*     */   {
/* 409 */     return _get_delegate().servant_preinvoke(this, paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public void _servant_postinvoke(ServantObject paramServantObject)
/*     */   {
/* 428 */     _get_delegate().servant_postinvoke(this, paramServantObject);
/*     */   }
/*     */ 
/*     */   public OutputStream _request(String paramString, boolean paramBoolean)
/*     */   {
/* 449 */     return _get_delegate().request(this, paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   public InputStream _invoke(OutputStream paramOutputStream)
/*     */     throws ApplicationException, RemarshalException
/*     */   {
/* 475 */     return _get_delegate().invoke(this, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void _releaseReply(InputStream paramInputStream)
/*     */   {
/* 492 */     _get_delegate().releaseReply(this, paramInputStream);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 502 */     if (this.__delegate != null) {
/* 503 */       return this.__delegate.toString(this);
/*     */     }
/* 505 */     return getClass().getName() + ": no delegate set";
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 514 */     if (this.__delegate != null) {
/* 515 */       return this.__delegate.hashCode(this);
/*     */     }
/* 517 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(java.lang.Object paramObject)
/*     */   {
/* 529 */     if (this.__delegate != null) {
/* 530 */       return this.__delegate.equals(this, paramObject);
/*     */     }
/* 532 */     return this == paramObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.portable.ObjectImpl
 * JD-Core Version:    0.6.2
 */