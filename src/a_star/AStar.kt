package a_star

import Game
import WINDOW_HEIGHT
import WINDOW_WIDTH
import twod.GridSquare
import twod.Vec2d
import twod.v
import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

const val SIZE = 10
const val ROWS = WINDOW_WIDTH / SIZE
const val COLS = WINDOW_HEIGHT / SIZE
class AStar : Game() {
    override val title = "A Star Simulation"

    private var board = ArrayList<ArrayList<Square>>()
    private var start : Pair<Square, Vec2d>
    private var end   : Pair<Square, Vec2d>
    private var current : Square
    private var path = PriorityQueue<Square>()
    private var currentPath = 0f

    var highlight = false

    init {
        for (i in 0 until (ROWS)) {
            board.add(ArrayList())
            for (j in 0 until COLS) board[board.size - 1].add(Square(i, j, ""))
        }
        start = board[0][0] to (0 v 0)
        end   = board[ROWS - 1][COLS - 1] to (ROWS - 1 v COLS - 1)
        start.first.color = Color.GREEN
        end.first.color   = Color.RED
        current = start.first
        current.g = 0f
        path.add(current)
        running = false
    }

    private lateinit var mouse: Vec2d

    override fun keyPressed(e: KeyEvent?) {
        super.keyPressed(e)
        if (e?.keyCode == KeyEvent.VK_SPACE) {
            running = true
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        super.mouseMoved(e)
        if (e != null) mouse = e.locationOnScreen.x v e.locationOnScreen.y
    }

    override fun mouseDragged(e: MouseEvent?) {
        super.mouseDragged(e)
        if (e != null) mouse = e.locationOnScreen.x v e.locationOnScreen.y
        fill(mouse)
    }

    override fun mouseClicked(e: MouseEvent?) {
        super.mouseClicked(e)
        if (!running) {
            update(highlight)
            highlight = !highlight
        }
    }

    private fun reset() {
        running = false
        board.clear()
        for (i in 0 until (ROWS)) {
            board.add(ArrayList())
            for (j in 0 until COLS) board[board.size - 1].add(Square(i, j, ""))
        }
        start = board[0][0] to (0 v 0)
        end   = board[ROWS - 1][COLS - 1] to (ROWS - 1 v COLS - 1)
        start.first.color = Color.GREEN
        end.first.color   = Color.RED
        current = start.first
        current.g = 0f
        path.clear()
        path.add(current)
        running = false
    }

    private fun update(highlight: Boolean) {
        val position = getGridPosition(mouse)
        val square = board[position.xi][position.yi]
        val old = if (highlight) start.first else end.first
        if (!highlight) end = square to position else start = square to position
        if (highlight) {
            path.clear()
            path.add(start.first)
            current.g = Float.MAX_VALUE
            current = start.first
            current.g = 0f
        }
        old.color = Color.BLACK
        square.color = if (highlight) Color.GREEN else Color.RED
    }

    private fun fill(mouse: Vec2d) {
        val position = getGridPosition(mouse)
        val square = board[position.xi][position.yi]
        if (square != start.first && square != end.first) {
            square.color = Color.BLUE
        }
    }

    private fun getAdjacent(location: Vec2d) : List<Square> {
        return getAdjacent(board[location.xi][location.yi])
    }

    private fun getAdjacent(square: Square) : List<Square> {
        return listOf(0 v 1, 1 v 0, 1 v 1, 0 v -1, -1 v 0, -1 v -1, 1 v -1, -1 v 1).filter { m ->
            square.x + m.x < ROWS && square.x + m.x >= 0 && square.y + m.y < COLS && square.y + m.y >= 0 && board[square.x + m.xi][square.y + m.yi].color != Color.BLUE && !board[square.x + m.xi][square.y + m.yi].visited
        }.map{ m ->
            val adjacent = board[square.x + m.xi][square.y + m.yi]
            if (adjacent.g > square.g + dist(square, adjacent))
                adjacent.prev = square
            adjacent.g = min(square.g + dist(square, adjacent), adjacent.g)
            adjacent.h = min(adjacent.h, dist(adjacent, end.first))
            adjacent.color = Color.GREEN
            adjacent
        }
    }

    fun dist(a: Square, b: Vec2d) : Float {
        return sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2))
    }

    fun dist(a: Square, b: Square) : Float {
        return sqrt((a.x - b.x).toFloat().pow(2) + (a.y - b.y).toFloat().pow(2))
    }

    private fun getGridPosition(loc: Vec2d) : Vec2d {
        val x = floor(loc.x / SIZE)
        val y = floor(loc.y / SIZE)
        return x v y
    }

    var foundEnd = false

    override fun update(g: Graphics2D, delta: Float) {
        g.color = Color.BLACK
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        for (i in 0 until board.size) for (j in 0 until board[i].size) {
            board[i][j].draw(g)
        }
        if (!foundEnd && running && current != end.first && path.size != 0) {
            currentPath += dist(path.peek(), current)
            current = path.poll()
            current.visit()
            if (current == end.first) {
                foundEnd = true
                current.color = Color.WHITE
            } else {
                val next = getAdjacent(current)
                path.addAll(next.filter { !path.contains(it) })
            }
        } else if (foundEnd && current != start.first) {
            // TODO: Find optimal path
            if (current.prev != null)
                current = current.prev!!
            current.color = Color.WHITE
        } else if (foundEnd) {
            foundEnd = false
            reset()
        }
    }
}

class Square(x: Int, y: Int, type: String) : GridSquare(x, y, color = when(type) {
    "Start", "Path" -> Color.GREEN
    "End" -> Color.RED
    "Wall" -> Color.BLUE
    "Visited" -> Color.ORANGE
    else -> Color.BLACK
}), Comparable<Square> {
    var visited = false
    var g = Float.MAX_VALUE
    var h = Float.MAX_VALUE
    var prev : Square? = null


    override fun compareTo(other: Square): Int {
        return f.compareTo(other.f)
    }

    val f : Float
        get() { return g + h }

    fun visit() {
        visited = true
        color = Color.ORANGE
    }
}