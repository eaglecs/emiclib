package basecode.com.data.cache.pager;

public interface ConfigSaver {
    String CONFIG_PAGER = "config_pager";
    String CONFIG_NAME = "config_name";
    String CONFIG_USER_TOKEN = "config_user_token";
    String CONFIG_LOGIN_TYPE = "config_login_type";
    String CONFIG_USER_MODEL = "config_user_model";
    String CONFIG_LOGIN_REQUEST = "config_login_request";


    void save(String key, Object data);

    <T> T get(String key);

    void delete(String key);

    void deleteAll();
}
