package twod

import java.awt.Color
import java.awt.Graphics2D

abstract class Phys2D(private var pos: Vec2d) {
    abstract var v: Vec2d
    abstract var a: Vec2d

    open val r: Float = 0.5f
    open val c: Color = Color.WHITE

    open fun update(g: Graphics2D, delta: Float) {
        draw(g)
        pos += v * delta
        v += a * delta
    }

    protected open fun draw(g: Graphics2D) {
        g.color = c
        g.fill(Circle(x, y, r))
    }

    abstract fun inBounds() : Boolean

    val x : Float
        get() = pos.x

    val y : Float
        get() = pos.y

    val xi : Int
        get() = pos.xi

    val yi : Int
        get() = pos.yi

    val ri : Int
        get() = r.toInt()
}