package com.taniajoseph.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bg;
	Texture[] birds;
	Texture toptube;
	Texture bottube;
//	ShapeRenderer shapeRenderer;
	Texture go;


	int flapState = 0;
	float birdY;
	float vel = 0;
	int score = 0;
	int scoreTube = 0;
	BitmapFont font;


	int gameState = 0;
	float gap = 400;
	float max;
	Random rand;
	float tubeVel = 4;
	int num = 4;

	float[] tubeX = new float[num] ;
	float[] tubeOffSet = new float[num];

	float dist;

	Circle birdCircle;
	Rectangle[] rectangle1;
	Rectangle[] rectangle2;



	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Texture("bg.png");
		birds = new Texture[2];
	//	shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		rectangle1 = new Rectangle[4];
        rectangle2 = new Rectangle[4];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(7 );

        go= new Texture("go.jpg");


		toptube = new Texture("toptube.png");
		bottube = new Texture("bottomtube.png");


		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		max = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		rand = new Random();

		dist = Gdx.graphics.getWidth()*3/4;

		StartGame();

	}

	public  void StartGame (){
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;


		for(int i = 0; i<num; i++){
			tubeOffSet[i] = (rand.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + Gdx.graphics.getWidth() + i*dist ;

			rectangle2[i] = new Rectangle();
			rectangle1[i] = new Rectangle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if(tubeX[scoreTube] <Gdx.graphics.getWidth()/2) {
				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if (scoreTube < 3) {
					scoreTube++;
				} else {
					scoreTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {

				vel = -20;

			}

			for(int i = 0; i<num; i++) {

				rectangle1[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(), toptube.getHeight());
				rectangle2[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottube.getHeight() + tubeOffSet[i], bottube.getWidth(), bottube.getHeight());


				if (tubeX[i] < -toptube.getWidth()) {

					tubeX[i] = num*dist - 200;
					tubeOffSet[i] = (rand.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);


				}else {

					tubeX[i] = tubeX[i] - tubeVel;

				}
					batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]);
					batch.draw(bottube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottube.getHeight() + tubeOffSet[i]);


			}


			if(birdY>0 ) {
				vel++;
				birdY -= vel;
			} else {
				gameState = 2;
			}


		} else if (gameState == 0){

			if (Gdx.input.justTouched()) {

				gameState = 1;
			}
		}
		else if (gameState==2){


			batch.draw(go, Gdx.graphics.getWidth()/2 - go.getWidth()/2, Gdx.graphics.getHeight()/2 - go.getHeight()/2);

			if (Gdx.input.justTouched()){
				gameState = 1;
				StartGame();
				score = 0;
				scoreTube = 0;
				vel = 0;

			}

		}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

			int x = Gdx.graphics.getWidth() / 2;

			batch.draw(birds[flapState], x - birds[flapState].getWidth() / 2, birdY);

		    font.draw(batch, "Score: " + String.valueOf(score),100, 150);

		    birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);


//		    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//			shapeRenderer.setColor(Color.RED);
//			shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

			for(int i =0; i<4;i++) {
//				shapeRenderer.rect(rectangle1[i].x, rectangle1[i].y, rectangle1[i].width, rectangle1[i].height);
//				shapeRenderer.rect(rectangle2[i].x, rectangle2[i].y, rectangle2[i].width, rectangle2[i].height);

				if(Intersector.overlaps(birdCircle, rectangle2[i]) || Intersector.overlaps(birdCircle, rectangle1[i])){

					Gdx.app.log("Collision", "yes!!");

					gameState = 2;
				}

			}

		//	shapeRenderer.end();


		batch.end();

		}
	}


