/**
 * 
 * @author Vu Minh Thang
 * 
 */
package com.kaorisan.lazyload;

import java.io.File;

import com.kaorisan.common.DebugLog;

import android.content.Context;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the dir to save cached images
    	try {
	        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
	        else {
	            cacheDir = context.getCacheDir();
	        }
	        if(!cacheDir.exists())
	            cacheDir.mkdirs();
    	} catch (Exception ex){
    		DebugLog.logd(ex.getMessage());
    	}
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}