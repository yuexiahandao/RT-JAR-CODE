/*     */ package com.sun.rowset.providers;
/*     */ 
/*     */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.sql.SQLException;
/*     */ import javax.sql.RowSetReader;
/*     */ import javax.sql.RowSetWriter;
/*     */ import javax.sql.rowset.spi.SyncProvider;
/*     */ import javax.sql.rowset.spi.SyncProviderException;
/*     */ import javax.sql.rowset.spi.XmlReader;
/*     */ import javax.sql.rowset.spi.XmlWriter;
/*     */ 
/*     */ public final class RIXMLProvider extends SyncProvider
/*     */ {
/*  83 */   private String providerID = "com.sun.rowset.providers.RIXMLProvider";
/*     */ 
/*  88 */   private String vendorName = "Oracle Corporation";
/*     */ 
/*  93 */   private String versionNumber = "1.0";
/*     */   private JdbcRowSetResourceBundle resBundle;
/*     */   private XmlReader xmlReader;
/*     */   private XmlWriter xmlWriter;
/*     */ 
/*     */   public RIXMLProvider()
/*     */   {
/* 105 */     this.providerID = getClass().getName();
/*     */     try {
/* 107 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 109 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getProviderID()
/*     */   {
/* 121 */     return this.providerID;
/*     */   }
/*     */ 
/*     */   public void setXmlReader(XmlReader paramXmlReader)
/*     */     throws SQLException
/*     */   {
/* 134 */     this.xmlReader = paramXmlReader;
/*     */   }
/*     */ 
/*     */   public void setXmlWriter(XmlWriter paramXmlWriter)
/*     */     throws SQLException
/*     */   {
/* 144 */     this.xmlWriter = paramXmlWriter;
/*     */   }
/*     */ 
/*     */   public XmlReader getXmlReader()
/*     */     throws SQLException
/*     */   {
/* 155 */     return this.xmlReader;
/*     */   }
/*     */ 
/*     */   public XmlWriter getXmlWriter()
/*     */     throws SQLException
/*     */   {
/* 166 */     return this.xmlWriter;
/*     */   }
/*     */ 
/*     */   public int getProviderGrade()
/*     */   {
/* 187 */     return 1;
/*     */   }
/*     */ 
/*     */   public int supportsUpdatableView()
/*     */   {
/* 195 */     return 6;
/*     */   }
/*     */ 
/*     */   public int getDataSourceLock()
/*     */     throws SyncProviderException
/*     */   {
/* 202 */     return 1;
/*     */   }
/*     */ 
/*     */   public void setDataSourceLock(int paramInt)
/*     */     throws SyncProviderException
/*     */   {
/* 210 */     throw new UnsupportedOperationException(this.resBundle.handleGetObject("rixml.unsupp").toString());
/*     */   }
/*     */ 
/*     */   public RowSetWriter getRowSetWriter()
/*     */   {
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */   public RowSetReader getRowSetReader()
/*     */   {
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 235 */     return this.versionNumber;
/*     */   }
/*     */ 
/*     */   public String getVendor()
/*     */   {
/* 246 */     return this.vendorName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.providers.RIXMLProvider
 * JD-Core Version:    0.6.2
 */