package pl.masslany.awesomearkanoid.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Block {
    private Vector2 position;
    private float width, height;
    private final int CAMERA_MULTIPLIER = 5;

    private OrthographicCamera camera;
    private GameEngine gameEngine;
    private Body blockBody;
    private int type, count;

    private TextureAtlas blockTextureAtlas;
    private Sprite blockSprite;
    private Color color;

    public Block(int blockType, Vector2 position, GameEngine gameEngine,
                 int count) {
        this.gameEngine = gameEngine;
        camera = gameEngine.getCamera();

        this.count = count;
        this.position = position;
        width = camera.viewportWidth * 0.085f;
        height = camera.viewportHeight * 0.035f;
        type = blockType;
        createBody();
        switch (type) {
            case 1:
                createBlueSprite();
                setColor(gameEngine.getBlue());
                break;
            case 2:
                createOrangeSprite();
                setColor(gameEngine.getOrange());
                break;
            case 3:
                createRedSprite();
                setColor(gameEngine.getRed());
                break;

        }
    }

    private void createBody() {
        BodyDef blockBodyDef = new BodyDef();
        blockBodyDef.position.set(position.x, position.y);
        blockBodyDef.type = BodyType.StaticBody;

        blockBody = gameEngine.getWorld().createBody(blockBodyDef);
        blockBody.setUserData(this);
        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(width / 2, height / 2);
        blockBody.createFixture(blockShape, 0f);
        blockShape.dispose();

    }

    private void createRedSprite() {
        blockTextureAtlas = new TextureAtlas("data/shapes.pack");

        blockSprite = blockTextureAtlas.createSprite("red");
        blockSprite.setColor(1, 1, 1, 1);
        blockSprite.setSize(width * CAMERA_MULTIPLIER, height
                * CAMERA_MULTIPLIER);
        blockSprite.setOrigin(width / 2, height / 2);
        blockSprite.setPosition((blockBody.getPosition().x - width / 2)
                * CAMERA_MULTIPLIER, (blockBody.getPosition().y - height / 2)
                * CAMERA_MULTIPLIER);
    }

    private void createBlueSprite() {
        blockTextureAtlas = new TextureAtlas("data/shapes.pack");

        blockSprite = blockTextureAtlas.createSprite("blue");
        blockSprite.setColor(1, 1, 1, 1);
        blockSprite.setSize(width * CAMERA_MULTIPLIER, height
                * CAMERA_MULTIPLIER);
        blockSprite.setOrigin(width / 2, height / 2);
        blockSprite.setPosition((blockBody.getPosition().x - width / 2)
                * CAMERA_MULTIPLIER, (blockBody.getPosition().y - height / 2)
                * CAMERA_MULTIPLIER);
    }

    private void createOrangeSprite() {
        blockTextureAtlas = new TextureAtlas("data/shapes.pack");

        blockSprite = blockTextureAtlas.createSprite("orange");
        blockSprite.setColor(1, 1, 1, 1);
        blockSprite.setSize(width * CAMERA_MULTIPLIER, height
                * CAMERA_MULTIPLIER);
        blockSprite.setOrigin(width / 2, height / 2);
        blockSprite.setPosition((blockBody.getPosition().x - width / 2)
                * CAMERA_MULTIPLIER, (blockBody.getPosition().y - height / 2)
                * CAMERA_MULTIPLIER);
    }

    public void removeSprite() {
        blockSprite.setColor(1, 1, 1, 0);
    }

    public Body getBlockBody() {
        return blockBody;
    }

    public void setBlockBody(Body blockBody) {
        this.blockBody = blockBody;
    }

    public Sprite getBlockSprite() {
        return blockSprite;
    }

    public void setBlockSprite(Sprite blockSprite) {
        this.blockSprite = blockSprite;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case 1:
                createBlueSprite();
                setColor(gameEngine.getBlue());
                break;
            case 2:
                createOrangeSprite();
                setColor(gameEngine.getOrange());
                break;
            case 3:
                createRedSprite();
                setColor(gameEngine.getRed());
                break;
        }
    }
}
