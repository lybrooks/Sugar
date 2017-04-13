package view.views;

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

public interface IGoodsDetailsView<T> extends IView {
    void showResult(T t);
}
