/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ public class AuthCacheImpl
/*     */   implements AuthCache
/*     */ {
/*     */   HashMap hashtable;
/*     */ 
/*     */   public AuthCacheImpl()
/*     */   {
/*  44 */     this.hashtable = new HashMap();
/*     */   }
/*     */ 
/*     */   public void setMap(HashMap paramHashMap) {
/*  48 */     this.hashtable = paramHashMap;
/*     */   }
/*     */ 
/*     */   public synchronized void put(String paramString, AuthCacheValue paramAuthCacheValue)
/*     */   {
/*  55 */     LinkedList localLinkedList = (LinkedList)this.hashtable.get(paramString);
/*  56 */     String str = paramAuthCacheValue.getPath();
/*  57 */     if (localLinkedList == null) {
/*  58 */       localLinkedList = new LinkedList();
/*  59 */       this.hashtable.put(paramString, localLinkedList);
/*     */     }
/*     */ 
/*  62 */     ListIterator localListIterator = localLinkedList.listIterator();
/*  63 */     while (localListIterator.hasNext()) {
/*  64 */       AuthenticationInfo localAuthenticationInfo = (AuthenticationInfo)localListIterator.next();
/*  65 */       if ((localAuthenticationInfo.path == null) || (localAuthenticationInfo.path.startsWith(str))) {
/*  66 */         localListIterator.remove();
/*     */       }
/*     */     }
/*  69 */     localListIterator.add(paramAuthCacheValue);
/*     */   }
/*     */ 
/*     */   public synchronized AuthCacheValue get(String paramString1, String paramString2)
/*     */   {
/*  76 */     Object localObject = null;
/*  77 */     LinkedList localLinkedList = (LinkedList)this.hashtable.get(paramString1);
/*  78 */     if ((localLinkedList == null) || (localLinkedList.size() == 0)) {
/*  79 */       return null;
/*     */     }
/*  81 */     if (paramString2 == null)
/*     */     {
/*  83 */       return (AuthenticationInfo)localLinkedList.get(0);
/*     */     }
/*  85 */     ListIterator localListIterator = localLinkedList.listIterator();
/*  86 */     while (localListIterator.hasNext()) {
/*  87 */       AuthenticationInfo localAuthenticationInfo = (AuthenticationInfo)localListIterator.next();
/*  88 */       if (paramString2.startsWith(localAuthenticationInfo.path)) {
/*  89 */         return localAuthenticationInfo;
/*     */       }
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void remove(String paramString, AuthCacheValue paramAuthCacheValue) {
/*  96 */     LinkedList localLinkedList = (LinkedList)this.hashtable.get(paramString);
/*  97 */     if (localLinkedList == null) {
/*  98 */       return;
/*     */     }
/* 100 */     if (paramAuthCacheValue == null) {
/* 101 */       localLinkedList.clear();
/* 102 */       return;
/*     */     }
/* 104 */     ListIterator localListIterator = localLinkedList.listIterator();
/* 105 */     while (localListIterator.hasNext()) {
/* 106 */       AuthenticationInfo localAuthenticationInfo = (AuthenticationInfo)localListIterator.next();
/* 107 */       if (paramAuthCacheValue.equals(localAuthenticationInfo))
/* 108 */         localListIterator.remove();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.AuthCacheImpl
 * JD-Core Version:    0.6.2
 */