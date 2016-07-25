/*     */ package sun.security.acl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Group;
/*     */ import java.security.acl.LastOwnerException;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.security.acl.Owner;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class OwnerImpl
/*     */   implements Owner
/*     */ {
/*     */   private Group ownerGroup;
/*     */ 
/*     */   public OwnerImpl(Principal paramPrincipal)
/*     */   {
/*  42 */     this.ownerGroup = new GroupImpl("AclOwners");
/*  43 */     this.ownerGroup.addMember(paramPrincipal);
/*     */   }
/*     */ 
/*     */   public synchronized boolean addOwner(Principal paramPrincipal1, Principal paramPrincipal2)
/*     */     throws NotOwnerException
/*     */   {
/*  61 */     if (!isOwner(paramPrincipal1)) {
/*  62 */       throw new NotOwnerException();
/*     */     }
/*  64 */     this.ownerGroup.addMember(paramPrincipal2);
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean deleteOwner(Principal paramPrincipal1, Principal paramPrincipal2)
/*     */     throws NotOwnerException, LastOwnerException
/*     */   {
/*  85 */     if (!isOwner(paramPrincipal1)) {
/*  86 */       throw new NotOwnerException();
/*     */     }
/*  88 */     Enumeration localEnumeration = this.ownerGroup.members();
/*     */ 
/*  92 */     Object localObject = localEnumeration.nextElement();
/*  93 */     if (localEnumeration.hasMoreElements()) {
/*  94 */       return this.ownerGroup.removeMember(paramPrincipal2);
/*     */     }
/*  96 */     throw new LastOwnerException();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isOwner(Principal paramPrincipal)
/*     */   {
/* 106 */     return this.ownerGroup.isMember(paramPrincipal);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.OwnerImpl
 * JD-Core Version:    0.6.2
 */