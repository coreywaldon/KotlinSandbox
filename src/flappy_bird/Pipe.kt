package flappy_bird

import WINDOW_HEIGHT
import WINDOW_WIDTH
import twod.Phys2D
import twod.Vec2d
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class Pipe(y: Float, private val img: BufferedImage, pos: Vec2d = Vec2d(WINDOW_WIDTH, y)) : Phys2D(pos) {

    override fun inBounds(): Boolean {
        return xi + 100 > 0
    }

    override var v: Vec2d = Vec2d(-15, 0)
    override var a: Vec2d = Vec2d(0, 0)

    public override fun draw(g: Graphics2D) {
        g.drawImage(img, AffineTransformOp(AffineTransform.getScaleInstance(1.0, -1.0), g.renderingHints), xi, yi)
        g.drawImage(img, AffineTransformOp(AffineTransform.getScaleInstance(1.0, 1.0), g.renderingHints), xi, yi + 150)
    }

    fun collides(other: Rectangle) : Boolean {
        val top = Rectangle(xi,  0, 100, yi)
        val bottom = Rectangle(xi, yi + 150, 100, WINDOW_HEIGHT - yi -150)
        return top.intersects(other) or bottom.intersects(other)
    }
}