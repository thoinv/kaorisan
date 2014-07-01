package com.niw.kuttypanda.scene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.os.Looper;

import com.niw.kuttypanda.ShareFacebookActivity;
import com.niw.kuttypanda.base.BaseScene;
import com.niw.kuttypanda.base.DebugLog;
import com.niw.kuttypanda.common.Constants;
import com.niw.kuttypanda.manager.SceneManager;
import com.niw.kuttypanda.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene {
	
	@Override
	public void createScene() {
		attachChild(new Sprite(0, 0, Constants.width, Constants.height, rm.backgroundTiledTextureRegion, vbom));
		
		Sprite sprite_logo = new Sprite(Constants.width / 2 - 118, 200, rm.textureRegion_logo, vbom);
		attachChild(sprite_logo);
		sprite_logo.setScale(1.5f);

		Sprite sprite_startButton = new Sprite(Constants.width / 3, Constants.height / 2, rm.textureRegion_startButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					SceneManager.getInstance().loadGameScene(engine);
				}
				return true;
			}
		};
		
		Sprite sprite_settingsButton = new Sprite(Constants.width / 8, Constants.height / 3 * 2, rm.textureRegion_settingsButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					SceneManager.getInstance().loadSettingsScene(engine);
				}
				return true;
			}
		};
		attachChild(sprite_settingsButton);
		registerTouchArea(sprite_settingsButton);
		
		attachChild(sprite_startButton);
		registerTouchArea(sprite_startButton);
		
		Sprite sprite_shareButton = new Sprite(Constants.width / 3 * 2, Constants.height / 3 * 2, rm.textureRegion_shareButton,vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					(new Thread() {
		                public void run() {
		                     Looper.prepare();
		                     DebugLog.loge("click");
					    	 ShareFacebookActivity shareFacebookActivity = new ShareFacebookActivity();
					    	 shareFacebookActivity.shareFacebook();
		                }
		            }).start();
				}
				return true;
			}
		};
		
		attachChild(sprite_shareButton);
		registerTouchArea(sprite_shareButton);		
	}


	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {

	}
}
