/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class MimeType
/*     */   implements Externalizable
/*     */ {
/*     */   private String primaryType;
/*     */   private String subType;
/*     */   private MimeTypeParameterList parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
/*     */ 
/*     */   public MimeType()
/*     */   {
/*  52 */     this.primaryType = "application";
/*  53 */     this.subType = "*";
/*  54 */     this.parameters = new MimeTypeParameterList();
/*     */   }
/*     */ 
/*     */   public MimeType(String rawdata)
/*     */     throws MimeTypeParseException
/*     */   {
/*  63 */     parse(rawdata);
/*     */   }
/*     */ 
/*     */   public MimeType(String primary, String sub)
/*     */     throws MimeTypeParseException
/*     */   {
/*  77 */     if (isValidToken(primary))
/*  78 */       this.primaryType = primary.toLowerCase(Locale.ENGLISH);
/*     */     else {
/*  80 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     }
/*     */ 
/*  84 */     if (isValidToken(sub))
/*  85 */       this.subType = sub.toLowerCase(Locale.ENGLISH);
/*     */     else {
/*  87 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */     }
/*     */ 
/*  90 */     this.parameters = new MimeTypeParameterList();
/*     */   }
/*     */ 
/*     */   private void parse(String rawdata)
/*     */     throws MimeTypeParseException
/*     */   {
/*  97 */     int slashIndex = rawdata.indexOf('/');
/*  98 */     int semIndex = rawdata.indexOf(';');
/*  99 */     if ((slashIndex < 0) && (semIndex < 0))
/*     */     {
/* 102 */       throw new MimeTypeParseException("Unable to find a sub type.");
/* 103 */     }if ((slashIndex < 0) && (semIndex >= 0))
/*     */     {
/* 106 */       throw new MimeTypeParseException("Unable to find a sub type.");
/* 107 */     }if ((slashIndex >= 0) && (semIndex < 0))
/*     */     {
/* 109 */       this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 111 */       this.subType = rawdata.substring(slashIndex + 1).trim().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 113 */       this.parameters = new MimeTypeParameterList();
/* 114 */     } else if (slashIndex < semIndex)
/*     */     {
/* 116 */       this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 118 */       this.subType = rawdata.substring(slashIndex + 1, semIndex).trim().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 120 */       this.parameters = new MimeTypeParameterList(rawdata.substring(semIndex));
/*     */     }
/*     */     else
/*     */     {
/* 124 */       throw new MimeTypeParseException("Unable to find a sub type.");
/*     */     }
/*     */ 
/* 130 */     if (!isValidToken(this.primaryType)) {
/* 131 */       throw new MimeTypeParseException("Primary type is invalid.");
/*     */     }
/*     */ 
/* 134 */     if (!isValidToken(this.subType))
/* 135 */       throw new MimeTypeParseException("Sub type is invalid.");
/*     */   }
/*     */ 
/*     */   public String getPrimaryType()
/*     */   {
/* 144 */     return this.primaryType;
/*     */   }
/*     */ 
/*     */   public void setPrimaryType(String primary)
/*     */     throws MimeTypeParseException
/*     */   {
/* 156 */     if (!isValidToken(this.primaryType))
/* 157 */       throw new MimeTypeParseException("Primary type is invalid.");
/* 158 */     this.primaryType = primary.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   public String getSubType()
/*     */   {
/* 167 */     return this.subType;
/*     */   }
/*     */ 
/*     */   public void setSubType(String sub)
/*     */     throws MimeTypeParseException
/*     */   {
/* 179 */     if (!isValidToken(this.subType))
/* 180 */       throw new MimeTypeParseException("Sub type is invalid.");
/* 181 */     this.subType = sub.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   public MimeTypeParameterList getParameters()
/*     */   {
/* 190 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 201 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */   public void setParameter(String name, String value)
/*     */   {
/* 212 */     this.parameters.set(name, value);
/*     */   }
/*     */ 
/*     */   public void removeParameter(String name)
/*     */   {
/* 221 */     this.parameters.remove(name);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 228 */     return getBaseType() + this.parameters.toString();
/*     */   }
/*     */ 
/*     */   public String getBaseType()
/*     */   {
/* 238 */     return this.primaryType + "/" + this.subType;
/*     */   }
/*     */ 
/*     */   public boolean match(MimeType type)
/*     */   {
/* 249 */     return (this.primaryType.equals(type.getPrimaryType())) && ((this.subType.equals("*")) || (type.getSubType().equals("*")) || (this.subType.equals(type.getSubType())));
/*     */   }
/*     */ 
/*     */   public boolean match(String rawdata)
/*     */     throws MimeTypeParseException
/*     */   {
/* 263 */     return match(new MimeType(rawdata));
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 276 */     out.writeUTF(toString());
/* 277 */     out.flush();
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 294 */       parse(in.readUTF());
/*     */     } catch (MimeTypeParseException e) {
/* 296 */       throw new IOException(e.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isTokenChar(char c)
/*     */   {
/* 306 */     return (c > ' ') && (c < '') && ("()<>@,;:/[]?=\\\"".indexOf(c) < 0);
/*     */   }
/*     */ 
/*     */   private boolean isValidToken(String s)
/*     */   {
/* 313 */     int len = s.length();
/* 314 */     if (len > 0) {
/* 315 */       for (int i = 0; i < len; i++) {
/* 316 */         char c = s.charAt(i);
/* 317 */         if (!isTokenChar(c)) {
/* 318 */           return false;
/*     */         }
/*     */       }
/* 321 */       return true;
/*     */     }
/* 323 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.MimeType
 * JD-Core Version:    0.6.2
 */