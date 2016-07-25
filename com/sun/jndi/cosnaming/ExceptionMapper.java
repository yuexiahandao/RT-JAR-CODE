/*     */ package com.sun.jndi.cosnaming;
/*     */ 
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.ContextNotEmptyException;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameAlreadyBoundException;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.NamingManager;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ import org.omg.CosNaming.NamingContext;
/*     */ import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
/*     */ import org.omg.CosNaming.NamingContextPackage.CannotProceed;
/*     */ import org.omg.CosNaming.NamingContextPackage.InvalidName;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotEmpty;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotFound;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
/*     */ 
/*     */ public final class ExceptionMapper
/*     */ {
/*     */   private static final boolean debug = false;
/*     */ 
/*     */   public static final NamingException mapException(Exception paramException, CNCtx paramCNCtx, NameComponent[] paramArrayOfNameComponent)
/*     */     throws NamingException
/*     */   {
/*  47 */     if ((paramException instanceof NamingException)) {
/*  48 */       return (NamingException)paramException;
/*     */     }
/*     */ 
/*  51 */     if ((paramException instanceof RuntimeException))
/*  52 */       throw ((RuntimeException)paramException);
/*     */     Object localObject;
/*  56 */     if ((paramException instanceof NotFound)) {
/*  57 */       if (paramCNCtx.federation) {
/*  58 */         return tryFed((NotFound)paramException, paramCNCtx, paramArrayOfNameComponent);
/*     */       }
/*     */ 
/*  61 */       localObject = new NameNotFoundException();
/*     */     }
/*  64 */     else if ((paramException instanceof CannotProceed))
/*     */     {
/*  66 */       localObject = new CannotProceedException();
/*  67 */       NamingContext localNamingContext = ((CannotProceed)paramException).cxt;
/*  68 */       NameComponent[] arrayOfNameComponent1 = ((CannotProceed)paramException).rest_of_name;
/*     */ 
/*  73 */       if ((paramArrayOfNameComponent != null) && (paramArrayOfNameComponent.length > arrayOfNameComponent1.length)) {
/*  74 */         NameComponent[] arrayOfNameComponent2 = new NameComponent[paramArrayOfNameComponent.length - arrayOfNameComponent1.length];
/*     */ 
/*  76 */         System.arraycopy(paramArrayOfNameComponent, 0, arrayOfNameComponent2, 0, arrayOfNameComponent2.length);
/*     */ 
/*  80 */         ((NamingException)localObject).setResolvedObj(new CNCtx(paramCNCtx._orb, paramCNCtx.orbTracker, localNamingContext, paramCNCtx._env, paramCNCtx.makeFullName(arrayOfNameComponent2)));
/*     */       }
/*     */       else
/*     */       {
/*  84 */         ((NamingException)localObject).setResolvedObj(paramCNCtx);
/*     */       }
/*     */ 
/*  87 */       ((NamingException)localObject).setRemainingName(CNNameParser.cosNameToName(arrayOfNameComponent1));
/*     */     }
/*  89 */     else if ((paramException instanceof InvalidName)) {
/*  90 */       localObject = new InvalidNameException();
/*  91 */     } else if ((paramException instanceof AlreadyBound)) {
/*  92 */       localObject = new NameAlreadyBoundException();
/*  93 */     } else if ((paramException instanceof NotEmpty)) {
/*  94 */       localObject = new ContextNotEmptyException();
/*     */     } else {
/*  96 */       localObject = new NamingException("Unknown reasons");
/*     */     }
/*     */ 
/*  99 */     ((NamingException)localObject).setRootCause(paramException);
/* 100 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static final NamingException tryFed(NotFound paramNotFound, CNCtx paramCNCtx, NameComponent[] paramArrayOfNameComponent) throws NamingException
/*     */   {
/* 105 */     Object localObject1 = paramNotFound.rest_of_name;
/*     */ 
/* 116 */     if ((localObject1.length == 1) && (paramArrayOfNameComponent != null))
/*     */     {
/* 118 */       localObject2 = paramArrayOfNameComponent[(paramArrayOfNameComponent.length - 1)];
/* 119 */       if ((!localObject1[0].id.equals(((NameComponent)localObject2).id)) || (localObject1[0].kind == null) || (!localObject1[0].kind.equals(((NameComponent)localObject2).kind)))
/*     */       {
/* 126 */         NameNotFoundException localNameNotFoundException = new NameNotFoundException();
/* 127 */         localNameNotFoundException.setRemainingName(CNNameParser.cosNameToName((NameComponent[])localObject1));
/* 128 */         localNameNotFoundException.setRootCause(paramNotFound);
/* 129 */         throw localNameNotFoundException;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 135 */     Object localObject2 = null;
/* 136 */     int i = 0;
/* 137 */     if ((paramArrayOfNameComponent != null) && (paramArrayOfNameComponent.length >= localObject1.length))
/*     */     {
/* 139 */       if (paramNotFound.why == NotFoundReason.not_context)
/*     */       {
/* 142 */         i = paramArrayOfNameComponent.length - (localObject1.length - 1);
/*     */ 
/* 145 */         if (localObject1.length == 1)
/*     */         {
/* 147 */           localObject1 = null;
/*     */         } else {
/* 149 */           localObject3 = new NameComponent[localObject1.length - 1];
/* 150 */           System.arraycopy(localObject1, 1, localObject3, 0, localObject3.length);
/* 151 */           localObject1 = localObject3;
/*     */         }
/*     */       } else {
/* 154 */         i = paramArrayOfNameComponent.length - localObject1.length;
/*     */       }
/*     */ 
/* 157 */       if (i > 0) {
/* 158 */         localObject2 = new NameComponent[i];
/* 159 */         System.arraycopy(paramArrayOfNameComponent, 0, localObject2, 0, i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 164 */     Object localObject3 = new CannotProceedException();
/* 165 */     ((CannotProceedException)localObject3).setRootCause(paramNotFound);
/* 166 */     if ((localObject1 != null) && (localObject1.length > 0)) {
/* 167 */       ((CannotProceedException)localObject3).setRemainingName(CNNameParser.cosNameToName((NameComponent[])localObject1));
/*     */     }
/* 169 */     ((CannotProceedException)localObject3).setEnvironment(paramCNCtx._env);
/*     */ 
/* 176 */     final CNCtx localCNCtx = localObject2 != null ? paramCNCtx.callResolve((NameComponent[])localObject2) : paramCNCtx;
/*     */     Object localObject5;
/* 179 */     if ((localCNCtx instanceof Context))
/*     */     {
/* 183 */       localObject4 = new RefAddr("nns") { private static final long serialVersionUID = 669984699392133792L;
/*     */ 
/* 185 */         public Object getContent() { return localCNCtx; }
/*     */ 
/*     */       };
/* 190 */       localObject5 = new Reference("java.lang.Object", (RefAddr)localObject4);
/*     */ 
/* 193 */       CompositeName localCompositeName = new CompositeName();
/* 194 */       localCompositeName.add("");
/*     */ 
/* 196 */       ((CannotProceedException)localObject3).setResolvedObj(localObject5);
/* 197 */       ((CannotProceedException)localObject3).setAltName(localCompositeName);
/* 198 */       ((CannotProceedException)localObject3).setAltNameCtx((Context)localCNCtx);
/*     */ 
/* 200 */       return localObject3;
/*     */     }
/*     */ 
/* 204 */     Object localObject4 = CNNameParser.cosNameToName((NameComponent[])localObject2);
/*     */     Object localObject7;
/*     */     try {
/* 207 */       localObject5 = NamingManager.getObjectInstance(localCNCtx, (Name)localObject4, paramCNCtx, paramCNCtx._env);
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 210 */       throw localNamingException;
/*     */     } catch (Exception localException) {
/* 212 */       localObject7 = new NamingException("problem generating object using object factory");
/*     */ 
/* 214 */       ((NamingException)localObject7).setRootCause(localException);
/* 215 */       throw ((Throwable)localObject7);
/*     */     }
/*     */ 
/* 219 */     if ((localObject5 instanceof Context)) {
/* 220 */       ((CannotProceedException)localObject3).setResolvedObj(localObject5);
/*     */     }
/*     */     else {
/* 223 */       ((Name)localObject4).add("");
/* 224 */       ((CannotProceedException)localObject3).setAltName((Name)localObject4);
/*     */ 
/* 227 */       final Object localObject6 = localObject5;
/* 228 */       localObject7 = new RefAddr("nns") { private static final long serialVersionUID = -785132553978269772L;
/*     */ 
/* 230 */         public Object getContent() { return localObject6; }
/*     */ 
/*     */       };
/* 235 */       Reference localReference = new Reference("java.lang.Object", (RefAddr)localObject7);
/* 236 */       ((CannotProceedException)localObject3).setResolvedObj(localReference);
/* 237 */       ((CannotProceedException)localObject3).setAltNameCtx(paramCNCtx);
/*     */     }
/* 239 */     return localObject3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.ExceptionMapper
 * JD-Core Version:    0.6.2
 */