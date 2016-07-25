/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Insets
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   public int top;
/*     */   public int left;
/*     */   public int bottom;
/*     */   public int right;
/*     */   private static final long serialVersionUID = -2272572637695466749L;
/*     */ 
/*     */   public Insets(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 104 */     this.top = paramInt1;
/* 105 */     this.left = paramInt2;
/* 106 */     this.bottom = paramInt3;
/* 107 */     this.right = paramInt4;
/*     */   }
/*     */ 
/*     */   public void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 120 */     this.top = paramInt1;
/* 121 */     this.left = paramInt2;
/* 122 */     this.bottom = paramInt3;
/* 123 */     this.right = paramInt4;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 136 */     if ((paramObject instanceof Insets)) {
/* 137 */       Insets localInsets = (Insets)paramObject;
/* 138 */       return (this.top == localInsets.top) && (this.left == localInsets.left) && (this.bottom == localInsets.bottom) && (this.right == localInsets.right);
/*     */     }
/*     */ 
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 150 */     int i = this.left + this.bottom;
/* 151 */     int j = this.right + this.top;
/* 152 */     int k = i * (i + 1) / 2 + this.left;
/* 153 */     int m = j * (j + 1) / 2 + this.top;
/* 154 */     int n = k + m;
/* 155 */     return n * (n + 1) / 2 + m;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     return getClass().getName() + "[top=" + this.top + ",left=" + this.left + ",bottom=" + this.bottom + ",right=" + this.right + "]";
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 177 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 180 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/*  89 */     Toolkit.loadLibraries();
/*  90 */     if (!GraphicsEnvironment.isHeadless())
/*  91 */       initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Insets
 * JD-Core Version:    0.6.2
 */