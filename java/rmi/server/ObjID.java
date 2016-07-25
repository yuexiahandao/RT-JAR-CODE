/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public final class ObjID
/*     */   implements Serializable
/*     */ {
/*     */   public static final int REGISTRY_ID = 0;
/*     */   public static final int ACTIVATOR_ID = 1;
/*     */   public static final int DGC_ID = 2;
/*     */   private static final long serialVersionUID = -6386392263968365220L;
/*  87 */   private static final AtomicLong nextObjNum = new AtomicLong(0L);
/*  88 */   private static final UID mySpace = new UID();
/*  89 */   private static final SecureRandom secureRandom = new SecureRandom();
/*     */   private final long objNum;
/*     */   private final UID space;
/*     */ 
/*     */   public ObjID()
/*     */   {
/* 117 */     if (useRandomIDs()) {
/* 118 */       this.space = new UID();
/* 119 */       this.objNum = secureRandom.nextLong();
/*     */     } else {
/* 121 */       this.space = mySpace;
/* 122 */       this.objNum = nextObjNum.getAndIncrement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjID(int paramInt)
/*     */   {
/* 136 */     this.space = new UID((short)0);
/* 137 */     this.objNum = paramInt;
/*     */   }
/*     */ 
/*     */   private ObjID(long paramLong, UID paramUID)
/*     */   {
/* 144 */     this.objNum = paramLong;
/* 145 */     this.space = paramUID;
/*     */   }
/*     */ 
/*     */   public void write(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 165 */     paramObjectOutput.writeLong(this.objNum);
/* 166 */     this.space.write(paramObjectOutput);
/*     */   }
/*     */ 
/*     */   public static ObjID read(ObjectInput paramObjectInput)
/*     */     throws IOException
/*     */   {
/* 191 */     long l = paramObjectInput.readLong();
/* 192 */     UID localUID = UID.read(paramObjectInput);
/* 193 */     return new ObjID(l, localUID);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 203 */     return (int)this.objNum;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 220 */     if ((paramObject instanceof ObjID)) {
/* 221 */       ObjID localObjID = (ObjID)paramObject;
/* 222 */       return (this.objNum == localObjID.objNum) && (this.space.equals(localObjID.space));
/*     */     }
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 239 */     return "[" + (this.space.equals(mySpace) ? "" : new StringBuilder().append(this.space).append(", ").toString()) + this.objNum + "]";
/*     */   }
/*     */ 
/*     */   private static boolean useRandomIDs()
/*     */   {
/* 244 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.randomIDs"));
/*     */ 
/* 246 */     return str == null ? true : Boolean.parseBoolean(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.ObjID
 * JD-Core Version:    0.6.2
 */