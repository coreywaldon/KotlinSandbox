package boids

import WINDOW_HEIGHT
import WINDOW_WIDTH
import math.Physics
import math.Vector
import math.random
import math.v
import twod.Circle
import java.awt.*
import java.awt.geom.AffineTransform
import kotlin.math.cos
import kotlin.math.sin

class Boid(
    val getNeighbors: (Rectangle) -> List<Physics>,
    position: Vector = random(WINDOW_WIDTH) v random(WINDOW_HEIGHT),
    var r : Float = random(5, 10),
    val topSpeed: Float = random(5, 5)
) : Physics(
    position = position,
    velocity = random(-5, 5) v random(-5, 5)
) {


    private var searchRadius = random(100, 100).toInt()

    fun draw(g: Graphics2D) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)
        g.color = Color.WHITE
        var poly = Polygon()
        g.fill(Circle(x, y, r))
    }

    fun rule1(neighbors: ArrayList<Physics>): Vector {
        var pos = 0 v 0
        neighbors.forEach { pos += it.pos }
        pos /= neighbors.size
        return (pos - this.pos) / 100
    }

    fun rule2(neighbors: ArrayList<Physics>): Vector {
        var c = 0 v 0
        neighbors.forEach { if (it.distance(this) < r * 2.5) c -= (it.pos) - pos }
        return c / 4
    }

    fun rule3(neighbors: ArrayList<Physics>): Vector {
        var v = 0 v 0
        neighbors.forEach { v += it.v }
        v /= neighbors.size
        return v / 8
    }

    fun rule4(): Vector {
        var c = 0 v 0
        val top = pos.x v 0
        val bottom = pos.x v WINDOW_HEIGHT
        val left = 0 v pos.y
        val right = WINDOW_WIDTH v pos.y
        val edges = arrayListOf(top, bottom, left, right)
        val topleft = 0 v 0
        val topright = WINDOW_WIDTH v 0
        val botleft = 0 v WINDOW_HEIGHT
        val botright = WINDOW_WIDTH v WINDOW_HEIGHT
        val corners = arrayListOf(topleft, topright, botleft, botright)
        var center = WINDOW_WIDTH / 2 v WINDOW_HEIGHT / 2
        edges.addAll(corners)
        edges.forEach {
            if (pos.distance(it) < r)
               c -= (it - pos)
        }

        return c
    }

    fun rule5() : Vector {
        return random(-topSpeed, topSpeed) v random(-topSpeed, topSpeed)
    }

    override fun update(delta: Float) {
        // region Wrap
        if (x > WINDOW_WIDTH) x = 0f
        if (y > WINDOW_HEIGHT) y = 0f
        if (x < 0) x = WINDOW_WIDTH.toFloat()
        if (y < 0) y = WINDOW_HEIGHT.toFloat()
        // endregion

        val neighbors = ArrayList<Physics>()
        for (neighbor in getNeighbors(
            Rectangle(
                xi - searchRadius,
                yi - searchRadius,
                2 * searchRadius,
                2 * searchRadius
            )
        )) {
            if (neighbor.distance(this) < searchRadius && neighbor != this) {
                // In range
                neighbors.add(neighbor)
            }
        }

        if (neighbors.size > 0) {
            val v1 = rule1(neighbors)
            val v2 = rule2(neighbors)
            val v3 = rule3(neighbors)
//            val v4 = rule4()
            val v5 = rule5()

            v += v1 + v2 + v3
            v.clamp(-topSpeed, topSpeed)
//            v += v4
//            v += v5
        }

        super.update(delta)

    }

    val ri: Int
        get() = r.toInt()
}