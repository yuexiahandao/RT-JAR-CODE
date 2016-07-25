/*     */ package java.awt.datatransfer;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ class MimeType
/*     */   implements Externalizable, Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -6568722458793895906L;
/*     */   private String primaryType;
/*     */   private String subType;
/*     */   private MimeTypeParameterList parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";
/*     */ 
/*     */   public MimeType()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MimeType(String paramString)
/*     */     throws MimeTypeParseException
/*     */   {
/*  66 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   public MimeType(String paramString1, String paramString2)
/*     */     throws MimeTypeParseException
/*     */   {
/*  79 */     this(paramString1, paramString2, new MimeTypeParameterList());
/*     */   }
/*     */ 
/*     */   public MimeType(String paramString1, String paramString2, MimeTypeParameterList paramMimeTypeParameterList)
/*     */     throws MimeTypeParseException
/*     */   {
/*  95 */     if (isValidToken(paramString1))
/*  96 */       this.primaryType = paramString1.toLowerCase();
/*     */     else {
/*  98 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     }
/*     */ 
/* 102 */     if (isValidToken(paramString2))
/* 103 */       this.subType = paramString2.toLowerCase();
/*     */     else {
/* 105 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */     }
/*     */ 
/* 108 */     this.parameters = ((MimeTypeParameterList)paramMimeTypeParameterList.clone());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 115 */     int i = 0;
/* 116 */     i += this.primaryType.hashCode();
/* 117 */     i += this.subType.hashCode();
/* 118 */     i += this.parameters.hashCode();
/* 119 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 132 */     if (!(paramObject instanceof MimeType)) {
/* 133 */       return false;
/*     */     }
/* 135 */     MimeType localMimeType = (MimeType)paramObject;
/* 136 */     boolean bool = (this.primaryType.equals(localMimeType.primaryType)) && (this.subType.equals(localMimeType.subType)) && (this.parameters.equals(localMimeType.parameters));
/*     */ 
/* 140 */     return bool;
/*     */   }
/*     */ 
/*     */   private void parse(String paramString)
/*     */     throws MimeTypeParseException
/*     */   {
/* 149 */     int i = paramString.indexOf('/');
/* 150 */     int j = paramString.indexOf(';');
/* 151 */     if ((i < 0) && (j < 0))
/*     */     {
/* 154 */       throw new MimeTypeParseException("Unable to find a sub type.");
/* 155 */     }if ((i < 0) && (j >= 0))
/*     */     {
/* 158 */       throw new MimeTypeParseException("Unable to find a sub type.");
/* 159 */     }if ((i >= 0) && (j < 0))
/*     */     {
/* 161 */       this.primaryType = paramString.substring(0, i).trim().toLowerCase();
/*     */ 
/* 163 */       this.subType = paramString.substring(i + 1).trim().toLowerCase();
/*     */ 
/* 165 */       this.parameters = new MimeTypeParameterList();
/* 166 */     } else if (i < j)
/*     */     {
/* 168 */       this.primaryType = paramString.substring(0, i).trim().toLowerCase();
/*     */ 
/* 170 */       this.subType = paramString.substring(i + 1, j).trim().toLowerCase();
/*     */ 
/* 172 */       this.parameters = new MimeTypeParameterList(paramString.substring(j));
/*     */     }
/*     */     else
/*     */     {
/* 177 */       throw new MimeTypeParseException("Unable to find a sub type.");
/*     */     }
/*     */ 
/* 183 */     if (!isValidToken(this.primaryType)) {
/* 184 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     }
/*     */ 
/* 188 */     if (!isValidToken(this.subType))
/* 189 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */   }
/*     */ 
/*     */   public String getPrimaryType()
/*     */   {
/* 197 */     return this.primaryType;
/*     */   }
/*     */ 
/*     */   public String getSubType()
/*     */   {
/* 204 */     return this.subType;
/*     */   }
/*     */ 
/*     */   public MimeTypeParameterList getParameters()
/*     */   {
/* 211 */     return (MimeTypeParameterList)this.parameters.clone();
/*     */   }
/*     */ 
/*     */   public String getParameter(String paramString)
/*     */   {
/* 219 */     return this.parameters.get(paramString);
/*     */   }
/*     */ 
/*     */   public void setParameter(String paramString1, String paramString2)
/*     */   {
/* 229 */     this.parameters.set(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void removeParameter(String paramString)
/*     */   {
/* 238 */     this.parameters.remove(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 245 */     return getBaseType() + this.parameters.toString();
/*     */   }
/*     */ 
/*     */   public String getBaseType()
/*     */   {
/* 253 */     return this.primaryType + "/" + this.subType;
/*     */   }
/*     */ 
/*     */   public boolean match(MimeType paramMimeType)
/*     */   {
/* 268 */     if (paramMimeType == null)
/* 269 */       return false;
/* 270 */     return (this.primaryType.equals(paramMimeType.getPrimaryType())) && ((this.subType.equals("*")) || (paramMimeType.getSubType().equals("*")) || (this.subType.equals(paramMimeType.getSubType())));
/*     */   }
/*     */ 
/*     */   public boolean match(String paramString)
/*     */     throws MimeTypeParseException
/*     */   {
/* 290 */     if (paramString == null)
/* 291 */       return false;
/* 292 */     return match(new MimeType(paramString));
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 303 */     String str = toString();
/*     */ 
/* 305 */     if (str.length() <= 65535) {
/* 306 */       paramObjectOutput.writeUTF(str);
/*     */     } else {
/* 308 */       paramObjectOutput.writeByte(0);
/* 309 */       paramObjectOutput.writeByte(0);
/* 310 */       paramObjectOutput.writeInt(str.length());
/* 311 */       paramObjectOutput.write(str.getBytes());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 326 */     String str = paramObjectInput.readUTF();
/* 327 */     if ((str == null) || (str.length() == 0)) {
/* 328 */       byte[] arrayOfByte = new byte[paramObjectInput.readInt()];
/* 329 */       paramObjectInput.readFully(arrayOfByte);
/* 330 */       str = new String(arrayOfByte);
/*     */     }
/*     */     try {
/* 333 */       parse(str);
/*     */     } catch (MimeTypeParseException localMimeTypeParseException) {
/* 335 */       throw new IOException(localMimeTypeParseException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 345 */     MimeType localMimeType = null;
/*     */     try {
/* 347 */       localMimeType = (MimeType)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 350 */     localMimeType.parameters = ((MimeTypeParameterList)this.parameters.clone());
/* 351 */     return localMimeType;
/*     */   }
/*     */ 
/*     */   private static boolean isTokenChar(char paramChar)
/*     */   {
/* 364 */     return (paramChar > ' ') && (paramChar < '') && ("()<>@,;:\\\"/[]?=".indexOf(paramChar) < 0);
/*     */   }
/*     */ 
/*     */   private boolean isValidToken(String paramString)
/*     */   {
/* 373 */     int i = paramString.length();
/* 374 */     if (i > 0) {
/* 375 */       for (int j = 0; j < i; j++) {
/* 376 */         char c = paramString.charAt(j);
/* 377 */         if (!isTokenChar(c)) {
/* 378 */           return false;
/*     */         }
/*     */       }
/* 381 */       return true;
/*     */     }
/* 383 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.MimeType
 * JD-Core Version:    0.6.2
 */