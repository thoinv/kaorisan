package com.niw.kuttypanda.scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.niw.kuttypanda.GameDao;
import com.niw.kuttypanda.base.BaseScene;
import com.niw.kuttypanda.base.DebugLog;
import com.niw.kuttypanda.common.Constants;
import com.niw.kuttypanda.manager.SceneManager;
import com.niw.kuttypanda.manager.SceneManager.SceneType;

public class SettingsScene extends BaseScene {
	AnimatedSprite sprite_easyModeButton;
	AnimatedSprite sprite_hardModeButton;
	
	@Override
	public void createScene() {
		rm.backGroundSprite = new Sprite(0, 0, Constants.width, Constants.height, rm.backgroundTiledTextureRegion, vbom);
		attachChild(rm.backGroundSprite);

		Sprite sprite_panel = new Sprite(Constants.width / 9, Constants.height / 3, rm.textureRegion_panel, vbom);
		attachChild(sprite_panel);
		
		sprite_easyModeButton = new AnimatedSprite(Constants.width / 2, Constants.height / 2 - 120, rm.textureRegion_checkButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					DebugLog.loge("easy click" + GameDao.getSettings());
					if(GameDao.getSettings().equals(Constants.hard)){
						sprite_easyModeButton.animate(new long[]{1}, new int[]{1}, 0);
						sprite_hardModeButton.animate(new long[]{1}, new int[]{0}, 0);
						GameDao.setSettings(Constants.easy);
					}
				}
				return true;
			}
		};
		attachChild(sprite_easyModeButton);
		sprite_easyModeButton.setScale(0.5f);
		registerTouchArea(sprite_easyModeButton);
		
		sprite_hardModeButton = new AnimatedSprite(Constants.width / 2 , Constants.height / 2 - 40, rm.textureRegion_checkButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					DebugLog.loge("hard click" + GameDao.getSettings());
					if(GameDao.getSettings().equals(Constants.easy)){
						sprite_easyModeButton.animate(new long[]{1}, new int[]{0}, 0);
						sprite_hardModeButton.animate(new long[]{1}, new int[]{1}, 0);
						GameDao.setSettings(Constants.hard);
					}
				}
				return true;
			}
		};
		
		attachChild(sprite_hardModeButton);
		sprite_hardModeButton.setScale(0.5f);
		registerTouchArea(sprite_hardModeButton);
		
		if(GameDao.getSettings().equals(Constants.hard)){
			sprite_easyModeButton.animate(new long[]{1}, new int[]{0}, 0);
			sprite_hardModeButton.animate(new long[]{1}, new int[]{1}, 0);
		}else{
			sprite_easyModeButton.animate(new long[]{1}, new int[]{1}, 0);
			sprite_hardModeButton.animate(new long[]{1}, new int[]{0}, 0);
		}
		
		Sprite sprite_oke = new Sprite(Constants.width / 3, Constants.height / 3 * 2, rm.textureRegion_oke, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					SceneManager.getInstance().loadMenuScene(engine);
				}
				return true;
			}
		};
		attachChild(sprite_oke);
//		this.sprite_oke.setScale(1.5f);
		registerTouchArea(sprite_oke);
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
