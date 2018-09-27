package com.zhongruan.android.fingerprint_demo.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.zhongruan.android.fingerprint_demo.config.ABLConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.android.BuildConfig;

public class Utils {
    public static final String DEVICETYPE_YLT1 = "BM7502";
    public static final String DEVICETYPE_YLT2 = "BM7510";
    public static final int LEFT_FINGER_FIRST = 3;
    public static final int LEFT_FINGER_FOURTH = 1;
    public static final int LEFT_FINGER_LITTLE = 0;
    public static final int LEFT_FINGER_MIDDLE = 2;
    public static final int LEFT_FINGER_THUMB = 4;
    final List<String> list;
    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;
    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.##");

    public Utils(List<String> list) {
        this.list = list;
    }

    public static String getUSBPath() {
        String str = Build.MODEL;
        int obj = -1;
        switch (str.hashCode()) {
            case 1962326088:
                if (str.equals(DEVICETYPE_YLT1) || str.equals(DEVICETYPE_YLT2)) {
                    obj = LEFT_FINGER_FOURTH;
                    break;
                }
                break;
        }
        switch (obj) {
            case LEFT_FINGER_FOURTH:
                return "/storage/usbhost1/";
            default:
                return null;
        }
    }

    public static boolean checkUSBInserted() {
        String path = getUSBPath();
        if (stringIsEmpty(path)) {
            return false;
        }
        boolean flag;
        File file = new File(path);
        if (file.exists()) {
            flag = true;
            String tempPath = BuildConfig.VERSION_NAME;
            File[] files = file.listFiles();
            if (files != null && files.length == LEFT_FINGER_FOURTH && files[LEFT_FINGER_LITTLE].isDirectory()) {
                LogUtil.i("根目录多了一层目录，进入检测 : " + files[LEFT_FINGER_LITTLE].getAbsolutePath());
                tempPath = files[LEFT_FINGER_LITTLE].getAbsolutePath();
            } else {
                LogUtil.i("直接检测根目录 : " + path);
                tempPath = path;
            }
            File testFile = new File(tempPath + File.separator + "test.test");
            try {
                LogUtil.i("test.test", "create");
                testFile.createNewFile();
            } catch (Exception e) {
                LogUtil.i("test.test", "create failed");
                flag = false;
            }
            try {
                LogUtil.i("test.test", "delete");
                testFile.delete();
            } catch (Exception e2) {
                LogUtil.i("test.test", "delete failed");
            }
            if (flag) {
                LogUtil.i("U盘已插入");
            } else {
                LogUtil.i("U盘未插入");
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static List<String> checkUSBZip(String directory, String prefix) {
        if (!checkUSBInserted()) {
            return new ArrayList();
        }
        int i;
        List<String> list = new ArrayList();
        for (i = LEFT_FINGER_LITTLE; i < LEFT_FINGER_THUMB; i += LEFT_FINGER_FOURTH) {
            list.add(BuildConfig.VERSION_NAME);
        }
        File folder = new File(directory);
        if (!folder.exists()) {
            return list;
        }
        list.set(LEFT_FINGER_FIRST, folder.getAbsolutePath());
        File[] files = folder.listFiles();
        if (files == null) {
            return list;
        }
        if (files.length == LEFT_FINGER_FOURTH && files[LEFT_FINGER_LITTLE].isDirectory()) {
            LogUtil.i("根目录下有一层文件夹，尝试进入搜索");
            return checkUSBZip(files[LEFT_FINGER_LITTLE].getAbsolutePath(), prefix);
        }
        int count = LEFT_FINGER_LITTLE;
        StringBuffer sb = new StringBuffer();
        for (i = LEFT_FINGER_LITTLE; i < files.length; i += LEFT_FINGER_FOURTH) {
            File f = files[i];
            if (f.isFile()) {
                String name = f.getName();
                sb.setLength(LEFT_FINGER_LITTLE);
                sb.append(directory);
                sb.append(File.separator);
                sb.append(name);
                if (stringIsEmpty(prefix)) {
                    if (!(name == null || name.equals(BuildConfig.VERSION_NAME) || !name.toUpperCase().endsWith(".ZIP") || name.indexOf(ABLConfig.VALUE_SPLIT) == 0)) {
                        list.set(LEFT_FINGER_LITTLE, sb.toString());
                        count += LEFT_FINGER_FOURTH;
                        list.add(sb.toString());
                    }
                } else if (!(name == null || name.equals(BuildConfig.VERSION_NAME) || !name.toUpperCase().endsWith(".ZIP") || name.indexOf(ABLConfig.VALUE_SPLIT) == 0 || name.indexOf(prefix) != 0)) {
                    list.set(LEFT_FINGER_LITTLE, sb.toString());
                    count += LEFT_FINGER_FOURTH;
                    list.add(sb.toString());
                }
                if (!(name == null || name.equals(BuildConfig.VERSION_NAME) || !name.toUpperCase().endsWith(".SN"))) {
                    list.set(LEFT_FINGER_FOURTH, sb.toString());
                }
            }
        }
        if (count < LEFT_FINGER_MIDDLE) {
            return list;
        }
        list.set(LEFT_FINGER_MIDDLE, count + BuildConfig.VERSION_NAME);
        return list;
    }

    public static String checkPwd(String path) {
        String str = null;
        try {
            File file = new File(path + "/PackageDataPwd.SN");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = BuildConfig.VERSION_NAME;
                StringBuffer buffer = new StringBuffer();
                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    buffer.append(line);
                }
                str = new CryptoTools().getDecString(buffer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static String formatFileSize(long paramLong) {
        DecimalFormat localDecimalFormat1 = fileIntegerFormat;
        if ((paramLong < 1024L) && (paramLong > 0L)) {
            return localDecimalFormat1.format(paramLong) + "B";
        }
        if (paramLong < 1048576L) {
            return localDecimalFormat1.format(paramLong / 1024.0D) + "K";
        }
        if (paramLong < 1073741824L) {
            return localDecimalFormat1.format(paramLong / 1048576.0D) + "M";
        }
        DecimalFormat localDecimalFormat2 = fileDecimalFormat;
        return localDecimalFormat2.format(paramLong / 1073741824.0D) + "G";
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            StatFs localStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
        }
        return -1L;
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return -1;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static long getAvailableInternalMemorySize() {
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
    }

    public static long getTotalInternalMemorySize() {
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        return localStatFs.getBlockSize() * localStatFs.getBlockCount();
    }


    public static boolean stringIsEmpty(String string) {
        if (string == null || string.trim().equals(BuildConfig.VERSION_NAME) || string.trim().toLowerCase().equals("null") || string.trim().toLowerCase().equals("<null>")) {
            return true;
        }
        return false;
    }

    public static boolean isEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null && str2 != null) {
            return false;
        }
        if (str1 != null && str2 == null) {
            return false;
        }
        if (str1.trim().equals(str2.trim())) {
            return true;
        }
        return false;
    }
}
