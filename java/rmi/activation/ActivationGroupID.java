/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.rmi.server.UID;
/*     */ 
/*     */ public class ActivationGroupID
/*     */   implements Serializable
/*     */ {
/*     */   private ActivationSystem system;
/*  57 */   private UID uid = new UID();
/*     */   private static final long serialVersionUID = -1648432278909740833L;
/*     */ 
/*     */   public ActivationGroupID(ActivationSystem paramActivationSystem)
/*     */   {
/*  69 */     this.system = paramActivationSystem;
/*     */   }
/*     */ 
/*     */   public ActivationSystem getSystem()
/*     */   {
/*  78 */     return this.system;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  90 */     return this.uid.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 106 */     if (this == paramObject)
/* 107 */       return true;
/* 108 */     if ((paramObject instanceof ActivationGroupID)) {
/* 109 */       ActivationGroupID localActivationGroupID = (ActivationGroupID)paramObject;
/* 110 */       return (this.uid.equals(localActivationGroupID.uid)) && (this.system.equals(localActivationGroupID.system));
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationGroupID
 * JD-Core Version:    0.6.2
 */