package com.example.youmehe.intellectualpropertyright.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.youmehe.intellectualpropertyright.Bean.AreaBean;
import com.example.youmehe.intellectualpropertyright.Bean.ChangeIconEntity;
import com.example.youmehe.intellectualpropertyright.Bean.ShortResponseEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.SPUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;

public class EditUserInfoActivity extends AppCompatActivity {

  @Bind(R.id.image_edit_user_icon) CircleImageView imageEditUserIcon;
  @Bind(R.id.linear_edit_user_icon) LinearLayout linearEditUserIcon;
  @Bind(R.id.txt_edit_user_nickname) TextView txtEditUserNickname;
  @Bind(R.id.linear_edit_user_nickname) LinearLayout linearEditUserNickname;
  @Bind(R.id.txt_edit_user_sexual) TextView txtEditUserSexual;
  @Bind(R.id.linear_edit_user_sexual) LinearLayout linearEditUserSexual;
  @Bind(R.id.txt_edit_user_birthday) TextView txtEditUserBirthday;
  @Bind(R.id.linear_edit_user_birthday) LinearLayout linearEditUserBirthday;
  @Bind(R.id.txt_edit_user_area) TextView txtEditUserArea;
  @Bind(R.id.linear_edit_user_area) LinearLayout linearEditUserArea;
  @Bind(R.id.activity_edit_my_info) LinearLayout activityEditMyInfo;

  SharedPreferences sp;
  String area, name, icon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_user_info);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    //解析省市区
    mHandler.sendEmptyMessage(MSG_LOAD_DATA);

    sp = getSharedPreferences("myInfo", MODE_PRIVATE);
    if (null != imageEditUserIcon) {
      //如果本地有头像缓存则直接设置
      final File file = new File(getExternalCacheDir(), "/userCropTemp.png");
      if (file.exists()) {
        imageEditUserIcon.setImageURI(Uri.fromFile(file));
      }
    }
    name = sp.getString("userName", "");
    sexual = sp.getString("userSexual", "");
    birthday = sp.getString("userBirthday", "");
    area = sp.getString("userArea", "");
    icon = sp.getString("userIcon", "");
    if (!TextUtils.isEmpty(sexual)) {
      txtEditUserSexual.setText(sexual);
    }
    if (!TextUtils.isEmpty(birthday)) {
      txtEditUserBirthday.setText(birthday);
    }
    if (!TextUtils.isEmpty(area)) {
      txtEditUserArea.setText(area);
    }
    if (!TextUtils.isEmpty(name)) {
      txtEditUserNickname.setText(name);
    }
  }

  private String sexual;

  //----------------------------------------------更改性别部分开始-------------------------------------
  @OnClick(R.id.linear_edit_user_sexual) void changeUserSexual() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final View view = View.inflate(this, R.layout.dialog_edit_user_sexual, null);
    builder.setView(view);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (((RadioButton) view.findViewById(R.id.radio_men)).isChecked()) {
          sexual = "男";
          txtEditUserSexual.setText("男");
        }
        if (((RadioButton) view.findViewById(R.id.radio_women)).isChecked()) {
          sexual = "女";
          txtEditUserSexual.setText("女");
        }
        if (((RadioButton) view.findViewById(R.id.radio_sercert)).isChecked()) {
          sexual = "保密";
          txtEditUserSexual.setText("保密");
        }
        submitEdit(name, sexual, birthday, area);
      }
    });
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    builder.show();
  }

  private void submitEdit(final String userName, final String userSexual,
      final String userBirthday,
      final String userArea) {
    Log.e("tag", "submitEdit: "
        + userName
        + "|"
        + userSexual
        + "|"
        + userBirthday
        + "|"
        + userArea);
    //网络访问数据库完成登录操作
    new Thread(new Runnable() {
      @Override public void run() {
        Gson gson = new Gson();
        String updateJson = NetWorkUtils.getInstance()
            .updateSelf(userName, userSexual, userBirthday, userArea);
        ShortResponseEntity mShortResponseEntity =
            gson.fromJson(updateJson, ShortResponseEntity.class);
        Log.e("tag", "run: " + updateJson);
        //返回-1表示用户名存在
        if (mShortResponseEntity.getRet_code() == 0) {
          mHandler.sendEmptyMessage(SUBMIT_EDIT_INFO_SUCCESS);
        } else {
          //登录失败时获取服务返回的错误码
          mHandler.sendEmptyMessage(SUBMIT_EDIT_INFO_FAILED);
        }
      }
    }).start();
  }
  //----------------------------------------------更改性别部分结束-------------------------------------

  private String birthday;

  //----------------------------------------------更改生日部分开始-------------------------------------
  @OnClick(R.id.linear_edit_user_birthday) void changeUserBirthday() {
    new DatePickerDialog(this,
        // 绑定监听器
        new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date selecteDate = new Date(year - 1900, monthOfYear, dayOfMonth);
            if (selecteDate.getTime() > new Date().getTime()) {
              T.shortToast(EditUserInfoActivity.this, "请选择正确的日期");
            } else {
              T.shortToast(EditUserInfoActivity.this, "修改成功");
              birthday = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
              T.shortToast(EditUserInfoActivity.this, birthday);
              txtEditUserBirthday.setText(birthday);
              submitEdit(name, sexual, birthday, area);
            }
          }
        }
        // 设置初始日期
        , 1990, 0, 1).show();
  }
  //----------------------------------------------更改生日部分开始---------------------------

  //----------------------------------------------更改个人信息（昵称，详细地址，大学，院系，专业）部分开始-------------------------------------
  //@OnClick(R.id.linear_edit_user_nickname) void changeUserNickName() {
  //  AlertDialog.Builder build = new AlertDialog.Builder(this);
  //  final View view = View.inflate(this, R.layout.dialog_edit_user_info, null);
  //  build.setView(view);
  //  ((TextView) view.findViewById(R.id.txt_dialog_title)).setText("修改昵称");
  //  build.setPositiveButton("确定", new DialogInterface.OnClickListener() {
  //    @Override
  //    public void onClick(DialogInterface dialog, int which) {
  //      String temp =
  //          ((EditText) view.findViewById(R.id.txt_dialog_content)).getText().toString().trim();
  //
  //      txtEditUserNickname.setText(temp);
  //    }
  //  });
  //
  //  build.setNegativeButton("取消", new DialogInterface.OnClickListener() {
  //    @Override
  //    public void onClick(DialogInterface dialog, int which) {
  //    }
  //  });
  //  build.show();
  //}

  private ArrayList<AreaBean> options1Items = new ArrayList<>();
  private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
  private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
  private Thread thread;
  private final int MSG_LOAD_DATA = 0x0001;
  private final int MSG_LOAD_SUCCESS = 0x0002;
  private final int MSG_LOAD_FAILED = 0x0003;
  private final int SUBMIT_EDIT_INFO_SUCCESS = 0x0004;
  private final int SUBMIT_EDIT_INFO_FAILED = 0x005;
  private final int EXIT_APP = 383;//退出APP
  private final int SERVICE_ERROR = 0x006;//服务器异常
  private boolean isLoaded = false;
  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_LOAD_DATA:
          if (thread == null) {//如果已创建就不再重新创建子线程了
            //Toast.makeText(EditMyInfoActivity.this, "开始解析数据", Toast.LENGTH_SHORT).show();
            thread = new Thread(new Runnable() {
              @Override
              public void run() {
                // 写子线程中的操作,解析省市区数据
                initJsonData();
              }
            });
            thread.start();
          }
          break;

        case MSG_LOAD_SUCCESS:
          isLoaded = true;
          break;

        case MSG_LOAD_FAILED:
          T.shortToast(EditUserInfoActivity.this, "解析数据失败");
          break;

        case SUBMIT_EDIT_INFO_SUCCESS:
          SPUtils mSputils = new SPUtils(EditUserInfoActivity.this, "myInfo");
          mSputils.put("userIcon", icon);
          mSputils.put("userSexual", sexual);
          mSputils.put("userArea", area);
          mSputils.put("userBirthday", birthday);
          break;
        case SERVICE_ERROR:
          T.shortToast(EditUserInfoActivity.this, "网络连接异常");
          break;

        case SUBMIT_EDIT_INFO_FAILED:
          T.shortToast(EditUserInfoActivity.this, "修改信息失败");
          break;
      }
    }
  };

  //解析本地存得省市区信息
  private void initJsonData() {//解析数据

    /**
     * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
     * 关键逻辑在于循环体
     *
     * */
    String JsonData = getJson(this, "province.json");//获取assets目录下的json文件数据

    ArrayList<AreaBean> jsonBean = parseData(JsonData);//用Gson 转成实体

    /**
     * 添加省份数据
     *
     * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
     * PickerView会通过getPickerViewText方法获取字符串显示出来。
     */
    options1Items = jsonBean;

    for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
      ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
      ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

      for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
        String CityName = jsonBean.get(i).getCityList().get(c).getName();
        CityList.add(CityName);//添加城市

        ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
        if (jsonBean.get(i).getCityList().get(c).getArea() == null
            || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
          City_AreaList.add("");
        } else {

          for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size();
              d++) {//该城市对应地区所有数据
            String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

            City_AreaList.add(AreaName);//添加该城市所有地区数据
          }
        }
        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
      }

      /**
       * 添加城市数据
       */
      options2Items.add(CityList);

      /**
       * 添加地区数据
       */
      options3Items.add(Province_AreaList);
    }

    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
  }

  public String getJson(Context context, String fileName) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      AssetManager assetManager = context.getAssets();
      BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
      String line;
      while ((line = bf.readLine()) != null) {
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }

  public ArrayList<AreaBean> parseData(String result) {//Gson 解析
    ArrayList<AreaBean> detail = new ArrayList<>();
    try {
      JSONArray data = new JSONArray(result);
      Gson gson = new Gson();
      for (int i = 0; i < data.length(); i++) {
        AreaBean entity = gson.fromJson(data.optJSONObject(i).toString(), AreaBean.class);
        detail.add(entity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
    }
    return detail;
  }

  //----------------------------------------------更改个人地区开始-------------------------------------
  @OnClick(R.id.linear_edit_user_area) void ShowPickerView() {// 弹出选择器

    OptionsPickerView pvOptions =
        new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int options2, int options3, View v) {
            //返回的分别是三个级别的选中位置
            String tx = options1Items.get(options1).getPickerViewText() + " " +
                options2Items.get(options1).get(options2) + " " +
                options3Items.get(options1).get(options2).get(options3);
            area = tx;
            txtEditUserArea.setText(tx);
            submitEdit(name, sexual, birthday, area);
          }
        })

            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .setOutSideCancelable(false)// default is true
            .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
    pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
    pvOptions.show();
  }
  //----------------------------------------------更改个人地区开始-------------------------------------

  private Uri photoUri;//跳转至裁图时使用的Uri
  private final int TAKE_PHOTO = 0;//拍照
  private final int PICK_PHOTO = 1;//相册
  private final String USER_ICON_CROP_TEMP_PNG = "/userCropTemp.png";//头像缓存文件
  private Bitmap temp;

  //---------------------------------------------------更改头像部分开始--------------------------------
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case TAKE_PHOTO:
          //2017.4.7 by WYT
          //拍照后直接进入UcropActivity进行裁剪
          startCropActivity(photoUri);
          break;
        case PICK_PHOTO:
          //2017.4.7 by WYT
          //data中自带有返回的uri，获取通过相册选中的照片进入UcropActivity进行裁剪
          photoUri = data.getData();
          startCropActivity(photoUri);
          break;
        case UCrop.REQUEST_CROP:
          //2017.4.7 by WYT
          //裁剪完毕后就如这里获取到裁剪后的图片更新头像
          Uri resultUri = null;
          if (null != data) {
            resultUri = UCrop.getOutput(data);
          }
          if (resultUri != null) {
            ContentResolver contentProvider = getContentResolver();
            ParcelFileDescriptor mPhoto;
            try {
              //获取contentProvider图片
              mPhoto = contentProvider.openFileDescriptor(resultUri, "r");
              FileDescriptor fileDescriptor = null;
              if (mPhoto != null) {
                fileDescriptor = mPhoto.getFileDescriptor();
              }
              temp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
              imageEditUserIcon.setImageBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor));
              uploadPicBg();
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }
          } else {
            T.shortToast(this, getString(R.string.user_fragment_icon_set_failed));
          }
          break;
      }
    }
  }

  //弹出Dialog选择设置头像方式  2017.4.7  by  WYT
  @OnClick(R.id.linear_edit_user_icon) void OnAlertIconClick() {
    new AlertDialog.Builder(this).setTitle(getString(R.string.user_fragment_icon_switch_title))
        .setNegativeButton(getString(R.string.user_fragment_icon_from_pick_picture),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                userIconFromPicture();
              }
            })
        .setPositiveButton(getString(R.string.user_fragment_icon_from_take_photo),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                userIconFromCamera();
              }
            })
        .show();
  }

  //跳转到相册
  private void userIconFromPicture() {
    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(albumIntent, PICK_PHOTO);
  }

  //跳转到拍照
  private void userIconFromCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //拍照后的图片在指定位置:storage/emulated/0/Android/data/com.ruanyikeji.vesal.vesal/cache/userCropTemp.png
    File outputImage = new File(this.getExternalCacheDir(), "/cameraTemp.png");
    //Android N后若要在应用间共享文件，应发送一项 content://URI，并授予 URI 临时访问权限。进行此授权的最简单方式是使用 FileProvider类
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      photoUri = Uri.fromFile(outputImage);
    } else {
      photoUri =
          FileProvider.getUriForFile(this,
              "com.example.youmehe.intellectualpropertyright.fileprovider", outputImage);
      intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
    startActivityForResult(intent, TAKE_PHOTO);
  }

  //进入裁剪   2017.4.7 by WYT
  private void startCropActivity(@NonNull Uri uri) {
    //第一个uri是来源，第二个是参见后输出的uri，并设置了裁剪后的图片大小
    UCrop uCrop =
        UCrop.of(uri, Uri.fromFile(new File(getExternalCacheDir(), USER_ICON_CROP_TEMP_PNG)))
            .withMaxResultSize(300, 300);
    uCrop.start(this);
  }

  private void uploadPicBg() {

    new Thread() {
      public void run() {
        String retString = NetWorkUtils.getInstance().uploadFileToPhpServer(
            "http://youmehe.wang/universityWork/upload_img.php"
                + "?user_name=" + name + "&which_one=user_img",
            getExternalCacheDir() + "/userCropTemp.png");
        Log.e("tag", "run: " + retString);
        Gson gson = new Gson();
        ChangeIconEntity mChangeIconEntity = gson.fromJson(retString, ChangeIconEntity.class);
        if (mChangeIconEntity.getRet_code() == 0) {
          mHandler.sendEmptyMessage(SUBMIT_EDIT_INFO_SUCCESS);
          setResult(23);
        } else {
          mHandler.sendEmptyMessage(SUBMIT_EDIT_INFO_FAILED);
        }
      }
    }.start();
  }
}
