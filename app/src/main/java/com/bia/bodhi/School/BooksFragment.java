package com.bia.bodhi.School;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.bia.bodhi.R;
import com.bia.bodhi.Student.FetchFromDB;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;

public class BooksFragment extends Fragment implements View.OnClickListener {
    EditText Book_name,Book_description;
    Button pick_book,BooksFragment_upload;
    String[] Class_list = { "1st", "2nd", "3rd", "4th", "5th","6th","7th","8th","9th","10th","11th","12th" };
    private static final int PICK_FROM_GALLERY = 101;
    ArrayList<String> SubjectName = new ArrayList();
    ArrayList<String> SubjectID = new ArrayList();
    Spinner spin_subjects;
    String ID;
    String Cls;
    Uri filePath = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_books, container, false);
        StartServerFile();
        // Class spinner
        Spinner spin = (Spinner)v. findViewById(R.id.BooksFragment_Class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Class_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cls = Class_list[position];
                Log.e("class",Cls);
                Toast.makeText(getActivity(), "Selected : "+Class_list[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Subject spinner
        spin_subjects = (Spinner)v. findViewById(R.id.BooksFragment_Subject);

        Book_name = (EditText)v.findViewById(R.id.Book_name);
        Book_description = (EditText)v.findViewById(R.id.Book_description);
        pick_book = (Button)v.findViewById(R.id.pick_book);
        pick_book.setOnClickListener(this);
        BooksFragment_upload = (Button)v.findViewById(R.id.BooksFragment_upload);
        BooksFragment_upload.setOnClickListener(this);
        return v;
    }

    public void onPreServerFile()
    {
        //
    }

    public void StartServerFile()
    {

        String url = "https://bodhi.shwetaaromatics.co.in/SubjectFetch.php";
        FetchFromDB asyncTask = (FetchFromDB) new FetchFromDB(url,new FetchFromDB.AsyncResponse()
        {
            @Override
            public void processFinish(String output) //onPOstFinish
            {
                //this function executes after
                Toast.makeText(getActivity(),"END",Toast.LENGTH_SHORT).show();
                try
                {
                    ConvertFromJSON(output);
                    EndServerFile();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
    public void EndServerFile()
    {
        ArrayAdapter<String> subject_adaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SubjectName);
        subject_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_subjects.setAdapter(subject_adaptor);
        spin_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ID =  SubjectID.get(position);
                Log.e("idaayi",ID);
                // Toast.makeText(getActivity(), "Selected : "+subjects_name[position] ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void ConvertFromJSON(String json)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                SubjectName.add(obj.getString("SubjectName"));
                SubjectID.add(obj.getString("SubjectID"));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /* Get uri related content real local file path. */
    private String getUriRealPath(Context ctx, Uri uri)
    {
        String ret = "";

        if( isAboveKitKat() )
        {
            // Android OS above sdk version 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        }else
        {
            // Android OS below sdk version 19
            ret = getImageRealPath(ctx.getContentResolver(), uri, null);
        }

        return ret;
    }

    private String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";

        if(ctx != null && uri != null) {

            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            }else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){

                // Get uri related document id.
                String documentId = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    documentId = DocumentsContract.getDocumentId(uri);
                }

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if(isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if("video".equals(docType))
                        {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if("audio".equals(docType))
                        {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }

                }else if(isDownloadDoc(uriAuthority))
                {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);

                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private boolean isAboveKitKat()
    {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ret = DocumentsContract.isDocumentUri(ctx, uri);
            }
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {


            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    private void UploadFile(Uri path)
    {
        String uploadId = UUID.randomUUID().toString();
        String x = Commons.getPath(path, getActivity());
        /*String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file =  filename;
        }
        Log.e("real path",x);
        Log.e("Name with extension",filename);
        Log.e("Name without extention ",file);*/
        Log.e("filepath",x);
        int type = 0;
        try
        {
            String url = "https://bodhi.shwetaaromatics.co.in/School/UploadMedia.php";
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(String.valueOf(x), "Media")
                    .addParameter("MediaName",Book_name.getText().toString())
                    .addParameter("Description",Book_description.getText().toString())
                    .addParameter("SubjectID",ID)
                    .addParameter("Class",Cls)
                    //.addParameter("Media",file)
                    .addParameter("Type", String.valueOf(type))
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                            Log.e("ERROR",exception+"-----");
                            //Failed
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Book_description.getText().clear();
                            Book_name.getText().clear();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    }).startUpload();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            Log.e("heljl", String.valueOf(filePath));

        }
    }

    public void openFolder()
    {
        Intent chooseFile = new Intent(Intent. ACTION_GET_CONTENT );
        chooseFile.setType("application/epub+zip");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult( chooseFile,PICK_FROM_GALLERY);
    }

    @Override
    public void onClick(View v) {
        if(v == BooksFragment_upload)
        {
            if(Book_name.getText().toString()!=null && !Book_name.getText().toString().trim().equals("")
                    && Book_description.getText().toString()!=null && !Book_description.getText().toString().trim().equals(""))
            {
                UploadFile(filePath);
            }
            else
            {
                Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == pick_book)
        {
            openFolder();
        }
    }

}
