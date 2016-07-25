/*    */ package sun.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class LocaleDataMetaInfo
/*    */ {
/* 43 */   private static final HashMap<String, String> resourceNameToLocales = new HashMap(6);
/*    */ 
/*    */   public static String getSupportedLocaleString(String paramString)
/*    */   {
/* 79 */     return (String)resourceNameToLocales.get(paramString);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 54 */     resourceNameToLocales.put("sun.text.resources.FormatData", "  be be_BY bg bg_BG ca ca_ES cs cs_CZ da da_DK de de_AT de_CH de_DE de_LU el el_CY el_GR en en_AU en_CA en_GB en_IE en_IN en_MT en_NZ en_PH en_SG en_US en_ZA es es_AR es_BO es_CL es_CO es_CR es_DO es_EC es_ES es_GT es_HN es_MX es_NI es_PA es_PE es_PR es_PY es_SV es_US es_UY es_VE et et_EE fi fi_FI fr fr_BE fr_CA fr_CH fr_FR fr_LU ga ga_IE hr hr_HR hu hu_HU in in_ID is is_IS it it_CH it_IT lt lt_LT lv lv_LV mk mk_MK ms ms_MY mt mt_MT nl nl_BE nl_NL no no_NO no_NO_NY pl pl_PL pt pt_BR pt_PT ro ro_RO ru ru_RU sk sk_SK sl sl_SI sq sq_AL sr sr_BA sr_CS sr_Latn sr_Latn_BA sr_Latn_ME sr_Latn_RS sr_ME sr_RS sv sv_SE tr tr_TR uk uk_UA |  ar ar_AE ar_BH ar_DZ ar_EG ar_IQ ar_JO ar_KW ar_LB ar_LY ar_MA ar_OM ar_QA ar_SA ar_SD ar_SY ar_TN ar_YE hi_IN iw iw_IL ja ja_JP ja_JP_JP ko ko_KR th th_TH th_TH_TH vi vi_VN zh zh_CN zh_HK zh_SG zh_TW ");
/*    */ 
/* 57 */     resourceNameToLocales.put("sun.text.resources.CollationData", "  be bg ca cs da de el en es et fi fr hr hu is it lt lv mk nl no pl pt ro ru sk sl sq sr sr_Latn sv tr uk |  ar hi iw ja ko th vi zh zh_HK zh_TW ");
/*    */ 
/* 60 */     resourceNameToLocales.put("sun.util.resources.TimeZoneNames", "  de en en_CA en_GB en_IE es fr it pt_BR sv |  hi ja ko zh_CN zh_HK zh_TW ");
/*    */ 
/* 63 */     resourceNameToLocales.put("sun.util.resources.LocaleNames", "  be bg ca cs da de el el_CY en en_MT en_PH en_SG es es_US et fi fr ga hr hu in is it lt lv mk ms mt nl no no_NO_NY pl pt pt_BR pt_PT ro ru sk sl sq sr sr_Latn sv tr uk |  ar hi iw ja ko th vi zh zh_HK zh_SG zh_TW ");
/*    */ 
/* 66 */     resourceNameToLocales.put("sun.util.resources.CurrencyNames", "  be_BY bg_BG ca_ES cs_CZ da_DK de de_AT de_CH de_DE de_GR de_LU el_CY el_GR en_AU en_CA en_GB en_IE en_IN en_MT en_NZ en_PH en_SG en_US en_ZA es es_AR es_BO es_CL es_CO es_CR es_CU es_DO es_EC es_ES es_GT es_HN es_MX es_NI es_PA es_PE es_PR es_PY es_SV es_US es_UY es_VE et_EE fi_FI fr fr_BE fr_CA fr_CH fr_FR fr_LU ga_IE hr_HR hu_HU in_ID is_IS it it_CH it_IT lt_LT lv_LV mk_MK ms_MY mt_MT nl_BE nl_NL no_NO pl_PL pt pt_BR pt_PT ro_RO ru_RU sk_SK sl_SI sq_AL sr_BA sr_CS sr_Latn_BA sr_Latn_ME sr_Latn_RS sr_ME sr_RS sv sv_SE tr_TR uk_UA |  ar_AE ar_BH ar_DZ ar_EG ar_IQ ar_JO ar_KW ar_LB ar_LY ar_MA ar_OM ar_QA ar_SA ar_SD ar_SY ar_TN ar_YE hi_IN iw_IL ja ja_JP ko ko_KR th_TH vi_VN zh_CN zh_HK zh_SG zh_TW ");
/*    */ 
/* 69 */     resourceNameToLocales.put("sun.util.resources.CalendarData", "  be bg ca cs da de el el_CY en en_GB en_IE en_MT es es_ES es_US et fi fr fr_CA hr hu in_ID is it lt lv mk ms_MY mt mt_MT nl no pl pt pt_BR pt_PT ro ru sk sl sq sr sr_Latn_BA sr_Latn_ME sr_Latn_RS sv tr uk |  ar hi iw ja ko th vi zh ");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.LocaleDataMetaInfo
 * JD-Core Version:    0.6.2
 */