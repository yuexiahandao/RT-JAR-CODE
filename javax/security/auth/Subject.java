/*      */ package javax.security.auth;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.DomainCombiner;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Set;
/*      */ import sun.security.util.ResourcesMgr;
/*      */ 
/*      */ public final class Subject
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -8308522755600156056L;
/*      */   Set<Principal> principals;
/*      */   transient Set<Object> pubCredentials;
/*      */   transient Set<Object> privCredentials;
/*  128 */   private volatile boolean readOnly = false;
/*      */   private static final int PRINCIPAL_SET = 1;
/*      */   private static final int PUB_CREDENTIAL_SET = 2;
/*      */   private static final int PRIV_CREDENTIAL_SET = 3;
/*  134 */   private static final ProtectionDomain[] NULL_PD_ARRAY = new ProtectionDomain[0];
/*      */ 
/*      */   public Subject()
/*      */   {
/*  156 */     this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
/*      */ 
/*  158 */     this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
/*      */ 
/*  160 */     this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
/*      */   }
/*      */ 
/*      */   public Subject(boolean paramBoolean, Set<? extends Principal> paramSet, Set<?> paramSet1, Set<?> paramSet2)
/*      */   {
/*  203 */     if ((paramSet == null) || (paramSet1 == null) || (paramSet2 == null))
/*      */     {
/*  206 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
/*      */     }
/*      */ 
/*  209 */     this.principals = Collections.synchronizedSet(new SecureSet(this, 1, paramSet));
/*      */ 
/*  211 */     this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2, paramSet1));
/*      */ 
/*  213 */     this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3, paramSet2));
/*      */ 
/*  215 */     this.readOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setReadOnly()
/*      */   {
/*  239 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  240 */     if (localSecurityManager != null) {
/*  241 */       localSecurityManager.checkPermission(AuthPermissionHolder.SET_READ_ONLY_PERMISSION);
/*      */     }
/*      */ 
/*  244 */     this.readOnly = true;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly()
/*      */   {
/*  255 */     return this.readOnly;
/*      */   }
/*      */ 
/*      */   public static Subject getSubject(AccessControlContext paramAccessControlContext)
/*      */   {
/*  285 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  286 */     if (localSecurityManager != null) {
/*  287 */       localSecurityManager.checkPermission(AuthPermissionHolder.GET_SUBJECT_PERMISSION);
/*      */     }
/*      */ 
/*  290 */     if (paramAccessControlContext == null) {
/*  291 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.AccessControlContext.provided"));
/*      */     }
/*      */ 
/*  296 */     return (Subject)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Subject run() {
/*  299 */         DomainCombiner localDomainCombiner = this.val$acc.getDomainCombiner();
/*  300 */         if (!(localDomainCombiner instanceof SubjectDomainCombiner))
/*  301 */           return null;
/*  302 */         SubjectDomainCombiner localSubjectDomainCombiner = (SubjectDomainCombiner)localDomainCombiner;
/*  303 */         return localSubjectDomainCombiner.getSubject();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static <T> T doAs(Subject paramSubject, PrivilegedAction<T> paramPrivilegedAction)
/*      */   {
/*  343 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  344 */     if (localSecurityManager != null) {
/*  345 */       localSecurityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
/*      */     }
/*  347 */     if (paramPrivilegedAction == null) {
/*  348 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
/*      */     }
/*      */ 
/*  353 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/*      */ 
/*  356 */     return AccessController.doPrivileged(paramPrivilegedAction, createContext(paramSubject, localAccessControlContext));
/*      */   }
/*      */ 
/*      */   public static <T> T doAs(Subject paramSubject, PrivilegedExceptionAction<T> paramPrivilegedExceptionAction)
/*      */     throws PrivilegedActionException
/*      */   {
/*  402 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  403 */     if (localSecurityManager != null) {
/*  404 */       localSecurityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
/*      */     }
/*      */ 
/*  407 */     if (paramPrivilegedExceptionAction == null) {
/*  408 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
/*      */     }
/*      */ 
/*  412 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/*      */ 
/*  415 */     return AccessController.doPrivileged(paramPrivilegedExceptionAction, createContext(paramSubject, localAccessControlContext));
/*      */   }
/*      */ 
/*      */   public static <T> T doAsPrivileged(Subject paramSubject, PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext)
/*      */   {
/*  456 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  457 */     if (localSecurityManager != null) {
/*  458 */       localSecurityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
/*      */     }
/*      */ 
/*  461 */     if (paramPrivilegedAction == null) {
/*  462 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
/*      */     }
/*      */ 
/*  467 */     AccessControlContext localAccessControlContext = paramAccessControlContext == null ? new AccessControlContext(NULL_PD_ARRAY) : paramAccessControlContext;
/*      */ 
/*  473 */     return AccessController.doPrivileged(paramPrivilegedAction, createContext(paramSubject, localAccessControlContext));
/*      */   }
/*      */ 
/*      */   public static <T> T doAsPrivileged(Subject paramSubject, PrivilegedExceptionAction<T> paramPrivilegedExceptionAction, AccessControlContext paramAccessControlContext)
/*      */     throws PrivilegedActionException
/*      */   {
/*  520 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  521 */     if (localSecurityManager != null) {
/*  522 */       localSecurityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
/*      */     }
/*      */ 
/*  525 */     if (paramPrivilegedExceptionAction == null) {
/*  526 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
/*      */     }
/*      */ 
/*  530 */     AccessControlContext localAccessControlContext = paramAccessControlContext == null ? new AccessControlContext(NULL_PD_ARRAY) : paramAccessControlContext;
/*      */ 
/*  536 */     return AccessController.doPrivileged(paramPrivilegedExceptionAction, createContext(paramSubject, localAccessControlContext));
/*      */   }
/*      */ 
/*      */   private static AccessControlContext createContext(Subject paramSubject, final AccessControlContext paramAccessControlContext)
/*      */   {
/*  545 */     return (AccessControlContext)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public AccessControlContext run() {
/*  548 */         if (this.val$subject == null) {
/*  549 */           return new AccessControlContext(paramAccessControlContext, null);
/*      */         }
/*  551 */         return new AccessControlContext(paramAccessControlContext, new SubjectDomainCombiner(this.val$subject));
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public Set<Principal> getPrincipals()
/*      */   {
/*  577 */     return this.principals;
/*      */   }
/*      */ 
/*      */   public <T extends Principal> Set<T> getPrincipals(Class<T> paramClass)
/*      */   {
/*  604 */     if (paramClass == null) {
/*  605 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
/*      */     }
/*      */ 
/*  610 */     return new ClassSet(1, paramClass);
/*      */   }
/*      */ 
/*      */   public Set<Object> getPublicCredentials()
/*      */   {
/*  631 */     return this.pubCredentials;
/*      */   }
/*      */ 
/*      */   public Set<Object> getPrivateCredentials()
/*      */   {
/*  671 */     return this.privCredentials;
/*      */   }
/*      */ 
/*      */   public <T> Set<T> getPublicCredentials(Class<T> paramClass)
/*      */   {
/*  698 */     if (paramClass == null) {
/*  699 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
/*      */     }
/*      */ 
/*  704 */     return new ClassSet(2, paramClass);
/*      */   }
/*      */ 
/*      */   public <T> Set<T> getPrivateCredentials(Class<T> paramClass)
/*      */   {
/*  743 */     if (paramClass == null) {
/*  744 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
/*      */     }
/*      */ 
/*  749 */     return new ClassSet(3, paramClass);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  775 */     if (paramObject == null) {
/*  776 */       return false;
/*      */     }
/*  778 */     if (this == paramObject) {
/*  779 */       return true;
/*      */     }
/*  781 */     if ((paramObject instanceof Subject))
/*      */     {
/*  783 */       Subject localSubject = (Subject)paramObject;
/*      */       HashSet localHashSet;
/*  787 */       synchronized (localSubject.principals)
/*      */       {
/*  789 */         localHashSet = new HashSet(localSubject.principals);
/*      */       }
/*  791 */       if (!this.principals.equals(localHashSet)) {
/*  792 */         return false;
/*      */       }
/*      */ 
/*  796 */       synchronized (localSubject.pubCredentials)
/*      */       {
/*  798 */         ??? = new HashSet(localSubject.pubCredentials);
/*      */       }
/*  800 */       if (!this.pubCredentials.equals(???)) {
/*  801 */         return false;
/*      */       }
/*      */ 
/*  805 */       synchronized (localSubject.privCredentials)
/*      */       {
/*  807 */         ??? = new HashSet(localSubject.privCredentials);
/*      */       }
/*  809 */       if (!this.privCredentials.equals(???)) {
/*  810 */         return false;
/*      */       }
/*  812 */       return true;
/*      */     }
/*  814 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  825 */     return toString(true);
/*      */   }
/*      */ 
/*      */   String toString(boolean paramBoolean)
/*      */   {
/*  835 */     String str1 = ResourcesMgr.getString("Subject.");
/*  836 */     String str2 = "";
/*      */     Iterator localIterator;
/*      */     Object localObject1;
/*  838 */     synchronized (this.principals) {
/*  839 */       localIterator = this.principals.iterator();
/*  840 */       while (localIterator.hasNext()) {
/*  841 */         localObject1 = (Principal)localIterator.next();
/*  842 */         str2 = str2 + ResourcesMgr.getString(".Principal.") + ((Principal)localObject1).toString() + ResourcesMgr.getString("NEWLINE");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  847 */     synchronized (this.pubCredentials) {
/*  848 */       localIterator = this.pubCredentials.iterator();
/*  849 */       while (localIterator.hasNext()) {
/*  850 */         localObject1 = localIterator.next();
/*  851 */         str2 = str2 + ResourcesMgr.getString(".Public.Credential.") + localObject1.toString() + ResourcesMgr.getString("NEWLINE");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  857 */     if (paramBoolean) {
/*  858 */       synchronized (this.privCredentials) {
/*  859 */         localIterator = this.privCredentials.iterator();
/*      */         while (true) if (localIterator.hasNext()) {
/*      */             try {
/*  862 */               localObject1 = localIterator.next();
/*  863 */               str2 = str2 + ResourcesMgr.getString(".Private.Credential.") + localObject1.toString() + ResourcesMgr.getString("NEWLINE");
/*      */             }
/*      */             catch (SecurityException localSecurityException)
/*      */             {
/*  868 */               str2 = str2 + ResourcesMgr.getString(".Private.Credential.inaccessible.");
/*      */             }
/*      */           }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  875 */     return str1 + str2;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  901 */     int i = 0;
/*      */     Iterator localIterator;
/*  903 */     synchronized (this.principals) {
/*  904 */       localIterator = this.principals.iterator();
/*  905 */       while (localIterator.hasNext()) {
/*  906 */         Principal localPrincipal = (Principal)localIterator.next();
/*  907 */         i ^= localPrincipal.hashCode();
/*      */       }
/*      */     }
/*      */ 
/*  911 */     synchronized (this.pubCredentials) {
/*  912 */       localIterator = this.pubCredentials.iterator();
/*  913 */       while (localIterator.hasNext()) {
/*  914 */         i ^= getCredHashCode(localIterator.next());
/*      */       }
/*      */     }
/*  917 */     return i;
/*      */   }
/*      */ 
/*      */   private int getCredHashCode(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/*  925 */       return paramObject.hashCode(); } catch (IllegalStateException localIllegalStateException) {
/*      */     }
/*  927 */     return paramObject.getClass().toString().hashCode();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  936 */     synchronized (this.principals) {
/*  937 */       paramObjectOutputStream.defaultWriteObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  948 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*      */ 
/*  950 */     this.readOnly = localGetField.get("readOnly", false);
/*      */ 
/*  952 */     Set localSet = (Set)localGetField.get("principals", null);
/*      */ 
/*  955 */     if (localSet == null) {
/*  956 */       throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
/*      */     }
/*      */     try
/*      */     {
/*  960 */       this.principals = Collections.synchronizedSet(new SecureSet(this, 1, localSet));
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/*  965 */       this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
/*      */     }
/*      */ 
/*  971 */     this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
/*      */ 
/*  973 */     this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
/*      */   }
/*      */ 
/*      */   static class AuthPermissionHolder
/*      */   {
/* 1426 */     static final AuthPermission DO_AS_PERMISSION = new AuthPermission("doAs");
/*      */ 
/* 1429 */     static final AuthPermission DO_AS_PRIVILEGED_PERMISSION = new AuthPermission("doAsPrivileged");
/*      */ 
/* 1432 */     static final AuthPermission SET_READ_ONLY_PERMISSION = new AuthPermission("setReadOnly");
/*      */ 
/* 1435 */     static final AuthPermission GET_SUBJECT_PERMISSION = new AuthPermission("getSubject");
/*      */ 
/* 1438 */     static final AuthPermission MODIFY_PRINCIPALS_PERMISSION = new AuthPermission("modifyPrincipals");
/*      */ 
/* 1441 */     static final AuthPermission MODIFY_PUBLIC_CREDENTIALS_PERMISSION = new AuthPermission("modifyPublicCredentials");
/*      */ 
/* 1444 */     static final AuthPermission MODIFY_PRIVATE_CREDENTIALS_PERMISSION = new AuthPermission("modifyPrivateCredentials");
/*      */   }
/*      */ 
/*      */   private class ClassSet<T> extends AbstractSet<T>
/*      */   {
/*      */     private int which;
/*      */     private Class<T> c;
/*      */     private Set<T> set;
/*      */ 
/*      */     ClassSet(Class<T> arg2)
/*      */     {
/*      */       int i;
/* 1341 */       this.which = i;
/*      */       Object localObject1;
/* 1342 */       this.c = localObject1;
/* 1343 */       this.set = new HashSet();
/*      */ 
/* 1345 */       switch (i) {
/*      */       case 1:
/* 1347 */         synchronized (Subject.this.principals) { populateSet(); }
/* 1348 */         break;
/*      */       case 2:
/* 1350 */         synchronized (Subject.this.pubCredentials) { populateSet(); }
/* 1351 */         break;
/*      */       default:
/* 1353 */         synchronized (Subject.this.privCredentials) { populateSet(); }
/*      */ 
/*      */       }
/*      */     }
/*      */ 
/*      */     private void populateSet()
/*      */     {
/*      */       final Iterator localIterator;
/* 1360 */       switch (this.which) {
/*      */       case 1:
/* 1362 */         localIterator = Subject.this.principals.iterator();
/* 1363 */         break;
/*      */       case 2:
/* 1365 */         localIterator = Subject.this.pubCredentials.iterator();
/* 1366 */         break;
/*      */       default:
/* 1368 */         localIterator = Subject.this.privCredentials.iterator();
/*      */       }
/*      */ 
/* 1375 */       while (localIterator.hasNext())
/*      */       {
/*      */         Object localObject;
/* 1377 */         if (this.which == 3)
/* 1378 */           localObject = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/* 1381 */               return localIterator.next();
/*      */             }
/*      */           });
/*      */         else {
/* 1385 */           localObject = localIterator.next();
/*      */         }
/* 1387 */         if (this.c.isAssignableFrom(localObject.getClass()))
/* 1388 */           if (this.which != 3) {
/* 1389 */             this.set.add(localObject);
/*      */           }
/*      */           else {
/* 1392 */             SecurityManager localSecurityManager = System.getSecurityManager();
/* 1393 */             if (localSecurityManager != null) {
/* 1394 */               localSecurityManager.checkPermission(new PrivateCredentialPermission(localObject.getClass().getName(), Subject.this.getPrincipals()));
/*      */             }
/*      */ 
/* 1398 */             this.set.add(localObject);
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int size()
/*      */     {
/* 1405 */       return this.set.size();
/*      */     }
/*      */ 
/*      */     public Iterator<T> iterator() {
/* 1409 */       return this.set.iterator();
/*      */     }
/*      */ 
/*      */     public boolean add(T paramT)
/*      */     {
/* 1414 */       if (!paramT.getClass().isAssignableFrom(this.c)) {
/* 1415 */         MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.class"));
/*      */ 
/* 1417 */         Object[] arrayOfObject = { this.c.toString() };
/* 1418 */         throw new SecurityException(localMessageFormat.format(arrayOfObject));
/*      */       }
/*      */ 
/* 1421 */       return this.set.add(paramT);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SecureSet<E> extends AbstractSet<E>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7911754171111800359L;
/*  992 */     private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("this$0", Subject.class), new ObjectStreamField("elements", LinkedList.class), new ObjectStreamField("which", Integer.TYPE) };
/*      */     Subject subject;
/*      */     LinkedList<E> elements;
/*      */     private int which;
/*      */ 
/*      */     SecureSet(Subject paramSubject, int paramInt)
/*      */     {
/* 1014 */       this.subject = paramSubject;
/* 1015 */       this.which = paramInt;
/* 1016 */       this.elements = new LinkedList();
/*      */     }
/*      */ 
/*      */     SecureSet(Subject paramSubject, int paramInt, Set<? extends E> paramSet) {
/* 1020 */       this.subject = paramSubject;
/* 1021 */       this.which = paramInt;
/* 1022 */       this.elements = new LinkedList(paramSet);
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1026 */       return this.elements.size();
/*      */     }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 1030 */       final LinkedList localLinkedList = this.elements;
/* 1031 */       return new Iterator() {
/* 1032 */         ListIterator<E> i = localLinkedList.listIterator(0);
/*      */ 
/* 1034 */         public boolean hasNext() { return this.i.hasNext(); }
/*      */ 
/*      */         public E next() {
/* 1037 */           if (Subject.SecureSet.this.which != 3) {
/* 1038 */             return this.i.next();
/*      */           }
/*      */ 
/* 1041 */           SecurityManager localSecurityManager = System.getSecurityManager();
/* 1042 */           if (localSecurityManager != null) {
/*      */             try {
/* 1044 */               localSecurityManager.checkPermission(new PrivateCredentialPermission(localLinkedList.get(this.i.nextIndex()).getClass().getName(), Subject.SecureSet.this.subject.getPrincipals()));
/*      */             }
/*      */             catch (SecurityException localSecurityException)
/*      */             {
/* 1048 */               this.i.next();
/* 1049 */               throw localSecurityException;
/*      */             }
/*      */           }
/* 1052 */           return this.i.next();
/*      */         }
/*      */ 
/*      */         public void remove()
/*      */         {
/* 1057 */           if (Subject.SecureSet.this.subject.isReadOnly()) {
/* 1058 */             throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
/*      */           }
/*      */ 
/* 1062 */           SecurityManager localSecurityManager = System.getSecurityManager();
/* 1063 */           if (localSecurityManager != null) {
/* 1064 */             switch (Subject.SecureSet.this.which) {
/*      */             case 1:
/* 1066 */               localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
/* 1067 */               break;
/*      */             case 2:
/* 1069 */               localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
/* 1070 */               break;
/*      */             default:
/* 1072 */               localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
/*      */             }
/*      */           }
/*      */ 
/* 1076 */           this.i.remove();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public boolean add(E paramE)
/*      */     {
/* 1083 */       if (this.subject.isReadOnly()) {
/* 1084 */         throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
/*      */       }
/*      */ 
/* 1088 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1089 */       if (localSecurityManager != null) {
/* 1090 */         switch (this.which) {
/*      */         case 1:
/* 1092 */           localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
/* 1093 */           break;
/*      */         case 2:
/* 1095 */           localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
/* 1096 */           break;
/*      */         default:
/* 1098 */           localSecurityManager.checkPermission(Subject.AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1103 */       switch (this.which) {
/*      */       case 1:
/* 1105 */         if (!(paramE instanceof Principal)) {
/* 1106 */           throw new SecurityException(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.java.security.Principal.to.a.Subject.s.Principal.Set"));
/*      */         }
/*      */ 
/*      */         break;
/*      */       }
/*      */ 
/* 1116 */       if (!this.elements.contains(paramE)) {
/* 1117 */         return this.elements.add(paramE);
/*      */       }
/* 1119 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject)
/*      */     {
/* 1124 */       final Iterator localIterator = iterator();
/* 1125 */       while (localIterator.hasNext())
/*      */       {
/*      */         Object localObject;
/* 1127 */         if (this.which != 3)
/* 1128 */           localObject = localIterator.next();
/*      */         else {
/* 1130 */           localObject = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public E run() {
/* 1133 */               return localIterator.next();
/*      */             }
/*      */           });
/*      */         }
/*      */ 
/* 1138 */         if (localObject == null) {
/* 1139 */           if (paramObject == null) {
/* 1140 */             localIterator.remove();
/* 1141 */             return true;
/*      */           }
/* 1143 */         } else if (localObject.equals(paramObject)) {
/* 1144 */           localIterator.remove();
/* 1145 */           return true;
/*      */         }
/*      */       }
/* 1148 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 1152 */       final Iterator localIterator = iterator();
/* 1153 */       while (localIterator.hasNext())
/*      */       {
/*      */         Object localObject;
/* 1155 */         if (this.which != 3) {
/* 1156 */           localObject = localIterator.next();
/*      */         }
/*      */         else
/*      */         {
/* 1165 */           SecurityManager localSecurityManager = System.getSecurityManager();
/* 1166 */           if (localSecurityManager != null) {
/* 1167 */             localSecurityManager.checkPermission(new PrivateCredentialPermission(paramObject.getClass().getName(), this.subject.getPrincipals()));
/*      */           }
/*      */ 
/* 1171 */           localObject = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public E run() {
/* 1174 */               return localIterator.next();
/*      */             }
/*      */           });
/*      */         }
/*      */ 
/* 1179 */         if (localObject == null) {
/* 1180 */           if (paramObject == null)
/* 1181 */             return true;
/*      */         }
/* 1183 */         else if (localObject.equals(paramObject)) {
/* 1184 */           return true;
/*      */         }
/*      */       }
/* 1187 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean removeAll(Collection<?> paramCollection)
/*      */     {
/* 1192 */       boolean bool = false;
/* 1193 */       final Iterator localIterator1 = iterator();
/* 1194 */       while (localIterator1.hasNext())
/*      */       {
/*      */         Object localObject1;
/* 1196 */         if (this.which != 3)
/* 1197 */           localObject1 = localIterator1.next();
/*      */         else {
/* 1199 */           localObject1 = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public E run() {
/* 1202 */               return localIterator1.next();
/*      */             }
/*      */           });
/*      */         }
/*      */ 
/* 1207 */         Iterator localIterator2 = paramCollection.iterator();
/* 1208 */         while (localIterator2.hasNext()) {
/* 1209 */           Object localObject2 = localIterator2.next();
/* 1210 */           if (localObject1 == null) {
/* 1211 */             if (localObject2 == null) {
/* 1212 */               localIterator1.remove();
/* 1213 */               bool = true;
/* 1214 */               break;
/*      */             }
/* 1216 */           } else if (localObject1.equals(localObject2)) {
/* 1217 */             localIterator1.remove();
/* 1218 */             bool = true;
/* 1219 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1223 */       return bool;
/*      */     }
/*      */ 
/*      */     public boolean retainAll(Collection<?> paramCollection)
/*      */     {
/* 1228 */       boolean bool = false;
/* 1229 */       int i = 0;
/* 1230 */       final Iterator localIterator1 = iterator();
/* 1231 */       while (localIterator1.hasNext()) {
/* 1232 */         i = 0;
/*      */         Object localObject1;
/* 1234 */         if (this.which != 3)
/* 1235 */           localObject1 = localIterator1.next();
/*      */         else {
/* 1237 */           localObject1 = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public E run() {
/* 1240 */               return localIterator1.next();
/*      */             }
/*      */           });
/*      */         }
/*      */ 
/* 1245 */         Iterator localIterator2 = paramCollection.iterator();
/* 1246 */         while (localIterator2.hasNext()) {
/* 1247 */           Object localObject2 = localIterator2.next();
/* 1248 */           if (localObject1 == null) {
/* 1249 */             if (localObject2 == null) {
/* 1250 */               i = 1;
/* 1251 */               break;
/*      */             }
/* 1253 */           } else if (localObject1.equals(localObject2)) {
/* 1254 */             i = 1;
/* 1255 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1259 */         if (i == 0) {
/* 1260 */           localIterator1.remove();
/* 1261 */           i = 0;
/* 1262 */           bool = true;
/*      */         }
/*      */       }
/* 1265 */       return bool;
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1269 */       final Iterator localIterator = iterator();
/* 1270 */       while (localIterator.hasNext())
/*      */       {
/*      */         Object localObject;
/* 1272 */         if (this.which != 3)
/* 1273 */           localObject = localIterator.next();
/*      */         else {
/* 1275 */           localObject = AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public E run() {
/* 1278 */               return localIterator.next();
/*      */             }
/*      */           });
/*      */         }
/* 1282 */         localIterator.remove();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/* 1300 */       if (this.which == 3)
/*      */       {
/* 1302 */         localObject = iterator();
/* 1303 */         while (((Iterator)localObject).hasNext()) {
/* 1304 */           ((Iterator)localObject).next();
/*      */         }
/*      */       }
/* 1307 */       Object localObject = paramObjectOutputStream.putFields();
/* 1308 */       ((ObjectOutputStream.PutField)localObject).put("this$0", this.subject);
/* 1309 */       ((ObjectOutputStream.PutField)localObject).put("elements", this.elements);
/* 1310 */       ((ObjectOutputStream.PutField)localObject).put("which", this.which);
/* 1311 */       paramObjectOutputStream.writeFields();
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/* 1317 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1318 */       this.subject = ((Subject)localGetField.get("this$0", null));
/* 1319 */       this.which = localGetField.get("which", 0);
/*      */ 
/* 1321 */       LinkedList localLinkedList = (LinkedList)localGetField.get("elements", null);
/* 1322 */       if (localLinkedList.getClass() != LinkedList.class)
/* 1323 */         this.elements = new LinkedList(localLinkedList);
/*      */       else
/* 1325 */         this.elements = localLinkedList;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.Subject
 * JD-Core Version:    0.6.2
 */