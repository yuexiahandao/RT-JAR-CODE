/*     */ package sun.security.acl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Group;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class GroupImpl
/*     */   implements Group
/*     */ {
/*  37 */   private Vector<Principal> groupMembers = new Vector(50, 100);
/*     */   private String group;
/*     */ 
/*     */   public GroupImpl(String paramString)
/*     */   {
/*  45 */     this.group = paramString;
/*     */   }
/*     */ 
/*     */   public boolean addMember(Principal paramPrincipal)
/*     */   {
/*  55 */     if (this.groupMembers.contains(paramPrincipal)) {
/*  56 */       return false;
/*     */     }
/*     */ 
/*  59 */     if (this.group.equals(paramPrincipal.toString())) {
/*  60 */       throw new IllegalArgumentException();
/*     */     }
/*  62 */     this.groupMembers.addElement(paramPrincipal);
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removeMember(Principal paramPrincipal)
/*     */   {
/*  73 */     return this.groupMembers.removeElement(paramPrincipal);
/*     */   }
/*     */ 
/*     */   public Enumeration<? extends Principal> members()
/*     */   {
/*  80 */     return this.groupMembers.elements();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  89 */     if (this == paramObject) {
/*  90 */       return true;
/*     */     }
/*  92 */     if (!(paramObject instanceof Group)) {
/*  93 */       return false;
/*     */     }
/*  95 */     Group localGroup = (Group)paramObject;
/*  96 */     return this.group.equals(localGroup.toString());
/*     */   }
/*     */ 
/*     */   public boolean equals(Group paramGroup)
/*     */   {
/* 101 */     return equals(paramGroup);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return this.group;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 115 */     return this.group.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean isMember(Principal paramPrincipal)
/*     */   {
/* 131 */     if (this.groupMembers.contains(paramPrincipal)) {
/* 132 */       return true;
/*     */     }
/* 134 */     Vector localVector = new Vector(10);
/* 135 */     return isMemberRecurse(paramPrincipal, localVector);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 143 */     return this.group;
/*     */   }
/*     */ 
/*     */   boolean isMemberRecurse(Principal paramPrincipal, Vector<Group> paramVector)
/*     */   {
/* 153 */     Enumeration localEnumeration = members();
/* 154 */     while (localEnumeration.hasMoreElements()) {
/* 155 */       boolean bool = false;
/* 156 */       Principal localPrincipal = (Principal)localEnumeration.nextElement();
/*     */ 
/* 159 */       if (localPrincipal.equals(paramPrincipal))
/* 160 */         return true;
/*     */       Object localObject;
/* 161 */       if ((localPrincipal instanceof GroupImpl))
/*     */       {
/* 171 */         localObject = (GroupImpl)localPrincipal;
/* 172 */         paramVector.addElement(this);
/* 173 */         if (!paramVector.contains(localObject))
/* 174 */           bool = ((GroupImpl)localObject).isMemberRecurse(paramPrincipal, paramVector);
/* 175 */       } else if ((localPrincipal instanceof Group)) {
/* 176 */         localObject = (Group)localPrincipal;
/* 177 */         if (!paramVector.contains(localObject)) {
/* 178 */           bool = ((Group)localObject).isMember(paramPrincipal);
/*     */         }
/*     */       }
/* 181 */       if (bool)
/* 182 */         return bool;
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.GroupImpl
 * JD-Core Version:    0.6.2
 */