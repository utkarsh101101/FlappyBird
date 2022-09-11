package com.utkarshanand.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] bird;

	float birdY = 0;
	int flapState = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 2;
	int score = 0;
	int scoringTube = 0;
	Texture bottomTube;
	Texture topTube;
	Texture gameOver;
	float gap = 890;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 5;
	int noOfTubes = 4;
	float[] tubeOffset = new float[noOfTubes];
	float[] tubeX = new float[noOfTubes];
	float distanceBetweenTubes;
	BitmapFont font;
	Circle birdCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		bird = new Texture[2];
		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");
		randomGenerator = new Random();
		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangles = new Rectangle[noOfTubes];
		bottomTubeRectangles = new Rectangle[noOfTubes];
		startGame();

	}

	public void startGame() {
		birdY = Gdx.graphics.getHeight() / 2 - bird[flapState].getHeight() / 2;

		for(int i = 0; i < noOfTubes; i++){
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()- gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {
			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;
				Gdx.app.log("score", String.valueOf(score));
				if(scoringTube < noOfTubes -1)
					scoringTube++;
				else
					scoringTube = 0;
			}
			if(Gdx.input.justTouched()){
				velocity = -30;
			}
			for(int i = 0; i<noOfTubes; i++) {
				if(tubeX[i]<-topTube.getWidth()){
					tubeX[i] =+ noOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}
				else{
					tubeX[i] -= tubeVelocity;
				}
				tubeX[i] -= tubeVelocity;
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}
			if(birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			}
			else
				gameState = 2;
			if (flapState == 0)
				flapState = 1;
			else
				flapState = 0;
		}
		else if (gameState==0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if(gameState==2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			if(Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}

		batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + bird[flapState].getHeight()/2, bird[flapState].getWidth()/2);

		for(int i = 0; i < noOfTubes; i++){
			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
				gameState = 2;
			}
		}
	}
}
