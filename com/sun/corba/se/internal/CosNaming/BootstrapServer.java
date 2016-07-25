/*     */ package com.sun.corba.se.internal.CosNaming;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*     */ import com.sun.corba.se.spi.resolver.Resolver;
/*     */ import com.sun.corba.se.spi.resolver.ResolverDefault;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.ORBPackage.InvalidName;
/*     */ 
/*     */ public class BootstrapServer
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*     */ 
/*     */   public static final void main(String[] paramArrayOfString)
/*     */   {
/*  63 */     String str = null;
/*  64 */     int i = 900;
/*     */ 
/*  67 */     for (int j = 0; j < paramArrayOfString.length; j++)
/*     */     {
/*  69 */       if ((paramArrayOfString[j].equals("-InitialServicesFile")) && (j < paramArrayOfString.length - 1)) {
/*  70 */         str = paramArrayOfString[(j + 1)];
/*     */       }
/*     */ 
/*  78 */       if ((paramArrayOfString[j].equals("-ORBInitialPort")) && (j < paramArrayOfString.length - 1)) {
/*  79 */         i = Integer.parseInt(paramArrayOfString[(j + 1)]);
/*     */       }
/*     */     }
/*     */ 
/*  83 */     if (str == null) {
/*  84 */       System.out.println(CorbaResourceUtil.getText("bootstrap.usage", "BootstrapServer"));
/*     */ 
/*  86 */       return;
/*     */     }
/*     */ 
/*  90 */     File localFile = new File(str);
/*     */ 
/*  93 */     if ((localFile.exists() == true) && (!localFile.canRead())) {
/*  94 */       System.err.println(CorbaResourceUtil.getText("bootstrap.filenotreadable", localFile.getAbsolutePath()));
/*     */ 
/*  96 */       return;
/*     */     }
/*     */ 
/* 100 */     System.out.println(CorbaResourceUtil.getText("bootstrap.success", Integer.toString(i), localFile.getAbsolutePath()));
/*     */ 
/* 104 */     Properties localProperties = new Properties();
/*     */ 
/* 110 */     localProperties.put("com.sun.CORBA.ORBServerPort", Integer.toString(i));
/*     */ 
/* 113 */     com.sun.corba.se.spi.orb.ORB localORB = (com.sun.corba.se.spi.orb.ORB)org.omg.CORBA.ORB.init(paramArrayOfString, localProperties);
/*     */ 
/* 115 */     LocalResolver localLocalResolver1 = localORB.getLocalResolver();
/* 116 */     Resolver localResolver1 = ResolverDefault.makeFileResolver(localORB, localFile);
/* 117 */     Resolver localResolver2 = ResolverDefault.makeCompositeResolver(localResolver1, localLocalResolver1);
/* 118 */     LocalResolver localLocalResolver2 = ResolverDefault.makeSplitLocalResolver(localResolver2, localLocalResolver1);
/*     */ 
/* 120 */     localORB.setLocalResolver(localLocalResolver2);
/*     */     try
/*     */     {
/* 124 */       localORB.resolve_initial_references("RootPOA");
/*     */     } catch (InvalidName localInvalidName) {
/* 126 */       RuntimeException localRuntimeException = new RuntimeException("This should not happen");
/* 127 */       localRuntimeException.initCause(localInvalidName);
/* 128 */       throw localRuntimeException;
/*     */     }
/*     */ 
/* 131 */     localORB.run();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.internal.CosNaming.BootstrapServer
 * JD-Core Version:    0.6.2
 */