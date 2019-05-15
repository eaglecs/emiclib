package basecode.com.data.cache.pager;

public interface ConfigSaver {
    String CONFIG_PAGER = "config_pager";
    String CONFIG_NAME = "config_name";


    void save(String key, Object data);

    <T> T get(String key);

    void delete(String key);

    void deleteAll();
}
