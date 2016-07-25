/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.PGPData;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMPGPData extends DOMStructure
/*     */   implements PGPData
/*     */ {
/*     */   private final byte[] keyId;
/*     */   private final byte[] keyPacket;
/*     */   private final List externalElements;
/*     */ 
/*     */   public DOMPGPData(byte[] paramArrayOfByte, List paramList)
/*     */   {
/*  71 */     if (paramArrayOfByte == null) {
/*  72 */       throw new NullPointerException("keyPacket cannot be null");
/*     */     }
/*  74 */     if ((paramList == null) || (paramList.isEmpty())) {
/*  75 */       this.externalElements = Collections.EMPTY_LIST;
/*     */     } else {
/*  77 */       ArrayList localArrayList = new ArrayList(paramList);
/*  78 */       int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  79 */         if (!(localArrayList.get(i) instanceof XMLStructure)) {
/*  80 */           throw new ClassCastException("other[" + i + "] is not a valid PGPData type");
/*     */         }
/*     */       }
/*     */ 
/*  84 */       this.externalElements = Collections.unmodifiableList(localArrayList);
/*     */     }
/*  86 */     this.keyPacket = ((byte[])paramArrayOfByte.clone());
/*  87 */     checkKeyPacket(paramArrayOfByte);
/*  88 */     this.keyId = null;
/*     */   }
/*     */ 
/*     */   public DOMPGPData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List paramList)
/*     */   {
/* 112 */     if (paramArrayOfByte1 == null) {
/* 113 */       throw new NullPointerException("keyId cannot be null");
/*     */     }
/*     */ 
/* 116 */     if (paramArrayOfByte1.length != 8) {
/* 117 */       throw new IllegalArgumentException("keyId must be 8 bytes long");
/*     */     }
/* 119 */     if ((paramList == null) || (paramList.isEmpty())) {
/* 120 */       this.externalElements = Collections.EMPTY_LIST;
/*     */     } else {
/* 122 */       ArrayList localArrayList = new ArrayList(paramList);
/* 123 */       int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/* 124 */         if (!(localArrayList.get(i) instanceof XMLStructure)) {
/* 125 */           throw new ClassCastException("other[" + i + "] is not a valid PGPData type");
/*     */         }
/*     */       }
/*     */ 
/* 129 */       this.externalElements = Collections.unmodifiableList(localArrayList);
/*     */     }
/* 131 */     this.keyId = ((byte[])paramArrayOfByte1.clone());
/* 132 */     this.keyPacket = (paramArrayOfByte2 == null ? null : (byte[])paramArrayOfByte2.clone());
/* 133 */     if (paramArrayOfByte2 != null)
/* 134 */       checkKeyPacket(paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   public DOMPGPData(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/* 145 */     byte[] arrayOfByte1 = null;
/* 146 */     byte[] arrayOfByte2 = null;
/* 147 */     NodeList localNodeList = paramElement.getChildNodes();
/* 148 */     int i = localNodeList.getLength();
/* 149 */     ArrayList localArrayList = new ArrayList(i);
/* 150 */     for (int j = 0; j < i; j++) {
/* 151 */       Node localNode = localNodeList.item(j);
/* 152 */       if (localNode.getNodeType() == 1) {
/* 153 */         Element localElement = (Element)localNode;
/* 154 */         String str = localElement.getLocalName();
/*     */         try {
/* 156 */           if (str.equals("PGPKeyID"))
/* 157 */             arrayOfByte1 = Base64.decode(localElement);
/* 158 */           else if (str.equals("PGPKeyPacket"))
/* 159 */             arrayOfByte2 = Base64.decode(localElement);
/*     */           else
/* 161 */             localArrayList.add(new javax.xml.crypto.dom.DOMStructure(localElement));
/*     */         }
/*     */         catch (Base64DecodingException localBase64DecodingException)
/*     */         {
/* 165 */           throw new MarshalException(localBase64DecodingException);
/*     */         }
/*     */       }
/*     */     }
/* 169 */     this.keyId = arrayOfByte1;
/* 170 */     this.keyPacket = arrayOfByte2;
/* 171 */     this.externalElements = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public byte[] getKeyId() {
/* 175 */     return this.keyId == null ? null : (byte[])this.keyId.clone();
/*     */   }
/*     */ 
/*     */   public byte[] getKeyPacket() {
/* 179 */     return this.keyPacket == null ? null : (byte[])this.keyPacket.clone();
/*     */   }
/*     */ 
/*     */   public List getExternalElements() {
/* 183 */     return this.externalElements;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 188 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 190 */     Element localElement1 = DOMUtils.createElement(localDocument, "PGPData", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     Element localElement2;
/* 194 */     if (this.keyId != null) {
/* 195 */       localElement2 = DOMUtils.createElement(localDocument, "PGPKeyID", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 197 */       localElement2.appendChild(localDocument.createTextNode(Base64.encode(this.keyId)));
/*     */ 
/* 199 */       localElement1.appendChild(localElement2);
/*     */     }
/*     */ 
/* 203 */     if (this.keyPacket != null) {
/* 204 */       localElement2 = DOMUtils.createElement(localDocument, "PGPKeyPacket", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 206 */       localElement2.appendChild(localDocument.createTextNode(Base64.encode(this.keyPacket)));
/*     */ 
/* 208 */       localElement1.appendChild(localElement2);
/*     */     }
/*     */ 
/* 212 */     int i = 0; for (int j = this.externalElements.size(); i < j; i++) {
/* 213 */       DOMUtils.appendChild(localElement1, ((javax.xml.crypto.dom.DOMStructure)this.externalElements.get(i)).getNode());
/*     */     }
/*     */ 
/* 217 */     paramNode.appendChild(localElement1);
/*     */   }
/*     */ 
/*     */   private void checkKeyPacket(byte[] paramArrayOfByte)
/*     */   {
/* 230 */     if (paramArrayOfByte.length < 3) {
/* 231 */       throw new IllegalArgumentException("keypacket must be at least 3 bytes long");
/*     */     }
/*     */ 
/* 235 */     int i = paramArrayOfByte[0];
/*     */ 
/* 237 */     if ((i & 0x80) != 128) {
/* 238 */       throw new IllegalArgumentException("keypacket tag is invalid: bit 7 is not set");
/*     */     }
/*     */ 
/* 242 */     if ((i & 0x40) != 64) {
/* 243 */       throw new IllegalArgumentException("old keypacket tag format is unsupported");
/*     */     }
/*     */ 
/* 248 */     if (((i & 0x6) != 6) && ((i & 0xE) != 14) && ((i & 0x5) != 5) && ((i & 0x7) != 7))
/*     */     {
/* 250 */       throw new IllegalArgumentException("keypacket tag is invalid: must be 6, 14, 5, or 7");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMPGPData
 * JD-Core Version:    0.6.2
 */