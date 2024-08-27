package com.youvalehman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.net.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class aGame extends ApplicationAdapter {
    Texture background;
    SpriteBatch batch;
    Texture bird1;
    Texture bird2;
    Circle birdCircle;
    int birdNum;
    int birdX;
    int birdY;
    Texture bottomPole;
    int bottomPoleLocY;
    Rectangle[] bottomPoleRectangle;
    int bottomScreenPadding;
    int bumpUpSpeed;
    int currentLevel;
    int flapRate;
    BitmapFont gameOverFont;
    int gameState;
    int gapRightLeftPoles;
    int gapUpDownPoles;
    int goingLeftSpeed;
    int gravity;
    Map<String, Integer> leadingScores;
    int midScreenX;
    int midScreenY;
    int nextLevelParameter;
    int numOfObjects;
    int[] poleLocX;
    int[] poleLocY;
    Random random;
    int score;
    BitmapFont scoringFont;
    int scoringPole;
    int screenX;
    int screenY;
    int ticks;
    int ticks2;
    Texture upperPole;
    Rectangle[] upperPoleRectangle;
    int upperScreenPadding;

    public void create() {
        this.leadingScores = new HashMap();
        this.ticks = 0;
        this.batch = new SpriteBatch();
        this.numOfObjects = 3;
        this.scoringFont = new BitmapFont();
        this.gameOverFont = new BitmapFont();
        this.scoringFont.setColor(Color.BLUE);
        this.scoringFont.getData().setScale(5.0f);
        this.gameOverFont.setColor(Color.BLACK);
        this.gameOverFont.getData().setScale(7.0f);
        this.screenX = Gdx.graphics.getWidth();
        this.screenY = Gdx.graphics.getHeight();
        this.midScreenX = this.screenX / 2;
        this.midScreenY = this.screenY / 2;
        this.poleLocX = new int[this.numOfObjects];
        this.poleLocY = new int[this.numOfObjects];
        this.upperPoleRectangle = new Rectangle[this.numOfObjects];
        this.bottomPoleRectangle = new Rectangle[this.numOfObjects];
        this.bird1 = new Texture("bird.png");
        this.bird2 = new Texture("bird2.png");
        this.upperPole = new Texture("toptube.png");
        this.bottomPole = new Texture("bottomtube.png");
        this.background = new Texture("bg.png");
        this.birdCircle = new Circle(0.0f, 0.0f, (float) (this.bird1.getHeight() / 2));
        for (int i = 0; i < this.numOfObjects; i++) {
            this.upperPoleRectangle[i] = new Rectangle(0.0f, 0.0f, (float) this.upperPole.getWidth(), (float) this.upperPole.getHeight());
            this.bottomPoleRectangle[i] = new Rectangle(0.0f, 0.0f, (float) this.bottomPole.getWidth(), (float) this.bottomPole.getHeight());
        }
        this.gameState = 1;
        this.random = new Random();
        this.currentLevel = 0;
        this.bumpUpSpeed = Input.Keys.NUMPAD_6;
        this.flapRate = 10;
        this.upperScreenPadding = this.screenY - 100;
        this.bottomScreenPadding = 100 - this.bottomPole.getHeight();
        this.birdNum = 1;
        this.ticks2 = 0;
        this.nextLevelParameter = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    public void drawBird() {
        this.ticks++;
        if (this.birdNum == 1) {
            this.batch.draw(this.bird1, (float) this.birdX, (float) this.birdY);
            if (this.ticks == this.flapRate) {
                this.birdNum = 2;
                this.ticks = 0;
                return;
            }
            return;
        }
        this.batch.draw(this.bird2, (float) this.birdX, (float) this.birdY);
        if (this.ticks == this.flapRate) {
            this.birdNum = 1;
            this.ticks = 0;
        }
    }

    public void render() {
        this.ticks2++;
        this.batch.begin();
        this.batch.draw(this.background, 0.0f, 0.0f, (float) this.screenX, (float) this.screenY);
        int i = 0;
        if (this.gameState == 1) {
            this.goingLeftSpeed = 5;
            this.gapRightLeftPoles = 700;
            this.gravity = 5;
            this.gapUpDownPoles = 1000;
            this.score = 0;
            this.scoringPole = 0;
            this.currentLevel = 0;
            this.ticks2 = 0;
            this.birdX = this.midScreenX - (this.bird1.getWidth() / 2);
            this.birdY = this.midScreenY - (this.bird1.getHeight() / 2);
            this.bottomPoleLocY = 0;
            for (int i2 = 0; i2 < this.numOfObjects; i2++) {
                this.poleLocY[i2] = 0;
                this.poleLocX[i2] = this.screenX + (this.gapRightLeftPoles * i2);
                while (true) {
                    double rand = this.random.nextDouble();
                    int[] iArr = this.poleLocY;
                    iArr[i2] = (int) (((double) iArr[i2]) + (((double) this.screenY) * (rand - 0.5d)));
                    this.bottomPoleLocY = (this.poleLocY[i2] - (this.gapUpDownPoles / 2)) - this.bottomPole.getHeight();
                    if (this.poleLocY[i2] <= this.upperScreenPadding && this.bottomPoleLocY >= this.bottomScreenPadding) {
                        break;
                    }
                }
            }
            if (Gdx.input.justTouched()) {
                this.gameState = 2;
            }
        } else if (this.gameState == 2) {
            if (this.birdY > 0) {
                this.birdY -= this.gravity;
            }
            if (Gdx.input.justTouched()) {
                this.birdY += this.bumpUpSpeed;
            }
            for (int i3 = 0; i3 < this.numOfObjects; i3++) {
                int[] iArr2 = this.poleLocX;
                iArr2[i3] = iArr2[i3] - this.goingLeftSpeed;
                if (this.poleLocX[i3] < (-this.upperPole.getWidth())) {
                    int[] iArr3 = this.poleLocX;
                    iArr3[i3] = iArr3[i3] + (this.gapRightLeftPoles * this.numOfObjects);
                    while (true) {
                        double rand2 = this.random.nextDouble();
                        int[] iArr4 = this.poleLocY;
                        iArr4[i3] = (int) (((double) iArr4[i3]) + (((double) this.screenY) * (rand2 - 0.5d)));
                        if (this.poleLocY[i3] <= this.upperScreenPadding && (this.poleLocY[i3] - (this.gapUpDownPoles / 2)) - this.bottomPole.getHeight() >= this.bottomScreenPadding) {
                            break;
                        }
                    }
                }
            }
            for (int i4 = 0; i4 < this.numOfObjects; i4++) {
                if (Intersector.overlaps(this.birdCircle, this.bottomPoleRectangle[i4]) || Intersector.overlaps(this.birdCircle, this.upperPoleRectangle[i4])) {
                    this.gameState = 3;
                }
            }
        } else if (this.gameState == 3) {
            if (Gdx.input.justTouched()) {
                this.gameState = 1;
            }
        } else if (this.gameState == 4 && Gdx.input.justTouched()) {
            this.gameState = 1;
        }
        for (int i5 = 0; i5 < this.numOfObjects; i5++) {
            if (this.poleLocX[this.scoringPole] < this.midScreenX) {
                this.scoringPole++;
                this.score++;
                if (this.scoringPole == this.numOfObjects) {
                    this.scoringPole = 0;
                }
            }
        }
        drawBird();
        this.birdCircle.setPosition((float) (this.birdX + (this.bird1.getWidth() / 2)), (float) (this.birdY + (this.bird1.getHeight() / 2)));
        while (true) {
            int i6 = i;
            if (i6 >= this.numOfObjects) {
                break;
            }
            this.bottomPoleLocY = (this.poleLocY[i6] - (this.gapUpDownPoles / 2)) - this.bottomPole.getHeight();
            this.batch.draw(this.upperPole, (float) this.poleLocX[i6], (float) this.poleLocY[i6]);
            this.batch.draw(this.bottomPole, (float) this.poleLocX[i6], (float) this.bottomPoleLocY);
            this.upperPoleRectangle[i6].setPosition((float) this.poleLocX[i6], (float) this.poleLocY[i6]);
            this.bottomPoleRectangle[i6].setPosition((float) this.poleLocX[i6], (float) this.bottomPoleLocY);
            i = i6 + 1;
        }
        this.scoringFont.draw((Batch) this.batch, (CharSequence) "Score: " + String.valueOf(this.score) + "   Level: " + String.valueOf(this.currentLevel), 80.0f, 150.0f);
        if (this.gameState == 3) {
            this.gameOverFont.draw((Batch) this.batch, (CharSequence) "Game Over!!", 80.0f, (float) (this.midScreenY + 100));
        }
        if (this.gameState == 1) {
            this.gameOverFont.draw((Batch) this.batch, (CharSequence) "Tap To Start!", 80.0f, (float) (this.midScreenY + 100));
        }
        if (this.gameState == 4) {
            this.gameOverFont.draw((Batch) this.batch, (CharSequence) "Game Finished!", 80.0f, (float) (this.midScreenY + 100));
        }
        this.batch.end();
        if (this.ticks2 % this.nextLevelParameter == 0) {
            this.currentLevel++;
            this.goingLeftSpeed++;
            this.gravity = (int) (((double) this.gravity) + 0.5d);
        }
    }

    public void dispose() {
        this.batch.dispose();
        this.bird1.dispose();
        this.bird2.dispose();
        this.upperPole.dispose();
        this.bottomPole.dispose();
        this.background.dispose();
    }
}
