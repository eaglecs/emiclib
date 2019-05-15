package basecode.com.data.cache.pager

object ConfigUtil {
    fun getName(): String {
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        return configSaver.get(ConfigSaver.CONFIG_NAME)
    }

    fun saveName(name: String) {
        val configSaver = PaperConfigSaverImpl(ConfigSaver.CONFIG_PAGER)
        configSaver.save(ConfigSaver.CONFIG_NAME, name)
    }
}