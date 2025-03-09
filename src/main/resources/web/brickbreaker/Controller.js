
export class Controller{

    constructor(gameObject) {
        this.gameObject = gameObject
        this.handler = this.handler.bind(this);
        this.handleKeyDown = this.handleKeyDown.bind(this);
        this.handleKeyUp = this.handleKeyUp.bind(this);

        this.leftKeyUp = true;
        this.rightKeyUp = true;
    }

    replaceGameObjects(gameObjects){
        this.gameObject = gameObjects;
    }

    handler(event){
        event.preventDefault();
        if(event.type === 'keydown'){
            this.handleKeyDown(event, this.gameObject);
        } else if (event.type === 'keyup'){
            this.handleKeyUp(event);
        }
    }

    handleKeyUp(event){
        let paused = this.gameObject.isPaused();
        switch(event.keyCode){
            case 37:
            case 65:
            case 39:
            case 68:
                if(!paused){
                    this.gameObject.paddles.forEach(function(paddle){
                        paddle.deltaX = 0;
                    });
                }
                break;
        }
    }

    handleKeyDown(event){
        let paused = this.gameObject.isPaused();
        switch(event.keyCode){
            case 32: //Spacebar
                if(this.gameObject.playerLives0()){
                    console.log("Get DB") //Ill add this later
                } else {
                    this.gameObject.pause(!paused);
                }
                break;
            case 37:
            case 65:
                if(!paused){
                    this.gameObject.paddles.forEach(function(paddle){
                        paddle.deltaX = -paddle.speed;
                    });
                }
                break;
            case 39:
            case 68:
                if(!paused){
                    this.gameObject.paddles.forEach(function(paddle){
                        paddle.deltaX = paddle.speed;
                    });
                }
                break;
            case 13:
                if(this.gameObject.playerLives0()){
                    this.gameObject.restartGame = true;
                }
                break;
            case 16:
                if(this.gameObject.playerLives0()){
                    this.gameObject.restartLevel = true;
                }
                break;
            default:
                console.log(`Key actually pressed: ${event.keyCode}`);
        }
    }
}