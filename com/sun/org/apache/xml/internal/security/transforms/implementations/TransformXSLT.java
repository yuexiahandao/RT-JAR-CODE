/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerConfigurationException;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class TransformXSLT extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xslt-19991116";
/*    */   static final String XSLTSpecNS = "http://www.w3.org/1999/XSL/Transform";
/*    */   static final String defaultXSLTSpecNSprefix = "xslt";
/*    */   static final String XSLTSTYLESHEET = "stylesheet";
/* 66 */   static Logger log = Logger.getLogger(TransformXSLT.class.getName());
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 76 */     return "http://www.w3.org/TR/1999/REC-xslt-19991116";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws IOException, TransformationException
/*    */   {
/* 91 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform) throws IOException, TransformationException
/*    */   {
/*    */     try
/*    */     {
/* 98 */       Element localElement = paramTransform.getElement();
/*    */ 
/* 100 */       localObject1 = XMLUtils.selectNode(localElement.getFirstChild(), "http://www.w3.org/1999/XSL/Transform", "stylesheet", 0);
/*    */ 
/* 104 */       if (localObject1 == null) {
/* 105 */         localObject2 = new Object[] { "xslt:stylesheet", "Transform" };
/*    */ 
/* 107 */         throw new TransformationException("xml.WrongContent", (Object[])localObject2);
/*    */       }
/*    */ 
/* 110 */       Object localObject2 = TransformerFactory.newInstance();
/*    */ 
/* 113 */       ((TransformerFactory)localObject2).setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*    */ 
/* 121 */       StreamSource localStreamSource1 = new StreamSource(new ByteArrayInputStream(paramXMLSignatureInput.getBytes()));
/*    */ 
/* 134 */       Object localObject3 = new ByteArrayOutputStream();
/* 135 */       Transformer localTransformer = ((TransformerFactory)localObject2).newTransformer();
/* 136 */       Object localObject5 = new DOMSource((Node)localObject1);
/* 137 */       StreamResult localStreamResult = new StreamResult((OutputStream)localObject3);
/*    */ 
/* 139 */       localTransformer.transform((Source)localObject5, localStreamResult);
/*    */ 
/* 141 */       StreamSource localStreamSource2 = new StreamSource(new ByteArrayInputStream(((ByteArrayOutputStream)localObject3).toByteArray()));
/*    */ 
/* 145 */       localObject3 = ((TransformerFactory)localObject2).newTransformer(localStreamSource2);
/*    */       try
/*    */       {
/* 153 */         ((Transformer)localObject3).setOutputProperty("{http://xml.apache.org/xalan}line-separator", "\n");
/*    */       }
/*    */       catch (Exception localException) {
/* 156 */         log.log(Level.WARNING, "Unable to set Xalan line-separator property: " + localException.getMessage());
/*    */       }
/*    */ 
/* 160 */       if (paramOutputStream == null) {
/* 161 */         localObject4 = new ByteArrayOutputStream();
/* 162 */         localObject5 = new StreamResult((OutputStream)localObject4);
/* 163 */         ((Transformer)localObject3).transform(localStreamSource1, (Result)localObject5);
/* 164 */         return new XMLSignatureInput(((ByteArrayOutputStream)localObject4).toByteArray());
/*    */       }
/* 166 */       Object localObject4 = new StreamResult(paramOutputStream);
/*    */ 
/* 168 */       ((Transformer)localObject3).transform(localStreamSource1, (Result)localObject4);
/* 169 */       localObject5 = new XMLSignatureInput((byte[])null);
/* 170 */       ((XMLSignatureInput)localObject5).setOutputStream(paramOutputStream);
/* 171 */       return localObject5;
/*    */     } catch (XMLSecurityException localXMLSecurityException) {
/* 173 */       localObject1 = new Object[] { localXMLSecurityException.getMessage() };
/*    */ 
/* 175 */       throw new TransformationException("generic.EmptyMessage", (Object[])localObject1, localXMLSecurityException);
/*    */     } catch (TransformerConfigurationException localTransformerConfigurationException) {
/* 177 */       localObject1 = new Object[] { localTransformerConfigurationException.getMessage() };
/*    */ 
/* 179 */       throw new TransformationException("generic.EmptyMessage", (Object[])localObject1, localTransformerConfigurationException);
/*    */     } catch (TransformerException localTransformerException) {
/* 181 */       Object localObject1 = { localTransformerException.getMessage() };
/*    */ 
/* 183 */       throw new TransformationException("generic.EmptyMessage", (Object[])localObject1, localTransformerException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXSLT
 * JD-Core Version:    0.6.2
 */