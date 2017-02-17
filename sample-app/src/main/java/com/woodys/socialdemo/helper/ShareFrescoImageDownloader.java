

package com.woodys.socialdemo.helper;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.woodys.socialsdk.share.download.AbsImageDownloader;
import com.woodys.socialsdk.share.download.IImageDownloader;
import com.woodys.socialsdk.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author woodys
 *
 * @since 2016/2/7
 */
public class ShareFrescoImageDownloader extends AbsImageDownloader {

    @Override
    protected void downloadDirectly(final String imageUrl, final String filePath, final IImageDownloader.OnImageDownloadListener listener) {
        if (listener != null)
            listener.onStart();

        final ImageRequest request = ImageRequest.fromUri(imageUrl);
        DataSource<CloseableReference<CloseableImage>> dataSource =
                Fresco.getImagePipeline().fetchDecodedImage(request, null);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {

            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                CloseableReference<CloseableImage> result = dataSource.getResult();
                if (result != null) {
                    ImageRequest imageRequest = ImageRequest.fromUri(imageUrl);
                    CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                            .getEncodedCacheKey(imageRequest);
                    BinaryResource resource = Fresco.getImagePipelineFactory()
                            .getMainDiskStorageCache()
                            .getResource(cacheKey);
                    if (resource instanceof FileBinaryResource) {
                        File cacheFile = ((FileBinaryResource) resource).getFile();
                        try {
                            FileUtil.copyFile(cacheFile, new File(filePath));
                            if (listener != null)
                                listener.onSuccess(filePath);
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (listener != null)
                    listener.onFailed(imageUrl);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (listener != null)
                    listener.onFailed(imageUrl);
            }

        }, UiThreadImmediateExecutorService.getInstance());
    }

}
