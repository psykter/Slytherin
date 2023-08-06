document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById("gameCanvas");

    if (canvas !== null) {
        const ctx = canvas.getContext("2d");

        function drawGrass() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.strokeStyle = "green";
            ctx.lineWidth = 2;

            for (let i = 0; i < canvas.width; i += 10) {
                for (let j = 0; j < canvas.height; j += 10) {
                    ctx.beginPath();
                    ctx.moveTo(i, j);
                    ctx.lineTo(i + 5, j - 10);
                    ctx.stroke();
                }
            }
        }

        drawGrass();
    }
});