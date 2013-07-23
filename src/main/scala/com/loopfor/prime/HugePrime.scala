package com.loopfor.prime

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, FileReader, Reader}
import javax.imageio.ImageIO
import scala.annotation.tailrec
import scala.collection.immutable.SortedMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object HugePrime {
  private val X_SIZE = 1000
  private val Y_SIZE = 1000
  type Matrix = Array[Array[Int]]

  def main(args: Array[String]): Unit = {
    println("parsing input")
    val in = new FileReader("huge-prime.txt")
    val ns = try parse(in) finally in.close()

    println("generating matrix")
    val matrix = generate(ns)

    println("calculating distribution")
    val dist = distribution(matrix)
    dist foreach { case (k, v) => println(s"$k = $v")}

    println("creating image")
    val image = plot(matrix)
    ImageIO.write(image, "PNG", new File("huge-prime.png"))
  }

  private def plot(matrix: Matrix): BufferedImage = {
    def colorOf(count: Int): Int = {
      if (count > 0) {
        val c = 64 + (count * 12)
        (c << 16) | (c << 8) | c
      } else
        Color.BLACK.getRGB
    }
    val image = new BufferedImage(X_SIZE, Y_SIZE, BufferedImage.TYPE_INT_RGB)
    for {
      x <- 0 until X_SIZE
      y <- 0 until Y_SIZE
    } image.setRGB(x, y, colorOf(matrix(x)(y)))
    image
  }

  private def distribution(matrix: Matrix): Map[Int, Int] = {
    val dist = mutable.Map[Int, Int]()
    for {
      x <- 0 until X_SIZE
      y <- 0 until Y_SIZE
      count = matrix(x)(y)
    } if (dist contains count) dist(count) += 1 else dist(count) = 1
    (SortedMap[Int, Int]() /: dist) { case (r, e) => r + e }
  }

  private def generate(ns: Seq[Int]): Matrix = {
    val matrix = Array.ofDim[Int](X_SIZE, Y_SIZE)
    @tailrec def generate(ns: Seq[Int]): Matrix = ns take 2 match {
      case Seq(x, y) =>
        matrix(x)(y) += 1
        generate(ns drop 2)
      case _ => matrix
    }
    generate(ns)
  }

  private def parse(in: Reader): Seq[Int] = {
    @tailrec def parse(buf: String, ns: ListBuffer[Int]): Seq[Int] = in.read() match {
      case '\n' =>
        if (buf.length == 0) parse(buf, ns)
        else parse("", ns += buf.toInt)
      case -1 =>
        if (buf.length > 0) ns += buf.toInt
        ns.toSeq
      case c =>
        val ch = c.toChar
        if (buf.length < 3) parse(buf + ch, ns)
        else parse(ch.toString, ns += buf.toInt)
    }
    parse("", ListBuffer())
  }
}
