package com.ascba.rebate.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.task.InitAddressTask;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.AddressPickerView;
import com.ascba.rebate.view.ShopABarText;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.qqtheme.framework.beans.Province;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 新增地址
 */

public class AddAdressActivity extends BaseNetActivity implements View.OnClickListener {

    private ShopABarText bar;
    private RelativeLayout btn_contact, btn_selectPosition, btn_selectStreet;
    private Context context;
    private String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
    };
    private EditText name, phone, address;
    private CheckBox chbDefault;
    private TextView txProvince;
    private Province province;
    private Province.City city;
    private Province.City.District district;
    private AddressPickerView pickerView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;
        initView();
        initRegion();
    }


    private void initView() {
        bar = (ShopABarText) findViewById(R.id.add_address_bar);
        bar.setBtnText("保存");
        bar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
                //保存
                if (!StringUtils.isEmpty(name.getText().toString())
                        && !StringUtils.isEmpty(phone.getText().toString())
                        && !StringUtils.isEmpty(address.getText().toString())
                        && province != null && province.getId() != 0) {
                    submitData();
                } else {
                    showToast("请填写完整收货地址信息");
                }
            }
        });

        //选择联系人
        btn_contact = (RelativeLayout) findViewById(R.id.activity_add_rl_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }
        });

        name = (EditText) findViewById(R.id.address_name);
        phone = (EditText) findViewById(R.id.address_phone);
        address = (EditText) findViewById(R.id.address);
        chbDefault = (CheckBox) findViewById(R.id.chb_default);

        //地区和街道
        btn_selectPosition = (RelativeLayout) findViewById(R.id.rl_selectPosition);
        btn_selectPosition.setOnClickListener(this);
        btn_selectStreet = (RelativeLayout) findViewById(R.id.rl_selectStreet);
        btn_selectStreet.setOnClickListener(this);

        txProvince = (TextView) findViewById(R.id.address_province);
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    /**
     * 获取联系人
     */
    private void getContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }

    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.READ_CONTACTS)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意使用read
            getContact();
        } else {
            //用户不同意，自行处理即可
            Toast.makeText(this, "无法使用此功能，因为你拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            //处理返回的data,获取选择的联系人信息
            Uri uri = data.getData();
            String[] contacts = getPhoneContacts(uri);

            name.setText(contacts[0]);

            String phoneNum = contacts[1];
            phone.setText(getNumbers(phoneNum.trim()));
        }

    }

    //截取数字
    public String getNumbers(String content) {
        String str2 = "";
        if (content != null && !"".equals(content)) {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) >= 48 && content.charAt(i) <= 57) {
                    str2 += content.charAt(i);
                }
            }
        }
        return str2;
    }

    /**
     * 获取联系人数据
     */
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    /**
     * 提交数据
     */
    private void submitData() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.memberAddressAdd, 0, true);
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("consignee", name.getText().toString().trim());//收货人
        jsonRequest.add("mobile", phone.getText().toString().trim());//手机号
        jsonRequest.add("province", province.getId());//省份ID
        jsonRequest.add("city", city.getId());//市ID
        jsonRequest.add("district", district.getId());//地区ID
        jsonRequest.add("twon", 1158);//乡镇ID
        jsonRequest.add("address", address.getText().toString().trim());//地址内容
        jsonRequest.add("default", chbDefault.isChecked() ? 1 : 0);//是否默认——1：是， 0——否

        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                Intent intent = new Intent();
                setResult(2, intent);
                getDm().buildAlertDialog("保存成功");
            }

            @Override
            public void handle404(String message) {
                getDm().buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                getDm().buildAlertDialog("请检查网络！");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_selectStreet:
                break;
            case R.id.rl_selectPosition:
                if (pickerView == null) {
                    dialog = ProgressDialog.show(context, null, "正在初始化数据...", true, true);
                } else {
                    pickerView.showPicker();
                }
                break;
        }
    }

    /*
     初始化地区数据
    */
    private void initRegion() {
        InitAddressTask task = new InitAddressTask(this);
        task.setInitData(new InitAddressTask.InitData() {
            @Override
            public void onSuccess(ArrayList<Province> data, Province argo, Province.City arg1, Province.City.District arg2) {
                pickerView = new AddressPickerView(AddAdressActivity.this, data);
                if (argo == null) {
                    //默认地区
                    pickerView.setRegion("北京市", "北京市", "东城区");
                } else {
                    txProvince.setText(argo.getName() + "-" + arg1.getName() + "-" + arg2.getName());
                    pickerView.setRegion(argo.getName(), arg1.getName(), arg2.getName());
                }
                pickerView.setCallback(new InitAddressTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province argo, Province.City arg1, Province.City.District arg2) {
                        province = argo;
                        city = arg1;
                        district = arg2;
                        txProvince.setText(province.getName() + "-" + city.getName() + "-" + district.getName());
                    }
                });

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    pickerView.showPicker();
                }
            }

            @Override
            public void onFailed() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                showToast("数据初始化失败");
            }
        });
        task.execute();
    }

}
