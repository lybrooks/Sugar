package presenter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

public class PresenterBaseImage implements IPresenterBaseImage {
    public PresenterBaseImage(Object view) {
    }

    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }

    @Override
    public void showImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
