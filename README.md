# MediaPickerLib
### Kotlin based media picker library, to pick multiple images and/or vidoes from built-in gallery. Easy to implement and use :) 



[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![](https://jitpack.io/v/WrathChaos/MediaPickerLib.svg)](https://jitpack.io/#WrathChaos/MediaPickerLib)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/WrathChaos/MediaPickerLib)

## Setup
Add this on your module:app build.gradle
```
allprojects {
  repositories {
  	...
  	maven { url 'https://jitpack.io' }
  }
}
```
## Gradle

```
implementation 'com.github.WrathChaos:MediaPickerLib:0.1.2'
```

## Usage


##### This is the request code to handle and get the picked images/videos
```
private val OPEN_MEDIA_PICKER = 1  // Request code
```


##### You need to import Gallery from MediaPickerLib and send mandatory intents to work
```
val intent = Intent(this, Gallery::class.java)
// Set the title for toolbar
intent.putExtra("title", "Select media")
// Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
intent.putExtra("mode", 1)
intent.putExtra("maxSelection", 3) // Optional
startActivityForResult(intent, OPEN_MEDIA_PICKER)
```


##### Receive what you picked here: This is an example from sample project, you can handle whatever you want with the path :)

```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    // Check which request we're responding to
    if (requestCode == OPEN_MEDIA_PICKER) {
        // Make sure the request was successful
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectionResult = data.getStringArrayListExtra("result")
            selectionResult.forEach {
                try {
                    Log.d("MyApp", "Image Path : " + it)
                    val uriFromPath = Uri.fromFile(File(it))
                    Log.d("MyApp", "Image URI : " + uriFromPath)
                    // Convert URI to Bitmap
                    val bm = BitmapFactory.decodeStream(
                            contentResolver.openInputStream(uriFromPath))
                    image.setImageBitmap(bm)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
```

## Customization

#### You can customize the color of the library (More customization features is coming soon)

Title and back's button color
```xml
 <color name="titleTextColor">#ffffff</color>  
```
Unselected image and video's tab title
```xml
 <color name="titleTabColor">#afafaf</color>   
```
Selected image and video's tab title
```xml
<color name="titleSelectedTabColor">#ffffff</color>
``` 
Gallery's fab button color
```xml
<color name="fabColor">#931931</color>
``` 
## Fork
This is original a fork from [multiple-media-picker](https://github.com/erikagtierrez/multiple-media-picker) | Currently not working 
* Re-written on Kotlin
* Added new features 
* Tablet support
* Fixed bugs

#### Thanks for inspiration [Erikagtierrez](https://github.com/erikagtierrez) :)

## Author

FreakyCoder, kurayogun@gmail.com

## License

MediaPickerLib is available under the MIT license. See the LICENSE file for more info.
