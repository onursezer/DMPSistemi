package com.bitirme2.onursezer.dmpsistemi;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by OnurSezer on 26.12.2017.
 */

public class StHWtab2 extends Fragment {

    Context fCon;
    FirebaseDatabase db;
    Homework homeworkObj;
    User userObj;

    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView2;
    private EditText txtImageName2;
    private Uri imgUri;

    public  String FB_STORAGE_PATH = "";
    public  String FB_DATABASE_PATH =  "";
    public static final int REQUEST_CODE = 1234;

    public static StHWtab2 newInstance(String homework, String user) {
        StHWtab2 result = new StHWtab2();
        Bundle bundle = new Bundle();
        bundle.putString("hw", homework);
        bundle.putString("user", user);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        String hwGson = bundle.getString("hw");
        String userGson = bundle.getString("user");
        Gson gS = new Gson();
        homeworkObj = gS.fromJson(hwGson, Homework.class);
        userObj = gS.fromJson(userGson, User.class);
        String email = userObj.getEmail();
        email = email.replace(".", ""); email = email.replace("#", "");
        email = email.replace("$", ""); email = email.replace("[", "");
        email = email.replace("]", "");

        FB_STORAGE_PATH = email + homeworkObj.getHwId() + "/";
        FB_DATABASE_PATH = email + homeworkObj.getHwId();
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
        txtImageName2 = (EditText) getView().findViewById(R.id.txtImageName2);

        Button btnBrowse_Click2 = (Button) getView().findViewById(R.id.btnBrowse_Click2);
        Button btnUpload_Click2 = (Button) getView().findViewById(R.id.btnUpload_Click2);

        btnBrowse_Click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        btnUpload_Click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri != null) {
                    final AlertDialog dialog = new SpotsDialog(fCon);
                    dialog.show();

                    //Get the storage reference
                    StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

                    //Add file to reference

                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            //Dimiss dialog when success
                            dialog.dismiss();
                            //Display success toast msg
                            Toast.makeText(fCon, "Yüklendi", Toast.LENGTH_SHORT).show();
                            ImageUpload imageUpload = new ImageUpload(txtImageName2.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            //Save image info in to firebase database
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(imageUpload);








                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery2 = ref2.child("Map4").orderByChild("hwID").equalTo(homeworkObj.getHwId());
                            applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot tasksSnapshot) {
                                    for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                                        List<HomeworkInfo> l = snapshot.getValue(MapHomeworkAndStudent.class).getList();
                                        if(l == null)
                                        {
                                            l = new ArrayList<HomeworkInfo>();
                                        }
                                        l.add(new HomeworkInfo("", (new StudentInfo(userObj.getName(), userObj.getSurname(), userObj.getEmail())),
                                                FB_DATABASE_PATH ));
                                        snapshot.getRef().child("list").setValue(l);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            ref2.onDisconnect();




                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    //Dimiss dialog when error
                                    dialog.dismiss();
                                    //Display err toast msg
                                    Toast.makeText(fCon, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    //Show upload progress

                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    dialog.setMessage("Yükleniyor... " + (int) progress + "%");
                                }
                            });
                } else {
                    Toast.makeText(fCon, "Önce seçim yapın", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st_hw_tab2, container, false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(fCon.getContentResolver(), imgUri);
                imageView2.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = fCon.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
