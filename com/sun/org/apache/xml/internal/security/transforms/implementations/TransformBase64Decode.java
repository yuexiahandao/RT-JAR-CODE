/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class TransformBase64Decode extends TransformSpi
/*     */ {
/*     */   public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#base64";
/*     */ 
/*     */   protected String engineGetURI()
/*     */   {
/*  85 */     return "http://www.w3.org/2000/09/xmldsig#base64";
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*     */     throws IOException, CanonicalizationException, TransformationException
/*     */   {
/* 102 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform)
/*     */     throws IOException, CanonicalizationException, TransformationException
/*     */   {
/*     */     try
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/*     */       Object localObject3;
/* 110 */       if (paramXMLSignatureInput.isElement()) {
/* 111 */         localObject1 = paramXMLSignatureInput.getSubNode();
/* 112 */         if (paramXMLSignatureInput.getSubNode().getNodeType() == 3) {
/* 113 */           localObject1 = ((Node)localObject1).getParentNode();
/*     */         }
/* 115 */         localObject2 = new StringBuffer();
/* 116 */         traverseElement((Element)localObject1, (StringBuffer)localObject2);
/* 117 */         if (paramOutputStream == null) {
/* 118 */           localObject3 = Base64.decode(((StringBuffer)localObject2).toString());
/* 119 */           return new XMLSignatureInput((byte[])localObject3);
/*     */         }
/* 121 */         Base64.decode(((StringBuffer)localObject2).toString(), paramOutputStream);
/* 122 */         localObject3 = new XMLSignatureInput((byte[])null);
/* 123 */         ((XMLSignatureInput)localObject3).setOutputStream(paramOutputStream);
/* 124 */         return localObject3;
/*     */       }
/*     */ 
/* 127 */       if ((paramXMLSignatureInput.isOctetStream()) || (paramXMLSignatureInput.isNodeSet()))
/*     */       {
/* 130 */         if (paramOutputStream == null) {
/* 131 */           localObject1 = paramXMLSignatureInput.getBytes();
/* 132 */           localObject2 = Base64.decode((byte[])localObject1);
/* 133 */           return new XMLSignatureInput((byte[])localObject2);
/*     */         }
/* 135 */         if ((paramXMLSignatureInput.isByteArray()) || (paramXMLSignatureInput.isNodeSet()))
/* 136 */           Base64.decode(paramXMLSignatureInput.getBytes(), paramOutputStream);
/*     */         else {
/* 138 */           Base64.decode(new BufferedInputStream(paramXMLSignatureInput.getOctetStreamReal()), paramOutputStream);
/*     */         }
/*     */ 
/* 141 */         localObject1 = new XMLSignatureInput((byte[])null);
/* 142 */         ((XMLSignatureInput)localObject1).setOutputStream(paramOutputStream);
/* 143 */         return localObject1;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 151 */         localObject1 = DocumentBuilderFactory.newInstance();
/* 152 */         ((DocumentBuilderFactory)localObject1).setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 154 */         localObject2 = ((DocumentBuilderFactory)localObject1).newDocumentBuilder().parse(paramXMLSignatureInput.getOctetStream());
/*     */ 
/* 157 */         localObject3 = ((Document)localObject2).getDocumentElement();
/* 158 */         StringBuffer localStringBuffer = new StringBuffer();
/* 159 */         traverseElement((Element)localObject3, localStringBuffer);
/* 160 */         byte[] arrayOfByte = Base64.decode(localStringBuffer.toString());
/*     */ 
/* 162 */         return new XMLSignatureInput(arrayOfByte);
/*     */       } catch (ParserConfigurationException localParserConfigurationException) {
/* 164 */         throw new TransformationException("c14n.Canonicalizer.Exception", localParserConfigurationException);
/*     */       } catch (SAXException localSAXException) {
/* 166 */         throw new TransformationException("SAX exception", localSAXException);
/*     */       }
/*     */     } catch (Base64DecodingException localBase64DecodingException) {
/* 169 */       throw new TransformationException("Base64Decoding", localBase64DecodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void traverseElement(Element paramElement, StringBuffer paramStringBuffer) {
/* 174 */     Node localNode = paramElement.getFirstChild();
/* 175 */     while (localNode != null) {
/* 176 */       switch (localNode.getNodeType()) {
/*     */       case 1:
/* 178 */         traverseElement((Element)localNode, paramStringBuffer);
/* 179 */         break;
/*     */       case 3:
/* 181 */         paramStringBuffer.append(((Text)localNode).getData());
/*     */       }
/* 183 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformBase64Decode
 * JD-Core Version:    0.6.2
 */