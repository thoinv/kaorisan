package com.niw.slappybug.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.niw.slappybird.base.BaseScene;
import com.niw.slappybug.manager.SceneManager.SceneType;

public class SplashScene extends BaseScene{
	private Sprite splash;

	@Override
	public void createScene() {
		splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		
		splash.setScale(1.5f);
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}

}
