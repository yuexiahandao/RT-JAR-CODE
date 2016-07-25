/*     */ package org.omg.PortableServer;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.omg.CORBA.BAD_INV_ORDER;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.PortableServer.portable.Delegate;
/*     */ 
/*     */ public abstract class Servant
/*     */ {
/*  44 */   private transient Delegate _delegate = null;
/*     */ 
/*     */   public final Delegate _get_delegate()
/*     */   {
/*  52 */     if (this._delegate == null) {
/*  53 */       throw new BAD_INV_ORDER("The Servant has not been associated with an ORB instance");
/*     */     }
/*     */ 
/*  58 */     return this._delegate;
/*     */   }
/*     */ 
/*     */   public final void _set_delegate(Delegate paramDelegate)
/*     */   {
/*  69 */     this._delegate = paramDelegate;
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.Object _this_object()
/*     */   {
/*  79 */     return _get_delegate().this_object(this);
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.Object _this_object(org.omg.CORBA.ORB paramORB)
/*     */   {
/*     */     try
/*     */     {
/*  90 */       ((org.omg.CORBA_2_3.ORB)paramORB).set_delegate(this);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/*  93 */       throw new BAD_PARAM("POA Servant requires an instance of org.omg.CORBA_2_3.ORB");
/*     */     }
/*     */ 
/*  98 */     return _this_object();
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.ORB _orb()
/*     */   {
/* 108 */     return _get_delegate().orb(this);
/*     */   }
/*     */ 
/*     */   public final POA _poa()
/*     */   {
/* 117 */     return _get_delegate().poa(this);
/*     */   }
/*     */ 
/*     */   public final byte[] _object_id()
/*     */   {
/* 128 */     return _get_delegate().object_id(this);
/*     */   }
/*     */ 
/*     */   public POA _default_POA()
/*     */   {
/* 139 */     return _get_delegate().default_POA(this);
/*     */   }
/*     */ 
/*     */   public boolean _is_a(String paramString)
/*     */   {
/* 155 */     return _get_delegate().is_a(this, paramString);
/*     */   }
/*     */ 
/*     */   public boolean _non_existent()
/*     */   {
/* 167 */     return _get_delegate().non_existent(this);
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object _get_interface_def()
/*     */   {
/* 224 */     Delegate localDelegate = _get_delegate();
/*     */     try
/*     */     {
/* 229 */       return localDelegate.get_interface_def(this);
/*     */     }
/*     */     catch (AbstractMethodError localAbstractMethodError) {
/*     */       try {
/* 233 */         Class[] arrayOfClass = { Servant.class };
/* 234 */         localObject = localDelegate.getClass().getMethod("get_interface", arrayOfClass);
/*     */ 
/* 236 */         java.lang.Object[] arrayOfObject = { this };
/* 237 */         return (org.omg.CORBA.Object)((Method)localObject).invoke(localDelegate, arrayOfObject);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 239 */         java.lang.Object localObject = localInvocationTargetException.getTargetException();
/* 240 */         if ((localObject instanceof Error))
/* 241 */           throw ((Error)localObject);
/* 242 */         if ((localObject instanceof RuntimeException)) {
/* 243 */           throw ((RuntimeException)localObject);
/*     */         }
/* 245 */         throw new NO_IMPLEMENT();
/*     */       }
/*     */       catch (RuntimeException localRuntimeException) {
/* 248 */         throw localRuntimeException; } catch (Exception localException) {  }
/*     */     }
/* 250 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public abstract String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.Servant
 * JD-Core Version:    0.6.2
 */