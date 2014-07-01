package com.niw.kuttypanda.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.niw.kuttypanda.manager.ResourcesManager;
import com.niw.kuttypanda.manager.SceneManager.SceneType;

public abstract class BaseScene extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager rm;
	protected VertexBufferObjectManager vbom;
	protected Camera camera;
	
	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------
	
	public BaseScene()
	{
		this.rm = ResourcesManager.getInstance();
		this.engine = rm.engine;
		this.activity = rm.activity;
		this.vbom = rm.vbom;
		this.camera = rm.camera;
		createScene();
	}
	
	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------
	
	public abstract void createScene();
	
	public abstract void onBackKeyPressed();
	
	public abstract SceneType getSceneType();
	
	public abstract void disposeScene();
}