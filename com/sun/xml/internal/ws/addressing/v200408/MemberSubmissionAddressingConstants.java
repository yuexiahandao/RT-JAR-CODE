/*    */ package com.sun.xml.internal.ws.addressing.v200408;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface MemberSubmissionAddressingConstants
/*    */ {
/*    */   public static final String WSA_NAMESPACE_NAME = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
/*    */   public static final String WSA_NAMESPACE_WSDL_NAME = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
/*    */   public static final String WSA_NAMESPACE_POLICY_NAME = "http://schemas.xmlsoap.org/ws/2004/08/addressing/policy";
/*    */   public static final String WSA_SERVICENAME_NAME = "ServiceName";
/*    */   public static final String WSA_PORTTYPE_NAME = "PortType";
/*    */   public static final String WSA_PORTNAME_NAME = "PortName";
/*    */   public static final String WSA_ADDRESS_NAME = "Address";
/* 45 */   public static final QName WSA_ADDRESS_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address");
/*    */   public static final String WSA_ANONYMOUS_ADDRESS = "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
/*    */   public static final String WSA_NONE_ADDRESS = "";
/*    */   public static final String WSA_DEFAULT_FAULT_ACTION = "http://schemas.xmlsoap.org/ws/2004/08/addressing/fault";
/* 52 */   public static final QName INVALID_MAP_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "InvalidMessageInformationHeader");
/* 53 */   public static final QName MAP_REQUIRED_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "MessageInformationHeaderRequired");
/* 54 */   public static final QName DESTINATION_UNREACHABLE_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "DestinationUnreachable");
/* 55 */   public static final QName ACTION_NOT_SUPPORTED_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ActionNotSupported");
/* 56 */   public static final QName ENDPOINT_UNAVAILABLE_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "EndpointUnavailable");
/*    */   public static final String ACTION_NOT_SUPPORTED_TEXT = "The \"%s\" cannot be processed at the receiver.";
/*    */   public static final String DESTINATION_UNREACHABLE_TEXT = "No route can be determined to reach the destination role defined by the WS-Addressing To.";
/*    */   public static final String ENDPOINT_UNAVAILABLE_TEXT = "The endpoint is unable to process the message at this time.";
/*    */   public static final String INVALID_MAP_TEXT = "A message information header is not valid and the message cannot be processed.";
/*    */   public static final String MAP_REQUIRED_TEXT = "A required message information header, To, MessageID, or Action, is not present.";
/* 64 */   public static final QName PROBLEM_ACTION_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ProblemAction");
/* 65 */   public static final QName PROBLEM_HEADER_QNAME_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ProblemHeaderQName");
/* 66 */   public static final QName FAULT_DETAIL_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "FaultDetail");
/*    */   public static final String ANONYMOUS_EPR = "<EndpointReference xmlns=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">\n    <Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</Address>\n</EndpointReference>";
/* 73 */   public static final QName MEX_METADATA = new QName("http://schemas.xmlsoap.org/ws/2004/09/mex", "Metadata", "mex");
/* 74 */   public static final QName MEX_METADATA_SECTION = new QName("http://schemas.xmlsoap.org/ws/2004/09/mex", "MetadataSection", "mex");
/*    */   public static final String MEX_METADATA_DIALECT_ATTRIBUTE = "Dialect";
/*    */   public static final String MEX_METADATA_DIALECT_VALUE = "http://schemas.xmlsoap.org/wsdl/";
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants
 * JD-Core Version:    0.6.2
 */