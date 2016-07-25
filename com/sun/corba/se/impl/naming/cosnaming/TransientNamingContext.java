/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.Hashtable;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.omg.CORBA.Object;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CosNaming.Binding;
/*     */ import org.omg.CosNaming.BindingIterator;
/*     */ import org.omg.CosNaming.BindingIteratorHelper;
/*     */ import org.omg.CosNaming.BindingIteratorHolder;
/*     */ import org.omg.CosNaming.BindingListHolder;
/*     */ import org.omg.CosNaming.BindingType;
/*     */ import org.omg.CosNaming.BindingTypeHolder;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ import org.omg.CosNaming.NamingContext;
/*     */ import org.omg.CosNaming.NamingContextHelper;
/*     */ import org.omg.PortableServer.POA;
/*     */ 
/*     */ public class TransientNamingContext extends NamingContextImpl
/*     */   implements NamingContextDataStore
/*     */ {
/*     */   private Logger readLogger;
/*     */   private Logger updateLogger;
/*     */   private Logger lifecycleLogger;
/*     */   private NamingSystemException wrapper;
/* 351 */   private final Hashtable theHashtable = new Hashtable();
/*     */   public Object localRoot;
/*     */ 
/*     */   public TransientNamingContext(ORB paramORB, Object paramObject, POA paramPOA)
/*     */     throws Exception
/*     */   {
/* 102 */     super(paramORB, paramPOA);
/* 103 */     this.wrapper = NamingSystemException.get(paramORB, "naming");
/*     */ 
/* 105 */     this.localRoot = paramObject;
/* 106 */     this.readLogger = paramORB.getLogger("naming.read");
/* 107 */     this.updateLogger = paramORB.getLogger("naming.update");
/* 108 */     this.lifecycleLogger = paramORB.getLogger("naming.lifecycle");
/*     */ 
/* 110 */     this.lifecycleLogger.fine("Root TransientNamingContext LIFECYCLE.CREATED");
/*     */   }
/*     */ 
/*     */   public final void Bind(NameComponent paramNameComponent, Object paramObject, BindingType paramBindingType)
/*     */     throws SystemException
/*     */   {
/* 129 */     InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/* 130 */     NameComponent[] arrayOfNameComponent = new NameComponent[1];
/* 131 */     arrayOfNameComponent[0] = paramNameComponent;
/* 132 */     Binding localBinding = new Binding(arrayOfNameComponent, paramBindingType);
/* 133 */     InternalBindingValue localInternalBindingValue1 = new InternalBindingValue(localBinding, null);
/* 134 */     localInternalBindingValue1.theObjectRef = paramObject;
/*     */ 
/* 136 */     InternalBindingValue localInternalBindingValue2 = (InternalBindingValue)this.theHashtable.put(localInternalBindingKey, localInternalBindingValue1);
/*     */ 
/* 139 */     if (localInternalBindingValue2 != null) {
/* 140 */       this.updateLogger.warning("<<NAMING BIND>>Name " + getName(paramNameComponent) + " Was Already Bound");
/*     */ 
/* 142 */       throw this.wrapper.transNcBindAlreadyBound();
/*     */     }
/* 144 */     if (this.updateLogger.isLoggable(Level.FINE))
/* 145 */       this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>>Name Component: " + paramNameComponent.id + "." + paramNameComponent.kind);
/*     */   }
/*     */ 
/*     */   public final Object Resolve(NameComponent paramNameComponent, BindingTypeHolder paramBindingTypeHolder)
/*     */     throws SystemException
/*     */   {
/* 168 */     if ((paramNameComponent.id.length() == 0) && (paramNameComponent.kind.length() == 0))
/*     */     {
/* 171 */       paramBindingTypeHolder.value = BindingType.ncontext;
/* 172 */       return this.localRoot;
/*     */     }
/*     */ 
/* 176 */     InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/*     */ 
/* 178 */     InternalBindingValue localInternalBindingValue = (InternalBindingValue)this.theHashtable.get(localInternalBindingKey);
/*     */ 
/* 180 */     if (localInternalBindingValue == null) return null;
/* 181 */     if (this.readLogger.isLoggable(Level.FINE)) {
/* 182 */       this.readLogger.fine("<<NAMING RESOLVE>><<SUCCESS>>Namecomponent :" + getName(paramNameComponent));
/*     */     }
/*     */ 
/* 187 */     paramBindingTypeHolder.value = localInternalBindingValue.theBinding.binding_type;
/* 188 */     return localInternalBindingValue.theObjectRef;
/*     */   }
/*     */ 
/*     */   public final Object Unbind(NameComponent paramNameComponent)
/*     */     throws SystemException
/*     */   {
/* 205 */     InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/* 206 */     InternalBindingValue localInternalBindingValue = (InternalBindingValue)this.theHashtable.remove(localInternalBindingKey);
/*     */ 
/* 210 */     if (localInternalBindingValue == null) {
/* 211 */       if (this.updateLogger.isLoggable(Level.FINE)) {
/* 212 */         this.updateLogger.fine("<<NAMING UNBIND>><<FAILURE>> There was no binding with the name " + getName(paramNameComponent) + " to Unbind ");
/*     */       }
/*     */ 
/* 216 */       return null;
/*     */     }
/* 218 */     if (this.updateLogger.isLoggable(Level.FINE)) {
/* 219 */       this.updateLogger.fine("<<NAMING UNBIND>><<SUCCESS>> NameComponent:  " + getName(paramNameComponent));
/*     */     }
/*     */ 
/* 222 */     return localInternalBindingValue.theObjectRef;
/*     */   }
/*     */ 
/*     */   public final void List(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*     */     throws SystemException
/*     */   {
/*     */     try
/*     */     {
/* 246 */       TransientBindingIterator localTransientBindingIterator = new TransientBindingIterator(this.orb, (Hashtable)this.theHashtable.clone(), this.nsPOA);
/*     */ 
/* 250 */       localTransientBindingIterator.list(paramInt, paramBindingListHolder);
/*     */ 
/* 252 */       byte[] arrayOfByte = this.nsPOA.activate_object(localTransientBindingIterator);
/* 253 */       Object localObject = this.nsPOA.id_to_reference(arrayOfByte);
/*     */ 
/* 256 */       BindingIterator localBindingIterator = BindingIteratorHelper.narrow(localObject);
/*     */ 
/* 259 */       paramBindingIteratorHolder.value = localBindingIterator;
/*     */     } catch (SystemException localSystemException) {
/* 261 */       this.readLogger.warning("<<NAMING LIST>><<FAILURE>>" + localSystemException);
/* 262 */       throw localSystemException;
/*     */     }
/*     */     catch (Exception localException) {
/* 265 */       this.readLogger.severe("<<NAMING LIST>><<FAILURE>>" + localException);
/* 266 */       throw this.wrapper.transNcListGotExc(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final NamingContext NewContext()
/*     */     throws SystemException
/*     */   {
/*     */     try
/*     */     {
/* 283 */       TransientNamingContext localTransientNamingContext = new TransientNamingContext(this.orb, this.localRoot, this.nsPOA);
/*     */ 
/* 287 */       byte[] arrayOfByte = this.nsPOA.activate_object(localTransientNamingContext);
/* 288 */       Object localObject = this.nsPOA.id_to_reference(arrayOfByte);
/* 289 */       this.lifecycleLogger.fine("TransientNamingContext LIFECYCLE.CREATE SUCCESSFUL");
/*     */ 
/* 291 */       return NamingContextHelper.narrow(localObject);
/*     */     }
/*     */     catch (SystemException localSystemException) {
/* 294 */       this.lifecycleLogger.log(Level.WARNING, "<<LIFECYCLE CREATE>><<FAILURE>>", localSystemException);
/*     */ 
/* 296 */       throw localSystemException;
/*     */     } catch (Exception localException) {
/* 298 */       this.lifecycleLogger.log(Level.WARNING, "<<LIFECYCLE CREATE>><<FAILURE>>", localException);
/*     */ 
/* 300 */       throw this.wrapper.transNcNewctxGotExc(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void Destroy()
/*     */     throws SystemException
/*     */   {
/*     */     try
/*     */     {
/* 314 */       byte[] arrayOfByte = this.nsPOA.servant_to_id(this);
/* 315 */       if (arrayOfByte != null) {
/* 316 */         this.nsPOA.deactivate_object(arrayOfByte);
/*     */       }
/* 318 */       if (this.lifecycleLogger.isLoggable(Level.FINE))
/* 319 */         this.lifecycleLogger.fine("<<LIFECYCLE DESTROY>><<SUCCESS>>");
/*     */     }
/*     */     catch (SystemException localSystemException)
/*     */     {
/* 323 */       this.lifecycleLogger.log(Level.WARNING, "<<LIFECYCLE DESTROY>><<FAILURE>>", localSystemException);
/*     */ 
/* 325 */       throw localSystemException;
/*     */     } catch (Exception localException) {
/* 327 */       this.lifecycleLogger.log(Level.WARNING, "<<LIFECYCLE DESTROY>><<FAILURE>>", localException);
/*     */ 
/* 329 */       throw this.wrapper.transNcDestroyGotExc(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getName(NameComponent paramNameComponent)
/*     */   {
/* 337 */     return paramNameComponent.id + "." + paramNameComponent.kind;
/*     */   }
/*     */ 
/*     */   public final boolean IsEmpty()
/*     */   {
/* 347 */     return this.theHashtable.isEmpty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.TransientNamingContext
 * JD-Core Version:    0.6.2
 */