package com.niw.kuttypanda;

import java.io.IOException;

import org.andengine.AndEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.niw.kuttypanda.common.Constants;
import com.niw.kuttypanda.manager.ResourcesManager;
import com.niw.kuttypanda.manager.SceneManager;

public class GameActivity extends BaseGameActivity{
	public Camera camera;
	
	private RelativeLayout relativeLayout;
	private AdView adView;
	private AdRequest adRequest;
	private Camera MyCamera;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.camera = new Camera(0, 0, Constants.width, Constants.height);
		SQLiteDatabaseAdapter.setContext(this);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(Constants.width, Constants.height), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}

	@Override
	protected void onSetContentView() {
		relativeLayout = new RelativeLayout(this);
		final RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

		if (!AndEngine.isDeviceSupported()) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(3500);
						android.os.Process.killProcess(android.os.Process.myPid());
					} catch (InterruptedException e) {
					}
				}
			};
			this.toastOnUIThread("This device does not support AndEngine GLES2, so this game will not work. Sorry.");
			finish();
			thread.start();

			this.setContentView(relativeLayout, relativeLayoutParams);
		} else {
			this.mRenderSurfaceView = new RenderSurfaceView(this);
			mRenderSurfaceView.setRenderer(mEngine, this);

			relativeLayout.addView(mRenderSurfaceView, GameActivity.createSurfaceViewLayoutParams());
				
			try {
				adView = new AdView(this, AdSize.BANNER, "a15323f3a1c9dee");
				adView.setTag("adView");
				adView.refreshDrawableState();
				adView.setVisibility(AdView.VISIBLE);
				
				adRequest = new AdRequest();
				adView.loadAd(adRequest);
				
				AdView.LayoutParams adViewParams = new AdView.LayoutParams(AdView.LayoutParams.WRAP_CONTENT, AdView.LayoutParams.WRAP_CONTENT);
				adViewParams.addRule(AdView.ALIGN_PARENT_BOTTOM);

				relativeLayout.addView(adView, adViewParams);
			} catch (Exception e) {
			}
			this.setContentView(relativeLayout, relativeLayoutParams);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);	
	}

}
