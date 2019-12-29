import a_star.AStar
import boids.Boid
import boids.Boids
import flappy_bird.FlappyBirdGame
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel


// Constants
const val WINDOW_WIDTH  = 1000
const val WINDOW_HEIGHT = 1000
const val FPS           = 60

lateinit var frame: JFrame

fun main() {
    Sandbox()
}

class Sandbox {
    init {
        // Game
//        val game: Game? = FlappyBirdGame()
//        val game: Game?  = AStar()
        val game: Game? = Boids()

        // JFrame setup
        frame = JFrame(game?.title ?: "Default")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.addKeyListener(game)
        frame.addMouseListener(game)
        frame.addMouseMotionListener(game)
        frame.add(Screen(game))
        frame.size = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        frame.isUndecorated = true
        frame.isVisible = true
    }
}

class Screen(private val game: Game?) : JPanel() {

    private val timer = Timer()

    init {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                repaint()
            }
        }, 0, (1000 / FPS).toLong())
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        game?.update(g as Graphics2D, (1000f / FPS) / 100)
    }
}

abstract class Game : KeyListener, MouseMotionListener, MouseListener {
    abstract val title : String
    var time : Int = 0
    var running = true

    override fun mouseMoved(e: MouseEvent?) {}
    override fun mouseDragged(e: MouseEvent?) {}
    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mousePressed(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun keyTyped(e: KeyEvent?) {}
    override fun keyPressed(e: KeyEvent?) {}
    override fun keyReleased(e: KeyEvent?) {}
    abstract fun update(g: Graphics2D, delta: Float)
}