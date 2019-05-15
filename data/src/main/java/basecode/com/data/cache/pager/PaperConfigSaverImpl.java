package basecode.com.data.cache.pager;

import io.paperdb.Paper;

public class PaperConfigSaverImpl implements ConfigSaver {

    private String bookName;

    public PaperConfigSaverImpl(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public void save(String key, Object data) {
        if (data != null) {
            Paper.book(this.bookName).write(key, data);
        } else {
            //delete if data null
            delete(key);
        }
    }

    @Override
    public <T> T get(String key) {
        return Paper.book(this.bookName).read(key);
    }

    @Override
    public void delete(String key) {
        Paper.book(this.bookName).delete(key);
    }

    @Override
    public void deleteAll() {
        Paper.book(this.bookName).destroy();
    }


}
