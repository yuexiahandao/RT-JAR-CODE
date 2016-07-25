/*     */ package javax.naming.spi;
/*     */ 
/*     */ import com.sun.naming.internal.FactoryEnumeration;
/*     */ import com.sun.naming.internal.ResourceManager;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ 
/*     */ public class DirectoryManager extends NamingManager
/*     */ {
/*     */   public static DirContext getContinuationDirContext(CannotProceedException paramCannotProceedException)
/*     */     throws NamingException
/*     */   {
/*  90 */     Hashtable localHashtable = paramCannotProceedException.getEnvironment();
/*  91 */     if (localHashtable == null) {
/*  92 */       localHashtable = new Hashtable(7);
/*     */     }
/*     */     else {
/*  95 */       localHashtable = (Hashtable)localHashtable.clone();
/*     */     }
/*  97 */     localHashtable.put("java.naming.spi.CannotProceedException", paramCannotProceedException);
/*     */ 
/*  99 */     return new ContinuationDirContext(paramCannotProceedException, localHashtable);
/*     */   }
/*     */ 
/*     */   public static Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable, Attributes paramAttributes)
/*     */     throws Exception
/*     */   {
/* 160 */     ObjectFactoryBuilder localObjectFactoryBuilder = getObjectFactoryBuilder();
/*     */     ObjectFactory localObjectFactory;
/* 161 */     if (localObjectFactoryBuilder != null)
/*     */     {
/* 163 */       localObjectFactory = localObjectFactoryBuilder.createObjectFactory(paramObject, paramHashtable);
/* 164 */       if ((localObjectFactory instanceof DirObjectFactory)) {
/* 165 */         return ((DirObjectFactory)localObjectFactory).getObjectInstance(paramObject, paramName, paramContext, paramHashtable, paramAttributes);
/*     */       }
/*     */ 
/* 168 */       return localObjectFactory.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     }
/*     */ 
/* 174 */     Reference localReference = null;
/* 175 */     if ((paramObject instanceof Reference))
/* 176 */       localReference = (Reference)paramObject;
/* 177 */     else if ((paramObject instanceof Referenceable)) {
/* 178 */       localReference = ((Referenceable)paramObject).getReference();
/*     */     }
/*     */ 
/* 183 */     if (localReference != null) {
/* 184 */       String str = localReference.getFactoryClassName();
/* 185 */       if (str != null)
/*     */       {
/* 188 */         localObjectFactory = getObjectFactoryFromReference(localReference, str);
/* 189 */         if ((localObjectFactory instanceof DirObjectFactory)) {
/* 190 */           return ((DirObjectFactory)localObjectFactory).getObjectInstance(localReference, paramName, paramContext, paramHashtable, paramAttributes);
/*     */         }
/* 192 */         if (localObjectFactory != null) {
/* 193 */           return localObjectFactory.getObjectInstance(localReference, paramName, paramContext, paramHashtable);
/*     */         }
/*     */ 
/* 199 */         return paramObject;
/*     */       }
/*     */ 
/* 206 */       localObject = processURLAddrs(localReference, paramName, paramContext, paramHashtable);
/* 207 */       if (localObject != null) {
/* 208 */         return localObject;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     Object localObject = createObjectFromFactories(paramObject, paramName, paramContext, paramHashtable, paramAttributes);
/*     */ 
/* 216 */     return localObject != null ? localObject : paramObject;
/*     */   }
/*     */ 
/*     */   private static Object createObjectFromFactories(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable, Attributes paramAttributes)
/*     */     throws Exception
/*     */   {
/* 223 */     FactoryEnumeration localFactoryEnumeration = ResourceManager.getFactories("java.naming.factory.object", paramHashtable, paramContext);
/*     */ 
/* 226 */     if (localFactoryEnumeration == null) {
/* 227 */       return null;
/*     */     }
/*     */ 
/* 230 */     Object localObject = null;
/*     */ 
/* 232 */     while ((localObject == null) && (localFactoryEnumeration.hasMore())) {
/* 233 */       ObjectFactory localObjectFactory = (ObjectFactory)localFactoryEnumeration.next();
/* 234 */       if ((localObjectFactory instanceof DirObjectFactory)) {
/* 235 */         localObject = ((DirObjectFactory)localObjectFactory).getObjectInstance(paramObject, paramName, paramContext, paramHashtable, paramAttributes);
/*     */       }
/*     */       else {
/* 238 */         localObject = localObjectFactory.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */       }
/*     */     }
/*     */ 
/* 242 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static DirStateFactory.Result getStateToBind(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 308 */     FactoryEnumeration localFactoryEnumeration = ResourceManager.getFactories("java.naming.factory.state", paramHashtable, paramContext);
/*     */ 
/* 311 */     if (localFactoryEnumeration == null)
/*     */     {
/* 313 */       return new DirStateFactory.Result(paramObject, paramAttributes);
/*     */     }
/*     */ 
/* 319 */     DirStateFactory.Result localResult = null;
/* 320 */     while ((localResult == null) && (localFactoryEnumeration.hasMore())) {
/* 321 */       StateFactory localStateFactory = (StateFactory)localFactoryEnumeration.next();
/* 322 */       if ((localStateFactory instanceof DirStateFactory)) {
/* 323 */         localResult = ((DirStateFactory)localStateFactory).getStateToBind(paramObject, paramName, paramContext, paramHashtable, paramAttributes);
/*     */       }
/*     */       else {
/* 326 */         Object localObject = localStateFactory.getStateToBind(paramObject, paramName, paramContext, paramHashtable);
/*     */ 
/* 328 */         if (localObject != null) {
/* 329 */           localResult = new DirStateFactory.Result(localObject, paramAttributes);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 334 */     return localResult != null ? localResult : new DirStateFactory.Result(paramObject, paramAttributes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.DirectoryManager
 * JD-Core Version:    0.6.2
 */