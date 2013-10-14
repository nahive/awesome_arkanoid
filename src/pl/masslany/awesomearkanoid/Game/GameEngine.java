package pl.masslany.awesomearkanoid.Game;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
// This is main engine class. Everything is created here: world, lights, dash, ball etc.
public class GameEngine implements ApplicationListener, ContactListener,
		InputProcessor {

	private OrthographicCamera camera;
	private final int CAMERA_MULTIPLIER = 5;
	private World world;
	private Box2DDebugRenderer renderer;
	private Context context;
	private RayHandler handler;

	private SpriteBatch batch;

	Game game;
    Resources res;
	private float width, height;
	private Dash dash;
	private Ball ball;
	private Label pointLabel;
	private BitmapFont font;

	private FileLoad fileLoad;
	private int levelPicked, howManyBlocksLeft, dashColor;
	private Color orange, blue, green, purple, red, aqua;
	private boolean started = false;
	private int lifes = 3, points = 0;

	private ArrayList<Body> bodiesToRemove;
	private ArrayList<Body> ballToRemove;
	private Sprite[] heartSprites;

	private PointLight ballLight;

	public GameEngine(Context context, int levelPicked) {
		super();
		this.context = context;
		this.game = (Game) context;
		res = game.getResources();
		this.levelPicked = levelPicked;
	}

	@Override
	public void create() { // run once
		bodiesToRemove = new ArrayList<Body>();
		ballToRemove = new ArrayList<Body>();
		width = Gdx.graphics.getWidth() / CAMERA_MULTIPLIER;
		height = Gdx.graphics.getHeight() / CAMERA_MULTIPLIER;

		// camera
		camera = new OrthographicCamera(width, height);
		camera.position.set(width * 0.5f, height * 0.5f, 0f);
		camera.update();

		// world
		world = new World(new Vector2(0, 0), false);
		world.setContactListener(this);

		// light
		handler = new RayHandler(world);
		handler.setCombinedMatrix(camera.combined);
		handler.setAmbientLight(0.9f);

		// debug
		// renderer = new Box2DDebugRenderer();

		font = new BitmapFont(Gdx.files.internal("data/font_white32.fnt"),
				false);

		batch = new SpriteBatch();

		Gdx.input.setInputProcessor(this);
		loadFiles();
		createWalls();
		createEntities();
		createUI();
		addBallLights(ball);

	}

	@Override
	public void render() { // run every frame
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// renderer.render(world, camera.combined);

		handler.updateAndRender();
		world.step(1 / 60f, 6, 2);
		// logger.log();

		removeBodies();
		checkGameState();
		updateGraphics();
	}

	private void updateGraphics() {
		batch.begin();

		ball.getBallSprite().draw(batch);
		ball.getBallSprite().setPosition(
				(ball.getBallBody().getPosition().x - ball.getSize() / 2)
						* CAMERA_MULTIPLIER,
				(ball.getBallBody().getPosition().y - ball.getSize() / 2)
						* CAMERA_MULTIPLIER);

		for (int i = 0; i < fileLoad.getBlocksCount(); i++) {
			fileLoad.getBlocks()[i].getBlockSprite().draw(batch);
		}

		dash.getDashSprite().draw(batch);
		dash.getDashSprite().setPosition(
				(dash.getDashBody().getPosition().x - dash.getWidth() / 2)
						* CAMERA_MULTIPLIER,
				(dash.getDashBody().getPosition().y - dash.getHeight() / 2)
						* CAMERA_MULTIPLIER);

		for (int i = 0; i < lifes; i++) {
			heartSprites[i].draw(batch);
		}
		pointLabel.setText("PTS: " + points);
		pointLabel.draw(batch, 1);
		batch.end();

	}

	private void checkGameState() {
		if (howManyBlocksLeft < 1) {
			fileLoad = new FileLoad("mem", context);
			if (levelPicked > fileLoad.getLevelBeaten()) {
				fileLoad.setLevelBeaten(levelPicked);
			}
			fileLoad.writeMem(context);
			dispose();
			game.gameWon(points);
		}
		if (ball.getBallBody().getPosition().y < 0) {
			if (lifes < 2) {
				game.gameLost();
			} else {
				started = false;
				ballToRemove.add(ball.getBallBody());
				ball = new Ball(this, dash);
				ballLight.attachToBody(ball.getBallBody(), 0, 0);
				lifes--;
			}

		}
	}

	private void createUI() {
		TextureAtlas heartTextureAtlas = new TextureAtlas("data/shapes.pack");
		heartSprites = new Sprite[lifes];
		for (int i = 0; i < lifes; i++) {
			Sprite heartSprite = heartTextureAtlas.createSprite("red");
			heartSprite.setColor(1, 1, 1, 1);
			heartSprite.setSize(camera.viewportWidth * 0.1f,
					camera.viewportWidth * 0.1f);
			heartSprite.setPosition(
					(camera.viewportWidth * 0.9f + camera.viewportWidth * 0.03f
							* i)
							* CAMERA_MULTIPLIER, camera.viewportWidth * 0.05f
							* CAMERA_MULTIPLIER);
			heartSprites[i] = heartSprite;
		}
		LabelStyle ls = new LabelStyle(font, red);
		pointLabel = new Label("PTS: " + points, ls);
		pointLabel.setX(camera.viewportWidth * 0.1f);
		pointLabel.setY(camera.viewportHeight * 0.1f);

	}

	private void createEntities() {
		dash = new Dash(this, dashColor);
		ball = new Ball(this, dash);
	}

	private void createWalls() {
		BodyDef ceilingBodyDef = new BodyDef();
		ceilingBodyDef.position.set(0, camera.viewportHeight);
		Body ceilingBody = world.createBody(ceilingBodyDef);

		PolygonShape ceilingBox = new PolygonShape();
		ceilingBox.setAsBox(camera.viewportWidth, 0);
		ceilingBody.createFixture(ceilingBox, 0f);

		BodyDef rightBodyDef = new BodyDef();
		rightBodyDef.position.set(camera.viewportWidth + 1, 0);
		Body rightBody = world.createBody(rightBodyDef);

		PolygonShape rightBox = new PolygonShape();
		rightBox.setAsBox(0, camera.viewportHeight);
		rightBody.createFixture(rightBox, 0f);

		BodyDef leftBodyDef = new BodyDef();
		leftBodyDef.position.set(0, -1);
		Body leftBody = world.createBody(leftBodyDef);

		PolygonShape leftBox = new PolygonShape();
		leftBox.setAsBox(0, camera.viewportHeight);
		leftBody.createFixture(leftBox, 0f);
		ceilingBox.dispose();
		rightBox.dispose();
		leftBox.dispose();

	}

	private void addBallLights(Ball ball) {
		ballLight = new PointLight(handler, 750, Color.WHITE, 150, width / 2 - 50,
				height / 2 + 15);
		ballLight.setSoft(true);
		ballLight.setSoftnessLenght(7.5f);
		ballLight.attachToBody(ball.getBallBody(), 0, 0);
		
		/*
		PointLight lightDash = new PointLight(handler, 100, blue, 300,
				width / 2 - 50, height / 2 + 15);
		lightDash.attachToBody(dash.getDashBody(), 0, 0);

		for (int i = 0; i < fileLoad.getBlocksCount(); i++) {
			PointLight blockLight = new PointLight(handler, 25,
					fileLoad.getBlocks()[i].getColor(), 50, width / 2 - 50,
					height / 2 + 15);
			blockLight.attachToBody(fileLoad.getBlocks()[i].getBlockBody(), 0,
					0);
		}
	*/
	}

	private void loadFiles() {
		orange = new Color(1f, 0.73f, 0.2f, 1f);
		blue = new Color(0.2f, 0.71f, 0.9f, 1f);
		green = new Color(0.6f, 0.8f, 0f, 1f);
		purple = new Color(0.67f, 0.5f, 0.8f, 1f);
		red = new Color(1f, 0.27f, 0.27f, 1f);
		aqua = new Color(0.04f, 0.86f, 0.85f, 1f);
		fileLoad = new FileLoad("levels/level" + levelPicked, context);
		fileLoad.loadBlocks(this);
		fileLoad.loadMem(context);
		howManyBlocksLeft = fileLoad.getBlocksCount();
		dashColor = fileLoad.getPickedColor();
	}

	private void removeBodies() {
		for (Body b : bodiesToRemove) {
			if (b != null) {
				Block block = (Block) b.getUserData();
				block.removeSprite();
				world.destroyBody(b);
				bodiesToRemove.remove(b);
				howManyBlocksLeft--;
			}
		}
		for (Body b : ballToRemove) {
			if (b != null) {
				world.destroyBody(b);
				ballToRemove.remove(b);
			}
		}
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		world.dispose();
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endContact(Contact contact) {
		Object A = contact.getFixtureA().getBody().getUserData();
		Object B = contact.getFixtureB().getBody().getUserData();
		if (A instanceof Ball) {
			if (B instanceof Block) {
				if (((Block) B).getType() < 2) {
					bodiesToRemove.add(((Block) B).getBlockBody());
					points += 200;
				} else {
					((Block) B).setType(((Block) B).getType() - 1);
				}
			}
		} else if (A instanceof Block) {
			if (B instanceof Ball) {
				if (((Block) A).getType() < 2) {
					bodiesToRemove.add(((Block) A).getBlockBody());
					points += 200;
				} else {
					((Block) A).setType(((Block) A).getType() - 1);
				}
			}
		}
		if (A instanceof Ball) {
			if (B instanceof Dash) {
				((Ball) A).dashVelocity(dash);
			}
		} else if (A instanceof Dash) {
			if (B instanceof Ball) {
				((Ball) B).dashVelocity(dash);
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public Color getOrange() {
		return orange;
	}

	public void setOrange(Color orange) {
		this.orange = orange;
	}

	public Color getBlue() {
		return blue;
	}

	public void setBlue(Color blue) {
		this.blue = blue;
	}

	public Color getGreen() {
		return green;
	}

	public void setGreen(Color green) {
		this.green = green;
	}

	public Color getPurple() {
		return purple;
	}

	public void setPurple(Color purple) {
		this.purple = purple;
	}

	public Color getRed() {
		return red;
	}

	public void setRed(Color red) {
		this.red = red;
	}

	public int getCAMERA_MULTIPLIER() {
		return CAMERA_MULTIPLIER;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!started) {
			ball.getBallBody().setLinearVelocity(
					camera.viewportWidth * ball.getSpeedMulti(),
					camera.viewportHeight * ball.getSpeedMulti());
			started = true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!started) {
			ball.getBallBody().setTransform(
					new Vector2(dash.getDashBody().getPosition().x, dash
							.getDashBody().getPosition().y + ball.getSize()),
					0f);
		}
		Vector2 touchPos = new Vector2();
		touchPos.set(screenX / CAMERA_MULTIPLIER, dash.getPosition().y);
		dash.getDashBody().setTransform(touchPos, 0f);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		System.out.println(character);
		return false;
	}

}