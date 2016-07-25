package com.sun.org.apache.bcel.internal.generic;

public abstract interface Visitor
{
  public abstract void visitStackInstruction(StackInstruction paramStackInstruction);

  public abstract void visitLocalVariableInstruction(LocalVariableInstruction paramLocalVariableInstruction);

  public abstract void visitBranchInstruction(BranchInstruction paramBranchInstruction);

  public abstract void visitLoadClass(LoadClass paramLoadClass);

  public abstract void visitFieldInstruction(FieldInstruction paramFieldInstruction);

  public abstract void visitIfInstruction(IfInstruction paramIfInstruction);

  public abstract void visitConversionInstruction(ConversionInstruction paramConversionInstruction);

  public abstract void visitPopInstruction(PopInstruction paramPopInstruction);

  public abstract void visitStoreInstruction(StoreInstruction paramStoreInstruction);

  public abstract void visitTypedInstruction(TypedInstruction paramTypedInstruction);

  public abstract void visitSelect(Select paramSelect);

  public abstract void visitJsrInstruction(JsrInstruction paramJsrInstruction);

  public abstract void visitGotoInstruction(GotoInstruction paramGotoInstruction);

  public abstract void visitUnconditionalBranch(UnconditionalBranch paramUnconditionalBranch);

  public abstract void visitPushInstruction(PushInstruction paramPushInstruction);

  public abstract void visitArithmeticInstruction(ArithmeticInstruction paramArithmeticInstruction);

  public abstract void visitCPInstruction(CPInstruction paramCPInstruction);

  public abstract void visitInvokeInstruction(InvokeInstruction paramInvokeInstruction);

  public abstract void visitArrayInstruction(ArrayInstruction paramArrayInstruction);

  public abstract void visitAllocationInstruction(AllocationInstruction paramAllocationInstruction);

  public abstract void visitReturnInstruction(ReturnInstruction paramReturnInstruction);

  public abstract void visitFieldOrMethod(FieldOrMethod paramFieldOrMethod);

  public abstract void visitConstantPushInstruction(ConstantPushInstruction paramConstantPushInstruction);

  public abstract void visitExceptionThrower(ExceptionThrower paramExceptionThrower);

  public abstract void visitLoadInstruction(LoadInstruction paramLoadInstruction);

  public abstract void visitVariableLengthInstruction(VariableLengthInstruction paramVariableLengthInstruction);

  public abstract void visitStackProducer(StackProducer paramStackProducer);

  public abstract void visitStackConsumer(StackConsumer paramStackConsumer);

  public abstract void visitACONST_NULL(ACONST_NULL paramACONST_NULL);

  public abstract void visitGETSTATIC(GETSTATIC paramGETSTATIC);

  public abstract void visitIF_ICMPLT(IF_ICMPLT paramIF_ICMPLT);

  public abstract void visitMONITOREXIT(MONITOREXIT paramMONITOREXIT);

  public abstract void visitIFLT(IFLT paramIFLT);

  public abstract void visitLSTORE(LSTORE paramLSTORE);

  public abstract void visitPOP2(POP2 paramPOP2);

  public abstract void visitBASTORE(BASTORE paramBASTORE);

  public abstract void visitISTORE(ISTORE paramISTORE);

  public abstract void visitCHECKCAST(CHECKCAST paramCHECKCAST);

  public abstract void visitFCMPG(FCMPG paramFCMPG);

  public abstract void visitI2F(I2F paramI2F);

  public abstract void visitATHROW(ATHROW paramATHROW);

  public abstract void visitDCMPL(DCMPL paramDCMPL);

  public abstract void visitARRAYLENGTH(ARRAYLENGTH paramARRAYLENGTH);

  public abstract void visitDUP(DUP paramDUP);

  public abstract void visitINVOKESTATIC(INVOKESTATIC paramINVOKESTATIC);

  public abstract void visitLCONST(LCONST paramLCONST);

  public abstract void visitDREM(DREM paramDREM);

  public abstract void visitIFGE(IFGE paramIFGE);

  public abstract void visitCALOAD(CALOAD paramCALOAD);

  public abstract void visitLASTORE(LASTORE paramLASTORE);

  public abstract void visitI2D(I2D paramI2D);

  public abstract void visitDADD(DADD paramDADD);

  public abstract void visitINVOKESPECIAL(INVOKESPECIAL paramINVOKESPECIAL);

  public abstract void visitIAND(IAND paramIAND);

  public abstract void visitPUTFIELD(PUTFIELD paramPUTFIELD);

  public abstract void visitILOAD(ILOAD paramILOAD);

  public abstract void visitDLOAD(DLOAD paramDLOAD);

  public abstract void visitDCONST(DCONST paramDCONST);

  public abstract void visitNEW(NEW paramNEW);

  public abstract void visitIFNULL(IFNULL paramIFNULL);

  public abstract void visitLSUB(LSUB paramLSUB);

  public abstract void visitL2I(L2I paramL2I);

  public abstract void visitISHR(ISHR paramISHR);

  public abstract void visitTABLESWITCH(TABLESWITCH paramTABLESWITCH);

  public abstract void visitIINC(IINC paramIINC);

  public abstract void visitDRETURN(DRETURN paramDRETURN);

  public abstract void visitFSTORE(FSTORE paramFSTORE);

  public abstract void visitDASTORE(DASTORE paramDASTORE);

  public abstract void visitIALOAD(IALOAD paramIALOAD);

  public abstract void visitDDIV(DDIV paramDDIV);

  public abstract void visitIF_ICMPGE(IF_ICMPGE paramIF_ICMPGE);

  public abstract void visitLAND(LAND paramLAND);

  public abstract void visitIDIV(IDIV paramIDIV);

  public abstract void visitLOR(LOR paramLOR);

  public abstract void visitCASTORE(CASTORE paramCASTORE);

  public abstract void visitFREM(FREM paramFREM);

  public abstract void visitLDC(LDC paramLDC);

  public abstract void visitBIPUSH(BIPUSH paramBIPUSH);

  public abstract void visitDSTORE(DSTORE paramDSTORE);

  public abstract void visitF2L(F2L paramF2L);

  public abstract void visitFMUL(FMUL paramFMUL);

  public abstract void visitLLOAD(LLOAD paramLLOAD);

  public abstract void visitJSR(JSR paramJSR);

  public abstract void visitFSUB(FSUB paramFSUB);

  public abstract void visitSASTORE(SASTORE paramSASTORE);

  public abstract void visitALOAD(ALOAD paramALOAD);

  public abstract void visitDUP2_X2(DUP2_X2 paramDUP2_X2);

  public abstract void visitRETURN(RETURN paramRETURN);

  public abstract void visitDALOAD(DALOAD paramDALOAD);

  public abstract void visitSIPUSH(SIPUSH paramSIPUSH);

  public abstract void visitDSUB(DSUB paramDSUB);

  public abstract void visitL2F(L2F paramL2F);

  public abstract void visitIF_ICMPGT(IF_ICMPGT paramIF_ICMPGT);

  public abstract void visitF2D(F2D paramF2D);

  public abstract void visitI2L(I2L paramI2L);

  public abstract void visitIF_ACMPNE(IF_ACMPNE paramIF_ACMPNE);

  public abstract void visitPOP(POP paramPOP);

  public abstract void visitI2S(I2S paramI2S);

  public abstract void visitIFEQ(IFEQ paramIFEQ);

  public abstract void visitSWAP(SWAP paramSWAP);

  public abstract void visitIOR(IOR paramIOR);

  public abstract void visitIREM(IREM paramIREM);

  public abstract void visitIASTORE(IASTORE paramIASTORE);

  public abstract void visitNEWARRAY(NEWARRAY paramNEWARRAY);

  public abstract void visitINVOKEINTERFACE(INVOKEINTERFACE paramINVOKEINTERFACE);

  public abstract void visitINEG(INEG paramINEG);

  public abstract void visitLCMP(LCMP paramLCMP);

  public abstract void visitJSR_W(JSR_W paramJSR_W);

  public abstract void visitMULTIANEWARRAY(MULTIANEWARRAY paramMULTIANEWARRAY);

  public abstract void visitDUP_X2(DUP_X2 paramDUP_X2);

  public abstract void visitSALOAD(SALOAD paramSALOAD);

  public abstract void visitIFNONNULL(IFNONNULL paramIFNONNULL);

  public abstract void visitDMUL(DMUL paramDMUL);

  public abstract void visitIFNE(IFNE paramIFNE);

  public abstract void visitIF_ICMPLE(IF_ICMPLE paramIF_ICMPLE);

  public abstract void visitLDC2_W(LDC2_W paramLDC2_W);

  public abstract void visitGETFIELD(GETFIELD paramGETFIELD);

  public abstract void visitLADD(LADD paramLADD);

  public abstract void visitNOP(NOP paramNOP);

  public abstract void visitFALOAD(FALOAD paramFALOAD);

  public abstract void visitINSTANCEOF(INSTANCEOF paramINSTANCEOF);

  public abstract void visitIFLE(IFLE paramIFLE);

  public abstract void visitLXOR(LXOR paramLXOR);

  public abstract void visitLRETURN(LRETURN paramLRETURN);

  public abstract void visitFCONST(FCONST paramFCONST);

  public abstract void visitIUSHR(IUSHR paramIUSHR);

  public abstract void visitBALOAD(BALOAD paramBALOAD);

  public abstract void visitDUP2(DUP2 paramDUP2);

  public abstract void visitIF_ACMPEQ(IF_ACMPEQ paramIF_ACMPEQ);

  public abstract void visitIMPDEP1(IMPDEP1 paramIMPDEP1);

  public abstract void visitMONITORENTER(MONITORENTER paramMONITORENTER);

  public abstract void visitLSHL(LSHL paramLSHL);

  public abstract void visitDCMPG(DCMPG paramDCMPG);

  public abstract void visitD2L(D2L paramD2L);

  public abstract void visitIMPDEP2(IMPDEP2 paramIMPDEP2);

  public abstract void visitL2D(L2D paramL2D);

  public abstract void visitRET(RET paramRET);

  public abstract void visitIFGT(IFGT paramIFGT);

  public abstract void visitIXOR(IXOR paramIXOR);

  public abstract void visitINVOKEVIRTUAL(INVOKEVIRTUAL paramINVOKEVIRTUAL);

  public abstract void visitFASTORE(FASTORE paramFASTORE);

  public abstract void visitIRETURN(IRETURN paramIRETURN);

  public abstract void visitIF_ICMPNE(IF_ICMPNE paramIF_ICMPNE);

  public abstract void visitFLOAD(FLOAD paramFLOAD);

  public abstract void visitLDIV(LDIV paramLDIV);

  public abstract void visitPUTSTATIC(PUTSTATIC paramPUTSTATIC);

  public abstract void visitAALOAD(AALOAD paramAALOAD);

  public abstract void visitD2I(D2I paramD2I);

  public abstract void visitIF_ICMPEQ(IF_ICMPEQ paramIF_ICMPEQ);

  public abstract void visitAASTORE(AASTORE paramAASTORE);

  public abstract void visitARETURN(ARETURN paramARETURN);

  public abstract void visitDUP2_X1(DUP2_X1 paramDUP2_X1);

  public abstract void visitFNEG(FNEG paramFNEG);

  public abstract void visitGOTO_W(GOTO_W paramGOTO_W);

  public abstract void visitD2F(D2F paramD2F);

  public abstract void visitGOTO(GOTO paramGOTO);

  public abstract void visitISUB(ISUB paramISUB);

  public abstract void visitF2I(F2I paramF2I);

  public abstract void visitDNEG(DNEG paramDNEG);

  public abstract void visitICONST(ICONST paramICONST);

  public abstract void visitFDIV(FDIV paramFDIV);

  public abstract void visitI2B(I2B paramI2B);

  public abstract void visitLNEG(LNEG paramLNEG);

  public abstract void visitLREM(LREM paramLREM);

  public abstract void visitIMUL(IMUL paramIMUL);

  public abstract void visitIADD(IADD paramIADD);

  public abstract void visitLSHR(LSHR paramLSHR);

  public abstract void visitLOOKUPSWITCH(LOOKUPSWITCH paramLOOKUPSWITCH);

  public abstract void visitDUP_X1(DUP_X1 paramDUP_X1);

  public abstract void visitFCMPL(FCMPL paramFCMPL);

  public abstract void visitI2C(I2C paramI2C);

  public abstract void visitLMUL(LMUL paramLMUL);

  public abstract void visitLUSHR(LUSHR paramLUSHR);

  public abstract void visitISHL(ISHL paramISHL);

  public abstract void visitLALOAD(LALOAD paramLALOAD);

  public abstract void visitASTORE(ASTORE paramASTORE);

  public abstract void visitANEWARRAY(ANEWARRAY paramANEWARRAY);

  public abstract void visitFRETURN(FRETURN paramFRETURN);

  public abstract void visitFADD(FADD paramFADD);

  public abstract void visitBREAKPOINT(BREAKPOINT paramBREAKPOINT);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.Visitor
 * JD-Core Version:    0.6.2
 */