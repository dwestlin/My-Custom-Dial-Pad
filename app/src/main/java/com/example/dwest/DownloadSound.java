package com.example.dwest;


import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.content.Intent;
import android.os.AsyncTask;
import android.webkit.WebViewClient;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.app.ProgressDialog;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URL;


public class DownloadSound extends AppCompatActivity
{
    private WebView webView;
    private ExternalStorageHandler storageHandler;

    private String voiceUrl;
    private String storageLocation;
    private String state;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        init();
    }

    private void init(){
        Intent intent = getIntent();

        voiceUrl = getString(R.string.voice_url);
        state = Environment.getExternalStorageState();
        storageLocation = intent.getStringExtra(MainActivity.STORAGE);
        storageHandler = new ExternalStorageHandler(this);


        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new SoundWebView());
        webView.loadUrl(voiceUrl);
    }
    private class SoundWebView extends WebViewClient implements DownloadListener
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.setDownloadListener(this);

            return false;
        }


        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
        {
            if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)|| Environment.MEDIA_MOUNTED.equals(state))
            {
                new Downloader().execute(url);
            }
        }

        private class Downloader extends AsyncTask<String, Integer, String>
        {
            private ProgressDialog downloadDialog;
            private String file = null;
            private String fileNameCheck;
            private File soundFile;


            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                init();

            }

            private void init(){

                downloadDialog = new ProgressDialog(DownloadSound.this);
                downloadDialog.setTitle(getResources().getString(R.string.download_progress));
                downloadDialog.setMax(100);
                downloadDialog.setMessage("");
                downloadDialog.setIndeterminate(false);
                downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                downloadDialog.show();
            }

            @Override
            protected String doInBackground(String... params)
            {
                int startIdx = params[0].lastIndexOf('/') + 1;
                int length = params[0].length();

                file = params[0].substring(startIdx, length);
                soundFile = null;

                fileNameCheck = file.substring(0, file.indexOf('.'));

                if(storageHandler.isDir(storageLocation + fileNameCheck + '/'))
                {
                    return "";
                }

                try
                {
                    URL pUrl = new URL(params[0]);

                    URLConnection urlConnection = pUrl.openConnection();

                    soundFile = new File(getExternalFilesDir("Download"), file);
                    InputStream inputStream = urlConnection.getInputStream();
                    FileOutputStream outPutStream = new FileOutputStream(soundFile);

                    storageHandler.createDir(storageLocation);

                    byte[] data = new byte[1024];
                    int dataCount;
                    long totalData = 0;

                    while((dataCount = inputStream.read(data)) > 0)
                    {
                        totalData += dataCount;
                        outPutStream.write(data, 0, dataCount);

                        int progress = (int) totalData * 100 / urlConnection.getContentLength();
                        publishProgress(progress);
                    }

                    closeStreams(inputStream,outPutStream);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                return soundFile.getAbsolutePath();
            }


            private void closeStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }


            @Override
            protected void onProgressUpdate(Integer... progress)
            {
                super.onProgressUpdate(progress);

                downloadDialog.setMessage(file);
                downloadDialog.setProgress(progress[0]);
            }

            protected void onPostExecute(String fileLocation)
            {
                downloadDialog.dismiss();

                if(ZIP.decompress(fileLocation, storageLocation))
                {
                    Toast.makeText(DownloadSound.this, R.string.download_success, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(DownloadSound.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
