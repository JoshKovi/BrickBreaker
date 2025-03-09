
export class GameObjects {

    constructor(level = 0, display, player = {lives: 3, score: 0},
                bricksPerRow = 10, fps = 1000 / 60) {
        this.center = display.getCenter();
        let viewerWidth = this.center.x * 2;

        this.restartGame = false;
        this.restartLevel = false;
        this.level = level;
        this.brickCount = (level * bricksPerRow) + bricksPerRow * 2;
        this.fps = fps;
        this.paused = false;
        this.d_paddle = {
            height: 30,
            length: viewerWidth / 8,
            center: {x: this.center.x, y: this.center.y * 2 - 30},
            speed: 650 / fps,
            deltaX: 0
        };
        this.brickGap = 20;
        this.d_brick_width = (viewerWidth - bricksPerRow * this.brickGap) / (bricksPerRow + 2);
        let brickRows = this.brickCount / bricksPerRow
        this.d_brick_height = Math.min((this.center.y - brickRows * this.brickGap) / (brickRows + 1),
            this.d_brick_width * .4);
        this.d_brick = {
            center: {x: 0, y: 0},
            hp: 1
        };

        this.d_ball = {
            radius: 17,
            center: {x: this.center.x, y: this.center.y * 2 - 220},
            linear_speed: 350 / fps,
            deltaX: NaN,
            deltaY: NaN
        };

        this.player = player;
        this.bricksPerRow = bricksPerRow;
        let brickStart = {
            x: this.center.x - (bricksPerRow * this.d_brick_width + bricksPerRow * this.brickGap) / 2,
            y: this.d_brick_height
        }
        this.bricks = this.genBricks(brickStart, this.brickGap);
        this.balls = [structuredClone(this.d_ball)];
        this.paddles = [structuredClone(this.d_paddle)];
    }

    genBricks(brickStart) {
        let halfHeight = this.d_brick_height / 2 + brickStart.y;
        let halfWidth = this.d_brick_width / 2 + brickStart.x;
        let bricks = [];
        for (let i = 0; i < this.brickCount; i++) {
            let brickRow = Math.floor(i / this.bricksPerRow);
            let brickColumn = Math.floor(i % this.bricksPerRow);
            let brick = structuredClone(this.d_brick);
            brick.center = {
                x: halfWidth + (brickColumn * this.d_brick_width) + (brickColumn * this.brickGap),
                y: halfHeight + (brickRow * this.d_brick_height) + (brickRow * this.brickGap) + 80
            }
            brick.hp = Math.floor(Math.random() * this.level + 1);
            bricks.push(brick)
        }
        return bricks;
    }

    addPaddle(screenWidth) {
        let paddle = structuredClone(this.d_paddle);
        paddle.center.x = this.paddles.last().center.x + paddle.length;
        this.paddles.push(paddle);
    }

    addBall() {
        let ball = structuredClone(this.d_ball);
        this.balls.push(ball);
    }

    needsRecenter(newCenter){
        let recenter = !(newCenter.x === this.center.x) || !(newCenter.y === this.center.y);
        let adj = {
            x : this.center.x - newCenter.x,
            y : this.center.y - newCenter.y
        }
        if(recenter){
            this.resizeBricksByCenter(newCenter);
            this.resizePaddlesByCenter(newCenter, adj);
            this.resizeBallsByCenter(newCenter, adj);
        }
        this.adjustBricks(recenter, adj);
    }

    adjustBricks(recenter, adj) {
        for (let i = this.bricks.length - 1; i >= 0; i--) {
            let brick = this.bricks[i];
            if (brick.hp <= 0) {
                this.bricks.splice(i, 1);
                continue;
            }
            if (recenter) {
                brick.center.x -= adj.x;
                brick.center.y -= adj.y;
            }
        }
    }

    resizeBricksByCenter(newCenter) {
        this.center = newCenter;
        let viewerWidth = this.center.x * 2;
        this.d_brick_width = (viewerWidth - this.bricksPerRow * this.brickGap) / (this.bricksPerRow + 2);
        let brickRows = this.brickCount / this.bricksPerRow
        this.d_brick_height = Math.min((this.center.y - brickRows * this.brickGap) / (brickRows + 1),
            this.d_brick_width * .4);
    }

    resizePaddlesByCenter(newCenter, adj){
        let length = newCenter.x / 4;
        let yHeight = newCenter.y * 2 - 30;
        this.paddles.forEach((paddle)=>{
           paddle.length = length;
           paddle.center.x -= adj.x;
           paddle.center.y = yHeight;
        });
    }

    resizeBallsByCenter(newCenter, adj){
        this.balls.forEach((ball) =>{
           ball.center.x -= adj.x;
           ball.center.y -= adj.y;
        });
    }


    isPaused() {
        return this.paused;
    }

    pause(pause) {
        this.paused = pause;
    }

    bricksEmpty() {
        return this.bricks.length === 0;
    }

    ballsEmpty() {
        return this.balls.length === 0;
    }

    playerLives0() {
        return this.player.lives <= 0;
    }

}
