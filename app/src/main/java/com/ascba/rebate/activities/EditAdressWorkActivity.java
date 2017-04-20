package com.ascba.rebate.activities;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 编辑收货地址
 */

public class EditAdressWorkActivity extends BaseNetWorkActivity {

    private ShopABarText bar;
    private RelativeLayout btn_contact;
    private Context context;
    private String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
    };
    private EditText name, phone, address;
    private CheckBox chbDefault;
    private DialogManager dm;
    private ReceiveAddressBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            bean = intent.getParcelableExtra("address");
            initView();
        } else {
            showToast("发生错误！");
            finish();
        }
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
                submitData();
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
        name.setText(bean.getName());
        phone = (EditText) findViewById(R.id.address_phone);
        phone.setText(bean.getPhone());
        address = (EditText) findViewById(R.id.address);
        address.setText(bean.getAddress());
        chbDefault = (CheckBox) findViewById(R.id.chb_default);
        if (bean.getIsDefault().equals("1")) {
            chbDefault.setChecked(true);
        } else {
            chbDefault.setChecked(false);
        }
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
     *
     * @param uri
     * @return
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
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.memberAddressEdit, 0, true);
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("member_address_id", bean.getId());
        jsonRequest.add("consignee", name.getText().toString().trim());//收货人
        jsonRequest.add("mobile", phone.getText().toString().trim());//手机号
        jsonRequest.add("province", 1);//省份ID
        jsonRequest.add("city", 710682);//市ID
        jsonRequest.add("district", 1106);//地区ID
        jsonRequest.add("twon", 1158);//乡镇ID
        jsonRequest.add("address", address.getText().toString().trim());//地址内容
        jsonRequest.add("default", chbDefault.isChecked() ? 1 : 0);//是否默认——1：是， 0——否
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                Intent intent = new Intent();
                setResult(2, intent);
                dm.buildAlertDialog("保存成功");
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
            }
        });
    }
}
