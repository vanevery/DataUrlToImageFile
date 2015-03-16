DataUrl2ImagePlugin
============

This plugin allows you to save the contents of an HTML5 Data URI (from a canvas tag or otherwise) to the file system on Android.

It was inspired by Canvas2ImagePlugin [https://github.com/devgeeks/Canvas2ImagePlugin]

Installation
------------

### For Cordova 4.0.x:

1. To add this plugin just type: `cordova plugin add https://github.com/vanevery/DataUrlToImageFile.git` or `phonegap local plugin add https://github.com/vanevery/DataUrlToImageFile.git`
2. To remove this plugin type: `cordova plugin remove com.mobvcasting.DataUrlToImageFile` or `phonegap local plugin remove com.mobvcasting.DataUrlToImageFile`

Usage:
------

Call the `window.dataUrlToImageFile.saveImageData(dataurl, options, success, error)` :

### Example
```html
<canvas id="acanvas" width="100px" height="100px"></canvas>
```

```javascript
function onDeviceReady()
{
	var canvas = document.getElementById('acanvas');
	var dataURL = canvas.toDataURL("image/jpeg");

	window.dataUrl2ImagePlugin.saveImageDataURLtoFileSystem(
		dataUrl,
		{
			format: "image/jpeg"
		}
        function(filePath){
            console.log(filePath);
        },
        function(err){
            console.log(err);
        }
    );
}
```


