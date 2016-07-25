/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class GeneralNames
/*     */ {
/*     */   private final List<GeneralName> names;
/*     */ 
/*     */   public GeneralNames(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  56 */     this();
/*  57 */     if (paramDerValue.tag != 48) {
/*  58 */       throw new IOException("Invalid encoding for GeneralNames.");
/*     */     }
/*  60 */     if (paramDerValue.data.available() == 0) {
/*  61 */       throw new IOException("No data available in passed DER encoded value.");
/*     */     }
/*     */ 
/*  65 */     while (paramDerValue.data.available() != 0) {
/*  66 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/*     */ 
/*  68 */       GeneralName localGeneralName = new GeneralName(localDerValue);
/*  69 */       add(localGeneralName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public GeneralNames()
/*     */   {
/*  77 */     this.names = new ArrayList();
/*     */   }
/*     */ 
/*     */   public GeneralNames add(GeneralName paramGeneralName) {
/*  81 */     if (paramGeneralName == null) {
/*  82 */       throw new NullPointerException();
/*     */     }
/*  84 */     this.names.add(paramGeneralName);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   public GeneralName get(int paramInt) {
/*  89 */     return (GeneralName)this.names.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  93 */     return this.names.isEmpty();
/*     */   }
/*     */ 
/*     */   public int size() {
/*  97 */     return this.names.size();
/*     */   }
/*     */ 
/*     */   public Iterator<GeneralName> iterator() {
/* 101 */     return this.names.iterator();
/*     */   }
/*     */ 
/*     */   public List<GeneralName> names() {
/* 105 */     return this.names;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 115 */     if (isEmpty()) {
/* 116 */       return;
/*     */     }
/*     */ 
/* 119 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 120 */     for (GeneralName localGeneralName : this.names) {
/* 121 */       localGeneralName.encode(localDerOutputStream);
/*     */     }
/* 123 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 132 */     if (this == paramObject) {
/* 133 */       return true;
/*     */     }
/* 135 */     if (!(paramObject instanceof GeneralNames)) {
/* 136 */       return false;
/*     */     }
/* 138 */     GeneralNames localGeneralNames = (GeneralNames)paramObject;
/* 139 */     return this.names.equals(localGeneralNames.names);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 143 */     return this.names.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 147 */     return this.names.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.GeneralNames
 * JD-Core Version:    0.6.2
 */