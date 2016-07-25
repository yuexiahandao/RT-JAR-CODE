/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class SnmpParameters extends SnmpParams
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1822462497931733790L;
/*     */   static final String defaultRdCommunity = "public";
/* 206 */   private int _protocolVersion = 0;
/*     */   private String _readCommunity;
/*     */   private String _writeCommunity;
/*     */   private String _informCommunity;
/*     */ 
/*     */   public SnmpParameters()
/*     */   {
/*  43 */     this._readCommunity = "public";
/*  44 */     this._informCommunity = "public";
/*     */   }
/*     */ 
/*     */   public SnmpParameters(String paramString1, String paramString2)
/*     */   {
/*  56 */     this._readCommunity = paramString1;
/*  57 */     this._writeCommunity = paramString2;
/*  58 */     this._informCommunity = "public";
/*     */   }
/*     */ 
/*     */   public SnmpParameters(String paramString1, String paramString2, String paramString3)
/*     */   {
/*  70 */     this._readCommunity = paramString1;
/*  71 */     this._writeCommunity = paramString2;
/*  72 */     this._informCommunity = paramString3;
/*     */   }
/*     */ 
/*     */   public String getRdCommunity()
/*     */   {
/*  80 */     return this._readCommunity;
/*     */   }
/*     */ 
/*     */   public synchronized void setRdCommunity(String paramString)
/*     */   {
/*  88 */     if (paramString == null)
/*  89 */       this._readCommunity = "public";
/*     */     else
/*  91 */       this._readCommunity = paramString;
/*     */   }
/*     */ 
/*     */   public String getWrCommunity()
/*     */   {
/*  99 */     return this._writeCommunity;
/*     */   }
/*     */ 
/*     */   public void setWrCommunity(String paramString)
/*     */   {
/* 107 */     this._writeCommunity = paramString;
/*     */   }
/*     */ 
/*     */   public String getInformCommunity()
/*     */   {
/* 115 */     return this._informCommunity;
/*     */   }
/*     */ 
/*     */   public void setInformCommunity(String paramString)
/*     */   {
/* 123 */     if (paramString == null)
/* 124 */       this._informCommunity = "public";
/*     */     else
/* 126 */       this._informCommunity = paramString;
/*     */   }
/*     */ 
/*     */   public boolean allowSnmpSets()
/*     */   {
/* 134 */     return this._writeCommunity != null;
/*     */   }
/*     */ 
/*     */   public synchronized boolean equals(Object paramObject)
/*     */   {
/* 145 */     if (!(paramObject instanceof SnmpParameters)) {
/* 146 */       return false;
/*     */     }
/* 148 */     if (this == paramObject)
/* 149 */       return true;
/* 150 */     SnmpParameters localSnmpParameters = (SnmpParameters)paramObject;
/* 151 */     if ((this._protocolVersion == localSnmpParameters._protocolVersion) && 
/* 152 */       (this._readCommunity.equals(localSnmpParameters._readCommunity)))
/* 153 */       return true;
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/* 162 */     SnmpParameters localSnmpParameters = null;
/*     */     try {
/* 164 */       localSnmpParameters = (SnmpParameters)super.clone();
/*     */ 
/* 166 */       localSnmpParameters._readCommunity = this._readCommunity;
/* 167 */       localSnmpParameters._writeCommunity = this._writeCommunity;
/* 168 */       localSnmpParameters._informCommunity = this._informCommunity;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 170 */       throw new InternalError();
/*     */     }
/* 172 */     return localSnmpParameters;
/*     */   }
/*     */ 
/*     */   public byte[] encodeAuthentication(int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 184 */       if (paramInt == 163)
/* 185 */         return this._writeCommunity.getBytes("8859_1");
/* 186 */       if (paramInt == 166) {
/* 187 */         return this._informCommunity.getBytes("8859_1");
/*     */       }
/* 189 */       return this._readCommunity.getBytes("8859_1");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 191 */       throw new SnmpStatusException(localUnsupportedEncodingException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpParameters
 * JD-Core Version:    0.6.2
 */