package com.example.qianyl.cameraalbumtest;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takePhoto=(Button)findViewById(R.id.take_photo);
        picture=(ImageView)findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        Log.d("MainActivity","outputImage delete");
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24){
                    Log.d("MainActivity","version android 7.0+");
                    imageUri= FileProvider.getUriForFile(MainActivity.this,
                            "com.example.qianyl.cameraalbumtest.fileprovider",outputImage);
                }else{
                    Log.d("MainActivity","version android 7.0-");
                    imageUri=Uri.fromFile(outputImage);
                    Log.d("MainActivity",imageUri.toString());
                }
                //startup the camera
                //Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
        Button chooseFromAlbum=(Button)findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new
                            String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
    }
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case TAKE_PHOTO:
               Log.d("MainActivity","TAKE_PHOTO OK");
               Log.d("MainActivity",Integer.toString(resultCode));
               Log.d("MainActivity",Integer.toString(RESULT_OK));
               if (resultCode==RESULT_OK){
                   try{
                       //show the picture
                       Log.d("MainActivity","show picture");
                       Log.d("MainActivity",imageUri.toString());
                       Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().
                               openInputStream(imageUri));
                      // Bitmap bitmap=data.getParcelableExtra("data");
                       Log.d("MainActivity",bitmap.toString());
                       picture.setImageBitmap(bitmap);
                   }catch (FileNotFoundException e){
                       e.printStackTrace();
                   }
               }
               break;
           case CHOOSE_PHOTO:
               if (resultCode == RESULT_OK){
                   if (Build.VERSION.SDK_INT >= 19){
                       handleImageOnKitKat(data);
                   }else{
                       handleImageBeforeKitKat(data);
                   }
               }
               break;
           default:
               break;
       }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri=data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //if the type of document is Uri,using document id
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.document".equals(uri.getAuthority())){
                String id =docId.split(":")[1];//get the Id in number format
                String selection=MediaStore.Images.Media._ID + "=" + id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads" +
                        "/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //if the type of Uri is content,useing the comment way
            imagePath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //if the type of Uri is content,directly tack the path of the image
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path =null;
        //using uri and selection to get the path of real image
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
