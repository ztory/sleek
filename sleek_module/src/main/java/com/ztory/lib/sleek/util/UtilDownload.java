package com.ztory.lib.sleek.util;

import android.os.Build;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jonruna on 2017-04-19.
 */
public class UtilDownload {

    //TODO BUILD UNIT TEST TO VERIFY THAT FUNCTIONALITY IN THIS CLASS IS WORKING AS EXPECTED !!!!

    public interface FileDownload {
        void downloadProgress(float percent);
        void downloadFinished(File file);
    }

    public static final FileDownload EMPTY_DL_LISTENER = new FileDownload() {
        @Override
        public void downloadProgress(float percent) {
            //DO NOTHING
        }
        @Override
        public void downloadFinished(File file) {
            //DO NOTHING
        }
    };

    public static final Executor
            EXECUTOR = createExecutor(UtilDownload.class.getName() + "_EXECUTOR", 8);

    private static final HashMap<String, List<FileDownload>> sActiveDownloads = new HashMap<>(40);

    /**
     * Add a FileDownload instance to have its progress and finished methods called when download
     * of the urlString progresses or finishes.
     * @param urlString url or download
     * @param fileDownload FileDownload implementation that will listen on download
     * @return true if there was already an active download for urlString
     */
    private static boolean addDownloadListener(String urlString, FileDownload fileDownload) {
        synchronized (sActiveDownloads) {
            List<FileDownload> downloadList = sActiveDownloads.get(urlString);
            final boolean downloadAlreadyStarted = downloadList != null;
            if (!downloadAlreadyStarted) {
                downloadList = new ArrayList<>(2);
                sActiveDownloads.put(urlString, downloadList);
            }
            downloadList.add(fileDownload);
            return downloadAlreadyStarted;
        }
    }

    private static void notifyDownloadListeners(String urlString, float percent) {
        synchronized (sActiveDownloads) {
            List<FileDownload> downloadList = sActiveDownloads.get(urlString);
            if (downloadList != null) {
                for (FileDownload iterFileDownload : downloadList) {
                    iterFileDownload.downloadProgress(percent);
                }
            }
        }
    }

    private static void completeDownloadListeners(String urlString, File file) {
        synchronized (sActiveDownloads) {
            List<FileDownload> downloadList = sActiveDownloads.remove(urlString);
            if (downloadList != null) {
                for (FileDownload iterFileDownload : downloadList) {
                    iterFileDownload.downloadFinished(file);
                }
            }
        }
    }

    public static boolean isDownloadingUrl(String urlString) {
        synchronized (sActiveDownloads) {
            return sActiveDownloads.get(urlString) != null;
        }
    }

    public static void waitForDownloadToFinish(String urlString, long maxWaitTimeMs) {
        long startTime = System.currentTimeMillis();
        while (isDownloadingUrl(urlString)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("UtilDownload",
                    "UtilDownload" +
                    " | Waited: " + (System.currentTimeMillis() - startTime)
            );

            // Wait a maximum of maxWaitTimeMs
            if (System.currentTimeMillis() - startTime > maxWaitTimeMs) {
                break;
            }
        }
    }

    public static File downloadUrl(String urlString) {
        return downloadUrl(
                urlString,
                300000,// 5 min
                EMPTY_DL_LISTENER
        );
    }

    public static File downloadUrl(
            String urlString,
            long synchronousWaitTimeMs,
            FileDownload fileDownload
    ) {

        boolean success = false;

        File downloadFile = null;

        InputStream urlInputStream = null;
        File tempDownloadFile = null;
        FileOutputStream tempFileOutputStream = null;
        try {
            downloadFile = new File(
                    UtilPx.getDefaultContext().getCacheDir(),
                    fileNameFromUrl(urlString)
            );

            // Early return if file has already been downloaded
            if (downloadFile.exists() && downloadFile.length() > 0) {
                fileDownload.downloadFinished(downloadFile);
                return downloadFile;
            }

            boolean downloadAlreadyStarted = addDownloadListener(urlString, fileDownload);
            if (downloadAlreadyStarted) {
                waitForDownloadToFinish(urlString, synchronousWaitTimeMs);
                return downloadFile;
            }

            URL url = new URL(urlString);
            URLConnection cn = url.openConnection();
            cn.connect();
            urlInputStream = cn.getInputStream();
            if (urlInputStream == null) {
                throw new RuntimeException("stream is null");
            }

            float percentProgress = 0.0f;
            String contentLengthString = cn.getHeaderField("Content-Length");
            long contentLength = 0;
            try {
                contentLength = Long.parseLong(contentLengthString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Need to download to a temporary file, and when download is complete...
            // ...copy temp file contents to actual download file.
            // This prevents corrupted file data caused by interupted downloads.
            tempDownloadFile = new File(
                    UtilPx.getDefaultContext().getCacheDir(),
                    System.currentTimeMillis() +
                    fileNameFromUrl(urlString)
            );

            tempFileOutputStream = new FileOutputStream(tempDownloadFile);
            int bufSize = 1024;
            long bytesRead = 0;
            byte buf[] = new byte[bufSize];
            do {

                bytesRead += bufSize;

                if (contentLength > 0) {
                    percentProgress = (float) bytesRead / (float) contentLength;
                }

                if (percentProgress > 1.0f) {
                    percentProgress = 1.0f;
                }

                notifyDownloadListeners(urlString, percentProgress);

                int numread = urlInputStream.read(buf);
                if (numread <= 0) {
                    break;
                }

                tempFileOutputStream.write(buf, 0, numread);
            } while (true);

            if (!copyFile(tempDownloadFile, downloadFile)) {
                throw new IOException(
                        "Failed to copy [" + tempDownloadFile.getAbsolutePath() +
                        "] to [" + downloadFile.getAbsolutePath() + "] !"
                );
            }

            success = true;
        } catch (Exception e) {
            e.printStackTrace();

            success = false;
        } finally {

            closeSafe(urlInputStream);
            closeSafe(tempFileOutputStream);

            if (tempDownloadFile != null) {
                try {
                    tempDownloadFile.delete();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        if (success) {
            completeDownloadListeners(urlString, downloadFile);
            return downloadFile;
        }
        else {
            completeDownloadListeners(urlString, null);
            return null;
        }
    }

    public static String fileNameFromUrl(String urlString) throws UnsupportedEncodingException {
        return URLEncoder.encode(urlString, "UTF-8");
    }

    public static boolean copyFile(File source, File destination) {
        InputStream in = null;
        OutputStream out = null;
        boolean success = false;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(destination);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSafe(in);
            closeSafe(out);
        }
        return success;
    }

    public static void closeSafe(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ThreadPoolExecutor createExecutor(
            final String threadNamePrefix,
            int poolSizeMax
    ) {
        ThreadPoolExecutor returnPool = new ThreadPoolExecutor(
                poolSizeMax,
                poolSizeMax,
                4L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);
                    public Thread newThread(Runnable runnable) {
                        return new Thread(
                                runnable,
                                threadNamePrefix + " #" + mCount.getAndIncrement()
                        );
                    }
                }
        );
        if (Build.VERSION.SDK_INT >= 9) {
            returnPool.allowCoreThreadTimeOut(true);
        }
        return returnPool;
    }

}
