/*     */ package sun.net.ftp;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class FtpDirEntry
/*     */ {
/*     */   private final String name;
/*  57 */   private String user = null;
/*  58 */   private String group = null;
/*  59 */   private long size = -1L;
/*  60 */   private Date created = null;
/*  61 */   private Date lastModified = null;
/*  62 */   private Type type = Type.FILE;
/*  63 */   private boolean[][] permissions = (boolean[][])null;
/*  64 */   private HashMap<String, String> facts = new HashMap();
/*     */ 
/*     */   private FtpDirEntry() {
/*  67 */     this.name = null;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry(String paramString)
/*     */   {
/*  76 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  85 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getUser()
/*     */   {
/*  96 */     return this.user;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setUser(String paramString)
/*     */   {
/* 108 */     this.user = paramString;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   public String getGroup()
/*     */   {
/* 120 */     return this.group;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setGroup(String paramString)
/*     */   {
/* 132 */     this.group = paramString;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 143 */     return this.size;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setSize(long paramLong)
/*     */   {
/* 154 */     this.size = paramLong;
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 170 */     return this.type;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setType(Type paramType)
/*     */   {
/* 182 */     this.type = paramType;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   public Date getLastModified()
/*     */   {
/* 195 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setLastModified(Date paramDate)
/*     */   {
/* 207 */     this.lastModified = paramDate;
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean canRead(Permission paramPermission)
/*     */   {
/* 218 */     if (this.permissions != null) {
/* 219 */       return this.permissions[paramPermission.value][0];
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canWrite(Permission paramPermission)
/*     */   {
/* 231 */     if (this.permissions != null) {
/* 232 */       return this.permissions[paramPermission.value][1];
/*     */     }
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canExexcute(Permission paramPermission)
/*     */   {
/* 244 */     if (this.permissions != null) {
/* 245 */       return this.permissions[paramPermission.value][2];
/*     */     }
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setPermissions(boolean[][] paramArrayOfBoolean)
/*     */   {
/* 262 */     this.permissions = paramArrayOfBoolean;
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry addFact(String paramString1, String paramString2)
/*     */   {
/* 276 */     this.facts.put(paramString1.toLowerCase(), paramString2);
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */   public String getFact(String paramString)
/*     */   {
/* 288 */     return (String)this.facts.get(paramString.toLowerCase());
/*     */   }
/*     */ 
/*     */   public Date getCreated()
/*     */   {
/* 298 */     return this.created;
/*     */   }
/*     */ 
/*     */   public FtpDirEntry setCreated(Date paramDate)
/*     */   {
/* 310 */     this.created = paramDate;
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 326 */     if (this.lastModified == null) {
/* 327 */       return this.name + " [" + this.type + "] (" + this.user + " / " + this.group + ") " + this.size;
/*     */     }
/* 329 */     return this.name + " [" + this.type + "] (" + this.user + " / " + this.group + ") {" + this.size + "} " + DateFormat.getDateInstance().format(this.lastModified);
/*     */   }
/*     */ 
/*     */   public static enum Permission
/*     */   {
/*  49 */     USER(0), GROUP(1), OTHERS(2);
/*     */ 
/*     */     int value;
/*     */ 
/*  53 */     private Permission(int paramInt) { this.value = paramInt; }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  44 */     FILE, DIR, PDIR, CDIR, LINK;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ftp.FtpDirEntry
 * JD-Core Version:    0.6.2
 */