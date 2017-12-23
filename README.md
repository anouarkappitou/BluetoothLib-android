# Simple bluetooth library for android
# installation

* Add it to your root build.gradle

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
 * add the dependency
 
```
 	dependencies {
	        compile 'com.github.anouarkappitou:BluetoothLib-android:v1.0'
	}
```


# usage

 * add bluetooth permissions to your manifest file
 
 ```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

 * instanciate bluetooth object
 
  ```java
Bluetooth bluetooth = new Bluetooth(Context);
```


 * Enable bluetooth adapter
 
 ```java
bluetooth.enable();
```


* Set OnDeviceFound callback
 
 ```java
bluetooth.set_found_callback(new IFoundCallback() {
            @Override
            public void onDevice(BluetoothDevice device) {
             	// logic
            }
});
```


* Start discovery
 
 ```java
bluetooth.start_discovery();
```


 
