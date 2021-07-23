package com.app.hmp.cognitive.afrocamgistchat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.app.hmp.cognitive.afrocamgistchat.api.Webservices;
import com.app.hmp.cognitive.afrocamgistchat.constants.Constants;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ErrorReporter implements Thread.UncaughtExceptionHandler{
    private String[] _recipients = new String[]{"afrocamgist@gmail.com"};
    private String _subject = "Crash Report of Afrocamgist App Android";

    private static String VersionName;
    private static String buildNumber;
    private static String PackageName;
    private static String FilePath;
    private static String PhoneModel;
    private static String AndroidVersion;
    private static String Board;
    private static String Brand;
    private static String Device;
    private static String Display;
    private static String FingerPrint;
    private static String Host;
    private static String ID;
    private static String Manufacturer;
    private static String Model;
    private static String Product;
    private static String Tags;
    long Time;
    private static String Type;
    private static String User;
    HashMap<String, String> CustomParameters = new HashMap<String, String>();

    private Thread.UncaughtExceptionHandler PreviousHandler;
    private ErrorReporter S_mInstance;
    private Context CurContext;

    public void AddCustomData(String Key, String Value) {
        CustomParameters.put(Key, Value);
    }

    private String CreateCustomInfoString() {
        String CustomInfo = "";
        Iterator iterator = CustomParameters.keySet().iterator();
        while (iterator.hasNext()) {
            String CurrentKey = (String) iterator.next();
            String CurrentVal = CustomParameters.get(CurrentKey);
            CustomInfo += CurrentKey + " = " + CurrentVal + "\n";
        }
        return CustomInfo;
    }

    ErrorReporter getInstance() {
        if (S_mInstance == null)
            S_mInstance = new ErrorReporter();
        return S_mInstance;
    }

    public void Init(Context context) {
        PreviousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        CurContext = context;
    }



    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    void RecoltInformations(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            // Version
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            VersionName = pi.versionName;
            buildNumber = currentVersionNumber(context);
            // Package name
            PackageName = pi.packageName;

            // Device model
            PhoneModel = Build.MODEL;
            // Android version
            AndroidVersion = Build.VERSION.RELEASE;

            Board = Build.BOARD;
            Brand = Build.BRAND;
            Device = Build.DEVICE;
            Display = Build.DISPLAY;
            FingerPrint = Build.FINGERPRINT;
            Host = Build.HOST;
            ID = Build.ID;
            Model = Build.MODEL;
            Product = Build.PRODUCT;
            Tags = Build.TAGS;
            Time = Build.TIME;
            Type = Build.TYPE;
            User = Build.USER;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String CreateInformationString() {
        RecoltInformations(CurContext);

        String ReturnVal = "";
        ReturnVal += "Version : " + VersionName;
        ReturnVal += "\n";
        ReturnVal += "Build Number : " + buildNumber;
        ReturnVal += "\n";
        ReturnVal += "Package : " + PackageName;
        ReturnVal += "\n";
        ReturnVal += "FilePath : " + FilePath;
        ReturnVal += "\n";
        ReturnVal += "Phone Model" + PhoneModel;
        ReturnVal += "\n";
        ReturnVal += "Android Version : " + AndroidVersion;
        ReturnVal += "\n";
        ReturnVal += "Board : " + Board;
        ReturnVal += "\n";
        ReturnVal += "Brand : " + Brand;
        ReturnVal += "\n";
        ReturnVal += "Device : " + Device;
        ReturnVal += "\n";
        ReturnVal += "Display : " + Display;
        ReturnVal += "\n";
        ReturnVal += "Finger Print : " + FingerPrint;
        ReturnVal += "\n";
        ReturnVal += "Host : " + Host;
        ReturnVal += "\n";
        ReturnVal += "ID : " + ID;
        ReturnVal += "\n";
        ReturnVal += "Model : " + Model;
        ReturnVal += "\n";
        ReturnVal += "Product : " + Product;
        ReturnVal += "\n";
        ReturnVal += "Tags : " + Tags;
        ReturnVal += "\n";
        ReturnVal += "Time : " + Time;
        ReturnVal += "\n";
        ReturnVal += "Type : " + Type;
        ReturnVal += "\n";
        ReturnVal += "User : " + User;
        ReturnVal += "\n";
        ReturnVal += "Total Internal memory : "
                + getTotalInternalMemorySize();
        ReturnVal += "\n";
        ReturnVal += "Available Internal memory : "
                + getAvailableInternalMemorySize();
        ReturnVal += "\n";

        return ReturnVal;
    }

    public void uncaughtException(Thread t, Throwable e) {
        String Report = "";
        //Date CurDate = new Date(Time);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String CurDate = df.format(Calendar.getInstance().getTime());

        Report += "Error Report collected on : " + CurDate;
        Report += "\n";
        Report += "\n";
        Report += "Informations :";
        Report += "\n";
        Report += "==============";
        Report += "\n";
        Report += "\n";
        Report += CreateInformationString();

        Report += "Custom Informations :\n";
        Report += "=====================\n";
        Report += CreateCustomInfoString();

        Report += "\n\n";
        Report += "Stack : \n";
        Report += "======= \n";
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        Report += stacktrace;

        Report += "\n";
        Report += "Cause : \n";
        Report += "======= \n";

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            Report += result.toString();
            cause = cause.getCause();
        }
        printWriter.close();
        Report += "*** End of current Report **";
        SaveAsFile(Report);
        // SendErrorMail( Report );
        PreviousHandler.uncaughtException(t, e);
    }

    private void SendErrorMail(Context _context, String ErrorContent) {
        //send mail popup
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = _subject;
        String body = "\n\n" + ErrorContent + "\n\n";
        sendIntent.putExtra(Intent.EXTRA_EMAIL, _recipients);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(Intent.createChooser(sendIntent, "Title:"));

    }

    private void SaveAsFile(String ErrorContent) {
        try {
            Random generator = new Random();
            int random = generator.nextInt(99999);
            String FileName = "stack-" + random + ".doc";
            FileOutputStream trace = CurContext.openFileOutput(FileName,
                    Context.MODE_PRIVATE);
            trace.write(ErrorContent.getBytes());
            trace.close();

        } catch (Exception e) {
            e.printStackTrace();
            // ...
        }
    }

    private String[] GetErrorFileList() {
        File dir = new File(FilePath + "/");
        // Try to create the files folder if it doe````sn't exist
        dir.mkdir();
        // Filter for ".stacktrace" files
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {

                Log.d("filename1",""+name);


                return name.endsWith(".doc");
            }
        };

        return dir.list(filter);
    }

    private boolean bIsThereAnyErrorFile() {
        return GetErrorFileList().length > 0;
    }


    public void CheckErrorAndSendMail(Context _context) {
        try {
            FilePath = _context.getFilesDir().getAbsolutePath();
            if (bIsThereAnyErrorFile()) {
                String WholeErrorText = "";

                String[] ErrorFileList = GetErrorFileList();
                int curIndex = 0;
                final int MaxSendMail = 5;
                for (String curString : ErrorFileList) {

                    Log.d("ErrorFileLists","111==="+curString);


                    if (curIndex++ <= MaxSendMail) {
                        WholeErrorText += "New Trace collected :\n";
                        WholeErrorText += "=====================\n ";
                        String filePath = FilePath + "/" + curString;
                        // Log.e("ERROR", filePath);

                        Log.d("ErrorFileLists","222==="+FilePath +"/"+curString);

                        BufferedReader input = new BufferedReader(
                                new FileReader(filePath));
                        String line;
                        while ((line = input.readLine()) != null) {
                            WholeErrorText += line + "\n";
                        }
                        input.close();
                    }

                    // DELETE FILES !!!!
                    File curFile = new File(FilePath + "/" + curString);
                    curFile.delete();
                }
                //SendErrorMail(_context, WholeErrorText);
                createTxtFile(_context, WholeErrorText);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void createTxtFile(Context _context, String wholeErrorText){
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "AfrocamgistLogs");
            if (!root.exists()) {
                root.mkdirs();
            }
            String fileName = "android_user_" + LocalStorage.getUserDetails().getUserId() + ".txt";
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(wholeErrorText);
            writer.flush();
            writer.close();

            uploadLogFileToServer(_context,fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String currentVersionNumber(Context a) {
        PackageManager pm = a.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("com.TBI.afrocamgist",
                    PackageManager.GET_SIGNATURES);
            return pi.versionName
                    + (pi.versionCode > 0 ? " (" + pi.versionCode + ")"
                    : "");
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private void uploadLogFileToServer(Context _context, String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AfrocamgistLogs/";
        File crashLogFile = new File(path + fileName);
        if(crashLogFile.exists()){
            Log.d("response99","exists....");
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(Constants.MEDIA, crashLogFile.getName(), RequestBody.create(MediaType.parse("text/plain"), crashLogFile));

            Map<String, String> headers = new HashMap<>();
            headers.put(Constants.CONTENT_TYPE,"multipart/form-data");
            //headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

            Webservices.getData((Activity) _context,Webservices.Method.POST, builder, headers, UrlEndpoints.UPLOAD_CRASH_FILE, response -> {
                if ("".equals(response)) {
                    Utils.showAlert((Activity) _context, "Oops something went wrong....");
                } else {
                    try {
                        Log.d("response99",""+response.toString());
                        JSONObject object = new JSONObject(response);
                        if (object.has(Constants.MESSAGE)) {

                        }else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Utils.showAlert(getActivity(), "Oops something went wrong....");
                    }
                }
            });
        }
    }

}
