package flappy_bird

import WINDOW_HEIGHT
import WINDOW_WIDTH
import twod.Phys2D
import twod.Vec2d
import twod.v
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class City(private val img: BufferedImage, pos: Vec2d = WINDOW_WIDTH v (WINDOW_HEIGHT - img.height)) : Phys2D(pos) {
    override var v: Vec2d = -10 v 0
    override var a: Vec2d = 0 v 0

    public override fun draw(g: Graphics2D) {
        g.drawImage(img, AffineTransformOp(AffineTransform.getScaleInstance(1.0, 1.0), g.renderingHints), xi, yi)
    }

    override fun inBounds(): Boolean {
        return xi + img.width > 0
    }
}