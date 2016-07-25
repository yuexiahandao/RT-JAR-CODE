/*     */ package com.sun.rowset;
/*     */ 
/*     */ import com.sun.rowset.internal.WebRowSetXmlReader;
/*     */ import com.sun.rowset.internal.WebRowSetXmlWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Hashtable;
/*     */ import javax.sql.rowset.WebRowSet;
/*     */ import javax.sql.rowset.spi.SyncFactory;
/*     */ import javax.sql.rowset.spi.SyncProvider;
/*     */ 
/*     */ public class WebRowSetImpl extends CachedRowSetImpl
/*     */   implements WebRowSet
/*     */ {
/*     */   private WebRowSetXmlReader xmlReader;
/*     */   private WebRowSetXmlWriter xmlWriter;
/*     */   private int curPosBfrWrite;
/*     */   private SyncProvider provider;
/*     */   static final long serialVersionUID = -8771775154092422943L;
/*     */ 
/*     */   public WebRowSetImpl()
/*     */     throws SQLException
/*     */   {
/*  91 */     this.xmlReader = new WebRowSetXmlReader();
/*  92 */     this.xmlWriter = new WebRowSetXmlWriter();
/*     */   }
/*     */ 
/*     */   public WebRowSetImpl(Hashtable paramHashtable)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 109 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */ 
/* 112 */     if (paramHashtable == null) {
/* 113 */       throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.nullhash").toString());
/*     */     }
/*     */ 
/* 116 */     String str = (String)paramHashtable.get("rowset.provider.classname");
/*     */ 
/* 120 */     this.provider = SyncFactory.getInstance(str);
/*     */   }
/*     */ 
/*     */   public void writeXml(ResultSet paramResultSet, Writer paramWriter)
/*     */     throws SQLException
/*     */   {
/* 138 */     populate(paramResultSet);
/*     */ 
/* 141 */     this.curPosBfrWrite = getRow();
/*     */ 
/* 143 */     writeXml(paramWriter);
/*     */   }
/*     */ 
/*     */   public void writeXml(Writer paramWriter)
/*     */     throws SQLException
/*     */   {
/* 159 */     if (this.xmlWriter != null)
/*     */     {
/* 162 */       this.curPosBfrWrite = getRow();
/*     */ 
/* 164 */       this.xmlWriter.writeXML(this, paramWriter);
/*     */     } else {
/* 166 */       throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidwr").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readXml(Reader paramReader)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 181 */       if (paramReader != null) {
/* 182 */         this.xmlReader.readXML(this, paramReader);
/*     */ 
/* 187 */         if (this.curPosBfrWrite == 0) {
/* 188 */           beforeFirst();
/*     */         }
/*     */         else
/*     */         {
/* 193 */           absolute(this.curPosBfrWrite);
/*     */         }
/*     */       }
/*     */       else {
/* 197 */         throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidrd").toString());
/*     */       }
/*     */     } catch (Exception localException) {
/* 200 */       throw new SQLException(localException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readXml(InputStream paramInputStream)
/*     */     throws SQLException, IOException
/*     */   {
/* 213 */     if (paramInputStream != null) {
/* 214 */       this.xmlReader.readXML(this, paramInputStream);
/*     */ 
/* 219 */       if (this.curPosBfrWrite == 0) {
/* 220 */         beforeFirst();
/*     */       }
/*     */       else
/*     */       {
/* 225 */         absolute(this.curPosBfrWrite);
/*     */       }
/*     */     }
/*     */     else {
/* 229 */       throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidrd").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeXml(OutputStream paramOutputStream)
/*     */     throws SQLException, IOException
/*     */   {
/* 243 */     if (this.xmlWriter != null)
/*     */     {
/* 246 */       this.curPosBfrWrite = getRow();
/*     */ 
/* 248 */       this.xmlWriter.writeXML(this, paramOutputStream);
/*     */     } else {
/* 250 */       throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidwr").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeXml(ResultSet paramResultSet, OutputStream paramOutputStream)
/*     */     throws SQLException, IOException
/*     */   {
/* 265 */     populate(paramResultSet);
/*     */ 
/* 268 */     this.curPosBfrWrite = getRow();
/*     */ 
/* 270 */     writeXml(paramOutputStream);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 280 */     paramObjectInputStream.defaultReadObject();
/*     */     try
/*     */     {
/* 283 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 285 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.WebRowSetImpl
 * JD-Core Version:    0.6.2
 */