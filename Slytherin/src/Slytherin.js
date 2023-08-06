document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById("gameCanvas");

    if (canvas !== null) {
        const ctx = canvas.getContext("2d");

        const DOT_SIZE = 10;
        const RAND_POS = 29;
        const B_WIDTH = canvas.width;
        const B_HEIGHT = canvas.height;

        let x = new Array(900);
        let y = new Array(900);
        let dots;
        let apple_x;
        let apple_y;

        let paused = false;
        let leftDirection = false;
        let rightDirection = true;
        let upDirection = false;
        let downDirection = false;
        let inGame = true;

        function initGame() {
            dots = 3;

            for (let z = 0; z < dots; z++) {
                x[z] = 50 - z * 10;
                y[z] = 50;
            }

            locateApple();
        }

        function toggleFullscreen(id) {
            var element = document.getElementById(id);
            if (!document.fullscreenElement) {
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.webkitRequestFullscreen) {
                    element.webkitRequestFullscreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.msRequestFullscreen) {
                    element.msRequestFullscreen();
                }
            } else {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.webkitExitFullscreen) {
                    document.webkitExitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
            }
        }


        function locateApple() {
            let r = Math.floor(Math.random() * RAND_POS);
            apple_x = r * DOT_SIZE;
            r = Math.floor(Math.random() * RAND_POS);
            apple_y = r * DOT_SIZE;
        }

        function update() {
            for (let z = dots; z > 0; z--) {
                x[z] = x[z - 1];
                y[z] = y[z - 1];
            }

            if (leftDirection) {
                x[0] -= DOT_SIZE;
            }

            if (rightDirection) {
                x[0] += DOT_SIZE;
            }

            if (upDirection) {
                y[0] -= DOT_SIZE;
            }

            if (downDirection) {
                y[0] += DOT_SIZE;
            }

            checkSnakeFoodCollision();
            checkSnakeWallCollision();
            checkSnakeSelfCollision();
        }

        function checkSnakeFoodCollision() {
            if (x[0] === apple_x && y[0] === apple_y) {
                dots++;
                locateApple();
            }
        }

        function checkSnakeWallCollision() {
            if (x[0] < 0 || x[0] >= B_WIDTH || y[0] < 0 || y[0] >= B_HEIGHT) {
                inGame = false;
            }
        }

        function checkSnakeSelfCollision() {
            for (let z = dots; z > 0; z--) {
                if (z > 3 && x[0] === x[z] && y[0] === y[z]) {
                    inGame = false;
                }
            }
        }

        function render() {

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            if (inGame) {
                ctx.fillStyle = "red";
                ctx.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

                ctx.fillStyle = "#A0522D";
                for (let z = 0; z < dots; z++) {
                    ctx.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
                }
            } else {
                ctx.fillStyle = "white";
                ctx.font = "30px Arial";
                ctx.fillText("Game Over", B_WIDTH / 2 - 70, B_HEIGHT / 2);
            }
        }

        function gameLoop() {
            if (inGame && !paused) {
                update();
                render();
            }

            setTimeout(gameLoop, 100);
        }

        document.addEventListener("keydown", (e) => {
            const key = e.key;

            if (key === "ArrowLeft" && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key === "ArrowRight" && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key === "ArrowUp" && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key === "ArrowDown" && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key === "Enter") {
                paused = !paused;
            }
        });

        initGame();
        gameLoop();
    }

    window.onload = function() {
        var checkBox = document.getElementById("myCheck");

        if (checkBox !== null) {
            checkBox.addEventListener("change", function() {
                var text = document.getElementById("text");
                if (checkBox.checked == true){
                    text.style.display = "none";
                } else {
                    text.style.display = "block";
                }
            });
        }
    }
});