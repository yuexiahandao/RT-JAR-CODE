/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SnmpIndex
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8712159739982192146L;
/* 183 */   private Vector<SnmpOid> oids = new Vector();
/*     */ 
/* 189 */   private int size = 0;
/*     */ 
/*     */   public SnmpIndex(SnmpOid[] paramArrayOfSnmpOid)
/*     */   {
/*  66 */     this.size = paramArrayOfSnmpOid.length;
/*  67 */     for (int i = 0; i < this.size; i++)
/*     */     {
/*  70 */       this.oids.addElement(paramArrayOfSnmpOid[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpIndex(SnmpOid paramSnmpOid)
/*     */   {
/*  80 */     this.oids.addElement(paramSnmpOid);
/*  81 */     this.size = 1;
/*     */   }
/*     */ 
/*     */   public int getNbComponents()
/*     */   {
/*  90 */     return this.size;
/*     */   }
/*     */ 
/*     */   public Vector<SnmpOid> getComponents()
/*     */   {
/*  99 */     return this.oids;
/*     */   }
/*     */ 
/*     */   public boolean equals(SnmpIndex paramSnmpIndex)
/*     */   {
/* 111 */     if (this.size != paramSnmpIndex.getNbComponents()) {
/* 112 */       return false;
/*     */     }
/*     */ 
/* 119 */     Vector localVector = paramSnmpIndex.getComponents();
/* 120 */     for (int i = 0; i < this.size; i++) {
/* 121 */       SnmpOid localSnmpOid1 = (SnmpOid)this.oids.elementAt(i);
/* 122 */       SnmpOid localSnmpOid2 = (SnmpOid)localVector.elementAt(i);
/* 123 */       if (!localSnmpOid1.equals(localSnmpOid2))
/* 124 */         return false;
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   public int compareTo(SnmpIndex paramSnmpIndex)
/*     */   {
/* 138 */     int i = paramSnmpIndex.getNbComponents();
/* 139 */     Vector localVector = paramSnmpIndex.getComponents();
/*     */ 
/* 143 */     for (int k = 0; k < this.size; k++) {
/* 144 */       if (k > i)
/*     */       {
/* 147 */         return 1;
/*     */       }
/*     */ 
/* 151 */       SnmpOid localSnmpOid1 = (SnmpOid)this.oids.elementAt(k);
/* 152 */       SnmpOid localSnmpOid2 = (SnmpOid)localVector.elementAt(k);
/* 153 */       int j = localSnmpOid1.compareTo(localSnmpOid2);
/* 154 */       if (j != 0)
/*     */       {
/* 156 */         return j;
/*     */       }
/*     */     }
/* 158 */     return 0;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     StringBuffer localStringBuffer = new StringBuffer();
/* 169 */     for (Enumeration localEnumeration = this.oids.elements(); localEnumeration.hasMoreElements(); ) {
/* 170 */       SnmpOid localSnmpOid = (SnmpOid)localEnumeration.nextElement();
/* 171 */       localStringBuffer.append("//" + localSnmpOid.toString());
/*     */     }
/* 173 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpIndex
 * JD-Core Version:    0.6.2
 */