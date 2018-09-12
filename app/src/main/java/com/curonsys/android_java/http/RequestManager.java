package com.curonsys.android_java.http;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.ContentsListModel;
import com.curonsys.android_java.model.DownloadModel;
import com.curonsys.android_java.model.MarkerListModel;
import com.curonsys.android_java.model.MarkerModel;
import com.curonsys.android_java.model.UserContentsModel;
import com.curonsys.android_java.model.UserModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ijin-yeong on 2018. 7. 23..
 */

public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();

    private static String mBaseUrl = "https://kskim-curonsys.com";

    private static RequestManager mInstance;
    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    private AsyncHttpClient client;
    private FirebaseFirestore mFirestore;
    private ListenerRegistration mListenerRegistration;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    public interface JsonResponseCallback{
        public void onResponse(JSONObject success);
    }

    public interface BoolResponseCallback{
        public void onResponse(Boolean success);
    }

    public interface StringResponseCallback{
        public void onResponse(String success);
    }

    public interface UserCallback {
        public void onResponse(UserModel response);
    }

    public interface ContentsListCallback {
        public void onResponse(ArrayList<ContentModel> response);
    }

    public interface ContentCallback {
        public void onResponse(ContentModel response);
    }

    public interface MarkerListCallback {
        public void onResponse(ArrayList<MarkerModel> response);
    }

    public interface MarkerCallback {
        public void onResponse(MarkerModel response);
    }

    public interface SuccessCallback {
        public void onResponse(boolean success);
    }

    public interface DownloadCallback {
        public void onResponse(DownloadModel response);
    }

    public RequestManager() {
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance("gs://my-first-project-7e28c.appspot.com");

    }

    public RequestManager(String baseurl) {
        mBaseUrl = baseurl;
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void requestGetUserInfo(String userid, final UserCallback callback) {
        DocumentReference docRef = mFirestore.collection("users").document(userid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                UserModel model = new UserModel(documentSnapshot.getData());
                callback.onResponse(model);
            }
        });
    }

    public void requestSetUserInfo(UserModel data, final UserCallback callback) {
        Map<String, Object> user = data.getData();

        mFirestore.collection("users").document(data.getUserId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User data successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing user document", e);
                    }
                });
    }

    public void requestGetContentInfo(String contentid, final ContentCallback callback) {
        DocumentReference docRef = mFirestore.collection("models").document(contentid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                ContentModel model = new ContentModel(documentSnapshot.getData());
                callback.onResponse(model);
            }
        });
    }

    public void requestGetContentsList(ArrayList<String> ids, final ContentsListCallback callback) {
        // 1. query with conditions
        /*
        Query query = mFirestore.collection("models");
        String condition = "";

        query = query.whereArrayContains("content_id", condition);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<ContentModel> list = new ArrayList<ContentModel>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        ContentModel model = new ContentModel(document.getData());
                        list.add(model);
                    }
                    callback.onResponse(list);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        */

        // 2. get all docs, compare each docs
        mFirestore.collection("models")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ContentModel> list = new ArrayList<ContentModel>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (ids.contains(document.getId())) {
                                    ContentModel model = new ContentModel(document.getData());
                                    list.add(model);
                                }
                            }
                            callback.onResponse(list);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void requestGetAllContents() {
        mFirestore.collection("models")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void requestSetContentInfo(ContentModel data, final ContentCallback callback) {
        Map<String, Object> content = data.getData();

        mFirestore.collection("models").document(data.getContentId())
                .set(content)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Content data successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing content document", e);
                    }
                });
    }

    public void requestGetMarkerList(LatLng location, String city, final MarkerListCallback callback) {
        ArrayList<MarkerModel> list = new ArrayList<MarkerModel>();

        mFirestore.collection("markers")
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MarkerModel data = new MarkerModel(document.getData());
                                list.add(data);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            callback.onResponse(list);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void requestGetMarkerInfo(String markerid, final MarkerCallback callback) {
        DocumentReference docRef = mFirestore.collection("markers").document(markerid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                MarkerModel model = new MarkerModel(documentSnapshot.getData());
                callback.onResponse(model);
            }
        });
    }

    public void requestSetMarkerInfo(MarkerModel data, final MarkerCallback callback) {
        Map<String, Object> marker = data.getData();

        mFirestore.collection("markers").document(data.getMarkerId())
                .set(marker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Marker data successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing marker document", e);
                    }
                });
    }

    public void requestRunTransaction(/* ... */ String userid, final SuccessCallback callback) {
        // temp
        final DocumentReference sfDocRef = mFirestore.collection("users").document(userid);

        mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                //double newPopulation = snapshot.getDouble("population") + 1;
                //transaction.update(sfDocRef, "population", newPopulation);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }

    public void requestRunBatch(/* ... */ String userid, final SuccessCallback callback) {
        // temp
        WriteBatch batch = mFirestore.batch();

        DocumentReference nycRef = mFirestore.collection("cities").document("NYC");
        batch.set(nycRef, new MarkerModel());

        DocumentReference sfRef = mFirestore.collection("cities").document("SF");
        batch.update(sfRef, "population", 1000000L);

        DocumentReference laRef = mFirestore.collection("cities").document("LA");
        batch.delete(laRef);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // ...
                callback.onResponse(true);
            }
        });
    }

    public void requestStartCheckUpdate(/* ... */ String userid, final SuccessCallback callback) {
        // temp
        final DocumentReference docRef = mFirestore.collection("userid").document("SF");
        mListenerRegistration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void requestStopCheckUpdate(/* ... */) {
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    public void downloadFileFromStorage(String name, String path, String suffix, final DownloadCallback callback) {
        mStorageRef = mStorage.getReference(path);
        try {
            File localFile = File.createTempFile(name, suffix);
            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DownloadModel data = new DownloadModel(localFile.getAbsolutePath(), suffix, taskSnapshot.getTotalByteCount());
                    //Log.d(TAG, "onSuccess: file download success (" + taskSnapshot.getTotalByteCount() + ", " + localFile.getAbsolutePath() + ")");
                    callback.onResponse(data);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "onFailure: file download failed (" + exception.getMessage() + ")");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
