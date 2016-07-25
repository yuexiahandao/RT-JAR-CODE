/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.nio.channels.MembershipKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ class MembershipRegistry
/*     */ {
/*  42 */   private Map<InetAddress, List<MembershipKeyImpl>> groups = null;
/*     */ 
/*     */   MembershipKey checkMembership(InetAddress paramInetAddress1, NetworkInterface paramNetworkInterface, InetAddress paramInetAddress2)
/*     */   {
/*  54 */     if (this.groups != null) {
/*  55 */       List localList = (List)this.groups.get(paramInetAddress1);
/*  56 */       if (localList != null) {
/*  57 */         for (MembershipKeyImpl localMembershipKeyImpl : localList) {
/*  58 */           if (localMembershipKeyImpl.networkInterface().equals(paramNetworkInterface))
/*     */           {
/*  61 */             if (paramInetAddress2 == null) {
/*  62 */               if (localMembershipKeyImpl.sourceAddress() == null)
/*  63 */                 return localMembershipKeyImpl;
/*  64 */               throw new IllegalStateException("Already a member to receive all packets");
/*     */             }
/*     */ 
/*  69 */             if (localMembershipKeyImpl.sourceAddress() == null)
/*  70 */               throw new IllegalStateException("Already have source-specific membership");
/*  71 */             if (paramInetAddress2.equals(localMembershipKeyImpl.sourceAddress()))
/*  72 */               return localMembershipKeyImpl;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   void add(MembershipKeyImpl paramMembershipKeyImpl)
/*     */   {
/*  84 */     InetAddress localInetAddress = paramMembershipKeyImpl.group();
/*     */     Object localObject;
/*  86 */     if (this.groups == null) {
/*  87 */       this.groups = new HashMap();
/*  88 */       localObject = null;
/*     */     } else {
/*  90 */       localObject = (List)this.groups.get(localInetAddress);
/*     */     }
/*  92 */     if (localObject == null) {
/*  93 */       localObject = new LinkedList();
/*  94 */       this.groups.put(localInetAddress, localObject);
/*     */     }
/*  96 */     ((List)localObject).add(paramMembershipKeyImpl);
/*     */   }
/*     */ 
/*     */   void remove(MembershipKeyImpl paramMembershipKeyImpl)
/*     */   {
/* 103 */     InetAddress localInetAddress = paramMembershipKeyImpl.group();
/* 104 */     List localList = (List)this.groups.get(localInetAddress);
/* 105 */     if (localList != null) {
/* 106 */       Iterator localIterator = localList.iterator();
/* 107 */       while (localIterator.hasNext()) {
/* 108 */         if (localIterator.next() == paramMembershipKeyImpl) {
/* 109 */           localIterator.remove();
/*     */         }
/*     */       }
/*     */ 
/* 113 */       if (localList.isEmpty())
/* 114 */         this.groups.remove(localInetAddress);
/*     */     }
/*     */   }
/*     */ 
/*     */   void invalidateAll()
/*     */   {
/* 123 */     if (this.groups != null)
/* 124 */       for (InetAddress localInetAddress : this.groups.keySet())
/* 125 */         for (MembershipKeyImpl localMembershipKeyImpl : (List)this.groups.get(localInetAddress))
/* 126 */           localMembershipKeyImpl.invalidate();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.MembershipRegistry
 * JD-Core Version:    0.6.2
 */