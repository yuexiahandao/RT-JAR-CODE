/*     */ package java.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class LinkedHashSet<E> extends HashSet<E>
/*     */   implements Set<E>, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2851667679971038690L;
/*     */ 
/*     */   public LinkedHashSet(int paramInt, float paramFloat)
/*     */   {
/* 134 */     super(paramInt, paramFloat, true);
/*     */   }
/*     */ 
/*     */   public LinkedHashSet(int paramInt)
/*     */   {
/* 146 */     super(paramInt, 0.75F, true);
/*     */   }
/*     */ 
/*     */   public LinkedHashSet()
/*     */   {
/* 154 */     super(16, 0.75F, true);
/*     */   }
/*     */ 
/*     */   public LinkedHashSet(Collection<? extends E> paramCollection)
/*     */   {
/* 168 */     super(Math.max(2 * paramCollection.size(), 11), 0.75F, true);
/* 169 */     addAll(paramCollection);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.LinkedHashSet
 * JD-Core Version:    0.6.2
 */