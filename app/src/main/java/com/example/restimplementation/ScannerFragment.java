package com.example.restimplementation;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.restimplementation.ui.home.HomeFragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScannerFragment extends Fragment implements  HomeFragment.OnFragmentInteractionListener{
    SharedPreferences sp;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String last_barcode;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private OnFragmentInteractionListener mListener;

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    public ScannerFragment() {
        // Required empty public constructor
    }
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_scanner, container, false);
        surfaceView = root.findViewById(R.id.surfaceView);
        last_barcode="";
        sp = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        txtBarcodeValue=root.findViewById(R.id.barcodeDataID);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getActivity(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getActivity(), "Come back to scan more", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            String barcodeScanTime;
                            String barcodeFormat;
                            String barcodeValueType;
                            String barcodeScanDate;
                            Toast.makeText(getActivity(), "Scanned", Toast.LENGTH_SHORT).show();
                            if(!last_barcode.equals(barcodes.valueAt(0).displayValue)) {
                                //Date
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
                                Date date = new Date();
                                barcodeScanDate=dateFormat.format(date);
                                //Time
                                DateFormat timeFormat = new SimpleDateFormat("HH:MM:SS");
                                barcodeScanTime=timeFormat.format(date);
                                //Barcode Format
                                int barForm,barcodeValueFormat;
                                barForm=barcodes.valueAt(0).format;
                                barcodeValueFormat= barcodes.valueAt(0).valueFormat;
                                switch(barForm)
                                {
                                    case 0:
                                        barcodeFormat="ALL_FORMATS";
                                        break;
                                    case 1:
                                        barcodeFormat="CODE_128";
                                        break;
                                    case 2:
                                        barcodeFormat="CODE_39";
                                        break;
                                    case 4:
                                        barcodeFormat="CODE_93";
                                        break;
                                    case 8:
                                        barcodeFormat="CODABAR";
                                        break;
                                    case 16:
                                        barcodeFormat="DATA_MATRIX";
                                        break;
                                    case 32:
                                        barcodeFormat="EAN_13";
                                        break;
                                    case 64:
                                        barcodeFormat="EAN_8";
                                        break;
                                    case 128:
                                        barcodeFormat="ITF";
                                        break;
                                    case 256:
                                        barcodeFormat="QR CODE";
                                        break;
                                    case 512:
                                        barcodeFormat="UPC-A";
                                        break;
                                    case 1024:
                                        barcodeFormat="UPC-E";
                                        break;
                                    case 2048:
                                        barcodeFormat="PDF417";
                                        break;
                                    case 4096:
                                        barcodeFormat="AZTEC";
                                        break;
                                    default:
                                        barcodeFormat="Invalid";
                                        break;
                                }
                                //Barcode Value Format
                                switch(barcodeValueFormat)
                                {
                                    case 1:
                                        barcodeValueType="CONTACT INFO";
                                        break;
                                    case 2:
                                        barcodeValueType="EMAIL";
                                        break;
                                    case 3:
                                        barcodeValueType="ISBN";
                                        break;
                                    case 4:
                                        barcodeValueType="PHONE";
                                        break;
                                    case 5:
                                        barcodeValueType="PRODUCT";
                                        break;
                                    case 6 :
                                        barcodeValueType="SMS";
                                        break;
                                    case 7:
                                        barcodeValueType="TEXT";
                                        break;
                                    case 8:
                                        barcodeValueType="URC";
                                        break;
                                    case 9:
                                        barcodeValueType="WI-FI";
                                        break;
                                    case 10:
                                        barcodeValueType="GIO";
                                        break;
                                    case 11:
                                        barcodeValueType="CALENDAR EVENTS";
                                        break;
                                    case 12:
                                        barcodeValueType="DRIVER LICENCE";
                                        break;
                                    default:
                                        barcodeValueType="Invalid";
                                        break;
                                }

                                InsertBarcodeData ibd = new InsertBarcodeData();
                                String result = ibd.InsertBarcode(sp.getString("UserIDKey",""),barcodes.valueAt(0).displayValue,barcodeFormat,barcodeValueType,barcodeScanDate,barcodeScanTime);
                                last_barcode=barcodes.valueAt(0).displayValue;

                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
