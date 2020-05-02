package com.example.restimplementation;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class ExportPdfFragment extends Fragment {
    WebView mWebView;
    public ExportPdfFragment() {
        // Required empty public constructor
    }
    ImageButton download_pdf;
    DownloadManager downloadManager;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_export_pdf, container, false);
        download_pdf=view.findViewById(R.id.imageButton);
        download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadManager=(DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                Uri uri= Uri.parse("http://scanner-app.sarangtech.co.in/pdfview?download=pdf");
                DownloadManager.Request request=new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                long reference=downloadManager.enqueue(request);
                onPdfDownload();
            }
        });

        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.loadUrl("http://scanner-app.sarangtech.co.in/tableshow");
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());


        return view;
    }

    private void onPdfDownload() {

    }
}
