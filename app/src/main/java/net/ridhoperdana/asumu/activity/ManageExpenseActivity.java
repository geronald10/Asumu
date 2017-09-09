package net.ridhoperdana.asumu.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.helper.FileImageHelper;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.Scanner;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageExpenseActivity extends AppCompatActivity {

    private final String TAG = ManageExpenseActivity.class.getSimpleName();

    private ViewGroup mLinearLayout;
    private SweetAlertDialog pDialog;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    private ImageView imgPreview, imgNoPreview;
    private TextView textScanned;
    private TextRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expense);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        textScanned = (TextView) findViewById(R.id.tvTextScanned);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        imgNoPreview = (ImageView) findViewById(R.id.imgNoPreview);

        mLinearLayout = (ViewGroup) findViewById(R.id.linear_layout_parent);
        final LinearLayout captureImageLayout = (LinearLayout) findViewById(R.id.cameraMenuLayout);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Manage expense");
        ImageView ivLogo = (ImageView) findViewById(R.id.iv_logo_toolbar);
        ivLogo.setVisibility(View.GONE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonAddMoreExpense = (Button) findViewById(R.id.add_more_expense);
        Button buttonUpdateExpense = (Button) findViewById(R.id.update_daily_expense);
        Button buttonCapture = (Button) findViewById(R.id.btnShowImageLayout);
        Button buttonCapturePicture = (Button) findViewById(R.id.btnCapturePicture);

        buttonAddMoreExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreItems();
            }
        });
        buttonUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllText(mLinearLayout);
            }
        });

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captureImageLayout.getVisibility() == View.VISIBLE)
                    captureImageLayout.setVisibility(View.GONE);
                else
                    captureImageLayout.setVisibility(View.VISIBLE);
            }
        });

        buttonCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        Log.d("Test Recognizer", String.valueOf(recognizer));
        if(recognizer.isOperational()) {
            Log.d("recognizer", "can't set up the detector!");
        }
    }

    private void addMoreItems() {
        Log.d("more Items", "added");
        View layoutItem = LayoutInflater.from(this).inflate(R.layout.input_layout, mLinearLayout, false);
        EditText keyExpense = (EditText) findViewById(R.id.input_key_daily);
        EditText valueExpense = (EditText) findViewById(R.id.input_value_daily);
        mLinearLayout.addView(layoutItem);
    }

    private void getAllText(ViewGroup parent) {
        List<EditText> edtKeyExpense = new ArrayList<>();
        List<EditText> edtValueExpense = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            Log.d("childCount", String.valueOf(parent.getChildCount()));
            View child = parent.getChildAt(i);
            if (child instanceof EditText) {
                Log.d("found", "edittext");
                if (i % 2 == 0) {
                    edtKeyExpense.add((EditText) child);
                } else if (i % 2 == 1) {
                    edtValueExpense.add((EditText) child);
                }
            }
            if (child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup) child;
                getAllText(group);
            }
        }

        Bundle bundle = getIntent().getExtras();
        Log.d("bundle", String.valueOf(bundle));
        storeText(bundle.getString("username"), bundle.getString("targetId"),
                bundle.getString("date"), edtKeyExpense, edtValueExpense);
    }

    private void storeText(final String username, final String idTarget, final String date,
                           final List<EditText> key, final List<EditText> value) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.HISTORY_DAILY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showSuccessResult();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showErrorResult();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("id_target", idTarget);
                params.put("date", date);
                for (int i = 0; i < key.size(); i++) {
                    params.put(key.get(i).getText().toString(), value.get(i).getText().toString());
                    Log.d("key", key.get(i).getText().toString());
                    Log.d("value", key.get(i).getText().toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("cek call", "masuk");
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void showLoading() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void showSuccessResult() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!")
                    .setContentText("Expense updated successfully")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void showErrorResult() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Failed!")
                    .setContentText("Internal Server Error")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
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
                    bitmap = scanner.decodeBitmapUri(getApplicationContext(), fileUri);
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
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
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

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(fileUri.getPath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                imgPreview.setImageBitmap(rotatedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        sendBroadcast(mediaScanIntent);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
