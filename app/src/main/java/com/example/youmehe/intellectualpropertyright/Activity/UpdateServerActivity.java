package com.example.youmehe.intellectualpropertyright.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.youmehe.intellectualpropertyright.Bean.ChangeIconEntity;
import com.example.youmehe.intellectualpropertyright.R;
import com.example.youmehe.intellectualpropertyright.Utils.NetWorkUtils;
import com.example.youmehe.intellectualpropertyright.Utils.T;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class UpdateServerActivity extends AppCompatActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.image_post_icon) ImageView imagePostIcon;
  @Bind(R.id.edit_post_title) EditText editPostTitle;
  @Bind(R.id.edit_post_content) EditText editPostContent;
  @Bind(R.id.edit_post_price) EditText editPostPrice;
  @Bind(R.id.btn_update_post) AppCompatButton btnUpdatePost;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_server);
    ButterKnife.bind(this);
  }

  private Uri photoUri;//跳转至裁图时使用的Uri
  private final int TAKE_PHOTO = 0;//拍照
  private final int PICK_PHOTO = 1;//相册
  private final String USER_ICON_CROP_TEMP_PNG = "/postUserCropTemp.png";//头像缓存文件
  private Bitmap temp;
  private boolean isChangePostIcon = false;

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
              imagePostIcon.setImageBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor));
              isChangePostIcon = true;
              //uploadPicBg();
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
  @OnClick(R.id.image_post_icon) void OnAlertIconClick() {
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
    File outputImage = new File(this.getExternalCacheDir(), "/postCameraTemp.png");
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
            .withMaxResultSize(500, 500);
    uCrop.start(this);
  }

  @OnClick(R.id.btn_update_post) void updateServer() {
    SharedPreferences sp = getSharedPreferences("myInfo", MODE_PRIVATE);
    final String name = sp.getString("userName", "");
    final String title = editPostTitle.getText().toString().trim();
    final String content = editPostContent.getText().toString().trim();
    final String price = editPostPrice.getText().toString().trim();
    if (!isChangePostIcon) {
      T.shortToast(UpdateServerActivity.this, "请上传封面");
      return;
    }
    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(price)) {
      new Thread() {
        public void run() {
          String retString = NetWorkUtils.getInstance().uploadFileToPhpServer(
              "http://youmehe.wang/universityWork/upload_product.php"
                  + "?user_name="
                  + name
                  + "&which_one=user_img"
                  + "&title="
                  + title
                  + "&content="
                  + content
                  + "&price="
                  + price
              , getExternalCacheDir() + USER_ICON_CROP_TEMP_PNG);
          Log.e("tag", "run: " + retString);
          Gson gson = new Gson();
          ChangeIconEntity mChangeIconEntity = gson.fromJson(retString, ChangeIconEntity.class);
          if (mChangeIconEntity.getRet_code() == 0) {
            mHandler.sendEmptyMessage(102);
          } else {
            mHandler.sendEmptyMessage(103);
          }
        }
      }.start();
    } else {
      T.shortToast(UpdateServerActivity.this, "信息不完整");
    }
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 102:
          T.shortToast(UpdateServerActivity.this, "服务已上传");
          finish();
          break;

        case 103:
          T.shortToast(UpdateServerActivity.this, "服务上传失败");
          break;
      }
    }
  };
}