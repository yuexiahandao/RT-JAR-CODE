/*     */ package java.beans.beancontext;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class BeanContextMembershipEvent extends BeanContextEvent
/*     */ {
/*     */   private static final long serialVersionUID = 3499346510334590959L;
/*     */   protected Collection children;
/*     */ 
/*     */   public BeanContextMembershipEvent(BeanContext paramBeanContext, Collection paramCollection)
/*     */   {
/*  69 */     super(paramBeanContext);
/*     */ 
/*  71 */     if (paramCollection == null) throw new NullPointerException("BeanContextMembershipEvent constructor:  changes is null.");
/*     */ 
/*  74 */     this.children = paramCollection;
/*     */   }
/*     */ 
/*     */   public BeanContextMembershipEvent(BeanContext paramBeanContext, Object[] paramArrayOfObject)
/*     */   {
/*  87 */     super(paramBeanContext);
/*     */ 
/*  89 */     if (paramArrayOfObject == null) throw new NullPointerException("BeanContextMembershipEvent:  changes is null.");
/*     */ 
/*  92 */     this.children = Arrays.asList(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  99 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 107 */     return this.children.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 114 */     return this.children.toArray();
/*     */   }
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 120 */     return this.children.iterator();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextMembershipEvent
 * JD-Core Version:    0.6.2
 */