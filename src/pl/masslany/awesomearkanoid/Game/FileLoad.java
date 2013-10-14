package pl.masslany.awesomearkanoid.Game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

import com.badlogic.gdx.math.Vector2;

// FileLoad class is for loading and writing memory in android such as points, levels or favourite color.
public class FileLoad {
    private String file;
    private Block[] blocks = new Block[100];
    private AssetManager am;
    private int howManyBlocks = 0;
    private int levelBeaten, pickedColor;

    public FileLoad(String file, Context context) {
        am = context.getAssets();
        this.file = file;
    }

    public void loadBlocks(GameEngine gameEngine) {
        BufferedReader br;
        float currentX = gameEngine.getCamera().viewportWidth * 0.1f;
        float currentY = gameEngine.getCamera().viewportHeight * 0.66f;
        float howMuchAddX = gameEngine.getCamera().viewportWidth * 0.09f;
        try {
            InputStream is = am.open(file);
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            int length = Integer.parseInt(line);
            howManyBlocks = 0;
            for (int i = 0; i < length; i++) {
                line = br.readLine();
                String[] block = line.split(" ");

                for (int j = 0; j < block.length; j++) {
                    if (Integer.parseInt(block[j]) == 0) {
                        currentX += howMuchAddX;
                    } else {
                        blocks[howManyBlocks] = new Block(
                                Integer.parseInt(block[j]), new Vector2(
                                currentX, currentY), gameEngine, howManyBlocks);
                        currentX += howMuchAddX;
                        howManyBlocks++;
                    }
                }
                currentY += gameEngine.getCamera().viewportHeight * 0.04f;
                currentX = gameEngine.getCamera().viewportWidth * 0.1f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NIE OTWORZYLEM LVLA");
        }
    }

    public void loadMem(Context context) {
        BufferedReader br;
        try {
            InputStream is = context.openFileInput("mem");
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            String[] memo = line.split(" ");
            levelBeaten = Integer.parseInt(memo[0]);
            pickedColor = Integer.parseInt(memo[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NIE OTWORZYLEM MEM");
        }
    }

    public void writeMem(Context context) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("mem", Context.MODE_PRIVATE);
            outputStream.write((levelBeaten + " " + pickedColor).getBytes());
            System.out.println("ZAPISALEM " + pickedColor);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("NIEMA");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NIE ZAPISALEM");
        }
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public int getBlocksCount() {
        return howManyBlocks;
    }

    public int getPickedColor() {
        return pickedColor;
    }

    public void setPickedColor(int pickedColor) {
        this.pickedColor = pickedColor;
    }

    public int getLevelBeaten() {
        return levelBeaten;
    }

    public void setLevelBeaten(int levelBeaten) {
        this.levelBeaten = levelBeaten;
    }

}
