package basecode.com.data.cache.pager

import basecode.com.domain.extention.valueOrEmpty

object ConfigUtil {
    fun getUserToken(): String {
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        return configSaver.get<String>(ConfigSaver.CONFIG_USER_TOKEN).valueOrEmpty()
    }

    fun saveUserToken(userToken: String) {
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        configSaver.save(ConfigSaver.CONFIG_USER_TOKEN, userToken)
    }

    fun saveLoginType(loginType: String) {
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        configSaver.save(ConfigSaver.CONFIG_LOGIN_TYPE, loginType)
    }

    fun getLoginType(): String{
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        return configSaver.get<String>(ConfigSaver.CONFIG_LOGIN_TYPE).valueOrEmpty()
    }
}