/*package com.shang.noticeuefa.weibo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 

import com.mobclick.android.MobclickAgent;
import com.shang.noticeuefa.R;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class SinaShareActivity extends Activity implements OnClickListener,
        RequestListener {
    
    public static String  RESPONSE = null;
    
    private TextView mTextNum;

    private Button mSend;

    private EditText mEdit;

    private FrameLayout mPiclayout;

    private ImageButton cameraImageButton;

    private ImageButton photoChooseButton;
    private ImageButton locationButton;

    private ImageView image = null;

    private String mPicPath = "";

    private String mContent = "";

    private String mAccessToken = "";

    private String mTokenSecret = "";

    private String timestamp = null;
    private long weiboId;
    private int currentMode;

    public static final String EXTRA_WEIBO_CONTENT = "com.weibo.android.content";

    public static final String EXTRA_PIC_URI = "com.weibo.android.pic.uri";
    
    public static final String EXTRA_ACCESS_TOKEN = "com.weibo.android.accesstoken";

    public static final String EXTRA_TOKEN_SECRET = "com.weibo.android.token.secret";
    public static final String EXTRA_WEIBO_ID = "com.weibo.android.weiboid";
    public static final String EXTRA_MODE = "mode";
    public static final int UPLOAD_MODE = 0;
    public static final int REPOST_MODE = 1;
    public static final int COMMENT_MODE = 2;
    public static int[] MODE_STRINGID= {R.string.send, R.string.repost,R.string.comment};
   
    public static final int WEIBO_MAX_LENGTH = 140;

    public static final int NONE = 0;

    public static final int PHOTOHRAPH = 1;

    public static final int PHOTOZOOM = 2;

    public static final int PHOTORESOULT = 3;

    public static final int PHOTORESOULTFROMPHOTOHRAPH = 4;
    
    public static final int MAXSIDELENGTH = 800;
    private static boolean isSetPic= false;
    private String mylon = "";
    private String mylat = "";
    Bitmap thephoto = null;
    LocationTools locationTools;
    private final String picdir = Environment.getExternalStorageDirectory() + File.separator+"tclubpic"+File.separator;
    private String tempPicPath = picdir + "temp"  + ".jpg";
 
    
    private String getPicfilename(String timestamp) {
      return   picdir+"temp"+ timestamp + ".jpg" ; 
    }
     

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        this.setContentView(R.layout.tr_share_mblog_view);
        
        File tempdirFile= new File(picdir);
        if(!tempdirFile.exists()) {
            tempdirFile.mkdirs();
        }

        Intent in = this.getIntent();
        mPicPath = in.getStringExtra(EXTRA_PIC_URI);
        mContent = in.getStringExtra(EXTRA_WEIBO_CONTENT);
        mAccessToken = in.getStringExtra(EXTRA_ACCESS_TOKEN);
        mTokenSecret = in.getStringExtra(EXTRA_TOKEN_SECRET);
        weiboId = in.getLongExtra(EXTRA_WEIBO_ID, 0);
        currentMode = in.getIntExtra(EXTRA_MODE, UPLOAD_MODE);
        
        AccessToken accessToken = new AccessToken(mAccessToken, mTokenSecret);
        Weibo weibo = Weibo.getInstance();
        weibo.setAccessToken(accessToken);

        Button close = (Button) this.findViewById(R.id.btnClose);
        close.setOnClickListener(this);
        mSend = (Button) this.findViewById(R.id.btnSend);
        mSend.setOnClickListener(this);
        LinearLayout total = (LinearLayout) this
                .findViewById(R.id.ll_text_limit_unit);
        total.setOnClickListener(this);
        mTextNum = (TextView) this.findViewById(R.id.tv_text_limit);
        ImageView picture = (ImageView) this.findViewById(R.id.ivDelPic);

        picture.setOnClickListener(this);

        mEdit = (EditText) this.findViewById(R.id.etEdit);
        mEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                String mText = mEdit.getText().toString();
                String mStr;
                int len = mText.length();
                if (len <= WEIBO_MAX_LENGTH) {
                    len = WEIBO_MAX_LENGTH - len;
                    mTextNum.setTextColor(R.color.text_num_gray);
                    if (!mSend.isEnabled())
                        mSend.setEnabled(true);
                } else {
                    len = len - WEIBO_MAX_LENGTH;

                    mTextNum.setTextColor(Color.RED);
                    if (mSend.isEnabled())
                        mSend.setEnabled(false);
                }
                mTextNum.setText(String.valueOf(len));
            }
        });
        mEdit.setText(mContent);
        
        locationButton = (ImageButton) findViewById(R.id.location_btn);
        cameraImageButton = (ImageButton) findViewById(R.id.camera_btn);
        photoChooseButton = (ImageButton) findViewById(R.id.photochoose_btn);
        mPiclayout = (FrameLayout) SinaShareActivity.this.findViewById(R.id.flPic);
        image = (ImageView) this.findViewById(R.id.ivImage);
        if (TextUtils.isEmpty(this.mPicPath)) {
            mPiclayout.setVisibility(View.GONE);
            } else {
                        mPiclayout.setVisibility(View.VISIBLE);
                File file = new File(mPicPath);
                if (file.exists()) {
                    Bitmap pic = BitmapFactory.decodeFile(this.mPicPath);
            
                    image.setImageBitmap(pic);
                } else {
                    mPiclayout.setVisibility(View.INVISIBLE);
                }
            }
        
        if(currentMode != UPLOAD_MODE) {
            mSend.setText(MODE_STRINGID[currentMode]);
            
            locationButton.setVisibility(View.GONE);
            cameraImageButton.setVisibility(View.GONE);
            photoChooseButton.setVisibility(View.GONE);
            return;
        }
        
        locationButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(locationTools == null)
                    locationTools = new LocationTools(SinaShareActivity.this, new Handler() {
                        public void handleMessage(Message msg) {  
                            
                            Location location = locationTools.getNowLocation() ; 
                               // mylon =String.format("%.4f", location.getLongitude());
                               // mylat =String.format("%.4f", location.getLatitude()); 
                            mylon =String.valueOf(location.getLongitude());  
                            mylat =String.valueOf(location.getLatitude());  
                            Log.d("mylon",mylon);
                            Log.d("mylat",mylat);
                            Toast.makeText(v.getContext(),"位置信息: lon:"+mylon+" lat:"+mylat,1500).show();
                            locationTools.stopListen() ;
                            
                        }
                    });
                locationTools.startListen() ; 
               
            }
        }); 
         
        cameraImageButton.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Date nowtime = new Date();
                timestamp = String.valueOf(nowtime.getTime());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                         getPicfilename(timestamp))));
                startActivityForResult(intent, PHOTOHRAPH);
            }
        });

         
        photoChooseButton.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                timestamp = String.valueOf(new Date().getTime());
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTOZOOM);
            }
        });
 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(getPicfilename(timestamp));
            startPhotoZoomfromPHOTOHRAPH(Uri.fromFile(picture));
        }

        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Log.d("tag1", "PHOTORESOULT!!!");
            Bundle extras = data.getExtras();

            if (extras != null) {
               
                Log.d("tagextras", extras.toString()); 
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                cursor.moveToFirst();

                String imgPath = cursor.getString(1); // 图片文件路径
                String imgName = cursor.getString(3); // 图片文件名
                Log.d("imgPath", imgPath);
                Log.d("imgName", imgName);
                // Bitmap photo = extras.getParcelable("data");

                image.setImageURI(data.getData());
                if (thephoto != null)
                    thephoto.recycle();
                // image.setImageBitmap(photo);
                mPicPath = imgPath;
                mPiclayout.setVisibility(View.VISIBLE);
                cursor.close();
                thephoto = BitmapFactory.decodeFile(mPicPath, getBitmapOption());
            
                try {
                    saveBitmap2File(tempPicPath,
                            DrawTools.limitBitmap(thephoto, MAXSIDELENGTH)); 
                    isSetPic = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (thephoto != null)
                    thephoto.recycle();

            }

        }

        if (requestCode == PHOTORESOULTFROMPHOTOHRAPH) {
            Log.d("tag1", "PHOTORESOULTFROMPHOTOHRAPH"); 
         //   mPicPath = getPicfilename(timestamp);
             
            mPicPath = tempPicPath;   //NOTE:startPhotoZoomfromPHOTOHRAPH note处 如未注释需要此行
   
            if (thephoto != null)
                thephoto.recycle();
            
            thephoto = BitmapFactory.decodeFile(mPicPath, getBitmapOption());
            image.setImageBitmap(thephoto);
            mPiclayout.setVisibility(View.VISIBLE);
         //   mPicPath = tempPicPath;
             try {
                saveBitmap2File(tempPicPath, DrawTools.limitBitmap(thephoto, MAXSIDELENGTH));
                isSetPic = true;
            } catch (IOException e) {
                e.printStackTrace();
            } 
             

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static final String IMAGE_UNSPECIFIED = "image/*";

    public void startPhotoZoomfromPHOTOHRAPH(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        // intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", "JPEG");
 
        
        //TODO:NOTE:此处裁剪手机output没问题，x86模拟器也没问题,待改进,取消此处注释,
        //PHOTORESOULTFROMPHOTOHRAPH 处也需修改
        intent.putExtra("output", Uri.parse( "file:/" +new File(tempPicPath)) );
        
        startActivityForResult(intent, PHOTORESOULTFROMPHOTOHRAPH);
    }

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        // intent.putExtra("crop", "true"); 
        // intent.putExtra("outputX", 600);
        // intent.putExtra("outputY", 600);
        // intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", false);

        startActivityForResult(intent, PHOTORESOULT);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btnClose) {
            finish();
        } else if (viewId == R.id.btnSend) {
            Weibo weibo = Weibo.getInstance();
            try {
                if (!TextUtils.isEmpty((String) (weibo.getAccessToken()
                        .getToken()))) { 
                    this.mContent = mEdit.getText().toString();
                    
                    switch(currentMode) {
                    case UPLOAD_MODE:
                        Log.d("mylon11","mylon: "+mylon);
                        Log.d("mylat11","mylat: "+mylat);
                        if (!TextUtils.isEmpty(tempPicPath) && isSetPic)  {
                            showDialog(DIALOG_PROGRESS_BAR);
                            upload(weibo, Weibo.getAppKey(), tempPicPath,
                                    this.mContent, mylon, mylat);
                           
                        } else {
                            // Just update a text weibo!
                            showDialog(DIALOG_PROGRESS_BAR);
                            update(weibo, Weibo.getAppKey(), mContent,  mylon, mylat);
                         
                        }
                        
                        break;
                    case REPOST_MODE:
                        if(weiboId != 0) {
                            repost(weibo, Weibo.getAppKey(), this.mContent, weiboId);
                        }
                        break;
                         
                        
                    case COMMENT_MODE:
                        if(weiboId != 0) {
                            comment(weibo, Weibo.getAppKey(), this.mContent, weiboId);
                        }
                        break;
                        
                    }
             
                    
                } else {
                    Toast.makeText(this, this.getString(R.string.please_login),
                            Toast.LENGTH_LONG);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WeiboException e) {
                e.printStackTrace();
            }
            if(dialog != null)
                dialog.dismiss();
        } else if (viewId == R.id.ll_text_limit_unit) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.attention)
                    .setMessage(R.string.delete_all)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    mEdit.setText("");
                                }
                            }).setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        } else if (viewId == R.id.ivDelPic) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.attention)
                    .setMessage(R.string.del_pic)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    mPiclayout.setVisibility(View.GONE);
                                    mPicPath = "";
                                    isSetPic = false;
                                }
                            }).setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        }
    }

    private String upload(Weibo weibo, String source, String file,
            String status, String lon, String lat) throws WeiboException {
   
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("pic", file);
        bundle.add("status", status);
        Log.d("upload","lon@@@@ "+lon);
        Log.d("upload","lat@@@@ "+lat);
        //注意key名叫long
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        String rlt = "";
        String url = Weibo.SERVER + "statuses/upload.json";
       // rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST, weibo.getAccessToken());
        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
        weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);
        
        return rlt;
    }

    private String update(Weibo weibo, String source, String status,
            String lon, String lat) throws MalformedURLException, IOException,
            WeiboException {
         
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("status", status);
        //注意key名叫long
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        String rlt = "";
        String url = Weibo.SERVER + "statuses/update.json";
        
       //rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST, weibo.getAccessToken());
       AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
       weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);
     
        return rlt;
    }
    
    
    private String repost(Weibo weibo, String source, String status,
           long weiboId) throws MalformedURLException, IOException,
            WeiboException { 
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("status", status);
        bundle.add("id", String.valueOf(weiboId));  
        String rlt = "";
        String url = Weibo.SERVER + "statuses/repost.json";     
       AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
       weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this); 
       return rlt;
    }
    
    private String comment(Weibo weibo, String source, String comment,
            long weiboId) throws MalformedURLException, IOException,
             WeiboException { 
         WeiboParameters bundle = new WeiboParameters();
         bundle.add("source", source);
         bundle.add("comment", comment);
         bundle.add("id", String.valueOf(weiboId));  
         String rlt = "";
         String url = Weibo.SERVER + "comments/create.json";     
        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
        weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this); 
        return rlt;
     }

    @Override
    public void onComplete(final String response) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                 
                RESPONSE = response;
                Toast.makeText(SinaShareActivity.this, R.string.send_sucess,
                        Toast.LENGTH_LONG).show();
            }
        });

        this.finish();
    }

    @Override
    public void onIOException(IOException e) {
        RESPONSE = null;
    }

    @Override
    public void onError(final WeiboException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RESPONSE = null;
                Toast.makeText(
                        SinaShareActivity.this,
                        String.format(
                                SinaShareActivity.this
                                        .getString(R.string.send_failed)
                                        + ":%s", e.getMessage()),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void saveBitmap2File(String fileName, Bitmap bitmap)
            throws IOException {
        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
        f.createNewFile();
        FileOutputStream fOutputStream = null;
        try {
            fOutputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutputStream);
            fOutputStream.flush();
            fOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

         
        // bitmap.recycle();
    }
    
    private BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }
    
    private AlertDialog dialog;
    private static final int DIALOG_PROGRESS_BAR =  1; ;
    protected Dialog onCreateDialog(int id) {
         if (id == DIALOG_PROGRESS_BAR) {
             Log.d("tag", "##########!!!!!!!!!!!");
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.updating));
            ((ProgressDialog) dialog).setIndeterminate(true);
            dialog.setCancelable(true);
            return dialog;
        } 
         
 
        return super.onCreateDialog(id);
        }
    
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        }
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        }
    

}
*/