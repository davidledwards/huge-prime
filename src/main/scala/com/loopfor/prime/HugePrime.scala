package com.loopfor.prime

import java.awt.{AlphaComposite, Color, Font}
import java.awt.image.BufferedImage
import java.io.{File, FileReader, Reader}
import javax.imageio.ImageIO
import scala.annotation.tailrec
import scala.collection.immutable.SortedMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object HugePrime {
  private val XSize = 1000
  private val YSize = 1000
  private val PixelSize = 4

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

    // Draw pixels.
    val image = new BufferedImage(XSize * PixelSize, YSize * PixelSize, BufferedImage.TYPE_INT_RGB)
    for (x <- 0 until XSize; y <- 0 until YSize) {
      val color = colorOf(matrix(x)(y))
      val xpos = x * PixelSize
      val ypos = y * PixelSize
      for (xofs <- 0 until PixelSize; yofs <- 0 until PixelSize) image.setRGB(xpos + xofs, ypos + yofs, color)
    }

    // Draw prime number as watermark.
    val g = image.createGraphics
    g.setFont(new Font("Consolas", Font.PLAIN, 108 * PixelSize))
    g.setColor(Color.YELLOW)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.25F))
    val PrimeText = "2^57885161-1"
    val bounds = g.getFontMetrics.getStringBounds(PrimeText, g)
    val x = (XSize * PixelSize - bounds.getWidth) / 2
    val y = YSize * PixelSize / 2
    g.drawString(PrimeText, x.toFloat, y.toFloat)
    image
  }

  private def distribution(matrix: Matrix): Map[Int, Int] = {
    val dist = mutable.Map[Int, Int]()
    for {
      x <- 0 until XSize
      y <- 0 until YSize
      count = matrix(x)(y)
    } if (dist contains count) dist(count) += 1 else dist(count) = 1
    (SortedMap[Int, Int]() /: dist) { case (r, e) => r + e }
  }

  private def generate(ns: Seq[Int]): Matrix = {
    val matrix = Array.ofDim[Int](XSize, YSize)
    @tailrec def generate(ns: Seq[Int]): Matrix = ns take 2 match {
      case Seq(x, y) =>
        matrix(x)(y) += 1
        generate(ns drop 1)
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
