package com.jagtar.fishing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic extends SurfaceView implements Runnable {
	private Thread gameThread = null;
	private volatile boolean gameIsRunning;
	private Canvas canvas;
	private Paint paintbrush;
	private SurfaceHolder holder;
	private int screenWidth;
	private int screenHeight;
	private static int initalPinXpos;

	double MOUSETAP_X = 100;
	double MOUSETAP_Y = 700;
	double SET_TIME_HERE = 10.0;

	GameBackground movingbg, bgonly, outofwater, pin, sky;
	Sounds sound;

	//list of targets in game
	List<Fish_Sprite> target_fishes;
	//List of catched Fishes
	List<Fish_Sprite> catched_fishes;

	public GameLogic(Context context, int screenW, int screenH) {
		super(context);
		this.holder = this.getHolder();
		this.paintbrush = new Paint();

		this.screenWidth = screenW;
		this.screenHeight = screenH;
		sound = new Sounds(getContext());
		this.movingbg = new GameBackground(this.getContext(), 0, 0, R.drawable.gamebackgroundlight);
		this.bgonly = new GameBackground(this.getContext(), 0, 0, R.drawable.bgonly);
		this.outofwater = new GameBackground(this.getContext(), 0, 0, R.drawable.boatbackground);
		this.pin = new GameBackground(this.getContext(), ((screenWidth / 2) - 70), 680, R.drawable.pin32);
		this.sky = new GameBackground(this.getContext(), 0, -screenHeight, R.drawable.back);
		initalPinXpos = this.pin.getxPosition();
		huds.setFishString("" + Math.round(SET_TIME_HERE * 10));
		this.target_fishes = new ArrayList<Fish_Sprite>();
		this.catched_fishes = new ArrayList<Fish_Sprite>();
		sound.getbgMusic();
	}

	@Override
	public void run() {
		while (gameIsRunning == true) {
			steps();
			drawsteps();
			controlFPS();
		}
	}

	boolean bgMovingDown = false;
	boolean bgMovingUp = false;
	boolean targetMovingDown = false;
	boolean targetMovingUp = false;
	boolean drawbgDown = false;
	boolean drawbgUp = false;
	boolean usertapped = false;
	double fishingstring = SET_TIME_HERE;
	double timetofish = SET_TIME_HERE;
	double showStringLength = SET_TIME_HERE;
	int movingSpeed = 25;
	double stringleft = 0.0;
	boolean pindown = false;
	boolean pinup = false;
	double oldTime = System.currentTimeMillis();
	double newTime = 0.0;
	int ccc = 0;
	int total_Target_Count = 0;
	boolean itsTime = false;
	boolean completedown = false;
	Huds huds = new Huds();
	boolean completeset = false;
	boolean hasReachedup = false;
	int shootingTime;
	int golden_cnt = 0;

	public void steps() {

		//movement of the targets in the game
		if (itsTime) {
			for (int i = 0; i < target_fishes.size(); i++) {
				//method is decalred below takes object of Fish_Sprite
				move_target_animals(target_fishes.get(i));
				chatchTheFish(target_fishes.get(i));
			}
		}

		/*if fishing string is finished, move the background in opposite direction*/
		//this condition only works if hook don't collide with fish while going down
		if (fishingstring <= 0 && bgMovingUp) {
			bgMovingUp = false;
			bgMovingDown = true;
			targetMovingDown = true;
			targetMovingUp = false;
			drawbgDown = true;
			fishingstring = 0.0;
			outofwater.setyPosition(-900);
			oldTime = System.currentTimeMillis();
		}
		/************************************/

		//setting the area for fish shooting
		if (hasReachedup) {
			if ((outofwater.getyPosition() + 900) <= screenHeight - 300) {
				outofwater.setyPosition(outofwater.getyPosition() + movingSpeed);
				sky.setyPosition(sky.getyPosition() + movingSpeed + 20);
				pindown = false;
				pinup = false;
				pin.setyPosition(outofwater.getyPosition() + 700);
				pin.updateHitbox();
				completedown = true;
//				if (pin.getyPosition() != (getHeight() - 250)) {
//					pin.setyPosition(pin.getyPosition() + movingSpeed);
//					pin.updateHitbox();
//
//				}
			}
		}

		// displaying catched fishes at random location to shoot
		if (completedown && completeset == false) {
			int x;
			int y = 175;
			for (int i = 0; i < catched_fishes.size(); i++) {
				x = rX_Pos_Gen();
				y = y + (6*(i+1));
				catched_fishes.get(i).setxPosition(x);
				catched_fishes.get(i).setyPosition(y);
				catched_fishes.get(i).updateHitbox();
			}
			completeset = true;
			shootingTime = (int) System.currentTimeMillis();
		}

		int curtime = (int) System.currentTimeMillis();
		if (((curtime - shootingTime) >= 7000) && ((curtime - shootingTime) <= 7050)) {
			if (completeset) {
				Log.d("Jarvis", "Hello " + (curtime - shootingTime));
				this.sky = new GameBackground(this.getContext(), 0, -screenHeight, R.drawable.back);
				this.outofwater = new GameBackground(this.getContext(), 0, 0, R.drawable.boatbackground);
				this.pin = new GameBackground(this.getContext(), ((screenWidth / 2) - 70), 680, R.drawable.pin32);
				target_fishes.clear();
				catched_fishes.clear();
				total_Target_Count = 0;
				// shootingTime = curtime;
				completeset = false;
				hasReachedup = false;
				usertapped = false;
			}
		}


		Log.d("fishing", fishingstring + "");
		//background moving down
		if (bgMovingDown == true) {
			if (fishingstring <= timetofish) {
				newTime = System.currentTimeMillis();
				if ((newTime - oldTime) >= 200.0) {
					huds.setFishString("" + Math.round(showStringLength * 10.0));
					fishingstring += 0.2;
					showStringLength += 0.2;
					huds.setFishString("" + Math.round(showStringLength * 10.0));
					Log.d("fishingtime", +(fishingstring) * 100.0 / 100.0 + "");
					oldTime = newTime;
				}
//                fishingstring += 10;
				movingbg.setyPosition((movingbg.getyPosition() + movingSpeed));
				//Grabbing back the fishing string
			}


			/*IF TOP IS NEAR THEN MOVE BOATBACKGROUND BACK AT TIS PLACE*/
			if ((fishingstring >= (stringleft))) {
				Log.d("timetofish", "fishingstring:" + fishingstring + "stringleft: " + stringleft);
				if ((outofwater.getyPosition() + 900) <= 900) {
					outofwater.setyPosition(outofwater.getyPosition() + movingSpeed);
				}
			}


			if (fishingstring >= timetofish) {

				bgMovingUp = false;
				bgMovingDown = false;
				targetMovingDown = false;
				targetMovingUp = false;
				pinup = true;
//                usertapped = false;
				this.pin.setxPosition(initalPinXpos);
				hasReachedup = true;
			}
			/****************************************************************/
			if (movingbg.getyPosition() > screenHeight) {
				movingbg.setyPosition(0);
			}
		}

		if (bgMovingUp == true) {
			//Throwing fishing string
			newTime = System.currentTimeMillis();
			if ((newTime - oldTime) >= 200.0) {
				huds.setFishString("" + Math.round(showStringLength * 10.0));
				fishingstring -= 0.2;
				showStringLength -= 0.2;
				huds.setFishString("" + Math.round(showStringLength * 10.0));
				oldTime = newTime;
			}

			//moving the background
			movingbg.setyPosition((movingbg.getyPosition() - movingSpeed));
			//moving boatbackground up
			if ((outofwater.getyPosition() + 900) >= 0) {
				outofwater.setyPosition(outofwater.getyPosition() - movingSpeed);
				Log.d("timetofish", "value of fishingstring" + (fishingstring) + "");
				stringleft = fishingstring;
				Log.d("stleft", "decrease this much: " + (stringleft) + "");
			}
			if ((outofwater.getyPosition() + 900) <= 0) {
				itsTime = true;
			}

			//resetting background when it reaches top
			if ((movingbg.getyPosition() + screenHeight) < 0) {
				movingbg.setyPosition(0);
				ccc = 1;
			}
		}

		/*********FISHING PIN MOVEMENT********/
		if (pin.getyPosition() >= (screenHeight / 2) - 200) {
			pindown = false;
		}
		if (pin.getyPosition() <= 680) {
			pinup = false;
		}
		if (pindown) {
			pin.setyPosition(pin.getyPosition() + 10);
			pin.updateHitbox();
		}
		if (pinup) {
			pin.setyPosition(pin.getyPosition() - 10);
			pin.updateHitbox();
		}
		/*********END FISHING PIN MOVEMENT********/

		/**************CATCHED FISH MOVEMENT*************/
		if (completedown == false) {
			for (int i = 0; i < catched_fishes.size(); i++) {
				catched_fishes.get(i).setxPosition(pin.getxPosition());
				catched_fishes.get(i).setyPosition(pin.getyPosition() + 80);
				catched_fishes.get(i).updateHitbox();
			}
		}
		/**************END CATCHED FISH MOVEMENT*************/


	}

	public void move_target_animals(Fish_Sprite fs) {

		if (fs.getxPosition() > (screenWidth - fs.getImage().getWidth())) {
			//fish.setxPosition(fish.getxPosition() + 20);
			fs.setMoving_left(false);
		} else if (fs.getxPosition() < 0) {
			fs.setMoving_left(true);
		}

		//Fish movement along X axis
		if (fs.isMoving_left() == true) {
			fs.setxPosition(fs.getxPosition() + 10);
		} else {
			fs.setxPosition(fs.getxPosition() - 10);
		}

		//Fish movement along Y axis
		if (targetMovingUp == true) {
			fs.setyPosition(fs.getyPosition() - movingSpeed);
		}
		if (targetMovingDown == true) {
			fs.setyPosition(fs.getyPosition() + movingSpeed);
		}

		// moving hitbox of all fishes
		fs.updateHitbox();
	}

	/*method to check the collision between hook and fish and then
	  removing fish from target and adding to catched*/
	public void chatchTheFish(Fish_Sprite whichfish) {
		if (pin.getHitbox().intersect(whichfish.getHitbox())) {
			sound.getHookCollision();
			if (bgMovingUp) // if colliding with fish while going down
			{
				//manipulating the time to fish and stringleft accordingly
				timetofish = timetofish - fishingstring;
				stringleft = (stringleft - fishingstring);
				Log.d("timetofish", timetofish + "");
				Log.d("timetofish", "string left " + (stringleft) + "");
				bgMovingUp = false;
				bgMovingDown = true;
				targetMovingDown = true;
				targetMovingUp = false;
				drawbgDown = true;
				showStringLength = fishingstring;
				fishingstring = 0.0;
				outofwater.setyPosition(-900);
//                time = (int) System.currentTimeMillis();
			}
			catched_fishes.add(whichfish);
			target_fishes.remove(whichfish);
			huds.setScore(huds.getScore() + 1);
		}
		Log.d("catched", catched_fishes.size() + "");

	}

	long currentTime = 0;
	long previousTime = 0;

	public void drawsteps() {
		if (holder.getSurface().isValid()) {
			canvas = holder.lockCanvas();
			paintbrush.setStyle(Paint.Style.FILL);
			paintbrush.setStrokeWidth(8);
			//flat background
			Rect bgg = new Rect(0, 0, screenWidth, screenHeight);
			canvas.drawBitmap(bgonly.getImage(), null, bgg, null);

			//moving background
			Rect rr = new Rect(0, movingbg.getyPosition(), screenWidth, screenHeight + movingbg.getyPosition());
			canvas.drawBitmap(movingbg.getImage(), null, rr, null);

			if ((movingbg.getyPosition() > 0) && drawbgDown) {
				Rect spacecover = new Rect(0, (movingbg.getyPosition() - (screenHeight)), screenWidth, (screenHeight + movingbg.getyPosition()) - screenHeight);
				canvas.drawBitmap(movingbg.getImage(), null, spacecover, null);
			}
			if (((movingbg.getyPosition() + screenHeight) < screenHeight) && drawbgUp) {
				Rect spacecover2 = new Rect(0, (movingbg.getyPosition() + (screenHeight)), screenWidth, (screenHeight + movingbg.getyPosition()) + screenHeight);
				canvas.drawBitmap(movingbg.getImage(), null, spacecover2, null);
			}

			Paint p = new Paint();
			for (int i = 0; i < target_fishes.size(); i++) {
				canvas.drawBitmap(target_fishes.get(i).getImage(), target_fishes.get(i).getxPosition(), target_fishes.get(i).getyPosition(), null);
				// Drawing the hitbox arround the target fishes
				p.setColor(Color.RED);
				p.setStyle(Paint.Style.STROKE);
				canvas.drawRect(target_fishes.get(i).getHitbox(), p);
			}

			//Sky Area
			Rect skyup = new Rect(0, sky.getyPosition() - 1500, screenWidth, outofwater.getyPosition() + 10);
			canvas.drawBitmap(sky.getImage(), null, skyup, null);

			//boat background
			Rect waterout = new Rect(0, outofwater.getyPosition(), screenWidth, outofwater.getyPosition() + 900);
			canvas.drawBitmap(outofwater.getImage(), null, waterout, null);


			// spawning of sprites after time interval
			currentTime = System.currentTimeMillis();
			if ((currentTime - previousTime) > 500) {
				System.out.println("tapout done");

				//Draw target sprites
				if (itsTime == true) {
					if (total_Target_Count < 20) {
						int rx = rX_Pos_Gen();
						int ry = screenHeight - 20;
						Fish_Sprite target = null;
						Random r = new Random();
						int sprite_Index = r.nextInt(5);

						if (sprite_Index == 0) {
							target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.fish, "Fish");
						}

						if (sprite_Index == 1) {
							target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.octopus, "Octopus");
						}

						if (sprite_Index == 2) {
							target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.seahorse, "Sea Horse");
						}

						if (sprite_Index == 3) {
							target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.jellyfish, "Jellyfish");
						}

						if (sprite_Index == 4) {
							golden_cnt++;
							if (golden_cnt == 1) {
								target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.goldfish, "Goldfish");
							}

						}

						if (target != null) {

							//Generating hitboxes for all targets
							Rect target_hitbox = new Rect(target.getxPosition(), target.getxPosition(), target.getImage().getWidth(), target.getImage().getHeight());
							target.setHitbox(target_hitbox);
							target_fishes.add(target);
						}

						total_Target_Count++;
					}
				}
				previousTime = currentTime;
			}

			//Starting instruction for the game
			if (usertapped == false) {
				String todis = "TAP TO START!";
				p.setStyle(Paint.Style.FILL_AND_STROKE);
				p.setTextAlign(Paint.Align.CENTER);
				p.setFakeBoldText(true);
				p.setColor(Color.BLACK);
				p.setTextSize(80);
				canvas.drawText(todis, (screenWidth / 2), (screenHeight - 500), p);
			}


			if (usertapped) {


				for (int i = 0; i < catched_fishes.size(); i++) {
					if ((MOUSETAP_X >= catched_fishes.get(i).getxPosition()) &&
							(MOUSETAP_X <= (catched_fishes.get(i).getxPosition() + catched_fishes.get(i).getImage().getWidth())) &&
							(MOUSETAP_Y >= catched_fishes.get(i).getyPosition()) &&
							(MOUSETAP_Y <= (catched_fishes.get(i).getyPosition() + catched_fishes.get(i).getImage().getHeight()))
					) {
						sound.getShoot();
						Log.d("Hit", "Hit hard");
						//Normal Fish == 5pts increased
						if (catched_fishes.get(i).getI_name() == "Fish") {
							System.out.println("Score Update");
							huds.setScore(huds.getScore() + 5);
						}
						//Octopus == 15pts increased
						else if (catched_fishes.get(i).getI_name() == "Octopus") {
							System.out.println("Score Update");
							huds.setScore(huds.getScore() + 15);
						}
						//Sea Horse == 20pts increased
						else if (catched_fishes.get(i).getI_name() == "Sea Horse") {
							huds.setScore(huds.getScore() + 20);
							System.out.println("Score Update");
						}
						//Jellyfish == 25pts decreased
						else if (catched_fishes.get(i).getI_name() == "Jellyfish") {
							huds.setScore(huds.getScore() - 25);
							if (huds.getScore() <= 0) {
								huds.setScore(0);
							}
							System.out.println("Score Update");
						}
						//Goldfish == 100pts increased
						else if (catched_fishes.get(i).getI_name() == "Goldfish") {
							huds.setScore(huds.getScore() + 100);
							System.out.println("Score Update");
						}
						catched_fishes.remove(i);
					}
				}
			}

			//HUDS Plate
			p.setStyle(Paint.Style.FILL);
			// argb == alpha, red, green, blue where alpha is used for transparency
			p.setColor(Color.argb(200, 86, 101, 115));

			// RectF is a function to draw rounded rectangle
			RectF hudBg = new RectF(
					10,
					20,
					this.screenWidth - 10,
					150
			);
			this.canvas.drawRoundRect(hudBg, 30, 30, p);

			// Text style
			p.setStyle(Paint.Style.FILL_AND_STROKE);
			// Text Color
			p.setColor(Color.BLACK);
			// Text Size
			p.setTextSize(75);
			// Text transparency
			p.setAlpha(200);
			//Alignment
			p.setTextAlign(Paint.Align.LEFT);
			// Displaying Lives Status
			this.canvas.drawText(("Pts.: " + huds.getScore()), hudBg.left + 20, hudBg.bottom - 30, p);
			// Calculating Score Text x position
			int scoreX = (int) (hudBg.left + ((hudBg.right - hudBg.left) / 2));
			//Alignment
			p.setTextAlign(Paint.Align.LEFT);
			// Displaying Lives Status
			this.canvas.drawText(("Time: " + huds.getFishString()), hudBg.right - 600, hudBg.bottom - 30, p);


			//drawing pin
			canvas.drawBitmap(pin.getImage(), pin.getxPosition(), pin.getyPosition(), null);
//            Rect r = new Rect(pin.getxPosition(), pin.getyPosition(), pin.getxPosition() + pin.getImageWidth(), pin.getyPosition() + pin.getImageHeight());
//            pin.setHitbox(r);
//            pin.getHitbox();
			pin.setHitbox(pin.getHitbox());
			p.setColor(Color.RED);
			p.setStyle(Paint.Style.STROKE);
			canvas.drawRect(pin.getHitbox(), p);

			//drawing catched fishes
			for (int i = 0; i < catched_fishes.size(); i++) {
				canvas.drawBitmap(catched_fishes.get(i).getImage(), catched_fishes.get(i).getxPosition(), catched_fishes.get(i).getyPosition(), null);
				// Drawing the hitbox arround the target fishes
				p.setColor(Color.RED);
				p.setStyle(Paint.Style.STROKE);
				canvas.drawRect(catched_fishes.get(i).getHitbox(), p);
			}

			holder.unlockCanvasAndPost(canvas);
		}
	}

	public int rX_Pos_Gen() {
		Random r_num = new Random();
		int pos = r_num.nextInt(screenWidth - 32);
		return pos;
	}

	public int rY_Pos_Gen() {
		int maximum = getHeight() / 2;
		int minimum = 200;
		Random rn = new Random();
		int n = maximum - minimum + 1;
		int i = rn.nextInt() % n;
		int randomNum = minimum + i;

//        Random r_num = new Random();
//        int pos = r_num.nextInt(200, getHeight() / 2);
		return randomNum;
	}

	public void controlFPS() {
		try {
			gameThread.sleep(17);
		} catch (InterruptedException e) {
		}
	}

	double mouseXm[] = new double[50];
	double mouseYm[] = new double[50];

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int userAction = event.getActionMasked();
		if (userAction == MotionEvent.ACTION_DOWN) {
			MOUSETAP_X = event.getX();
			MOUSETAP_Y = event.getY();

			//moving bg on tap
			if (usertapped == false) {
				sound.getHookStart();
				bgMovingUp = true;
				fishingstring = SET_TIME_HERE;
				timetofish = SET_TIME_HERE;
				showStringLength = SET_TIME_HERE;
				pindown = false;
				stringleft = 0.0;
				pinup = false;
				targetMovingUp = true;
				drawbgUp = true;
				usertapped = true;
				completedown = false;
				huds = new Huds();
				hasReachedup = false;

				completeset = false;
				//moving pin
				pindown = true;
				Log.d("screensize", screenWidth + "");
				Log.d("screensize", screenHeight + "");
			}


		} else if (userAction == MotionEvent.ACTION_UP) {

		} else if (userAction == MotionEvent.ACTION_MOVE) {
			int tapX = (int) event.getX();
			if(tapX <= (getWidth() - pin.getImage().getWidth())) {
				pin.setxPosition(tapX);
				pin.updateHitbox();
			}
		}
		return true;
	}

	public void pauseGame() {
		gameIsRunning = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
		}
	}

	public void resumeGame() {
		gameIsRunning = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

}