/*     */ package javax.naming;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class RefAddr
/*     */   implements Serializable
/*     */ {
/*     */   protected String addrType;
/*     */   private static final long serialVersionUID = -1468165120479154358L;
/*     */ 
/*     */   protected RefAddr(String paramString)
/*     */   {
/*  71 */     this.addrType = paramString;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/*  80 */     return this.addrType;
/*     */   }
/*     */ 
/*     */   public abstract Object getContent();
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 106 */     if ((paramObject != null) && ((paramObject instanceof RefAddr))) {
/* 107 */       RefAddr localRefAddr = (RefAddr)paramObject;
/* 108 */       if (this.addrType.compareTo(localRefAddr.addrType) == 0) {
/* 109 */         Object localObject1 = getContent();
/* 110 */         Object localObject2 = localRefAddr.getContent();
/* 111 */         if (localObject1 == localObject2)
/* 112 */           return true;
/* 113 */         if (localObject1 != null)
/* 114 */           return localObject1.equals(localObject2);
/*     */       }
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 129 */     return getContent() == null ? this.addrType.hashCode() : this.addrType.hashCode() + getContent().hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 141 */     StringBuffer localStringBuffer = new StringBuffer("Type: " + this.addrType + "\n");
/*     */ 
/* 143 */     localStringBuffer.append("Content: " + getContent() + "\n");
/* 144 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.RefAddr
 * JD-Core Version:    0.6.2
 */