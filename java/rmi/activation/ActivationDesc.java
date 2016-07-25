/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.rmi.MarshalledObject;
/*     */ 
/*     */ public final class ActivationDesc
/*     */   implements Serializable
/*     */ {
/*     */   private ActivationGroupID groupID;
/*     */   private String className;
/*     */   private String location;
/*     */   private MarshalledObject<?> data;
/*     */   private boolean restart;
/*     */   private static final long serialVersionUID = 7455834104417690957L;
/*     */ 
/*     */   public ActivationDesc(String paramString1, String paramString2, MarshalledObject<?> paramMarshalledObject)
/*     */     throws ActivationException
/*     */   {
/* 115 */     this(ActivationGroup.internalCurrentGroupID(), paramString1, paramString2, paramMarshalledObject, false);
/*     */   }
/*     */ 
/*     */   public ActivationDesc(String paramString1, String paramString2, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean)
/*     */     throws ActivationException
/*     */   {
/* 153 */     this(ActivationGroup.internalCurrentGroupID(), paramString1, paramString2, paramMarshalledObject, paramBoolean);
/*     */   }
/*     */ 
/*     */   public ActivationDesc(ActivationGroupID paramActivationGroupID, String paramString1, String paramString2, MarshalledObject<?> paramMarshalledObject)
/*     */   {
/* 186 */     this(paramActivationGroupID, paramString1, paramString2, paramMarshalledObject, false);
/*     */   }
/*     */ 
/*     */   public ActivationDesc(ActivationGroupID paramActivationGroupID, String paramString1, String paramString2, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean)
/*     */   {
/* 219 */     if (paramActivationGroupID == null)
/* 220 */       throw new IllegalArgumentException("groupID can't be null");
/* 221 */     this.groupID = paramActivationGroupID;
/* 222 */     this.className = paramString1;
/* 223 */     this.location = paramString2;
/* 224 */     this.data = paramMarshalledObject;
/* 225 */     this.restart = paramBoolean;
/*     */   }
/*     */ 
/*     */   public ActivationGroupID getGroupID()
/*     */   {
/* 238 */     return this.groupID;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 248 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 258 */     return this.location;
/*     */   }
/*     */ 
/*     */   public MarshalledObject<?> getData()
/*     */   {
/* 268 */     return this.data;
/*     */   }
/*     */ 
/*     */   public boolean getRestartMode()
/*     */   {
/* 286 */     return this.restart;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 299 */     if ((paramObject instanceof ActivationDesc)) {
/* 300 */       ActivationDesc localActivationDesc = (ActivationDesc)paramObject;
/* 301 */       return (this.groupID == null ? localActivationDesc.groupID == null : this.groupID.equals(localActivationDesc.groupID)) && (this.className == null ? localActivationDesc.className == null : this.className.equals(localActivationDesc.className)) && (this.location == null ? localActivationDesc.location == null : this.location.equals(localActivationDesc.location)) && (this.data == null ? localActivationDesc.data == null : this.data.equals(localActivationDesc.data)) && (this.restart == localActivationDesc.restart);
/*     */     }
/*     */ 
/* 313 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 323 */     return (this.location == null ? 0 : this.location.hashCode() << 24) ^ (this.groupID == null ? 0 : this.groupID.hashCode() << 16) ^ (this.className == null ? 0 : this.className.hashCode() << 9) ^ (this.data == null ? 0 : this.data.hashCode() << 1) ^ (this.restart ? 1 : 0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationDesc
 * JD-Core Version:    0.6.2
 */