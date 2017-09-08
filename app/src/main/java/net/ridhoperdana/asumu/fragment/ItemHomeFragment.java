package net.ridhoperdana.asumu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.Scanner;
import net.ridhoperdana.asumu.helper.FileImageHelper;

import java.io.FileNotFoundException;

public class ItemHomeFragment extends Fragment {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    private ImageView imgPreview, imgNoPreview;
    private Button btnCapturePicture;
    private TextView textScanned;
    private TextRecognizer recognizer;

    public static ItemHomeFragment newInstance() {
        ItemHomeFragment fragment = new ItemHomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_home, container, false);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            getActivity().finish();
        }

        imgPreview = (ImageView) view.findViewById(R.id.imgPreview);
        imgNoPreview = (ImageView) view.findViewById(R.id.imgNoPreview);
        textScanned = (TextView) view.findViewById(R.id.tvTextScanned);
        btnCapturePicture = (Button) view.findViewById(R.id.btnCapturePicture);

        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        recognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
        Log.d("Test Recognizer", String.valueOf(recognizer));
        if(recognizer.isOperational()) {
            Log.d("recognizer", "can't set up the detector!");
        }

        return view;
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = FileImageHelper.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                launchMediaScanIntent();
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
                imgNoPreview.setVisibility(View.GONE);
                imgPreview.setVisibility(View.VISIBLE);

                Scanner scanner = new Scanner();
                Bitmap bitmap = null;
                try {
                    bitmap = scanner.decodeBitmapUri(getActivity().getApplicationContext(), fileUri);
                    Log.d("bitmap", "can read bitmap");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (recognizer.isOperational() && bitmap != null) {
                    Log.d("masuk recognizer", "masuk");
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> barcodes = recognizer.detect(frame);

                    Log.i("Hello", String.valueOf(barcodes.size()));
                    for (int index = 0; index < barcodes.size(); index++) {
                        TextBlock code = barcodes.valueAt(index);
                        textScanned.setText("" + code.getValue() + "\n");
                        //Required only if you need to extract the type of barcode
                        //   int type = barcodes.valueAt(index).valueFormat;
                    }
                    if (barcodes.size() == 0) {
                        textScanned.setText("Scan Failed: Found nothing to scan");
                    }
                    Log.d("end of function", "masuk");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
                imgNoPreview.setVisibility(View.VISIBLE);
                imgPreview.setVisibility(View.GONE);
            }
        }
    }

    private void previewCapturedImage() {
        try {
            imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            // bitmap terambil
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(fileUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
}
