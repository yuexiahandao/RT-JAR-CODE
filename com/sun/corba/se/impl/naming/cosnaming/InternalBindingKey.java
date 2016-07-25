/*    */ package com.sun.corba.se.impl.naming.cosnaming;
/*    */ 
/*    */ import org.omg.CosNaming.NameComponent;
/*    */ 
/*    */ public class InternalBindingKey
/*    */ {
/*    */   public NameComponent name;
/*    */   private int idLen;
/*    */   private int kindLen;
/*    */   private int hashVal;
/*    */ 
/*    */   public InternalBindingKey()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InternalBindingKey(NameComponent paramNameComponent)
/*    */   {
/* 51 */     this.idLen = 0;
/* 52 */     this.kindLen = 0;
/* 53 */     setup(paramNameComponent);
/*    */   }
/*    */ 
/*    */   protected void setup(NameComponent paramNameComponent)
/*    */   {
/* 58 */     this.name = paramNameComponent;
/*    */ 
/* 60 */     if (this.name.id != null) {
/* 61 */       this.idLen = this.name.id.length();
/*    */     }
/* 63 */     if (this.name.kind != null) {
/* 64 */       this.kindLen = this.name.kind.length();
/*    */     }
/* 66 */     this.hashVal = 0;
/* 67 */     if (this.idLen > 0)
/* 68 */       this.hashVal += this.name.id.hashCode();
/* 69 */     if (this.kindLen > 0)
/* 70 */       this.hashVal += this.name.kind.hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 75 */     if (paramObject == null)
/* 76 */       return false;
/* 77 */     if ((paramObject instanceof InternalBindingKey)) {
/* 78 */       InternalBindingKey localInternalBindingKey = (InternalBindingKey)paramObject;
/*    */ 
/* 80 */       if ((this.idLen != localInternalBindingKey.idLen) || (this.kindLen != localInternalBindingKey.kindLen)) {
/* 81 */         return false;
/*    */       }
/*    */ 
/* 84 */       if ((this.idLen > 0) && (!this.name.id.equals(localInternalBindingKey.name.id))) {
/* 85 */         return false;
/*    */       }
/*    */ 
/* 88 */       if ((this.kindLen > 0) && (!this.name.kind.equals(localInternalBindingKey.name.kind))) {
/* 89 */         return false;
/*    */       }
/*    */ 
/* 92 */       return true;
/*    */     }
/* 94 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 99 */     return this.hashVal;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.InternalBindingKey
 * JD-Core Version:    0.6.2
 */