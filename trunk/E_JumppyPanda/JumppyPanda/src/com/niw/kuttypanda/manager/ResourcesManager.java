package com.niw.kuttypanda.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

import com.niw.kuttypanda.GameActivity;
import com.niw.kuttypanda.common.Constants;

public class ResourcesManager {
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	
	private BitmapTextureAtlas splashTextureAtlas;
	private BitmapTextureAtlas bitmapTextureAtlas_background;
	private BitmapTextureAtlas bitmapTextureAtlas_logo;
	private BitmapTextureAtlas bitmapTextureAtlas_startButton;
	private BitmapTextureAtlas bitmapTextureAtlas_shareButton;
	private BitmapTextureAtlas bitmapTextureAtlas_settingsButton;
	private BitmapTextureAtlas bitmapTextureAtlas_panda;
	private BitmapTextureAtlas bitmapTextureAtlas_pipe1;
	private BitmapTextureAtlas bitmapTextureAtlas_pipe2;
	private BitmapTextureAtlas bitmapTextureAtlas_scrore_board;
	private BitmapTextureAtlas bitmapTextureAtlas_game_over;
	private BitmapTextureAtlas bitmapTextureAtlas_oke;
	private BitmapTextureAtlas bitmapTextureAtlas_checkButton;
	private BitmapTextureAtlas bitmapTextureAtlas_panel;
	
	public ITextureRegion splashTextureRegion;
	public TextureRegion backgroundTiledTextureRegion;
	public TextureRegion textureRegion_logo;
	public TextureRegion textureRegion_startButton;
	public TextureRegion textureRegion_shareButton;
	public TextureRegion textureRegion_settingsButton;
	public TiledTextureRegion tiledTextureRegion_panda;
	public TextureRegion textureRegion_game_over;
	public TextureRegion textureRegion_pipe1;
	public TextureRegion textureRegion_pipe2; 
	public TextureRegion textureRegion_score_board;
	public TextureRegion textureRegion_oke;
	public TiledTextureRegion textureRegion_checkButton;
	public TextureRegion textureRegion_panel;
	
	private BitmapTextureAtlas mFontTexture_score;
	private BitmapTextureAtlas mFontTexture_score2;
	private BitmapTextureAtlas mFontTexture_score_best;

	public AnimatedSprite animatedSprite_panda;
	public Sprite backGroundSprite;
	public Sprite sprite_game_over;
	public Sprite sprite_startButton;
	public Sprite sprite_shareButton;
	public 	Sprite sprite_settingsButton;
	public 	Sprite sprite_pipe1;
	public 	Sprite sprite_pipe2;
	public 	Sprite sprite_score_board;
	
	public 	Music mSwooshing, mPoint, mDie, mWing, mHit;

	private int splash_width = 296;
	private int splash_height = 114;
	
	private int logo_width = 323;
	private int logo_height = 79;
	
	private int start_button_width = 137;
	private int start_button_height = 90;
	
	private int share_button_width = 122;
	private int share_button_height = 65;
	
	private int settings_button_width = 155;
	private int settings_button_height = 65;
	
	public 	int panda_position = 4;
	public 	int panda_width = 113;
	public 	int panda_height = 1648;
	public float panda_scale = 0.6f;
	
	public 	int pipe_width = 128;
	public 	int pipe_height = 500;
	
	private int game_over_width = 197;
	private int game_over_height = 27;
	
	private int score_board_width = 400;
	private int score_board_height = 183;
	
	private int score_width = 256;
	private int score_height = 256;
	
	private int score2_width = 256;
	private int score2_height = 256;
	
	private int score_best_width = 256;
	private int score_best_height = 256;
	
	private int oke_width = 122;
	private int oke_height = 68;
	
	private int check_button_width = 240;
	private int check_button_height = 90;
	
	private int panel_width = 400;
	private int panel_height = 183;
	
	public Font mFont;
	
	public Text mScoreText;
	public Text mScoreText2;
	public 	Text mScoreTextBest;
	
	public 	int speed = 1;
	
	public int point = 0;
	
	public int panda_up = 5;
	public float panda_down = 3f;
	public int pipe_transition = 3;
	public int pipe_move = 3;
	
	public 	int heightR;
	
	public Boolean isDie = false;
	public 	boolean cham = false;

	public void loadMenuResources() {
		loadMenuGraphics();
		loadSettingsGraphics();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.bitmapTextureAtlas_background = new BitmapTextureAtlas(activity.getTextureManager(), Constants.width, Constants.height, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.backgroundTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_background, activity, "bg-tre.png", 0, 0);
		this.bitmapTextureAtlas_background.load();
		
		this.bitmapTextureAtlas_logo = new BitmapTextureAtlas(activity.getTextureManager(), logo_width, logo_height , TextureOptions.BILINEAR);
		this.textureRegion_logo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_logo, activity, "logo.png", 0, 0);
		this.bitmapTextureAtlas_logo.load();
		
		this.bitmapTextureAtlas_startButton = new BitmapTextureAtlas(activity.getTextureManager(), start_button_width, start_button_height, TextureOptions.BILINEAR);
		this.textureRegion_startButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_startButton, activity, "start.png", 0, 0);
		this.bitmapTextureAtlas_startButton.load();

		this.bitmapTextureAtlas_shareButton = new BitmapTextureAtlas(activity.getTextureManager(), share_button_width, share_button_height, TextureOptions.BILINEAR);
		this.textureRegion_shareButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_shareButton, activity, "share.png", 0, 0);
		this.bitmapTextureAtlas_shareButton.load();
		
		this.bitmapTextureAtlas_settingsButton = new BitmapTextureAtlas(activity.getTextureManager(), settings_button_width, settings_button_height, TextureOptions.BILINEAR);
		this.textureRegion_settingsButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_settingsButton, activity, "setting.png", 0, 0);
		this.bitmapTextureAtlas_settingsButton.load();
	}

	private void loadSettingsGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.bitmapTextureAtlas_oke = new BitmapTextureAtlas(activity.getTextureManager(), oke_width, oke_height, TextureOptions.BILINEAR);
		this.textureRegion_oke = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_oke, activity, "oke.png", 0, 0);
		this.bitmapTextureAtlas_oke.load();
		
		this.bitmapTextureAtlas_checkButton = new BitmapTextureAtlas(activity.getTextureManager(), check_button_width, check_button_height, TextureOptions.BILINEAR);
		this.textureRegion_checkButton = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bitmapTextureAtlas_checkButton, activity, "check.png", 0, 0, 2, 1);
		this.bitmapTextureAtlas_checkButton.load();
		
		this.bitmapTextureAtlas_panel = new BitmapTextureAtlas(activity.getTextureManager(), panel_width, panel_height, TextureOptions.BILINEAR);
		this.textureRegion_panel = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_panel, activity, "panel.png", 0, 0);
		this.bitmapTextureAtlas_panel.load();
	}
	
	public void unloadSettingsGraphics() {
		bitmapTextureAtlas_oke.unload();
		textureRegion_oke = null;
		
		bitmapTextureAtlas_checkButton.unload();
		textureRegion_checkButton = null;
		
		bitmapTextureAtlas_panel.unload();
		textureRegion_panel = null;
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.bitmapTextureAtlas_panda = new BitmapTextureAtlas(activity.getTextureManager(), panda_width, panda_height, TextureOptions.BILINEAR);
		this.tiledTextureRegion_panda = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bitmapTextureAtlas_panda, activity, "panda.png", 0, 0, 1, 8);
		this.bitmapTextureAtlas_panda.load();

		this.bitmapTextureAtlas_pipe1 = new BitmapTextureAtlas(activity.getTextureManager(), pipe_width, pipe_height,TextureOptions.BILINEAR);
		this.textureRegion_pipe1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_pipe1, activity, "column.png", 0, 0);
		this.bitmapTextureAtlas_pipe1.load();

		this.bitmapTextureAtlas_pipe2 = new BitmapTextureAtlas(activity.getTextureManager(), pipe_width, pipe_height, TextureOptions.BILINEAR);
		this.textureRegion_pipe2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_pipe2, activity, "column1.png", 0, 0);
		this.bitmapTextureAtlas_pipe2.load();
		
		this.bitmapTextureAtlas_game_over = new BitmapTextureAtlas(activity.getTextureManager(), game_over_width, game_over_height , TextureOptions.BILINEAR);
		this.textureRegion_game_over = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_game_over, activity, "gameover.png", 0, 0);
		this.bitmapTextureAtlas_game_over.load();
		
		this.bitmapTextureAtlas_scrore_board = new BitmapTextureAtlas(activity.getTextureManager(), score_board_width, score_board_height, TextureOptions.BILINEAR);
		this.textureRegion_score_board = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas_scrore_board, activity, "score.png", 0, 0);
		this.bitmapTextureAtlas_scrore_board.load();
	}

	private void loadGameFonts() {
		this.mFontTexture_score = new BitmapTextureAtlas(activity.getTextureManager(), score_width, score_height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFontTexture_score2 = new BitmapTextureAtlas(activity.getTextureManager(), score2_width, score2_height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFontTexture_score_best = new BitmapTextureAtlas(activity.getTextureManager(), score_best_width, score_best_height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mFont = new Font(activity.getFontManager(), this.mFontTexture_score, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLACK);
		this.mFont = new Font(activity.getFontManager(), this.mFontTexture_score2, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLACK);
		this.mFont = new Font(activity.getFontManager(), this.mFontTexture_score_best, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLACK);
		
		activity.getTextureManager().loadTexture(this.mFontTexture_score);
		activity.getTextureManager().loadTexture(this.mFontTexture_score2);
		activity.getTextureManager().loadTexture(this.mFontTexture_score_best);
		activity.getFontManager().loadFont(this.mFont);
	}

	private void loadGameAudio() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			this.mSwooshing = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "swooshing.ogg");
			this.mPoint = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "point.ogg");
			this.mDie = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "die.ogg");
			this.mHit = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "hit.ogg");
			this.mWing = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "wing.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), splash_width, splash_height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		this.splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		this.splashTextureAtlas.unload();
		this.splashTextureRegion = null;
	}

	public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}
