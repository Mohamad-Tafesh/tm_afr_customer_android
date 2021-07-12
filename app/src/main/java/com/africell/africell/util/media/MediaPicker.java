package com.africell.africell.util.media;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.africell.africell.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MediaPicker {
    private static final String EXTENSION_JPEG = ".jpg";
    private static final String EXTENSION_MP4 = ".mp4";
    private static final String MIME_TYPE_IMAGE = "image/*";
    private static final String MIME_TYPE_VIDEO = "video/*";

    private Activity activity;
    private Fragment fragment;
    private int permissionsRequestCode;
    private int pickerRequestCode;
    private String directoryName;
    private File directory;
    private String fileName;
    private File mediaFile;
    private Uri mediaUri;
    private int type = Type.IMAGE_BOTH;
    private int selectedType = Type.IMAGE_CAPTURE;
    private String[] permissions;
    private String permissionExplanation;


    private Callback callback;
    private ImageDecoder imageDecoder;

    public interface Type {
        int IMAGE_CAPTURE = 0;
        int IMAGE_PICK = 1;
        int IMAGE_BOTH = 2;
        int VIDEO_CAPTURE = 3;
        int VIDEO_PICK = 4;
        int VIDEO_BOTH = 5;
    }

    private MediaPicker(Builder builder) {
        activity = builder.activity;
        fragment = builder.fragment;
        type = builder.type;
        permissionsRequestCode = builder.permissionsRequestCode;
        pickerRequestCode = builder.pickerRequestCode;
        directoryName = builder.directoryName;
        directory = builder.directory;
        fileName = builder.fileName;
        mediaFile = builder.mediaFile;
        mediaUri = builder.mediaUri;
        callback = builder.callback;
        imageDecoder = builder.imageDecoder;
    }


    private void checkPermissions() {
        if (selectedType == Type.IMAGE_CAPTURE || selectedType == Type.VIDEO_CAPTURE) {
            permissions = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE};
            permissionExplanation = activity.getString(R.string.media_picker__camera_permissions_explanation);
        } else if (selectedType == Type.IMAGE_PICK || selectedType == Type.VIDEO_PICK) {
            // to read a file from storage we need READ_EXTERNAL_STORAGE permission
            // for Api level <19 we already have the permission from the Manifest
            // for Api level >=19 we are using Storage Access Framework which gives an implicit read permission
            launchIntent();
            return;
        } else {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Type %d not allowed", selectedType));
        }

        boolean permissionsGranted = true;
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }
        if (permissionsGranted) {
            // we have the needed permissions
            launchIntent();
        } else {
            // we need to request permissions
            // should we show an explanation?
            boolean showExplanation = false;
            for (String p : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, p)) {
                    showExplanation = true;
                    break;
                }
            }
            if (showExplanation) {
                showPermissionsExplanation();
            } else {
                // No explanation needed, we can request the permission.
                if (fragment != null) {
                    fragment.requestPermissions(permissions, permissionsRequestCode);
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, permissionsRequestCode);
                }
            }
        }
    }

    private void showPermissionsExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.media_picker__permissions));
        builder.setMessage(permissionExplanation);
        builder.setNegativeButton(activity.getString(R.string.media_picker__cancel), null);
        builder.setPositiveButton(activity.getString(R.string.media_picker__grant_permissions),
                (dialogInterface, i) -> {
                    if (fragment != null) {
                        fragment.requestPermissions(permissions, permissionsRequestCode);
                    } else {
                        ActivityCompat.requestPermissions(activity, permissions, permissionsRequestCode);
                    }
                });
        builder.create().show();
    }

    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == permissionsRequestCode) {
            if (grantResults.length > 0) {
                for (int res : grantResults) {
                    if (res == PackageManager.PERMISSION_DENIED) {
                        return;
                    }
                }
                // we have the needed permissions
                launchIntent();
            }
        }
    }

    private boolean prepareFile(String extension) {
        if (directoryName == null) {
            directoryName = "picker";
        }
        if (directory == null) {
            directory = new File(activity.getExternalFilesDir(null), directoryName);
        }
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }

        if (fileName == null) {
            fileName = System.currentTimeMillis() + extension;
        }
        if (mediaFile == null) {
            mediaFile = new File(directory, fileName);
        }
        if (!mediaFile.exists()) {
            try {
                return mediaFile.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * &lt;uses-permission
     * android:name="android.permission.READ_EXTERNAL_STORAGE"
     * android:maxSdkVersion="18" //>
     */
    private void launchIntent() {
        if (selectedType == Type.IMAGE_CAPTURE) {
            if (prepareFile(EXTENSION_JPEG)) {
                launchCaptureIntent(MediaStore.ACTION_IMAGE_CAPTURE);
            } else {
                Snackbar.make(activity.findViewById(android.R.id.content), R.string.media_picker__could_not_prepare_file, Snackbar.LENGTH_LONG).show();
            }
        } else if (selectedType == Type.IMAGE_PICK) {
            launchPickIntent(MIME_TYPE_IMAGE);
        } else if (selectedType == Type.VIDEO_CAPTURE) {
            if (prepareFile(EXTENSION_MP4)) {
                launchCaptureIntent(MediaStore.ACTION_VIDEO_CAPTURE);
            } else {
                Snackbar.make(activity.findViewById(android.R.id.content), R.string.media_picker__could_not_prepare_file, Snackbar.LENGTH_LONG).show();
            }
        } else if (selectedType == Type.VIDEO_PICK) {
            launchPickIntent(MIME_TYPE_VIDEO);
        }
    }

    private void launchCaptureIntent(String action) {
        final Intent captureIntent = new Intent(action);
        mediaUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", mediaFile);
        captureIntent.putExtra("output", mediaUri);
        captureIntent.putExtra("return-data", true);

        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, mediaUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(captureIntent, pickerRequestCode);
    }

    private void launchPickIntent(String mimeType) {
        final Intent pickIntent;

        boolean useStorageAccessFramework = true;//TODO change to custom property
        if (useStorageAccessFramework) {
            // use Storage Access Framework
            pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            pickIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        pickIntent.setType(mimeType);
        startActivityForResult(pickIntent, pickerRequestCode);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == pickerRequestCode && resultCode == Activity.RESULT_OK) {

            switch (selectedType) {
                case Type.IMAGE_PICK:
                    mediaUri = data.getData();
                    callback.onImagePicked(mediaUri);
                    if (imageDecoder != null) {
                        imageDecoder.decode(mediaUri);
                    }
                    break;

                case Type.IMAGE_CAPTURE:
                    callback.onImagePicked(mediaUri);
                    if (imageDecoder != null) {
                        imageDecoder.decode(mediaUri);
                    }
                    break;

                case Type.VIDEO_PICK:
                    mediaUri = data.getData();
                    callback.onVideoPicked(mediaUri);
                    break;

                case Type.VIDEO_CAPTURE:
                    callback.onVideoPicked(mediaUri);
                    break;
            }
        }
    }

    public void launch() {
        switch (type) {
            case Type.IMAGE_CAPTURE:
            case Type.IMAGE_PICK:
            case Type.VIDEO_PICK:
            case Type.VIDEO_CAPTURE:
                selectedType = type;
                checkPermissions();
                break;
            case Type.IMAGE_BOTH:
                // show alert dialog
                String[] imageTypes = {activity.getString(R.string.media_picker__take_photo),
                        activity.getString(R.string.media_picker__pick_photo)};
                ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, imageTypes);
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.media_picker__choose_dialog_title)
                        .setAdapter(imageAdapter, (dialogInterface, i) -> {
                            switch (i) {
                                case 0: // camera
                                    selectedType = Type.IMAGE_CAPTURE;
                                    break;
                                case 1: // gallery
                                    selectedType = Type.IMAGE_PICK;
                                    break;
                            }
                            checkPermissions();
                        }).show();
                break;

            case Type.VIDEO_BOTH:
                // show alert dialog
                String[] videoTypes = {activity.getString(R.string.media_picker__take_video),
                        activity.getString(R.string.media_picker__pick_video)};
                ArrayAdapter<String> videoAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, videoTypes);
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.media_picker__choose_dialog_title)
                        .setAdapter(videoAdapter, (dialogInterface, i) -> {
                            switch (i) {
                                case 0: // camera
                                    selectedType = Type.VIDEO_CAPTURE;
                                    break;
                                case 1: // gallery
                                    selectedType = Type.VIDEO_PICK;
                                    break;
                            }
                            checkPermissions();
                        }).show();
                break;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setImageDecoder(ImageDecoder imageDecoder) {
        this.imageDecoder = imageDecoder;
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public interface Callback {
        void onImagePicked(@NonNull Uri imageUri);

        void onVideoPicked(@NonNull Uri videoUri);
    }


    public static final class Builder {
        private Activity activity;
        private Fragment fragment;
        private int type = Type.IMAGE_BOTH;
        private int permissionsRequestCode = 1337;
        private int pickerRequestCode = 1338;
        private String directoryName;
        private File directory;
        private String fileName;
        private File mediaFile;
        private Uri mediaUri;
        private Callback callback;
        private ImageDecoder imageDecoder;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder(MediaPicker copy) {
            this.activity = copy.activity;
            this.fragment = copy.fragment;
            this.type = copy.type;
            this.permissionsRequestCode = copy.permissionsRequestCode;
            this.pickerRequestCode = copy.pickerRequestCode;
            this.directoryName = copy.directoryName;
            this.directory = copy.directory;
            this.fileName = copy.fileName;
            this.mediaFile = copy.mediaFile;
            this.mediaUri = copy.mediaUri;
            this.callback = copy.callback;
            this.imageDecoder = copy.imageDecoder;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder fragment(Fragment val) {
            fragment = val;
            return this;
        }

        public Builder permissionsRequestCode(int val) {
            permissionsRequestCode = val;
            return this;
        }

        public Builder pickerRequestCode(int val) {
            pickerRequestCode = val;
            return this;
        }

        public Builder directoryName(String val) {
            directoryName = val;
            return this;
        }

        public Builder directory(File val) {
            directory = val;
            return this;
        }

        public Builder fileName(String val) {
            fileName = val;
            return this;
        }

        public Builder mediaFile(File val) {
            mediaFile = val;
            return this;
        }

        public Builder mediaUri(Uri val) {
            mediaUri = val;
            return this;
        }

        public Builder callback(Callback val) {
            callback = val;
            return this;
        }

        public Builder imageDecoder(ImageDecoder val) {
            imageDecoder = val;
            return this;
        }

        public MediaPicker build() {
            return new MediaPicker(this);
        }
    }

    public void saveState(@Nullable Bundle outState) {
        if (outState != null) {
            outState.putParcelable("img_picker_uri", mediaUri);
        }
    }

    public void restoreState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mediaUri = savedInstanceState.getParcelable("img_picker_uri");
        }
    }

    public Uri getMediaUri() {
        return mediaUri;
    }

    public @Nullable
    String getFileName() {
        return fileName;
    }
}

