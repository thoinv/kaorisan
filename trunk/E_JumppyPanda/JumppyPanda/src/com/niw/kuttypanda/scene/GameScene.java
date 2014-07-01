package com.niw.kuttypanda.scene;

import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.opengl.GLES20;

import com.niw.kuttypanda.GameDao;
import com.niw.kuttypanda.base.BaseScene;
import com.niw.kuttypanda.base.DebugLog;
import com.niw.kuttypanda.common.Constants;
import com.niw.kuttypanda.manager.SceneManager;
import com.niw.kuttypanda.manager.SceneManager.SceneType;

public class GameScene extends BaseScene {
	
	@Override
	public void createScene() {
		rm.mSwooshing.play();
		rm.isDie = false;
		Sprite backGroundSprite = new Sprite(0, 0, Constants.width, Constants.height, rm.backgroundTiledTextureRegion, vbom);
		attachChild(backGroundSprite);

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
//				animatedSprite_panda.animate(new long[]{200, 200, 200}, 1, 3, true);
				rm.panda_down = 2f;
				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() - rm.panda_up);
				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() - rm.panda_up);
				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() - rm.panda_up);
				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() - rm.panda_up);
				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() - rm.panda_up);
				rm.mWing.play();
				return true;
			}
		});

		rm.animatedSprite_panda = new AnimatedSprite(Constants.width / rm.panda_position, Constants.height / 2, rm.tiledTextureRegion_panda, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					DebugLog.logd("click");
				}
				return true;
			}
		};
		
		rm.animatedSprite_panda.animate(150);
//		long ani = 200; 
//		this.animatedSprite_panda.animate(new long[]{ani, ani, ani, ani, ani}, 0, 4, true);
		rm.animatedSprite_panda.setScale(rm.panda_scale);
		attachChild(rm.animatedSprite_panda);
		registerTouchArea(rm.animatedSprite_panda);
		rm.animatedSprite_panda.setZIndex(1);
		
		rm.mScoreText = new Text(5, Constants.height / 20 * 19, rm.mFont, "Score: 0", "Score: XXXX".length(), vbom);
		rm.mScoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		rm.mScoreText.setAlpha(0.5f);
		attachChild(rm.mScoreText);
		rm.mScoreText.setZIndex(1);
		
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {

			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				try {
					Thread.sleep(rm.speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!rm.isDie){
					rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() + rm.panda_down);
					rm.panda_down = rm.panda_down + 0.05f;
				}
				if (rm.animatedSprite_panda.getY() >= Constants.height -(rm.panda_height/7 * rm.panda_scale / 2)) {
					deathEvent();
				}
			}
		});
		
		rm.sprite_pipe1 = new Sprite(Constants.width, -rm.pipe_height / 2, rm.textureRegion_pipe1, vbom);
		attachChild(rm.sprite_pipe1);
		rm.sprite_pipe1.setVisible(false);

		rm.sprite_pipe2 = new Sprite(Constants.width, Constants.height - rm.pipe_height / 2, rm.textureRegion_pipe2, vbom);
		attachChild(rm.sprite_pipe2);
		rm.sprite_pipe2.setVisible(false);

		rm.sprite_pipe1.setZIndex(0);
		rm.sprite_pipe2.setZIndex(0);
		
		rm.sprite_score_board = new Sprite(Constants.width / 8, Constants.height / 3, rm.textureRegion_score_board, vbom);
		attachChild(rm.sprite_score_board);
		rm.sprite_score_board.setVisible(false);
		
		rm.mScoreText2 = new Text(Constants.width / 2 + 30, Constants.height / 3 + 50, rm.mFont, "0", "XXXX".length(), vbom);
		rm.mScoreText2.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		rm.mScoreText2.setAlpha(0.5f);
		attachChild(rm.mScoreText2);
		rm.mScoreText2.setVisible(false);
		
		rm.mScoreTextBest = new Text(Constants.width / 2 + 30, Constants.height / 3 + 105, rm.mFont, "0", "XXXX".length(), vbom);
		rm.mScoreTextBest.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		rm.mScoreTextBest.setAlpha(0.5f);
		attachChild(rm.mScoreTextBest);
		rm.mScoreTextBest.setVisible(false);
		
		rm.sprite_game_over = new Sprite(Constants.width / 3, Constants.height / 5, rm.textureRegion_game_over, vbom);
		rm.sprite_game_over.setScale(2);
		attachChild(rm.sprite_game_over);
		rm.sprite_game_over.setVisible(false);

		registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						loadSpritePipe();
					}
				}));
		

	}

	private void loadSpritePipe() {
		DebugLog.logd("PLAY_SCENE: loadSpritePipe");
		
		rm.sprite_pipe1.setVisible(true);
		rm.sprite_pipe2.setVisible(true);
		
		rm.point = 0;
		rm.heightR = rm.pipe_height / 2 ;

		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {

			}

			
			boolean chuyen = false;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sortChildren();
				
				if(!rm.isDie){
						if (!chuyen) {
							rm.heightR = getRandom(0, rm.pipe_height);
							rm.sprite_pipe1.setPosition(Constants.width, -rm.heightR);
							rm.sprite_pipe2.setPosition(Constants.width, -rm.heightR + Constants.height);
							
							if (rm.sprite_pipe1.getX() > (Constants.width / 5 * 4)) chuyen = true;
						} else {
							if(GameDao.getSettings().equals(Constants.hard)){
								hardModeEvent();
							}else{
								rm.sprite_pipe1.setPosition(rm.sprite_pipe1.getX() -rm.pipe_transition, rm.sprite_pipe1.getY());
								rm.sprite_pipe2.setPosition(rm.sprite_pipe2.getX() - rm.pipe_transition, rm.sprite_pipe2.getY());
							}
							
							if (rm.sprite_pipe1.getX() < -rm.pipe_width || rm.sprite_pipe2.getX() < -rm.pipe_width) {
								chuyen = false;
							}
						}
						pointUpEvent();
				}

				collisionEvent();
			}

		});
	}
	
	private void hardModeEvent() {
		if (rm.heightR <= rm.pipe_height / 2) {
			if (rm.sprite_pipe2.getY() >= Constants.height) {
				rm.cham = false;
			}
			if (rm.sprite_pipe2.getY() <= (rm.pipe_height - rm.heightR)) {
				rm.cham = true;
			}
			if (!rm.cham) {
				rm.sprite_pipe1.setPosition(rm.sprite_pipe1.getX() - rm.pipe_transition, rm.sprite_pipe1.getY() - rm.pipe_move);
				rm.sprite_pipe2.setPosition(rm.sprite_pipe2.getX() - rm.pipe_transition, rm.sprite_pipe2.getY() - rm.pipe_move);
			} else {
				rm.sprite_pipe1.setPosition(rm.sprite_pipe1.getX() - rm.pipe_transition, rm.sprite_pipe1.getY() +rm. pipe_move );
				rm.sprite_pipe2.setPosition(rm.sprite_pipe2.getX() - rm.pipe_transition, rm.sprite_pipe2.getY() + rm.pipe_move);
			}
		} else {
			if (rm.sprite_pipe1.getY() >= (-rm.heightR + Constants.height - rm.pipe_height)) {
				rm.cham = true;
			}
			if (rm.sprite_pipe1.getY() <= -rm.pipe_height) {
				rm.cham = false;
			}
			if (!rm.cham) {
				rm.sprite_pipe1.setPosition(rm.sprite_pipe1.getX()- rm.pipe_transition, rm.sprite_pipe1.getY() + rm.pipe_move);
				rm.sprite_pipe2.setPosition(rm.sprite_pipe2.getX() - rm.pipe_transition, rm.sprite_pipe2.getY() + rm.pipe_move);
			} else {
				rm.sprite_pipe1.setPosition(rm.sprite_pipe1.getX() - rm.pipe_transition, rm.sprite_pipe1.getY() - rm.pipe_move);
				rm.sprite_pipe2.setPosition(rm.sprite_pipe2.getX() - rm.pipe_transition, rm.sprite_pipe2.getY() - rm.pipe_move);
			}
		}
	}
	
	private int getRandom(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	private void pointUpEvent() {
		if (rm.sprite_pipe1.getX() >= ((Constants.width / rm.panda_position) - (rm.panda_width * rm.panda_scale)) 
				&& rm.sprite_pipe1.getX() <= ((Constants.width / rm.panda_position) + rm.pipe_move + 1 - (rm.panda_width * rm.panda_scale))) {
			if (rm.animatedSprite_panda.getY() <= -(rm.panda_height/7 * rm.panda_scale)) {
				rm.mHit.play();
				this.deathEvent();
			} else {
				if(!rm.isDie){
					rm.point++;
					rm.mPoint.play();
					rm.mScoreText.setText("Score: " + rm.point);
					rm.mScoreText2.setText(""+rm.point);
				}
			}
		}
	}

	private void collisionEvent() {
		if ((rm.animatedSprite_panda.collidesWith(rm.sprite_pipe1)) || 
				(rm.animatedSprite_panda.collidesWith(rm.sprite_pipe2))) {
			if(!rm.isDie){
				rm.mHit.play();
				this.deathEvent();				
			}
		}
	}
	
	private void deathEvent(){
		rm.isDie = true;
		rm.animatedSprite_panda.setRotation(180);
		sortChildren();
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {

			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				try {
					Thread.sleep(rm.speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				rm.animatedSprite_panda.setPosition(rm.animatedSprite_panda.getX(), rm.animatedSprite_panda.getY() + 30);
				if (rm.animatedSprite_panda.getY() >= Constants.height -(rm.panda_width * rm.panda_scale / 2)) {
					setIgnoreUpdate(true);
					rm.mDie.play();
					showScore();
				}
			}
		});
	}

	private void showScore(){
		if(GameDao.getSettings().equals(Constants.easy)){
			if(GameDao.getBestScoreEasy() < rm.point){
				GameDao.setBestScoreEasy(rm.point);
			}
			rm.mScoreTextBest.setText("" + GameDao.getBestScoreEasy());
		}else{
			if(GameDao.getBestScoreHard() < rm.point){
				GameDao.setBestScoreHard(rm.point);
			}
			rm.mScoreTextBest.setText("" + GameDao.getBestScoreHard());
		}
		
		
		if(rm.point < 10){
			rm.mScoreText2.setPosition(Constants.width / 2 + 30, Constants.height / 3 + 50);
		}else if(rm.point < 100){
			rm.mScoreText2.setPosition(Constants.width / 2 + 15, Constants.height / 3 + 50);
		}else if(rm.point < 1000){
			rm.mScoreText2.setPosition(Constants.width / 2, Constants.height / 3 + 50);
		}
		
		if(GameDao.getBestScoreHard() < 10){
			rm.mScoreTextBest.setPosition(Constants.width / 2 + 30, Constants.height / 3 + 105);
		}else if(GameDao.getBestScoreHard() < 100){
			rm.mScoreTextBest.setPosition(Constants.width / 2 + 15, Constants.height / 3 + 105);
		}else if(GameDao.getBestScoreHard() < 1000){
			rm.mScoreTextBest.setPosition(Constants.width / 2, Constants.height / 3 +105);
		}
		
		rm.sprite_score_board.setVisible(true);
		rm.sprite_game_over.setVisible(true);
		rm.mScoreText.setVisible(false);
		rm.mScoreText2.setVisible(true);
		rm.mScoreTextBest.setVisible(true);
		rm.sprite_startButton = new Sprite(Constants.width / 3, Constants.height / 5 * 3, rm.textureRegion_startButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					detachSelf();
					SceneManager.getInstance().loadGameScene(engine);
					rm.isDie = false;
				}
				return true;
			}
		};
		attachChild(rm.sprite_startButton);
		registerTouchArea(rm.sprite_startButton);
		
		rm.sprite_settingsButton = new Sprite(Constants.width / 7, Constants.height / 5 * 4, rm.textureRegion_settingsButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					SceneManager.getInstance().loadSettingsScene(engine);
					}
				return true;
			}
		};
		attachChild(rm.sprite_settingsButton);
		registerTouchArea(rm.sprite_settingsButton);
		
		rm.sprite_shareButton = new Sprite(Constants.width / 3 * 2, Constants.height / 5 * 4, rm.textureRegion_shareButton, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					//TODO
				}
				return true;
			}
		};
		attachChild(rm.sprite_shareButton);
		registerTouchArea(rm.sprite_shareButton);
		
		setOnSceneTouchListener(null);
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
