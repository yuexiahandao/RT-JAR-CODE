/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class TransformAttribute
/*     */   implements Serializable
/*     */ {
/*     */   private AffineTransform transform;
/* 100 */   public static final TransformAttribute IDENTITY = new TransformAttribute(null);
/*     */   static final long serialVersionUID = 3356247357827709530L;
/*     */ 
/*     */   public TransformAttribute(AffineTransform paramAffineTransform)
/*     */   {
/*  70 */     if ((paramAffineTransform != null) && (!paramAffineTransform.isIdentity()))
/*  71 */       this.transform = new AffineTransform(paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public AffineTransform getTransform()
/*     */   {
/*  81 */     AffineTransform localAffineTransform = this.transform;
/*  82 */     return localAffineTransform == null ? new AffineTransform() : new AffineTransform(localAffineTransform);
/*     */   }
/*     */ 
/*     */   public boolean isIdentity()
/*     */   {
/*  93 */     return this.transform == null;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 107 */     if (this.transform == null) {
/* 108 */       this.transform = new AffineTransform();
/*     */     }
/* 110 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 117 */     if ((this.transform == null) || (this.transform.isIdentity())) {
/* 118 */       return IDENTITY;
/*     */     }
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 130 */     return this.transform == null ? 0 : this.transform.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 145 */       TransformAttribute localTransformAttribute = (TransformAttribute)paramObject;
/* 146 */       if (this.transform == null) {
/* 147 */         return localTransformAttribute.transform == null;
/*     */       }
/* 149 */       return this.transform.equals(localTransformAttribute.transform);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TransformAttribute
 * JD-Core Version:    0.6.2
 */