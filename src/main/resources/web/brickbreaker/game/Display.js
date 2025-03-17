
export class Display{
    constructor() {
        this.canvas = document.getElementById("game-canvas");
        this.ctx = this.canvas.getContext('2d');
        this.colors = {
            "AQUA" : "rgba(12,232,232,.52)",
            "GREEN" : "rgba(30, 250, 30, .52)"
        }
        this.brickColors = {
            1 : "rgba(244,168,35,.95)",
            2 : "rgba(238,102,4,0.95)",
            3 : "rgba(173,29,16,0.95)",
            4 : "rgba(134,13,2,0.95)",
            5 : "rgba(238,4,137,0.95)",
            6 : "rgba(4,16,238,0.95)"
        }
    }

    getCenter(resize = true){
        if(resize) this.resize();
        return { x : this.canvas.width / 2, y : this.canvas.height/ 2};
    }

    drawOrder(gameObject){
        this.resize();
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.background();
        gameObject.needsRecenter(this.getCenter(false));
        this.drawBricks(gameObject);
        this.drawPaddles(gameObject);
        this.drawBalls(gameObject);
        this.drawLives(gameObject.player.lives);
        this.drawScore(gameObject.player.score);
        if(this.canvas.width < 200 || this.canvas.height < 100){
            gameObject.player.lives = 0;
        }
    }

    resize(){
        const parent = this.canvas.parentElement;
        this.canvas.width = parent.clientWidth * .8;
        this.canvas.height = this.canvas.width * .5;
    }

    background(){
        this.ctx.beginPath();
        this.ctx.rect(0,0, this.canvas.width, this.canvas.height);
        this.ctx.fillStyle = this.colors.AQUA;
        this.ctx.fill();
        this.ctx.closePath();
    }

    drawBricks(gameObject){
        let halfHeight = gameObject.d_brick_height / 2;
        let halfWidth = gameObject.d_brick_width / 2;
        for(let i = 0; i < gameObject.bricks.length; i++){
            let brick = gameObject.bricks[i];
            this.ctx.beginPath();
            this.ctx.rect(
              brick.center.x - halfWidth,
              brick.center.y - halfHeight,
              gameObject.d_brick_width,
              gameObject.d_brick_height
            );
            this.ctx.fillStyle = this.brickColors[brick.hp];
            this.ctx.fill();
            this.ctx.closePath();
        }
    }

    drawPaddles(gameObject){
        for(let i = 0; i < gameObject.paddles.length; i++){
            let paddle = gameObject.paddles[i];
            this.ctx.beginPath();
            this.ctx.rect(
                paddle.center.x - paddle.length / 2,
                paddle.center.y - paddle.height / 2,
                paddle.length,
                paddle.height
            );
            this.ctx.fillStyle = this.colors.GREEN;
            this.ctx.fill();
            this.ctx.closePath();
        }
    }

    drawBalls(gameObject){
        for(let i = 0; i < gameObject.balls.length; i++){
            let ball = gameObject.balls[i];
            this.ctx.beginPath();
            this.ctx.arc(ball.center.x, ball.center.y, ball.radius, 0, Math.PI * 2)
            this.ctx.fillStyle = this.brickColors["6"];
            this.ctx.fill();
            this.ctx.closePath();
        }
    }

    drawLives(lives){
        for(let i = 0; i < lives; i++){
            let bottomX = 40 + (45*i);
            let topLeft = bottomX - 15;
            let topRight = bottomX + 15;

            this.ctx.beginPath();
            this.ctx.lineWidth = 3;
            this.ctx.strokeStyle = 'black';
            this.ctx.lineJoin = "round";
            this.ctx.fillStyle = "red";
            this.ctx.moveTo(bottomX, 40);
            this.ctx.lineTo(topLeft, 25);
            this.ctx.arc(topLeft + 5, 20, 8.5, Math.PI, 0);
            this.ctx.arc(topRight - 5, 20, 8.5, Math.PI, 0);
            this.ctx.lineTo(bottomX, 40);
            this.ctx.stroke();
            this.ctx.fill();
        }
    }

    drawScore(score){
        this.ctx.font = '20px Arial';
        this.ctx.fillStyle = 'black';
        this.ctx.textAlign = "center";
        this.ctx.fillText("Score: " + score,  Math.floor(this.canvas.width * .7), 40);
    }

    drawPaused(){
        this.ctx.font = '40px Arial';
        this.ctx.fillStyle = 'black';
        this.ctx.textAlign = "center";
        let center = this.getCenter(false);
        this.ctx.fillText("Paused (Press {Space} to resume)", center.x, center.y);
    }

    drawDead(){
        this.ctx.font = '40px Arial';
        this.ctx.fillStyle = 'black';
        this.ctx.textAlign = "center";
        let center = this.getCenter(false);
        this.ctx.fillText("You died!", center.x, center.y);
        this.ctx.fillText("Restart level with {Shift}", center.x, center.y + 40);
        this.ctx.fillText("Restart game with {Enter}", center.x, center.y + 80);
        this.ctx.fillText("See leader board with {Space}", center.x, center.y + 120);

    }

}