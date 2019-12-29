package boids

import Game
import WINDOW_HEIGHT
import WINDOW_WIDTH
import math.Physics
import math.Vector
import math.v
import java.awt.*
import java.awt.event.MouseEvent
import java.util.*
import kotlin.collections.ArrayList

class Boids : Game() {
    override val title = "Boids"

    val boids: MutableList<Boid> = (0..99).map { Boid(getNeighbors) }.toMutableList()
    var rootQuad: Quad

    init {
        val root = Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        rootQuad = Quad(root)
        buildQuadtree(rootQuad, root)
        boids.forEach { rootQuad.insert(it) }
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e != null) {
            boids.add(Boid(getNeighbors, position = e.x v e.y))
        }
    }

    override fun mouseDragged(e: MouseEvent?) {
        mouseClicked(e)
    }

    private val getNeighbors: (Rectangle) -> List<Physics>
        get() = {
            this.rootQuad.query(it)
        }

    private fun buildQuadtree(root: Quad, space: Rectangle, limitX : Int = WINDOW_WIDTH, limitY : Int = WINDOW_HEIGHT) {
        if (space.width < limitX || space.height < limitY) return
        val quads = (0..3).map {
            createQuad(space, it)
        }
        quads.forEachIndexed { i, quad ->
            root.children[i] = quad
            buildQuadtree(quad, quad.rectangle)
        }
    }

    override fun update(g: Graphics2D, delta: Float) {
        val root = Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        rootQuad = Quad(root)
        buildQuadtree(rootQuad, root)
        boids.forEach { rootQuad.insert(it) }
        g.color = Color.BLACK
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        rootQuad.draw(g)
        boids.forEach {
            it.update(delta)
            it.draw(g)
        }
    }
}

class Quad(val rectangle: Rectangle) {
    val children = arrayListOf<Quad?>(null, null, null, null)
    val nodes = arrayListOf<Physics>()

    fun query(rect: Rectangle) : List<Physics> {
        val nodes = ArrayList<Physics>()
        if (rect.intersects(rectangle)) {
            nodes.addAll(this.nodes)
            children.forEach { if (it != null) nodes += it.query(rect) }
        }
        return nodes
    }

    fun insert(node: Physics) {
        if (inBounds(node)) {
            val quadrant = getQuad(node)
            var quad = children[quadrant]
            if (quad == null) {
                quad = createQuad(rectangle, quadrant)
                children[quadrant] = quad
            }
            if (rectangle.width > WINDOW_WIDTH / 32 && rectangle.height > WINDOW_HEIGHT / 32) {
                quad.insert(node)
            } else quad.nodes.add(node)

        }
    }

    fun getQuad(node: Physics) : Int {
        if (node.x <= rectangle.maxX - (rectangle.width / 2)) {
            if (node.y <= rectangle.maxY - (rectangle.height / 2))
                return 0
            return 2
        }
        if (node.y <= rectangle.maxY - (rectangle.height / 2)) return 1
        return 3
    }

    private fun inBounds(node: Physics) : Boolean {
        return node.x < rectangle.maxX && node.x > rectangle.x && node.y < rectangle.maxY && node.y > rectangle.y
    }

    fun draw(g: Graphics2D) {
        g.color = Color.WHITE
        g.draw(rectangle)
        children.forEach {it?.draw(g)}
//        nodes.forEach { g.drawLine(rectangle.centerX.toInt(), rectangle.centerY.toInt(), it.xi, it.yi) }
    }
}

fun createQuad(space: Rectangle, quadrant: Int) : Quad {

    /*
        | 0 | 1 |
        | 2 | 3 |
     */
    return Quad(
        when (quadrant) {
            0 -> Rectangle(
                space.x,
                space.y,
                (space.width / 2),
                (space.height / 2)
            )
            1 -> Rectangle(
                space.x + (space.width / 2),
                space.y,
                (space.width / 2),
                (space.height / 2)
            )
            2 -> Rectangle(
                space.x,
                space.y + (space.height / 2),
                (space.width / 2),
                (space.height / 2)
            )
            3 -> Rectangle(
                space.x + (space.width / 2),
                space.y + (space.height / 2),
                (space.width / 2),
                (space.height / 2)
            )
            else -> Rectangle(space.x, space.y, space.width, space.height)
        }
    )
}