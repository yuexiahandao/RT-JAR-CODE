/*     */ package javax.xml.ws;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.ws.spi.Provider;
/*     */ 
/*     */ @XmlTransient
/*     */ public abstract class EndpointReference
/*     */ {
/*     */   public static EndpointReference readFrom(Source eprInfoset)
/*     */   {
/* 109 */     return Provider.provider().readEndpointReference(eprInfoset);
/*     */   }
/*     */ 
/*     */   public abstract void writeTo(Result paramResult);
/*     */ 
/*     */   public <T> T getPort(Class<T> serviceEndpointInterface, WebServiceFeature[] features)
/*     */   {
/* 180 */     return Provider.provider().getPort(this, serviceEndpointInterface, features);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     StringWriter w = new StringWriter();
/* 189 */     writeTo(new StreamResult(w));
/* 190 */     return w.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.EndpointReference
 * JD-Core Version:    0.6.2
 */