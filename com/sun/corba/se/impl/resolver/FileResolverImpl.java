/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.resolver.Resolver;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashSet;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class FileResolverImpl
/*    */   implements Resolver
/*    */ {
/*    */   private ORB orb;
/*    */   private File file;
/*    */   private Properties savedProps;
/* 49 */   private long fileModified = 0L;
/*    */ 
/*    */   public FileResolverImpl(ORB paramORB, File paramFile)
/*    */   {
/* 53 */     this.orb = paramORB;
/* 54 */     this.file = paramFile;
/* 55 */     this.savedProps = new Properties();
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 60 */     check();
/* 61 */     String str = this.savedProps.getProperty(paramString);
/* 62 */     if (str == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     return this.orb.string_to_object(str);
/*    */   }
/*    */ 
/*    */   public Set list()
/*    */   {
/* 70 */     check();
/*    */ 
/* 72 */     HashSet localHashSet = new HashSet();
/*    */ 
/* 75 */     Enumeration localEnumeration = this.savedProps.propertyNames();
/* 76 */     while (localEnumeration.hasMoreElements()) {
/* 77 */       localHashSet.add(localEnumeration.nextElement());
/*    */     }
/*    */ 
/* 80 */     return localHashSet;
/*    */   }
/*    */ 
/*    */   private void check()
/*    */   {
/* 89 */     if (this.file == null) {
/* 90 */       return;
/*    */     }
/* 92 */     long l = this.file.lastModified();
/* 93 */     if (l > this.fileModified)
/*    */       try {
/* 95 */         FileInputStream localFileInputStream = new FileInputStream(this.file);
/* 96 */         this.savedProps.clear();
/* 97 */         this.savedProps.load(localFileInputStream);
/* 98 */         localFileInputStream.close();
/* 99 */         this.fileModified = l;
/*    */       } catch (FileNotFoundException localFileNotFoundException) {
/* 101 */         System.err.println(CorbaResourceUtil.getText("bootstrap.filenotfound", this.file.getAbsolutePath()));
/*    */       }
/*    */       catch (IOException localIOException) {
/* 104 */         System.err.println(CorbaResourceUtil.getText("bootstrap.exception", this.file.getAbsolutePath(), localIOException.toString()));
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.FileResolverImpl
 * JD-Core Version:    0.6.2
 */