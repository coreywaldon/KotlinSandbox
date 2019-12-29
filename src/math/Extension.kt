package math

fun random(min : Number = 0, max : Number) : Float {
    return Math.random().toFloat() * (max.toFloat() - min.toFloat()) + min.toFloat()
}

fun random(max : Number) : Float {
    return Math.random().toFloat() * max.toFloat()
}