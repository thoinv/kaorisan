package com.niw.kuttypanda.manager;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.niw.kuttypanda.base.BaseScene;
import com.niw.kuttypanda.scene.GameScene;
import com.niw.kuttypanda.scene.MainMenuScene;
import com.niw.kuttypanda.scene.SettingsScene;
import com.niw.kuttypanda.scene.SplashScene;

public class SceneManager
{
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene settingsScene;
	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	
	private BaseScene currentScene;
	
	private Engine engine = ResourcesManager.getInstance().engine;
	
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_SETTINGS,
	}
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
			case SCENE_MENU:
				setScene(menuScene);
				break;
			case SCENE_GAME:
				setScene(gameScene);
				break;
			case SCENE_SPLASH:
				setScene(splashScene);
				break;
			case SCENE_SETTINGS:
				setScene(settingsScene);
				break;
			default:
				break;
		}
	}
	
	public void createMenuScene(){
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
	}
	
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	private void disposeSplashScene(){
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	public void loadGameScene(final Engine mEngine){
		ResourcesManager.getInstance().loadGameResources();
		gameScene = new GameScene();
		setScene(gameScene);
	}
	
	public void loadSettingsScene(final Engine mEngine){
		ResourcesManager.getInstance().loadGameResources();
		settingsScene = new SettingsScene();
		setScene(settingsScene);
	}
	
	public void loadMenuScene(final Engine mEngine){
		setScene(menuScene);
	}
	
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType(){
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
}