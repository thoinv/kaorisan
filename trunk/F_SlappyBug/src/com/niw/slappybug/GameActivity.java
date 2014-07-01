package com.niw.slappybug;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.niw.slappybug.manager.ResourcesManager;
import com.niw.slappybug.manager.SceneManager.SceneType;
import com.niw.slappybug.scene.SplashScene;

public class GameActivity extends BaseGameActivity{
	
	private Camera camera;
	private ResourcesManager resourcesManager;
	private SplashScene splashScene;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, Consts.SCREEN_HEIGHT, Consts.SCREEN_WIDTH);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(Consts.SCREEN_HEIGHT, Consts.SCREEN_WIDTH), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	@Override
	public void onPopulateScene(Scene scene, OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

}
