# Glide-app

## Overview
This Android application demonstrates different methods of loading and displaying images in an Android app using Glide library. The app showcases three main image loading capabilities:
1. Loading local images from resources
2. Loading images from URLs
3. Loading images from device storage (Downloads folder)

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### File Provider Configuration
The app uses FileProvider for secure file access. Configuration is done in two files:

1. AndroidManifest.xml - Provider Registration:
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### Key Components

#### Image Loading Methods
1. **Local Resource Loading**
   ```kotlin
   binding.imageView.setImageResource(R.drawable.img)
   ```

2. **URL Loading with Glide**
   ```kotlin
   Glide.with(this)
       .load(url)
       .error(R.drawable.error)
       .override(300, 300)
       .into(binding.imageView)
   ```

3. **Device Storage Loading**
   - Implements runtime permission checking
   - Uses FileProvider for secure file access
   - Supports both legacy and modern Android permission models

#### Permission Handling
The app implements a robust permission handling system that:
- Supports Android 13+ with `READ_MEDIA_IMAGES`
- Falls back to `READ_EXTERNAL_STORAGE` for older versions
- Uses the modern Activity Result API for permission requests
- Provides user feedback through Toast messages

## Usage

1. **Local Image**: Click the "Local" button to load an image from app resources
2. **URL Image**: Click the "URL" button to load an image from the internet
3. **Upload**: Click the "Upload" button to load an image from device storage
   - Grant permissions when prompted
   - Ensure you have an image named "xml.png" in your Downloads folder

## Contributing
Feel free to submit issues and enhancement requests!

## License

MIT License

Copyright (c) 2024 [@NezarEa](https://github.com/NezarEa)


## Credits
- Special thanks to [@NizarETH](https://github.com/NizarETH) for assistance with the "Upload" button functionality, ensuring smooth image loading from device storage.
- Glide library: https://github.com/bumptech/glide
- Icons from flaticon.com

## Screenshots
| Screen | Description |
|--------|-------------|
| <img src="https://github.com/user-attachments/assets/38943b15-221b-4575-bbc1-3dc01a462253" width="200"/> | Main interface with three buttons: Local, URL, and Upload |
| <img src="https://github.com/user-attachments/assets/053214fd-2855-40ee-af04-42162f57fc8c" width="200"/> | Loading image from app resources |
| <img src="https://github.com/user-attachments/assets/07dc824a-3b02-4926-b089-5c1ecc707295" width="200"/> | Loading image from remote URL |
| <img src="https://github.com/user-attachments/assets/4d3ce3bc-bb09-497e-8e9c-8b60f0679d13" width="200"/> | Loading image from device storage |
