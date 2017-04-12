package model.net;

import model.utils.ImageLoader;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ModelBase implements IModelBase {
    @Override
    public void realse() {
        OkHttpUtils.release();
        ImageLoader.release();
    }
}
