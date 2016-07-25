/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDROutputStream;
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.spi.ior.Identifiable;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.WriteContents;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class EncapsulationUtility
/*     */ {
/*     */   public static void readIdentifiableSequence(List paramList, IdentifiableFactoryFinder paramIdentifiableFactoryFinder, InputStream paramInputStream)
/*     */   {
/*  70 */     int i = paramInputStream.read_long();
/*  71 */     for (int j = 0; j < i; j++) {
/*  72 */       int k = paramInputStream.read_long();
/*  73 */       Identifiable localIdentifiable = paramIdentifiableFactoryFinder.create(k, paramInputStream);
/*  74 */       paramList.add(localIdentifiable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void writeIdentifiableSequence(List paramList, OutputStream paramOutputStream)
/*     */   {
/*  83 */     paramOutputStream.write_long(paramList.size());
/*  84 */     Iterator localIterator = paramList.iterator();
/*  85 */     while (localIterator.hasNext()) {
/*  86 */       Identifiable localIdentifiable = (Identifiable)localIterator.next();
/*  87 */       paramOutputStream.write_long(localIdentifiable.getId());
/*  88 */       localIdentifiable.write(paramOutputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void writeOutputStream(OutputStream paramOutputStream1, OutputStream paramOutputStream2)
/*     */   {
/*  99 */     byte[] arrayOfByte = ((CDROutputStream)paramOutputStream1).toByteArray();
/* 100 */     paramOutputStream2.write_long(arrayOfByte.length);
/* 101 */     paramOutputStream2.write_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static InputStream getEncapsulationStream(InputStream paramInputStream)
/*     */   {
/* 112 */     byte[] arrayOfByte = readOctets(paramInputStream);
/* 113 */     EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(paramInputStream.orb(), arrayOfByte, arrayOfByte.length);
/*     */ 
/* 115 */     localEncapsInputStream.consumeEndian();
/* 116 */     return localEncapsInputStream;
/*     */   }
/*     */ 
/*     */   public static byte[] readOctets(InputStream paramInputStream)
/*     */   {
/* 124 */     int i = paramInputStream.read_ulong();
/* 125 */     byte[] arrayOfByte = new byte[i];
/* 126 */     paramInputStream.read_octet_array(arrayOfByte, 0, i);
/* 127 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static void writeEncapsulation(WriteContents paramWriteContents, OutputStream paramOutputStream)
/*     */   {
/* 133 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB)paramOutputStream.orb());
/*     */ 
/* 136 */     localEncapsOutputStream.putEndian();
/*     */ 
/* 138 */     paramWriteContents.writeContents(localEncapsOutputStream);
/*     */ 
/* 140 */     writeOutputStream(localEncapsOutputStream, paramOutputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.EncapsulationUtility
 * JD-Core Version:    0.6.2
 */