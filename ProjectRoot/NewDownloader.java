/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// This class downloads a file from a URL.
public class NewDownloader extends Observable implements Runnable, Serializable {

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    // These are the status names.
    public static final String STATUSES[] = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};

    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private URL url; // download URL
    private String path; // this is the (location+file name) part
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download

    public boolean HAS_A_STAGE;

    // Constructor for Download.
    public NewDownloader(String sUrl, String sPath) throws MalformedURLException {
        this.url = new URL(sUrl);
        this.path = sPath;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        HAS_A_STAGE = false;

        // Begin the download.
        download();
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    //shudhu file name - baire theke paoar jnno
    public String getFName() {
        return getFileName(url);
    }
    
    //file er full location paoar jnno
    public String getPath() {
        return path;
    }

    // Get this download's size.
    public int getSize() {
        return size;
    }

    //Get size in gb, mb, kb
    public String getSizeInString() {
        int x = this.getSize();
        float xf = (float) (x * 1.0);
        int idx = 0;
        //System.out.println(size + " " + xf + " " + idx);
        while ((xf / 1000) >= 1.000 && idx < 3) {
            xf = xf / 1000;
            idx++;
            // System.out.println(xf + " " + idx);
        }
        String[] iV = {"bytes", "KB", "MB", "GB"};
        String sz = Float.toString(xf);
        //System.out.println(sz);
        if (sz.length() >= (sz.indexOf('.') + 4)) {
            //doshomik er por value ase, tahole 3 ghor prjnto dekhabo
            sz = sz.substring(0, sz.indexOf('.') + 4) + " " + iV[idx];

        } else {
            //doshomik er por value nai, purata dekhabo
            sz = sz + " " + iV[idx];
        }

        //size na paile unknown return korte hbe
        if (x < 0) {
            return "Unknown";
        }

        return sz;
    }

    //get downloaded value
    public int getDownloadedVal() {
        return downloaded;
    }

    //Get downloaded value in gb, mb, kb
    public String getDownloadedValInString() {
        int x = this.getDownloadedVal();
        float xf = (float) (x * 1.0);
        int idx = 0;
        //System.out.println(size + " " + xf + " " + idx);
        while ((xf / 1000) >= 1.000 && idx < 3) {
            xf = xf / 1000;
            idx++;
            //System.out.println(xf + " " + idx);
        }
        String[] iV = {"bytes", "KB", "MB", "GB"};
        String sz = Float.toString(xf);
        if (sz.length() >= (sz.indexOf('.') + 4)) {
            //doshomik er por value ase, tahole 3 ghor prjnto dekhabo
            sz = sz.substring(0, sz.indexOf('.') + 4) + " " + iV[idx];
        } else {
            //doshomik er por value nai, purata dekhabo
            sz = sz + " " + iV[idx];
        }
        return sz;
    }

    // Get this download's progress. -> 0 to 1
    public float getProgress() {
        //System.out.println(downloaded + "/" + size);
        return ((float) downloaded / size);
    }

    // get this downloads progress percentage as string -> 0 to 100
    public String getProgressPercentage() {
        int prog = (int) (this.getProgress() * 10000);
        float progress = (float) prog / 100;
        String s = Float.toString(progress) + "% finished...";
        return s;
    }

    // Get this download's status.
    public int getStatus() {
        return status;
    }

    // Pause this download.
    public void pause() {
        if (status != COMPLETE) {
            status = PAUSED;
            stateChanged();
        }
    }

    // Resume this download.
    public void resume() {
        if (status != COMPLETE) {
            status = DOWNLOADING;
            stateChanged();
            download();
        }
    }
    
    //for redownloading
    public void redownload() {
        downloaded = 0;
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    // Cancel this download.
    public void cancel() {
        if (status != COMPLETE) {
            status = CANCELLED;
            stateChanged();
        }
    }

    // Mark this download as having an error.
    private void error() {
        status = ERROR;
        stateChanged();
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

            // Connect to server.
            connection.connect();
            System.out.println("YES 1!" + connection.getResponseCode());
            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                System.out.println("YES 2!" + connection.getResponseCode());
                error();
            }

            // Check for valid content length.
            int contentLength = connection.getContentLength();
            //System.out.println("yes 1!" + contentLength);

            if (contentLength < 1) {
                //System.out.println("yes 1!" + contentLength);

                error();
            }

            /* Set the size for this download if it
         hasn't been already set. */
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            //size connection paisi eta waiting thread k pathacchi noti
            synchronized (this) {
                notify();
            }

            // Open file and seek to the end of it.
            file = new RandomAccessFile(path, "rw");
            file.seek(downloaded);

            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
                /* Size buffer according to how much of the
           file is left to download. */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }

                // Write buffer to file.
                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }

            /* Change status to complete if this point was
         reached because downloading has finished. */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }

    public StringProperty fileNameProperty() {
        return (new SimpleStringProperty(this.getFName()));
    }

    public StringProperty statusProperty() {
        return (new SimpleStringProperty(STATUSES[this.getStatus()]));
    }
    
    public StringProperty percentageProperty() {
        return (new SimpleStringProperty(this.getProgressPercentage()));
    }

    public StringProperty fullSizeProperty() {
        return (new SimpleStringProperty(this.getSizeInString()));
    }

    public StringProperty downloadedSizeProperty() {
        return (new SimpleStringProperty(this.getDownloadedValInString()));
    }
    
    public StringProperty urlLinkProperty() {
        return (new SimpleStringProperty(this.getUrl()));
    }
}
