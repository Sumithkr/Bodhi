package com.bia.bodhinew.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bia.bodhinew.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomePage extends Fragment {


    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    String[] VideoLinks = new String[1000];

    String[] VideoDescription= new String[1000];

    private boolean firstTime = true;

    View RootView;

    ImageView image;

    /*public void  checkPermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        if(firstTime)
                        {
                            Log.e("permission", "granted");
                            //new UploadFeed().execute();
                            firstTime = false;
                        }

//                        Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                        //    new UploadFeed().execute();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                        // check for permanent denial of permission
                        Log.e("permission","denied");
//                      Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                        new HomePage().finish();
                        moveTaskToBack(false);
                        firstTime = true;

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        /*if (response.isPermanentlyDenied()) {
                            // navigate user to app settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView = inflater.inflate(R.layout.activity_home_page, container, false);

        image= RootView.findViewById(R.id.demo_imageview);

        //File file = new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media" + "/" + "WhatsApp Video" + "/" + "VID-20200513-WA0017.mp4");

        String[] DemoLinks = {"https://shwetaaromatics.co.in/ca/video_upload/files/26vmp4", "https://shwetaaromatics.co.in/ca/video_upload/files/22vmp4",
                "https://shwetaaromatics.co.in/ca/video_upload/files/26vmp4", "https://shwetaaromatics.co.in/ca/video_upload/files/22vmp4",};
        //file.toString()};

        for (int i=0; i<4; i++){

            VideoLinks[i]= DemoLinks[i];
            VideoDescription[i]= i + " Thumbnail";

        }

        getImages();

        return RootView;

    }


    private void getImages() {

        int k=0;

        while(VideoDescription[k]!= null){

            mImageUrls.add(VideoLinks[k]);
            mNames.add(VideoDescription[k]);

            k++;

        }

        initRecyclerView();

    }

    private void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HomePageRecyclerAdapter adapter = new HomePageRecyclerAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

}
