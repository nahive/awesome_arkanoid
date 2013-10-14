package pl.masslany.awesomearkanoid.Game;

import android.R;
import android.content.res.Resources;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Dash {
    private Vector2 position;
    private float width, height;
    private final int CAMERA_MULTIPLIER = 5;

    private OrthographicCamera camera;
    private GameEngine gameEngine;

    private Body dashBody;
    private TextureAtlas dashTextureAtlas;
    private Sprite dashSprite;
    private int color;

    Dash(GameEngine gameEngine, int pickedColor) {
        this.gameEngine = gameEngine;
        camera = gameEngine.getCamera();
        width = camera.viewportWidth * 0.2f;
        height = camera.viewportHeight * 0.025f;
        position = new Vector2(camera.viewportWidth * 0.5f,
                camera.viewportHeight * 0.1f);
        color = pickedColor;
        createBody();
        createDashSprite(color);
    }

    private void createBody() {
        BodyDef dashBodyDef = new BodyDef();
        dashBodyDef.position.set(position.x, position.y);
        dashBodyDef.type = BodyType.StaticBody;

        dashBody = gameEngine.getWorld().createBody(dashBodyDef);
        dashBody.setUserData(this);
        PolygonShape dashShape = new PolygonShape();
        dashShape.setAsBox(width / 2, height / 2);
        dashBody.createFixture(dashShape, 0f);

        dashShape.dispose();
    }

    private void createDashSprite(int color) {
        dashTextureAtlas = new TextureAtlas("data/shapes.pack");
        if(color == gameEngine.res.getColor(android.R.color.holo_green_light))
        	dashSprite = dashTextureAtlas.createSprite("green");
        else if(color == gameEngine.res.getColor(android.R.color.holo_red_light))
        	dashSprite = dashTextureAtlas.createSprite("red");
        else if(color == gameEngine.res.getColor(android.R.color.holo_purple))
        	dashSprite = dashTextureAtlas.createSprite("purple");
        else if(color == gameEngine.res.getColor(android.R.color.holo_orange_light))
        	dashSprite = dashTextureAtlas.createSprite("orange");
        else
        	dashSprite = dashTextureAtlas.createSprite("blue");
        dashSprite.setColor(1, 1, 1, 1);
        dashSprite.setSize(width * CAMERA_MULTIPLIER, height * CAMERA_MULTIPLIER);
        dashSprite.setOrigin(width / 2, height / 2);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Body getDashBody() {
        return dashBody;
    }

    public void setDashBody(Body dashBody) {
        this.dashBody = dashBody;
    }

    public Sprite getDashSprite() {
        return dashSprite;
    }

    public void setDashSprite(Sprite dashSprite) {
        this.dashSprite = dashSprite;
    }

}
