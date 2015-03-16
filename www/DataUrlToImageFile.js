//
//  DataUrlToImageFile.js
//  Base64 Encoded HTML5 DataUrl to Native Image File PhoneGap/Cordova plugin for Android
//
//  Created by Shawn Van Every on 3/14/2015.
//

  module.exports = {
    
    dataUrlToImageFile:function(dataUrl, options, successCallback, failureCallback) {
    	var format = "image/jpeg";
    	
    	if (dataUrl.indexOf("data:image/png;base64,") > -1) {
    		format = "image/png";
    	} else if (dataUrl.indexOf("data:image/jpeg;base64,") > -1) {
    		format = "image/jpeg";
    	} else if (dataUrl.indexOf("data:image/webp;base64,") > -1) {
    		format = "image/webp";
    	}
    	
		var base64Data = dataUrl.replace(/data:image\/.*;base64,/,'');

		return cordova.exec(successCallback, failureCallback, "DataUrlToImageFile","saveImageData",[base64Data, format]);
    }
  };
  
