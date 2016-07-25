/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.Identifiable;
/*    */ import java.util.Arrays;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public abstract class GenericIdentifiable
/*    */   implements Identifiable
/*    */ {
/*    */   private int id;
/*    */   private byte[] data;
/*    */ 
/*    */   public GenericIdentifiable(int paramInt, InputStream paramInputStream)
/*    */   {
/* 46 */     this.id = paramInt;
/* 47 */     this.data = EncapsulationUtility.readOctets(paramInputStream);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 52 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 57 */     paramOutputStream.write_ulong(this.data.length);
/* 58 */     paramOutputStream.write_octet_array(this.data, 0, this.data.length);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     return "GenericIdentifiable[id=" + getId() + "]";
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 68 */     if (paramObject == null) {
/* 69 */       return false;
/*    */     }
/* 71 */     if (!(paramObject instanceof GenericIdentifiable)) {
/* 72 */       return false;
/*    */     }
/* 74 */     GenericIdentifiable localGenericIdentifiable = (GenericIdentifiable)paramObject;
/*    */ 
/* 76 */     return (getId() == localGenericIdentifiable.getId()) && (Arrays.equals(getData(), localGenericIdentifiable.getData()));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 82 */     int i = 17;
/* 83 */     for (int j = 0; j < this.data.length; j++)
/* 84 */       i = 37 * i + this.data[j];
/* 85 */     return i;
/*    */   }
/*    */ 
/*    */   public GenericIdentifiable(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 90 */     this.id = paramInt;
/* 91 */     this.data = ((byte[])paramArrayOfByte.clone());
/*    */   }
/*    */ 
/*    */   public byte[] getData()
/*    */   {
/* 96 */     return this.data;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.GenericIdentifiable
 * JD-Core Version:    0.6.2
 */