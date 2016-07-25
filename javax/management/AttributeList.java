/*     */ package javax.management;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AttributeList extends ArrayList<Object>
/*     */ {
/*     */   private volatile transient boolean typeSafe;
/*     */   private volatile transient boolean tainted;
/*     */   private static final long serialVersionUID = -4077085769279709076L;
/*     */ 
/*     */   public AttributeList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AttributeList(int paramInt)
/*     */   {
/*  89 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public AttributeList(AttributeList paramAttributeList)
/*     */   {
/* 106 */     super(paramAttributeList);
/*     */   }
/*     */ 
/*     */   public AttributeList(List<Attribute> paramList)
/*     */   {
/* 128 */     if (paramList == null) {
/* 129 */       throw new IllegalArgumentException("Null parameter");
/*     */     }
/*     */ 
/* 133 */     adding(paramList);
/*     */ 
/* 137 */     super.addAll(paramList);
/*     */   }
/*     */ 
/*     */   public List<Attribute> asList()
/*     */   {
/* 163 */     this.typeSafe = true;
/* 164 */     if (this.tainted)
/* 165 */       adding(this);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   public void add(Attribute paramAttribute)
/*     */   {
/* 175 */     super.add(paramAttribute);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Attribute paramAttribute)
/*     */   {
/*     */     try
/*     */     {
/* 191 */       super.add(paramInt, paramAttribute);
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 194 */       throw new RuntimeOperationsException(localIndexOutOfBoundsException, "The specified index is out of range");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(int paramInt, Attribute paramAttribute)
/*     */   {
/*     */     try
/*     */     {
/* 210 */       super.set(paramInt, paramAttribute);
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 213 */       throw new RuntimeOperationsException(localIndexOutOfBoundsException, "The specified index is out of range");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean addAll(AttributeList paramAttributeList)
/*     */   {
/* 230 */     return super.addAll(paramAttributeList);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, AttributeList paramAttributeList)
/*     */   {
/*     */     try
/*     */     {
/* 251 */       return super.addAll(paramInt, paramAttributeList);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 253 */       throw new RuntimeOperationsException(localIndexOutOfBoundsException, "The specified index is out of range");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/* 272 */     adding(paramObject);
/* 273 */     return super.add(paramObject);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Object paramObject)
/*     */   {
/* 284 */     adding(paramObject);
/* 285 */     super.add(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<?> paramCollection)
/*     */   {
/* 296 */     adding(paramCollection);
/* 297 */     return super.addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<?> paramCollection)
/*     */   {
/* 308 */     adding(paramCollection);
/* 309 */     return super.addAll(paramInt, paramCollection);
/*     */   }
/*     */ 
/*     */   public Object set(int paramInt, Object paramObject)
/*     */   {
/* 320 */     adding(paramObject);
/* 321 */     return super.set(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   private void adding(Object paramObject) {
/* 325 */     if ((paramObject == null) || ((paramObject instanceof Attribute)))
/* 326 */       return;
/* 327 */     if (this.typeSafe) {
/* 328 */       throw new IllegalArgumentException("Not an Attribute: " + paramObject);
/*     */     }
/* 330 */     this.tainted = true;
/*     */   }
/*     */ 
/*     */   private void adding(Collection<?> paramCollection) {
/* 334 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 335 */       adding(localObject);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.AttributeList
 * JD-Core Version:    0.6.2
 */