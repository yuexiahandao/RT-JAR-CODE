/*      */
package java.lang;

/*      */
/*      */ class CharacterData00 extends CharacterData
/*      */ {
    /*      */   static final CharacterData00 instance;
    /*      */   static final char[][][] charMap;
    /*      */   static final char[] X;
    /*      */   static final char[] Y;
    /*      */   static final int[] A;
    /*      */   static final String A_DATA = "";
    /*      */   static final char[] B;

    /*      */
/*      */   int getProperties(int paramInt)
/*      */ {
/*   72 */
        int i = (char) paramInt;
/*   73 */
        int j = A[(Y[(X[(i >> 5)] | i >> 1 & 0xF)] | i & 0x1)];
/*   74 */
        return j;
/*      */
    }

    /*      */
/*      */   int getPropertiesEx(int paramInt) {
/*   78 */
        int i = (char) paramInt;
/*   79 */
        int j = B[(Y[(X[(i >> 5)] | i >> 1 & 0xF)] | i & 0x1)];
/*   80 */
        return j;
/*      */
    }

    /*      */
/*      */   int getType(int paramInt) {
/*   84 */
        int i = getProperties(paramInt);
/*   85 */
        return i & 0x1F;
/*      */
    }

    /*      */
/*      */   boolean isOtherLowercase(int paramInt) {
/*   89 */
        int i = getPropertiesEx(paramInt);
/*   90 */
        return (i & 0x1) != 0;
/*      */
    }

    /*      */
/*      */   boolean isOtherUppercase(int paramInt) {
/*   94 */
        int i = getPropertiesEx(paramInt);
/*   95 */
        return (i & 0x2) != 0;
/*      */
    }

    /*      */
/*      */   boolean isOtherAlphabetic(int paramInt) {
/*   99 */
        int i = getPropertiesEx(paramInt);
/*  100 */
        return (i & 0x4) != 0;
/*      */
    }

    /*      */
/*      */   boolean isIdeographic(int paramInt) {
/*  104 */
        int i = getPropertiesEx(paramInt);
/*  105 */
        return (i & 0x10) != 0;
/*      */
    }

    /*      */
/*      */   boolean isJavaIdentifierStart(int paramInt) {
/*  109 */
        int i = getProperties(paramInt);
/*  110 */
        return (i & 0x7000) >= 20480;
/*      */
    }

    /*      */
/*      */   boolean isJavaIdentifierPart(int paramInt) {
/*  114 */
        int i = getProperties(paramInt);
/*  115 */
        return (i & 0x3000) != 0;
/*      */
    }

    /*      */
/*      */   boolean isUnicodeIdentifierStart(int paramInt) {
/*  119 */
        int i = getProperties(paramInt);
/*  120 */
        return (i & 0x7000) == 28672;
/*      */
    }

    /*      */
/*      */   boolean isUnicodeIdentifierPart(int paramInt) {
/*  124 */
        int i = getProperties(paramInt);
/*  125 */
        return (i & 0x1000) != 0;
/*      */
    }

    /*      */
/*      */   boolean isIdentifierIgnorable(int paramInt) {
/*  129 */
        int i = getProperties(paramInt);
/*  130 */
        return (i & 0x7000) == 4096;
/*      */
    }

    /*      */
/*      */   int toLowerCase(int paramInt) {
/*  134 */
        int i = paramInt;
/*  135 */
        int j = getProperties(paramInt);
/*      */ 
/*  137 */
        if ((j & 0x20000) != 0) {
/*  138 */
            if ((j & 0x7FC0000) == 133955584) {
/*  139 */
                switch (paramInt) {
/*      */
                    case 304:
/*  141 */
                        i = 105;
                        break;
/*      */
                    case 8486:
/*  142 */
                        i = 969;
                        break;
/*      */
                    case 8490:
/*  143 */
                        i = 107;
                        break;
/*      */
                    case 8491:
/*  144 */
                        i = 229;
                        break;
/*      */
                    case 8072:
/*  147 */
                        i = 8064;
                        break;
/*      */
                    case 8073:
/*  148 */
                        i = 8065;
                        break;
/*      */
                    case 8074:
/*  149 */
                        i = 8066;
                        break;
/*      */
                    case 8075:
/*  150 */
                        i = 8067;
                        break;
/*      */
                    case 8076:
/*  151 */
                        i = 8068;
                        break;
/*      */
                    case 8077:
/*  152 */
                        i = 8069;
                        break;
/*      */
                    case 8078:
/*  153 */
                        i = 8070;
                        break;
/*      */
                    case 8079:
/*  154 */
                        i = 8071;
                        break;
/*      */
                    case 8088:
/*  155 */
                        i = 8080;
                        break;
/*      */
                    case 8089:
/*  156 */
                        i = 8081;
                        break;
/*      */
                    case 8090:
/*  157 */
                        i = 8082;
                        break;
/*      */
                    case 8091:
/*  158 */
                        i = 8083;
                        break;
/*      */
                    case 8092:
/*  159 */
                        i = 8084;
                        break;
/*      */
                    case 8093:
/*  160 */
                        i = 8085;
                        break;
/*      */
                    case 8094:
/*  161 */
                        i = 8086;
                        break;
/*      */
                    case 8095:
/*  162 */
                        i = 8087;
                        break;
/*      */
                    case 8104:
/*  163 */
                        i = 8096;
                        break;
/*      */
                    case 8105:
/*  164 */
                        i = 8097;
                        break;
/*      */
                    case 8106:
/*  165 */
                        i = 8098;
                        break;
/*      */
                    case 8107:
/*  166 */
                        i = 8099;
                        break;
/*      */
                    case 8108:
/*  167 */
                        i = 8100;
                        break;
/*      */
                    case 8109:
/*  168 */
                        i = 8101;
                        break;
/*      */
                    case 8110:
/*  169 */
                        i = 8102;
                        break;
/*      */
                    case 8111:
/*  170 */
                        i = 8103;
                        break;
/*      */
                    case 8124:
/*  171 */
                        i = 8115;
                        break;
/*      */
                    case 8140:
/*  172 */
                        i = 8131;
                        break;
/*      */
                    case 8188:
/*  173 */
                        i = 8179;
                        break;
/*      */
                    case 570:
/*  175 */
                        i = 11365;
                        break;
/*      */
                    case 574:
/*  176 */
                        i = 11366;
                        break;
/*      */
                    case 4256:
/*  177 */
                        i = 11520;
                        break;
/*      */
                    case 4257:
/*  178 */
                        i = 11521;
                        break;
/*      */
                    case 4258:
/*  179 */
                        i = 11522;
                        break;
/*      */
                    case 4259:
/*  180 */
                        i = 11523;
                        break;
/*      */
                    case 4260:
/*  181 */
                        i = 11524;
                        break;
/*      */
                    case 4261:
/*  182 */
                        i = 11525;
                        break;
/*      */
                    case 4262:
/*  183 */
                        i = 11526;
                        break;
/*      */
                    case 4263:
/*  184 */
                        i = 11527;
                        break;
/*      */
                    case 4264:
/*  185 */
                        i = 11528;
                        break;
/*      */
                    case 4265:
/*  186 */
                        i = 11529;
                        break;
/*      */
                    case 4266:
/*  187 */
                        i = 11530;
                        break;
/*      */
                    case 4267:
/*  188 */
                        i = 11531;
                        break;
/*      */
                    case 4268:
/*  189 */
                        i = 11532;
                        break;
/*      */
                    case 4269:
/*  190 */
                        i = 11533;
                        break;
/*      */
                    case 4270:
/*  191 */
                        i = 11534;
                        break;
/*      */
                    case 4271:
/*  192 */
                        i = 11535;
                        break;
/*      */
                    case 4272:
/*  193 */
                        i = 11536;
                        break;
/*      */
                    case 4273:
/*  194 */
                        i = 11537;
                        break;
/*      */
                    case 4274:
/*  195 */
                        i = 11538;
                        break;
/*      */
                    case 4275:
/*  196 */
                        i = 11539;
                        break;
/*      */
                    case 4276:
/*  197 */
                        i = 11540;
                        break;
/*      */
                    case 4277:
/*  198 */
                        i = 11541;
                        break;
/*      */
                    case 4278:
/*  199 */
                        i = 11542;
                        break;
/*      */
                    case 4279:
/*  200 */
                        i = 11543;
                        break;
/*      */
                    case 4280:
/*  201 */
                        i = 11544;
                        break;
/*      */
                    case 4281:
/*  202 */
                        i = 11545;
                        break;
/*      */
                    case 4282:
/*  203 */
                        i = 11546;
                        break;
/*      */
                    case 4283:
/*  204 */
                        i = 11547;
                        break;
/*      */
                    case 4284:
/*  205 */
                        i = 11548;
                        break;
/*      */
                    case 4285:
/*  206 */
                        i = 11549;
                        break;
/*      */
                    case 4286:
/*  207 */
                        i = 11550;
                        break;
/*      */
                    case 4287:
/*  208 */
                        i = 11551;
                        break;
/*      */
                    case 4288:
/*  209 */
                        i = 11552;
                        break;
/*      */
                    case 4289:
/*  210 */
                        i = 11553;
                        break;
/*      */
                    case 4290:
/*  211 */
                        i = 11554;
                        break;
/*      */
                    case 4291:
/*  212 */
                        i = 11555;
                        break;
/*      */
                    case 4292:
/*  213 */
                        i = 11556;
                        break;
/*      */
                    case 4293:
/*  214 */
                        i = 11557;
                        break;
/*      */
                    case 7838:
/*  215 */
                        i = 223;
                        break;
/*      */
                    case 11362:
/*  216 */
                        i = 619;
                        break;
/*      */
                    case 11363:
/*  217 */
                        i = 7549;
                        break;
/*      */
                    case 11364:
/*  218 */
                        i = 637;
                        break;
/*      */
                    case 11373:
/*  219 */
                        i = 593;
                        break;
/*      */
                    case 11374:
/*  220 */
                        i = 625;
                        break;
/*      */
                    case 11375:
/*  221 */
                        i = 592;
                        break;
/*      */
                    case 11376:
/*  222 */
                        i = 594;
                        break;
/*      */
                    case 11390:
/*  223 */
                        i = 575;
                        break;
/*      */
                    case 11391:
/*  224 */
                        i = 576;
                        break;
/*      */
                    case 42877:
/*  225 */
                        i = 7545;
                        break;
/*      */
                    case 42893:
/*  226 */
                        i = 613;
/*      */
                }
/*      */ 
/*      */
            }
/*      */
            else
/*      */ {
/*  233 */
                int k = j << 5 >> 23;
/*  234 */
                i = paramInt + k;
/*      */
            }
/*      */
        }
/*  237 */
        return i;
/*      */
    }

    /*      */
/*      */   int toUpperCase(int paramInt) {
/*  241 */
        int i = paramInt;
/*  242 */
        int j = getProperties(paramInt);
/*      */ 
/*  244 */
        if ((j & 0x10000) != 0) {
/*  245 */
            if ((j & 0x7FC0000) == 133955584) {
/*  246 */
                switch (paramInt) {
/*      */
                    case 181:
/*  248 */
                        i = 924;
                        break;
/*      */
                    case 383:
/*  249 */
                        i = 83;
                        break;
/*      */
                    case 8126:
/*  250 */
                        i = 921;
                        break;
/*      */
                    case 8064:
/*  252 */
                        i = 8072;
                        break;
/*      */
                    case 8065:
/*  253 */
                        i = 8073;
                        break;
/*      */
                    case 8066:
/*  254 */
                        i = 8074;
                        break;
/*      */
                    case 8067:
/*  255 */
                        i = 8075;
                        break;
/*      */
                    case 8068:
/*  256 */
                        i = 8076;
                        break;
/*      */
                    case 8069:
/*  257 */
                        i = 8077;
                        break;
/*      */
                    case 8070:
/*  258 */
                        i = 8078;
                        break;
/*      */
                    case 8071:
/*  259 */
                        i = 8079;
                        break;
/*      */
                    case 8080:
/*  260 */
                        i = 8088;
                        break;
/*      */
                    case 8081:
/*  261 */
                        i = 8089;
                        break;
/*      */
                    case 8082:
/*  262 */
                        i = 8090;
                        break;
/*      */
                    case 8083:
/*  263 */
                        i = 8091;
                        break;
/*      */
                    case 8084:
/*  264 */
                        i = 8092;
                        break;
/*      */
                    case 8085:
/*  265 */
                        i = 8093;
                        break;
/*      */
                    case 8086:
/*  266 */
                        i = 8094;
                        break;
/*      */
                    case 8087:
/*  267 */
                        i = 8095;
                        break;
/*      */
                    case 8096:
/*  268 */
                        i = 8104;
                        break;
/*      */
                    case 8097:
/*  269 */
                        i = 8105;
                        break;
/*      */
                    case 8098:
/*  270 */
                        i = 8106;
                        break;
/*      */
                    case 8099:
/*  271 */
                        i = 8107;
                        break;
/*      */
                    case 8100:
/*  272 */
                        i = 8108;
                        break;
/*      */
                    case 8101:
/*  273 */
                        i = 8109;
                        break;
/*      */
                    case 8102:
/*  274 */
                        i = 8110;
                        break;
/*      */
                    case 8103:
/*  275 */
                        i = 8111;
                        break;
/*      */
                    case 8115:
/*  276 */
                        i = 8124;
                        break;
/*      */
                    case 8131:
/*  277 */
                        i = 8140;
                        break;
/*      */
                    case 8179:
/*  278 */
                        i = 8188;
                        break;
/*      */
                    case 575:
/*  280 */
                        i = 11390;
                        break;
/*      */
                    case 576:
/*  281 */
                        i = 11391;
                        break;
/*      */
                    case 592:
/*  282 */
                        i = 11375;
                        break;
/*      */
                    case 593:
/*  283 */
                        i = 11373;
                        break;
/*      */
                    case 594:
/*  284 */
                        i = 11376;
                        break;
/*      */
                    case 613:
/*  285 */
                        i = 42893;
                        break;
/*      */
                    case 619:
/*  286 */
                        i = 11362;
                        break;
/*      */
                    case 625:
/*  287 */
                        i = 11374;
                        break;
/*      */
                    case 637:
/*  288 */
                        i = 11364;
                        break;
/*      */
                    case 7545:
/*  289 */
                        i = 42877;
                        break;
/*      */
                    case 7549:
/*  290 */
                        i = 11363;
                        break;
/*      */
                    case 11365:
/*  291 */
                        i = 570;
                        break;
/*      */
                    case 11366:
/*  292 */
                        i = 574;
                        break;
/*      */
                    case 11520:
/*  293 */
                        i = 4256;
                        break;
/*      */
                    case 11521:
/*  294 */
                        i = 4257;
                        break;
/*      */
                    case 11522:
/*  295 */
                        i = 4258;
                        break;
/*      */
                    case 11523:
/*  296 */
                        i = 4259;
                        break;
/*      */
                    case 11524:
/*  297 */
                        i = 4260;
                        break;
/*      */
                    case 11525:
/*  298 */
                        i = 4261;
                        break;
/*      */
                    case 11526:
/*  299 */
                        i = 4262;
                        break;
/*      */
                    case 11527:
/*  300 */
                        i = 4263;
                        break;
/*      */
                    case 11528:
/*  301 */
                        i = 4264;
                        break;
/*      */
                    case 11529:
/*  302 */
                        i = 4265;
                        break;
/*      */
                    case 11530:
/*  303 */
                        i = 4266;
                        break;
/*      */
                    case 11531:
/*  304 */
                        i = 4267;
                        break;
/*      */
                    case 11532:
/*  305 */
                        i = 4268;
                        break;
/*      */
                    case 11533:
/*  306 */
                        i = 4269;
                        break;
/*      */
                    case 11534:
/*  307 */
                        i = 4270;
                        break;
/*      */
                    case 11535:
/*  308 */
                        i = 4271;
                        break;
/*      */
                    case 11536:
/*  309 */
                        i = 4272;
                        break;
/*      */
                    case 11537:
/*  310 */
                        i = 4273;
                        break;
/*      */
                    case 11538:
/*  311 */
                        i = 4274;
                        break;
/*      */
                    case 11539:
/*  312 */
                        i = 4275;
                        break;
/*      */
                    case 11540:
/*  313 */
                        i = 4276;
                        break;
/*      */
                    case 11541:
/*  314 */
                        i = 4277;
                        break;
/*      */
                    case 11542:
/*  315 */
                        i = 4278;
                        break;
/*      */
                    case 11543:
/*  316 */
                        i = 4279;
                        break;
/*      */
                    case 11544:
/*  317 */
                        i = 4280;
                        break;
/*      */
                    case 11545:
/*  318 */
                        i = 4281;
                        break;
/*      */
                    case 11546:
/*  319 */
                        i = 4282;
                        break;
/*      */
                    case 11547:
/*  320 */
                        i = 4283;
                        break;
/*      */
                    case 11548:
/*  321 */
                        i = 4284;
                        break;
/*      */
                    case 11549:
/*  322 */
                        i = 4285;
                        break;
/*      */
                    case 11550:
/*  323 */
                        i = 4286;
                        break;
/*      */
                    case 11551:
/*  324 */
                        i = 4287;
                        break;
/*      */
                    case 11552:
/*  325 */
                        i = 4288;
                        break;
/*      */
                    case 11553:
/*  326 */
                        i = 4289;
                        break;
/*      */
                    case 11554:
/*  327 */
                        i = 4290;
                        break;
/*      */
                    case 11555:
/*  328 */
                        i = 4291;
                        break;
/*      */
                    case 11556:
/*  329 */
                        i = 4292;
                        break;
/*      */
                    case 11557:
/*  330 */
                        i = 4293;
/*      */
                }
/*      */ 
/*      */
            }
/*      */
            else
/*      */ {
/*  339 */
                int k = j << 5 >> 23;
/*  340 */
                i = paramInt - k;
/*      */
            }
/*      */
        }
/*  343 */
        return i;
/*      */
    }

    /*      */
/*      */   int toTitleCase(int paramInt) {
/*  347 */
        int i = paramInt;
/*  348 */
        int j = getProperties(paramInt);
/*      */ 
/*  350 */
        if ((j & 0x8000) != 0)
/*      */ {
/*  352 */
            if ((j & 0x10000) == 0)
/*      */ {
/*  355 */
                i = paramInt + 1;
/*      */
            }
/*  357 */
            else if ((j & 0x20000) == 0)
/*      */ {
/*  360 */
                i = paramInt - 1;
/*      */
            }
/*      */ 
/*      */
        }
/*  368 */
        else if ((j & 0x10000) != 0)
/*      */ {
/*  371 */
            i = toUpperCase(paramInt);
/*      */
        }
/*  373 */
        return i;
/*      */
    }

    /*      */
/*      */   int digit(int paramInt1, int paramInt2) {
/*  377 */
        int i = -1;
/*  378 */
        if ((paramInt2 >= 2) && (paramInt2 <= 36)) {
/*  379 */
            int j = getProperties(paramInt1);
/*  380 */
            int k = j & 0x1F;
/*  381 */
            if (k == 9) {
/*  382 */
                i = paramInt1 + ((j & 0x3E0) >> 5) & 0x1F;
/*      */
            }
/*  384 */
            else if ((j & 0xC00) == 3072)
/*      */ {
/*  386 */
                i = (paramInt1 + ((j & 0x3E0) >> 5) & 0x1F) + 10;
/*      */
            }
/*      */
        }
/*  389 */
        return i < paramInt2 ? i : -1;
/*      */
    }

    /*      */
/*      */   int getNumericValue(int paramInt) {
/*  393 */
        int i = getProperties(paramInt);
/*  394 */
        int j = -1;
/*      */ 
/*  396 */
        switch (i & 0xC00) {
/*      */
            case 0:
/*      */
            default:
/*  399 */
                j = -1;
/*  400 */
                break;
/*      */
            case 1024:
/*  402 */
                j = paramInt + ((i & 0x3E0) >> 5) & 0x1F;
/*  403 */
                break;
/*      */
            case 2048:
/*  405 */
                switch (paramInt) {
                    case 3057:
/*  406 */
                        j = 100;
                        break;
/*      */
                    case 3058:
/*  407 */
                        j = 1000;
                        break;
/*      */
                    case 4981:
/*  408 */
                        j = 40;
                        break;
/*      */
                    case 4982:
/*  409 */
                        j = 50;
                        break;
/*      */
                    case 4983:
/*  410 */
                        j = 60;
                        break;
/*      */
                    case 4984:
/*  411 */
                        j = 70;
                        break;
/*      */
                    case 4985:
/*  412 */
                        j = 80;
                        break;
/*      */
                    case 4986:
/*  413 */
                        j = 90;
                        break;
/*      */
                    case 4987:
/*  414 */
                        j = 100;
                        break;
/*      */
                    case 4988:
/*  415 */
                        j = 10000;
                        break;
/*      */
                    case 8543:
/*  416 */
                        j = 1;
                        break;
/*      */
                    case 8556:
/*  417 */
                        j = 50;
                        break;
/*      */
                    case 8557:
/*  418 */
                        j = 100;
                        break;
/*      */
                    case 8558:
/*  419 */
                        j = 500;
                        break;
/*      */
                    case 8559:
/*  420 */
                        j = 1000;
                        break;
/*      */
                    case 8572:
/*  421 */
                        j = 50;
                        break;
/*      */
                    case 8573:
/*  422 */
                        j = 100;
                        break;
/*      */
                    case 8574:
/*  423 */
                        j = 500;
                        break;
/*      */
                    case 8575:
/*  424 */
                        j = 1000;
                        break;
/*      */
                    case 8576:
/*  425 */
                        j = 1000;
                        break;
/*      */
                    case 8577:
/*  426 */
                        j = 5000;
                        break;
/*      */
                    case 8578:
/*  427 */
                        j = 10000;
                        break;
/*      */
                    case 12892:
/*  429 */
                        j = 32;
                        break;
/*      */
                    case 12893:
/*  431 */
                        j = 33;
                        break;
/*      */
                    case 12894:
/*  432 */
                        j = 34;
                        break;
/*      */
                    case 12895:
/*  433 */
                        j = 35;
                        break;
/*      */
                    case 12977:
/*  434 */
                        j = 36;
                        break;
/*      */
                    case 12978:
/*  435 */
                        j = 37;
                        break;
/*      */
                    case 12979:
/*  436 */
                        j = 38;
                        break;
/*      */
                    case 12980:
/*  437 */
                        j = 39;
                        break;
/*      */
                    case 12981:
/*  438 */
                        j = 40;
                        break;
/*      */
                    case 12982:
/*  439 */
                        j = 41;
                        break;
/*      */
                    case 12983:
/*  440 */
                        j = 42;
                        break;
/*      */
                    case 12984:
/*  441 */
                        j = 43;
                        break;
/*      */
                    case 12985:
/*  442 */
                        j = 44;
                        break;
/*      */
                    case 12986:
/*  443 */
                        j = 45;
                        break;
/*      */
                    case 12987:
/*  444 */
                        j = 46;
                        break;
/*      */
                    case 12988:
/*  445 */
                        j = 47;
                        break;
/*      */
                    case 12989:
/*  446 */
                        j = 48;
                        break;
/*      */
                    case 12990:
/*  447 */
                        j = 49;
                        break;
/*      */
                    case 12991:
/*  448 */
                        j = 50;
                        break;
/*      */
                    case 3441:
/*  450 */
                        j = 100;
                        break;
/*      */
                    case 3442:
/*  451 */
                        j = 1000;
                        break;
/*      */
                    case 8582:
/*  452 */
                        j = 50;
                        break;
/*      */
                    case 8583:
/*  453 */
                        j = 50000;
                        break;
/*      */
                    case 8584:
/*  454 */
                        j = 100000;
                        break;
/*      */
                    default:
/*  456 */
                        j = -2;
                }
                break;
/*      */
            case 3072:
/*  460 */
                j = (paramInt + ((i & 0x3E0) >> 5) & 0x1F) + 10;
/*      */
        }
/*      */ 
/*  463 */
        return j;
/*      */
    }

    /*      */
/*      */   boolean isWhitespace(int paramInt) {
/*  467 */
        int i = getProperties(paramInt);
/*  468 */
        return (i & 0x7000) == 16384;
/*      */
    }

    /*      */
/*      */   byte getDirectionality(int paramInt) {
/*  472 */
        int i = getProperties(paramInt);
/*  473 */
        byte b = (byte) ((i & 0x78000000) >> 27);
/*  474 */
        if (b == 15) {
/*  475 */
            switch (paramInt)
/*      */ {
/*      */
                case 8234:
/*  478 */
                    b = 14;
/*  479 */
                    break;
/*      */
                case 8235:
/*  482 */
                    b = 16;
/*  483 */
                    break;
/*      */
                case 8236:
/*  486 */
                    b = 18;
/*  487 */
                    break;
/*      */
                case 8237:
/*  490 */
                    b = 15;
/*  491 */
                    break;
/*      */
                case 8238:
/*  494 */
                    b = 17;
/*  495 */
                    break;
/*      */
                default:
/*  497 */
                    b = -1;
/*      */
            }
/*      */
        }
/*      */ 
/*  501 */
        return b;
/*      */
    }

    /*      */
/*      */   boolean isMirrored(int paramInt) {
/*  505 */
        int i = getProperties(paramInt);
/*  506 */
        return (i & 0x80000000) != 0;
/*      */
    }

    /*      */
/*      */   int toUpperCaseEx(int paramInt) {
/*  510 */
        int i = paramInt;
/*  511 */
        int j = getProperties(paramInt);
/*      */ 
/*  513 */
        if ((j & 0x10000) != 0) {
/*  514 */
            if ((j & 0x7FC0000) != 133955584) {
/*  515 */
                int k = j << 5 >> 23;
/*  516 */
                i = paramInt - k;
/*      */
            }
/*      */
            else {
/*  519 */
                switch (paramInt) {
/*      */
                    case 181:
/*  521 */
                        i = 924;
                        break;
/*      */
                    case 383:
/*  522 */
                        i = 83;
                        break;
/*      */
                    case 8126:
/*  523 */
                        i = 921;
                        break;
/*      */
                    case 575:
/*  525 */
                        i = 11390;
                        break;
/*      */
                    case 576:
/*  526 */
                        i = 11391;
                        break;
/*      */
                    case 592:
/*  527 */
                        i = 11375;
                        break;
/*      */
                    case 593:
/*  528 */
                        i = 11373;
                        break;
/*      */
                    case 594:
/*  529 */
                        i = 11376;
                        break;
/*      */
                    case 613:
/*  530 */
                        i = 42893;
                        break;
/*      */
                    case 619:
/*  531 */
                        i = 11362;
                        break;
/*      */
                    case 625:
/*  532 */
                        i = 11374;
                        break;
/*      */
                    case 637:
/*  533 */
                        i = 11364;
                        break;
/*      */
                    case 7545:
/*  534 */
                        i = 42877;
                        break;
/*      */
                    case 7549:
/*  535 */
                        i = 11363;
                        break;
/*      */
                    case 11365:
/*  536 */
                        i = 570;
                        break;
/*      */
                    case 11366:
/*  537 */
                        i = 574;
                        break;
/*      */
                    case 11520:
/*  538 */
                        i = 4256;
                        break;
/*      */
                    case 11521:
/*  539 */
                        i = 4257;
                        break;
/*      */
                    case 11522:
/*  540 */
                        i = 4258;
                        break;
/*      */
                    case 11523:
/*  541 */
                        i = 4259;
                        break;
/*      */
                    case 11524:
/*  542 */
                        i = 4260;
                        break;
/*      */
                    case 11525:
/*  543 */
                        i = 4261;
                        break;
/*      */
                    case 11526:
/*  544 */
                        i = 4262;
                        break;
/*      */
                    case 11527:
/*  545 */
                        i = 4263;
                        break;
/*      */
                    case 11528:
/*  546 */
                        i = 4264;
                        break;
/*      */
                    case 11529:
/*  547 */
                        i = 4265;
                        break;
/*      */
                    case 11530:
/*  548 */
                        i = 4266;
                        break;
/*      */
                    case 11531:
/*  549 */
                        i = 4267;
                        break;
/*      */
                    case 11532:
/*  550 */
                        i = 4268;
                        break;
/*      */
                    case 11533:
/*  551 */
                        i = 4269;
                        break;
/*      */
                    case 11534:
/*  552 */
                        i = 4270;
                        break;
/*      */
                    case 11535:
/*  553 */
                        i = 4271;
                        break;
/*      */
                    case 11536:
/*  554 */
                        i = 4272;
                        break;
/*      */
                    case 11537:
/*  555 */
                        i = 4273;
                        break;
/*      */
                    case 11538:
/*  556 */
                        i = 4274;
                        break;
/*      */
                    case 11539:
/*  557 */
                        i = 4275;
                        break;
/*      */
                    case 11540:
/*  558 */
                        i = 4276;
                        break;
/*      */
                    case 11541:
/*  559 */
                        i = 4277;
                        break;
/*      */
                    case 11542:
/*  560 */
                        i = 4278;
                        break;
/*      */
                    case 11543:
/*  561 */
                        i = 4279;
                        break;
/*      */
                    case 11544:
/*  562 */
                        i = 4280;
                        break;
/*      */
                    case 11545:
/*  563 */
                        i = 4281;
                        break;
/*      */
                    case 11546:
/*  564 */
                        i = 4282;
                        break;
/*      */
                    case 11547:
/*  565 */
                        i = 4283;
                        break;
/*      */
                    case 11548:
/*  566 */
                        i = 4284;
                        break;
/*      */
                    case 11549:
/*  567 */
                        i = 4285;
                        break;
/*      */
                    case 11550:
/*  568 */
                        i = 4286;
                        break;
/*      */
                    case 11551:
/*  569 */
                        i = 4287;
                        break;
/*      */
                    case 11552:
/*  570 */
                        i = 4288;
                        break;
/*      */
                    case 11553:
/*  571 */
                        i = 4289;
                        break;
/*      */
                    case 11554:
/*  572 */
                        i = 4290;
                        break;
/*      */
                    case 11555:
/*  573 */
                        i = 4291;
                        break;
/*      */
                    case 11556:
/*  574 */
                        i = 4292;
                        break;
/*      */
                    case 11557:
/*  575 */
                        i = 4293;
                        break;
/*      */
                    default:
/*  576 */
                        i = -1;
/*      */
                }
/*      */
            }
/*      */
        }
/*  580 */
        return i;
/*      */
    }

    /*      */
/*      */   char[] toUpperCaseCharArray(int paramInt) {
/*  584 */
        char[] arrayOfChar = {(char) paramInt};
/*  585 */
        int i = findInCharMap(paramInt);
/*  586 */
        if (i != -1) {
/*  587 */
            arrayOfChar = charMap[i][1];
/*      */
        }
/*  589 */
        return arrayOfChar;
/*      */
    }

    /*      */
/*      */   int findInCharMap(int paramInt)
/*      */ {
/*  601 */
        if ((charMap == null) || (charMap.length == 0)) {
/*  602 */
            return -1;
/*      */
        }
/*      */ 
/*  605 */
        int j = 0;
/*  606 */
        int i = charMap.length;
/*  607 */
        int k = i / 2;
/*      */ 
/*  609 */
        while (i - j > 1) {
/*  610 */
            if (paramInt >= charMap[k][0][0])
/*  611 */ j = k;
/*      */
            else {
/*  613 */
                i = k;
/*      */
            }
/*  615 */
            k = (i + j) / 2;
/*      */
        }
/*  617 */
        if (paramInt == charMap[k][0][0]) return k;
/*  618 */
        return -1;
/*      */
    }

    /*      */   static {
/*  621 */
        instance = new CharacterData00();
/*      */ 
/*  629 */
        X = "".toCharArray();
/*      */ 
/*  790 */
        Y = "".toCharArray();
/*      */ 
/* 1156 */
        A = new int[920];
/*      */ 
/* 1290 */
        B = "".toCharArray();
/*      */ 
/* 1344 */
        charMap = new char[][][]{{{'ß'}, {'S', 'S'}}, {{'İ'}, {'İ'}}, {{'ŉ'}, {'ʼ', 'N'}}, {{'ǰ'}, {'J', '̌'}}, {{'ΐ'}, {'Ι', '̈', '́'}}, {{'ΰ'}, {'Υ', '̈', '́'}}, {{'և'}, {'Ե', 'Ւ'}}, {{'ẖ'}, {'H', '̱'}}, {{'ẗ'}, {'T', '̈'}}, {{'ẘ'}, {'W', '̊'}}, {{'ẙ'}, {'Y', '̊'}}, {{'ẚ'}, {'A', 'ʾ'}}, {{'ὐ'}, {'Υ', '̓'}}, {{'ὒ'}, {'Υ', '̓', '̀'}}, {{'ὔ'}, {'Υ', '̓', '́'}}, {{'ὖ'}, {'Υ', '̓', '͂'}}, {{'ᾀ'}, {'Ἀ', 'Ι'}}, {{'ᾁ'}, {'Ἁ', 'Ι'}}, {{'ᾂ'}, {'Ἂ', 'Ι'}}, {{'ᾃ'}, {'Ἃ', 'Ι'}}, {{'ᾄ'}, {'Ἄ', 'Ι'}}, {{'ᾅ'}, {'Ἅ', 'Ι'}}, {{'ᾆ'}, {'Ἆ', 'Ι'}}, {{'ᾇ'}, {'Ἇ', 'Ι'}}, {{'ᾈ'}, {'Ἀ', 'Ι'}}, {{'ᾉ'}, {'Ἁ', 'Ι'}}, {{'ᾊ'}, {'Ἂ', 'Ι'}}, {{'ᾋ'}, {'Ἃ', 'Ι'}}, {{'ᾌ'}, {'Ἄ', 'Ι'}}, {{'ᾍ'}, {'Ἅ', 'Ι'}}, {{'ᾎ'}, {'Ἆ', 'Ι'}}, {{'ᾏ'}, {'Ἇ', 'Ι'}}, {{'ᾐ'}, {'Ἠ', 'Ι'}}, {{'ᾑ'}, {'Ἡ', 'Ι'}}, {{'ᾒ'}, {'Ἢ', 'Ι'}}, {{'ᾓ'}, {'Ἣ', 'Ι'}}, {{'ᾔ'}, {'Ἤ', 'Ι'}}, {{'ᾕ'}, {'Ἥ', 'Ι'}}, {{'ᾖ'}, {'Ἦ', 'Ι'}}, {{'ᾗ'}, {'Ἧ', 'Ι'}}, {{'ᾘ'}, {'Ἠ', 'Ι'}}, {{'ᾙ'}, {'Ἡ', 'Ι'}}, {{'ᾚ'}, {'Ἢ', 'Ι'}}, {{'ᾛ'}, {'Ἣ', 'Ι'}}, {{'ᾜ'}, {'Ἤ', 'Ι'}}, {{'ᾝ'}, {'Ἥ', 'Ι'}}, {{'ᾞ'}, {'Ἦ', 'Ι'}}, {{'ᾟ'}, {'Ἧ', 'Ι'}}, {{'ᾠ'}, {'Ὠ', 'Ι'}}, {{'ᾡ'}, {'Ὡ', 'Ι'}}, {{'ᾢ'}, {'Ὢ', 'Ι'}}, {{'ᾣ'}, {'Ὣ', 'Ι'}}, {{'ᾤ'}, {'Ὤ', 'Ι'}}, {{'ᾥ'}, {'Ὥ', 'Ι'}}, {{'ᾦ'}, {'Ὦ', 'Ι'}}, {{'ᾧ'}, {'Ὧ', 'Ι'}}, {{'ᾨ'}, {'Ὠ', 'Ι'}}, {{'ᾩ'}, {'Ὡ', 'Ι'}}, {{'ᾪ'}, {'Ὢ', 'Ι'}}, {{'ᾫ'}, {'Ὣ', 'Ι'}}, {{'ᾬ'}, {'Ὤ', 'Ι'}}, {{'ᾭ'}, {'Ὥ', 'Ι'}}, {{'ᾮ'}, {'Ὦ', 'Ι'}}, {{'ᾯ'}, {'Ὧ', 'Ι'}}, {{'ᾲ'}, {'Ὰ', 'Ι'}}, {{'ᾳ'}, {'Α', 'Ι'}}, {{'ᾴ'}, {'Ά', 'Ι'}}, {{'ᾶ'}, {'Α', '͂'}}, {{'ᾷ'}, {'Α', '͂', 'Ι'}}, {{'ᾼ'}, {'Α', 'Ι'}}, {{'ῂ'}, {'Ὴ', 'Ι'}}, {{'ῃ'}, {'Η', 'Ι'}}, {{'ῄ'}, {'Ή', 'Ι'}}, {{'ῆ'}, {'Η', '͂'}}, {{'ῇ'}, {'Η', '͂', 'Ι'}}, {{'ῌ'}, {'Η', 'Ι'}}, {{'ῒ'}, {'Ι', '̈', '̀'}}, {{'ΐ'}, {'Ι', '̈', '́'}}, {{'ῖ'}, {'Ι', '͂'}}, {{'ῗ'}, {'Ι', '̈', '͂'}}, {{'ῢ'}, {'Υ', '̈', '̀'}}, {{'ΰ'}, {'Υ', '̈', '́'}}, {{'ῤ'}, {'Ρ', '̓'}}, {{'ῦ'}, {'Υ', '͂'}}, {{'ῧ'}, {'Υ', '̈', '͂'}}, {{'ῲ'}, {'Ὼ', 'Ι'}}, {{'ῳ'}, {'Ω', 'Ι'}}, {{'ῴ'}, {'Ώ', 'Ι'}}, {{'ῶ'}, {'Ω', '͂'}}, {{'ῷ'}, {'Ω', '͂', 'Ι'}}, {{'ῼ'}, {'Ω', 'Ι'}}, {{64256}, {'F', 'F'}}, {{64257}, {'F', 'I'}}, {{64258}, {'F', 'L'}}, {{64259}, {'F', 'F', 'I'}}, {{64260}, {'F', 'F', 'L'}}, {{64261}, {'S', 'T'}}, {{64262}, {'S', 'T'}}, {{64275}, {'Մ', 'Ն'}}, {{64276}, {'Մ', 'Ե'}}, {{64277}, {'Մ', 'Ի'}}, {{64278}, {'Վ', 'Ն'}}, {{64279}, {'Մ', 'Խ'}}};
/*      */ 
/* 1450 */
        char[] arrayOfChar = "".toCharArray();
/* 1451 */
        assert (arrayOfChar.length == 1840);
/* 1452 */
        int i = 0;
        int j = 0;
/* 1453 */
        while (i < 1840) {
/* 1454 */
            int k = arrayOfChar[(i++)] << '\020';
/* 1455 */
            A[(j++)] = (k | arrayOfChar[(i++)]);
/*      */
        }
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterData00
 * JD-Core Version:    0.6.2
 */