package com.africell.africell.util.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BitmapDecoder implements ImageDecoder {

    public enum ResizeType {
        /**
         * No resize
         */
        NONE,
        /**
         * Downsample only, no exact resize
         */
        DOWNSAMPLE,
        /**
         * Exact resize, will scale up if smaller
         */
        EXACT,
        /**
         * Max size, will scale down but not up
         */
        MAX
    }

    private Context context;
    private Callback<WeakReference<Bitmap>> callback;
    private Disposable decodeDisposable;

    private ResizeType resizeType = ResizeType.NONE;
    private int resizeWidth = 0;
    private int resizeHeight = 0;

    public BitmapDecoder(Context context) {
        this.context = context;
    }

    private BitmapDecoder(Builder builder) {
        context = builder.context;
        resizeType = builder.resizeType;
        resizeWidth = builder.resizeWidth;
        resizeHeight = builder.resizeHeight;
        callback = builder.callback;
    }


    private Observable<WeakReference<Bitmap>> bitmapFromUri(Uri uri) {
        InputStream imageStream;
        Bitmap bitmap;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            imageStream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(imageStream, null, options);
            if (imageStream != null) {
                imageStream.close();
            }
        } catch (IOException ex) {
            return Observable.error(ex);
        }

        if (resizeType == ResizeType.NONE ||
                (resizeWidth == 0 && resizeHeight == 0)) {
            options.inSampleSize = 1;
        } else {
            options.inSampleSize = calculateInSampleSize(options, resizeWidth, resizeHeight);
        }

        options.inJustDecodeBounds = false;

        try {
            imageStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(imageStream);
            if (imageStream != null) {
                imageStream.close();
            }
        } catch (Exception e) {
            return Observable.error(e);
        }

        if (resizeType == ResizeType.NONE
                || resizeType == ResizeType.DOWNSAMPLE
                || (resizeWidth == 0 && resizeHeight == 0)) {
            // no further resizing is necessary
            return Observable.just(new WeakReference<>(bitmap));
        } else if (resizeType == ResizeType.EXACT) {
            bitmap = resizeExact(bitmap, resizeWidth, resizeHeight);
        } else if (resizeType == ResizeType.MAX) {
            bitmap = resizeMax(bitmap, resizeWidth, resizeHeight);
        }

        // We don't want the subscription to retain the bitmap instance
        // so we return a WeakReference to the bitmap
        // once the bitmap is no longer used, it can be garbage collected
        return Observable.just(new WeakReference<>(bitmap));
    }

    @Override
    public void decode(final Uri imageUri) {
        decodeDisposable = Observable.defer((Callable<ObservableSource<WeakReference<Bitmap>>>) () -> bitmapFromUri(imageUri))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    if (callback != null) {
                        callback.onFinished(bitmap);
                    }
                }, t -> {
                    if (callback != null) {
                        callback.onError(t);
                    }
                });

        if (callback != null) {
            callback.onStarted();
        }
    }

    public Disposable getDecodeDisposable() {
        return decodeDisposable;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap resizeExact(Bitmap bitmap, int width, int height) {
        if (width == 0 && height == 0) {
            throw new IllegalArgumentException("width and height cannot both be 0");
        }

        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            // no need to scale
            return bitmap;
        }

        if (height == 0) {
            // resize to exact width
            // calculate height to preserve aspect ratio
            height = width * bitmap.getHeight() / bitmap.getWidth();
        } else if (width == 0) {
            // resize to exact height
            // calculate width to preserve aspect ratio
            width = height * bitmap.getWidth() / bitmap.getHeight();
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap resizeMax(Bitmap bitmap, int width, int height) {
        if (width == 0 && height == 0) {
            throw new IllegalArgumentException("width and height cannot both be 0");
        }

        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            // no need to scale
            return bitmap;
        }

        width = Math.min(width, bitmap.getWidth());
        height = Math.min(height, bitmap.getHeight());

        if (width != 0 && height != 0) {
            // we have constraints for both height and width
            // we need to preserve aspect ratio
            // we resize to the largest downsize ratio
            if ((float) bitmap.getWidth() / width < (float) bitmap.getHeight() / height) {
                width = height * bitmap.getWidth() / bitmap.getHeight();
            } else {
                height = width * bitmap.getHeight() / bitmap.getWidth();
            }
        } else if (height == 0) {
            // resize to max width
            // calculate height to preserve aspect ratio
            height = width * bitmap.getHeight() / bitmap.getWidth();
        } else {
            // resize to max height
            // calculate width to preserve aspect ratio
            width = height * bitmap.getWidth() / bitmap.getHeight();
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    public static final class Builder {
        private Context context;
        private Callback<WeakReference<Bitmap>> callback;
        private ResizeType resizeType = ResizeType.NONE;
        private int resizeWidth = 0;
        private int resizeHeight = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder callback(Callback<WeakReference<Bitmap>> val) {
            callback = val;
            return this;
        }

        public Builder resize(ResizeType type, int width, int height) {
            if (width < 0 || height < 0) {
                throw new IllegalArgumentException("width and height cannot be negative");
            }
            resizeType = type;
            resizeWidth = width;
            resizeHeight = height;
            return this;
        }

        public BitmapDecoder build() {
            return new BitmapDecoder(this);
        }
    }
}
