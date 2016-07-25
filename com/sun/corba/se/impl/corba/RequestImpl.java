/*     */ package com.sun.corba.se.impl.corba;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.Bounds;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ContextList;
/*     */ import org.omg.CORBA.Environment;
/*     */ import org.omg.CORBA.ExceptionList;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.Request;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.WrongTransaction;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class RequestImpl extends Request
/*     */ {
/*     */   protected org.omg.CORBA.Object _target;
/*     */   protected String _opName;
/*     */   protected NVList _arguments;
/*     */   protected ExceptionList _exceptions;
/*     */   private NamedValue _result;
/*     */   protected Environment _env;
/*     */   private Context _ctx;
/*     */   private ContextList _ctxList;
/*     */   protected ORB _orb;
/*     */   private ORBUtilSystemException _wrapper;
/*  87 */   protected boolean _isOneWay = false;
/*     */   private int[] _paramCodes;
/*     */   private long[] _paramLongs;
/*     */   private java.lang.Object[] _paramObjects;
/*  95 */   protected boolean gotResponse = false;
/*     */ 
/*     */   public RequestImpl(ORB paramORB, org.omg.CORBA.Object paramObject, Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList)
/*     */   {
/* 113 */     this._orb = paramORB;
/* 114 */     this._wrapper = ORBUtilSystemException.get(paramORB, "oa.invocation");
/*     */ 
/* 118 */     this._target = paramObject;
/* 119 */     this._ctx = paramContext;
/* 120 */     this._opName = paramString;
/*     */ 
/* 123 */     if (paramNVList == null)
/* 124 */       this._arguments = new NVListImpl(this._orb);
/*     */     else {
/* 126 */       this._arguments = paramNVList;
/*     */     }
/*     */ 
/* 129 */     this._result = paramNamedValue;
/*     */ 
/* 132 */     if (paramExceptionList == null)
/* 133 */       this._exceptions = new ExceptionListImpl();
/*     */     else {
/* 135 */       this._exceptions = paramExceptionList;
/*     */     }
/*     */ 
/* 138 */     if (paramContextList == null)
/* 139 */       this._ctxList = new ContextListImpl(this._orb);
/*     */     else {
/* 141 */       this._ctxList = paramContextList;
/*     */     }
/*     */ 
/* 144 */     this._env = new EnvironmentImpl();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object target()
/*     */   {
/* 150 */     return this._target;
/*     */   }
/*     */ 
/*     */   public String operation()
/*     */   {
/* 155 */     return this._opName;
/*     */   }
/*     */ 
/*     */   public NVList arguments()
/*     */   {
/* 160 */     return this._arguments;
/*     */   }
/*     */ 
/*     */   public NamedValue result()
/*     */   {
/* 165 */     return this._result;
/*     */   }
/*     */ 
/*     */   public Environment env()
/*     */   {
/* 170 */     return this._env;
/*     */   }
/*     */ 
/*     */   public ExceptionList exceptions()
/*     */   {
/* 175 */     return this._exceptions;
/*     */   }
/*     */ 
/*     */   public ContextList contexts()
/*     */   {
/* 180 */     return this._ctxList;
/*     */   }
/*     */ 
/*     */   public synchronized Context ctx()
/*     */   {
/* 185 */     if (this._ctx == null)
/* 186 */       this._ctx = new ContextImpl(this._orb);
/* 187 */     return this._ctx;
/*     */   }
/*     */ 
/*     */   public synchronized void ctx(Context paramContext)
/*     */   {
/* 192 */     this._ctx = paramContext;
/*     */   }
/*     */ 
/*     */   public synchronized Any add_in_arg()
/*     */   {
/* 197 */     return this._arguments.add(1).value();
/*     */   }
/*     */ 
/*     */   public synchronized Any add_named_in_arg(String paramString)
/*     */   {
/* 202 */     return this._arguments.add_item(paramString, 1).value();
/*     */   }
/*     */ 
/*     */   public synchronized Any add_inout_arg()
/*     */   {
/* 207 */     return this._arguments.add(3).value();
/*     */   }
/*     */ 
/*     */   public synchronized Any add_named_inout_arg(String paramString)
/*     */   {
/* 212 */     return this._arguments.add_item(paramString, 3).value();
/*     */   }
/*     */ 
/*     */   public synchronized Any add_out_arg()
/*     */   {
/* 217 */     return this._arguments.add(2).value();
/*     */   }
/*     */ 
/*     */   public synchronized Any add_named_out_arg(String paramString)
/*     */   {
/* 222 */     return this._arguments.add_item(paramString, 2).value();
/*     */   }
/*     */ 
/*     */   public synchronized void set_return_type(TypeCode paramTypeCode)
/*     */   {
/* 227 */     if (this._result == null)
/* 228 */       this._result = new NamedValueImpl(this._orb);
/* 229 */     this._result.value().type(paramTypeCode);
/*     */   }
/*     */ 
/*     */   public synchronized Any return_value()
/*     */   {
/* 234 */     if (this._result == null)
/* 235 */       this._result = new NamedValueImpl(this._orb);
/* 236 */     return this._result.value();
/*     */   }
/*     */ 
/*     */   public synchronized void add_exception(TypeCode paramTypeCode)
/*     */   {
/* 241 */     this._exceptions.add(paramTypeCode);
/*     */   }
/*     */ 
/*     */   public synchronized void invoke()
/*     */   {
/* 246 */     doInvocation();
/*     */   }
/*     */ 
/*     */   public synchronized void send_oneway()
/*     */   {
/* 251 */     this._isOneWay = true;
/* 252 */     doInvocation();
/*     */   }
/*     */ 
/*     */   public synchronized void send_deferred()
/*     */   {
/* 257 */     AsynchInvoke localAsynchInvoke = new AsynchInvoke(this._orb, this, false);
/* 258 */     new Thread(localAsynchInvoke).start();
/*     */   }
/*     */ 
/*     */   public synchronized boolean poll_response()
/*     */   {
/* 268 */     return this.gotResponse;
/*     */   }
/*     */ 
/*     */   public synchronized void get_response()
/*     */     throws WrongTransaction
/*     */   {
/* 274 */     while (!this.gotResponse)
/*     */     {
/*     */       try
/*     */       {
/* 278 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void doInvocation()
/*     */   {
/* 293 */     Delegate localDelegate = StubAdapter.getDelegate(this._target);
/*     */ 
/* 300 */     this._orb.getPIHandler().initiateClientPIRequest(true);
/* 301 */     this._orb.getPIHandler().setClientPIInfo(this);
/*     */ 
/* 303 */     InputStream localInputStream = null;
/*     */     try {
/* 305 */       OutputStream localOutputStream = localDelegate.request(null, this._opName, !this._isOneWay);
/*     */       try
/*     */       {
/* 308 */         for (int i = 0; i < this._arguments.count(); i++) {
/* 309 */           NamedValue localNamedValue = this._arguments.item(i);
/* 310 */           switch (localNamedValue.flags()) {
/*     */           case 1:
/* 312 */             localNamedValue.value().write_value(localOutputStream);
/* 313 */             break;
/*     */           case 2:
/* 315 */             break;
/*     */           case 3:
/* 317 */             localNamedValue.value().write_value(localOutputStream);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Bounds localBounds) {
/* 322 */         throw this._wrapper.boundsErrorInDiiRequest(localBounds);
/*     */       }
/*     */ 
/* 325 */       localInputStream = localDelegate.invoke(null, localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException)
/*     */     {
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/* 332 */       doInvocation();
/*     */     } catch (SystemException localSystemException) {
/* 334 */       this._env.exception(localSystemException);
/*     */ 
/* 338 */       throw localSystemException;
/*     */     } finally {
/* 340 */       localDelegate.releaseReply(null, localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unmarshalReply(InputStream paramInputStream)
/*     */   {
/*     */     java.lang.Object localObject;
/* 348 */     if (this._result != null) {
/* 349 */       Any localAny1 = this._result.value();
/* 350 */       localObject = localAny1.type();
/* 351 */       if (((TypeCode)localObject).kind().value() != 1) {
/* 352 */         localAny1.read_value(paramInputStream, (TypeCode)localObject);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 357 */       for (int i = 0; i < this._arguments.count(); i++) {
/* 358 */         localObject = this._arguments.item(i);
/* 359 */         switch (((NamedValue)localObject).flags()) {
/*     */         case 1:
/* 361 */           break;
/*     */         case 2:
/*     */         case 3:
/* 364 */           Any localAny2 = ((NamedValue)localObject).value();
/* 365 */           localAny2.read_value(paramInputStream, localAny2.type());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Bounds localBounds)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.RequestImpl
 * JD-Core Version:    0.6.2
 */