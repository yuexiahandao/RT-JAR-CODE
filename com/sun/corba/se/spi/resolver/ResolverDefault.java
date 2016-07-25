/*     */ package com.sun.corba.se.spi.resolver;
/*     */ 
/*     */ import com.sun.corba.se.impl.resolver.BootstrapResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.CompositeResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.FileResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.INSURLOperationImpl;
/*     */ import com.sun.corba.se.impl.resolver.LocalResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.ORBDefaultInitRefResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.ORBInitRefResolverImpl;
/*     */ import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.Operation;
/*     */ import com.sun.corba.se.spi.orb.StringPair;
/*     */ import java.io.File;
/*     */ 
/*     */ public class ResolverDefault
/*     */ {
/*     */   public static LocalResolver makeLocalResolver()
/*     */   {
/*  51 */     return new LocalResolverImpl();
/*     */   }
/*     */ 
/*     */   public static Resolver makeORBInitRefResolver(Operation paramOperation, StringPair[] paramArrayOfStringPair)
/*     */   {
/*  59 */     return new ORBInitRefResolverImpl(paramOperation, paramArrayOfStringPair);
/*     */   }
/*     */ 
/*     */   public static Resolver makeORBDefaultInitRefResolver(Operation paramOperation, String paramString)
/*     */   {
/*  65 */     return new ORBDefaultInitRefResolverImpl(paramOperation, paramString);
/*     */   }
/*     */ 
/*     */   public static Resolver makeBootstrapResolver(ORB paramORB, String paramString, int paramInt)
/*     */   {
/*  75 */     return new BootstrapResolverImpl(paramORB, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public static Resolver makeCompositeResolver(Resolver paramResolver1, Resolver paramResolver2)
/*     */   {
/*  85 */     return new CompositeResolverImpl(paramResolver1, paramResolver2);
/*     */   }
/*     */ 
/*     */   public static Operation makeINSURLOperation(ORB paramORB, Resolver paramResolver)
/*     */   {
/*  90 */     return new INSURLOperationImpl(paramORB, paramResolver);
/*     */   }
/*     */ 
/*     */   public static LocalResolver makeSplitLocalResolver(Resolver paramResolver, LocalResolver paramLocalResolver)
/*     */   {
/*  97 */     return new SplitLocalResolverImpl(paramResolver, paramLocalResolver);
/*     */   }
/*     */ 
/*     */   public static Resolver makeFileResolver(ORB paramORB, File paramFile)
/*     */   {
/* 102 */     return new FileResolverImpl(paramORB, paramFile);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.resolver.ResolverDefault
 * JD-Core Version:    0.6.2
 */