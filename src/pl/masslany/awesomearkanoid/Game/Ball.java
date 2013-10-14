package pl.masslany.awesomearkanoid.Game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Ball {
    private Vector2 position, velocity;
    private float size, speedMulti = 1;
    private final int CAMERA_MULTIPLIER = 5;

    private OrthographicCamera camera;
    private GameEngine gameEngine;
    private Dash dash;
    private Body ballBody;

    private Texture ballTexture;
    private Sprite ballSprite;

    public Ball(GameEngine gameEngine, Dash dash) {
        this.gameEngine = gameEngine;
        this.dash = dash;
        camera = gameEngine.getCamera();

        size = camera.viewportWidth * 0.03f;
        position = new Vector2(dash.getPosition().x, dash.getPosition().y
                + size);
        velocity = new Vector2(0, 0);
        createBody();
        createSprite();
    }

    private void createBody() {
        BodyDef ballBodyDef = new BodyDef();
        ballBodyDef.type = BodyType.DynamicBody;
        ballBodyDef.position.set(position.x, position.y);
        ballBodyDef.fixedRotation = false;
        ballBodyDef.linearVelocity.set(velocity);

        ballBody = gameEngine.getWorld().createBody(ballBodyDef);
        ballBody.setUserData(this);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(size / 2);

        FixtureDef ballFixture = new FixtureDef();
        ballFixture.shape = ballShape;
        ballFixture.density = 0f;
        ballFixture.restitution = 1f;
        ballFixture.friction = 0f;

        ballBody.createFixture(ballFixture);
        ballShape.dispose();
    }

    private void createSprite() {
        ballTexture = new Texture("data/ball.png");
        ballSprite = new Sprite(ballTexture);
        ballSprite.setColor(1, 1, 1, 1);
        ballSprite.setSize(size * CAMERA_MULTIPLIER, size * CAMERA_MULTIPLIER);
        ballSprite.setOrigin(size / 2, size / 2);

    }

    public void dashVelocity(Dash dash) {
        float dashCenter = dash.getDashBody().getPosition().x;
        float ballCenter = ballBody.getPosition().x;
        float relev = ((ballCenter - dashCenter) / (dash.getWidth() / 2)) * 0.75f;
        System.out.println(relev + " X " + camera.viewportWidth * relev + "  Y " + camera.viewportHeight * (1 - Math.abs(relev)));
        ballBody.setLinearVelocity(camera.viewportWidth * relev * speedMulti,
                camera.viewportHeight * (1 - Math.abs(relev)) * speedMulti);

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Body getBallBody() {
        return ballBody;
    }

    public void setBallBody(Body ballBody) {
        this.ballBody = ballBody;
    }

    public Sprite getBallSprite() {
        return ballSprite;
    }

    public void setBallSprite(Sprite ballSprite) {
        this.ballSprite = ballSprite;
    }

    public float getSpeedMulti() {
        return speedMulti;
    }

    public void setSpeedMulti(float speedMulti) {
        this.speedMulti = speedMulti;
    }

}
