package presenter;

import android.content.Context;

import model.net.IModelBoutique;

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

public class PresenterBoutique implements IPresenterBoutique {
   // private IBoutiqueView<BoutiqueBean[]> mBoutiqueView;
    private IModelBoutique mModel;

    @Override
    public void release() {

    }

    @Override
    public void downloadBoutique(Context context, int action) {


    }
}
