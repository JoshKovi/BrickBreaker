


import {Display} from "/brickbreaker?Display.js";
import {GameObjects} from "/brickbreaker?GameObjects.js";
import {Motion} from "/brickbreaker?Motion.js";
import {Controller} from "/brickbreaker?Controller.js";
import {Engine} from "/brickbreaker?Engine.js";

export function initializeGame() {
    const display = new Display();
    const gameObjects = new GameObjects(0, display);
    const motion = new Motion(gameObjects);
    const controller = new Controller(gameObjects);
    window.addEventListener("keydown", controller.handler);
    window.addEventListener("keyup", controller.handler);
    const engine = new Engine(display, gameObjects, controller,  motion);
    const canvasElement = document.getElementById("game-canvas");
}
window.initializeGame = initializeGame;

if(document.readyState === "complete"){
    console.log("Ready State was complete, initializing directly.")
    initializeGame();
} else{
    console.log("Ready State was not complete, adding listener.")
    window.addEventListener('load', initializeGame);
}


