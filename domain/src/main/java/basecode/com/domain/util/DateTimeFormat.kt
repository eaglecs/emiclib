package basecode.com.domain.util

enum class DateTimeFormat(val value: String) {
    DDMMYY_HHMM_FORMAT("dd/MM/yyyy HH:mm"),
    DDMMYY_HHMMSS_FORMAT("dd/MM/yyyy HH:mm:ss"),
    YYMMDD_HHMM_FORMAT("yyyy/MM/dd HH:mm"),
    DDMMYYFORMAT("dd/MM/yyyy"),
    DDMMFORMAT("dd/MM"),
    SAVE_TIME_FORMAT("yyyy/MM/dd HH:mm:ss"),
    SAVE_TIME_FORMAT_WITHOUT_SECOND("yyyy/MM/dd HH:mm"),
    SAVE_DATE_ORDER_NUMBER_FORMAT("ddMMyy"),
    HOUR_TIME_FORMAT("HH:mm"),
    HOUR_TIME_AA_FORMAT("hh:mm aa"),
    HOUR_TIME_A_FORMAT("hh:mm a"),
    YYMMDD_FORMAT("yyyy/MM/dd"),
    YYMMDD_HHMM_AA_FORMAT("dd/MM/yyyy hh:mm aa"),
    YYMMDD_HHMM_A_FORMAT("dd/MM/yyyy hh:mm a"),
    DDMMHHMM_FORMAT("dd/MM HH:mm"),
    YY_MM_DD_FORMAT("yyyy-MM-dd"),
    DD_MM_YY_FORMAT("dd-MM-yyyy"),
    YY_MM_DD_HHMMSS("yyyy-MM-dd HH:mm:ss"),
    YY_MM_DD_T_HH_MM_SS("yyyy-MM-dd'T'HH:mm:ss"),
    YY_MM_DD_HH_MM("yyyy-MM-dd HH:mm")
}