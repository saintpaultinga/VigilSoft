package com.pindelia.android.vigilsoft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.JsonElement;
import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.net.Utils;
import com.pindelia.android.vigilsoft.net.tools.ApiResponse;
import com.pindelia.android.vigilsoft.util.CustomImageListener;
import com.pindelia.android.vigilsoft.util.ViewModelFactory;
import com.pindelia.android.vigilsoft.viewmodel.VisitorViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

import static com.pindelia.android.vigilsoft.net.Utils.isConnectedToInternet;

public class FormActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    private static final String TAG = "FormActivity";
    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.edmobile) EditText ed_phoneNumber;
    @BindView(R.id.ednom) EditText ed_firstName;
    @BindView(R.id.prnom) EditText ed_lastName;
    @BindView(R.id.birthday) EditText ed_birthDate;
    @BindView(R.id.edexp) EditText ed_expiryDate;
    @BindView(R.id.edcivilite) EditText ed_gender;
    @BindView(R.id.ednumpiece) EditText ed_idNumber;
    @BindView(R.id.edPerson_visited) EditText ed_personVisited;
    @BindView(R.id.edObject) EditText ed_visitObject;
    @BindView(R.id.image) ImageView imageview;
    @BindView(R.id.fab) FloatingActionButton flaction;
    Button bsave;
    Button bclear;
    VisitorViewModel viewModel;
    ProgressDialog progressDialog;
    SignaturePad signaturePad;
    int sign_flag = 0;

    static String signatureString = "";

    private String cardNature;
    Bitmap capturesign;

    static String pieceString = "";
    static String piecepile = "";
    
    private String mPhoneNumber;
    private String mFirstName;
    private String mLastName;
    private String mBirthDay;
    private String mExpiryDate;
    private String mGender;
    private String mIdNumber;
    private String mPersonVisited;
    private String mVisitedObject;
    private Visitor visitor;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        progressDialog = Utils.getProgressDialog(this, "Envoie encours...");

        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VisitorViewModel.class);
        viewModel.createVisitorResponse().observe(this, this::consumeResponse);

        Bundle bundle = getIntent().getExtras();
        visitor = bundle != null ? bundle.getParcelable("visitor") : null;
        if(visitor != null) {
            injectVisitorInView(visitor);
            cardNature = visitor.getIdCode();
            if (cardNature.equalsIgnoreCase("P")) {
                findViewById(R.id.prevs).setVisibility(View.GONE);
                findViewById(R.id.prevp).setVisibility(View.GONE);
                findViewById(R.id.prevpass).setVisibility(View.VISIBLE);
                Bitmap pi = ScanCardActivity.decodeSampledBitmapFromFile(CustomImageListener.fileName, 400, 600);
                imageview.setImageBitmap(pi);
            } else {
                Bitmap pi = ScanCardActivity.decodeSampledBitmapFromFile(ScanCardActivity.mCameraFile, 400, 600);
                imageview.setImageBitmap(pi);
            }
        }

        flaction.setOnClickListener(v -> {
            final Dialog signature_dialog = new Dialog(FormActivity.this);
            signature_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            signature_dialog.setContentView(R.layout.layout_signature);
            signaturePad = signature_dialog.findViewById(R.id.signature_pad);
            signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {

                }

                @Override
                public void onSigned() {
                    bsave.setEnabled(true);
                    bclear.setEnabled(true);
                    Button signprev = findViewById(R.id.prevsign);
                    signprev.setVisibility(View.VISIBLE);
                }

                @Override
                public void onClear() {
                    bsave.setEnabled(false);
                    bclear.setEnabled(false);
                    capturesign = null;
                }
            });

            bsave = signature_dialog.findViewById(R.id.bsave);
            bclear = signature_dialog.findViewById(R.id.bclear);
            bsave.setOnClickListener(v1 -> {
                SaveSignature();
                signature_dialog.dismiss();
            });


            bclear.setOnClickListener(v12 -> signaturePad.clear());

            signature_dialog.show();
        });
    }

    public void SaveSignature() {
        capturesign = signaturePad.getTransparentSignatureBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        capturesign.compress(Bitmap.CompressFormat.PNG, 0, stream);
        signatureString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        setSign_flag(1);
    }

    private void injectVisitorInView(Visitor visitor) {
        ed_phoneNumber.setText(visitor.getPhoneNumber());
        ed_firstName.setText(visitor.getFirstName());
        ed_lastName.setText(visitor.getLastName());
        ed_birthDate.setText(visitor.getBirthDate());
        ed_expiryDate.setText(visitor.getIdExpiryDate());
        ed_gender.setText(visitor.getGender());
        ed_idNumber.setText(visitor.getIdNumber());
        ed_personVisited.setText(visitor.getEmpId());
        ed_visitObject.setText(visitor.getReason());
    }
    
    private void mapViewToVisitor() {
        if (visitor == null) {
            Log.d(TAG, "mapViewInVisitor: visitor was null!!!");
            return;
        }
        visitor.setPhoneNumber(ed_phoneNumber.getText().toString());
        visitor.setFirstName(ed_firstName.getText().toString());
        visitor.setLastName(ed_lastName.getText().toString());
        visitor.setBirthDate(ed_birthDate.getText().toString());
        visitor.setIdExpiryDate(ed_expiryDate.getText().toString());
        visitor.setGender(ed_gender.getText().toString());
        visitor.setIdNumber(ed_idNumber.getText().toString());
        visitor.setEmpId(ed_personVisited.getText().toString());
        visitor.setReason(ed_visitObject.getText().toString());
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(@NonNull ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(FormActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (!response.isJsonNull()) {
            Log.d("response=", response.toString());
        } else {
            Toast.makeText(FormActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
        }
    }

    public void InitFields() {
        ed_phoneNumber.setText("");
        ed_firstName.setText("");
        ed_lastName.setText("");
        ed_birthDate.setText("");
        ed_expiryDate.setText("");
        ed_gender.setText("");
        ed_idNumber.setText("");
        ed_personVisited.setText("");
        ed_visitObject.setText("");
        signatureString = "";
        new File(ScanCardActivity.mCameraFile).delete();
        //new File(ScanCardActivity.mCameraFile1).delete();
        new File(CustomImageListener.fileName).delete();

        pieceString = "";
        imageview.setImageBitmap(null);
        imageview.setVisibility(View.GONE);
    }

    /**
     * methode qui affiche une  Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog titre
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);


        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new File(ScanCardActivity.mCameraFile).delete();
        //new File(ScanCardActivity.mCameraFile1).delete();
        new File(CustomImageListener.fileName).delete();

        Intent it = new Intent(this, MenuActivity.class);
        startActivity(it);
        finish();
    }


    public void showSignature(View view) {
        final Dialog signaturedialog = new Dialog(FormActivity.this);
        signaturedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signaturedialog.setContentView(R.layout.layout_photopreview);
        ImageView viewsign = (ImageView) signaturedialog.findViewById(R.id.photoid);
        if (getSign_flag() == 1) {
            viewsign.setImageBitmap(capturesign);
            Button e = (Button) signaturedialog.findViewById(R.id.exit);
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signaturedialog.dismiss();
                }
            });
            signaturedialog.show();
        } else {
            Toast.makeText(this, "Veuillez capter une signature,svp", Toast.LENGTH_LONG).show();
        }
    }

    public void showId(View view) {
        final Dialog photodialog = new Dialog(FormActivity.this);
        photodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photodialog.setContentView(R.layout.layout_photopreview);
        ImageView viewphoto = (ImageView) photodialog.findViewById(R.id.photoid);
        Bitmap pi = ScanCardActivity.decodeSampledBitmapFromFile(CustomImageListener.fileName, 400, 600);
        viewphoto.setImageBitmap(pi);
        Button exit = (Button) photodialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photodialog.dismiss();
            }
        });
        photodialog.show();
    }

    public void SaveVisitor(View view) {

    }

    public void cancelSending(View view) {

        final Dialog boite = new Dialog(this);
        boite.setContentView(R.layout.confirm_dialog);
        Display display = getWindowManager().getDefaultDisplay();

        boite.getWindow().setLayout(display.getWidth(), (display.getHeight() - 50));
        boite.setTitle("Information");
        Button dialogButon = (Button) boite.findViewById(R.id.non);
        TextView tv = (TextView) boite.findViewById(R.id.affiche);
        tv.setText("Voulez-vous vraiment annuler cette action?");
        // if button is clicked, close the custom dialog
        dialogButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boite.dismiss();
            }
        });
        Button dialogButon2 = (Button) boite.findViewById(R.id.oui);
        // if button is clicked, close the custom dialog
        dialogButon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitFields();
                boite.dismiss();
            }
        });
        boite.show();
    }

    public void sendVisitor(View view) {
        final Visitor newabo = new Visitor();
        final Dialog boit = new Dialog(this);
        boit.setContentView(R.layout.confirm_dialog);
        Display disp = getWindowManager().getDefaultDisplay();
        boit.getWindow().setLayout(disp.getWidth(), disp.getHeight() - 50);
        boit.setTitle("Information");
        Button dialogBut = (Button) boit.findViewById(R.id.non);
        TextView tv1 = (TextView) boit.findViewById(R.id.affiche);
        tv1.setText("Voulez-vous vraiment transférer ces données?");
        // if button no is clicked, close the custom dialog
        dialogBut.setOnClickListener(v -> boit.dismiss());
        Button dialogBut2 = (Button) boit.findViewById(R.id.oui);
        // if button yes is clicked, save the data
        dialogBut2.setOnClickListener(v -> {

            mapViewToVisitor();
            if (cardNature.equalsIgnoreCase("P")) {
                Bitmap bitmap = ScanCardActivity.decodeSampledBitmapFromFile(CustomImageListener.fileName, 400, 600);
                pieceString = com.pindelia.android.vigilsoft.util.Utils.ConvertBitmapToString(bitmap);
            } else {
                Bitmap bitmap = ScanCardActivity.decodeSampledBitmapFromFile(ScanCardActivity.mCameraFile, 400, 600);
                Bitmap bitmapi = ScanCardActivity.decodeSampledBitmapFromFile(CustomImageListener.fileName, 400, 600);
                pieceString = com.pindelia.android.vigilsoft.util.Utils.ConvertBitmapToString(bitmap);
                piecepile = com.pindelia.android.vigilsoft.util.Utils.ConvertBitmapToString(bitmapi);
            }

            //---SD Storage---
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/userlog");
            File file = new File(directory, "user.txt");
            FileInputStream fIn = null;
            try {
                fIn = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(fIn);


            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;
            try {
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    //---convert the chars to a String---
                    String readString = String.copyValueOf(inputBuffer, 0,
                            charRead);
                    s += readString;
                    inputBuffer = new char[READ_BLOCK_SIZE];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO set the nature of the id {passport, idcart}
            visitor.setSignature(signatureString.trim());
            visitor.setIdFrontImage(pieceString);
            visitor.setIdBackImage(piecepile);
            // TODO set the right user id here
            visitor.setUser_id("spaul");
            if (isConnectedToInternet(FormActivity.this)) {
                viewModel.executeVisitorCreation(visitor);
                boit.dismiss();
            } else {
                showAlertDialog(FormActivity.this, "Problème de connexion:", "Aucune connexion n'est active,vos données sont stockées en local.", false);
                InitFields();
                boit.dismiss();
            }
        });
    }

    public void showIdFront(View view) {
        final Dialog photodialog = new Dialog(FormActivity.this);
        photodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photodialog.setContentView(R.layout.layout_photopreview);
        ImageView viewphoto = (ImageView) photodialog.findViewById(R.id.photoid);
        Bitmap pi = ScanCardActivity.decodeSampledBitmapFromFile(ScanCardActivity.mCameraFile, 400, 600);
        viewphoto.setImageBitmap(pi);
        Button exit = (Button) photodialog.findViewById(R.id.exit);
        exit.setOnClickListener(view1 -> photodialog.dismiss());
        photodialog.show();
    }

    public void showIdBack(View view) {
        final Dialog photodialog = new Dialog(FormActivity.this);
        photodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photodialog.setContentView(R.layout.layout_photopreview);
        ImageView viewphoto = (ImageView) photodialog.findViewById(R.id.photoid);
        Bitmap pi = ScanCardActivity.decodeSampledBitmapFromFile(CustomImageListener.fileName, 400, 600);
        viewphoto.setImageBitmap(pi);
        Button exit = (Button) photodialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photodialog.dismiss();
            }
        });
        photodialog.show();
    }

    public int getSign_flag() {
        return sign_flag;
    }

    public void setSign_flag(int sign_flag) {
        this.sign_flag = sign_flag;
    }
}
