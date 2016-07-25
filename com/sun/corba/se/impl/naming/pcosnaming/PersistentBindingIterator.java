/*     */ package com.sun.corba.se.impl.naming.pcosnaming;
/*     */ 
/*     */ import com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CosNaming.Binding;
/*     */ import org.omg.CosNaming.BindingHolder;
/*     */ import org.omg.CosNaming.BindingType;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ import org.omg.PortableServer.POA;
/*     */ 
/*     */ public class PersistentBindingIterator extends BindingIteratorImpl
/*     */ {
/*     */   private POA biPOA;
/*     */   private int currentSize;
/*     */   private Hashtable theHashtable;
/*     */   private Enumeration theEnumeration;
/*     */   private ORB orb;
/*     */ 
/*     */   public PersistentBindingIterator(ORB paramORB, Hashtable paramHashtable, POA paramPOA)
/*     */     throws Exception
/*     */   {
/*  74 */     super(paramORB);
/*  75 */     this.orb = paramORB;
/*  76 */     this.theHashtable = paramHashtable;
/*  77 */     this.theEnumeration = this.theHashtable.keys();
/*  78 */     this.currentSize = this.theHashtable.size();
/*  79 */     this.biPOA = paramPOA;
/*     */   }
/*     */ 
/*     */   public final boolean NextOne(BindingHolder paramBindingHolder)
/*     */   {
/*  92 */     boolean bool = this.theEnumeration.hasMoreElements();
/*  93 */     if (bool) {
/*  94 */       InternalBindingKey localInternalBindingKey = (InternalBindingKey)this.theEnumeration.nextElement();
/*     */ 
/*  96 */       InternalBindingValue localInternalBindingValue = (InternalBindingValue)this.theHashtable.get(localInternalBindingKey);
/*     */ 
/*  98 */       NameComponent localNameComponent = new NameComponent(localInternalBindingKey.id, localInternalBindingKey.kind);
/*  99 */       NameComponent[] arrayOfNameComponent = new NameComponent[1];
/* 100 */       arrayOfNameComponent[0] = localNameComponent;
/* 101 */       BindingType localBindingType = localInternalBindingValue.theBindingType;
/*     */ 
/* 103 */       paramBindingHolder.value = new Binding(arrayOfNameComponent, localBindingType);
/*     */     }
/*     */     else
/*     */     {
/* 107 */       paramBindingHolder.value = new Binding(new NameComponent[0], BindingType.nobject);
/*     */     }
/* 109 */     return bool;
/*     */   }
/*     */ 
/*     */   public final void Destroy()
/*     */   {
/*     */     try
/*     */     {
/* 120 */       byte[] arrayOfByte = this.biPOA.servant_to_id(this);
/* 121 */       if (arrayOfByte != null)
/* 122 */         this.biPOA.deactivate_object(arrayOfByte);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 126 */       throw new INTERNAL("Exception in BindingIterator.Destroy " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int RemainingElements()
/*     */   {
/* 135 */     return this.currentSize;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.PersistentBindingIterator
 * JD-Core Version:    0.6.2
 */