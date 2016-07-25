/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ class SubjectCodeSource extends CodeSource
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6039418085604715275L;
/*  50 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  54 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private Subject subject;
/*     */   private LinkedList<PolicyParser.PrincipalEntry> principals;
/*  61 */   private static final Class[] PARAMS = { String.class };
/*  62 */   private static final Debug debug = Debug.getInstance("auth", "\t[Auth Access]");
/*     */   private ClassLoader sysClassLoader;
/*     */ 
/*     */   SubjectCodeSource(Subject paramSubject, LinkedList<PolicyParser.PrincipalEntry> paramLinkedList, URL paramURL, Certificate[] paramArrayOfCertificate)
/*     */   {
/*  93 */     super(paramURL, paramArrayOfCertificate);
/*  94 */     this.subject = paramSubject;
/*  95 */     this.principals = (paramLinkedList == null ? new LinkedList() : new LinkedList(paramLinkedList));
/*     */ 
/*  98 */     this.sysClassLoader = ((ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 101 */         return ClassLoader.getSystemClassLoader();
/*     */       }
/*     */     }));
/*     */   }
/*     */ 
/*     */   LinkedList<PolicyParser.PrincipalEntry> getPrincipals()
/*     */   {
/* 118 */     return this.principals;
/*     */   }
/*     */ 
/*     */   Subject getSubject()
/*     */   {
/* 133 */     return this.subject;
/*     */   }
/*     */ 
/*     */   public boolean implies(CodeSource paramCodeSource)
/*     */   {
/* 170 */     LinkedList localLinkedList = null;
/*     */ 
/* 172 */     if ((paramCodeSource == null) || (!(paramCodeSource instanceof SubjectCodeSource)) || (!super.implies(paramCodeSource)))
/*     */     {
/* 176 */       if (debug != null)
/* 177 */         debug.println("\tSubjectCodeSource.implies: FAILURE 1");
/* 178 */       return false;
/*     */     }
/*     */ 
/* 181 */     SubjectCodeSource localSubjectCodeSource = (SubjectCodeSource)paramCodeSource;
/*     */ 
/* 187 */     if (this.principals == null) {
/* 188 */       if (debug != null)
/* 189 */         debug.println("\tSubjectCodeSource.implies: PASS 1");
/* 190 */       return true;
/*     */     }
/*     */ 
/* 193 */     if ((localSubjectCodeSource.getSubject() == null) || (localSubjectCodeSource.getSubject().getPrincipals().size() == 0))
/*     */     {
/* 195 */       if (debug != null)
/* 196 */         debug.println("\tSubjectCodeSource.implies: FAILURE 2");
/* 197 */       return false;
/*     */     }
/*     */ 
/* 200 */     ListIterator localListIterator = this.principals.listIterator(0);
/*     */ 
/* 202 */     while (localListIterator.hasNext()) {
/* 203 */       PolicyParser.PrincipalEntry localPrincipalEntry1 = (PolicyParser.PrincipalEntry)localListIterator.next();
/*     */       try
/*     */       {
/* 208 */         Class localClass = Class.forName(localPrincipalEntry1.principalClass, true, this.sysClassLoader);
/*     */ 
/* 211 */         localObject1 = localClass.getConstructor(PARAMS);
/* 212 */         localObject2 = (PrincipalComparator)((Constructor)localObject1).newInstance(new Object[] { localPrincipalEntry1.principalName });
/*     */ 
/* 216 */         if (!((PrincipalComparator)localObject2).implies(localSubjectCodeSource.getSubject())) {
/* 217 */           if (debug != null)
/* 218 */             debug.println("\tSubjectCodeSource.implies: FAILURE 3");
/* 219 */           return false;
/*     */         }
/* 221 */         if (debug != null)
/* 222 */           debug.println("\tSubjectCodeSource.implies: PASS 2");
/* 223 */         return true;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */         Object localObject1;
/*     */         Object localObject2;
/* 229 */         if (localLinkedList == null)
/*     */         {
/* 231 */           if (localSubjectCodeSource.getSubject() == null) {
/* 232 */             if (debug != null) {
/* 233 */               debug.println("\tSubjectCodeSource.implies: FAILURE 4");
/*     */             }
/* 235 */             return false;
/*     */           }
/* 237 */           localObject1 = localSubjectCodeSource.getSubject().getPrincipals().iterator();
/*     */ 
/* 240 */           localLinkedList = new LinkedList();
/* 241 */           while (((Iterator)localObject1).hasNext()) {
/* 242 */             localObject2 = (Principal)((Iterator)localObject1).next();
/* 243 */             PolicyParser.PrincipalEntry localPrincipalEntry2 = new PolicyParser.PrincipalEntry(localObject2.getClass().getName(), ((Principal)localObject2).getName());
/*     */ 
/* 246 */             localLinkedList.add(localPrincipalEntry2);
/*     */           }
/*     */         }
/*     */ 
/* 250 */         if (!subjectListImpliesPrincipalEntry(localLinkedList, localPrincipalEntry1)) {
/* 251 */           if (debug != null)
/* 252 */             debug.println("\tSubjectCodeSource.implies: FAILURE 5");
/* 253 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 258 */     if (debug != null)
/* 259 */       debug.println("\tSubjectCodeSource.implies: PASS 3");
/* 260 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean subjectListImpliesPrincipalEntry(LinkedList<PolicyParser.PrincipalEntry> paramLinkedList, PolicyParser.PrincipalEntry paramPrincipalEntry)
/*     */   {
/* 288 */     ListIterator localListIterator = paramLinkedList.listIterator(0);
/*     */ 
/* 290 */     while (localListIterator.hasNext()) {
/* 291 */       PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)localListIterator.next();
/*     */ 
/* 293 */       if ((paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) || (paramPrincipalEntry.principalClass.equals(localPrincipalEntry.principalClass)))
/*     */       {
/* 298 */         if ((paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")) || (paramPrincipalEntry.principalName.equals(localPrincipalEntry.principalName)))
/*     */         {
/* 302 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 326 */     if (paramObject == this) {
/* 327 */       return true;
/*     */     }
/* 329 */     if (!super.equals(paramObject)) {
/* 330 */       return false;
/*     */     }
/* 332 */     if (!(paramObject instanceof SubjectCodeSource)) {
/* 333 */       return false;
/*     */     }
/* 335 */     SubjectCodeSource localSubjectCodeSource = (SubjectCodeSource)paramObject;
/*     */     try
/*     */     {
/* 339 */       if (getSubject() != localSubjectCodeSource.getSubject())
/* 340 */         return false;
/*     */     } catch (SecurityException localSecurityException) {
/* 342 */       return false;
/*     */     }
/*     */ 
/* 345 */     if (((this.principals == null) && (localSubjectCodeSource.principals != null)) || ((this.principals != null) && (localSubjectCodeSource.principals == null)))
/*     */     {
/* 347 */       return false;
/*     */     }
/* 349 */     if ((this.principals != null) && (localSubjectCodeSource.principals != null) && (
/* 350 */       (!this.principals.containsAll(localSubjectCodeSource.principals)) || (!localSubjectCodeSource.principals.containsAll(this.principals))))
/*     */     {
/* 353 */       return false;
/*     */     }
/*     */ 
/* 356 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 367 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 378 */     String str = super.toString();
/*     */     Object localObject;
/* 379 */     if (getSubject() != null) {
/* 380 */       if (debug != null) {
/* 381 */         localObject = getSubject();
/* 382 */         str = str + "\n" + (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public String run()
/*     */           {
/* 386 */             return this.val$finalSubject.toString();
/*     */           } } );
/*     */       }
/*     */       else {
/* 390 */         str = str + "\n" + getSubject().toString();
/*     */       }
/*     */     }
/* 393 */     if (this.principals != null) {
/* 394 */       localObject = this.principals.listIterator();
/*     */ 
/* 396 */       while (((ListIterator)localObject).hasNext()) {
/* 397 */         PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)((ListIterator)localObject).next();
/* 398 */         str = str + rb.getString("NEWLINE") + localPrincipalEntry.principalClass + " " + localPrincipalEntry.principalName;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 403 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.SubjectCodeSource
 * JD-Core Version:    0.6.2
 */