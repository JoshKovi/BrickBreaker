

function mainLoop(engine){
    if(engine.needResize){

    }
    engine.update();
    if(engine.go.bricksEmpty()) engine.levelUp();
    if(engine.go.ballsEmpty() && !engine.go.playerLives0()) engine.newRound();
    if(engine.go.playerLives0()) engine.decideRestart();

    if(engine.firstLoop){
        engine.go.pause(true);
        engine.firstLoop = false;
        engine.needResize = true;
    }

    setTimeout(function(){
        requestAnimationFrame(
            function(){mainLoop(engine)});
    }, engine.frames);

}

import {GameObjects} from "/brickbreaker?GameObjects.js";

export class Engine{
    constructor(display, gameObjects, controller, motion, framesPerSec = Math.floor(1000/30)) {
        this.frames = framesPerSec;
        this.go = gameObjects;
        this.motion = motion;
        this.controller = controller;
        this.firstLoop = true;
        this.needResize = false;

        this.display = display;
        mainLoop(this);
    }

    update(){
        if(this.go.playerLives0()){
            this.updateDeath();
        } else if(this.go.isPaused()){
            this.updatePaused();
        } else {
            this.updateNotPaused();
        }
    }

    updateNotPaused(){
        this.motion.updatePaddles();
        this.motion.updateBalls();
        this.display.drawOrder(this.go);
        this.motion.ballPaddleCollisionCheck();
        this.motion.ballBrickCollision();
        this.motion.ballWallCollision();
    }

    updatePaused(){
        this.display.drawOrder(this.go);
        this.display.drawPaused();
    }

    updateDeath(){
        this.display.drawOrder(this.go);
        this.display.drawDead();
    }

    levelUp(){
        console.log("You beat the level!");
        this.go.player.lives += 1;
        this.go = new GameObjects(this.go.level + 1, this.display, this.go.player)
        this.updateGameObject();

    }

    newRound(){
        this.go.player.lives -= 1;
        this.go.addBall();
        this.go.paddles = [structuredClone(this.go.d_paddle)]
        this.firstLoop = true;
    }

    decideRestart(){
        this.go.pause(true);
        if(this.go.restartGame) this.gameRestart();
        else if(this.go.restartLevel) this.levelRestart();
    }

    levelRestart(){
        this.go = new GameObjects(this.go.level, this.display)
        this.updateGameObject();
    }

    gameRestart(){
        this.go = new GameObjects(0, this.display)
        this.updateGameObject();
    }

    updateGameObject(){
        this.motion.replaceGameObjects(this.go);
        this.controller.replaceGameObjects(this.go);
        this.firstLoop = true;
    }

}



























