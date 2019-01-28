package com.pindelia.android.vigilsoft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.microblink.activity.BaseScanActivity;
import com.microblink.activity.ScanActivity;
import com.microblink.activity.ScanCard;
import com.microblink.directApi.DirectApiErrorListener;
import com.microblink.directApi.Recognizer;
import com.microblink.metadata.MetadataSettings;
import com.microblink.recognition.FeatureNotSupportedException;
import com.microblink.recognition.InvalidLicenceKeyException;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognitionResult;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognizerSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.util.ApplicationConfig;
import com.pindelia.android.vigilsoft.util.CustomImageListener;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ScanCardActivity extends Activity {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    /**
     * Request code for built-in camera activity.
     */
    public static final int CAMERA_REQUEST_CODE = 0x101;
    /**
     * File that will hold the image taken from camera.
     */
    public static String mCameraFile = Environment.getExternalStorageDirectory().getPath() + "/my-photo.jpg";
    public static String mCameraFile1 = Environment.getExternalStorageDirectory().getPath() + "/my-photo1.jpg";
    /**
     * Tag for logcat.
     */
    public static final String TAG = "BlinkIDDemo";



    /**
     * Image view which shows current image that will be scanned.
     */
    private ImageView mImgView;


    /**
     * Recognizer instance
     */
    private Recognizer mRecognizer = null;
    /**
     * Recognition settings instance.
     */
    private RecognitionSettings mSettings;
    private String mLicenseKey;

    //String representation of bitmap
    static String pieceface = "";
    static String piecepile = "";
    /**
     * Flags to identify which camera is on
     */
    private boolean cam_front = false;
    private boolean cam_back = false;
    private String msisdn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_id_card);


        mImgView = (ImageView) findViewById(R.id.imgfrontside);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mSettings = extras.getParcelable(BaseScanActivity.EXTRAS_RECOGNITION_SETTINGS);
            mLicenseKey = extras.getString(BaseScanActivity.EXTRAS_LICENSE_KEY);
            msisdn = extras.getString("MSISDN");
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        // get the recognizer instance
        try {
            mRecognizer = Recognizer.getSingletonInstance();
        } catch (FeatureNotSupportedException e) {
            Toast.makeText(this, "Feature not supported! Reason: " + e.getReason().getDescription(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // In order for scanning to work, you must enter a valid licence key. Without licence key,
        // scanning will not work. Licence key is bound the the package name of your app, so when
        // obtaining your licence key from Microblink make sure you give us the correct package name
        // of your app. You can obtain your licence key at http://microblink.com/login or contact us
        // at http://help.microblink.com.
        // Licence key also defines which recognizers are enabled and which are not. Since the licence
        // key validation is performed on image processing thread in native code, all enabled recognizers
        // that are disallowed by licence key will be turned off without any error and information
        // about turning them off will be logged to ADB logcat.
        try {
            mRecognizer.setLicenseKey(this, mLicenseKey);
        } catch (InvalidLicenceKeyException exc) {
            Toast.makeText(this, "License key check failed! Reason: " + exc.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // initialize recognizer singleton
        mRecognizer.initialize(this, mSettings, new DirectApiErrorListener() {
            @Override
            public void onRecognizerError(Throwable t) {
                Log.e(TAG, "Failed to initialize recognizer.", t);
                Toast.makeText(ScanCardActivity.this, "Failed to initialize recognizer. Reason: "
                        + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * This method will build scan intent for BlinkID. Method needs array of recognizer settings
     * to know which recognizers to enable, activity to which intent will be sent and optionally
     * an intent for HelpActivity that will be used if user taps the Help button on scan activity.
     */
    private Intent buildIntent(RecognizerSettings[] settArray, Class<?> target, Intent helpIntent) {
        // first create intent for given activity
        final Intent intent = new Intent(this, target);

        // optionally, if you want the beep sound to be played after a scan
        // add a sound resource id as EXTRAS_BEEP_RESOURCE extra
        intent.putExtra(ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        // if we have help intent, we can pass it to scan activity so it can invoke
        // it if user taps the help button. If we do not set the help intent,
        // scan activity will hide the help button.
        if (helpIntent != null) {
            intent.putExtra(ScanActivity.EXTRAS_HELP_INTENT, helpIntent);
        }

        // prepare the recognition settings
        RecognitionSettings settings = new RecognitionSettings();

        // with setNumMsBeforeTimeout you can define number of miliseconds that must pass
        // after first partial scan result has arrived before scan activity triggers a timeout.
        // Timeout is good for preventing infinitely long scanning experience when user attempts
        // to scan damaged or unsupported slip. After timeout, scan activity will return only
        // data that was read successfully. This might be incomplete data.
        settings.setNumMsBeforeTimeout(2000);

        // If you add more recognizers to recognizer settings array, you can choose whether you
        // want to have the ability to obtain multiple scan results from same video frame. For example,
        // if both payment slip and payment barcode are visible on a single frame, by setting
        // setAllowMultipleScanResultsOnSingleImage to true you can obtain both scan results
        // from barcode and slip. If this is false (default), you will get the first valid result
        // (i.e. first result that contains all required data). Having this option turned off
        // creates better and faster user experience.
//        settings.setAllowMultipleScanResultsOnSingleImage(true);

        // now add array with recognizer settings so that scan activity will know
        // what do you want to scan. Setting recognizer settings array is mandatory.
        settings.setRecognizerSettingsArray(settArray);
        intent.putExtra(ScanActivity.EXTRAS_RECOGNITION_SETTINGS, settings);

        // In order for scanning to work, you must enter a valid licence key. Without licence key,
        // scanning will not work. Licence key is bound the the package name of your app, so when
        // obtaining your licence key from Microblink make sure you give us the correct package name
        // of your app. You can obtain your licence key at http://microblink.com/login or contact us
        // at http://help.microblink.com.
        // Licence key also defines which recognizers are enabled and which are not. Since the licence
        // key validation is performed on image processing thread in native code, all enabled recognizers
        // that are disallowed by licence key will be turned off without any error and information
        // about turning them off will be logged to ADB logcat.
        intent.putExtra(ScanActivity.EXTRAS_LICENSE_KEY, ApplicationConfig.LICENSE_KEY);

        // If you want, you can disable drawing of OCR results on scan activity. Drawing OCR results can be visually
        // appealing and might entertain the user while waiting for scan to complete, but might introduce a small
        // performance penalty.
        // intent.putExtra(ScanActivity.EXTRAS_SHOW_OCR_RESULT, false);

        /// If you want you can have scan activity display the focus rectangle whenever camera
        // attempts to focus, similarly to various camera app's touch to focus effect.
        // By default this is off, and you can turn this on by setting EXTRAS_SHOW_FOCUS_RECTANGLE
        // extra to true.
        intent.putExtra(ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE, true);

        // If you want, you can enable the pinch to zoom feature of scan activity.
        // By enabling this you allow the user to use the pinch gesture to zoom the camera.
        // By default this is off and can be enabled by setting EXTRAS_ALLOW_PINCH_TO_ZOOM extra to true.
        intent.putExtra(ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM, true);
        intent.putExtra(ScanActivity.EXTRAS_SPLASH_SCREEN_LAYOUT_RESOURCE,R.layout.camera_splashscreen);

        MetadataSettings.ImageMetadataSettings ims = new MetadataSettings.ImageMetadataSettings();
        // sets whether image that was used to obtain valid scanning result will be available
        ims.setSuccessfulScanFrameEnabled(true);
        intent.putExtra(ScanActivity.EXTRAS_IMAGE_METADATA_SETTINGS,ims);
        intent.putExtra(ScanActivity.EXTRAS_IMAGE_LISTENER,new CustomImageListener());


        return intent;
    }


    /**
     * Starts built-in camera intent for taking scan images.
     */
    private void startCamera() {
        if (isCam_front()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFile)));
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } else if (isCam_back()) {
            // prepare settings for Machine Readable Travel Document (MRTD) recognizer
            MRTDRecognizerSettings mrtd = new MRTDRecognizerSettings();

            Intent takePictureIntent = buildIntent(new RecognizerSettings[]{mrtd}, ScanCard.class,null);
           // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFile1)));
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void startNewActivity(Visitor visitor) {
        Intent result = new Intent(this, FormActivity.class);
        result.putExtra("visitor", visitor);
        startActivity(result);
    }

    /**
     * Handler for button "Take Photo"
     */
    public void takePhotoHandler(View view) {
        setCam_front(true);
        setCam_back(false);
        startCamera();
    }

    public void takePhotoHandler1(View view) {
        setCam_back(true);
        setCam_front(false);
        startCamera();
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (mRecognizer != null) {
            // terminate the native library
            mRecognizer.terminate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // obtain image that was saved to external storage by camera activity
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = BITMAP_CONFIG;
                    if (isCam_front()) {
                        //
                        //Current bitmap for recognition.
                        //
                        Bitmap mBitmap;
                        BitmapFactory.decodeFile(mCameraFile, options);
                        mBitmap = decodeSampledBitmapFromFile(mCameraFile, 400, 600);
                        // new File(mCameraFile).delete();
                        //captureBmp = (Bitmap) data.getExtras().get("data");
                        //pieceface = Tools.ConvertBitmapToString(mBitmap);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                        // show camera image
                        mImgView.setImageBitmap(mBitmap);
                    } else if (isCam_back()) {
                       /* mBitmapb = BitmapFactory.decodeFile(mCameraFile1, options);
                        mBitmapb = decodeSampledBitmapFromFile(mCameraFile1, 400, 600);
                        //piecepile = Tools.ConvertBitmapToString(mBitmapb);
                        //new File(mCameraFile1).delete();
                        //captureBmp = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        mBitmapb.compress(Bitmap.CompressFormat.PNG, 40, out);
                        // show camera image
                        mImgView1.setImageBitmap(mBitmapb);*/

                        RecognitionResults mResults = data.getExtras().getParcelable(
                                ScanActivity.EXTRAS_RECOGNITION_RESULTS);
                        getAbonnes(mResults);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast toast = Toast.makeText(ScanCardActivity.this, R.string.action_cancel, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.bstyle);
                toast.show();
                Intent menuActivity = new Intent(this, MenuActivity.class);
                startActivity(menuActivity);
            }
        }
    }

    public boolean isCam_front() {
        return cam_front;
    }

    public void setCam_front(boolean cam_front) {
        this.cam_front = cam_front;
    }

    public boolean isCam_back() {
        return cam_back;
    }

    public void setCam_back(boolean cam_back) {
        this.cam_back = cam_back;
    }

    public void getAbonnes(RecognitionResults results) {
        //déclaration de l'abonné
        Visitor visitor = new Visitor();
        // Get scan results array. If scan was successful, array will contain at least one element.
        // Multiple element may be in array if multiple scan results from single image were allowed in settings.
        BaseRecognitionResult[] resultArray = results.getRecognitionResults();
        if (resultArray != null && resultArray.length > 0) {
            if (resultArray[0] instanceof MRTDRecognitionResult) {
                MRTDRecognitionResult result = (MRTDRecognitionResult) resultArray[0];
                visitor.setFirstName(result.getPrimaryId());
                visitor.setLastName(result.getSecondaryId());
                String year = result.getDateOfBirth().toString().substring(0, 2);
                String month = result.getDateOfBirth().toString().substring(2, 4);
                String day = result.getDateOfBirth().toString().substring(4, 6);
                visitor.setBirthDate(day + "-" + month + "-" + "19" + year);
                year = result.getDateOfExpiry().toString().substring(0, 2);
                month = result.getDateOfExpiry().toString().substring(2, 4);
                day = result.getDateOfExpiry().toString().substring(4, 6);
                visitor.setIdExpiryDate(day + "-" + month + "-" + "20" + year);
                visitor.setGender(result.getSex());
                visitor.setNationality(result.getNationality());
                visitor.setIdNumber(result.getDocumentNumber());
                visitor.setIdCode(result.getDocumentCode().substring(0, 1));
                //ab.setPieceface(pieceface);
                //ab.setPiecepile("nan");
                startNewActivity(visitor);
                visitor.setIdFrontImage(pieceface);
                visitor.setIdBackImage(piecepile);
                startNewActivity(visitor);
            }
        } else {
            Toast toast = Toast.makeText(ScanCardActivity.this, R.string.extract_error, Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackgroundResource(R.drawable.toast_bg_style);
            toast.show();
        }

    }


}
