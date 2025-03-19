
export class Motion{

    constructor(gameObjects) {
        this.go = gameObjects;
    }

    replaceGameObjects(gameObjects){
        this.go = gameObjects;
    }

    updateBalls(){
        for(let i = 0; i < this.go.balls.length; i++){
            let ball = this.go.balls[i];
            if(Number.isNaN(ball.deltaX) || Number.isNaN(ball.deltaY)){
                ball.deltaX = -ball.linear_speed * (Math.random() - .5);
                ball.deltaY = ball.linear_speed * (Math.random() + .85);
            }
            ball.deltaX = Math.min(ball.linear_speed, ball.deltaX);
            ball.deltaY = Math.min(ball.linear_speed, ball.deltaY);
            ball.center.x += ball.deltaX;
            ball.center.y += ball.deltaY;
        }
    }

    updatePaddles(){
        for(let i = 0; i < this.go.paddles.length; i++){
            let paddle = this.go.paddles[i];
            if((paddle.center.x + paddle.length * .5 + paddle.deltaX > this.go.center.x * 2) ||
                (paddle.center.x - paddle.length * .5 + paddle.deltaX < 0)) {
                paddle.deltaX = 0;
            }
            this.go.paddles[i].center.x += this.go.paddles[i].deltaX;

        }
    }

    ballPaddleCollisionCheck(){
        for(let i = 0; i < this.go.balls.length; i++){
            let ball = this.go.balls[i];
            this.go.paddles.forEach(function(paddle){
                let paddleHalfHeight = (paddle.height * .5);
                let paddleHalfWidth = (paddle.length * .5);
                // Narrows the range to check for collision, excludes area above paddle, left, right, then finally under;
                if((ball.center.y + ball.radius + ball.deltaY) <= paddle.center.y - paddleHalfHeight) return;
                if((ball.center.x + ball.radius + ball.deltaX) <= paddle.center.x - paddleHalfWidth) return;
                if((ball.center.x - ball.radius + ball.deltaX) >= paddle.center.x + paddleHalfWidth) return;
                if((ball.center.y - ball.radius + ball.deltaX) > paddle.center.y + paddleHalfHeight) return;
                ball.deltaX = Math.min(ball.linear_speed, (paddle.deltaX * (Math.random() * 1.25)))
                let tempYMod = ball.center.y - (paddle.center.y - paddleHalfHeight);
                ball.deltaY *= (tempYMod === 0 || tempYMod === -ball.radius) ? -1 : tempYMod / ball.radius;
                if(Math.abs(ball.deltaY) <= ball.linear_speed / 10){
                    ball.deltaY -= ball.linear_speed /10;
                }

            });
        }
    }

    ballBrickCollision(){
        let brickHalfHeight = this.go.d_brick_height / 2;
        let brickHalfWidth = this.go.d_brick_width / 2;
        let balls = this.go.balls;
        let bricks = this.go.bricks;
        for(let i = 0; i < balls.length; i++){
            let ball = balls[i];
            let ballTop = ball.center.y - ball.radius + ball.deltaY;
            let ballBottom = ball.center.y + ball.radius + ball.deltaY;
            let ballLeft = ball.center.x - ball.radius + ball.deltaX;
            let ballRight = ball.center.x + ball.radius + ball.deltaX;

            for(let j = 0; j < this.go.bricks.length; j++){
                let brick = bricks[j];
                if(ballTop > brick.center.y + brickHalfHeight) continue;
                if(ballLeft > brick.center.x + brickHalfWidth) continue;
                if(ballRight < brick.center.x - brickHalfWidth) continue;
                if(ballBottom < brick.center.y - brickHalfHeight) continue;
                //Ball appears to have hit this brick!
                brick.hp -= 1;
                this.go.player.score += 10;
                if(ball.center.y > brick.center.y - brickHalfHeight && ball.center.y < brick.center.y + brickHalfHeight) {
                    ball.deltaX *= -1;
                }
                else if((ballTop > brick.center.y) || (ballBottom < brick.center.y)){
                    ball.deltaY *= -1;
                } else {
                    ball.deltaX *= -1;
                    ball.deltaY *= -1;
                }
            }
        }
    }

    ballWallCollision(){
        for(let i = this.go.balls.length -1; i >= 0; i--){
            let ball = this.go.balls[i];
            let ballX = ball.center.x + ball.deltaX;
            let ballY = ball.center.y + ball.deltaY;
            if(ballY + ball.radius >= this.go.center.y * 2) {
                this.go.balls.splice(i, 1);
            }
            if(ballY - ball.radius <= 0) ball.deltaY *= -1;
            if(ballX - ball.radius <= 0 || ballX + ball.radius >= this.go.center.x * 2 -1) ball.deltaX *= -1;
        }
    }

}