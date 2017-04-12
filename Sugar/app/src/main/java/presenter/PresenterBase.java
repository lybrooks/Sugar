package presenter;

import model.net.IModelBase;
import model.net.ModelBase;

/**
 * All rights Reserved, Designed By www.tydic.com
 *
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * Created by guowang on 2017/4/12.
 * @date: ${date} ${time}
 * @Copyright: ${year} www.tydic.com Inc. All rights reserved.
 */

public class PresenterBase<T> implements IPresenterBase {
    IModelBase mModel;
    T view;

    public PresenterBase(T view) {
        mModel = new ModelBase();
        this.view = view;
    }

    /**
     * 释放OkHttp所有的网络请求，并释放对Activity或Fragment的持有
     */
    public void release() {
        mModel.realse();
        this.view = null;
    }
}
