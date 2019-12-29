package twod

import java.awt.Shape
import java.awt.geom.Ellipse2D

fun Circle(x: Number, y: Number, r: Number) : Shape {
    return Ellipse2D.Float(x.toFloat(), y.toFloat(), r.toFloat()*2, r.toFloat()*2)
}