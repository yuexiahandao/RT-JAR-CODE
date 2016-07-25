/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class RoleUnresolvedList extends ArrayList<Object>
/*     */ {
/*     */   private transient boolean typeSafe;
/*     */   private transient boolean tainted;
/*     */   private static final long serialVersionUID = 4054902803091433324L;
/*     */ 
/*     */   public RoleUnresolvedList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RoleUnresolvedList(int paramInt)
/*     */   {
/*  81 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public RoleUnresolvedList(List<RoleUnresolved> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 104 */     if (paramList == null) {
/* 105 */       throw new IllegalArgumentException("Null parameter");
/*     */     }
/*     */ 
/* 109 */     checkTypeSafe(paramList);
/*     */ 
/* 113 */     super.addAll(paramList);
/*     */   }
/*     */ 
/*     */   public List<RoleUnresolved> asList()
/*     */   {
/* 139 */     if (!this.typeSafe) {
/* 140 */       if (this.tainted)
/* 141 */         checkTypeSafe(this);
/* 142 */       this.typeSafe = true;
/*     */     }
/* 144 */     return (List)Util.cast(this);
/*     */   }
/*     */ 
/*     */   public void add(RoleUnresolved paramRoleUnresolved)
/*     */     throws IllegalArgumentException
/*     */   {
/* 161 */     if (paramRoleUnresolved == null) {
/* 162 */       String str = "Invalid parameter";
/* 163 */       throw new IllegalArgumentException(str);
/*     */     }
/* 165 */     super.add(paramRoleUnresolved);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, RoleUnresolved paramRoleUnresolved)
/*     */     throws IllegalArgumentException, IndexOutOfBoundsException
/*     */   {
/* 187 */     if (paramRoleUnresolved == null) {
/* 188 */       String str = "Invalid parameter";
/* 189 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 192 */     super.add(paramInt, paramRoleUnresolved);
/*     */   }
/*     */ 
/*     */   public void set(int paramInt, RoleUnresolved paramRoleUnresolved)
/*     */     throws IllegalArgumentException, IndexOutOfBoundsException
/*     */   {
/* 213 */     if (paramRoleUnresolved == null) {
/* 214 */       String str = "Invalid parameter";
/* 215 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 218 */     super.set(paramInt, paramRoleUnresolved);
/*     */   }
/*     */ 
/*     */   public boolean addAll(RoleUnresolvedList paramRoleUnresolvedList)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 237 */     if (paramRoleUnresolvedList == null) {
/* 238 */       return true;
/*     */     }
/*     */ 
/* 241 */     return super.addAll(paramRoleUnresolvedList);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, RoleUnresolvedList paramRoleUnresolvedList)
/*     */     throws IllegalArgumentException, IndexOutOfBoundsException
/*     */   {
/* 264 */     if (paramRoleUnresolvedList == null) {
/* 265 */       String str = "Invalid parameter";
/* 266 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 269 */     return super.addAll(paramInt, paramRoleUnresolvedList);
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/* 280 */     if (!this.tainted)
/* 281 */       this.tainted = isTainted(paramObject);
/* 282 */     if (this.typeSafe)
/* 283 */       checkTypeSafe(paramObject);
/* 284 */     return super.add(paramObject);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Object paramObject)
/*     */   {
/* 289 */     if (!this.tainted)
/* 290 */       this.tainted = isTainted(paramObject);
/* 291 */     if (this.typeSafe)
/* 292 */       checkTypeSafe(paramObject);
/* 293 */     super.add(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<?> paramCollection)
/*     */   {
/* 298 */     if (!this.tainted)
/* 299 */       this.tainted = isTainted(paramCollection);
/* 300 */     if (this.typeSafe)
/* 301 */       checkTypeSafe(paramCollection);
/* 302 */     return super.addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<?> paramCollection)
/*     */   {
/* 307 */     if (!this.tainted)
/* 308 */       this.tainted = isTainted(paramCollection);
/* 309 */     if (this.typeSafe)
/* 310 */       checkTypeSafe(paramCollection);
/* 311 */     return super.addAll(paramInt, paramCollection);
/*     */   }
/*     */ 
/*     */   public Object set(int paramInt, Object paramObject)
/*     */   {
/* 316 */     if (!this.tainted)
/* 317 */       this.tainted = isTainted(paramObject);
/* 318 */     if (this.typeSafe)
/* 319 */       checkTypeSafe(paramObject);
/* 320 */     return super.set(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   private static void checkTypeSafe(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 328 */       paramObject = (RoleUnresolved)paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 330 */       throw new IllegalArgumentException(localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkTypeSafe(Collection<?> paramCollection)
/*     */   {
/*     */     try
/*     */     {
/* 340 */       for (localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 341 */         RoleUnresolved localRoleUnresolved = (RoleUnresolved)localObject;
/*     */       }
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*     */       Iterator localIterator;
/* 343 */       throw new IllegalArgumentException(localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isTainted(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 352 */       checkTypeSafe(paramObject);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 354 */       return true;
/*     */     }
/* 356 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isTainted(Collection<?> paramCollection)
/*     */   {
/*     */     try
/*     */     {
/* 364 */       checkTypeSafe(paramCollection);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 366 */       return true;
/*     */     }
/* 368 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RoleUnresolvedList
 * JD-Core Version:    0.6.2
 */