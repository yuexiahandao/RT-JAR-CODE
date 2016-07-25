/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class BasicPermission extends Permission
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6279438298436773498L;
/*     */   private transient boolean wildcard;
/*     */   private transient String path;
/*     */   private transient boolean exitVM;
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/*  92 */     if (paramString == null) {
/*  93 */       throw new NullPointerException("name can't be null");
/*     */     }
/*  95 */     int i = paramString.length();
/*     */ 
/*  97 */     if (i == 0) {
/*  98 */       throw new IllegalArgumentException("name can't be empty");
/*     */     }
/*     */ 
/* 101 */     int j = paramString.charAt(i - 1);
/*     */ 
/* 104 */     if ((j == 42) && ((i == 1) || (paramString.charAt(i - 2) == '.'))) {
/* 105 */       this.wildcard = true;
/* 106 */       if (i == 1)
/* 107 */         this.path = "";
/*     */       else {
/* 109 */         this.path = paramString.substring(0, i - 1);
/*     */       }
/*     */     }
/* 112 */     else if (paramString.equals("exitVM")) {
/* 113 */       this.wildcard = true;
/* 114 */       this.path = "exitVM.";
/* 115 */       this.exitVM = true;
/*     */     } else {
/* 117 */       this.path = paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public BasicPermission(String paramString)
/*     */   {
/* 136 */     super(paramString);
/* 137 */     init(paramString);
/*     */   }
/*     */ 
/*     */   public BasicPermission(String paramString1, String paramString2)
/*     */   {
/* 154 */     super(paramString1);
/* 155 */     init(paramString1);
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 176 */     if ((paramPermission == null) || (paramPermission.getClass() != getClass())) {
/* 177 */       return false;
/*     */     }
/* 179 */     BasicPermission localBasicPermission = (BasicPermission)paramPermission;
/*     */ 
/* 181 */     if (this.wildcard) {
/* 182 */       if (localBasicPermission.wildcard)
/*     */       {
/* 184 */         return localBasicPermission.path.startsWith(this.path);
/*     */       }
/*     */ 
/* 187 */       return (localBasicPermission.path.length() > this.path.length()) && (localBasicPermission.path.startsWith(this.path));
/*     */     }
/*     */ 
/* 191 */     if (localBasicPermission.wildcard)
/*     */     {
/* 193 */       return false;
/*     */     }
/*     */ 
/* 196 */     return this.path.equals(localBasicPermission.path);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 211 */     if (paramObject == this) {
/* 212 */       return true;
/*     */     }
/* 214 */     if ((paramObject == null) || (paramObject.getClass() != getClass())) {
/* 215 */       return false;
/*     */     }
/* 217 */     BasicPermission localBasicPermission = (BasicPermission)paramObject;
/*     */ 
/* 219 */     return getName().equals(localBasicPermission.getName());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 232 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 244 */     return "";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 260 */     return new BasicPermissionCollection(getClass());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 270 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 272 */     init(getName());
/*     */   }
/*     */ 
/*     */   final String getCanonicalName()
/*     */   {
/* 284 */     return this.exitVM ? "exitVM.*" : getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.BasicPermission
 * JD-Core Version:    0.6.2
 */