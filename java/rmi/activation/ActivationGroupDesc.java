/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.util.Arrays;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public final class ActivationGroupDesc
/*     */   implements Serializable
/*     */ {
/*     */   private String className;
/*     */   private String location;
/*     */   private MarshalledObject<?> data;
/*     */   private CommandEnvironment env;
/*     */   private Properties props;
/*     */   private static final long serialVersionUID = -4936225423168276595L;
/*     */ 
/*     */   public ActivationGroupDesc(Properties paramProperties, CommandEnvironment paramCommandEnvironment)
/*     */   {
/* 113 */     this(null, null, null, paramProperties, paramCommandEnvironment);
/*     */   }
/*     */ 
/*     */   public ActivationGroupDesc(String paramString1, String paramString2, MarshalledObject<?> paramMarshalledObject, Properties paramProperties, CommandEnvironment paramCommandEnvironment)
/*     */   {
/* 140 */     this.props = paramProperties;
/* 141 */     this.env = paramCommandEnvironment;
/* 142 */     this.data = paramMarshalledObject;
/* 143 */     this.location = paramString2;
/* 144 */     this.className = paramString1;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 155 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 164 */     return this.location;
/*     */   }
/*     */ 
/*     */   public MarshalledObject<?> getData()
/*     */   {
/* 173 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Properties getPropertyOverrides()
/*     */   {
/* 182 */     return this.props != null ? (Properties)this.props.clone() : null;
/*     */   }
/*     */ 
/*     */   public CommandEnvironment getCommandEnvironment()
/*     */   {
/* 191 */     return this.env;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 333 */     if ((paramObject instanceof ActivationGroupDesc)) {
/* 334 */       ActivationGroupDesc localActivationGroupDesc = (ActivationGroupDesc)paramObject;
/* 335 */       return (this.className == null ? localActivationGroupDesc.className == null : this.className.equals(localActivationGroupDesc.className)) && (this.location == null ? localActivationGroupDesc.location == null : this.location.equals(localActivationGroupDesc.location)) && (this.data == null ? localActivationGroupDesc.data == null : this.data.equals(localActivationGroupDesc.data)) && (this.env == null ? localActivationGroupDesc.env == null : this.env.equals(localActivationGroupDesc.env)) && (this.props == null ? localActivationGroupDesc.props == null : this.props.equals(localActivationGroupDesc.props));
/*     */     }
/*     */ 
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 357 */     return (this.location == null ? 0 : this.location.hashCode() << 24) ^ (this.env == null ? 0 : this.env.hashCode() << 16) ^ (this.className == null ? 0 : this.className.hashCode() << 8) ^ (this.data == null ? 0 : this.data.hashCode());
/*     */   }
/*     */ 
/*     */   public static class CommandEnvironment
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 6165754737887770191L;
/*     */     private String command;
/*     */     private String[] options;
/*     */ 
/*     */     public CommandEnvironment(String paramString, String[] paramArrayOfString)
/*     */     {
/* 233 */       this.command = paramString;
/*     */ 
/* 236 */       if (paramArrayOfString == null) {
/* 237 */         this.options = new String[0];
/*     */       } else {
/* 239 */         this.options = new String[paramArrayOfString.length];
/* 240 */         System.arraycopy(paramArrayOfString, 0, this.options, 0, paramArrayOfString.length);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String getCommandPath()
/*     */     {
/* 252 */       return this.command;
/*     */     }
/*     */ 
/*     */     public String[] getCommandOptions()
/*     */     {
/* 266 */       return (String[])this.options.clone();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 279 */       if ((paramObject instanceof CommandEnvironment)) {
/* 280 */         CommandEnvironment localCommandEnvironment = (CommandEnvironment)paramObject;
/* 281 */         return (this.command == null ? localCommandEnvironment.command == null : this.command.equals(localCommandEnvironment.command)) && (Arrays.equals(this.options, localCommandEnvironment.options));
/*     */       }
/*     */ 
/* 286 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 299 */       return this.command == null ? 0 : this.command.hashCode();
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 316 */       paramObjectInputStream.defaultReadObject();
/* 317 */       if (this.options == null)
/* 318 */         this.options = new String[0];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationGroupDesc
 * JD-Core Version:    0.6.2
 */