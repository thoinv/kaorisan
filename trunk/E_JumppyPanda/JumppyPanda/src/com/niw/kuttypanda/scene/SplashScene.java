package com.niw.kuttypanda.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.niw.kuttypanda.base.BaseScene;
import com.niw.kuttypanda.common.Constants;
import com.niw.kuttypanda.manager.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	private Sprite splash;
	
	@Override
	public void createScene(){
		setBackground(new Background(Color.WHITE));
		this.splash = new Sprite(0, 0, rm.splashTextureRegion, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		this.splash.setScale(1.5f);
		this.splash.setPosition((Constants.width - splash.getWidth()) * 0.5f, (Constants.height - splash.getHeight()) * 0.5f);
    	attachChild(splash);
	}

	@Override
	public void onBackKeyPressed(){
		return;
	}

	@Override
	public SceneType getSceneType(){
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene()	{
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}
}