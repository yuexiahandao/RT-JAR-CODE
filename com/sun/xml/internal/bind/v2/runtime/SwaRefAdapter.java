/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*    */ import javax.activation.DataHandler;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*    */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*    */ 
/*    */ public final class SwaRefAdapter extends XmlAdapter<String, DataHandler>
/*    */ {
/*    */   public DataHandler unmarshal(String cid)
/*    */   {
/* 60 */     AttachmentUnmarshaller au = UnmarshallingContext.getInstance().parent.getAttachmentUnmarshaller();
/*    */ 
/* 62 */     return au.getAttachmentAsDataHandler(cid);
/*    */   }
/*    */ 
/*    */   public String marshal(DataHandler data) {
/* 66 */     if (data == null) return null;
/* 67 */     AttachmentMarshaller am = XMLSerializer.getInstance().attachmentMarshaller;
/*    */ 
/* 69 */     return am.addSwaRefAttachment(data);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.SwaRefAdapter
 * JD-Core Version:    0.6.2
 */