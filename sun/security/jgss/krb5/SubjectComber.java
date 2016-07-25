/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.kerberos.KerberosKey;
/*     */ import javax.security.auth.kerberos.KerberosPrincipal;
/*     */ import javax.security.auth.kerberos.KerberosTicket;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ 
/*     */ class SubjectComber
/*     */ {
/*  48 */   private static final boolean DEBUG = Krb5Util.DEBUG;
/*     */ 
/*     */   static <T> T find(Subject paramSubject, String paramString1, String paramString2, Class<T> paramClass)
/*     */   {
/*  59 */     return findAux(paramSubject, paramString1, paramString2, paramClass, true);
/*     */   }
/*     */ 
/*     */   static <T> List<T> findMany(Subject paramSubject, String paramString1, String paramString2, Class<T> paramClass)
/*     */   {
/*  66 */     return (List)findAux(paramSubject, paramString1, paramString2, paramClass, false);
/*     */   }
/*     */ 
/*     */   private static <T> Object findAux(Subject paramSubject, String paramString1, String paramString2, Class<T> paramClass, boolean paramBoolean)
/*     */   {
/*  79 */     if (paramSubject == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     ArrayList localArrayList = paramBoolean ? null : new ArrayList();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  84 */     if (paramClass == KeyTab.class)
/*     */     {
/*  86 */       localObject1 = paramSubject.getPrivateCredentials(paramClass).iterator();
/*     */ 
/*  88 */       while (((Iterator)localObject1).hasNext()) {
/*  89 */         localObject2 = ((Iterator)localObject1).next();
/*  90 */         if (DEBUG) {
/*  91 */           System.out.println("Found " + paramClass.getSimpleName());
/*     */         }
/*  93 */         if (paramBoolean) {
/*  94 */           return localObject2;
/*     */         }
/*  96 */         localArrayList.add(localObject2);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject3;
/*  99 */       if (paramClass == KerberosKey.class)
/*     */       {
/* 101 */         localObject1 = paramSubject.getPrivateCredentials(paramClass).iterator();
/*     */ 
/* 103 */         while (((Iterator)localObject1).hasNext()) {
/* 104 */           localObject2 = ((Iterator)localObject1).next();
/* 105 */           localObject3 = ((KerberosKey)localObject2).getPrincipal().getName();
/* 106 */           if ((paramString1 == null) || (paramString1.equals(localObject3))) {
/* 107 */             if (DEBUG) {
/* 108 */               System.out.println("Found " + paramClass.getSimpleName() + " for " + (String)localObject3);
/*     */             }
/*     */ 
/* 111 */             if (paramBoolean) {
/* 112 */               return localObject2;
/*     */             }
/* 114 */             if (paramString1 == null)
/*     */             {
/* 117 */               paramString1 = (String)localObject3;
/*     */             }
/* 119 */             localArrayList.add(localObject2);
/*     */           }
/*     */         }
/*     */       }
/* 123 */       else if (paramClass == KerberosTicket.class)
/*     */       {
/* 126 */         localObject1 = paramSubject.getPrivateCredentials();
/* 127 */         synchronized (localObject1) {
/* 128 */           localObject3 = ((Set)localObject1).iterator();
/* 129 */           while (((Iterator)localObject3).hasNext()) {
/* 130 */             Object localObject4 = ((Iterator)localObject3).next();
/* 131 */             if ((localObject4 instanceof KerberosTicket)) {
/* 132 */               KerberosTicket localKerberosTicket = (KerberosTicket)localObject4;
/* 133 */               if (DEBUG) {
/* 134 */                 System.out.println("Found ticket for " + localKerberosTicket.getClient() + " to go to " + localKerberosTicket.getServer() + " expiring on " + localKerberosTicket.getEndTime());
/*     */               }
/*     */ 
/* 141 */               if (!localKerberosTicket.isCurrent())
/*     */               {
/* 145 */                 if (!paramSubject.isReadOnly()) {
/* 146 */                   ((Iterator)localObject3).remove();
/*     */                   try {
/* 148 */                     localKerberosTicket.destroy();
/* 149 */                     if (DEBUG) {
/* 150 */                       System.out.println("Removed and destroyed the expired Ticket \n" + localKerberosTicket);
/*     */                     }
/*     */ 
/*     */                   }
/*     */                   catch (DestroyFailedException localDestroyFailedException)
/*     */                   {
/* 156 */                     if (DEBUG) {
/* 157 */                       System.out.println("Expired ticket not detroyed successfully. " + localDestroyFailedException);
/*     */                     }
/*     */                   }
/*     */ 
/*     */                 }
/*     */ 
/*     */               }
/* 164 */               else if ((paramString1 == null) || (localKerberosTicket.getServer().getName().equals(paramString1)))
/*     */               {
/* 167 */                 if ((paramString2 == null) || (paramString2.equals(localKerberosTicket.getClient().getName())))
/*     */                 {
/* 170 */                   if (paramBoolean) {
/* 171 */                     return localKerberosTicket;
/*     */                   }
/*     */ 
/* 175 */                   if (paramString2 == null) {
/* 176 */                     paramString2 = localKerberosTicket.getClient().getName();
/*     */                   }
/*     */ 
/* 179 */                   if (paramString1 == null) {
/* 180 */                     paramString1 = localKerberosTicket.getServer().getName();
/*     */                   }
/*     */ 
/* 183 */                   localArrayList.add(localKerberosTicket);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 192 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.SubjectComber
 * JD-Core Version:    0.6.2
 */