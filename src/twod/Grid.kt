package twod

import a_star.SIZE
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

open class GridSquare(val x: Int,
                      val y: Int,
                      val size : Int = SIZE,
                      var color : Color = Color.BLACK,
                      var stroke : Color = Color.WHITE) {

    fun draw(g: Graphics2D) {
        if (color != Color.BLACK) {
            g.color = color
            g.fill(Circle(x * size, y * size, size / 5))
        }
    }

    override fun toString(): String {
        return "$x, $y"
    }
}