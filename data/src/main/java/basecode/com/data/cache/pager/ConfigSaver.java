package basecode.com.data.cache.pager;

import org.jetbrains.annotations.Nullable;

public interface ConfigSaver {
    String CONFIG_PAGER = "config_pager";
    String CONFIG_NAME = "config_name";
    String CONFIG_USER_TOKEN = "config_user_token";


    void save(String key, Object data);

    <T> T get(String key);

    void delete(String key);

    void deleteAll();
}
