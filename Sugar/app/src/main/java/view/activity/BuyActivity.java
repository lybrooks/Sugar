package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.CartBean;
import cn.sugar.bean.MessageBean;
import cn.sugar.bean.UserBean;
import day.sugar.R;
import model.net.IModelCart;
import model.net.ModelCart;
import model.utils.CommonUtils;
import model.utils.OkHttpUtils;

public class BuyActivity extends AppCompatActivity implements PaymentHandler {


    private static String URL = "http://218.244.151.190/demo/charge";
    @Bind(R.id.Et_UserName)
    EditText EtUserName;
    @Bind(R.id.ET_phoneNumber)
    EditText ETPhoneNumber;
    @Bind(R.id.Spin_city)
    Spinner SpinCity;
    @Bind(R.id.ET_Address)
    EditText ETAddress;
    @Bind(R.id.tv_sumPrizes)
    TextView tvSumPrizes;

    UserBean user;
    BuyActivity mContext;

    ArrayList<CartBean> mList;
    int rankPrice;
    String[] ids;

    IModelCart modelCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        initView();
        initDara();
        mContext = this;
        mList = new ArrayList<>();
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void initDara() {
        String cartIds = getIntent().getStringExtra(I.Cart.ID);
        user = SugarApplication.getUserBean();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }
        ids = cartIds.split(",");
        getOrderList();
    }

    private void getOrderList() {
        modelCart.downUserCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                OkHttpUtils utils = new OkHttpUtils(mContext);
                ArrayList list = utils.array2List(result);
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mList.addAll(list);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }
            }
        }
        tvSumPrizes.setText("合计：￥" + Double.valueOf(rankPrice));

    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    private void initView() {
        modelCart = new ModelCart();

    }

    private void checkOrder() {
        String ETname = EtUserName.getText().toString();
        if (TextUtils.isEmpty(ETname)) {
            EtUserName.setError("收货人姓名不能为空");
            EtUserName.requestFocus();
            return;
        }
        String ETphone = ETPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(ETphone)) {
            ETPhoneNumber.setError("手机号码不能为空");
            ETPhoneNumber.requestFocus();
            return;
        }
        if (!ETphone.matches("[\\d]{11}")) {
            ETPhoneNumber.setError("手机号码格式错误");
            ETPhoneNumber.requestFocus();
            return;
        }
        String area = SpinCity.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(BuyActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = ETAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            ETAddress.setError("街道地址不能为空");
            ETAddress.requestFocus();
            return;
        }
        gotoStatements();

    }

    private void gotoStatements() {

        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice * 100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);


    }

    @OnClick(R.id.btn_pay)
    public void onClick() {
        checkOrder();
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "" + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            int resultCode = data.getExtras().getInt("code");
            switch (resultCode) {
                case 1:
                    paySuccess();
                    CommonUtils.showLongToast("支付完成");
                    break;
                case -1:
                    CommonUtils.showLongToast("支付失败");
                    finish();
                    break;

            }
        }
    }

    private void paySuccess() {
        for (String id : ids) {
            modelCart.deleteCart(mContext, Integer.parseInt(id), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {

                }

                @Override
                public void onError(String error) {

                }
            });
        }
        finish();
    }

}
