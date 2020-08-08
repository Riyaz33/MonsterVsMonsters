package sample;

import com.sun.javafx.event.CompositeEventHandler;
import javafx.event.Event;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.chrono.MinguoChronology;
import java.util.Random;


// fire -800, monsters -400
public class MiniGame extends Application {
    static final int W_WIDTH = 1280;
    static final int W_HEIGHT = 598;
    private static final int TEMP_ENEMIES = 21; //

    private static final int LEVEL_ONE_MONSTERSPEED = 2; //  change to 1
    private static final int LEVEL_TWO_MONSTERSPEED = 5; //  change to x
    private static final int LEVEL_THREE_MONSTERSPEED = 8; //  change to y

    private static final int LEVEL_ONE_FIRELENGTH_L = 420;
    private static final int LEVEL_ONE_FIRELENGTH_R = 860;
    private static final int LEVEL_TWO_FIRELENGTH_L = 500;
    private static final int LEVEL_TWO_FIRELENGTH_R = 780;
    private static final int LEVEL_THREE_FIRELENGTH_L = 540;
    private static final int LEVEL_THREE_FIRELENGTH_R = 740;


    public static int lives = 6;
    public static int score = 0;
    public static int enemies = 21; /*21 */ //
    public static int monsterRightIndex = 0;
    public static int monsterLeftIndex = 0;
    public  static boolean disableShoot = false;
    public static int currLevel = 1;
    public static int monsterSpeed;
    public static int fireLengthL;
    public static int fireLengthR;
    public static double currYPlus;
    public static boolean paused = false;

    public Image getMonster(){
        return new Image("/resources/img/enemy.png");
    }

    @Override
    public void start(Stage stage) throws Exception {


        stage.setResizable(false);

            Group rootIntro = new Group();
            Scene introScene = new Scene(rootIntro , W_WIDTH, W_HEIGHT);

            Canvas canvas = new Canvas(W_WIDTH,W_HEIGHT);
            rootIntro.getChildren().add(canvas);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(new Image("/resources/img/background2.png",W_WIDTH, W_WIDTH, true,true),0,0);

            Image logo = new Image("/resources/img/logo.png");
            int centerLogo = (W_WIDTH -  (int) logo.getWidth() ) / 2;

            gc.drawImage(logo, centerLogo ,10 );

            Label instructionText = new Label("CONTROLS" +
                    "\nUSE LEFT/RIGHT ARROWS TO FIRE " +
                    "\n" +
                    "\nPRESS ENTER TO START" +
                    "\nPRESS Q TO QUIT");
            InputStream istream = getClass().getResourceAsStream("/resources/SHOWG.ttf");
            Font gameFont = Font.loadFont(istream, 40);
            istream.close();
            instructionText.setFont(gameFont);
            instructionText.setTextFill(Color.WHITE);
            int centerInstructions = (W_WIDTH / 2) - 325;
            instructionText.setLayoutX(centerInstructions);
            instructionText.setLayoutY(W_HEIGHT / 2);
            instructionText.setTextAlignment(TextAlignment.CENTER);
            rootIntro.getChildren().add(instructionText);

            Label myMetadata = new Label("Riyaz Shaikh" +
                    "\n20616383");
            myMetadata.setFont(new Font("Helvetica", 20));
            myMetadata.setLayoutX(10);
            myMetadata.setLayoutY(10);
            rootIntro.getChildren().add(myMetadata);


            // scenes for each screen

            // level 1 scene
            Group rootLevelOne = new Group();
            Scene levelOneScene = new Scene(rootLevelOne, W_WIDTH,W_HEIGHT);
            Canvas canvasL1 = new Canvas(W_WIDTH,W_HEIGHT);
            rootLevelOne.getChildren().add(canvasL1);
            GraphicsContext gcL1 = canvasL1.getGraphicsContext2D();
            gcL1.drawImage(new Image("/resources/img/background2.png",W_WIDTH, W_WIDTH, true,true),0,0);

            InputStream istream2 = getClass().getResourceAsStream("/resources/SHOWG.ttf");
            Font gameFont2 = Font.loadFont(istream2, 30);
            istream2.close();

            Label livesRemaining = new Label("Lives remaining: "+ MiniGame.lives / 2);
            livesRemaining.setFont(gameFont2);
            livesRemaining.setLayoutY(10);
            livesRemaining.setLayoutX(8);
            livesRemaining.setTextFill(Color.GREEN);


            Label enemiesRemaining = new Label("Enemies remaining: " + MiniGame.enemies);
            enemiesRemaining.setFont(gameFont2);
            enemiesRemaining.setLayoutY(48);
            enemiesRemaining.setLayoutX(8);
            enemiesRemaining.setTextFill(Color.RED);

            Label score = new Label("Score: " + MiniGame.score);
            score.setFont(gameFont2);
            score.setLayoutY(86);
            score.setLayoutX(8);
            score.setTextFill(Color.WHITE);

            rootLevelOne.getChildren().add(livesRemaining);
            rootLevelOne.getChildren().add(enemiesRemaining);
            rootLevelOne.getChildren().add(score);


           ///game over scene
            Group gameOverGroup = new Group();
            Scene gameOverScene = new Scene(gameOverGroup, W_WIDTH,W_HEIGHT);

            ImageView endbg = new ImageView();
            endbg.setImage(new Image("/resources/img/background2.png",W_WIDTH, W_HEIGHT, true,true));
            gameOverGroup.getChildren().add(endbg);

        Rectangle gameOverBg = new Rectangle();
        gameOverBg.setHeight(350);
        gameOverBg.setWidth(700);
        gameOverBg.setX(W_WIDTH/2 - 700/2);
        gameOverBg.setY(W_HEIGHT/2 - 350/2);
        gameOverBg.setFill(Color.rgb(241,226,192));
        gameOverGroup.getChildren().add(gameOverBg);

        Rectangle gameOverDialog = new Rectangle();
        gameOverDialog.setHeight(300);
        gameOverDialog.setWidth(650);
        gameOverDialog.setX(W_WIDTH/2 - 650/2);
        gameOverDialog.setY(W_HEIGHT/2 - 300/2);
        gameOverDialog.setFill(Color.rgb(246,246,246));
        gameOverGroup.getChildren().add(gameOverDialog);

        Label gameOverText = new Label();
        gameOverText.setText("GAME OVER YOU LOSE\nPRESS ENTTER TO PLAY AGAIN\n" +
                "PRESS Q TO QUIT" +
                "\nFINAL SCORE: "+ MiniGame.score);
        gameOverText.setFont(gameFont);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setTextFill(Color.rgb(168,157,41));
        gameOverText.setLayoutX(W_WIDTH/2-550/2);
        gameOverText.setLayoutY(W_HEIGHT/2-220/2);
        gameOverGroup.getChildren().add(gameOverText);


        /// level advance scene
        Group levelAdvanceGroup = new Group();
        Scene levelAdvanceScene = new Scene(levelAdvanceGroup,W_WIDTH,W_HEIGHT);
        ImageView leveladvancebg = new ImageView();
        leveladvancebg.setImage(new Image("/resources/img/background2.png",W_WIDTH, W_WIDTH, true,true));
        levelAdvanceGroup.getChildren().add(leveladvancebg);

        Rectangle levelOverBg = new Rectangle();
        levelOverBg.setHeight(325);
        levelOverBg.setWidth(690);
        levelOverBg.setX(W_WIDTH/2 - 690/2);
        levelOverBg.setY(W_HEIGHT/2 - 325/2);
        levelOverBg.setFill(Color.rgb(241,226,192));
        levelAdvanceGroup.getChildren().add(levelOverBg);

        Rectangle levelOverDialog = new Rectangle();
        levelOverDialog.setHeight(275);
        levelOverDialog.setWidth(640);
        levelOverDialog.setX(W_WIDTH/2 - 640/2);
        levelOverDialog.setY(W_HEIGHT/2 - 275/2);
        levelOverDialog.setFill(Color.rgb(246,246,246));
        levelAdvanceGroup.getChildren().add(levelOverDialog);

        Label levelOverText = new Label();
        levelOverText.setText("LEVEL "+ (MiniGame.currLevel - 1) +" PASSED\n" +
                "PRESS ENTER TO CONTINUE\n" +
                "PRESS Q TO QUIT");
        levelOverText.setFont(gameFont);
        levelOverText.setTextFill(Color.rgb(168,157,41));
        levelOverText.setLayoutX(W_WIDTH/2 - 550/2);
        levelOverText.setLayoutY(W_HEIGHT/2 - 160/2);
        levelOverText.setTextAlignment(TextAlignment.CENTER);
        levelAdvanceGroup.getChildren().add(levelOverText);


        // GAME WIN SCENE
        Group gameWinGroup = new Group();
        Scene gameWinScene = new Scene(gameWinGroup, W_WIDTH,W_HEIGHT);

        ImageView endGameWin = new ImageView();
        endGameWin.setImage(new Image("/resources/img/background2.png",W_WIDTH, W_HEIGHT, true,true));
        gameWinGroup.getChildren().add(endGameWin);

        Rectangle gameWinBg = new Rectangle();
        gameWinBg.setHeight(350);
        gameWinBg.setWidth(700);
        gameWinBg.setX(W_WIDTH/2 - 700/2);
        gameWinBg.setY(W_HEIGHT/2 - 350/2);
        gameWinBg.setFill(Color.rgb(241,226,192));
        gameWinGroup.getChildren().add(gameWinBg);

        Rectangle gameWinDialog = new Rectangle();
        gameWinDialog.setHeight(300);
        gameWinDialog.setWidth(650);
        gameWinDialog.setX(W_WIDTH/2 - 650/2);
        gameWinDialog.setY(W_HEIGHT/2 - 300/2);
        gameWinDialog.setFill(Color.rgb(246,246,246));
        gameWinGroup.getChildren().add(gameWinDialog);

        Label gameWinText = new Label();
        gameWinText.setText("WINNER! GAME OVER\nPRESS ENTER TO PLAY AGAIN\n" +
                "PRESS Q TO QUIT" +
                "\nFINAL SCORE: "+ MiniGame.score);
        gameWinText.setFont(gameFont);
        gameWinText.setTextAlignment(TextAlignment.CENTER);
        gameWinText.setTextFill(Color.rgb(168,157,41));
        gameWinText.setLayoutX(W_WIDTH/2-605/2);
        gameWinText.setLayoutY(W_HEIGHT/2-160/2);
        gameWinGroup.getChildren().add(gameWinText);

        ////////////






            // quit  handler
            EventHandler<KeyEvent> quitHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode() == KeyCode.Q) {
                        System.out.println("Thanks for playing!");
                        Platform.exit();
                        System.exit(0);
                    }
                }
            };



    /////////////////////

            Image idle = new Image("/resources/img/fire.png");

            ImageView idleImgView = new ImageView();
            idleImgView.setImage(idle);
            idleImgView.setX((W_WIDTH - idle.getWidth() )/2);
            idleImgView.setY(W_HEIGHT-idle.getHeight() - 25);

            int totalMonsters = 10;
            ImageView[] monstersLeft = new ImageView[totalMonsters];
            for(int i = 0; i < totalMonsters; ++i ){
                monstersLeft[i] = new ImageView(getMonster());
                monstersLeft[i].setY(W_HEIGHT-idle.getHeight() - 10);
                monstersLeft[i].setX(getMonster().getWidth() * -1 - 10);

            }

            ImageView[] monstersRight = new ImageView[totalMonsters + 1];
            for(int i = 0; i < totalMonsters + 1; ++i ){
                monstersRight[i] = new ImageView(getMonster());
                monstersRight[i].setY(W_HEIGHT-idle.getHeight() - 10);
                //    monstersRight[i].setX(W_WIDTH  - getMonster().getWidth() - 25);
                monstersRight[i].setX(W_WIDTH + 10);
            }

            AnimationTimer spawnMonstersTimer = new AnimationTimer() {
                double monsterXRight = W_WIDTH - getMonster().getWidth() - 25;
                private long lastUpdate = 0;
                @Override
                public void handle(long l) {
                    Random rand = new Random();
                    if (l - lastUpdate >= 1147483647) {
                        lastUpdate = l;
                        int flipCoin = rand.nextInt(2);
                        if (flipCoin == 0) {
                            if (MiniGame.monsterRightIndex < 11 /* 11*/) { //
                                if(!rootLevelOne.getChildren().contains(monstersRight[MiniGame.monsterRightIndex])){
                                    rootLevelOne.getChildren().add(monstersRight[MiniGame.monsterRightIndex]);
                                } else {
                                    monstersRight[MiniGame.monsterRightIndex].setY(W_HEIGHT-idle.getHeight() - 10);
                                    monstersRight[MiniGame.monsterRightIndex].setX(W_WIDTH + 10);
                                }
                                MiniGame.monsterRightIndex++;
                                System.out.println("Spawn right");
                            }
                        } else if (flipCoin != 0) {
                            if (MiniGame.monsterLeftIndex < 10 /*10*/) {//
                                //flip image
                                monstersLeft[MiniGame.monsterLeftIndex].setScaleX(-1);
                                if(!rootLevelOne.getChildren().contains(monstersLeft[MiniGame.monsterLeftIndex])){
                                    rootLevelOne.getChildren().add(monstersLeft[MiniGame.monsterLeftIndex]);
                                } else {
                                    monstersLeft[MiniGame.monsterLeftIndex].setY(W_HEIGHT-idle.getHeight() - 10);
                                    monstersLeft[MiniGame.monsterLeftIndex].setX(getMonster().getWidth() * -1 - 10);
                                }
                                MiniGame.monsterLeftIndex++;
                                System.out.println("Spawn Left");
                            }

                        }
                    }

                    // move
                    for(int i = 0; i < MiniGame.monsterRightIndex; i++) {
                        double currX = monstersRight[i].getX();
                        monstersRight[i].setX(currX-=MiniGame.monsterSpeed); //
                    }
                    for(int i = 0; i < MiniGame.monsterLeftIndex; i++) {
                        double currX = monstersLeft[i].getX();
                        monstersLeft[i].setX(currX+=MiniGame.monsterSpeed);
                    }

                    // check player death
                    for(int i = 0; i < MiniGame.monsterRightIndex; i++){
                        if(checkBounds(monstersRight[i], false, MiniGame.paused)){
                           // System.out.println("hitting right");
                            MiniGame.lives--;
                            livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                        }
                    }
                    for(int i = 0; i < MiniGame.monsterLeftIndex; i++){
                        if(checkBounds(monstersLeft[i], true, MiniGame.paused)){
                            MiniGame.lives--;
                            livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                        }
                    }

                }

                private boolean myOverlapCheck(ImageView monster, ImageView idle, boolean isLeft){
                   if(monster.getBoundsInLocal().getMinY() < 0) return false;
                   //return false;
                   if(monster.getY() == MiniGame.currYPlus ) {
                       System.out.println("TTRUE ");
                       return false;
                   }
                    if(isLeft){
                       if(monster.getBoundsInLocal().getMaxX() > idle.getBoundsInLocal().getMinX() + 50){

                           return true;
                       }
                   } else {
                       if(monster.getBoundsInLocal().getMinX() < idle.getBoundsInLocal().getMaxX() - 50) {
                        return true;
                       }
                   }
                    return false;
                }

                private boolean checkBounds(ImageView monster, boolean isLeft, boolean paused) {
                  if(paused) return false;
                    //  if(monster.getBoundsInLocal().intersects(idleImgView.getBoundsInLocal())){
                    if(myOverlapCheck(monster,idleImgView,isLeft)){
                        System.out.println("HITTT");
                        if(MiniGame.lives < 1){
                            /// end game here
                            MiniGame.paused = true;
                            monster.setY(-400);
                            if(isLeft){
                                monster.setX(W_WIDTH + 10);

                            } else{
                                monster.setX(getMonster().getWidth() * -1 - 10);
                            }
                            MiniGame.disableShoot = true;

                            URL url = this.getClass().getResource("/resources/sound/fail.wav");
                            File failSoundFile = null;
                            try {
                                failSoundFile = new File(url.toURI());
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            Media failSoundMedia = new Media(failSoundFile.toURI().toString());
                            MediaPlayer mediaPlayer = new MediaPlayer(failSoundMedia);
                            mediaPlayer.play();

                            gameOverText.setText("GAME OVER - YOU LOSE\nPRESS ENTER TO PLAY AGAIN\n" +
                                    "PRESS Q TO QUIT" +
                                    "\nFINAL SCORE: "+ MiniGame.score);

                            gameOverScene.addEventHandler(KeyEvent.KEY_PRESSED,quitHandler);
                            stage.setScene(gameOverScene);



                            stop();

                            return true;
                        }

                        // step back animation
                       /* if(isLeft){
                            double currX = monster.getX();
                            currX -= 70;
                            monster.setX(currX);
                        } else {
                            double currX = monster.getX();
                            currX += 70;
                            monster.setX(currX);
                        }*/

                        final int[] move_count = {0};
                        //MiniGame.currYPlus = monster.getY() - 10;
                        AnimationTimer bounceBack = new AnimationTimer() {
                            double currX = monster.getX();
                            @Override
                            public void handle(long l) {
                               // monster.setY(MiniGame.currYPlus);
                                if(isLeft){
                                    handleLeft();

                                } else {
                                    handleRight();
                                }

                            }
                            int multiplier = 5;
                            private void handleRight() {
                               if(move_count[0] < 7) {
                                   if(MiniGame.currLevel == 1 ){
                                       currX += LEVEL_ONE_MONSTERSPEED*multiplier;
                                   } else if (MiniGame.currLevel == 2){
                                       currX += LEVEL_TWO_MONSTERSPEED*multiplier;

                                   } else if(MiniGame.currLevel == 3){
                                       currX += LEVEL_THREE_MONSTERSPEED*multiplier;
                                   }

                                   monster.setX(currX);
                                   move_count[0]++;
                               } else {
                                //   monster.setY(MiniGame.currYPlus+10);

                                   stop();
                               }
                            }

                            private void handleLeft() {
                                if(move_count[0] < 9) {
                                    if(MiniGame.currLevel == 1 ){
                                        currX -= LEVEL_ONE_MONSTERSPEED*multiplier;
                                    } else if (MiniGame.currLevel == 2){
                                        currX -= LEVEL_TWO_MONSTERSPEED*multiplier;

                                    } else if(MiniGame.currLevel == 3){
                                        currX -= LEVEL_THREE_MONSTERSPEED*multiplier;
                                    }
                                    monster.setX(currX);
                                    move_count[0]++;
                                } else {
                                 //   monster.setY(MiniGame.currYPlus+10);

                                    stop();
                                }
                            }
                        };
                        bounceBack.start();
                        return true;
                    }
                    return false;
                }
            };

        //ESC handle
        EventHandler<KeyEvent> escHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    for(int i = 0; i < MiniGame.monsterLeftIndex; ++i ){
                        monstersLeft[i].setY(W_HEIGHT-idle.getHeight() - 10);
                        monstersLeft[i].setX(getMonster().getWidth() * -1 - 10);

                    }

                    for(int i = 0; i < MiniGame.monsterRightIndex; ++i ){
                        monstersRight[i].setY(W_HEIGHT-idle.getHeight() - 10);
                        monstersRight[i].setX(W_WIDTH + 10);
                    }
                       /* System.out.println("escape");
                        // return to main menu ..
                        MiniGame.score = 0;
                        stage.setScene(introScene);*/

                    System.out.println("level 1");
                    stage.setTitle("Level 1");
                    //LIVES TEST RUN BEGUN
                    // LINE 680
                    MiniGame.lives = 6;
                    MiniGame.score = 0;
                    score.setText("Score: " + MiniGame.score);
                    livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                    MiniGame.enemies = TEMP_ENEMIES;
                    enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                    MiniGame.disableShoot = false;
                    MiniGame.paused = false;

                    MiniGame.monsterLeftIndex = 0;
                    MiniGame.monsterRightIndex = 0;

                    MiniGame.currLevel = 1;
                    MiniGame.monsterSpeed = LEVEL_ONE_MONSTERSPEED;
                    MiniGame.fireLengthR = LEVEL_ONE_FIRELENGTH_R;
                    MiniGame.fireLengthL = LEVEL_ONE_FIRELENGTH_L;

                    stage.setScene(levelOneScene);
                    spawnMonstersTimer.start();

                }
            }
        };

        gameOverScene.addEventHandler(KeyEvent.KEY_PRESSED,escHandler);

        rootLevelOne.getChildren().add(idleImgView);

            EventHandler<KeyEvent> levelOneHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode() == KeyCode.LEFT){
                        System.out.println("LEFT");
                        try {
                            shoot(true,MiniGame.disableShoot);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else if(keyEvent.getCode() == KeyCode.RIGHT){
                        System.out.println("RIGHT");
                        try {
                            shoot(false,MiniGame.disableShoot);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }

                private void shoot(boolean isLeft, boolean disable) throws URISyntaxException {
                    if(disable) return;
                    ImageView fire = new ImageView(new Image("/resources/img/fireball2.gif"));
                    final int[] fireX = {W_WIDTH / 2};

                    if(isLeft) {
                        fire.setScaleX(-1);
                        idleImgView.setScaleX(1);
                        fireX[0] = (W_WIDTH -125) / 2;
                        fire.setX(fireX[0]);
                    } else {
                        idleImgView.setScaleX(-1);
                        fireX[0] = (W_WIDTH +80) / 2;
                        fire.setX(fireX[0]);
                    }


                    fire.setY(W_HEIGHT - 125);

                    rootLevelOne.getChildren().add(fire);

                    URL url = this.getClass().getResource("/resources/sound/firecast.wav");
                    File fireSoundFile = new File(url.toURI());
                    Media fireSoundMedia = new Media(fireSoundFile.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(fireSoundMedia);
                    mediaPlayer.play();

                    AnimationTimer shootLeftTime = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            try {
                                handleAnimation();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }


                        private void handleAnimation() throws URISyntaxException {

                            if(isLeft) {
                                fireX[0] -= 1;
                                if(fire.getX() < MiniGame.fireLengthL){
                                    fire.setY(-800);
                                    MiniGame.score--;
                                    score.setText("Score: " + MiniGame.score);
                                    System.out.println("score  decreasing 320");
                                    this.stop();
                                }
                            }
                            else {
                                if(fire.getX() > MiniGame.fireLengthR){
                                    fire.setY(-800);
                                    MiniGame.score--;
                                    score.setText("Score: " + MiniGame.score);
                                    System.out.println("score  decreasing 960");
                                    this.stop();
                                }
                                fireX[0] += 1;
                            }
                            fire.setX(fireX[0]);
                            if(checkDeath(fire)){
                                // reduce enemies
                                MiniGame.enemies--;
                                enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);
                                MiniGame.score++;
                                score.setText("Score: " + MiniGame.score);
                                System.out.println("DEATH");

                                if(MiniGame.enemies < 1 ){
                                    // Success level up
                                    MiniGame.paused = true;
                                    stop();
                                    MiniGame.disableShoot = true;
                                    MiniGame.currLevel += 1;
                                    System.out.println("CURRENT LEVEL " + MiniGame.currLevel);

                                    URL url = this.getClass().getResource("/resources/sound/won.wav");
                                    File wonSoundFile = new File(url.toURI());
                                    Media wonSoundMedia = new Media(wonSoundFile.toURI().toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(wonSoundMedia);
                                    mediaPlayer.play();

                                    levelOverText.setText("LEVEL "+ (MiniGame.currLevel - 1) +" PASSED\n" +
                                            "PRESS ENTER TO CONTINUE\n" +
                                            "PRESS Q TO QUIT");

                                    if(MiniGame.currLevel < 4) {
                                        levelAdvanceScene.addEventHandler(KeyEvent.KEY_PRESSED, quitHandler);
                                        //levelAdvanceScene.addEventHandler(KeyEvent.KEY_PRESSED, escHandler);
                                        stage.setScene(levelAdvanceScene);
                                    } else {
                                        // Game WIN SCREEN!
                                        gameWinScene.addEventHandler(KeyEvent.KEY_PRESSED, quitHandler);
                                        gameWinScene.addEventHandler(KeyEvent.KEY_PRESSED,escHandler);
                                        gameWinText.setText("WINNER! GAME OVER\nPRESS ENTER TO PLAY AGAIN\n" +
                                                "PRESS Q TO QUIT" +
                                                "\nFINAL SCORE: "+ MiniGame.score);
                                        stage.setScene(gameWinScene);

                                    }

                                }
                            }
                        }
                        private boolean myOverlapCheckFire(ImageView fire, ImageView monster, boolean isLeft){
                            if(fire.getBoundsInLocal().getMinY() < 0 ) return false;
                            if(monster.getBoundsInLocal().getMinY() < 0) return false;
                            if(isLeft){
                                if(fire.getBoundsInLocal().getMinX() < monster.getBoundsInLocal().getMaxX() - 20){

                                    return true;
                                }
                            } else {
                                if(fire.getBoundsInLocal().getMaxX() > monster.getBoundsInLocal().getMinX() + 20) {
                                    return true;
                                }
                            }
                            return false;
                        }
                        private boolean checkDeath(ImageView fire) throws URISyntaxException {
                            for(int i = 0; i < MiniGame.monsterLeftIndex; i++){
                                 // if(fire.getBoundsInLocal().intersects(monstersLeft[i].getBoundsInLocal())){
                                if(myOverlapCheckFire(fire,monstersLeft[i],true)){
                                    System.out.println("trueL");
                                    monstersLeft[i].setY(-400);
                                    fire.setY(-800);
                                    URL url = this.getClass().getResource("/resources/sound/kill_sound.wav");
                                    File killSoundFile = new File(url.toURI());
                                    Media killSoundMedia = new Media(killSoundFile.toURI().toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(killSoundMedia);
                                    mediaPlayer.play();
                                    this.stop();
                                    return true;
                                }
                            }
                            for(int i = 0; i < MiniGame.monsterRightIndex; i++){
                              // if(fire.getBoundsInLocal().intersects(monstersRight[i].getBoundsInLocal())){
                                if(myOverlapCheckFire(fire,monstersRight[i],false)){
                                    monstersRight[i].setY(-400);
                                    fire.setY(-800);
                                    URL url = this.getClass().getResource("/resources/sound/kill_sound.wav");
                                    File killSoundFile = new File(url.toURI());
                                    Media killSoundMedia = new Media(killSoundFile.toURI().toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(killSoundMedia);
                                    mediaPlayer.play();
                                    this.stop();
                                    return true;
                                }
                            }
                            return false;
                        }
                    };
                    shootLeftTime.start();
                }
            };

            // level two handler
        //level 2 handler
        EventHandler<KeyEvent> levelTwoHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                for(int i = 0; i < MiniGame.monsterLeftIndex; ++i ){
                    monstersLeft[i].setY(W_HEIGHT-idle.getHeight() - 10);
                    monstersLeft[i].setX(getMonster().getWidth() * -1 - 10);

                }

                for(int i = 0; i < MiniGame.monsterRightIndex; ++i ){
                    monstersRight[i].setY(W_HEIGHT-idle.getHeight() - 10);
                    monstersRight[i].setX(W_WIDTH + 10);
                }
                if(keyEvent.getCode() == KeyCode.ENTER && MiniGame.currLevel ==2){
                    System.out.println("level 2");
                    stage.setTitle("Level 2");
                    //LIVES TEST RUN BEGUN
                    // LINE 680
                    MiniGame.lives = 6;
                    score.setText("Score: " + MiniGame.score);
                    livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                    MiniGame.enemies = TEMP_ENEMIES;
                    enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                    MiniGame.disableShoot = false;
                    MiniGame.paused = false;

                    MiniGame.monsterLeftIndex = 0;
                    MiniGame.monsterRightIndex = 0;

                    MiniGame.currLevel = 2;
                    MiniGame.monsterSpeed = LEVEL_TWO_MONSTERSPEED;
                    MiniGame.fireLengthR = LEVEL_TWO_FIRELENGTH_R;
                    MiniGame.fireLengthL = LEVEL_TWO_FIRELENGTH_L;

                    stage.setScene(levelOneScene);
                    spawnMonstersTimer.start();
                } else if (keyEvent.getCode() == KeyCode.ENTER && MiniGame.currLevel ==3){
                    // move to level 3
                    System.out.println("Lives "+MiniGame.lives);
                    stage.setTitle("Level 3");
                    MiniGame.lives = 6;
                    score.setText("Score: " + MiniGame.score);
                    livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                    MiniGame.enemies = TEMP_ENEMIES;
                    enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                    MiniGame.disableShoot = false;
                    MiniGame.paused = false;

                    MiniGame.monsterLeftIndex = 0;
                    MiniGame.monsterRightIndex = 0;

                    MiniGame.currLevel = 3;
                    MiniGame.monsterSpeed = LEVEL_THREE_MONSTERSPEED;
                    MiniGame.fireLengthR = LEVEL_THREE_FIRELENGTH_R;
                    MiniGame.fireLengthL = LEVEL_THREE_FIRELENGTH_L;

                    stage.setScene(levelOneScene);
                    spawnMonstersTimer.start();
                }
            }
        };
        levelAdvanceScene.addEventHandler(KeyEvent.KEY_PRESSED,levelTwoHandler);


        levelOneScene.addEventHandler(KeyEvent.KEY_PRESSED,levelOneHandler);
            EventHandler<KeyEvent> introHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    for(int i = 0; i < MiniGame.monsterLeftIndex; ++i ){
                        monstersLeft[i].setY(W_HEIGHT-idle.getHeight() - 10);
                        monstersLeft[i].setX(getMonster().getWidth() * -1 - 10);

                    }

                    for(int i = 0; i < MiniGame.monsterRightIndex; ++i ){
                        monstersRight[i].setY(W_HEIGHT-idle.getHeight() - 10);
                        monstersRight[i].setX(W_WIDTH + 10);
                    }
                    if(keyEvent.getCode() == KeyCode.Q){
                        System.out.println("Thanks for playing");

                        Platform.exit();
                        System.exit(0);
                    }

                    else if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.DIGIT1){
                        // move to level 1
                        System.out.println("Lives "+MiniGame.lives);
                        stage.setTitle("Level 1");
                        MiniGame.lives = 6;
                        MiniGame.score = 0;
                        score.setText("Score: " + 0);
                        livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                        MiniGame.enemies = TEMP_ENEMIES;
                        enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                        MiniGame.disableShoot = false;
                        MiniGame.paused = false;

                        MiniGame.monsterLeftIndex = 0;
                        MiniGame.monsterRightIndex = 0;

                        MiniGame.currLevel = 1;
                        MiniGame.monsterSpeed = LEVEL_ONE_MONSTERSPEED;
                        MiniGame.fireLengthR = LEVEL_ONE_FIRELENGTH_R;
                        MiniGame.fireLengthL = LEVEL_ONE_FIRELENGTH_L;

                        stage.setScene(levelOneScene);
                        spawnMonstersTimer.start();

                    } else if (keyEvent.getCode() == KeyCode.DIGIT2){
                        // move to level 2
                        System.out.println("Lives "+MiniGame.lives);
                        stage.setTitle("Level 2");
                        MiniGame.lives = 6;
                        score.setText("Score: " + MiniGame.score);
                        livesRemaining.setText("Lives remaining: " + MiniGame.lives / 2);
                        MiniGame.enemies = TEMP_ENEMIES;
                        enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                        MiniGame.disableShoot = false;
                        MiniGame.paused = false;

                        MiniGame.monsterLeftIndex = 0;
                        MiniGame.monsterRightIndex = 0;

                        MiniGame.currLevel = 2;
                        MiniGame.monsterSpeed = LEVEL_TWO_MONSTERSPEED;
                        MiniGame.fireLengthR = LEVEL_TWO_FIRELENGTH_R;
                        MiniGame.fireLengthL = LEVEL_TWO_FIRELENGTH_L;

                        stage.setScene(levelOneScene);
                        spawnMonstersTimer.start();

                    } else if (keyEvent.getCode() == KeyCode.DIGIT3){
                        // move to level 3
                        System.out.println("Lives "+MiniGame.lives);
                        stage.setTitle("Level 3");
                        MiniGame.lives = 6;
                        score.setText("Score: " + MiniGame.score);
                        livesRemaining.setText("Lives remaining: " + MiniGame.lives/2);
                        MiniGame.enemies = TEMP_ENEMIES;
                        enemiesRemaining.setText("Enemies remaining: " + MiniGame.enemies);

                        MiniGame.disableShoot = false;
                        MiniGame.paused = false;

                        MiniGame.monsterLeftIndex = 0;
                        MiniGame.monsterRightIndex = 0;

                        MiniGame.currLevel = 3;
                        MiniGame.monsterSpeed = LEVEL_THREE_MONSTERSPEED;
                        MiniGame.fireLengthR = LEVEL_THREE_FIRELENGTH_R;
                        MiniGame.fireLengthL = LEVEL_THREE_FIRELENGTH_L;

                        stage.setScene(levelOneScene);
                        spawnMonstersTimer.start();
                    }
                }
            };

            introScene.addEventHandler(KeyEvent.KEY_PRESSED,introHandler);

            stage.setScene(introScene);
            stage.setTitle("One Finger Death Punch Lite");
            stage.show();


    }


   /* public static void main(String[] args) {
        launch(args);
    } */
}
