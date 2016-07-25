/*     */ package com.sun.corba.se.impl.ior.iiop;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
/*     */ import com.sun.corba.se.impl.encoding.MarshalInputStream;
/*     */ import com.sun.corba.se.impl.encoding.MarshalOutputStream;
/*     */ import com.sun.corba.se.spi.ior.TaggedComponentBase;
/*     */ import com.sun.corba.se.spi.ior.iiop.CodeSetsComponent;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public class CodeSetsComponentImpl extends TaggedComponentBase
/*     */   implements CodeSetsComponent
/*     */ {
/*     */   CodeSetComponentInfo csci;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  51 */     if (!(paramObject instanceof CodeSetsComponentImpl)) {
/*  52 */       return false;
/*     */     }
/*  54 */     CodeSetsComponentImpl localCodeSetsComponentImpl = (CodeSetsComponentImpl)paramObject;
/*     */ 
/*  56 */     return this.csci.equals(localCodeSetsComponentImpl.csci);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  61 */     return this.csci.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  66 */     return "CodeSetsComponentImpl[csci=" + this.csci + "]";
/*     */   }
/*     */ 
/*     */   public CodeSetsComponentImpl()
/*     */   {
/*  72 */     this.csci = new CodeSetComponentInfo();
/*     */   }
/*     */ 
/*     */   public CodeSetsComponentImpl(InputStream paramInputStream)
/*     */   {
/*  77 */     this.csci = new CodeSetComponentInfo();
/*  78 */     this.csci.read((MarshalInputStream)paramInputStream);
/*     */   }
/*     */ 
/*     */   public CodeSetsComponentImpl(ORB paramORB)
/*     */   {
/*  83 */     if (paramORB == null)
/*  84 */       this.csci = new CodeSetComponentInfo();
/*     */     else
/*  86 */       this.csci = paramORB.getORBData().getCodeSetComponentInfo();
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo getCodeSetComponentInfo()
/*     */   {
/*  91 */     return this.csci;
/*     */   }
/*     */ 
/*     */   public void writeContents(OutputStream paramOutputStream)
/*     */   {
/*  96 */     this.csci.write((MarshalOutputStream)paramOutputStream);
/*     */   }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 101 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.CodeSetsComponentImpl
 * JD-Core Version:    0.6.2
 */