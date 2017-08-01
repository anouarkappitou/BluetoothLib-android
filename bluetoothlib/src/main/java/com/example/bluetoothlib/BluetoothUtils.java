package com.example.bluetoothlib;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by anouarkappitou on 8/1/17.
 */

public class BluetoothUtils {

    public static boolean check_bluetooth_permission( Context context ){
        String permission = "android.permission.BLUETOOTH";
        int result = context.checkCallingOrSelfPermission( permission );
        return ( result == PackageManager.PERMISSION_GRANTED );
    }

    public static boolean check_bluetooth_admin_permission( Context context ){
        String permission = "android.permission.BLUETOOTH_ADMIN";
        int result = context.checkCallingOrSelfPermission( permission );
        return ( result == PackageManager.PERMISSION_GRANTED );
    }
}
