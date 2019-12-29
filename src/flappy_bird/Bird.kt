package flappy_bird

import WINDOW_HEIGHT
import WINDOW_WIDTH
import twod.Circle
import twod.Phys2D
import twod.Vec2d
import twod.v
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

class Bird(pos: Vec2d = (WINDOW_WIDTH / 2) v (WINDOW_HEIGHT / 2)) : Phys2D(pos) {
    override fun inBounds(): Boolean {
        return true
    }

    override var v = Vec2d(0, 0)
    override var a = Vec2d(0, 9)
    override val r = 20.0f

    private var alive = true

    val collider : Rectangle
        get() =
            Rectangle(xi, yi, ri * 2, ri * 2)

    override fun update(g: Graphics2D, delta: Float) {
        g.color = Color.BLACK
        if (alive) super.update(g, delta)
        else draw(g)
    }

    public override fun draw(g: Graphics2D) {
        super.draw(g)
        g.color = Color.BLACK
        g.draw(Circle(x, y, r))
    }

    fun jump() {
        v *= 0
        v += 0 v -36
    }

    fun kill() {
        alive = false
    }
}