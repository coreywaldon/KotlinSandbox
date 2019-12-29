package flappy_bird

import Game
import WINDOW_HEIGHT
import WINDOW_WIDTH
import twod.Phys2D
import twod.v
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


class FlappyBirdGame : Game() {
    override val title = "Flappy Bird"

    private val pipesIMG: BufferedImage = ImageIO.read(javaClass.getResourceAsStream("resources/pipe.png"))
    private val cityIMG: BufferedImage = ImageIO.read(javaClass.getResourceAsStream("resources/city.png"))

//    val birdIMG = ImageIO.read(File(""))
//    val cloudIMG = ImageIO.read(File(""))

    private var bird = Bird()
    private val pipes = ArrayList<Pipe>()
    private val city = ArrayList<City>()
    private var currentPipe = 0
    private var score = 0

    init {
        addCities()
    }

    override fun keyPressed(e: KeyEvent?) {
        super.keyPressed(e)
        if (e != null) {
            when (e.keyCode) {
                KeyEvent.VK_SPACE -> if (running) bird.jump() else restart()
            }
        }
    }

    private var last = System.currentTimeMillis()
    private var fpsLast = 60L
    override fun update(g: Graphics2D, delta: Float) {
        val current = System.currentTimeMillis()
        val change = current - last
        last = current

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.paint = GradientPaint(0f, 0f, Color.decode("#8563aa"), 0f, WINDOW_HEIGHT.toFloat(), Color.WHITE)
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        val top = Rectangle(0, 0, WINDOW_WIDTH, 25)
        val bot = Rectangle(0, WINDOW_HEIGHT - 25, WINDOW_WIDTH, 25)


        time += (delta * 100).toInt()
        val decay = ArrayList<Phys2D>()
        if (running) {
            addCities()
            if (time == 0 || time > 2000) {
                time = 0
                pipes.add(Pipe((Math.random().toFloat() * WINDOW_HEIGHT / 3) + WINDOW_HEIGHT / 5, pipesIMG))
            }
            if (pipes.size > currentPipe && pipes[currentPipe].x < bird.x) {
                println(++score)
                currentPipe++
            }
            city.reversed().forEach { city ->
                city.update(g, delta)
                if (!city.inBounds()) decay.add(city)
            }
            pipes.forEach { pipe ->
                pipe.update(g, delta)
                if (pipe.collides(bird.collider)) gameOver()
                if (!pipe.inBounds()) decay.add(pipe)
            }
            bird.update(g, delta)
            g.color = Color.BLACK
            g.fill(top)
            g.color = Color.BLACK
            g.fill(bot)
            g.color = Color.WHITE
            if (top.intersects(bird.collider) or bot.intersects(bird.collider)) gameOver()
            drawText(g, "$score", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 4, center = true)
        } else {
            city.reversed().forEach { city ->
                city.draw(g)
            }
            pipes.forEach { pipe ->
                pipe.draw(g)
            }
            bird.draw(g)
            g.color = Color.BLACK
            g.fill(top)
            g.color = Color.BLACK
            g.fill(bot)
            g.color = Color.WHITE
            drawText(g, "GAME OVER", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, center = true)
        }
        decay.forEach {
            pipes.remove(it)
            if (it is Pipe) currentPipe--
        }
        drawText(g, "${(fpsLast + (1000 / change)) / 2} FPS", 0, 0)
        fpsLast = (fpsLast + (1000 / change)) / 2
    }

    private fun drawText(
        g: Graphics2D,
        text: String,
        x: Int,
        y: Int,
        center: Boolean = false
    ) {
        g.font = Font("Flappy Bird Regular", Font.PLAIN, 72)
        val glyphVector = g.font.createGlyphVector(g.fontRenderContext, text)
        // get the shape object
        val textShape = glyphVector.outline
        val stroke = BasicStroke(5f)
        val hints = g.renderingHints
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.color = Color.BLACK
        g.stroke = stroke
        textShape.bounds
        g.translate(
            if (center) x - (textShape.bounds.width / 2) else x,
            if (center) y - (textShape.bounds.height / 2) - textShape.bounds.y else y - textShape.bounds.y
        )
        g.draw(textShape)
        g.color = Color.WHITE
        g.fill(textShape)
        g.setRenderingHints(hints)
        g.translate(
            -(if (center) x - (textShape.bounds.width / 2) else x),
            -(if (center) y - (textShape.bounds.height / 2) - textShape.bounds.y else y - textShape.bounds.y)
        )
    }

    private fun addCities() {
        var start = if (city.size == 0) 0f else (city[city.size - 1].x + cityIMG.width)
        while (start < WINDOW_WIDTH + 1) {
            city.add(City(cityIMG, start v (WINDOW_HEIGHT - cityIMG.height)))
            start += cityIMG.width
        }
    }

    private fun gameOver() {
        println("GAME OVER")
        bird.kill()
        running = false
    }

    private fun restart() {
        currentPipe = 0
        score = 0
        bird = Bird()
        pipes.clear()
        city.clear()
        running = true
    }
}