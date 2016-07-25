/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class XMLDTDDescription extends XMLResourceIdentifierImpl
/*     */   implements com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription
/*     */ {
/*  87 */   protected String fRootName = null;
/*     */ 
/*  91 */   protected ArrayList fPossibleRoots = null;
/*     */ 
/*     */   public XMLDTDDescription(XMLResourceIdentifier id, String rootName)
/*     */   {
/*  95 */     setValues(id.getPublicId(), id.getLiteralSystemId(), id.getBaseSystemId(), id.getExpandedSystemId());
/*     */ 
/*  97 */     this.fRootName = rootName;
/*  98 */     this.fPossibleRoots = null;
/*     */   }
/*     */ 
/*     */   public XMLDTDDescription(String publicId, String literalId, String baseId, String expandedId, String rootName)
/*     */   {
/* 103 */     setValues(publicId, literalId, baseId, expandedId);
/* 104 */     this.fRootName = rootName;
/* 105 */     this.fPossibleRoots = null;
/*     */   }
/*     */ 
/*     */   public XMLDTDDescription(XMLInputSource source) {
/* 109 */     setValues(source.getPublicId(), null, source.getBaseSystemId(), source.getSystemId());
/*     */ 
/* 111 */     this.fRootName = null;
/* 112 */     this.fPossibleRoots = null;
/*     */   }
/*     */ 
/*     */   public String getGrammarType()
/*     */   {
/* 118 */     return "http://www.w3.org/TR/REC-xml";
/*     */   }
/*     */ 
/*     */   public String getRootName()
/*     */   {
/* 125 */     return this.fRootName;
/*     */   }
/*     */ 
/*     */   public void setRootName(String rootName)
/*     */   {
/* 130 */     this.fRootName = rootName;
/* 131 */     this.fPossibleRoots = null;
/*     */   }
/*     */ 
/*     */   public void setPossibleRoots(ArrayList possibleRoots)
/*     */   {
/* 136 */     this.fPossibleRoots = possibleRoots;
/*     */   }
/*     */ 
/*     */   public void setPossibleRoots(Vector possibleRoots)
/*     */   {
/* 141 */     this.fPossibleRoots = (possibleRoots != null ? new ArrayList(possibleRoots) : null);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object desc)
/*     */   {
/* 158 */     if (!(desc instanceof XMLGrammarDescription)) return false;
/* 159 */     if (!getGrammarType().equals(((XMLGrammarDescription)desc).getGrammarType())) {
/* 160 */       return false;
/*     */     }
/*     */ 
/* 163 */     XMLDTDDescription dtdDesc = (XMLDTDDescription)desc;
/* 164 */     if (this.fRootName != null) {
/* 165 */       if ((dtdDesc.fRootName != null) && (!dtdDesc.fRootName.equals(this.fRootName))) {
/* 166 */         return false;
/*     */       }
/* 168 */       if ((dtdDesc.fPossibleRoots != null) && (!dtdDesc.fPossibleRoots.contains(this.fRootName))) {
/* 169 */         return false;
/*     */       }
/*     */     }
/* 172 */     else if (this.fPossibleRoots != null) {
/* 173 */       if (dtdDesc.fRootName != null) {
/* 174 */         if (!this.fPossibleRoots.contains(dtdDesc.fRootName))
/* 175 */           return false;
/*     */       }
/*     */       else {
/* 178 */         if (dtdDesc.fPossibleRoots == null) {
/* 179 */           return false;
/*     */         }
/*     */ 
/* 182 */         boolean found = false;
/* 183 */         int size = this.fPossibleRoots.size();
/* 184 */         for (int i = 0; i < size; i++) {
/* 185 */           String root = (String)this.fPossibleRoots.get(i);
/* 186 */           found = dtdDesc.fPossibleRoots.contains(root);
/* 187 */           if (found) break;
/*     */         }
/* 189 */         if (!found) return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 194 */     if (this.fExpandedSystemId != null) {
/* 195 */       if (!this.fExpandedSystemId.equals(dtdDesc.fExpandedSystemId)) {
/* 196 */         return false;
/*     */       }
/*     */     }
/* 199 */     else if (dtdDesc.fExpandedSystemId != null) {
/* 200 */       return false;
/*     */     }
/* 202 */     if (this.fPublicId != null) {
/* 203 */       if (!this.fPublicId.equals(dtdDesc.fPublicId)) {
/* 204 */         return false;
/*     */       }
/*     */     }
/* 207 */     else if (dtdDesc.fPublicId != null) {
/* 208 */       return false;
/*     */     }
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 220 */     if (this.fExpandedSystemId != null) {
/* 221 */       return this.fExpandedSystemId.hashCode();
/*     */     }
/* 223 */     if (this.fPublicId != null) {
/* 224 */       return this.fPublicId.hashCode();
/*     */     }
/*     */ 
/* 227 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDDescription
 * JD-Core Version:    0.6.2
 */