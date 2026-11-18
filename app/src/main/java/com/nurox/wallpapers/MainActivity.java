package com.nurox.wallpapers;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.content.ContentValues;
import android.provider.MediaStore;
import android.content.ContentResolver;
import android.net.Uri;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {
    private WebView web;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(5,6,15));
        getWindow().setNavigationBarColor(android.graphics.Color.rgb(5,6,15));
        web = new WebView(this);
        web.setBackgroundColor(android.graphics.Color.rgb(5,6,15));
        WebSettings s = web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setLoadsImagesAutomatically(true);
        web.setWebViewClient(new WebViewClient()); web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new NativeBridge(), "NurOX");
        setContentView(web); web.loadUrl("file:///android_asset/index.html");
    }
    public class NativeBridge {
        @JavascriptInterface public void toast(String msg) { runOnUiThread(() -> Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show()); }
        @JavascriptInterface public void downloadWallpaper(String imageUrl) {
            new Thread(() -> {
                try {
                    Bitmap b = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "NurOX_" + System.currentTimeMillis() + ".jpg");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/NurOX");
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    OutputStream out = getContentResolver().openOutputStream(uri);
                    b.compress(Bitmap.CompressFormat.JPEG, 95, out); out.close();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this,"Saved to Pictures/NurOX",Toast.LENGTH_SHORT).show());
                } catch(Exception e) { runOnUiThread(() -> Toast.makeText(MainActivity.this,"Download failed. Check internet.",Toast.LENGTH_LONG).show()); }
            }).start();
        }
        @JavascriptInterface public void setWallpaper(String imageUrl) {
            new Thread(() -> { try { Bitmap b = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent()); WallpaperManager.getInstance(MainActivity.this).setBitmap(b); runOnUiThread(() -> Toast.makeText(MainActivity.this,"Wallpaper applied",Toast.LENGTH_SHORT).show()); } catch(Exception e) { runOnUiThread(() -> Toast.makeText(MainActivity.this,"Could not apply wallpaper. Check internet.",Toast.LENGTH_LONG).show()); } }).start();
        }
    }
    @Override public void onBackPressed() { if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
