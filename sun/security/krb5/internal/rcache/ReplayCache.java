/*     */ package sun.security.krb5.internal.rcache;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ 
/*     */ public class ReplayCache extends LinkedList<AuthTime>
/*     */ {
/*     */   private static final long serialVersionUID = 2997933194993803994L;
/*     */   private String principal;
/*     */   private CacheTable table;
/*  52 */   private int nap = 600000;
/*     */ 
/*  54 */   private boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public ReplayCache(String paramString, CacheTable paramCacheTable)
/*     */   {
/*  62 */     this.principal = paramString;
/*  63 */     this.table = paramCacheTable;
/*     */   }
/*     */ 
/*     */   public synchronized void put(AuthTime paramAuthTime, long paramLong)
/*     */   {
/*  72 */     if (size() == 0) {
/*  73 */       addFirst(paramAuthTime);
/*     */     }
/*     */     else {
/*  76 */       AuthTime localAuthTime1 = (AuthTime)getFirst();
/*  77 */       if (localAuthTime1.kerberosTime < paramAuthTime.kerberosTime)
/*     */       {
/*  80 */         addFirst(paramAuthTime);
/*     */       }
/*  82 */       else if (localAuthTime1.kerberosTime == paramAuthTime.kerberosTime) {
/*  83 */         if (localAuthTime1.cusec < paramAuthTime.cusec) {
/*  84 */           addFirst(paramAuthTime);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  89 */         ListIterator localListIterator1 = listIterator(1);
/*  90 */         while (localListIterator1.hasNext()) {
/*  91 */           localAuthTime1 = (AuthTime)localListIterator1.next();
/*  92 */           if (localAuthTime1.kerberosTime < paramAuthTime.kerberosTime) {
/*  93 */             add(indexOf(localAuthTime1), paramAuthTime);
/*     */           }
/*  97 */           else if ((localAuthTime1.kerberosTime == paramAuthTime.kerberosTime) && 
/*  98 */             (localAuthTime1.cusec < paramAuthTime.cusec)) {
/*  99 */             add(indexOf(localAuthTime1), paramAuthTime);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 108 */     long l = paramLong - KerberosTime.getDefaultSkew() * 1000L;
/* 109 */     ListIterator localListIterator2 = listIterator(0);
/* 110 */     AuthTime localAuthTime2 = null;
/* 111 */     int i = -1;
/* 112 */     while (localListIterator2.hasNext())
/*     */     {
/* 114 */       localAuthTime2 = (AuthTime)localListIterator2.next();
/* 115 */       if (localAuthTime2.kerberosTime < l) {
/* 116 */         i = indexOf(localAuthTime2);
/*     */       }
/*     */     }
/*     */ 
/* 120 */     if (i > -1)
/*     */     {
/*     */       do
/* 123 */         removeLast();
/* 124 */       while (size() > i);
/*     */     }
/* 126 */     if (this.DEBUG)
/* 127 */       printList();
/*     */   }
/*     */ 
/*     */   private void printList()
/*     */   {
/* 136 */     Object[] arrayOfObject = toArray();
/* 137 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 138 */       System.out.println("object " + i + ": " + ((AuthTime)arrayOfObject[i]).kerberosTime + "/" + ((AuthTime)arrayOfObject[i]).cusec);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.rcache.ReplayCache
 * JD-Core Version:    0.6.2
 */