/*     */ package com.sun.corba.se.impl.naming.pcosnaming;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ 
/*     */ public class InternalBindingKey
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5410796631793704055L;
/*     */   public String id;
/*     */   public String kind;
/*     */ 
/*     */   public InternalBindingKey()
/*     */   {
/*     */   }
/*     */ 
/*     */   public InternalBindingKey(NameComponent paramNameComponent)
/*     */   {
/*  55 */     setup(paramNameComponent);
/*     */   }
/*     */ 
/*     */   protected void setup(NameComponent paramNameComponent)
/*     */   {
/*  60 */     this.id = paramNameComponent.id;
/*  61 */     this.kind = paramNameComponent.kind;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  66 */     if (paramObject == null)
/*  67 */       return false;
/*  68 */     if ((paramObject instanceof InternalBindingKey)) {
/*  69 */       InternalBindingKey localInternalBindingKey = (InternalBindingKey)paramObject;
/*  70 */       if ((this.id != null) && (localInternalBindingKey.id != null))
/*     */       {
/*  72 */         if (this.id.length() != localInternalBindingKey.id.length())
/*     */         {
/*  74 */           return false;
/*     */         }
/*     */ 
/*  77 */         if ((this.id.length() > 0) && (!this.id.equals(localInternalBindingKey.id)))
/*     */         {
/*  79 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*  86 */       else if (((this.id == null) && (localInternalBindingKey.id != null)) || ((this.id != null) && (localInternalBindingKey.id == null)))
/*     */       {
/*  89 */         return false;
/*     */       }
/*     */ 
/*  92 */       if ((this.kind != null) && (localInternalBindingKey.kind != null))
/*     */       {
/*  94 */         if (this.kind.length() != localInternalBindingKey.kind.length())
/*     */         {
/*  96 */           return false;
/*     */         }
/*     */ 
/*  99 */         if ((this.kind.length() > 0) && (!this.kind.equals(localInternalBindingKey.kind)))
/*     */         {
/* 101 */           return false;
/*     */         }
/*     */ 
/*     */       }
/* 108 */       else if (((this.kind == null) && (localInternalBindingKey.kind != null)) || ((this.kind != null) && (localInternalBindingKey.kind == null)))
/*     */       {
/* 111 */         return false;
/*     */       }
/*     */ 
/* 115 */       return true;
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 124 */     int i = 0;
/* 125 */     if (this.id.length() > 0)
/*     */     {
/* 127 */       i += this.id.hashCode();
/*     */     }
/* 129 */     if (this.kind.length() > 0)
/*     */     {
/* 131 */       i += this.kind.hashCode();
/*     */     }
/* 133 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.InternalBindingKey
 * JD-Core Version:    0.6.2
 */